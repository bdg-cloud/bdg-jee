package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.stock.dto.InventaireDTO;
import fr.legrain.stock.model.TaInventaire;


public class TaInventaireMapper implements ILgrMapper<InventaireDTO, TaInventaire> {

	@Override
	public TaInventaire mapDtoToEntity(InventaireDTO dto, TaInventaire entity) {
		if(entity==null)
			entity = new TaInventaire();

		entity.setIdInventaire(dto.getId()!=null?dto.getId():0);
		entity.setCodeInventaire(dto.getCodeInventaire());
		entity.setCommentaire(dto.getCommentaire());
		entity.setDateInventaire(dto.getDateInventaire());
		entity.setLibelleInventaire(dto.getLibelleInventaire());
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public InventaireDTO mapEntityToDto(TaInventaire entity, InventaireDTO dto) {
		if(dto==null)
			dto = new InventaireDTO();

		dto.setId(entity.getIdInventaire());
		dto.setCodeInventaire(entity.getCodeInventaire());
		dto.setCommentaire(entity.getCommentaire());
		dto.setLibelleInventaire(entity.getLibelleInventaire());
		dto.setDateInventaire(entity.getDateInventaire());
		if(entity.getTaEtat()!=null){
			dto.setCodeEtat(entity.getTaEtat().getCodeEtat());
			dto.setIdEtat(entity.getTaEtat().getIdEtat());			
		}
		if(entity.getTaEntrepot()!=null){
			dto.setCodeEntrepot(entity.getTaEntrepot().getCodeEntrepot());
			dto.setIdEntrepot(entity.getTaEntrepot().getIdEntrepot());			
		}
		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
