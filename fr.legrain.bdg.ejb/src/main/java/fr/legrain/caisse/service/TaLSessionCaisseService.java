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
import fr.legrain.bdg.caisse.service.remote.ITaLSessionCaisseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLSessionCaisseMapper;
import fr.legrain.caisse.dao.ILSessionCaisseDAO;
import fr.legrain.caisse.dto.TaLSessionCaisseDTO;
import fr.legrain.caisse.model.TaLSessionCaisse;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLSessionCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLSessionCaisseService extends AbstractApplicationDAOServer<TaLSessionCaisse, TaLSessionCaisseDTO> implements ITaLSessionCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaLSessionCaisseService.class);

	@Inject private ILSessionCaisseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLSessionCaisseService() {
		super(TaLSessionCaisse.class,TaLSessionCaisseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLSessionCaisse a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLSessionCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLSessionCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLSessionCaisse persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLSessionCaisse()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLSessionCaisse merge(TaLSessionCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLSessionCaisse merge(TaLSessionCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLSessionCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLSessionCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLSessionCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLSessionCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLSessionCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLSessionCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLSessionCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLSessionCaisseDTO entityToDTO(TaLSessionCaisse entity) {
//		TaLSessionCaisseDTO dto = new TaLSessionCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLSessionCaisseMapper mapper = new TaLSessionCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLSessionCaisseDTO> listEntityToListDTO(List<TaLSessionCaisse> entity) {
		List<TaLSessionCaisseDTO> l = new ArrayList<TaLSessionCaisseDTO>();

		for (TaLSessionCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLSessionCaisseDTO> selectAllDTO() {
		System.out.println("List of TaLSessionCaisseDTO EJB :");
		ArrayList<TaLSessionCaisseDTO> liste = new ArrayList<TaLSessionCaisseDTO>();

		List<TaLSessionCaisse> projects = selectAll();
		for(TaLSessionCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLSessionCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLSessionCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLSessionCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLSessionCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLSessionCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaLSessionCaisseMapper mapper = new TaLSessionCaisseMapper();
			TaLSessionCaisse entity = null;
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
	
	public void persistDTO(TaLSessionCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLSessionCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaLSessionCaisseMapper mapper = new TaLSessionCaisseMapper();
			TaLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLSessionCaisseDTO dto) throws RemoveException {
		try {
			TaLSessionCaisseMapper mapper = new TaLSessionCaisseMapper();
			TaLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLSessionCaisse refresh(TaLSessionCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLSessionCaisse value, String validationContext) /*throws ExceptLgr*/ {
		
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
	public void validateEntityProperty(TaLSessionCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLSessionCaisseDTO dto, String validationContext) {
		try {
			TaLSessionCaisseMapper mapper = new TaLSessionCaisseMapper();
			TaLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLSessionCaisseDTO> validator = new BeanValidator<TaLSessionCaisseDTO>(TaLSessionCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLSessionCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaLSessionCaisseMapper mapper = new TaLSessionCaisseMapper();
			TaLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLSessionCaisseDTO> validator = new BeanValidator<TaLSessionCaisseDTO>(TaLSessionCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLSessionCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLSessionCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLSessionCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLSessionCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaLSessionCaisseDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

}
