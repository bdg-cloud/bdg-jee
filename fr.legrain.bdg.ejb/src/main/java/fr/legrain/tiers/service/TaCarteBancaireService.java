package fr.legrain.tiers.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaCarteBancaireMapper;
import fr.legrain.bdg.tiers.service.remote.ITaCarteBancaireServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCarteBancaireServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ICarteBancaireDAO;
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaCarteBancaireBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCarteBancaireService extends AbstractApplicationDAOServer<TaCarteBancaire, TaCarteBancaireDTO> implements ITaCarteBancaireServiceRemote {

	static Logger logger = Logger.getLogger(TaCarteBancaireService.class);

	@Inject private ICarteBancaireDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCarteBancaireService() {
		super(TaCarteBancaire.class,TaCarteBancaireDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCarteBancaire a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCarteBancaire transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCarteBancaire transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCarteBancaire persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCarteBancaire()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCarteBancaire merge(TaCarteBancaire detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCarteBancaire merge(TaCarteBancaire detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCarteBancaire findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCarteBancaire findByCode(String iban) throws FinderException {
		return dao.findByCode(iban);
	}

//	@RolesAllowed("admin")
	public List<TaCarteBancaire> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCarteBancaireDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCarteBancaireDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCarteBancaire> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCarteBancaireDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCarteBancaireDTO entityToDTO(TaCarteBancaire entity) {
//		TaCarteBancaireDTO dto = new TaCarteBancaireDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCarteBancaireMapper mapper = new TaCarteBancaireMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCarteBancaireDTO> listEntityToListDTO(List<TaCarteBancaire> entity) {
		List<TaCarteBancaireDTO> l = new ArrayList<TaCarteBancaireDTO>();

		for (TaCarteBancaire taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCarteBancaireDTO> selectAllDTO() {
		System.out.println("List of TaCarteBancaireDTO EJB :");
		ArrayList<TaCarteBancaireDTO> liste = new ArrayList<TaCarteBancaireDTO>();

		List<TaCarteBancaire> projects = selectAll();
		for(TaCarteBancaire project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCarteBancaireDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCarteBancaireDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCarteBancaireDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCarteBancaireDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}
	

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCarteBancaireDTO dto, String validationContext) throws EJBException {
		try {
			TaCarteBancaireMapper mapper = new TaCarteBancaireMapper();
			TaCarteBancaire entity = null;
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
	
	public void persistDTO(TaCarteBancaireDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCarteBancaireDTO dto, String validationContext) throws CreateException {
		try {
			TaCarteBancaireMapper mapper = new TaCarteBancaireMapper();
			TaCarteBancaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCarteBancaireDTO dto) throws RemoveException {
		try {
			TaCarteBancaireMapper mapper = new TaCarteBancaireMapper();
			TaCarteBancaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCarteBancaire refresh(TaCarteBancaire persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCarteBancaire value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCarteBancaire value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCarteBancaireDTO dto, String validationContext) {
		try {
			TaCarteBancaireMapper mapper = new TaCarteBancaireMapper();
			TaCarteBancaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCarteBancaireDTO> validator = new BeanValidator<TaCarteBancaireDTO>(TaCarteBancaireDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCarteBancaireDTO dto, String propertyName, String validationContext) {
		try {
			TaCarteBancaireMapper mapper = new TaCarteBancaireMapper();
			TaCarteBancaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCarteBancaireDTO> validator = new BeanValidator<TaCarteBancaireDTO>(TaCarteBancaireDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCarteBancaireDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCarteBancaireDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCarteBancaire value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCarteBancaire value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaCarteBancaire findByTiersEntreprise(TaTPaiement tPaiement) {
		// TODO Auto-generated method stub
		return dao.findByTiersEntreprise(tPaiement);
	}

	@Override
	public TaCarteBancaire findByTiersEntreprise() {
		// TODO Auto-generated method stub
		return dao.findByTiersEntreprise();
	}

	@Override
	public List<TaCarteBancaire> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement) {
		// TODO Auto-generated method stub
		return dao.findByTiersEntrepriseListeComptePrelevement(tPaiement);
	}

	@Override
	public List<TaCarteBancaire> selectCompteEntreprise() {
		// TODO Auto-generated method stub
		return dao.selectCompteEntreprise();
	}

	@Override
	public List<TaCarteBancaire> selectCompteTiers(TaTiers taTiers) {
		// TODO Auto-generated method stub
		return dao.selectCompteTiers(taTiers);
	}

}
