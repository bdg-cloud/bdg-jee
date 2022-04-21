package fr.legrain.documents.dao.jpa;

import java.math.BigDecimal;

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
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.documents.dao.IReglementDAO;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaRAcompte.
 * @see fr.legrain.documents.dao.TaRAcompte
 * @author Hibernate Tools
 */
public class TaReglementDAO /*extends AbstractApplicationDAO<TaReglement>*/ implements IReglementDAO,IDocumentDAO<TaReglement> {

//	private static final Log log = LogFactory.getLog(TaRAcompteDAO.class);
	static Logger logger = Logger.getLogger(TaReglementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaReglement a";
	
	public TaReglementDAO(){
//		this(null);
	}
	
//	public TaReglementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaReglement.class.getSimpleName());
//		initChampId(TaReglement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaReglement());
//	}
	
//	public TaReglement refresh(TaReglement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaReglement.class, detachedInstance.getIdDocument());
//		    return detachedInstance;
//
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaReglement transientInstance) {
		logger.debug("persisting TaReglement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaReglement persistentInstance) {
		logger.debug("removing TaReglement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaReglement merge(TaReglement detachedInstance) {
		logger.debug("merging TaReglement instance");
		try {
			TaReglement result = entityManager.merge(detachedInstance);
//			entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaReglement findById(int id) {
		logger.debug("getting TaReglement instance with id: " + id);
		try {
			TaReglement instance = entityManager.find(TaReglement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
//	
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
//	 */
//	public List<TaReglement> selectAll(TaFacture taFacture) {
//		logger.debug("selectAll TaReglement");
//		try {
//			if(taFacture!=null){
//			Query ejbQuery = entityManager.createQuery("select a from TaReglement a where a.taFacture=?");
//			ejbQuery.setParameter(1, taFacture);
//			List<TaReglement> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}


	public List<TaReglement> selectAllLieAuDocumentOld(IDocumentTiers taDocument,Date dateDeb,Date dateFin) {
		TaTiers taTiersLoc=null;
		if(taDocument!=null)taTiersLoc=taDocument.getTaTiers();
		logger.debug("selectAll TaReglement");
		try {
			if(taTiersLoc!=null){				
				if(dateDeb==null)dateDeb=new Date(0);
				if(dateFin==null)dateFin=new Date("01/01/3000");
				
				String requete=
				" select a from TaReglement a where " +
				" exists(select r from TaRReglement r where r.taReglement.idDocument=a.id " +
				" and r.taFacture=:facture) " +
				" and a.taTiers =:tiers"+
				" and a.dateDocument between :deb and :fin "+
//				" union "+
//				" select a from TaReglement a where " +
//				" not exists(select r from TaRReglement r where r.taReglement.idDocument=a.id " +
//				" and (r.taFacture=:facture)) " +
//				" and a.netTtcCalc > (select sum(ra.affectation) from TaRReglement ra join ra.taReglement r where r.idDocument=a.idDocument) "+
//				" and a.taTiers =:tiers"+
//				" and a.dateDocument between :deb and :fin "+
				 " order by a.codeDocument ";
				
				Query ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("facture", taDocument);
				ejbQuery.setParameter("tiers", taTiersLoc);
				ejbQuery.setParameter("deb", dateDeb,TemporalType.DATE);
				ejbQuery.setParameter("fin", dateFin,TemporalType.DATE);
			
			List<TaReglement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public int selectCountDisponible(TaTiers taTiers) {
		logger.debug("TaReglement selectCountDisponible");
		Long count=(long) 0;
		try {
			String requete ="";
			Query ejbQuery =null;
			if(taTiers!=null ){
				requete ="select count(a) from TaReglement  a " +
				" where a.taTiers=:taTiers and (" +
				" not exists(select ra from TaRReglement ra " +
				"where  ra.taReglement=a and ra.taFacture is not null) or" +
				" (a.netTtcCalc > (select sum(ra.affectation) from TaRReglement ra " +
				"where  ra.taReglement=a and ra.taFacture is not null))) " ;
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
	
	public List<TaReglementDTO> selectAllLieAuDocument(TaFactureDTO taDocument, Date dateDeb,
			Date dateFin) {
		String taTiersLoc=null;
		if(taDocument!=null)taTiersLoc=taDocument.getCodeTiers();		
		logger.debug("selectAll TaReglement");
		try {
			if(taTiersLoc!=null){				
				if(dateDeb==null)dateDeb=new Date(0);
				if(dateFin==null)dateFin=new Date("01/01/3000");
				String requete=
				" select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument, f.dateLivDocument, f.libelleDocument, t.codeTiers, "
				+ "t.nomTiers,f.netTtcCalc,rr.affectation,(select coalesce(sum(r.affectation),0)from TaRReglement r where r.taReglement=f) "
				+ " ,coalesce(tp.idTPaiement,0),tp.codeTPaiement,tp.libTPaiement,cb.idCompteBanque,cb.codeBIC,cb.iban,"
				+ "(select coalesce(count(r),0)from TaRReglement r where r.taReglement=f),ac.codeDocument,p.codeDocument,f.dateExport,f.dateVerrouillage, mad.accessibleSurCompteClient, mad.envoyeParEmail, mad.imprimePourClient, "
				+ " rr.dateExport,rr.dateVerrouillage, madrr.accessibleSurCompteClient, madrr.envoyeParEmail, madrr.imprimePourClient) "
				+ " from TaRReglement rr  left join rr.taMiseADisposition madrr   join rr.taReglement f join f.taTiers t  left join f.taMiseADisposition mad  left join f.taTPaiement tp left join f.taCompteBanque cb  left join f.taAcompte ac left join f.taPrelevement p where " +
				"  rr.taFacture.codeDocument=:facture " +
				" and t.codeTiers =:tiers"+
				" and f.dateDocument between :deb and :fin "+

				 " order by f.codeDocument ";
				
				Query ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("facture", taDocument.getCodeDocument());
				ejbQuery.setParameter("tiers", taTiersLoc);
				ejbQuery.setParameter("deb", dateDeb,TemporalType.DATE);
				ejbQuery.setParameter("fin", dateFin,TemporalType.DATE);
			
			List<TaReglementDTO> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public List<TaReglementDTO> selectReglementNonLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin) {
		logger.debug("selectAll TaReglement");
		String taTiersLoc=null;
		if(taDocument!=null)taTiersLoc=taDocument.getCodeTiers();	
		try {
			if(taTiersLoc!=null){				
				if(dateDeb==null)dateDeb=new Date(0);
				if(dateFin==null)dateFin=new Date("01/01/3000");
				
				String requete=
				" select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument, f.dateLivDocument, f.libelleDocument, t.codeTiers, "
				+ "t.nomTiers,f.netTtcCalc,cast(0.0 as big_decimal),(select coalesce(sum(r.affectation),0)from TaRReglement r where r.taReglement=f )"
				+ " ,coalesce(tp.idTPaiement,0),tp.codeTPaiement,tp.libTPaiement,cb.idCompteBanque,cb.codeBIC,cb.iban,"
				+ "(select coalesce(count(r),0)from TaRReglement r where r.taReglement=f),ac.codeDocument,p.codeDocument,f.dateExport,f.dateVerrouillage, mad.accessibleSurCompteClient, mad.envoyeParEmail, mad.imprimePourClient) "				
				+ " from TaReglement f  join f.taTiers t  left join f.taMiseADisposition mad left join f.taTPaiement tp left join f.taCompteBanque cb left join f.taAcompte ac left join f.taPrelevement p where " +
				" not exists(select rr from TaRReglement rr join rr.taReglement r  where r.idDocument=f.idDocument " +
				" and rr.taFacture.codeDocument=:facture) "+
				" and f.netTtcCalc > (select coalesce(sum(ra.affectation),0) from TaRReglement ra join ra.taReglement r where r.idDocument=f.idDocument) "+
				" and t.codeTiers =:tiers"+
				" and f.dateDocument between :deb and :fin "+
				 " order by f.codeDocument ";
				
				Query ejbQuery = entityManager.createQuery(requete);
				ejbQuery.setParameter("facture", taDocument.getCodeDocument());
				ejbQuery.setParameter("tiers", taTiersLoc);
				ejbQuery.setParameter("deb", dateDeb,TemporalType.DATE);
				ejbQuery.setParameter("fin", dateFin,TemporalType.DATE);
			
			List<TaReglementDTO> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	public List<TaReglement> selectSumReglementDocument(TaFacture taFacture) {
//		logger.debug("selectAll TaRAcompte");
//		try {
//			if(taFacture!=null){
//			Query ejbQuery = entityManager.createQuery("select a from TaReglement a where a.taFacture=?");
//			ejbQuery.setParameter(1, taFacture);
//			List<TaReglement> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}

	public TaReglement findByCode(String code) {
		logger.debug("getting TaReglement instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaReglement a where codeDocument like :codeDocument");
			query.setParameter("codeDocument", code);
			TaReglement instance = (TaReglement)query.getSingleResult();
			return instance;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
	
	public TaReglement findByCodeAcompte(String code) {
		logger.debug("getting findByCodeAcompte instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaReglement a join a.taAcompte ac where ac.codeDocument like :codeDocument");
			query.setParameter("codeDocument", code);
			TaReglement instance = (TaReglement)query.getSingleResult();
			return instance;
		} catch (RuntimeException re) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public TaReglement findByCodePrelevement(String code) {
		logger.debug("getting findByCodeAcompte instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaReglement a  join a.taPrelevement p where p.codeDocument like :codeDocument");
			query.setParameter("codeDocument", code);
			TaReglement instance = (TaReglement)query.getSingleResult();
			return instance;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
	
//	select r.codeDocument,t.codeTiers,r.dateDocument,r.libelleDocument,r.netTtcCalc,rr.affectation,
//	tp.codeTPaiement,f.codeDocument
//	from taReglement r
//	join r.taRReglement rr
//	join rr.taFacture f 
//	join r.taTiers t 
//	left join r.taTPaiement tp 
	
	
	/**
	 * Recherche les avoirs entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaReglement> rechercheDocument(Date dateDeb, Date dateFin) {
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		List<TaReglement> l = query.getResultList();		
		return l;
	}
	
	/**
	 * Recherche les factures entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaReglement> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		List<TaReglement> l = query.getResultList();		
		return l;
	}
	/**
	 * Recherche les avoirs entre 2 codes avoirs
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des avoirs entre ces 2 codes (null en cas d'erreur)
	 */
	//public String[] rechercheAvoir(String codeDeb, String codeFin) {
	public List<TaReglement> rechercheDocument(String codeDeb, String codeFin) {
		//String[] result = null;
		List<TaReglement> result = null;
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		List<TaReglement> l = query.getResultList();
		return l;

	}
	
//	@Override
	public List<TaReglement> selectAll() {
		logger.debug("selectAll TaReglement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaReglement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les reglements non inclus dans des remises entre 2 dates
	 * @param codeTiers
	 * @param dateDeb
	 * @param DateFin
	 * @param export
	 * @param codeTPaiement
	 * @param compteBancaire
	 * @return String[] - tableau contenant les IDs des reglements entre ces 2 dates (null en cas d'erreur)
	 *  */
	public List<TaReglement> rechercheReglementNonRemises(String codeTiers,Date dateDeb,Date DateFin,Boolean export,String codeTPaiement,String ibanBancaire,boolean byDate,int nbLigneMax) {
		String requete="select r from TaReglement r left join r.taTPaiement tp left join r.taCompteBanque cb where not exists" +
				"(select a from TaLRemise a where a.taReglement.codeDocument=r.codeDocument)" +
				" and r.taTiers.codeTiers like :tiers " +
				" and r.dateDocument between :dateDeb and :dateFin" +
				" and (tp.codeTPaiement like :tPaiement)";
		
		if(ibanBancaire!=null)requete+=" and (cb.iban like :iban)" ;
				
				
		if(!export)requete+=" and r.dateExport is null " ;
		if(byDate)requete+=" order by r.dateDocument";else requete+=" order by r.codeDocument";
		
		try {
			if(codeTiers==null ||codeTiers.equals(""))codeTiers="%";
			if(codeTPaiement==null || codeTPaiement.equals(""))codeTPaiement="%";
//			if(ibanBancaire==null || ibanBancaire.equals(""))ibanBancaire="%";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("tiers",codeTiers );
			ejbQuery.setParameter("dateDeb",dateDeb );
			ejbQuery.setParameter("dateFin", DateFin);
			ejbQuery.setParameter("tPaiement", codeTPaiement);
			if(ibanBancaire!=null)ejbQuery.setParameter("iban", ibanBancaire);
			
			if(nbLigneMax>0)ejbQuery.setMaxResults(nbLigneMax);
			
			
			
			List<TaReglement> l = ejbQuery.getResultList();
			logger.debug("rechercheReglementNonRemises successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheReglementNonRemises failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaReglementDTO> rechercheReglementNonRemisesDTO(String codeTiers,Date dateDeb,Date DateFin,Boolean export,String codeTPaiement,String ibanBancaire,boolean byDate,int nbLigneMax) {
		
		String requete="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers," + 
				" f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) " +
				" from TaReglement f  join f.taTiers tiers left join f.taTPaiement tp left join f.taCompteBanque cb where not exists" +
				" (select a from TaLRemise a where a.taReglement.codeDocument=r.codeDocument)" +
				" and f.taTiers.codeTiers like :tiers " +
				" and f.dateDocument between :dateDeb and :dateFin" +
				" and (tp.codeTPaiement like :tPaiement)";
		
		if(ibanBancaire!=null)requete+=" and (cb.iban like :iban)" ;
				
				
		if(!export)requete+=" and f.dateExport is null " ;
		if(byDate)requete+=" order by f.dateDocument";else requete+=" order by f.codeDocument";
		
		try {
			if(codeTiers==null ||codeTiers.equals(""))codeTiers="%";
			if(codeTPaiement==null || codeTPaiement.equals(""))codeTPaiement="%";
//			if(ibanBancaire==null || ibanBancaire.equals(""))ibanBancaire="%";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("tiers",codeTiers );
			ejbQuery.setParameter("dateDeb",dateDeb );
			ejbQuery.setParameter("dateFin", DateFin);
			ejbQuery.setParameter("tPaiement", codeTPaiement);
			if(ibanBancaire!=null)ejbQuery.setParameter("iban", ibanBancaire);
			
			if(nbLigneMax>0)ejbQuery.setMaxResults(nbLigneMax);
			
			
			
			List<TaReglementDTO> l = ejbQuery.getResultList();
			logger.debug("rechercheReglementNonRemises successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheReglementNonRemises failed", re);
			throw re;
		}
	}
	/**
	 * Recherche les reglements entre 2 dates et non exportees
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des reglements entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaReglement> rechercheDocumentNonExporte(Date dateDeb, Date dateFin) {
		List<TaReglement> result = null;
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_NON_EXPORT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	
	@Override
	public List<TaReglement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers, null);
	}

	@Override
	public List<TaReglement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers, null);
	}

	@Override
	public List<TaReglement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaReglement> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_NON_EXPORT);
		else query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaReglement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaReglement> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_CODE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_CODE_NON_EXPORT);
		else query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		List<Object[]> result = null;
		Query query = null;
		if(light)
			query =entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		List<Object[]> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,String codeTiers){
		List<TaReglementDTO> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_AND_DATE_DTO);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;	
	}
	
	@Override
	public List<TaReglementDTO> rechercheDocumentDTO(String  codeDeb, String codeFin){
		List<TaReglementDTO> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_CODE_DTO);
		query.setParameter("codeTiers","%");
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;	
	}
	
	public List<TaReglementDTO> rechercheDocumentDTO(String  codeDeb, String codeFin,String codeTiers){
		List<TaReglementDTO> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers","%");
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;	
	}
	public List<TaReglementDTO> rechercheDocumentDTO(String codeTiers){
		List<TaReglementDTO> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_DTO);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);

		result = query.getResultList();
		return result;	
	}
	
	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers) {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}

	@Override
	public List<TaReglement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaReglement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaReglement> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaReglement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaReglement value) throws Exception {
		BeanValidator<TaReglement> validator = new BeanValidator<TaReglement>(TaReglement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaReglement value, String propertyName)
			throws Exception {
		BeanValidator<TaReglement> validator = new BeanValidator<TaReglement>(TaReglement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaReglement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public List<TaReglement> rechercheDocumentNonExporte(Date dateDeb, Date dateFin, boolean parDate) {
		List<TaReglement> result = null;
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_NON_EXPORT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	@Override
	public List<TaReglementDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin, boolean parDate) {
		List<TaReglementDTO> result = null;
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}

	@Override
	public List<TaReglement> selectAll(IDocumentTiers taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<TaReglement> rechercheDocumentVerrouille(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		List<TaReglement> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_VERROUILLE);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_NON_VERROUILLE);
		else
			query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE);	
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();		
		return result;
	}

	@Override
	public List<TaReglement> rechercheDocumentVerrouille(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaReglement> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_CODE_VERROUILLE);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_CODE_NON_VERROUILLE);
		else
		query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<Date> findDateExport(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select distinct(f.dateExport) from TaReglement f where f.dateExport is not null and f.dateExport between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			List<Date> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	@Override
	public List<TaReglement> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaReglement> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATEEXPORT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_AND_DATE);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}
		result = query.getResultList();
		return result;
	}

	/**
	 * Permet d'obtenir le ca généré par les Proforma non transformés à relancer sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total non transformés à relancer directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}

	/**
	 * Permet d'obtenir le ca généré par les Devis non transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total NON Transformé directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}

	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}

	/**
	 * Permet d'obtenir le ca généré par les Devis transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total Transformé directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}

	/**
	 * Classe permettant d'obtenir le nombre de facture dans la période
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre total de facture dans la période
	 */
	public long countDocument(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre de règlement dans periode");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";
	
			requete = "SELECT "
				+" count(d)"
				+" FROM TaReglement d " 
				+" where d.dateDocument between :dateDebut and :dateFin and d.taTiers.codeTiers like :codeTiers";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			Long nbFacture = (Long)query.getSingleResult();
			result = nbFacture;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Classe permettant d'obtenir les factures non payées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de facture non payées totalement
	 */
	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre règlement non affecté totalement");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";
	
			requete="select count(f) "
					+ " from TaReglement f "					
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null "
					+ " and not exists(select rr from TaRReglement rr "
					+ " join rr.taReglement f2 where f=f2 )";
	
	
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers", codeTiers);
			Long nbDevisNonTranforme = (Long)query.getSingleResult();
			result = nbDevisNonTranforme;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
		 * Classe permettant d'obtenir les facture non payées
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le nombre de facture non payées à relancer
		 */
		public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
			logger.debug("getting nombre facture non payées à relancer");
			Long result = (long) 0;
			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			if(codeTiers==null)codeTiers="%";
			try {
				String requete = "";
				requete="select count(f) "
						+ " from TaReglement f  "
						+ " where f.dateDocument between :dateDebut and :dateFin  "
						+ " and f.taTiers.codeTiers like :codeTiers"
						+ " and f.taEtat is null"
						+ " and  exists(select coalesce(sum(rr.affectation),0)from TaRReglement rr "
						+ " left join rr.taReglement f2  where f=f2 "
						+ " group by f2.netTtcCalc "					
						+ "  having coalesce(sum(rr.affectation),0)< f2.netTtcCalc )";
				
	
				Query query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", debut);
				query.setParameter("dateFin", fin);
	//			query.setParameter("dateref", dateref);
//				query.setParameter("dateJour", dateJour);
				query.setParameter("codeTiers", codeTiers);
				Long nbFactureNonTranformeARelancer = (Long)query.getSingleResult();
				result = nbFactureNonTranformeARelancer;
				return result;
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}

	/**
	 * Classe permettant d'obtenir les factures Totalement payées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de factures Totalement payées
	 */
	public long countDocumentTransforme(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre facture transfos");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";		
		try {
			String requete = "";
	
			requete="select count(f) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin "
					+ " and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null"
					+ " and  exists(select f2.netTtcCalc from TaReglement f2 "
					+ " join f2.taRReglements rr   where f=f2 "
					+ " group by f2.netTtcCalc "					
					+ "  having coalesce(sum(rr.affectation),0)>= f2.netTtcCalc )";
	
			
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers", codeTiers);
			Long nbFactureNonTranforme = (Long)query.getSingleResult();
			result = nbFactureNonTranforme;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Procedure permettant d'obtenir le ca généré par les facture d'un article sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param codeTiers code du Tiers
	 * @return La requête renvoyée renvoi le CA des factures sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeArticleDTO(Date dateDebut, Date dateFin, String codeArticle) {
		try {
			Query query = null;
			query = entityManager.createNamedQuery(TaReglement.QN.SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codearticle", codeArticle);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}

	/**
	 * Procedure permettant d'obtenir le ca généré par les facture d'un Tiers sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param codeTiers code du Tiers
	 * @return La requête renvoyée renvoi le CA des factures sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeTiersDTO(Date dateDebut, Date dateFin, String codeTiers) {
		try {
			Query query = null;
			query = entityManager.createNamedQuery(TaReglement.QN.SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers", codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}

	@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
				int precision, int deltaNbJours,String codeTiers) {
			// TODO Auto-generated method stub
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			try {
				Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
				Date datejour = LibDate.dateDuJour();
				switch (precision) {
				case 0:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER);
					
					break;
	
				case 1:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER);
				
					break;
				case 2:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER);
					
					break;
	
				default:
					break;
				}
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//			query.setParameter("dateref", dateref, TemporalType.DATE);
//				query.setParameter("datejour", datejour, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;
	
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
	//		return null;	
		}

	//début ici
	
		 /**
		 * Classe permettant d'obtenir la listes des devis non transformés
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
		 * ne doivent pas dépasser par rapport à la date du jour 
		 * @return La requête renvoyée renvoi la liste des devis non transformés à relancer
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
			logger.debug("getting reste à règler des factures non totalement règler et qui n'ont pas d'état et donc à relancer");
			List<TaDevisDTO> result = null;
			if(codeTiers==null)codeTiers="%";
			try {
			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date datejour = LibDate.dateDuJour();
			Query query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//		query.setParameter("dateref", dateref, TemporalType.DATE);
//			query.setParameter("datejour", datejour, TemporalType.DATE);
	//		query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
	//		return null;
		}

	@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
				int precision,String codeTiers) {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			try {
				switch (precision) {
				case 0:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE);
					
					break;
	
				case 1:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE);
					
					break;
				case 2:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE);
					
					break;
	
				default:
					break;
				}
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//			query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				logger.debug("get successful");
				return l;
	
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
	//		return null;	
		}

	/**
		 * Classe permettant d'obtenir le reste à règler des factures non totalement réglée sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le CA des devis non transformés sur la période 
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
			try {
				Query query = null;
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createNamedQuery(TaReglement.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//			query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;
	
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
	//		return null;	
		}

	/**
		 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le CA des devis sur la période 
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
			try {
				Query query = null;
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createNamedQuery(TaReglement.QN.SUM_CA_TOTAL_LIGTH_PERIODE);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//			query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;
	
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
				
		}

	@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			try {
				switch (precision) {
				case 0:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_CA_ANNEE_LIGTH_PERIODE);
					
					break;
	
				case 1:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_CA_MOIS_LIGTH_PERIODE);
					
					break;
				case 2:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_CA_JOUR_LIGTH_PERIODE);
					
					break;
	
				default:
					break;
				}
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//			query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;
	
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
				
		}

	/**
	 * Classe permettant d'obtenir le ca généré par les factures - les avoirs sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des factures - les avoirs sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin , String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createNamedQuery(TaReglement.QN.SUM_CA_TOTAL_SANS_AVOIR_LIGTH_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
			
	}

	/**
		 * Classe permettant d'obtenir le ca généré par les devis transformés sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le CA des devis transformés sur la période éclaté en fonction de la précision 
		 * Jour Mois Année
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			try {
				switch (precision) {
				case 0:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE);
					
					break;
	
				case 1:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE);
					
					break;
				case 2:
					query = entityManager.createNamedQuery(TaReglement.QN.SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE);
					
					break;
	
				default:
					break;
				}
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//			query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				logger.debug("get successful");
				return l;
	
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
	//		return null;	
		}

	@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
			try {
				Query query = null;
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createNamedQuery(TaReglement.QN.SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//			query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;
	
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
	//		return null;	
		}

	
	
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_ALL_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
		 * Classe permettant d'obtenir la listes des Factures non transformées
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
		 * ne doivent pas dépasser par rapport à la date du jour 
		 * @return La requête renvoyée renvoi la liste des Factures non transformées à relancer
		 */
		public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
			logger.debug("getting liste Prelevement non transfos à relancer");
			List<DocumentDTO> result = null;
			if(codeTiers==null)codeTiers="%";
			try {
			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date datejour = LibDate.dateDuJour();
			Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
	//		query.setParameter("dateref", dateref, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
//			query.setParameter("datejour", datejour, TemporalType.DATE);
			List<DocumentDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		}

	/**
	 * Classe permettant d'obtenir les Factures non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
	 */
	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		logger.debug("getting nombre Prelevement non transfos");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_NON_PAYE_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		query.setParameter("codeTiers",codeTiers);
		List<DocumentDTO> l = query.getResultList();;
		logger.debug("get successful");
		return l;
	
	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}

	/**
	 * Classe permettant d'obtenir la liste des Factures transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
	 */
	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		logger.debug("getting nombre Prelevement transforme");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_PAYE_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		query.setParameter("codeTiers",codeTiers);
		List<DocumentDTO> l = query.getResultList();;
		logger.debug("get successful");
		return l;
	
	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}
	
	
	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
		try {
			Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_ARTICLES_PAR_TIERS_PAR_MOIS);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			List<TaArticlesParTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
		try {
			Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_ARTICLES_PAR_TIERS_TRANSFORME);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			List<TaArticlesParTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
		try {
			Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			List<TaArticlesParTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours) {
		try {
			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date datejour = LibDate.dateDuJour();
			Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
//			query.setParameter("dateref", dateref, TemporalType.DATE);
//			query.setParameter("datejour", datejour, TemporalType.DATE);
			List<TaArticlesParTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut,
			Date dateFin, String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement,
			boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
			int deltaNbJours, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regroupee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date debut, Date fin,
			String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin ,String codeArticle) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle){
		// TODO Auto-generated method stub
				return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriodeSimple(Date dateDebut, Date dateFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOIntervalle(String codeDebut, String codeFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentTiers findDocByLDoc(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin) {
		List<TaReglementDTO> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATEEXPORT_LIGHT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}


		result = query.getResultList();
		return result;
	}



	@Override
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaReglementDTO> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_EXPORT_LIGHT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		else query = entityManager.createNamedQuery(TaReglement.QN.FIND_BY_TIERS_AND_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	
	@Override
	public void executeUpdate(String requete,List<Integer> ids) {
		Query query = null;
		try {
			query = entityManager.createQuery(requete);
			query.setParameter("ids", ids);
			query.executeUpdate();
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	@Override
	public Date selectMinDateDocumentNonExporte(Date dateDeb,Date dateFin) {
		try {
//			select a.taDocument from TaLRemise a join a.taReglement r where r.codeDocument like :codeDocument
			Query query = entityManager.createQuery("select min(f.dateDocument) from TaReglement f where f.dateExport is null and f.dateDocument between :dateDeb and :dateFin and"
					+ " not exists(select a.taDocument from TaLRemise a join a.taReglement r where r=f)");
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

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin, String codeTiers, String etat) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaReglement.QN.FIND_ALL_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("etat",etat);
			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin, String codeTiers,
			String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, String orderBy, int deltaNbJours) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisAvecEtat(Date debut, Date fin, String codeTiers,
			String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers,
			String codeArticle, String etat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return null;
	}


	

	
}
