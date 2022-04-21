package fr.legrain.moncompte.service;

import java.rmi.RemoteException;
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
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.autorisations.ws.ListeModules;
import fr.legrain.autorisations.ws.Module;
import fr.legrain.autorisations.ws.client.AutorisationWebServiceClientCXF;
import fr.legrain.bdg.moncompte.service.remote.ITaDossierServiceRemote;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.moncompte.dao.IClientDAO;
import fr.legrain.moncompte.dao.IDossierDAO;
import fr.legrain.moncompte.dao.IUtilisateurDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.TaUtilisateur;
import fr.legrain.moncompte.model.mapping.mapper.TaDossierMapper;


/**
 * Session Bean implementation class TaDossierBean
 */
@Stateless
@DeclareRoles("admin")
public class TaDossierService extends AbstractApplicationDAOServer<TaDossier, TaDossierDTO> implements ITaDossierServiceRemote {

	static Logger logger = Logger.getLogger(TaDossierService.class);

	@Inject private IDossierDAO dao;
	@Inject private IClientDAO daoClient;
	@Inject private IUtilisateurDAO daoUtilisateur;
	
	/**
	 * Default constructor. 
	 */
	public TaDossierService() {
		super(TaDossier.class,TaDossierDTO.class);
	}
	
	public TaClient findClientDossier(String codeDossier) {
		return dao.findClientDossier(codeDossier);
	}
	
	public List<TaDossier> findListeDossierClient(int idClient) {
		return dao.findListeDossierClient(idClient);
	}
	
	public List<TaDossier> findListeDossierProduit(String idProduit) {
		return dao.findListeDossierProduit(idProduit);
	}
	
