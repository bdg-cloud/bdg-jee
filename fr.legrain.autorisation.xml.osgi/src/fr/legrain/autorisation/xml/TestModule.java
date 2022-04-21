package fr.legrain.autorisation.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TestModule {

	/**
	 * @param args
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws JAXBException {
		String basePath = "C:/LGRDOSS/BureauDeGestion/";
		ListeModules map = new ListeModules();
		File id = new File(basePath+"essai.txt");
		if(id.exists()) {
			
			Unmarshaller unmarshaller = JAXBContext.newInstance(ListeModules.class).createUnmarshaller();
			map = (ListeModules) unmarshaller.unmarshal(id);
			System.out.println(map);
			
		} else {
			map.module.add(new Module("Facture"));
			map.module.add(new Module("BonLiv"));
			map.module.add(new Module("Devis"));
			
			Marshaller marshaller = JAXBContext.newInstance(ListeModules.class).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(map, id);
			
		}

	}
	


}
