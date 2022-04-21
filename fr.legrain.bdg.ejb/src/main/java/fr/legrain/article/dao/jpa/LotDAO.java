package fr.legrain.article.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ILotDAO;
import fr.legrain.article.dao.IMouvementDAO;
import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.article.model.TracabiliteMP;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.validator.BeanValidator;



/**
 * Home object for domain model class TaLot.
 * @see fr.legrain.tiers.model.old.TaLot
 * @author Hibernate Tools
 */
public class LotDAO implements ILotDAO,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5713393845254726482L;

	static Logger logger = Logger.getLogger(LotDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLot a";
	private @Inject IMouvementDAO daoMouvement;
	
	public LotDAO(){
	}



	@Override
	public void persist(TaLot transientInstance) {
		logger.debug("persisting TaLot instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaLot persistentInstance) {
		logger.debug("removing TaLot instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaLot merge(TaLot detachedInstance) {
		logger.debug("merging TaLot instance");
		try {
			TaLot result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaLot findById(int id) {
		logger.debug("getting TaLot instance with id: " + id);
		try {
			TaLot instance = entityManager.find(TaLot.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	public TaLot findLotVirtuelArticle(int idArticle) {
		logger.debug("getting TaLot virtuel instance with idArticle: " + idArticle);
		try {
			Query query = entityManager.createQuery("select a from TaLot a where a.taArticle.idArticle='"+idArticle+"' and a.virtuel = true and a.virtuelUnique = false");
			TaLot instance = (TaLot)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (Exception re) {
			//logger.error("get failed", re);
			return null;
		}
		
	}
	
	public TaLot fetchResultatControleLazy(int idLot) {
		logger.debug("getting lot avec resultat de controle pour le lot: " + idLot);
		try {
			Query query = entityManager.createQuery("select a from TaLot a join fetch a.taResultatConformites rc where a.idLot='"+idLot+"'");
			TaLot instance = (TaLot)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (Exception re) {
			//logger.error("get failed", re);
			return null;
		}
		
	}
	
	public void changeLotTermine(List<Integer> listeDesIdLotATraite, boolean termine) {
		try {
		final int changes =
		        entityManager.createQuery("update TaLot l set l.termine = :termine where l.idLot in :listeDesIdLotATraite")
		        	//	+ " (select a.idCompteServiceWebExterne from TaCompteServiceWebExterne a join a.taServiceWebExterne s join s.taTServiceWebExterne t where t.codeTServiceWebExterne = :codeTypeServiceWebExterne and s.actif=true and a.actif=true and a.idCompteServiceWebExterne <> :id)")
		        .setParameter("listeDesIdLotATraite",listeDesIdLotATraite)
		        .setParameter("termine", termine)
		        .executeUpdate();
		} catch (Exception re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public List<TaLot> selectAll() {
	
		logger.debug("selectAll TaLot");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLot> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public TaLot findByIdAAndIdLot(Integer idArticle, Integer idLot) {
		logger.debug("getting TaArticle instance with id article: " + idArticle + "and id Lot: " + idLot);
		try {
			if(!idArticle.equals("") && !idLot.equals("")){
			Query query = entityManager.createQuery("select a from TaLot a where a.taArticle.idArticle='"+idArticle+"' and a.id="+idLot+"'");
			TaLot instance = (TaLot)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	public TaLot findByCodeAAndNumLot(String codeA, String numLot) {
		
		try {
			if(!codeA.equals("") && !numLot.equals("")){
				Query query = entityManager.createQuery("select a from TaLot a where a.taArticle.codeArticle='"+codeA+"' and a.numLot= '"+numLot+"'");
				TaLot instance = (TaLot)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	public List<Object[]> findAllObjectLight() {
		logger.debug("getting TaArticle instance with id article: " );
		try {
		
			Query query = entityManager.createQuery("select la.idLot, a.idArticle, la.idLot, a.codeArticle, a.libellecArticle,"+
			" la.numLot, la.unite1, la.unite2, la.rapport,la.nbDecimal,la.sens,la.termine from TaLot la  join la.taArticle a order by la.numLot");
			List<Object[]> l = query.getResultList();
			logger.debug("get successful");
			return l;
			
			
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	@Override
	public List<TaLot> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLot> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLot> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLot> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLot value) throws Exception {
		BeanValidator<TaLot> validator = new BeanValidator<TaLot>(TaLot.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLot value, String propertyName) throws Exception {
		BeanValidator<TaLot> validator = new BeanValidator<TaLot>(TaLot.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLot transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaLot findByCode(String code) {
		try {
			if(code!=null &&!code.equals("") ){
			Query query = entityManager.createQuery("select a from TaLot a where a.numLot='"+code+"'");
			TaLot instance = (TaLot)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLot findInstance() {
		int premierId = 1;
		logger.debug("getting TaLot instance with id: "+premierId);
		try {
			TaLot instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaLot trouve, creation d'une nouvelle instance vide");
				instance = new TaLot();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne la liste de tous les articles en stock
	 * @return
	 */
	public List<TaArticle>  articleEnStock() {
		try {
			Query query = entityManager.createQuery("select distinct m.taArticle from TaLot m where m.termine <> false");
			List<TaArticle> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne la liste de tous les lots non terminé (donc pour les quels il reste des articles en stock)
	 * @return
	 */
	public List<TaLot>  lotsNonTermine() {
		try {
			Query query = entityManager.createQuery("select distinct m from TaLot m where m.termine <> false");
			List<TaLot> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * @param codeArticle
	 * @param codeEntrepot
	 * @param emplacement
	 * @param termine
	 * @return
	 */
	public List<TaLot> lotsArticle(String codeArticle, String codeEntrepot, String emplacement, Boolean termine) {
		try {
			String q = "select distinct m from TaLot m where m.taArticle.codeArticle='"+codeArticle+"'";
			if(codeEntrepot!=null) {
				q+=" and m.taEntrepot.codeEntrepot='"+codeEntrepot+"'";
			}
			if(emplacement!=null) {
				q+=" and m.emplacement='"+emplacement+"'";
			}
			if(termine!=null) {
				if(termine) {
					q+=" and m.termine = true";
				} else {
					q+=" and m.termine = false";
				}
			}
			Query query = entityManager.createQuery(q);
			List<TaLot> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* - Dima - Début - */
	/*
	public List<TaLot> findByNonConforme(){
		TaLot lot = new TaLot();
		List<TaResultatConformite> resComf = new ArrayList<TaResultatConformite>();
		ArrayList<TaResultatConformite> res = new ArrayList<TaResultatConformite>();
		try{
			resComf = lot.getTaResultatConformites();
			for (TaResultatConformite taResConf : resComf) {
				taResConf.getIdResultatConformite();
				taResConf.getTaConformite();
				taResConf.getTaResultatCorrections();
				res.add(taResConf);
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	*/
	public List<TaLot> findByNonConformeNoDate(){ // Recherche des lot avec l'Etat de Non-Conformité sans dates
		logger.debug("getting TaLot instance with \"Non-Conformité\" status");
		try{
			Query query = entityManager.createNamedQuery(TaLot.QN.FIND_BY_NON_CONFORME_NO_DATE);
			List<TaLot> l = query.getResultList();		
			logger.debug("get successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaResultatCorrection> findByNonConformeCorrection(){
		logger.debug("getting TaResultatCorrection instance with \"Non-Conformité\" status");
		try{
			Query query = entityManager.createNamedQuery(TaLot.QN.FIND_BY_NON_CONFORME_CORRECTION);
			List<TaResultatCorrection> l = query.getResultList();		
			logger.debug("get successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		//return null;
	}
	
	public TracabiliteLot findTracaLot(String numLot) {
		logger.debug("findTracaLot()");
//		//origine
//		String TaFabrication_FIND_BY_NUM_LOT_PRODUIT_FINAL = "select a from TaFabrication a left join a.lignes lp left join lp.taLot tl where lp.taMouvementStock.qte1Stock > 0 and tl.numLot = ?";
//		String TaBonReception_FIND_BY_NUM_LOT_RECU = "select a from TaBonReception a left join a.lignes lp left join lp.taLot tl where tl.numLot = ?";
//		//utilisation
//		String TaFabrication_FIND_BY_NUM_LOT_MATIERE_PREMIERE = "select a from TaFabrication a left join a.lignes lp left join lp.taLot tl where lp.taMouvementStock.qte1Stock < 0 and tl.numLot = ?";
		
		//origine
		String TaFabrication_FIND_BY_NUM_LOT_PRODUIT_FINAL =    "select new fr.legrain.article.dto.TaFabricationDTO(f.id, f.codeDocument, f.dateDebR, f.dateDocument, f.libelleDocument, '', '', tf.codeTFabrication, tf.liblTFabrication) from TaFabrication f left join f.lignesProduits lp left join lp.taLot tl left join f.taTFabrication tf where lp.taMouvementStock.qte1Stock > 0 and tl.numLot = :numlot";
		String TaBonReception_FIND_BY_NUM_LOT_RECU = "select new fr.legrain.document.dto.TaBonReceptionDTO(f.id, f.codeDocument, f.dateDocument, f.libelleDocument, 't.codeTiers', 't.nomTiers', tr.codeTReception, tr.liblTReception) from TaBonReception f left join f.lignes lp left join lp.taLot tl join f.taTiers t left join f.taTReception tr where tl.numLot = :numlot";
		//utilisation
		String TaFabrication_FIND_BY_NUM_LOT_MATIERE_PREMIERE = "select new fr.legrain.article.dto.TaFabricationDTO(f.id, f.codeDocument, f.dateDebR, f.dateDocument, f.libelleDocument, '', '', tf.codeTFabrication, tf.liblTFabrication) from TaFabrication f left join f.lignesMatierePremieres lp left join lp.taLot tl left join f.taTFabrication tf where lp.taMouvementStock.qte1Stock < 0 and tl.numLot = :numlot";
		
		try{
			Query query1 = entityManager.createQuery(TaFabrication_FIND_BY_NUM_LOT_PRODUIT_FINAL);
			query1.setParameter("numlot", numLot);
			List<TaFabricationDTO> l1 = query1.getResultList();		
			logger.debug("get successful");
			
			Query query2 = entityManager.createQuery(TaBonReception_FIND_BY_NUM_LOT_RECU);
			query2.setParameter("numlot", numLot);
			List<TaBonReceptionDTO> l2 = query2.getResultList();		
			logger.debug("get successful");
			
			Query query3 = entityManager.createQuery(TaFabrication_FIND_BY_NUM_LOT_MATIERE_PREMIERE);
			query3.setParameter("numlot", numLot);
			List<TaFabricationDTO> l3 = query3.getResultList();		
			logger.debug("get successful");
			
			TracabiliteLot traca = new TracabiliteLot();
			traca.lot = findByCodeLight(numLot).get(0);
			if(l1!=null && !l1.isEmpty()) {
				traca.origineFabrication = l1.get(0);
			}
			if(l2!=null && !l2.isEmpty()) {
				traca.origineBonReception = l2.get(0);
			}
			if(l3!=null && !l3.isEmpty()) {
				//traca.utilFabrication = l3.get(0);
				traca.listeUtilFabrication = l3;
			}
			
			return traca;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		//return null;
	}
	
	/*
	 public List<TaLFabrication> findWithMatPremNumLot(String numLot, Date dateDebut, Date dateFin){ //Recherche par numLot de Matière Première
		logger.debug("getting TaFabrication instance with numLot de Matière Première : " + numLot );
		try {
			Query query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_NUM_LOT_MATIERE_PREMIERE);
			query.setParameter(1, numLot);
			query.setParameter(2, dateDebut);
			query.setParameter(3, dateFin);
			List<TaLFabrication> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	 */
	
	/* - Dima -  Fin  - */
	
	
	public List<TaLotDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaLot.QN.FIND_ALL_LIGHT);
			List<TaLotDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLotDTO> findByCodeLight(String code) {
		logger.debug("getting TaLotDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaLot.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("numLot", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaLot.QN.FIND_ALL_LIGHT);
			}

			List<TaLotDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaLotDTO> selectAllInReception() {
		List<TaLotDTO> retour=null;
		logger.debug("selectAllInReception " );
		try {
			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaLotDTO(l.numLot,l.libelle,a.matierePremiere,a.produitFini) from TaLot l join l.taArticle a ,"
					+ "TaLBonReception bl where bl.taLot = l order by l.numLot ");
			List<TaLotDTO> temp =query.getResultList();
			retour=new LinkedList<TaLotDTO>();
			for (TaLotDTO obj : temp) {
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
	public List<TaLotDTO> selectAllPFInFabrication() {
		List<TaLotDTO> retour=null;
		logger.debug("selectAllInReception " );
		try {
			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaLotDTO(l.numLot,l.libelle,a.matierePremiere,a.produitFini) from TaLot l join l.taArticle a ,"
					+ "TaLFabricationPF bl  where bl.taLot = l order by l.numLot ");
			List<TaLotDTO> temp =query.getResultList();
			retour=new LinkedList<TaLotDTO>();
			for (TaLotDTO obj : temp) {
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
	public List<TaLotDTO> selectAllInFabrication() {
		List<TaLotDTO> retour=null;
		logger.debug("selectAllInReception " );
		try {
			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaLotDTO(l.numLot,l.libelle,a.matierePremiere,a.produitFini) from TaLot l join l.taArticle a ,"
					+ "TaLFabricationMP bl  where bl.taLot = l order by l.numLot");
			List<TaLotDTO> temp =query.getResultList();
			retour=new LinkedList<TaLotDTO>();
			for (TaLotDTO obj : temp) {
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
	
	
	public TracabiliteMP findTracaMP(String numLot) {
		//origine
		String TaFabrication_FIND_BY_NUM_LOT_PRODUIT_FINAL = "select new fr.legrain.article.dto.TaFabricationDTO(f.id, f.codeDocument, f.dateDebR, f.dateDocument, f.libelleDocument, '', '',f.taTFabrication.codeTFabrication,f.taTFabrication.liblTFabrication) from TaFabrication f left join f.lignesProduits lp left join lp.taLot tl  where lp.taMouvementStock.qte1Stock > 0 and tl.numLot = :numlot";
		String TaBonReception_FIND_BY_NUM_LOT_RECU = "select new fr.legrain.document.dto.TaBonReceptionDTO(f.id, f.codeDocument, f.dateDocument, f.libelleDocument,  t.codeTiers, t.nomTiers,f.taTReception.codeTReception,f.taTReception.liblTReception) from TaBonReception f left join f.lignes lp left join lp.taLot tl  join f.taTiers t where tl.numLot = :numlot";
		//utilisation
		String TaFabrication_FIND_BY_NUM_LOT_MATIERE_PREMIERE = "select new fr.legrain.article.dto.TaFabricationDTO(f.id, f.codeDocument, f.dateDebR, f.dateDocument, f.libelleDocument, '', '') from TaFabrication f left join f.lignesMatierePremieres lp left join lp.taLot tl   where lp.taMouvementStock.qte1Stock < 0 and tl.numLot = :numlot";

		try{
			Query query1 = entityManager.createQuery(TaFabrication_FIND_BY_NUM_LOT_PRODUIT_FINAL);
			query1.setParameter("numlot", numLot);
			List<TaFabricationDTO> l1 = query1.getResultList();		
			logger.debug("get successful");

			Query query2 = entityManager.createQuery(TaBonReception_FIND_BY_NUM_LOT_RECU);
			query2.setParameter("numlot", numLot);
			List<TaBonReceptionDTO> l2 = query2.getResultList();		
			logger.debug("get successful");

			Query query3 = entityManager.createQuery(TaFabrication_FIND_BY_NUM_LOT_MATIERE_PREMIERE);
			query3.setParameter("numlot", numLot);
			List<TaFabricationDTO> l3 = query3.getResultList();		
			logger.debug("get successful");

			/******************************************/
			TracabiliteLot traca = new TracabiliteLot();
			traca.lot = findByCodeLight(numLot).get(0);
			if(l1!=null && !l1.isEmpty()) {
				traca.origineFabrication = l1.get(0);
			}
			if(l2!=null && !l2.isEmpty()) {
				traca.origineBonReception = l2.get(0);
			}

			/******************************************/
			Query query ;
			TracabiliteMP tracaMP=new TracabiliteMP();
			tracaMP.setTracaLot(traca);

			for (TaFabricationDTO taFabricationDTO : l3) {
				
				if(taFabricationDTO!=null){
					query= entityManager.createQuery("select f from TaFabrication f  "
							+ " where f.idDocument= :idDocument  ");
					query.setParameter("idDocument", taFabricationDTO.getId());
					
//					List<TaLFabrication> listeLigneFabPF = query.getResultList();
					TaFabrication doc = (TaFabrication) query.getSingleResult();
					tracaMP.getListUtilFabrication().add(doc);
					
				}
			}
			return tracaMP;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}


	}
	
	public List<TaLotDTO> findLotNonConforme(Date debut, Date fin) {
		return findLot(debut, fin, null);
	}
	
	
	public List<TaLotDTO> findLotAvecQte(Date debut, Date fin, Boolean presenceDeNonConformite) {
		List<TaLotDTO> retour=null;
		logger.debug("findLotAvecQte " );
		
		//TODO a supprimer, en attendant d'être sur que les différents trigger de recalcul de la table ta_etat_stock lors de la l'ajout/suppression/modification de mouvement fonctionne de façon sure.
		daoMouvement.recalculEtatStock();

		try {
			//String codeUniteRef, Integer idUniteRef, String liblUniteRef, BigDecimal qteRef,Boolean utiliseDlc,	Date dluo,
			String jpql = "select new fr.legrain.article.dto.TaLotDTO(l.id, l.numLot, l.dateLot, l.libelle, l.taArticle.codeArticle,"
					+ " l.termine, l.lotConforme, l.presenceDeNonConformite, s.idStatusConformite, s.codeStatusConformite, s.libelleStatusConformiteLot,"
					+ " es.unRef, l.versionObj, es.unRef, sum(es.qteRef), l.taArticle.utiliseDlc, l.dluo, "
					+ " l.versionObj) "
					+ " from TaLot l left join l.taStatusConformite s , TaEtatStock es "
					+ " where ";
			if(presenceDeNonConformite!=null) {
				if(presenceDeNonConformite) {
					jpql += " l.presenceDeNonConformite = true and ";
				} else {
					jpql += " l.presenceDeNonConformite = false and ";
				}
			}
			jpql += "  l.numLot = es.numLot and  l.dateLot between :d and :f "
					+ "group by"
					+ " l.id, l.numLot, l.dateLot, l.libelle, l.taArticle.codeArticle," 
					+ "	l.termine, l.lotConforme, l.presenceDeNonConformite, s.idStatusConformite, s.codeStatusConformite, s.libelleStatusConformiteLot,"
					+ "	 es.unRef, l.versionObj, es.unRef, l.taArticle.utiliseDlc, l.dluo, " 
					+ " l.versionObj"
					+ " order by l.numLot ";
//			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaLotDTO(l.id, l.numLot, l.dateLot, l.libelle, l.taArticle.codeArticle,"
//					+ " l.termine, l.lotConforme, l.presenceDeNonConformite,l.versionObj) from TaLot l "
//					+ " where l.presenceDeNonConformite = true and l.dateLot between :d and :f order by l.numLot");
			Query query = entityManager.createQuery(jpql);
			query.setParameter("d", debut);
			query.setParameter("f", fin);
			retour =query.getResultList();
			
			logger.debug("get successful");
			return retour;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaLotDTO> findLot(Date debut, Date fin, Boolean presenceDeNonConformite) {
		List<TaLotDTO> retour=null;
		logger.debug("findLotNonConforme " );
		try {
			String jpql = "select new fr.legrain.article.dto.TaLotDTO(l.id, l.numLot, l.dateLot, l.libelle, l.taArticle.codeArticle,"
					+ " l.termine, l.lotConforme, l.presenceDeNonConformite, s.idStatusConformite, s.codeStatusConformite, s.libelleStatusConformiteLot, l.versionObj) "
					+ " from TaLot l left join l.taStatusConformite s "
					+ " where ";
			if(presenceDeNonConformite!=null) {
				if(presenceDeNonConformite) {
					jpql += " l.presenceDeNonConformite = true and ";
				} else {
					jpql += " l.presenceDeNonConformite = false and ";
				}
			}
			jpql += "l.dateLot between :d and :f order by l.numLot ";
//			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaLotDTO(l.id, l.numLot, l.dateLot, l.libelle, l.taArticle.codeArticle,"
//					+ " l.termine, l.lotConforme, l.presenceDeNonConformite,l.versionObj) from TaLot l "
//					+ " where l.presenceDeNonConformite = true and l.dateLot between :d and :f order by l.numLot");
			Query query = entityManager.createQuery(jpql);
			query.setParameter("d", debut);
			query.setParameter("f", fin);
			retour =query.getResultList();
			
			logger.debug("get successful");
			return retour;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	@Override
	public TaLot findAvecResultatConformite(String numLot) {
		try {
			if(numLot!=null &&!numLot.equals("") ){
			Query query = entityManager.createQuery("select a from TaLot a left join a.taResultatConformites rc where a.numLot='"+numLot+"'");
			TaLot instance = (TaLot)query.getSingleResult();
			retourControl(instance);
//			instance.retourControl();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	@Override
	public boolean controle(TaLot lot) {
		boolean controlesOK = false;
		//Certains controle sont faits
		//controlesOK = (taResultatConformites!=null && taResultatConformites.size()>0);
		
		//Tous les controles sont fait (pas forcement OK)
		controlesOK = (lot.getTaResultatConformites()!=null 
				&& lot.getTaResultatConformites().size()>0
				&& lot.getTaArticle().getTaConformites()!=null
				&& lot.getTaArticle().getTaConformites().size()>0
				&&lot.getTaResultatConformites().size() == lot.getTaArticle().getTaConformites().size()
				);
		
		if(controlesOK) {
			//tous les controles sont fait
			boolean unControleFaux = false;
			for (TaResultatConformite rc : lot.getTaResultatConformites()) {
				for (TaResultatCorrection rcorr : rc.getTaResultatCorrections()) {
					//vérification des actions correctives pour les controles "constatés faux"
					if(!rcorr.getValide()) {
						unControleFaux = true; 
					}
				}
			}
			if(unControleFaux) {
				// au moins une action corrective n'a pas été effectué =>> au moins un controle est dans un état "faux"
				controlesOK = false;
			}
		}
		return controlesOK;
	}
	
	@Override
	public String retourControl(TaLot lot) {
		String retour = "";
		//Certains controle sont faits
		//controlesOK = (taResultatConformites!=null && taResultatConformites.size()>0);
//		if(this.getTaArticle().getTaConformites()!=null && this.getTaArticle().getTaConformites().size()>0)retour="V";
		
		if(lot.getTaArticle().getTaConformites()!=null && lot.getTaArticle().getTaConformites().size()>0)retour="";		
		if(lot.getTaResultatConformites().size()>0) retour="OK";

			boolean unControleFaux = false;
			for (TaResultatConformite rc : lot.getTaResultatConformites()) {
				for (TaResultatCorrection rcorr : rc.getTaResultatCorrections()) {
					//vérification des actions correctives pour les controles "constatés faux"
					if(!rcorr.getValide()) {
						unControleFaux = true; 
					}
				}
			}
			if(unControleFaux) {
				// au moins un controle est dans un état "faux"
				retour = "N";
			}
		
		return retour;
	}
	
	@Override
	public TaLot initListeResultatControle(List<TaConformite> l, TaLot lot) {
		if(lot.getTaArticle().getTaConformites()!=null) {
			lot.setTaResultatConformites(new LinkedList<TaResultatConformite>());
			TaResultatConformite r = null;
			if(l!=null) {
				for (TaConformite ctrl : l) {
					r = new TaResultatConformite();
					r.setTaConformite(ctrl);
					r.setTaLot(lot);
					lot.getTaResultatConformites().add(r);
				}
			} else {
				for (TaConformite ctrl : lot.getTaArticle().getTaConformites()) {
					r = new TaResultatConformite();
					r.setTaConformite(ctrl);
					r.setTaLot(lot);
					lot.getTaResultatConformites().add(r);
				}
			}
		}
		return lot;
	}

	@Override
	public void suppression_lot_non_utilise() {
		logger.debug("getting supprime_lot_non_utilise ");
		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("supprime_lot_non_utilise");
			query.execute();
			logger.debug("get successful");
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
