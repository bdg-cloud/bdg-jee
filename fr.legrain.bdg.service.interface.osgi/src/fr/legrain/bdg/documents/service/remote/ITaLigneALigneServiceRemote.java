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
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;

@Remote
public interface ITaLigneALigneServiceRemote extends IGenericCRUDServiceRemote<TaLigneALigne,TaLigneALigneDTO>,
IAbstractLgrDAOServer<TaLigneALigne>,IAbstractLgrDAOServerDTO<TaLigneALigneDTO>,
IDocumentService<TaLigneALigne>, IDocumentTiersStatistiquesService<TaLigneALigne>,
IDocumentCodeGenere {
	
	public List<TaLigneALigne> dejaGenere(String requete);
	public List<TaLigneALigneDTO> dejaGenereLigneDocument(String requete);
	public List<IDocumentTiers> dejaGenereDocument(String requete);
	public List<TaLigneALigne> selectAll(ILigneDocumentTiers taDocument, Date dateDeb, Date dateFin);
	public List<TaLigneALigne> findByLDocument(ILigneDocumentTiers ligneDocument);
	public List<TaLigneALigne> findByLDocument(TaLFlash ligneDocument);
//	public List<TaLigneALigneDTO> recupListeLienLigneALigneDTO(IDocumentTiers persistentInstance);
	public List<IDocumentDTO> recupIdDocumentSrc();
}
