package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.edition.model.TaEdition;

@Remote
public interface ITaApporteurServiceRemote extends IGenericCRUDServiceDocumentRemote<TaApporteur,TaApporteurDTO>,
IDocumentLigneALigneService<TaApporteur>,
														IAbstractLgrDAOServer<TaApporteur>,IAbstractLgrDAOServerDTO<TaApporteurDTO>,
														IDocumentService<TaApporteur>, IDocumentTiersStatistiquesService<TaApporteur>,
														IDocumentCodeGenere, IDashboardDocumentServiceRemote{
	
	public static final String validationContext = "APPORTEUR";
	
	public TaApporteur mergeAndFindById(TaApporteur detachedInstance, String validationContext);
	
	public Date calculDateEcheance(TaApporteur doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	public Date calculDateEcheanceAbstract(TaApporteur doc, Integer report, Integer finDeMois);
	public List<TaApporteur> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaApporteur> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaApporteurDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	public List<TaApporteurDTO> findAllLight() ;
	
	public String writingFileEdition(TaEdition taEdition);

	public List<TaApporteurDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);

	public List<TaApporteurDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);

	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
	

	public TaEtat etatLigneInsertion(TaApporteur masterEntity);

	public TaApporteur mergeEtat(IDocumentTiers detachedInstance, List<IDocumentTiers> avecLien);
}
