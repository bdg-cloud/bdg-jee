package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaMouvementStock;


public class TaMouvementStockMapper implements ILgrMapper<MouvementStocksDTO , TaMouvementStock> {

	
	@Override
	public TaMouvementStock mapDtoToEntity(MouvementStocksDTO dto, TaMouvementStock entity) {
		if(entity==null)
			entity = new TaMouvementStock();

		entity.setIdMouvementStock(dto.getId()!=null?dto.getId():0);
		
		entity.setDateStock(dto.getDateStock());
		entity.setLibelleStock(dto.getLibelleStock());
		entity.setDescription(dto.getDescription());
		entity.setEmplacement(dto.getEmplacement());
//		entity.setTypeDoc(dto.getTypeDoc());
//		entity.setIdDocument(dto.getIdDocument());
		
		entity.setQte1Stock(dto.getQte1Stock());		
		entity.setQte2Stock(dto.getQte2Stock());
		entity.setUn1Stock(dto.getUn1Stock());
		entity.setUn2Stock(dto.getUn2Stock());

//		entity.setNumLot(dto.getNumLot());
		

		
		if(dto.getNumLot()!=null) {
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public MouvementStocksDTO mapEntityToDto(TaMouvementStock entity, MouvementStocksDTO dto) {
		if(dto==null)
			dto = new MouvementStocksDTO();

		dto.setId(entity.getIdMouvementStock());
		if(entity.getTaGrMouvStock()!=null){
		  dto.setCodeGrMouvStock(entity.getTaGrMouvStock().getCodeGrMouvStock());
		  dto.setIdGrMouvStock(entity.getTaGrMouvStock().getIdGrMouvStock());
		}
		dto.setDateStock(entity.getDateStock());
		dto.setLibelleStock(entity.getLibelleStock());
		dto.setDescription(entity.getDescription());
		if(entity.getQte1Stock()!=null)dto.setQte1Stock(entity.getQte1Stock().abs());		
		dto.setUn1Stock(entity.getUn1Stock());
		if(entity.getQte2Stock()!=null)dto.setQte2Stock(entity.getQte2Stock().abs());
		dto.setUn2Stock(entity.getUn2Stock());	

//		dto.setTypeDoc(entity.getTypeDoc());
//		dto.setIdDocument(entity.getIdDocument());
//		if(entity.getNumLot()!=null){
//			
//		}

		if(entity.getTaLot()!=null){
			TaLot lot=entity.getTaLot();
			dto.setIdLot(lot.getIdLot());
			dto.setNumLot(lot.getNumLot());
			if(lot.getTaArticle()!=null){
				TaArticle taArticle=lot.getTaArticle();
				dto.setIdArticle(taArticle.getIdArticle());
				dto.setCodeArticle(taArticle.getCodeArticle());
			}
		}
		
		if(entity.getTaEntrepot()!=null){
			dto.setIdEntrepot(entity.getTaEntrepot().getIdEntrepot());
			dto.setCodeEntrepot(entity.getTaEntrepot().getCodeEntrepot());			
		}
		
		if(entity.getTaEntrepotDest()!=null){
			//récupérer le numéro de la la ligne qui est liée à cette enregistrement dont le lettrageDeplacement correspond
			//pour tout afficher sur même ligne
			dto.setCodeEntrepotDest(entity.getTaEntrepotDest().getCodeEntrepot());
			dto.setEmplacementDest(entity.getEmplacementDest());
		}
				
		dto.setEmplacement(entity.getEmplacement());


		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
