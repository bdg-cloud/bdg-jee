package fr.legrain.controle.service;

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

import fr.legrain.bdg.general.service.remote.ITaVerrouCodeGenereServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaVerrouCodeGenereMapper;
import fr.legrain.controle.dao.IVerrouCodeGenereDAO;
import fr.legrain.controle.dto.TaVerrouCodeGenereDTO;
import fr.legrain.controle.model.TaVerrouCodeGenere;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

/**
 * Session Bean implementation class TaVerrouCodeGenereBean
 */
@Stateless
//@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaVerrouCodeGenereService extends AbstractApplicationDAOServer<TaVerrouCodeGenere, TaVerrouCodeGenereDTO> implements ITaVerrouCodeGenereServiceRemote {

	static Logger logger = Logger.getLogger(TaVerrouCodeGenereService.class);

	@Inject private IVerrouCodeGenereDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaVerrouCodeGenereService() {
		super(TaVerrouCodeGenere.class,TaVerrouCodeGenereDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaVerrouCodeGenere a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaVerrouCodeGenere transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaVerrouCodeGenere transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaVerrouCodeGenere persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaVerrouCodeGenere merge(TaVerrouCodeGenere detachedInstance) {
		return merge(detachedInstance,null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaVerrouCodeGenere merge(TaVerrouCodeGenere detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaVerrouCodeGenere findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaVerrouCodeGenere findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaVerrouCodeGenere> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaVerrouCodeGenereDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaVerrouCodeGenereDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaVerrouCodeGenere> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaVerrouCodeGenereDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaVerrouCodeGenereDTO entityToDTO(TaVerrouCodeGenere entity) {
//		TaVerrouCodeGenereDTO dto = new TaVerrouCodeGenereDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaVerrouCodeGenereMapper mapper = new TaVerrouCodeGenereMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaVerrouCodeGenereDTO> listEntityToListDTO(List<TaVerrouCodeGenere> entity) {
		List<TaVerrouCodeGenereDTO> l = new ArrayList<TaVerrouCodeGenereDTO>();

		for (TaVerrouCodeGenere taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaVerrouCodeGenereDTO> selectAllDTO() {
		System.out.println("List of TaVerrouCodeGenereDTO EJB :");
		ArrayList<TaVerrouCodeGenereDTO> liste = new ArrayList<TaVerrouCodeGenereDTO>();

		List<TaVerrouCodeGenere> projects = selectAll();
		for(TaVerrouCodeGenere project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaVerrouCodeGenereDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaVerrouCodeGenereDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaVerrouCodeGenereDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaVerrouCodeGenereDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaVerrouCodeGenereDTO dto, String validationContext) throws EJBException {
		try {
			TaVerrouCodeGenereMapper mapper = new TaVerrouCodeGenereMapper();
			TaVerrouCodeGenere entity = null;
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
	
	public void persistDTO(TaVerrouCodeGenereDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaVerrouCodeGenereDTO dto, String validationContext) throws CreateException {
		try {
			TaVerrouCodeGenereMapper mapper = new TaVerrouCodeGenereMapper();
			TaVerrouCodeGenere entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaVerrouCodeGenereDTO dto) throws RemoveException {
		try {
			TaVerrouCodeGenereMapper mapper = new TaVerrouCodeGenereMapper();
			TaVerrouCodeGenere entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaVerrouCodeGenere refresh(TaVerrouCodeGenere persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaVerrouCodeGenere value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaVerrouCodeGenere value, String propertyName, String validationContext) {
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
	public void validateDTO(TaVerrouCodeGenereDTO dto, String validationContext) {
		try {
			TaVerrouCodeGenereMapper mapper = new TaVerrouCodeGenereMapper();
			TaVerrouCodeGenere entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaVerrouCodeGenereDTO> validator = new BeanValidator<TaVerrouCodeGenereDTO>(TaVerrouCodeGenereDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	public void validateDTOProperty(TaVerrouCodeGenereDTO dto, String propertyName, String validationContext) {
		try {
			TaVerrouCodeGenereMapper mapper = new TaVerrouCodeGenereMapper();
			TaVerrouCodeGenere entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaVerrouCodeGenereDTO> validator = new BeanValidator<TaVerrouCodeGenereDTO>(TaVerrouCodeGenereDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}

	@Override
	public void validateDTO(TaVerrouCodeGenereDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	public void validateDTOProperty(TaVerrouCodeGenereDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	public void validateEntity(TaVerrouCodeGenere value) {
		validateEntity(value,null);
	}

	@Override
	public void validateEntityProperty(TaVerrouCodeGenere value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
