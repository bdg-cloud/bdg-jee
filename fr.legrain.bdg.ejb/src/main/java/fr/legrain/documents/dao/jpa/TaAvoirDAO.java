package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.documents.dao.IAvoirDAO;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaAvoir.
 * @see fr.legrain.documents.dao.TaAvoir
 * @author Hibernate Tools
 */
public class TaAvoirDAO extends AbstractDocumentPayableDAO<TaAvoir,TaInfosAvoir,TaLAvoir>/*extends AbstractApplicationDAO<TaAvoir>*/ implements IAvoirDAO, IDocumentDAO<TaAvoir>,
IDocumentTiersStatistiquesDAO<TaAvoir>,IDocumentTiersEtatDAO<TaAvoir> {

	//	private static final Log log = LogFactory.getLog(TaAvoirDAO.class);
	static Logger logger = Logger.getLogger(TaAvoirDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaAvoir a";

	public TaAvoirDAO(){
//		this(null);
	}



	public void persist(TaAvoir transientInstance) {
		logger.debug("persisting TaAvoir instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaAvoir persistentInstance) {
		logger.debug("removing TaAvoir instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaAvoir merge(TaAvoir detachedInstance) {
		logger.debug("merging TaAvoir instance");
		try {
			TaAvoir result = entityManager.merge(detachedInstance);
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

	public TaAvoir findById(int id) {
		logger.debug("getting TaAvoir instance with id: " + id);
		try {
			TaAvoir instance = entityManager.find(TaAvoir.class, id);
			instance.getTaTiers().getIdTiers();
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
			Query query = entityManager.createQuery("select count(f) from TaAvoir f where f.codeDocument='"+code+"'");
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
	
	
	public TaAvoir findByCode(String code) {
		logger.debug("getting TaAvoir instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaAvoir a " +
					"  left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
			" order by l.numLigneLDocument");
			TaAvoir instance = (TaAvoir)query.getSingleResult();
			instance.getTaTiers().getIdTiers();

			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaAvoir> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaAvoir instance with codeTiers: " + codeTiers);
		List<TaAvoir> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_TIERS_AND_DATE);
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
		logger.debug("getting TaAvoir instance with codeTiers: " + codeTiers);
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
				+" from TaAvoir f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between ? and ? "
				+"group by "+groupBy+",t.codeTiers"
				;

			//			jpql = "select  "
			//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
			//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
			//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
			//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
			//			+" from TaAvoir f "
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
	 * Recherche les avoirs entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaAvoir> rechercheDocument(Date dateDeb, Date dateFin) {
		Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		List<TaAvoir> l = query.getResultList();
		return l;
	}

	
	/**
	 * Recherche les avoirs entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaAvoir> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		List<TaAvoir> l = query.getResultList();
		return l;
	}
	
	/**
	 * Recherche les avoirs entre 2 codes avoirs
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 codes (null en cas d'erreur)
	 */
	//public String[] rechercheAvoir(String codeDeb, String codeFin) {
	public List<TaAvoir> rechercheDocument(String codeDeb, String codeFin) {
		//String[] result = null;
		List<TaAvoir> result = null;
		Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
		List<TaAvoir> l = query.getResultList();
		return l;

	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaAvoir> selectAll() {
		logger.debug("selectAll TaAvoir");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAvoir> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaAvoir> selectAllLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin) {
		String taTiersLoc=null;
		if(taDocument!=null)taTiersLoc=taDocument.getCodeTiers();
		logger.debug("selectAll TaAvoir");
		try {
			if(taTiersLoc!=null){				
				if(dateDeb==null)dateDeb=new Date(0);
				if(dateFin==null)dateFin=new Date("01/01/3000");
				
				String requete=
//				"select a from TaAvoir a where " +
//				" not exists(select r from TaRAvoir r  where r.taAvoir.idDocument=a.id " +
//				" and (r.taFacture=:facture))" +
//				" and a.netTtcCalc > (select sum(ra.affectation) from TaRAvoir ra where ra.taAvoir.idDocument=a.idDocument)"+
//				" and a.taTiers =:tiers"+
//				" and a.dateDocument between :deb and :fin "+
//				" union "+
				" select a from TaAvoir a where " +
				" exists(select rr from TaRAvoir rr where rr.taAvoir.idDocument=a.id " +
				" and (rr.taFacture.codeDocument=:facture))" +
				" and a.taTiers.codeTiers =:tiers"+
				" and a.dateDocument between :deb and :fin"+
				
				 " order by a.codeDocument";
				
				Query ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("facture", taDocument.getCodeDocument());
				ejbQuery.setParameter("tiers", taTiersLoc);
				ejbQuery.setParameter("deb", dateDeb,TemporalType.DATE);
				ejbQuery.setParameter("fin", dateFin,TemporalType.DATE);

				List<TaAvoir> l = ejbQuery.getResultList();

				logger.debug("selectAll successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public List<TaAvoir> selectReglementNonLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin) {
		String taTiersLoc=null;
		if(taDocument!=null)taTiersLoc=taDocument.getCodeTiers();
		logger.debug("selectAll TaAvoir");
		try {
			if(taTiersLoc!=null){				
				if(dateDeb==null)dateDeb=new Date(0);
				if(dateFin==null)dateFin=new Date("01/01/3000");
				
				String requete=
						"select a from TaAvoir a where " +
						" not exists(select r from TaRAvoir r  where r.taAvoir.idDocument=a.id " +
						" and (r.taFacture.codeDocument=:facture))" +
						" and a.netTtcCalc > (select coalesce(sum(ra.affectation),0) from TaRAvoir ra where ra.taAvoir.idDocument=a.idDocument)"+
						" and a.taTiers.codeTiers =:tiers"+
						" and a.dateDocument between :deb and :fin "+
						
						 " order by a.codeDocument";
						
						Query ejbQuery = entityManager.createQuery(requete);
						ejbQuery.setParameter("facture", taDocument.getCodeDocument());
						ejbQuery.setParameter("tiers", taTiersLoc);
						ejbQuery.setParameter("deb", dateDeb,TemporalType.DATE);
						ejbQuery.setParameter("fin", dateFin,TemporalType.DATE);


				List<TaAvoir> l = ejbQuery.getResultList();
				logger.debug("selectAll successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeTiers, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaAvoir> selectAllDisponible(TaFacture taFacture) {
		logger.debug("TaAvoir selectAllDisponible");
		try {
			String requete ="";
			Query ejbQuery =null;
			if(taFacture!=null ){
				requete ="select a from TaAvoir  a " +
				" where a.taTiers=:taTiers and (" +
				" not exists(select ra from TaRAvoir ra " +
				"where  ra.taAvoir=a and ra.taFacture is not null) or" +
				" (a.netTtcCalc > (select sum(ra.affectation) from TaRAvoir ra " +
				"where  ra.taAvoir=a and ra.taFacture is not null))) " ;
				ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("taTiers", taFacture.getTaTiers());
			}

			if(taFacture!=null ){
				List<TaAvoir> l = ejbQuery.getResultList();
				logger.debug("selectAllDisponible successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAllDisponible failed", re);
			throw re;
		}
	}

	public int selectCountDisponible(TaTiers taTiers) {
		logger.debug("TaAvoir selectCountDisponible");
		Long count=(long) 0;
		try {
			String requete ="";
			Query ejbQuery =null;
			if(taTiers!=null ){
				requete ="select count(a) from TaAvoir  a " +
				" where a.taTiers=:taTiers and (" +
				" not exists(select ra from TaRAvoir ra " +
				"where  ra.taAvoir=a and ra.taFacture is not null) or" +
				" (a.netTtcCalc > (select sum(ra.affectation) from TaRAvoir ra " +
				"where  ra.taAvoir=a and ra.taFacture is not null))) " ;
				ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("taTiers", taTiers);
			}

			if(taTiers!=null ){
				count = (long) ejbQuery.getSingleResult();
				logger.debug("selectAllDisponible successful");
				return  count.intValue();
			}
			return 0;
		} catch (RuntimeException re) {
			logger.error("selectAllDisponible failed", re);
			throw re;
		}
	}
	
	public List<TaAvoirDTO> selectAllDisponibleDTO(TaFacture taFacture) {
		logger.debug("TaAvoir selectAllDisponibleDTO");
		try {
			String requete ="";
			Query ejbQuery =null;
			if(taFacture!=null ){
				requete ="select new fr.legrain.document.dto.TaAvoirDTO(f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, "
						+ "infos.nomTiers,infos.prenomTiers,infos.nomEntreprise,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc) "
						+ "from TaAvoir f join f.taInfosDocument infos join f.taTiers tiers  a " +
				" where a.taTiers=:taTiers and (" +
				" not exists(select ra from TaRAvoir ra " +
				"where  ra.taAvoir=a and ra.taFacture is not null) or" +
				" (a.netTtcCalc > (select sum(ra.affectation) from TaRAvoir ra " +
				"where  ra.taAvoir=a and ra.taFacture is not null))) " ;
				ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("taTiers", taFacture.getTaTiers());
			}

			if(taFacture!=null ){
				List<TaAvoirDTO> l = ejbQuery.getResultList();
				logger.debug("selectAllDisponible successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAllDisponible failed", re);
			throw re;
		}
	}

	public void messageNonAutoriseModificationRelation(TaAvoir avoir) throws Exception{
//		if(!dataSetEnModif()) {
//			String message="l'avoir "+avoir.getCodeDocument()+" a été modifié par ailleurs !!!" +
//			"\r\nIl doit être rechargé pour y " +
//			"apporter de nouvelles modifications.";
//			MessageDialog.openWarning(PlatformUI.getWorkbench()
//					.getActiveWorkbenchWindow().getShell(),"Attention",
//					message);
//			throw new Exception();
//		}
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les avoirs sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoi le montant des par les avoirs sur la période
	 */
	public List<Object> findCASurPeriode(Date debut, Date fin) {
		logger.debug("getting nombre ca des avoirs");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" sum(d.netHtCalc)"
				+" FROM TaAvoir d " 
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
			+" FROM TaAvoir f " 
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
			+" FROM TaAvoir f " 
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
			+" FROM TaLAvoir l " 
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
	
	/**
	 * Recherche les avoirs entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaAvoir> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaAvoir> result = null;
		Query query = null;
		if(parDate)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_NON_EXPORT_PARDATE);
		else
		query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_NON_EXPORT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	/**
	 * Recherche les avoirs entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaAvoirDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaAvoirDTO> result = null;
		Query query = null;
		if(parDate)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_NON_EXPORT_PARDATE_LIGHT);
		else
		query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	/**
	 * Recupere la liste des avoirs exportees ou non exportees
	 * @param export - vrai pour l'avoir deja exportees, faux pour celles non exportees
	 * @return
	 */
	public List<TaAvoir> findByExport(boolean export) {
		try {
			String requete="select a from TaAvoir a ";
			Query query = entityManager.createQuery(requete );
			if(export)
				requete+="where a.dateExport is not null";
			else 
				requete+="where a.dateExport is null";
			List<TaAvoir> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			logger.error("findByExport failed", re);
			throw re;
		}
	}

	@Override
	public List<TaAvoir> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers,null);
	}

	@Override
	public List<TaAvoir> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers,null);
		
	}

	@Override
	public List<TaAvoirDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaAvoirDTO> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_EXPORT_LIGHT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		else query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	
	@Override
	public List<TaAvoir> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaAvoir> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_NON_EXPORT);
		else query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaAvoir> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaAvoir> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATEEXPORT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_TIERS_AND_DATE);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}
		result = query.getResultList();
		return result;
	}
	
	@Override
	public List<TaAvoir> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaAvoir> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_CODE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_CODE_NON_EXPORT);
		else query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_TIERS_AND_CODE);
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
			query =entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE);
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
		query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_LIGHT);
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
			Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}

	@Override
	public List<TaAvoir> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAvoir> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAvoir> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAvoir> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAvoir value) throws Exception {
		BeanValidator<TaAvoir> validator = new BeanValidator<TaAvoir>(TaAvoir.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAvoir value, String propertyName)
			throws Exception {
		BeanValidator<TaAvoir> validator = new BeanValidator<TaAvoir>(TaAvoir.class);
		return validator.validateField(value,propertyName);
	}
	

	@Override
	public void detach(TaAvoir transientInstance) {
		entityManager.detach(transientInstance);
	}

	
	public List<TaAvoirDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_ALL_LIGHT);
			List<TaAvoirDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaAvoir> rechercheDocumentEtat(Date dateDeb, Date dateFin, String codeEtat) {
//		List<TaAvoir> result = null;
//		Query query = null;
////		Date date2 = LibDate.incrementDate(dateDeb, nbJours, 0, 0);
////		System.out.println("rechercheDocumentEtat => date2 : "+date2);
//		query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_ETAT_DATE);
//		query.setParameter(1, dateDeb);
//		query.setParameter(2, dateFin);
//		query.setParameter(3, codeEtat);
//		result = query.getResultList();
//		return result;
		return null;
	}

	@Override
	public List<TaAvoir> rechercheDocumentEtat(Date dateDeb, Date datefin, String codeEtat, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaAvoir> selectAll(IDocumentTiers taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	 /**
//	 * Classe permettant d'obtenir le nombre d'avoir dans la période
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre total d'avoir dans la période
//	 */
//	@Override
//	public long countDocument(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre avoir dans periode");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaAvoir d " 
//				+" where d.dateDocument between :dateDebut and :dateFin and d.taTiers.codeTiers like :codeTiers";
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("dateFin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbAvoir = (Long)query.getSingleResult();
//			result = nbAvoir;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

	/**
	 * Classe permettant d'obtenir les factures non payées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de facture non payées
	 */
//	public long countFactureNonPayes(Date debut, Date fin) {
//		logger.debug("getting nombre facture non paye");
//		Long result = (long) 0;
//		
//		
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaFacture d " 
//				+" where d.dateDocument between :dateDebut and :datefin"
//				+" and d.taReglement IS NULL";
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("datefin", fin);
//			Long nbDevisNonTranforme = (Long)query.getSingleResult();
//			result = nbDevisNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

	/**
	 * Classe permettant d'obtenir le nombre d'avoir non affecté
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre d'avoir non affecté
	 */
//	public long countFactureNonPayeARelancer(Date debut, Date fin, int deltaNbJours) {
//		logger.debug("getting nombre facture non payées à relancer");
//		Long result = (long) 0;
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date dateJour = LibDate.dateDuJour();
//		
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaFacture d " 
//				+" where d.dateDocument between :dateDebut and :datefin"
//				+" and d.dateEchDocument <= :dateref"
//				+" and d.dateEchDocument >= :datejour"
//				+" and d.taReglement IS NULL ";
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("dateref", dateref);
//			query.setParameter("datejour", dateJour);
//			Long nbFactureNonTranformeARelancer = (Long)query.getSingleResult();
//			result = nbFactureNonTranformeARelancer;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

//	/**
//	 * Classe permettant d'obtenir les avoirs affectés même partiellement
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre d'avoirs affectés 
//	 */
//	public long countDocumentTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre avoir transfos");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";		
//		try {
//			String requete = "";
//
//			requete="select count(f) "
//					+ " from TaAvoir f  "
//					+ " where f.dateDocument between :dateDebut and :dateFin "
//					+ " and f.taTiers.codeTiers like :codeTiers"
//					+ " and f.taEtat is null"
//					+ " and  exists(select f2.netTtcCalc from TaAvoir f2 "
//					+ " left join f2.taRAvoirs ra where f=f2 "
//					+ " group by f2.netTtcCalc "					
//					+ "  having coalesce(sum(ra.affectation),0)>= f2.netTtcCalc )";
//
//			
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("dateFin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbFactureNonTranforme = (Long)query.getSingleResult();
//			result = nbFactureNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

	
//	//début ici
//
//	/**
//	 * Classe permettant d'obtenir le ca généré par les factures sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA total ht des avoirs sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date dateDebut, Date dateFin) {
//		try {
//			Query query = null;
//			query = entityManager.createNamedQuery(SUM_CA_TOTAL_LIGTH_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			List<DocumentChiffreAffaireDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
////			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}


//	@Override
//	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre avoir non affecté totalement");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete="select count(f) "
//					+ " from TaAvoir f "					
//					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"
//					+ " and f.taEtat is null "
//					+ " and exists(select f2.netTtcCalc from TaAvoir f2 "
//					+ " left join f2.taRAvoirs ra where f=f2 "
//					+ " group by f2.netTtcCalc "					
//					+ "  having coalesce(sum(ra.affectation),0)< f2.netTtcCalc )";
//
//
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("dateFin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbDevisNonTranforme = (Long)query.getSingleResult();
//			result = nbDevisNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

//	@Override
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting nombre avoir non affectes à relancer");
//		Long result = (long) 0;
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date dateJour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//			requete="select count(f) "
//					+ " from TaAvoir f  "
//					+ " where f.dateDocument between :dateDebut and :dateFin  and f.dateEchDocument < :datejour"
//					+ " and f.taTiers.codeTiers like :codeTiers"
//					+ " and f.taEtat is null"
//					+ " and  exists(select f2.netTtcCalc from TaAvoir f2 "
//					+ " left join f2.taRAvoirs ra where f=f2 "
//					+ " group by f2.netTtcCalc "					
//					+ "  having coalesce(sum(ra.affectation),0)< f2.netTtcCalc )";
//			
//
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("dateFin", fin);
////			query.setParameter("dateref", dateref);
//			query.setParameter("datejour", dateJour);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbFactureNonTranformeARelancer = (Long)query.getSingleResult();
//			result = nbFactureNonTranformeARelancer;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//
//
//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaAvoir.QN.SUM_CA_TOTAL_LIGTH_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("codeTiers","%");
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}
//			
//	}
//


//	@Override
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		DocumentChiffreAffaireDTO infosCaTotal = null;
//		infosCaTotal = listeChiffreAffaireTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//		infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		return infosCaTotal;
//}



//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaAvoir.QN.SUM_AFFECTATION_TOTAL_LIGHT_PERIODE_TOTALEMENTAFFECTE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("codeTiers","%");
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}
////		return null;	
//	}
//


//	@Override
//	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		DocumentChiffreAffaireDTO infosCaTotal = null;
//		infosCaTotal = listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//		infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		return infosCaTotal;
//}



//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_TOTAL_LIGTH_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("codeTiers","%");
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}
////		return null;	
//	}
//


//	@Override
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		DocumentChiffreAffaireDTO infosCaTotal = null;
//		infosCaTotal = listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//		infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		return infosCaTotal;
//}


//
//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut,
//			Date dateFin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting reste à règler des factures non totalement règler et qui n'ont pas d'état et donc à relancer");
//		List<TaDevisDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		Query query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_TOTAL_LIGTH_PERIODE_A_RELANCER);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
////		query.setParameter("dateref", dateref, TemporalType.DATE);
//		query.setParameter("datejour", datejour, TemporalType.DATE);
////		query.setParameter("codeTiers","%");
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentChiffreAffaireDTO> l = query.getResultList();
//		if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//			l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//		}
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//	}
////		return null;
//	}



//	@Override
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin,
//			int deltaNbJours,String codeTiers) {
//		DocumentChiffreAffaireDTO infosCaTotal = null;
//		infosCaTotal = listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours,codeTiers).get(0);
//		infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//		return infosCaTotal;
//}


//
//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_CA_ANNEE_LIGTH_PERIODE);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_CA_MOIS_LIGTH_PERIODE);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_CA_JOUR_LIGTH_PERIODE);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("codeTiers","%");
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


//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
//			int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_ANNEE_LIGTH_PERIODE);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_MOIS_LIGTH_PERIODE);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_JOUR_LIGTH_PERIODE);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("codeTiers","%");
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
////		return null;	
//	}
//

//
//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin,
//			int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_AFFECTATION_ANNEE_LIGTH_PERIODE_TOTALEMENTAFFECTE);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_AFFECTATION_MOIS_LIGTH_PERIODE_TOTALEMENTAFFECTE);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_AFFECTATION_JOUR_LIGTH_PERIODE_TOTALEMENTAFFECTE);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("codeTiers","%");
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
////		return null;	
//	}
//


//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
//			int precision, int deltaNbJours,String codeTiers) {
//		// TODO Auto-generated method stub
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//			Date datejour = LibDate.dateDuJour();
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_ANNEE_LIGTH_PERIODE_A_RELANCER);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_MOIS_LIGTH_PERIODE_A_RELANCER);
//			
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAvoir.QN.SUM_RESTE_A_AFFECTER_JOUR_LIGTH_PERIODE_A_RELANCER);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("dateref", dateref, TemporalType.DATE);
//			query.setParameter("datejour", datejour, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
////		return null;	
//	}



//	@Override
//	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin, String codeTiers) {
//		try {
//			if(codeTiers==null)codeTiers="%";
//			Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_ALL_LIGHT_PERIODE);
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



//	@Override
//	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin, String codeTiers) {
//		logger.debug("getting nombre Acompte non transfos");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_NON_AFFECTE_LIGHT_PERIODE);
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
//
//
//	@Override
//	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,
//			String codeTiers) {
//		logger.debug("getting liste Acompte non transfos à relancer");
//		List<DocumentDTO> result = null;
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_NON_AFFECTE_ARELANCER_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
////		query.setParameter("dateref", dateref, TemporalType.DATE);
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
//
//
//	@Override
//	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin, String codeTiers) {
//		logger.debug("getting nombre Acompte transforme");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaAvoir.QN.FIND_AFFECTE_LIGHT_PERIODE);
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


	
	@Override
	public List<TaAvoir> rechercheDocumentVerrouille(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		List<TaAvoir> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_VERROUILLE);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE_NON_VERROUILLE);
		else
			query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATE);	
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();		
		return result;
	}

	@Override
	public List<TaAvoir> rechercheDocumentVerrouille(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaAvoir> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_CODE_VERROUILLE);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_CODE_NON_VERROUILLE);
		else
		query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", codeDeb);
		query.setParameter("dateFin", codeFin);
		result = query.getResultList();
		return result;
	}



	@Override
	public List<Date> findDateExport(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select distinct(f.dateExport) from TaAvoir f where f.dateExport is not null and f.dateExport between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			List<Date> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}



	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return "select f2.netTtcCalc from TaAvoir f2  left join f2.taRAvoirs ra where "+prefixe+"=f2 " + 
				"	group by f2.netTtcCalc  having coalesce(sum(ra.affectation),0)>= f2.netTtcCalc ";
	}



	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getRequeteSommeAffecte(String prefixe) {
		// TODO Auto-generated method stub
		return " select coalesce(sum(ra.affectation),0) from Ta_Avoir f3  left join ta_R_Avoir ra  on ra.id_avoir=f3.id_document " + 
				"				where f3="+prefixe+" group by f3.id_Document ";
	}



	@Override
	public String getRequeteAffectationReglement() {
		// TODO Auto-generated method stub
		return 	" select f.id_document,(coalesce(sum(0),0))as affectation from ta_avoir f left join ta_r_avoir ra on ra.id_avoir=f.id_document " + 
		" group by f.id_document ";
	}



	@Override
	public String getRequeteAffectationAvoir() {
		// TODO Auto-generated method stub
		return 	" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_avoir f left join ta_r_avoir ra on ra.id_avoir=f.id_document " + 
		" group by f.id_document ";
	}



	@Override
	public String getRequeteAffectationReglementJPQL() {
		// TODO Auto-generated method stub
		return 	" select f.idDocument,(coalesce(sum(0),0))as affectation from TaAvoir f "
		+ "left join taRAvoirs ra " + 
		" group by f.idDocument";
	}



	@Override
	public String getRequeteAffectationAvoirJPQL() {
		// TODO Auto-generated method stub
		return 	" select f.idDocument,(coalesce(sum(ra.affectation),0))as affectationAvoir from TaAvoir f "
		+ "left join taRAvoirs ra " + 
		" group by f.idDocument";
	}



	@Override
	public String getRequeteSommeAffecteJPQL(String prefixe) {
		// TODO Auto-generated method stub
		return " select coalesce(sum(ra.affectation),0) from TaAvoir f3  left join f3.taRAvoirs ra   " + 
		"				where f3="+prefixe+" group by f3.idDocument ";
}



	@Override
	public String getRequeteARelancer() {
		// TODO Auto-generated method stub
		return " ";
	}

	@Override
	public String getRequeteARelancerJPQL() {
		// TODO Auto-generated method stub
		return "  ";
	}


	public String getRequeteTypePaiement(String prefixe) {	
		return "select f from TaAvoir f join doc.taRAvoirs rr  join rr.taReglement reg join reg.taTPaiement tp  where f="+prefixe+" and tp.codeTPaiement like :valeur ";
	};

	public String getRequeteTypePaiementSQL(String prefixe) {	
		return "select f from ta_avoir f left join ta_r_avoir ra on ra.id_avoir=f.id_document join ta_T_Paiement tp on tp.id_T_paiement=f.id_T_paiement "
				+ " where f="+prefixe+" and tp.code_T_Paiement like :valeur ";
	};
	

	

	@Override
	public TaAvoir findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaAvoir a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaAvoir instance = (TaAvoir)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}


	@Override
	public List<TaAvoirDTO> rechercheDocumentDTO(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaAvoirDTO> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_DATEEXPORT_LIGHT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaAvoir.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}


		result = query.getResultList();
		return result;
	}


	@Override
	public Date selectMinDateDocumentNonExporte(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select min(f.dateDocument) from TaAvoir f where f.dateExport is null and f.dateDocument between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			Date l = (Date) query.getSingleResult();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	@Override
	public TaAvoir findByIdFetch(int id) {
		logger.debug("getting TaAvoir instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaAvoir a " +
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
			TaAvoir instance = (TaAvoir)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaAvoir findByCodeFetch(String code) {
		logger.debug("getting TaAvoir instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaAvoir a " +
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
		
			TaAvoir instance = (TaAvoir)query.getSingleResult();
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

