package fr.legrain.servicewebexterne.service.divers;

import java.util.ArrayList;
import java.util.Date;
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

import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.paiement.service.remote.ITaLogPaiementInternetServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaSynchroServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneShippingboServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;
import fr.legrain.servicewebexterne.dao.ITaSynchroServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dao.ITaLigneShippingboDAO;
import fr.legrain.servicewebexterne.dto.TaSynchroServiceExterneDTO;
import fr.legrain.servicewebexterne.mapper.TaSynchroServiceExterneMapper;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaSynchroServiceExterneService extends AbstractApplicationDAOServer<TaSynchroServiceExterne, TaSynchroServiceExterneDTO> implements ITaSynchroServiceExterneServiceRemote {

	static Logger logger = Logger.getLogger(TaSynchroServiceExterneService.class);

	@Inject private ITaSynchroServiceWebExterneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaSynchroServiceExterneService() {
		super(TaSynchroServiceExterne.class,TaSynchroServiceExterneDTO.class);
	}
	
	public void persist(TaSynchroServiceExterne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaSynchroServiceExterne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaSynchroServiceExterne persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdSynchroServiceExterne()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaSynchroServiceExterne merge(TaSynchroServiceExterne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaSynchroServiceExterne merge(TaSynchroServiceExterne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaSynchroServiceExterne findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaSynchroServiceExterne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaSynchroServiceExterne> selectAll() {
		return dao.selectAll();
	}
	 
	////////////////////////////DTO/////////////////////////////////////////////////////////////
	public List<TaSynchroServiceExterneDTO> findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne){
		return dao.findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO(typeEntite, idServiceWebExterne);
	}
	public Date findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne) {
		return dao.findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO(typeEntite,idServiceWebExterne);
	}
	public List<TaSynchroServiceExterneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaSynchroServiceExterneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaSynchroServiceExterne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaSynchroServiceExterneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaSynchroServiceExterneDTO entityToDTO(TaSynchroServiceExterne entity) {
//		TaSynchroServiceExterneDTO dto = new TaSynchroServiceExterneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaSynchroServiceExterneMapper mapper = new TaSynchroServiceExterneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaSynchroServiceExterneDTO> listEntityToListDTO(List<TaSynchroServiceExterne> entity) {
		List<TaSynchroServiceExterneDTO> l = new ArrayList<TaSynchroServiceExterneDTO>();

		for (TaSynchroServiceExterne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaSynchroServiceExterneDTO> selectAllDTO() {
		System.out.println("List of TaSynchroServiceExterneDTO EJB :");
		ArrayList<TaSynchroServiceExterneDTO> liste = new ArrayList<TaSynchroServiceExterneDTO>();

		List<TaSynchroServiceExterne> projects = selectAll();
		for(TaSynchroServiceExterne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaSynchroServiceExterneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaSynchroServiceExterneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaSynchroServiceExterneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaSynchroServiceExterneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaSynchroServiceExterneDTO dto, String validationContext) throws EJBException {
		try {
			TaSynchroServiceExterneMapper mapper = new TaSynchroServiceExterneMapper();
			TaSynchroServiceExterne entity = null;
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
	
	public void persistDTO(TaSynchroServiceExterneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaSynchroServiceExterneDTO dto, String validationContext) throws CreateException {
		try {
			TaSynchroServiceExterneMapper mapper = new TaSynchroServiceExterneMapper();
			TaSynchroServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaSynchroServiceExterneDTO dto) throws RemoveException {
		try {
			TaSynchroServiceExterneMapper mapper = new TaSynchroServiceExterneMapper();
			TaSynchroServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaSynchroServiceExterne refresh(TaSynchroServiceExterne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaSynchroServiceExterne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaSynchroServiceExterne value, String propertyName, String validationContext) {
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
	public void validateDTO(TaSynchroServiceExterneDTO dto, String validationContext) {
		try {
			TaSynchroServiceExterneMapper mapper = new TaSynchroServiceExterneMapper();
			TaSynchroServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaSynchroServiceExterneDTO> validator = new BeanValidator<TaSynchroServiceExterneDTO>(TaSynchroServiceExterneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaSynchroServiceExterneDTO dto, String propertyName, String validationContext) {
		try {
			TaSynchroServiceExterneMapper mapper = new TaSynchroServiceExterneMapper();
			TaSynchroServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaSynchroServiceExterneDTO> validator = new BeanValidator<TaSynchroServiceExterneDTO>(TaSynchroServiceExterneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaSynchroServiceExterneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaSynchroServiceExterneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaSynchroServiceExterne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaSynchroServiceExterne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	
	
	
	
}
