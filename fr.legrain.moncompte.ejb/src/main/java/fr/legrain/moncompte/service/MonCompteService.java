package fr.legrain.moncompte.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.moncompte.service.remote.IMonCompteServiceFacadeRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaCategorieProServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaCgPartenaireServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaCgvServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaCommissionServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaDossierServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaLimitationServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaPrixParUtilisateurServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaSetupServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaTNiveauServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaTypePaiementServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaTypePartenaireServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaTypeProduitServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.lib.data.LibDate;
import fr.legrain.moncompte.dao.IClientDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaClientDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaCategoriePro;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.moncompte.model.TaCgv;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaLigneCommission;
import fr.legrain.moncompte.model.TaLignePanier;
import fr.legrain.moncompte.model.TaLimitation;
import fr.legrain.moncompte.model.TaPanier;
import fr.legrain.moncompte.model.TaPartenaire;
import fr.legrain.moncompte.model.TaPrixParUtilisateur;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.TaTNiveau;
import fr.legrain.moncompte.model.TaTypePaiement;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.moncompte.model.mapping.mapper.TaClientMapper;


/**
 * Session Bean implementation class TaClientBean
 */
@Stateless
@WebService
@DeclareRoles("admin")
public class MonCompteService implements IMonCompteServiceFacadeRemote {

	static Logger logger = Logger.getLogger(MonCompteService.class);

	//@Inject private IClientDAO dao;

	@EJB private ITaClientServiceRemote taClientService;
	@EJB private ITaProduitServiceRemote taProduitService;
	@EJB private ITaSetupServiceRemote taSetupService;
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB private ITaDossierServiceRemote taDossierService;
	@EJB private ITaPanierServiceRemote taPanierService;
	@EJB private ITaPrixParUtilisateurServiceRemote taPrixParUtilisateurService;
	@EJB private ITaLimitationServiceRemote taLimitationService;
	@EJB private ITaTNiveauServiceRemote taTNiveauService;
	@EJB private ITaCgvServiceRemote taCgvService;
	@EJB private ITaCgPartenaireServiceRemote taCgPartenaireService;
	@EJB private ITaCategorieProServiceRemote taCategorieProService;
	@EJB private ITaTypePartenaireServiceRemote taTypePartenaireProService;
	@EJB private ITaCommissionServiceRemote taCommissionServiceRemote;
	@EJB private ITaTypePaiementServiceRemote taTypePaiementServiceRemote;
	
	public String chargeDernierSetup(String codeTiers, String codeProduit, String versionClient) throws RemoteException {
		return taSetupService.chargeDernierSetup(codeTiers, codeProduit, versionClient);
	}

	@Override
	public List<TaProduit> selectAllProduit(String type) {
		return taProduitService.selectAll();
	}
	
	@Override
	public List<TaProduit> selectAllProduitCategoriePro(int idCategoriePro) {
		return taProduitService.selectAll();
	}

	@Override
	public TaClient mergeClient(TaClient c) {
		return taClientService.merge(c);
	}

	@Override
	public boolean utilisateurExiste(String adresseEmail) {
		boolean existe = false;
		if(taUtilisateurService.findByEmail(adresseEmail)!=null) {
			existe = true;
		}
		return existe;
	}
	
	public List<TaTNiveau> selectAllTNiveau() {
		return taTNiveauService.selectAll();
	}

