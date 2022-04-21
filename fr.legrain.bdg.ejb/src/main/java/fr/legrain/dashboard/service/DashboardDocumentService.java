// ne plus utiliser et à supprimer dès que possible !!!!

package fr.legrain.dashboard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class DashboardDocumentService implements IDashboardDocumentServiceRemote {

	protected @EJB ITaFactureServiceRemote taFactureService;
	protected @EJB ITaDevisServiceRemote taDevisService;
	protected @EJB ITaAvoirServiceRemote taAvoirService;
	protected @EJB ITaPrelevementServiceRemote taPrelevementService;
	protected @EJB ITaProformaServiceRemote taProformaService;
	protected @EJB ITaBoncdeServiceRemote taBoncdeService;
	protected @EJB ITaBonlivServiceRemote taBonlivService;
	protected @EJB ITaAcompteServiceRemote taAcompteService;
	
    // Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision) {
		List<DocumentChiffreAffaireDTO> list;
		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			list = taFactureService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
		

//		case TaDevis.TYPE_DOC:
//			list = taDevisService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaPrelevement.TYPE_DOC:
//			list = taPrelevementService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaProforma.TYPE_DOC:
//			list = taProformaService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaBoncde.TYPE_DOC:
//			list = taBoncdeService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaBonliv.TYPE_DOC:
//			list = taBonlivService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaAcompte.TYPE_DOC:
//			list = taAcompteService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
		default:
			break;
		}
		
    	
    	// penser à créer une exception
		return null;
    	
    }

	// Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision) {
		List<DocumentChiffreAffaireDTO> list;
		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			list = taFactureService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
		

//		case TaDevis.TYPE_DOC:
//			list = taDevisService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaPrelevement.TYPE_DOC:
//			list = taPrelevementService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaProforma.TYPE_DOC:
//			list = taProformaService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaBoncde.TYPE_DOC:
//			list = taBoncdeService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaBonliv.TYPE_DOC:
//			list = taBonlivService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaAcompte.TYPE_DOC:
//			list = taAcompteService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
		default:
			break;
		}
		
    	
    	// penser à créer une exception
		return null;
    	
    }

	// Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision) {
		List<DocumentChiffreAffaireDTO> list;
		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			list = taFactureService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
		

//		case TaDevis.TYPE_DOC:
//			list = taDevisService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaPrelevement.TYPE_DOC:
//			list = taPrelevementService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaProforma.TYPE_DOC:
//			list = taProformaService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaBoncde.TYPE_DOC:
//			list = taBoncdeService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaBonliv.TYPE_DOC:
//			list = taBonlivService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaAcompte.TYPE_DOC:
//			list = taAcompteService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
		default:
			break;
		}
		
    	
    	// penser à créer une exception
		return null;
    	
    }
	
	// Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeARelancerJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision, int deltaNbJours) {
		List<DocumentChiffreAffaireDTO> list;
		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			list = taFactureService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//			return list;
		
//
//		case TaDevis.TYPE_DOC:
//			list = taDevisService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaPrelevement.TYPE_DOC:
//			list = taPrelevementService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaProforma.TYPE_DOC:
//			list = taProformaService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision, deltaNbJours);
//			return list;
			
