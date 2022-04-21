package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;

import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosAvisEcheance;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.IAvisEcheanceDAO;
import fr.legrain.dossier.dao.IInfoEntrepriseDAO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.dao.IFamilleTiersDAO;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaAvisEcheance.
 * @see fr.legrain.documents.dao.TaAvisEcheance
 * @author Hibernate Tools
 */
public class TaAvisEcheanceDAO /*extends AbstractApplicationDAO<TaAvisEcheance>*/ 
//implements IAvisEcheanceDAO, IDocumentDAO<TaAvisEcheance>,IDocumentTiersStatistiquesDAO<TaAvisEcheance>,IDocumentTiersEtatDAO<TaAvisEcheance> {
extends AbstractDocumentDAO<TaAvisEcheance,TaInfosAvisEcheance,TaLAvisEcheance> /*extends AbstractApplicationDAO<TaAvisEcheance>*/ 
implements IAvisEcheanceDAO {

//	private static final Log log = LogFactory.getLog(TaAvisEcheanceDAO.class);
	static Logger logger = Logger.getLogger(TaAvisEcheanceDAO.class);
	protected DocumentDTO documentDTO = new DocumentDTO();
	private String entity = "TaAvisEcheance";
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaAvisEcheance a";
	
	@Inject IInfoEntrepriseDAO taInfoEntrepriseDAO;
	@Inject IFamilleTiersDAO daoFamilleTiers;
	
	public TaAvisEcheanceDAO(){
//		this(null);
	}
	//public static final String FIND_NON_TRANSFORME_LIGHT_PERIODE_ABO_EN_COURS = "TaAvisEcheance.findAvisEcheanceNonTransfosDTOAboEnCours";
	//public static final String FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE_ABO_EN_COURS = "TaAvisEcheance.findAvisEcheanceNonTransfosARelancerDTOAboEnCours";

	//SPECIFIQUE ABONNEMENTS
	String FIND_NON_TRANSFORME_LIGHT_PERIODE_ABO_EN_COURS = "select distinct "
	+documentDTO.retournChampAndAlias("doc","idDocument")+", "
	+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
	+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
	+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
	+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
	+documentDTO.retournChampAndAlias("infos","nomTiers")+","
	+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
	+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
	+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
	+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
	+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
	+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
			+ " from TaAvisEcheance doc"
			+ " join doc.taInfosDocument infos"
			+ " join doc.taTiers tiers"
			+ " join doc.lignes ldoc "
			+ " join ldoc.taLEcheance ech "
			+ " join ech.taAbonnement sub"
			+ " where doc.dateDocument between :dateDebut and :dateFin "
			+ " and doc.taTiers.codeTiers like :codeTiers  "
			+ " and doc.taEtat is null "
			+ " and ldoc.taArticle is not null"
			+ " and sub.dateAnnulation is null"
			+ " and not exists (select r  from TaRDocument r  where r.taAvisEcheance = doc and ( r.taFacture IS NOT NULL "
			+ "	OR r.taBonliv IS NOT NULL OR r.taBoncde IS NOT NULL)) "
			+ " order by doc.dateDocument DESC, doc.codeDocument DESC";
			
	String FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE_ABO_EN_COURS = "select distinct "
			+documentDTO.retournChampAndAlias("doc","idDocument")+", "
			+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
			+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
			+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
			+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
			+documentDTO.retournChampAndAlias("infos","nomTiers")+","
			+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
			+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
			+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
			+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
			+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
			+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
			+ " from TaAvisEcheance doc "
			+ " join doc.taInfosDocument infos"
			+ " join doc.taTiers tiers"
			+ " join doc.lignes ldoc "
			+ " join ldoc.taLEcheance ech "
			+ " join ech.taAbonnement sub"
			+ " where doc.dateDocument between :dateDebut and :dateFin"
			+ " and doc.taTiers.codeTiers like :codeTiers  "
			+ " and doc.taEtat is null "
			+ " and ldoc.taArticle is not null"
			+ " and sub.dateAnnulation is null"
			+ " and doc.dateEchDocument <= :dateRef and doc.dateEchDocument >= :dateJour"
			+ " and not exists (select r  from TaRDocument r  where r.taAvisEcheance = doc and ( r.taFacture IS NOT NULL "
			+ "	OR r.taBonliv IS NOT NULL OR r.taBoncde IS NOT NULL)) "
			+ " order by doc.dateDocument DESC, doc.codeDocument DESC";
			
			
	public List<DocumentDTO> findDocumentNonTransfosDTOAboEnCours(Date dateDebut, Date dateFin,String codeTiers){
		logger.debug("getting nombre doc non transfos");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
			//Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_NON_TRANSFORME_LIGHT_PERIODE_ABO_EN_COURS);
			Query query = entityManager.createQuery(FIND_NON_TRANSFORME_LIGHT_PERIODE_ABO_EN_COURS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
			//List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTOAboEnCours(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers){
		logger.debug("getting liste doc non transfos à relancer");
		List<DocumentDTO> result = null;
		try {
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			if(codeTiers==null)codeTiers="%";
			//Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE_ABO_EN_COURS);
			Query query = entityManager.createQuery(FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE_ABO_EN_COURS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeTiers",codeTiers);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
			//List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTOAboEnCours(Date dateDebut, Date dateFin, String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.SUM_CA_TOTAL_LIGTH_PERIODE_NON_TRANSFORME_ABO_EN_COURS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			DocumentChiffreAffaireDTO l = (DocumentChiffreAffaireDTO) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTOAboEnCours(Date dateDebut,Date dateFin, int deltaNbJours,String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.SUM_CA_TOTAL_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE_ABO_EN_COURS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeTiers",codeTiers);
			DocumentChiffreAffaireDTO l = (DocumentChiffreAffaireDTO) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	public long countDocumentNonTransformeAboEnCours(Date debut, Date fin, String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.COUNT_NON_TRANSFORME_PERIODE_ABO_EN_COURS);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			Long l = (Long) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	public long countDocumentNonTransformeARelancerAboEnCours(Date debut, Date fin, int deltaNbJours,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.COUNT_NON_TRANSFORME_ARELANCER_PERIODE_ABO_EN_COURS);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeTiers",codeTiers);
			Long l = (Long) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	

	
//	public TaAvisEcheanceDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaAvisEcheance.class.getSimpleName());
//		initChampId(TaAvisEcheance.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaAvisEcheance());
//	}
//	
//	public TaAvisEcheance refresh(TaAvisEcheance detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			Map<IDocumentTiers,Boolean> listeEtatLegrain=new LinkedHashMap<IDocumentTiers, Boolean>();
//			detachedInstance.setLegrain(false);
//			
//        	
//        	entityManager.refresh(detachedInstance);
//        	
////			detachedInstance.setLegrain(false);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////
////			for (TaRAcompte a : detachedInstance.getTaRAcomptes()) {
////				session.evict(a.getTaAcompte().getTaRAcomptes());
////				session.evict(a.getTaAcompte());
////				session.evict(a);
////			}
////			session.evict(detachedInstance.getTaRAcomptes());
////			session.evict(detachedInstance);
////
////			detachedInstance = entityManager.find(TaAvisEcheance.class, detachedInstance.getIdDocument());
//			detachedInstance.setLegrain(true);
//			detachedInstance.calculLignesTva();
//			return detachedInstance;
//
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public List<TaAvisEcheance> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaFacture instance with codeTiers: " + codeTiers);
		List<TaAvisEcheance> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_TIERS_AND_DATE_POUR_COMPTE_CLIENT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public void persist(TaAvisEcheance transientInstance) {
		logger.debug("persisting TaAvisEcheance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaAvisEcheance persistentInstance) {
		logger.debug("removing TaProforma instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
//		logger.debug("removing TaAvisEcheance instance");
//		try {
//			
//			TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
//			Date dateDeb=taInfoEntreprise.getDatedebInfoEntreprise();
//			if(dateDeb.compareTo(persistentInstance.getDateDocument())>0)dateDeb=persistentInstance.getDateDocument();
//			TaAvisEcheance facture = null;
//			for (TaRDocument rDocument : persistentInstance.getTaRDocuments()) {
//				if(facture==null)facture=rDocument.getTaAvisEcheance();
//			}
////			if(facture==null){    //****** pour compile maven à remettre isa*****///
////				entityManager.remove(persistentInstance);
////				new TaComptePointDAO().calculPointUtilise(persistentInstance.getTaTiers().getIdTiers(), dateDeb);
////			}else{
////				MessageDialog.openWarning(PlatformUI.getWorkbench()
////						.getActiveWorkbenchWindow().getShell(), "Suppression impossible", "Vous ne pouvez pas supprimer ce document car il est lié à la facture "+facture.getCodeDocument());
////				entityManager.remove(null);
////			}
//
//			logger.debug("remove successful");
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("remove failed", re);
//			throw re2;
//		}
	}

	public TaAvisEcheance merge(TaAvisEcheance detachedInstance) {
		logger.debug("merging TaAvisEcheance instance");
		try {
			TaAvisEcheance result = entityManager.merge(detachedInstance);
//			if(detachedInstance.getTaInfosDocument()==null) {
//				throw new RuntimeException("Il manque l'infoDocument du document n°: "+detachedInstance.getCodeDocument());
//			}			
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaAvisEcheance findById(int id) {
		logger.debug("getting TaAvisEcheance instance with id: " + id);
		try {
			TaAvisEcheance instance = entityManager.find(TaAvisEcheance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public List<TaAvisEcheance> findByAbonnementAndDate(TaStripeSubscription taStripeSubscription, Date dateEcheance) {
//		logger.debug("getting TaAvisEcheance instance with date : " + dateEcheance);
//		try {
//			Query query = entityManager.createQuery("select f from TaAvisEcheance f "
//					+ " where f.taStripeSubscription.id="+taStripeSubscription.getIdStripeSubscription()+" "
//					+ " and f.dateEchDocument = :dateEcheance");
//			query.setParameter("dateEcheance", dateEcheance);
//			List<TaAvisEcheance> instance = (List<TaAvisEcheance>) query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
	
//	public List<TaAvisEcheance> findByAbonnementAndDate(TaAbonnement taAbonnement, Date dateEcheance) {
//		logger.debug("getting TaAvisEcheance instance with date : " + dateEcheance);
//		try {
//			Query query = entityManager.createQuery("select f from TaAvisEcheance f "
//					+ " where f.taAbonnement.idDocument= :idAbonnement "
//					+ " and f.dateEchDocument = :dateEcheance");
//			query.setParameter("idAbonnement", taAbonnement.getIdDocument());
//			query.setParameter("dateEcheance", dateEcheance);
//			List<TaAvisEcheance> instance = (List<TaAvisEcheance>) query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
	

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaAvisEcheance f where f.codeDocument='"+code+"'");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public TaAvisEcheance findByCode(String code) {
		logger.debug("getting TaAvisEcheance instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaAvisEcheance a " +
					" left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");
			TaAvisEcheance instance = (TaAvisEcheance)query.getSingleResult();
			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaAvisEcheance> selectAll() {
		logger.debug("selectAll TaAvisEcheance");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAvisEcheance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaAvisEcheance> findByCodeTiers(String codeTiers) {
		logger.debug("getting TaAvisEcheance instance with codeTiers: " + codeTiers);
		List<TaAvisEcheance> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_TIERS);
			query.setParameter("codeTiers", codeTiers);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaAvisEcheance> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaAvisEcheance instance with codeTiers: " + codeTiers);
		List<TaAvisEcheance> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_TIERS_AND_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * |année|codeTiers|HT|TVA|TTC
	 * @param codeTiers
	 * @param debut
	 * @param fin
	 * @return
	 */
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers,Date debut, Date fin, int precision) {
		logger.debug("getting TaAvisEcheance instance with codeTiers: " + codeTiers);
		List<Object> result = null;
		try {
			String jpql = null;
//			precision = 0;
			String groupBy = null;
			String select = null;
			
			if(precision==0) {
				select = "'','',cast(extract(year from f.dateDocument)as string)";
				groupBy = "'','',extract(year from f.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "'',extract(month from f.dateDocument),extract(year from f.dateDocument)";
			} else {
				select = "cast(extract(day from f.dateDocument)as string),cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			}
			
			jpql = "select  "
				+select+", t.codeTiers,"
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
				+" from TaAvisEcheance f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
				+"group by "+groupBy+",t.codeTiers"
				;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaAvisEcheance f "
//			+"left join f.taTiers t "
//			+"where  t.codeTiers = ? and f.dateDocument between ? and ? "
//			+"group by extract(year from f.dateDocument),t.codeTiers"
//			;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les devis entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaAvisEcheance> rechercheDocument(Date dateDeb, Date dateFin) {

		List<TaAvisEcheance> result = null;
		Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		return result;
	}
	
	
	/**
	 * Recherche les devis entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaAvisEcheance> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {

		List<TaAvisEcheance> result = null;
		Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		return result;
	}
	/**
	 * Recherche les devis entre 2 codes devis
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 codes (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(String codeDeb, String codeFin) {
	public List<TaAvisEcheance> rechercheDocument(String codeDeb, String codeFin) {		
		List<TaAvisEcheance> result = null;
		Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
		result = query.getResultList();
		return result;

	}
	
//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeTiers, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le montant des devis sur la période
	 */
	public List<Object> findCAAvisEcheanceSurPeriode(Date debut, Date fin) {
		logger.debug("getting nombre ca des devis");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" sum(d.netHtCalc)"
				+" FROM TaAvisEcheance d " 
				+" where d.dateDocument between :dateDeb and :dateFin";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaSumTaLAvisEcheance> rechercheSommeAvisEcheance(TaAvisEcheance taDoc) {
		logger.debug(" rechercheSommeAvisEcheance");
		try {
			Query ejbQuery = entityManager.createQuery("select NEW fr.legrain.documents.dao.TaSumTaLAvisEcheance( sum(tlf.mtTtcLApresRemiseGlobaleDocument),ta.idTAbonnement,ta.codeTAbonnement,cast(count(tlf.idLDocument) as integer)) from TaLAvisEcheance tlf join tlf.taLEcheance le "+
					" join le.taAbonnement a join a.taTAbonnement ta"+  
					 " where tlf.taDocument.idDocument = "+taDoc.getIdDocument()+" group by ta.idTAbonnement,ta.codeTAbonnement");
			List<TaSumTaLAvisEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheSommeAvisEcheance successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheSommeAvisEcheance failed", re);
			throw re;
		}
	}
	
	public List<Object> findChiffreAffaireTotalTransfo(Date debut, Date fin, int precision) {
		logger.debug("getting ChiffreAffaire total transfo");
		List<Object> result = null;
		try {
			String requete = "";
			String groupBy = "";
			String select = "";
			
			if(precision==0) {
				select = "'','',cast(extract(year from d.dateDocument)as string)";
				groupBy = "'','',extract(year from d.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "'',extract(month from d.dateDocument),extract(year from d.dateDocument)";
			} else {
				select = "cast(extract(day from d.dateDocument)as string),cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "extract(day from d.dateDocument),extract(month from d.dateDocument),extract(year from d.dateDocument)";
			}
			
			requete = "SELECT "+select+ ", "
			+" sum(d.netHtCalc), "
			+" sum(d.netTvaCalc), "
			+" sum(d.netTtcCalc) "
			+" FROM TaAvisEcheance d " 
			+" where d.dateDocument between :dateDeb and :dateFin "
			+" and exists (select r " 
			+" from TaRDocument r " 
			+" where r.taAvisEcheance = d " 
			+" and ( r.TaAvisEcheance IS NOT NULL " 
			+" OR r.taBonliv IS NOT NULL " 
			+" OR r.taBoncde IS NOT NULL " 
			+" OR r.taProforma IS NOT NULL )) "
			+" group by "+groupBy; 
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir les devis transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des devis transformés
	 */
	public List<TaAvisEcheance> findAvisEcheanceTransfos(Date debut, Date fin) {
		logger.debug("getting nombre devis non transfos");
		List<TaAvisEcheance> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" d"
				+" FROM TaAvisEcheance d " 
				+" where d.dateDocument between :dateDeb and :dateFin"
				+" and  exists (select r " +
						"from TaRDocument r " +
						" where r.taAvisEcheance = d" +
						" and ( TaAvisEcheance IS NOT NULL" +
						" OR taBonliv IS NOT NULL " +
						" OR taBoncde IS NOT NULL " +
						" OR taProforma IS NOT NULL))"
				+" order by d.mtHtCalc DESC";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir les devis non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des devis non transformés
	 */
	public List<TaAvisEcheance> findAvisEcheanceNonTransfos(Date debut, Date fin) {
		logger.debug("getting nombre devis non transfos");
		List<TaAvisEcheance> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" d"
				+" FROM TaAvisEcheance d " 
				+" where d.dateDocument between :dateDeb and :dateFin"
				+" and not exists (select r " +
						"from TaRDocument r " +
						" where r.taAvisEcheance = d" +
						" and ( TaAvisEcheance IS NOT NULL" +
						" OR taBonliv IS NOT NULL " +
						" OR taBoncde IS NOT NULL " +
						" OR taProforma IS NOT NULL))"
						+" order by d.mtHtCalc DESC";;
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaAvisEcheance> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers, null);
	}

	@Override
	public List<TaAvisEcheance> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers, null);
	}

	@Override
	public List<TaAvisEcheance> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaAvisEcheance> result = null;
		Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaAvisEcheance> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaAvisEcheance> result = null;
		Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
		result = query.getResultList();
		return result;
	}	
	
	@Override
	public List<TaAvisEcheance> rechercheDocumentEtat(Date dateDeb, Date datefin, String codeEtat) {
		List<TaAvisEcheance> result = null;
		Query query = null;
		if(codeEtat!=null) {
			query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_ETAT_DATE);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("codeEtat", codeEtat);
		} else {
			query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_ETAT_ENCOURS_DATE);
			query.setParameter("date", datefin);
		}
		
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaAvisEcheance> rechercheDocumentEtat(Date dateDeb, Date datefin,
			String codeEtat, String codeTiers) {
		List<TaAvisEcheance> result = null;
		Query query = null;
		if(codeEtat!=null) {
			query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_ETAT_TIERS_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("codeEtat", codeEtat);		
		} else {
			query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_ETAT_TIERS_ENCOURS_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", datefin);
		}
		
		result = query.getResultList();
		return result;
	}
	
	public List<TaAvisEcheanceDTO> rechercheNonLieAFactureByIdTiers(Integer id) {
		logger.debug("getting rechercheNonLieAFactureByIdTiers instance with idtier: " + id);
		try {
			
			String jpql = "select new fr.legrain.document.dto.TaAvisEcheanceDTO( ae.idDocument, ae.codeDocument, ae.dateDocument, ae.libelleDocument, tiers.codeTiers, info.nomTiers, ae.dateEchDocument, ae.dateExport, ae.netHtCalc, ae.netTvaCalc, ae.netTtcCalc)"
			+ " from TaAvisEcheance ae "
			+ " join ae.taInfosDocument info "
			+ " join ae.taTiers tiers"
			+ " where tiers.idTiers = :id and"
			+ " ae.idDocument not in (select rd.taAvisEcheance.idDocument from TaRDocument rd where rd.taAvisEcheance.idDocument = ae.idDocument)";
			
			
			Query query = entityManager.createQuery(jpql);
			query.setParameter("id",id);
			List<TaAvisEcheanceDTO> instance = (List<TaAvisEcheanceDTO>)query.getResultList();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			System.out.println(re);
		    return null;
		}
	}
	
	public List<TaAvisEcheance> rechercheByIdAbonnement(Integer idAbo) {
		logger.debug("getting TaAvisEcheance instance with idAbo: " + idAbo);
		try {
			
			String jpql = "select distinct lae.taDocument "
			+ " from TaLAvisEcheance lae "
			+ " join lae.taLEcheance ech "
			+ " where ech.taAbonnement.idDocument = :idAbo";
			
			
			Query query = entityManager.createQuery(jpql);
			query.setParameter("idAbo",idAbo);
			List<TaAvisEcheance> instance = (List<TaAvisEcheance>)query.getResultList();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public void removeAllRDocumentEvenementAvisEcheance() {
		String nativeQuery = "delete from ta_r_document_evenement t where t.id_avis_echeance is not null";
		Query query = entityManager.createNativeQuery(nativeQuery);
		query.executeUpdate();
	}
	
	
	
	public List<TaAvisEcheance> rechercheAvisFaux() {
		logger.debug("getting rechercheAvisFaux");
		try {
			
//			select distinct avis from ta_l_echeance tle 
//			join ta_abonnement ta on tle.id_abonnement = ta.id_document 
//			join ta_l_abonnement tla on tle.id_l_abonnement = tla.id_l_document
//			join ta_l_avis_echeance tlae2 on tlae2.id_l_echeance=tle.id_l_echeance
//			join ta_avis_echeance avis on avis.id_document = tlae2.id_document 
//			where (tle.id_abonnement != tla.id_document or  tle.id_article != tla.id_article) and exists (select * from ta_l_avis_echeance tlae where tlae.id_l_echeance=tle.id_l_echeance);
//			
			String jpql = "select distinct avis from TaLEcheance tle "
			+ " join tle.taAbonnement ta"
			+ " join tle.taLAbonnement tla"
			+ " join TaLAvisEcheance tlae2 on tle.idLEcheance = tlae2.taLEcheance.idLEcheance"
			+ " join tlae2.taDocument avis"
			+ " where(tle.taAbonnement.idDocument != tla.taDocument.idDocument or  tle.taArticle.idArticle != tla.taArticle.idArticle) and exists (select tlae from TaLAvisEcheance tlae where tlae.taLEcheance.idLEcheance = tle.idLEcheance)";
			
			
			Query query = entityManager.createQuery(jpql);
			List<TaAvisEcheance> instance = (List<TaAvisEcheance>)query.getResultList();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	
	
	public List<TaAvisEcheance> rechercheDocumentSubscription(int idStripeSubscription) {
		logger.debug("getting TaAvisEcheance instance with idStripeSubscription: " + idStripeSubscription);
		try {
			
			String jpql = "select distinct lae.taDocument "
			+ " from TaLAvisEcheance lae "
			+ " join lae.taLEcheance ech "
			+ " where ech.taAbonnement.idDocument = :idStripeSubscription";
			
			
			Query query = entityManager.createQuery(jpql);
			query.setParameter("idStripeSubscription",idStripeSubscription);
			List<TaAvisEcheance> instance = (List<TaAvisEcheance>)query.getResultList();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaAvisEcheance> rechercheDocumentTAbonnement(Date dateDeb, Date datefin,
			String codeTAbonnement, String codeTiers , BigDecimal montantDepart, boolean nonTransforme) {
		boolean accept=true;
		List<TaAvisEcheance> result = new LinkedList<TaAvisEcheance>();
		List<TaAvisEcheance> temp = null;
		Query query = null;
		if(codeTAbonnement!=null)codeTAbonnement="%";
		if(codeTiers==null)codeTiers="%";            //*
		String querys="select a from TaAvisEcheance a  join a.lignes la  join la.taLEcheance ech join a.taTiers t where t.codeTiers like :codeTiers " +
				" and  exists(select le from TaLEcheance le join le.taAbonnement a join a.taTAbonnement ta  where " +
				"(le.debutPeriode between :dateDeb and :datefin )and(le.finPeriode between :dateDeb2 and :datefin2 ) and le.idLEcheance=ech.idLEcheance" +
				" and ta.codeTAbonnement like :codeTAbonnement ) and a.netHtCalc >= :montantDepart  and t.actifTiers=1" +
				" order by a.codeDocument";
		
		// 
		
		
		//			query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_TABONNEMENT_TIERS_DATE);
		query = entityManager.createQuery(querys);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb);
		query.setParameter("datefin", datefin);
		query.setParameter("dateDeb2", dateDeb);
		query.setParameter("datefin2", datefin);			
		query.setParameter("codeTAbonnement", codeTAbonnement);	
		query.setParameter("montantDepart", montantDepart);	
		temp = query.getResultList();
		for (TaAvisEcheance taAvisEcheance : temp) {
			accept=true;
			if(nonTransforme){
				for (TaRDocument rdoc : taAvisEcheance.getTaRDocuments()) {
					if(accept)accept=rdoc.getTaAvisEcheance()==null;
					if(accept && rdoc.getTaPrelevement()!=null){
						TaPrelevement prel = rdoc.getTaPrelevement(); 
						if(prel!=null){
							for (TaRDocument rdocPrel : prel.getTaRDocuments()) {
								if(accept)accept=rdocPrel.getTaAvisEcheance()==null;
							}
						}
					}
				}
			}
			if(!result.contains(taAvisEcheance)&& accept)result.add(taAvisEcheance);
		}
		return result;
	}	


	
	public class InfosSupport {
		String email;
		String groupe;
		Integer telechargement;
		
		public InfosSupport() {
			super();
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getGroupe() {
			return groupe;
		}

		public void setGroupe(String groupe) {
			this.groupe = groupe;
		}

		public Integer getTelechargement() {
			return telechargement;
		}

		public void setTelechargement(Integer telechargement) {
			this.telechargement = telechargement;
		}

		
	}
	
	




	public InfosSupport recupInfosSupport(Integer idDocument){ /*n'est pas utilisé dans la version actuelle, commenté par isa*/
//		InfosSupport infosSupport=new InfosSupport();
//		String groupeCogere = null;
//		TaSupportAbonDAO daoSupport=new TaSupportAbonDAO();
//		TaSupportAbon support=null;
//		LinkedList<String> listeEmail = new LinkedList<String>();
//		String jpql = " select tlf from TaLAvisEcheance tlf join tlf.taLEcheance le  join le.taAbonnement a"+ 
//				" join a.taSupportAbon ts  where tlf.taDocument.idDocument = "+idDocument ;
//		Query query = null;		
//		query = entityManager.createQuery(jpql);
//		List<TaLAvisEcheance> result = new LinkedList<TaLAvisEcheance>();
//		List<TaLAvisEcheance> temp = query.getResultList();
//		for (TaLAvisEcheance taAvisLEcheance : temp) {
//
//			support=daoSupport.findByCode(taAvisLEcheance.getTaLEcheance().getTaAbonnement().getTaSupportAbon().getCodeSupportAbon() );
//			if(support instanceof TaLicenceEpicea ||
//					support instanceof TaLicenceSara ||
//					support instanceof TaLicenceBdg ){
//				TaLicenceLogiciel logiciel =(TaLicenceLogiciel) support;
//				
//					if(logiciel.getTaUtilisateur()!=null && 
//							logiciel.getTaUtilisateur().getEmail()!=null){
//					if(infosSupport.email=="" || infosSupport.email==null)listeEmail.add(logiciel.getTaUtilisateur().getEmail());
//					else 
//					if(infosSupport.email!=null && infosSupport.email!="" && !listeEmail.contains(logiciel.getTaUtilisateur().getEmail()))			
//						listeEmail.add(logiciel.getTaUtilisateur().getEmail());
//					}
//					if((logiciel.getGroupe()!=null && 
//							logiciel.getGroupe().getCodeFamille()!=null)){			
//							 if(infosSupport.groupe=="" || infosSupport.groupe==null){
//								 infosSupport.groupe=logiciel.getGroupe().getCodeFamille();
//							 	if(infosSupport.groupe!=null && infosSupport.groupe.length()>=6 &&
//							 			infosSupport.groupe.toLowerCase().substring(0, 6).equals("cogere"))groupeCogere="cogere";
//							 }
////							else if(infosSupport.groupe!=null && infosSupport.groupe!="" && groupeCogere==null && logiciel.
////							getGroupe().getCodeFamille()!=(infosSupport.groupe)){
////								infosSupport.groupe="*";
////								}
//							 infosSupport.groupe=groupeCogere;
//							}
//					if((logiciel.getTelechargement()!=null)){
//						if(infosSupport.telechargement==null || infosSupport.telechargement==0){
//							infosSupport.telechargement=logiciel.getTelechargement();
//							}
//						}			
//				}
//			infosSupport.email="";	
//			for (String lemail : listeEmail) {
//				if (infosSupport.email!=null || infosSupport.email.equals(""))
//					infosSupport.email=infosSupport.email+lemail;
//					else infosSupport.email=infosSupport.email+";"+lemail;
//			}
//						
//			
//			}
//	
//	return infosSupport;		
return null;  /*n'est pas utilisé dans la version actuelle, commenté par isa*/
	}


	public List<TaAvisEcheance> rechercheAvisTelechargement(Date dateDeb, Date dateFin,Boolean telechargement,String groupe) {
		int download=LibConversion.booleanToInt(telechargement);
		List<TaAvisEcheance> result = null;
		
		Map<String,List<TaAvisEcheance>> listeFinale = new LinkedHashMap<String, List<TaAvisEcheance>>();
		
		TaFamilleTiers groupeCogere = daoFamilleTiers.rechercheFamilleCogere();
		
		List<TaAvisEcheance> listeTemp = new LinkedList<TaAvisEcheance>();
		String requete="";
		String comparaison ="like";
		String codeFamille="%";
		
		
		if(groupe != null && !groupe.equals("autres")){
			codeFamille=groupe;
			comparaison ="like";
		}
		else if(groupe != null && groupe.equals("autres")){
			comparaison ="<>";
			codeFamille=groupeCogere.getCodeFamille();
		}
		
//		if(telechargement){
			if(groupe != null && !groupe.equals("autres"))
			requete=" select av from TaAvisEcheance av where   av.dateDocument between :dateDeb and :dateFin  " +
					" and (exists(select l from TaLicenceEpicea l join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+" and g.codeFamille "+comparaison+" '"+codeFamille+"')" +
					" or exists(select l from TaLicenceBdg l join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+"  and g.codeFamille "+comparaison+" '"+codeFamille+"')" +
					" or exists(select l from TaLicenceSara l join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+"  and g.codeFamille "+comparaison+" '"+codeFamille+"'))" +
							" order by av.taTiers.codeTiers" ;
			else
				requete=" select av from TaAvisEcheance av where   av.dateDocument between :dateDeb and :dateFin  " +
						" and (exists(select l from TaLicenceEpicea l left join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+" and ((g is null) or (g.codeFamille "+comparaison+" '"+codeFamille+"')))" +
						" or exists(select l from TaLicenceBdg l left join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+"  and ((g is null) or (g.codeFamille "+comparaison+" '"+codeFamille+"')))" +
						" or exists(select l from TaLicenceSara l left join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+"  and ((g is null) or( g.codeFamille "+comparaison+" '"+codeFamille+"'))))" +
						" order by av.taTiers.codeTiers" ;

//			requete=" select av from TaAvisEcheance av where   av.dateDocument between ? and ?  " +
//					" and (exists(select l from TaLicenceEpicea l left join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+" )" +
//					" or exists(select l from TaLicenceBdg l left join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+"  )" +
//					" or exists(select l from TaLicenceSara l left join l.groupe g where l.taTiers=av.taTiers  and l.telechargement="+download+" ))" +
//					" order by av.taTiers.codeTiers" ;

		
		Query query = entityManager.createQuery(requete);
//		query.setParameter(1,"%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);

		result = query.getResultList();
		List<TaAvisEcheance> listeAvisVersion;
		String variableVersion="";
		String separateur="";
		for (TaAvisEcheance doc :result) {
			variableVersion="";
			separateur="";
			for (TaLAvisEcheance ligne : doc.getLignes()) {
				if(ligne.getTaArticle()!=null && ligne.getTaArticle().getTaFamille()!=null && 
						(ligne.getTaArticle().getTaFamille().getIdFamille()==21 || ligne.getTaArticle().getTaFamille().getIdFamille()==44)){
					if(!variableVersion.equals(""))separateur="||";
					variableVersion+=separateur+ligne.getTaArticle().getCodeArticle();
				}
			}
			//si variableVersion remplie, on la cherche dans la listeFinale ou on la rajoute
			if(!variableVersion.equals("")){
				if(!listeFinale.containsKey(variableVersion))listeFinale.put(variableVersion, new LinkedList<TaAvisEcheance>());
				//quand récupérer tous les codes articles de l'avis
				listeFinale.get(variableVersion).add(doc);
			}

		}
		result.clear();
for (String version : listeFinale.keySet()) {
	if(listeFinale.get(version).size()>0 && listeFinale.get(version).get(0)!=null)
		for (TaAvisEcheance taAvisEcheance : listeFinale.get(version)) {
			System.out.println(LibConversion.booleanToInt(telechargement)+";"+groupe+";"+version+";"+taAvisEcheance.getTaTiers().getCodeTiers());
			result.add(taAvisEcheance);
		}
		logger.debug("Téléchargement : "+LibConversion.booleanToStringFrancais(telechargement)+" - Groupe : "+groupe+" - Version : "+version+" - Nb : "+listeFinale.get(version).size()+" Premier code tiers : "+listeFinale.get(version).get(0).getTaTiers().getCodeTiers());
}
		return result;
	}
	



	/**
	 * Recherche les factures entre 2 dates light
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light) {
		List<Object[]> result = null;
		Query query = null;
		if(light)
			query =entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,String codeTiers) {
		List<Object[]> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers) {
			List<Object[]> result = null;
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}
	
	@Override
	public List<TaAvisEcheance> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAvisEcheance> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAvisEcheance> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAvisEcheance> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAvisEcheance value) throws Exception {
		BeanValidator<TaAvisEcheance> validator = new BeanValidator<TaAvisEcheance>(TaAvisEcheance.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAvisEcheance value, String propertyName)
			throws Exception {
		BeanValidator<TaAvisEcheance> validator = new BeanValidator<TaAvisEcheance>(TaAvisEcheance.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAvisEcheance transientInstance) {
		entityManager.detach(transientInstance);
	}

	//@Override
	public String genereCode() {
		System.err.println("******************* NON IMPLEMENTE ****************************************");
		return null;
	}

	@Override
	public List<TaAvisEcheance> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		logger.debug("TaAvisEcheance selectAll");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAvisEcheance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaAvisEcheance> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaAvisEcheance> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Date> findDateExport(Date dateDeb,Date dateFin) {
		return null;
	}

	@Override
	public List<TaAvisEcheance> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return "select r  from TaRDocument r  where r.taAvisEcheance = "+prefixe+" and ( r.taFacture IS NOT NULL " + 
				"	 OR r.taBonliv IS NOT NULL OR r.taBoncde IS NOT NULL)";
	}

	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return "doc.dateEchDocument";
	}

	@Override
	public String getRequeteARelancer() {
		// TODO Auto-generated method stub
		return " and doc.date_Ech_Document <= :dateRef and doc.date_Ech_Document >= :dateJour ";
	}

	
	@Override
	public String getRequeteARelancerJPQL() {
		// TODO Auto-generated method stub
		return " and doc.dateEchDocument <= :dateRef and doc.dateEchDocument >= :dateJour ";
	}

	@Override
	public List<TaAvisEcheanceDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_ALL_LIGHT);
			List<TaAvisEcheanceDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			if(codeTiers==null)codeTiers="%";
//			Query query = entityManager.createNamedQuery(TaAvisEcheance.QN.FIND_ALL_LIGHT_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
	public List<TaAbonnement> rechercheAbonnementDansAvisEcheance(TaAvisEcheance taAvisEcheance) {
		try {
			
			String jpql = "select abo "
					+ " from TaLAvisEcheance lae "
					+ " join lae.taLEcheance le "
					+ " join le.taAbonnement abo "
					+ " where lae.taDocument.idDocument="+taAvisEcheance.getIdDocument();
			Query query = entityManager.createQuery(jpql);
			List<TaAbonnement> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	

	@Override
	public TaAvisEcheance findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaAvisEcheance a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaAvisEcheance instance = (TaAvisEcheance)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}


	
	@Override
	public TaAvisEcheance findByIdFetch(int id) {
		logger.debug("getting TaAvisEcheance instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaAvisEcheance a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
//					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.idDocument="+id+" " +
					" order by l.numLigneLDocument");
			TaAvisEcheance instance = (TaAvisEcheance)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaAvisEcheance findByCodeFetch(String code) {
		logger.debug("getting TaAvisEcheance instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaAvisEcheance a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.codeDocument='"+code+"' " +
					" order by l.numLigneLDocument");
		
			TaAvisEcheance instance = (TaAvisEcheance)query.getSingleResult();
			instance.setLegrain(true);

			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		} catch (Exception e) {
			return null;
		}
	}

}
