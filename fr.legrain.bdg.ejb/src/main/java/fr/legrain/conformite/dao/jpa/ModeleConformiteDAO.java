package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IModeleConformiteDAO;
import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaModeleConformite.
 * @see fr.legrain.articles.dao.TaModeleConformite
 * @author Hibernate Tools
 */
public class ModeleConformiteDAO implements IModeleConformiteDAO {

	private static final Log logger = LogFactory.getLog(ModeleConformiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaModeleConformiteDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaModeleConformite f";
	
	public ModeleConformiteDAO(){
	}

	public void persist(TaModeleConformite transientInstance) {
		logger.debug("persisting TaModeleConformite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaModeleConformite persistentInstance) {
		logger.debug("removing TaModeleConformite instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdModeleConformite()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaModeleConformite merge(TaModeleConformite detachedInstance) {
		logger.debug("merging TaModeleConformite instance");
		try {
			TaModeleConformite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaModeleConformite findByCode(String code) {
		logger.debug("getting TaModeleConformite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaModeleConformite f where f.libelleConformite='"+code+"'");
			TaModeleConformite instance = (TaModeleConformite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaModeleConformite findById(int id) {
		logger.debug("getting TaModeleConformite instance with id: " + id);
		try {
			TaModeleConformite instance = entityManager.find(TaModeleConformite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaModeleConformite> selectAll() {
		logger.debug("selectAll TaModeleConformite");
		try {
			String jpql = "select f from TaModeleConformite f left join f.taGroupe g order by g.codeGroupe, f.libelleConformite";
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaModeleConformite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaModeleConformite> findByModeleGroupe(TaModeleGroupe taModeleGroupe) {
		logger.debug("getting TaModeleConformite instance with code TaModeleGroupe : " + taModeleGroupe.getCodeGroupe());
		try {//TaRGroupeModeleConformite TaModeleConformite taModeleGroupe

			//Query query = entityManager.createQuery("select f from TaModeleConformite f left join f.taModeleGroupe grp where grp.codeGroupe='"+taModeleGroupe.getCodeGroupe()+"'");
			Query query = entityManager.createQuery(""
					+ "select f.modeleConformite "
					+ "from TaRGroupeModeleConformite f left join f.modeleGroupe grp "
					+ "where grp.codeGroupe='"+taModeleGroupe.getCodeGroupe()+"' "
							+ "order by f.position"
							+ "");
//					+ "select f.modeleConformite "
//					+ "from TaRGroupeModeleConformite f left join f.modeleGroupe grp "
//					+ "where grp.codeGroupe='"+taModeleGroupe.getCodeGroupe()+"' "
//							+ " and f.modeleConformite.version = (select max(z.version) from TaModeleConformite z where  )"
//							+ "order by f.position"
//							+ "");
			List<TaModeleConformite> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
		    return null;
		}
	}

	public void ctrlSaisieSpecifique(TaModeleConformite entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaModeleConformite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaModeleConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaModeleConformite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaModeleConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaModeleConformite value) throws Exception {
		BeanValidator<TaModeleConformite> validator = new BeanValidator<TaModeleConformite>(TaModeleConformite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaModeleConformite value, String propertyName) throws Exception {
		BeanValidator<TaModeleConformite> validator = new BeanValidator<TaModeleConformite>(TaModeleConformite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaModeleConformite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
