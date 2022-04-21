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

import fr.legrain.bdg.documents.service.remote.ITaLAvoirServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLAvoirMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLAvoirDTO;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.documents.dao.ILAvoirDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLAvoirBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLAvoirService extends AbstractApplicationDAOServer<TaLAvoir, TaLAvoirDTO> implements ITaLAvoirServiceRemote {

	static Logger logger = Logger.getLogger(TaLAvoirService.class);

	@Inject private ILAvoirDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLAvoirService() {
		super(TaLAvoir.class,TaLAvoirDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLAvoir a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLAvoir transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLAvoir transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLAvoir persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLAvoir merge(TaLAvoir detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLAvoir merge(TaLAvoir detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLAvoir findById(int id) throws FinderException {
		TaLAvoir o= dao.findById(id);
		o=(TaLAvoir) recupSetREtat(o.getTaDocument());
		o=(TaLAvoir) recupSetHistREtat(o.getTaDocument());		
		o=(TaLAvoir) recupSetLigneALigne(o.getTaDocument());
		o=(TaLAvoir) recupSetRDocument(o.getTaDocument());
		o=(TaLAvoir) recupSetREtatLigneDocuments(o.getTaDocument());
		o=(TaLAvoir) recupSetHistREtatLigneDocuments(o.getTaDocument());
		return o;
	}

	public TaLAvoir findByCode(String code) throws FinderException {
		TaLAvoir o= dao.findByCode(code);
		o=(TaLAvoir) recupSetREtat(o.getTaDocument());
		o=(TaLAvoir) recupSetHistREtat(o.getTaDocument());		
		o=(TaLAvoir) recupSetLigneALigne(o.getTaDocument());
		o=(TaLAvoir) recupSetRDocument(o.getTaDocument());
		o=(TaLAvoir) recupSetREtatLigneDocuments(o.getTaDocument());
		o=(TaLAvoir) recupSetHistREtatLigneDocuments(o.getTaDocument());
		return o;
	}

//	@RolesAllowed("admin")
	public List<TaLAvoir> selectAll() {
		List<TaLAvoir> l= dao.selectAll();
		for (TaLAvoir o : l) {
			o=(TaLAvoir) recupSetREtat(o.getTaDocument());
			o=(TaLAvoir) recupSetHistREtat(o.getTaDocument());		
			o=(TaLAvoir) recupSetLigneALigne(o.getTaDocument());
			o=(TaLAvoir) recupSetRDocument(o.getTaDocument());
			o=(TaLAvoir) recupSetREtatLigneDocuments(o.getTaDocument());
			o=(TaLAvoir) recupSetHistREtatLigneDocuments(o.getTaDocument());		
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLAvoirDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLAvoirDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLAvoir> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLAvoirDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLAvoirDTO entityToDTO(TaLAvoir entity) {
//		LAvoirDTO dto = new LAvoirDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLAvoirMapper mapper = new TaLAvoirMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLAvoirDTO> listEntityToListDTO(List<TaLAvoir> entity) {
		List<TaLAvoirDTO> l = new ArrayList<TaLAvoirDTO>();

		for (TaLAvoir taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLAvoirDTO> selectAllDTO() {
		System.out.println("List of LAvoirDTO EJB :");
		ArrayList<TaLAvoirDTO> liste = new ArrayList<TaLAvoirDTO>();

		List<TaLAvoir> projects = selectAll();
		for(TaLAvoir project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLAvoirDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLAvoirDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLAvoirDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLAvoirDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLAvoirDTO dto, String validationContext) throws EJBException {
		try {
			TaLAvoirMapper mapper = new TaLAvoirMapper();
			TaLAvoir entity = null;
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
	
	public void persistDTO(TaLAvoirDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLAvoirDTO dto, String validationContext) throws CreateException {
		try {
			TaLAvoirMapper mapper = new TaLAvoirMapper();
			TaLAvoir entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLAvoirDTO dto) throws RemoveException {
		try {
			TaLAvoirMapper mapper = new TaLAvoirMapper();
			TaLAvoir entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLAvoir refresh(TaLAvoir persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLAvoir value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLAvoir value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLAvoirDTO dto, String validationContext) {
		try {
			TaLAvoirMapper mapper = new TaLAvoirMapper();
			TaLAvoir entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<LAvoirDTO> validator = new BeanValidator<LAvoirDTO>(LAvoirDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLAvoirDTO dto, String propertyName, String validationContext) {
		try {
			TaLAvoirMapper mapper = new TaLAvoirMapper();
			TaLAvoir entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<LAvoirDTO> validator = new BeanValidator<LAvoirDTO>(LAvoirDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLAvoirDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLAvoirDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLAvoir value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLAvoir value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
