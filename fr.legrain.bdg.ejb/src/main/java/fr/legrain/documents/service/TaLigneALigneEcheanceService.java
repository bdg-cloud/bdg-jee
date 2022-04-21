package fr.legrain.documents.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
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
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.ILigneALigneDAO;
import fr.legrain.documents.dao.ILigneALigneEcheanceDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLigneALigneEcheanceService extends AbstractApplicationDAOServer<TaLigneALigneEcheance, TaLigneALigneEcheanceDTO> implements ITaLigneALigneEcheanceServiceRemote {

	static Logger logger = Logger.getLogger(TaLigneALigneEcheanceService.class);

	@Inject private ILigneALigneEcheanceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLigneALigneEcheanceService() {
		super(TaLigneALigneEcheance.class,TaLigneALigneEcheanceDTO.class);
	}

	@Override
	public void persistDTO(TaLigneALigneEcheanceDTO transientInstance)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaLigneALigneEcheanceDTO persistentInstance)
			throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLigneALigneEcheanceDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaLigneALigneEcheanceDTO transientInstance,
			String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLigneALigneEcheanceDTO detachedInstance,
			String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLigneALigneEcheanceDTO> findWithNamedQueryDTO(String queryName)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheanceDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheanceDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLigneALigneEcheanceDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLigneALigneEcheanceDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaLigneALigneEcheanceDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaLigneALigneEcheanceDTO dto, String propertyName)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaLigneALigneEcheanceDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaLigneALigneEcheanceDTO dto, String propertyName,
			String validationContext) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaLigneALigneEcheanceDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(TaLigneALigneEcheance transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(TaLigneALigneEcheance persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			dao.remove(merge(persistentInstance));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<TaLigneALigneEcheance> findAllByIdLDocumentAndTypeDoc(Integer id, String typeDoc){
		return dao.findAllByIdLDocumentAndTypeDoc(id, typeDoc);
	}
	
	public void removeAllByIdLDocumentAndTypeDoc(Integer id, String typeDoc) {
		 List<TaLigneALigneEcheance> liste = findAllByIdLDocumentAndTypeDoc(id, typeDoc);
		 for (TaLigneALigneEcheance lal : liste) {
			dao.remove(lal);
		}
	}
	
	public List<TaLigneALigneEcheance> findAllByIdEcheance(Integer id){
		return dao.findAllByIdEcheance(id);
	}
	public void removeAllByIdEcheance(Integer id) {
		 List<TaLigneALigneEcheance> liste = findAllByIdEcheance(id);
		 for (TaLigneALigneEcheance lal : liste) {
			dao.remove(lal);
		}
		
	}
	public List<TaLigneALigneEcheance> findAllByIdDocumentAndTypeDoc(Integer id, String typeDoc){
		return dao.findAllByIdDocumentAndTypeDoc(id, typeDoc);
	}
	public void removeAllByIdDocumentAndTypeDoc(Integer id, String typeDoc) {
		 List<TaLigneALigneEcheance> liste = findAllByIdDocumentAndTypeDoc(id, typeDoc);
		 for (TaLigneALigneEcheance lal : liste) {
			dao.remove(lal);
		}
	}


	@Override
	public TaLigneALigneEcheance merge(TaLigneALigneEcheance detachedInstance) {
		// TODO Auto-generated method stub
		detachedInstance= 	dao.merge(detachedInstance);
		return dao.findById(detachedInstance.getId());
	}

	@Override
	public void persist(TaLigneALigneEcheance transientInstance, String validationContext)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaLigneALigneEcheance merge(TaLigneALigneEcheance detachedInstance,
			String validationContext) {
		// TODO Auto-generated method stub
		validateEntity(detachedInstance, validationContext);
		return merge(detachedInstance);
	}

	@Override
	public TaLigneALigneEcheance findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaLigneALigneEcheance findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaLigneALigneEcheance value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLigneALigneEcheance value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaLigneALigneEcheance value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLigneALigneEcheance value, String propertyName,
			String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLigneALigneEcheance> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocument(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocument(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected TaLigneALigneEcheance refresh(TaLigneALigneEcheance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLigneALigneEcheance findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaLigneALigneEcheance> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers, Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeArticle, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeArticle, Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genereCode(Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void annuleCode(String code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verrouilleCode(String code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLigneALigneEcheance> dejaGenere(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenere(requete);
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneEcheance> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IDocumentTiers> dejaGenereDocument(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenereDocument(requete);
	}

	@Override
	public List<TaLigneALigneEcheance> selectAll(IDocumentTiers taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}
	
	public List<TaLigneALigneEcheance> selectAll(ILigneDocumentTiers taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectAll(taDocument, dateDeb, dateFin);
	}

	@Override
	public List<TaLigneALigneEcheanceDTO> dejaGenereLigneDocument(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenereLigneDocument(requete);
	}
	
	@Override
	public List<TaLEcheance> dejaGenereLigneEcheance(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenereLigneEcheance(requete);
	}

	@Override
	public List<TaLigneALigneEcheance> findByLDocument(ILigneDocumentTiers ligneDocument) {
		// TODO Auto-generated method stub
		return dao.findByLDocument(ligneDocument);
	}
	
	@Override
	public List<TaLigneALigneEcheance> findByLDocument(TaLFlash ligneDocument) {
		// TODO Auto-generated method stub
		return dao.findByLDocument(ligneDocument);
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<IDocumentDTO> recupIdDocumentSrc() {
		// TODO Auto-generated method stub
		return dao.recupIdDocumentSrc();
	}

//	@Override
//	public List<TaLigneALigneEcheanceDTO> recupListeLienLigneALigneDTO(IDocumentTiers persistentInstance) {
//		// TODO Auto-generated method stub
//		return dao.recupListeLienLigneALigneInt(persistentInstance);
//	}

	
	public List<TaLigneALigneEcheanceDTO> dejaGenereLEcheanceDocument(IDocumentDTO ds ) {
		List<TaLigneALigneEcheanceDTO> dejaGenere = null;
		String requeteFixeRDocument = null;
		String requeteWhereSupp = null;
		if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAcompte l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvoir l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvisEcheance l join l.taDocument d    left join l.taArticle art  where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLApporteur l join l.taDocument d    left join l.taArticle art  where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncde l join l.taDocument d    left join l.taArticle art  where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncdeAchat l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonReception l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonliv l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLDevis l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFacture l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPrelevement l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLProforma l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLTicketDeCaisse l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPreparation l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
		if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFlash l join l.taFlash d   left join l.taArticle art   where d.idFlash="+ds.getId();
		if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPanier l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();

		if(requeteFixeRDocument!=null) {
			String requeteDoc="";
			if(ds!=null && ds.getId()!=null && ds.getId()!=0) {
				String jpql = "select x "+requeteFixeRDocument;
				String nomEntity = null;
				String typeEntity = null;
				int idEntity = -1;
				List<TaLigneALigneEcheance> l = dejaGenere(jpql);
				if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
				for (TaLigneALigneEcheance ligne : l) {
					requeteWhereSupp="";
					requeteDoc="";




					if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {            
						//            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
						requeteDoc="select new fr.legrain.document.dto.TaLigneALigneEcheanceDTO(x.id,x.idLigneSrc,l.idLDocument,x.taLEcheance.idLEcheance,x.taLEcheance.taAbonnement.idDocument,x.taLEcheance.taAbonnement.codeDocument,"
								+ "x.taLEcheance.libLDocument,art.codeArticle,x.taLEcheance.debutPeriode,x.taLEcheance.finPeriode,x.taLEcheance.taLAbonnement.complement1,x.taLEcheance.taLAbonnement.complement2,x.taLEcheance.taLAbonnement.complement3) ";
					}
					List<TaLigneALigneEcheanceDTO> l2 = new LinkedList<>();
					if(requeteDoc!=null && !requeteDoc.equals(""))
						l2 =dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
					for (TaLigneALigneEcheanceDTO iLDocumentTiers : l2) {
						dejaGenere.add(iLDocumentTiers);
					}
				}
				System.err.println(l.size());
			} 
		}
		return dejaGenere;
	}

	
	
	public List<TaLigneALigneEcheanceDTO> dejaGenereLEcheanceDocument(ILigneDocumentDTO ds ) {
		List<TaLigneALigneEcheanceDTO> dejaGenere = null;
		String requeteFixeRDocument = null;
		String requeteWhereSupp = null;
		if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAcompte l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvoir l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvisEcheance l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLApporteur l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncde l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncdeAchat l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonReception l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonliv l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLDevis l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFacture l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPrelevement l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLProforma l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLTicketDeCaisse l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPreparation l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFlash l join l.taFlash d  left join l.taArticle art    where l.idLFlash="+ds.getIdLDocument();
		if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPanier l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();

		if(requeteFixeRDocument!=null) {
			String requeteDoc="";
			if(ds!=null && ds.getIdLDocument()!=null && ds.getIdLDocument()!=0) {
				String jpql = "select x "+requeteFixeRDocument;
				String nomEntity = null;
				String typeEntity = null;
				int idEntity = -1;
				List<TaLigneALigneEcheance> l = dejaGenere(jpql);
				if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
				for (TaLigneALigneEcheance ligne : l) {
					requeteWhereSupp="";
					requeteDoc="";




					if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {            
						//            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
						requeteDoc="select new fr.legrain.document.dto.TaLigneALigneEcheanceDTO(x.id,x.idLigneSrc,l.idLDocument,x.taLEcheance.idLEcheance,x.taLEcheance.taAbonnement.idDocument,x.taLEcheance.taAbonnement.codeDocument,"
								+ "x.taLEcheance.libLDocument,art.codeArticle,x.taLEcheance.debutPeriode,x.taLEcheance.finPeriode,x.taLEcheance.taLAbonnement.complement1,x.taLEcheance.taLAbonnement.complement2,x.taLEcheance.taLAbonnement.complement3) ";
					}
					List<TaLigneALigneEcheanceDTO> l2 = new LinkedList<>();
					if(requeteDoc!=null && !requeteDoc.equals(""))
						l2 =dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
					for (TaLigneALigneEcheanceDTO iLDocumentTiers : l2) {
						dejaGenere.add(iLDocumentTiers);
					}
				}

				System.err.println(l.size());
			}
		}
		return dejaGenere;
	}
	
	@Override
	public List<TaLEcheance> dejaGenereLEcheanceDocument(ILigneDocumentTiers ds ) {
		List<TaLEcheance> dejaGenere = null;
		String requeteFixeRDocument = null;
		String requeteWhereSupp = null;
		if(ds.getTaDocument().getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAcompte l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvoir l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvisEcheance l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLApporteur l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncde l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncdeAchat l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonReception l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonliv l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLDevis l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFacture l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPrelevement l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLProforma l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLTicketDeCaisse l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPreparation l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFlash l join l.taFlash d  left join l.taArticle art    where l.idLFlash="+ds.getIdLDocument();
		if(ds.getTaDocument().getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPanier l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();

		if(requeteFixeRDocument!=null) {
			String requeteDoc="";
			if(ds!=null &&  ds.getIdLDocument()!=0) {
				String jpql = "select x "+requeteFixeRDocument;
				String nomEntity = null;
				String typeEntity = null;
				int idEntity = -1;
				List<TaLigneALigneEcheance> l = dejaGenere(jpql);
				if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
				for (TaLigneALigneEcheance ligne : l) {
					requeteWhereSupp="";
					requeteDoc="";




					if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) { 
						requeteDoc="select x.taLEcheance ";
//						requeteDoc="select new fr.legrain.document.dto.TaLigneALigneEcheanceDTO(x.id,x.idLigneSrc,l.idLDocument,x.taLEcheance.idLEcheance,x.taLEcheance.taAbonnement.idDocument,x.taLEcheance.taAbonnement.codeDocument,"
//								+ "x.taLEcheance.libLDocument,art.codeArticle,x.taLEcheance.debutPeriode,x.taLEcheance.finPeriode,x.taLEcheance.taLAbonnement.complement1,x.taLEcheance.taLAbonnement.complement2,x.taLEcheance.taLAbonnement.complement1) ";
					}
					List<TaLEcheance> l2 = new LinkedList<>();
					if(requeteDoc!=null && !requeteDoc.equals(""))
						l2 =dejaGenereLigneEcheance(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
					for (TaLEcheance iLDocumentTiers : l2) {
						dejaGenere.add(iLDocumentTiers);
					}
				}

				System.err.println(l.size());
			}
		}
		return dejaGenere;
	}

}
