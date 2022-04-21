package fr.legrain.conformite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.conformite.service.remote.ITaModeleConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaModeleConformiteMapper;
import fr.legrain.conformite.dao.IModeleConformiteDAO;
import fr.legrain.conformite.dto.TaModeleConformiteDTO;
import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaFacture;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaModeleConformiteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaModeleConformiteService extends AbstractApplicationDAOServer<TaModeleConformite, TaModeleConformiteDTO> implements ITaModeleConformiteServiceRemote {

	static Logger logger = Logger.getLogger(TaModeleConformiteService.class);

	@Inject private IModeleConformiteDAO dao;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaGenCodeExServiceRemote gencode;

	/**
	 * Default constructor. 
	 */
	public TaModeleConformiteService() {
		super(TaModeleConformite.class,TaModeleConformiteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaModeleConformite a";
	
	public List<TaModeleConformite> findByModeleGroupe(TaModeleGroupe taModeleGroupe) {
		return dao.findByModeleGroupe(taModeleGroupe);
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaModeleConformite.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaModeleConformite.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaModeleConformite.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaModeleConformite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaModeleConformite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaModeleConformite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdModeleConformite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaModeleConformite merge(TaModeleConformite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaModeleConformite merge(TaModeleConformite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaModeleConformite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaModeleConformite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaModeleConformite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaModeleConformiteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaModeleConformiteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaModeleConformite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaModeleConformiteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaModeleConformiteDTO entityToDTO(TaModeleConformite entity) {
//		TaModeleConformiteDTO dto = new TaModeleConformiteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaModeleConformiteMapper mapper = new TaModeleConformiteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaModeleConformiteDTO> listEntityToListDTO(List<TaModeleConformite> entity) {
		List<TaModeleConformiteDTO> l = new ArrayList<TaModeleConformiteDTO>();

		for (TaModeleConformite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaModeleConformiteDTO> selectAllDTO() {
		System.out.println("List of TaModeleConformiteDTO EJB :");
		ArrayList<TaModeleConformiteDTO> liste = new ArrayList<TaModeleConformiteDTO>();

		List<TaModeleConformite> projects = selectAll();
		for(TaModeleConformite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaModeleConformiteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaModeleConformiteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaModeleConformiteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaModeleConformiteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaModeleConformiteDTO dto, String validationContext) throws EJBException {
		try {
			TaModeleConformiteMapper mapper = new TaModeleConformiteMapper();
			TaModeleConformite entity = null;
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
	
	public void persistDTO(TaModeleConformiteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaModeleConformiteDTO dto, String validationContext) throws CreateException {
		try {
			TaModeleConformiteMapper mapper = new TaModeleConformiteMapper();
			TaModeleConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaModeleConformiteDTO dto) throws RemoveException {
		try {
			TaModeleConformiteMapper mapper = new TaModeleConformiteMapper();
			TaModeleConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaModeleConformite refresh(TaModeleConformite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaModeleConformite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaModeleConformite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaModeleConformiteDTO dto, String validationContext) {
		try {
			TaModeleConformiteMapper mapper = new TaModeleConformiteMapper();
			TaModeleConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleConformiteDTO> validator = new BeanValidator<TaModeleConformiteDTO>(TaModeleConformiteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaModeleConformiteDTO dto, String propertyName, String validationContext) {
		try {
			TaModeleConformiteMapper mapper = new TaModeleConformiteMapper();
			TaModeleConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleConformiteDTO> validator = new BeanValidator<TaModeleConformiteDTO>(TaModeleConformiteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaModeleConformiteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaModeleConformiteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaModeleConformite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaModeleConformite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
