package fr.legrain.abonnement.stripe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.abonnement.stripe.dao.IStripePaiementPrevuDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dao.IStripeSourceDAO;
import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeChargeServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripePaiementPrevuMapper;
import fr.legrain.bdg.paiement.service.remote.ILgrStripe;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.generation.service.CreationDocumentMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;


/**
 * Session Bean implementation class TaStripePaiementPrevuBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripePaimentPrevuService extends AbstractApplicationDAOServer<TaStripePaiementPrevu, TaStripePaiementPrevuDTO> implements ITaStripePaiementPrevuServiceRemote {

	static Logger logger = Logger.getLogger(TaStripePaimentPrevuService.class);

	@Inject private IStripePaiementPrevuDAO dao;
	
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;

	/**
	 * Default constructor. 
	 */
	public TaStripePaimentPrevuService() {
		super(TaStripePaiementPrevu.class,TaStripePaiementPrevuDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripePaiementPrevu a";

	public List<TaStripePaiementPrevuDTO> rechercherPaiementPrevuCustomerDTO(String idExterneCustomer) {
		return dao.rechercherPaiementPrevuCustomerDTO(idExterneCustomer);
	}
	
	public List<TaStripePaiementPrevu> rechercherPaiementPrevuCustomer(String idExterneCustomer) {
		return dao.rechercherPaiementPrevuCustomer(idExterneCustomer);
	}

	@EJB private ITaStripeChargeServiceRemote taStripeChargeService;
	@EJB private CreationDocumentMultiple creationDocumentMultiple;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaFactureServiceRemote taFactureService;
	public void declencherPaiementStripe(TaStripePaiementPrevu paiementPrevu) {
		try {
			TaFacture taFacture = null;
			//générer la facture à partir de l'avis d'échéance OU du prélèvement
			if(paiementPrevu.getTaAvisEcheance()!=null) { //TODO copier dans PaiementStripeDossierService.validerPaymentIntent() ==> à unifier
				//TaAvisEcheance taAvisEcheance = taAvisEcheanceService.findById(37);
				TaAvisEcheance taAvisEcheance = taAvisEcheanceService.findById(paiementPrevu.getTaAvisEcheance().getIdDocument());
				ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
				param.setDateDocument(new Date());
//				result.setLibelle(nouveauLibelle);
				param.setTypeDest(TaFacture.TYPE_DOC);
//				if(option==null)option=option3;
//				result.setRepriseReferenceSrc(option.equals(option1));
//				result.setRepriseLibelleSrc(option.equals(option2));
				
				param.setDocumentSrc(paiementPrevu.getTaAvisEcheance());
				List<IDocumentTiers> listeDocumentSrc = new ArrayList<>();
				listeDocumentSrc.add(taAvisEcheance);
				param.setListeDocumentSrc(listeDocumentSrc);
				
				param.setRepriseAucun(true);
//				result.setRepriseAucun(option.equals(option3));
//
//				result.setCodeTiers(codeTiers);
				param.setRetour(true);
				creationDocumentMultiple.setParam(param);
				RetourGenerationDoc retourGenerationDoc = creationDocumentMultiple.creationDocument(true);
				
//				taAvisEcheance.setRelationDocument(true);
				IAbstractGenereDocServiceRemote genereDocAvisEcheanceVersFacture = creationDocumentMultiple.create(taAvisEcheance, new TaFacture());
				boolean enregistre = true;
				taFacture = (TaFacture) retourGenerationDoc.getDoc();
//				taFacture = (TaFacture) genereDocAvisEcheanceVersFacture.genereDocument(taAvisEcheance, 
//						new TaFacture(), taAvisEcheance.getTaTiers().getCodeTiers(),
//						enregistre, false, true);
			} else if(paiementPrevu.getTaPrelevement()!=null) {
				
			}
			
			
			//passer la facture en tant que document payable
			if(taFacture!=null) {
				taFacture = taFactureService.findByCode(taFacture.getCodeDocument());
			}
			
			String chargeStripeId = paiementEnLigneDossierService.payerAvecSourceStripe(null,
					paiementPrevu.getMontant(), 
					paiementPrevu.getTaStripeCustomer().getIdExterne(), 
					paiementPrevu.getTaStripeSource().getIdExterne(), 
					paiementPrevu.getRaisonPaiement(),
					taFacture, //documentPayableCB
					null, // Tiers
					null  //originePaiement
					);
			//paiementEnLigneDossierService.rechercherCharge(null, chargeStripeId);
			TaStripeCharge taStripeCharge = taStripeChargeService.findByCode(chargeStripeId);
			paiementPrevu = findById(paiementPrevu.getIdStripePaiementPrevu());
			paiementPrevu.setTaStripeCharge(taStripeCharge);
			merge(paiementPrevu);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<TaStripePaiementPrevu> findByAbonnementAndDate(TaAbonnement taAbonnement, Date dateEcheance) {
		return dao.findByAbonnementAndDate(taAbonnement, dateEcheance);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripePaiementPrevu transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripePaiementPrevu transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripePaiementPrevu persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripePaiementPrevu()));
	}
	
	public TaStripePaiementPrevu merge(TaStripePaiementPrevu detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripePaiementPrevu merge(TaStripePaiementPrevu detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripePaiementPrevu findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripePaiementPrevu findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripePaiementPrevu> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripePaiementPrevuDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripePaiementPrevuDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripePaiementPrevu> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripePaiementPrevuDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripePaiementPrevuDTO entityToDTO(TaStripePaiementPrevu entity) {
//		TaStripePaiementPrevuDTO dto = new TaStripePaiementPrevuDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripePaiementPrevuMapper mapper = new TaStripePaiementPrevuMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripePaiementPrevuDTO> listEntityToListDTO(List<TaStripePaiementPrevu> entity) {
		List<TaStripePaiementPrevuDTO> l = new ArrayList<TaStripePaiementPrevuDTO>();

		for (TaStripePaiementPrevu taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripePaiementPrevuDTO> selectAllDTO() {
		System.out.println("List of TaStripePaiementPrevuDTO EJB :");
		ArrayList<TaStripePaiementPrevuDTO> liste = new ArrayList<TaStripePaiementPrevuDTO>();

		List<TaStripePaiementPrevu> projects = selectAll();
		for(TaStripePaiementPrevu project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripePaiementPrevuDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripePaiementPrevuDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripePaiementPrevuDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripePaiementPrevuDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripePaiementPrevuDTO dto, String validationContext) throws EJBException {
		try {
			TaStripePaiementPrevuMapper mapper = new TaStripePaiementPrevuMapper();
			TaStripePaiementPrevu entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaStripePaiementPrevuDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripePaiementPrevuDTO dto, String validationContext) throws CreateException {
		try {
			TaStripePaiementPrevuMapper mapper = new TaStripePaiementPrevuMapper();
			TaStripePaiementPrevu entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripePaiementPrevuDTO dto) throws RemoveException {
		try {
			TaStripePaiementPrevuMapper mapper = new TaStripePaiementPrevuMapper();
			TaStripePaiementPrevu entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripePaiementPrevu refresh(TaStripePaiementPrevu persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripePaiementPrevu value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaStripePaiementPrevu value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaStripePaiementPrevuDTO dto, String validationContext) {
		try {
			TaStripePaiementPrevuMapper mapper = new TaStripePaiementPrevuMapper();
			TaStripePaiementPrevu entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripePaiementPrevuDTO> validator = new BeanValidator<TaStripePaiementPrevuDTO>(TaStripePaiementPrevuDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripePaiementPrevuDTO dto, String propertyName, String validationContext) {
		try {
			TaStripePaiementPrevuMapper mapper = new TaStripePaiementPrevuMapper();
			TaStripePaiementPrevu entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripePaiementPrevuDTO> validator = new BeanValidator<TaStripePaiementPrevuDTO>(TaStripePaiementPrevuDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripePaiementPrevuDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripePaiementPrevuDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripePaiementPrevu value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripePaiementPrevu value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
