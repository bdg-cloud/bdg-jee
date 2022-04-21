package fr.legrain.article.dao.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IEditionEtatTracabiliteDAO;
import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.validator.BeanValidator;

/** Créé par Dima **/

public class EditionEtatTracabiliteDAO implements IEditionEtatTracabiliteDAO {

	private static final Log logger = LogFactory.getLog(EditionEtatTracabiliteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select u from TaFabrication u";
	
	public EditionEtatTracabiliteDAO(){}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.EditionEtatTracabilite)
	 */
	public void persist(EditionEtatTracabilite transientInstance) {
		logger.debug("persisting EditionEtatTracabilite instance");
		try {
			
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.EditionEtatTracabilite)
	 */
	public void remove(EditionEtatTracabilite persistentInstance) {
		logger.debug("removing EditionEtatTracabilite instance");
		try {
			EditionEtatTracabilite e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.EditionEtatTracabilite)
	 */
	public EditionEtatTracabilite merge(EditionEtatTracabilite detachedInstance) {
		logger.debug("merging EditionEtatTracabilite instance");
		try {
			EditionEtatTracabilite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public EditionEtatTracabilite findByCode(String code) {
		logger.debug("getting EditionEtatTracabilite instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from EditionEtatTracabilite f where f.codeDocument='"+code+"'");
				EditionEtatTracabilite instance = (EditionEtatTracabilite)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public EditionEtatTracabilite findById(int id) {
		logger.debug("getting EditionEtatTracabilite instance with id: " + id);
		try {
			EditionEtatTracabilite instance = entityManager.find(EditionEtatTracabilite.class, id);
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
	public List<EditionEtatTracabilite> selectAll() {
		logger.debug("selectAll EditionEtatTracabilite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<EditionEtatTracabilite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public EditionEtatTracabilite refresh(EditionEtatTracabilite detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(EditionEtatTracabilite.class, detachedInstance.getPfCodeFabrication());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	

	@Override
	public List<EditionEtatTracabilite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<EditionEtatTracabilite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<EditionEtatTracabilite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<EditionEtatTracabilite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(EditionEtatTracabilite value) throws Exception {
		BeanValidator<EditionEtatTracabilite> validator = new BeanValidator<EditionEtatTracabilite>(EditionEtatTracabilite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(EditionEtatTracabilite value, String propertyName) throws Exception {
		BeanValidator<EditionEtatTracabilite> validator = new BeanValidator<EditionEtatTracabilite>(EditionEtatTracabilite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(EditionEtatTracabilite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public List <EditionEtatTracabilite> editionCA(String codeArticle){
		List<Object[]> mp ;//= new ArrayList<Object>();
//		List<EditionEtatTracabilite> mpe;
//		List<Object[]> pf ;

		/*
		Article
		- Code d'Article // cheked
		- Libellé/Nom d'Article // cheked
		
		Code Fabrication  // cheked
		
		Produit Fini - ?
		- Numéro du Lot
		- Libellé
		
		Matière Première - ?
		- Numéro du Lot
		- Libellé
		
		Date de la production
		
		DLUO
		
		Quantité rééle + Unité
		
		Quantité produite + Unité
		
		Entrepot :
		- fl.getTaEntrepot().getCodeEntrepot();
		- fl.getTaEntrepot().getLibelle();
		
		*/
		
		logger.debug("getting EditionEtatTracabilite instance with Code Article : " + codeArticle );
		String qStringMP = 	"select  "+
								/* Lot/Article */
								"f.taLot.numLot, f.taLot.taArticle.codeArticle, f.taLot.taArticle.libellecArticle, "+
								"f.qteLDocument, f.qte2LDocument, f.u1LDocument, f.u2LDocument, "+
								"f.taLot.dluo, f.taEntrepot.codeEntrepot, f.taEntrepot.libelle, " +
								"f.taLot.taArticle.taFamille.libcFamille, " +
								//"f.taDocument.codeDocument "+
								
								/* Bon de Reception */
								"b.taDocument.codeDocument, b.taDocument.dateDocument, b.qteLDocument, b.u1LDocument, "+
								"b.taDocument.taTiers.nomTiers, b.taDocument.taTiers.codeTiers "+
							
							"from "+
								"TaLBonReception b, TaLFabrication f, TaLFabrication f2 join f.taDocument fab "+
							"where "+
								"f2.taLot.taArticle.codeArticle != '"+codeArticle+"' "+
								"and f.taDocument.codeDocument = f2.taDocument.codeDocument "+ // and 
								"and f.taLot.numLot = b.taLot.numLot "+
								"and f.taLot.taArticle.codeArticle = '"+codeArticle+"' " +
								//"or fab.taLot.taArticle.codeArticle = '"+codeArticle+"' "+
								//"and f.taLot.taArticle.codeArticle != f2.taLot.taArticle.codeArticle " +
								" "; //   
		   

		Query queryMP = entityManager.createQuery(qStringMP); 
//			Query queryPF = entityManager.createQuery(qStringPF);

		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		
		mp = queryMP.getResultList();
//			mpe = queryMP.getResultList();
//			pf = queryPF.getResultList();

//			for (Object[] tab : pf){
//				tab[0].toString();
//			}

		for (Object[] tab : mp){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMpNumLot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMpNumLot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMpCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMpCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMpLibelleArticle(tab[2].toString());
			} else {
				editionEtatTracabilite.setMpLibelleArticle(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMpQte1(LibConversion.stringToBigDecimal(tab[3].toString()));
			} else {
				editionEtatTracabilite.setMpQte1(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMpQte2(LibConversion.stringToBigDecimal(tab[4].toString()));
			} else {
				editionEtatTracabilite.setMpQte2(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMpUnite1(tab[5].toString());
			} else {
				editionEtatTracabilite.setMpUnite1(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMpUnite2(tab[6].toString());
			} else {
				editionEtatTracabilite.setMpUnite2(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMpDluo(new Date(((Timestamp)tab[7]).getTime()));
			} else {
				editionEtatTracabilite.setMpDluo(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setMpCodeEntrepot(tab[8].toString());
			} else {
				editionEtatTracabilite.setMpCodeEntrepot(null);
			}
			if (tab[9] != null){
				editionEtatTracabilite.setMpLibelleEntrepot(tab[9].toString());
			} else {
				editionEtatTracabilite.setMpNumLot(null);
			}
			if (tab[10] != null){
				editionEtatTracabilite.setMpFamille(tab[10].toString());
			} else {
				editionEtatTracabilite.setMpFamille(null);
			}
			if (tab[11] != null){
				editionEtatTracabilite.setBrNumReception(tab[11].toString());
			} else {
				editionEtatTracabilite.setBrNumReception(null);
			}
			if (tab[12] != null){
				editionEtatTracabilite.setBrDateReception(new Date(((java.sql.Date)tab[12]).getTime()));
//					editionEtatTracabilite.setBrDateReception(LibDate.stringToDateSql(tab[12].toString()));
			} else {
				editionEtatTracabilite.setBrDateReception(null);
			}
			if (tab[13] != null){
				editionEtatTracabilite.setBrQteReception(LibConversion.stringToBigDecimal(tab[13].toString()));
			} else {
				editionEtatTracabilite.setBrQteReception(null);
			}
			if (tab[14] != null){
				editionEtatTracabilite.setBrUniteReception(tab[14].toString());
			} else {
				editionEtatTracabilite.setBrUniteReception(null);
			}
			if (tab[15] != null){
				editionEtatTracabilite.setBrNomFournisseur(tab[15].toString());
			} else {
				editionEtatTracabilite.setBrNomFournisseur(null);
			}
			if (tab[16] != null){
				editionEtatTracabilite.setBrCodeFournisseur(tab[16].toString() );
			} else {
				editionEtatTracabilite.setBrCodeFournisseur(null);
			}	
			
			// NumLot de Produit Fini pour les PFs multiples
			//editionEtatTracabilite.setPfNumLot(numLotPF);
			
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
//			return mpe;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
	}
	
	public List <EditionEtatTracabilite> editionCADate(String codeArticle, Date dateDebut, Date dateFin){
		List<Object[]> mp ;//= new ArrayList<Object>();
//		List<EditionEtatTracabilite> mpe;
//		List<Object[]> pf ;

		/*
		Article
		- Code d'Article // cheked
		- Libellé/Nom d'Article // cheked
		
		Code Fabrication  // cheked
		
		Produit Fini - ?
		- Numéro du Lot
		- Libellé
		
		Matière Première - ?
		- Numéro du Lot
		- Libellé
		
		Date de la production
		
		DLUO
		
		Quantité rééle + Unité
		
		Quantité produite + Unité
		
		Entrepot :
		- fl.getTaEntrepot().getCodeEntrepot();
		- fl.getTaEntrepot().getLibelle();
		
		*/
		
		logger.debug("getting EditionEtatTracabilite instance with Code Article : " + codeArticle + " et les Dates de début \"" + dateDebut + "\" et la fin \"" + dateFin + "\"" );
/*		String qStringMP = 	"select  "+
								//* Lot/Article 
								"f.taLot.numLot, f.taLot.taArticle.codeArticle, f.taLot.taArticle.libellecArticle, "+
								"f.qteLDocument, f.qte2LDocument, f.u1LDocument, f.u2LDocument, "+
								"f.taLot.dluo, f.taEntrepot.codeEntrepot, f.taEntrepot.libelle, " +
								"f.taLot.taArticle.taFamille.libcFamille, " +
								//"f.taDocument.codeDocument "+
								
								//* Bon de Reception 
								"b.taDocument.codeDocument, b.taDocument.dateDocument, b.qteLDocument, b.u1LDocument, "+
								"b.taDocument.taTiers.nomTiers, b.taDocument.taTiers.codeTiers "+
							
							"from "+
								"TaLBonReception b, TaLFabrication f, TaLFabrication f2 join f.taDocument fab "+
							"where "+
								"f2.taLot.taArticle.codeArticle != '"+codeArticle+"' "+
								"and f.taDocument.codeDocument = f2.taDocument.codeDocument "+ // and 
								"and f.taLot.numLot = b.taLot.numLot "+
								"and f.taLot.taArticle.codeArticle = '"+codeArticle+"' " +
								//"or fab.taLot.taArticle.codeArticle = '"+codeArticle+"' "+
								//"and f.taLot.taArticle.codeArticle != f2.taLot.taArticle.codeArticle " +
								" "; //   
*/
		/*
			set schema 'demo'; 
			select  
			  a.code_article, a.libellec_article, f.libc_famille, l.unite1, l.unite2, l.dluo, 
			  m.date_stock, m.libelle_stock, m.qte1_stock, m.un1_stock, m.qte2_stock, m.un2_stock, m.num_lot, 
			  e.code_entrepot, e.libelle, gm.code_gr_mouv_stock, gm.date_gr_mouv_stock, gm.libelle_gr_mouv_stock
			from 
			  ta_article a, ta_gr_mouv_stock gm, ta_famille f, ta_lot l, ta_mouvement_stock m, ta_entrepot e 
			where 
			  gm.date_gr_mouv_stock between '2015-07-01' and '2015-08-09' and 
			  a.id_famille = f.id_famille and
			  a.id_article = l.id_article and
			  a.code_article = 'ART_00004' and
			  l.numlot = m.num_lot and
			  m.id_gr_mouv_stock = gm.id_gr_mouv_stock and
			  e.id_entrepot = m.id_entrepot
			order by
			  gm.date_gr_mouv_stock;
 */
		
		String qStringMP = "select  " +
			  "a.code_article, a.libellec_article, f.libc_famille, l.dluo, " + 
			  "m.qte1_stock, m.un1_stock, m.qte2_stock, m.un2_stock, l.numlot, " + 
			  "e.code_entrepot, e.libelle, gm.code_gr_mouv_stock, gm.date_gr_mouv_stock, gm.libelle_gr_mouv_stock " +
			"from " + 
			  "ta_article a, ta_gr_mouv_stock gm, ta_famille f, ta_lot l, ta_mouvement_stock m, ta_entrepot e " + 
			"where " + 
			  "gm.date_gr_mouv_stock between '"+dateDebut+"' and '"+dateFin+"' and " + 
			  "a.id_famille = f.id_famille and " +
			  "a.id_article = l.id_article and " +
			  "a.code_article = '"+codeArticle+"' and " +
			  "l.id_lot = m.id_lot and " +
			  "m.id_gr_mouv_stock = gm.id_gr_mouv_stock and " +
			  "e.id_entrepot = m.id_entrepot " +
			"order by " +
			  "gm.date_gr_mouv_stock";
		
		Query queryMP = entityManager.createNativeQuery(qStringMP); 
//			Query queryPF = entityManager.createQuery(qStringPF);

		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		
		mp = queryMP.getResultList();
//			mpe = queryMP.getResultList();
//			pf = queryPF.getResultList();

//			for (Object[] tab : pf){
//				tab[0].toString();
//			}

		for (Object[] tab : mp){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMouvCodeArticle(tab[0].toString());
			} else {
				editionEtatTracabilite.setMouvCodeArticle(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMouvLibelleArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMouvLibelleArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMouveTypeFamille(tab[2].toString());
			} else {
				editionEtatTracabilite.setMouveTypeFamille(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMouveDluo(new Date(((Timestamp)tab[3]).getTime()));
			} else {
				editionEtatTracabilite.setMouveDluo(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMouveQte1(LibConversion.stringToBigDecimal(tab[4].toString()));
			} else {
				editionEtatTracabilite.setMouveQte1(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMouveUnite1(tab[5].toString());
			} else {
				editionEtatTracabilite.setMouveUnite1(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMouveQte2(LibConversion.stringToBigDecimal(tab[6].toString()));
			} else {
				editionEtatTracabilite.setMouveQte2(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMouveUnite2(tab[7].toString());
			} else {
				editionEtatTracabilite.setMouveUnite2(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setMouvNumLot(tab[8].toString());
			} else {
				editionEtatTracabilite.setMouvNumLot(null);
			}
			if (tab[9] != null){
				editionEtatTracabilite.setMouvEntrepot(tab[9].toString());
			} else {
				editionEtatTracabilite.setMouvEntrepot(null);
			}
			if (tab[10] != null){
				editionEtatTracabilite.setMouvLibEntrepot(tab[10].toString());
			} else {
				editionEtatTracabilite.setMouvLibEntrepot(null);
			}
			if (tab[11] != null){
				editionEtatTracabilite.setMouveCodeDocument(tab[11].toString());
			} else {
				editionEtatTracabilite.setMouveCodeDocument(null);
			}
			if (tab[12] != null){
				editionEtatTracabilite.setMouveDate(new Date(((java.sql.Date)tab[12]).getTime()));
//					editionEtatTracabilite.setBrDateReception(LibDate.stringToDateSql(tab[12].toString()));
			} else {
				editionEtatTracabilite.setMouveDate(null);
			}
			if (tab[13] != null){
				editionEtatTracabilite.setMouveLibDocument(tab[13].toString() );
			} else {
				editionEtatTracabilite.setMouveLibDocument(null);
			}	
			
			// NumLot de Produit Fini pour les PFs multiples
			//editionEtatTracabilite.setPfNumLot(numLotPF);
			
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
//			return mpe;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
	}
		
	public List <EditionEtatTracabilite> editionPFNL(String numLotPF){
		List<Object[]> mp ;//= new ArrayList<Object>();
//		List<EditionEtatTracabilite> mpe;
//		List<Object[]> pf ;

		logger.debug("getting EditionEtatTracabilite instance with numLot de Produit Finit : " + numLotPF );
		String qStringMP = 	"select  "+
								/* Matière Première */
								"f.taLot.numLot, f.taLot.taArticle.codeArticle, f.taLot.taArticle.libellecArticle, "+
								"f.qteLDocument, f.qte2LDocument, f.u1LDocument, f.u2LDocument, "+
								"f.taLot.dluo, f.taEntrepot.codeEntrepot, f.taEntrepot.libelle, f.taLot.taArticle.taFamille.libcFamille, "+
								
								/* Bon de Reception */
								"b.taDocument.codeDocument, b.taDocument.dateDocument, b.qteLDocument, b.u1LDocument, "+
								"b.taDocument.taTiers.nomTiers, b.taDocument.taTiers.codeTiers "+
							
							"from "+
								"TaLBonReception b, TaLFabrication f, TaLFabrication f2 join f.taDocument fab "+
							"where "+
								"f2.taLot.numLot = '"+numLotPF+"' "+
								"and f.taDocument.codeDocument = f2.taDocument.codeDocument "+
								"and f.taLot.numLot = b.taLot.numLot "+
								"and f.taLot.numLot != '"+numLotPF+"' "; //   
		   

		Query queryMP = entityManager.createQuery(qStringMP); 
//			Query queryPF = entityManager.createQuery(qStringPF);

		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		
		mp = queryMP.getResultList();
//			mpe = queryMP.getResultList();
//			pf = queryPF.getResultList();

//			for (Object[] tab : pf){
//				tab[0].toString();
//			}

		for (Object[] tab : mp){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMpNumLot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMpNumLot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMpCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMpCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMpLibelleArticle(tab[2].toString());
			} else {
				editionEtatTracabilite.setMpLibelleArticle(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMpQte1(LibConversion.stringToBigDecimal(tab[3].toString()));
			} else {
				editionEtatTracabilite.setMpQte1(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMpQte2(LibConversion.stringToBigDecimal(tab[4].toString()));
			} else {
				editionEtatTracabilite.setMpQte2(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMpUnite1(tab[5].toString());
			} else {
				editionEtatTracabilite.setMpUnite1(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMpUnite2(tab[6].toString());
			} else {
				editionEtatTracabilite.setMpUnite2(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMpDluo(new Date(((Timestamp)tab[7]).getTime()));
			} else {
				editionEtatTracabilite.setMpDluo(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setMpCodeEntrepot(tab[8].toString());
			} else {
				editionEtatTracabilite.setMpCodeEntrepot(null);
			}
			if (tab[9] != null){
				editionEtatTracabilite.setMpLibelleEntrepot(tab[9].toString());
			} else {
				editionEtatTracabilite.setMpNumLot(null);
			}
			if (tab[10] != null){
				editionEtatTracabilite.setMpFamille(tab[10].toString());
			} else {
				editionEtatTracabilite.setMpFamille(null);
			}
			if (tab[11] != null){
				editionEtatTracabilite.setBrNumReception(tab[11].toString());
			} else {
				editionEtatTracabilite.setBrNumReception(null);
			}
			if (tab[12] != null){
				editionEtatTracabilite.setBrDateReception(new Date(((java.sql.Date)tab[12]).getTime()));
//					editionEtatTracabilite.setBrDateReception(LibDate.stringToDateSql(tab[12].toString()));
			} else {
				editionEtatTracabilite.setBrDateReception(null);
			}
			if (tab[13] != null){
				editionEtatTracabilite.setBrQteReception(LibConversion.stringToBigDecimal(tab[13].toString()));
			} else {
				editionEtatTracabilite.setBrQteReception(null);
			}
			if (tab[14] != null){
				editionEtatTracabilite.setBrUniteReception(tab[14].toString());
			} else {
				editionEtatTracabilite.setBrUniteReception(null);
			}
			if (tab[15] != null){
				editionEtatTracabilite.setBrNomFournisseur(tab[15].toString());
			} else {
				editionEtatTracabilite.setBrNomFournisseur(null);
			}
			if (tab[16] != null){
				editionEtatTracabilite.setBrCodeFournisseur(tab[16].toString() );
			} else {
				editionEtatTracabilite.setBrCodeFournisseur(null);
			}	
			
			// NumLot de Produit Fini pour les PFs multiples
			editionEtatTracabilite.setPfNumLot(numLotPF);
			
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
//			return mpe;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
	}
	
	public List <EditionEtatTracabilite> editionPFL(String libellePF){
		List<Object[]> mp ;//= new ArrayList<Object>();
//		List<EditionEtatTracabilite> mpe;
//		List<Object[]> pf ;

		// TaArticle.libellecArticle
		
		logger.debug("getting EditionEtatTracabilite instance with numLot de Produit Finit : " + libellePF );
		String qStringMP = 	"select  "+
								/* Matière Première */
								"f.taLot.numLot, f.taLot.taArticle.codeArticle, f.taLot.taArticle.libellecArticle, "+
								"f.qteLDocument, f.qte2LDocument, f.u1LDocument, f.u2LDocument, "+
								"f.taLot.dluo, f.taEntrepot.codeEntrepot, f.taEntrepot.libelle, f.taLot.taArticle.taFamille.libcFamille, "+
								
								/* Bon de Reception */
								"b.taDocument.codeDocument, b.taDocument.dateDocument, b.qteLDocument, b.u1LDocument, "+
								"b.taDocument.taTiers.nomTiers, b.taDocument.taTiers.codeTiers "+
							
							"from "+
								"TaLBonReception b, TaLFabrication f, TaLFabrication f2 join f.taDocument fab "+
							"where "+
								"f2.taLot.taArticle.libellecArticle = '"+libellePF+"' "+
								"and f.taDocument.codeDocument = f2.taDocument.codeDocument "+
								"and f.taLot.numLot = b.taLot.numLot "+
								"and f.taLot.taArticle.libellecArticle != '"+libellePF+"' "; //   
		   

		Query queryMP = entityManager.createQuery(qStringMP); 
//			Query queryPF = entityManager.createQuery(qStringPF);

		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		
		mp = queryMP.getResultList();
//			mpe = queryMP.getResultList();
//			pf = queryPF.getResultList();

//			for (Object[] tab : pf){
//				tab[0].toString();
//			}

		for (Object[] tab : mp){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMpNumLot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMpNumLot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMpCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMpCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMpLibelleArticle(tab[2].toString());
			} else {
				editionEtatTracabilite.setMpLibelleArticle(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMpQte1(LibConversion.stringToBigDecimal(tab[3].toString()));
			} else {
				editionEtatTracabilite.setMpQte1(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMpQte2(LibConversion.stringToBigDecimal(tab[4].toString()));
			} else {
				editionEtatTracabilite.setMpQte2(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMpUnite1(tab[5].toString());
			} else {
				editionEtatTracabilite.setMpUnite1(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMpUnite2(tab[6].toString());
			} else {
				editionEtatTracabilite.setMpUnite2(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMpDluo(new Date(((Timestamp)tab[7]).getTime()));
			} else {
				editionEtatTracabilite.setMpDluo(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setMpCodeEntrepot(tab[8].toString());
			} else {
				editionEtatTracabilite.setMpCodeEntrepot(null);
			}
			if (tab[9] != null){
				editionEtatTracabilite.setMpLibelleEntrepot(tab[9].toString());
			} else {
				editionEtatTracabilite.setMpNumLot(null);
			}
			if (tab[10] != null){
				editionEtatTracabilite.setMpFamille(tab[10].toString());
			} else {
				editionEtatTracabilite.setMpFamille(null);
			}
			if (tab[11] != null){
				editionEtatTracabilite.setBrNumReception(tab[11].toString());
			} else {
				editionEtatTracabilite.setBrNumReception(null);
			}
			if (tab[12] != null){
				editionEtatTracabilite.setBrDateReception(new Date(((java.sql.Date)tab[12]).getTime()));
//					editionEtatTracabilite.setBrDateReception(LibDate.stringToDateSql(tab[12].toString()));
			} else {
				editionEtatTracabilite.setBrDateReception(null);
			}
			if (tab[13] != null){
				editionEtatTracabilite.setBrQteReception(LibConversion.stringToBigDecimal(tab[13].toString()));
			} else {
				editionEtatTracabilite.setBrQteReception(null);
			}
			if (tab[14] != null){
				editionEtatTracabilite.setBrUniteReception(tab[14].toString());
			} else {
				editionEtatTracabilite.setBrUniteReception(null);
			}
			if (tab[15] != null){
				editionEtatTracabilite.setBrNomFournisseur(tab[15].toString());
			} else {
				editionEtatTracabilite.setBrNomFournisseur(null);
			}
			if (tab[16] != null){
				editionEtatTracabilite.setBrCodeFournisseur(tab[16].toString() );
			} else {
				editionEtatTracabilite.setBrCodeFournisseur(null);
			}	
			
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
//			return mpe;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
	}
	
	public List <EditionEtatTracabilite> editionSG(Date dateD, Date dateF, String article , String famille, String entrepot ){ // Etat Stock Global
		List<Object[]> stock ;
		String where ="";
		logger.debug("getting EditionEtatTracabilite instance with Dates pour les Etats des Stocks Global : ");
		
		String qString = 	" select  case when e.code_entrepot is not null then e.code_entrepot else ''  end , a.code_article, l.numlot, a.libellec_article, " +
				"(select sum(s1.qte1_stock) from ta_mouvement_stock s1 where s1.date_Stock < '"+dateD+"' and s1.id_lot =s.id_lot and ((s1.id_entrepot is null and s.id_entrepot is null) or (s.id_entrepot=s1.id_entrepot))) as depart, "+
				"(select sum(s1.qte1_stock) from ta_mouvement_stock s1 where s1.qte1_stock>0 and s1.date_Stock between '"+dateD+"' and '"+dateF+"' and s1.id_lot =s.id_lot and ((s1.id_entrepot is null and s.id_entrepot is null) or (s.id_entrepot=s1.id_entrepot))) as entree, "+
				"(select sum(s1.qte1_stock) from ta_mouvement_stock s1 where s1.qte1_stock<=0 and s1.date_Stock between '"+dateD+"' and '"+dateF+"' and s1.id_lot =s.id_lot and ((s1.id_entrepot is null and s.id_entrepot is null) or (s.id_entrepot=s1.id_entrepot))) as sortie "+
				" ,f.code_famille "+
			" from "+ 
				"ta_mouvement_stock s "
				+ " left join ta_lot l on s.id_lot = l.id_lot "
				+ " join ta_article a on l.id_article = a.id_article "
				+ " left join ta_famille f on a.id_famille = f.id_famille "
				+ " left join ta_entrepot e on  e.id_entrepot = s.id_entrepot ";
//			if(famille!=null){
//				qString +=where+" f.code_famille like '"+famille+"'";
//				where=" where ";
//			}
//			if(article!=null){
//				qString +=where+" a.code_article like '"+article+"'";
//				where=" where ";
//			}
//			if(entrepot!=null){
//				qString +=where+" e.code_entrepot like '"+entrepot+"'";
//				where=" where ";
//			}
		//qString +=" where s.date_Stock between '"+dateD+"' and '"+dateF+"' ";
		qString +=" where a.gestion_Stock = true ";
			qString +=" group by f.code_famille,a.code_article,numlot,s.id_lot,e.code_entrepot,s.id_entrepot,a.libellec_article "+
			" order by f.code_famille,a.code_article,numlot,e.code_entrepot  ";
							
		Query query = entityManager.createNativeQuery(qString); 
		
		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		
		stock = query.getResultList();
		
		for (Object[] tab : stock){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMouvEntrepot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMouvEntrepot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMouvCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMouvCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMouvNumLot(tab[2].toString());
			} else {
				editionEtatTracabilite.setMouvNumLot(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMouvLibelle(tab[3].toString());
			} else {
				editionEtatTracabilite.setMouvLibelle(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMouvQteDepart(LibConversion.stringToBigDecimal(tab[4].toString()));
			} else {
				editionEtatTracabilite.setMouvQteDepart(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMouvQteEntree(LibConversion.stringToBigDecimal(tab[5].toString()));
			} else {
				editionEtatTracabilite.setMouvQteEntree(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMouvQteSortie(LibConversion.stringToBigDecimal(tab[6].toString()));
			} else {
				editionEtatTracabilite.setMouvQteSortie(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMouveTypeFamille(tab[7].toString());
			} else {
				editionEtatTracabilite.setMouveTypeFamille(null);
			}
//			if (tab[4] != null || tab[5] != null || tab[6] != null){
//				BigDecimal dis = new BigDecimal(0.0);
//				BigDecimal dep = editionEtatTracabilite.getMouvQteDepart();
//				dis.add(dep);
//				BigDecimal ent = editionEtatTracabilite.getMouvQteEntree();
//				dis.add(ent);
//				BigDecimal sor = editionEtatTracabilite.getMouvQteSortie();
//				dis.add(sor);
//				editionEtatTracabilite.setMouvQteDisponible(dis);
//			} else {
//				editionEtatTracabilite.setMouvQteDisponible(null);
//			}
				
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
		
//		return null; //tmp
	}

	public List <EditionEtatTracabilite> editionSG(Date dateD, Date dateF ){ // Etat Stock Global
		List<Object[]> stock ;
		String where ="";
		logger.debug("getting EditionEtatTracabilite instance with Dates pour les Etats des Stocks Global : ");
		
		String qString = 	" select e.code_entrepot, a.code_article, numlot, a.libellec_article, " +
				"(select sum(qte1_stock) from ta_mouvement_stock s1 where s1.date_Stock < '"+dateD+"' and id_lot =s.id_lot) as depart, "+
				"(select sum(qte1_stock) from ta_mouvement_stock s1 where qte1_stock>0 and s1.date_Stock between '"+dateD+"' and '"+dateF+"' and id_lot =s.id_lot) as entree, "+
				"(select sum(qte1_stock) from ta_mouvement_stock s1 where qte1_stock<=0 and s1.date_Stock between '"+dateD+"' and '"+dateF+"' and id_lot =s.id_lot) as sortie "+
				" ,f.code_famille "+
			" from "+ 
				"ta_mouvement_stock s "
				+ " left join ta_lot l on s.id_lot = l.id_lot "
				+ " join ta_article a on l.id_article = a.id_article "
				+ " left join ta_famille f on a.id_famille = f.id_famille "
				+ " left join ta_entrepot e on  e.id_entrepot = s.id_entrepot ";

		qString +=" where a.gestion_Stock = true ";
			qString +=" group by f.code_famille,a.code_article,numlot,s.id_lot,e.code_entrepot,a.libellec_article "+
			" order by f.code_famille,a.code_article,numlot,e.code_entrepot  ";
							
		Query query = entityManager.createNativeQuery(qString); 
		
		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		
		stock = query.getResultList();
		
		for (Object[] tab : stock){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMouvEntrepot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMouvEntrepot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMouvCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMouvCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMouvNumLot(tab[2].toString());
			} else {
				editionEtatTracabilite.setMouvNumLot(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMouvLibelle(tab[3].toString());
			} else {
				editionEtatTracabilite.setMouvLibelle(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMouvQteDepart(LibConversion.stringToBigDecimal(tab[4].toString()));
			} else {
				editionEtatTracabilite.setMouvQteDepart(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMouvQteEntree(LibConversion.stringToBigDecimal(tab[5].toString()));
			} else {
				editionEtatTracabilite.setMouvQteEntree(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMouvQteSortie(LibConversion.stringToBigDecimal(tab[6].toString()));
			} else {
				editionEtatTracabilite.setMouvQteSortie(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMouveTypeFamille(tab[7].toString());
			} else {
				editionEtatTracabilite.setMouveTypeFamille(null);
			}
//			if (tab[4] != null || tab[5] != null || tab[6] != null){
//				BigDecimal dis = new BigDecimal(0.0);
//				BigDecimal dep = editionEtatTracabilite.getMouvQteDepart();
//				dis.add(dep);
//				BigDecimal ent = editionEtatTracabilite.getMouvQteEntree();
//				dis.add(ent);
//				BigDecimal sor = editionEtatTracabilite.getMouvQteSortie();
//				dis.add(sor);
//				editionEtatTracabilite.setMouvQteDisponible(dis);
//			} else {
//				editionEtatTracabilite.setMouvQteDisponible(null);
//			}
				
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
		
//		return null; //tmp
	}
	public List <EditionEtatTracabilite> editionDMA(String codeArticle, Date dateD, Date dateF){ // Detail de Mouvement d'Article
		List<Object[]> stock ;
		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		int x = 10;
		
		logger.debug("getting EditionEtatTracabilite instance with Dates pour les Detail Mouvement d'Article : ");
		
		/*
		select  e.code_entrepot,a.code_article,s.num_lot,tm.libelle,a.libellec_article,gm.date_gr_mouv_stock,gm.code_gr_mouv_stock,gm.libelle_gr_mouv_stock,s.qte1_stock,s.un1_stock
		from ta_mouvement_stock s, ta_lot l,ta_article a, ta_entrepot e, ta_fabrication f, ta_gr_mouv_stock gm, ta_type_mouvement tm
		where
		s.date_stock between '01/01/2015' and '31/12/2015'
		and s.num_lot = l.numlot
		and l.id_article = a.id_article
		and e.id_entrepot = s.id_entrepot
		and gm.id_gr_mouv_stock = s.id_gr_mouv_stock
		and tm.id_type_mouvement = gm.id_type_mouv
		group by s.num_lot,a.code_article,e.code_entrepot,a.libellec_article,gm.date_gr_mouv_stock,gm.libelle_gr_mouv_stock,s.qte1_stock,s.un1_stock,tm.id_type_mouvement,gm.code_gr_mouv_stock
		order by a.code_article  
		*/
		
		String qString = 	" select  "+
								"e.code_entrepot,a.code_article,l.numlot,tm.libelle as libelleType,a.libellec_article,gm.date_gr_mouv_stock,"+
								"gm.code_gr_mouv_stock,l.libelle as libelleLot,s.qte1_stock,s.un1_stock,s.qte2_stock,s.un2_stock "+
							" from "+ 
								"ta_mouvement_stock s "
								+ " left join ta_lot l on s.id_lot = l.id_lot "
								+ " join ta_article a on l.id_article = a.id_article "
								+ " left join ta_entrepot e on e.id_entrepot = s.id_entrepot "
								+ " join ta_gr_mouv_stock gm on gm.id_gr_mouv_stock = s.id_gr_mouv_stock "
								+ " join ta_type_mouvement tm on tm.id_type_mouvement = gm.id_type_mouv "+
							" where "+
								"s.date_stock between '"+dateD+"' and '"+dateF+"' "+
								"and a.code_article like '"+codeArticle+"' "+
//							" group by "+
//								"s.num_lot,a.code_article,e.code_entrepot,a.libellec_article,gm.date_gr_mouv_stock,l.libelle,s.qte1_stock,"+
//								"s.un1_stock,s.qte2_stock,s.un2_stock,tm.id_type_mouvement,gm.code_gr_mouv_stock "+
							" order by "+
								"gm.date_gr_mouv_stock,l.numlot,tm.libelle,gm.code_gr_mouv_stock ";
							
		Query query = entityManager.createNativeQuery(qString); 
		
		stock = query.getResultList();
//		System.out.println(" -------------------- debug ------------------------------");
//		x++;
//		System.out.println("------------debug ------------ x : "+x);
		for (Object[] tab : stock){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMouvEntrepot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMouvEntrepot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMouvCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMouvCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMouvNumLot(tab[2].toString());
			} else {
				editionEtatTracabilite.setMouvNumLot(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMouveTypeFamille(tab[3].toString());
			} else {
				editionEtatTracabilite.setMouveTypeFamille(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMouvLibelleArticle(tab[4].toString());
			} else {
				editionEtatTracabilite.setMouvLibelleArticle(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMouveDate(new Date(((java.sql.Date)tab[5]).getTime()));
			} else {
				editionEtatTracabilite.setMouveDate(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMouveCodeDocument(tab[6].toString());
			} else {
				editionEtatTracabilite.setMouveCodeDocument(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMouvLibelle(tab[7].toString());
			} else {
				editionEtatTracabilite.setMouvLibelle(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setMouveQte1(LibConversion.stringToBigDecimal(tab[8].toString()));
			} else {
				editionEtatTracabilite.setMouveQte1(null);
			}
			if (tab[9] != null){
				editionEtatTracabilite.setMouveUnite1(tab[9].toString());
			} else {
				editionEtatTracabilite.setMouveUnite1(null);
			}
			if (tab[10] != null){
				editionEtatTracabilite.setMouveQte2(LibConversion.stringToBigDecimal(tab[10].toString()));
			} else {
				editionEtatTracabilite.setMouveQte2(null);
			}
			if (tab[11] != null){
				editionEtatTracabilite.setMouveUnite2(tab[11].toString());
			} else {
				editionEtatTracabilite.setMouveUnite2(null);
			}				
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
		
//		return null; //tmp
	}

	public List <EditionEtatTracabilite> editionDMAtous(String codeArticle, Date dateD, Date dateF){ // Detail de Mouvement d'Article
		List<Object[]> stock ;
		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		int x = 10;
		
		logger.debug("getting EditionEtatTracabilite instance with Dates pour le Detail des Mouvements de tous les Articles : ");

		
		String qString = 	" select "+
								"e.code_entrepot,a.code_article,l.numlot,tm.libelle as libelleType,a.libellec_article,gm.date_gr_mouv_stock,"+
								"gm.code_gr_mouv_stock,l.libelle as libelleLot,s.qte1_stock,s.un1_stock,s.qte2_stock,s.un2_stock "+
							" from "+ 
								"ta_mouvement_stock s "
								+ " left join ta_lot l on s.id_lot = l.id_lot "
								+ " left join ta_article a on l.id_article = a.id_article "
								+ " left join ta_entrepot e on e.id_entrepot = s.id_entrepot "
								+ " left join ta_gr_mouv_stock gm on gm.id_gr_mouv_stock = s.id_gr_mouv_stock "
								+ " left join ta_type_mouvement tm on tm.id_type_mouvement = gm.id_type_mouv "+
							" where "+
								"s.date_stock between '"+dateD+"' and '"+dateF+"' "+
//							" group by "+
//								"s.num_lot,a.code_article,e.code_entrepot,a.libellec_article,gm.date_gr_mouv_stock,l.libelle,s.qte1_stock,"+
//								"s.un1_stock,s.qte2_stock,s.un2_stock,tm.id_type_mouvement,gm.code_gr_mouv_stock "+
							" order by "+
								"gm.date_gr_mouv_stock,a.code_article,l.numlot,tm.libelle ";
							
		System.out.println("Requete :"+qString);
		
		Query query = entityManager.createNativeQuery(qString); 
		
		stock = query.getResultList();
		System.out.println("nb :"+stock.size());
		
//		x++;
//		System.out.println("------------debug ------------ x : "+x);
		for (Object[] tab : stock){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMouvEntrepot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMouvEntrepot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMouvCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMouvCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMouvNumLot(tab[2].toString());
			} else {
				editionEtatTracabilite.setMouvNumLot(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMouveTypeFamille(tab[3].toString());
			} else {
				editionEtatTracabilite.setMouveTypeFamille(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMouvLibelleArticle(tab[4].toString());
			} else {
				editionEtatTracabilite.setMouvLibelleArticle(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMouveDate(new Date(((java.sql.Date)tab[5]).getTime()));
			} else {
				editionEtatTracabilite.setMouveDate(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMouveCodeDocument(tab[6].toString());
			} else {
				editionEtatTracabilite.setMouveCodeDocument(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMouvLibelle(tab[7].toString());
			} else {
				editionEtatTracabilite.setMouvLibelle(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setMouveQte1(LibConversion.stringToBigDecimal(tab[8].toString()));
			} else {
				editionEtatTracabilite.setMouveQte1(null);
			}
			if (tab[9] != null){
				editionEtatTracabilite.setMouveUnite1(tab[9].toString());
			} else {
				editionEtatTracabilite.setMouveUnite1(null);
			}
			if (tab[10] != null){
				editionEtatTracabilite.setMouveQte2(LibConversion.stringToBigDecimal(tab[10].toString()));
			} else {
				editionEtatTracabilite.setMouveQte2(null);
			}
			if (tab[11] != null){
				editionEtatTracabilite.setMouveUnite2(tab[11].toString());
			} else {
				editionEtatTracabilite.setMouveUnite2(null);
			}				
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
		
//		return null; //tmp
	}
	public List <EditionEtatTracabilite> editionDML(String numLot, Date dateD, Date dateF){ // Detail de Mouvement d'Article
		List<Object[]> stock ;
		List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
		int x = 10;
		
		logger.debug("getting EditionEtatTracabilite instance with Dates pour les Détail des Mouvements d'un Lot : ");
		
		/*
		select  e.code_entrepot,a.code_article,s.num_lot,tm.libelle,a.libellec_article,gm.date_gr_mouv_stock,gm.code_gr_mouv_stock,gm.libelle_gr_mouv_stock,s.qte1_stock,s.un1_stock
		from ta_mouvement_stock s, ta_lot l,ta_article a, ta_entrepot e, ta_fabrication f, ta_gr_mouv_stock gm, ta_type_mouvement tm
		where
		s.date_stock between '01/01/2015' and '31/12/2015'
		and s.num_lot = l.numlot
		and l.id_article = a.id_article
		and e.id_entrepot = s.id_entrepot
		and gm.id_gr_mouv_stock = s.id_gr_mouv_stock
		and tm.id_type_mouvement = gm.id_type_mouv
		group by s.num_lot,a.code_article,e.code_entrepot,a.libellec_article,gm.date_gr_mouv_stock,gm.libelle_gr_mouv_stock,s.qte1_stock,s.un1_stock,tm.id_type_mouvement,gm.code_gr_mouv_stock
		order by a.code_article  
		*/
		
		String qString = 	"select  "+
								"e.code_entrepot,a.code_article,l.numlot,tm.libelle,a.libellec_article,gm.date_gr_mouv_stock,"+
								"gm.code_gr_mouv_stock,l.libelle as libelleLot,s.qte1_stock,s.un1_stock,s.qte2_stock,s.un2_stock "+
							" from "+ 
								"ta_mouvement_stock s "
								+ " join ta_lot l on s.id_lot = l.id_lot "
								+ " join ta_article a on l.id_article = a.id_article "
								+ " left join ta_entrepot e on e.id_entrepot = s.id_entrepot "
								+ " join ta_gr_mouv_stock gm on gm.id_gr_mouv_stock = s.id_gr_mouv_stock "
								+ " join ta_type_mouvement tm on tm.id_type_mouvement = gm.id_type_mouv "+
							" where "+
								"s.date_stock between '"+dateD+"' and '"+dateF+"' "+
								"and l.numlot = '"+numLot+"' "+
//							"group by "+
//								"l.numlot,a.code_article,e.code_entrepot,a.libellec_article,gm.date_gr_mouv_stock,tm.libelle,s.qte1_stock,"+
//								"s.un1_stock,s.qte2_stock,s.un2_stock,tm.id_type_mouvement,gm.code_gr_mouv_stock,l.libelle "+
							" order by "+
								"gm.date_gr_mouv_stock,tm.libelle,gm.code_gr_mouv_stock ";
							
		Query query = entityManager.createNativeQuery(qString); 
		
		stock = query.getResultList();
//		System.out.println(" -------------------- debug ------------------------------");
//		x++;
//		System.out.println("------------debug ------------ x : "+x);
		for (Object[] tab : stock){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setMouvEntrepot(tab[0].toString());
			} else {
				editionEtatTracabilite.setMouvEntrepot(null);
			}
			if (tab[1] != null){
				editionEtatTracabilite.setMouvCodeArticle(tab[1].toString());
			} else {
				editionEtatTracabilite.setMouvCodeArticle(null);
			}
			if (tab[2] != null){
				editionEtatTracabilite.setMouvNumLot(tab[2].toString());
			} else {
				editionEtatTracabilite.setMouvNumLot(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setMouveTypeFamille(tab[3].toString());
			} else {
				editionEtatTracabilite.setMouveTypeFamille(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setMouvLibelleArticle(tab[4].toString());
			} else {
				editionEtatTracabilite.setMouvLibelleArticle(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setMouveDate(new Date(((java.sql.Date)tab[5]).getTime()));
			} else {
				editionEtatTracabilite.setMouveDate(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setMouveCodeDocument(tab[6].toString());
			} else {
				editionEtatTracabilite.setMouveCodeDocument(null);
			}
			if (tab[7] != null){
				editionEtatTracabilite.setMouvLibelle(tab[7].toString());
			} else {
				editionEtatTracabilite.setMouvLibelle(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setMouveQte1(LibConversion.stringToBigDecimal(tab[8].toString()));
			} else {
				editionEtatTracabilite.setMouveQte1(null);
			}
			if (tab[9] != null){
				editionEtatTracabilite.setMouveUnite1(tab[9].toString());
			} else {
				editionEtatTracabilite.setMouveUnite1(null);
			}
			if (tab[10] != null){
				editionEtatTracabilite.setMouveQte2(LibConversion.stringToBigDecimal(tab[10].toString()));
			} else {
				editionEtatTracabilite.setMouveQte2(null);
			}
			if (tab[11] != null){
				editionEtatTracabilite.setMouveUnite2(tab[11].toString());
			} else {
				editionEtatTracabilite.setMouveUnite2(null);
			}
				
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
		
//		return null; //tmp
	}
	
	public List <EditionEtatTracabilite> editionNonConforme(Date dateD, Date dateF){ // Etat des Non-Conformites
		List<Object[]> stock ;
		List<EditionEtatTracabilite> r = new LinkedList<EditionEtatTracabilite>();
		//int x = 10;
		
		logger.debug("getting EditionEtatTracabilite instance Non-Conforemes : ");
		
		/*  // version complette
		set schema 'demo'; 
		select  
		  tb.expression_verifiee, tb.action_corrective, tb.forcage, tc.valeur_defaut, tc.deuxieme_valeur, 
		  tc.libelle_conformite, tr.valeur_constatee, trc.observation, trc.effectuee, trc.valide, tl.numlot, 
		  tl.dluo, ta.code_article, ta.libellec_article, trc.quand_modif, tr.quand_cree, tg.code_groupe, tg.libelle 
		from 
		  ta_bareme tb, ta_conformite tc, ta_groupe tg,
		  ta_resultat_conformite tr, ta_resultat_correction trc, 
		  ta_article ta, ta_lot tl  
		where 
		  tb.id_conformite = tc.id_conformite and
		  tc.id_conformite = tr.id_conformite and
		  tg.id_groupe = tc.id_groupe and 
		  tr.id_resultat_conformite = trc.id_resultat_conformite and
		  tr.id_lot = tl.id_lot and
		  ta.id_article = tl.id_article  
		order by 
		  trc.quand_modif
		*/ //18 elements (dont 7 obligatoires et 1 optionel)
		
		String qString = 	
				"select  "+
							"tl.numlot, br.date_document, ta.code_article, tg.libelle, tc.libelle_conformite, tr.valeur_constatee, tb.action_corrective,tb.expression_verifiee,tb.forcage, "
							+ "trc.effectuee,trc.observation,tl.libelle as libelleLot,'2' as typeDocument,tdoc.code_t_reception,br.code_Document ,lbr.qte_L_Document,lbr.u1_L_Document"+
							" from "+ 
								" ta_bareme tb "
								+ " join ta_conformite tc on tb.id_conformite = tc.id_conformite "
								+ " join ta_resultat_conformite tr on tc.id_conformite = tr.id_conformite "
								+ " left join ta_resultat_correction trc on tr.id_resultat_conformite = trc.id_resultat_conformite "
								+ " join ta_lot tl on tr.id_lot = tl.id_lot "
								+ " left join ta_groupe tg on tg.id_groupe = tc.id_groupe "
								+ " join ta_article ta on ta.id_article = tl.id_article "
								+ " join ta_l_bon_reception lbr on lbr.id_lot=tl.id_lot "
								+ " join ta_bon_reception br on br.id_document=lbr.id_document "
								+ " join ta_t_reception tdoc on tdoc.id_t_reception=br.id_t_reception "+
							" where "+
								" br.date_document  between :dateD and :dateF"
							
								+ " union all"
							
							+" select  "+
							"tl.numlot, br.date_document, ta.code_article, tg.libelle, tc.libelle_conformite, tr.valeur_constatee, tb.action_corrective,tb.expression_verifiee,tb.forcage,"
							+ " trc.effectuee,trc.observation,tl.libelle as libelleLot,'1' as typeDocument,tdoc.code_t_fabrication,br.code_Document ,lbr.qte_l_document,lbr.u1_l_document "+
						" from "+ 
							" ta_bareme tb "
							+ " join ta_conformite tc on tb.id_conformite = tc.id_conformite "
							+ " join ta_resultat_conformite tr on tc.id_conformite = tr.id_conformite "
							+ " left join ta_resultat_correction trc on tr.id_resultat_conformite = trc.id_resultat_conformite "
							+ " join ta_lot tl on tr.id_lot = tl.id_lot "
							+ " left join ta_groupe tg on tg.id_groupe = tc.id_groupe "
							+ " join ta_article ta on ta.id_article = tl.id_article "
							+ " join ta_l_fabrication_pf lbr on lbr.id_lot=tl.id_lot "
							+ " join ta_fabrication br on br.id_document=lbr.id_document "
							+ " join ta_t_fabrication tdoc on tdoc.id_t_fabrication=br.id_t_fabrication "+
						" where "+
							" br.date_document  between :dateD and :dateF"
								
							+ " union all"
							
							+" select  "+
							"tl.numlot, br.date_document, ta.code_article, tg.libelle, tc.libelle_conformite, tr.valeur_constatee, tb.action_corrective,tb.expression_verifiee,tb.forcage,"
							+ " trc.effectuee,trc.observation,tl.libelle as libelleLot,'1' as typeDocument,tdoc.code_t_fabrication,br.code_Document ,lbr.qte_l_document,lbr.u1_l_document "+
						" from "+ 
							" ta_bareme tb "
							+ " join ta_conformite tc on tb.id_conformite = tc.id_conformite "
							+ " join ta_resultat_conformite tr on tc.id_conformite = tr.id_conformite "
							+ " left join ta_resultat_correction trc on tr.id_resultat_conformite = trc.id_resultat_conformite "
							+ " join ta_lot tl on tr.id_lot = tl.id_lot "
							+ " left join ta_groupe tg on tg.id_groupe = tc.id_groupe "
							+ " join ta_article ta on ta.id_article = tl.id_article "
							+ " join ta_l_fabrication_mp lbr on lbr.id_lot=tl.id_lot "
							+ " join ta_fabrication br on br.id_document=lbr.id_document "
							+ " join ta_t_fabrication tdoc on tdoc.id_t_fabrication=br.id_t_fabrication "+
						" where "+
							" br.date_document  between :dateD and :dateF" 
							+"order by "+
								" typeDocument ,date_document,code_article,numlot ";
							
		Query query = entityManager.createNativeQuery(qString); 
		
		query.setParameter("dateD", dateD,TemporalType.DATE);
		query.setParameter("dateF", dateF,TemporalType.DATE);
		query.setParameter("dateD", dateD,TemporalType.DATE);
		query.setParameter("dateF", dateF,TemporalType.DATE);
		query.setParameter("dateD", dateD,TemporalType.DATE);
		query.setParameter("dateF", dateF,TemporalType.DATE);		
		stock = query.getResultList();
//		System.out.println(" -------------------- debug ------------------------------");
//		x++;
//		System.out.println("------------debug ------------ x : "+x);
		for (Object[] tab : stock){
			EditionEtatTracabilite editionEtatTracabilite = new EditionEtatTracabilite();
			
			if (tab[0] != null){
				editionEtatTracabilite.setNumLot(tab[0].toString());
			} else {
				editionEtatTracabilite.setNumLot(null);
			} 
			if (tab[1] != null){
				
				editionEtatTracabilite.setDateModif(new Date(((Date)tab[1]).getTime()));
//				editionEtatTracabilite.setDateModif(LibDate.);
			} else {
				editionEtatTracabilite.setDateModif(new Date());
			}
			if (tab[2] != null){
				editionEtatTracabilite.setCodeArticle(tab[2].toString());
			} else {
				editionEtatTracabilite.setCodeArticle(null);
			}
			if (tab[3] != null){
				editionEtatTracabilite.setLibelleGroupe(tab[3].toString());
			} else {
				editionEtatTracabilite.setLibelleGroupe(null);
			}
			if (tab[4] != null){
				editionEtatTracabilite.setLibelleConformite(tab[4].toString());
			} else {
				editionEtatTracabilite.setLibelleConformite(null);
			}
			if (tab[5] != null){
				editionEtatTracabilite.setResultat(tab[5].toString());
			} else {
				editionEtatTracabilite.setResultat(null);
			}
			if (tab[6] != null){
				editionEtatTracabilite.setActionCorrective(tab[6].toString());
			} else {
				editionEtatTracabilite.setActionCorrective(null);
			}
			
			if (tab[7] != null){
				editionEtatTracabilite.setExpressionVerifiee(tab[7].toString());
			} else {
				editionEtatTracabilite.setExpressionVerifiee(null);
			}
			if (tab[8] != null){
				editionEtatTracabilite.setForcage(LibConversion.StringToBoolean(tab[8].toString()));
			} else {
				editionEtatTracabilite.setForcage(false);
			}
			
			
			if (tab[9] != null){
				editionEtatTracabilite.setEffectuee(LibConversion.StringToBoolean(tab[9].toString()));
			} else {
				editionEtatTracabilite.setEffectuee(false);
			}
			
			if (tab[10] != null){
				editionEtatTracabilite.setObservation(tab[10].toString());
			} else {
				editionEtatTracabilite.setObservation(null);
			}
			
			
			if (tab[11] != null){
				editionEtatTracabilite.setLibelleLot(tab[11].toString());
			} else {
				editionEtatTracabilite.setLibelleLot(null);
			}
			if (tab[12] != null){
				if(tab[12].toString().equals("1"))editionEtatTracabilite.setTypeDocument("Fabrication");
				else  if(tab[12].toString().equals("2"))	editionEtatTracabilite.setTypeDocument("Réception");
			} else {
				editionEtatTracabilite.setTypeDocument(null);
			}			
			if (tab[13] != null){
				editionEtatTracabilite.setCodeTDocument(tab[13].toString());
			} else {
				editionEtatTracabilite.setCodeTDocument(null);
			}			
			if (tab[14] != null){
				editionEtatTracabilite.setCodeDocument(tab[14].toString());
			} else {
				editionEtatTracabilite.setCodeDocument(null);
			}
			if (tab[15] != null){
				editionEtatTracabilite.setQte1(LibConversion.stringToBigDecimal(tab[15].toString()));
			} else {
				editionEtatTracabilite.setQte1(null);
			}
			if (tab[16] != null){
				editionEtatTracabilite.setU1(tab[16].toString());
			} else {
				editionEtatTracabilite.setU1(null);
			}			
			
			r.add(editionEtatTracabilite);
		}
		
		logger.debug("get successful");
		return r;
		
//		return null; //tmp
	}
	
}

