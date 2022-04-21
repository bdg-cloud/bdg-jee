package fr.legrain.autorisation.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="auth")
public class ListeModules {

	@XmlElementWrapper(name="modules")
	public List<Module> module = new ArrayList<Module>();
	
	@XmlElement(name="nb-poste-client")
	public String nbPosteClient = "";
	
	@XmlElement(name="nb-utilisateur")
	public String nbUtilisateur = "";
	
	@XmlElement(name="acces-webservice")
	public String accessWebservice = "";
	
	public File creeXmlModuleFile(ListeModules listModule, String path){
		File id = new File(path);
		Marshaller marshaller;
		try {
			marshaller = JAXBContext.newInstance(ListeModules.class).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(listModule, id);
			return id;
		} catch (JAXBException e) {
			return null;
		}
	}
	
	public String creeXmlModuleString(ListeModules listModule){
		Marshaller marshaller;
		Writer w = new StringWriter();
		try {
			marshaller = JAXBContext.newInstance(ListeModules.class).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(listModule, w);
			return w.toString();
		} catch (JAXBException e) {
			return null;
		}
	}
	
	public ListeModules recupModulesXml(String input){
		ListeModules map = new ListeModules();
		InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		Unmarshaller unmarshaller;
		try {
			unmarshaller = JAXBContext.newInstance(ListeModules.class).createUnmarshaller();
			map = (ListeModules) unmarshaller.unmarshal(stream);
		} catch (JAXBException e) {
			return null;
		}
		return map;
	}
	
	public boolean contientModule(Module m) {
		boolean trouve = false;
		Iterator<Module> ite = module.iterator();
		Module var = null;
		while(!trouve && ite.hasNext()) {
			var = ite.next();
			if(var.id.equals(m.id)) {
				trouve = true;
			}
		}
		return trouve;
	}

}
