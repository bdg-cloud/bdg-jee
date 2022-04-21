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

import fr.legrain.bdg.documents.service.remote.ITaLBoncdeAchatServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLBoncdeAchatMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLBoncdeAchatDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.documents.dao.ILBoncdeAchatDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLBoncdeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLBoncdeAchatService extends AbstractApplicationDAOServer<TaLBoncdeAchat, TaLBoncdeAchatDTO> implements ITaLBoncdeAchatServiceRemote {

	static Logger logger = Logger.getLogger(TaLBoncdeAchatService.class);

	@Inject private ILBoncdeAchatDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLBoncdeAchatService() {
		super(TaLBoncdeAchat.class,TaLBoncdeAchatDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLBoncdeAchat a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLBoncdeAchat transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLBoncdeAchat transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLBoncdeAchat persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLBoncdeAchat merge(TaLBoncdeAchat detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLBoncdeAchat merge(TaLBoncdeAchat detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLBoncdeAchat findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLBoncdeAchat findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLBoncdeAchat> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLBoncdeAchatDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLBoncdeAchatDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLBoncdeAchat> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLBoncdeAchatDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLBoncdeAchatDTO entityToDTO(TaLBoncdeAchat entity) {
//		TaLBoncdeAchatDTO dto = new TaLBoncdeAchatDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLBoncdeAchatMapper mapper = new TaLBoncdeAchatMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLBoncdeAchatDTO> listEntityToListDTO(List<TaLBoncdeAchat> entity) {
		List<TaLBoncdeAchatDTO> l = new ArrayList<TaLBoncdeAchatDTO>();

		for (TaLBoncdeAchat taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLBoncdeAchatDTO> selectAllDTO() {
		System.out.println("List of TaLBoncdeAchatDTO EJB :");
		ArrayList<TaLBoncdeAchatDTO> liste = new ArrayList<TaLBoncdeAchatDTO>();

		List<TaLBoncdeAchat> projects = selectAll();
		for(TaLBoncdeAchat project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLBoncdeAchatDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLBoncdeAchatDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLBoncdeAchatDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLBoncdeAchatDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLBoncdeAchatDTO dto, String validationContext) throws EJBException {
		try {
			TaLBoncdeAchatMapper mapper = new TaLBoncdeAchatMapper();
			TaLBoncdeAchat entity = null;
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
	
	public void persistDTO(TaLBoncdeAchatDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLBoncdeAchatDTO dto, String validationContext) throws CreateException {
		try {
			TaLBoncdeAchatMapper mapper = new TaLBoncdeAchatMapper();
			TaLBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLBoncdeAchatDTO dto) throws RemoveException {
		try {
			TaLBoncdeAchatMapper mapper = new TaLBoncdeAchatMapper();
			TaLBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLBoncdeAchat refresh(TaLBoncdeAchat persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLBoncdeAchat value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLBoncdeAchat value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLBoncdeAchatDTO dto, String validationContext) {
		try {
			TaLBoncdeAchatMapper mapper = new TaLBoncdeAchatMapper();
			TaLBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLBoncdeAchatDTO> validator = new BeanValidator<TaLBoncdeAchatDTO>(TaLBoncdeAchatDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLBoncdeAchatDTO dto, String propertyName, String validationContext) {
		try {
			TaLBoncdeAchatMapper mapper = new TaLBoncdeAchatMapper();
			TaLBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLBoncdeAchatDTO> validator = new BeanValidator<TaLBoncdeAchatDTO>(TaLBoncdeAchatDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLBoncdeAchatDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLBoncdeAchatDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLBoncdeAchat value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLBoncdeAchat value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

//	@Override
//	public List<ILigneDocumentTiers> selectLigneDocNonAffecte2(IDocumentTiers doc, String codeTiers, String typeOrigine,
//			String typeDest, Date debut, Date fin,boolean afficheLigneAvecEtat) {
//		// TODO Auto-generated method stub
//		return dao.selectLigneDocNonAffecte2(doc, codeTiers, typeOrigine, typeDest, debut, fin, afficheLigneAvecEtat);
//	}
	@Override
	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(IDocumentTiers doc, String codeTiers, String typeOrigine,
			String typeDest, Date debut, Date fin,TaEtat etat,String tEtat) {
		// TODO Auto-generated method stub
		return dao.selectLigneDocNonAffecte(doc, codeTiers, typeOrigine, typeDest, debut, fin, etat,tEtat);
	}




}
