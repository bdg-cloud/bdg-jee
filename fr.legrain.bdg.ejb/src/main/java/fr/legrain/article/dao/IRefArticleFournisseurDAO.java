package fr.legrain.article.dao;

import java.util.List;

import javax.persistence.Query;

import fr.legrain.article.dto.TaRefArticleFournisseurDTO;
import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IRefArticleFournisseurDAO extends IGenericDAO<TaRefArticleFournisseur> {
	
	public TaRefArticleFournisseurDTO findByCodeFournisseurAndCodeBarreLight(String codeBarre , String codeFournisseur);
	
	public List<TaRefArticleFournisseurDTO> findByCodeFournisseurLight( String codeFournisseur) ;
	
	public TaRefArticleFournisseur findByCodeFournisseurAndCodeBarre(String codeBarre , String codeFournisseur);	
	
	public List<TaRefArticleFournisseur> findByCodeFournisseur( String codeFournisseur) ;

}
