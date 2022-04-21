package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaRefArticleFournisseurDTO;
import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaRefArticleFournisseurServiceRemote extends IGenericCRUDServiceRemote<TaRefArticleFournisseur,TaRefArticleFournisseurDTO>,
														IAbstractLgrDAOServer<TaRefArticleFournisseur>,IAbstractLgrDAOServerDTO<TaRefArticleFournisseurDTO>{
	public static final String validationContext = "REF_ARTICLE_FOURNISSEUR";
	
	public TaRefArticleFournisseurDTO findByCodeFournisseurAndCodeBarreLight(String codeBarre , String codeFournisseur);
	
	public List<TaRefArticleFournisseurDTO> findByCodeFournisseurLight( String codeFournisseur) ;
	
	public TaRefArticleFournisseur findByCodeFournisseurAndCodeBarre(String codeBarre , String codeFournisseur);	
	
	public List<TaRefArticleFournisseur> findByCodeFournisseur( String codeFournisseur) ;
}
