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

import fr.legrain.bdg.documents.service.remote.ITaLBonlivServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLBonlivMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLBonlivDTO;
import fr.legrain.document.dto.TaLBonlivDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.documents.dao.ILBonlivDAO;
import fr.legrain.documents.dao.ILFactureDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLBonlivBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLBonlivService extends AbstractApplicationDAOServer<TaLBonliv, TaLBonlivDTO> implements ITaLBonlivServiceRemote {

	static Logger logger = Logger.getLogger(TaLBonlivService.class);

	@Inject private ILBonlivDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLBonlivService() {
		super(TaLBonliv.class,TaLBonlivDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLBonliv a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLBonliv transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLBonliv transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLBonliv persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLBonliv merge(TaLBonliv detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLBonliv merge(TaLBonliv detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLBonliv findById(int id) throws FinderException {
		TaLBonliv o= dao.findById(id);
//		o=(TaLBonliv) recupSetREtat(o.getTaDocument());
//		o=(TaLBonliv) recupSetHistREtat(o.getTaDocument());		
//		o=(TaLBonliv) recupSetLigneALigne(o.getTaDocument());
//		o=(TaLBonliv) recupSetRDocument(o.getTaDocument());
//		o=(TaLBonliv) recupSetREtatLigneDocuments(o.getTaDocument());
//		o=(TaLBonliv) recupSetHistREtatLigneDocuments(o.getTaDocument());
		return o;
	}

	public TaLBonliv findByCode(String code) throws FinderException {
		TaLBonliv o= dao.findByCode(code);
//		o=(TaLBonliv) recupSetREtat(o.getTaDocument());
//		o=(TaLBonliv) recupSetHistREtat(o.getTaDocument());		
//		o=(TaLBonliv) recupSetLigneALigne(o.getTaDocument());
//		o=(TaLBonliv) recupSetRDocument(o.getTaDocument());
//		o=(TaLBonliv) recupSetREtatLigneDocuments(o.getTaDocument());
//		o=(TaLBonliv) recupSetHistREtatLigneDocuments(o.getTaDocument());
		return o;
	}

//	@RolesAllowed("admin")
	public List<TaLBonliv> selectAll() {
		List<TaLBonliv> l= dao.selectAll();
		for (TaLBonliv o : l) {
			o=(TaLBonliv) recupSetREtat(o.getTaDocument());
			o=(TaLBonliv) recupSetHistREtat(o.getTaDocument());		
			o=(TaLBonliv) recupSetLigneALigne(o.getTaDocument());
			o=(TaLBonliv) recupSetRDocument(o.getTaDocument());
			o=(TaLBonliv) recupSetREtatLigneDocuments(o.getTaDocument());
			o=(TaLBonliv) recupSetHistREtatLigneDocuments(o.getTaDocument());		
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLBonlivDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLBonlivDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLBonliv> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLBonlivDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLBonlivDTO entityToDTO(TaLBonliv entity) {
//		LFactureDTO dto = new LFactureDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLBonlivMapper mapper = new TaLBonlivMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLBonlivDTO> listEntityToListDTO(List<TaLBonliv> entity) {
		List<TaLBonlivDTO> l = new ArrayList<TaLBonlivDTO>();

		for (TaLBonliv taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLBonlivDTO> selectAllDTO() {
		System.out.println("List of LFactureDTO EJB :");
		ArrayList<TaLBonlivDTO> liste = new ArrayList<TaLBonlivDTO>();

		List<TaLBonliv> projects = selectAll();
		for(TaLBonliv project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLBonlivDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLBonlivDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLBonlivDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLBonlivDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLBonlivDTO dto, String validationContext) throws EJBException {
		try {
			TaLBonlivMapper mapper = new TaLBonlivMapper();
			TaLBonliv entity = null;
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
	
	public void persistDTO(TaLBonlivDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLBonlivDTO dto, String validationContext) throws CreateException {
		try {
			TaLBonlivMapper mapper = new TaLBonlivMapper();
			TaLBonliv entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLBonlivDTO dto) throws RemoveException {
		try {
			TaLBonlivMapper mapper = new TaLBonlivMapper();
			TaLBonliv entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLBonliv refresh(TaLBonliv persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLBonliv value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLBonliv value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLBonlivDTO dto, String validationContext) {
		try {
			TaLBonlivMapper mapper = new TaLBonlivMapper();
			TaLBonliv entity = mapper.mapDtoToEntity(dto,null);
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
	public void validateDTOProperty(TaLBonlivDTO dto, String propertyName, String validationContext) {
		try {
			TaLBonlivMapper mapper = new TaLBonlivMapper();
			TaLBonliv entity = mapper.mapDtoToEntity(dto,null);
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
	public void validateDTO(TaLBonlivDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLBonlivDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLBonliv value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLBonliv value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
