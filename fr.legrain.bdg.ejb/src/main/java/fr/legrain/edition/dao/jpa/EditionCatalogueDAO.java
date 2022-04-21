package fr.legrain.edition.dao.jpa;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.edition.dao.IEditionCatalogueDAO;
import fr.legrain.edition.dao.IEditionDAO;
import fr.legrain.edition.dto.TaEditionCatalogueDTO;
import fr.legrain.edition.model.TaEditionCatalogue;
import fr.legrain.edition.model.TaTEditionCatalogue;
import fr.legrain.validator.BeanValidator;

public class EditionCatalogueDAO implements IEditionCatalogueDAO {

	static Logger logger = Logger.getLogger(EditionCatalogueDAO.class);
	
	@PersistenceContext(unitName = "bdg_prog")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaEditionCatalogue a";
	
	public EditionCatalogueDAO(){
	}

//	public TaEditionCatalogue refresh(TaEditionCatalogue detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaEditionCatalogue.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaEditionCatalogue transientInstance) {
		logger.debug("persisting TaEditionCatalogue instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaEditionCatalogue persistentInstance) {
		logger.debug("removing TaEditionCatalogue instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaEditionCatalogue merge(TaEditionCatalogue detachedInstance) {
		logger.debug("merging TaEditionCatalogue instance");
		try {
			TaEditionCatalogue result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaEditionCatalogue findById(int id) {
		logger.debug("getting TaEditionCatalogue instance with id: " + id);
		try {
			TaEditionCatalogue instance = entityManager.find(TaEditionCatalogue.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEditionCatalogue> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEditionCatalogue");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEditionCatalogue> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees) {
		logger.debug("selectAll TaEditionCatalogue");
		try {
			
			String jpql = "select "
					+ " new fr.legrain.edition.dto.TaEditionCatalogueDTO("
					+ " f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) "
					+ " from TaEditionCatalogue f ";
//					if(codeTypeEdition!=null) {
						jpql += " left join f.taTEdition t ";
//					}
					if(idAction!=null) {
						jpql += " join f.actionInterne act ";
					}
			
					jpql += " where (f.codeDossierEditionPersonalisee=:codeDoss or f.codeDossierEditionPersonalisee=null) ";
					
					if(idAction!=null) {
						jpql += " act.idAction=:action ";
					}
					
					jpql += " order by f.codeEdition";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("codeDoss", codeDossier);
			if(idAction!=null) {
				ejbQuery.setParameter("action", idAction);
			}
			
			
//			Query ejbQuery = entityManager.createNamedQuery(TaEditionCatalogue.QN.FIND_ALL_LIGHT);
			
			List<TaEditionCatalogueDTO> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, Map<String,String> mapCodeEditionDejaImporteesVersion) {
		logger.debug("selectAll TaEditionCatalogue");
		try {
			Query ejbQuery = entityManager.createNamedQuery(TaEditionCatalogue.QN.FIND_ALL_LIGHT);
			
			List<TaEditionCatalogueDTO> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	

	
	@Override
	public List<TaEditionCatalogue> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEditionCatalogue> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEditionCatalogue> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEditionCatalogue> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEditionCatalogue value) throws Exception {
		BeanValidator<TaEditionCatalogue> validator = new BeanValidator<TaEditionCatalogue>(TaEditionCatalogue.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEditionCatalogue value, String propertyName) throws Exception {
		BeanValidator<TaEditionCatalogue> validator = new BeanValidator<TaEditionCatalogue>(TaEditionCatalogue.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEditionCatalogue transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaEditionCatalogue findByCode(String code) {
		logger.debug("getting TaEditionCatalogue instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaEditionCatalogue a where a.codeEdition='"+code+"'");
				TaEditionCatalogue instance = (TaEditionCatalogue)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
	public List<TaEditionCatalogueDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			query = entityManager.createNamedQuery(TaEditionCatalogue.QN.FIND_ALL_LIGHT);
			
			List<TaEditionCatalogueDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaEditionCatalogueDTO findByCodeLight(String code) {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			query = entityManager.createNamedQuery(TaEditionCatalogue.QN.FIND_BY_CODE_LIGHT);
			query.setParameter("codeEdition", code);
			
			TaEditionCatalogueDTO l = (TaEditionCatalogueDTO) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
