package fr.legrain.bdg.synchronisation.dossier.service.remote;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.article.model.TaArticle;

@Remote
public interface ISynchronisationDossierServiceRemote {
	
	public List<TaArticle> listeArticle(String codeDossierSource) throws EJBException;
	
	public List<TaArticle> synchronisationArticle(String codeDossierSource) throws EJBException;
}
