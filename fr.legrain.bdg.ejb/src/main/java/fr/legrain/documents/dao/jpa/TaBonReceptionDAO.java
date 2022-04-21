package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ILotDAO;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.documents.dao.IBonReceptionDAO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaBonReception.
 * @see fr.legrain.documents.dao.TaBonReception
 * @author Hibernate Tools
 */
public class TaBonReceptionDAO /*extends AbstractApplicationDAO<TaBonReception> */
implements IBonReceptionDAO/*, IDocumentDAO<TaBonReception>,IDocumentTiersStatistiquesDAO<TaBonReception>,IDocumentTiersEtatDAO<TaBonReception>*/ {

//	private static final Log log = LogFactory.getLog(TaBonReceptionDAO.class);
	static Logger logger = Logger.getLogger(TaBonReceptionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	private @Inject ILotDAO daoLot;
	private @EJB ITaBoncdeAchatServiceRemote daoTaBoncdeAchat;
	private String defaultJPQLQuery = "select a from TaBonReception a";
	
	public TaBonReceptionDAO(){
//		this(null);
	}
	
//	public TaBonReceptionDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaBonReception.class.getSimpleName());
//		initChampId(TaBonReception.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaBonReception());
//	}
	
//	public TaBonReception refresh(TaBonReception detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			Map<IDocumentTiers,Boolean> listeEtatLegrain=new LinkedHashMap<IDocumentTiers, Boolean>();
//			detachedInstance.setLegrain(false);
//			
//        	for (TaRAcompte obj : detachedInstance.getTaRAcomptes()) {
//        		listeEtatLegrain.put(obj.getTaAcompte(),obj.getTaAcompte().isLegrain());
//        		obj.getTaAcompte().setLegrain(false);
//			}
//        	
//        	entityManager.refresh(detachedInstance);
//        	
//        	for (TaRAcompte obj : detachedInstance.getTaRAcomptes()) {        		
//        		obj.getTaAcompte().setLegrain(retourneEtatLegrain(listeEtatLegrain,obj.getTaAcompte()));
//			}
////			detachedInstance.setLegrain(false);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////
////			for (TaRAcompte a : detachedInstance.getTaRAcomptes()) {
////				session.evict(a.getTaAcompte().getTaRAcomptes());
////				session.evict(a.getTaAcompte());
////				session.evict(a);
////			}
////			session.evict(detachedInstance.getTaRAcomptes());
////			session.evict(detachedInstance);
////
////			detachedInstance = entityManager.find(TaBonReception.class, detachedInstance.getIdDocument());
//			detachedInstance.setLegrain(true);
//			detachedInstance.calculLignesTva();
//			return detachedInstance;
//
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaBonReception transientInstance) {
		logger.debug("persisting TaBonReception instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaBonReception persistentInstance) {
		logger.debug("removing TaBonReception instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaBonReception merge(TaBonReception detachedInstance) {
		logger.debug("merging TaBonReception instance");
		try {
			TaBonReception result = entityManager.merge(detachedInstance);
//			if(detachedInstance.getTaInfosDocument()==null) {
//				throw new RuntimeException("Il manque l'infoDocument du document n°: "+detachedInstance.getCodeDocument());
//			}
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaBonReception findById(int id) {
		logger.debug("getting TaBonReception instance with id: " + id);
		try {
			TaBonReception instance = entityManager.find(TaBonReception.class, id);
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
			Query query = entityManager.createQuery("select count(f) from TaBonReception f where f.codeDocument='"+code+"'");
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
	
	public TaBonReception findByCode(String code) {
		logger.debug("getting TaBonReception instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaBonReception a " +
					" left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");
			TaBonReception instance = (TaBonReception)query.getSingleResult();
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
	public List<TaBonReception> selectAll() {
		logger.debug("selectAll TaBonReception");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaBonReception> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaBonReception> findByCodeTiers(String codeTiers) {
		logger.debug("getting TaBonReception instance with codeTiers: " + codeTiers);
		List<TaBonReception> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_TIERS);
			query.setParameter("codeTiers", codeTiers);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaBonReception> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaBonReception instance with codeTiers: " + codeTiers);
		List<TaBonReception> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_TIERS_AND_DATE);
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
		logger.debug("getting TaBonReception instance with codeTiers: " + codeTiers);
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
				+" from TaBonReception f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
				+"group by "+groupBy+",t.codeTiers"
				;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaBonReception f "
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
	 * Recherche les devis entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaBonReception> rechercheDocument(Date dateDeb, Date dateFin) {

		List<TaBonReception> result = null;
		Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		return result;
	}
	
	
	/**
	 * Recherche les devis entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaBonReception> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {

		List<TaBonReception> result = null;
		Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		return result;
	}
	/**
	 * Recherche les devis entre 2 codes devis
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 codes (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(String codeDeb, String codeFin) {
	public List<TaBonReception> rechercheDocument(String codeDeb, String codeFin) {		
		List<TaBonReception> result = null;
		Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;

	}
//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeArticle, Date debut,
			Date fin) {
		return null;

	}

	public List<TaTiersDTO> findTiersDTOByCodeArticleAndDate(String codeArticle, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		List<TaTiersDTO> result = null;
		Query query = entityManager.createNamedQuery(TaLBonReception.QN.FIND_FOURNISSEUR_BY_CODE_ARTICLE_PAR_DATE);
		query.setParameter("codeArticle",codeArticle);
		query.setParameter("dateDeb", debut);
		query.setParameter("dateFin", fin);
		result = query.getResultList();
		return result;

	}
	
	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le montant des devis sur la période
	 */
	public List<Object> findCADevisSurPeriode(Date debut, Date fin) {
		logger.debug("getting nombre ca des devis");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" sum(d.netHtCalc)"
				+" FROM TaBonReception d " 
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
			+" FROM TaBonReception d " 
			+" where d.dateDocument between :dateDeb and :dateFin "
			+" and exists (select r " 
			+" from TaRDocument r " 
			+" where r.taDevis = d " 
			+" and ( r.taFacture IS NOT NULL " 
			+" OR r.taBonliv IS NOT NULL " 
			+" OR r.taBoncde IS NOT NULL " 
			+" OR r.taProforma IS NOT NULL )) "
			+" group by "+groupBy; 
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
	
	/**
	 * Classe permettant d'obtenir les devis transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des devis transformés
	 */
	public List<TaBonReception> findDevisTransfos(Date debut, Date fin) {
		logger.debug("getting nombre devis non transfos");
		List<TaBonReception> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" d"
				+" FROM TaBonReception d " 
				+" where d.dateDocument between :dateDeb and :dateFin"
				+" and  exists (select r " +
						"from TaRDocument r " +
						" where r.taDevis = d" +
						" and ( taFacture IS NOT NULL" +
						" OR taBonliv IS NOT NULL " +
						" OR taBoncde IS NOT NULL " +
						" OR taProforma IS NOT NULL))"
				+" order by d.mtHtCalc DESC";
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
	
	/**
	 * Classe permettant d'obtenir les devis non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des devis non transformés
	 */
	public List<TaBonReception> findDevisNonTransfos(Date debut, Date fin) {
		logger.debug("getting nombre devis non transfos");
		List<TaBonReception> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" d"
				+" FROM TaBonReception d " 
				+" where d.dateDocument between :dateDeb and :dateFin"
				+" and not exists (select r " +
						"from TaRDocument r " +
						" where r.taDevis = d" +
						" and ( taFacture IS NOT NULL" +
						" OR taBonliv IS NOT NULL " +
						" OR taBoncde IS NOT NULL " +
						" OR taProforma IS NOT NULL))"
						+" order by d.mtHtCalc DESC";
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
	
	@Override
	public List<TaBonReception> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers, null);
	}

	@Override
	public List<TaBonReception> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers, null);
	}

	@Override
	public List<TaBonReception> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaBonReception> result = null;
		Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaBonReception> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaBonReception> result = null;
		Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}	
	
	@Override
	public List<TaBonReception> rechercheDocumentEtat(Date dateDeb, Date datefin, String codeEtat) {
		List<TaBonReception> result = null;
//		Query query = null;
//		if(codeEtat!=null) {
//			query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_ETAT_DATE);
//			query.setParameter(1, dateDeb);
//			query.setParameter(2, datefin);
//			query.setParameter(3, codeEtat);
//		} else {
//			query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_ETAT_ENCOURS_DATE);
//			query.setParameter(1, datefin);
//		}
//		
//		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaBonReception> rechercheDocumentEtat(Date dateDeb, Date datefin,
			String codeEtat, String codeTiers) {
		List<TaBonReception> result = null;
//		Query query = null;
//		if(codeEtat!=null) {
//			query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_ETAT_TIERS_DATE);
//			query.setParameter(1, codeTiers);
//			query.setParameter(2, dateDeb);
//			query.setParameter(3, datefin);
//			query.setParameter(4, codeEtat);		
//		} else {
//			query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_ETAT_TIERS_ENCOURS_DATE);
//			query.setParameter(1, codeTiers);
//			query.setParameter(2, datefin);
//		}
//		
//		result = query.getResultList();
		return result;
	}

	/**
	 * Recherche les devis entre 2 dates light
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light) {
		List<Object[]> result = null;
		Query query = null;
		if(light)
			query =entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_DATE);
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
		query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,String codeTiers) {
			List<Object[]> result = null;
			Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}

	@Override
	public List<TaBonReception> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaBonReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaBonReception> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaBonReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaBonReception value) throws Exception {
		BeanValidator<TaBonReception> validator = new BeanValidator<TaBonReception>(TaBonReception.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaBonReception value, String propertyName)
			throws Exception {
		BeanValidator<TaBonReception> validator = new BeanValidator<TaBonReception>(TaBonReception.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaBonReception transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public String genereCode() {
		System.err.println("******************* NON IMPLEMENTE ****************************************");
		return null;
	}

	// Dima - Début
	public List<TaLBonReception> bonRecepFindByCodeArticle(String codeArticle){
		
		logger.debug("getting TaLBonReception instance with Code Article : " + codeArticle );
		try {
			Query query = entityManager.createNamedQuery(TaLBonReception.QN.FIND_BY_CODE_ARTICLE);
			query.setParameter("codeArticle", codeArticle);
			List<TaLBonReception> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
		//return null;
	}
	
	public List<TaLBonReception> bonRecepFindByCodeArticleParDate(String codeArticle , Date dateDeb, Date dateFin){
		
		logger.debug("getting TaLBonReception instance with Code Article : " + codeArticle );
		try {
			Query query = entityManager.createNamedQuery(TaLBonReception.QN.FIND_BY_CODE_ARTICLE_PAR_DATE);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", dateFin);
			List<TaLBonReception> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
		//return null;
	}
	
	public List<TaLBonReception> bonRecepFindByLotParDate(String numLot , Date dateDeb, Date dateFin){
		
		logger.debug("getting TaLBonReception instance with lot : " + numLot );
		try {
			Query query = entityManager.createNamedQuery(TaLBonReception.QN.FIND_BY_LOT_PAR_DATE);
			query.setParameter("numLot", numLot);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", dateFin);
			List<TaLBonReception> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
		//return null;
	}
	
	
	public List<TaLBonReceptionDTO> bonRecepFindByCodeFournisseurParDate(String codeFournisseur , Date dateDeb, Date dateFin){
		
		logger.debug("getting TaLBonReceptionDTO instance with Code fournisseur : " + codeFournisseur );
		try {
//			if(codeFournisseur==null || codeFournisseur.equals(Const.C_CHOISIR))codeFournisseur="%";
			Query query = entityManager.createNamedQuery(TaLBonReception.QN.FIND_BY_CODE_FOURNISSEUR_PAR_DATE);
			query.setParameter("codeTiers", codeFournisseur);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", dateFin);
			List<TaLBonReceptionDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
		//return null;
	}
	// Dima -  Fin
	
	public List<TaBonReceptionDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaBonReception.QN.FIND_ALL_LIGHT);
			List<TaBonReceptionDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaBonReceptionDTO> findByCodeLight(String code) {
		logger.debug("getting TaBonReceptionDTO instance");
		try {		
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaBonReception.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeDocument", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaBonReception.QN.FIND_ALL_LIGHT);
			}

			List<TaBonReceptionDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaBonReception> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public TaBonReception findAvecResultatConformites(int idBr) {
		logger.debug("findResultatConformites");
		try {
			String query="select br from TaBonReception br join br.lignes lbr join lbr.taLot l left join  l.taResultatConformites "
					+ "   LEFT JOIN  l.taArticle.taConformites cf "
					+ " where br.idDocument ="+idBr;
			Query ejbQuery = entityManager.createQuery(query);
			TaBonReception instance = (TaBonReception) ejbQuery.getSingleResult();
			for (TaLBonReception l : instance.getLignes()) {
				l.getTaLot().setRetourControlLot(daoLot.retourControl(l.getTaLot()));
			}
			logger.debug("selectAll successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaBonReception> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBonReception> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
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
	public List<TaBonReception> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public TaBonReception findDocByLDoc(ILigneDocumentTiers lDoc) {
			try {
				Query query = entityManager.createQuery("select a from TaBonReception a " +
						"  join  a.lignes l"+
						" where l=:ldoc " +
								" order by l.numLigneLDocument");
				query.setParameter("ldoc", lDoc);
				TaBonReception instance = (TaBonReception)query.getSingleResult();
//				instance.setLegrain(true);
				logger.debug("get successful");
				return instance;
			} catch (RuntimeException re) {
				throw re;
			}
		}	

	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a.idDocument from TaBonReception a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			int instance = (int)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public TaBonReception findByIdFetch(int id) {
		logger.debug("getting TaBonReception instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaBonReception a " +
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
			TaBonReception instance = (TaBonReception)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaBonReception findByCodeFetch(String code) {
		logger.debug("getting TaBonReception instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaBonReception a " +
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
		
			TaBonReception instance = (TaBonReception)query.getSingleResult();
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
