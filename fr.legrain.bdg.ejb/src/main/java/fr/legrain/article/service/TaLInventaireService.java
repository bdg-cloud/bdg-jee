package fr.legrain.article.service;

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

import fr.legrain.article.dao.ILInventaireDAO;
import fr.legrain.article.dao.IMouvementDAO;
import fr.legrain.bdg.article.service.remote.ITaLInventaireServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLInventaireMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.dto.LInventaireDTO;
import fr.legrain.stock.model.TaLInventaire;


/**
 * Session Bean implementation class TaLInventaireBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLInventaireService extends AbstractApplicationDAOServer<TaLInventaire, LInventaireDTO> implements ITaLInventaireServiceRemote {

	static Logger logger = Logger.getLogger(TaLInventaireService.class);

	@Inject private ILInventaireDAO dao;
	@Inject private IMouvementDAO daoMouvement;

	/**
	 * Default constructor. 
	 */
	public TaLInventaireService() {
		super(TaLInventaire.class,LInventaireDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLInventaire a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLInventaire transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLInventaire transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}
	
	public void supprimer(TaLInventaire persistentInstance) throws Exception{
		daoMouvement.remove(persistentInstance.getTaMouvementStock());
		remove(persistentInstance);
	}

	public void remove(TaLInventaire persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLInventaire()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLInventaire merge(TaLInventaire detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLInventaire merge(TaLInventaire detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLInventaire findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLInventaire findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLInventaire> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<LInventaireDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<LInventaireDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLInventaire> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<LInventaireDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public LInventaireDTO entityToDTO(TaLInventaire entity) {
//		LInventaireDTO dto = new LInventaireDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLInventaireMapper mapper = new TaLInventaireMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<LInventaireDTO> listEntityToListDTO(List<TaLInventaire> entity) {
		List<LInventaireDTO> l = new ArrayList<LInventaireDTO>();

		for (TaLInventaire taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<LInventaireDTO> selectAllDTO() {
		System.out.println("List of LInventaireDTO EJB :");
		ArrayList<LInventaireDTO> liste = new ArrayList<LInventaireDTO>();

		List<TaLInventaire> projects = selectAll();
		for(TaLInventaire project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public LInventaireDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public LInventaireDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(LInventaireDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(LInventaireDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(LInventaireDTO dto, String validationContext) throws EJBException {
		try {
			TaLInventaireMapper mapper = new TaLInventaireMapper();
			TaLInventaire entity = null;
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
	
	public void persistDTO(LInventaireDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(LInventaireDTO dto, String validationContext) throws CreateException {
		try {
			TaLInventaireMapper mapper = new TaLInventaireMapper();
			TaLInventaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(LInventaireDTO dto) throws RemoveException {
		try {
			TaLInventaireMapper mapper = new TaLInventaireMapper();
			TaLInventaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLInventaire refresh(TaLInventaire persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLInventaire value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLInventaire value, String propertyName, String validationContext) {
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
	public void validateDTO(LInventaireDTO dto, String validationContext) {
		try {
			TaLInventaireMapper mapper = new TaLInventaireMapper();
			TaLInventaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<LInventaireDTO> validator = new BeanValidator<LInventaireDTO>(LInventaireDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(LInventaireDTO dto, String propertyName, String validationContext) {
		try {
			TaLInventaireMapper mapper = new TaLInventaireMapper();
			TaLInventaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<LInventaireDTO> validator = new BeanValidator<LInventaireDTO>(LInventaireDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(LInventaireDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(LInventaireDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLInventaire value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLInventaire value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<LInventaireDTO> findAllLight(Integer idInventaire) {
		// TODO Auto-generated method stub
		return dao.findAllLight(idInventaire);
	}

}
