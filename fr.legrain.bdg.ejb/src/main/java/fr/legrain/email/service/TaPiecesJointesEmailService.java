package fr.legrain.email.service;

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

import fr.legrain.bdg.email.service.remote.ITaPiecesJointesEmailServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaPieceJointeEmailMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.email.dao.IMessageEmailDAO;
import fr.legrain.email.dao.IPiecesJointesEmailDAO;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.email.model.TaPieceJointeEmail;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaPiecesJointesEmailService extends AbstractApplicationDAOServer<TaPieceJointeEmail, TaPieceJointeEmailDTO> implements ITaPiecesJointesEmailServiceRemote {

	static Logger logger = Logger.getLogger(TaPiecesJointesEmailService.class);

	@Inject private IPiecesJointesEmailDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaPiecesJointesEmailService() {
		super(TaPieceJointeEmail.class,TaPieceJointeEmailDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaPieceJointeEmail a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaPieceJointeEmailDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaPieceJointeEmailDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaPieceJointeEmail transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPieceJointeEmail transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaPieceJointeEmail persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLogPieceJointeEmail()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaPieceJointeEmail merge(TaPieceJointeEmail detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPieceJointeEmail merge(TaPieceJointeEmail detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaPieceJointeEmail findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPieceJointeEmail findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaPieceJointeEmail> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPieceJointeEmailDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPieceJointeEmailDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPieceJointeEmail> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPieceJointeEmailDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPieceJointeEmailDTO entityToDTO(TaPieceJointeEmail entity) {
//		TaPieceJointeEmailDTO dto = new TaPieceJointeEmailDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaPieceJointeEmailMapper mapper = new TaPieceJointeEmailMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPieceJointeEmailDTO> listEntityToListDTO(List<TaPieceJointeEmail> entity) {
		List<TaPieceJointeEmailDTO> l = new ArrayList<TaPieceJointeEmailDTO>();

		for (TaPieceJointeEmail taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPieceJointeEmailDTO> selectAllDTO() {
		System.out.println("List of TaPieceJointeEmailDTO EJB :");
		ArrayList<TaPieceJointeEmailDTO> liste = new ArrayList<TaPieceJointeEmailDTO>();

		List<TaPieceJointeEmail> projects = selectAll();
		for(TaPieceJointeEmail project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPieceJointeEmailDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPieceJointeEmailDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPieceJointeEmailDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPieceJointeEmailDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPieceJointeEmailDTO dto, String validationContext) throws EJBException {
		try {
			TaPieceJointeEmailMapper mapper = new TaPieceJointeEmailMapper();
			TaPieceJointeEmail entity = null;
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
	
	public void persistDTO(TaPieceJointeEmailDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPieceJointeEmailDTO dto, String validationContext) throws CreateException {
		try {
			TaPieceJointeEmailMapper mapper = new TaPieceJointeEmailMapper();
			TaPieceJointeEmail entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPieceJointeEmailDTO dto) throws RemoveException {
		try {
			TaPieceJointeEmailMapper mapper = new TaPieceJointeEmailMapper();
			TaPieceJointeEmail entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPieceJointeEmail refresh(TaPieceJointeEmail persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPieceJointeEmail value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaPieceJointeEmail value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPieceJointeEmailDTO dto, String validationContext) {
		try {
			TaPieceJointeEmailMapper mapper = new TaPieceJointeEmailMapper();
			TaPieceJointeEmail entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPieceJointeEmailDTO> validator = new BeanValidator<TaPieceJointeEmailDTO>(TaPieceJointeEmailDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPieceJointeEmailDTO dto, String propertyName, String validationContext) {
		try {
			TaPieceJointeEmailMapper mapper = new TaPieceJointeEmailMapper();
			TaPieceJointeEmail entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPieceJointeEmailDTO> validator = new BeanValidator<TaPieceJointeEmailDTO>(TaPieceJointeEmailDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPieceJointeEmailDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPieceJointeEmailDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPieceJointeEmail value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPieceJointeEmail value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
