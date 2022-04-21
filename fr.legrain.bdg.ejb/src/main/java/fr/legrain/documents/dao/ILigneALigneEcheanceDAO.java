package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigneEcheance;

//@Remote
public interface ILigneALigneEcheanceDAO extends IGenericDAO<TaLigneALigneEcheance> {

	public List<TaLigneALigneEcheance> dejaGenere(String requete);
	public List<TaLigneALigneEcheanceDTO> dejaGenereLigneDocument(String requete);
	public List<IDocumentTiers> dejaGenereDocument(String requete);
	List<TaLigneALigneEcheance> selectAll(ILigneDocumentTiers taLDocument, Date dateDeb, Date dateFin);
	public List<TaLigneALigneEcheance> findByLDocument(ILigneDocumentTiers ligneDocument);
	public List<TaLigneALigneEcheance> findByLDocument(TaLFlash ligneDocument) ;
//	public List<TaLigneALigneEcheanceDTO> recupListeLienLigneALigneInt(IDocumentTiers persistentInstance);
	public List<IDocumentDTO> recupIdDocumentSrc();
	
	public List<TaLigneALigneEcheance> findAllByIdLDocumentAndTypeDoc(Integer id, String typeDoc);
	public List<TaLigneALigneEcheance> findAllByIdEcheance(Integer id);
	public List<TaLigneALigneEcheance> findAllByIdDocumentAndTypeDoc(Integer id, String typeDoc);
	public List<TaLEcheance> dejaGenereLigneEcheance(String requete);
	
}
