package fr.legrain.servicewebexterne.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaCompteServiceWebExterneMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.dao.ICompteServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaCompteServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCompteServiceWebExterneService extends AbstractApplicationDAOServer<TaCompteServiceWebExterne, TaCompteServiceWebExterneDTO> implements ITaCompteServiceWebExterneServiceRemote {

	static Logger logger = Logger.getLogger(TaCompteServiceWebExterneService.class);

	@Inject private ICompteServiceWebExterneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCompteServiceWebExterneService() {
		super(TaCompteServiceWebExterne.class,TaCompteServiceWebExterneDTO.class);
	}
	
//	public List<TaCompteServiceWebExterne> findAgendaUtilisateur(TaUtilisateur utilisateur) {
//		return dao.findAgendaUtilisateur(utilisateur);
//	}
	
	//	private String defaultJPQLQuery = "select a from TaCompteServiceWebExterne a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCompteServiceWebExterne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCompteServiceWebExterne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCompteServiceWebExterne persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCompteServiceWebExterne()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCompteServiceWebExterne merge(TaCompteServiceWebExterne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCompteServiceWebExterne merge(TaCompteServiceWebExterne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}
	
	public void changeDefaut(TaCompteServiceWebExterne detachedInstance) {
		dao.changeDefaut(detachedInstance);
	}

	public TaCompteServiceWebExterne findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCompteServiceWebExterne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCompteServiceWebExterne> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCompteServiceWebExterneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCompteServiceWebExterneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCompteServiceWebExterne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCompteServiceWebExterneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCompteServiceWebExterneDTO entityToDTO(TaCompteServiceWebExterne entity) {
//		TaCompteServiceWebExterneDTO dto = new TaCompteServiceWebExterneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCompteServiceWebExterneMapper mapper = new TaCompteServiceWebExterneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCompteServiceWebExterneDTO> listEntityToListDTO(List<TaCompteServiceWebExterne> entity) {
		List<TaCompteServiceWebExterneDTO> l = new ArrayList<TaCompteServiceWebExterneDTO>();

		for (TaCompteServiceWebExterne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCompteServiceWebExterneDTO> selectAllDTO() {
		System.out.println("List of TaCompteServiceWebExterneDTO EJB :");
		ArrayList<TaCompteServiceWebExterneDTO> liste = new ArrayList<TaCompteServiceWebExterneDTO>();

		List<TaCompteServiceWebExterne> projects = selectAll();
		for(TaCompteServiceWebExterne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCompteServiceWebExterneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCompteServiceWebExterneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCompteServiceWebExterneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCompteServiceWebExterneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCompteServiceWebExterneDTO dto, String validationContext) throws EJBException {
		try {
			TaCompteServiceWebExterneMapper mapper = new TaCompteServiceWebExterneMapper();
			TaCompteServiceWebExterne entity = null;
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
	
	public void persistDTO(TaCompteServiceWebExterneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCompteServiceWebExterneDTO dto, String validationContext) throws CreateException {
		try {
			TaCompteServiceWebExterneMapper mapper = new TaCompteServiceWebExterneMapper();
			TaCompteServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCompteServiceWebExterneDTO dto) throws RemoveException {
		try {
			TaCompteServiceWebExterneMapper mapper = new TaCompteServiceWebExterneMapper();
			TaCompteServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCompteServiceWebExterne refresh(TaCompteServiceWebExterne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCompteServiceWebExterne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCompteServiceWebExterne value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCompteServiceWebExterneDTO dto, String validationContext) {
		try {
			TaCompteServiceWebExterneMapper mapper = new TaCompteServiceWebExterneMapper();
			TaCompteServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCompteServiceWebExterneDTO> validator = new BeanValidator<TaCompteServiceWebExterneDTO>(TaCompteServiceWebExterneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCompteServiceWebExterneDTO dto, String propertyName, String validationContext) {
		try {
			TaCompteServiceWebExterneMapper mapper = new TaCompteServiceWebExterneMapper();
			TaCompteServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCompteServiceWebExterneDTO> validator = new BeanValidator<TaCompteServiceWebExterneDTO>(TaCompteServiceWebExterneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCompteServiceWebExterneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCompteServiceWebExterneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCompteServiceWebExterne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCompteServiceWebExterne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne) {
		return dao.findCompteDefautPourAction(codeTypeServiceWebExterne);
	}
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne, boolean compteTest){
		return dao.findCompteDefautPourAction(codeTypeServiceWebExterne,compteTest);
	}
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne){
		return dao.findCompteDefautPourType(codeTypeServiceWebExterne);
	}
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne, boolean compteTest){
		return dao.findCompteDefautPourType(codeTypeServiceWebExterne,compteTest);
	}
	
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne){
		return dao.findListeComptePourAction(codeTypeServiceWebExterne);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne, boolean compteTest){
		return dao.findListeComptePourAction(codeTypeServiceWebExterne,compteTest);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne){
		return dao.findListeComptePourType(codeTypeServiceWebExterne);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne, boolean compteTest){
		return dao.findListeComptePourType(codeTypeServiceWebExterne,compteTest);
	}
	
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne){
		return dao.findCompteDefautPourFournisseurService(codeServiceWebExterne);
	}
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne, boolean compteTest){
		return dao.findCompteDefautPourFournisseurService(codeServiceWebExterne,compteTest);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne){
		return dao.findListeComptePourFournisseurService(codeServiceWebExterne);
	}
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne, boolean compteTest){
		return dao.findListeComptePourFournisseurService(codeServiceWebExterne,compteTest);
	}

}
