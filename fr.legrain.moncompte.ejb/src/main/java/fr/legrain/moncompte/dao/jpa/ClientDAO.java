package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.moncompte.dao.IClientDAO;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.validator.BeanValidator;


public class ClientDAO implements IClientDAO {

	private static final Log logger = LogFactory.getLog(ClientDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaClient p order by p.code";
	
	public ClientDAO(){
//		this(null);
	}

//	public TaTaClientDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaClient.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaClient());
//	}


	public void persist(TaClient transientInstance) {
		logger.debug("persisting TaClient instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaClient persistentInstance) {
		logger.debug("removing TaClient instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaClient merge(TaClient detachedInstance) {
		logger.debug("merging TaClient instance");
		try {
			TaClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaClient findById(int id) {
		logger.debug("getting TaClient instance with id: " + id);
		try {
			TaClient instance = entityManager.find(TaClient.class, id);
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
	public List<TaClient> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaClient entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaClient value) throws Exception {
		BeanValidator<TaClient> validator = new BeanValidator<TaClient>(TaClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaClient value, String propertyName) throws Exception {
		BeanValidator<TaClient> validator = new BeanValidator<TaClient>(TaClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaClient transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaClient findByCode(String code) {
		logger.debug("getting TaClient instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaClient f where f.code='"+code+"'");
			TaClient instance = (TaClient)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	@Override
	public TaClient findByCodePartenaire(String codePartenaire) {
		logger.debug("getting TaClient instance with codePartenaire: " + codePartenaire);
		try {
			if(!codePartenaire.equals("")){
				Query query = entityManager.createQuery("select c from TaClient c join c.taPartenaire p where p.codePartenaire='"+codePartenaire+"'");
				TaClient instance = (TaClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	public List<TaClient> listeDemandePartenariat(){
		try {
			Query ejbQuery = entityManager.createNamedQuery(TaClient.QN.FIND_ALL_DEMANDE_PARTENARIAT);
			List<TaClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}
	
	public List<TaClient> listePartenaire(){
		try {
			Query ejbQuery = entityManager.createNamedQuery(TaClient.QN.FIND_ALL_PARTENAIRE);
			List<TaClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}
	
	public List<TaClient> listePartenaireType(int idTypePartenaire){
		try {
			Query ejbQuery = entityManager.createNamedQuery(TaClient.QN.FIND_ALL_PARTENAIRE);
			ejbQuery.setParameter("idTypePartenaire", idTypePartenaire);
			List<TaClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}
	
	public String maxCodePartenaire(String debutCode) {
		
		logger.debug("getting maxCodePartenaire instance with debutCode: " + debutCode);
		try {
			if(!debutCode.equals("")){
				Query query = entityManager.createQuery("select max(substring (p.codePartenaire,5,8)) from TaPartenaire  p where p.codePartenaire like :debutCode");
				query.setParameter("debutCode", debutCode+"%");
				String instance = (String)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
		
	}
	
	public String maxCodeClient(String debutCode) {
		
		logger.debug("getting maxCodeClient instance with debutCode: " + debutCode);
		try {
			if(!debutCode.equals("")){
				Query query = entityManager.createQuery("select max(substring (c.code,5,7)) from TaClient c where c.code like :debutCode");
				query.setParameter("debutCode", debutCode+"%");
				String instance = (String)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
		
	}
}

