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

import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaConformiteMapper;
import fr.legrain.conformite.dao.IConformiteDAO;
import fr.legrain.conformite.dto.TaConformiteDTO;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaFacture;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaConformiteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaConformiteService extends AbstractApplicationDAOServer<TaConformite, TaConformiteDTO> implements ITaConformiteServiceRemote {

	static Logger logger = Logger.getLogger(TaConformiteService.class);

	@Inject private IConformiteDAO dao;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaGenCodeExServiceRemote gencode;

	/**
	 * Default constructor. 
	 */
	public TaConformiteService() {
		super(TaConformite.class,TaConformiteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaConformite a";
	
	public Boolean controleUtiliseDansUnLot(int idControleConformite) {
		return dao.controleUtiliseDansUnLot(idControleConformite);
	}
	
	public List<TaConformite> controleArticleDerniereVersion(int idArticle) {
		return dao.controleArticleDerniereVersion(idArticle);
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaConformite.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaConformite.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaConformite.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaConformite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaConformite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaConformite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdConformite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaConformite merge(TaConformite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaConformite merge(TaConformite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaConformite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaConformite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaConformite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaConformiteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaConformiteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaConformite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaConformiteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaConformiteDTO entityToDTO(TaConformite entity) {
//		TaConformiteDTO dto = new TaConformiteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaConformiteMapper mapper = new TaConformiteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaConformiteDTO> listEntityToListDTO(List<TaConformite> entity) {
		List<TaConformiteDTO> l = new ArrayList<TaConformiteDTO>();

		for (TaConformite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaConformiteDTO> selectAllDTO() {
		System.out.println("List of TaConformiteDTO EJB :");
		ArrayList<TaConformiteDTO> liste = new ArrayList<TaConformiteDTO>();

		List<TaConformite> projects = selectAll();
		for(TaConformite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaConformiteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaConformiteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaConformiteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaConformiteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaConformiteDTO dto, String validationContext) throws EJBException {
		try {
			TaConformiteMapper mapper = new TaConformiteMapper();
			TaConformite entity = null;
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
	
	public void persistDTO(TaConformiteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaConformiteDTO dto, String validationContext) throws CreateException {
		try {
			TaConformiteMapper mapper = new TaConformiteMapper();
			TaConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaConformiteDTO dto) throws RemoveException {
		try {
			TaConformiteMapper mapper = new TaConformiteMapper();
			TaConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaConformite refresh(TaConformite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaConformite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaConformite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaConformiteDTO dto, String validationContext) {
		try {
			TaConformiteMapper mapper = new TaConformiteMapper();
			TaConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaConformiteDTO> validator = new BeanValidator<TaConformiteDTO>(TaConformiteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaConformiteDTO dto, String propertyName, String validationContext) {
		try {
			TaConformiteMapper mapper = new TaConformiteMapper();
			TaConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaConformiteDTO> validator = new BeanValidator<TaConformiteDTO>(TaConformiteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaConformiteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaConformiteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaConformite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaConformite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
