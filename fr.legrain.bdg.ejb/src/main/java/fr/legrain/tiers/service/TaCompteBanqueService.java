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

import fr.legrain.bdg.model.mapping.mapper.TaCompteBanqueMapper;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaCompteBanqueBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCompteBanqueService extends AbstractApplicationDAOServer<TaCompteBanque, TaCompteBanqueDTO> implements ITaCompteBanqueServiceRemote {

	static Logger logger = Logger.getLogger(TaCompteBanqueService.class);

	@Inject private ICompteBanqueDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCompteBanqueService() {
		super(TaCompteBanque.class,TaCompteBanqueDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCompteBanque a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCompteBanque transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCompteBanque transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCompteBanque persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCompteBanque()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCompteBanque merge(TaCompteBanque detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCompteBanque merge(TaCompteBanque detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCompteBanque findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCompteBanque findByCode(String iban) throws FinderException {
		return dao.findByCode(iban);
	}

//	@RolesAllowed("admin")
	public List<TaCompteBanque> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCompteBanqueDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCompteBanqueDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCompteBanque> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCompteBanqueDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCompteBanqueDTO entityToDTO(TaCompteBanque entity) {
//		TaCompteBanqueDTO dto = new TaCompteBanqueDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCompteBanqueDTO> listEntityToListDTO(List<TaCompteBanque> entity) {
		List<TaCompteBanqueDTO> l = new ArrayList<TaCompteBanqueDTO>();

		for (TaCompteBanque taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCompteBanqueDTO> selectAllDTO() {
		System.out.println("List of TaCompteBanqueDTO EJB :");
		ArrayList<TaCompteBanqueDTO> liste = new ArrayList<TaCompteBanqueDTO>();

		List<TaCompteBanque> projects = selectAll();
		for(TaCompteBanque project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCompteBanqueDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCompteBanqueDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCompteBanqueDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCompteBanqueDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}
	

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCompteBanqueDTO dto, String validationContext) throws EJBException {
		try {
			TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
			TaCompteBanque entity = null;
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
	
	public void persistDTO(TaCompteBanqueDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCompteBanqueDTO dto, String validationContext) throws CreateException {
		try {
			TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
			TaCompteBanque entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCompteBanqueDTO dto) throws RemoveException {
		try {
			TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
			TaCompteBanque entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCompteBanque refresh(TaCompteBanque persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCompteBanque value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCompteBanque value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCompteBanqueDTO dto, String validationContext) {
		try {
			TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
			TaCompteBanque entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCompteBanqueDTO> validator = new BeanValidator<TaCompteBanqueDTO>(TaCompteBanqueDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCompteBanqueDTO dto, String propertyName, String validationContext) {
		try {
			TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
			TaCompteBanque entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCompteBanqueDTO> validator = new BeanValidator<TaCompteBanqueDTO>(TaCompteBanqueDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCompteBanqueDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCompteBanqueDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCompteBanque value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCompteBanque value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaCompteBanque findByTiersEntreprise(TaTPaiement tPaiement) {
		// TODO Auto-generated method stub
		return dao.findByTiersEntreprise(tPaiement);
	}

	@Override
	public TaCompteBanque findByTiersEntreprise() {
		// TODO Auto-generated method stub
		return dao.findByTiersEntreprise();
	}

	@Override
	public List<TaCompteBanque> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement) {
		// TODO Auto-generated method stub
		return dao.findByTiersEntrepriseListeComptePrelevement(tPaiement);
	}

	@Override
	public List<TaCompteBanque> selectCompteEntreprise() {
		// TODO Auto-generated method stub
		return dao.selectCompteEntreprise();
	}

	@Override
	public List<TaCompteBanque> selectCompteTiers(TaTiers taTiers) {
		// TODO Auto-generated method stub
		return dao.selectCompteTiers(taTiers);
	}

}
