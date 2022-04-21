package fr.legrain.servicewebexterne.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaTAuthentificationMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaTAuthentificationServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.dao.ITAuthentificationDAO;
import fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO;
import fr.legrain.servicewebexterne.model.TaTAuthentification;
import fr.legrain.tache.dao.IAgendaDAO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTAuthentificationService extends AbstractApplicationDAOServer<TaTAuthentification, TaTAuthentificationDTO> implements ITaTAuthentificationServiceRemote {

	static Logger logger = Logger.getLogger(TaTAuthentificationService.class);

	@Inject private ITAuthentificationDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTAuthentificationService() {
		super(TaTAuthentification.class,TaTAuthentificationDTO.class);
	}
	
	public List<TaTAuthentificationDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
//	public List<TaTAuthentification> findAgendaUtilisateur(TaUtilisateur utilisateur) {
//		return dao.findAgendaUtilisateur(utilisateur);
//	}
	
	//	private String defaultJPQLQuery = "select a from TaTAuthentification a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTAuthentification transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTAuthentification transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTAuthentification persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTAuthentification()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTAuthentification merge(TaTAuthentification detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTAuthentification merge(TaTAuthentification detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTAuthentification findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTAuthentification findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTAuthentification> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTAuthentificationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTAuthentificationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTAuthentification> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTAuthentificationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTAuthentificationDTO entityToDTO(TaTAuthentification entity) {
//		TaTAuthentificationDTO dto = new TaTAuthentificationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTAuthentificationMapper mapper = new TaTAuthentificationMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTAuthentificationDTO> listEntityToListDTO(List<TaTAuthentification> entity) {
		List<TaTAuthentificationDTO> l = new ArrayList<TaTAuthentificationDTO>();

		for (TaTAuthentification taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTAuthentificationDTO> selectAllDTO() {
		System.out.println("List of TaTAuthentificationDTO EJB :");
		ArrayList<TaTAuthentificationDTO> liste = new ArrayList<TaTAuthentificationDTO>();

		List<TaTAuthentification> projects = selectAll();
		for(TaTAuthentification project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTAuthentificationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTAuthentificationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTAuthentificationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTAuthentificationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTAuthentificationDTO dto, String validationContext) throws EJBException {
		try {
			TaTAuthentificationMapper mapper = new TaTAuthentificationMapper();
			TaTAuthentification entity = null;
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
	
	public void persistDTO(TaTAuthentificationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTAuthentificationDTO dto, String validationContext) throws CreateException {
		try {
			TaTAuthentificationMapper mapper = new TaTAuthentificationMapper();
			TaTAuthentification entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTAuthentificationDTO dto) throws RemoveException {
		try {
			TaTAuthentificationMapper mapper = new TaTAuthentificationMapper();
			TaTAuthentification entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTAuthentification refresh(TaTAuthentification persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTAuthentification value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTAuthentification value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTAuthentificationDTO dto, String validationContext) {
		try {
			TaTAuthentificationMapper mapper = new TaTAuthentificationMapper();
			TaTAuthentification entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTAuthentificationDTO> validator = new BeanValidator<TaTAuthentificationDTO>(TaTAuthentificationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTAuthentificationDTO dto, String propertyName, String validationContext) {
		try {
			TaTAuthentificationMapper mapper = new TaTAuthentificationMapper();
			TaTAuthentification entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTAuthentificationDTO> validator = new BeanValidator<TaTAuthentificationDTO>(TaTAuthentificationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTAuthentificationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTAuthentificationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTAuthentification value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTAuthentification value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
