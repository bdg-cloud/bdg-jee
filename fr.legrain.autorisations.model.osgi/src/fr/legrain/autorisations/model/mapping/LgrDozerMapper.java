package fr.legrain.autorisations.model.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import net.sf.dozer.util.mapping.DozerBeanMapper;



import org.apache.log4j.Logger;
//import org.eclipse.core.runtime.FileLocator;
//import org.eclipse.core.runtime.Path;
import org.dozer.DozerBeanMapper;


public class LgrDozerMapper<Source,Destination extends Object> {
	
	static Logger logger = Logger.getLogger(LgrDozerMapper.class.getName());
	
	private static DozerBeanMapper mapper = null;

	private String mappingFile = "dozerBeanMapping.xml";
//	private String mappingFile = "META-INF/dozerBeanMapping.xml";
	private List<String> myMappingFiles = new ArrayList<String>();
	
	public LgrDozerMapper() {
		if(mapper == null) {
			try {
			mapper = new DozerBeanMapper();
			} catch(Exception e) {
				e.printStackTrace();
			}
			mapper.setMappingFiles(getMappingFiles());
		}
	}
	
	public List<String> getMappingFiles() {
		myMappingFiles.clear();
//		URL mappingURL = FileLocator.find(gestComBdPlugin.getDefault().getBundle(),new Path(mappingFile),null);
//		//myMappingFiles.add("file:/donnees/Projet/Java/Eclipse/GestionCommerciale/gestComBd/dozerBeanMapping.xml");
//		try {
//			logger.debug("DozerMappingFile : "+FileLocator.toFileURL(mappingURL).toString());
//			myMappingFiles.add(FileLocator.toFileURL(mappingURL).toString());
//		} catch (IOException e) {
//			logger.error("",e);
//		}
//		InputStream in = this.getClass().getResourceAsStream(mappingFile);
		myMappingFiles.add(mappingFile);
		return myMappingFiles;
	}
	
	public DozerBeanMapper getMapper() {
		if(mapper == null) {
			mapper = new DozerBeanMapper();
			mapper.setMappingFiles(myMappingFiles);
		}
		return mapper;
	}
	
	public Destination map(Source o, Class dest) {
		try {
			return (Destination) getMapper().map(o, dest);
		} catch (Exception e) {
			logger.error("",e);
		}
		return null;
	}
	
	public void map(Source o, Destination d) {
		getMapper().map(o, d);
	}

	public List<String> getMyMappingFiles() {
		return myMappingFiles;
	}

	public String getMappingFile() {
		return mappingFile;
	}







}
