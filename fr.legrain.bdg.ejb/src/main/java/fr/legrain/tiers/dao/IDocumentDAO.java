package fr.legrain.tiers.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaTicketDeCaisse;

public interface IDocumentDAO<T> {
	
	public List<T> rechercheDocument(Date dateDeb, Date dateFin);
	
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light);
	
	public List<T> rechercheDocument(String codeDeb, String codeFin);
	
	public List<T> rechercheDocument(Date dateDeb, Date dateFin,String codeTiers);
	
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,String codeTiers);
	
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers);
	
	public List<T> rechercheDocument(String codeDeb, String codeFin,String codeTiers);
	
	public List<T> rechercheDocument(Date dateDeb, Date dateFin,String codeTiers,Boolean export);
	
	public List<T> rechercheDocument(String codeDeb, String codeFin,String codeTiers,Boolean export);

	public T findById(int id) ;
	
	public List<T> selectAll(IDocumentTiers taDocument,Date dateDeb,Date dateFin);

	public List<T> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers, Boolean verrouille);

	public List<T> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers, Boolean verrouille);

	public List<Date> findDateExport(Date DateDeb,Date DateFin);

	public List<T> rechercheDocument( Date dateExport, String codeTiers,Date dateDeb, Date dateFin);

	public IDocumentTiers findDocByLDoc(ILigneDocumentTiers lDoc);

	public int findDocByLDocDTO(ILigneDocumentTiers lDoc);

	public IDocumentTiers findByCodeFetch(String code);

	public IDocumentTiers findByIdFetch(int id);


	
}
