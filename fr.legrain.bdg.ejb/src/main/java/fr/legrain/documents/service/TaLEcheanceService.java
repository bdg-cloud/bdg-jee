package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;
import org.jboss.ejb3.annotation.TransactionTimeout;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLEcheanceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.documents.dao.ILAvisEcheanceDAO;
import fr.legrain.documents.dao.ILEcheanceDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLEcheanceBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLEcheanceService extends AbstractApplicationDAOServer<TaLEcheance, TaLEcheanceDTO> implements ITaLEcheanceServiceRemote {

	static Logger logger = Logger.getLogger(TaLEcheanceService.class);

	@Inject private ILEcheanceDAO dao;
	
	@EJB private ITaLAbonnementServiceRemote taLAbonnementService;
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	@EJB private ITaLAvisEcheanceServiceRemote taLAvisEcheanceService;
	@EJB private ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	@EJB private ITaArticleServiceRemote taArticleService;

	/**
	 * Default constructor. 
	 */
	public TaLEcheanceService() {
		super(TaLEcheance.class,TaLEcheanceDTO.class);
	}
	
	public List<TaLEcheance> rechercheEcheanceLieAAbonnement(TaAbonnement taAbonnement) {
		return dao.rechercheEcheanceLieAAbonnement(taAbonnement);
	}
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceTiers(String codeTiers) {
		return dao.rechercheEcheanceNonLieAAvisEcheanceTiers(codeTiers);
	}
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceTiers() {
		return dao.rechercheEcheanceNonLieAAvisEcheanceTiers();
	}
	
//	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceSubscription() {
//		return dao.rechercheEcheanceNonLieAAvisEcheanceSubscription();
//	}
//	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceSubscription(TaStripeSubscription taStripeSubscription) {
//		return dao.rechercheEcheanceNonLieAAvisEcheanceSubscription(taStripeSubscription);
//	}
	public List<TaLEcheance> rechercheEcheanceSuspendusTiers(String codeTiers){
		return dao.rechercheEcheanceSuspendusTiers( codeTiers);
	}
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiers(String codeTiers) {
		return dao.rechercheEcheanceEnCoursOuSuspendusTiers(codeTiers);
	}
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement(Integer id){
		return dao.rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement(id);
	}
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdAbonnement(Integer id){
		return dao.rechercheEcheanceEnCoursByIdAbonnement(id);
	}
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdLAbonnement(Integer id){
		return dao.rechercheEcheanceEnCoursByIdLAbonnement(id);
	}
	public List<TaLEcheance> rechercheEcheanceByIdLAbonnement(Integer id) {
		return dao.rechercheEcheanceByIdLAbonnement(id);
	}
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceAbonnement(){
		return dao.rechercheEcheanceNonLieAAvisEcheanceAbonnement();
	}
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceAbonnement(TaAbonnement taAbonnement){
		return dao.rechercheEcheanceNonLieAAvisEcheanceAbonnement(taAbonnement);
	}
	public List<TaLEcheance> rechercheEcheanceLightNonLieAAvisEcheanceAbonnement(){
		return dao.rechercheEcheanceLightNonLieAAvisEcheanceAbonnement();
	}
	
	public List<TaLEcheance> rechercheEcheanceEnCoursTiers(String codeTiers){
		return dao.rechercheEcheanceEnCoursTiers(codeTiers);
	}
	
	public List<TaLEcheance> rechercheEcheanceBycodeAvisEcheance(String code){
		return dao.rechercheEcheanceBycodeAvisEcheance(code);
	}
	
//	public Integer countAllByIdSubscriptionItem(Integer idSubscriptionItem) {
//		return dao.countAllByIdSubscriptionItem(idSubscriptionItem);
//	}
	public Integer countAllByIdLigneAbo(Integer idLigneAbo) {
		return dao.countAllByIdLigneAbo(idLigneAbo);
	}
	public long countTiersEcheanceNonLieAAvisEcheance() {
		return dao.countTiersEcheanceNonLieAAvisEcheance();
	}
	public BigDecimal montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers(String codeTiers) {
		return dao.montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers(codeTiers);
	}
	public long countEcheanceNonLieAAvisEcheanceTiers(String codeTiers) {
		return dao.countEcheanceNonLieAAvisEcheanceTiers(codeTiers);
	}
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement(String codeTiers, String codeTPaiement){
		return dao.rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement( codeTiers,  codeTPaiement);
	}
	public List<TaLEcheance> findAllSuspendues(){
		return dao.findAllSuspendues();
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	///////////BDG TUNNEL VENTE/////////////////
	public List<TaLEcheance> findAllEcheanceEnCoursOuSuspendueModuleBDG(Integer idTiers){
		return dao.findAllEcheanceEnCoursOuSuspendueModuleBDG(idTiers);
	}
	public List<TaLEcheanceDTO> findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(){
		return dao.findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(null);
	}
	public List<TaLEcheanceDTO> findAllEcheanceByCodeEtatsAndByIdTiersDTO(List<String> etats, Boolean codeModuleBDG, Integer idTiers){
		return dao.findAllEcheanceByCodeEtatsAndByIdTiersDTO(etats,codeModuleBDG,idTiers);
	}
	public List<TaLEcheanceDTO> findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(Integer idTiers){
		return dao.findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(idTiers);
	}
	///////////FIN BDG TUNNEL VENTE////////////
	public List<TaLEcheance> findAllEnCoursDepasse(){
		return dao.findAllEnCoursDepasse();
	}
	public List<TaLEcheance> findAllEcheanceSansEtat(){
		return dao.findAllEcheanceSansEtat();
	}
	
	public TaLEcheance findByIdLAvisEcheance(Integer idLAvisEcheance) {
		return dao.findByIdLAvisEcheance(idLAvisEcheance);
	}
	public TaLEcheance findByIdLFacture(Integer idLFacture) {
		return dao.findByIdLFacture( idLFacture);
	}
	public void persist(TaLEcheance transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLEcheance transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLEcheance persistentInstance) throws RemoveException {
		TaLEcheance ech = dao.findById(persistentInstance.getIdLEcheance());//a changer par un merge car plus light
//		taLigneALigneEcheanceService.removeAllByIdEcheance(ech.getIdLEcheance());
		dao.remove(ech);
	}
	
	public TaLEcheance merge(TaLEcheance detachedInstance) {
		return merge(detachedInstance, null);
	}
	public TaLEcheance donneEtatSuspendu(TaLEcheance detachedInstance) {
		try {
			TaEtat etat = taEtatService.findByCode("doc_suspendu");
			detachedInstance= findById(detachedInstance.getIdLEcheance());
			detachedInstance.addREtatLigneDoc(etat);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return merge(detachedInstance, null);
	}
	
	public void suppressionEcheanceAnnule() {
		List<TaLAbonnement> listeLigneAbo = taLAbonnementService.findAllAnnule();
		Integer nbEchSuppr = 0;
		for (TaLAbonnement taLAbonnement : listeLigneAbo) {
			List<TaLEcheance> listeEch = findAllEnCoursByIdLAbonnement(taLAbonnement.getIdLDocument());
			
			for (TaLEcheance ech : listeEch) {
				try {
					remove(ech);
					nbEchSuppr++;
				} catch (RemoveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		System.out.println(nbEchSuppr+" échéances en cours viennent d'être supprimé car lié a des lignes abo annulées");
	}
	
	
	
	public List<TaLEcheance> findAllEnCoursByIdLAbonnement(Integer id){
		return dao.findAllEnCoursByIdLAbonnement(id);
	}
	public List<TaLEcheance> findAllEnCoursOuSuspenduByIdLAbonnement(Integer id){
		return dao.findAllEnCoursOuSuspenduByIdLAbonnement(id);
	}
	public TaLEcheance donneEtat(TaLEcheance detachedInstance, TaEtat etat) {
		try {
			detachedInstance= findById(detachedInstance.getIdLEcheance());
			detachedInstance.addREtatLigneDoc(etat);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return merge(detachedInstance, null);
	}
	public List<TaLEcheance> findAllEnCoursArrivantAEcheanceDans60Jours(){
		return dao.findAllEnCoursArrivantAEcheanceDans60Jours();
	}
	public void suppressionEcheanceDans60Jours() {
		List<TaLEcheance> listeEcheance = findAllEnCoursArrivantAEcheanceDans60Jours();
		
		for (TaLEcheance taLEcheance : listeEcheance) {
			try {
				remove(taLEcheance);
			} catch (RemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    public Integer supprimeEcheanceSansEtat() {
    	List<TaLEcheance> listeEcheance = findAllEcheanceSansEtat();
		Integer nb = 0;
		for (TaLEcheance taLEcheance : listeEcheance) {
			try {
				List<TaLAvisEcheance> listeLigneAvis = taLAvisEcheanceService.findAllByIdEcheance(taLEcheance.getIdLEcheance());
				
				for (TaLAvisEcheance ligneAvis : listeLigneAvis) {
					ligneAvis.setTaLEcheance(null);
					taLAvisEcheanceService.merge(ligneAvis);
				}
				remove(taLEcheance);
				nb++;
			} catch (RemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return nb;
	}
	
	public List<TaLEcheance> findAllLieAAvisEcheanceTiers(String codeTiers){
		return dao.findAllLieAAvisEcheanceTiers(codeTiers);
	}
	
	public List<TaLEcheance> findAllLieAAvisEcheancePayeTiers(String codeTiers){
		return dao.findAllLieAAvisEcheancePayeTiers(codeTiers);
	}
	
	public List<TaLEcheance> findAllNonLieAAvisEcheancePayeTiers(String codeTiers){
		return dao.findAllNonLieAAvisEcheancePayeTiers(codeTiers);
	}
	
	public List<TaLEcheance> findAllEcheanceNonTotTransforme(){
		return dao.findAllEcheanceNonTotTransforme();
	}
	
	public void transformeEcheance() {
		List<TaLEcheance> listeEcheance = findAllLieAAvisEcheancePayeTiers(null);
		TaEtat etat;
		try {
			etat = taEtatService.findByCode("doc_tot_Transforme");
			for (TaLEcheance taLEcheance : listeEcheance) {
				//TaLAbonnement abo = taLAbonnementService.findByIdLEcheance(taLEcheance.getIdLEcheance());
					
					donneEtat(taLEcheance, etat);
					//taLAbonnementService.donneEtat(abo, etat);
			}
		} catch (FinderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void donneEtatEnCours() {
		List<TaLEcheance> listeEcheance = findAllNonLieAAvisEcheancePayeTiers(null);
		TaEtat etat;
		try {
			etat = taEtatService.findByCode("doc_encours");
			for (TaLEcheance taLEcheance : listeEcheance) {
				//TaLAbonnement abo = taLAbonnementService.findByIdLEcheance(taLEcheance.getIdLEcheance());
					
					donneEtat(taLEcheance, etat);
					//taLAbonnementService.donneEtat(abo, etat);
			}
		} catch (FinderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@TransactionTimeout(value=30,unit=TimeUnit.MINUTES)
	public void suspendEcheances() {
		List<TaLEcheance> listeEcheance = findAllEnCoursDepasse();
		TaEtat etat;
		try {
			etat = taEtatService.findByCode("doc_suspendu");
			for (TaLEcheance taLEcheance : listeEcheance) {
				TaLAbonnement abo = taLAbonnementService.findByIdLEcheance(taLEcheance.getIdLEcheance());
				donneEtat(taLEcheance, etat);
				taLAbonnementService.donneEtat(abo, etat);
				
			}
		} catch (FinderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	@TransactionTimeout(value=30,unit=TimeUnit.MINUTES)
	public void supprimeEcheanceDelaiSurvieDepasse() {
		List<TaLEcheance> listeEcheances = findAllSuspendues();
		Integer nbEchSupprimee = 0;
		Date now = new Date();
		
		for (TaLEcheance ech : listeEcheances) {
			if(ech.getIdLEcheance() == 5840) {
				System.out.println("je suis bien sur la bonne échéance suspendu");
			}
			TaArticle article;
			try {
				article = taArticleService.findById(ech.getTaLAbonnement().getTaArticle().getIdArticle());
				TaLAbonnement ligneABo =ech.getTaLAbonnement();
				Integer nbJoursDelai = article.getDelaiSurvie();
				Integer nbJoursDelaiGrace = article.getDelaiGrace(); 
				LocalDateTime finDelaiSurvie = new Date(ech.getDebutPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				LocalDateTime nowLD = new Date(now.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				finDelaiSurvie = finDelaiSurvie.plusDays(nbJoursDelai);
				
				//si now est après a la (date de début période  + le délai de survie)
				if(nowLD.isAfter(finDelaiSurvie)) {
					System.out.println(nowLD+" est après "+finDelaiSurvie+" donc on va supprimé cette échéance");
					try {
						
						
						if(nbJoursDelaiGrace != null && nbJoursDelaiGrace > 0) {
							//dans le cas ou le délai de grace se calcule par rapport à la date d'échéance ou date début période 
							LocalDateTime finDelaiGrace = new Date(ech.getDebutPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
							finDelaiGrace = finDelaiGrace.plusDays(nbJoursDelaiGrace);
							//si on est plus dans le délai de grace , on supprime
							if(nowLD.isAfter(finDelaiGrace)) {
								List<TaLAvisEcheance> listeLigneAvis = taLAvisEcheanceService.findAllByIdEcheance(ech.getIdLEcheance());
								
								for (TaLAvisEcheance ligneAvis : listeLigneAvis) {
									ligneAvis.setTaLEcheance(null);
									taLAvisEcheanceService.merge(ligneAvis);
								}
								
								remove(ech);
								nbEchSupprimee++;
								ligneABo = taLAbonnementService.donneEtatAnnule(ligneABo);	
								
							//Si on est dans le délai de grace
							}else {
								//on ne supprime pas 
								//on attribue un état à l'échéance
								ech.setGrace(true);
								merge(ech);
							}
							
						}else {
							//ici je vais aller chercher d'éventuel avis liés à l'échéance (ancienne version des abonnements fonctionner comme ça) et les déliés sinon je ne peu pas supprimé
							// on pourra virer ce code dès que la nouvelle version est bien en place et que plus aucun ancien avis n'est lié a des échéances
							List<TaLAvisEcheance> listeLigneAvis = taLAvisEcheanceService.findAllByIdEcheance(ech.getIdLEcheance());
							
							for (TaLAvisEcheance ligneAvis : listeLigneAvis) {
								ligneAvis.setTaLEcheance(null);
								taLAvisEcheanceService.merge(ligneAvis);
							}
							
							remove(ech);
							nbEchSupprimee++;
							ligneABo = taLAbonnementService.donneEtatAnnule(ligneABo);
						}
						
					//} catch (RemoveException e) {
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (FinderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
			
		}
		System.out.println(nbEchSupprimee+" échéances viennent d'etres supprimées car délai de survie dépassé.");
	}
	
//	public void supprimeLiaisonsAvisEcheance(TaAvisEcheance avis) {
//		for (TaLAvisEcheance ligne : avis.getLignes()) {
//			supprimeLiaisonsAvisEcheance(ligne);
//		}
//	}
//	public void supprimeLiaisonsAvisEcheance(TaLAvisEcheance ligne) {
//		List<TaLEcheance> liste = findAllByIdLAvisEcheance(ligne.getIdLDocument());
//		for (TaLEcheance taLEcheance : liste) {
//			taLEcheance.setTaLAvisEcheance(null);
//			merge(taLEcheance);
//		}
//
//	}
//	public void supprimeLiaisonsFacture(TaFacture facture) {
//		for (TaLFacture ligne : facture.getLignes()) {
//			supprimeLiaisonsFacture(ligne);
//		}
//	}
//	public void supprimeLiaisonsFacture(TaLFacture ligne) {
//		List<TaLEcheance> liste = findAllByIdLFacture(ligne.getIdLDocument());
//		for (TaLEcheance taLEcheance : liste) {
//			taLEcheance.setTaLFacture(null);
//			merge(taLEcheance);
//		}
//	}
	
	public List<TaLEcheance> selectAllEcheanceARelancer(String codeTiers){
		return dao.selectAllEcheanceARelancer(codeTiers);
	}
	
	public List<TaLEcheance> findAllByIdLAvisEcheance(Integer idLAvisEcheance){
		return dao.findAllByIdLAvisEcheance(idLAvisEcheance);
	}
	public List<TaLEcheance> findAllByIdLFacture(Integer idLFacture){
		return dao.findAllByIdLFacture(idLFacture);
	}
	
	public void regleEcheances(List<TaLEcheance> liste) {
		TaEtat etat;
    	TaEtat etatEncours;
		try {
			etat = taEtatService.findByCode("doc_tot_Transforme");
			etatEncours = taEtatService.findByCode("doc_encours");
			for (TaLEcheance ligne : liste) {
				
				TaLAbonnement ligneAbo = taLAbonnementService.findByIdLEcheance(ligne.getIdLEcheance());
				TaAbonnement abo = ligneAbo.getTaDocument();
				boolean miseAJourDates = true;
				
				//si l'abonnement n'a pas de dates de période active, c'est que cette échéance est la première 
				if (abo.getDateDebutPeriodeActive() == null) {
					// on remplit les dates de périodes active de l'abonnement avec les dates de périodes de l'échéance
					abo.setDateDebutPeriodeActive(ligne.getDebutPeriode());
					abo.setDateFinPeriodeActive(ligne.getFinPeriode());
					miseAJourDates = false;
					taAbonnementService.merge(abo);
					
				} else {
					//si c'est une échéance suspendue, donc en retard
//					if(ligne.getTaREtat().getTaEtat().getCodeEtat().equals("doc_suspendu")) {
//						//on cherche si il y a une échéance en cours
//						List<TaLEcheance> echeances = rechercheEcheanceEnCoursByIdAbonnement(abo.getIdDocument());
//						
//						//si il y a une échéance en cours, on ne met pas à jour les dates
//						if(echeances != null && !echeances.isEmpty()) {
//							miseAJourDates = false;
//							
//						}else {
//							//si non, on met les dates à jour
//							miseAJourDates = true;
//						}
//						
//					//si c'est une échéance normale en cours	
//					}else {
						//on essai de savoir si les date de périodes active ou déjà etait mise à jour ou pas
						//pour cela on compare les dates périodes active de l'abo avec les dates périodes de l'échéance.
						Date dateDebutPeriode = abo.getDateDebutPeriodeActive();
						Date dateDebutPeriodeEcheance = ligne.getDebutPeriode();
						LocalDateTime localDateTimeDebutPeriode = new Date(dateDebutPeriode.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						LocalDateTime localDateTimeDebutPeriodeEcheance = new Date(dateDebutPeriodeEcheance.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						//si la date de début période active de l'abo est avant la date début période active de la ligne, c'est que les dates de périodes de l'abo sont en retard
						
						if(localDateTimeDebutPeriode.isBefore(localDateTimeDebutPeriodeEcheance)) {
							//donc on met a jour
							miseAJourDates = true;
							
						//sinon, si la date début période de l'abo est après ou égale a la date de début période de l'échéance, c'est l'échéance qui est en retard
						//donc on ne met pas a jour
						}else {
							miseAJourDates = false;
						}
						
//					}
					
				}
				
				if(miseAJourDates) {
					//pour mettre les dates de périodes actives de l'abonnement à jour, il suffit de rajouter la fréquence à la date de fin période.
					Date newDateDebut = null;
					Date newDateFin = null;
					//on récupère les dates actuelle de périodes active de l'abonnement
					Date dateDebutPeriode = abo.getDateDebutPeriodeActive();
					Date dateFinPeriode = abo.getDateFinPeriodeActive();
					LocalDateTime localDateTimeDebutPeriode = new Date(dateDebutPeriode.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDateTime localDateTimeFinPeriode = new Date(dateFinPeriode.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					//la nouvelle date de début est égale à la date de fin actuelle +1 jour
					//on ajout 1 jours pour que la date de début de la nouvelle période active commence un jour apres la période précédante
					localDateTimeDebutPeriode = localDateTimeFinPeriode.plusDays(1);
					
					//on ajoute le nombre de mois (fréquence de facturation) a la date de fin période actuelle
					localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(abo.getNbMoisFrequenceFacturation());
					
					newDateFin = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
					
					
					newDateDebut =  Date.from(localDateTimeDebutPeriode.atZone(ZoneId.systemDefault()).toInstant());
					
					abo.setDateDebutPeriodeActive(newDateDebut);
					abo.setDateFinPeriodeActive(newDateFin);
					
					taAbonnementService.merge(abo);
				}
				
				
				ligne = donneEtat(ligne,etat);
				taLAbonnementService.donneEtat(ligneAbo, etatEncours);
				//on risque de ne pas générer la prochaine echéance ici, on laisse le CRON le faire au dernier moment
				//TaLEcheance newLigne = taAbonnementService.genereUneProchaineEcheance(ligne);
			}
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLEcheance merge(TaLEcheance detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLEcheance findById(int id) throws FinderException {
		return dao.findById(id);
	}
	
	public TaLEcheance findByIdFetchLigneAbo(int id) throws FinderException {
		TaLEcheance ech = dao.findById(id);
		ech.getTaLAbonnement();
		return ech;
	}
	
	public TaLAbonnement fetchLigneAbo(int id) throws FinderException {
		TaLEcheance ech = dao.findById(id);
		TaLAbonnement ligneAbo = ech.getTaLAbonnement();
		return ligneAbo;
	} 

	public TaLEcheance findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}
	



//	@RolesAllowed("admin")
	public List<TaLEcheance> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLEcheanceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLEcheanceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLEcheance> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLEcheanceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLEcheanceDTO entityToDTO(TaLEcheance entity) {
//		TaLEcheanceDTO dto = new TaLEcheanceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLEcheanceMapper mapper = new TaLEcheanceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLEcheanceDTO> listEntityToListDTO(List<TaLEcheance> entity) {
		List<TaLEcheanceDTO> l = new ArrayList<TaLEcheanceDTO>();

		for (TaLEcheance taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLEcheanceDTO> selectAllDTO() {
		System.out.println("List of TaLEcheanceDTO EJB :");
		ArrayList<TaLEcheanceDTO> liste = new ArrayList<TaLEcheanceDTO>();

		List<TaLEcheance> projects = selectAll();
		for(TaLEcheance project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLEcheanceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLEcheanceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLEcheanceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLEcheanceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLEcheanceDTO dto, String validationContext) throws EJBException {
		try {
			TaLEcheanceMapper mapper = new TaLEcheanceMapper();
			TaLEcheance entity = null;
			if(dto.getIdLEcheance()!=null) {
				entity = dao.findById(dto.getIdLEcheance());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdLEcheance()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaLEcheanceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLEcheanceDTO dto, String validationContext) throws CreateException {
		try {
			TaLEcheanceMapper mapper = new TaLEcheanceMapper();
			TaLEcheance entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLEcheanceDTO dto) throws RemoveException {
		try {
			TaLEcheanceMapper mapper = new TaLEcheanceMapper();
			TaLEcheance entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLEcheance refresh(TaLEcheance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLEcheance value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaLEcheance value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaLEcheanceDTO dto, String validationContext) {
		try {
			TaLEcheanceMapper mapper = new TaLEcheanceMapper();
			TaLEcheance entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLEcheanceDTO> validator = new BeanValidator<TaLEcheanceDTO>(TaLEcheanceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLEcheanceDTO dto, String propertyName, String validationContext) {
		try {
			TaLEcheanceMapper mapper = new TaLEcheanceMapper();
			TaLEcheance entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLEcheanceDTO> validator = new BeanValidator<TaLEcheanceDTO>(TaLEcheanceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLEcheanceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLEcheanceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLEcheance value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLEcheance value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
