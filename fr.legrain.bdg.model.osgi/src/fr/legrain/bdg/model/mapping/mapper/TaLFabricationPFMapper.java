package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLFabricationDTO;



public class TaLFabricationPFMapper implements ILgrMapper<TaLFabricationDTO, TaLFabricationPF> {

	@Override
	public TaLFabricationPF mapDtoToEntity(TaLFabricationDTO dto, TaLFabricationPF entity) {
		if(entity==null) {
			//entity = new TaLFabrication();
			try {
				throw new Exception("entite nulle et classe abstraite à gérer");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLFabricationDTO mapEntityToDto(TaLFabricationPF entity, TaLFabricationDTO dto) {
		if(dto==null)
			dto = new TaLFabricationDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
