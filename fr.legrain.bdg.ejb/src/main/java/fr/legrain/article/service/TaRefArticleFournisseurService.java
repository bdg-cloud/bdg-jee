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

import fr.legrain.article.dao.IRefArticleFournisseurDAO;
import fr.legrain.article.dto.TaRefArticleFournisseurDTO;
import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.bdg.article.service.remote.ITaRefArticleFournisseurServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRefArticleFournisseurMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaRefArticleFournisseurBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRefArticleFournisseurService extends AbstractApplicationDAOServer<TaRefArticleFournisseur, TaRefArticleFournisseurDTO> implements ITaRefArticleFournisseurServiceRemote {

	static Logger logger = Logger.getLogger(TaRefArticleFournisseurService.class);

	@Inject private IRefArticleFournisseurDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRefArticleFournisseurService() {
		super(TaRefArticleFournisseur.class,TaRefArticleFournisseurDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRefArticleFournisseur a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRefArticleFournisseur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRefArticleFournisseur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRefArticleFournisseur persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdRefArticleFournisseur()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRefArticleFournisseur merge(TaRefArticleFournisseur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRefArticleFournisseur merge(TaRefArticleFournisseur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRefArticleFournisseur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRefArticleFournisseur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRefArticleFournisseur> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRefArticleFournisseurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRefArticleFournisseurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRefArticleFournisseur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRefArticleFournisseurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRefArticleFournisseurDTO entityToDTO(TaRefArticleFournisseur entity) {
//		TaRefArticleFournisseurDTO dto = new TaRefArticleFournisseurDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRefArticleFournisseurMapper mapper = new TaRefArticleFournisseurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRefArticleFournisseurDTO> listEntityToListDTO(List<TaRefArticleFournisseur> entity) {
		List<TaRefArticleFournisseurDTO> l = new ArrayList<TaRefArticleFournisseurDTO>();

		for (TaRefArticleFournisseur taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRefArticleFournisseurDTO> selectAllDTO() {
		System.out.println("List of TaRefArticleFournisseurDTO EJB :");
		ArrayList<TaRefArticleFournisseurDTO> liste = new ArrayList<TaRefArticleFournisseurDTO>();

		List<TaRefArticleFournisseur> projects = selectAll();
		for(TaRefArticleFournisseur project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRefArticleFournisseurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRefArticleFournisseurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRefArticleFournisseurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRefArticleFournisseurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRefArticleFournisseurDTO dto, String validationContext) throws EJBException {
		try {
			TaRefArticleFournisseurMapper mapper = new TaRefArticleFournisseurMapper();
			TaRefArticleFournisseur entity = null;
			if(dto.getId()!=0) {
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
	
	public void persistDTO(TaRefArticleFournisseurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRefArticleFournisseurDTO dto, String validationContext) throws CreateException {
		try {
			TaRefArticleFournisseurMapper mapper = new TaRefArticleFournisseurMapper();
			TaRefArticleFournisseur entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRefArticleFournisseurDTO dto) throws RemoveException {
		try {
			TaRefArticleFournisseurMapper mapper = new TaRefArticleFournisseurMapper();
			TaRefArticleFournisseur entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRefArticleFournisseur refresh(TaRefArticleFournisseur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRefArticleFournisseur value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRefArticleFournisseur value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRefArticleFournisseurDTO dto, String validationContext) {
		try {
			TaRefArticleFournisseurMapper mapper = new TaRefArticleFournisseurMapper();
			TaRefArticleFournisseur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRefArticleFournisseurDTO> validator = new BeanValidator<TaRefArticleFournisseurDTO>(TaRefArticleFournisseurDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRefArticleFournisseurDTO dto, String propertyName, String validationContext) {
		try {
			TaRefArticleFournisseurMapper mapper = new TaRefArticleFournisseurMapper();
			TaRefArticleFournisseur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRefArticleFournisseurDTO> validator = new BeanValidator<TaRefArticleFournisseurDTO>(TaRefArticleFournisseurDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRefArticleFournisseurDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRefArticleFournisseurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRefArticleFournisseur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRefArticleFournisseur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaRefArticleFournisseurDTO findByCodeFournisseurAndCodeBarreLight(String codeBarre, String codeFournisseur) {
		// TODO Auto-generated method stub
		return dao.findByCodeFournisseurAndCodeBarreLight(codeBarre, codeFournisseur);
	}

	@Override
	public List<TaRefArticleFournisseurDTO> findByCodeFournisseurLight(String codeFournisseur) {
		// TODO Auto-generated method stub
		return dao.findByCodeFournisseurLight(codeFournisseur);
	}

	@Override
	public TaRefArticleFournisseur findByCodeFournisseurAndCodeBarre(String codeBarre, String codeFournisseur) {
		// TODO Auto-generated method stub
		return dao.findByCodeFournisseurAndCodeBarre(codeBarre, codeFournisseur);
	}

	@Override
	public List<TaRefArticleFournisseur> findByCodeFournisseur(String codeFournisseur) {
		// TODO Auto-generated method stub
		return dao.findByCodeFournisseur(codeFournisseur);
	}

}
