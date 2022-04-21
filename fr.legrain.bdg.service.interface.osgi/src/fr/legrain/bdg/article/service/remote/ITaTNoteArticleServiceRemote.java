package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTNoteArticleDTO;
import fr.legrain.article.model.TaTNoteArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTNoteArticleServiceRemote extends IGenericCRUDServiceRemote<TaTNoteArticle,TaTNoteArticleDTO>,
														IAbstractLgrDAOServer<TaTNoteArticle>,IAbstractLgrDAOServerDTO<TaTNoteArticleDTO>{
	public static final String validationContext = "T_NOTE_ARTICLE";
}
