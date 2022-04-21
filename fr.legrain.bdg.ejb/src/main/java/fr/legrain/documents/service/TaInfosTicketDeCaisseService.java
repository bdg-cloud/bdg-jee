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

import fr.legrain.bdg.documents.service.remote.ITaInfosTicketDeCaisseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosTicketDeCaisseMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaInfosTicketDeCaisse;
import fr.legrain.documents.dao.IInfosFactureDAO;
import fr.legrain.documents.dao.IInfosTicketDeCaisseDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosTicketDeCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosTicketDeCaisseService extends AbstractApplicationDAOServer<TaInfosTicketDeCaisse, TaTicketDeCaisseDTO> implements ITaInfosTicketDeCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosTicketDeCaisseService.class);

	@Inject private IInfosTicketDeCaisseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosTicketDeCaisseService() {
		super(TaInfosTicketDeCaisse.class,TaTicketDeCaisseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosTicketDeCaisse a";
	
	public TaInfosTicketDeCaisse findByCodeFacture(String code) {
		return dao.findByCodeFacture(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosTicketDeCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosTicketDeCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosTicketDeCaisse persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosTicketDeCaisse merge(TaInfosTicketDeCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosTicketDeCaisse merge(TaInfosTicketDeCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosTicketDeCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosTicketDeCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosTicketDeCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTicketDeCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTicketDeCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosTicketDeCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTicketDeCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTicketDeCaisseDTO entityToDTO(TaInfosTicketDeCaisse entity) {
//		TaTicketDeCaisseDTO dto = new TaTicketDeCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosTicketDeCaisseMapper mapper = new TaInfosTicketDeCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTicketDeCaisseDTO> listEntityToListDTO(List<TaInfosTicketDeCaisse> entity) {
		List<TaTicketDeCaisseDTO> l = new ArrayList<TaTicketDeCaisseDTO>();

		for (TaInfosTicketDeCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTicketDeCaisseDTO> selectAllDTO() {
		System.out.println("List of TaTicketDeCaisseDTO EJB :");
		ArrayList<TaTicketDeCaisseDTO> liste = new ArrayList<TaTicketDeCaisseDTO>();

		List<TaInfosTicketDeCaisse> projects = selectAll();
		for(TaInfosTicketDeCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTicketDeCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTicketDeCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTicketDeCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTicketDeCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTicketDeCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosTicketDeCaisseMapper mapper = new TaInfosTicketDeCaisseMapper();
			TaInfosTicketDeCaisse entity = null;
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
	
	public void persistDTO(TaTicketDeCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTicketDeCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosTicketDeCaisseMapper mapper = new TaInfosTicketDeCaisseMapper();
			TaInfosTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTicketDeCaisseDTO dto) throws RemoveException {
		try {
			TaInfosTicketDeCaisseMapper mapper = new TaInfosTicketDeCaisseMapper();
			TaInfosTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosTicketDeCaisse refresh(TaInfosTicketDeCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosTicketDeCaisse value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosTicketDeCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTicketDeCaisseDTO dto, String validationContext) {
		try {
			TaInfosTicketDeCaisseMapper mapper = new TaInfosTicketDeCaisseMapper();
			TaInfosTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTicketDeCaisseDTO> validator = new BeanValidator<TaTicketDeCaisseDTO>(TaTicketDeCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTicketDeCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosTicketDeCaisseMapper mapper = new TaInfosTicketDeCaisseMapper();
			TaInfosTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTicketDeCaisseDTO> validator = new BeanValidator<TaTicketDeCaisseDTO>(TaTicketDeCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTicketDeCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTicketDeCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosTicketDeCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosTicketDeCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
