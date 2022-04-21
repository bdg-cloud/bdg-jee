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

import fr.legrain.article.dao.IFamilleDAO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.model.TaFamille;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaFamilleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaFamilleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaFamilleService extends AbstractApplicationDAOServer<TaFamille, TaFamilleDTO> implements ITaFamilleServiceRemote {

	static Logger logger = Logger.getLogger(TaFamilleService.class);

	@Inject private IFamilleDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaFamilleService() {
		super(TaFamille.class,TaFamilleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaFamille a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFamille transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFamille transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFamille persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdFamille()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaFamille merge(TaFamille detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFamille merge(TaFamille detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaFamille findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaFamille findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaFamille> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFamilleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFamilleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFamille> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFamilleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFamilleDTO entityToDTO(TaFamille entity) {
//		TaFamilleDTO dto = new TaFamilleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaFamilleMapper mapper = new TaFamilleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFamilleDTO> listEntityToListDTO(List<TaFamille> entity) {
		List<TaFamilleDTO> l = new ArrayList<TaFamilleDTO>();

		for (TaFamille taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFamilleDTO> selectAllDTO() {
		System.out.println("List of TaFamilleDTO EJB :");
		ArrayList<TaFamilleDTO> liste = new ArrayList<TaFamilleDTO>();

		List<TaFamille> projects = selectAll();
		for(TaFamille project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFamilleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFamilleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFamilleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFamilleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFamilleDTO dto, String validationContext) throws EJBException {
		try {
			TaFamilleMapper mapper = new TaFamilleMapper();
			TaFamille entity = null;
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
	
	public void persistDTO(TaFamilleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFamilleDTO dto, String validationContext) throws CreateException {
		try {
			TaFamilleMapper mapper = new TaFamilleMapper();
			TaFamille entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFamilleDTO dto) throws RemoveException {
		try {
			TaFamilleMapper mapper = new TaFamilleMapper();
			TaFamille entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFamille refresh(TaFamille persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFamille value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaFamille value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFamilleDTO dto, String validationContext) {
		try {
			TaFamilleMapper mapper = new TaFamilleMapper();
			TaFamille entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFamilleDTO> validator = new BeanValidator<TaFamilleDTO>(TaFamilleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFamilleDTO dto, String propertyName, String validationContext) {
		try {
			TaFamilleMapper mapper = new TaFamilleMapper();
			TaFamille entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFamilleDTO> validator = new BeanValidator<TaFamilleDTO>(TaFamilleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFamilleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFamilleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFamille value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFamille value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	// Dima - Début
	public List<TaFamilleDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

	@Override
	public List<TaFamilleDTO> findAllLight() {
		// TODO Auto-generated method stub
		return dao.findAllLight();
	}

	@Override
	public List<TaFamilleDTO> findLightSurCodeTTarif(String codeTTarif) {
		// TODO Auto-generated method stub
		return dao.findLightSurCodeTTarif(codeTTarif);
	}

	@Override
	public List<TaFamilleDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findLightSurCodeTTarifEtCodeTiers(codeTTarif, codeTiers);
	}

	@Override
	public TaFamille findByLibelle(String libelle) {
		// TODO Auto-generated method stub
		return dao.findByLibelle(libelle);
	}
	
}
