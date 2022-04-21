package fr.legrain.tiers.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaParamEspaceClientMapper;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IAdresseDAO;
import fr.legrain.tiers.dao.IEspaceClientDAO;
import fr.legrain.tiers.dao.IParamEspaceClientDAO;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.model.TaParamEspaceClient;

/**
 * Session Bean implementation class TaParamEspaceClientBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaParamEspaceClientService extends AbstractApplicationDAOServer<TaParamEspaceClient, TaParamEspaceClientDTO> implements ITaParamEspaceClientServiceRemote {

	static Logger logger = Logger.getLogger(TaParamEspaceClientService.class);

	@Inject private IParamEspaceClientDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaParamEspaceClientService() {
		super(TaParamEspaceClient.class,TaParamEspaceClientDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaParamEspaceClient a";
	
	public TaParamEspaceClient findInstance() {
		return dao.findInstance();
	}
	
	public TaParamEspaceClientDTO findInstanceDTO() {
		TaParamEspaceClientDTO dto = entityToDTO(dao.findInstance());
		TaInfoEntreprise taInfoEntreprise = entrepriseService.findInstance();
		dto.setRaisonSociale(taInfoEntreprise.getNomInfoEntreprise());
		dto.setContactEmail(taInfoEntreprise.getEmailInfoEntreprise());
		dto.setContactWeb(taInfoEntreprise.getWebInfoEntreprise());
		dto.setContactTel(taInfoEntreprise.getTelInfoEntreprise());
		dto.setAdresse1(taInfoEntreprise.getAdresse1InfoEntreprise());
		dto.setAdresse2(taInfoEntreprise.getAdresse2InfoEntreprise());
		dto.setAdresse3(taInfoEntreprise.getAdresse3InfoEntreprise());
		dto.setCodePostal(taInfoEntreprise.getCodepostalInfoEntreprise());
		dto.setVille(taInfoEntreprise.getVilleInfoEntreprise());
		dto.setPays(taInfoEntreprise.getPaysInfoEntreprise());
		return dto;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaParamEspaceClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaParamEspaceClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaParamEspaceClient persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdParamEspaceClient()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaParamEspaceClient merge(TaParamEspaceClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaParamEspaceClient merge(TaParamEspaceClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaParamEspaceClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaParamEspaceClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaParamEspaceClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaParamEspaceClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaParamEspaceClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaParamEspaceClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaParamEspaceClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaParamEspaceClientDTO entityToDTO(TaParamEspaceClient entity) {
//		TaParamEspaceClientDTO dto = new TaParamEspaceClientDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaParamEspaceClientMapper mapper = new TaParamEspaceClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaParamEspaceClientDTO> listEntityToListDTO(List<TaParamEspaceClient> entity) {
		List<TaParamEspaceClientDTO> l = new ArrayList<TaParamEspaceClientDTO>();

		for (TaParamEspaceClient taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaParamEspaceClientDTO> selectAllDTO() {
		System.out.println("List of TaParamEspaceClientDTO EJB :");
		ArrayList<TaParamEspaceClientDTO> liste = new ArrayList<TaParamEspaceClientDTO>();

		List<TaParamEspaceClient> projects = selectAll();
		for(TaParamEspaceClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaParamEspaceClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaParamEspaceClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaParamEspaceClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaParamEspaceClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaParamEspaceClientDTO dto, String validationContext) throws EJBException {
		try {
			TaParamEspaceClientMapper mapper = new TaParamEspaceClientMapper();
			TaParamEspaceClient entity = null;
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
	
	public void persistDTO(TaParamEspaceClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaParamEspaceClientDTO dto, String validationContext) throws CreateException {
		try {
			TaParamEspaceClientMapper mapper = new TaParamEspaceClientMapper();
			TaParamEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaParamEspaceClientDTO dto) throws RemoveException {
		try {
			TaParamEspaceClientMapper mapper = new TaParamEspaceClientMapper();
			TaParamEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaParamEspaceClient refresh(TaParamEspaceClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaParamEspaceClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaParamEspaceClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaParamEspaceClientDTO dto, String validationContext) {
		try {
			TaParamEspaceClientMapper mapper = new TaParamEspaceClientMapper();
			TaParamEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamEspaceClientDTO> validator = new BeanValidator<TaParamEspaceClientDTO>(TaParamEspaceClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaParamEspaceClientDTO dto, String propertyName, String validationContext) {
		try {
			TaParamEspaceClientMapper mapper = new TaParamEspaceClientMapper();
			TaParamEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamEspaceClientDTO> validator = new BeanValidator<TaParamEspaceClientDTO>(TaParamEspaceClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaParamEspaceClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaParamEspaceClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaParamEspaceClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaParamEspaceClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}


}