//		case TaBoncde.TYPE_DOC:
//			list = taBoncdeService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaBonliv.TYPE_DOC:
//			list = taBonlivService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
//		case TaAcompte.TYPE_DOC:
//			list = taAcompteService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//			return list;
			
		default:
			break;
		}
		
    	
    	// penser à créer une exception
		return null;
    	
    }

    // Retourne les CA HT, TVA et TTC pour un type de document , en fonction de la période
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDocument(String nomTypeDocument, Date dateDebut, Date dateFin) {
		List<DocumentChiffreAffaireDTO> listeVide;
		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			return taFactureService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//
//		case TaDevis.TYPE_DOC:
//			return taDevisService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//			
//		case TaAvoir.TYPE_DOC:
//			return taAvoirService.findChiffreAffaireTotalDTO(dateDebut, dateFin);			
//
//		case TaPrelevement.TYPE_DOC:
//			return taPrelevementService.findChiffreAffaireTotalDTO(dateDebut, dateFin);			
					
//		case TaProforma.TYPE_DOC:
//			return taProformaService.chiffreAffaireTotalDTO(dateDebut, dateFin);
			
//		case TaBoncde.TYPE_DOC:
//			return taBoncdeService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//		
//		case TaBonliv.TYPE_DOC:
//			return taBonlivService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
			
//		case TaAcompte.TYPE_DOC:
//			return taAcompteService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
			
		default:
			break;
		}
		
    	
    	// penser à créer une exception
		return listeVide = new ArrayList<DocumentChiffreAffaireDTO>();
    	
    }

	 // Retourne les CA HT, TVA et TTC pour un type de document , en fonction de la période
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTODocument(String nomTypeDocument, Date dateDebut, Date dateFin) {
		List<DocumentChiffreAffaireDTO> listeVide;
		
		
		switch (nomTypeDocument) {
		case TaFacture.TYPE_DOC:
			
			break;

//		case TaDevis.TYPE_DOC:
//			listeVide = taDevisService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			

//		case TaPrelevement.TYPE_DOC:
//			listeVide = taPrelevementService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
		
//		case TaProforma.TYPE_DOC:
//			listeVide = taProformaService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
//		case TaBoncde.TYPE_DOC:
//			listeVide = taBoncdeService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			
//		case TaBonliv.TYPE_DOC:
//			listeVide = taBonlivService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
//		case TaAcompte.TYPE_DOC:
//			listeVide = taAcompteService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
		default:
			break;
		}
		  				
		// penser à créer une exception
		return listeVide = new ArrayList<DocumentChiffreAffaireDTO>();
   	
   }

	// Retourne les CA HT, TVA et TTC pour un type de document , en fonction de la période
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTODocument(String nomTypeDocument, Date dateDebut, Date dateFin) {
		List<DocumentChiffreAffaireDTO> listeVide;
		
		
		switch (nomTypeDocument) {
		case TaFacture.TYPE_DOC:
			
			break;

//		case TaDevis.TYPE_DOC:
//			listeVide = taDevisService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			

//		case TaPrelevement.TYPE_DOC:
//			listeVide = taPrelevementService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
		
//		case TaProforma.TYPE_DOC:
//			listeVide = taProformaService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
//		case TaBoncde.TYPE_DOC:
//			listeVide = taBoncdeService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
//		case TaBonliv.TYPE_DOC:
//			listeVide = taBonlivService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
//		case TaAcompte.TYPE_DOC:
//			listeVide = taAcompteService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
			
		default:
			break;
		}
		
    	
		
		
		// penser à créer une exception
		return listeVide = new ArrayList<DocumentChiffreAffaireDTO>();
    	
    }

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.listeChiffreAffaireTotalDTO()");
		return null;
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.chiffreAffaireTotalDTO()");
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut,
			Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.listeChiffreAffaireNonTransformeTotalDTO()");
		return null;
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.chiffreAffaireNonTransformeTotalDTO()");
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.chiffreAffaireTransformeTotalDTO()");
		return null;
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.chiffreAffaireTransformeTotalDTO()");
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin,
			int deltaNbJours,String codeTiers) {
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.listeChiffreAffaireNonTransfosARelancerDTO");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin,
			int deltaNbJours,String codeTiers) {
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.chiffreAffaireNonTransformeARelancerTotalDTO");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countDocument(Date dateDebut, Date dateFin,String codeTiers) {
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.countDocument");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long countDocumentTransforme(Date dateDebut, Date dateFin,String codeTiers) {
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.countDocumentTransforme");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long countDocumentNonTransforme(Date dateDebut, Date dateFin,String codeTiers) {
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.countDocumentNonTransforme");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long countDocumentNonTransformeARelancer(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.countDocumentNonTransformeARelancer");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.countDocumentNonTransformeARelancer");
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.listeChiffreAffaireNonTransformeJmaDTO");
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.listeChiffreAffaireTransformeJmaDTO");
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
			int precision, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		System.out.println("NE DOIT PAS PASSER PAR ICI !!!!!! DashboardDocumentService.listeChiffreAffaireNonTransformeARelancerJmaDTO");
		return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<DocumentDTO> findAllDTOPeriodeSimple(Date dateDebut, Date dateFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut,
			Date dateFin, String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement,
			boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
			int deltaNbJours, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regroupee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
			int deltaNbJours) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle){
		return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOIntervalle(String codeDebut, String codeFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin, String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin, String codeTiers,
			String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers,
			String codeArticle, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, String orderBy, int deltaNbJours) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisAvecEtat(Date debut, Date fin, String codeTiers,
			String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return null;
	}





//	@Override
//	public long countDocument(Date debut, Date fin, String codeTiers, String codeArticle) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

}
