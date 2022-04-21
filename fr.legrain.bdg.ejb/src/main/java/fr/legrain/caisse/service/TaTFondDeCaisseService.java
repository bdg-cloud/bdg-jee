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
import fr.legrain.bdg.caisse.service.remote.ITaTFondDeCaisseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTFondDeCaisseMapper;
import fr.legrain.caisse.dao.ITFondDeCaisseDAO;
import fr.legrain.caisse.dto.TaTFondDeCaisseDTO;
import fr.legrain.caisse.model.TaTFondDeCaisse;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

 
/**
 * Session Bean implementation class TaTFondDeCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTFondDeCaisseService extends AbstractApplicationDAOServer<TaTFondDeCaisse, TaTFondDeCaisseDTO> implements ITaTFondDeCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaTFondDeCaisseService.class);

	@Inject private ITFondDeCaisseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTFondDeCaisseService() {
		super(TaTFondDeCaisse.class,TaTFondDeCaisseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTFondDeCaisse a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTFondDeCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTFondDeCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTFondDeCaisse persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTFondDeCaisse()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTFondDeCaisse merge(TaTFondDeCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTFondDeCaisse merge(TaTFondDeCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTFondDeCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTFondDeCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTFondDeCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTFondDeCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTFondDeCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTFondDeCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTFondDeCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTFondDeCaisseDTO entityToDTO(TaTFondDeCaisse entity) {
//		TaTFondDeCaisseDTO dto = new TaTFondDeCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTFondDeCaisseMapper mapper = new TaTFondDeCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTFondDeCaisseDTO> listEntityToListDTO(List<TaTFondDeCaisse> entity) {
		List<TaTFondDeCaisseDTO> l = new ArrayList<TaTFondDeCaisseDTO>();

		for (TaTFondDeCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTFondDeCaisseDTO> selectAllDTO() {
		System.out.println("List of TaTFondDeCaisseDTO EJB :");
		ArrayList<TaTFondDeCaisseDTO> liste = new ArrayList<TaTFondDeCaisseDTO>();

		List<TaTFondDeCaisse> projects = selectAll();
		for(TaTFondDeCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTFondDeCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTFondDeCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTFondDeCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTFondDeCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTFondDeCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaTFondDeCaisseMapper mapper = new TaTFondDeCaisseMapper();
			TaTFondDeCaisse entity = null;
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
	
	public void persistDTO(TaTFondDeCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTFondDeCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaTFondDeCaisseMapper mapper = new TaTFondDeCaisseMapper();
			TaTFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTFondDeCaisseDTO dto) throws RemoveException {
		try {
			TaTFondDeCaisseMapper mapper = new TaTFondDeCaisseMapper();
			TaTFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTFondDeCaisse refresh(TaTFondDeCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTFondDeCaisse value, String validationContext) /*throws ExceptLgr*/ {
		
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
	public void validateEntityProperty(TaTFondDeCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTFondDeCaisseDTO dto, String validationContext) {
		try {
			TaTFondDeCaisseMapper mapper = new TaTFondDeCaisseMapper();
			TaTFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTFondDeCaisseDTO> validator = new BeanValidator<TaTFondDeCaisseDTO>(TaTFondDeCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTFondDeCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaTFondDeCaisseMapper mapper = new TaTFondDeCaisseMapper();
			TaTFondDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTFondDeCaisseDTO> validator = new BeanValidator<TaTFondDeCaisseDTO>(TaTFondDeCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTFondDeCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTFondDeCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTFondDeCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTFondDeCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaTFondDeCaisseDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

}