	@Override
	public boolean dossierExiste(String codeDossier) {
		boolean existe = false;
		try {
			if(taDossierService.findByCode(codeDossier)!=null) {
				existe = true;
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return existe;
	}

	@Override
	public TaDossier findDossier(String codeDossier) {
		try {
			return taDossierService.findByCode(codeDossier);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaDossier> findListeDossierClient(int idClient) {
		try {
			return taDossierService.findListeDossierClient(idClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<TaDossier> findListeDossierProduit(String idProduit) {
		//return findListeDossierClient(37);
		return taDossierService.findListeDossierProduit(idProduit);
		//return taDossierService.selectAll();
	}
	
	public TaClient findClientDossier(String codeDossier) {
		return taDossierService.findClientDossier(codeDossier);
	}

	@Override
	public TaDossier mergeDossier(TaDossier d) {
		return taDossierService.merge(d);
	}

	@Override
	public TaPanier mergePanier(TaPanier p) {
		return taPanierService.merge(p);
	}
	
	@Override
	public TaPanier findPanier(int idPanier) {
		try {
			return taPanierService.findById(idPanier);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<TaPanier> findPanierDossier(String codeDossier) {
		return taPanierService.findPanierDossier(codeDossier);
	}

	@Override
	public List<TaPanier> findPanierClient(String codeClient) {
		return taPanierService.findPanierClient(codeClient);
	}
	
	public List<TaPrixParUtilisateur> findPrixParUtilisateur() {
		return taPrixParUtilisateurService.selectAll();
	}

	public TaLimitation findLimitationFacture(String typeLimitation) {
		try {
			return taLimitationService.findByCode(typeLimitation);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public TaCgv findCgv(int idCgv) {
		try {
			return taCgvService.findById(idCgv);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public TaCgv findCgvCourant() {
    	return taCgvService.findCgvCourant();
    }
    
    public List<TaCategoriePro> selectAllCategoriePro() {
    	return taCategorieProService.selectAll();
    }
    
    //////////////////
    // Partenariat
    /////////////////
    public TaCgPartenaire findCgPartenaire(int idCgv) {
		try {
			return taCgPartenaireService.findById(idCgv);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public TaCgPartenaire findCgPartenaireCourant() {
    	return taCgPartenaireService.findCgPartenaireCourant();
    }

    public TaTypePartenaire findTypePartenaire(String codeTypePartenaire) {
		try {
			return taTypePartenaireProService.findByCode(codeTypePartenaire);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * Pour un partenaire, permet de retrouver les informations de son "compte partenaire" à partir de son dossier
     * 
     */
    public TaPartenaire findPartenaire(String codeDossier) {
		try {
			TaClient c = findClientDossier(codeDossier);
			if(c!=null) {
				if(c.getTaPartenaire()!=null) {
					return c.getTaPartenaire();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public TaPartenaire findPartenaireByCode(String codePartenaire) {
		try {
			TaClient c = taClientService.findByCodePartenaire(codePartenaire);
			if(c!=null) {
				if(c.getTaPartenaire()!=null) {
					return c.getTaPartenaire();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public String genereCodeClient(String nomClient) {
		try {
			String c = taClientService.genereCodeClient(nomClient);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<TaCommission> findCommissionPartenaire(String codePartenaire, Date debut, Date fin) {
		return taCommissionServiceRemote.findCommissionPartenaire(codePartenaire,debut,fin);
//		return taCommissionServiceRemote.findCommissionPartenaire(codePartenaire);
	}
	
	public TaTypePaiement findTypePaiement(String codeTypePaiement) {
		try {
			return taTypePaiementServiceRemote.findByCode(codeTypePaiement);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Date findDerniereCreationDossierPayantPartenaire(String codePartenaire) {
		return taCommissionServiceRemote.findDerniereCreationDossierPayantPartenaire(codePartenaire);
	}
	
	public BigDecimal findMontantVentePartenaire(String codePartenaire, Date debut, Date fin) {
		return taCommissionServiceRemote.findMontantVentePartenaire(codePartenaire, debut, fin);
	}
	
	public BigDecimal findTauxCommissionPartenaireAvecDecote(String codePartenaire) {
		return taCommissionServiceRemote.findTauxCommissionPartenaire(codePartenaire);
	}
	
	public BigDecimal findTauxCommissionPartenaireSansDecote(String codePartenaire) {
		return taCommissionServiceRemote.findTauxCommissionPartenaire(codePartenaire,false);
	}
	
	public BigDecimal findDecoteTauxCommissionPartenaire(String codePartenaire) {
		BigDecimal decote = BigDecimal.ZERO;
		BigDecimal txAvecDecote = taCommissionServiceRemote.findTauxCommissionPartenaire(codePartenaire);
		BigDecimal txSansDecote = taCommissionServiceRemote.findTauxCommissionPartenaire(codePartenaire,false);
		if(txAvecDecote!=null && txSansDecote!=null) {
			decote = txAvecDecote.subtract(txSansDecote);
		}
		return decote;
	}

	@Override
	public TaCommission genereCommission(TaPanier panier) {
		
		TaClient partenaire = taClientService.findByCodePartenaire(panier.getCodeRevendeur());
		
		//TODO vérifier que ce panier n'a pas déjà généré une ligne de commission
		
		if(partenaire!=null) {
			TaCommission com = new TaCommission();
			com.setTaPartenaire(partenaire.getTaPartenaire());
			com.setTaPanier(panier);
			com.setCommissionPayee(false);
			com.setDatePaiementCommission(null);
			///////////////
			///////////
			BigDecimal pourcentageCommission = taCommissionServiceRemote.findTauxCommissionPartenaire(partenaire.getTaPartenaire().getCodePartenaire()).divide(new BigDecimal(100));
			///////////
			////////////////
			BigDecimal montantReference = new BigDecimal(0);
			BigDecimal montantCommission = new BigDecimal(0);
			
			List<TaLigneCommission> lignes = new ArrayList<>();
			TaLigneCommission ligne = null;
			for (TaLignePanier l : panier.getLignes()) {
				if(l.getTaProduit().getEligibleCommission()) {
					ligne = new TaLigneCommission();
					
					ligne.setTaCommission(com);
					ligne.setTaLignePanier(l);
					ligne.setTaProduit(l.getTaProduit());
					ligne.setMontantReference(l.getMontantHT());
					ligne.setPourcentageCommission(pourcentageCommission);
					ligne.setMontantCommission(ligne.getMontantReference().multiply(ligne.getPourcentageCommission()));
					
					montantReference = montantReference.add(ligne.getMontantReference());
					montantCommission = montantCommission.add(ligne.getMontantCommission());
					
					lignes.add(ligne);
				}
			}
			com.setLignesCommission(lignes);
			com.setMontantReference(montantReference);
			com.setPourcentageCommission(pourcentageCommission);
			com.setMontantCommission(montantCommission);
			
			taCommissionServiceRemote.merge(com);
		}
		return null;
	}
	
	public TaDossier majAutorisation(TaPanier panier) {
		try {
			if(panier!=null){
				Date dateFin=panier.getDatePaiement();
				dateFin = LibDate.incrementDate(dateFin, 0, panier.getNbMois(), 0);
				
				for (TaLignePanier l : panier.getLignes()) {
					ajouteAutorisation(panier.getTaDossier(), l.getTaProduit(),panier.getDatePaiement(),dateFin,panier.getNbMois());
				}
				
				panier.getTaDossier().setNbUtilisateur(panier.getNbUtilisateur());
				panier.getTaDossier().setNbPosteInstalle(panier.getNbPosteInstalle());
				panier.getTaDossier().setAccesWs(panier.getAccesWS());
				panier.getTaDossier().setTaTNiveau(taTNiveauService.findByCode(panier.getNiveau()));
				
				TaDossier d = mergeDossier(panier.getTaDossier());
	//			dossierCourant=wsMonCompte.findDossier(dossierCourant.getCode());
				return d;
			}
		} catch(FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void ajouteAutorisation(TaDossier dossier, TaProduit p, Date dateAchat, Date dateFin,int nbMois) {
		if(p!=null) {
			TaAutorisation auto = rechercheAutorisationProduitExistante(dossier, p);
			if(auto==null) {
				//pas d'autorisation pour ce produit sur ce dossier, on créer une nouvelle autorisation
				auto = new TaAutorisation();
				auto.setDateAchat(dateAchat);
				auto.setDateFin(dateFin);
				auto.setTaDossier(dossier);
				auto.setTaProduit(p);
				auto.setVersionObj(0);
				dossier.getListeAutorisation().add(auto);
			} else {
				//il y a deja une autorisation, on la met à jour
				Date date;
				if(auto.getDateFin().before(dateAchat)
						|| auto.getDateFin().equals(dateAchat)){
					//la date de fin d'autorisation du module est deja dépassée, la date d'achat deviens la nouvelle date de référence pour calculer la durée
					date=dateAchat;
					auto.setDateAchat(dateAchat);
				} else {
					//la date de fin de l'autorisation précédente n'est pas encore dépassée, on prolonge la durée à partir de cette date.
					date=auto.getDateFin();
				}
				date = LibDate.incrementDate(date, 0, nbMois, 0);	
					
				//on récupère la date de fin en courant et on y rajoute le nb de mois payé
				auto.setDateFin(date);
				auto.setPaye(true);
			}
		}
	}
	
	private TaAutorisation rechercheAutorisationProduitExistante(TaDossier dossier, TaProduit produit){
		for (TaAutorisation ligne : dossier.getListeAutorisation()) {
			if(equalsProduitAndVersion(ligne.getTaProduit(),produit))
				return ligne;
		}
		return null;
	}	
	
	public boolean equalsProduitAndVersion(TaProduit obj1,Object obj2) {
		if (this == obj2)
			return true;
		if (obj2 == null)
			return false;
		if (obj1.getClass() != obj2.getClass())
			return false;
		TaProduit other = (TaProduit) obj2;
		if (obj1.getCode() == null) {
			if (other.getCode() != null)
				return false;
		} else if (!obj1.getCode().equals(other.getCode()))
			return false;
		if (obj1.getIdProduit() == null) {
			if (other.getIdProduit() != null)
				return false;
		} else if (!obj1.getIdProduit().equals(other.getIdProduit()))
			return false;
		if (obj1.getVersionBrowser() == null) {
			if (other.getVersionBrowser() != null)
				return false;
		} else if (!obj1.getVersionBrowser().equals(other.getVersionBrowser()))
			return false;
		if (obj1.getVersionOS() == null) {
			if (other.getVersionOS() != null)
				return false;
		} else if (!obj1.getVersionOS().equals(other.getVersionOS()))
			return false;
		if (obj1.getVersionProduit() == null) {
			if (other.getVersionProduit() != null)
				return false;
		} else if (!obj1.getVersionProduit().equals(other.getVersionProduit()))
			return false;
		///
//		if (obj1.getTaTNiveau() == null) {
//			if (other.getTaTNiveau() != null)
//				return false;
//		} else if (!obj1.getTaTNiveau().getCode().equals(other.getTaTNiveau().getCode()))
//			return false;
		///
		return true;
	}
	
}
