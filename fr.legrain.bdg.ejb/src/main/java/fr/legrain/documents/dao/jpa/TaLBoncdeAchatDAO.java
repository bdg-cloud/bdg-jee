package fr.legrain.documents.dao.jpa;

import java.util.Date;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.documents.dao.ILBoncdeAchatDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLDevis.
 * @see fr.legrain.documents.dao.TaLDevis
 * @author Hibernate Tools
 */
public class TaLBoncdeAchatDAO /*extends AbstractApplicationDAO<TaLBoncdeAchat>*/ implements ILBoncdeAchatDAO{

//	private static final Log log = LogFactory.getLog(TaLDevisDAO.class);
	static Logger logger = Logger.getLogger(TaLBoncdeAchatDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLBoncdeAchat a";
	
	public TaLBoncdeAchatDAO(){
//		this(null);
	}
	
//	public TaLBoncdeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLBoncdeAchat.class.getSimpleName());
//		initChampId(TaLBoncdeAchat.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLBoncdeAchat());
//	}
	
//	public TaLBoncdeAchat refresh(TaLBoncdeAchat detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLBoncdeAchat.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLBoncdeAchat transientInstance) {
		logger.debug("persisting TaLBoncdeAchat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLBoncdeAchat persistentInstance) {
		logger.debug("removing TaLBoncdeAchat instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLBoncdeAchat merge(TaLBoncdeAchat detachedInstance) {
		logger.debug("merging TaLBoncdeAchat instance");
		try {
			TaLBoncdeAchat result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLBoncdeAchat findById(int id) {
		logger.debug("getting TaLBoncdeAchat instance with id: " + id);
		try {
			TaLBoncdeAchat instance = entityManager.find(TaLBoncdeAchat.class, id);
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
	public List<TaLBoncdeAchat> selectAll() {
		logger.debug("selectAll TaLBoncdeAchat");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLBoncdeAchat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaLBoncdeAchat findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLBoncdeAchat> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLBoncdeAchat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLBoncdeAchat> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLBoncdeAchat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLBoncdeAchat value) throws Exception {
		BeanValidator<TaLBoncdeAchat> validator = new BeanValidator<TaLBoncdeAchat>(TaLBoncdeAchat.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLBoncdeAchat value, String propertyName)
			throws Exception {
		BeanValidator<TaLBoncdeAchat> validator = new BeanValidator<TaLBoncdeAchat>(TaLBoncdeAchat.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLBoncdeAchat transientInstance) {
		entityManager.detach(transientInstance);
	}


	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin,TaEtat etat,String tEtat) {
		// TODO Auto-generated method stub
		logger.debug("selectLigneDocNonAffecte ");
		if(etat!=null && etat.getTaTEtat()!=null)tEtat=etat.getTaTEtat().getCodeTEtat();
		if(tEtat==null)tEtat=TaEtat.ENCOURS;
		try {			
			String requete = "select new fr.legrain.document.dto.TaLigneALigneDTO(l.idLDocument,a.idDocument,  a.codeDocument,  art.codeArticle,l.datePrevue, l.libLDocument,  "
					+ "l.u1LDocument,  l.qteLDocument, coalesce(l.qteLDocument-sum(li.qteRecue),l.qteLDocument),l.prixULDocument,et.codeEtat) "
					+ " from TaLigneALigne li  right join li.taL"+typeOrigine+" l   join l.taDocument a join a.taTiers t join l.taArticle art left join l.taREtatLigneDocument rt left join rt.taEtat et where " ;
			if(codeTiers!=null ) 
				requete+=" t.codeTiers like '"+codeTiers+"' and ";
			if(doc!=null)
				requete+=" a.idDocument = "+doc.getIdDocument()+" and ";
			requete+=" a.dateDocument between :dateDeb and :dateFin ";
			requete+=" and et is not null and et.taTEtat.codeTEtat like '"+tEtat.toUpperCase()+"'";
			if(etat!=null) {
				switch (etat.getCodeEtat()) {
				case TaEtat.ETAT_TOUS:
					break;					
				default:
					requete+=" and et is not null and et.codeEtat like '"+etat.getCodeEtat()+"' ";
					break;
				}
			}
//			if(!afficheLigneAvecEtat)requete+=" and et is null ";
//			requete+=" and l.qteLDocument > (select coalesce(sum(li.qteRecue),0)  from TaLigneALigne li " + 
//					"		join li.taL"+typeOrigine+" org " + 
//					"			join li.taL"+typeDest+" dest "  + 
//					"			 where org.idLDocument" + 
//					"	= l.idLDocument and (dest.idLDocument is not null or dest.idLDocument!=0))" ;
			requete+=" group by l.idLDocument,a.idDocument,  a.codeDocument,  art.codeArticle,l.datePrevue, l.libLDocument,  l.u1LDocument,  l.qteLDocument,l.prixULDocument,et.codeEtat";			
					requete+="  order by l.datePrevue,a.codeDocument,a.dateDocument,art.codeArticle";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("dateDeb", debut);
			ejbQuery.setParameter("dateFin", fin);
			List<TaLigneALigneDTO> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
//	public List<ILigneDocumentTiers> selectLigneDocNonAffecte2(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin) {
//		// TODO Auto-generated method stub
//		logger.debug("selectLigneDocNonAffecte ");
//		try {			
//			String requete = "select l from TaL"+typeOrigine+" l join l.taDocument a join a.taTiers t join l.taArticle art where " ;
//			if(codeTiers!=null ) //&& !codeTiers.equals("")
//				requete+=" t.codeTiers like '"+codeTiers+"' and ";
//			if(doc!=null)
//				requete+=" a.idDocument = "+doc.getIdDocument()+" and ";
//			requete+=" a.dateDocument between :dateDeb and :dateFin ";
//			requete+=" and l.qteLDocument > (select coalesce(sum(li.qteRecue),0)  from TaLigneALigne li "+
//			"join li.taL"+typeOrigine+" org " +
//			"join li.taL"+typeDest+" dest " +
//			" where org.idLDocument" +
//			" = l.idLDocument and (dest.idLDocument is not null or dest.idLDocument!=0))";
//			
//			requete+="  order by a.dateDocument,a.codeDocument,art.codeArticle";
//			Query ejbQuery = entityManager.createQuery(requete);
//			ejbQuery.setParameter("dateDeb", debut);
//			ejbQuery.setParameter("dateFin", fin);
//			List<ILigneDocumentTiers> l = ejbQuery.getResultList();
//			return l;
//		} catch (RuntimeException re) {
//			throw re;
//		}
//	}
}
