package fr.legrain.article.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.data.IGenericDAO;

/** Créé par Dima **/

public interface IEditionEtatTracabiliteDAO extends IGenericDAO<EditionEtatTracabilite> {
	
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
