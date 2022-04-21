package fr.legrain.bdg.compteclient.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.compteclient.dao.IMesFournisseursDAO;
import fr.legrain.bdg.compteclient.dto.TaMesFournisseursDTO;
import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.bdg.compteclient.model.mapping.mapper.TaMesFournisseursMapper;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaMesFournisseursServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;

@Stateless
public class TaMesFournisseursService extends AbstractApplicationDAOServer<TaMesFournisseurs, TaMesFournisseursDTO> implements ITaMesFournisseursServiceRemote{

	static Logger logger = Logger.getLogger(TaFournisseurService.class);

	@Inject private IMesFournisseursDAO dao;

	/**
	* Default constructor. 
	*/
	public TaMesFournisseursService() {
		super(TaMesFournisseurs.class,TaMesFournisseursDTO.class);
	}
	
	public TaMesFournisseurs findInstance() {
		return dao.findInstance();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaMesFournisseurs transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
		
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaMesFournisseurs transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaMesFournisseurs persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaMesFournisseurs merge(TaMesFournisseurs detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaMesFournisseurs merge(TaMesFournisseurs detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaMesFournisseurs findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaMesFournisseurs findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

	public List<TaMesFournisseurs> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaMesFournisseursDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaMesFournisseursDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaMesFournisseurs> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaMesFournisseursDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaMesFournisseursDTO entityToDTO(TaMesFournisseurs entity) {
		TaMesFournisseursMapper mapper = new TaMesFournisseursMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaMesFournisseursDTO> listEntityToListDTO(List<TaMesFournisseurs> entity) {
		List<TaMesFournisseursDTO> l = new ArrayList<TaMesFournisseursDTO>();

		for (TaMesFournisseurs TaMesFournisseurs : entity) {
			l.add(entityToDTO(TaMesFournisseurs));
		}

		return l;
	}

	public List<TaMesFournisseursDTO> selectAllDTO() {
		System.out.println("List of TaFournisseurDTO EJB :");
		ArrayList<TaMesFournisseursDTO> liste = new ArrayList<TaMesFournisseursDTO>();

		List<TaMesFournisseurs> projects = selectAll();
		for(TaMesFournisseurs project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaMesFournisseursDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaMesFournisseursDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaMesFournisseursDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
	}
	
	public void mergeDTO(TaMesFournisseursDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaMesFournisseursDTO dto, String validationContext) throws EJBException {
		try {
			TaMesFournisseursMapper mapper = new TaMesFournisseursMapper();
			TaMesFournisseurs entity = null;
			if(dto.getIdFournisseur()!=0) {
				entity = dao.findById(dto.getIdFournisseur());
				if(dto.getVersionObj()!=entity.getTaFournisseur().getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdFournisseur()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getTaFournisseur().getVersionObj());
				} else {
					entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaMesFournisseursDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaMesFournisseursDTO dto, String validationContext) throws CreateException {
		try {
			TaMesFournisseursMapper mapper = new TaMesFournisseursMapper();
			TaMesFournisseurs entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaMesFournisseursDTO dto) throws RemoveException {
		try {
			TaMesFournisseursMapper mapper = new TaMesFournisseursMapper();
			TaMesFournisseurs entity = mapper.mapDtoToEntity(dto,null);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaMesFournisseurs refresh(TaMesFournisseurs persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaMesFournisseurs value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaMesFournisseurs value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaMesFournisseursDTO dto, String validationContext) {
		try {
			TaMesFournisseursMapper mapper = new TaMesFournisseursMapper();
			TaMesFournisseurs entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaMesFournisseursDTO dto, String propertyName, String validationContext) {
		try {
			TaMesFournisseursMapper mapper = new TaMesFournisseursMapper();
			TaMesFournisseurs entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaMesFournisseursDTO dto) {
		validateDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaMesFournisseursDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaMesFournisseurs value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaMesFournisseurs value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaMesFournisseurs> rechercheMesFournisseurs(int idUtilisateur){
		return dao.rechercheMesFournisseurs(idUtilisateur);
	}
	
	public boolean verifieSiLiaisonClientFournisseurExisteDeja(int idFournisseur, String codeClient){
		return dao.verifieSiLiaisonClientFournisseurExisteDeja(idFournisseur,codeClient);
	}
}
