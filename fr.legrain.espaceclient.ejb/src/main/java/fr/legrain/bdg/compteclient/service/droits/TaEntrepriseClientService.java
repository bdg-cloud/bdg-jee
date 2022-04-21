package fr.legrain.bdg.compteclient.service.droits;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.compteclient.dao.droits.IEntrepriseClientDAO;
import fr.legrain.bdg.compteclient.dto.droits.TaEntrepriseClientDTO;
import fr.legrain.bdg.compteclient.model.droits.TaEntrepriseClient;
import fr.legrain.bdg.compteclient.model.mapping.mapper.TaEntrepriseClientMapper;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaEntrepriseClientServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;


/**
 * Session Bean implementation class TaEntrepriseClientBean
 */
@Stateless
//@Interceptors(ServerTenantInterceptor.class)
public class TaEntrepriseClientService extends AbstractApplicationDAOServer<TaEntrepriseClient, TaEntrepriseClientDTO> implements ITaEntrepriseClientServiceRemote {

	static Logger logger = Logger.getLogger(TaEntrepriseClientService.class);

	@Inject private IEntrepriseClientDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaEntrepriseClientService() {
		super(TaEntrepriseClient.class,TaEntrepriseClientDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaEntrepriseClient a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaEntrepriseClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEntrepriseClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEntrepriseClient persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaEntrepriseClient merge(TaEntrepriseClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEntrepriseClient merge(TaEntrepriseClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEntrepriseClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEntrepriseClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEntrepriseClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaEntrepriseClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaEntrepriseClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEntrepriseClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaEntrepriseClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaEntrepriseClientDTO entityToDTO(TaEntrepriseClient entity) {
//		TaEntrepriseClientDTO dto = new TaEntrepriseClientDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEntrepriseClientMapper mapper = new TaEntrepriseClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaEntrepriseClientDTO> listEntityToListDTO(List<TaEntrepriseClient> entity) {
		List<TaEntrepriseClientDTO> l = new ArrayList<TaEntrepriseClientDTO>();

		for (TaEntrepriseClient taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaEntrepriseClientDTO> selectAllDTO() {
		System.out.println("List of TaEntrepriseClientDTO EJB :");
		ArrayList<TaEntrepriseClientDTO> liste = new ArrayList<TaEntrepriseClientDTO>();

		List<TaEntrepriseClient> projects = selectAll();
		for(TaEntrepriseClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaEntrepriseClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaEntrepriseClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaEntrepriseClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaEntrepriseClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaEntrepriseClientDTO dto, String validationContext) throws EJBException {
		try {
			TaEntrepriseClientMapper mapper = new TaEntrepriseClientMapper();
			TaEntrepriseClient entity = null;
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
	
	public void persistDTO(TaEntrepriseClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaEntrepriseClientDTO dto, String validationContext) throws CreateException {
		try {
			TaEntrepriseClientMapper mapper = new TaEntrepriseClientMapper();
			TaEntrepriseClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaEntrepriseClientDTO dto) throws RemoveException {
		try {
			TaEntrepriseClientMapper mapper = new TaEntrepriseClientMapper();
			TaEntrepriseClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEntrepriseClient refresh(TaEntrepriseClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEntrepriseClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEntrepriseClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaEntrepriseClientDTO dto, String validationContext) {
		try {
			TaEntrepriseClientMapper mapper = new TaEntrepriseClientMapper();
			TaEntrepriseClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEntrepriseClientDTO> validator = new BeanValidator<TaEntrepriseClientDTO>(TaEntrepriseClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaEntrepriseClientDTO dto, String propertyName, String validationContext) {
		try {
			TaEntrepriseClientMapper mapper = new TaEntrepriseClientMapper();
			TaEntrepriseClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEntrepriseClientDTO> validator = new BeanValidator<TaEntrepriseClientDTO>(TaEntrepriseClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaEntrepriseClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaEntrepriseClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEntrepriseClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEntrepriseClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
