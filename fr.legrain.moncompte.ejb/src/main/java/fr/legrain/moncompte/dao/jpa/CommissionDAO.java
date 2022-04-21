package fr.legrain.moncompte.dao.jpa;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ICommissionDAO;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaPanier;
import fr.legrain.validator.BeanValidator;


public class CommissionDAO implements ICommissionDAO {

	private static final Log logger = LogFactory.getLog(CommissionDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaCommission p";
	
	public CommissionDAO(){
//		this(null);
	}

//	public TaTaCommissionDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaCommission.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCommission());
//	}


	public void persist(TaCommission transientInstance) {
		logger.debug("persisting TaCommission instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaCommission persistentInstance) {
		logger.debug("removing TaCommission instance");
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

	public TaCommission merge(TaCommission detachedInstance) {
		logger.debug("merging TaCommission instance");
		try {
			TaCommission result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCommission findById(int id) {
		logger.debug("getting TaCommission instance with id: " + id);
		try {
			TaCommission instance = entityManager.find(TaCommission.class, id);
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
	public List<TaCommission> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCommission> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaCommission entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaCommission> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCommission> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCommission> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCommission> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCommission value) throws Exception {
		BeanValidator<TaCommission> validator = new BeanValidator<TaCommission>(TaCommission.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCommission value, String propertyName) throws Exception {
		BeanValidator<TaCommission> validator = new BeanValidator<TaCommission>(TaCommission.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCommission transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaCommission findByCode(String code) {
		logger.debug("getting TaCommission instance with username: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaCommission f where f.username='"+code+"'");
			TaCommission instance = (TaCommission)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCommission> findPanierDossier(String codeDossier) {
		try {
			if(!codeDossier.equals("")){
				Query query = entityManager.createQuery("select f from TaCommission f where f.taDossier.code='"+codeDossier+"'");
				List<TaCommission> instance = query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCommission> findCommissionPartenaire(String codePartenaire) {
		return findCommissionPartenaire(codePartenaire, null,null);
	}
	
	public List<TaCommission> findCommissionPartenaire(String codePartenaire, Date debut, Date fin) {
		try {
			String jpql = null;
			if(!codePartenaire.equals("")){
				jpql = "select c from TaCommission c join c.taPartenaire p where p.codePartenaire='"+codePartenaire+"'";
				if(debut!=null && fin!=null) {
					jpql += " and c.quandCree between :debut and :fin";
				}
				jpql += " order by c.quandCree";
				Query query = entityManager.createQuery(jpql);
				if(debut!=null && fin!=null) {
					query.setParameter("debut", debut,TemporalType.TIMESTAMP);
					query.setParameter("fin", fin,TemporalType.TIMESTAMP);
				}
				List<TaCommission> instance = query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public BigDecimal findMontantVentePartenaire(String codePartenaire,Date debut, Date fin) {
		try {
			String jpql = null;
			if(!codePartenaire.equals("")){
				jpql = "select sum(p.totalHT) from TaPanier p where p.codeRevendeur='"+codePartenaire+"' and p.paye = true and p.datePaiement between :debut and :fin";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("debut", debut,TemporalType.TIMESTAMP);
				query.setParameter("fin", fin,TemporalType.TIMESTAMP);
				Object instance = query.getSingleResult();
				logger.debug("get successful");
				if(instance!=null) {
					return (BigDecimal) instance;
				} else {
					return new BigDecimal(0);
				}
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public Date findDerniereCreationDossierPayantPartenaire(String codePartenaire) {
		try {
			String jpql = null;
			if(!codePartenaire.equals("")){
				//and p.datePaiement between :debut and : fin          d.quandCree
				jpql = "select distinct d.code,p.datePaiement,d,p from TaPanier p join p.taDossier d where p.codeRevendeur='"+codePartenaire+"' and p.paye = 'TRUE' order by p.datePaiement desc";
				Query query = entityManager.createQuery(jpql);
				query.setMaxResults(1);
				Object instance = query.getSingleResult();
				String codeDossier = (String)((Object[])instance)[0];
				Timestamp datePaiement = (Timestamp)((Object[])instance)[1];
				TaDossier taDossier = (TaDossier)((Object[])instance)[2];
				TaPanier taPanier = (TaPanier)((Object[])instance)[3];
				System.out.println(instance);
				logger.debug("get successful");
				return new Date(datePaiement.getTime());
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public BigDecimal findTauxCommissionPartenaire(String codePartenaire) {
		// TODO Auto-generated method stub
		return null;
	}

}

