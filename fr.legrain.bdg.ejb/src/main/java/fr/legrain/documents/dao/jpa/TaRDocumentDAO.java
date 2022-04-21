package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.documents.dao.IRDocumentDAO;


/**
 * Home object for domain model class TaRDocument.
 * @see fr.legrain.documents.dao.TaRDocument
 * @author Hibernate Tools
 */
public class TaRDocumentDAO implements IRDocumentDAO /*extends AbstractApplicationDAO<TaRDocument>*/ {

//	private static final Log log = LogFactory.getLog(TaRDocumentDAO.class);
	static Logger logger = Logger.getLogger(TaRDocumentDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaRDocument a";

	
	public TaRDocumentDAO(){
//		this(null);
	}
	
	public void persist(TaRDocument transientInstance) {
		logger.debug("persisting TaRDocument instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRDocument persistentInstance) {
		logger.debug("removing TaRDocument instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRDocument merge(TaRDocument detachedInstance) {
		logger.debug("merging TaRDocument instance");
		try {
			TaRDocument result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRDocument findById(int id) {
		logger.debug("getting TaRDocument instance with id: " + id);
		try {
			TaRDocument instance = entityManager.find(TaRDocument.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaRDocument findByIdGenere(TaRDocument taDocumentGenere) {
		logger.debug("getting findByIdGenere instance with : " + taDocumentGenere);
		try {
			Query ejbQuery = entityManager.createQuery("select r from TaRDocument r where r.taDocumentGenere=:taDocumentGenere");
			ejbQuery.setParameter("taDocumentGenere", taDocumentGenere);
			Object instance =  ejbQuery.getSingleResult();
			logger.debug("get successful");
			if(instance==null)return null;
			return (TaRDocument) instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public Boolean exist(TaRDocument taDocumentGenere) {
		logger.debug("exist with taDocumentGenere: " + taDocumentGenere);
		try {
			if(taDocumentGenere!=null ){
			Query ejbQuery = entityManager.createQuery("select count(f) from TaRDocument f where f.taDocumentGenere= :taDocumentGenere");
			ejbQuery.setParameter("taDocumentGenere", taDocumentGenere);
			Long instance = (Long)ejbQuery.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaRDocument> selectAll() {
		logger.debug("selectAll TaRDocument");
		try {
			 String requete = "select a from TaRDocument a ";
//				 		+ "left join fetch a.taFacture "
//				 		+ "left join fetch a.taAvoir "
//				 		+ "left join fetch a.taAcompte "
//				 		+ "left join fetch a.taApporteur "
//				 		+ "left join fetch a.taBoncde "
//				 		+ "left join fetch a.taBoncdeAchat "
//				 		+ "left join fetch a.taDevis "
//				 		+ "left join fetch a.taProforma "
//				 		+ "left join fetch a.taPrelevement "
//				 		+ "left join fetch a.taBonliv "
//				 		+ "left join fetch a.taTicketDeCaisse "
//				 		+ "left join fetch a.taAvisEcheance "
//				 		+ "left join fetch a.taBonReception "       
//				 		+ "left join fetch a.taPreparation ";
			Query ejbQuery = entityManager.createQuery(requete);
			List<TaRDocument> l = ejbQuery.getResultList();

			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	
	
	/**
	 * Classe permettant d'obtenir les Bons de Livraison non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des BL non transformés
	 */
	public List<TaBonliv> findBLNonTransfos(Date debut, Date fin) {
		logger.debug("getting nombre BL non transfos");
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
						" and taFacture IS NOT NULL)";
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
	 * Classe permettant d'obtenir les docs transformés en document typedoc
	 * @param typedoc le type du document
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des devis non transformés
	 */
	public List<Object> findDocTransfosEn(String typeDocSourceMaj ,
			String typeDocSourceMin, String typedocfinal ,Date debut, Date fin) {
		logger.debug("getting doc non transfos");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" d"
				+" FROM "+typeDocSourceMaj+" d " 
				+" where d.dateDocument between :dateDeb and :dateFin"
				+" and exists (select r " +
						"from TaRDocument r " +
						" where r."+typeDocSourceMin+" = d" +
						" and "+typedocfinal+" IS NOT NULL)";
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
	 * Classe permettant d'obtenir les devis transformés en document typedoc
	 * @param typedoc le type du document
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des devis non transformés
	 */
	public List<Object> findDocTransPlusieursFois(String typedocMaj, String typedocMin,Date debut, Date fin) {
		logger.debug("getting nombre devis non transfos");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" d"
				+" FROM "+typedocMaj+" d " 
				+" where d.dateDocument between :dateDeb and :dateFin"
				+" and (select count(r) " +
						"from TaRDocument r " +
						" where r."+typedocMin+" = d) > 1";
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
	

	
	
	public List<Object> findChiffreAffaireTotalByTypeDoc(String typedoc,Date debut, Date fin, int precision) {
		logger.debug("getting ChiffreAffaire total");
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
			+" FROM "+typedoc+" d " 
			+" where d.dateDocument between :dateDeb and :dateFin"
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

	@Override
	public TaRDocument findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaRDocument value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaRDocument value, String propertyName)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaRDocument transientInstance) {
		// TODO Auto-generated method stub
		
	}
	public List<TaRDocument> dejaGenere(String requete){
		List<TaRDocument> result;
		try {
			Query query = entityManager.createQuery(requete);

			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
		}
		return null;
	}
	
	
	public List<TaRDocumentDTO> dejaGenereDocument(String requete){
		List<TaRDocumentDTO> result;
		try {
			Query query = entityManager.createQuery(requete);

			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
		}
		return null;
	}
	
	

}
