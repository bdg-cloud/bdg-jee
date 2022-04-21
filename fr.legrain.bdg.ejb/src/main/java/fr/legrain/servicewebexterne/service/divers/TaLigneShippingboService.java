package fr.legrain.servicewebexterne.service.divers;

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

import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.paiement.service.remote.ITaLogPaiementInternetServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneShippingboServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;
import fr.legrain.servicewebexterne.dao.ITaLigneShippingboDAO;
import fr.legrain.servicewebexterne.dto.TaLigneShippingboDTO;
import fr.legrain.servicewebexterne.mapper.TaLigneShippingBoMapper;
import fr.legrain.servicewebexterne.model.TaLigneShippingBo;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLigneShippingboService extends AbstractApplicationDAOServer<TaLigneShippingBo, TaLigneShippingboDTO> implements ITaLigneShippingboServiceRemote {

	static Logger logger = Logger.getLogger(TaLigneShippingboService.class);

	@Inject private ITaLigneShippingboDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLigneShippingboService() {
		super(TaLigneShippingBo.class,TaLigneShippingboDTO.class);
	}
	
	public void persist(TaLigneShippingBo transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLigneShippingBo transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLigneShippingBo persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLigneShippingbo()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLigneShippingBo merge(TaLigneShippingBo detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLigneShippingBo merge(TaLigneShippingBo detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLigneShippingBo findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLigneShippingBo findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLigneShippingBo> selectAll() {
		return dao.selectAll();
	}
	
	
	////////////////////////////DTO/////////////////////////////////////////////////////////////
	
	public List<TaLigneShippingboDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLigneShippingboDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLigneShippingBo> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLigneShippingboDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLigneShippingboDTO entityToDTO(TaLigneShippingBo entity) {
//		TaLigneShippingboDTO dto = new TaLigneShippingboDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLigneShippingBoMapper mapper = new TaLigneShippingBoMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLigneShippingboDTO> listEntityToListDTO(List<TaLigneShippingBo> entity) {
		List<TaLigneShippingboDTO> l = new ArrayList<TaLigneShippingboDTO>();

		for (TaLigneShippingBo taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLigneShippingboDTO> selectAllDTO() {
		System.out.println("List of TaLigneShippingboDTO EJB :");
		ArrayList<TaLigneShippingboDTO> liste = new ArrayList<TaLigneShippingboDTO>();

		List<TaLigneShippingBo> projects = selectAll();
		for(TaLigneShippingBo project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLigneShippingboDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLigneShippingboDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLigneShippingboDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLigneShippingboDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLigneShippingboDTO dto, String validationContext) throws EJBException {
		try {
			TaLigneShippingBoMapper mapper = new TaLigneShippingBoMapper();
			TaLigneShippingBo entity = null;
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
	
	public void persistDTO(TaLigneShippingboDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLigneShippingboDTO dto, String validationContext) throws CreateException {
		try {
			TaLigneShippingBoMapper mapper = new TaLigneShippingBoMapper();
			TaLigneShippingBo entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLigneShippingboDTO dto) throws RemoveException {
		try {
			TaLigneShippingBoMapper mapper = new TaLigneShippingBoMapper();
			TaLigneShippingBo entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLigneShippingBo refresh(TaLigneShippingBo persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLigneShippingBo value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLigneShippingBo value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLigneShippingboDTO dto, String validationContext) {
		try {
			TaLigneShippingBoMapper mapper = new TaLigneShippingBoMapper();
			TaLigneShippingBo entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLigneShippingboDTO> validator = new BeanValidator<TaLigneShippingboDTO>(TaLigneShippingboDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLigneShippingboDTO dto, String propertyName, String validationContext) {
		try {
			TaLigneShippingBoMapper mapper = new TaLigneShippingBoMapper();
			TaLigneShippingBo entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLigneShippingboDTO> validator = new BeanValidator<TaLigneShippingboDTO>(TaLigneShippingboDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLigneShippingboDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLigneShippingboDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLigneShippingBo value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLigneShippingBo value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	
	
	
	
}
