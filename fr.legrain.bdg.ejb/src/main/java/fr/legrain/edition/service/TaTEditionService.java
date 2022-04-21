package fr.legrain.edition.service;

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

import fr.legrain.bdg.edition.service.remote.ITaTEditionServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTEditionMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.edition.model.TaTEdition;
import fr.legrain.edition.dao.ITEditionDAO;
import fr.legrain.edition.dto.TaTEditionDTO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTEditionService extends AbstractApplicationDAOServer<TaTEdition, TaTEditionDTO> implements ITaTEditionServiceRemote {

	static Logger logger = Logger.getLogger(TaTEditionService.class);

	@Inject private ITEditionDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTEditionService() {
		super(TaTEdition.class,TaTEditionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTEdition a";
	
//	public List<TaTEditionDTO> findAllLight() {
//		return dao.findAllLight();
//	}
//	
//	public List<TaTEdition> selectAll(int idTiers) {
//		return dao.selectAll(idTiers);
//	}
	
//	public List<TaTEditionDTO> selectAllDTO(int idTiers) {
//		System.out.println("List of TaTEditionDTO EJB :");
//		ArrayList<TaTEditionDTO> liste = new ArrayList<TaTEditionDTO>();
//
//		List<TaTEdition> projects = selectAll(idTiers);
//		for(TaTEdition project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaTEditionDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaTEditionDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaTEdition transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTEdition transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTEdition persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTEdition()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTEdition merge(TaTEdition detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTEdition merge(TaTEdition detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTEdition findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTEdition findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTEdition> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTEditionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTEditionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTEdition> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTEditionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTEditionDTO entityToDTO(TaTEdition entity) {
//		TaTEditionDTO dto = new TaTEditionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTEditionMapper mapper = new TaTEditionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTEditionDTO> listEntityToListDTO(List<TaTEdition> entity) {
		List<TaTEditionDTO> l = new ArrayList<TaTEditionDTO>();

		for (TaTEdition taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTEditionDTO> selectAllDTO() {
		System.out.println("List of TaTEditionDTO EJB :");
		ArrayList<TaTEditionDTO> liste = new ArrayList<TaTEditionDTO>();

		List<TaTEdition> projects = selectAll();
		for(TaTEdition project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTEditionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTEditionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTEditionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTEditionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTEditionDTO dto, String validationContext) throws EJBException {
		try {
			TaTEditionMapper mapper = new TaTEditionMapper();
			TaTEdition entity = null;
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
	
	public void persistDTO(TaTEditionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTEditionDTO dto, String validationContext) throws CreateException {
		try {
			TaTEditionMapper mapper = new TaTEditionMapper();
			TaTEdition entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTEditionDTO dto) throws RemoveException {
		try {
			TaTEditionMapper mapper = new TaTEditionMapper();
			TaTEdition entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTEdition refresh(TaTEdition persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTEdition value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTEdition value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTEditionDTO dto, String validationContext) {
		try {
			TaTEditionMapper mapper = new TaTEditionMapper();
			TaTEdition entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEditionDTO> validator = new BeanValidator<TaTEditionDTO>(TaTEditionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTEditionDTO dto, String propertyName, String validationContext) {
		try {
			TaTEditionMapper mapper = new TaTEditionMapper();
			TaTEdition entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEditionDTO> validator = new BeanValidator<TaTEditionDTO>(TaTEditionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTEditionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTEditionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTEdition value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTEdition value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
