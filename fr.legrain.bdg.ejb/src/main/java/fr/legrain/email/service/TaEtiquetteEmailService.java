package fr.legrain.email.service;

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

import fr.legrain.bdg.email.service.remote.ITaEtiquetteEmailServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaEtiquetteEmailMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.email.dao.IEtiquetteEmailDAO;
import fr.legrain.email.dao.IMessageEmailDAO;
import fr.legrain.email.dto.TaEtiquetteEmailDTO;
import fr.legrain.email.model.TaEtiquetteEmail;
import fr.legrain.email.model.TaEtiquetteEmail;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEtiquetteEmailService extends AbstractApplicationDAOServer<TaEtiquetteEmail, TaEtiquetteEmailDTO> implements ITaEtiquetteEmailServiceRemote {

	static Logger logger = Logger.getLogger(TaEtiquetteEmailService.class);

	@Inject private IEtiquetteEmailDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaEtiquetteEmailService() {
		super(TaEtiquetteEmail.class,TaEtiquetteEmailDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaEtiquetteEmail a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaEtiquetteEmailDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaEtiquetteEmailDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaEtiquetteEmail transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEtiquetteEmail transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEtiquetteEmail persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdEtiquetteEmail()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaEtiquetteEmail merge(TaEtiquetteEmail detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEtiquetteEmail merge(TaEtiquetteEmail detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEtiquetteEmail findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEtiquetteEmail findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEtiquetteEmail> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaEtiquetteEmailDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaEtiquetteEmailDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEtiquetteEmail> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaEtiquetteEmailDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaEtiquetteEmailDTO entityToDTO(TaEtiquetteEmail entity) {
//		TaEtiquetteEmailDTO dto = new TaEtiquetteEmailDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEtiquetteEmailMapper mapper = new TaEtiquetteEmailMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaEtiquetteEmailDTO> listEntityToListDTO(List<TaEtiquetteEmail> entity) {
		List<TaEtiquetteEmailDTO> l = new ArrayList<TaEtiquetteEmailDTO>();

		for (TaEtiquetteEmail taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaEtiquetteEmailDTO> selectAllDTO() {
		System.out.println("List of TaEtiquetteEmailDTO EJB :");
		ArrayList<TaEtiquetteEmailDTO> liste = new ArrayList<TaEtiquetteEmailDTO>();

		List<TaEtiquetteEmail> projects = selectAll();
		for(TaEtiquetteEmail project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaEtiquetteEmailDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaEtiquetteEmailDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaEtiquetteEmailDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaEtiquetteEmailDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaEtiquetteEmailDTO dto, String validationContext) throws EJBException {
		try {
			TaEtiquetteEmailMapper mapper = new TaEtiquetteEmailMapper();
			TaEtiquetteEmail entity = null;
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
	
	public void persistDTO(TaEtiquetteEmailDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaEtiquetteEmailDTO dto, String validationContext) throws CreateException {
		try {
			TaEtiquetteEmailMapper mapper = new TaEtiquetteEmailMapper();
			TaEtiquetteEmail entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaEtiquetteEmailDTO dto) throws RemoveException {
		try {
			TaEtiquetteEmailMapper mapper = new TaEtiquetteEmailMapper();
			TaEtiquetteEmail entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEtiquetteEmail refresh(TaEtiquetteEmail persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEtiquetteEmail value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEtiquetteEmail value, String propertyName, String validationContext) {
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
	public void validateDTO(TaEtiquetteEmailDTO dto, String validationContext) {
		try {
			TaEtiquetteEmailMapper mapper = new TaEtiquetteEmailMapper();
			TaEtiquetteEmail entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEtiquetteEmailDTO> validator = new BeanValidator<TaEtiquetteEmailDTO>(TaEtiquetteEmailDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaEtiquetteEmailDTO dto, String propertyName, String validationContext) {
		try {
			TaEtiquetteEmailMapper mapper = new TaEtiquetteEmailMapper();
			TaEtiquetteEmail entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEtiquetteEmailDTO> validator = new BeanValidator<TaEtiquetteEmailDTO>(TaEtiquetteEmailDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaEtiquetteEmailDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaEtiquetteEmailDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEtiquetteEmail value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEtiquetteEmail value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
