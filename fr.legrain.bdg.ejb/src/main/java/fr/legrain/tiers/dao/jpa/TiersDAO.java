package fr.legrain.tiers.dao.jpa;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;
//import com.sun.jmx.snmp.Timestamp;

/**
 * Home object for domain model class TaTiers.
 * @see fr.legrain.tiers.dao.old.test.TaTiers
 * @author Hibernate Tools
 */
public class TiersDAO implements ITiersDAO, Serializable {

	private static final long serialVersionUID = 7779109921937949887L;

	static Logger logger = Logger.getLogger(TiersDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTiers a where systeme != true ORDER BY a.codeTiers";
	private String defaultJPQLQueryIdentiteEntrepise = "select a from TaTiers a where systeme = true";
	private String tiersActif = "select a from TaTiers a where actifTiers =1 and systeme != true";
	private String fournisseurProduit="select a from TaTiers a join a.taFournisseurs f where a.codeTiers like :code";
	private String tiersActiflight = "select a.codeTiers,a.nomTiers,a.codeCompta,a.compte,ttiers.libelleTTiers from TaTiers a join a.taTTiers ttiers where a.actifTiers =1 and a.systeme != true";
	private String orderByCodeTiers = " order by a.codeTiers";
	private String preferences = null;
	
	private String defaultLightJPQLQuery = "select a.idTiers, a.codeTiers, a.nomTiers, a.prenomTiers, a.dateAnniv,  "
			+ "adresse.villeAdresse, adresse.codepostalAdresse, "
			+ "email.adresseEmail, tel.numeroTelephone, entreprise.nomEntreprise, "
			+ "a.actifTiers "
			+ "from TaTiers a left outer join a.taAdresse adresse "
			+ "left outer join a.taEmail email "
			+ "left outer join a.taTelephone tel "
			+ "left outer join a.taEntreprise entreprise "
			+ "where a.systeme != true";
	
	static public final int C_ID_IDENTITE_ENTREPRISE_INT = -1;
	static public final String C_ID_IDENTITE_ENTREPRISE_STR = "-1";
	
	public TiersDAO() {
	}
	
	public String getDefaultJPQLQueryOrderByCodeTiers() {
		return this.defaultJPQLQuery+" "+orderByCodeTiers;
	}
	
	public String getDefaultJPQLQuery() {
		return this.defaultJPQLQuery;
	}
	
	public String getDefaultJPQLQueryIdentiteEntrepise() {
		return this.defaultJPQLQueryIdentiteEntrepise;
	}


	public void persist(TaTiers transientInstance) {
		logger.debug("persisting TaTiers instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTiers persistentInstance) {
		logger.debug("removing TaTiers instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);			
			throw re;
		}
	}
	//@Transactional(value=TxType.REQUIRES_NEW)
	public TaTiers merge(TaTiers detachedInstance) {
		logger.debug("merging TaTiers instance");
		try {
			TaTiers result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
				RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
				logger.error("merge failed", re);
				throw re2;
		}
	}
	
	/**
	 * Pour insertion batch
	 * http://docs.jboss.org/hibernate/core/3.3/reference/en/html/batch.html
	 * @param detachedInstance
	 */
	public void mergeFlush(TaTiers detachedInstance) {
		logger.debug("refresh instance");
		try {			
			merge(detachedInstance);
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.flush();
		    session.clear();		    
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("mergeFlush failed", re);
			throw re2;
		}
	}

//	public TaTiers refresh(TaTiers detachedInstance) {
//		logger.debug("refresh TaTiers instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTiers.class, detachedInstance.getIdTiers());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	//@Transactional(value=TxType.REQUIRES_NEW)
	public TaTiers findById(int id) {
		logger.debug("getting TaTiers instance with id: " + id);
		try {
			TaTiers instance = entityManager.find(TaTiers.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTiers findByCode(String code) {
		logger.debug("getting TaTiers instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaTiers a where a.codeTiers='"+code+"'");
				TaTiers instance = (TaTiers)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}		

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaTiers f where f.codeTiers like '"+code+"'");
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
	
	/**
	 * Recherche les tiers qui ont été importés d'un autre programme
	 * @param origineImport
	 * @param idImport - id externe (dans le programme d'origine)
	 * @return
	 */
	public List<TaTiers> rechercheParImport(String origineImport, String idImport) {
		Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_IMPORT);
		query.setParameter("origineImport", origineImport);
		query.setParameter("idImport", idImport);
		List<TaTiers> l = query.getResultList();

		return l;
	}
	
	/**
	 * Recherche les tiers d'un certain type
	 * @param codeType - le code du type
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 codes (null en cas d'erreur)
	 */
	public List<TaTiers> rechercheParType(String codeType) {
		Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_TYPE);
		query.setParameter("codeTTiers", codeType);
		List<TaTiers> l = query.getResultList();

		return l;
	}
	
	public List<TaTiers> findByEmail(String adresseEmail) {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_EMAIL);
			query.setParameter("adresseEmail", adresseEmail);
			List<TaTiers> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			logger.error("findByEmail", re);
			return null;
		}
	}
	
	public List<TaTiers> findByEmailParDefaut(String adresseEmail) {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_EMAIL_PAR_DEFAUT);
			query.setParameter("adresseEmail", adresseEmail);
			List<TaTiers> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			logger.error("findByEmailParDefaut", re);
			return null;
		}
	}
	
	public TaTiers findByEmailAndCodeTiers(String adresseEmail, String codeTiers) {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_EMAIL_AND_CODE_TIERS);
			query.setParameter("adresseEmail", adresseEmail);
			query.setParameter("codeTiers", codeTiers);
			TaTiers l = (TaTiers) query.getSingleResult();
			return l;
		} catch (RuntimeException re) {
			logger.error("findByEmail", re);
			return null;
		}
	}
	
	public TaTiers findByEmailParDefautAndCodeTiers(String adresseEmail, String codeTiers) {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_EMAIL_PAR_DEFAUT_AND_CODE_TIERS);
			query.setParameter("adresseEmail", adresseEmail);
			query.setParameter("codeTiers", codeTiers);
			TaTiers l = (TaTiers) query.getSingleResult();
			return l;
		} catch (RuntimeException re) {
			logger.error("findByEmailParDefaut", re);
			return null;
		}
	}

	@Override
	public List<TaTiers> selectAll() {
		logger.debug("selectAll TaTiers");
		try {
			//logger.debug(new Timestamp(0));;
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTiers> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			//logger.debug(new Timestamp(0));
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaTiers> selectTiersSurTypeTiers() {
		logger.debug("selectTiersSurTypeTiers ");
		try {
			String requete = tiersActif + clauseWhereTypeTiers("");
			requete+= orderByCodeTiers;
			Query ejbQuery = entityManager.createQuery(requete);
			List<TaTiers> l = ejbQuery.getResultList();
			logger.debug("selectTiersSurTypeTiers successful");
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<Object[]> selectTiersSurTypeTiersLight() {
		// TODO Auto-generated method stub
		logger.debug("selectTiersSurTypeTiersLight ");
		try {
			String requete = tiersActiflight + clauseWhereTypeTiers("");
			requete+= orderByCodeTiers;
			Query ejbQuery = entityManager.createQuery(requete);
			List<Object[]> l = ejbQuery.getResultList();
			logger.debug("selectTiersSurTypeTiersLight successful");
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<TaTiers> selectTiersDocNonTransforme(String typeOrigine,String typeDest,Date debut,Date fin) {
		logger.debug("selectTiersBonLivNonTransforme ");
		try {
			
			String requete = selectTiersDocNonTransformeRequete(typeOrigine,typeDest,debut,fin);
			//requete+= orderByCodeTiers;
			Query ejbQuery = entityManager.createQuery(requete);
//			ejbQuery.setParameter(1, debut, TemporalType.DATE);
//			ejbQuery.setParameter(2, fin, TemporalType.DATE);
			List<TaTiers> l = ejbQuery.getResultList();
			logger.debug("selectTiersSurTypeTiers successful");
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/* passage ejb
	 * 
	public List<TaTiers> selectTiersTypeDoc(String partieRequeteTiers,IDocumentTiers doc,String typeOrigine,String typeDest,Date debut,Date fin) {
		logger.debug("selectTiersTypeDoc ");
		try {
			
			String requete = selectTiersTypeDocRequete( partieRequeteTiers,doc,typeOrigine,typeDest,debut,fin);
			//requete+= orderByCodeTiers;
			Query ejbQuery = entityManager.createQuery(requete);
//			ejbQuery.setParameter(1, debut, TemporalType.DATE);
//			ejbQuery.setParameter(2, fin, TemporalType.DATE);
			List<TaTiers> l = ejbQuery.getResultList();
			logger.debug("selectTiersTypeDoc successful");
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	*/
	
	public String selectTiersDocNonTransformeRequete(String typeOrigine,String typeDest,Date debut,Date fin) {
		logger.debug("selectTiersBonLivNonTransforme ");
		try {
			
			String requete = "select t from TaTiers t where exists (" ;
			requete=requete+" select doc from Ta"+typeOrigine+" doc join doc.taTiers t2 where t2.idTiers = t.idTiers" +
					" and doc.dateDocument between cast('"+LibDate.dateToStringSql(debut)+"'as date) and cast('"+LibDate.dateToStringSql(fin)+"'as date)" ;
			requete=requete+" and not exists(select r from TaRDocument r join r.ta"+typeOrigine+" org " +
					" join r.ta"+typeDest+" dest where org.idDocument" +
					" = doc.idDocument and (dest.idDocument is not null or dest.idDocument!=0))";
			requete=requete+" ) ";

			return requete;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/* passage ejb
	 * 
	public String selectTiersTypeDocRequete(String partieRequeteTiers,IDocumentTiers doc,String typeOrigine,String typeDest,Date debut,Date fin) {
		logger.debug("selectTiersTypeDocRequete ");
		try {
			
			String requete = "select t from TaTiers t where " +
					"t in("+partieRequeteTiers+")"+
					//"t." +nomCritere+" "+comparateur+" '"+critere+"'"+
					" and exists (" ;
			requete=requete+" select doc from Ta"+typeOrigine+" doc join doc.taTiers t2 where " ;
			if(doc!=null)requete=requete+" doc.idDocument = "+doc.getIdDocument()+" and " ;
			requete=requete+" t2.idTiers = t.idTiers and " ;
			requete=requete+"  doc.dateDocument between cast('"+LibDate.dateToStringSql(debut)+"'as date) and cast('"+LibDate.dateToStringSql(fin)+"'as date)" ;
			requete=requete+" and not exists(select r from TaRDocument r join r.ta"+typeOrigine+" org join r.ta"+typeDest+" dest " +
					" where org.idDocument" +
					" = doc.idDocument and (dest.idDocument is not null or dest.idDocument!=0))";
			requete=requete+"  ) order by t.codeTiers   ";

			return requete;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	 */
	
	public List<TaTiersDTO> findLightTTarif() {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_LIGHT_TTARIF);
			List<TaTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTiersDTO> findLightTTarifFamille(String codeTTarif, String codeFamille) {
		try {
			String requete="";
			requete+=" select new fr.legrain.tiers.dto.TaTiersDTO(a.idTiers,a.codeTiers,tt.codeTTiers,tta.codeTTarif,ent.nomEntreprise,a.nomTiers,a.prenomTiers,a.actifTiers, "
					+ " ft.idFamille, ft.codeFamille, ft.libcFamille, a.taAdresse.codepostalAdresse, a.taAdresse.villeAdresse, a.taAdresse.paysAdresse, email.adresseEmail, tel.numeroTelephone) "
					+ " from TaTiers a left join a.taTTiers tt  left join a.taTTarif tta left join a.taFamilleTiers ft left join a.taAdresse adr left join a.taEntreprise ent left join a.taEmail email left join a.taTelephone tel "
					+ " where  a.systeme!=true ";
			if(!codeTTarif.equals("%")&& !codeTTarif.isEmpty())requete+="  and (tta.codeTTarif like :codeTTarif) ";
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+="  and (ft.codeFamille like :codeFamille) ";
			
			requete+= " order by a.codeTiers";

			Query query = entityManager.createQuery(requete);
			if(!codeTTarif.equals("%")&& !codeTTarif.isEmpty())query.setParameter("codeTTarif", codeTTarif);
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())query.setParameter("codeFamille", codeFamille);
			
			List<TaTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTiersDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_ALL_LIGHT_PLUS);
//			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_ALL_LIGHT);
//			Query query = entityManager.createQuery("select "
//					+ "new fr.legrain.tiers.dto.TaTiersDTO("
//					+ "a.idTiers,a.codeTiers,tt.codeTTiers,a.nomTiers,a.prenomTiers,a.actifTiers, "
//							+ "ft.idFamille, ft.codeFamille, ft.libcFamille, "
//							+ "adr.codepostalAdresse, adr.villeAdresse, adr.paysAdresse) "
//							+ "from "
//							+ "TaTiers a "
//							+ "left join a.taTTiers tt "
//							+ "left join a.taFamilleTiers ft "
//							+ "left join a.taAdresse adr "
//							+ "where a.systeme!=true order by a.codeTiers");
//			Query query = entityManager.createQuery("select "
//					+ "new fr.legrain.tiers.dto.TaTiersDTO("
//					+ "a.idTiers,a.codeTiers,tt.codeTTiers,a.nomTiers,a.prenomTiers,a.actifTiers, "
//							+ "ftt.idFamille, ftt.codeFamille, ftt.libcFamille, "
//							+ "adr.codepostalAdresse, adr.villeAdresse, adr.paysAdresse"
//							+ ") "
//							+ " from "
//							+ "TaTiers a "
//							+ "left join a.taTTiers tt "
//							+ "left join a.taFamilleTiers ftt "
//							+ "left join a.taAdresse adr "
//							+ "where a.systeme!=true order by a.codeTiers");
			@SuppressWarnings("unchecked")
			List<TaTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTiersDTO> findAllLightAdresseComplete() {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_ALL_LIGHT_PLUS_ADRESSE_COMPLETE);

			@SuppressWarnings("unchecked")
			List<TaTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTiersDTO> findByCodeLight(String code) {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTiers", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTiers.QN.FIND_ALL_LIGHT_ACTIF);
			}

			List<TaTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaTiers entity,String field) throws ExceptLgr {	
		
	}
	
	public String clauseWhereTypeTiers(String where){
		//String preferences =BonLivraisonPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.TYPE_TIERS_DOCUMENT);
		if (preferences!=null && !preferences.equals("")){
		String debut ="";
		String fin ="";
		String suite ="";
		if(where.equals("")) where=" where (";
		else debut=" and (";
			String[]typeTiers = preferences.split(";"); 
			for (int i = 0; i < typeTiers.length; i++) {
				if(suite.equals(""))
					suite+=" a.TaTiers.codeTTiers = '"+typeTiers[i].split(",")[0]+"'";
				else
					suite+=" or a.TaTiers.codeTTiers = '"+typeTiers[i].split(",")[0]+"'";
			}
			fin+=")";
			where+=debut+suite+fin;
		}
		return where;
	}


	public String getPreferences() {
		return preferences;
	}


	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getTiersActif() {
		return tiersActif;
	}

	public void setTiersActif(String tiersActif) {
		this.tiersActif = tiersActif;
	}
	
	@Override
	public List<TaTiers> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTiers> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTiers value) throws Exception {
		BeanValidator<TaTiers> validator = new BeanValidator<TaTiers>(TaTiers.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTiers value, String propertyName) throws Exception {
		BeanValidator<TaTiers> validator = new BeanValidator<TaTiers>(TaTiers.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTiers transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public String getDefaultLightJPQLQueryOrderByCodeTiers() {
		return this.defaultLightJPQLQuery + orderByCodeTiers;
	}

	public List<TaTiers> selectTiersTypeDoc(String partieRequeteTiers,IDocumentTiers doc,String typeOrigine,String typeDest,Date debut,Date fin) {
		// TODO Auto-generated method stub
		logger.debug("selectTiersTypeDoc ");
		try {
			
			String requete = selectTiersTypeDocRequete( partieRequeteTiers,doc,typeOrigine,typeDest,debut,fin);
			//requete+= orderByCodeTiers;
			Query ejbQuery = entityManager.createQuery(requete);
//			ejbQuery.setParameter(1, debut, TemporalType.DATE);
//			ejbQuery.setParameter(2, fin, TemporalType.DATE);
			List<TaTiers> l = ejbQuery.getResultList();
			logger.debug("selectTiersTypeDoc successful");
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public String selectTiersTypeDocRequete(String partieRequeteTiers,IDocumentTiers doc,String typeOrigine,String typeDest,Date debut,Date fin) {
		// TODO Auto-generated method stub
		logger.debug("selectTiersTypeDocRequete ");
		try {
			
			String requete = "select t from TaTiers t where " +
					"t in("+partieRequeteTiers+")"+
					//"t." +nomCritere+" "+comparateur+" '"+critere+"'"+
					" and exists (" ;
			requete=requete+" select doc from Ta"+typeOrigine+" doc join doc.taTiers t2 where " ;
			if(doc!=null)requete=requete+" doc.idDocument = "+doc.getIdDocument()+" and " ;
			requete=requete+" t2.idTiers = t.idTiers and " ;
			requete=requete+"  doc.dateDocument between cast('"+LibDate.dateToStringSql(debut)+"'as date) and cast('"+LibDate.dateToStringSql(fin)+"'as date)" ;
			requete=requete+" and not exists(select r from TaRDocument r join r.ta"+typeOrigine+" org left join r.ta"+typeDest+" dest " +
					" where "
					+ "org.idDocument = doc.idDocument and (r.idSrc=org.idDocument or (dest.idDocument is not null or dest.idDocument!=0)))";
			requete=requete+"  ) order by t.codeTiers   ";

			return requete;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	// retourne la liste et le nombre des tiers par type
	public List<TaTiers> countTiersParType() {
		Query query = entityManager.createNamedQuery(TaTiers.QN.COUNT_BY_TYPE_TIERS);
		List<TaTiers> l = query.getResultList();

		return l;
	}
	
	public int countAllTiersActif() {
		int nbtiers = 0;
		Query query = entityManager.createNamedQuery(TaTiers.QN.COUNT_ALL_ACTIF_TIERS);
		
		List<TaTiersDTO> l = query.getResultList();
		if ( l!= null && ! l.isEmpty()){
			nbtiers = l.get(0).getNbTiers();
		}
		return nbtiers;
	}
	
	public List<TaTiers> rechercheTiersPourCreationEspaceClientEnSerie() {
		String jpql = "select distinct t "
				+ " from "
				+ " TaLAbonnement labo "
				+ " join labo.taDocument abo "
				+ " join abo.taTiers t "
				+ " join labo.taArticle art "
				+ " where "
				+ " t.actifTiers=1 "
				+ " and t.systeme != true "
				+ " and art.taFamille.codeFamille='MAI' "
				+ " and t.taEmail.adresseEmail is not null";
		Query query = entityManager.createQuery(jpql);
//		query.setParameter("codeTTiers", codeType);
		List<TaTiers> l = query.getResultList();

		return l;
	}
	
	
	@Override
	public List<TaTiersDTO> findListeTiersBoutique(String codeTTiers) {
		try {
			Query query = entityManager.createNamedQuery(TaTiers.QN.FIND_BY_TYPE_LIGHT);
			query.setParameter("codeTTiers", codeTTiers);
			List<TaTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
}
