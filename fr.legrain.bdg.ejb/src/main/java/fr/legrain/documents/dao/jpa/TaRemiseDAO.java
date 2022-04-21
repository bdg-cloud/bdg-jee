package fr.legrain.documents.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.documents.dao.IRemiseDAO;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.dao.TaTLiens
 * @author Hibernate Tools
 */
public class TaRemiseDAO /*extends AbstractApplicationDAO<TaRemise>*/ implements IRemiseDAO,IDocumentDAO<TaRemise>{

	//private static final Log logger = LogFactory.getLog(TaTLiensDAO.class);
	static Logger logger = Logger.getLogger(TaRemiseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaRemise a ";
	
	public TaRemiseDAO() {
//		this(null);
	}
	
//	public TaRemiseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaRemise.class.getSimpleName());
//		initChampId(TaRemise.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRemise());
//	}
	
//	public TaRemise refresh(TaRemise detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaRemise.class, detachedInstance.getIdRemise());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaRemise transientInstance) {
		logger.debug("persisting TaRemise instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRemise persistentInstance) {
		logger.debug("removing TaRemise instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRemise merge(TaRemise detachedInstance) {
		logger.debug("merging TaRemise instance");
		try {
			TaRemise result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRemise findById(int id) {
		logger.debug("getting TaRemise instance with id: " + id);
		try {
			TaRemise instance = entityManager.find(TaRemise.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaRemise findByCode(String code) {
		logger.debug("getting TaRemise instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaRemise f where f.codeDocument='"+code+"'");
			TaRemise instance = (TaRemise)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
	
	public TaRemiseDTO findByCodeDTO(String code) {
		logger.debug("getting TaRemise instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE_DTO);
			query.setParameter("dateDebut", code);
			query.setParameter("dateFin", code);
			TaRemiseDTO instance = (TaRemiseDTO)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaRemiseDTO> RechercheDocumentDTO(String codeDeb,String codeFin) {
		try {
			List<TaRemiseDTO> result = null;
			Query query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE_DTO);
			query.setParameter("dateDebut", codeDeb);
			query.setParameter("codeFin", codeFin);
			result = query.getResultList();
			logger.debug("get successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaRemise> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaRemise");
		try {  
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRemise> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	/**
	 * Recherche les remises entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des remises entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaRemise> rechercheDocument(Date dateDeb, Date dateFin) {
		return rechercheDocument(dateDeb, dateFin, null, null);
	}
	
	/**
	 * Recherche les remises entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des remises entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaRemise> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		List<TaRemise> result = null;
		Query query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();		
		return result;		
	}
	
	/**
	 * Recherche les remises entre 2 codes remises
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des remises entre ces 2 codes (null en cas d'erreur)
	 */
	public List<TaRemise> rechercheDocument(String codeDeb, String codeFin) {
		return rechercheDocument(codeDeb, codeFin, null, null);
	}
	
	/**
	 * Recherche les remises entre 2 dates et non exportees
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des remises entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaRemise> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaRemise> result = null;
		Query query = null;
		if(parDate)query=entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_NON_EXPORT_PARDATE);
		else query=entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_NON_EXPORT);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	@Override
	public List<TaRemiseDTO> rechercheDocumentNonExporteDTO(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaRemiseDTO> result = null;
		Query query = null;
		if(parDate)query=entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_NON_EXPORT_PARDATE_LIGHT);
		else query=entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	public List<TaRemise> findSiReglementDansRemise(String code) {
		try {
			Query query = entityManager.createQuery("select a.taDocument from TaLRemise a join a.taReglement r where r.codeDocument like :codeDocument");
			query.setParameter("codeDocument", code);
			return query.getResultList();
			 
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<TaRemise> findSiAcompteDansRemise(String code) {
		try {
			Query query = entityManager.createQuery("select a.taDocument from TaLRemise a where a.taAcompte.codeDocument like :codeDocument");
			query.setParameter("codeDocument", code);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void ctrlSaisieSpecifique(TaRemise entity,String field) throws ExceptLgr {	
		
	}

	@Override
	public List<TaRemise> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb,codeFin);
	}

	@Override
	public List<TaRemise> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb,dateFin);
	}

	@Override
	public List<TaRemise> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		List<TaRemise> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_NON_EXPORT);
		else
			query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE);	
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();		
		return result;
	}

	@Override
	public List<TaRemise> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaRemise> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE_NON_EXPORT);
		else
		query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers) {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}

	@Override
	public List<TaRemise> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRemise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRemise> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRemise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRemise value) throws Exception {
		BeanValidator<TaRemise> validator = new BeanValidator<TaRemise>(TaRemise.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRemise value, String propertyName)
			throws Exception {
		BeanValidator<TaRemise> validator = new BeanValidator<TaRemise>(TaRemise.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRemise transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public List<TaRemise> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Stub de la méthode généré automatiquement
		List<TaRemiseDTO> result = null;
		Query query = null;
		if(parDate)query = entityManager.createNamedQuery(TaRemise.QN.FIND_ALL_LIGHT_PERIODE);
		else query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE_DTO);
		
		query.setParameter("dateDebut", dateDeb);
		query.setParameter("dateFin", dateFin);
		
		result = query.getResultList();
		return result;
	}
	
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin) {
		// TODO Stub de la méthode généré automatiquement
		List<TaRemiseDTO> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaRemise.QN.FIND_ALL_LIGHT_PERIODE);
		
		query.setParameter("dateDebut", dateDeb);
		query.setParameter("dateFin", dateFin);
		
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaRemise> rechercheDocumentVerrouille(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		List<TaRemise> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_VERROUILLE);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_NON_VERROUILLE);
		else
			query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE);	
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();		
		return result;
	}

	@Override
	public List<TaRemise> rechercheDocumentVerrouille(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaRemise> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE_VERROUILLE);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE_NON_VERROUILLE);
		else
		query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_CODE);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<Date> findDateExport(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select distinct(f.dateExport) from TaRemise f where f.dateExport is not null and f.dateExport between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			List<Date> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<TaRemise> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaRemise> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATEEXPORT);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}
		result = query.getResultList();
		return result;
	}

	@Override
	public IDocumentTiers findDocByLDoc(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaRemiseDTO> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATEEXPORT_LIGHT);
//			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
//		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}


		result = query.getResultList();
		return result;
	}


	@Override
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,String codeTiers, Boolean export) {
		List<TaRemiseDTO> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_EXPORT_LIGHT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		else query = entityManager.createNamedQuery(TaRemise.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
//		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}


	@Override
	public Date selectMinDateDocumentNonExporte(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select min(f.dateDocument) from TaRemise f where f.dateExport is null and f.dateDocument between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			Date l = (Date) query.getSingleResult();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IDocumentTiers findByCodeFetch(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentTiers findByIdFetch(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
