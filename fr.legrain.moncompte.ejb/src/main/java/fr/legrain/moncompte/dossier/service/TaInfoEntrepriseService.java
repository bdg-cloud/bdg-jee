package fr.legrain.moncompte.dossier.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.LocalBean;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dossier.dao.IInfoEntrepriseDAO;
import fr.legrain.moncompte.dossier.dto.TaInfoEntrepriseDTO;
import fr.legrain.moncompte.dossier.model.TaInfoEntreprise;
import fr.legrain.moncompte.model.mapping.mapper.TaInfoEntrepriseMapper;

//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@WebService
public class TaInfoEntrepriseService extends AbstractApplicationDAOServer<TaInfoEntreprise, TaInfoEntrepriseDTO> implements ITaInfoEntrepriseServiceRemote,ITaInfoEntrepriseServiceLocal {

	static Logger logger = Logger.getLogger(TaInfoEntrepriseService.class);

	@Inject private IInfoEntrepriseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfoEntrepriseService() {
		super(TaInfoEntreprise.class,TaInfoEntrepriseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfoEntreprise a";
	
	public TaInfoEntreprise findInstance() {
		return dao.findInstance();
	}
	
	public Date dateDansExercice(Date valeur) throws Exception {
		TaInfoEntreprise taInfoEntreprise = dao.findInstance();
		// si date inférieur à dateDeb dossier
		if (LibDate.compareTo(valeur, taInfoEntreprise.getDatedebInfoEntreprise()) == -1) {
			return taInfoEntreprise.getDatedebInfoEntreprise();
		} else
			// si date supérieur à dateFin dossier
			if (LibDate.compareTo(valeur, taInfoEntreprise.getDatefinInfoEntreprise()) == 1) {
				return taInfoEntreprise.getDatefinInfoEntreprise();
			}
		//return new Date();
	return valeur;
}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfoEntreprise transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
		
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfoEntreprise transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfoEntreprise persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfoEntreprise merge(TaInfoEntreprise detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfoEntreprise merge(TaInfoEntreprise detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfoEntreprise findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfoEntreprise findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfoEntreprise> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaInfoEntrepriseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaInfoEntrepriseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfoEntreprise> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaInfoEntrepriseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaInfoEntrepriseDTO entityToDTO(TaInfoEntreprise entity) {
		TaInfoEntrepriseMapper mapper = new TaInfoEntrepriseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaInfoEntrepriseDTO> listEntityToListDTO(List<TaInfoEntreprise> entity) {
		List<TaInfoEntrepriseDTO> l = new ArrayList<TaInfoEntrepriseDTO>();

		for (TaInfoEntreprise TaInfoEntreprise : entity) {
			l.add(entityToDTO(TaInfoEntreprise));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaInfoEntrepriseDTO> selectAllDTO() {
		System.out.println("List of TaInfoEntrepriseDTO EJB :");
		ArrayList<TaInfoEntrepriseDTO> liste = new ArrayList<TaInfoEntrepriseDTO>();

		List<TaInfoEntreprise> projects = selectAll();
		for(TaInfoEntreprise project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaInfoEntrepriseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaInfoEntrepriseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaInfoEntrepriseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaInfoEntrepriseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaInfoEntrepriseDTO dto, String validationContext) throws EJBException {
		try {
			TaInfoEntrepriseMapper mapper = new TaInfoEntrepriseMapper();
			TaInfoEntreprise entity = null;
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
	
	public void persistDTO(TaInfoEntrepriseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaInfoEntrepriseDTO dto, String validationContext) throws CreateException {
		try {
			TaInfoEntrepriseMapper mapper = new TaInfoEntrepriseMapper();
			TaInfoEntreprise entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaInfoEntrepriseDTO dto) throws RemoveException {
		try {
			TaInfoEntrepriseMapper mapper = new TaInfoEntrepriseMapper();
			TaInfoEntreprise entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfoEntreprise refresh(TaInfoEntreprise persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfoEntreprise value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfoEntreprise value, String propertyName, String validationContext) {
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
	public void validateDTO(TaInfoEntrepriseDTO dto, String validationContext) {
		try {
			TaInfoEntrepriseMapper mapper = new TaInfoEntrepriseMapper();
			TaInfoEntreprise entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaInfoEntrepriseDTO> validator = new BeanValidator<TaInfoEntrepriseDTO>(TaInfoEntrepriseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaInfoEntrepriseDTO dto, String propertyName, String validationContext) {
		try {
			TaInfoEntrepriseMapper mapper = new TaInfoEntrepriseMapper();
			TaInfoEntreprise entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaInfoEntrepriseDTO> validator = new BeanValidator<TaInfoEntrepriseDTO>(TaInfoEntrepriseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaInfoEntrepriseDTO dto) {
		validateDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaInfoEntrepriseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfoEntreprise value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfoEntreprise value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
