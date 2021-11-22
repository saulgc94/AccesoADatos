package practica2;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Dom_parser {

	public static void main(String[] args) {
		String ruta = "GOTini.xml";
		//String rutaE = "GOTiniCopy.xml";


		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		factoria.setIgnoringComments(true);
		factoria.setIgnoringComments(true);
		try {
			DocumentBuilder db = factoria.newDocumentBuilder();
			Document doc = db.parse(new File(ruta));

			
			//Lectura iterativa
			lecturaIterativa(doc); 
			
			//Lectura recursiva
			System.out.println("Lectura recursiva");
			lecturaRecursiva(doc,0);

			//Elemento por tag name
			eleTagName(doc,"id"); 

			//Añadir el played by
			System.out.println("Añadir el playedBy");
			playedBy(doc, 0);
					
			//Añadir a Jon Snow
			jonSnow(doc);

			
			DOMSource domSource = new DOMSource(doc);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			//Copiarlo en un nuevo xml
			StreamResult r = new StreamResult(new File("GOTiniCopy.xml"));

			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(domSource, r);


		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	
	public static void indentar(int nivel) {
		for (int i = 0; i < nivel; i++) {
			System.out.print("-");
		}
	}
	
	public static void lecturaIterativa(Node doc) {
		int lvl = 0;
		System.out.println("Lectura iterativa: ");

		NodeList nodosHijos = doc.getChildNodes(); // Muestra nodos hijos
		for (int i = 0; i < nodosHijos.getLength(); i++) {
			Node h = nodosHijos.item(i);
			if (h.getNodeType() == Node.ELEMENT_NODE) {
				lvl++;
				indentar(lvl);
				System.out.println(h.getNodeName() + ":");
				NodeList nodosHijos2 = h.getChildNodes();
				for (int j = 0; j < nodosHijos2.getLength(); j++) {
					Node h2 = nodosHijos2.item(j);
					if (h2.getNodeType() == Node.ELEMENT_NODE) {
						lvl++;
						indentar(lvl);
						System.out.println(h2.getNodeName() + ":");
						NodeList nodosHijos3 = h2.getChildNodes();
						for (int p = 0; p < nodosHijos3.getLength(); p++) {
							Node h3 = nodosHijos3.item(p);
							if (h3.getNodeType() == Node.ELEMENT_NODE) {
								lvl++;
								indentar(lvl);
								System.out.println(h3.getNodeName() + ":" + h3.getTextContent() );
								lvl--;
							}

						}lvl--;
					}
				}

			}

		}
	}

	public static void lecturaRecursiva(Node nodo, int nivel) {

		if (nodo.getNodeType() == Node.TEXT_NODE) { // Ignora textos vacÃ­os
			String text = nodo.getNodeValue();
			if (text.trim().length() == 0) {
				return;
			}
		}
		for (int i = 0; i < nivel; i++) {
			System.out.print("--");

		}

		switch (nodo.getNodeType()) { // Escribe informaciï¿½n de nodo segï¿½n tipo
		case Node.DOCUMENT_NODE: // Documento
			System.out.println("DOCUMENTO");
			break;
		case Node.ELEMENT_NODE: // Elemento
			System.out.println("<" + nodo.getNodeName() + ">");
			break;
		case Node.TEXT_NODE: // Texto
			System.out.println("<" + nodo.getNodeValue() + ">");
			break;
		}
		NodeList nodosHijos = nodo.getChildNodes(); // Nodos hijos
		for (int i = 0; i < nodosHijos.getLength(); i++) {
			lecturaRecursiva(nodosHijos.item(i), nivel + 1);
		}

	}

	

	public static void eleTagName(Document doc, String x) {
		System.out.println("Element by tag name");
		
		NodeList pjs = doc.getElementsByTagName("character");
		NodeList ids = doc.getElementsByTagName("id");
		NodeList names = doc.getElementsByTagName("name");
		NodeList titulos = doc.getElementsByTagName("titles");

		for ( int i = 0;i<pjs.getLength();i++) {
			System.out.println(pjs.item(i).getNodeName());
			System.out.println(ids.item(i).getNodeName()+": "+ids.item(i).getTextContent());
			System.out.println(names.item(i).getNodeName()+": "+names.item(i).getTextContent());
			System.out.println(titulos.item(i).getNodeName()+": "+titulos.item(i).getTextContent());

			
		}
		
		

	}

	
	
	public static void playedBy(Node nodo, int nivel) {
		String[] pjs = {"Alfie Allen","Isaac Hempstead-Wright", "Art Parkinson", "Richard Madden", "Sophie Turner"};
		 int posicion=0;
		 
		Document doc = nodo.getOwnerDocument();
		if (nodo.getNodeType() == Node.TEXT_NODE) { // Ignora textos vacÃ­os
			String text = nodo.getNodeValue();
			if (text.trim().length() == 0) {
				return;
			}
		}
		for (int i = 0; i < nivel; i++) {
			System.out.print("--");

		}
		switch (nodo.getNodeType()) { // Escribe informaciï¿½n de nodo segï¿½n tipo
		case Node.DOCUMENT_NODE: // Documento
			System.out.println("DOCUMENTO");
			break;
		case Node.ELEMENT_NODE: // Elemento
			if(nodo.getNodeName()=="character") {
				Element pb = doc.createElement("PlayedBy");
				pb.appendChild(doc.createTextNode(pjs[posicion]));
				posicion++;
				nodo.appendChild(pb);
			}
			System.out.println("<" + nodo.getNodeName() + ">");
			break;
		case Node.TEXT_NODE: // Texto
			System.out.println("<" + nodo.getNodeValue() + ">");
			break;
		}
		NodeList nodosHijos = nodo.getChildNodes(); // Nodos hijos
		for (int i = 0; i < nodosHijos.getLength(); i++) {
			playedBy(nodosHijos.item(i), nivel + 1);
		}
	}
	
	
	
	public static void jonSnow(Document doc) {
		Element pj =doc.createElement("character");
		
		Element id = doc.createElement("id");
		id.appendChild(doc.createTextNode("583"));
		pj.appendChild(id);
		
		Element name = doc.createElement("name");
		name.appendChild(doc.createTextNode("Jon Snow"));
		pj.appendChild(name);
		
		
		Element born = doc.createElement("born");
		born.appendChild(doc.createTextNode("In 283 AC, at Winterfell"));
		pj.appendChild(born);
		
		
		Element alive = doc.createElement("alive");
		alive.appendChild(doc.createTextNode("FALSE"));
		pj.appendChild(alive);
		
		Element titles = doc.createElement("titles");
		pj.appendChild(titles);

		Element title = doc.createElement("title");
		titles.appendChild(title);
		title.appendChild(doc.createTextNode("Lord Commander of the Night's Watch"));
		
		Element title2 = doc.createElement("title");
		titles.appendChild(title2);
		title2.appendChild(doc.createTextNode("King in the North"));
		
		
		Element aliases = doc.createElement("aliases");
		Element alias = doc.createElement("alias");
		aliases.appendChild(alias);

		alias.appendChild(doc.createTextNode("Lord Snow"));
		
		Element alias2 = doc.createElement("alias");
		aliases.appendChild(alias2);
		alias2.appendChild(doc.createTextNode("Ned Stark's Bastard"));
		
		Element alias3 = doc.createElement("alias");
		aliases.appendChild(alias3);
		alias3.appendChild(doc.createTextNode("The Snow of Winterfell"));
		
		Element alias4 = doc.createElement("alias");
		aliases.appendChild(alias4);
		alias4.appendChild(doc.createTextNode("The Crow-Come-Over"));
		
		Element alias5 = doc.createElement("alias");
		alias5.appendChild(doc.createTextNode("The 998th Lord Commander of the Night's Watch"));
		aliases.appendChild(alias5);
		
		Element alias6 = doc.createElement("alias");
		aliases.appendChild(alias6);
		alias6.appendChild(doc.createTextNode("The Bastard of Winterfell"));

		
		Element alias7 = doc.createElement("alias");
		aliases.appendChild(alias7);
		alias7.appendChild(doc.createTextNode("The Black Bastard of the Wall"));
		
		Element alias8 = doc.createElement("alias");
		aliases.appendChild(alias8);
		alias8.appendChild(doc.createTextNode("Lord Crow"));
		pj.appendChild(aliases);
		
		Element actor = doc.createElement("Actor");
		actor.appendChild(doc.createTextNode("Kit Harrington"));
		pj.appendChild(actor);
		
		Node nodGOT = doc.getFirstChild();
		nodGOT.appendChild(pj);
		
	}

}
