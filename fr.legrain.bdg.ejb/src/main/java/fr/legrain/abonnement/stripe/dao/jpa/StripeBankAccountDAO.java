package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeBankAccountDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeBankAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeBankAccountDAO implements IStripeBankAccountDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeBankAccountDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeBankAccount u";
	//private String defaultJPQLQuery = "select u from TaStripeBankAccount u where u.taArticle is null";
	
	public StripeBankAccountDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeBankAccount)
	 */
	public void persist(TaStripeBankAccount transientInstance) {
		logger.debug("persisting TaStripeBankAccount instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeBankAccount)
	 */
	public void remove(TaStripeBankAccount persistentInstance) {
		logger.debug("removing TaStripeBankAccount instance");
		try {
			TaStripeBankAccount e = entityManager.merge(findById(persistentInstance.getIdStripeBankAccount()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeBankAccount)
	 */
	public TaStripeBankAccount merge(TaStripeBankAccount detachedInstance) {
		logger.debug("merging TaStripeBankAccount instance");
		try {
			TaStripeBankAccount result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeBankAccount findByCode(String idExterne) {
		logger.debug("getting TaStripeBankAccount instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeBankAccount f where f.idExterne='"+idExterne+"' ");
				TaStripeBankAccount instance = (TaStripeBankAccount)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeBankAccount findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeBankAccount instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeBankAccount f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeBankAccount instance = (TaStripeBankAccount)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaStripeBankAccount findById(int id) {
		logger.debug("getting TaStripeBankAccount instance with id: " + id);
		try {
			TaStripeBankAccount instance = entityManager.find(TaStripeBankAccount.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeBankAccount> selectAll() {
		logger.debug("selectAll TaStripeBankAccount");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeBankAccount> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeBankAccount refresh(TaStripeBankAccount detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeBankAccount.class, detachedInstance.getIdStripeBankAccount());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeBankAccount> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeBankAccount instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeBankAccount.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeBankAccount> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeBankAccountDAO,TaStripeBankAccount> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeBankAccountDAO,TaStripeBankAccount>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeBankAccountDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeBankAccount.QN.FIND_ALL_LIGHT);
//			List<TaStripeBankAccountDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeBankAccountDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeBankAccountDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeBankAccount.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeBankAccount.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeBankAccountDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeBankAccount entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeBankAccount> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeBankAccount> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeBankAccount> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeBankAccount> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeBankAccount value) throws Exception {
		BeanValidator<TaStripeBankAccount> validator = new BeanValidator<TaStripeBankAccount>(TaStripeBankAccount.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeBankAccount value, String propertyName) throws Exception {
		BeanValidator<TaStripeBankAccount> validator = new BeanValidator<TaStripeBankAccount>(TaStripeBankAccount.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeBankAccount transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
