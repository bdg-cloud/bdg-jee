package fr.legrain.documents.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.documents.dao.ILPanierDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLPanier.
 * @see fr.legrain.documents.dao.TaLPanier
 * @author Hibernate Tools
 */
public class TaLPanierDAO /*extends AbstractApplicationDAO<TaLPanier>*/ implements ILPanierDAO{

//	private static final Log log = LogFactory.getLog(TaLPanierDAO.class);
	static Logger logger = Logger.getLogger(TaLPanierDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
//	private String JPQLQueryMouvDocumentSorties=
//	 "select L.taDocument.dateDocument,L.taArticle.codeArticle,sum(-1*L.qteLDocument),"+
//	 " L.u1LDocument,sum(-1*L.qte2LDocument),L.u2LDocument  "+
//	 "from TaLPanier L "+
//	 " where L.taDocument.idDocument not in (select rdoc.taPanier.idDocument from TaRDocument rdoc) "+
//	 " and (L.u1LDocument='' "+
//	 " or L.u1LDocument in (select u.codeUnite from TaUnite u)) "+
//	 " and L.taArticle.codeArticle is not null  "+ 
//	 " group by  L.taDocument.dateDocument,L.taArticle.codeArticle, L.u1LDocument, L.u2LDocument";

	private String defaultJPQLQuery = "select a from TaLPanier a";
	
	public TaLPanierDAO(){
//		this(null);
	}
	
//	public TaLPanierDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLPanier.class.getSimpleName());
//		initChampId(TaLPanier.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLPanier());
//	}
	
//	public TaLPanier refresh(TaLPanier detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLPanier.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLPanier transientInstance) {
		logger.debug("persisting TaLPanier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLPanier persistentInstance) {
		logger.debug("removing TaLPanier instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLPanier merge(TaLPanier detachedInstance) {
		logger.debug("merging TaLPanier instance");
		try {
			TaLPanier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLPanier findById(int id) {
		logger.debug("getting TaLPanier instance with id: " + id);
		try {
			TaLPanier instance = entityManager.find(TaLPanier.class, id);
			for (TaLigneALigne l : instance.getTaLigneALignes()) {
					l.getId();
			}
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
	public List<TaLPanier> selectAll() {
		logger.debug("selectAll TaLPanier");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLPanier> l = ejbQuery.getResultList();
			for (TaLPanier taLPanier : l) {
				for (TaLigneALigne ll : taLPanier.getTaLigneALignes()) {
						ll.getId();
				}
			}

			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public TaLPanier findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLPanier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLPanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLPanier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLPanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLPanier value) throws Exception {
		BeanValidator<TaLPanier> validator = new BeanValidator<TaLPanier>(TaLPanier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLPanier value, String propertyName)
			throws Exception {
		BeanValidator<TaLPanier> validator = new BeanValidator<TaLPanier>(TaLPanier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLPanier transientInstance) {
		entityManager.detach(transientInstance);
	}
	


}
