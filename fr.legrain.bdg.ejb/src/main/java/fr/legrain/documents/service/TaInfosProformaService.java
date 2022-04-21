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

import fr.legrain.bdg.documents.service.remote.ITaInfosProformaServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosProformaMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaInfosProforma;
import fr.legrain.documents.dao.IInfosProformaDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosProformaBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosProformaService extends AbstractApplicationDAOServer<TaInfosProforma, TaProformaDTO> implements ITaInfosProformaServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosProformaService.class);

	@Inject private IInfosProformaDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosProformaService() {
		super(TaInfosProforma.class,TaProformaDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosProforma a";
	
	public TaInfosProforma findByCodeProforma(String code) {
		return dao.findByCodeProforma(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosProforma transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosProforma transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosProforma persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosProforma merge(TaInfosProforma detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosProforma merge(TaInfosProforma detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosProforma findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosProforma findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosProforma> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaProformaDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaProformaDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosProforma> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaProformaDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaProformaDTO entityToDTO(TaInfosProforma entity) {
//		TaProformaDTO dto = new TaProformaDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosProformaMapper mapper = new TaInfosProformaMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaProformaDTO> listEntityToListDTO(List<TaInfosProforma> entity) {
		List<TaProformaDTO> l = new ArrayList<TaProformaDTO>();

		for (TaInfosProforma taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaProformaDTO> selectAllDTO() {
		System.out.println("List of TaProformaDTO EJB :");
		ArrayList<TaProformaDTO> liste = new ArrayList<TaProformaDTO>();

		List<TaInfosProforma> projects = selectAll();
		for(TaInfosProforma project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaProformaDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaProformaDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaProformaDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaProformaDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaProformaDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosProformaMapper mapper = new TaInfosProformaMapper();
			TaInfosProforma entity = null;
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
	
	public void persistDTO(TaProformaDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaProformaDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosProformaMapper mapper = new TaInfosProformaMapper();
			TaInfosProforma entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaProformaDTO dto) throws RemoveException {
		try {
			TaInfosProformaMapper mapper = new TaInfosProformaMapper();
			TaInfosProforma entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosProforma refresh(TaInfosProforma persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosProforma value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosProforma value, String propertyName, String validationContext) {
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
	public void validateDTO(TaProformaDTO dto, String validationContext) {
		try {
			TaInfosProformaMapper mapper = new TaInfosProformaMapper();
			TaInfosProforma entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaProformaDTO> validator = new BeanValidator<TaProformaDTO>(TaProformaDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaProformaDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosProformaMapper mapper = new TaInfosProformaMapper();
			TaInfosProforma entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaProformaDTO> validator = new BeanValidator<TaProformaDTO>(TaProformaDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaProformaDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaProformaDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosProforma value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosProforma value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
