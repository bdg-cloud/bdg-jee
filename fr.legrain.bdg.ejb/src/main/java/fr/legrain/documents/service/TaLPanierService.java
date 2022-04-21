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

import fr.legrain.bdg.documents.service.remote.ITaLPanierServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLPanierMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.documents.dao.ILPanierDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLPanierBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLPanierService extends AbstractApplicationDAOServer<TaLPanier, TaLPanierDTO> implements ITaLPanierServiceRemote {

	static Logger logger = Logger.getLogger(TaLPanierService.class);

	@Inject private ILPanierDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLPanierService() {
		super(TaLPanier.class,TaLPanierDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLPanier a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLPanier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLPanier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLPanier persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLPanier merge(TaLPanier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLPanier merge(TaLPanier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLPanier findById(int id) throws FinderException {
		TaLPanier o = dao.findById(id);
		recupSetLigneALigne(o.getTaDocument());
		recupSetRDocument(o.getTaDocument());
		recupSetREtatLigneDocuments(o.getTaDocument());
		recupSetHistREtatLigneDocuments(o.getTaDocument());
		recupSetLigneALigneEcheance(o.getTaDocument());
		return o;
	}

	public TaLPanier findByCode(String code) throws FinderException {
		TaLPanier o = dao.findByCode(code);
		recupSetLigneALigne(o.getTaDocument());
		recupSetRDocument(o.getTaDocument());
		recupSetREtatLigneDocuments(o.getTaDocument());
		recupSetHistREtatLigneDocuments(o.getTaDocument());

		return o;
	}
	
	

	
	
	
//	@RolesAllowed("admin")
	public List<TaLPanier> selectAll() {
		List<TaLPanier> l= dao.selectAll();
		for (TaLPanier o : l) {
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

	public List<TaLPanierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLPanierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLPanier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLPanierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLPanierDTO entityToDTO(TaLPanier entity) {
//		LFactureDTO dto = new LFactureDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLPanierMapper mapper = new TaLPanierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLPanierDTO> listEntityToListDTO(List<TaLPanier> entity) {
		List<TaLPanierDTO> l = new ArrayList<TaLPanierDTO>();

		for (TaLPanier taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLPanierDTO> selectAllDTO() {
		System.out.println("List of LFactureDTO EJB :");
		ArrayList<TaLPanierDTO> liste = new ArrayList<TaLPanierDTO>();

		List<TaLPanier> projects = selectAll();
		for(TaLPanier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLPanierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLPanierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLPanierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLPanierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLPanierDTO dto, String validationContext) throws EJBException {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLPanier entity = null;
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
	
	public void persistDTO(TaLPanierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLPanierDTO dto, String validationContext) throws CreateException {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLPanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLPanierDTO dto) throws RemoveException {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLPanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLPanier refresh(TaLPanier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLPanier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLPanier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLPanierDTO dto, String validationContext) {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLPanier entity = mapper.mapDtoToEntity(dto,null);
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
	public void validateDTOProperty(TaLPanierDTO dto, String propertyName, String validationContext) {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLPanier entity = mapper.mapDtoToEntity(dto,null);
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
	public void validateDTO(TaLPanierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLPanierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLPanier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLPanier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
