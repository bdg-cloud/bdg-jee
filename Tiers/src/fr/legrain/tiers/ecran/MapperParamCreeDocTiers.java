package fr.legrain.tiers.ecran;

import java.util.ArrayList;
import java.util.HashSet;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTDocServiceRemote;
import fr.legrain.document.dto.TaTDocDTO;
import fr.legrain.document.model.TaTDoc;
//import fr.legrain.gestCom.Appli.LgrDozerMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dto.TaParamCreeDocTiersDTO;
import fr.legrain.tiers.model.TaParamCreeDocTiers;

public class MapperParamCreeDocTiers implements ILgrMapper<TaParamCreeDocTiersDTO, TaParamCreeDocTiers> {
	
	private LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers> mapperUIToModel = 
			new LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers>();
	private LgrDozerMapper<TaParamCreeDocTiers,TaParamCreeDocTiersDTO> mapperModelToUI  = 
			new LgrDozerMapper<TaParamCreeDocTiers,TaParamCreeDocTiersDTO>();
	
	private LgrDozerMapper<TaTDocDTO,TaTDoc> mapperUIToModelTdoc  = new LgrDozerMapper<TaTDocDTO,TaTDoc>();
	private LgrDozerMapper<TaTDoc,TaTDocDTO> mapperModelToUITdoc  = new LgrDozerMapper<TaTDoc,TaTDocDTO>();
	
//	private EntityManager em = null;
	private ITaTDocServiceRemote taTDocDAO = null;
	
	public MapperParamCreeDocTiers() throws NamingException {
		taTDocDAO = new EJBLookup<ITaTDocServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_DOC_SERVICE, ITaTDocServiceRemote.class.getName());
	}

//	@Override
//	public TaParamCreeDocTiers dtoToEntity(TaParamCreeDocTiersDTO e) {
//		TaParamCreeDocTiers doc = new TaParamCreeDocTiers();
//		mapperUIToModel(doc, e);
//		doc.setTaTDoc(new HashSet<TaTDoc>());
////		TaTDocDAO taTDocDAO = null;
//		TaTDoc taTDoc = null;
////		if(em!=null) {
////			taTDocDAO = new TaTDocDAO(em);
//			for (TaTDocDTO tdoc : e.getTaTDoc()) {
//				taTDoc = taTDocDAO.findById(tdoc.getId());
//				doc.getTaTDoc().add(taTDoc);
//			}
////		}
//		return doc;
//	}
	
	public TaParamCreeDocTiers mapDtoToEntity(TaParamCreeDocTiersDTO dto, TaParamCreeDocTiers entity) {
		if(entity==null) {
			entity = new TaParamCreeDocTiers();
		}
		mapperModelToUI(dto,entity);
		
		entity.setTaTDoc(new HashSet<TaTDoc>());
//		TaTDocDAO taTDocDAO = null;
		TaTDoc taTDoc = null;
//		if(em!=null && e.getTaTDoc()!=null) {
//			taTDocDAO = new TaTDocDAO(em);
			for (TaTDocDTO tdoc : dto.getTaTDoc()) {
				try {
					taTDoc = taTDocDAO.findById(tdoc.getId());
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				entity.getTaTDoc().add(taTDoc);
			}
//		} 
		return entity;
	}
	private void mapperUIToModel(TaParamCreeDocTiers e,TaParamCreeDocTiersDTO doc){
		if(e.getTiers()!=null)doc.setTiers(LibConversion.intToBoolean(e.getTiers()));
		if(e.getDocument()!=null)doc.setDocument(LibConversion.intToBoolean(e.getDocument()));
		if(e.getSemaine()!=null)doc.setSemaine(LibConversion.intToBoolean(e.getSemaine()));
		if(e.getDeuxSemaines()!=null)doc.setDeuxSemaines(LibConversion.intToBoolean(e.getDeuxSemaines()));
		if(e.getDecade()!=null)doc.setDecade(LibConversion.intToBoolean(e.getDecade()));
		if(e.getMois()!=null)doc.setMois(LibConversion.intToBoolean(e.getMois()));
		if(e.getxJours()!=null)doc.setxJours(LibConversion.intToBoolean(e.getxJours()));
		doc.setId(e.getIdParamCreeDocTiers());
		if(e.getNbJours()!=null)doc.setNbJours(e.getNbJours());
		if(e.getTaTiers()!=null)doc.setIdTiers(e.getTaTiers().getIdTiers());
		if(e.getCodeParam()!=null)doc.setCodeParam(e.getCodeParam());
	}
	private void mapperModelToUI(TaParamCreeDocTiersDTO doc,TaParamCreeDocTiers e){
		if(doc.getTiers()!=null)e.setTiers(LibConversion.booleanToInt(doc.getTiers()));
		if(doc.getDocument()!=null)e.setDocument(LibConversion.booleanToInt(doc.getDocument()));
		if(doc.getSemaine()!=null)e.setSemaine(LibConversion.booleanToInt(doc.getSemaine()));
		if(doc.getDeuxSemaines()!=null)e.setDeuxSemaines(LibConversion.booleanToInt(doc.getDeuxSemaines()));
		if(doc.getDecade()!=null)e.setDecade(LibConversion.booleanToInt(doc.getDecade()));
		if(doc.getMois()!=null)e.setMois(LibConversion.booleanToInt(doc.getMois()));
		if(doc.getxJours()!=null)e.setxJours(LibConversion.booleanToInt(doc.getxJours()));
		if(doc.getCodeParam()!=null)e.setCodeParam(doc.getCodeParam());
		e.setIdParamCreeDocTiers(doc.getId());
		e.setNbJours(doc.getNbJours());
//		e.setTaTiers(e.getTaTiers().getIdTiers());
	}
	
	@Override
	public TaParamCreeDocTiersDTO mapEntityToDto(TaParamCreeDocTiers entity, TaParamCreeDocTiersDTO dto) {
		if(dto==null) {
			dto = new TaParamCreeDocTiersDTO();
		}

		mapperUIToModel(entity, dto);
		
		dto.setTaTDoc(new ArrayList<TaTDocDTO>());
		TaTDocDTO swtTDoc = null;
		for (TaTDoc tdoc : entity.getTaTDoc()) {
			swtTDoc = new TaTDocDTO();
			mapperModelToUITdoc.map(tdoc, swtTDoc);
			dto.getTaTDoc().add(swtTDoc);
		}
		return dto;
	}


//	public void setEm(EntityManager em) {
//		this.em = em;
//	}

}
