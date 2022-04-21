package fr.legrain.autorisations.autorisations.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.autorisation.xml.ListeModules;
import fr.legrain.autorisations.autorisation.dto.TaAutorisationsDTO;
import fr.legrain.autorisations.autorisation.model.TaAutorisations;
import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.autorisations.autorisations.dao.IAutorisationsDAO;
import fr.legrain.autorisations.data.AbstractApplicationDAOServer;
import fr.legrain.bdg.autorisations.service.remote.ITaAutorisationsServiceRemote;
import fr.legrain.bdg.autorisations.service.remote.ITaTypeProduitServiceRemote;
import fr.legrain.autorisations.model.mapping.mapper.TaAutorisationsMapper;

/**
 * Session Bean implementation class TaAutorisationsBean
 */
@Stateless
//@DeclareRoles("admin")
@WebService
//@SOAPBinding(style=Style.DOCUMENT,parameterStyle=ParameterStyle.WRAPPED)
@HandlerChain(file="/fr/legrain/autorisations/autorisations/service/handler-chain.xml") 
public class TaAutorisationsService extends AbstractApplicationDAOServer<TaAutorisations, TaAutorisationsDTO> implements ITaAutorisationsServiceRemote {

	static Logger logger = Logger.getLogger(TaAutorisationsService.class);

	@Inject private IAutorisationsDAO dao;
	@EJB private ITaTypeProduitServiceRemote taTypeProduitService;
	private String codeTypeProduitDefaut = "Bdg"; 
	//TODO type produit par défaut, il faudra faire un WebService pour accéder aux type de produit
	//Comme ça on pourra créer une nouvelle autorisation pour un type produit particulier

	/**
	 * Default constructor. 
	 */
	public TaAutorisationsService() {
		super(TaAutorisations.class,TaAutorisationsDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaAutorisations a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAutorisations transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAutorisations transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaAutorisations persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdAutorisation()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaAutorisations merge(TaAutorisations detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAutorisations merge(TaAutorisations detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		
		
		if(detachedInstance.getTaTypeProduit()==null) {
			try {
				System.out.println("Pas de type produit - Affection du type produit par defaut : "+codeTypeProduitDefaut);
				TaTypeProduit tp = taTypeProduitService.findByCode(codeTypeProduitDefaut);
				detachedInstance.setTaTypeProduit(tp);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return dao.merge(detachedInstance);
	}

	public TaAutorisations findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAutorisations findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

	public ListeModules findByDossierTypeProduit(String codeDossier, Integer idTypeProduit){
		TaAutorisations autorisation=dao.findByDossierTypeProduit(codeDossier,idTypeProduit);
		ListeModules liste=null;
		if(autorisation!=null){
			liste=new ListeModules();
			liste = liste.recupModulesXml(autorisation.getModules());
		}
		return liste;
	}
	
//	@RolesAllowed("admin")
	public List<TaAutorisations> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAutorisationsDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAutorisationsDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAutorisations> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAutorisationsDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAutorisationsDTO entityToDTO(TaAutorisations entity) {
//		TaAutorisationsDTO dto = new TaAutorisationsDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaAutorisationsMapper mapper = new TaAutorisationsMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAutorisationsDTO> listEntityToListDTO(List<TaAutorisations> entity) {
		List<TaAutorisationsDTO> l = new ArrayList<TaAutorisationsDTO>();

		for (TaAutorisations taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAutorisationsDTO> selectAllDTO() {
		System.out.println("List of TaAutorisationsDTO EJB :");
		ArrayList<TaAutorisationsDTO> liste = new ArrayList<TaAutorisationsDTO>();

		List<TaAutorisations> projects = selectAll();
		for(TaAutorisations project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAutorisationsDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAutorisationsDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAutorisationsDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAutorisationsDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAutorisationsDTO dto, String validationContext) throws EJBException {
		try {
			TaAutorisationsMapper mapper = new TaAutorisationsMapper();
			TaAutorisations entity = null;
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
	
	public void persistDTO(TaAutorisationsDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAutorisationsDTO dto, String validationContext) throws CreateException {
		try {
			TaAutorisationsMapper mapper = new TaAutorisationsMapper();
			TaAutorisations entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAutorisationsDTO dto) throws RemoveException {
		try {
			TaAutorisationsMapper mapper = new TaAutorisationsMapper();
			TaAutorisations entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAutorisations refresh(TaAutorisations persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAutorisations value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAutorisations value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAutorisationsDTO dto, String validationContext) {
		try {
			TaAutorisationsMapper mapper = new TaAutorisationsMapper();
			TaAutorisations entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationsDTO> validator = new BeanValidator<TaAutorisationsDTO>(TaAutorisationsDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAutorisationsDTO dto, String propertyName, String validationContext) {
		try {
			TaAutorisationsMapper mapper = new TaAutorisationsMapper();
			TaAutorisations entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationsDTO> validator = new BeanValidator<TaAutorisationsDTO>(TaAutorisationsDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAutorisationsDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAutorisationsDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAutorisations value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAutorisations value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
