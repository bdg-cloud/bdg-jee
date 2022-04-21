package fr.legrain.general.service;

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

import fr.legrain.bdg.general.service.remote.ITaLiaisonDossierMaitreServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLiaisonDossierMaitreMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.general.dao.ILiaisonDossierMaitreDAO;
import fr.legrain.general.dto.TaLiaisonDossierMaitreDTO;
import fr.legrain.general.model.TaLiaisonDossierMaitre;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IAdresseDAO;
import fr.legrain.tiers.dao.IEspaceClientDAO;


/**
 * Session Bean implementation class TaLiaisonDossierMaitreBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLiaisonDossierMaitreService extends AbstractApplicationDAOServer<TaLiaisonDossierMaitre, TaLiaisonDossierMaitreDTO> implements ITaLiaisonDossierMaitreServiceRemote {

	static Logger logger = Logger.getLogger(TaLiaisonDossierMaitreService.class);

	@Inject private ILiaisonDossierMaitreDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLiaisonDossierMaitreService() {
		super(TaLiaisonDossierMaitre.class,TaLiaisonDossierMaitreDTO.class);
	}
	
	public TaLiaisonDossierMaitre login(String login, String password) throws EJBException {
		return dao.login(login, password);
	}
	
	public TaLiaisonDossierMaitreDTO loginDTO(String login, String password) {
		return dao.loginDTO(login, password);
	}
	
	
	public List<TaLiaisonDossierMaitreDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public TaLiaisonDossierMaitre findInstance() {
		return dao.findInstance();
	}
	public TaLiaisonDossierMaitre findInstance(String email, String password,  String codeTiers) {
		return dao.findInstance(email, password,   codeTiers);
	}
	public void persist(TaLiaisonDossierMaitre transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLiaisonDossierMaitre transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLiaisonDossierMaitre persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLiaisonDossierMaitre()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLiaisonDossierMaitre merge(TaLiaisonDossierMaitre detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLiaisonDossierMaitre merge(TaLiaisonDossierMaitre detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLiaisonDossierMaitre findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLiaisonDossierMaitre findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLiaisonDossierMaitre> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLiaisonDossierMaitreDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLiaisonDossierMaitreDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLiaisonDossierMaitre> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLiaisonDossierMaitreDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLiaisonDossierMaitreDTO entityToDTO(TaLiaisonDossierMaitre entity) {
//		TaLiaisonDossierMaitreDTO dto = new TaLiaisonDossierMaitreDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLiaisonDossierMaitreMapper mapper = new TaLiaisonDossierMaitreMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLiaisonDossierMaitreDTO> listEntityToListDTO(List<TaLiaisonDossierMaitre> entity) {
		List<TaLiaisonDossierMaitreDTO> l = new ArrayList<TaLiaisonDossierMaitreDTO>();

		for (TaLiaisonDossierMaitre taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLiaisonDossierMaitreDTO> selectAllDTO() {
		System.out.println("List of TaLiaisonDossierMaitreDTO EJB :");
		ArrayList<TaLiaisonDossierMaitreDTO> liste = new ArrayList<TaLiaisonDossierMaitreDTO>();

		List<TaLiaisonDossierMaitre> projects = selectAll();
		for(TaLiaisonDossierMaitre project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLiaisonDossierMaitreDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLiaisonDossierMaitreDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLiaisonDossierMaitreDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLiaisonDossierMaitreDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLiaisonDossierMaitreDTO dto, String validationContext) throws EJBException {
		try {
			TaLiaisonDossierMaitreMapper mapper = new TaLiaisonDossierMaitreMapper();
			TaLiaisonDossierMaitre entity = null;
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
	
	public void persistDTO(TaLiaisonDossierMaitreDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLiaisonDossierMaitreDTO dto, String validationContext) throws CreateException {
		try {
			TaLiaisonDossierMaitreMapper mapper = new TaLiaisonDossierMaitreMapper();
			TaLiaisonDossierMaitre entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLiaisonDossierMaitreDTO dto) throws RemoveException {
		try {
			TaLiaisonDossierMaitreMapper mapper = new TaLiaisonDossierMaitreMapper();
			TaLiaisonDossierMaitre entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLiaisonDossierMaitre refresh(TaLiaisonDossierMaitre persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLiaisonDossierMaitre value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLiaisonDossierMaitre value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLiaisonDossierMaitreDTO dto, String validationContext) {
		try {
			TaLiaisonDossierMaitreMapper mapper = new TaLiaisonDossierMaitreMapper();
			TaLiaisonDossierMaitre entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLiaisonDossierMaitreDTO> validator = new BeanValidator<TaLiaisonDossierMaitreDTO>(TaLiaisonDossierMaitreDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLiaisonDossierMaitreDTO dto, String propertyName, String validationContext) {
		try {
			TaLiaisonDossierMaitreMapper mapper = new TaLiaisonDossierMaitreMapper();
			TaLiaisonDossierMaitre entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLiaisonDossierMaitreDTO> validator = new BeanValidator<TaLiaisonDossierMaitreDTO>(TaLiaisonDossierMaitreDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLiaisonDossierMaitreDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLiaisonDossierMaitreDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLiaisonDossierMaitre value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLiaisonDossierMaitre value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}


}
