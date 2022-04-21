package fr.legrain.documents.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.ejb.FinderException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaRelanceDTO;
import fr.legrain.document.model.TaRelance;
import fr.legrain.document.model.TaTRelance;
import fr.legrain.documents.dao.IRelanceDAO;
import fr.legrain.documents.dao.ITRelanceDAO;
import fr.legrain.lib.data.ExceptLgr;

public class TaRelanceDAO /*extends AbstractApplicationDAO<TaRelance>*/ implements IRelanceDAO {

	//private static final Log logger = LogFactory.getLog(TaTLiensDAO.class);
	static Logger logger = Logger.getLogger(TaRelanceDAO.class);
	
	@Inject private ITRelanceDAO daoTRelance ;
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaRelance a ";
	
	public TaRelanceDAO() {
//		this(null);
	}

	
	public void persist(TaRelance transientInstance) {
		logger.debug("persisting TaRelance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRelance persistentInstance) {
		logger.debug("removing TaRelance instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRelance merge(TaRelance detachedInstance) {
		logger.debug("merging TaRelance instance");
		try {
			TaRelance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRelance findById(int id) {
		logger.debug("getting TaRelance instance with id: " + id);
		try {
			TaRelance instance = entityManager.find(TaRelance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaRelance findByCode(String code) {
		logger.debug("getting TaRelance instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaRelance f where f.codeRelance='"+code+"'");
				TaRelance instance = (TaRelance)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
//	@Override
	public List<TaRelance> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaRelance");
		try {  
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRelance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	/**
	 * Récupère le type de relance supérieur au type maxi déjà relancé
	 * s'il n'y en a pas au dessus, renvoie le max
	 * s'il n'y a pas de type de relance possible, renvoie null.
	 * @param taLRelance
	 * @return
	 */
	public TaTRelance maxTaTRelance(IDocumentTiers taDocument){
		try {  
			String requete="select max(rt.ordreTRelance) from TaRelance r " +
					" join r.taLRelances rl" +
					" join rl.taTRelance rt" +
					" where rl.codeDocument like :codeDocument";
			Integer l=null;
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("codeDocument", taDocument.getCodeDocument());
			if(ejbQuery.getSingleResult()!=null){
				l = (Integer)ejbQuery.getSingleResult();
			}else{
				l = -1;
			}
			return daoTRelance.taTRelanceSuperieur(l);
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaRelance entity,String field) throws ExceptLgr {	
		
	}

	@Override
	public List<TaRelance> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRelance> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaRelance value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaRelance value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaRelance transientInstance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaRelanceDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}
}
