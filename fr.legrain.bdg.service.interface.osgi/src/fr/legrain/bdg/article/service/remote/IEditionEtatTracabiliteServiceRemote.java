package fr.legrain.bdg.article.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.EditionEtatTracabiliteDTO;
import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

/** Créé par Dima **/

@Remote
public interface IEditionEtatTracabiliteServiceRemote extends IGenericCRUDServiceRemote<EditionEtatTracabilite,EditionEtatTracabiliteDTO>,
										IAbstractLgrDAOServer<EditionEtatTracabilite>,IAbstractLgrDAOServerDTO<EditionEtatTracabiliteDTO> {

	public static final String validationContext = "EDITIONS";

	//public EditionEtatTracabilite findByCodeWithList(String code);
	
	public String genereCode();
	
	public List<EditionEtatTracabilite> editionPFNL(String numLotPF);
	public List<EditionEtatTracabilite> editionPFL(String libellePF);
	public List<EditionEtatTracabilite> editionCA(String codeArticle);
	public List<EditionEtatTracabilite> editionSG(Date dateD, Date dateF, String codeArticle,String familleArticle,String codeEntrepot);
	public List<EditionEtatTracabilite> editionDMA(String codeArticle, Date dateDebut, Date dateFin);
	public List<EditionEtatTracabilite> editionDML(String mouvementLot, Date dateDebut, Date dateFin);
	public List<EditionEtatTracabilite> editionNonConforme(Date dateD, Date dateF);
	public List<EditionEtatTracabilite> editionCADate(String codeArticle, Date dateDebut, Date dateFin);
	public List <EditionEtatTracabilite> editionDMAtous(String codeArticle, Date dateD, Date dateF);
	
}
