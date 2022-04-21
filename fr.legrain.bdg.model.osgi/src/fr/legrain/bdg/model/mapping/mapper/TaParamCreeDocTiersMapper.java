package fr.legrain.bdg.model.mapping.mapper;

import java.util.ArrayList;
import java.util.HashSet;

import javax.ejb.FinderException;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.TaTDocDTO;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaParamCreeDocTiersDTO;
import fr.legrain.tiers.model.TaParamCreeDocTiers;


public class TaParamCreeDocTiersMapper implements ILgrMapper<TaParamCreeDocTiersDTO, TaParamCreeDocTiers> {

	@Override
	public TaParamCreeDocTiers mapDtoToEntity(TaParamCreeDocTiersDTO dto, TaParamCreeDocTiers entity) {
		if(entity==null) {
			entity = new TaParamCreeDocTiers();
		}
		mapperModelToUI(dto,entity);
		
		entity.setTaTDoc(new HashSet<TaTDoc>());
//		TaTDocDAO taTDocDAO = null;
		TaTDoc taTDoc = null;
		if(dto.getTaTDoc()!=null) {
		
			for (TaTDocDTO tdocTemp : dto.getTaTDoc()) {
				taTDoc = new TaTDoc();
				mapperModelToUITdoc(tdocTemp, taTDoc);
				entity.getTaTDoc().add(taTDoc);
			}
		}
//		} 
		entity.setVersionObj(dto.getVersionObj());
		return entity;
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
			mapperUIToModelTdoc(tdoc,swtTDoc);
			dto.getTaTDoc().add(swtTDoc);
		}
		dto.setVersionObj(entity.getVersionObj());
		return dto;
	}

	private void mapperModelToUITdoc(TaTDocDTO doc,TaTDoc e){
		if(doc.getCodeTDoc()!=null)e.setCodeTDoc(doc.getCodeTDoc());
		if(doc.getId()!=null)e.setIdTDoc(doc.getId());
		if(doc.getLibTDoc()!=null)e.setLibTDoc(doc.getLibTDoc());
		doc.setVersionObj(e.getVersionObj());
	}
	
	private void mapperUIToModelTdoc(TaTDoc e,TaTDocDTO doc){
		doc.setCodeTDoc(e.getCodeTDoc());
		doc.setId(e.getIdTDoc());
		doc.setLibTDoc(e.getLibTDoc());
		e.setVersionObj(e.getVersionObj());
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
		if(doc.getId()!=null)e.setIdParamCreeDocTiers(doc.getId());
		if(doc.getNbJours()!=null)e.setNbJours(doc.getNbJours());
//		e.setTaTiers(e.getTaTiers().getIdTiers());
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
}
