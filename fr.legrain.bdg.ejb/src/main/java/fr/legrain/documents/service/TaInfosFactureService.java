package fr.legrain.documents.service;

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

import fr.legrain.bdg.documents.service.remote.ITaInfosFactureServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosFactureMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.documents.dao.IInfosFactureDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosFactureBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosFactureService extends AbstractApplicationDAOServer<TaInfosFacture, TaFactureDTO> implements ITaInfosFactureServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosFactureService.class);

	@Inject private IInfosFactureDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosFactureService() {
		super(TaInfosFacture.class,TaFactureDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosFacture a";
	
	public TaInfosFacture findByCodeFacture(String code) {
		return dao.findByCodeFacture(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosFacture transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosFacture transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosFacture persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosFacture merge(TaInfosFacture detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosFacture merge(TaInfosFacture detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosFacture findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosFacture findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosFacture> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFactureDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFactureDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosFacture> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFactureDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFactureDTO entityToDTO(TaInfosFacture entity) {
//		TaFactureDTO dto = new TaFactureDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosFactureMapper mapper = new TaInfosFactureMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFactureDTO> listEntityToListDTO(List<TaInfosFacture> entity) {
		List<TaFactureDTO> l = new ArrayList<TaFactureDTO>();

		for (TaInfosFacture taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFactureDTO> selectAllDTO() {
		System.out.println("List of TaFactureDTO EJB :");
		ArrayList<TaFactureDTO> liste = new ArrayList<TaFactureDTO>();

		List<TaInfosFacture> projects = selectAll();
		for(TaInfosFacture project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFactureDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFactureDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFactureDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFactureDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFactureDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosFactureMapper mapper = new TaInfosFactureMapper();
			TaInfosFacture entity = null;
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
	
	public void persistDTO(TaFactureDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFactureDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosFactureMapper mapper = new TaInfosFactureMapper();
			TaInfosFacture entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFactureDTO dto) throws RemoveException {
		try {
			TaInfosFactureMapper mapper = new TaInfosFactureMapper();
			TaInfosFacture entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosFacture refresh(TaInfosFacture persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosFacture value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosFacture value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFactureDTO dto, String validationContext) {
		try {
			TaInfosFactureMapper mapper = new TaInfosFactureMapper();
			TaInfosFacture entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFactureDTO> validator = new BeanValidator<TaFactureDTO>(TaFactureDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFactureDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosFactureMapper mapper = new TaInfosFactureMapper();
			TaInfosFacture entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFactureDTO> validator = new BeanValidator<TaFactureDTO>(TaFactureDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFactureDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFactureDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosFacture value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosFacture value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
