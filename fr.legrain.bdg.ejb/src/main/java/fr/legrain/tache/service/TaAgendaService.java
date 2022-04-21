package fr.legrain.tache.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaAgendaMapper;
import fr.legrain.bdg.tache.service.remote.ITaAgendaServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.dao.IAgendaDAO;
import fr.legrain.tache.dto.TaAgendaDTO;
import fr.legrain.tache.model.TaAgenda;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaAgendaService extends AbstractApplicationDAOServer<TaAgenda, TaAgendaDTO> implements ITaAgendaServiceRemote {

	static Logger logger = Logger.getLogger(TaAgendaService.class);

	@Inject private IAgendaDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaAgendaService() {
		super(TaAgenda.class,TaAgendaDTO.class);
	}
	
	public List<TaAgenda> findAgendaUtilisateur(TaUtilisateur utilisateur) {
		return dao.findAgendaUtilisateur(utilisateur);
	}
	
	//	private String defaultJPQLQuery = "select a from TaAgenda a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAgenda transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAgenda transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaAgenda persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdAgenda()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaAgenda merge(TaAgenda detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAgenda merge(TaAgenda detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaAgenda findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAgenda findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaAgenda> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAgendaDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAgendaDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAgenda> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAgendaDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAgendaDTO entityToDTO(TaAgenda entity) {
//		TaAgendaDTO dto = new TaAgendaDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaAgendaMapper mapper = new TaAgendaMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAgendaDTO> listEntityToListDTO(List<TaAgenda> entity) {
		List<TaAgendaDTO> l = new ArrayList<TaAgendaDTO>();

		for (TaAgenda taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAgendaDTO> selectAllDTO() {
		System.out.println("List of TaAgendaDTO EJB :");
		ArrayList<TaAgendaDTO> liste = new ArrayList<TaAgendaDTO>();

		List<TaAgenda> projects = selectAll();
		for(TaAgenda project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAgendaDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAgendaDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAgendaDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAgendaDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAgendaDTO dto, String validationContext) throws EJBException {
		try {
			TaAgendaMapper mapper = new TaAgendaMapper();
			TaAgenda entity = null;
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
	
	public void persistDTO(TaAgendaDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAgendaDTO dto, String validationContext) throws CreateException {
		try {
			TaAgendaMapper mapper = new TaAgendaMapper();
			TaAgenda entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAgendaDTO dto) throws RemoveException {
		try {
			TaAgendaMapper mapper = new TaAgendaMapper();
			TaAgenda entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAgenda refresh(TaAgenda persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAgenda value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAgenda value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAgendaDTO dto, String validationContext) {
		try {
			TaAgendaMapper mapper = new TaAgendaMapper();
			TaAgenda entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAgendaDTO> validator = new BeanValidator<TaAgendaDTO>(TaAgendaDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAgendaDTO dto, String propertyName, String validationContext) {
		try {
			TaAgendaMapper mapper = new TaAgendaMapper();
			TaAgenda entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAgendaDTO> validator = new BeanValidator<TaAgendaDTO>(TaAgendaDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAgendaDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAgendaDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAgenda value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAgenda value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
