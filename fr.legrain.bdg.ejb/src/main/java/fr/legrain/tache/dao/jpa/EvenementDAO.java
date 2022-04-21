package fr.legrain.tache.dao.jpa;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.tache.dao.IEvenementDAO;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaRDocumentEvenement;
import fr.legrain.tiers.model.TaDocumentTiers;
import fr.legrain.validator.BeanValidator;

public class EvenementDAO implements IEvenementDAO {

	static Logger logger = Logger.getLogger(EvenementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaEvenement a";
	
	public EvenementDAO(){
	}

//	public TaEvenement refresh(TaEvenement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaEvenement.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaEvenement transientInstance) {
		logger.debug("persisting TaEvenement instance");
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
	public void remove(TaEvenement persistentInstance) {
		logger.debug("removing TaEvenement instance");
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
	public TaEvenement merge(TaEvenement detachedInstance) {
		logger.debug("merging TaEvenement instance");
		try {
			TaEvenement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaEvenement findById(int id) {
		logger.debug("getting TaEvenement instance with id: " + id);
		try {
			TaEvenement instance = entityManager.find(TaEvenement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEvenement> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEvenement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEvenement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEvenement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEvenement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEvenement value) throws Exception {
		BeanValidator<TaEvenement> validator = new BeanValidator<TaEvenement>(TaEvenement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEvenement value, String propertyName) throws Exception {
		BeanValidator<TaEvenement> validator = new BeanValidator<TaEvenement>(TaEvenement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEvenement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaEvenement findByCode(String code) {
		return null;
	}

	public List<TaRDocumentEvenement> findListeDocuments(TaEvenement event) {
		String requete="select l from TaEvenement e join e.listeDocument l where e=:event ";
		Query query = entityManager.createQuery(requete);
		query.setParameter("event",event);
		List<TaRDocumentEvenement> l =  query.getResultList();
		for (TaRDocumentEvenement taRDocumentEvenement : l) {
			if(taRDocumentEvenement.getTaDevis()!=null) taRDocumentEvenement.getTaDevis().getCodeDocument();
			if(taRDocumentEvenement.getTaFacture()!=null) taRDocumentEvenement.getTaFacture().getCodeDocument();
			if(taRDocumentEvenement.getTaAcompte()!=null) taRDocumentEvenement.getTaAcompte().getCodeDocument();
			if(taRDocumentEvenement.getTaApporteur()!=null) taRDocumentEvenement.getTaApporteur().getCodeDocument();
			if(taRDocumentEvenement.getTaAvisEcheance()!=null) taRDocumentEvenement.getTaAvisEcheance().getCodeDocument();
			if(taRDocumentEvenement.getTaAvoir()!=null) taRDocumentEvenement.getTaAvoir().getCodeDocument();
			if(taRDocumentEvenement.getTaBoncde()!=null) taRDocumentEvenement.getTaBoncde().getCodeDocument();
			if(taRDocumentEvenement.getTaBonliv()!=null) taRDocumentEvenement.getTaBonliv().getCodeDocument();
			if(taRDocumentEvenement.getTaBonReception()!=null) taRDocumentEvenement.getTaBonReception().getCodeDocument();
			if(taRDocumentEvenement.getTaFabrication()!=null) taRDocumentEvenement.getTaFabrication().getCodeDocument();
			if(taRDocumentEvenement.getTaPrelevement()!=null) taRDocumentEvenement.getTaPrelevement().getCodeDocument();
			if(taRDocumentEvenement.getTaProforma()!=null) taRDocumentEvenement.getTaProforma().getCodeDocument();
		}
		return l;
	}
	
	public List<TaEvenement> findByDate(Date debut, Date fin, List<TaAgenda> listeAgenda) {
		logger.debug("getting TaEvenement instance");
		try {		
			Query query = null;
			List<TaEvenement> l = null;
			if(debut!=null && fin!=null && listeAgenda!=null && !listeAgenda.isEmpty()) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select e from TaEvenement e where e.taAgenda in :listeAgenda and e.dateDebut between :dateDebut and :dateFin";
				query = entityManager.createQuery(jpql);
				query.setParameter("listeAgenda",listeAgenda);
				query.setParameter("dateDebut",debut);
				query.setParameter("dateFin",fin);
			} else {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_ALL_LIGHT);
			}

			if(query!=null) {
				l = query.getResultList();
				//Initialisation des relation document en LAZY, voir comment on peu faire mieux, surement avec une requete directe et un type de document
				for (TaEvenement taEvenement : l) {
					if(taEvenement.getListeDocument()!=null && !taEvenement.getListeDocument().isEmpty()) {
						int nbdoc = taEvenement.getListeDocument().size();
						for(int i=0; i<nbdoc; i++) {
							if(taEvenement.getListeDocument().get(i).getTaDevis()!=null) taEvenement.getListeDocument().get(i).getTaDevis().getCodeDocument();
							if(taEvenement.getListeDocument().get(i).getTaFacture()!=null) taEvenement.getListeDocument().get(i).getTaFacture().getCodeDocument();
						}
					}
				}
			} else {
				l = new ArrayList<>();
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaEvenement entity,String field) throws ExceptLgr {	
//		
//	}

}
