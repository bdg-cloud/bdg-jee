package fr.legrain.bdg.compteclient.model.mapping.mapper;


import fr.legrain.bdg.compteclient.dto.TaMesFournisseursDTO;
import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.bdg.compteclient.model.mapping.ILgrMapper;

public class TaMesFournisseursMapper implements ILgrMapper<TaMesFournisseursDTO, TaMesFournisseurs> {

	@Override
	public TaMesFournisseurs mapDtoToEntity(TaMesFournisseursDTO dto, TaMesFournisseurs entity) {
		if(entity==null)
			entity = new TaMesFournisseurs();

		entity.setIdFournisseur(dto.getIdFournisseur()!=0?dto.getIdFournisseur():0);
		entity.getTaFournisseur().setRaisonSocialeFournisseur(dto.getRaisonSocialeFournisseur());
		entity.getTaFournisseur().setAdresse1Fournisseur(dto.getAdresse1Fournisseur());
		entity.getTaFournisseur().setAdresse2Fournisseur(dto.getAdresse2Fournisseur());
		entity.getTaFournisseur().setAdresse3Fournisseur(dto.getAdresse3Fournisseur());
		entity.getTaFournisseur().setCodePostalFournisseur(dto.getCodePostalFournisseur());
		entity.getTaFournisseur().setVilleFournisseur(dto.getPaysFournisseur());
		entity.getTaFournisseur().setPaysFournisseur(dto.getVilleFournisseur());
		entity.getTaFournisseur().setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaMesFournisseursDTO mapEntityToDto(TaMesFournisseurs entity, TaMesFournisseursDTO dto) {
		if(dto==null)
			dto = new TaMesFournisseursDTO();

		dto.setIdFournisseur(entity.getIdFournisseur());
		
		dto.setAdresse1Fournisseur(entity.getTaFournisseur().getAdresse1Fournisseur());
		dto.setAdresse2Fournisseur(entity.getTaFournisseur().getAdresse2Fournisseur());
		dto.setAdresse3Fournisseur(entity.getTaFournisseur().getAdresse3Fournisseur());
		dto.setCodePostalFournisseur(entity.getTaFournisseur().getCodePostalFournisseur());
		dto.setVilleFournisseur(entity.getTaFournisseur().getVilleFournisseur());
		dto.setPaysFournisseur(entity.getTaFournisseur().getPaysFournisseur());

		
		dto.setVersionObj(entity.getTaFournisseur().getVersionObj());

		return dto;
	}

	
}
