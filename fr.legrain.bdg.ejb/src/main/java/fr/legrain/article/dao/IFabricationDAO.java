package fr.legrain.article.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.dto.TaFabricationDTO;
//import fr.legrain.article.model.TaArticle;
//import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IFabricationDAO extends IGenericDAO<TaFabrication> {
	
	public List<TaFabricationDTO> findAllLight();
	public List<TaFabricationDTO> findByCodeLight(String code);
	public List<TaFabricationDTO> findByDateLight(Date debut, Date fin);

	//-ajouté par Dima - Début
	public List<TaFabrication> findWithCodeArticle(String codeArticle, Date dateDebut, Date dateFin);
	public List<TaLFabricationMP> findWithMatPremNumLot(String numLot, Date dateDebut, Date dateFin);
	public List<TaLFabricationMP> findWithMatPremLibelle(String nomProdtuit, Date dateDebut, Date dateFin);
	public List<TaLFabricationPF> findWithProdFinNumLot(String numLot, Date dateDebut, Date dateFin);
	public List<TaLFabricationPF> findWithProdFinLibelle(String nomProdtuit, Date dateDebut, Date dateFin);
	public List<TaFabrication> findWithNumFab(String codeFabrication, Date dateDebut, Date dateFin);
	
	public List<TaFabrication> findWithCodeArticleNoDate(String codeArticle);
	public List<TaFabrication> findWithMatPremNumLotNoDate(String numLot);
	public List<TaFabrication> findWithMatPremLibelleNoDate(String nomProdtuit);
	public List<TaFabrication> findWithProdFinNumLotNoDate(String numLot);
	public List<TaFabrication> findWithProdFinLibelleNoDate(String nomProdtuit);
	public List<TaFabrication> findWithNumFabNoDate(String codeFabrication);
	public TaFabrication findAvecResultatConformites(int idf);
}
