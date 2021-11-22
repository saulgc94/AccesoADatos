package ieslavereda.es.PR04;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.query.Query;

import ORM.Course;
import ORM.Person;


/**
 *
 * @author sgarciac1
 */

//PRÁCTICA 4
public class Main {
    public static void main(String []args) {
    	//creamos la sesion con el hibernateutil
    	try (Session s = HibernateUtil.getSessionFactory().openSession()) {
    		//Listado de ejercicios a los que se le pasa la sesión
    		
    		//Ejercicio 1
    		ej1(s);
    		
    		//Ejercicio 2
			ej2(s);
			
			//Ejercicoi 3
    		ej3(s);
    		
    		//Ejercicio 4 con Fetch
			ej4Fetch(s,"Gryffindor","Potions");
			
			//Ejercicio 4 sin Fetch
    		ej4NoFetch(s,"Gryffindor","Potions");

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
        
    }
    public static void ej1(Session s) {
    	//Se recoge la session y se lanza una query,guardamos el resultado en una lista de personas con un resultList  
    	//y por ultimo recorremos con un for mostrando los datos deseados
    	Query q = s.createQuery("from Person p where p.lastName like 'Potter'");
    	List<ORM.Person> pj = (List<ORM.Person>) q.getResultList();
    	for (ORM.Person person : pj) {
			System.out.println("Familiar: "+person.getId()+". Nombre: "+person.getFirstName()+" "+person.getLastName()+". Casa: "+person.getHouse().getName());
		}
    	
    }
    public static void ej2(Session s) {
    	//Se recoge la session y se lanza una query,guardamos el resultado con un scrollableresult y el while-next  en un objeto persona  
    	//y por ultimo recorremos mostrando los datos deseados
    	ScrollableResults sr = s.createQuery("from Person p join fetch p.courses_1 order by p.lastName").scroll();
    	while(sr.next()) {
    		Person p = (Person)sr.get(0);
    		//Course c = (Course)sr.get(1);
    		System.out.println(p.getLastName()+","+p.getFirstName());
    	}
    	Long num = (Long)s.createQuery("select count(distinct p) from Person p join p.courses_1 order by p.lastName").uniqueResult();
    	System.out.println("El número de alumnos totales es "+num);
    }
    public static void ej3(Session s) {
    	//Se recoge la session y se lanza una query,guardamos en una lista de HousePoints con un resultList  
    	//y por ultimo recorremos con un for mostrando los datos deseados
    	int puntosT=0;
    	Query q = s.createQuery("select p.housePointsesForReceiver\n"
    			+ "from Person p where \n"
    			+ "p.firstName like 'Harry' and p.lastName like 'Potter' or \n"
    			+ "p.firstName like 'Ron' and p.lastName like 'Weasley' or \n"
    			+ "p.firstName like 'Hermione' and p.lastName like 'Granger' ");
    	List<ORM.HousePoints> houseP = (List<ORM.HousePoints>) q.getResultList();
    	for (ORM.HousePoints hp : houseP) {
    		System.out.println(hp.getPersonByGiver().getFirstName()+" "+hp.getPersonByGiver().getLastName()+" Da "+hp.getPoints()+" a "
    	+hp.getPersonByReceiver().getFirstName()+" "+hp.getPersonByReceiver().getLastName());
    		puntosT+=hp.getPoints();
    		
		}
    	System.out.println("Los puntos totales son "+puntosT);
    	
    }
    
    public static void ej4Fetch(Session s,String casa, String clase) {
    	
    	//Se recoge al profesor de la clase
    	Person p =(Person) s.createQuery("select c.person from Course c where c.name like :cl").setParameter("cl", clase).getSingleResult();

    	//se lanza una query con fetch,guardamos en una lista de Person con un resultList
    	Query q = s.createQuery("from Person p join fetch p.courses_1 c where p.house.name like :ca and c.name like :cl").setParameter("ca", casa)
    			.setParameter("cl", clase);
    	List<ORM.Person> pj = (List<ORM.Person>) q.getResultList();
    	
    	
    	//y por ultimo recorremos con un for mostrando los datos deseados
    	System.out.println("Alumnos: ");
    	for(ORM.Person person : pj) {
    		System.out.println(person.getLastName()+", "+person.getFirstName()+" estudia "+ clase+" con "+p.getFirstName()+" "+p.getLastName());
    	}

    	
    }
    public static void ej4NoFetch(Session s,String casa, String clase) {
    	
    	//Se recoge al profesor de la clase
    	Person pj =(Person) s.createQuery("select c.person from Course c where c.name like :cl").setParameter("cl", clase).getSingleResult();
    	
    	//se lanza una query de Person que serán los alumnos y 
    	//guardamos el resultado con un scrollableresult 
    	//en el while-next cargamos el person de cada iteración  
    	ScrollableResults sr = s.createQuery("select p from Person p join p.courses_1 c where p.house.name like :ca and c.name like :cl").setParameter("ca", casa)
    			.setParameter("cl", clase).scroll();
    	
    	//y por ultimo recorremos mostrando los datos deseados
    	System.out.println("Alumnos: ");
    	while(sr.next()) {
    		Person p = (Person)sr.get(0);
    		System.out.println(p.getLastName()+", "+p.getFirstName()+" estudia "+ clase+" con "+pj.getFirstName()+" "+pj.getLastName());
    		
    	}
    }
    
    
}