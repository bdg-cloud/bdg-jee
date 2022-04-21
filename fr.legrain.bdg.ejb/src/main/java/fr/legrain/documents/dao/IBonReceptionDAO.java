package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.dto.TaTiersDTO;

//@Remote
public interface IBonReceptionDAO extends IGenericDAO<TaBonReception>, IDocumentDAO<TaBonReception>,IDocumentTiersStatistiquesDAO<TaBonReception>,IDocumentTiersEtatDAO<TaBonReception> {
	public String genereCode();
	
	public List<TaBonReceptionDTO> findAllLight();
	public List<TaBonReceptionDTO> findByCodeLight(String code);
	
	// Dima - Début
	public List<TaLBonReception> bonRecepFindByCodeArticle(String codeArticle);
	public List<TaLBonReception> bonRecepFindByLotParDate(String numLot , Date dateDeb, Date dateFin);
	public List<TaLBonReception> bonRecepFindByCodeArticleParDate(String codeArticle , Date dateDeb, Date dateFin);
	public List<TaLBonReceptionDTO> bonRecepFindByCodeFournisseurParDate(String codeFournisseur , Date dateDeb, Date dateFin);
	public List<TaTiersDTO> findTiersDTOByCodeArticleAndDate(String codeArticle, Date debut,Date fin);
	
	public TaBonReception findAvecResultatConformites(int idBr) ;
//	public TaBonReception findDocByLDoc(ILigneDocumentTiers lDoc);
	// Dima -  Fin
}
