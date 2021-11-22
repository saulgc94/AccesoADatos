package jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetaDatos {
	
	public static void muestraErrorSQL(SQLException e) {
		System.err.println("SQL ERROR mensaje: " + e.getMessage());
		System.err.println("SQL Estado: " + e.getSQLState());
		System.err.println("SQL código específico: " + e.getErrorCode());
	}

	public static void main(String[] args) {

		String basedatos = "starwars";
		String host = "localhost";
		String port = "3306";
		String parAdic = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos + parAdic;
		String user = "saul";
		String pwd = "1234";
		
		//Realizamos la conexión a la base de datos y creamos la interfaz DatabaseMetaData que le pasaremos a la función
		try (Connection c = DriverManager.getConnection(urlConnection, user, pwd)) {
			DatabaseMetaData meta = c.getMetaData();
			
			infoTablas(meta,basedatos);

		} catch (SQLException e) {
			muestraErrorSQL(e);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	
	
	
	public static void infoTablas(DatabaseMetaData meta, String basedatos) {
		try {
			//Recuperamos los datos de las tablas
			ResultSet rs = meta.getTables(basedatos,null, null, null);
			
			while(rs.next()) {
				//Recuperamos el nombre de la tabla y lo mostramos
				String TABLA = rs.getString("TABLE_NAME");
				System.out.println("Tabla: "+TABLA);

				
				//Recuperamos los datos de primary keys y las mostramos
				ResultSet rs2 = meta.getPrimaryKeys(basedatos,null, TABLA);
				while(rs2.next()) {
				System.out.println("	Clave primaria: " +rs2.getString("COLUMN_NAME"));
				
				}
				
				//Recuperamos los datos de las keys importadas y mostamos la clave aliena, de la tabla de que procede y su clave primaria
				rs2 = meta.getImportedKeys(basedatos, null, TABLA);
				while(rs2.next()) {
					System.out.println("	Clave aliena: "+ rs2.getString("FKCOLUMN_NAME")+ " de la tabla: "+ rs2.getString("PKTABLE_NAME")+" con la clave primaria: "+rs2.getString("PKCOLUMN_NAME"));

					

				}
				//Recuperamos los datos de las columnas y mostramos algunos de los datos que nos proporcionan
				System.out.println("	Columnas: ");
				rs2= meta.getColumns(basedatos, null, TABLA, null);
				while(rs2.next()) {
					System.out.println("		"+rs2.getString("COLUMN_NAME")+" - "+rs2.getString("TYPE_NAME")+"("+rs2.getString("COLUMN_SIZE")+")"+" - nulable? "+rs2.getString("IS_NULLABLE"));
				}
				System.out.println("");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
