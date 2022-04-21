package fr.legrain.droits.service;

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

import fr.legrain.bdg.droits.service.remote.ITaGroupeEntrepriseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaGroupeEntrepriseMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.dao.IGroupeEntrepriseDAO;
import fr.legrain.droits.dto.TaGroupeEntrepriseDTO;
import fr.legrain.droits.model.TaGroupeEntreprise;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaGroupeEntrepriseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaGroupeEntrepriseService extends AbstractApplicationDAOServer<TaGroupeEntreprise, TaGroupeEntrepriseDTO> implements ITaGroupeEntrepriseServiceRemote {

	static Logger logger = Logger.getLogger(TaGroupeEntrepriseService.class);

	@Inject private IGroupeEntrepriseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaGroupeEntrepriseService() {
		super(TaGroupeEntreprise.class,TaGroupeEntrepriseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaGroupeEntreprise a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaGroupeEntreprise transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaGroupeEntreprise transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaGroupeEntreprise persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaGroupeEntreprise merge(TaGroupeEntreprise detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaGroupeEntreprise merge(TaGroupeEntreprise detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaGroupeEntreprise findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaGroupeEntreprise findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaGroupeEntreprise> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaGroupeEntrepriseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaGroupeEntrepriseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaGroupeEntreprise> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaGroupeEntrepriseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaGroupeEntrepriseDTO entityToDTO(TaGroupeEntreprise entity) {
//		TaGroupeEntrepriseDTO dto = new TaGroupeEntrepriseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaGroupeEntrepriseMapper mapper = new TaGroupeEntrepriseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaGroupeEntrepriseDTO> listEntityToListDTO(List<TaGroupeEntreprise> entity) {
		List<TaGroupeEntrepriseDTO> l = new ArrayList<TaGroupeEntrepriseDTO>();

		for (TaGroupeEntreprise taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaGroupeEntrepriseDTO> selectAllDTO() {
		System.out.println("List of TaGroupeEntrepriseDTO EJB :");
		ArrayList<TaGroupeEntrepriseDTO> liste = new ArrayList<TaGroupeEntrepriseDTO>();

		List<TaGroupeEntreprise> projects = selectAll();
		for(TaGroupeEntreprise project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaGroupeEntrepriseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaGroupeEntrepriseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaGroupeEntrepriseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaGroupeEntrepriseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaGroupeEntrepriseDTO dto, String validationContext) throws EJBException {
		try {
			TaGroupeEntrepriseMapper mapper = new TaGroupeEntrepriseMapper();
			TaGroupeEntreprise entity = null;
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
	
	public void persistDTO(TaGroupeEntrepriseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaGroupeEntrepriseDTO dto, String validationContext) throws CreateException {
		try {
			TaGroupeEntrepriseMapper mapper = new TaGroupeEntrepriseMapper();
			TaGroupeEntreprise entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaGroupeEntrepriseDTO dto) throws RemoveException {
		try {
			TaGroupeEntrepriseMapper mapper = new TaGroupeEntrepriseMapper();
			TaGroupeEntreprise entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaGroupeEntreprise refresh(TaGroupeEntreprise persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaGroupeEntreprise value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaGroupeEntreprise value, String propertyName, String validationContext) {
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
	public void validateDTO(TaGroupeEntrepriseDTO dto, String validationContext) {
		try {
			TaGroupeEntrepriseMapper mapper = new TaGroupeEntrepriseMapper();
			TaGroupeEntreprise entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGroupeEntrepriseDTO> validator = new BeanValidator<TaGroupeEntrepriseDTO>(TaGroupeEntrepriseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaGroupeEntrepriseDTO dto, String propertyName, String validationContext) {
		try {
			TaGroupeEntrepriseMapper mapper = new TaGroupeEntrepriseMapper();
			TaGroupeEntreprise entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGroupeEntrepriseDTO> validator = new BeanValidator<TaGroupeEntrepriseDTO>(TaGroupeEntrepriseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaGroupeEntrepriseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaGroupeEntrepriseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaGroupeEntreprise value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaGroupeEntreprise value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
