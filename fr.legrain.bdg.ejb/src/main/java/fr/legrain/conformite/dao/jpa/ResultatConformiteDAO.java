package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IResultatConformiteDAO;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaResultatConformite.
 * @see fr.legrain.articles.dao.TaResultatConformite
 * @author Hibernate Tools
 */
public class ResultatConformiteDAO implements IResultatConformiteDAO {

	private static final Log logger = LogFactory.getLog(ResultatConformiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaResultatConformiteDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaResultatConformite f";
	
	public ResultatConformiteDAO(){
//		this(null);
	}
	
//	public TaResultatConformiteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaResultatConformite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaResultatConformite());
//	}
	
//	public TaResultatConformite refresh(TaResultatConformite detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaResultatConformite.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaResultatConformite transientInstance) {
		logger.debug("persisting TaResultatConformite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaResultatConformite persistentInstance) {
		logger.debug("removing TaResultatConformite instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdResultatConformite()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaResultatConformite merge(TaResultatConformite detachedInstance) {
		logger.debug("merging TaResultatConformite instance");
		try {
			TaResultatConformite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaResultatConformite findByCode(String code) {
		logger.debug("getting TaResultatConformite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaResultatConformite f where f.codeFamille='"+code+"'");
			TaResultatConformite instance = (TaResultatConformite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaResultatConformite findByLotAndControleConformite(int idLot, int idControleConformite) {
		logger.debug("getting TaResultatConformite instance with idLot: " + idLot+" et idControleConformite: "+idControleConformite);
		try {
//			if(!code.equals("")){
				Query query = entityManager.createQuery(""
						+ "select f from TaResultatConformite f where f.taConformite.idConformite="+idControleConformite+" and f.taLot.idLot="+idLot+""
								+ " order by f.quandCree desc").setMaxResults(1);
				TaResultatConformite instance = (TaResultatConformite)query.getSingleResult();
				logger.debug("get successful");
				return instance;
//			}
//			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStatusConformite etatLot(int idLot) {
		return etatLot(idLot,false);
	}
	
	public TaStatusConformite etatLot(int idLot, boolean priseEnCompteDesControleFactultatif) {
		try {

//			Query query3 = entityManager.createNativeQuery(
//					"select s.* "
//					+ "from ta_status_conformite s "
//					+ "join ( "
//						+ "select f.id_resultat_conformite,f.id_status_conformite "
//						+ "from ta_resultat_conformite f "
//						+ "join ( select max(quand_cree) as quand, id_conformite"
//								+" from ta_resultat_conformite where id_lot= "+idLot+" "
//								+" group by id_conformite) as f3 "
//								+" on (f.quand_cree = f3.quand)"
//								+" where f.id_lot= "+idLot+" "
//						+ ") as s1 "
//						+ "on (s.id_status_conformite=s1.id_status_conformite or s.code_status_conformite='vide') "
//						+ "group by s.importance,s.id_status_conformite "
//						+ "order by s.importance desc "
//						+ "limit 1");
						
			//https://docs.postgresql.fr/9.3/tutorial-window.html
			//http://sqlfiddle.com/#!15/f82a8/169
			Query query3 = null;
			
			if(priseEnCompteDesControleFactultatif) {
				query3 = entityManager.createNativeQuery(
						"select s.* "
							+" from ta_status_conformite s "
							+" join ( "
								+" SELECT id_resultat_conformite,id_status_conformite,id_conformite,id_lot FROM ( "
									+" SELECT id_resultat_conformite,id_status_conformite,quand_cree,id_conformite,id_lot, "
									+"  ROW_NUMBER() OVER (PARTITION BY id_conformite ORDER BY coalesce(date_controle,'1970-1-1') DESC) rn "
								+" FROM ta_resultat_conformite "
								+" ) tmp WHERE id_lot= :idLot and rn = 1) "
							+"  as s1 "
							//+" on (s.id_status_conformite=s1.id_status_conformite or s.code_status_conformite='vide' or s.code_status_conformite='vide_facultatif') "
							+" on (s.id_status_conformite=s1.id_status_conformite) "
							+" group by s.importance,s.id_status_conformite "
							+" order by s.importance desc "
							+" limit 1;");
			
				query3.setParameter("idLot", idLot);
				
				Object[] zzzz = (Object[]) query3.getSingleResult();
	
				TaStatusConformite instance = entityManager.find(TaStatusConformite.class, zzzz[0]);
				logger.debug("get successful");
				
				return instance;
				
			} else {
				
				return etatLotPourLesControlesObligatoires(idLot);
			}
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	
	public TaStatusConformite etatLotPourLesControlesObligatoires(int idLot) {
		return etatCtrlLot(idLot,false);
	}
	public TaStatusConformite etatLotPourLesControlesFacultatif(int idLot) {
		return etatCtrlLot(idLot,true);
	}
	
	private TaStatusConformite etatCtrlLot(int idLot, boolean facultatif) {
		try {

			Query query3 = null;
			
				query3 = entityManager.createNativeQuery(
					//	rc.quand_cree => rc.date_controle en cas de restauration d'un dump de la base quand_cree est faux car le trigger le re génère
					"select s.* "
						+" from ta_status_conformite s "
						+" join ( "
							+" SELECT id_resultat_conformite,id_status_conformite,id_conformite,id_lot,ctrl_facultatif FROM ( "
								+" SELECT id_resultat_conformite,id_status_conformite,rc.date_controle,rc.id_conformite,rc.id_lot , c.ctrl_facultatif, "
								+"  ROW_NUMBER() OVER (PARTITION BY rc.id_conformite ORDER BY coalesce(rc.date_controle,'1970-1-1') DESC) rn "
								// ?? supprime=false and order by version_ctrl ??
							+" FROM ta_resultat_conformite rc , ta_conformite c where rc.id_conformite = c.id_conformite "
							+" ) tmp WHERE id_lot= :idLot and rn = 1 and ctrl_facultatif = :facultatif) "
						+"  as s1 "
						//+" on (s.id_status_conformite=s1.id_status_conformite or s.code_status_conformite='vide' or s.code_status_conformite='vide_facultatif') "
						+" on (s.id_status_conformite=s1.id_status_conformite) "
						+" group by s.importance,s.id_status_conformite "
						+" order by s.importance desc "
						+" limit 1;");
			
				query3.setParameter("idLot", idLot);
				query3.setParameter("facultatif", facultatif);
				
			Object[] zzzz = (Object[]) query3.getSingleResult();

			TaStatusConformite instance = entityManager.find(TaStatusConformite.class, zzzz[0]);
				logger.debug("get successful");
				return instance;
//			}
//			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	
	
	public List<TaResultatConformite> findByLotAndControleConformiteHistorique(int idLot, int idControleConformite) {
		logger.debug("getting TaResultatConformite instance with idLot: " + idLot+" et idControleConformite: "+idControleConformite);
		try {
//			if(!code.equals("")){
				Query query = entityManager.createQuery(""
						+ "select f from TaResultatConformite f where f.taConformite.idConformite="+idControleConformite+" and f.taLot.idLot="+idLot+""
								+ " order by f.quandCree desc");
				List<TaResultatConformite> instance = (List<TaResultatConformite>)query.getResultList();
				logger.debug("get successful");
				return instance;
//			}
//			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaResultatConformite findById(int id) {
		logger.debug("getting TaResultatConformite instance with id: " + id);
		try {
			TaResultatConformite instance = entityManager.find(TaResultatConformite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaResultatConformite> selectAll() {
		logger.debug("selectAll TaResultatConformite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaResultatConformite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaResultatConformite entity,String field) throws ExceptLgr {	
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
	public List<TaResultatConformite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaResultatConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaResultatConformite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaResultatConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaResultatConformite value) throws Exception {
		BeanValidator<TaResultatConformite> validator = new BeanValidator<TaResultatConformite>(TaResultatConformite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaResultatConformite value, String propertyName) throws Exception {
		BeanValidator<TaResultatConformite> validator = new BeanValidator<TaResultatConformite>(TaResultatConformite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaResultatConformite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
