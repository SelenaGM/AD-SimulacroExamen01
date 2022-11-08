import modelos.Animal;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Scanner sc;
    private static ArrayList<Animal> listaAnimales;
    private static File fichero;

    static {
        sc = new Scanner(System.in);
        listaAnimales = new ArrayList<>();
        fichero = new File("ListaAnimales.dat");
    }

    public static void main(String[] args) {

        int opcion = 0;

        do {
            try {
                opcion = menu();
                sc.nextLine();
                switch (opcion) {
                    case 1:
                        Animal animal = crearAnimal();
                        listaAnimales.add(animal);
                        //SELENA HAZ UN FUNCION QUE TE LEA LAS LISTAS...

                        break;
                    case 2:
                        escribirFicheroBinario();
                        break;
                    case 3:
                        cargarFicheroBinario();
                        for (Animal a : listaAnimales) {
                            System.out.println(a);
                        }
                        break;
                    case 4:

                        escribirFicheroXML();

                        break;
                    case 5:
                        leerFicheroXML();
                        break;
                    case 6:
                        System.out.println("Hasta la próxima.");
                        break;
                    default:
                        System.out.println("Esa opción no existe, introduzca otro número.");
                }
            } catch (InputMismatchException e) {
                opcion = 0;
                sc.nextLine();
                System.out.println("Debe introducir un número");
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        } while (opcion != 6);

    }

    private static void leerFicheroXML() throws ParserConfigurationException, IOException, SAXException {
        listaAnimales.clear();

        File ficheroAnimalesXML = new File("animales.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(ficheroAnimalesXML);

        document.getDocumentElement().normalize();

        NodeList nodos = document.getElementsByTagName("animal");

        for (int i = 0; i < nodos.getLength(); i++) {

            Node nodo = nodos.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element animal = (Element) nodo;
                String especie = animal.getElementsByTagName("especie").item(0).getTextContent();
                String raza = animal.getElementsByTagName("raza").item(0).getTextContent();
                int edad = Integer.parseInt(animal.getElementsByTagName("edad").item(0).getTextContent());
                String color = animal.getElementsByTagName("color").item(0).getTextContent();


                Animal animalN = new Animal(especie, raza, edad, color);
                listaAnimales.add(animalN);

            }


        }
        for (Animal a : listaAnimales) {
            System.out.println(a);
        }
    }

    private static void escribirFicheroXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        Element raiz = document.createElement("animales");
        document.appendChild(raiz);

        for (int i = 0; i < listaAnimales.size(); i++) {
            Element animal = document.createElement("animal");
            raiz.appendChild(animal);
            Element especie = document.createElement("especie");
            especie.setTextContent(listaAnimales.get(i).getEspecie());
            animal.appendChild(especie);

            Element raza = document.createElement("raza");
            raza.setTextContent(listaAnimales.get(i).getRaza());
            animal.appendChild(raza);

            Element edad = document.createElement("edad");
            edad.setTextContent(String.valueOf(listaAnimales.get(i).getEdad()));
            animal.appendChild(edad);

            Element color = document.createElement("color");
            color.setTextContent(listaAnimales.get(i).getColor());
            animal.appendChild(color);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        DOMSource ds = new DOMSource(document);

        t.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(new File("animales.xml"));
        t.transform(ds, result);
    }

    private static void cargarFicheroBinario() {
        //ACUERDATE DEL EOFEXCEPTION!!!
        FileInputStream fis;
        listaAnimales.clear();
        try {
            if (fichero.exists()) {
                if (listaAnimales.isEmpty()) {
                    System.out.println("La lista está vacia");
                } else {
                    System.out.println("La lista no está vacía");
                }
                fis = new FileInputStream(fichero);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while (true) {
                    Animal animal = (Animal) ois.readObject();
                    listaAnimales.add(animal);

                }


            } else {
                System.out.println("No hay datos dentro del fichero");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (EOFException e) {
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void escribirFicheroBinario() {
        FileOutputStream fos;
        try {
            ObjectOutputStream oos;
            if (fichero.exists()) {
                oos = new MyOOS(new FileOutputStream(fichero, true));
            } else {
                oos = new ObjectOutputStream(new FileOutputStream(fichero, true));
            }
            for (Animal a : listaAnimales) {
                oos.writeObject(a);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Animal crearAnimal() {
        String especie, raza, color;
        int edad;
        try {
            System.out.println("Introduce los datos del animal");
            System.out.println("Especie:");
            especie = sc.nextLine();
            System.out.println("Raza:");
            raza = sc.nextLine();
            System.out.println("Edad:");
            edad = sc.nextInt();
            System.out.println("Color:");
            sc.nextLine();
            color = sc.nextLine();
            return new Animal(especie, raza, edad, color);


        } catch (InputMismatchException e) {
            System.out.println("No se ha podido ingresar el animal debido a un fallo ingresando los datos, asegurese que son correctos.");
            return null;
        }

    }

    private static int menu() {
        System.out.println("1-Crear animal");
        System.out.println("2-Escribir en Fichero Binario");
        System.out.println("3-Cargar de Fichero Binario");
        System.out.println("4-Escribir en Fichero XML");
        System.out.println("5-Leer de Fichero XML");
        System.out.println("6-Salir");
        return sc.nextInt();
    }
}