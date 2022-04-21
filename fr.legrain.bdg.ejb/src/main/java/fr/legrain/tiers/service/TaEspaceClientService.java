package fr.legrain.tiers.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaEspaceClientMapper;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IAdresseDAO;
import fr.legrain.tiers.dao.IEspaceClientDAO;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.model.TaEspaceClient;

/**
 * Session Bean implementation class TaEspaceClientBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEspaceClientService extends AbstractApplicationDAOServer<TaEspaceClient, TaEspaceClientDTO> implements ITaEspaceClientServiceRemote {

	static Logger logger = Logger.getLogger(TaEspaceClientService.class);

	@Inject private IEspaceClientDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaEspaceClientService() {
		super(TaEspaceClient.class,TaEspaceClientDTO.class);
	}
	
	public TaEspaceClient login(String login, String password) throws EJBException {
		return dao.login(login, password);
	}
	
	public TaEspaceClientDTO loginDTO(String login, String password) {
		return dao.loginDTO(login, password);
	}
	
	public TaEspaceClient findByCodeTiers(String codeTiers) {
		return dao.findByCodeTiers(codeTiers);
	}
	
	public List<TaEspaceClientDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaEspaceClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEspaceClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEspaceClient persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdEspaceClient()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaEspaceClient merge(TaEspaceClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEspaceClient merge(TaEspaceClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEspaceClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEspaceClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEspaceClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaEspaceClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaEspaceClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEspaceClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaEspaceClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaEspaceClientDTO entityToDTO(TaEspaceClient entity) {
//		TaEspaceClientDTO dto = new TaEspaceClientDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEspaceClientMapper mapper = new TaEspaceClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaEspaceClientDTO> listEntityToListDTO(List<TaEspaceClient> entity) {
		List<TaEspaceClientDTO> l = new ArrayList<TaEspaceClientDTO>();

		for (TaEspaceClient taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaEspaceClientDTO> selectAllDTO() {
		System.out.println("List of TaEspaceClientDTO EJB :");
		ArrayList<TaEspaceClientDTO> liste = new ArrayList<TaEspaceClientDTO>();

		List<TaEspaceClient> projects = selectAll();
		for(TaEspaceClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaEspaceClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaEspaceClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaEspaceClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaEspaceClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaEspaceClientDTO dto, String validationContext) throws EJBException {
		try {
			TaEspaceClientMapper mapper = new TaEspaceClientMapper();
			TaEspaceClient entity = null;
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
	
	public void persistDTO(TaEspaceClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaEspaceClientDTO dto, String validationContext) throws CreateException {
		try {
			TaEspaceClientMapper mapper = new TaEspaceClientMapper();
			TaEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaEspaceClientDTO dto) throws RemoveException {
		try {
			TaEspaceClientMapper mapper = new TaEspaceClientMapper();
			TaEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEspaceClient refresh(TaEspaceClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEspaceClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEspaceClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaEspaceClientDTO dto, String validationContext) {
		try {
			TaEspaceClientMapper mapper = new TaEspaceClientMapper();
			TaEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEspaceClientDTO> validator = new BeanValidator<TaEspaceClientDTO>(TaEspaceClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaEspaceClientDTO dto, String propertyName, String validationContext) {
		try {
			TaEspaceClientMapper mapper = new TaEspaceClientMapper();
			TaEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEspaceClientDTO> validator = new BeanValidator<TaEspaceClientDTO>(TaEspaceClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaEspaceClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaEspaceClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEspaceClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEspaceClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}


}
