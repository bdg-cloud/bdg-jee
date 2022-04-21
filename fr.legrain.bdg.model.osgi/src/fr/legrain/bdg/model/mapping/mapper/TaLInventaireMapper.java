package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.stock.dto.LInventaireDTO;
import fr.legrain.stock.model.TaLInventaire;


public class TaLInventaireMapper implements ILgrMapper<LInventaireDTO, TaLInventaire> {

	@Override
	public TaLInventaire mapDtoToEntity(LInventaireDTO dto, TaLInventaire entity) {
		if(entity==null)
			entity = new TaLInventaire();

		entity.setIdLInventaire(dto.getId()!=null?dto.getId():0);
		entity.setQteTheorique(dto.getQteTheorique());
		entity.setQteReelle(dto.getQteReelle());
		entity.setUn1Inventaire(dto.getUn1Inventaire());
		entity.setUn2Inventaire(dto.getUn2Inventaire());
		entity.setNumLot(dto.getNumLot());
		entity.setLibelleInventaire(dto.getLibelleLInventaire());
		entity.setEmplacement(dto.getEmplacement());
		entity.setQte2LInventaire(dto.getQte2LInventaire());
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public LInventaireDTO mapEntityToDto(TaLInventaire entity, LInventaireDTO dto) {
		if(dto==null)
			dto = new LInventaireDTO();

		dto.setId(entity.getIdLInventaire());
		
		dto.setQteTheorique(entity.getQteTheorique());
		dto.setQteReelle(entity.getQteReelle());
		dto.setQte2LInventaire(entity.getQte2LInventaire());
		dto.setUn1Inventaire(entity.getUn1Inventaire());
		dto.setUn2Inventaire(entity.getUn2Inventaire());
		dto.setNumLot(entity.getNumLot());
		dto.setLibelleLInventaire(entity.getLibelleInventaire());
		dto.setEmplacement(entity.getEmplacement());
		dto.setEcart(entity.getQteReelle().subtract(entity.getQteTheorique()));
		if(entity.getTaEntrepot()!=null) dto.setCodeEntrepot(entity.getTaEntrepot().getCodeEntrepot());
		else if(entity.getTaMouvementStock()!=null && entity.getTaMouvementStock().getTaEntrepot()!=null)
			dto.setCodeEntrepot(entity.getTaMouvementStock().getTaEntrepot().getCodeEntrepot());	
		//if(entity.getTaLot()!=null) dto.setNumLot(entity.getTaLot().getNumLot());
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
