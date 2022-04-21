package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaBoncdeAchatDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.dto.TaPreparationDTO;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.ILigneALigneDAO;


/**
 * Home object for domain model class TaLigneALigne.
 * @see fr.legrain.documents.dao.TaLigneALigne
 * @author Hibernate Tools
 */
public class TaLigneALigneDAO implements ILigneALigneDAO /*extenligneDocument.getTaDocument() AbstractApplicationDAO<TaLigneALigne>*/ {

//	private static final Log log = LogFactory.getLog(TaLigneALigneDAO.class);
	static Logger logger = Logger.getLogger(TaLigneALigneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLigneALigne a";
	
	public TaLigneALigneDAO(){
//		this(null);
	}
	
	public void persist(TaLigneALigne transientInstance) {
		logger.debug("persisting TaLigneALigne instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLigneALigne persistentInstance) {
		logger.debug("removing TaLigneALigne instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLigneALigne merge(TaLigneALigne detachedInstance) {
		logger.debug("merging TaLigneALigne instance");
		try {
			TaLigneALigne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLigneALigne findById(int id) {
		logger.debug("getting TaLigneALigne instance with id: " + id);
		try {
			TaLigneALigne instance = entityManager.find(TaLigneALigne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLigneALigne findByIdGenere(TaLigneALigne taDocumentGenere) {
		logger.debug("getting findByIdGenere instance with : " + taDocumentGenere);
		try {
			Query ejbQuery = entityManager.createQuery("select r from TaLigneALigne r where r.taDocumentGenere=:taDocumentGenere");
			ejbQuery.setParameter("taDocumentGenere", taDocumentGenere);
			Object instance =  ejbQuery.getSingleResult();
			logger.debug("get successful");
			if(instance==null)return null;
			return (TaLigneALigne) instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public Boolean exist(TaLigneALigne taDocumentGenere) {
		logger.debug("exist with taDocumentGenere: " + taDocumentGenere);
		try {
			if(taDocumentGenere!=null ){
			Query ejbQuery = entityManager.createQuery("select count(f) from TaLigneALigne f where f.taDocumentGenere= :taDocumentGenere");
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
	
	public List<TaLigneALigne> findByLDocument(TaLFlash ligneDocument) {
		logger.debug("getting findByLDocument instance with : " + ligneDocument);
		try {
			String requeteFixeRDocument = null;
			if(ligneDocument.getTaFlash().getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument="  where r.taLFlash=:ligneDocument";

			if(requeteFixeRDocument!=null) {
				Query ejbQuery = entityManager.createQuery("select r from TaLigneALigne r "+requeteFixeRDocument);
				ejbQuery.setParameter("ligneDocument", ligneDocument);
				List<TaLigneALigne> l =  ejbQuery.getResultList();

				logger.debug("get successful");
				if(l==null)return null;
				return  l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLigneALigne> findByLDocument(ILigneDocumentTiers ligneDocument) {
		logger.debug("getting findByLDocument instance with : " + ligneDocument);
		try {
			String requeteFixeRDocument = null;
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" where r.taLAcompte=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument="  where r.taLAvoir=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument="  where r.taLAvisEcheance=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument="  where r.taLApporteur=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument="  where r.taLBoncde=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument="  where r.taLBoncdeAchat=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument="  where r.taLBonReception=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument="  where r.taLBonliv=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument="  where r.taLDevis=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument="  where r.taLFacture=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument="  where r.taLPrelevement=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument="  where r.taLProforma=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument="  where r.taLTicketDeCaisse=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument="  where r.taLPreparation=:ligneDocument";
			if(ligneDocument.getTaDocument().getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument="  where r.taLPanier=:ligneDocument";

			if(requeteFixeRDocument!=null) {
				Query ejbQuery = entityManager.createQuery("select r from TaLigneALigne r "+requeteFixeRDocument);
				ejbQuery.setParameter("ligneDocument", ligneDocument);
				List<TaLigneALigne> l =  ejbQuery.getResultList();

				logger.debug("get successful");
				if(l==null)return null;
				return  l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLigneALigne> selectAll() {
		logger.debug("selectAll TaLigneALigne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLigneALigne> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	  @Override
	public List<TaLigneALigne> selectAll(ILigneDocumentTiers taLDocument, Date dateDeb, Date dateFin) {
		logger.debug("selectAll TaLigneALigne");
		try {
			String requeteDebut="select l from TaLigneALigne l ";
			String requeteRajout="";
			List<TaLigneALigne> l=null;
			if(taLDocument !=null) {
			switch (taLDocument.getTaDocument().getTypeDocument()) {
			case TaFacture.TYPE_DOC:
				requeteRajout=" where l.taLFacture = :ligne ";
				break;
			case TaDevis.TYPE_DOC:
				requeteRajout=" where l.taLDevis = :ligne ";
				break;
			case TaBonliv.TYPE_DOC:
				requeteRajout=" where l.taLBonliv = :ligne ";
				break;
			case TaBoncde.TYPE_DOC:
				requeteRajout=" where l.taLBoncde = :ligne ";
				break;
			case TaBonReception.TYPE_DOC:
				requeteRajout=" where l.taLBonReception = :ligne ";
				break;
			case TaBoncdeAchat.TYPE_DOC:
				requeteRajout=" where l.taLBoncdeAchat = :ligne ";
				break;
			case TaTicketDeCaisse.TYPE_DOC:
				requeteRajout=" where l.taLTicketDeCaisse = :ligne ";
				break;
			case TaProforma.TYPE_DOC:
				requeteRajout=" where l.taLProforma = :ligne ";
				break;
			case TaAvoir.TYPE_DOC:
				requeteRajout=" where l.taLAvoir = :ligne ";
				break;
			case TaApporteur.TYPE_DOC:
				requeteRajout=" where l.taLApporteur = :ligne ";
				break;
			case TaAcompte.TYPE_DOC:
				requeteRajout=" where l.taLAcompte = :ligne ";
				break;
			case TaPrelevement.TYPE_DOC:
				requeteRajout=" where l.taLPrelevement = :ligne ";
				break;
			case TaAvisEcheance.TYPE_DOC:
				requeteRajout=" where l.taLAvisEcheance = :ligne ";
				break;
			default:
				break;
			}
			Query ejbQuery = entityManager.createQuery(requeteDebut+requeteRajout);
			ejbQuery.setParameter("ligne", taLDocument);
			l = ejbQuery.getResultList();
			}
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
						"from TaLigneALigne r " +
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
						"from TaLigneALigne r " +
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
						"from TaLigneALigne r " +
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
	public TaLigneALigne findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaLigneALigne value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaLigneALigne value, String propertyName)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaLigneALigne transientInstance) {
		// TODO Auto-generated method stub
		
	}
	public List<TaLigneALigne> dejaGenere(String requete){
		List<TaLigneALigne> result;
		try {
			Query query = entityManager.createQuery(requete);

			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
		}
		return null;
	}
	
	public List<IDocumentTiers> dejaGenereDocument(String requete){
		List<IDocumentTiers> result;
		try {
			Query query = entityManager.createQuery(requete);

			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
		}
		return null;
	}
	
	public List<TaLigneALigneDTO> dejaGenereLigneDocument(String requete){
		List<TaLigneALigneDTO> result;
		try {
			Query query = entityManager.createQuery(requete);

			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
		}
		return null;
	}
	
//	@Override
//	public List<TaLigneALigneDTO>  recupListeLienLigneALigneInt(IDocumentTiers persistentInstance) {
//		String requete = "";
//		List<Object> result = null;
//		List<TaLigneALigneDTO> l=new LinkedList<TaLigneALigneDTO>();
//		try {
//			requete = "select "
//					+ " case  "
//					+ " when pf.id_document is not null then pf.id_document "
//					+ " when f.id_document is not null then f.id_document "
//					+ " end as idDoc, "
//					+ " case"
//					+ " when pf.id_document is not null then '"+TaProforma.TYPE_DOC+"' "
//					+ " when f.id_document is not null then '"+TaFacture.TYPE_DOC+"' "
//					+ " end as typeDoc"
//					+ " "
//					+ " from ta_ligne_a_ligne ll " + 
//					" join ta_l_devis ld on ll.id_l_devis= ld.id_l_document " + 
//					" join ta_devis d on d.id_document=ld.id_document " +
//					
//				" left join ta_l_facture lf on ll.id_l_facture= lf.id_l_document " + 
//				" left join ta_facture f on f.id_document=lf.id_document " + 
//
//				" left join ta_l_proforma lpf on ll.id_l_proforma= lpf.id_l_document " + 
//				" left join ta_proforma pf on pf.id_document=lpf.id_document " + 
//
//				" where d.id_document=:idDoc";
//			Query query = entityManager.createNativeQuery(requete);
//			query.setParameter("idDoc", persistentInstance.getIdDocument());
//
//			result = query.getResultList();
//			for (Object object : result) {
//				TaLigneALigneDTO dto=new TaLigneALigneDTO();
//				dto.setIdDocumentDest((Integer) ((Object[])object)[0]);
//				dto.setTypeDocument((String) ((Object[])object)[1]);
//				l.add(dto);
//			}
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//		}
//		return l;
//	}

	public List<IDocumentDTO> recupIdDocumentSrc() {
		try {
			String requete="	select case  " + 
					"	when id_ligne_src=id_l_acompte then '"+TaAcompte.TYPE_DOC  +"'" +
					"	when id_ligne_src=id_l_apporteur then '" +TaApporteur.TYPE_DOC +"'" + 
					"	when id_ligne_src=id_l_avoir then '" +TaAvoir.TYPE_DOC +"'" + 
					"	when id_ligne_src=id_l_devis then '" + TaDevis.TYPE_DOC +"'" +
					"	when id_ligne_src=id_l_facture then  '" +TaFacture.TYPE_DOC +"'" + 
					"	when id_ligne_src=id_l_avis_echeance then  '" +TaAvisEcheance.TYPE_DOC +"'" + 
					"	when id_ligne_src=id_l_bonliv then  '" + TaBonliv.TYPE_DOC +"'" +
					"	when id_ligne_src=id_l_boncde then  '" +TaBoncde.TYPE_DOC  +"'" +
					"	when id_ligne_src=id_l_boncde_achat then '" + TaBoncdeAchat.TYPE_DOC +"'" +
					"	when id_ligne_src=id_l_proforma then  '" + TaProforma.TYPE_DOC +"'" +
					"	when id_ligne_src=id_l_prelevement then '" + TaPrelevement.TYPE_DOC +"'" +
					"	when id_ligne_src=id_l_preparation then  '" + TaPreparation.TYPE_DOC +"'" +
					"	when id_ligne_src=id_l_ticket_caisse then '" + TaTicketDeCaisse.TYPE_DOC +"'" +
					"	else 'NC'" + 
					"	end   as type," + 
					" case " + 
					" when id_ligne_src=id_l_acompte then (select id_document from ta_l_acompte lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_apporteur then (select id_document from ta_l_apporteur lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_avoir then (select id_document from ta_l_avoir lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_devis then (select id_document from ta_l_devis lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_facture then (select id_document from ta_l_facture lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_avis_echeance then (select id_document from ta_l_avis_echeance lf where lf.id_l_document=id_ligne_src) " + 
					" when id_ligne_src=id_l_bonliv then  (select id_document from ta_l_bonliv lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_boncde then  (select id_document from ta_l_boncde lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_boncde_achat then (select id_document from ta_l_boncde_achat lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_proforma then (select id_document from ta_l_proforma lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_prelevement then (select id_document from ta_l_prelevement lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_preparation then (select id_document from ta_l_preparation lf where lf.id_l_document=id_ligne_src)" + 
					" when id_ligne_src=id_l_ticket_caisse then (select id_document from ta_l_ticket_caisse lf where lf.id_l_document=id_ligne_src)" + 
					" end" + 
					" as id_doc" + 
					" from ta_ligne_a_ligne ll  ";
			List<IDocumentDTO> ll=new LinkedList<>();

				Query ejbQuery = entityManager.createNativeQuery(requete);

				List<Object[]> l =  ejbQuery.getResultList();
				String typeDoc;
				for (Object object : l) {
					typeDoc=(String) ((Object[])object)[0];
					if(typeDoc.equals(TaAcompte.TYPE_DOC)) {
						TaAcompteDTO dto = new TaAcompteDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaApporteur.TYPE_DOC)) {
						TaApporteurDTO dto = new TaApporteurDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaAvoir.TYPE_DOC)) {
						TaAvoirDTO dto = new TaAvoirDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaDevis.TYPE_DOC)) {
						TaDevisDTO dto = new TaDevisDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaFacture.TYPE_DOC)) {
						TaFactureDTO dto = new TaFactureDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaAvisEcheance.TYPE_DOC)) {
						TaAvisEcheanceDTO dto = new TaAvisEcheanceDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaBonliv.TYPE_DOC)) {
						TaBonlivDTO dto = new TaBonlivDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaBoncde.TYPE_DOC)) {
						TaBoncdeDTO dto = new TaBoncdeDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaBoncdeAchat.TYPE_DOC)) {
						TaBoncdeAchatDTO dto = new TaBoncdeAchatDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaProforma.TYPE_DOC)) {
						TaProformaDTO dto = new TaProformaDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaPrelevement.TYPE_DOC)) {
						TaPrelevementDTO dto = new TaPrelevementDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaPreparation.TYPE_DOC)) {
						TaPreparationDTO dto = new TaPreparationDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
					if(typeDoc.equals(TaTicketDeCaisse.TYPE_DOC)) {
						TaTicketDeCaisseDTO dto = new TaTicketDeCaisseDTO();
						dto.setId((Integer) ((Object[])object)[1]);
						ll.add(dto);
					}
				}

					
				logger.debug("get successful");

				

			return ll;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

}
