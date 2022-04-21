package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.documents.dao.ILFlashDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLFlash.
 * @see fr.legrain.documents.dao.TaLFlash
 * @author Hibernate Tools
 */
public class TaLFlashDAO /*extends AbstractApplicationDAO<TaLFlash>*/ implements ILFlashDAO{


	static Logger logger = Logger.getLogger(TaLFlashDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;


	private String defaultJPQLQuery = "select a from TaLFlash a";
	
	public TaLFlashDAO(){

	}
	

	
	public void persist(TaLFlash transientInstance) {
		logger.debug("persisting TaLFlash instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLFlash persistentInstance) {
		logger.debug("removing TaLFlash instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLFlash merge(TaLFlash detachedInstance) {
		logger.debug("merging TaLFlash instance");
		try {
			TaLFlash result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLFlash findById(int id) {
		logger.debug("getting TaLFlash instance with id: " + id);
		try {
			TaLFlash instance = entityManager.find(TaLFlash.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	public List<TaLFlash> selectAll() {
		logger.debug("selectAll TaLFlash");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLFlash> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public TaLFlash findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLFlash> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLFlash> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLFlash> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLFlash> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLFlash value) throws Exception {
		BeanValidator<TaLFlash> validator = new BeanValidator<TaLFlash>(TaLFlash.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLFlash value, String propertyName)
			throws Exception {
		BeanValidator<TaLFlash> validator = new BeanValidator<TaLFlash>(TaLFlash.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLFlash transientInstance) {
		entityManager.detach(transientInstance);
	}
	

	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(TaFlash doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin,TaEtat etat,String tEtat) {
		// TODO Auto-generated method stub
		logger.debug("selectLigneDocNonAffecte ");
		if(etat!=null && etat.getTaTEtat()!=null)tEtat=etat.getTaTEtat().getCodeTEtat();
		if(tEtat==null)tEtat=TaEtat.ENCOURS;
		if(codeTiers==null || codeTiers.isEmpty())codeTiers="%";
		try {			
			String requete = "select new fr.legrain.document.dto.TaLigneALigneDTO(l.idLFlash,a.idFlash,  a.codeFlash,  coalesce(l.codeArticle,''),a.dateFlash, l.libLFlash,  "
					+ "l.u1LFlash,  l.qteLFlash, coalesce(l.qteLFlash-sum(li.qteRecue),l.qteLFlash),et.codeEtat,l.codeTiers,l.numLot,td.codeTDoc,l.codeBarre) "
					+ " from TaLigneALigne li  right join li.taLFlash l   join l.taFlash a left join a.taTDoc td left join l.taREtatLigneDocument rt left join rt.taEtat et  where " ;
//			if(codeTiers!=null ) 
//				requete+=" a.codeTiers like '"+codeTiers+"' and ";
			if(doc!=null)
				requete+=" a.idFlash = "+doc.getIdFlash()+" and ";
			requete+=" a.dateFlash between :dateDeb and :dateFin ";
			requete+=" and et is not null and ";
			if(tEtat !=null)requete+= "et.taTEtat.codeTEtat like '"+tEtat.toUpperCase()+"'";
			if(etat!=null) {
				switch (etat.getCodeEtat()) {
				case TaEtat.ETAT_TOUS:
					break;					
				default:
					requete+=" and et is not null and et.codeEtat like '"+etat.getCodeEtat()+"' ";
					break;
				}
			}
			requete+=" and a.codeTDoc is not null and a.codeTDoc like :tDoc ";
			requete+=" group by l.idLFlash,a.idFlash,  a.codeFlash,  l.codeArticle,a.dateFlash, l.libLFlash,  l.u1LFlash,  l.qteLFlash,et.codeEtat,l.codeTiers,l.numLot,td.codeTDoc,l.codeBarre";			
					requete+="  order by a.dateFlash,a.codeFlash,l.codeArticle,l.numLot";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("dateDeb", debut);
			ejbQuery.setParameter("dateFin", fin);
			ejbQuery.setParameter("tDoc", typeDest);
			List<TaLigneALigneDTO> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
