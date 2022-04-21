package fr.legrain.pointLgr.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.pointLgr.model.TaComptePoint;
import fr.legrain.point_Lgr.dao.IComptePointDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaComptePointDAO implements IComptePointDAO {

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaComptePointDAO.class);

	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaComptePoint a";
	
	public TaComptePointDAO(){
	}
	

	
	public void persist(TaComptePoint transientInstance) {
		logger.debug("persisting TaComptePoint instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaComptePoint persistentInstance) {
		logger.debug("removing TaComptePoint instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaComptePoint merge(TaComptePoint detachedInstance) {
		logger.debug("merging TaComptePoint instance");
		try {
			TaComptePoint result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaComptePoint findById(int id) {
		logger.debug("getting TaComptePoint instance with id: " + id);
		try {
			TaComptePoint instance = entityManager.find(TaComptePoint.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public List<TaComptePoint> selectAllByTiersCompte(Integer idTiers) {
		logger.debug("getting selectAllByTiersCompte  with id: " + idTiers);
		try {
			Query query = entityManager.createQuery("select a from TaComptePoint a join a.taTiers t where t.idTiers = :idTiers");
			query.setParameter("idTiers", idTiers);
			List<TaComptePoint> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}


	
	
//	@Override
	public List<TaComptePoint> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaComptePoint");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaComptePoint> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	
	

	
	
	
	public void ctrlSaisieSpecifique(TaComptePoint entity,String field) throws ExceptLgr {	
		
	}




	public void enregistrePointBonusSurDocument(TaComptePoint comptePointTemp,IDocumentTiers doc) throws Exception{
		try {
			Date datePeremption=null;
			TaComptePoint comptePoint=null;
			
				datePeremption=doc.getDateDocument();
				datePeremption=LibDate.incrementDate(datePeremption, 0, 18, 0);
				
				
				if(comptePointTemp.getPoint()!=null && comptePointTemp.getPoint().signum()!=0){
					comptePoint=new TaComptePoint();
				comptePoint.setCodeDocument(doc.getCodeDocument());
				comptePoint.setDateAcquisition(doc.getDateDocument());
				comptePoint.setDatePeremption(datePeremption);
				comptePoint.setPoint(comptePointTemp.getPoint());
				comptePoint.setTaTiers(doc.getTaTiers());
				comptePoint.setTaTypeMouvPoint(comptePointTemp.getTaTypeMouvPoint());
				merge(comptePoint);
				}			
				
			
			logger.debug("enregistrePointBonusSurDocument successful");
		} catch (Exception re) {
			logger.error("enregistrePointBonusSurDocument failed", re);
			throw re;
		}
	}

	@Override
	public TaComptePoint findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<TaComptePoint> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaComptePoint> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public List<TaComptePoint> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaComptePoint> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}



	@Override
	public boolean validate(TaComptePoint value) throws Exception {
		BeanValidator<TaComptePoint> validator = new BeanValidator<TaComptePoint>(TaComptePoint.class);
		return validator.validate(value);
	}



	@Override
	public boolean validateField(TaComptePoint value, String propertyName)throws Exception {
		BeanValidator<TaComptePoint> validator = new BeanValidator<TaComptePoint>(TaComptePoint.class);
		return validator.validateField(value,propertyName);
	}



	@Override
	public void detach(TaComptePoint transientInstance) {
		// TODO Auto-generated method stub
		entityManager.detach(transientInstance);
	}
	
	
}
