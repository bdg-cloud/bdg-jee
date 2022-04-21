package fr.legrain.documents.service;

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

import fr.legrain.bdg.documents.service.remote.ITaLFlashServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLFlashMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLFlashDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.documents.dao.ILFlashDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLFlashBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLFlashService extends AbstractApplicationDAOServer<TaLFlash, TaLFlashDTO> implements ITaLFlashServiceRemote {

	static Logger logger = Logger.getLogger(TaLFlashService.class);

	@Inject private ILFlashDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLFlashService() {
		super(TaLFlash.class,TaLFlashDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLFlash a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLFlash transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLFlash transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLFlash persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLFlash merge(TaLFlash detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLFlash merge(TaLFlash detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLFlash findById(int id) throws FinderException {
		TaLFlash o= dao.findById(id);
		recupSetLigneALigne(o.getTaFlash());
		recupSetLigneALigne(o.getTaFlash());
//		recupSetRDocument(o.getTaFlash());
		recupSetREtatLigneDocuments(o.getTaFlash());
		recupSetHistREtatLigneDocuments(o.getTaFlash());
		return o;
	}

	public TaLFlash findByCode(String code) throws FinderException {
		TaLFlash o=  dao.findByCode(code);
		recupSetLigneALigne(o.getTaFlash());
		recupSetLigneALigne(o.getTaFlash());
//		recupSetRDocument(o.getTaFlash());
		recupSetREtatLigneDocuments(o.getTaFlash());
		recupSetHistREtatLigneDocuments(o.getTaFlash());
		return o;
	}

//	@RolesAllowed("admin")
	public List<TaLFlash> selectAll() {
		List<TaLFlash> l= dao.selectAll();
		for (TaLFlash o : l) {
			recupSetLigneALigne(o.getTaFlash());
			recupSetLigneALigne(o.getTaFlash());
//			recupSetRDocument(o.getTaFlash());
			recupSetREtatLigneDocuments(o.getTaFlash());
			recupSetHistREtatLigneDocuments(o.getTaFlash());		
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLFlashDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLFlashDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLFlash> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLFlashDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLFlashDTO entityToDTO(TaLFlash entity) {
//		LFactureDTO dto = new LFactureDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLFlashMapper mapper = new TaLFlashMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLFlashDTO> listEntityToListDTO(List<TaLFlash> entity) {
		List<TaLFlashDTO> l = new ArrayList<TaLFlashDTO>();

		for (TaLFlash taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLFlashDTO> selectAllDTO() {
		System.out.println("List of LFactureDTO EJB :");
		ArrayList<TaLFlashDTO> liste = new ArrayList<TaLFlashDTO>();

		List<TaLFlash> projects = selectAll();
		for(TaLFlash project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLFlashDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLFlashDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLFlashDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLFlashDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLFlashDTO dto, String validationContext) throws EJBException {
		try {
			TaLFlashMapper mapper = new TaLFlashMapper();
			TaLFlash entity = null;
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
	
	public void persistDTO(TaLFlashDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLFlashDTO dto, String validationContext) throws CreateException {
		try {
			TaLFlashMapper mapper = new TaLFlashMapper();
			TaLFlash entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLFlashDTO dto) throws RemoveException {
		try {
			TaLFlashMapper mapper = new TaLFlashMapper();
			TaLFlash entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLFlash refresh(TaLFlash persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLFlash value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLFlash value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLFlashDTO dto, String validationContext) {
		try {
			TaLFlashMapper mapper = new TaLFlashMapper();
			TaLFlash entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<LFactureDTO> validator = new BeanValidator<LFactureDTO>(LFactureDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLFlashDTO dto, String propertyName, String validationContext) {
		try {
			TaLFlashMapper mapper = new TaLFlashMapper();
			TaLFlash entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<LFactureDTO> validator = new BeanValidator<LFactureDTO>(LFactureDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLFlashDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLFlashDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLFlash value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLFlash value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	
	
	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(TaFlash doc, String codeTiers, String typeOrigine,
			String typeDest, Date debut, Date fin,TaEtat etat,String tEtat) {
		// TODO Auto-generated method stub
		return dao.selectLigneDocNonAffecte(doc, codeTiers, typeOrigine, typeDest, debut, fin, etat,tEtat);
	}
}
