package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import fr.legrain.document.dashboard.dto.CountDocumentParTypeRegrouptement;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.TaFactureDTO;

//@Remote
public interface IDashboardDocumentPayableDAO extends IDashboardDocumentDAO {


//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin,int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement);
//	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement);
//	
//	//liés aux états
//	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement) ;
//	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,Object codeEtat,String typeRegroupement,Object valeurRegroupement) ;
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement,boolean regrouper);

	
	
//	public List<DocumentDTO> findDocumentNonTransfosARelancerDTOParTypeRegroupement(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers,String typeRegroupement,Object valeurRegroupement) ;
//
//	public List<DocumentDTO> findDocumentNonTransfosDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement) ;
//
//	public List<DocumentDTO> findDocumentTransfosDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement) ;

//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeParTypeRegroupement(Date dateDebut, Date dateFin,String typeRegroupement,Object valeurRegroupement) ;

//	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement) ;

//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancerParTypeRegroupement(Date debut, Date fin, int deltaNbJours,String typeRegroupement,Object valeurRegroupement);

//	public List<TaArticlesParTiersDTO> findArticlesParTiersTransformeParTypeRegroupement(Date debut, Date fin,String typeRegroupement,Object valeurRegroupement);

//	public List<DocumentChiffreAffaireDTO> countDocumentParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) ;
//	
//	public List<TaFactureDTO> getFactureAllDTOPeriode(Date from, Date to);
//	
	
//	public List<DocumentChiffreAffaireDTO> chiffreAffaireParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement);
//	public List<DocumentDTO> findAllDTOPeriodeParTypeRegroupement(Date dateDebut, Date dateFin, String codeTiers,String typeRegroupement, Object valeurRegroupement);

}
