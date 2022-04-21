package fr.legrain.caisse.service;

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

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.caisse.service.remote.ITaFondDeCaisseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaFondDeCaisseMapper;
import fr.legrain.caisse.dao.IFondDeCaisseDAO;
import fr.legrain.caisse.dto.TaFondDeCaisseDTO;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaFondDeCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaFondDeCaisseService extends AbstractApplicationDAOServer<TaFondDeCaisse, TaFondDeCaisseDTO> implements ITaFondDeCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaFondDeCaisseService.class);

	@Inject private IFondDeCaisseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaFondDeCaisseService() {
		super(TaFondDeCaisse.class,TaFondDeCaisseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaFondDeCaisse a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFondDeCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFondDeCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFondDeCaisse persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdFondDeCaisse()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaFondDeCaisse merge(TaFondDeCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFondDeCaisse merge(TaFondDeCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaFondDeCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaFondDeCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaFondDeCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFondDeCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFondDeCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFondDeCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFondDeCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFondDeCaisseDTO entityToDTO(TaFondDeCaisse entity) {
//		TaFondDeCaisseDTO dto = new TaFondDeCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaFondDeCaisseMapper mapper = new TaFondDeCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFondDeCaisseDTO> listEntityToListDTO(List<TaFondDeCaisse> entity) {
		List<TaFondDeCaisseDTO> l = new ArrayList<TaFondDeCaisseDTO>();

		for (TaFondDeCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFondDeCaisseDTO> selectAllDTO() {
		System.out.println("List of TaFondDeCaisseDTO EJB :");
		ArrayList<TaFondDeCaisseDTO> liste = new ArrayList<TaFondDeCaisseDTO>();

		List<TaFondDeCaisse> projects = selectAll();
		for(TaFondDeCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFondDeCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFondDeCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFondDeCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFondDeCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFondDeCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaFondDeCaisseMapper mapper = new TaFondDeCaisseMapper();
			TaFondDeCaisse entity = null;
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
	
	public void persistDTO(TaFondDeCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFondDeCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaFondDeCaisseMapper mapper = new TaFondDeCaisseMapper();
			TaFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFondDeCaisseDTO dto) throws RemoveException {
		try {
			TaFondDeCaisseMapper mapper = new TaFondDeCaisseMapper();
			TaFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFondDeCaisse refresh(TaFondDeCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFondDeCaisse value, String validationContext) /*throws ExceptLgr*/ {
		
//		TransactionSynchronizationRegistry reg;
		try {
//			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
//			if(reg!=null && reg.getResource("tenant")!=null ) {
//				System.out.println("Pas de validation ==> client lourd");
//			} else {
	
				if(validationContext==null) validationContext="";
				validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
				//dao.validate(value); //validation automatique via la JSR bean validation
//			}
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaFondDeCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFondDeCaisseDTO dto, String validationContext) {
		try {
			TaFondDeCaisseMapper mapper = new TaFondDeCaisseMapper();
			TaFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFondDeCaisseDTO> validator = new BeanValidator<TaFondDeCaisseDTO>(TaFondDeCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFondDeCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaFondDeCaisseMapper mapper = new TaFondDeCaisseMapper();
			TaFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFondDeCaisseDTO> validator = new BeanValidator<TaFondDeCaisseDTO>(TaFondDeCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFondDeCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFondDeCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFondDeCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFondDeCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaFondDeCaisseDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

}
