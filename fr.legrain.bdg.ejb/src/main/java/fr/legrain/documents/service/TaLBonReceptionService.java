package fr.legrain.documents.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBonReceptionServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaLBonReceptionMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.documents.dao.ILBonReceptionDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLBonReceptionBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLBonReceptionService extends AbstractApplicationDAOServer<TaLBonReception, TaLBonReceptionDTO> implements ITaLBonReceptionServiceRemote {

	static Logger logger = Logger.getLogger(TaLBonReceptionService.class);

	@Inject private ILBonReceptionDAO dao;
	@Inject private	SessionInfo sessionInfo;
	
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	@EJB private ITaGenCodeExServiceRemote gencode;
	/**
	 * Default constructor. 
	 */
	public TaLBonReceptionService() {
		super(TaLBonReception.class,TaLBonReceptionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLBonReception a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLBonReception transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLBonReception transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLBonReception persistentInstance) throws RemoveException {
		try {
			
			TaLBonReception masterEntityLigne = findById(persistentInstance.getIdLDocument());
//			TaLot lotLigne = taLotService.findByCode(masterEntityLigne.getNumLot());
//			if(masterEntityLigne.getTaLot()!=null) {
//				taEtatStockService.supprimeEtatStockLot(masterEntityLigne.getTaLot().getIdLot());
//			}
			//masterEntityLigne = taLBonReceptionService.findById(masterEntityLigne.getIdLDocument());
			
//			TaLot lotLigne = masterEntityLigne.getTaLot();
//			TaMouvementStock mouvLigne = masterEntityLigne.getTaMouvementStock();
			
//			masterEntityLigne.setTaLot(null);
//			masterEntityLigne.setTaMouvementStock(null);
			
//			masterEntityLigne = merge(masterEntityLigne,ITaLBonReceptionServiceRemote.validationContext);
//			if(mouvLigne!=null){
////			mouvLigne.setTaLot(null);
////			mouvLigne = taMouvementStockService.merge(mouvLigne,ITaMouvementStockServiceRemote.validationContext);
//			taMouvementStockService.supprimer(mouvLigne);
//			}
//			if(lotLigne!=null)	taLotService.supprimer(lotLigne);
//			taLotService.supprimer(lotLigne);
			
			dao.remove(masterEntityLigne);
			
			//dao.remove(findById(persistentInstance.getIdLDocument()));
		} catch (FinderException e) {
			logger.error("", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TaLBonReception merge(TaLBonReception detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLBonReception merge(TaLBonReception detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLBonReception findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLBonReception findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLBonReception> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLBonReceptionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLBonReceptionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLBonReception> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLBonReceptionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLBonReceptionDTO entityToDTO(TaLBonReception entity) {
//		TaLBonReceptionDTO dto = new TaLBonReceptionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLBonReceptionMapper mapper = new TaLBonReceptionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLBonReceptionDTO> listEntityToListDTO(List<TaLBonReception> entity) {
		List<TaLBonReceptionDTO> l = new ArrayList<TaLBonReceptionDTO>();

		for (TaLBonReception taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLBonReceptionDTO> selectAllDTO() {
		System.out.println("List of TaLBonReceptionDTO EJB :");
		ArrayList<TaLBonReceptionDTO> liste = new ArrayList<TaLBonReceptionDTO>();

		List<TaLBonReception> projects = selectAll();
		for(TaLBonReception project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLBonReceptionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLBonReceptionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLBonReceptionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLBonReceptionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLBonReceptionDTO dto, String validationContext) throws EJBException {
		try {
			TaLBonReceptionMapper mapper = new TaLBonReceptionMapper();
			TaLBonReception entity = null;
			if(dto.getIdLDocument()!=null) {
				entity = dao.findById(dto.getIdLDocument());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdLDocument()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaLBonReceptionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLBonReceptionDTO dto, String validationContext) throws CreateException {
		try {
			TaLBonReceptionMapper mapper = new TaLBonReceptionMapper();
			TaLBonReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLBonReceptionDTO dto) throws RemoveException {
		try {
			TaLBonReceptionMapper mapper = new TaLBonReceptionMapper();
			TaLBonReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLBonReception refresh(TaLBonReception persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLBonReception value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLBonReception value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLBonReceptionDTO dto, String validationContext) {
		try {
			validateDTOAll(dto, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLBonReceptionDTO dto, String propertyName, String validationContext) throws BusinessValidationException {
		try {
			
			if(validationContext==null) validationContext="";
			validateDTO(dto, propertyName, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
//			throw new EJBException(e.getMessage());
			throw new BusinessValidationException(e.getMessage());
		}

	}

	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLBonReceptionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLBonReceptionDTO dto, String propertyName) throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLBonReception value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLBonReception value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaLBonReception> findLigneAvecResultatConformites(int idBr) {
		// TODO Auto-generated method stub
		return dao.findLigneAvecResultatConformites(idBr);
	}

	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaLBonReception.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaLBonReception.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaLBonReception.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
