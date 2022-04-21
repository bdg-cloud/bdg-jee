package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;

import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.model.TaBonReception;

public interface IDocumentService<T> {
	
//	public List<IDocumentTiers> dejaGenereDocument(IDocumentDTO ds );
	
	public List<T> rechercheDocument(Date dateDeb, Date dateFin);
	
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light);
	
	public List<T> rechercheDocument(String codeDeb, String codeFin);
	
	public List<T> rechercheDocument(Date dateDeb, Date dateFin,String codeTiers);
	
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,String codeTiers);
	
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers);
	
	public List<T> rechercheDocument(String codeDeb, String codeFin,String codeTiers);
	
	public List<T> rechercheDocument(Date dateDeb, Date dateFin,String codeTiers,Boolean export);
	
	public List<T> rechercheDocument(String codeTiers,Date dateExport,Date dateDeb, Date dateFin);
	
	public List<T> rechercheDocumentVerrouille(Date dateDeb, Date dateFin,String codeTiers,Boolean verrouille);
	
	public List<T> rechercheDocumentVerrouille(String codeDeb, String codeFin,String codeTiers,Boolean verrouille);
	
	public List<T> rechercheDocument(String codeDeb, String codeFin,String codeTiers,Boolean export);

	public T findByIdDocument(int id) throws FinderException;
	
	public List<T> selectAll(IDocumentTiers taDocument,Date dateDeb,Date dateFin);

	public int findDocByLDocDTO(ILigneDocumentTiers lDoc);


	

//	public T findDocByLDoc(ILigneDocumentTiers lDoc);
//	public T mergeEtat(IDocumentTiers detachedInstance);
	
}
