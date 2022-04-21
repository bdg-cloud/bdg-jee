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

import fr.legrain.bdg.edition.service.remote.ITaTEditionCatalogueServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTEditionCatalogueMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.edition.model.TaTEditionCatalogue;
import fr.legrain.edition.dao.ITEditionCatalogueDAO;
import fr.legrain.edition.dao.ITEditionDAO;
import fr.legrain.edition.dto.TaTEditionCatalogueDTO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTEditionCatalogueService extends AbstractApplicationDAOServer<TaTEditionCatalogue, TaTEditionCatalogueDTO> implements ITaTEditionCatalogueServiceRemote {

	static Logger logger = Logger.getLogger(TaTEditionCatalogueService.class);

	@Inject private ITEditionCatalogueDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTEditionCatalogueService() {
		super(TaTEditionCatalogue.class,TaTEditionCatalogueDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTEditionCatalogue a";
	
//	public List<TaTEditionCatalogueDTO> findAllLight() {
//		return dao.findAllLight();
//	}
//	
//	public List<TaTEditionCatalogue> selectAll(int idTiers) {
//		return dao.selectAll(idTiers);
//	}
	
//	public List<TaTEditionCatalogueDTO> selectAllDTO(int idTiers) {
//		System.out.println("List of TaTEditionCatalogueDTO EJB :");
//		ArrayList<TaTEditionCatalogueDTO> liste = new ArrayList<TaTEditionCatalogueDTO>();
//
//		List<TaTEditionCatalogue> projects = selectAll(idTiers);
//		for(TaTEditionCatalogue project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaTEditionCatalogueDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaTEditionCatalogueDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaTEditionCatalogue transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTEditionCatalogue transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTEditionCatalogue persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTEdition()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTEditionCatalogue merge(TaTEditionCatalogue detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTEditionCatalogue merge(TaTEditionCatalogue detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTEditionCatalogue findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTEditionCatalogue findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTEditionCatalogue> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTEditionCatalogueDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTEditionCatalogueDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTEditionCatalogue> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTEditionCatalogueDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTEditionCatalogueDTO entityToDTO(TaTEditionCatalogue entity) {
//		TaTEditionCatalogueDTO dto = new TaTEditionCatalogueDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTEditionCatalogueMapper mapper = new TaTEditionCatalogueMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTEditionCatalogueDTO> listEntityToListDTO(List<TaTEditionCatalogue> entity) {
		List<TaTEditionCatalogueDTO> l = new ArrayList<TaTEditionCatalogueDTO>();

		for (TaTEditionCatalogue taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTEditionCatalogueDTO> selectAllDTO() {
		System.out.println("List of TaTEditionCatalogueDTO EJB :");
		ArrayList<TaTEditionCatalogueDTO> liste = new ArrayList<TaTEditionCatalogueDTO>();

		List<TaTEditionCatalogue> projects = selectAll();
		for(TaTEditionCatalogue project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTEditionCatalogueDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTEditionCatalogueDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTEditionCatalogueDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTEditionCatalogueDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTEditionCatalogueDTO dto, String validationContext) throws EJBException {
		try {
			TaTEditionCatalogueMapper mapper = new TaTEditionCatalogueMapper();
			TaTEditionCatalogue entity = null;
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
	
	public void persistDTO(TaTEditionCatalogueDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTEditionCatalogueDTO dto, String validationContext) throws CreateException {
		try {
			TaTEditionCatalogueMapper mapper = new TaTEditionCatalogueMapper();
			TaTEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTEditionCatalogueDTO dto) throws RemoveException {
		try {
			TaTEditionCatalogueMapper mapper = new TaTEditionCatalogueMapper();
			TaTEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTEditionCatalogue refresh(TaTEditionCatalogue persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTEditionCatalogue value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTEditionCatalogue value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTEditionCatalogueDTO dto, String validationContext) {
		try {
			TaTEditionCatalogueMapper mapper = new TaTEditionCatalogueMapper();
			TaTEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEditionCatalogueDTO> validator = new BeanValidator<TaTEditionCatalogueDTO>(TaTEditionCatalogueDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTEditionCatalogueDTO dto, String propertyName, String validationContext) {
		try {
			TaTEditionCatalogueMapper mapper = new TaTEditionCatalogueMapper();
			TaTEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEditionCatalogueDTO> validator = new BeanValidator<TaTEditionCatalogueDTO>(TaTEditionCatalogueDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTEditionCatalogueDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTEditionCatalogueDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTEditionCatalogue value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTEditionCatalogue value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
