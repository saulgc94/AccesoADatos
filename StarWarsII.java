package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StarWarsII {
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

		try (Connection c = DriverManager.getConnection(urlConnection, user, pwd)) {
			
			//Ejercicio 1
			infoPlanetas(c);
			
			//Ejercicio 2
			insertPjs(c);
			
			//Ejercicio 3
			muertes(c);

		} catch (SQLException e) {
			muestraErrorSQL(e);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public static void infoPlanetas(Connection c) {
		@SuppressWarnings("resource")
		Scanner teclado = new Scanner(System.in);
		String[] infoPlanet = new String[] { "id", "name", "diameter" };
		int a, b, aux;

		for (int j = 0; j < 3; j++) {
			System.out.println("Introduzca el primer parámetro del diamentro");
			a = teclado.nextInt();

			System.out.println("Introduzca el segundo parámetro del diamentro");
			b = teclado.nextInt();
			
			if(b<a) {
				aux=a;
				a=b;
				b=aux;
			}
			aux=0;
			
			try {
				PreparedStatement ps = c
						.prepareStatement("select id,name,diameter from planets where diameter between ? and ? ");
				ps.setInt(1, a);
				ps.setInt(2, b);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					System.out.println("Planeta: ");
					for (int i = 0; i < infoPlanet.length; i++) {
						if (i > 0) {
							System.out.println("--" + infoPlanet[i] + ": " + rs.getString(infoPlanet[i]));

						} else {
							System.out.println(infoPlanet[i] + ": " + rs.getString(infoPlanet[i]));
						}
					}
					System.out.println("");

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void insertPjs(Connection c) {

		try {
			PreparedStatement ps = c.prepareStatement("insert into characters (id,name,planet_id) values(?,?,?);");
			ps.setInt(1, 105);
			ps.setString(2, "Sullivan Seiho");
			ps.setInt(3, 4);

			ps.executeUpdate();

			System.out.println("Personaje insertado");

			ps.setInt(1, 106);
			ps.setString(2, "Hiertro Pelmo");
			ps.setInt(3, 4);

			ps.executeUpdate();

			System.out.println("Personaje insertado");

			ps.setInt(1, 107);
			ps.setString(2, "Feaskul Quetrazo");
			ps.setInt(3, 8);

			ps.executeUpdate();

			System.out.println("Personaje insertado");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void muertes(Connection c) {
		int pelicula=0;
		for (int i = 0; i < 6; i++) {
			try {
				pelicula++;
				PreparedStatement ps = c.prepareStatement("select muertos,asesinos from(\n"
						+ "(select name muertos, deaths.id idM from deaths,characters where characters.id = deaths.id_character and id_film=?)as inst,\n"
						+ "(select name asesinos, deaths.id idA from deaths,characters where characters.id = deaths.id_killer and id_film=?) as inst2)\n"
						+ "where idM=idA");
				ps.setInt(1, pelicula);
				ps.setInt(2, pelicula);
				
				ResultSet rs = ps.executeQuery();
				
				System.out.println("EPISODIO "+pelicula+"-----------------------------------");
				
				while(rs.next()) {
					System.out.println("Personaje muerto: "+rs.getString(1)+". Asesinado a manos de: "+rs.getString(2));
				}
				System.out.println("");


				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
