package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.ejb.RemoveException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.IDossierDAO;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.validator.BeanValidator;


public class DossierDAO implements IDossierDAO {

	private static final Log logger = LogFactory.getLog(DossierDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaDossier p order by p.code";
	
	public DossierDAO(){
//		this(null);
	}

//	public TaTaDossierDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaDossier.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaDossier());
//	}
	
	public TaClient findClientDossier(String codeDossier) {
		logger.debug("getting TaClient instance with codeDossier: " + codeDossier);
		try {
			if(!codeDossier.equals("")){
			Query query = entityManager.createQuery("select f.taClient from TaDossier f where f.code='"+codeDossier+"'");
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


	public void persist(TaDossier transientInstance) {
		logger.debug("persisting TaDossier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaDossier persistentInstance) {
		logger.debug("removing TaDossier instance");
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

	
	public void removeLigneAutorisation(TaDossier persistentInstance, TaAutorisation ligne) {
		logger.debug("removeLigneAutorisation");
		try {			
			persistentInstance.getListeAutorisation().remove(ligne);
			Query query = entityManager.createQuery("delete  from TaAutorisation  where idAutorisation = "+ligne.getIdAutorisation());
			query.executeUpdate();
			logger.debug("removeLigneAutorisation successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("removeLigneAutorisation failed", re);
			throw re2;
		}
	}
	
	public TaDossier merge(TaDossier detachedInstance) {
		logger.debug("merging TaDossier instance");
		try {
			TaDossier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaDossier findById(int id) {
		logger.debug("getting TaDossier instance with id: " + id);
		try {
			TaDossier instance = entityManager.find(TaDossier.class, id);
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
	public List<TaDossier> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaDossier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaDossierDTO> selectAllLight() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createNamedQuery(TaDossier.QN.FIND_ALL_LIGHT);
			List<TaDossierDTO> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaDossier entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public List<TaDossier> findListeDossierClient(int idClient) {
		try {
			Query ejbQuery = entityManager.createQuery("select d from TaDossier d where d.taClient.id = "+idClient +" order by d.code");
			List<TaDossier> l = ejbQuery.getResultList();
			System.out.println("findListeDossierClient with idclient "+idClient);
			return l;
		} catch (RuntimeException re) {
			System.out.println("findListeDossierClient with idclient "+idClient+"failed");
			re.printStackTrace();
			throw re;
		}
	}
	
	public List<TaDossier> findListeDossierProduit(String idProduit) {
		try {
			/*
			 * ATTENTION, avec cette requete il est impossible de trouver un dossier pour lequel le produit rechercher est affecté via un produit compose
			 * Ex: 
			 * produit : noyau : id = 10
			 * produit : espace client : id =15
			 * Si espace client est dans le produit noyau et que l'on fait une recherche sur espace client le dossier concerné ne sera pas sélectionné
			 * 			Il faudrait faire une recherche sur les prduits composant un produit composé de façon récursive
			 * Si espace client est affecté directement, le dossier sera bien trouvé
			 */
			//Query ejbQuery = entityManager.createQuery("select d from TaDossier d where TaAutorisation a join a.taDossier d join a.taProduit p where p.code = '"+idProduit+"'");
			Query ejbQuery = entityManager.createQuery("select d from TaDossier d join d.listeAutorisation a join a.taProduit p where p.identifiantModule = '"+idProduit+"'");
			@SuppressWarnings("unchecked")
			List<TaDossier> l = ejbQuery.getResultList();
			System.out.println("findListeDossierProduit with idProduit "+idProduit);
			return l;
		} catch (RuntimeException re) {
			System.out.println("findListeDossierProduit with idProduit "+idProduit+"failed");
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	public List<TaDossier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaDossier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaDossier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaDossier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaDossier value) throws Exception {
		BeanValidator<TaDossier> validator = new BeanValidator<TaDossier>(TaDossier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaDossier value, String propertyName) throws Exception {
		BeanValidator<TaDossier> validator = new BeanValidator<TaDossier>(TaDossier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaDossier transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaDossier findByCode(String code) {
		logger.debug("getting TaDossier instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaDossier f where f.code='"+code+"'");
				TaDossier instance = (TaDossier)query.getSingleResult();
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
	public void removeLignePrixPerso(TaDossier persistentInstance,
			TaPrixPerso ligne) throws RemoveException {
		logger.debug("removeLignePrixPerso");
		try {			
			persistentInstance.getListeAutorisation().remove(ligne);
			Query query = entityManager.createQuery("delete  from TaPrixPerso  where id = "+ligne.getId());
			query.executeUpdate();
			logger.debug("removeLignePrixPerso successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("removeLignePrixPerso failed", re);
			throw re2;
		}
	}
	
}

