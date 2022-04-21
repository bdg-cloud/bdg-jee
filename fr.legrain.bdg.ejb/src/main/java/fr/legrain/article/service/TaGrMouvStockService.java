package fr.legrain.article.service;

import java.util.ArrayList;
import java.util.Date;
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

import fr.legrain.article.dao.IGrMouvStockDAO;
import fr.legrain.bdg.article.service.remote.ITaGrMouvStockServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaGrMouvStockMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.dto.GrMouvStockDTO;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaInventaire;
import fr.legrain.stock.model.TaMouvementStock;


/**
 * Session Bean implementation class TaGrMouvStockBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaGrMouvStockService extends AbstractApplicationDAOServer<TaGrMouvStock, GrMouvStockDTO> implements ITaGrMouvStockServiceRemote {

	static Logger logger = Logger.getLogger(TaGrMouvStockService.class);

	@Inject private IGrMouvStockDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private	SessionInfo sessionInfo;
	
	/**
	 * Default constructor. 
	 */
	public TaGrMouvStockService() {
		super(TaGrMouvStock.class,GrMouvStockDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaGrMouvStock a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaGrMouvStock transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaGrMouvStock transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaGrMouvStock persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdGrMouvStock()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaGrMouvStock merge(TaGrMouvStock detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaGrMouvStock merge(TaGrMouvStock detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		detachedInstance=dao.merge(detachedInstance);
		annuleCode(detachedInstance.getCodeGrMouvStock());
		return detachedInstance;
	}
	public TaGrMouvStock findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaGrMouvStock findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaGrMouvStock> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<GrMouvStockDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<GrMouvStockDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaGrMouvStock> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<GrMouvStockDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public GrMouvStockDTO entityToDTO(TaGrMouvStock entity) {
//		GrMouvStockDTO dto = new GrMouvStockDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaGrMouvStockMapper mapper = new TaGrMouvStockMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<GrMouvStockDTO> listEntityToListDTO(List<TaGrMouvStock> entity) {
		List<GrMouvStockDTO> l = new ArrayList<GrMouvStockDTO>();

		for (TaGrMouvStock taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<GrMouvStockDTO> selectAllDTO() {
		System.out.println("List of GrMouvStockDTO EJB :");
		ArrayList<GrMouvStockDTO> liste = new ArrayList<GrMouvStockDTO>();

		List<TaGrMouvStock> projects = selectAll();
		for(TaGrMouvStock project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public GrMouvStockDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public GrMouvStockDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(GrMouvStockDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(GrMouvStockDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(GrMouvStockDTO dto, String validationContext) throws EJBException {
		try {
			TaGrMouvStockMapper mapper = new TaGrMouvStockMapper();
			TaGrMouvStock entity = null;
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
	
	public void persistDTO(GrMouvStockDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(GrMouvStockDTO dto, String validationContext) throws CreateException {
		try {
			TaGrMouvStockMapper mapper = new TaGrMouvStockMapper();
			TaGrMouvStock entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(GrMouvStockDTO dto) throws RemoveException {
		try {
			TaGrMouvStockMapper mapper = new TaGrMouvStockMapper();
			TaGrMouvStock entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaGrMouvStock refresh(TaGrMouvStock persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaGrMouvStock value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaGrMouvStock value, String propertyName, String validationContext) {
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
	public void validateDTO(GrMouvStockDTO dto, String validationContext) {
		try {
			validateDTOAll(dto, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(GrMouvStockDTO dto, String propertyName, String validationContext)  throws BusinessValidationException {
		try {
			if(validationContext==null) validationContext="";
			validateDTO(dto, propertyName, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(GrMouvStockDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(GrMouvStockDTO dto, String propertyName)  throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaGrMouvStock value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaGrMouvStock value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaMouvementStock> findAllWithDates(Date dateDebut, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.findAllWithDates(dateDebut, dateFin);
	}
	public List<GrMouvStockDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<GrMouvStockDTO> findAllLight() {
		return dao.findAllLight();
	}

	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaGrMouvStock.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}

	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaGrMouvStock.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaGrMouvStock.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
