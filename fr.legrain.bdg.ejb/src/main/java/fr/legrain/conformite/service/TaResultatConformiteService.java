package fr.legrain.conformite.service;

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

import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaResultatConformiteMapper;
import fr.legrain.conformite.dao.IResultatConformiteDAO;
import fr.legrain.conformite.dto.TaResultatConformiteDTO;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaResultatConformiteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaResultatConformiteService extends AbstractApplicationDAOServer<TaResultatConformite, TaResultatConformiteDTO> implements ITaResultatConformiteServiceRemote {

	static Logger logger = Logger.getLogger(TaResultatConformiteService.class);

	@Inject private IResultatConformiteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaResultatConformiteService() {
		super(TaResultatConformite.class,TaResultatConformiteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaResultatConformite a";
	
	public TaResultatConformite findByLotAndControleConformite(int idLot, int idControleConformite) {
		return dao.findByLotAndControleConformite(idLot, idControleConformite);
	}
	
	public List<TaResultatConformite> findByLotAndControleConformiteHistorique(int idLot, int idControleConformite) {
		return dao.findByLotAndControleConformiteHistorique(idLot, idControleConformite);
	}
	
	public TaStatusConformite etatLot(int idLot) {
		return dao.etatLot(idLot);
	}
	
	public TaStatusConformite etatLot(int idLot, boolean priseEnCompteDesControleFactultatif) {
		return dao.etatLot(idLot,priseEnCompteDesControleFactultatif);
	}
	
	public TaStatusConformite etatLotPourLesControlesObligatoires(int idLot) {
		return dao.etatLotPourLesControlesObligatoires(idLot);
	}
	
	public TaStatusConformite etatLotPourLesControlesFacultatif(int idLot) {
		return dao.etatLotPourLesControlesFacultatif(idLot);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaResultatConformite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaResultatConformite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaResultatConformite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdResultatConformite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaResultatConformite merge(TaResultatConformite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaResultatConformite merge(TaResultatConformite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaResultatConformite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaResultatConformite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaResultatConformite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaResultatConformiteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaResultatConformiteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaResultatConformite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaResultatConformiteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaResultatConformiteDTO entityToDTO(TaResultatConformite entity) {
//		TaResultatConformiteDTO dto = new TaResultatConformiteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaResultatConformiteMapper mapper = new TaResultatConformiteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaResultatConformiteDTO> listEntityToListDTO(List<TaResultatConformite> entity) {
		List<TaResultatConformiteDTO> l = new ArrayList<TaResultatConformiteDTO>();

		for (TaResultatConformite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaResultatConformiteDTO> selectAllDTO() {
		System.out.println("List of TaResultatConformiteDTO EJB :");
		ArrayList<TaResultatConformiteDTO> liste = new ArrayList<TaResultatConformiteDTO>();

		List<TaResultatConformite> projects = selectAll();
		for(TaResultatConformite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaResultatConformiteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaResultatConformiteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaResultatConformiteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaResultatConformiteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaResultatConformiteDTO dto, String validationContext) throws EJBException {
		try {
			TaResultatConformiteMapper mapper = new TaResultatConformiteMapper();
			TaResultatConformite entity = null;
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
	
	public void persistDTO(TaResultatConformiteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaResultatConformiteDTO dto, String validationContext) throws CreateException {
		try {
			TaResultatConformiteMapper mapper = new TaResultatConformiteMapper();
			TaResultatConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaResultatConformiteDTO dto) throws RemoveException {
		try {
			TaResultatConformiteMapper mapper = new TaResultatConformiteMapper();
			TaResultatConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaResultatConformite refresh(TaResultatConformite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaResultatConformite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaResultatConformite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaResultatConformiteDTO dto, String validationContext) {
		try {
			TaResultatConformiteMapper mapper = new TaResultatConformiteMapper();
			TaResultatConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaResultatConformiteDTO> validator = new BeanValidator<TaResultatConformiteDTO>(TaResultatConformiteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaResultatConformiteDTO dto, String propertyName, String validationContext) {
		try {
			TaResultatConformiteMapper mapper = new TaResultatConformiteMapper();
			TaResultatConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaResultatConformiteDTO> validator = new BeanValidator<TaResultatConformiteDTO>(TaResultatConformiteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaResultatConformiteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaResultatConformiteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaResultatConformite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaResultatConformite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
