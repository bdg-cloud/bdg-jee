package fr.legrain.bdg.compteclient.model.mapping.mapper;

import fr.legrain.bdg.compteclient.dto.TaFournisseurDTO;
import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.model.mapping.ILgrMapper;

public class TaFournisseurMapper  implements ILgrMapper<TaFournisseurDTO, TaFournisseur> {

	@Override
	public TaFournisseur mapDtoToEntity(TaFournisseurDTO dto, TaFournisseur entity) {
		if(entity==null)
			entity = new TaFournisseur();

		entity.setIdFournisseur(dto.getIdFournisseur()!=0?dto.getIdFournisseur():0);
		entity.setRaisonSocialeFournisseur(dto.getRaisonSocialeFournisseur());
		entity.setAdresse1Fournisseur(dto.getAdresse1Fournisseur());
		entity.setAdresse2Fournisseur(dto.getAdresse2Fournisseur());
		entity.setAdresse3Fournisseur(dto.getAdresse3Fournisseur());
		entity.setCodePostalFournisseur(dto.getCodePostalFournisseur());
		entity.setPaysFournisseur(dto.getPaysFournisseur());
		entity.setVilleFournisseur(dto.getVilleFournisseur());
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFournisseurDTO mapEntityToDto(TaFournisseur entity, TaFournisseurDTO dto) {
		if(dto==null)
			dto = new TaFournisseurDTO();

		dto.setIdFournisseur(entity.getIdFournisseur());
		dto.setRaisonSocialeFournisseur(entity.getRaisonSocialeFournisseur());
		dto.setAdresse1Fournisseur(entity.getAdresse1Fournisseur());
		dto.setAdresse2Fournisseur(entity.getAdresse2Fournisseur());
		dto.setAdresse3Fournisseur(entity.getAdresse3Fournisseur());
		dto.setCodePostalFournisseur(entity.getCodePostalFournisseur());
		dto.setVilleFournisseur(entity.getVilleFournisseur());
		dto.setPaysFournisseur(entity.getPaysFournisseur());

		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

	
}
