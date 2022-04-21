package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBoncdeAchatDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosBoncdeAchat;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.documents.dao.IBoncdeAchatDAO;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaDevis.
 * @see fr.legrain.documents.dao.TaDevis
 * @author Hibernate Tools
 */
public class TaBoncdeAchatDAO extends AbstractDocumentDAO<TaBoncdeAchat,TaInfosBoncdeAchat,TaLBoncdeAchat> /*extends AbstractApplicationDAO<TaBoncdeAchat> */
implements IBoncdeAchatDAO, IDocumentDAO<TaBoncdeAchat>,IDocumentTiersStatistiquesDAO<TaBoncdeAchat>,IDocumentTiersEtatDAO<TaBoncdeAchat> {

//	private static final Log log = LogFactory.getLog(TaDevisDAO.class);
	static Logger logger = Logger.getLogger(TaBoncdeAchatDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	@Inject ITiersDAO daoTiersd;
	private String defaultJPQLQuery = "select a from TaBoncdeAchat a";
	
	public TaBoncdeAchatDAO(){
//		this(null);
	}
	
//	public TaBoncdeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaBoncdeAchat.class.getSimpleName());
//		initChampId(TaBoncdeAchat.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaBoncdeAchat());
//	}
	
//	public TaBoncdeAchat refresh(TaBoncdeAchat detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			detachedInstance.setLegrain(false);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////
////			for (TaRAcompte a : detachedInstance.getTaRAcomptes()) {
////				session.evict(a.getTaAcompte().getTaRAcomptes());
////				session.evict(a.getTaAcompte());
////			}
////			session.evict(detachedInstance.getTaRAcomptes());
////			session.evict(detachedInstance);
////
////			detachedInstance = entityManager.find(TaBoncdeAchat.class, detachedInstance.getIdDocument());
//			detachedInstance.setLegrain(true);
//			detachedInstance.calculLignesTva();
//			return detachedInstance;
//
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaBoncdeAchat transientInstance) {
		logger.debug("persisting TaBoncdeAchat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaBoncdeAchat persistentInstance) {
		logger.debug("removing TaBoncdeAchat instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaBoncdeAchat merge(TaBoncdeAchat detachedInstance) {
		logger.debug("merging TaBoncdeAchat instance");
		try {
			
			TaBoncdeAchat result = entityManager.merge(detachedInstance);

			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaBoncdeAchat findById(int id) {
		logger.debug("getting TaBoncdeAchat instance with id: " + id);
		try {
			TaBoncdeAchat instance = entityManager.find(TaBoncdeAchat.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaBoncdeAchat f where f.codeDocument='"+code+"'");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaBoncdeAchat findByCode(String code) {
		logger.debug("getting TaBoncdeAchat instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaBoncdeAchat a " +
					"  left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");
			TaBoncdeAchat instance = (TaBoncdeAchat)query.getSingleResult();
			instance.setLegrain(true);
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
	public List<TaBoncdeAchat> selectAll() {
		logger.debug("TaBoncdeAchat selectAll");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaBoncdeAchat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaBoncdeAchat> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaBoncdeAchat instance with codeTiers: " + codeTiers);
		List<TaBoncdeAchat> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_TIERS_AND_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * |année|codeTiers|HT|TVA|TTC
	 * @param codeTiers
	 * @param debut
	 * @param fin
	 * @return
	 */
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers,Date debut, Date fin, int precision) {
		logger.debug("getting TaBoncdeAchat instance with codeTiers: " + codeTiers);
		List<Object> result = null;
		try {
			String jpql = null;
//			precision = 0;
			String groupBy = null;
			String select = null;
			
			if(precision==0) {
				select = "'','',cast(extract(year from f.dateDocument)as string)";
				groupBy = "'','',extract(year from f.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "'',extract(month from f.dateDocument),extract(year from f.dateDocument)";
			} else {
				select = "cast(extract(day from f.dateDocument)as string),cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			}
			
			jpql = "select  "
				+select+", t.codeTiers,"
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
				+" from TaBoncdeAchat f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
				+"group by "+groupBy+",t.codeTiers"
				;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaBoncdeAchat f "
//			+"left join f.taTiers t "
//			+"where  t.codeTiers = ? and f.dateDocument between ? and ? "
//			+"group by extract(year from f.dateDocument),t.codeTiers"
//			;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les bon de commande entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des bon de commande entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaBoncdeAchat> rechercheDocument(Date dateDeb, Date dateFin) {
		List<TaBoncdeAchat> result = null;
		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	
	
	/**
	 * Recherche les bon de commande entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des bon de commande entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaBoncdeAchat> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		List<TaBoncdeAchat> result = null;
		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	/**
	 * Recherche les bon de commande entre 2 codes bon de commande
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des bon de commande entre ces 2 codes (null en cas d'erreur)
	 */
	public List<TaBoncdeAchat> rechercheDocument(String codeDeb, String codeFin) {		
		List<TaBoncdeAchat> result = null;
		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
		result = query.getResultList();
		return result;

	}
	
	/**
	 * Recherche les commandes qui ont été importés d'un autre programme
	 * @param origineImport
	 * @param idImport - id externe (dans le programme d'origine)
	 * @return
	 */
	public List<TaBoncdeAchat> rechercheParImport(String origineImport, String idImport) {
		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_IMPORT);
		query.setParameter("origineImport", origineImport);
		query.setParameter("idImport", idImport);
		List<TaBoncdeAchat> l = query.getResultList();

		return l;
	}

//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeTiers,
			Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les bons de commande sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoi le montant des par les BC sur la période
	 */
	public List<Object> findCABCSurPeriode(Date debut, Date fin) {
		logger.debug("getting nombre ca des bc");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" sum(d.netHtCalc)"
				+" FROM TaBoncdeAchat d " 
				+" where d.dateDocument between :dateDeb and :dateFin";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<Object> findChiffreAffaireTotalTransfo(Date debut, Date fin, int precision) {
		logger.debug("getting ChiffreAffaire total transfo");
		List<Object> result = null;
		try {
			String requete = "";
			String groupBy = "";
			String select = "";
			
			if(precision==0) {
				select = "'','',cast(extract(year from d.dateDocument)as string)";
				groupBy = "'','',extract(year from d.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "'',extract(month from d.dateDocument),extract(year from d.dateDocument)";
			} else {
				select = "cast(extract(day from d.dateDocument)as string),cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "extract(day from d.dateDocument),extract(month from d.dateDocument),extract(year from d.dateDocument)";
			}
			
			requete = "SELECT "+select+ ", "
			+" sum(d.netHtCalc), "
			+" sum(d.netTvaCalc), "
			+" sum(d.netTtcCalc) "
			+" FROM TaBoncdeAchat d " 
			+" where d.dateDocument between :debut and :fin "
			+" and exists (select r " 
			+" from TaRDocument r " 
			+" where r.taBoncde = d " 
			+" and ( r.taFacture IS NOT NULL " 
			+" OR r.taBonliv IS NOT NULL )) "
			+" group by "+groupBy; 
			Query query = entityManager.createQuery(requete);
			query.setParameter("debut", debut);
			query.setParameter("fin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir les commandes  transformées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des commandes transformées
	 */
	public List<TaBoncdeAchat> findCommandesTransfos(Date debut, Date fin) {
		logger.debug("getting nombre commandes transfos");
		List<TaBoncdeAchat> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
			+" d"
			+" FROM TaBoncdeAchat d " 
			+" where d.dateDocument between :debut and :fin"
			+" and exists (select r " +
					"from TaRDocument r " +
					" where r.taBoncde = d" +
					" and ( taFacture IS NOT NULL" +
					" OR taBonliv IS NOT NULL ))"
			+" order by d.mtHtCalc DESC";
			Query query = entityManager.createQuery(requete);
			query.setParameter("debut", debut);
			query.setParameter("fin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir les commandes non transformées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des commandes non transformées
	 */
	public List<TaBoncdeAchat> findCommandesNonTransfos(Date debut, Date fin) {
		logger.debug("getting nombre commandes non transfos");
		List<TaBoncdeAchat> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
			+" d"
			+" FROM TaBoncdeAchat d " 
			+" where d.dateDocument between :debut and :fin"
			+" and not exists (select r " +
					"from TaRDocument r " +
					" where r.taBoncde = d" +
					" and ( taFacture IS NOT NULL" +
					" OR taBonliv IS NOT NULL ))"
					+" order by d.mtHtCalc DESC";
			Query query = entityManager.createQuery(requete);
			query.setParameter("debut", debut);
			query.setParameter("fin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaBoncdeAchat> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers,null);
	}

	@Override
	public List<TaBoncdeAchat> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers,null);
	}

	@Override
	public List<TaBoncdeAchat> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaBoncdeAchat> result = null;
		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaBoncdeAchat> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaBoncdeAchat> result = null;
		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
		result = query.getResultList();
		return result;
	}
	
	@Override
	public List<TaBoncdeAchat> rechercheDocumentEtat(Date dateDeb, Date datefin, String codeEtat) {
		List<TaBoncdeAchat> result = null;
		Query query = null;
		if(codeEtat!=null) {
			query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_ETAT_DATE);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("codeEtat", codeEtat);
		} else {
			query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_ETAT_ENCOURS_DATE);
			query.setParameter("date", datefin);
		}
		
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaBoncdeAchat> rechercheDocumentEtat(Date dateDeb, Date datefin,
			String codeEtat, String codeTiers) {
		List<TaBoncdeAchat> result = null;
		Query query = null;
		if(codeEtat!=null) {
			query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_ETAT_TIERS_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("codeEtat", codeEtat);		
		} else {
			query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_ETAT_TIERS_ENCOURS_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", datefin);
		}
		
		result = query.getResultList();
		return result;
	}
	
	/**
	 * Recherche les factures entre 2 dates light
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light) {
		List<Object[]> result = null;
		Query query = null;
		if(light)
			query =entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,String codeTiers) {
		List<Object[]> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers) {
			List<Object[]> result = null;
			Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}

	@Override
	public List<TaBoncdeAchat> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaBoncdeAchat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaBoncdeAchat> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaBoncdeAchat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaBoncdeAchat value) throws Exception {
		BeanValidator<TaBoncdeAchat> validator = new BeanValidator<TaBoncdeAchat>(TaBoncdeAchat.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaBoncdeAchat value, String propertyName)
			throws Exception {
		BeanValidator<TaBoncdeAchat> validator = new BeanValidator<TaBoncdeAchat>(TaBoncdeAchat.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaBoncdeAchat transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public List<TaBoncdeAchat> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genereCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBoncdeAchatDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_ALL_LIGHT2);
			List<TaBoncdeAchatDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
// ----------------------------------------
	
//	/**-------------------------------------------------------------------------------------
//	 * Classe permettant d'obtenir le ca généré par les Prelevement sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */                                   
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_ANNEE_LIGTH_PERIODE);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_MOIS_LIGTH_PERIODE);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_JOUR_LIGTH_PERIODE);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}

//	/**
//	 * Classe permettant d'obtenir le ca généré par les Prelevement sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_TOTAL_LIGTH_PERIODE);
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
//
//
//	/**
//	 * Classe permettant d'obtenir le ca généré par les Prelevement transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement transformés sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_ANNEE_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_MOIS_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_JOUR_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
//	
//	/**
//	 * Classe permettant d'obtenir le ca généré par les Prelevement transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement transformés sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_TOTAL_LIGTH_PERIODE_TRANSFORME);
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//
//
//
////			if (query.getFirstResult() == 0){
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			// Erreur silencieuse car on initialise aussi la liste en cas d'erreur
//			logger.error("get failed", re);
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}	
//
//			
//	}
//
//	

//	/**
//	 * Classe permettant d'obtenir le ca généré par les Prelevement Non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement Non transformés sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
	
//	/**
//	 * Classe permettant d'obtenir le ca généré par les Prelevement Non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement Non transformés sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin, int precision, int deltaNbJours,String codeTiers) {
//		Query query = null;
//		try {
//			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//			Date datejour = LibDate.dateDuJour();
//			if(codeTiers==null)codeTiers="%";
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("dateref", dateref, TemporalType.DATE);
//			query.setParameter("datejour", datejour, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
//	
//	/**
//	 * Classe permettant d'obtenir le ca généré par les Prelevement non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement non transformés sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_TOTAL_LIGTH_PERIODE_NON_TRANSFORME);
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
////			if (query.getFirstResult() == 0){
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//				
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			// Erreur silencieuse car on initialise aussi la liste en cas d'erreur
//			logger.error("get failed", re);
//			
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}	
//	}
//	
//	 /**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
//	 */
//	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		logger.debug("getting nombre Boncde non transfos");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_NON_TRANSFORME_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentDTO> l = query.getResultList();;
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//	}
//
//	 /**
//	 * Classe permettant d'obtenir la liste des Prelevement transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
//	 */
//	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		logger.debug("getting nombre Boncde transforme");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_TRANSFORME_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentDTO> l = query.getResultList();;
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//	}
//	
//	 /**
//	 * Classe permettant d'obtenir la listes des Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
//	 * ne doivent pas dépasser par rapport à la date du jour 
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés à relancer
//	 */
//	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting liste Boncde non transfos à relancer");
//		List<DocumentDTO> result = null;
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateref", dateref, TemporalType.DATE);
//		query.setParameter("datejour", datejour, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentDTO> l = query.getResultList();;
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//	}
//	
//	 /**
//	 * Classe permettant d'obtenir la listes des Bons de cde non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
//	 * ne doivent pas dépasser par rapport à la date du jour 
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés à relancer
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting ca des Boncde non transfos à relancer");
//		List<TaBoncdeDTO> result = null;
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.SUM_CA_TOTAL_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateref", dateref, TemporalType.DATE);
//		query.setParameter("datejour", datejour, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//	}
//
//	
//	 /**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre total de Prelevement dans la période
//	 */
//	public long countDocument(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre Boncde dans periode");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaBoncdeAchat d " 
//				+" where d.dateDocument between :datedeb and :datefin  and d.taTiers.codeTiers like :codeTiers";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBoncde = (Long)query.getSingleResult();
//			result = nbBoncde;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	/**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de Prelevement non transformés
//	 */
//	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre Boncde non transfos");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaBoncdeAchat d " 
//				+" where d.dateDocument between :datedeb and :datefin  and d.taTiers.codeTiers like :codeTiers"
//				+" and not exists (select r " +
//						"from TaRDocument r " +
//						" where r.taBoncde = d" +
//						" and ( taFacture IS NOT NULL" +
//						" OR taBonliv IS NOT NULL))";;
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBoncdeNonTranforme = (Long)query.getSingleResult();
//			result = nbBoncdeNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	/**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de Prelevement non transformés à relancer
//	 */
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting nombre Boncde non transfos à relancer");
//		Long result = (long) 0;
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date dateJour = LibDate.dateDuJour();
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaBoncdeAchat d " 
//				+" where d.dateDocument between :datedeb and :datefin "
//				+" and d.dateEchDocument <= :dateref"
//				+" and d.dateEchDocument >= :datejour"
//				+" and not exists (select r " +
//						"from TaRDocument r " +
//						" where r.taBoncde = d" +
//						" and ( taFacture IS NOT NULL" +
//						" OR taBonliv IS NOT NULL)) and d.taTiers.codeTiers like :codeTiers";;
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("dateref", dateref);
//			query.setParameter("datejour", dateJour);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBoncdeNonTranformeARelancer = (Long)query.getSingleResult();
//			result = nbBoncdeNonTranformeARelancer;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	/**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de Prelevement transformés
//	 */
//	public long countDocumentTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre Boncde transfos");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaBoncdeAchat d " 
//				+" where d.dateDocument between :datedeb and :datefin"
//				+" and exists (select r " +
//						"from TaRDocument r " +
//						" where r.taBoncde = d" +
//						" and ( taFacture IS NOT NULL" +
//						" OR taBonliv IS NOT NULL))  and d.taTiers.codeTiers like :codeTiers";
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBoncdeNonTranforme = (Long)query.getSingleResult();
//			result = nbBoncdeNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			if(codeTiers==null)codeTiers="%";
//			Query query = entityManager.createNamedQuery(TaBoncdeAchat.QN.FIND_ALL_LIGHT_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}	

//	/**
//	 * Permet d'obtenir le ca généré par les Bons de Cde transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return Retourne les informations de CA Total Transformé directement dans un objet DocumentChiffreAffaireDTO 
//	 */
//	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}
//	
//	/**
//	 * Permet d'obtenir le ca généré par les Bons de Cde non transformés à relancer sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return Retourne les informations de CA Total non transformés à relancer directement dans un objet DocumentChiffreAffaireDTO 
//	 */
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}	
//
//	
//	/**
//	 * Permet d'obtenir le ca généré par les Bons de Cde non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return Retourne les informations de CA Total NON Transformé directement dans un objet DocumentChiffreAffaireDTO 
//	 */
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}
//	
//	
//	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}

	@Override
	public List<TaBoncdeAchat> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBoncdeAchat> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Date> findDateExport(Date DateDeb, Date DateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBoncdeAchat> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return "select r from TaRDocument r where r.taBoncde = "+prefixe+" and (taFacture IS NOT NULL OR taBonliv IS NOT NULL)";
	}

	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return "doc.dateEchDocument";
	}

	@Override
	public String getRequeteARelancer() {
		// TODO Auto-generated method stub
		return " and doc.date_Ech_Document <= :dateRef and doc.date_Ech_Document >= :dateJour ";
	}
	
	@Override
	public String getRequeteARelancerJPQL() {
		// TODO Auto-generated method stub
		return " and doc.dateEchDocument <= :dateRef and doc.dateEchDocument >= :dateJour ";
	}
	
	public TaBoncdeAchat findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaBoncdeAchat a " +
					"  join Fetch a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaBoncdeAchat instance = (TaBoncdeAchat)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}	




	@Override
	public TaBoncdeAchat findByIdFetch(int id) {
		logger.debug("getting TaBoncdeAchat instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaBoncdeAchat a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
//					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.idDocument="+id+" " +
					" order by l.numLigneLDocument");
			TaBoncdeAchat instance = (TaBoncdeAchat)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaBoncdeAchat findByCodeFetch(String code) {
		logger.debug("getting TaBoncdeAchat instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaBoncdeAchat a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.codeDocument='"+code+"' " +
					" order by l.numLigneLDocument");
		
			TaBoncdeAchat instance = (TaBoncdeAchat)query.getSingleResult();
			instance.setLegrain(true);

			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		} catch (Exception e) {
			return null;
		}
	}

}

