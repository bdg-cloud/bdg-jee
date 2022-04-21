package fr.legrain.servicewebexterne.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.dao.ICompteServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaCompteServiceWebExterneDTO;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.validator.BeanValidator;

public class CompteServiceWebExterneDAO implements ICompteServiceWebExterneDAO {

	static Logger logger = Logger.getLogger(CompteServiceWebExterneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaCompteServiceWebExterne a";
	
	public CompteServiceWebExterneDAO(){
	}

//	public TaCompteServiceWebExterne refresh(TaCompteServiceWebExterne detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaCompteServiceWebExterne.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaCompteServiceWebExterne transientInstance) {
		logger.debug("persisting TaCompteServiceWebExterne instance");
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
	public void remove(TaCompteServiceWebExterne persistentInstance) {
		logger.debug("removing TaCompteServiceWebExterne instance");
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
	public TaCompteServiceWebExterne merge(TaCompteServiceWebExterne detachedInstance) {
		logger.debug("merging TaCompteServiceWebExterne instance");
		try {
			TaCompteServiceWebExterne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaCompteServiceWebExterne findById(int id) {
		logger.debug("getting TaCompteServiceWebExterne instance with id: " + id);
		try {
			TaCompteServiceWebExterne instance = entityManager.find(TaCompteServiceWebExterne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaCompteServiceWebExterne> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaCompteServiceWebExterne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCompteServiceWebExterne> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaCompteServiceWebExterne> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCompteServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCompteServiceWebExterne> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCompteServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCompteServiceWebExterne value) throws Exception {
		BeanValidator<TaCompteServiceWebExterne> validator = new BeanValidator<TaCompteServiceWebExterne>(TaCompteServiceWebExterne.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCompteServiceWebExterne value, String propertyName) throws Exception {
		BeanValidator<TaCompteServiceWebExterne> validator = new BeanValidator<TaCompteServiceWebExterne>(TaCompteServiceWebExterne.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCompteServiceWebExterne transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaCompteServiceWebExterne findByCode(String code) {
		logger.debug("getting TaServiceWebExterne instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaCompteServiceWebExterne f where UPPER(f.codeCompteServiceWebExterne)='"+ code.toUpperCase()+"'");
				TaCompteServiceWebExterne instance = (TaCompteServiceWebExterne)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	public List<TaCompteServiceWebExterne> findAgendaUtilisateur(TaUtilisateur utilisateur) {
		logger.debug("getting TaEvenement instance");
		try {		
			Query query = null;
			if(utilisateur!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select e from TaCompteServiceWebExterne e where e.proprietaire.id = :idUtilisateur";
				query = entityManager.createQuery(jpql);
				query.setParameter("idUtilisateur",utilisateur.getId());
			} 
			List<TaCompteServiceWebExterne> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCompteServiceWebExterneDTO> findByCodeLight(String code) throws Exception {
//		try {
//			Query query = null;
//
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaCompteServiceWebExterne.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaCompteServiceWebExterne.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaCompteServiceWebExterneDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		throw new Exception("pas encore implémenter");
	}
	
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne) {
		return findCompteDefautPourAction(codeTypeServiceWebExterne,false);
	}
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne, boolean compteTest) {
		/**
		 * PourType == PourAction tant que la gestion des actions n'est pas réellement implémentée
		 */
		return findCompteDefautPourType(codeTypeServiceWebExterne,false);
	}
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne) {
		return findCompteDefautPourType(codeTypeServiceWebExterne,false);
	}
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne, boolean compteTest) {
		try {		
			Query query = null;
			if(codeTypeServiceWebExterne!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select c from TaCompteServiceWebExterne c join c.taServiceWebExterne s join s.taTServiceWebExterne t where t.codeTServiceWebExterne = :codeTypeServiceWebExterne and s.actif=true and c.actif=true and c.compteTest=:compteTest and c.defaut=true";
				query = entityManager.createQuery(jpql);
				query.setParameter("codeTypeServiceWebExterne",codeTypeServiceWebExterne);
				query.setParameter("compteTest",compteTest);
			} 
			TaCompteServiceWebExterne l = (TaCompteServiceWebExterne) query.getSingleResult();
			logger.debug("get successful");
			return l;
		} catch (NoResultException e) {
			System.out.println("Aucun compte 'web service' paramétré sur ce dossier "+codeTypeServiceWebExterne);
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	public void changeDefaut(TaCompteServiceWebExterne detachedInstance) {
		try {
		final int changes =
		        entityManager.createQuery("update TaCompteServiceWebExterne c set c.defaut = false where c.idCompteServiceWebExterne in "
		        		+ " (select a.idCompteServiceWebExterne from TaCompteServiceWebExterne a join a.taServiceWebExterne s join s.taTServiceWebExterne t where t.codeTServiceWebExterne = :codeTypeServiceWebExterne and s.actif=true and a.actif=true and a.idCompteServiceWebExterne <> :id)")
		        .setParameter("codeTypeServiceWebExterne",detachedInstance.getTaServiceWebExterne().getTaTServiceWebExterne().getCodeTServiceWebExterne())
		        .setParameter("id", detachedInstance.getIdCompteServiceWebExterne())
		        .executeUpdate();
		} catch (Exception re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne){
		return findListeComptePourAction(codeTypeServiceWebExterne,false);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne, boolean compteTest) {
		/**
		 * PourType == PourAction tant que la gestion des actions n'est pas réellement implémentée
		 */
		return findListeComptePourType(codeTypeServiceWebExterne,false);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne) {
		return findListeComptePourType(codeTypeServiceWebExterne,false);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne, boolean compteTest) {
		try {		
			Query query = null;
			if(codeTypeServiceWebExterne!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select c from TaCompteServiceWebExterne c join c.taServiceWebExterne s join s.taTServiceWebExterne t where t.codeTServiceWebExterne = :codeTypeServiceWebExterne and s.actif=true and c.actif=true and c.compteTest=:compteTest";
				query = entityManager.createQuery(jpql);
				query.setParameter("codeTypeServiceWebExterne",codeTypeServiceWebExterne);
				query.setParameter("compteTest",compteTest);
			} 
			List<TaCompteServiceWebExterne> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne) {
		return findCompteDefautPourFournisseurService(codeServiceWebExterne,false);
	}
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne, boolean compteTest) {
		try {		
			Query query = null;
			if(codeServiceWebExterne!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select c from TaCompteServiceWebExterne c join c.taServiceWebExterne s where s.codeServiceWebExterne = :codeServiceWebExterne and s.actif=true and c.actif=true and c.compteTest=:compteTest and c.defaut=true";
				query = entityManager.createQuery(jpql);
				query.setParameter("codeServiceWebExterne",codeServiceWebExterne);
				query.setParameter("compteTest",compteTest);
			} 
			TaCompteServiceWebExterne l = (TaCompteServiceWebExterne) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne){
		return findListeComptePourFournisseurService(codeServiceWebExterne,false);
	}
	
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne, boolean compteTest) {
		try {		
			Query query = null;
			if(codeServiceWebExterne!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select c from TaCompteServiceWebExterne c join c.taServiceWebExterne s where s.codeServiceWebExterne = :codeServiceWebExterne and s.actif=true and c.actif=true and c.compteTest=:compteTest";
				query = entityManager.createQuery(jpql);
				query.setParameter("codeServiceWebExterne",codeServiceWebExterne);
				query.setParameter("compteTest",compteTest);
			} 
			List<TaCompteServiceWebExterne> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
