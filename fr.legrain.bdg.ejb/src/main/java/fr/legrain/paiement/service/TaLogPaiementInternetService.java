package fr.legrain.paiement.service;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaLogPaiementInternetMapper;
import fr.legrain.bdg.paiement.service.remote.ITaLogPaiementInternetServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;
import fr.legrain.paiement.dto.TaLogPaiementInternetDTO;
import fr.legrain.paiement.model.TaLogPaiementInternet;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLogPaiementInternetService extends AbstractApplicationDAOServer<TaLogPaiementInternet, TaLogPaiementInternetDTO> implements ITaLogPaiementInternetServiceRemote {

	static Logger logger = Logger.getLogger(TaLogPaiementInternetService.class);

	@Inject private ILogPaiementInternetDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLogPaiementInternetService() {
		super(TaLogPaiementInternet.class,TaLogPaiementInternetDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLogPaiementInternet a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaLogPaiementInternet transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLogPaiementInternet transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLogPaiementInternet persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLogPaiementInternet()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLogPaiementInternet merge(TaLogPaiementInternet detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLogPaiementInternet merge(TaLogPaiementInternet detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLogPaiementInternet findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLogPaiementInternet findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLogPaiementInternet> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLogPaiementInternetDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLogPaiementInternetDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLogPaiementInternet> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLogPaiementInternetDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLogPaiementInternetDTO entityToDTO(TaLogPaiementInternet entity) {
//		TaLogPaiementInternetDTO dto = new TaLogPaiementInternetDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLogPaiementInternetMapper mapper = new TaLogPaiementInternetMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLogPaiementInternetDTO> listEntityToListDTO(List<TaLogPaiementInternet> entity) {
		List<TaLogPaiementInternetDTO> l = new ArrayList<TaLogPaiementInternetDTO>();

		for (TaLogPaiementInternet taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLogPaiementInternetDTO> selectAllDTO() {
		System.out.println("List of TaLogPaiementInternetDTO EJB :");
		ArrayList<TaLogPaiementInternetDTO> liste = new ArrayList<TaLogPaiementInternetDTO>();

		List<TaLogPaiementInternet> projects = selectAll();
		for(TaLogPaiementInternet project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLogPaiementInternetDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLogPaiementInternetDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLogPaiementInternetDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLogPaiementInternetDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLogPaiementInternetDTO dto, String validationContext) throws EJBException {
		try {
			TaLogPaiementInternetMapper mapper = new TaLogPaiementInternetMapper();
			TaLogPaiementInternet entity = null;
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
	
	public void persistDTO(TaLogPaiementInternetDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLogPaiementInternetDTO dto, String validationContext) throws CreateException {
		try {
			TaLogPaiementInternetMapper mapper = new TaLogPaiementInternetMapper();
			TaLogPaiementInternet entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLogPaiementInternetDTO dto) throws RemoveException {
		try {
			TaLogPaiementInternetMapper mapper = new TaLogPaiementInternetMapper();
			TaLogPaiementInternet entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLogPaiementInternet refresh(TaLogPaiementInternet persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLogPaiementInternet value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLogPaiementInternet value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLogPaiementInternetDTO dto, String validationContext) {
		try {
			TaLogPaiementInternetMapper mapper = new TaLogPaiementInternetMapper();
			TaLogPaiementInternet entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLogPaiementInternetDTO> validator = new BeanValidator<TaLogPaiementInternetDTO>(TaLogPaiementInternetDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLogPaiementInternetDTO dto, String propertyName, String validationContext) {
		try {
			TaLogPaiementInternetMapper mapper = new TaLogPaiementInternetMapper();
			TaLogPaiementInternet entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLogPaiementInternetDTO> validator = new BeanValidator<TaLogPaiementInternetDTO>(TaLogPaiementInternetDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLogPaiementInternetDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLogPaiementInternetDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLogPaiementInternet value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLogPaiementInternet value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
