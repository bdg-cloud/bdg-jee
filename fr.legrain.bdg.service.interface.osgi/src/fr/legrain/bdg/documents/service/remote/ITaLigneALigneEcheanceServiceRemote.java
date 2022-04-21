package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;

@Remote
public interface ITaLigneALigneEcheanceServiceRemote extends IGenericCRUDServiceRemote<TaLigneALigneEcheance,TaLigneALigneEcheanceDTO>,
IAbstractLgrDAOServer<TaLigneALigneEcheance>,IAbstractLgrDAOServerDTO<TaLigneALigneEcheanceDTO>,
IDocumentService<TaLigneALigneEcheance>, IDocumentTiersStatistiquesService<TaLigneALigneEcheance>,
IDocumentCodeGenere {
	
	public List<TaLigneALigneEcheance> dejaGenere(String requete);
	public List<TaLigneALigneEcheanceDTO> dejaGenereLigneDocument(String requete);
	public List<IDocumentTiers> dejaGenereDocument(String requete);
	public List<TaLigneALigneEcheance> selectAll(ILigneDocumentTiers taDocument, Date dateDeb, Date dateFin);
	public List<TaLigneALigneEcheance> findByLDocument(ILigneDocumentTiers ligneDocument);
	public List<TaLigneALigneEcheance> findByLDocument(TaLFlash ligneDocument);
//	public List<TaLigneALigneDTO> recupListeLienLigneALigneDTO(IDocumentTiers persistentInstance);
	public List<IDocumentDTO> recupIdDocumentSrc();
	
//	public List<TaLigneALigneEcheance> findAllByIdLDocumentAndTypeDoc(Integer id, String typeDoc);
//	public void removeAllByIdLDocumentAndTypeDoc(Integer id, String typeDoc);
//	public List<TaLigneALigneEcheance> findAllByIdEcheance(Integer id);
//	public void removeAllByIdEcheance(Integer id);
//	public void removeAllByIdDocumentAndTypeDoc(Integer id, String typeDoc);
//	public List<TaLigneALigneEcheance> findAllByIdDocumentAndTypeDoc(Integer id, String typeDoc);
	
	public List<TaLigneALigneEcheanceDTO> dejaGenereLEcheanceDocument(ILigneDocumentDTO ds );
	public List<TaLigneALigneEcheanceDTO> dejaGenereLEcheanceDocument(IDocumentDTO ds );
	public List<TaLEcheance> dejaGenereLEcheanceDocument(ILigneDocumentTiers ds );
	public List<TaLEcheance> dejaGenereLigneEcheance(String requete);
}
