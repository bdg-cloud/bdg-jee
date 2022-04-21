package fr.legrain.edition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import fr.legrain.bdg.edition.service.remote.ITaEditionCatalogueServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaEditionCatalogueMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.edition.dao.IEditionCatalogueDAO;
import fr.legrain.edition.dto.TaEditionCatalogueDTO;
import fr.legrain.edition.model.TaEditionCatalogue;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEditionCatalogueService extends AbstractApplicationDAOServer<TaEditionCatalogue, TaEditionCatalogueDTO> implements ITaEditionCatalogueServiceRemote {

	static Logger logger = Logger.getLogger(TaEditionCatalogueService.class);

	@Inject private IEditionCatalogueDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaEditionCatalogueService() {
		super(TaEditionCatalogue.class,TaEditionCatalogueDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaEditionCatalogue a";
	
	public List<TaEditionCatalogueDTO> findAllLight() {
		return dao.findAllLight();
	}

	public TaEditionCatalogueDTO findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaEditionCatalogue> rechercheEditionDisponibleProgrammeDefaut() {
		List<TaEditionCatalogue> l = new ArrayList<>();
//		InputStream is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_FACTURE);
//		byte[] b = null;
//		try {
//			b = IOUtils.toByteArray(is);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		TaEditionCatalogue e = new TaEditionCatalogue();
//		e.setLibelleEdition("tt");
//		e.setCodeEdition("tt");
//		e.setFichierBlob(b);
//		
//		l.add(e);
		return l;
	}
		
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees) {
		
		List<TaEditionCatalogueDTO> l = new ArrayList<>();
		l.addAll(dao.rechercheEditionDisponible(codeDossier, codeTypeEdition, idAction, listeCodeEditionDejaImportees));
//		l.addAll(rechercheEditionDisponibleProgrammeDefaut());
		return l;
	}
	
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, Map<String,String> mapCodeEditionDejaImporteesVersion) {
		List<TaEditionCatalogueDTO> l = new ArrayList<>();
		l.addAll(dao.rechercheEditionDisponible(codeDossier, codeTypeEdition, idAction, mapCodeEditionDejaImporteesVersion));
//		l.addAll(rechercheEditionDisponibleProgrammeDefaut());
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaEditionCatalogueDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaEditionCatalogueDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaEditionCatalogue transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEditionCatalogue transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEditionCatalogue persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdEdition()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaEditionCatalogue merge(TaEditionCatalogue detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEditionCatalogue merge(TaEditionCatalogue detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEditionCatalogue findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEditionCatalogue findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEditionCatalogue> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaEditionCatalogueDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaEditionCatalogueDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEditionCatalogue> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaEditionCatalogueDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaEditionCatalogueDTO entityToDTO(TaEditionCatalogue entity) {
//		TaEditionCatalogueDTO dto = new TaEditionCatalogueDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEditionCatalogueMapper mapper = new TaEditionCatalogueMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaEditionCatalogueDTO> listEntityToListDTO(List<TaEditionCatalogue> entity) {
		List<TaEditionCatalogueDTO> l = new ArrayList<TaEditionCatalogueDTO>();

		for (TaEditionCatalogue taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaEditionCatalogueDTO> selectAllDTO() {
		System.out.println("List of TaEditionCatalogueDTO EJB :");
		ArrayList<TaEditionCatalogueDTO> liste = new ArrayList<TaEditionCatalogueDTO>();

		List<TaEditionCatalogue> projects = selectAll();
		for(TaEditionCatalogue project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaEditionCatalogueDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaEditionCatalogueDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaEditionCatalogueDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaEditionCatalogueDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaEditionCatalogueDTO dto, String validationContext) throws EJBException {
		try {
			TaEditionCatalogueMapper mapper = new TaEditionCatalogueMapper();
			TaEditionCatalogue entity = null;
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
	
	public void persistDTO(TaEditionCatalogueDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaEditionCatalogueDTO dto, String validationContext) throws CreateException {
		try {
			TaEditionCatalogueMapper mapper = new TaEditionCatalogueMapper();
			TaEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaEditionCatalogueDTO dto) throws RemoveException {
		try {
			TaEditionCatalogueMapper mapper = new TaEditionCatalogueMapper();
			TaEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEditionCatalogue refresh(TaEditionCatalogue persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEditionCatalogue value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEditionCatalogue value, String propertyName, String validationContext) {
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
	public void validateDTO(TaEditionCatalogueDTO dto, String validationContext) {
		try {
			TaEditionCatalogueMapper mapper = new TaEditionCatalogueMapper();
			TaEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEditionCatalogueDTO> validator = new BeanValidator<TaEditionCatalogueDTO>(TaEditionCatalogueDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaEditionCatalogueDTO dto, String propertyName, String validationContext) {
		try {
			TaEditionCatalogueMapper mapper = new TaEditionCatalogueMapper();
			TaEditionCatalogue entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEditionCatalogueDTO> validator = new BeanValidator<TaEditionCatalogueDTO>(TaEditionCatalogueDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaEditionCatalogueDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaEditionCatalogueDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEditionCatalogue value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEditionCatalogue value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
