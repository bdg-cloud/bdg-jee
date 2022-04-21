package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaReglementServiceRemote extends IGenericCRUDServiceRemote<TaReglement,TaReglementDTO>,
														IAbstractLgrDAOServer<TaReglement>,IAbstractLgrDAOServerDTO<TaReglementDTO>,
														IDocumentService<TaReglement>,IDashboardDocumentServiceRemote{
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	public List<TaReglement> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaReglement> rechercheReglementNonRemises(String codeTiers,Date dateDeb,Date DateFin,Boolean export,String codeTPaiement,String ibanBancaire,boolean byDate,int nbLigneMax);
	public List<TaReglementDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin, boolean parDate);
	public List<TaReglementDTO> selectReglementNonLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin);
	public List<TaReglementDTO> selectAllLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin);
	public TaReglement findByCodeAcompte(String code);
	public TaReglement findByCodePrelevement(String code);
	public int selectCountDisponible(TaTiers taTiers) ;
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,String codeTiers);
	public List<TaReglementDTO> rechercheDocumentDTO(String codeTiers);
	
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin , String codeTiers);
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin);
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin);
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin);
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);
	public List<TaReglementDTO> rechercheDocumentDTO(String codeDeb, String codeFin);
	public List<TaReglementDTO> rechercheReglementNonRemisesDTO(String codeTiers, Date dateDeb, Date DateFin, Boolean export,
			String codeTPaiement, String ibanBancaire, boolean byDate, int nbLigneMax);
	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
	
}
