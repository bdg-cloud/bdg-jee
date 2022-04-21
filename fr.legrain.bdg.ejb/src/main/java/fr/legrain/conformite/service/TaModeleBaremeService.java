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

import fr.legrain.bdg.conformite.service.remote.ITaModeleBaremeServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaModeleBaremeMapper;
import fr.legrain.conformite.dao.IModeleBaremeDAO;
import fr.legrain.conformite.dto.TaModeleBaremeDTO;
import fr.legrain.conformite.model.TaModeleBareme;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaFacture;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaModeleBaremeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaModeleBaremeService extends AbstractApplicationDAOServer<TaModeleBareme, TaModeleBaremeDTO> implements ITaModeleBaremeServiceRemote {

	static Logger logger = Logger.getLogger(TaModeleBaremeService.class);

	@Inject private IModeleBaremeDAO dao;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaGenCodeExServiceRemote gencode;

	/**
	 * Default constructor. 
	 */
	public TaModeleBaremeService() {
		super(TaModeleBareme.class,TaModeleBaremeDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaModeleBareme a";
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaModeleBareme.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaModeleBareme.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaModeleBareme.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaModeleBareme transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaModeleBareme transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaModeleBareme persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdModeleBareme()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaModeleBareme merge(TaModeleBareme detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaModeleBareme merge(TaModeleBareme detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaModeleBareme findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaModeleBareme findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaModeleBareme> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaModeleBaremeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaModeleBaremeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaModeleBareme> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaModeleBaremeDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaModeleBaremeDTO entityToDTO(TaModeleBareme entity) {
//		TaModeleBaremeDTO dto = new TaModeleBaremeDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaModeleBaremeMapper mapper = new TaModeleBaremeMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaModeleBaremeDTO> listEntityToListDTO(List<TaModeleBareme> entity) {
		List<TaModeleBaremeDTO> l = new ArrayList<TaModeleBaremeDTO>();

		for (TaModeleBareme taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaModeleBaremeDTO> selectAllDTO() {
		System.out.println("List of TaModeleBaremeDTO EJB :");
		ArrayList<TaModeleBaremeDTO> liste = new ArrayList<TaModeleBaremeDTO>();

		List<TaModeleBareme> projects = selectAll();
		for(TaModeleBareme project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaModeleBaremeDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaModeleBaremeDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaModeleBaremeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaModeleBaremeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaModeleBaremeDTO dto, String validationContext) throws EJBException {
		try {
			TaModeleBaremeMapper mapper = new TaModeleBaremeMapper();
			TaModeleBareme entity = null;
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
	
	public void persistDTO(TaModeleBaremeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaModeleBaremeDTO dto, String validationContext) throws CreateException {
		try {
			TaModeleBaremeMapper mapper = new TaModeleBaremeMapper();
			TaModeleBareme entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaModeleBaremeDTO dto) throws RemoveException {
		try {
			TaModeleBaremeMapper mapper = new TaModeleBaremeMapper();
			TaModeleBareme entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaModeleBareme refresh(TaModeleBareme persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaModeleBareme value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaModeleBareme value, String propertyName, String validationContext) {
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
	public void validateDTO(TaModeleBaremeDTO dto, String validationContext) {
		try {
			TaModeleBaremeMapper mapper = new TaModeleBaremeMapper();
			TaModeleBareme entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleBaremeDTO> validator = new BeanValidator<TaModeleBaremeDTO>(TaModeleBaremeDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaModeleBaremeDTO dto, String propertyName, String validationContext) {
		try {
			TaModeleBaremeMapper mapper = new TaModeleBaremeMapper();
			TaModeleBareme entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleBaremeDTO> validator = new BeanValidator<TaModeleBaremeDTO>(TaModeleBaremeDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaModeleBaremeDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaModeleBaremeDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaModeleBareme value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaModeleBareme value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
