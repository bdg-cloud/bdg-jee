package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;

//@Remote
public interface ILigneALigneDAO extends IGenericDAO<TaLigneALigne> {

	public List<TaLigneALigne> dejaGenere(String requete);
	public List<TaLigneALigneDTO> dejaGenereLigneDocument(String requete);
	public List<IDocumentTiers> dejaGenereDocument(String requete);
	List<TaLigneALigne> selectAll(ILigneDocumentTiers taLDocument, Date dateDeb, Date dateFin);
	public List<TaLigneALigne> findByLDocument(ILigneDocumentTiers ligneDocument);
	public List<TaLigneALigne> findByLDocument(TaLFlash ligneDocument) ;
//	public List<TaLigneALigneDTO> recupListeLienLigneALigneInt(IDocumentTiers persistentInstance);
	public List<IDocumentDTO> recupIdDocumentSrc();
	
}
