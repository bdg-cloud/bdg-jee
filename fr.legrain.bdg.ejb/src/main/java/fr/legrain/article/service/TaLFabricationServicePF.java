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

import fr.legrain.article.dao.ILFabricationPFDAO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.bdg.article.service.remote.ITaLFabricationPFServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLFabricationPFMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLFabricationDTO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLFabricationBean
 */
@Stateless
//@Stateful
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLFabricationServicePF extends AbstractApplicationDAOServer<TaLFabricationPF, TaLFabricationDTO> implements ITaLFabricationPFServiceRemote {

	static Logger logger = Logger.getLogger(TaLFabricationServicePF.class);

	@Inject private ILFabricationPFDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private	SessionInfo sessionInfo;
	
	
	
	/**
	 * Default constructor. 
	 */
	public TaLFabricationServicePF() {
		super(TaLFabricationPF.class,TaLFabricationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLFabricationPF a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLFabricationPF transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLFabricationPF transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLFabricationPF persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLDocument()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLFabricationPF merge(TaLFabricationPF detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLFabricationPF merge(TaLFabricationPF detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLFabricationPF findById(int id) throws FinderException {
		return dao.findById(id);
	}
	
	public TaLFabricationPF findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLFabricationPF> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	@Override
	public List<TaLFabricationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLFabricationPF> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLFabricationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public List<TaLFabricationDTO> listEntityToListDTO(List<TaLFabricationPF> entity) {
		List<TaLFabricationDTO> l = new ArrayList<TaLFabricationDTO>();

		for (TaLFabricationPF taLotArticle : entity) {
			l.add(entityToDTO(taLotArticle));
		}

		return l;
	}
	
	public TaLFabricationDTO entityToDTO(TaLFabricationPF entity)  {
//		TaFabricationDTO dto = new TaFabricationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;

		TaLFabricationPFMapper mapper = new TaLFabricationPFMapper();
		TaLFabricationDTO dto = new TaLFabricationDTO();
		dto = mapper.mapEntityToDto(entity, null);
		
		
		
		return dto;
	}

//	@RolesAllowed("admin")
	public List<TaLFabricationDTO> selectAllDTO() {
		System.out.println("List of TaLFabricationDTO EJB :");
		ArrayList<TaLFabricationDTO> liste = new ArrayList<TaLFabricationDTO>();

		List<TaLFabricationPF> projects = selectAll();
		for(TaLFabricationPF project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}
	
	public TaLFabricationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLFabricationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLFabricationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLFabricationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLFabricationDTO dto, String validationContext) throws EJBException {
		try {
			TaLFabricationPFMapper mapper = new TaLFabricationPFMapper();
			TaLFabricationPF entity = null;
			if(dto.getIdLDocument()!=null) {
				entity = dao.findById(dto.getIdLDocument());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdLDocument()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaLFabricationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLFabricationDTO dto, String validationContext) throws CreateException {
		try {
			TaLFabricationPF entity =  dao.findById(dto.getIdLDocument());
			dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}
	

	@Override
	public void removeDTO(TaLFabricationDTO dto) throws RemoveException {
		try {
			TaLFabricationPFMapper mapper = new TaLFabricationPFMapper();
			TaLFabricationPF entity = mapper.mapDtoToEntity(dto,null);
			dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLFabricationPF refresh(TaLFabricationPF persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLFabricationPF value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLFabricationPF value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLFabricationDTO dto, String validationContext) {
		try {
			TaLFabricationPFMapper mapper = new TaLFabricationPFMapper();
			TaLFabricationPF entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLFabricationDTO> validator = new BeanValidator<TaLFabricationDTO>(TaLFabricationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLFabricationDTO dto, String propertyName, String validationContext) {
		try {
			TaLFabricationPFMapper mapper = new TaLFabricationPFMapper();
			TaLFabricationPF entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLFabricationDTO> validator = new BeanValidator<TaLFabricationDTO>(TaLFabricationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLFabricationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLFabricationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLFabricationPF value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLFabricationPF value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	
	public String genereCode() {
		return null;
	}

	@Override
	public List<TaLFabricationDTO> findWithNamedQueryDTO(String arg0)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}


	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaLFabricationPF.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaLFabricationPF.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaLFabricationPF.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
