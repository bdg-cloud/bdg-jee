package fr.legrain.documents.service;

import java.util.ArrayList;
import java.util.List;

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

import fr.legrain.bdg.documents.service.remote.ITaLAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLAvisEcheanceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.documents.dao.ILAvisEcheanceDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLAvisEcheanceBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLAvisEcheanceService extends AbstractApplicationDAOServer<TaLAvisEcheance, TaLAvisEcheanceDTO> implements ITaLAvisEcheanceServiceRemote {

	static Logger logger = Logger.getLogger(TaLAvisEcheanceService.class);

	@Inject private ILAvisEcheanceDAO dao;
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;

	/**
	 * Default constructor. 
	 */
	public TaLAvisEcheanceService() {
		super(TaLAvisEcheance.class,TaLAvisEcheanceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLAvisEcheance a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLAvisEcheance transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLAvisEcheance transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLAvisEcheance persistentInstance) throws RemoveException {
		// taLEcheanceService.supprimeLiaisonsAvisEcheance(persistentInstance);
		dao.remove(persistentInstance);
	}
	
	public TaLAvisEcheance merge(TaLAvisEcheance detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLAvisEcheance merge(TaLAvisEcheance detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLAvisEcheance findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLAvisEcheance findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLAvisEcheance> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLAvisEcheanceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLAvisEcheanceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLAvisEcheance> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLAvisEcheanceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLAvisEcheanceDTO entityToDTO(TaLAvisEcheance entity) {
//		TaLAvisEcheanceDTO dto = new TaLAvisEcheanceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLAvisEcheanceMapper mapper = new TaLAvisEcheanceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLAvisEcheanceDTO> listEntityToListDTO(List<TaLAvisEcheance> entity) {
		List<TaLAvisEcheanceDTO> l = new ArrayList<TaLAvisEcheanceDTO>();

		for (TaLAvisEcheance taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLAvisEcheanceDTO> selectAllDTO() {
		System.out.println("List of TaLAvisEcheanceDTO EJB :");
		ArrayList<TaLAvisEcheanceDTO> liste = new ArrayList<TaLAvisEcheanceDTO>();

		List<TaLAvisEcheance> projects = selectAll();
		for(TaLAvisEcheance project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLAvisEcheanceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLAvisEcheanceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLAvisEcheanceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLAvisEcheanceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLAvisEcheanceDTO dto, String validationContext) throws EJBException {
		try {
			TaLAvisEcheanceMapper mapper = new TaLAvisEcheanceMapper();
			TaLAvisEcheance entity = null;
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
	
	public void persistDTO(TaLAvisEcheanceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLAvisEcheanceDTO dto, String validationContext) throws CreateException {
		try {
			TaLAvisEcheanceMapper mapper = new TaLAvisEcheanceMapper();
			TaLAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLAvisEcheanceDTO dto) throws RemoveException {
		try {
			TaLAvisEcheanceMapper mapper = new TaLAvisEcheanceMapper();
			TaLAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}
	
	public List<TaLAvisEcheance> findAllByIdEcheance(Integer id){
		return dao.findAllByIdEcheance(id);
	}
	
	public List<TaLAvisEcheance> findAllByIdEcheanceSansLigneCom(Integer id){
		return dao.findAllByIdEcheanceSansLigneCom(id);
	}

	@Override
	protected TaLAvisEcheance refresh(TaLAvisEcheance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLAvisEcheance value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLAvisEcheance value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLAvisEcheanceDTO dto, String validationContext) {
		try {
			TaLAvisEcheanceMapper mapper = new TaLAvisEcheanceMapper();
			TaLAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLAvisEcheanceDTO> validator = new BeanValidator<TaLAvisEcheanceDTO>(TaLAvisEcheanceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLAvisEcheanceDTO dto, String propertyName, String validationContext) {
		try {
			TaLAvisEcheanceMapper mapper = new TaLAvisEcheanceMapper();
			TaLAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLAvisEcheanceDTO> validator = new BeanValidator<TaLAvisEcheanceDTO>(TaLAvisEcheanceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLAvisEcheanceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLAvisEcheanceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLAvisEcheance value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLAvisEcheance value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
