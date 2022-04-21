package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTTva;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.dossier.dao.IPreferencesDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaArticle.
 * @see fr.legrain.articles.dao.TaArticle
 * @author Hibernate Tools
 */
public class ArticleDAO implements IArticleDAO, Serializable {
	
	@Inject IPreferencesDAO daoPreferences;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4208864100983087548L;

	//private static final Log logger = LogFactory.getLog(TaArticleDAO.class);
	static Logger logger = Logger.getLogger(ArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaArticle a order by codeArticle";
	
	private String defaultLightJPQLQueryEcran = TaArticle.QN.FIND_BY_ECRAN_LIGHT;
	
	public ArticleDAO(){
//		this(null);
	}
	
//	public TaArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaArticle.class);
////		champIdTable=ctrlGeneraux.getID_TABLE(TaArticle.class.getSimpleName());
//		setJPQLQuery(defaultJPQLQuery);
//		setJPQLQueryInitial(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaArticle());
//	}
	
	
	public void persist(TaArticle transientInstance) {
		logger.debug("persisting TaArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaArticle persistentInstance) {
		logger.debug("removing TaArticle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaArticle merge(TaArticle detachedInstance) {
		logger.debug("merging TaArticle instance");
		try {
			TaArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaArticle findById(int id) {
		logger.debug("getting TaArticle instance with id: " + id);
		try {
			TaArticle instance = entityManager.find(TaArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaArticle findByCodeAvecLazy(String code,boolean titreTransport) {
		try {
			Query query;
			if(titreTransport)
			query =  entityManager.createQuery("select a from TaArticle a left join fetch a.taRTaTitreTransport rt left join fetch rt.taTitreTransport t where a.codeArticle='"+code+"'");
			else query =  entityManager.createQuery("select a from TaArticle a  where a.codeArticle='"+code+"'");
			TaArticle instance= (TaArticle) query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaArticle findByIdAvecLazy(int id,boolean titreTransport) {
		try {
			Query query;
			if(titreTransport)
			query =  entityManager.createQuery("select a from TaArticle a left join fetch a.taRTaTitreTransport rt left join fetch rt.taTitreTransport t where a.idArticle="+id);
			else query =  entityManager.createQuery("select a from TaArticle a  where a.idArticle="+id);
			TaArticle instance= (TaArticle) query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	
	
	public TaArticle findByCodeAvecConformite(String code) {
		try {
			Query query;
			query =  entityManager.createQuery("select a from TaArticle a left join fetch a.taConformites c  where a.codeArticle='"+code+"'");
			TaArticle instance= (TaArticle) query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaArticle findByIdAvecConformite(int id) {
		try {
			Query query;
			query =  entityManager.createQuery("select a from TaArticle a left join fetch a.taConformites c  where a.idArticle="+id);
			TaArticle instance= (TaArticle) query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	public List<TaArticleComposeDTO> findNomenclatureDTOByIdArticle(int idArticle){
		//logger.debug("getting TaArticle instance with actif: " + actif);
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_NOMENCLATURE_DTO_BY_ID_ARTICLE);
			query.setParameter("idArticle", idArticle);
			List<TaArticleComposeDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleCompose> findNomenclatureByIdArticle(int idArticle){
		//logger.debug("getting TaArticle instance with actif: " + actif);
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_NOMENCLATURE_BY_ID_ARTICLE);
			query.setParameter("idArticle", idArticle);
			List<TaArticleCompose> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaArticle findArticleEnfantByIdArticleParent(int idArticle){
		//logger.debug("getting TaArticle instance with actif: " + actif);
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ARTICLE_ENFANT_BY_ID_ARTICLE_PARENT);
			query.setParameter("idArticle", idArticle);
			TaArticle l = (TaArticle) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public TaArticle findByIdEM(int id) {
//		logger.debug("getting TaArticle instance with id: " + id);
//		try {
//			EntityManager em = new JPABdLgr().entityManager;
//			TaArticle instance = em.find(TaArticle.class, id);
////			em.close();
//			instance=entityManager.merge(instance);
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
	public TaArticle findByCode(String code) {
		logger.debug("getting TaArticle instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaArticle a where a.codeArticle='"+code+"'");
			TaArticle instance = (TaArticle)query.getSingleResult();
			instance.getTaRapportUnite();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public boolean articleEstUtilise(TaArticle taArticle) {
		try {
			Query query = entityManager.createQuery("select a from TaMouvementStock m join m.taLot l join l.taArticle a where a.idArticle="+taArticle.getIdArticle());
			List<TaArticle> temp = query.getResultList();
			logger.debug("get successful");
			return (temp==null || !temp.isEmpty());
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaArticle findByCodeBarre(String codeBarre) {
		logger.debug("getting TaArticle instance with codeBarre: " + codeBarre);
		try {
			if(!codeBarre.equals("")){
				Query query = entityManager.createQuery("select a from TaArticle a where a.codeBarre='"+codeBarre+"'");
				TaArticle instance = (TaArticle)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
	public List<TaArticle>  findByCodeTva(String codeTva) {
		logger.debug("findByCodeTva: " + codeTva);
		try {
			if(!codeTva.equals("")){
			Query query = entityManager.createQuery("select a from TaArticle a join a.taTva t where t.codeTva='"+codeTva+"'");
			List<TaArticle> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO>  findByMPremierePFiniDTO(Boolean mPremiere,Boolean pFini,Boolean lesDeux, Boolean unOuAutre) {
		logger.debug("findByMPremierePFini,Boolean lesDeux: ");
		try {
			if(mPremiere==null)mPremiere=false;
			if(pFini==null)pFini=false;
			if(lesDeux || unOuAutre){
				pFini=true;
				mPremiere=true;
			}
			Query query = null;
			 			
			String requete = "select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,fam.codeFamille,a.actif,a.matierePremiere,a.produitFini,uRef.codeUnite) from TaArticle a left join a.taFamille fam left join a.taUniteReference uRef ";
			if(unOuAutre){
				 query = entityManager.createQuery(requete+ " where a.matierePremiere="+mPremiere+" or  a.produitFini="+pFini+" order by fam.codeFamille, a.codeArticle");
			}else if(lesDeux){
				 query = entityManager.createQuery(requete+" where a.matierePremiere="+mPremiere+" and  a.produitFini="+pFini+" order by fam.codeFamille, a.codeArticle");	
			}else if(mPremiere){
				query = entityManager.createQuery(requete+" where a.matierePremiere=true "+" order by fam.codeFamille, a.codeArticle");
			}else if(pFini){
				query = entityManager.createQuery(requete+" where a.produitFini =true "+" order by fam.codeFamille, a.codeArticle");
			}

				List<TaArticleDTO> temp =query.getResultList();
				logger.debug("get successful");
				return temp;
						

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<TaArticle>  findByMPremierePFini(Boolean mPremiere,Boolean pFini,Boolean lesDeux, Boolean unOuAutre) {
		logger.debug("findByMPremierePFini,Boolean lesDeux: ");
		try {
			if(mPremiere==null)mPremiere=false;
			if(pFini==null)pFini=false;
			if(lesDeux || unOuAutre){
				pFini=true;
				mPremiere=true;
			}
			Query query = null;
			String requete = "select a from TaArticle a ";
			if(unOuAutre){
				 query = entityManager.createQuery(requete+ " where a.matierePremiere="+mPremiere+" or  a.produitFini="+pFini);
			}else if(lesDeux){
				 query = entityManager.createQuery(requete+" where a.matierePremiere="+mPremiere+" and  a.produitFini="+pFini);	
			}else if(mPremiere){
				query = entityManager.createQuery(requete+" where a.matierePremiere=true and (a.produitFini is null or a.produitFini=false)");
			}else if(pFini){
				query = entityManager.createQuery(requete+" where (a.matierePremiere is null or a.matierePremiere=false) and (a.produitFini =true)");
			}

				List<TaArticle> temp =query.getResultList();
				logger.debug("get successful");
				return temp;
						

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticle>  findByMPremiere(Boolean mPremiere) {
		logger.debug("findByMPremierePFini: ");
		try {
			if(mPremiere==null) {
				mPremiere=false;
			}

			Query query = null;

			 query = entityManager.createQuery("select a from TaArticle a where a.matierePremiere="+mPremiere);
				List<TaArticle> temp =query.getResultList();
				logger.debug("get successful");
				return temp;
						

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticle>  findByPFini(Boolean pFini) {
		logger.debug("findByMPremierePFini: ");
		try {
			if(pFini==null) {
				pFini=false;
			}

			Query query = null;

			 query = entityManager.createQuery("select a from TaArticle a where a.produitFini="+pFini);
				List<TaArticle> temp =query.getResultList();
				logger.debug("get successful");
				return temp;
						

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaArticle f where f.codeArticle='"+code+"'");
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
	
	public List<TaArticle> findByActif(boolean actif) {
		logger.debug("getting TaArticle instance with actif: " + actif);
		try {
			int param = 0;
			if(actif){
				param = 1;
			}
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_BY_ACTIF);
			query.setParameter("actif", param);
			List<TaArticle> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public void modificationUniteDeReference(int idArticle, int idNouvelleUniteRef) {
		logger.debug("modificationUniteDeReference");
		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("modification_unite_de_reference");
			query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			query.setParameter(1, idArticle);
			query.setParameter(2, idNouvelleUniteRef);
			query.execute();
			logger.debug("get successful");
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Liste des articles créé ou modifié par partir de la date passée en paramètre.
	 * @param d
	 * @return
	 */
	public List<TaArticle> findByNewOrUpdatedAfter(Date d) {
		logger.debug("getting TaArticle instance with quandCreeArticle or quandModifArticle >= " + d);
		try {
			List<TaArticle> l = null;
			if(d!=null) {
				Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER);
				query.setParameter("dateLimite", d);
//				query.setParameter(2, d);
				l = query.getResultList();;
				logger.debug("get successful");
			}
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les articles qui ont été importés d'un autre programme
	 * @param origineImport
	 * @param idImport - id externe (dans le programme d'origine)
	 * @return
	 */
	public List<TaArticle> rechercheParImport(String origineImport, String idImport) {
		Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_BY_IMPORT);
		query.setParameter("origineImport", origineImport);
		query.setParameter("idImport", idImport);
		List<TaArticle> l = query.getResultList();

		return l;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaArticle> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaArticle refresh(TaArticle detachedInstance) {
		logger.debug("refresh instance");
		try {			
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaArticle.class, detachedInstance.getIdArticle());
		    return detachedInstance;		    
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	/**
	 * Pour insertion batch
	 * http://docs.jboss.org/hibernate/core/3.3/reference/en/html/batch.html
	 * @param detachedInstance
	 */
	public void mergeFlush(TaArticle detachedInstance) {
		logger.debug("refresh instance");
		try {			
			merge(detachedInstance);
			entityManager.flush();
			entityManager.clear();
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.flush();
//		    session.clear();		    
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	

	public void initCodeTTva(TaArticle entity){
		if (entity.getTaTva()!=null && !LibChaine.empty(entity.getTaTva().getCodeTva())){
			if( entity.getTaTTva()==null)entity.setTaTTva(new TaTTva());  
				if(LibChaine.empty(entity.getTaTTva().getCodeTTva()))
					entity.getTaTTva().setCodeTTva("D");
		}else
			if(entity.getTaTva()==null || LibChaine.empty(entity.getTaTva().getCodeTva()))
				entity.setTaTTva(null);	
	}
	
	public void initPrixTTC(TaArticle entity,boolean changement){
//		boolean res = true;
//		CallableStatement cs;
//		try {
//			cs = cnx.prepareCall("{call " + "RECALCULPRIXTTC" +"(?,?)}");			
//			cs.setInt(1,entity.getIdArticle());
//			cs.setString(2,entity.getTaTva().getCodeTva());
//			cs.execute();
//			cnx.commit();
//		} catch (SQLException e) {
//			logger.error("", e);
//		}
		if(changement){
			
		}
	}
	

	public TaArticle remonteArticlePointBonus(){
		//passage ejb à remplacer quand TaConstDAO sous forme de service
//		try {
//			TaConstDAO daoConst = new TaConstDAO();
//			TaConst ligneConst=daoConst.findByNomConst("Article_point_bonus");
//			TaArticle ArticlePoint=findById(ligneConst.getvInteger());
//			
//			
//			return ArticlePoint;
//		} catch (Exception e) {
//			return null;
//		}
		return null;
	}
	
//	public Date remonteDateFin(Integer idDocument){
//		try {
//			//Date dateFin=null;
//			String jpql = "select cast(min(lec.finAbonnement)as date) from  TaLEcheance lec "+ 
//					 " where exists (select tlf  from TaLAvisEcheance tlf join tlf.taLEcheance le join le.taAbonnement ta join ta.taTAbonnement  where le.idLEcheance=lec.idLEcheance"+ 
//					 " and tlf.taDocument.idDocument = "+idDocument+" )";
//			Query query = entityManager.createQuery(jpql);			
//			Date objet = (Date) query.getSingleResult();
//			
//			return objet;
//		} catch (Exception e) {
//			return null;
//		}
//			
//		
//	}

	public String getDefaultJPQLQuery() {
		return defaultJPQLQuery;
	}

	public void setDefaultJPQLQuery(String defaultJPQLQuery) {
		this.defaultJPQLQuery = defaultJPQLQuery;
	}

	public String getDefaultLightJPQLQueryEcran() {
		return defaultLightJPQLQueryEcran;
	}

	public void setDefaultLightJPQLQueryEcran(String defaultLightJPQLQueryEcran) {
		this.defaultLightJPQLQueryEcran = defaultLightJPQLQueryEcran;
	}
	
	public List<TaArticleDTO> findArticleCaisseLight(String codeFamille) {
		logger.debug("getting TaArticle caisse with codeFamille: " + codeFamille);
		try {
			
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ARTICLE_CAISSE_LIGHT);
			query.setParameter("codeFamille", codeFamille);
			List<TaArticleDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<Object[]> findByActifLight(boolean actif) {
		logger.debug("getting TaArticle instance with actif: " + actif);
		try {
			int param = 0;
			if(actif){
				param = 1;
			}
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_BY_ACTIF_LIGHT);
			query.setParameter("actif", param);
			List<Object[]> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<Object[]> findByEcranLight() {
		logger.debug("getting TaArticle instance");
		try {

			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_BY_ECRAN_LIGHT);

			List<Object[]> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findAllLight2() {
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ALL_LIGHT2);
			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findAllNonCompose() {
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ALL_NON_COMPOSE);
			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findLightSurCodeTTarif(String codeTTarif) {
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_LIGHT_CODE_T_TARIF);
			query.setParameter("codeTTarif", codeTTarif);
			List<TaArticleDTO> l = query.getResultList();

			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers) {
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_LIGHT_CODE_T_TARIF_TIERS);

			query.setParameter("codeTTarif", codeTTarif);
			query.setParameter("codeTiers", codeTiers);
			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ALL_LIGHT);
			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findByCodeAndLibelleLight(String code) {
		logger.debug("getting TaArticle instance");
		boolean rechercheAvecCommentaire=false;
		try {
			
			TaPreferences p = daoPreferences.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_RECHERCHE, ITaPreferencesServiceRemote.COMMENTAIRE_RECHERCHE_ARTICLE);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				rechercheAvecCommentaire=LibConversion.StringToBoolean(p.getValeur());
			}
				
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
			//	query = entityManager.createNamedQuery(TaArticle.QN.FIND_BY_CODE_AND_LIBELLE_LIGHT);
				
				String jpql = "select "
						+ "new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.commentaireArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) "
						+ "from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un "
						+ "where  a.actif=1 "
						+ "and (upper(a.codeArticle) like upper(concat('%', :code,'%')) "
						+ "or upper(a.libellecArticle) like upper(concat('%', :code,'%')) "
						+ "or upper(a.libellelArticle) like upper(concat('%', :code,'%')) ";
				
				if(rechercheAvecCommentaire)jpql=jpql+ "or upper(a.commentaireArticle) like upper(concat('%', :code,'%')) ";
				jpql=jpql+ ") order by a.codeArticle";
				query = entityManager.createQuery(jpql);
				query.setParameter("code", "%"+code.toUpperCase()+"%");
			} else {
				query = entityManager.createNamedQuery(TaArticle.QN.FIND_ALL_LIGHT_ACTIF);
			}

			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findByCodeLight(String code) {
		logger.debug("getting TaArticle instance");
		try {
			
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaArticle.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaArticle.QN.FIND_ALL_LIGHT_ACTIF);
			}

			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findByCodeLightMP(String code) {
		logger.debug("getting TaArticle instance");
		try {
			
			String queryMP="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un where a.actif=1 and a.matierePremiere = true and a.codeArticle like :code order by a.codeArticle";
			String queryMPAll="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un where a.actif=1 and a.matierePremiere = true order by a.codeArticle";
		
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createQuery(queryMP);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createQuery(queryMPAll);
			}

			List<TaArticleDTO> l = query.getResultList();
//			List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO> findByCodeLightPF(String code) {
		logger.debug("getting TaArticle instance");
		try {
					
			String queryPF="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un where a.actif=1 and a.produitFini = true and a.codeArticle like :code order by a.codeArticle";
			String queryPFAll="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un where a.actif=1 and a.produitFini = true order by a.codeArticle";


			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createQuery(queryPF);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createQuery(queryPFAll);
			}

			List<TaArticleDTO> l = query.getResultList();
//			List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * @author yann
	 * @param l'id d'une fréquence par exemple année, jours, mois...
	 * logiquement cette méthode est déprécié car on utilise un nb mois fréquence plutot qu'un id frequence
	 * @return une liste de TaArticleDTO qui ont un plan avec l'id frequence en param
	 */
	public List<TaArticleDTO> selectAllDTOAvecPlanByIdFrequence(Integer idFrequence){
		logger.debug("getting Liste TaArticleDTO instance with plan with idFrequence: "+idFrequence);
		try {
			String jpql = "select new fr.legrain.article.dto.TaArticleDTO("
					+ " a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,"
					+ " p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva, a.coefMultiplicateur)"
					+ " from TaArticle a"
					+ " left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un"
					+ " where EXISTS "
					+ " ( select plan from TaStripePlan plan where plan.taArticle = a and plan.taFrequence.idFrequence = :idFrequence)";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("idFrequence", idFrequence);
				List<TaArticleDTO> instance = (List<TaArticleDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/**
	 * @author yann
	 * @param l'id d'une fréquence par exemple année, jours, mois...
	 * logiquement cette méthode est déprécié car on utilise un nb mois fréquence plutot qu'un id frequence
	 * @return une liste de TaArticleDTO qui ont un plan avec l'id frequence en param
	 */
	public List<TaArticleDTO> selectAllDTOAvecPlan(){
		logger.debug("getting Liste TaArticleDTO instance with plan : ");
		try {
			String jpql = "select new fr.legrain.article.dto.TaArticleDTO("
					+ " a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,"
					+ " p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva, a.coefMultiplicateur)"
					+ " from TaArticle a"
					+ " left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un"
					+ " where EXISTS "
					+ " ( select plan from TaStripePlan plan where plan.taArticle = a )";
				Query query = entityManager.createQuery(jpql);
				List<TaArticleDTO> instance = (List<TaArticleDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
		
	public void ctrlSaisieSpecifique(TaArticle entity,String field) throws ExceptLgr {	
		try {
			if(field.equals(
					Const.C_CODE_UNITE)){	
			}
			if(field.equals(Const.C_CODE_TVA )||field.equals(Const.C_CODE_T_TVA )){
				initCodeTTva(entity);
//				if(field.equals(Const.C_CODE_TVA ))
//					initPrixTTC(entity,true);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	@Override
	public List<TaArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaArticle value) throws Exception {
		BeanValidator<TaArticle> validator = new BeanValidator<TaArticle>(TaArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaArticle value, String propertyName) throws Exception {
		BeanValidator<TaArticle> validator = new BeanValidator<TaArticle>(TaArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaArticle transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public Map<Integer, String> findIdEtCode() {
		logger.debug("getting id and code article");
		try {

			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ID_AND_CODE);

			Map<Integer, String> m = (Map<Integer, String>) query.getSingleResult();
			logger.debug("get successful");
			 Map<Integer, String> map = null;
			
			return m;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	@Override
	public Map<String, String> findCodeEtLibelle() {
		logger.debug("getting code and libelle article");
		try {

			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_CODE_AND_LIBELLE);

			Map<String, String> m = (Map<String, String>) query.getSingleResult();
			logger.debug("get successful");
			 Map<Integer, String> map = null;
			
			return m;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaRapportUnite getRapport( String CodeArticle ) {
		try {
			
			Query query = entityManager.createQuery("select r from TaRapportUnite r where r.taArticle.codeArticle = '" + CodeArticle + "'");
			TaRapportUnite temp = (TaRapportUnite) query.getSingleResult();
			logger.debug("get successful");
			return temp;
			
			
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaArticleDTO> selectAllInReception() {
		List<TaArticleDTO> retour=null;
		logger.debug("selectAllInReception " );
		try {
			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaArticleDTO( a.codeArticle,a.libellecArticle,a.matierePremiere,a.produitFini) from TaArticle a ,"
					+ "TaLBonReception bl where bl.taArticle = a order by a.codeArticle");
			List<TaArticleDTO> temp =query.getResultList();
			retour=new LinkedList<TaArticleDTO>();
			for (TaArticleDTO obj : temp) {
				if(!retour.contains(obj))
					retour.add(obj);
			}
			logger.debug("get successful");
			return retour;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaArticleDTO> selectAllPFInFabrication() {
		List<TaArticleDTO> retour=null;
		logger.debug("selectAllPFInFabrication " );
		try {
			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaArticleDTO( a.codeArticle,a.libellecArticle,a.matierePremiere,a.produitFini) from TaArticle a ,"
					+ "TaLFabricationPF bl  where bl.taArticle = a  order by a.codeArticle");
			List<TaArticleDTO> temp =query.getResultList();
			retour=new LinkedList<TaArticleDTO>();
			for (TaArticleDTO obj : temp) {
				if(!retour.contains(obj))
					retour.add(obj);
			}
			logger.debug("get successful");
			return retour;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	@Override
	public List<TaArticleDTO> selectAllMPInFabrication() {
		List<TaArticleDTO> retour=null;
		logger.debug("selectAllInReception " );
		try {
//			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaArticleDTO( a.codeArticle,a.libellecArticle,a.matierePremiere,a.produitFini) from TaArticle a ,"
//					+ "TaLFabricationPF blPF,TaLFabricationMP blMP  where (blPF.taArticle = a or blMP.taArticle = a ) order by a.codeArticle");
			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaArticleDTO( a.codeArticle,a.libellecArticle,a.matierePremiere,a.produitFini) from TaArticle a  where exists( "
					+ " select blMP from TaLFabricationMP blMP  where  blMP.taArticle = a )  order by a.codeArticle");			
			List<TaArticleDTO> temp =query.getResultList();
			retour=new LinkedList<TaArticleDTO>();
			for (TaArticleDTO obj : temp) {
				if(!retour.contains(obj))
					retour.add(obj);
			}
			logger.debug("get successful");
			return retour;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public List<TaArticleDTO>  findProduitsFournisseur(String fournisseur) {
		logger.debug("findProduitsFournisseur");
		try {
			Query query = null;
			 query = entityManager.createQuery("select new fr.legrain.article.dto.TaArticleDTO( a.codeArticle,a.libellecArticle) from TaArticle a join a.taFournisseurs f where f.codeTiers like :code order by a.codeArticle");
				query.setParameter("code", fournisseur);
				List<TaArticleDTO> temp =query.getResultList();
				logger.debug("get successful");
				return temp;						

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	public List<TaTiersDTO>  findFournisseurProduit(String produit) {
		logger.debug("findFournisseurProduit");
		try {
			Query query = null;
			 query = entityManager.createQuery("select new fr.legrain.tiers.dto.TaTiersDTO(f.codeTiers,f.nomTiers,f.prenomTiers,f.actifTiers,cv.codeTCivilite,ent.nomEntreprise) from TaArticle a "
			 		+ "join a.taFournisseurs f left join f.taTCivilite cv left join f.taEntreprise ent where a.codeArticle like :code order by f.codeTiers");
				query.setParameter("code", produit);
				List<TaTiersDTO> temp =query.getResultList();
				logger.debug("get successful");
				return temp;						

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public int countAllArticleActif() {
		int nbarticle = 0;
		Query query = entityManager.createNamedQuery(TaArticle.QN.COUNT_ALL_ACTIF_ARTICLE);
		
		List<TaArticleDTO> l = query.getResultList();
		if ( l!= null && ! l.isEmpty()){
			nbarticle = l.get(0).getNbArticle();
		}
		return nbarticle;
	}	

	
	public List<TaArticleDTO>  remonteGrilleReference(String codeArticle, String codeFamille,boolean prixObligatoire) {
		logger.debug("remonteGrilleReference: " );
		if(codeArticle==null ||codeArticle.equals(""))codeArticle="%";
		if(codeFamille==null ||codeFamille.equals(""))codeFamille="%";
		if(!codeArticle.isEmpty() && !codeArticle.equals("%"))codeFamille="";
		
		try {

			String requete="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,"
					+ " 	CASE p WHEN null THEN cast('0' as integer) else  p.idPrix end, p.prixPrix,p.prixttcPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva) "
					+ " from TaArticle a "
					+ " left join a.taFamille fam "
					+ " left join a.taPrix p "
					+ " left join a.taTva tva "
					+ " left join a.taTTva ttva "
					+ " left join a.taUnite1 un "
					+ " where a.actif=1 "
					+ " and a.codeArticle like :codeArticle ";
			if(prixObligatoire)requete+=" and (p.prixPrix<>0 or p.prixttcPrix<>0)";
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+= " and fam.codeFamille like :codeFamille "	;
			
			requete+= " order by fam.codeFamille,a.codeArticle";
			Query query = entityManager.createQuery(requete);
			query.setParameter("codeArticle", codeArticle);
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())query.setParameter("codeFamille", codeFamille);
			List<TaArticleDTO> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
			
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<TaArticleDTO>  remonteGrilleTarifaire(String codeArticle, String codeFamille, String codeTTarif) {
		logger.debug("remonteGrilleTarifaire : " );
		if(codeArticle==null ||codeArticle.equals(""))codeArticle="%";
		if(codeFamille==null ||codeFamille.equals(""))codeFamille="%";
		if(!codeArticle.isEmpty() && !codeArticle.equals("%"))codeFamille="";
		List<String> liste = new LinkedList<>();
		List<TaArticleDTO> temp = new LinkedList<>();
		try {
			String requete="";
			requete+="select a.id_Article,a.code_Article,a.libellec_article," + 
					" p.id_Prix, p.prix_Prix,p.prixttc_Prix,un.code_Unite,fam.code_Famille,tva.code_Tva,tva.taux_Tva,'"+codeTTarif+"' as code_T_Tarif,0 as prix_PrixCalc,0 as prixttc_PrixCalc,0 as coef,null,'nouveau' as ligne" + 
					" from Ta_Article a " + 
					" left join ta_Famille fam on fam.id_famille=a.id_famille" + 
					" join ta_Prix p  on p.id_prix=a.id_prix" + 
					" left join ta_Tva tva  on tva.id_tva=a.id_tva" + 
					" left join ta_T_Tva ttva  on ttva.id_t_tva=a.id_t_tva" + 
					" left join ta_Unite un  on un.id_unite=a.id_unite_1" + 
					"" + 
					" where a.actif=1 and  a.code_Article like :codeArticle " + 
					" and a.id_article not in (select a3.id_article from ta_prix p3 join ta_article a3 on a3.id_article=p3.id_article" + 
					"  join ta_r_prix rp3 on rp3.id_prix=p3.id_prix join ta_t_tarif tt3 on tt3.id_t_tarif=rp3.id_t_tarif "
					+ " where tt3.code_T_Tarif  like :codeTTarif) "; 
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+= " and fam.code_Famille like :codeFamille "	;
			requete+=" union  " ;

			requete+=" select a.id_Article,a.code_Article,a.libellec_article," + 
					" p2.id_Prix, p2.prix_Prix,p2.prixttc_Prix,un.code_Unite,fam.code_Famille,tva.code_Tva,tva.taux_Tva,rp.code_T_Tarif,p.prix_Prix as prix_PrixCalc,p.prixttc_Prix as prixttc_PrixCalc,tt.coef,p.id_prix as idprixCalc,'existant' as ligne" + 
					" from Ta_prix p" + 
					" join ta_article a on a.id_article=p.id_article " + 
					" left join ta_Famille fam on fam.id_famille=a.id_famille" + 
					" join ta_Prix p2  on p2.id_prix=a.id_prix" + 
					" left join ta_Tva tva  on tva.id_tva=a.id_tva" + 
					" left join ta_T_Tva ttva  on ttva.id_t_tva=a.id_t_tva" + 
					" left join ta_Unite un  on un.id_unite=a.id_unite_1" + 
					" join ta_r_prix tt  on tt.id_prix=p.id_prix" + 
					" left join ta_t_tarif rP  on rP.id_t_tarif=tt.id_t_tarif" + 
					" where a.actif=1 and  a.code_Article like :codeArticle and rp.code_T_Tarif like :codeTTarif ";
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+= " and fam.code_Famille like :codeFamille "	;
			requete+= " order by 11,8,2";
			

			
//			requete+= " order by fam.code_Famille,a.code_Article";
			Query query = entityManager.createNativeQuery(requete);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("codeTTarif", codeTTarif);
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())query.setParameter("codeFamille", codeFamille);
			List<Object> l =query.getResultList();
			for (Object object : l) {
				TaArticleDTO dto=new TaArticleDTO();
				dto.setId((Integer) ((Object[])object)[0]);
				dto.setCodeArticle((String) ((Object[])object)[1]);
				dto.setLibellecArticle((String) ((Object[])object)[2]);
				dto.setIdPrix((Integer) ((Object[])object)[3]);
				dto.setPrixPrix((BigDecimal) ((Object[])object)[4]);
				dto.setPrixttcPrix((BigDecimal) ((Object[])object)[5]);
				dto.setCodeUnite((String) ((Object[])object)[6]);
				dto.setCodeFamille((String) ((Object[])object)[7]);
				dto.setCodeTva((String) ((Object[])object)[8]);
				dto.setTauxTva((BigDecimal) ((Object[])object)[9]);
				dto.setCodeTTarif((String) ((Object[])object)[10]);
				dto.setPrixPrixCalc((BigDecimal) ((Object[])object)[11]);
				dto.setPrixttcPrixCalc((BigDecimal) ((Object[])object)[12]);
				dto.setCoef((BigDecimal) ((Object[])object)[13]);
				dto.setIdPrixCalc( (Integer) ((Object[])object)[14]);
				if(((Object[])object)[15].equals("existant"))dto.setExistant(true);
				temp.add(dto);
				liste.add(dto.getCodeArticle());
//				id_Article,a.code_Article,a.libellec_article," + 
//				" p.id_Prix, p.prix_Prix,p.prixttc_Prix,un.code_Unite,fam.code_Famille,tva.code_Tva,tva.taux_Tva,rp.code_T_Tarif,p2.prix_Prix as ht_calc ,p2.prixttc_Prix as ttc_calc			
			}
//			List<TaArticleDTO> temp =query.getResultList();
			
//			for (TaArticleDTO taArticleDTO : temp) {
//				liste.add(taArticleDTO.getCodeArticle());
//			}
//			List<TaArticleDTO> temp2=remonteGrilleReference(codeArticle, codeFamille, true);
//			for (TaArticleDTO taArticleDTO : temp2) {
//				if(liste.indexOf(taArticleDTO.getCodeArticle())==-1) {
//					temp.add(taArticleDTO);
//				}
//			}
			
			logger.debug("get successful");
			return temp;
			
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticleDTO>  remonteGrilleReference(String codeArticle, String codeFamille) {
		return remonteGrilleReference(codeArticle,codeFamille,false);		
	}
	
	
	public List<TaArticleDTO>  remonteGrilleTarifaireTiers(String codeArticle, String codeFamille, String codeTTarif,String codeTiers) {
		logger.debug("remonteGrilleTarifaire : " );
		if(codeArticle==null ||codeArticle.equals(""))codeArticle="%";
		if(codeFamille==null ||codeFamille.equals(""))codeFamille="%";
		if(!codeArticle.isEmpty() && !codeArticle.equals("%"))codeFamille="";
		List<String> liste = new LinkedList<>();
		List<TaArticleDTO> temp = new LinkedList<>();
		try {
			String requete="";
			requete+=" select a.id_Article,a.code_Article,a.libellec_article," + 
					" p.id_Prix, p.prix_Prix,p.prixttc_Prix,un.code_Unite,fam.code_Famille,tva.code_Tva,tva.taux_Tva,'codeTTarif' as code_T_Tarif,0 as prix_PrixCalc,0 as prixttc_PrixCalc,0 as coef,null,'existantTTarif' as ligne" + 
					" from Ta_prix p" + 
					" join ta_article a on a.id_article=p.id_article " + 
					" left join ta_Famille fam on fam.id_famille=a.id_famille" + 
					" join ta_Prix p2  on p2.id_prix=a.id_prix" + 
					" left join ta_Tva tva  on tva.id_tva=a.id_tva" + 
					" left join ta_T_Tva ttva  on ttva.id_t_tva=a.id_t_tva" + 
					" left join ta_Unite un  on un.id_unite=a.id_unite_1" + 
					" join ta_r_prix tt  on tt.id_prix=p.id_prix" + 
					" join ta_t_tarif rP  on rP.id_t_tarif=tt.id_t_tarif" + 
					" where a.actif=1 and  a.code_Article like :codeArticle and rp.code_T_Tarif like :codeTTarif"+
					" and a.id_article not in (select a3.id_article from ta_prix p3 join ta_article a3 on a3.id_article=p3.id_article" + 
					"  join ta_r_prix_tiers rp3 on rp3.id_prix=p3.id_prix join ta_tiers tiers on tiers.id_tiers=rp3.id_tiers join ta_t_tarif tt3 on tt3.id_t_tarif=rp3.id_t_tarif "
					+ " where tiers.code_Tiers  like :codeTiers and tt3.code_T_tarif  like :codeTTarif) "; 
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+= " and fam.code_Famille like :codeFamille "	;
			requete+=" union  " ;

			requete+=" select a.id_Article,a.code_Article,a.libellec_article," + 
					" p3.id_Prix, p3.prix_Prix,p3.prixttc_Prix,un.code_Unite,fam.code_Famille,tva.code_Tva,tva.taux_Tva,rp.code_T_Tarif,p.prix_Prix as prix_PrixCalc,p.prixttc_Prix as prixttc_PrixCalc,tt.coef,p.id_prix as idprixCalc,'existantTiers' as ligne" + 
					" from Ta_prix p" + 
					" join ta_article a on a.id_article=p.id_article " + 
					" left join ta_Famille fam on fam.id_famille=a.id_famille" + 
					" join ta_Prix p2  on p2.id_prix=a.id_prix" + 
					" left join ta_Tva tva  on tva.id_tva=a.id_tva" + 
					" left join ta_T_Tva ttva  on ttva.id_t_tva=a.id_t_tva" + 
					" left join ta_Unite un  on un.id_unite=a.id_unite_1" + 
					" join ta_r_prix_tiers tt  on tt.id_prix=p.id_prix" + 
					" join ta_t_tarif rP  on rP.id_t_tarif=tt.id_t_tarif" + 
					" join ta_r_prix rptt on rptt.id_t_tarif=tt.id_t_tarif"+
					" join ta_prix p3 on p3.id_prix=rptt.id_prix and p3.id_article=a.id_article"+					
					" join ta_tiers tiers  on tiers.id_tiers=tt.id_tiers" + 
					" where a.actif=1 and  a.code_Article like :codeArticle and rp.code_T_Tarif like :codeTTarif and tiers.code_tiers like :codeTiers";
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+= " and fam.code_Famille like :codeFamille "	;
			requete+= " order by 11,8,2";
			

			
//			requete+= " order by fam.code_Famille,a.code_Article";
			Query query = entityManager.createNativeQuery(requete);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("codeTTarif", codeTTarif);
			query.setParameter("codeTiers", codeTiers);
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())query.setParameter("codeFamille", codeFamille);
			List<Object> l =query.getResultList();
			for (Object object : l) {
				TaArticleDTO dto=new TaArticleDTO();
				dto.setId((Integer) ((Object[])object)[0]);
				dto.setCodeArticle((String) ((Object[])object)[1]);
				dto.setLibellecArticle((String) ((Object[])object)[2]);
				dto.setIdPrix((Integer) ((Object[])object)[3]);
				dto.setPrixPrix((BigDecimal) ((Object[])object)[4]);
				dto.setPrixttcPrix((BigDecimal) ((Object[])object)[5]);
				dto.setCodeUnite((String) ((Object[])object)[6]);
				dto.setCodeFamille((String) ((Object[])object)[7]);
				dto.setCodeTva((String) ((Object[])object)[8]);
				dto.setTauxTva((BigDecimal) ((Object[])object)[9]);
				dto.setCodeTTarif((String) ((Object[])object)[10]);
				dto.setPrixPrixCalc((BigDecimal) ((Object[])object)[11]);
				dto.setPrixttcPrixCalc((BigDecimal) ((Object[])object)[12]);
				dto.setCoef((BigDecimal) ((Object[])object)[13]);
				dto.setIdPrixCalc( (Integer) ((Object[])object)[14]);
				if(((Object[])object)[15].equals("existant"))dto.setExistant(true);
				temp.add(dto);
				liste.add(dto.getCodeArticle());
			}

			
			logger.debug("get successful");
			return temp;
			
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<TaArticleDTO>  remonteGrilleTarifaireComplete(String codeArticle, String codeFamille, String codeTTarif,String codeTiers) {
		logger.debug("remonteGrilleTarifaireComplete : " );
		if(codeArticle==null ||codeArticle.equals(""))codeArticle="%";
		if(codeFamille==null ||codeFamille.equals(""))codeFamille="%";
		if(!codeArticle.isEmpty() && !codeArticle.equals("%"))codeFamille="";
		List<String> liste = new LinkedList<>();
		List<TaArticleDTO> temp = new LinkedList<>();
		try {
			String requete="";

			requete+=" select a.id_Article,a.code_Article,a.libellec_article," + 
					" p2.id_Prix, p2.prix_Prix,p2.prixttc_Prix,un.code_Unite,fam.code_Famille,tva.code_Tva,tva.taux_Tva,rp.code_T_Tarif,p.prix_Prix as prix_PrixCalc,p.prixttc_Prix as prixttc_PrixCalc,tt.coef,p.id_prix as idprixCalc,null as tiers,'1' as ligne" + 
					" from Ta_prix p" + 
					" join ta_article a on a.id_article=p.id_article " + 
					" left join ta_Famille fam on fam.id_famille=a.id_famille" + 
					" join ta_Prix p2  on p2.id_prix=a.id_prix" + 
					" left join ta_Tva tva  on tva.id_tva=a.id_tva" + 
					" left join ta_T_Tva ttva  on ttva.id_t_tva=a.id_t_tva" + 
					" left join ta_Unite un  on un.id_unite=a.id_unite_1" + 
					" join ta_r_prix tt  on tt.id_prix=p.id_prix" + 
					" left join ta_t_tarif rP  on rP.id_t_tarif=tt.id_t_tarif" + 
					" where a.actif=1 and  a.code_Article like :codeArticle and rp.code_T_Tarif like :codeTTarif ";
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+= " and fam.code_Famille like :codeFamille "	;
			
			requete+=" union  " ;
			requete+=" select a.id_Article,a.code_Article,a.libellec_article," + 
					" p3.id_Prix, p3.prix_Prix,p3.prixttc_Prix,un.code_Unite,fam.code_Famille,tva.code_Tva,tva.taux_Tva,rp.code_T_Tarif,p.prix_Prix as prix_PrixCalc,p.prixttc_Prix as prixttc_PrixCalc,tt.coef,p.id_prix as idprixCalc,tiers.code_tiers as tiers,'2' as ligne" + 
					" from Ta_prix p" + 
					" join ta_article a on a.id_article=p.id_article " + 
					" left join ta_Famille fam on fam.id_famille=a.id_famille" + 
					" join ta_Prix p2  on p2.id_prix=a.id_prix" + 
					" left join ta_Tva tva  on tva.id_tva=a.id_tva" + 
					" left join ta_T_Tva ttva  on ttva.id_t_tva=a.id_t_tva" + 
					" left join ta_Unite un  on un.id_unite=a.id_unite_1" + 
					" join ta_r_prix_tiers tt  on tt.id_prix=p.id_prix" + 
					" join ta_t_tarif rP  on rP.id_t_tarif=tt.id_t_tarif" + 
					" join ta_r_prix rptt on rptt.id_t_tarif=tt.id_t_tarif"+
					" join ta_prix p3 on p3.id_prix=rptt.id_prix and p3.id_article=a.id_article"+
					" join ta_tiers tiers  on tiers.id_tiers=tt.id_tiers" + 
					" where a.actif=1 and  a.code_Article like :codeArticle and rp.code_T_Tarif like :codeTTarif and tiers.code_tiers like :codeTiers";
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())requete+= " and fam.code_Famille like :codeFamille "	;
			
			requete+= " order by 11,17,2,16";
			

			
//			requete+= " order by fam.code_Famille,a.code_Article";
			Query query = entityManager.createNativeQuery(requete);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("codeTTarif", codeTTarif);
			query.setParameter("codeTiers", codeTiers);
			if(!codeFamille.equals("%")&& !codeFamille.isEmpty())query.setParameter("codeFamille", codeFamille);
			List<Object> l =query.getResultList();
			for (Object object : l) {
				TaArticleDTO dto=new TaArticleDTO();
				dto.setId((Integer) ((Object[])object)[0]);
				dto.setCodeArticle((String) ((Object[])object)[1]);
				dto.setLibellecArticle((String) ((Object[])object)[2]);
				dto.setIdPrix((Integer) ((Object[])object)[3]);
				dto.setPrixPrix((BigDecimal) ((Object[])object)[4]);
				dto.setPrixttcPrix((BigDecimal) ((Object[])object)[5]);
				dto.setCodeUnite((String) ((Object[])object)[6]);
				dto.setCodeFamille((String) ((Object[])object)[7]);
				dto.setCodeTva((String) ((Object[])object)[8]);
				dto.setTauxTva((BigDecimal) ((Object[])object)[9]);
				dto.setCodeTTarif((String) ((Object[])object)[10]);
				dto.setPrixPrixCalc((BigDecimal) ((Object[])object)[11]);
				dto.setPrixttcPrixCalc((BigDecimal) ((Object[])object)[12]);
				dto.setCoef((BigDecimal) ((Object[])object)[13]);
				dto.setIdPrixCalc( (Integer) ((Object[])object)[14]);
				if(((Object[])object)[15]!=null)dto.setCodeTiers( (String) ((Object[])object)[15]);
				if(((Object[])object)[16].equals("existant"))dto.setExistant(true);
				temp.add(dto);
				liste.add(dto.getCodeArticle());
			}

			
			logger.debug("get successful");
			return temp;
			
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

}
