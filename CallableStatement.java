package jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CallableStatement {
	
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
		
		
		//Conexión con la base de datos
		try (Connection c = DriverManager.getConnection(urlConnection, user, pwd)) {
			Ej1(c);
			Ej2(c);
			Ej3(c);
			
			
		} catch (SQLException e) {
			muestraErrorSQL(e);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	public static void Ej1(Connection c) {
		//Inicializo las variables de la llamada de la función y el valor de entrada
		String sql;
		int id;
		sql="{call Ej1(?,?,?)}";
		id=3;
		
		//Ejecuto el callableStatement asignando el valor y los tipos de output
		try {
			java.sql.CallableStatement cs = c.prepareCall(sql);
			cs.setInt(1, id);
			cs.registerOutParameter(2, java.sql.Types.VARCHAR);
			cs.registerOutParameter(3, java.sql.Types.INTEGER);
			
			cs.execute();
		//Muestro los resultados	
			String affi = cs.getString(2);
			System.out.println("=> Name: "+affi);
			int count = cs.getInt(3);
			System.out.println("=> Count: "+count);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static void Ej2(Connection c) {
		//Inicializo las variables de la llamada de la función y el valor de entrada
		String sql;
		int id;
		sql="{call Ej2(?)}";
		id=3;
		//Ejecuto el callableStatement asignando el valor y recojo los datos de la select con el resultset
		try {
			java.sql.CallableStatement cs = c.prepareCall(sql);
			cs.setInt(1, id);
			
			cs.execute();
			ResultSet rs = cs.getResultSet();
			
			//Muestro los datos de la select
			while(rs.next()) {
				
				System.out.println("["+rs.getInt(1)+"]");
				System.out.println("Affiliation Name: "+rs.getString(2));
				System.out.println("Members: "+rs.getInt(3));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	
	public static void Ej3(Connection c) {
		//Inicializo las variables de la llamada de la función y el valor de entrada
		String sql,pj;
		int idFilm;
		sql="{?=call Ej3(?,?)}";
		idFilm=4;
		pj="Darth Vader";
		
		//Ejecuto el callablestatement con los valores de los interrogantes asignados 
		try {
			java.sql.CallableStatement cs = c.prepareCall(sql);
			cs.setInt(2, idFilm);
			cs.setString(3, pj);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.execute();
			
			//Muestro el resultado
			System.out.println("Muertos: "+cs.getInt(1));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
