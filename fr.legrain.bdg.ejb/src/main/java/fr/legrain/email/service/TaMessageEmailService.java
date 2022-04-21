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

import fr.legrain.bdg.email.service.remote.ITaMessageEmailServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaMessageEmailMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.email.dao.IMessageEmailDAO;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaMessageEmailService extends AbstractApplicationDAOServer<TaMessageEmail, TaMessageEmailDTO> implements ITaMessageEmailServiceRemote {

	static Logger logger = Logger.getLogger(TaMessageEmailService.class);

	@Inject private IMessageEmailDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaMessageEmailService() {
		super(TaMessageEmail.class,TaMessageEmailDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaMessageEmail a";
	
	public List<TaMessageEmailDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	
	public List<TaMessageEmailDTO> findAllLight(int idTiers) {
		return dao.findAllLight(idTiers);
	}
	
	public List<TaMessageEmail> selectAll(int idTiers) {
		return dao.selectAll(idTiers);
	}
	
	public List<TaMessageEmailDTO> selectAllDTO(int idTiers) {
		System.out.println("List of TaMessageEmailDTO EJB :");
		ArrayList<TaMessageEmailDTO> liste = new ArrayList<TaMessageEmailDTO>();

		List<TaMessageEmail> projects = selectAll(idTiers);
		for(TaMessageEmail project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaMessageEmailDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaMessageEmailDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaMessageEmail transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaMessageEmail transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaMessageEmail persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdEmail()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaMessageEmail merge(TaMessageEmail detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaMessageEmail merge(TaMessageEmail detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaMessageEmail findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaMessageEmail findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaMessageEmail> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaMessageEmailDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaMessageEmailDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaMessageEmail> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaMessageEmailDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaMessageEmailDTO entityToDTO(TaMessageEmail entity) {
//		TaMessageEmailDTO dto = new TaMessageEmailDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaMessageEmailMapper mapper = new TaMessageEmailMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaMessageEmailDTO> listEntityToListDTO(List<TaMessageEmail> entity) {
		List<TaMessageEmailDTO> l = new ArrayList<TaMessageEmailDTO>();

		for (TaMessageEmail taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaMessageEmailDTO> selectAllDTO() {
		System.out.println("List of TaMessageEmailDTO EJB :");
		ArrayList<TaMessageEmailDTO> liste = new ArrayList<TaMessageEmailDTO>();

		List<TaMessageEmail> projects = selectAll();
		for(TaMessageEmail project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaMessageEmailDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaMessageEmailDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaMessageEmailDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaMessageEmailDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaMessageEmailDTO dto, String validationContext) throws EJBException {
		try {
			TaMessageEmailMapper mapper = new TaMessageEmailMapper();
			TaMessageEmail entity = null;
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
	
	public void persistDTO(TaMessageEmailDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaMessageEmailDTO dto, String validationContext) throws CreateException {
		try {
			TaMessageEmailMapper mapper = new TaMessageEmailMapper();
			TaMessageEmail entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaMessageEmailDTO dto) throws RemoveException {
		try {
			TaMessageEmailMapper mapper = new TaMessageEmailMapper();
			TaMessageEmail entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaMessageEmail refresh(TaMessageEmail persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaMessageEmail value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaMessageEmail value, String propertyName, String validationContext) {
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
	public void validateDTO(TaMessageEmailDTO dto, String validationContext) {
		try {
			TaMessageEmailMapper mapper = new TaMessageEmailMapper();
			TaMessageEmail entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaMessageEmailDTO> validator = new BeanValidator<TaMessageEmailDTO>(TaMessageEmailDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaMessageEmailDTO dto, String propertyName, String validationContext) {
		try {
			TaMessageEmailMapper mapper = new TaMessageEmailMapper();
			TaMessageEmail entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaMessageEmailDTO> validator = new BeanValidator<TaMessageEmailDTO>(TaMessageEmailDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaMessageEmailDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaMessageEmailDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaMessageEmail value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaMessageEmail value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
