package fr.legrain.moncompte.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.moncompte.dao.IClientDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaClientDTO;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.moncompte.model.mapping.mapper.TaClientMapper;


/**
 * Session Bean implementation class TaClientBean
 */
@Stateless
@DeclareRoles("admin")
public class TaClientService extends AbstractApplicationDAOServer<TaClient, TaClientDTO> implements ITaClientServiceRemote {

	static Logger logger = Logger.getLogger(TaClientService.class);

	@Inject private IClientDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaClientService() {
		super(TaClient.class,TaClientDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaClient a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaClient persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaClient merge(TaClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaClient merge(TaClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaClientDTO entityToDTO(TaClient entity) {
//		TaClientDTO dto = new TaClientDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaClientMapper mapper = new TaClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaClientDTO> listEntityToListDTO(List<TaClient> entity) {
		List<TaClientDTO> l = new ArrayList<TaClientDTO>();

		for (TaClient taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaClientDTO> selectAllDTO() {
		System.out.println("List of TaClientDTO EJB :");
		ArrayList<TaClientDTO> liste = new ArrayList<TaClientDTO>();

		List<TaClient> projects = selectAll();
		for(TaClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaClientDTO dto, String validationContext) throws EJBException {
		try {
			TaClientMapper mapper = new TaClientMapper();
			TaClient entity = null;
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
	
	public void persistDTO(TaClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaClientDTO dto, String validationContext) throws CreateException {
		try {
			TaClientMapper mapper = new TaClientMapper();
			TaClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaClientDTO dto) throws RemoveException {
		try {
			TaClientMapper mapper = new TaClientMapper();
			TaClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaClient refresh(TaClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaClientDTO dto, String validationContext) {
		try {
			TaClientMapper mapper = new TaClientMapper();
			TaClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaClientDTO> validator = new BeanValidator<TaClientDTO>(TaClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaClientDTO dto, String propertyName, String validationContext) {
		try {
			TaClientMapper mapper = new TaClientMapper();
			TaClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaClientDTO> validator = new BeanValidator<TaClientDTO>(TaClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

	@Override
	public List<TaClient> listeDemandePartenariat() {
		return dao.listeDemandePartenariat();
	}

	@Override
	public List<TaClient> listePartenaire() {
		return dao.listePartenaire();
	}

	@Override
	public List<TaClient> listePartenaireType(int idTypePartenaire) {
		return dao.listePartenaireType(idTypePartenaire);
	}
	
	public String genereCodePartenaire(TaClient c) {
		String code = "";
		if(c.getTaPartenaire()!=null && c.getTaPartenaire().getTaTypePartenaire()!=null) {
			//première lettre en fonction du type de partenaire
			if(c.getTaPartenaire().getTaTypePartenaire().getCode().equals(TaTypePartenaire.TYPE_PART_REVENDEUR_CODE)) {
				code += "R";
			}else if(c.getTaPartenaire().getTaTypePartenaire().getCode().equals(TaTypePartenaire.TYPE_PART_PARRAIN_CODE)) {
				code += "P";
			}else if(c.getTaPartenaire().getTaTypePartenaire().getCode().equals(TaTypePartenaire.TYPE_PART_GROUPEMENT_CODE)) {
				code += "G";
			}else if(c.getTaPartenaire().getTaTypePartenaire().getCode().equals(TaTypePartenaire.TYPE_PART_SALARIE_CODE)) {
				code += "S";
			}
			
			//les 3 première lettre du nom
			if(c.getNom().length()<3) {
				//si la longueur du nom est inférieure à 3 caractères, on complète avec des "0" avant de poursuivre.
				code = c.getNom();
				for(int i=0; code.length()<3; i++) {
					code += "0";
				}
			} else {
				//sinon on  prends les 4 premier caractères
				code += c.getNom().substring(0, 3);
			}
			//code += c.getNom().substring(0, 3).toUpperCase();
		
			//compteur numérique sur 4 caractère
			int longeurCompteur = 4;
			String max = dao.maxCodePartenaire(code);
			int compteur = 0;
			
			if(max!=null) {
				max = max.replace("0", "");
				compteur = LibConversion.stringToInteger(max);
			}

			compteur++;
			String compteurString = ""+compteur;
			for(int i=0; compteurString.length()<longeurCompteur; i++) {
				compteurString = "0"+compteurString;
			}
			code += compteurString;
		}
		return code;
	}
	
	//public String genereCodeClient(TaClient c) {
	public String genereCodeClient(String nomClient) {
		String code = "";
	
		if(nomClient!=null) {
			nomClient = nomClient.toUpperCase();
			
			//les 4 première lettre du nom
			//code += c.getNom().substring(0, 3).toUpperCase();
			if(nomClient.length()<4) {
				//si la longueur du nom est inférieure à 4 caractères, on complète avec des "0" avant de poursuivre.
				code = nomClient;
				for(int i=0; code.length()<4; i++) {
					code += "0";
				}
			} else {
				//sinon on  prends les 4 premier caractères
				code += nomClient.substring(0, 4);
			}
		
			//compteur numérique sur 3 caractère
			int longeurCompteur = 3;
			String max = dao.maxCodeClient(code);
			int compteur = 0;
			
			if(max!=null) {
				max = max.replace("0", "");
				compteur = LibConversion.stringToInteger(max);
			}
	
			compteur++;
			String compteurString = ""+compteur;
			for(int i=0; compteurString.length()<longeurCompteur; i++) {
				compteurString = "0"+compteurString;
			}
			code += compteurString;
		}
		return code;
	}
	
	public TaClient findByCodePartenaire(String codePartenaire) {
		return dao.findByCodePartenaire(codePartenaire);
	}

}
