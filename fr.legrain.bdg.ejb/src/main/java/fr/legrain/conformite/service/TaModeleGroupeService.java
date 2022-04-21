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

import fr.legrain.bdg.conformite.service.remote.ITaModeleGroupeServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaModeleGroupeMapper;
import fr.legrain.conformite.dao.IModeleGroupeDAO;
import fr.legrain.conformite.dto.TaModeleGroupeDTO;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaFacture;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaModeleGroupeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaModeleGroupeService extends AbstractApplicationDAOServer<TaModeleGroupe, TaModeleGroupeDTO> implements ITaModeleGroupeServiceRemote {

	static Logger logger = Logger.getLogger(TaModeleGroupeService.class);

	@Inject private IModeleGroupeDAO dao;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaGenCodeExServiceRemote gencode;

	/**
	 * Default constructor. 
	 */
	public TaModeleGroupeService() {
		super(TaModeleGroupe.class,TaModeleGroupeDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaModeleGroupe a";
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaModeleGroupe.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaModeleGroupe.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaModeleGroupe.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaModeleGroupe transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaModeleGroupe transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaModeleGroupe persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdModeleGroupe()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaModeleGroupe merge(TaModeleGroupe detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaModeleGroupe merge(TaModeleGroupe detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaModeleGroupe findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaModeleGroupe findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaModeleGroupe> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaModeleGroupeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaModeleGroupeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaModeleGroupe> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaModeleGroupeDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaModeleGroupeDTO entityToDTO(TaModeleGroupe entity) {
//		TaModeleGroupeDTO dto = new TaModeleGroupeDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaModeleGroupeMapper mapper = new TaModeleGroupeMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaModeleGroupeDTO> listEntityToListDTO(List<TaModeleGroupe> entity) {
		List<TaModeleGroupeDTO> l = new ArrayList<TaModeleGroupeDTO>();

		for (TaModeleGroupe taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaModeleGroupeDTO> selectAllDTO() {
		System.out.println("List of TaModeleGroupeDTO EJB :");
		ArrayList<TaModeleGroupeDTO> liste = new ArrayList<TaModeleGroupeDTO>();

		List<TaModeleGroupe> projects = selectAll();
		for(TaModeleGroupe project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaModeleGroupeDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaModeleGroupeDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaModeleGroupeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaModeleGroupeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaModeleGroupeDTO dto, String validationContext) throws EJBException {
		try {
			TaModeleGroupeMapper mapper = new TaModeleGroupeMapper();
			TaModeleGroupe entity = null;
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
	
	public void persistDTO(TaModeleGroupeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaModeleGroupeDTO dto, String validationContext) throws CreateException {
		try {
			TaModeleGroupeMapper mapper = new TaModeleGroupeMapper();
			TaModeleGroupe entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaModeleGroupeDTO dto) throws RemoveException {
		try {
			TaModeleGroupeMapper mapper = new TaModeleGroupeMapper();
			TaModeleGroupe entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaModeleGroupe refresh(TaModeleGroupe persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaModeleGroupe value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaModeleGroupe value, String propertyName, String validationContext) {
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
	public void validateDTO(TaModeleGroupeDTO dto, String validationContext) {
		try {
			TaModeleGroupeMapper mapper = new TaModeleGroupeMapper();
			TaModeleGroupe entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleGroupeDTO> validator = new BeanValidator<TaModeleGroupeDTO>(TaModeleGroupeDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaModeleGroupeDTO dto, String propertyName, String validationContext) {
		try {
			TaModeleGroupeMapper mapper = new TaModeleGroupeMapper();
			TaModeleGroupe entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleGroupeDTO> validator = new BeanValidator<TaModeleGroupeDTO>(TaModeleGroupeDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaModeleGroupeDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaModeleGroupeDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaModeleGroupe value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaModeleGroupe value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
