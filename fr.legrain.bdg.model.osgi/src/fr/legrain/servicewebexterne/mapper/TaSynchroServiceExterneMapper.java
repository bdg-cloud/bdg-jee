package fr.legrain.servicewebexterne.mapper;



import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaSynchroServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;




public class TaSynchroServiceExterneMapper implements ILgrMapper<TaSynchroServiceExterneDTO, TaSynchroServiceExterne> {

	@Override
	public TaSynchroServiceExterne mapDtoToEntity(TaSynchroServiceExterneDTO dto, TaSynchroServiceExterne entity) {
		if(entity==null)
			entity = new TaSynchroServiceExterne();
		if(dto.getId() != null) {
			entity.setIdSynchroServiceExterne(dto.getId());
		}
		entity.setDerniereSynchro(dto.getDerniereSynchro());
		entity.setTypeEntite(dto.getTypeEntite());
		return entity;
		
	}

	@Override
	public TaSynchroServiceExterneDTO mapEntityToDto(TaSynchroServiceExterne entity, TaSynchroServiceExterneDTO dto) {
		if(dto==null)
			dto = new TaSynchroServiceExterneDTO();
		
		dto.setId(entity.getIdSynchroServiceExterne());
		if(entity.getTaCompteServiceWebExterne() != null) {
			dto.setIdCompteServiceExterne(entity.getTaCompteServiceWebExterne().getIdCompteServiceWebExterne());
			dto.setCodeCompteServiceExterne(entity.getTaCompteServiceWebExterne().getCodeCompteServiceWebExterne());
			dto.setLibelleCompteServiceExterne(entity.getTaCompteServiceWebExterne().getLibelleCompteServiceWebExterne());
		}
		
		
		dto.setDerniereSynchro(entity.getDerniereSynchro());
		dto.setTypeEntite(entity.getTypeEntite());
		
		return dto;
		
	}

}
