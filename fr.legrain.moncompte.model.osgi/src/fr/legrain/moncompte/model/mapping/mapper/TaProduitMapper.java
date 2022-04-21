package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaProduitDTO;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaProduitMapper implements ILgrMapper<TaProduitDTO, TaProduit> {

	@Override
	public TaProduit mapDtoToEntity(TaProduitDTO dto, TaProduit entity) {
		if(entity==null)
			entity = new TaProduit();

		entity.setIdProduit(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelle(dto.getLibelle());
		entity.setCode(dto.getCode());
		entity.setDescription(dto.getDescription());
		entity.setLibelleHtml(dto.getLibelleHtml());
		entity.setDescriptionHtml(dto.getDescriptionHtml());
		entity.setCompose(dto.getCompose());
		entity.setSousProduit(dto.getSousProduit());
		entity.setVendable(dto.getVendable());
		entity.setIdentifiantModule(dto.getIdentifiantModule());
		entity.setVersionProduit(dto.getVersionProduit());
		entity.setPrixHT(dto.getPrixHT());
		entity.setPrixTTC(dto.getPrixTTC());
		entity.setTauxTVA(dto.getTauxTVA());
		entity.setTva(dto.getTva());
		
		entity.setPrixHTLight(dto.getPrixHTLight());
		entity.setPrixTTCLight(dto.getPrixTTCLight());
		entity.setTvaLight(dto.getTvaLight());
		
		entity.setPrixHTParPosteInstalle(dto.getPrixHTParPosteInstalle());
		entity.setPrixTTCParPosteInstalle(dto.getPrixTTCParPosteInstalle());
		entity.setTvaParPosteInstalle(dto.getTvaParPosteInstalle());
		
		entity.setPrixHTWs(dto.getPrixHTWs());
		entity.setPrixTTCWs(dto.getPrixTTCWs());
		entity.setTvaWs(dto.getTvaWs());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaProduitDTO mapEntityToDto(TaProduit entity, TaProduitDTO dto) {
		if(dto==null)
			dto = new TaProduitDTO();

		dto.setId(entity.getIdProduit());
		
		dto.setLibelle(entity.getLibelle());
		dto.setCode(entity.getCode());
		
		dto.setDescription(entity.getDescription());
		dto.setLibelleHtml(entity.getLibelleHtml());
		dto.setDescriptionHtml(entity.getDescriptionHtml());
		dto.setCompose(entity.getCompose());
		dto.setSousProduit(entity.getSousProduit());
		dto.setVendable(entity.getVendable());
		dto.setIdentifiantModule(entity.getIdentifiantModule());
		dto.setVersionProduit(entity.getVersionProduit());
		dto.setPrixHT(entity.getPrixHT());
		dto.setPrixTTC(entity.getPrixTTC());
		dto.setTauxTVA(entity.getTauxTVA());
		dto.setTva(entity.getTva());
		
		dto.setPrixHTLight(entity.getPrixHTLight());
		dto.setPrixTTCLight(entity.getPrixTTCLight());
		dto.setTvaLight(entity.getTvaLight());
		
		dto.setPrixHTParPosteInstalle(entity.getPrixHTParPosteInstalle());
		dto.setPrixTTCParPosteInstalle(entity.getPrixTTCParPosteInstalle());
		dto.setTvaParPosteInstalle(entity.getTvaParPosteInstalle());
		
		dto.setPrixHTWs(entity.getPrixHTWs());
		dto.setPrixTTCWs(entity.getPrixTTCWs());
		dto.setTvaWs(entity.getTvaWs());		
		if(entity.getTaTNiveau()!=null) dto.setCodeTNiveau(entity.getTaTNiveau().getCode());
		if(entity.getTaTypeProduit()!=null) dto.setCodeTypeProduit(entity.getTaTypeProduit().getCode());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
