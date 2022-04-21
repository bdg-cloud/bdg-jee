package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosApporteur;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.IApporteurDAO;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaApporteur.
 * @see fr.legrain.documents.dao.TaApporteur
 * @author Hibernate Tools
 */
public class TaApporteurDAO extends AbstractDocumentDAO<TaApporteur,TaInfosApporteur, TaLApporteur> implements IApporteurDAO,IDocumentDAO<TaApporteur>,IDocumentTiersStatistiquesDAO<TaApporteur> {

//	private static final Log log = LogFactory.getLog(TaApporteurDAO.class);
	static Logger logger = Logger.getLogger(TaApporteurDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaApporteur a";	
	public TaApporteurDAO(){
//		this(null);
	}
	
//	public TaApporteurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaApporteur.class.getSimpleName());
//		initChampId(TaApporteur.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaApporteur());
//	}
	
//	public TaApporteur refresh(TaApporteur detachedInstance) {
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
////			detachedInstance = entityManager.find(TaApporteur.class, detachedInstance.getIdDocument());
//			detachedInstance.setLegrain(true);
//			detachedInstance.calculLignesTva();
//			return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaApporteur transientInstance) {
		logger.debug("persisting TaApporteur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaApporteur persistentInstance) {
		logger.debug("removing TaApporteur instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}
	
	public void annuler(TaApporteur persistentInstance) throws Exception{
		persistentInstance.setLegrain(false);
		List<TaLApporteur> listTemp=new ArrayList<TaLApporteur>(0);
		for (Object ligne : persistentInstance.getLignes()) {
			if(((TaLApporteur)ligne).getIdLDocument()==0)
				listTemp.add((TaLApporteur)ligne);				
		}
		for (TaLApporteur taLApporteur : listTemp) {
			persistentInstance.getLignes().remove(taLApporteur);
		}
		//super.annuler(persistentInstance);
	}
	

	public TaApporteur merge(TaApporteur detachedInstance) {
		logger.debug("merging TaApporteur instance");
		try {
			TaApporteur result = entityManager.merge(detachedInstance);
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

	public TaApporteur findById(int id) {
		logger.debug("getting TaApporteur instance with id: " + id);
		try {
			TaApporteur instance = entityManager.find(TaApporteur.class, id);
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
			Query query = entityManager.createQuery("select count(f) from TaApporteur f where f.codeDocument='"+code+"'");
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
	
	
	public TaApporteur findByCode(String code) {
		logger.debug("getting TaApporteur instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaApporteur a " +
					"  left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");
			TaApporteur instance = (TaApporteur)query.getSingleResult();
			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaApporteur> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaApporteur instance with codeTiers: " + codeTiers);
		List<TaApporteur> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_TIERS_AND_DATE);
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
		logger.debug("getting TaApporteur instance with codeTiers: " + codeTiers);
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
				+" from TaApporteur f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
				+"group by "+groupBy+",t.codeTiers"
				;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaApporteur f "
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
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaApporteur> selectAll() {
		logger.debug("TaApporteur TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaApporteur> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les factures apporteur entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures apporteur entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaApporteur> rechercheDocument(Date dateDeb, Date dateFin) {
		List<TaApporteur> result = null;
		Query query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	
	/**
	 * Recherche les factures apporteur entre 2 codes factures apporteur
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des factures apporteur entre ces 2 codes (null en cas d'erreur)
	 */
	public List<TaApporteur> rechercheDocument(String codeDeb, String codeFin) {		
		List<TaApporteur> result = null;
		Query query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
		result = query.getResultList();
		return result;
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
	 * Classe permettant d'obtenir le ca généré par les apporteur sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoi le montant des par les apporteurs sur la période
	 */
	public List<Object> findCASurPeriode(Date debut, Date fin) {
		logger.debug("getting nombre ca des apporteurs");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" sum(d.netHtCalc)"
				+" FROM TaApporteur d " 
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
	
	/**
	 * Méthode permettant de retourner le chiffre d'affaire total généré
	 * @param debut
	 * @param fin
	 * @param precision
	 * @return
	 */
	public List<Object> findChiffreAffaireTotal(Date debut, Date fin, int precision) {
		logger.debug("getting ChiffreAffaire total");
		List<Object> result = null;
		try {
			String requete = "";
			String groupBy = "";
			String select = "";
			
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
			
			requete = "SELECT "+select+ ", "
			+" case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
			+" case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
			+" case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
			+" FROM TaApporteur f " 
			+" where f.dateDocument between :dateDeb and :dateFin"
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
	 * Classe permettant d'obtenir de la base de données X clients ordonnés par chiffre d'affaires
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param leNbResult le nombre de résultats à sortir de la BD
	 * Si cet int est positif, il sort les leNbResult meilleurs
	 * Si cet int est négatif, il sort les leNbResult moins bons
	 * @return La requête renvoyée renvoi le CA, le codeTiers, le nom et le CP
	 */
	public List<Object> findMeilleursClientsParCA(Date debut, Date fin, int leNbResult) {
		logger.debug("getting ChiffreAffaire total ordo tiers");
		
		int nbResult = leNbResult; // nombre de résultats à afficher
		String ordre = "DESC";     // Ordre d'affichage
		
		if (nbResult<0){ // On passe en paramètres une valeur négative : on souhaite les x derniers
			nbResult = Math.abs(leNbResult); // On prend la valeur absolue du nombre désiré
			ordre = "ASC";					   // On inverse l'ordre d'affichage
			
		}
		List<Object> result = null;
		try {
			String requete = "";

			
			requete = "SELECT "
			+" sum(f.mtHtCalc)"
			+", t.codeTiers"
			+", t.nomTiers"
			+", a.codepostalAdresse"
			+" FROM TaApporteur f " 
			+" left join f.taTiers t"
			+" left join t.taAdresse a"
			+" where f.dateDocument between :dateDeb and :dateFin"
			+" group by t.codeTiers, t.nomTiers, a.codepostalAdresse"
			+" order by sum(f.mtHtCalc)"+ordre;
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			query.setMaxResults(nbResult);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir de la base de données X articles ordonnés par chiffre d'affaires
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param leNbResult le nombre de résultats à sortir de la BD
	 * Si cet int est positif, il sort les leNbResult meilleurs
	 * Si cet int est négatif, il sort les leNbResult moins bons
	 * @return La requête renvoyée renvoi le CA, le codeTiers, le nom et le CP
	 */
	public List<Object> findMeilleursArticlesParCA(Date debut, Date fin, int leNbResult) {
		logger.debug("getting ChiffreAffaire total ordo articles");
		
		int nbResult = leNbResult; // nombre de résultats à afficher
		String ordre = "DESC";     // Ordre d'affichage
		
		if (nbResult<0){ // On passe en paramètres une valeur négative : on souhaite les x derniers
			nbResult = Math.abs(leNbResult); // On prend la valeur absolue du nombre désiré
			ordre = "ASC";					   // On inverse l'ordre d'affichage
			
		}
		List<Object> result = null;
		try {
			String requete = "";

			
			requete = "SELECT a.codeArticle"
			+", a.libellecArticle"
			+", p.prixPrix"
			+", sum(l.qteLDocument)"
			+", sum(l.mtHtLDocument)"
			+" FROM TaLApporteur l " 
			+" left join l.taDocument f"
			+" left join l.taArticle a"
			+" left join a.taPrix p"
			+" where f.dateDocument between :dateDeb and :dateFin"
			+" group by a.codeArticle, a.libellecArticle, p.prixPrix"
			+" order by sum(l.mtHtLDocument)"+ordre;
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			query.setMaxResults(nbResult);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaApporteur> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers,null);
		}

	@Override
	public List<TaApporteur> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers, null);
	}

	@Override
	public List<TaApporteur> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaApporteur> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaApporteur> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaApporteur> result = null;
		Query query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
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
			query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE);
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
		query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_LIGHT);
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
			Query query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}

	@Override
	public List<TaApporteur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaApporteur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaApporteur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaApporteur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaApporteur value) throws Exception {
		BeanValidator<TaApporteur> validator = new BeanValidator<TaApporteur>(TaApporteur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaApporteur value, String propertyName)
			throws Exception {
		BeanValidator<TaApporteur> validator = new BeanValidator<TaApporteur>(TaApporteur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaApporteur transientInstance) {
		entityManager.detach(transientInstance);		
	}

	@Override
	public List<TaApporteur> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Recherche les apporteurs entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des apporteurs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaApporteurDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaApporteurDTO> result = null;
		Query query = null;
		if(parDate)query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_NON_EXPORT_PARDATE_LIGHT);
		else
		query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	/**
	 * Recherche les apporteurs entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des apporteurs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaApporteur> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaApporteur> result = null;
		Query query = null;
		if(parDate)query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_NON_EXPORT_PARDATE);
		else
		query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_NON_EXPORT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}

	/**
	 * Recherche les factures apporteurs entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures apporteurs entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaApporteur> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		List<TaApporteur> result = null;
		Query query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}

	@Override
	public List<TaApporteur> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaApporteur> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Date> findDateExport(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select distinct(f.dateExport) from TaApporteur f where f.dateExport is not null and f.dateExport between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			List<Date> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<TaApporteur> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaApporteur> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATEEXPORT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_TIERS_AND_DATE);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}
		result = query.getResultList();
		return result;
	}


	public List<TaApporteurDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaApporteur.QN.FIND_ALL_LIGHT);
			List<TaApporteurDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteARelancer() {
		// TODO Auto-generated method stub
		return " ";
	}

	@Override
	public String getRequeteARelancerJPQL() {
		// TODO Auto-generated method stub
		return " ";
	}



	@Override
	public TaApporteur findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaApporteur a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaApporteur instance = (TaApporteur)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<TaApporteurDTO> rechercheDocumentDTO(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaApporteurDTO> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATEEXPORT_LIGHT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}


		result = query.getResultList();
		return result;
	}

	

	@Override
	public List<TaApporteurDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaApporteurDTO> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_EXPORT_LIGHT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		else query = entityManager.createNamedQuery(TaApporteur.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public Date selectMinDateDocumentNonExporte(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select min(f.dateDocument) from TaApporteur f where f.dateExport is null and f.dateDocument between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			Date l = (Date) query.getSingleResult();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public TaApporteur findByIdFetch(int id) {
		logger.debug("getting TaApporteur instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaApporteur a " +
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
			TaApporteur instance = (TaApporteur)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaApporteur findByCodeFetch(String code) {
		logger.debug("getting TaApporteur instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaApporteur a " +
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
		
			TaApporteur instance = (TaApporteur)query.getSingleResult();
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

