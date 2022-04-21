package fr.legrain.article.dao.jpa;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IPrixDAO;
import fr.legrain.article.model.TaPrix;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaPrix.
 * @see fr.legrain.articles.dao.TaPrix
 * @author Hibernate Tools
 */

public class PrixDAO implements IPrixDAO{

	private static final Log logger = LogFactory.getLog(PrixDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaPrix p";
	
	public PrixDAO(){
//		this(null);
	}

//	public TaPrixDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		//champIdTable=ctrlGeneraux.getID_TABLE(TaPrix.class.getSimpleName());
//		initChampId(TaPrix.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaPrix());
//	}


	public void persist(TaPrix transientInstance) {
		logger.debug("persisting TaPrix instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaPrix refresh(TaPrix detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaPrix.class, detachedInstance.getIdPrix());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaPrix persistentInstance) {
		logger.debug("removing TaPrix instance");
		boolean estRefPrix=false;
		try {
			//selection d'un prix d'un prix de reference "par defaut" (le premier de la liste)
			if(persistentInstance.getTaArticle().getTaPrix()!=null)
				estRefPrix=persistentInstance.getIdPrix()==persistentInstance.getTaArticle().getTaPrix().getIdPrix();
			
			entityManager.remove(persistentInstance);
			if(estRefPrix && !persistentInstance.getTaArticle().getTaPrixes().isEmpty())
				persistentInstance.getTaArticle().setTaPrix(persistentInstance.getTaArticle().getTaPrixes().iterator().next());

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaPrix merge(TaPrix detachedInstance) {
		logger.debug("merging TaPrix instance");
		try {
			TaPrix result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaPrix findById(int id) {
		logger.debug("getting TaPrix instance with id: " + id);
		try {
			TaPrix instance = entityManager.find(TaPrix.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les prix qui ont été importés d'un autre programme
	 * @param origineImport
	 * @param idImport - id externe (dans le programme d'origine)
	 * @return
	 */
	public List<TaPrix> rechercheParImport(String origineImport, String idImport) {
		Query query = entityManager.createNamedQuery(TaPrix.QN.FIND_BY_IMPORT);
		query.setParameter("origineImport", origineImport);
		query.setParameter("idImport", idImport);
		List<TaPrix> l = query.getResultList();

		return l;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaPrix> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPrix> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaPrix entity,String field) throws ExceptLgr {	
		try {
			if(field.equals(
					Const.C_PRIXTTC_PRIX)){
				if(!entity.getPrixttcPrix().equals("")){
					BigDecimal ht=null;
					BigDecimal n1,n2;
					if (entity.getTaArticle().getTaTva()!=null && !LibChaine.emptyNumeric(entity.getTaArticle().getTaTva().getTauxTva())){
						n1=entity.getPrixttcPrix().multiply(new BigDecimal(100));
						n2=BigDecimal.valueOf(100).add(entity.getTaArticle().getTaTva().getTauxTva());
						ht=n1.divide(n2,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
					}else {
						if(entity.getPrixttcPrix()!=null)
							ht =entity.getPrixttcPrix().setScale(2,BigDecimal.ROUND_HALF_UP);
					}
					entity.setPrixPrix(ht);
				}					
			}
			if(field.equals(
					Const.C_PRIX_PRIX)){
				if(!entity.getPrixPrix().equals("")){
					if(entity.getTaArticle().getTaTva()!=null){
						BigDecimal taux=entity.getTaArticle().getTaTva().getTauxTva().
						divide(new BigDecimal(100));
						BigDecimal	tva =entity.getPrixPrix().multiply(taux);
						entity.setPrixttcPrix(entity.getPrixPrix().add(tva).setScale(2,BigDecimal.ROUND_HALF_UP));
						//affecte(Const.C_PRIXTTC_PRIX,LibCalcul.calculStringString(valeurChamps.getValeur(),tva,LibCalcul.C_ADDITIONNER,false));
					}
					else entity.setPrixttcPrix(entity.getPrixPrix());
				}					
			};			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaPrix> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPrix> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPrix> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPrix> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPrix value) throws Exception {
		BeanValidator<TaPrix> validator = new BeanValidator<TaPrix>(TaPrix.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPrix value, String propertyName) throws Exception {
		BeanValidator<TaPrix> validator = new BeanValidator<TaPrix>(TaPrix.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPrix transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaPrix findByCode(String code) {
		return null;
	}
	
}

