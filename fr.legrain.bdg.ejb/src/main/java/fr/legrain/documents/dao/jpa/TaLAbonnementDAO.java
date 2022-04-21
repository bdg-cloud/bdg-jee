package fr.legrain.documents.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.documents.dao.ILAbonnementDAO;
import fr.legrain.documents.dao.ILDevisDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLAbonnement.
 * @see fr.legrain.documents.dao.TaLAbonnement
 * @author Hibernate Tools
 */
public class TaLAbonnementDAO /*extends AbstractApplicationDAO<TaLAbonnement>*/ implements ILAbonnementDAO{

//	private static final Log log = LogFactory.getLog(TaLAbonnementDAO.class);
	static Logger logger = Logger.getLogger(TaLAbonnementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLAbonnement a";
	
	public TaLAbonnementDAO(){
//		this(null);
	}
	
//	public TaLAbonnementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLAbonnement.class.getSimpleName());
//		initChampId(TaLAbonnement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLAbonnement());
//	}
	
//	public TaLAbonnement refresh(TaLAbonnement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLAbonnement.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLAbonnement transientInstance) {
		logger.debug("persisting TaLAbonnement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLAbonnement persistentInstance) {
		logger.debug("removing TaLAbonnement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLAbonnement merge(TaLAbonnement detachedInstance) {
		logger.debug("merging TaLAbonnement instance");
		try {
			TaLAbonnement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLAbonnement findById(int id) {
		logger.debug("getting TaLAbonnement instance with id: " + id);
		try {
			TaLAbonnement instance = entityManager.find(TaLAbonnement.class, id);
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
	public List<TaLAbonnement> selectAll() {
		logger.debug("selectAll TaLAbonnement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLAbonnement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public List<TaLAbonnementDTO> selectAllDTOAvecEtat() {
		logger.debug("getting selectAllDTOAvecEtat: ");
		try {
			String jpql = null;
			jpql = "select new fr.legrain.document.dto.TaLAbonnementDTO("
						+ " f.idLDocument, abo.codeDocument,art.codeArticle, f.libLDocument,f.qteLDocument,f.u1LDocument, f.dateDebutAbonnement, tiers.codeTiers, infos.nomTiers,infos.prenomTiers, "
						+ " f.mtHtLDocument,f.mtTtcLDocument, f.complement1, f.complement2, f.complement3, f.commissionBancaire, f.commissionPanier, f.nomDossier, tat.codeTLigne, etat.codeEtat, etat.liblEtat )"
						+ " from TaLAbonnement f "
						+ " left join f.taREtatLigneDocument retat "
						+ " left join retat.taEtat etat "
						+ " left join f.taPlan plan "
						+ " join f.taDocument abo "
						+ " join f.taArticle art "
						+ " join f.taTLigne tat "
						+ " join abo.taInfosDocument infos"
						+ " join abo.taTiers tiers";
				
			
			Query query = entityManager.createQuery(jpql);
			List<TaLAbonnementDTO> instance = (List<TaLAbonnementDTO>)query.getResultList();
			logger.debug("get successful");
			return instance;
			
		} catch (RuntimeException re) {
			logger.debug(re);
			System.out.println(re);
		    return null;
		}
	}
	public BigDecimal sumTtcLigneAboEnCoursEtSuspendu() {
		logger.debug("getting TaLAbonnement with id ligne echeance");
		BigDecimal result = null;
			
		try {
			String requete = "";

			requete = "SELECT SUM(l.mtTtcLDocument)"
				+ " from TaLAbonnement l"
				+ " join l.taREtatLigneDocument retat"
				+ " join retat.taEtat etat"
				+ " where etat.codeEtat = 'doc_encours' or etat.codeEtat = 'doc_suspendu'";
			
			
			Query query = entityManager.createQuery(requete);
			result = new BigDecimal(query.getSingleResult().toString());
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaLAbonnement findByIdLEcheance(Integer id) {
		logger.debug("getting TaLAbonnement with id ligne echeance");
		TaLAbonnement result = null;
			
		try {
			String requete = "";

			requete = "SELECT "
				+" ech.taLAbonnement"
				+" FROM TaLEcheance ech"
				+" where ech.idLEcheance = :id";
			
			
			Query query = entityManager.createQuery(requete);
			query.setParameter("id", id);
			result = (TaLAbonnement) query.getSingleResult();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaLAbonnement> findAllAnnule(){
		logger.debug("findAllAnnule");
		List<TaLAbonnement> result = null;
		try {
			String jpql = ""
					+ "select e from TaLAbonnement e "
					+ " join fetch e.taREtatLigneDocument retat"
					+ " join fetch retat.taEtat etat"
					+ " where etat.codeEtat = 'doc_abandonne' ";
			Query query = entityManager.createQuery(jpql);
			result = (List<TaLAbonnement>) query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaLAbonnement> findAllSansEtat(){
		logger.debug("findAllSansEtat TaLAbonnement");
		try {
			String jpql = null;
			jpql = "select li from TaLAbonnement li left join li.taREtatLigneDocument retat where retat = null" ;
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLAbonnement> l = ejbQuery.getResultList();
			logger.debug("findAllSansEtat successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllSansEtat failed", re);
			throw re;
		}
	}
	public List<TaLAbonnement> findAllByIdAbonnement(Integer id) {
		logger.debug("getting TaLAbonnement with id ligne echeance");
		List<TaLAbonnement> result = null;
			
		try {
			String requete = "";

			requete = "SELECT "
				+" li "
				+" FROM TaLAbonnement li"
				+" where li.taDocument.idDocument = :id";
			
			
			Query query = entityManager.createQuery(requete);
			query.setParameter("id", id);
			result = (List<TaLAbonnement>) query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public TaLAbonnement findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLAbonnement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLAbonnement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLAbonnement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLAbonnement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLAbonnement value) throws Exception {
		BeanValidator<TaLAbonnement> validator = new BeanValidator<TaLAbonnement>(TaLAbonnement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLAbonnement value, String propertyName)
			throws Exception {
		BeanValidator<TaLAbonnement> validator = new BeanValidator<TaLAbonnement>(TaLAbonnement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLAbonnement transientInstance) {
		entityManager.detach(transientInstance);
	}
}
