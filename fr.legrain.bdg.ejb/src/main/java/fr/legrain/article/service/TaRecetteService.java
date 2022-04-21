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

import fr.legrain.article.dao.IRecetteDAO;
import fr.legrain.article.model.TaRecette;
import fr.legrain.bdg.article.service.remote.ITaRecetteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRecetteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaRecetteDTO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaRecetteBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRecetteService extends AbstractApplicationDAOServer<TaRecette, TaRecetteDTO> implements ITaRecetteServiceRemote {

	static Logger logger = Logger.getLogger(TaRecetteService.class);

	@Inject private IRecetteDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	/**
	 * Default constructor. 
	 */
	public TaRecetteService() {
		super(TaRecette.class,TaRecetteDTO.class);
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaRecette.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	//	private String defaultJPQLQuery = "select a from TaRecette a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRecette transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRecette transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRecette persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaRecette merge(TaRecette detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRecette merge(TaRecette detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRecette findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRecette findByIdWithList(int id) throws FinderException {
		TaRecette fab =  dao.findById(id);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}

	public TaRecette findByCodeWithList(String code)   {
		TaRecette fab =  dao.findByCode(code);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}
	public TaRecette findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRecette> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRecetteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRecetteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRecette> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRecetteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRecetteDTO entityToDTO(TaRecette entity)  {
//		TaRecetteDTO dto = new TaRecetteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;

		TaRecetteMapper mapper = new TaRecetteMapper();
		TaRecetteDTO dto = new TaRecetteDTO();
		dto = mapper.mapEntityToDto(entity, null);
		
		
		
		return dto;
	}

	public List<TaRecetteDTO> listEntityToListDTO(List<TaRecette> entity) {
		List<TaRecetteDTO> l = new ArrayList<TaRecetteDTO>();

		for (TaRecette taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRecetteDTO> selectAllDTO() {
		System.out.println("List of TaRecetteDTO EJB :");
		ArrayList<TaRecetteDTO> liste = new ArrayList<TaRecetteDTO>();

		List<TaRecette> projects = selectAll();
		for(TaRecette project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRecetteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRecetteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRecetteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRecetteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRecetteDTO dto, String validationContext) throws EJBException {
		try {
			TaRecetteMapper mapper = new TaRecetteMapper();
			TaRecette entity = null;
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
	
	public void persistDTO(TaRecetteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRecetteDTO dto, String validationContext) throws CreateException {
		try {
			TaRecetteMapper mapper = new TaRecetteMapper();
			TaRecette entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRecetteDTO dto) throws RemoveException {
		try {
			TaRecetteMapper mapper = new TaRecetteMapper();
			TaRecette entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRecette refresh(TaRecette persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRecette value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRecette value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRecetteDTO dto, String validationContext) {
		try {
			TaRecetteMapper mapper = new TaRecetteMapper();
			TaRecette entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRecetteDTO> validator = new BeanValidator<TaRecetteDTO>(TaRecetteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRecetteDTO dto, String propertyName, String validationContext) {
		try {
			TaRecetteMapper mapper = new TaRecetteMapper();
			TaRecette entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRecetteDTO> validator = new BeanValidator<TaRecetteDTO>(TaRecetteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRecetteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRecetteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRecette value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRecette value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
