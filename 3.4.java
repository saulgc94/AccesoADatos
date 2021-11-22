package ieslavereda.es.PR05;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ORM.Course;
import ORM.House;
import ORM.HousePoints;
import ORM.Person;

/**
 *
 * @author Your name
 */
public class Main {
    public static void main(String[]args) {
    	//Creamos la sesión con el hibernateutil
    	try (Session s = HibernateUtil.getSessionFactory().openSession()){
    		
    		//Ejercicio 1
    		//ej1(s,"Xavier","Rosillo","Acceso a datos");
    		
    		//Ejercicio 2
    		ej2(s,"Xavier","Rosillo");
    		
    		//Ejercicio 3
    		ej3(s);
    		
    		//Ejercicio 4
    		//ej4(s,"Muggle Studies","Potions");
    		
    		//Ejercicio 4 version 2
    		//ej4(s,"Flying","Transfiguration");
    		
    		 
    	}catch (Exception e) {
			e.printStackTrace(System.err);
		}
    }
    
    public static void ej1(Session s,String nombre, String apellido, String asignatura) {
    	//creamos una transacción para asegurarnos que se realiza correctamente los cambios ue introducimos
    	Transaction t = null;
    	try {
    	
    		t= s.beginTransaction();
    		//creamos un profesor con los datos introducidos en parámetro
        	Person profe = new Person(666,nombre,apellido);
        	
        	//buscamos los alumnos con id menor de 10
        	Query q = s.createQuery("from Person p where p.id<10");
        	List<ORM.Person> alumns = (List<ORM.Person>) q.getResultList();
        	//pasamos la lista a set
        	Set<ORM.Person> alumnos = alumns.stream().collect(Collectors.toSet());
        	
        	//recogemos la casa de id 3 y guardamos al profe como parte de la csa
        	House h =(House)s.createQuery("from House h where h.id=3").getSingleResult();
        	profe.setHouse(h);
        	s.save(profe);
        	//creamos el curso con el id, el profe creado, el parámetro del nombre de la clase 
        	//añadimos los alumnos recogidos anteriormente y lo guardamos
        	//realizamos el commit para confirmar los cambios
        	Course curso = new Course(73,profe,asignatura,alumnos);
        	s.save(curso);
        	t.commit();
        	
        	System.out.println("Profesor introducido");
    	}catch (Exception e) {
    		e.printStackTrace();
    		//si algo no funciona deshace la operación
    		if(t!=null) {
    			t.rollback();
    		}
    	}
    	
    	
    }
    
