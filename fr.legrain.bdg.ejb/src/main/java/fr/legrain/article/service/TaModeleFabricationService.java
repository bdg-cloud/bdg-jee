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

import fr.legrain.article.dao.IModeleFabricationDAO;
import fr.legrain.article.dto.TaModeleFabricationDTO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaModeleFabrication;
import fr.legrain.bdg.article.service.remote.ITaModeleFabricationServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaModeleFabricationMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaModeleFabricationBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaModeleFabricationService extends AbstractApplicationDAOServer<TaModeleFabrication, TaModeleFabricationDTO> implements ITaModeleFabricationServiceRemote {

	static Logger logger = Logger.getLogger(TaModeleFabricationService.class);

	@Inject private IModeleFabricationDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	/**
	 * Default constructor. 
	 */
	public TaModeleFabricationService() {
		super(TaModeleFabrication.class,TaModeleFabricationDTO.class);
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaModeleFabrication.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String genereCode( Map<String, String> params){
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaModeleFabrication.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaModeleFabrication transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaModeleFabrication transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaModeleFabrication persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaModeleFabrication merge(TaModeleFabrication detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaModeleFabrication merge(TaModeleFabrication detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		detachedInstance=dao.merge(detachedInstance);
		annuleCode(detachedInstance.getCodeDocument());
		return detachedInstance;
	}

	public TaModeleFabrication findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaModeleFabrication findByIdWithList(int id) throws FinderException {
		TaModeleFabrication fab =  dao.findById(id);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}

	public TaModeleFabrication findByCodeWithList(String code)   {
		TaModeleFabrication fab =  dao.findByCode(code);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}
	public TaModeleFabrication findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaModeleFabrication> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaModeleFabricationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaModeleFabricationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaModeleFabrication> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaModeleFabricationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaModeleFabricationDTO entityToDTO(TaModeleFabrication entity)  {
//		TaModeleFabricationDTO dto = new TaModeleFabricationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;

		TaModeleFabricationMapper mapper = new TaModeleFabricationMapper();
		TaModeleFabricationDTO dto = new TaModeleFabricationDTO();
		dto = mapper.mapEntityToDto(entity, null);
		
		
		
		return dto;
	}

	public List<TaModeleFabricationDTO> listEntityToListDTO(List<TaModeleFabrication> entity) {
		List<TaModeleFabricationDTO> l = new ArrayList<TaModeleFabricationDTO>();

		for (TaModeleFabrication taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaModeleFabricationDTO> selectAllDTO() {
		System.out.println("List of TaModeleFabricationDTO EJB :");
		ArrayList<TaModeleFabricationDTO> liste = new ArrayList<TaModeleFabricationDTO>();

		List<TaModeleFabrication> projects = selectAll();
		for(TaModeleFabrication project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaModeleFabricationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaModeleFabricationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaModeleFabricationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaModeleFabricationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaModeleFabricationDTO dto, String validationContext) throws EJBException {
		try {
			TaModeleFabricationMapper mapper = new TaModeleFabricationMapper();
			TaModeleFabrication entity = null;
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
	
	public void persistDTO(TaModeleFabricationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaModeleFabricationDTO dto, String validationContext) throws CreateException {
		try {
			TaModeleFabricationMapper mapper = new TaModeleFabricationMapper();
			TaModeleFabrication entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaModeleFabricationDTO dto) throws RemoveException {
		try {
			TaModeleFabricationMapper mapper = new TaModeleFabricationMapper();
			TaModeleFabrication entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaModeleFabrication refresh(TaModeleFabrication persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaModeleFabrication value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaModeleFabrication value, String propertyName, String validationContext) {
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
	public void validateDTO(TaModeleFabricationDTO dto, String validationContext) {
		try {
			TaModeleFabricationMapper mapper = new TaModeleFabricationMapper();
			TaModeleFabrication entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleFabricationDTO> validator = new BeanValidator<TaModeleFabricationDTO>(TaModeleFabricationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaModeleFabricationDTO dto, String propertyName, String validationContext) {
		try {
			TaModeleFabricationMapper mapper = new TaModeleFabricationMapper();
			TaModeleFabrication entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaModeleFabricationDTO> validator = new BeanValidator<TaModeleFabricationDTO>(TaModeleFabricationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaModeleFabricationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaModeleFabricationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaModeleFabrication value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaModeleFabrication value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
