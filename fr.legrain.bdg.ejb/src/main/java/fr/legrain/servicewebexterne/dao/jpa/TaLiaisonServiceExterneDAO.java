package fr.legrain.servicewebexterne.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.servicewebexterne.dao.ITaLiaisonServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.validator.BeanValidator;

public class TaLiaisonServiceExterneDAO implements ITaLiaisonServiceWebExterneDAO {
	
static Logger logger = Logger.getLogger(TaLiaisonServiceExterneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLiaisonServiceExterne a";
	
	public TaLiaisonServiceExterneDAO(){
	}
	//@Transactional(value=TxType.REQUIRES_NEW)
	public TaLiaisonServiceExterne findByRefTaTPaiementAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
				logger.debug("findByRefTaTPaiementAndByIdServiceWebExterne TaLiaisonServiceExterne");
				String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT;
				try {
					String jpql = null;
					jpql = "select li from TaLiaisonServiceExterne li where li.refExterne = :refEntite and li.typeEntite = :typeEntite and li.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne" ;
					Query query = entityManager.createQuery(jpql);
					query.setParameter("refEntite", refEntite);
					query.setParameter("typeEntite", typeEntite);
					query.setParameter("idServiceWebExterne", idServiceWebExterne);
					TaLiaisonServiceExterne l = (TaLiaisonServiceExterne) query.getSingleResult();
					logger.debug("findByRefTaTPaiementAndByIdServiceWebExterne successful");
					return l;
				} catch (RuntimeException re) {
					//logger.error("findByRefArticleAndByIdServiceWebExterne failed", re);
					//throw re;
					return null;
				}
	}
	//@Transactional(value=TxType.REQUIRES_NEW)
	public TaLiaisonServiceExterne findByRefArticleAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
				logger.debug("findByRefArticleAndByIdServiceWebExterne TaLiaisonServiceExterne");
				String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE;
				try {
					String jpql = null;
					jpql = "select li from TaLiaisonServiceExterne li where li.refExterne = :refEntite and li.typeEntite = :typeEntite and li.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne" ;
					Query query = entityManager.createQuery(jpql);
					query.setParameter("refEntite", refEntite);
					query.setParameter("typeEntite", typeEntite);
					query.setParameter("idServiceWebExterne", idServiceWebExterne);
					TaLiaisonServiceExterne l = (TaLiaisonServiceExterne) query.getSingleResult();
					logger.debug("findByRefArticleAndByIdServiceWebExterne successful");
					return l;
				} catch (RuntimeException re) {
					//logger.error("findByRefArticleAndByIdServiceWebExterne failed", re);
					//throw re;
					return null;
				}
	}