    public static void ej2(Session s,String nombre,String apellido) {
    	int id,puntos;
    	//creamos una transacción para asegurarnos que se realiza correctamente los cambios ue introducimos
    	Transaction t = null;
    	try {
    		t=s.beginTransaction();
    		
        	//Guardamos en una variable person al profesor introducido en el ejercicio anterior. 
    		//Lo buscamos por las variables introducidas por parámetro
        	Person profe = (Person)s.createQuery("from Person p where p.firstName like :nombre and p.lastName like :apellido")
        			.setParameter("nombre",nombre).setParameter("apellido",apellido).getSingleResult();
        	
        	//Recogemos los alumnos de id entre 1 y 9 más Ron y Harry
        	Query q = s.createQuery("from Person p where p.firstName like 'harry' and p.lastName like 'potter' or \n"
        			+ "p.firstName like 'ron' and p.lastName like 'Weasley' or p.id between 1 and 9");
        	List<ORM.Person> alum = (List<ORM.Person>) q.getResultList();
        	//por cada alumno de id par le añadimos puntos igual a su id*7
        	//por cada alumno de id impar le restamos puntos igual a su id*7
        	//realizamos un save de los nuevos puntos introducidos
        	for (ORM.Person person : alum) {
        		id=person.getId();
        		if (id%2==0) {
        			puntos=id*7;
            		HousePoints hp = new HousePoints(profe,person,puntos);
            		s.save(hp);
            		System.out.println("Alumno: "+person.getLastName()+" recibe "+puntos+" de "+profe.getLastName());

    			}else {
    				puntos=id*(-7);
    	    		HousePoints hp = new HousePoints(profe,person,puntos);
    	    		s.save(hp);
    	    		System.out.println("Alumno: "+person.getLastName()+" recibe "+puntos+" de "+profe.getLastName());
    			}
    		}
        	//realizamos el commit para confirmar los datos guardados
        	t.commit();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		//si algo no funciona deshace la operación
    		if (t!=null) {
				t.rollback();
			}
    	}
    }
    
    
    public static void ej3(Session s) {
    	//creamos una transacción para asegurarnos que se realiza correctamente los cambios ue introducimos
    	Transaction t = null;
    	try {
    		t=s.beginTransaction();
    		
    		//Guardamos una lista de housepoints donde la persona que da los puntos sea el profesor Xavier Rosillo
    		List<ORM.HousePoints> hp = (List<HousePoints>)s.createQuery("from HousePoints hp where hp.personByGiver.firstName like 'xavier' "
    				+ "and hp.personByGiver.lastName like 'rosillo' and hp.points !=0").getResultList();
    		//pasamos la lista a set
    		Set<HousePoints> house = hp.stream().collect(Collectors.toSet());
    		//por cada housepoint recuperamos los puntos que se dieron, los dividimos por 7 que se les dió por error
    		//y los multiplicamos por 3 para tener los nuevos puntos
    		//hacemos el save y lo mostramos
        	for (HousePoints housePoints : house) {
        		int puntos=housePoints.getPoints();
        		puntos=(puntos/7)*3;
        		
				housePoints.setPoints(puntos);
				s.save(housePoints);
				System.out.println("Alumno: "+housePoints.getPersonByReceiver().getLastName()+" recibe "+puntos+" de "+
				housePoints.getPersonByGiver().getLastName());
			}
        	//realizamos el commit para confirmar los datos guardados
        	t.commit();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		//si algo no funciona deshace la operación
    		if (t!=null) {
				t.rollback();
			}
    	}
    	
    }
    
    public static void ej4(Session s, String clase1, String clase2) {
    	//creamos una transacción para asegurarnos que se realiza correctamente los cambios ue introducimos
    	Transaction t = null;
    	try {
    		t=s.beginTransaction();
    		
    		//Guardamos el número de alumnos de la clase 1 introducida por parámetro y lo mostramos
    		Long num = (Long)s.createQuery("select count(distinct p) from Person p join p.courses_1 c where c.name like :cl ")
    				.setParameter("cl", clase1).uniqueResult();
        	System.out.println("El número de alumnos matriculados en "+clase1+": "+num);
        	
        	//Recuperamos una lista de person que son los alumnos que cursan la clase 2 introducida por parámetro
        	List<ORM.Person> alum = (List<ORM.Person>)s.createQuery("select distinct p from Person p join p.courses_1 c "
        			+ "where c.name like :cl2").setParameter("cl2", clase2).getResultList();
        	Set<ORM.Person> pj = alum.stream().collect(Collectors.toSet());
        	
        	//Recogemos el curso de la clase 1 introducida por parámetro y guardamos la lista de alumnos en el curso
        	//Realizamos el save y el commit para asegurar los cambios
        	Course c = (Course)s.createQuery("from Course c where c.name like :cl").setParameter("cl", clase1).getSingleResult();
        	c.setPersons(pj);
        	s.save(c);
        	t.commit();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		//si algo no funciona deshace la operación
    		if (t!=null) {
				t.rollback();
			}
    	}
    	Long num = (Long)s.createQuery("select count(distinct p) from Person p join p.courses_1 c where c.name like :cl ")
				.setParameter("cl", clase1).uniqueResult();
    	Long num2 = (Long)s.createQuery("select count(distinct p) from Person p join p.courses_1 c where c.name like :cl2 ")
				.setParameter("cl2", clase2).uniqueResult();
    	System.out.println("----------------------------------------------------------");
    	
    	System.out.println("El número de alumnos matriculados en "+clase1+": "+num);
    	
    	System.out.println("El número de alumnos matriculados en "+clase2+": "+num2);
    	
    }
    
    
    
    
}