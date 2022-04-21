package fr.legrain.documents.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;

//@Remote
public interface ITicketDeCaisseDAO extends IGenericDAO<TaTicketDeCaisse>,IDocumentDAO<TaTicketDeCaisse>,IDocumentTiersStatistiquesDAO<TaTicketDeCaisse>,
IDocumentTiersEtatDAO<TaTicketDeCaisse>,IDashboardDocumentPayableDAO {
	
	
	public List<TaTicketDeCaisseDTO> findAllLight();
	
	public List<TaTicketDeCaisse> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaTicketDeCaisse> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	public List<Object> findChiffreAffaireTotal(Date debut, Date fin, int precision);
	
	public List<TaTicketDeCaisse> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);	
	public List<TaTicketDeCaisseDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin) ;
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin,String tiers,String document);
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegleAEcheance(Date dateDeb, Date dateFin,String tiers,String document,BigDecimal limite);
	public List<Object[]> rechercheDocumentNonTotalementRegleAEcheance2(Date dateDeb, Date dateFin,String tiers,String document) ;
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegleReel(Date dateDeb, Date dateFin);
	
	public List<TaTicketDeCaisse> findBySessionCaisseCourante();

	//A remonter et à implémenter dans les autres documents
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeTiersDTO(Date dateDebut, Date dateFin, String codeTiers);
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeArticleDTO(Date dateDebut, Date dateFin, String codeArticle);
	//-----------------------------------------------------//
	
	
	//specifique aux factures
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin , String codeTiers);


}
