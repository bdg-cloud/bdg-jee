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
import fr.legrain.bdg.caisse.service.remote.ITaTLSessionCaisseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTLSessionCaisseMapper;
import fr.legrain.caisse.dao.ITLSessionCaisseDAO;
import fr.legrain.caisse.dto.TaTLSessionCaisseDTO;
import fr.legrain.caisse.model.TaTLSessionCaisse;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTLSessionCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTLSessionCaisseService extends AbstractApplicationDAOServer<TaTLSessionCaisse, TaTLSessionCaisseDTO> implements ITaTLSessionCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaTLSessionCaisseService.class);

	@Inject private ITLSessionCaisseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTLSessionCaisseService() {
		super(TaTLSessionCaisse.class,TaTLSessionCaisseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTLSessionCaisse a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTLSessionCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTLSessionCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTLSessionCaisse persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTLSessionCaisse()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTLSessionCaisse merge(TaTLSessionCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTLSessionCaisse merge(TaTLSessionCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTLSessionCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTLSessionCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTLSessionCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTLSessionCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTLSessionCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTLSessionCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTLSessionCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTLSessionCaisseDTO entityToDTO(TaTLSessionCaisse entity) {
//		TaTLSessionCaisseDTO dto = new TaTLSessionCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTLSessionCaisseMapper mapper = new TaTLSessionCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTLSessionCaisseDTO> listEntityToListDTO(List<TaTLSessionCaisse> entity) {
		List<TaTLSessionCaisseDTO> l = new ArrayList<TaTLSessionCaisseDTO>();

		for (TaTLSessionCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTLSessionCaisseDTO> selectAllDTO() {
		System.out.println("List of TaTLSessionCaisseDTO EJB :");
		ArrayList<TaTLSessionCaisseDTO> liste = new ArrayList<TaTLSessionCaisseDTO>();

		List<TaTLSessionCaisse> projects = selectAll();
		for(TaTLSessionCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTLSessionCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTLSessionCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTLSessionCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTLSessionCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTLSessionCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaTLSessionCaisseMapper mapper = new TaTLSessionCaisseMapper();
			TaTLSessionCaisse entity = null;
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
	
	public void persistDTO(TaTLSessionCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTLSessionCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaTLSessionCaisseMapper mapper = new TaTLSessionCaisseMapper();
			TaTLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTLSessionCaisseDTO dto) throws RemoveException {
		try {
			TaTLSessionCaisseMapper mapper = new TaTLSessionCaisseMapper();
			TaTLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTLSessionCaisse refresh(TaTLSessionCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTLSessionCaisse value, String validationContext) /*throws ExceptLgr*/ {
		
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
	public void validateEntityProperty(TaTLSessionCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTLSessionCaisseDTO dto, String validationContext) {
		try {
			TaTLSessionCaisseMapper mapper = new TaTLSessionCaisseMapper();
			TaTLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTLSessionCaisseDTO> validator = new BeanValidator<TaTLSessionCaisseDTO>(TaTLSessionCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTLSessionCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaTLSessionCaisseMapper mapper = new TaTLSessionCaisseMapper();
			TaTLSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTLSessionCaisseDTO> validator = new BeanValidator<TaTLSessionCaisseDTO>(TaTLSessionCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTLSessionCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTLSessionCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTLSessionCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTLSessionCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaTLSessionCaisseDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

}
