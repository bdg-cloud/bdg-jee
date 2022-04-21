package fr.legrain.documents.service;

import java.util.Date;
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

import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.documents.dao.IRDocumentDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRDocumentService extends AbstractApplicationDAOServer<TaRDocument, TaRDocumentDTO> implements ITaRDocumentServiceRemote {

	static Logger logger = Logger.getLogger(TaRDocumentService.class);

	@Inject private IRDocumentDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRDocumentService() {
		super(TaRDocument.class,TaRDocumentDTO.class);
	}

	@Override
	public void persistDTO(TaRDocumentDTO transientInstance)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaRDocumentDTO persistentInstance)
			throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaRDocumentDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaRDocumentDTO transientInstance,
			String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaRDocumentDTO detachedInstance,
			String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaRDocumentDTO> findWithNamedQueryDTO(String queryName)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocumentDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocumentDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRDocumentDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRDocumentDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaRDocumentDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaRDocumentDTO dto, String propertyName)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaRDocumentDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaRDocumentDTO dto, String propertyName,
			String validationContext) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaRDocumentDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(TaRDocument transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(TaRDocument persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			dao.remove(merge(persistentInstance));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TaRDocument merge(TaRDocument detachedInstance) {
		// TODO Auto-generated method stub
		detachedInstance= 	dao.merge(detachedInstance);
		return dao.findById(detachedInstance.getIdRDocument());
	}

	@Override
	public void persist(TaRDocument transientInstance, String validationContext)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaRDocument merge(TaRDocument detachedInstance,
			String validationContext) {
		// TODO Auto-generated method stub
		validateEntity(detachedInstance, validationContext);
		return merge(detachedInstance);
	}

	@Override
	public TaRDocument findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaRDocument findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaRDocument value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaRDocument value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaRDocument value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaRDocument value, String propertyName,
			String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaRDocument> selectAll() {
		// TODO Auto-generated method stub
		List<TaRDocument> l=dao.selectAll();
		for (TaRDocument oo : l) {
			IDocumentTiers src = null;
			IDocumentTiers dest = null;
			if(oo.getIdSrc()!=null) {
			if(oo.getTaAbonnement()!=null && oo.getTaAbonnement().getIdDocument()==oo.getIdSrc()) src=oo.getTaAbonnement();
			else if(oo.getTaAbonnement()!=null) dest=oo.getTaAbonnement();
			if(oo.getTaAcompte()!=null && oo.getTaAcompte().getIdDocument()==oo.getIdSrc()) src=oo.getTaAcompte();
      else if(oo.getTaAcompte()!=null) dest=oo.getTaAcompte();
			if(oo.getTaApporteur()!=null && oo.getTaApporteur().getIdDocument()==oo.getIdSrc()) src=oo.getTaApporteur();
      else if(oo.getTaApporteur()!=null) dest=oo.getTaApporteur();
			if(oo.getTaAvisEcheance()!=null && oo.getTaAvisEcheance().getIdDocument()==oo.getIdSrc()) src=oo.getTaAvisEcheance();
      else if(oo.getTaAvisEcheance()!=null) dest=oo.getTaAvisEcheance();
			if(oo.getTaAvoir()!=null && oo.getTaAvoir().getIdDocument()==oo.getIdSrc()) src=oo.getTaAvoir();
      else if(oo.getTaAvoir()!=null) dest=oo.getTaAvoir();
			if(oo.getTaBoncde()!=null && oo.getTaBoncde().getIdDocument()==oo.getIdSrc()) src=oo.getTaBoncde();
      else if(oo.getTaBoncde()!=null) dest=oo.getTaBoncde();
			if(oo.getTaBoncdeAchat()!=null && oo.getTaBoncdeAchat().getIdDocument()==oo.getIdSrc()) src=oo.getTaBoncdeAchat();
      else if(oo.getTaBoncdeAchat()!=null) dest=oo.getTaBoncdeAchat();
			if(oo.getTaBonliv()!=null && oo.getTaBonliv().getIdDocument()==oo.getIdSrc()) src=oo.getTaBonliv();
      else if(oo.getTaBonliv()!=null) dest=oo.getTaBonliv();
			if(oo.getTaBonReception()!=null && oo.getTaBonReception().getIdDocument()==oo.getIdSrc()) src=oo.getTaBonReception();
      else if(oo.getTaBonReception()!=null) dest=oo.getTaBonReception();
			if(oo.getTaDevis()!=null && oo.getTaDevis().getIdDocument()==oo.getIdSrc()) src=oo.getTaDevis();
      else if(oo.getTaDevis()!=null) dest=oo.getTaDevis();
			if(oo.getTaFacture()!=null && oo.getTaFacture().getIdDocument()==oo.getIdSrc()) src=oo.getTaFacture();
      else if(oo.getTaFacture()!=null) dest=oo.getTaFacture();
			if(oo.getTaPrelevement()!=null && oo.getTaPrelevement().getIdDocument()==oo.getIdSrc()) src=oo.getTaPrelevement();
      else if(oo.getTaPrelevement()!=null) dest=oo.getTaPrelevement();
			if(oo.getTaPreparation()!=null && oo.getTaPreparation().getIdDocument()==oo.getIdSrc()) src=oo.getTaPreparation();
      else if(oo.getTaPreparation()!=null) dest=oo.getTaPreparation();
			if(oo.getTaProforma()!=null && oo.getTaProforma().getIdDocument()==oo.getIdSrc()) src=oo.getTaProforma();
      else if(oo.getTaProforma()!=null) dest=oo.getTaProforma();
			if(oo.getTaTicketDeCaisse()!=null && oo.getTaTicketDeCaisse().getIdDocument()==oo.getIdSrc()) src=oo.getTaTicketDeCaisse();
      else if(oo.getTaTicketDeCaisse()!=null) dest=oo.getTaTicketDeCaisse();
			}
			if(src!=null) {
			recupSetLigneALigne(src);
			recupSetREtat(src);
			recupSetHistREtat(src);
			recupSetREtatLigneDocuments(src);
			recupSetHistREtatLigneDocuments(src);
			}
			if(dest!=null) {
			recupSetLigneALigne(dest);
			recupSetREtat(dest);
			recupSetHistREtat(dest);
			recupSetREtatLigneDocuments(dest);
			recupSetHistREtatLigneDocuments(dest);
			}
		}
		return l;
	}

	@Override
	public List<TaRDocument> rechercheDocument(Date dateDeb, Date dateFin) {
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
	public List<TaRDocument> rechercheDocument(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaRDocument> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> selectAll(IDocumentTiers taDocument,
			Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TaRDocument refresh(TaRDocument persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRDocument findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaRDocument> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
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
	public List<TaRDocument> dejaGenere(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenere(requete);
	}

	@Override
	public List<TaRDocument> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocument> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRDocumentDTO> dejaGenereDocument(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenereDocument(requete);
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
