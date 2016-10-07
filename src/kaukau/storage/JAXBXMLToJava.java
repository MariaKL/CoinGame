//package kaukau.storage;
//import java.io.File;
//import java.util.ArrayList;
//import javax.xml.bind.JAXBContext;
//
//import javax.xml.bind.JAXBException;
//
//import javax.xml.bind.Unmarshaller;
//
//public class JAXBXMLToJava {
//
//	public static void main(String[] args) {
//
//  try {
//
//   // create JAXB context and initializing Marshaller
//
//   JAXBContext jaxbContext = JAXBContext.newInstance(Country.class);
//
//   Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//
//   // specify the location and name of xml file to be read
//
//   File XMLfile = new File("/home/khanshai/workspace/T2/CountryRecord.xml");
//
//   // this will create Java object - country from the XML file
//
//   Country countryIndia = (Country) jaxbUnmarshaller.unmarshal(XMLfile);
//
//   System.out.println("Country Name: "+countryIndia.getCountryName());
//
//   System.out.println("Country Population: "+countryIndia.getCountryPopulation());
//
//
//
//   ArrayList<State> listOfStates=countryIndia.getListOfStates();
//
//   int i=0;
//
//   for(State state:listOfStates)
//
//   {
//
//    i++;
//
//    System.out.println("State:"+i+" "+state.getStateName());
//
//   }
//
//
//
//  } catch (JAXBException e) {
//
//   // some exception occured
//
//   e.printStackTrace();
//
//  }
//
//
//
// }
//
//}