	//	private String defaultJPQLQuery = "select a from TaDossier a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaDossier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaDossier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaDossier persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}


	
	public TaDossier merge(TaDossier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaDossier merge(TaDossier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaDossier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaDossier findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaDossier> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaDossierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaDossierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaDossier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaDossierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaDossierDTO entityToDTO(TaDossier entity) {
//		TaDossierDTO dto = new TaDossierDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaDossierMapper mapper = new TaDossierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaDossierDTO> listEntityToListDTO(List<TaDossier> entity) {
		List<TaDossierDTO> l = new ArrayList<TaDossierDTO>();

		for (TaDossier taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaDossierDTO> selectAllDTO() {
		System.out.println("List of TaDossierDTO EJB :");
		ArrayList<TaDossierDTO> liste = new ArrayList<TaDossierDTO>();

		List<TaDossier> projects = selectAll();
		for(TaDossier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaDossierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaDossierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaDossierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaDossierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaDossierDTO dto, String validationContext) throws EJBException {
		try {
			TaDossierMapper mapper = new TaDossierMapper();
			TaDossier entity = null;
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
	
	public void persistDTO(TaDossierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaDossierDTO dto, String validationContext) throws CreateException {
		try {
			TaDossierMapper mapper = new TaDossierMapper();
			TaDossier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaDossierDTO dto) throws RemoveException {
		try {
			TaDossierMapper mapper = new TaDossierMapper();
			TaDossier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaDossier refresh(TaDossier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaDossier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaDossier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaDossierDTO dto, String validationContext) {
		try {
			TaDossierMapper mapper = new TaDossierMapper();
			TaDossier entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDossierDTO> validator = new BeanValidator<TaDossierDTO>(TaDossierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaDossierDTO dto, String propertyName, String validationContext) {
		try {
			TaDossierMapper mapper = new TaDossierMapper();
			TaDossier entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDossierDTO> validator = new BeanValidator<TaDossierDTO>(TaDossierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaDossierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaDossierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaDossier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaDossier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

	@Override
	public void removeLigneAutorisation(TaDossier persistentInstance,
			TaAutorisation ligne) throws RemoveException {
		// TODO Auto-generated method stub
		dao.removeLigneAutorisation(persistentInstance,ligne);
	}

	@Override
	public void removeLignePrixPerso(TaDossier persistentInstance,
			TaPrixPerso ligne) throws RemoveException {
		// TODO Auto-generated method stub
		dao.removeLignePrixPerso(persistentInstance,ligne);
	}

	public List<TaDossierDTO> selectAllLight() {
		return dao.selectAllLight();
	}

	public void supprimeDossier(TaDossier dossier) throws RemoveException {
		supprimeDossier(dossier,false,false);
	}
	
	public void supprimeDossier(TaDossier dossier, boolean supprimeClient, boolean supprimeUtilisateur) throws RemoveException {
		TaClient c = dossier.getTaClient();
		TaUtilisateur u = c.getTaUtilisateur();
		if(supprimeUtilisateur || supprimeClient) {
			List<TaDossier> l = dao.findListeDossierClient(dossier.getTaClient().getId());
			for (TaDossier taDossier : l) {
				remove(taDossier);
			}
			daoClient.remove(c);
		}
		if(supprimeUtilisateur) {
			daoUtilisateur.remove(u);
		}
		if(!supprimeUtilisateur && !supprimeClient) {
			remove(dossier);
		} //else dossier deja supprimer dans la boucle de suppression des dossiers du client
	}
	
	public void initDroitModules(TaDossier dossier) {
		AutorisationWebServiceClientCXF wsClient = new AutorisationWebServiceClientCXF();
		//String codeTiers = taClient.getCode();//"demo"
		//codeTiers="aaa";
		int idProduit = 4;
		
		try {
			ListeModules lm = wsClient.listeModulesAutorises(dossier.getCode(), idProduit);
			fr.legrain.autorisation.xml.ListeModules listeXml = new fr.legrain.autorisation.xml.ListeModules();
			if(lm!=null) {
				System.out.println("Modules autorisés pour ce dossier avant update: ");
				
				fr.legrain.autorisation.xml.Module moduleXml = null;
				for (Module m : lm.getModules().getModule()) {
					System.out.println(m.getId());
					
					moduleXml = new fr.legrain.autorisation.xml.Module();
					moduleXml.id = m.getId();
					moduleXml.nom = m.getNom();
					listeXml.module.add(moduleXml);
				}
				System.out.println("=============================");
			}
			
			System.out.println("Modules autorisés pour ce dossier après update: ");
			
			List<TaAutorisation> aut = dossier.getListeAutorisation();
			
			
			listeXml = new fr.legrain.autorisation.xml.ListeModules();
			fr.legrain.autorisation.xml.Module moduleXml = null;
			for (TaAutorisation taAutorisation : aut) {
				//vérifier la date de validité de l'autorisation
				if(taAutorisation.getDateFin().after(new Date())) {
					listeXml = ajouteAutorisation(listeXml,taAutorisation.getTaProduit());
				}
			}
			
			listeXml.nbPosteClient = LibConversion.integerToString(dossier.getNbPosteInstalle());
			listeXml.nbUtilisateur = LibConversion.integerToString(dossier.getNbUtilisateur());
			listeXml.accessWebservice = LibConversion.booleanToString(dossier.getAccesWs());
			
			wsClient.createUpdateDroits(dossier.getCode(), idProduit, listeXml);
			System.out.println("createUpdateDroits ok ");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public fr.legrain.autorisation.xml.ListeModules ajouteAutorisation(fr.legrain.autorisation.xml.ListeModules l, TaProduit p) {
		fr.legrain.autorisation.xml.Module moduleXml = new fr.legrain.autorisation.xml.Module();
		moduleXml.id = p.getIdentifiantModule();
		moduleXml.nom = p.getLibelle();
		if(!l.contientModule(moduleXml)) {
			l.module.add(moduleXml);
		}
		System.out.println(moduleXml.id);
		//gestion des dépendances et compostion
		for(TaProduit prod : p.getListeProduitDependant()) {
			moduleXml = new fr.legrain.autorisation.xml.Module();
			moduleXml.id = prod.getIdentifiantModule();
			if(!l.contientModule(moduleXml)) {
				l.module.add(moduleXml);
			}
			//appel récursif à faire
			if(prod.getListeProduitDependant()!=null) {
				l = ajouteAutorisation(l,prod);
			}
		}
		for(TaProduit prod : p.getListeSousProduit()) {
			moduleXml = new fr.legrain.autorisation.xml.Module();
			moduleXml.id = prod.getIdentifiantModule();
			moduleXml.nom = prod.getLibelle();
			if(!l.contientModule(moduleXml)) {
				l.module.add(moduleXml);
			}
			//appel récursif à faire
			if(prod.getListeSousProduit()!=null) {
				l = ajouteAutorisation(l,prod);
			}
		}
		return l;
	}
}
