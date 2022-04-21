package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.model.TaEtatStock;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.documents.dao.ILPreparationDAO;
import fr.legrain.lib.data.LibDate;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLPreparation.
 * @see fr.legrain.documents.dao.TaLPreparation
 * @author Hibernate Tools
 */
public class TaLPreparationDAO /*extends AbstractApplicationDAO<TaLPreparation>*/ implements ILPreparationDAO{

//	private static final Log log = LogFactory.getLog(TaLPreparationDAO.class);
	static Logger logger = Logger.getLogger(TaLPreparationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
//	private String JPQLQueryMouvDocumentSorties=
//	 "select L.taDocument.dateDocument,L.taArticle.codeArticle,sum(-1*L.qteLDocument),"+
//	 " L.u1LDocument,sum(-1*L.qte2LDocument),L.u2LDocument  "+
//	 "from TaLPreparation L "+
//	 " where L.taDocument.idDocument not in (select rdoc.taPreparation.idDocument from TaRDocument rdoc) "+
//	 " and (L.u1LDocument='' "+
//	 " or L.u1LDocument in (select u.codeUnite from TaUnite u)) "+
//	 " and L.taArticle.codeArticle is not null  "+ 
//	 " group by  L.taDocument.dateDocument,L.taArticle.codeArticle, L.u1LDocument, L.u2LDocument";

	private String defaultJPQLQuery = "select a from TaLPreparation a";
	
	public TaLPreparationDAO(){
//		this(null);
	}
	
//	public TaLPreparationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLPreparation.class.getSimpleName());
//		initChampId(TaLPreparation.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLPreparation());
//	}
	
//	public TaLPreparation refresh(TaLPreparation detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLPreparation.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLPreparation transientInstance) {
		logger.debug("persisting TaLPreparation instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLPreparation persistentInstance) {
		logger.debug("removing TaLPreparation instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLPreparation merge(TaLPreparation detachedInstance) {
		logger.debug("merging TaLPreparation instance");
		try {
			TaLPreparation result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLPreparation findById(int id) {
		logger.debug("getting TaLPreparation instance with id: " + id);
		try {
			TaLPreparation instance = entityManager.find(TaLPreparation.class, id);
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
	public List<TaLPreparation> selectAll() {
		logger.debug("selectAll TaLPreparation");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLPreparation> l = ejbQuery.getResultList();
			for (TaLPreparation taLPreparation : l) {
				for (TaLigneALigne ll : taLPreparation.getTaLigneALignes()) {
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
	public TaLPreparation findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLPreparation> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLPreparation> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLPreparation> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLPreparation> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLPreparation value) throws Exception {
		BeanValidator<TaLPreparation> validator = new BeanValidator<TaLPreparation>(TaLPreparation.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLPreparation value, String propertyName)
			throws Exception {
		BeanValidator<TaLPreparation> validator = new BeanValidator<TaLPreparation>(TaLPreparation.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLPreparation transientInstance) {
		entityManager.detach(transientInstance);
	}
	


}
