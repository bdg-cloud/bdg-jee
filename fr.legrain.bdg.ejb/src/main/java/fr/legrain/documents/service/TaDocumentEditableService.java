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

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.documents.service.remote.ITaDocumentEditableServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaDocumentEditableMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.DocumentEditableDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaDocumentEditable;
import fr.legrain.documents.dao.IDocumentEditableDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITCPaiementDAO;

/**
 * Session Bean implementation class TaDocumentEditableBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaDocumentEditableService extends AbstractApplicationDAOServer<TaDocumentEditable, DocumentEditableDTO> implements ITaDocumentEditableServiceRemote {

	static Logger logger = Logger.getLogger(TaDocumentEditableService.class);

	@Inject private IDocumentEditableDAO dao;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;

	/**
	 * Default constructor. 
	 */
	public TaDocumentEditableService() {
		super(TaDocumentEditable.class,DocumentEditableDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaDocumentEditable a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaDocumentEditable transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaDocumentEditable transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaDocumentEditable persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdDocumentDoc()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaDocumentEditable merge(TaDocumentEditable detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaDocumentEditable merge(TaDocumentEditable detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaDocumentEditable findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaDocumentEditable findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaDocumentEditable> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<DocumentEditableDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<DocumentEditableDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaDocumentEditable> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<DocumentEditableDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public DocumentEditableDTO entityToDTO(TaDocumentEditable entity) {
//		DocumentEditableDTO dto = new DocumentEditableDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaDocumentEditableMapper mapper = new TaDocumentEditableMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<DocumentEditableDTO> listEntityToListDTO(List<TaDocumentEditable> entity) {
		List<DocumentEditableDTO> l = new ArrayList<DocumentEditableDTO>();

		for (TaDocumentEditable taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<DocumentEditableDTO> selectAllDTO() {
		System.out.println("List of DocumentEditableDTO EJB :");
		ArrayList<DocumentEditableDTO> liste = new ArrayList<DocumentEditableDTO>();

		List<TaDocumentEditable> projects = selectAll();
		for(TaDocumentEditable project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public DocumentEditableDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public DocumentEditableDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(DocumentEditableDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(DocumentEditableDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(DocumentEditableDTO dto, String validationContext) throws EJBException {
		try {
			TaDocumentEditableMapper mapper = new TaDocumentEditableMapper();
			TaDocumentEditable entity = null;
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
	
	public void persistDTO(DocumentEditableDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(DocumentEditableDTO dto, String validationContext) throws CreateException {
		try {
			TaDocumentEditableMapper mapper = new TaDocumentEditableMapper();
			TaDocumentEditable entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(DocumentEditableDTO dto) throws RemoveException {
		try {
			TaDocumentEditableMapper mapper = new TaDocumentEditableMapper();
			TaDocumentEditable entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaDocumentEditable refresh(TaDocumentEditable persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaDocumentEditable value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaDocumentEditable value, String propertyName, String validationContext) {
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
	public void validateDTO(DocumentEditableDTO dto, String validationContext) {
		try {
			TaDocumentEditableMapper mapper = new TaDocumentEditableMapper();
			TaDocumentEditable entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<DocumentEditableDTO> validator = new BeanValidator<DocumentEditableDTO>(DocumentEditableDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(DocumentEditableDTO dto, String propertyName, String validationContext) {
		try {
			TaDocumentEditableMapper mapper = new TaDocumentEditableMapper();
			TaDocumentEditable entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<DocumentEditableDTO> validator = new BeanValidator<DocumentEditableDTO>(DocumentEditableDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(DocumentEditableDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(DocumentEditableDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaDocumentEditable value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaDocumentEditable value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaDocumentEditable> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocument(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocument(String codeDeb,
			String codeFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocument(Date dateDeb,
			Date dateFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocument(String codeDeb,
			String codeFin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocument(Date dateDeb,
			Date dateFin, String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocument(String codeDeb,
			String codeFin, String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<TaDocumentEditable> findByCodeTypeDoc(String codeTypeDoc) {
		return dao.findByCodeTypeDoc(codeTypeDoc);
	}

	@Override
	public TaDocumentEditable findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaDocumentEditable> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaDocumentEditable> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}

}
