package fr.legrain.documents.service;

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

import fr.legrain.bdg.documents.service.remote.ITaLPreparationServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLPreparationMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLPreparationDTO;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.documents.dao.ILPreparationDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLPreparationBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLPreparationService extends AbstractApplicationDAOServer<TaLPreparation, TaLPreparationDTO> implements ITaLPreparationServiceRemote {

	static Logger logger = Logger.getLogger(TaLPreparationService.class);

	@Inject private ILPreparationDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLPreparationService() {
		super(TaLPreparation.class,TaLPreparationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLPreparation a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLPreparation transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLPreparation transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLPreparation persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLPreparation merge(TaLPreparation detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLPreparation merge(TaLPreparation detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLPreparation findById(int id) throws FinderException {
		TaLPreparation o = dao.findById(id);
		recupSetREtat(o.getTaDocument());
		recupSetHistREtat(o.getTaDocument());		
		recupSetLigneALigne(o.getTaDocument());
		recupSetRDocument(o.getTaDocument());
		recupSetREtatLigneDocuments(o.getTaDocument());
		recupSetHistREtatLigneDocuments(o.getTaDocument());
		return o;
	}

	public TaLPreparation findByCode(String code) throws FinderException {
		TaLPreparation o = dao.findByCode(code);
		recupSetREtat(o.getTaDocument());
		recupSetHistREtat(o.getTaDocument());		
		recupSetLigneALigne(o.getTaDocument());
		recupSetRDocument(o.getTaDocument());
		recupSetREtatLigneDocuments(o.getTaDocument());
		recupSetHistREtatLigneDocuments(o.getTaDocument());
		return o;
	}

//	@RolesAllowed("admin")
	public List<TaLPreparation> selectAll() {
		List<TaLPreparation> l= dao.selectAll();
		for (TaLPreparation o : l) {
			recupSetREtat(o.getTaDocument());
			recupSetHistREtat(o.getTaDocument());		
			recupSetLigneALigne(o.getTaDocument());
			recupSetRDocument(o.getTaDocument());
			recupSetREtatLigneDocuments(o.getTaDocument());
			recupSetHistREtatLigneDocuments(o.getTaDocument());		
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLPreparationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLPreparationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLPreparation> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLPreparationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLPreparationDTO entityToDTO(TaLPreparation entity) {
//		LFactureDTO dto = new LFactureDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLPreparationMapper mapper = new TaLPreparationMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLPreparationDTO> listEntityToListDTO(List<TaLPreparation> entity) {
		List<TaLPreparationDTO> l = new ArrayList<TaLPreparationDTO>();

		for (TaLPreparation taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLPreparationDTO> selectAllDTO() {
		System.out.println("List of LFactureDTO EJB :");
		ArrayList<TaLPreparationDTO> liste = new ArrayList<TaLPreparationDTO>();

		List<TaLPreparation> projects = selectAll();
		for(TaLPreparation project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLPreparationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLPreparationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLPreparationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLPreparationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLPreparationDTO dto, String validationContext) throws EJBException {
		try {
			TaLPreparationMapper mapper = new TaLPreparationMapper();
			TaLPreparation entity = null;
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
	
	public void persistDTO(TaLPreparationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLPreparationDTO dto, String validationContext) throws CreateException {
		try {
			TaLPreparationMapper mapper = new TaLPreparationMapper();
			TaLPreparation entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLPreparationDTO dto) throws RemoveException {
		try {
			TaLPreparationMapper mapper = new TaLPreparationMapper();
			TaLPreparation entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLPreparation refresh(TaLPreparation persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLPreparation value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLPreparation value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLPreparationDTO dto, String validationContext) {
		try {
			TaLPreparationMapper mapper = new TaLPreparationMapper();
			TaLPreparation entity = mapper.mapDtoToEntity(dto,null);
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
	public void validateDTOProperty(TaLPreparationDTO dto, String propertyName, String validationContext) {
		try {
			TaLPreparationMapper mapper = new TaLPreparationMapper();
			TaLPreparation entity = mapper.mapDtoToEntity(dto,null);
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
	public void validateDTO(TaLPreparationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLPreparationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLPreparation value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLPreparation value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
