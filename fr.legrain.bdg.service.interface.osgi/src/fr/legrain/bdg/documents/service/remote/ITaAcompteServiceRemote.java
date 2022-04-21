package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.edition.model.TaEdition;

@Remote
public interface ITaAcompteServiceRemote extends IGenericCRUDServiceDocumentRemote<TaAcompte,TaAcompteDTO>,
IDocumentLigneALigneService<TaAcompte>,
														IAbstractLgrDAOServer<TaAcompte>,IAbstractLgrDAOServerDTO<TaAcompteDTO>,
														IDocumentService<TaAcompte>, IDocumentTiersStatistiquesService<TaAcompte>,IDashboardDocumentServiceRemote{
	
	public static final String validationContext = "ACOMPTE";
	
	public List<TaAcompte> selectAllDisponible(TaFacture taFacture);
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public TaAcompte mergeAndFindById(TaAcompte detachedInstance, String validationContext);
	
	public Date calculDateEcheanceAbstract(TaAcompte doc, Integer report, Integer finDeMois);
	
	public Date calculDateEcheance(TaAcompte doc, Integer report, Integer finDeMois);
	
	public List<TaAcompteDTO> findAllLight();
	public List<TaAcompte> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaAcompte> rechercheAcompteNonRemises(String codeTiers,Date dateDeb,Date DateFin,Boolean export,String codeTPaiement,String iban,boolean byDate);
	public List<TaAcompteDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	
	public String writingFileEdition(TaEdition taEdition);
	
//	public long countAcompte(Date debut, Date fin);	
//	public long countAcompteNonTransforme(Date debut, Date fin);
//	public long countAcompteNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countAcompteTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caAcompteNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
////	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeARelancerJmaDTO(Date debut, Date fin, int precision);
	
	public TaReglement creeRReglement(TaAcompte doc,String typePaiementDefaut,TaReglement reglement) throws Exception;

	public TaEtat etatLigneInsertion(TaAcompte masterEntity);

	public TaAcompte mergeEtat(IDocumentTiers detachedInstance, List<IDocumentTiers> avecLien);
	
	
}

