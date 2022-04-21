package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IFabricationDAO;
import fr.legrain.article.dao.ILotDAO;
import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaFabrication.
 * @see fr.legrain.TaFabrication.dao.TaFabrication
 * @author Hibernate Tools
 */
public class FabricationDAO implements IFabricationDAO {

	private static final Log logger = LogFactory.getLog(FabricationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private @Inject ILotDAO daoLot;
	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaFabrication> uniteValidator = new ClassValidator<TaFabrication>( TaFabrication.class );
	
	private String defaultJPQLQuery = "select u from TaFabrication u";
	//private String defaultJPQLQuery = "select u from TaFabrication u where u.taArticle is null";
	
	public FabricationDAO(){
//		this(null);
	}
	
//	public TaFabricationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaFabrication.class.getSimpleName());
//		initChampId(TaFabrication.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaFabrication());
//	}
	
//	public IStatus validate(TaFabrication entity){
//		ClassValidator<TaFabrication> uniteValidator = new ClassValidator<TaFabrication>( TaFabrication.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaFabrication)
	 */
	public void persist(TaFabrication transientInstance) {
		logger.debug("persisting TaFabrication instance");
		try {
			
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaFabrication)
	 */
	public void remove(TaFabrication persistentInstance) {
		logger.debug("removing TaFabrication instance");
		try {
			TaFabrication e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaFabrication)
	 */
	public TaFabrication merge(TaFabrication detachedInstance) {
		logger.debug("merging TaFabrication instance");
		try {
			TaFabrication result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaFabrication findByCode(String code) {
		logger.debug("getting TaFabrication instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaFabrication f where f.codeDocument='"+code+"'");
				TaFabrication instance = (TaFabrication)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaFabrication findById(int id) {
		logger.debug("getting TaFabrication instance with id: " + id);
		try {
			TaFabrication instance = entityManager.find(TaFabrication.class, id);
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
	public List<TaFabrication> selectAll() {
		logger.debug("selectAll TaFabrication");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaFabrication> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaFabrication refresh(TaFabrication detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaFabrication.class, detachedInstance.getIdDocument());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	public List<TaFabricationDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_ALL_LIGHT);
			List<TaFabricationDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFabricationDTO> findByCodeLight(String code) {
		logger.debug("getting TaFabricationDTO instance");
		try {		
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeDocument", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaFabrication.QN.FIND_ALL_LIGHT);
			}

			List<TaFabricationDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFabricationDTO> findByDateLight(Date debut, Date fin) {
		logger.debug("getting TaFabricationDTO instance");
		try {		
			Query query = null;
			if(debut!=null && fin!=null) {
				query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				query.setParameter("dateDebut",debut);
				query.setParameter("dateFin",fin);
			} else {
				query = entityManager.createNamedQuery(TaFabrication.QN.FIND_ALL_LIGHT);
			}

			List<TaFabricationDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public List<TaFabrication> findByArticle(String codeArticle) {
//		logger.debug("getting TaFabrication instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaFabrication> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaFabricationDAO,TaFabrication> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaFabricationDAO,TaFabrication>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaFabrication entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
//	public void ctrlSaisieSpecifique(ValeurChamps valeurChamps) throws ExceptLgr {	
//		try{
//			switch (this.getModeObjet().getMode()) {
//            case C_MO_CONSULTATION: break;
//            
//            case C_MO_IMPORTATION:            				
//            case C_MO_INSERTION:
//            	TCtrlGeneraux.ctrlSaisie(ex);
//				break;            	
//            case C_MO_EDITION:	
//            	TCtrlGeneraux.ctrlSaisie(ex);
//				break;
//            case C_MO_SUPPRESSION: 
//            	TCtrlGeneraux.ctrlSaisie(ex);
//				break;
//			default:				
//				break;
//			}
//		}catch (Exception e){
//			logger.error("Erreur : ctrlSaisieSpecifique", e);
//			throw new ExceptLgr(e.getMessage());
//		}
//	}

	@Override
	public List<TaFabrication> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFabrication> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFabrication> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFabrication> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFabrication value) throws Exception {
		BeanValidator<TaFabrication> validator = new BeanValidator<TaFabrication>(TaFabrication.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFabrication value, String propertyName) throws Exception {
		BeanValidator<TaFabrication> validator = new BeanValidator<TaFabrication>(TaFabrication.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFabrication transientInstance) {
		entityManager.detach(transientInstance);
	}
	/*
	public List<TaFabrication> findWithDate(Date date){ //Recherche par Date
		logger.debug("getting TaFabrication instance with Date: " + date);
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE);
			query.setParameter(1, date);
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	
	//-ajouté par Dima - Début
	/*
	public List<TaFabrication> findWithDateRange(Date dateDeb, Date dateFin){ //Recherche par periode etres les Dates
		logger.debug("getting TaFabrication instance with Dates: " + dateDeb + " and " + dateFin);
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE);
			query.setParameter(1, dateDeb);
			query.setParameter(2, dateFin);
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	*/
	
//	{
//		TaLFabrication f = new TaLFabrication();
//		f.getTaLot();
//	}
	
	public List<TaFabrication> findWithNumFab(String codeFabrication, Date dateDebut, Date dateFin){ //Recherche par code de la fabrication
		logger.debug("getting TaFabrication instance with Code Fabrication : " + codeFabrication );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_CODE_FABRICATION);
			query.setParameter("codeDocument", codeFabrication);
			query.setParameter("dateDebut", dateDebut);
			query.setParameter("dateFin", dateFin);
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLFabricationPF> findWithProdFinLibelle(String nomProdtuit, Date dateDebut, Date dateFin){ //Recherche par Libelle de Produit Finit
		logger.debug("getting TaFabrication instance with Libelle de Produit Finit : " + nomProdtuit );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NOM_PRODUIT_FINAL);
			query.setParameter("libellecArticle", "%"+nomProdtuit+"%");
			query.setParameter("dateDebut", dateDebut);
			query.setParameter("dateFin", dateFin);
			List<TaLFabricationPF> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLFabricationPF> findWithProdFinNumLot(String numLot, Date dateDebut, Date dateFin){ //Recherche par numLot de Produit Finit
		logger.debug("getting TaFabrication instance with numLot de Produit Finit : " + numLot );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NUM_LOT_PRODUIT_FINAL);
			query.setParameter("numLot", numLot);
			query.setParameter("dateDebut", dateDebut);
			query.setParameter("dateFin", dateFin);
			List<TaLFabricationPF> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLFabricationMP> findWithMatPremLibelle(String nomProdtuit, Date dateDebut, Date dateFin){ //Recherche par Libelle de Matière Première
		logger.debug("getting TaFabrication instance with Libelle de Matière Première : " + nomProdtuit );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NOM_MATIERE_PREMIERE);
			query.setParameter("libellecArticle", "%"+nomProdtuit+"%");
			query.setParameter("dateDebut", dateDebut);
			query.setParameter("dateFin", dateFin);
			List<TaLFabricationMP> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaLFabricationMP> findWithMatPremNumLot(String numLot, Date dateDebut, Date dateFin){ //Recherche par numLot de Matière Première
		logger.debug("getting TaFabrication instance with numLot de Matière Première : " + numLot );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NUM_LOT_MATIERE_PREMIERE);
			query.setParameter("numLot", numLot);
			query.setParameter("dateDebut", dateDebut);
			query.setParameter("dateFin", dateFin);
			List<TaLFabricationMP> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
/** // je ne sais pas si on a besoin de ce méthode!?! © Dima
	public List<TaLFabrication> findByNumLot(String numLot, Date dateDebut, Date dateFin){ //Recherche par numLot 
		logger.debug("getting TaFabrication instance with numLot : " + numLot );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NUM_LOT);
			query.setParameter(1, numLot);
			query.setParameter(2, dateDebut);
			query.setParameter(3, dateFin);
			List<TaLFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	*/
	public List<TaFabrication> findWithCodeArticle(String codeArticle, Date dateDebut, Date dateFin){ //Recherche par code Article
		logger.debug("getting TaFabrication instance with code Article : " + codeArticle );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_CODE_ARTICLE);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("dateDebut", dateDebut);
			query.setParameter("dateFin", dateFin);
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	// NoDate
	public List<TaFabrication> findWithNumFabNoDate(String codeFabrication){ //Recherche par code de la fabrication
		logger.debug("getting TaFabrication instance with Code Fabrication : " + codeFabrication );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_CODE_FABRICATION_NO_DATE);
			query.setParameter("codeFabrication", codeFabrication);
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFabrication> findWithProdFinLibelleNoDate(String nomProdtuit){ //Recherche par Libelle de Produit Finit
		logger.debug("getting TaFabrication instance with Libelle de Produit Finit : " + nomProdtuit );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NOM_PRODUIT_FINAL_NO_DATE);
			query.setParameter("libellecArticle", "%"+nomProdtuit+"%");
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFabrication> findWithProdFinNumLotNoDate(String numLot){ //Recherche par nomLot de Produit Finit
		logger.debug("getting TaFabrication instance with numLot de Produit Finit : " + numLot );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NUM_LOT_PRODUIT_FINAL_NO_DATE);
			query.setParameter("numLot", numLot);
			List<TaFabrication> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFabrication> findWithMatPremLibelleNoDate(String nomProdtuit){ //Recherche par Libelle de Matière Première
		logger.debug("getting TaFabrication instance with Libelle de Matière Première : " + nomProdtuit );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NOM_MATIERE_PREMIERE_NO_DATE);
			query.setParameter("libellecArticle", "%"+nomProdtuit+"%");
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFabrication> findWithMatPremNumLotNoDate(String numLot){ //Recherche par nomLot de Matière Première
		logger.debug("getting TaFabrication instance with numLot de Matière Première : " + numLot );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NUM_LOT_MATIERE_PREMIERE_NO_DATE);
			query.setParameter("numLot", numLot);
			List<TaFabrication> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFabrication> findWithCodeArticleNoDate(String codeArticle){ //Recherche par code Article
		logger.debug("getting TaFabrication instance with code Article : " + codeArticle );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_CODE_ARTICLE_NO_DATE);
			query.setParameter("codeArticle", codeArticle);
			List<TaFabrication> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public TaFabrication findAvecResultatConformites(int idf) {
		logger.debug("findResultatConformites");
		try {
			String query="select f from TaFabrication f"
					+ " left join f.lignesProduits lfp join lfp.taLot l1 left join l1.taResultatConformites "
					+ " left join f.lignesMatierePremieres lfmp join lfmp.taLot l2 left join l2.taResultatConformites "
					+ " left join l2.taArticle a left join a.taConformites cf "
					+ " where f.idDocument ="+idf;
			Query ejbQuery = entityManager.createQuery(query);
			TaFabrication instance = (TaFabrication) ejbQuery.getSingleResult();
			instance.findProduit();
			for (TaLFabricationMP l : instance.getLignesMatierePremieres()) {
				l.getTaLot().setRetourControlLot(daoLot.retourControl(l.getTaLot()));
			}
			for (TaLFabricationPF l : instance.getLignesProduits()) {
				l.getTaLot().setRetourControlLot(daoLot.retourControl(l.getTaLot()));
			}
			logger.debug("selectAll successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
}
