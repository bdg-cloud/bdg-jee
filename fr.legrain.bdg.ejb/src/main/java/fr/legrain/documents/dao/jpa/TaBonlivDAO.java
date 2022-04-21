package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.documents.dao.IBonlivDAO;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaBonliv.
 * @see fr.legrain.documents.dao.TaBonliv
 * @author Hibernate Tools
 */
public class TaBonlivDAO extends AbstractDocumentDAO<TaBonliv,TaInfosBonliv,TaLBonliv>  implements IBonlivDAO, IDocumentDAO<TaBonliv>,IDocumentTiersStatistiquesDAO<TaBonliv> {

//	private static final Log log = LogFactory.getLog(TaBonlivDAO.class);
	static Logger logger = Logger.getLogger(TaBonlivDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	

	private String defaultJPQLQuery = "select a from TaBonliv a";
	public TaBonlivDAO(){
		initialiseRequetes();
//		this(null);
	}
	
//	public TaBonlivDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaBonliv.class.getSimpleName());
//		initChampId(TaBonliv.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaBonliv());
//	}
	
//	public TaBonliv refresh(TaBonliv detachedInstance) {
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
////			detachedInstance = entityManager.find(TaBonliv.class, detachedInstance.getIdDocument());
//			detachedInstance.setLegrain(true);
//			detachedInstance.calculLignesTva();
//			return detachedInstance;
//
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaBonliv transientInstance) {
		logger.debug("persisting TaBonliv instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaBonliv persistentInstance) {
		logger.debug("removing TaBonliv instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaBonliv merge(TaBonliv detachedInstance) {
		logger.debug("merging TaBonliv instance");
		try {
//			if(detachedInstance.getTaInfosDocument()==null) {
//				throw new RuntimeException("Il manque l'infoDocument du document n°: "+detachedInstance.getCodeDocument());
//			}
			TaBonliv result = entityManager.merge(detachedInstance);
//			if(result.getTaInfosDocument()==null) {
//				throw new RuntimeException("Il manque l'infoDocument du document n°: "+result.getCodeDocument());
//			}
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaBonliv findById(int id) {
		logger.debug("getting TaBonliv instance with id: " + id);
		try {
			TaBonliv instance = entityManager.find(TaBonliv.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaBonliv findByCode(String code) {
		logger.debug("getting TaBonliv instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaBonliv a " +
					"  left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");			
			TaBonliv instance = (TaBonliv)query.getSingleResult();
			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaBonliv> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaBonliv instance with codeTiers: " + codeTiers);
		List<TaBonliv> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_TIERS_AND_DATE);
			if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
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
	
	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin) {
		// TODO Auto-generated method stub
		logger.debug("selectDocNonTransformeRequete ");
		try {			
			String requete = "select a from Ta"+typeOrigine+" a join a.taTiers t where " ;
			if(codeTiers!=null ) //&& !codeTiers.equals("")
				requete+=" t.codeTiers like '"+codeTiers+"' and ";
			if(doc!=null)
				requete+=" a.idDocument = "+doc.getIdDocument()+" and ";
			requete+=" a.dateDocument between :dateDeb and :dateFin and not exists(select r from TaRDocument r " +
					"join r.ta"+typeOrigine+" org " +
					"left join r.ta"+typeDest+" dest " +
					" where " +
					" ( org.idDocument = a.idDocument and r.idSrc=org.idDocument )"+
					"  or ( org.idDocument = a.idDocument and (dest.idDocument is not null or dest.idDocument!=0)))";
			requete+="  order by a.dateDocument,a.codeDocument";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("dateDeb", debut);
			ejbQuery.setParameter("dateFin", fin);
			List<IDocumentTiers> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
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
		logger.debug("getting TaBonliv instance with codeTiers: " + codeTiers);
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
				+" from TaBonliv f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
				+"group by "+groupBy+",t.codeTiers"
				;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaBonliv f "
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
	public List<TaBonliv> selectAll() {
		logger.debug("selectAll TaBonliv");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaBonliv> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les bon de livraison entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des bon de livraison entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaBonliv> rechercheDocument(Date dateDeb, Date dateFin) {
		List<TaBonliv> result = null;
		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	
	
	/**
	 * Recherche les bon de livraison entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des bon de livraison entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaBonliv> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		List<TaBonliv> result = null;
		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	/**
	 * Recherche les bon de livraison entre 2 codes bon de livraison
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des bon de livraison entre ces 2 codes (null en cas d'erreur)
	 */
	public List<TaBonliv> rechercheDocument(String codeDeb, String codeFin) {		
		List<TaBonliv> result = null;
		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_CODE);
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
	 * Classe permettant d'obtenir le ca généré par les bons de livraison sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoi le montant des par les BL sur la période
	 */
	public List<Object> findCABLSurPeriode(Date debut, Date fin) {
		logger.debug("getting nombre ca des bc");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" sum(d.netHtCalc)"
				+" FROM TaBonliv d " 
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
	 * Classe permettant d'obtenir les livraisons non transformées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des livraisons non transformées
	 */
	public List<TaBonliv> findLivraisonsNonTransfos(Date debut, Date fin) {
		logger.debug("getting nombre livraisons non transfos");
		List<TaBonliv> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
			+" d"
			+" FROM TaBonliv d " 
			+" where d.dateDocument between :dateDeb and :dateFin"
			+" and not exists (select r " +
					"from TaRDocument r " +
					" where r.taBonliv = d" +
					" and ( taFacture IS NOT NULL))"
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
			+" FROM TaBonliv d " 
			+" where d.dateDocument between :dateDeb and :dateFin "
			+" and exists (select r " 
			+" from TaRDocument r " 
			+" where r.taBonliv = d " 
			+" and ( r.taFacture IS NOT NULL)) "
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
	 * Classe permettant d'obtenir les livraisons  transformées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des livraisons transformées
	 */
	public List<TaBonliv> findLivraisonsTransfos(Date debut, Date fin) {
		logger.debug("getting nombre livraisons transfos");
		List<TaBonliv> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
			+" d"
			+" FROM TaBonliv d " 
			+" where d.dateDocument between :dateDeb and :dateFin"
			+" and exists (select r " +
					"from TaRDocument r " +
					" where r.taBonliv = d" +
					" and ( taFacture IS NOT NULL))"
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
	public List<TaBonliv> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers, null);
	}

	@Override
	public List<TaBonliv> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers, null);
	}

	@Override
	public List<TaBonliv> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaBonliv> result = null;
		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaBonliv> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaBonliv> result = null;
		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
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
			query =entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_DATE);
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
		query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_DATE_LIGHT);
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
			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}

	@Override
	public List<TaBonliv> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaBonliv> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaBonliv> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaBonliv> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaBonliv value) throws Exception {
		BeanValidator<TaBonliv> validator = new BeanValidator<TaBonliv>(TaBonliv.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaBonliv value, String propertyName)
			throws Exception {
		BeanValidator<TaBonliv> validator = new BeanValidator<TaBonliv>(TaBonliv.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaBonliv transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public List<TaBonlivDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_ALL_LIGHT);
			List<TaBonlivDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaBonlivDTO> findByCodeLight(String code) {
		logger.debug("getting TaBonlivDTO instance");
		try {		
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaBonliv.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeDocument", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaBonliv.QN.FIND_ALL_LIGHT);
			}

			List<TaBonlivDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaBonliv> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

//	/**-------------------------------------------------------------------------------------
//	 * Classe permettant d'obtenir le ca généré par les Bons de livraison sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des bonLiv sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_ANNEE_LIGTH_PERIODE);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_MOIS_LIGTH_PERIODE);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_JOUR_LIGTH_PERIODE);
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
//			query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_TOTAL_LIGTH_PERIODE);
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
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_ANNEE_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_MOIS_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_JOUR_LIGTH_PERIODE_TRANSFORME);
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
//	 * Classe permettant d'obtenir le ca généré par les Prelevement transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement transformés sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_TOTAL_LIGTH_PERIODE_TRANSFORME);
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
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME);
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
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin, int precision) {
//		Query query = null;
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
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
//	 * Classe permettant d'obtenir le ca généré par les Prelevement non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des Prelevement non transformés sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_TOTAL_LIGTH_PERIODE_NON_TRANSFORME);
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
	
//	 /**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
//	 */
//	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		logger.debug("getting nombre Bonliv non transfos");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_NON_TRANSFORME_LIGHT_PERIODE);
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

//	 /**
//	 * Classe permettant d'obtenir la liste des Prelevement transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
//	 */
//	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		logger.debug("getting nombre Bonliv transforme");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_TRANSFORME_LIGHT_PERIODE);
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
	
//	 /**
//	 * Classe permettant d'obtenir la listes des Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
//	 * ne doivent pas dépasser par rapport à la date du jour 
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés à relancer
//	 */
//	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting liste Bonliv non transfos à relancer");
//		List<DocumentDTO> result = null;
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
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
	
//	 /**
//	 * Classe permettant d'obtenir la listes des Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
//	 * ne doivent pas dépasser par rapport à la date du jour 
//	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés à relancer
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,int precision, int deltaNbJours,String codeTiers) {
//		Query query = null;
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		switch (precision) {
//		case 0:
//			query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//			
//			break;
//
//		case 1:
//			query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//			
//			break;
//		case 2:
//			query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//			
//			break;
//
//		default:
//			break;
//		}
//		
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateRef", dateref, TemporalType.DATE);
//		query.setParameter("dateJour", datejour, TemporalType.DATE);
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

	
//	 /**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre total de Prelevement dans la période
//	 */
//	public long countDocument(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre Bonliv dans periode");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaBonliv d " 
//				+" where d.dateDocument between :datedeb and :datefin and d.taTiers.codeTiers like :codeTiers";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBonliv = (Long)query.getSingleResult();
//			result = nbBonliv;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

//	/**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de Prelevement non transformés
//	 */
//	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre Bonliv non transfos");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaBonliv d " 
//				+" where d.dateDocument between :datedeb and :datefin"
//				+" and not exists (select r " +
//						"from TaRDocument r " +
//						" where r.taBonliv = d" +
//						" and ( taFacture IS NOT NULL)) and d.taTiers.codeTiers like :codeTiers";;
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBonlivNonTranforme = (Long)query.getSingleResult();
//			result = nbBonlivNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

//	/**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de Prelevement non transformés à relancer
//	 */
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting nombre Bonliv non transfos à relancer");
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
//				+" FROM TaBonliv d " 
//				+" where d.dateDocument between :datedeb and :datefin"
//				+" and d.dateLivDocument <= :dateref"
//				+" and d.dateLivDocument >= :datejour"
//				+" and not exists (select r " +
//						"from TaRDocument r " +
//						" where r.taBonliv = d" +
//						" and ( taFacture IS NOT NULL)) and d.taTiers.codeTiers like :codeTiers";;
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("dateref", dateref);
//			query.setParameter("datejour", dateJour);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBonlivNonTranformeARelancer = (Long)query.getSingleResult();
//			result = nbBonlivNonTranformeARelancer;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

//	/**
//	 * Classe permettant d'obtenir les Prelevement non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de Prelevement transformés
//	 */
//	public long countDocumentTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre Bonliv transfos");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaBonliv d " 
//				+" where d.dateDocument between :datedeb and :datefin"
//				+" and exists (select r " +
//						"from TaRDocument r " +
//						" where r.taBonliv = d" +
//						" and ( taFacture IS NOT NULL)) and d.taTiers.codeTiers like :codeTiers";
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbBonlivNonTranforme = (Long)query.getSingleResult();
//			result = nbBonlivNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
	//----------------------------

//	@Override
//	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
//		try {
//			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_ARTICLES_PAR_TIERS_PAR_MOIS);
//			query.setParameter("dateDebut", debut, TemporalType.DATE);
//			query.setParameter("dateFin", fin, TemporalType.DATE);
//			List<TaArticlesParTiersDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	@Override
//	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
//		try {
//			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_ARTICLES_PAR_TIERS_TRANSFORME);
//			query.setParameter("dateDebut", debut, TemporalType.DATE);
//			query.setParameter("dateFin", fin, TemporalType.DATE);
//			List<TaArticlesParTiersDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	@Override
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
//		try {
//			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME);
//			query.setParameter("dateDebut", debut, TemporalType.DATE);
//			query.setParameter("dateFin", fin, TemporalType.DATE);
//			List<TaArticlesParTiersDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	@Override
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours) {
//		try {
//			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//			Date datejour = LibDate.dateDuJour();
//			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER);
//			query.setParameter("dateDebut", debut, TemporalType.DATE);
//			query.setParameter("dateFin", fin, TemporalType.DATE);
//			query.setParameter("dateref", dateref, TemporalType.DATE);
//			query.setParameter("datejour", datejour, TemporalType.DATE);
//			List<TaArticlesParTiersDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
//	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			if(codeTiers==null)codeTiers="%";
//			Query query = entityManager.createNamedQuery(TaBonliv.QN.FIND_ALL_LIGHT_PERIODE);
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
//	 * Permet d'obtenir le ca généré par les bons de livraison non transformés à relancer sur une période donnée
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
//	/**
//	 * Permet d'obtenir le ca généré par les bons de livraison non transformés sur une période donnée
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
//	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}
//	
//	/**
//	 * Permet d'obtenir le ca généré par les bons de livraison transformés sur une période donnée
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

//	 /**
//	 * Classe permettant d'obtenir la listes des Bons de livraison non transformés à relancer
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
//	 * ne doivent pas dépasser par rapport à la date du jour 
//	 * @return La requête renvoyée renvoi la liste des Bons de livraison non transformés à relancer
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut,
//			Date dateFin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting ca des Bons de livraison non transfos à relancer");
//		List<TaProformaDTO> result = null;
//		try {
//		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date dateJour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		Query query = entityManager.createNamedQuery(TaBonliv.QN.SUM_CA_TOTAL_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateRef", dateRef, TemporalType.DATE);
//		query.setParameter("dateJour", dateJour, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentChiffreAffaireDTO> l = query.getResultList();;
////		if (query.getFirstResult() == 0){
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
//	}

	@Override
	public List<TaBonliv> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBonliv> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
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
	public List<TaBonliv> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return "select r   from TaRDocument r   where r.taBonliv = "+prefixe+"   and ( r.taFacture IS NOT NULL)";
	}

	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return "doc.dateLivDocument";
	}

	@Override
	public String getRequeteARelancer() {
		// TODO Auto-generated method stub
		return " and doc.date_Liv_Document <= :dateRef and doc.date_Liv_Document >= :dateJour ";
	}
	
	@Override
	public String getRequeteARelancerJPQL() {
		// TODO Auto-generated method stub
		return " and doc.dateLivDocument <= :dateRef and doc.dateLivDocument >= :dateJour ";
	}



	public TaBonliv findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
//			Query query = entityManager.createQuery("select a from TaBonliv a " +
//					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
//					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
//					+" where l=:ldoc " +
//							" order by l.numLigneLDocument");
			Query query = entityManager.createQuery("select a from TaBonliv a " +
					"  join fetch a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaBonliv instance = (TaBonliv)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}	


	@Override
	public TaBonliv findByIdFetch(int id) {
		logger.debug("getting TaBonliv instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaBonliv a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.idDocument="+id+" " +
					" order by l.numLigneLDocument");
			TaBonliv instance = (TaBonliv)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaBonliv findByCodeFetch(String code) {
		logger.debug("getting TaBonliv instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaBonliv a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.codeDocument='"+code+"' " +
					" order by l.numLigneLDocument");
		
			TaBonliv instance = (TaBonliv)query.getSingleResult();
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

	
	/**
	/* Si codeTransporteur = sans, alors on prend les Bl sans transporteur
	 Si codeTransporteur = null, alors on prend tous les Bl
	*/
	@Override
	public List<IDocumentTiersDTO> selectBLNonTermineSansLotTransporteur(String codeTransporteur,Date debut,Date fin) {
		// TODO Auto-generated method stub
		logger.debug("selectTourneeTransporteur ");
		try {			
			String requete = "select new fr.legrain.document.dto.TaBonlivDTO(t.codeTransporteur,tiers.codeTiers,tiers.nomTiers,infos.villeLiv,infos.paysLiv,a.codeDocument,a.dateDocument,a.dateLivDocument,a.numeroCommandeFournisseur,rEtat.taEtat.codeEtat) "
					+ " from TaBonliv a "
					+ " join a.lignes l "
					+ " left join l.taLot lot "
					+ " left join l.taArticle art "
					+ " join a.taTiers tiers  left join a.taTransporteur t "
					+ " join a.taInfosDocument infos join a.taREtat rEtat where " ;
			if(codeTransporteur!=null ) { //&& !codeTiers.equals("")
				if(codeTransporteur.equals("sans")) requete+=" t is null and ";
				else requete+=" t.codeTransporteur like '"+codeTransporteur+"' and ";
			}

			requete+=" a.dateLivDocument between :dateDeb and :dateFin ";
			requete+=" and  a.gestionLot=true and art.gestionLot = true and lot is null ";
			requete+="  order by t.codeTransporteur,a.dateLivDocument,infos.villeLiv,tiers.codeTiers";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("dateDeb", debut);
			ejbQuery.setParameter("dateFin", fin);
			List<IDocumentTiersDTO> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	/* Si codeTransporteur = sans, alors on prend les Bl sans transporteur
	 Si codeTransporteur = null, alors on prend tous les Bl
	*/
	@Override
	public List<IDocumentTiersDTO> selectTourneeTransporteur(String codeTransporteur,Date debut,Date fin) {
		// TODO Auto-generated method stub
		logger.debug("selectTourneeTransporteur ");
		try {			
			String requete = "select new fr.legrain.document.dto.TaBonlivDTO(t.codeTransporteur,tiers.codeTiers,tiers.nomTiers,infos.villeLiv,infos.paysLiv,a.codeDocument,a.dateDocument,a.dateLivDocument,a.numeroCommandeFournisseur,rEtat.taEtat.codeEtat) "
					+ " from TaBonliv a join a.taTiers tiers  left join a.taTransporteur t "
					+ " join a.taInfosDocument infos join a.taREtat rEtat where " ;
			if(codeTransporteur!=null ) { //&& !codeTiers.equals("")
				if(codeTransporteur.equals("sans")) requete+=" t is null and ";
				else requete+=" t.codeTransporteur like '"+codeTransporteur+"' and ";
			}

			requete+=" a.dateLivDocument between :dateDeb and :dateFin ";
			requete+="  order by t.codeTransporteur,a.dateLivDocument,infos.villeLiv,tiers.codeTiers";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("dateDeb", debut);
			ejbQuery.setParameter("dateFin", fin);
			List<IDocumentTiersDTO> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	

	
	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin, String codeArticle){
		return listLigneArticlePeriodeBonlivTransporteurDTO(codeTransporteur,dateDebut, dateFin, codeArticle, null);
	}
	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin, String codeArticle, Boolean synthese) {
		return listLigneArticlePeriodeBonlivTransporteurDTO(codeTransporteur,dateDebut,  dateFin,  codeArticle,  synthese,  null);
	}
	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy) {
					try {
						Query query = null;
						String codeTiers = "%";
						String sql = "";
						
						if(codeArticle==null)codeArticle="%";
						if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs bonliv
							if(codeTransporteur.equals("sans"))sql = LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_BONLIV_SANS_TRANSPORTEUR_GROUP_BY_ARTICLE;
							else sql = LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_BONLIV_TRANSPORTEUR_GROUP_BY_ARTICLE;
						}else {// si on veut le detail (lignes de docs bonliv)
							if(codeTransporteur.equals("sans"))sql = LIST_LIGNE_ARTICLE_PERIODE_BONLIV_SANS_TRANSPORTEUR;
							else sql = LIST_LIGNE_ARTICLE_PERIODE_BONLIV_TRANSPORTEUR;
							 
						}

						
						if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE)) {
							sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_libcFamille+", b."+DocumentChiffreAffaireDTO.f_codeArticle;
						}else
						if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_DOCUMENT)) {
							sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeDocument+", b."+DocumentChiffreAffaireDTO.f_codeArticle;
						}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
							sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+", b."+DocumentChiffreAffaireDTO.f_codeDocument+", b."+DocumentChiffreAffaireDTO.f_codeArticle;
						}else {
							sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeDocument+", b."+DocumentChiffreAffaireDTO.f_codeArticle;
						}
			
			query = entityManager.createNativeQuery(sql);
			
	
			
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			if(!codeTransporteur.equals("sans"))query.setParameter("codeTransporteur",codeTransporteur);
			
			
			// si on veut le detail (lignes de bonliv)
			if(synthese)addScalarDocumentArticleTransporteurDTOGroupByArticle(query);
			else addScalarDocumentArticleTransporteurDTODetail(query);
			
			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}

	
}

