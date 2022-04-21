package fr.legrain.article.service;

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

import fr.legrain.article.dao.IInventaireDAO;
import fr.legrain.bdg.article.service.remote.ITaInventaireServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInventaireMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaFacture;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.dto.InventaireDTO;
import fr.legrain.stock.model.TaInventaire;


/**
 * Session Bean implementation class TaInventaireBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInventaireService extends AbstractApplicationDAOServer<TaInventaire, InventaireDTO> implements ITaInventaireServiceRemote {

	static Logger logger = Logger.getLogger(TaInventaireService.class);

	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private IInventaireDAO dao;
	@Inject private	SessionInfo sessionInfo;
	/**
	 * Default constructor. 
	 */
	public TaInventaireService() {
		super(TaInventaire.class,InventaireDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInventaire a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInventaire transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInventaire transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInventaire persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdInventaire()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaInventaire merge(TaInventaire detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInventaire merge(TaInventaire detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		detachedInstance=dao.merge(detachedInstance);
		annuleCode(detachedInstance.getCodeInventaire());
		return detachedInstance;
	}

	public TaInventaire findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInventaire findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInventaire> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<InventaireDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<InventaireDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInventaire> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<InventaireDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public InventaireDTO entityToDTO(TaInventaire entity) {
//		InventaireDTO dto = new InventaireDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInventaireMapper mapper = new TaInventaireMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<InventaireDTO> listEntityToListDTO(List<TaInventaire> entity) {
		List<InventaireDTO> l = new ArrayList<InventaireDTO>();

		for (TaInventaire taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<InventaireDTO> selectAllDTO() {
		System.out.println("List of InventaireDTO EJB :");
		ArrayList<InventaireDTO> liste = new ArrayList<InventaireDTO>();

		List<TaInventaire> projects = selectAll();
		for(TaInventaire project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public InventaireDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public InventaireDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(InventaireDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(InventaireDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(InventaireDTO dto, String validationContext) throws EJBException {
		try {
			TaInventaireMapper mapper = new TaInventaireMapper();
			TaInventaire entity = null;
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
	
	public void persistDTO(InventaireDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(InventaireDTO dto, String validationContext) throws CreateException {
		try {
			TaInventaireMapper mapper = new TaInventaireMapper();
			TaInventaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(InventaireDTO dto) throws RemoveException {
		try {
			TaInventaireMapper mapper = new TaInventaireMapper();
			TaInventaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInventaire refresh(TaInventaire persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInventaire value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInventaire value, String propertyName, String validationContext) {
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
	public void validateDTO(InventaireDTO dto, String validationContext) {
		try {
			TaInventaireMapper mapper = new TaInventaireMapper();
			TaInventaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<InventaireDTO> validator = new BeanValidator<InventaireDTO>(InventaireDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(InventaireDTO dto, String propertyName, String validationContext) {
		try {
			TaInventaireMapper mapper = new TaInventaireMapper();
			TaInventaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<InventaireDTO> validator = new BeanValidator<InventaireDTO>(InventaireDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(InventaireDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(InventaireDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInventaire value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInventaire value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	@Override
	public String genereCode( Map<String, String> params){
		try {
			return gencode.genereCodeJPA(TaInventaire.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaInventaire.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaInventaire.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public InventaireDTO findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<InventaireDTO> findAllLight() {
		return dao.findAllLight();
	}
}