//	@Transactional(value=TxType.REQUIRES_NEW)
	public TaLiaisonServiceExterne findByRefTiersAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
				logger.debug("findByRefTiersAndByIdServiceWebExterne TaLiaisonServiceExterne");
				String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_TIERS;
				try {
					String jpql = null;
					jpql = "select li from TaLiaisonServiceExterne li where li.refExterne = :refEntite and li.typeEntite = :typeEntite and li.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne" ;
					Query query = entityManager.createQuery(jpql);
					query.setParameter("refEntite", refEntite);
					query.setParameter("typeEntite", typeEntite);
					query.setParameter("idServiceWebExterne", idServiceWebExterne);
					TaLiaisonServiceExterne l = (TaLiaisonServiceExterne) query.getSingleResult();
					logger.debug("findByRefTiersAndByIdServiceWebExterne successful");
					return l;
				} catch (RuntimeException re) {
					//logger.error("findByRefArticleAndByIdServiceWebExterne failed", re);
					//throw re;
					return null;
				}
	}
	
	public TaLiaisonServiceExterne findByIdEntityByTypeEntiteByIdServiceWebExterne(Integer idEntite, String typeEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
		logger.debug("findAllByIdEntityByTypeEntiteByIdServiceWebExterne TaLiaisonServiceExterne");
		try {
			String jpql = null;
			jpql = "select li from TaLiaisonServiceExterne li where li.idEntite = :idEntite and li.typeEntite = :typeEntite and li.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne" ;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("idEntite", idEntite);
			query.setParameter("typeEntite", typeEntite);
			query.setParameter("idServiceWebExterne", idServiceWebExterne);
			TaLiaisonServiceExterne l = (TaLiaisonServiceExterne) query.getSingleResult();
			logger.debug("findAllByIdEntityByTypeEntiteByIdServiceWebExterne successful");
			return l;
		} catch (RuntimeException re) {
			//logger.error("findAllByIdEntityByTypeEntiteByIdServiceWebExterne failed", re);
			//throw re;
			return null;
		}
	}
	
	public List<TaLiaisonServiceExterne> findAllByIdTiers(Integer idEntite) {
		// TODO Auto-generated method stub
		logger.debug("findAllByIdTiers TaLiaisonServiceExterne");
		String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_TIERS;
		try {
			String jpql = null;
			jpql = "select li from TaLiaisonServiceExterne li where li.idEntite = :idEntite and li.typeEntite = :typeEntite" ;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("idEntite", idEntite);
			query.setParameter("typeEntite", typeEntite);
			List<TaLiaisonServiceExterne> l =  query.getResultList();
			logger.debug("findAllByIdTiers successful");
			return l;
		} catch (RuntimeException re) {
			//logger.error("findAllByIdTiers failed", re);
			throw re;
		}
	}
	
	public List<TaLiaisonServiceExterne> findAllByIdArticle(Integer idEntite) {
		// TODO Auto-generated method stub
		logger.debug("findAllByIdArticle TaLiaisonServiceExterne");
		String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE;
		try {
			String jpql = null;
			jpql = "select li from TaLiaisonServiceExterne li where li.idEntite = :idEntite and li.typeEntite = :typeEntite" ;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("idEntite", idEntite);
			query.setParameter("typeEntite", typeEntite);
			List<TaLiaisonServiceExterne> l =  query.getResultList();
			logger.debug("findAllByIdArticle successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByIdArticle failed", re);
			throw re;
		}
	}
	
	public List<TaLiaisonServiceExterneDTO> findAllByTypeEntiteAndByIdServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
		logger.debug("findAllByTypeEntiteAndByIdServiceWebExterneDTO TaLiaisonServiceExterne");
		try {
			Query query = null;
			if(typeEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS)) {
				query =  entityManager.createNamedQuery(TaLiaisonServiceExterne.QN.FIND_ALL_BY_TYPE_TIERS_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO);
			}else if(typeEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE)) {
				query =  entityManager.createNamedQuery(TaLiaisonServiceExterne.QN.FIND_ALL_BY_TYPE_ARTICLE_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO);
			}else if(typeEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT)) {
				query =  entityManager.createNamedQuery(TaLiaisonServiceExterne.QN.FIND_ALL_BY_TYPE_T_PAIEMENT_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO);
			}else {
				return null;
			}
			
			query.setParameter("idServiceWebExterne", idServiceWebExterne);
			query.setParameter("typeEntite", typeEntite);
			List<TaLiaisonServiceExterneDTO> l =  query.getResultList();
			logger.debug("findAllByTypeEntiteAndByIdServiceWebExterneDTO successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByTypeEntiteAndByIdServiceWebExterneDTO failed", re);
			throw re;
		}
	}
	
	public List<TaLiaisonServiceExterne> findAllByTypeEntiteAndByIdServiceWebExterne(String typeEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
		logger.debug("findAllByTypeEntiteAndByIdServiceWebExterne TaLiaisonServiceExterne");
		try {
			String jpql = null;
			jpql = "select li from TaLiaisonServiceExterne li where li.typeEntite = :typeEntite and li.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne" ;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("idServiceWebExterne", idServiceWebExterne);
			query.setParameter("typeEntite", typeEntite);
			List<TaLiaisonServiceExterne> l =  query.getResultList();
			logger.debug("findAllByTypeEntiteAndByIdServiceWebExterne successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByTypeEntiteAndByIdServiceWebExterne failed", re);
			throw re;
		}
	}
	
	public TaLiaisonServiceExterne findByIdTaTPaiementAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
		logger.debug("findByIdTiersAndByIdServiceWebExterne TaLiaisonServiceExterne");
		String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT;
		try {
			return findByIdEntityByTypeEntiteByIdServiceWebExterne(idEntite, typeEntite, idServiceWebExterne);
		} catch (RuntimeException re) {
			//logger.error("findAllByIdTiers failed", re);
			//throw re;
			return null;
		}
	}
	
	public TaLiaisonServiceExterne findByIdTiersAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
		logger.debug("findByIdTiersAndByIdServiceWebExterne TaLiaisonServiceExterne");
		String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_TIERS;
		try {
			return findByIdEntityByTypeEntiteByIdServiceWebExterne(idEntite, typeEntite, idServiceWebExterne);
		} catch (RuntimeException re) {
			//logger.error("findAllByIdTiers failed", re);
			//throw re;
			return null;
		}
	}
	public TaLiaisonServiceExterne findByIdArticleAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
		logger.debug("findByIdArticleAndByIdServiceWebExterne TaLiaisonServiceExterne");
		String typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE;
		try {
			return findByIdEntityByTypeEntiteByIdServiceWebExterne(idEntite, typeEntite, idServiceWebExterne);
		} catch (RuntimeException re) {
			//logger.error("findAllByIdTiers failed", re);
		//	throw re;
			return null;
		}
	}
	

	
	
	
	@Override
	public void persist(TaLiaisonServiceExterne transientInstance) {
		logger.debug("persisting TaLiaisonServiceExterne instance");
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
	public void remove(TaLiaisonServiceExterne persistentInstance) {
		logger.debug("removing TaLiaisonServiceExterne instance");
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
	public TaLiaisonServiceExterne merge(TaLiaisonServiceExterne detachedInstance) {
		logger.debug("merging TaLiaisonServiceExterne instance");
		try {
			TaLiaisonServiceExterne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaLiaisonServiceExterne findById(int id) {
		logger.debug("getting TaLiaisonServiceExterne instance with id: " + id);
		try {
			TaLiaisonServiceExterne instance = entityManager.find(TaLiaisonServiceExterne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaLiaisonServiceExterne> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLiaisonServiceExterne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLiaisonServiceExterne> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLiaisonServiceExterne> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLiaisonServiceExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLiaisonServiceExterne> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLiaisonServiceExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public boolean validate(TaLiaisonServiceExterne value) throws Exception {
		BeanValidator<TaLiaisonServiceExterne> validator = new BeanValidator<TaLiaisonServiceExterne>(TaLiaisonServiceExterne.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLiaisonServiceExterne value, String propertyName) throws Exception {
		BeanValidator<TaLiaisonServiceExterne> validator = new BeanValidator<TaLiaisonServiceExterne>(TaLiaisonServiceExterne.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLiaisonServiceExterne transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaLiaisonServiceExterne findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}



}
