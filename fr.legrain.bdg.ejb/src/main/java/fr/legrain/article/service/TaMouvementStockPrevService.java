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

import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dao.IMouvementPrevDAO;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockPrevServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaMouvementStockPrevMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.dto.MouvementStocksPrevDTO;
import fr.legrain.stock.model.TaMouvementStockPrev;


/**
 * Session Bean implementation class TaMouvementStockPrevBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaMouvementStockPrevService extends AbstractApplicationDAOServer<TaMouvementStockPrev, MouvementStocksPrevDTO> implements ITaMouvementStockPrevServiceRemote {

	static Logger logger = Logger.getLogger(TaMouvementStockPrevService.class);

	@Inject private IMouvementPrevDAO dao;
	@Inject private IArticleDAO daoArticle;

	/**
	 * Default constructor. 
	 */
	public TaMouvementStockPrevService() {
		super(TaMouvementStockPrev.class,MouvementStocksPrevDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaMouvementStockPrev a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaMouvementStockPrev transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaMouvementStockPrev transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaMouvementStockPrev persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdMouvementStock()));
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public TaMouvementStockPrev merge(TaMouvementStockPrev detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaMouvementStockPrev merge(TaMouvementStockPrev detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaMouvementStockPrev findById(int id) throws FinderException {
		return dao.findById(id);
	}


	public TaMouvementStockPrev findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaMouvementStockPrev> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<MouvementStocksPrevDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<MouvementStocksPrevDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaMouvementStockPrev> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<MouvementStocksPrevDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public MouvementStocksPrevDTO entityToDTO(TaMouvementStockPrev entity) {
//		MouvementStocksPrevDTO dto = new MouvementStocksPrevDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaMouvementStockPrevMapper mapper = new TaMouvementStockPrevMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<MouvementStocksPrevDTO> listEntityToListDTO(List<TaMouvementStockPrev> entity) {
		List<MouvementStocksPrevDTO> l = new ArrayList<MouvementStocksPrevDTO>();

		for (TaMouvementStockPrev taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<MouvementStocksPrevDTO> selectAllDTO() {
		System.out.println("List of MouvementStocksPrevDTO EJB :");
		ArrayList<MouvementStocksPrevDTO> liste = new ArrayList<MouvementStocksPrevDTO>();

		List<TaMouvementStockPrev> projects = selectAll();
		for(TaMouvementStockPrev project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public MouvementStocksPrevDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public MouvementStocksPrevDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(MouvementStocksPrevDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(MouvementStocksPrevDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(MouvementStocksPrevDTO dto, String validationContext) throws EJBException {
		try {
			TaMouvementStockPrevMapper mapper = new TaMouvementStockPrevMapper();
			TaMouvementStockPrev entity = null;
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
	
	public void persistDTO(MouvementStocksPrevDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(MouvementStocksPrevDTO dto, String validationContext) throws CreateException {
		try {
			TaMouvementStockPrevMapper mapper = new TaMouvementStockPrevMapper();
			TaMouvementStockPrev entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(MouvementStocksPrevDTO dto) throws RemoveException {
		try {
			TaMouvementStockPrevMapper mapper = new TaMouvementStockPrevMapper();
			TaMouvementStockPrev entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}


	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaMouvementStockPrev value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaMouvementStockPrev value, String propertyName, String validationContext) {
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
	public void validateDTO(MouvementStocksPrevDTO dto, String validationContext) {
		try {
			validateDTOAll(dto, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(MouvementStocksPrevDTO dto, String propertyName, String validationContext)throws BusinessValidationException {
		try {
			if(validationContext==null) validationContext="";
			validateDTO(dto, propertyName, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
//			throw new EJBException(e.getMessage());
			throw new BusinessValidationException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(MouvementStocksPrevDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(MouvementStocksPrevDTO dto, String propertyName) throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaMouvementStockPrev value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaMouvementStockPrev value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	protected TaMouvementStockPrev refresh(TaMouvementStockPrev persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
