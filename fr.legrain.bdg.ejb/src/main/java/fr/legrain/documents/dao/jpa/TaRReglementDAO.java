package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.IDocumentTiersComplet;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.documents.dao.IRReglementDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaRAcompte.
 * @see fr.legrain.documents.dao.TaRAcompte
 * @author Hibernate Tools
 */
public class TaRReglementDAO /*extends AbstractApplicationDAO<TaRReglement>*/implements IRReglementDAO {

//	private static final Log log = LogFactory.getLog(TaRAcompteDAO.class);
	static Logger logger = Logger.getLogger(TaRReglementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaRReglement a";
	
	public TaRReglementDAO(){
//		this(null);
	}
	

	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void persist(TaRReglement transientInstance) {
		logger.debug("persisting TaRReglement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRReglement persistentInstance) {
		logger.debug("removing TaRReglement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRReglement merge(TaRReglement detachedInstance) {
		logger.debug("merging TaRReglement instance");
		try {
			TaRReglement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRReglement findById(int id) {
		logger.debug("getting TaRReglement instance with id: " + id);
		try {
			TaRReglement instance = entityManager.find(TaRReglement.class, id);
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
	public List<TaRReglement> selectAll(IDocumentTiersComplet taFacture) {
		logger.debug("selectAll TaRReglement");
		try {
			if(taFacture!=null){
			Query ejbQuery = entityManager.createQuery("select a from TaRReglement a " +
					"where a.taFacture.codeDocument=:codeDocument1" +
					" or a.taAvoir.codeDocument=:codeDocument2");
			ejbQuery.setParameter("codeDocument1", taFacture.getCodeDocument());
			ejbQuery.setParameter("codeDocument2", taFacture.getCodeDocument());
			List<TaRReglement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaRReglement> selectAll(TaTiers taTiers,Date dateDeb,Date dateFin) {
		logger.debug("selectAll TaRReglement");
		try {
			if(taTiers!=null){				
				if(dateDeb==null)dateDeb=new Date(0);
				if(dateFin==null)dateFin=new Date("01/01/3000");
				String requete="select a from TaRReglement a where a.taFacture.taTiers.codeTiers like :codeTiers " +
						"and a.taReglement.dateDocument between :dateDeb and :dateFin";
				Query ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("codeTiers", taTiers.getCodeTiers());
				ejbQuery.setParameter("dateDeb", dateDeb,TemporalType.DATE);
				ejbQuery.setParameter("dateFin", dateFin,TemporalType.DATE);
			
			List<TaRReglement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaRReglement> selectSumReglementDocument(TaFacture taFacture) {
		logger.debug("selectAll TaRAcompte");
		try {
			if(taFacture!=null){
			Query ejbQuery = entityManager.createQuery("select a from TaRReglement a " +
					"where a.taFacture=:taFacture");
			ejbQuery.setParameter("taFacture", taFacture);
			List<TaRReglement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public List<TaRReglement> findByCodeList(String code) {
		logger.debug("getting TaRReglement instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaRReglement a " +
					"where a.taReglement.codeDocument like :codeDocument");
			query.setParameter("codeDocument", code);
			List<TaRReglement> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
//	@Override
	public List<TaRReglement> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRReglement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}
	@Override
	public List<TaRReglement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRReglement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRReglement value) throws Exception {
		BeanValidator<TaRReglement> validator = new BeanValidator<TaRReglement>(TaRReglement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRReglement value, String propertyName)
			throws Exception {
		BeanValidator<TaRReglement> validator = new BeanValidator<TaRReglement>(TaRReglement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRReglement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaRReglement findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglementDTO> rechercheDocumentDTO(String codeDoc, String codeTiers) {
		List<TaRReglementDTO> result = null;
		try {
		Query query = null;
		query = entityManager.createNamedQuery(TaRReglement.QN.FIND_BY_REGLEMENT_DTO);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeDoc", codeDoc);
		query.setParameter("codeTiers", codeTiers);
		result = query.getResultList();
		return result;
	} catch (RuntimeException re) {
		System.out.println("selectAll failed");
		re.printStackTrace();
		throw re;
	}
	
	}

	@Override
	public List<Date> findDateExport(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select distinct(f.dateExport) from TaRReglement f where f.dateExport is not null and f.dateExport between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			List<Date> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
