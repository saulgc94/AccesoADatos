package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StarWars {
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

		try (Connection c = DriverManager.getConnection(urlConnection, user, pwd); 
				Statement s = c.createStatement()) {
			
			//Ejercicio 1
			mostrarPlanetas(s);
			
			//Ejercicio 2
			insertarPelis(s);
			
			//Ejercicio 3
			mostrarOrdenJedi(s);
			
			//Ejercicio 4
			muertes(s);

		} catch (SQLException e) {
			muestraErrorSQL(e);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public static void mostrarPlanetas(Statement s) {
		String[] infoPlanet = new String[] { "id", "name", "rotation_period", "orbital_period", "diameter", "climate",
				"gravity", "terrain", "surface_water", "population", "created_date", "updated_date", "url" };

		try {
			ResultSet rs = s.executeQuery("Select * from planets");
			while (rs.next()) {
				System.out.println("Planeta: ");

				for (int i = 0; i < infoPlanet.length; i++) {
					if (i>0) {
						System.out.println("--"+infoPlanet[i] + ": " + rs.getString(infoPlanet[i]));

					}else{
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

	public static void insertarPelis(Statement s) {
	
		try {
			s.executeUpdate("INSERT INTO films (id,episode,title) VALUES "
			          + " ('7','Episode VII','El Despertar de la fuerza'),"
			          + " ('8','Episode VIII', 'Los últimos Jedi'),"
			          + " ('9','Episode IX','El ascenso de Skywalker');");
		      System.out.println(" Filas insertadas.");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void mostrarOrdenJedi(Statement s) {
		String[] infoPj = new String[] { "id","name","height","mass","hair_color","skin_color","eye_color","birth_year",
				"gender","planet_id","created_date","updated_date","url","id_character","id_affiliation","id","affiliation"};
 

		try {
			ResultSet rs = s.executeQuery("select * from characters,character_affiliations, affiliations where \n"
					+ "characters.id=character_affiliations.id_character and\n"
					+ "affiliations.id=character_affiliations.id_affiliation  and affiliation like 'Jedi Order';");
			while (rs.next()) {
				System.out.println("Personaje: ");

				for (int i = 0; i < infoPj.length; i++) {
					if (i>0) {
						System.out.println("--"+infoPj[i] + ": " + rs.getString(infoPj[i]));

					}else{
						System.out.println(infoPj[i] + ": " + rs.getString(infoPj[i]));
					}

				}
				System.out.println("");

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void muertes(Statement s) {
		
		try {
			ResultSet rs = s.executeQuery("select muertos,asesinos from(\n"
					+ "(select name muertos, deaths.id idM from deaths,characters where characters.id = deaths.id_character and id_film=3)as inst,\n"
					+ "(select name asesinos, deaths.id idA from deaths,characters where characters.id = deaths.id_killer and id_film=3) as inst2)\n"
					+ "where idM=idA");
			
			while(rs.next()) {
				
				System.out.println("Personaje muerto: "+rs.getString(1)+". Asesinado a manos de: "+rs.getString(2));
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	
		
	}
	


}
