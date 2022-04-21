package fr.legrain.documents.dao.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.documents.dao.ILEcheanceDAO;

/**
 * Home object for domain model class TaLEcheance.
 * @see fr.legrain.documents.dao.TaLEcheance
 * @author Hibernate Tools
 */
public class TaLEcheanceDAO /*extends AbstractApplicationDAO<TaLEcheance>*/ implements ILEcheanceDAO {

//	private static final Log log = LogFactory.getLog(TaLEcheanceDAO.class);
	static Logger logger = Logger.getLogger(TaLEcheanceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLEcheance a";
	
	public TaLEcheanceDAO(){
//		this(null);
	}
	
//	public TaLEcheanceDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLEcheance.class.getSimpleName());
//		initChampId(TaLEcheance.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLEcheance());
//	}
//	public TaLEcheance refresh(TaLEcheance detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLEcheance.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLEcheance transientInstance) {
		logger.debug("persisting TaLEcheance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLEcheance persistentInstance) {
		logger.debug("removing TaLEcheance instance");
		try {
			//test yann
//			persistentInstance = entityManager.merge(persistentInstance);
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLEcheance merge(TaLEcheance detachedInstance) {
		logger.debug("merging TaLEcheance instance");
		try {
			TaLEcheance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLEcheance findById(int id) {
		logger.debug("getting TaLEcheance instance with id: " + id);
		try {
			TaLEcheance instance = entityManager.find(TaLEcheance.class, id);
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
	public List<TaLEcheance> selectAll() {
		logger.debug("selectAll TaLEcheance");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	public List<TaLEcheance> rechercheEcheanceLieAAbonnement(TaAbonnementOld taAbonnement) {
//		logger.debug(" rechercheEcheanceLieAAbonnement");
//		try {
//			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e where e.taAbonnement=:taAbonnement");
//			ejbQuery.setParameter("taAbonnement", taAbonnement);
//			List<TaLEcheance> l = ejbQuery.getResultList();
//			logger.debug("rechercheEcheanceLieAAbonnement successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("rechercheEcheanceLieAAbonnement failed", re);
//			throw re;
//		}
//	}
	/**
	 * @author yann
	 * @param idLAvisEcheance
	 * @return une taLEcheance
	 */
	//obsolete
//	public TaLEcheance findByIdLAvisEcheance(Integer idLAvisEcheance) {
//		logger.debug(" findByIdLAvisEcheance");
//		try {
//			Query ejbQuery = entityManager.createQuery("select e from TaLAvisEcheance ae join ae.taLEcheance e where ae.idLDocument =:idLAvisEcheance");
//			ejbQuery.setParameter("idLAvisEcheance", idLAvisEcheance);
//			TaLEcheance l = (TaLEcheance) ejbQuery.getSingleResult();
//			logger.debug("findByIdLAvisEcheance successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("findByIdLAvisEcheance failed", re);
//			throw re;
//		}
//	}
	public TaLEcheance findByIdLAvisEcheance(Integer idLAvisEcheance) {
		logger.debug(" findByIdLAvisEcheance");
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e join e.taLAvisEcheance lae where lae.idLDocument =:idLAvisEcheance");
			ejbQuery.setParameter("idLAvisEcheance", idLAvisEcheance);
			TaLEcheance l = (TaLEcheance) ejbQuery.getSingleResult();
			logger.debug("findByIdLAvisEcheance successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findByIdLAvisEcheance failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> findAllByIdLAvisEcheance(Integer idLAvisEcheance) {
		logger.debug(" findAllByIdLAvisEcheance");
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e join e.taLAvisEcheance lae where lae.idLDocument =:idLAvisEcheance");
			ejbQuery.setParameter("idLAvisEcheance", idLAvisEcheance);
			List<TaLEcheance> l = (List<TaLEcheance>) ejbQuery.getResultList();
			logger.debug("findAllByIdLAvisEcheance successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByIdLAvisEcheance failed", re);
			throw re;
		}
	}
	
	
	public TaLEcheance findByIdLFacture(Integer idLFacture) {
		logger.debug(" findByIdLFacture");
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e join e.taLFacture lae where lae.idLDocument =:idLFacture");
			ejbQuery.setParameter("idLFacture", idLFacture);
			TaLEcheance l = (TaLEcheance) ejbQuery.getSingleResult();
			logger.debug("findByIdLFacture successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findByIdLFacture failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> findAllByIdLFacture(Integer idLFacture) {
		logger.debug(" findAllByIdLFacture");
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e join e.taLFacture lae where lae.idLDocument =:idLFacture");
			ejbQuery.setParameter("idLFacture", idLFacture);
			List<TaLEcheance> l = (List<TaLEcheance>) ejbQuery.getResultList();
			logger.debug("findAllByIdLFacture successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByIdLFacture failed", re);
			throw re;
		}
	}

	
	public List<TaLEcheance> findAllSuspendues(){
		logger.debug(" findAllSuspendues");
		Date now = new Date();
		String codeEtat = "doc_suspendu";
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e left join e.taREtat re join re.taEtat et"
					+ " where et.codeEtat = :codeEtat");
			ejbQuery.setParameter("codeEtat", codeEtat);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllSuspendues successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllSuspendues failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> findAllEcheanceSansEtat(){
		logger.debug(" findAllEcheanceSansEtat");
		Date now = new Date();
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e left join e.taREtat re "
					+ " where re = null");
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllEcheanceSansEtat successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEcheanceSansEtat failed", re);
			throw re;
		}
	}
	
	
	public List<TaLEcheance> findAllEnCoursDepasse(){
		logger.debug(" findAllEnCoursDepasse");
		Date now = new Date();
		String codeEtat = "doc_encours";
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e left join e.taREtat re join re.taEtat et"
					+ " where e.debutPeriode < :now "
					+ " and et.codeEtat = :codeEtat");
			ejbQuery.setParameter("now", now);
			ejbQuery.setParameter("codeEtat", codeEtat);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllEnCoursDepasse successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEnCoursDepasse failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceLieAAbonnement(TaAbonnement taAbonnement) {
		logger.debug(" rechercheEcheanceLieAAbonnement");
		
		Integer id = taAbonnement.getIdDocument();
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaLEcheance e where e.taAbonnement.idDocument =:id");
			ejbQuery.setParameter("id", id);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceLieAAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceLieAAbonnement failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceTiers() {
		return rechercheEcheanceNonLieAAvisEcheanceTiers(null);
	}
	
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceTiers(String codeTiers) {
		logger.debug(" rechercheEcheanceNonLieAAvisEcheanceTiers");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " where e.idLEcheance not in (select ech.idLEcheance from TaLAvisEcheance lae join lae.taLEcheance ech)";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceNonLieAAvisEcheanceTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceNonLieAAvisEcheanceTiers failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TaLEcheance> findAllNonLieAAvisEcheancePayeTiers(String codeTiers){
		logger.debug(" findAllNonLieAAvisEcheancePayeTiers");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " where e.idLEcheance not in "
					+ " (select e2.idLEcheance from TaLAvisEcheance lae "
					+ " join lae.taLEcheance e2 "
					+ " join lae.taDocument ae "
					+ " join TaRDocument rdoc on rdoc.taAvisEcheance.idDocument = ae.idDocument)"
					+ " and abo.dateAnnulation is null";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllNonLieAAvisEcheancePayeTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllNonLieAAvisEcheancePayeTiers failed", re);
			throw re;
		}
	}
	
	
	public List<TaLEcheance> findAllLieAAvisEcheanceTiers(String codeTiers){
		logger.debug(" findAllLieAAvisEcheanceTiers");
		try {
			String jpql = ""
					+ "select distinct e from TaLAvisEcheance lae "
					+ " join lae.taLEcheance e"
					+ " join lae.taDocument ae"
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan ";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllLieAAvisEcheanceTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllLieAAvisEcheanceTiers failed", re);
			throw re;
		}
	}
	
	
	public List<TaLEcheance> findAllLieAAvisEcheancePayeTiers(String codeTiers) {
		logger.debug(" findAllLieAAvisEcheancePayeTiers");
		try {
			String jpql = ""
					+ "select distinct e from TaLAvisEcheance lae "
					+ " join lae.taLEcheance e"
					+ " join lae.taDocument ae"
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join TaRDocument rdoc on rdoc.taAvisEcheance.idDocument = ae.idDocument";
//					+ " where e.idLEcheance in "
//					+ "(select e.idLEcheance from TaLAvisEcheance lae join lae.taLEcheance ech where lae.taDocument.idDocument in"
//					+ "( select rdoc.taAvisEcheance.idDocument from TaRDocument rdoc where rdoc.taAvisEcheance.idDocument = lae.taDocument.idDocument))";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllLieAAvisEcheancePayeTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllLieAAvisEcheancePayeTiers failed", re);
			throw re;
		}
	}
	public List<TaLEcheance> findAllEcheanceNonTotTransforme(){
		logger.debug(" findAllEcheanceNonTotTransforme");
		try {
			String jpql = ""
					+ "select e from TaLEcheance e "
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " left join  e.taREtat retat"
					+ " left join  retat.taEtat etat"
					+ " where e.taREtat is null or etat.codeEtat not like 'doc_tot_Transforme'";

			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllEcheanceNonTotTransforme successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEcheanceNonTotTransforme failed", re);
			throw re;
		}
	}
	


	public List<TaLEcheanceDTO> findAllEcheanceByCodeEtatsAndByIdTiersDTO(List<String> etats, Boolean codeModuleBDG, Integer idTiers){
		try {
			String jpql = ""
					+ "select new fr.legrain.document.dto.TaLEcheanceDTO(e.idLEcheance, art.idArticle, art.codeArticle, e.libLDocument, e.qteLDocument,"
					+ " e.prixULDocument, e.tauxTvaLDocument, e.mtHtLDocument, e.mtTtcLDocument,"
					+ " e.mtHtLApresRemiseGlobaleDocument, e.mtTtcLApresRemiseGlobaleDocument, e.remTxLDocument, e.remHtLDocument,"
					+ " e.u1LDocument, abo.idDocument, e.debutPeriode, e.finPeriode, e.dateFinCalcul,"
					+ " e.debAbonnement, e.finAbonnement, e.dateEcheance, tiers.idTiers, tiers.codeTiers, tiers.nomTiers,"
					+ " e.coefMultiplicateur, etat.codeEtat, art.codeModuleBdg, abo.codeDocument "
					+ " ) from TaLEcheance e "
					+ " join e.taLAbonnement l "
					+ " join l.taDocument abo "
					+ " join abo.taTiers tiers "
					+ " join l.taArticle art "
					+ " join e.taREtat retat "
					+ " join retat.taEtat etat ";
			Boolean conditionAvant = false;
			
			if(idTiers!=null || (etats != null && !etats.isEmpty()) ||  codeModuleBDG != null) {
				jpql +=" where ";
			}
			if(etats != null && !etats.isEmpty()) {
				conditionAvant = true;
				Integer it = 0;
				for (String code : etats) {
					String or = " or ";
					boolean debut = false;
					boolean fin = false;
					if(it > 0) {
						jpql += or;
					}
					if (it == 0) {
						debut = true;
						fin = false;
					}
					if(it == etats.size()-1) {
						fin = true;
					}
					//position dans la boucle
					if(debut) {
						jpql += "(";
					}
										
					jpql += " etat.codeEtat  = '"+code+"' ";
					if(fin) {
						jpql += ")";
					}
					it++;
				}
			}
			if(idTiers!=null) {
				String and = " and ";
				if(conditionAvant == true) {
					jpql +=and;
				}
				jpql += " tiers.idTiers = '"+idTiers+"' ";
				conditionAvant = true;
			}
			if(codeModuleBDG != null &&  codeModuleBDG == true) {
				String and = " and ";
				if(conditionAvant == true) {
					jpql +=and;
				}
				jpql += " art.codeModuleBdg is not null ";
			}
			
			


			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);

			List<TaLEcheanceDTO> l = ejbQuery.getResultList();
			logger.debug("findAllEcheanceByCodeEtatsAndByIdTiersDTO successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEcheanceByCodeEtatsAndByIdTiersDTO failed", re);
			throw re;
		}
	}
	public List<TaLEcheanceDTO> findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(Integer idTiers) {
		try {
			String jpql = ""
					+ "select new fr.legrain.document.dto.TaLEcheanceDTO(e.idLEcheance, art.idArticle, art.codeArticle, e.libLDocument, e.qteLDocument,"
					+ " e.prixULDocument, e.tauxTvaLDocument, e.mtHtLDocument, e.mtTtcLDocument,"
					+ " e.mtHtLApresRemiseGlobaleDocument, e.mtTtcLApresRemiseGlobaleDocument, e.remTxLDocument, e.remHtLDocument,"
					+ " e.u1LDocument, abo.idDocument, e.debutPeriode, e.finPeriode, e.dateFinCalcul,"
					+ " e.debAbonnement, e.finAbonnement, e.dateEcheance, tiers.idTiers, tiers.codeTiers, tiers.nomTiers,"
					+ " e.coefMultiplicateur, etat.codeEtat, art.codeModuleBdg, abo.codeDocument "
					+ " ) from TaLEcheance e "
					+ " join e.taLAbonnement l "
					+ " join l.taDocument abo "
					+ " join abo.taTiers tiers "
					+ " join l.taArticle art "
					+ " join e.taREtat retat "
					+ " join retat.taEtat etat "
					+ " where (etat.codeEtat = 'doc_encours' or etat.codeEtat = 'doc_suspendu') "
					+ " and  art.codeModuleBdg is not null";
			
			if(idTiers!=null) {
				jpql += " and taTiers.idTiers = '"+idTiers+"'";
			}


			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);

			List<TaLEcheanceDTO> l = ejbQuery.getResultList();
			logger.debug("findAllEcheanceEnCoursOuSuspendueModuleBDGDTO successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEcheanceEnCoursOuSuspendueModuleBDGDTO failed", re);
			throw re;
		}
	}
	public List<TaLEcheance> findAllEcheanceEnCoursOuSuspendueModuleBDG(Integer idTiers){
		
			try {
					String jpql = ""
							+ "select e from TaLEcheance e "
							+ " join fetch e.taREtat retat"
							+ " join fetch retat.taEtat etat"
							+ " where (etat.codeEtat = 'doc_encours' or etat.codeEtat = 'doc_suspendu') "
							+ " and  e.taLAbonnement.taArticle.codeModuleBdg is not null";
					
					if(idTiers!=null) {
						jpql += " and e.taLAbonnement.taDocument.taTiers.idTiers = '"+idTiers+"'";
					}

		
					jpql += " order by e.dateEcheance";
					
					Query ejbQuery = entityManager.createQuery(jpql);

					List<TaLEcheance> l = ejbQuery.getResultList();
					logger.debug("findAllEcheanceEnCoursOuSuspendueModuleBDG successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("findAllEcheanceEnCoursOuSuspendueModuleBDG failed", re);
					throw re;
				}
	}
	
	public List<TaLEcheance> findAllEnCoursByIdLAbonnement(Integer id){
		try {
			
			String jpql = ""
					+ "select e from TaLEcheance e "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where etat.codeEtat = 'doc_encours' "
					+ " and e.taLAbonnement.idLDocument = :id";

			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllEnCoursByIdLAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEnCoursByIdLAbonnement failed", re);
			throw re;
		}
	}
	public List<TaLEcheance> findAllEnCoursOuSuspenduByIdLAbonnement(Integer id){
		try {
			
			String jpql = ""
					+ "select e from TaLEcheance e "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where (etat.codeEtat = 'doc_encours' or etat.codeEtat = 'doc_suspendu')"
					+ " and e.taLAbonnement.idLDocument = :id";

			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllEnCoursByIdLAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEnCoursByIdLAbonnement failed", re);
			throw re;
		}
	}
	public List<TaLEcheance> findAllEnCoursArrivantAEcheanceDans60Jours(){
		try {
			Date dateNow = new Date();
			LocalDateTime localDateTime = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			localDateTime = localDateTime.plusDays(60);
			Date dateX = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			
			String jpql = ""
					+ "select e from TaLEcheance e "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where etat.codeEtat = 'doc_encours' "
					+ " and e.dateEcheance > :dateX";

			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("dateX", dateX);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllEnCoursArrivantAEcheanceDans60Jours successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllEnCoursArrivantAEcheanceDans60Jours failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceEnCoursTiers(String codeTiers) {
		logger.debug(" rechercheEcheanceEnCoursTiers");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where etat.codeEtat = 'doc_encours' ";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceNonLieAAvisEcheanceTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceNonLieAAvisEcheanceTiers failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement(String codeTiers, String codeTPaiement) {
		logger.debug(" rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " left join fetch abo.taTPaiement tp "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where (etat.codeEtat = 'doc_encours' or etat.codeEtat = 'doc_suspendu')";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			if(codeTPaiement!=null) {
				jpql += " and tp.codeTPaiement = '"+codeTPaiement+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiers(String codeTiers) {
		logger.debug(" rechercheEcheanceEnCoursOuSuspendusTiers");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where (etat.codeEtat = 'doc_encours' or etat.codeEtat = 'doc_suspendu')";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceEnCoursOuSuspendusTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceEnCoursOuSuspendusTiers failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdAbonnement(Integer id){
		logger.debug(" rechercheEcheanceEnCoursByIdAbonnement");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " join fetch e.taLAbonnement l"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where abo.idDocument = :id"
					+ " and etat.codeEtat = 'doc_encours'";

			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceEnCoursByIdAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceEnCoursByIdAbonnement failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdLAbonnement(Integer id){
		logger.debug(" rechercheEcheanceEnCoursByIdLAbonnement");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " join fetch e.taLAbonnement l"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where l.idLDocument = :id"
					+ " and etat.codeEtat = 'doc_encours'";

			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceEnCoursByIdLAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceEnCoursByIdLAbonnement failed", re);
			throw re;
		}
	}
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement(Integer id) {
		logger.debug(" rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " join fetch e.taLAbonnement l"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where l.idLDocument = :id"
					+ " and (etat.codeEtat = 'doc_encours' or etat.codeEtat = 'doc_suspendu')";

			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceBycodeAvisEcheance(String code) {
		logger.debug(" rechercheEcheanceBycodeAvisEcheance");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					+ " join fetch e.taAbonnement abo"
					+ " join fetch e.taLAbonnement l"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where e.taLAvisEcheance.taDocument.codeDocument = :code";

			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("code", code);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceBycodeAvisEcheance successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceBycodeAvisEcheance failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> rechercheEcheanceByIdLAbonnement(Integer id) {
		logger.debug(" rechercheEcheanceByIdLAbonnement");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " join fetch e.taLAbonnement l"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where l.idLDocument = :id";

			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceByIdLAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceByIdLAbonnement failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> selectAllEcheanceARelancer(String codeTiers) {
		logger.debug(" selectAllEcheanceARelancer");
		try {
//			String jpql = " select distinct(tle) from ta_l_echeance tle "
//					+ " left join ta_article ta on ta.id_article = tle.id_article "
//					+ " left join ta_jour_relance tjr on tjr.id_article = ta.id_article "
//					+ " left join ta_abonnement abo on abo.id_document = tle.id_abonnement"
//					+ " left join ta_tiers t on t.id_tiers = abo.id_tiers"
//					+ " where current_date + interval '1' day * tjr.nb_jour = tle.date_echeance ";
			String jpql = " select distinct e from TaLEcheance e "
					+ " left join e.taArticle ta "
				    + " left join TaJourRelance tjr ON tjr.taArticle.idArticle = ta.idArticle"
					+ " left join e.taAbonnement abo "
					+ " left join abo.taTiers t "
					+ " join  e.taREtat retat "
					+ " join retat.taEtat etat "
					+ " where current_date + tjr.nbJour = e.dateEcheance "
					+ " and (etat.codeEtat = 'doc_suspendu' or  etat.codeEtat = 'doc_encours')";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("selectAllEcheanceARelancer successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAllEcheanceARelancer failed", re);
			System.out.println(re);
			throw re;
		}
	}
	
	
	public List<TaLEcheance> rechercheEcheanceSuspendusTiers(String codeTiers) {
		logger.debug(" rechercheEcheanceSuspendusTiers");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					//+ " join fetch e.taStripeSubscription s "
					+ " join fetch e.taAbonnement abo"
					+ " left join fetch abo.taStripeCustomer c "
					+ " join fetch abo.taTiers t "
					+ " join fetch abo.lignes i "
					+ " join fetch i.taPlan "
					+ " join fetch e.taREtat retat"
					+ " join fetch retat.taEtat etat"
					+ " where etat.codeEtat = 'doc_suspendu'";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceSuspendusTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceSuspendusTiers failed", re);
			throw re;
		}
	}
	
	public long countEcheanceNonLieAAvisEcheanceTiers(String codeTiers) {
		logger.debug(" countEcheanceNonLieAAvisEcheanceTiers");
		try {
			String jpql = ""
					+ "select count(distinct e) from TaLEcheance e "
					//+ " join e.taStripeSubscription s "
					+ " join e.taAbonnement abo"
					//+ " left join  abo.taStripeCustomer c "
					+ " join abo.taTiers t "
					+ " join abo.lignes i "
					+ " join i.taPlan "
					+ " where e.idLEcheance not in (select ech.idLEcheance from TaLAvisEcheance lae join lae.taLEcheance ech)"
					+ " and abo.suspension = false"
					+ " and abo.dateAnnulation is null";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			
			Query ejbQuery = entityManager.createQuery(jpql);
			Long l = (Long) ejbQuery.getSingleResult();
			logger.debug("countEcheanceNonLieAAvisEcheanceTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("countEcheanceNonLieAAvisEcheanceTiers failed", re);
			throw re;
		}
	}
	
	public BigDecimal montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers(String codeTiers) {
		logger.debug(" montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers");
		try {
			String jpql = ""
					+ "select sum(e.mtHtLApresRemiseGlobaleDocument) from TaLEcheance e "
					//+ " join e.taStripeSubscription s "
					+ " join e.taAbonnement abo"
					+ " left join abo.taStripeCustomer c "
					+ " join abo.taTiers t "
					+ " join abo.lignes i "
					+ " join i.taPlan "
					+ " where e.idLEcheance not in (select distinct(ech.idLEcheance) from TaLAvisEcheance lae join lae.taLEcheance ech)"
					+ " and abo.suspension = false"
					+ " and abo.dateAnnulation is null";
			if(codeTiers!=null) {
				jpql += " and t.codeTiers = '"+codeTiers+"'";
			}
			
			Query ejbQuery = entityManager.createQuery(jpql);
			BigDecimal l = (BigDecimal) ejbQuery.getSingleResult();
			logger.debug("montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers failed", re);
			throw re;
		}
	}
	
	public long countTiersEcheanceNonLieAAvisEcheance() {
		logger.debug(" countTiersEcheanceNonLieAAvisEcheance");
		try {
			String jpql = ""
					+ "select count(distinct t) from TaLEcheance e "
					//+ " join e.taStripeSubscription s "
					+ " join e.taAbonnement abo"
					+ " left join abo.taStripeCustomer c "
					+ " join abo.taTiers t "
					+ " join abo.lignes i "
					+ " join i.taPlan "
					+ " where e.idLEcheance not in (select ech.idLEcheance from TaLAvisEcheance lae join lae.taLEcheance ech)"
					+ " and abo.suspension = false"
					+ " and abo.dateAnnulation is null";
			
			
			Query ejbQuery = entityManager.createQuery(jpql);
			Long l = (Long) ejbQuery.getSingleResult();
			logger.debug("countTiersEcheanceNonLieAAvisEcheance successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("countTiersEcheanceNonLieAAvisEcheance failed", re);
			throw re;
		}
	}
	
//	public Integer countAllByIdSubscriptionItem(Integer idSubscriptionItem) {
//		logger.debug(" countAllByIdSubscriptionItem");
//		try {
//			String jpql = ""
//					+ "select count(e) from TaLEcheance e "
//					+ " join  e.taStripeSubscription s "
//					+ " join  e.taStripeSubscriptionItem si "
//					+ " join  s.taAbonnement abo"
//					+ " left join  s.taStripeCustomer c "
//					+ " join  abo.taTiers t "
//					+ " join  s.items i "
//					+ " join  i.taPlan "
//					+ " where si.idStripeSubscriptionItem = :idSubscriptionItem";
//			
//			Query ejbQuery = entityManager.createQuery(jpql);
//			ejbQuery.setParameter("idSubscriptionItem", idSubscriptionItem);
//			Long nblong = (Long) ejbQuery.getSingleResult();
//			Integer l = 0;
//			if(nblong != null) {
//				 l = (int) (long)nblong;
//			}
//			//Integer l = (int)(long) ejbQuery.getSingleResult();
//			logger.debug("countAllByIdSubscriptionItem successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("countAllByIdSubscriptionItem failed", re);
//			throw re;
//		}
//	}
	
	public Integer countAllByIdLigneAbo(Integer idLigneAbo) {
		logger.debug(" countAllByIdSubscriptionItem");
		try {
			String jpql = ""
					+ "select count(e.idLEcheance) from TaLEcheance e "
					+ " join  e.taAbonnement abo "
					+ " join  e.taLAbonnement si "
					+ " left join  abo.taStripeCustomer c "
					+ " join  abo.taTiers t "
					+ " join  abo.lignes i "
					+ " join  i.taPlan "
					+ " where si.idLDocument = :idLigneAbo";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("idLigneAbo", idLigneAbo);
			Long nblong = (Long) ejbQuery.getSingleResult();
			Integer l = 0;
			if(nblong != null) {
				 l = (int) (long)nblong;
			}
			//Integer l = (int)(long) ejbQuery.getSingleResult();
			logger.debug("countAllByIdSubscriptionItem successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("countAllByIdSubscriptionItem failed", re);
			throw re;
		}
	}
	
//	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceSubscription() {
//		return rechercheEcheanceNonLieAAvisEcheanceSubscription(null);
//	}
//	
//	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceSubscription(TaStripeSubscription taStripeSubscription) {
//		logger.debug(" rechercheEcheanceLieAAbonnement");
//		try {
//			String jpql = ""
//					+ "select distinct e from TaLEcheance e "
//					+ " join fetch e.taStripeSubscription s "
//					+ " join fetch s.items i "
//					+ " join fetch i.taPlan "
//					+ " where e.idLEcheance not in (select ech.idLEcheance from TaLAvisEcheance lae join lae.taLEcheance ech)";
//			if(taStripeSubscription!=null) {
//				jpql += " and e.taStripeSubscription=:taStripeSubscription ";
//			}
//			jpql += " order by e.dateEcheance";
//			
//			Query ejbQuery = entityManager.createQuery(jpql);
//			if(taStripeSubscription!=null) {
//				ejbQuery.setParameter("taStripeSubscription", taStripeSubscription);
//			}
//			List<TaLEcheance> l = ejbQuery.getResultList();
//			logger.debug("rechercheEcheanceLieAAbonnement successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("rechercheEcheanceLieAAbonnement failed", re);
//			throw re;
//		}
//	}
	
	
	public List<TaLEcheance> rechercheEcheanceLightNonLieAAvisEcheanceAbonnement(){
		logger.debug(" rechercheEcheanceLightNonLieAAvisEcheanceAbonnement");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					+ " where e.idLEcheance not in (select ech.idLEcheance from TaLAvisEcheance lae join lae.taLEcheance ech)";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceLightNonLieAAvisEcheanceAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceLightNonLieAAvisEcheanceAbonnement failed", re);
			throw re;
		}
	}
	

	
	
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceAbonnement(){
		return rechercheEcheanceNonLieAAvisEcheanceAbonnement(null);
	}
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceAbonnement(TaAbonnement taAbonnement){
		logger.debug(" rechercheEcheanceNonLieAAvisEcheanceAbonnement");
		try {
			String jpql = ""
					+ "select distinct e from TaLEcheance e "
					+ " join fetch e.taAbonnement s "
					+ " join fetch s.lignes i "
					+ " join fetch i.taPlan "
					+ " where e.idLEcheance not in (select ech.idLEcheance from TaLAvisEcheance lae join lae.taLEcheance ech)";
			if(taAbonnement!=null) {
				jpql += " and e.taAbonnement=:taAbonnement ";
			}
			jpql += " order by e.dateEcheance";
			
			Query ejbQuery = entityManager.createQuery(jpql);
			if(taAbonnement!=null) {
				ejbQuery.setParameter("taAbonnement", taAbonnement);
			}
			List<TaLEcheance> l = ejbQuery.getResultList();
			logger.debug("rechercheEcheanceNonLieAAvisEcheanceAbonnement successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("rechercheEcheanceNonLieAAvisEcheanceAbonnement failed", re);
			throw re;
		}
	}
	
	public List<TaLEcheance> findAllArrivantAEcheanceDansXJours(Integer nbJours, String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createQuery("");
			Date now = new Date();
			LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime localDateTimeNowPlusXJours = localDateTimeNow.plusDays(nbJours);
			Date nowPlusXJours = Date.from(localDateTimeNowPlusXJours.atZone(ZoneId.systemDefault()).toInstant());
			query.setParameter("now", now, TemporalType.DATE);
			query.setParameter("nowPlusXJours", nowPlusXJours, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<TaLEcheance> l = (List<TaLEcheance>) query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	


	
//	public void suppressionAbonnementDansLEcheance(TaAbonnementOld taAbonnement){
//		List<TaLEcheance> liste=rechercheEcheanceLieAAbonnement(taAbonnement);
//		if(!entityManager.getTransaction().isActive())entityManager.getTransaction().begin();
//		if(liste!=null){
//			for (TaLEcheance taLEcheance : liste) {		
//				//mise en comm car obsolète (abonnementOld) et risque de faire planté 
//				//taLEcheance.setTaAbonnement(null);
//				merge(taLEcheance);
//			}
//		}
//		entityManager.getTransaction().commit();
//	}
	
	public List<TaLEcheance> rechercheLigneRenouvellementAbonnement(Date dateDeb, Date datefin, String codeTAbonnement, String codeTiers) {
		List<TaLEcheance> result = null;
		Query query = null;

		String requete="select le from TaLEcheance le join le.taAbonnement ta join ta.taTAbonnement tat join ta.taTiers t join ta.taSupportAbon s where " +
				" (le.debutPeriode between :dateDeb and :dateFin )and(le.finPeriode between :dateDeb2 and :dateFin2 )" +
				" and tat.codeTAbonnement like :codeTAbonnement" +
				" and t.codeTiers like :codeTiers  and not exists(select a from TaLAvisEcheance a where a.taLEcheance.idLEcheance=le.idLEcheance)" +
				" and s.inactif<>1 and t.actifTiers=1" +
				" order by t.codeTiers,le.debAbonnement" ;		

		if(codeTAbonnement==null || codeTAbonnement.toLowerCase().equals("tous")) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("dateDeb2", dateDeb);
			query.setParameter("dateFin2", datefin);
			query.setParameter("codeTAbonnement", "%");
			query.setParameter("codeTiers", codeTiers);
		}else if(codeTAbonnement!=null ) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("dateDeb2", dateDeb);
			query.setParameter("dateFin2", datefin);
			query.setParameter("codeTAbonnement", codeTAbonnement);
			query.setParameter("codeTiers", codeTiers);
		}

		result = query.getResultList();


		return result;
	}

	@Override
	public TaLEcheance findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLEcheance> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLEcheance> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaLEcheance value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaLEcheance value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaLEcheance transientInstance) {
		// TODO Auto-generated method stub
		
	}

}
