package fr.legrain.edition.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.lib.osgi.ConstActionInterne;
import fr.legrain.bdg.model.mapping.mapper.TaEditionMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.edition.dao.IEditionDAO;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEditionService extends AbstractApplicationDAOServer<TaEdition, TaEditionDTO> implements ITaEditionServiceRemote {

	static Logger logger = Logger.getLogger(TaEditionService.class);

	@Inject private IEditionDAO dao;
	
	private @EJB ITaActionEditionServiceRemote taActionEditionService;

	/**
	 * Default constructor. 
	 */
	public TaEditionService() {
		super(TaEdition.class,TaEditionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaEdition a";
	
	public List<TaEditionDTO> findAllLight() {
		return dao.findAllLight();
	}

	public TaEditionDTO findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaEditionDTO> findByIdTypeDTO(int idTEdition) {
		return dao.findByIdTypeDTO(idTEdition);
	}
	public List<TaEditionDTO> findByCodeTypeDTO(String codeTEdition) {
		return dao.findByCodeTypeDTO(codeTEdition);
	}
	
	public List<TaEdition> findByIdType(int idTEdition){
		return dao.findByIdType(idTEdition);
	}
	public List<TaEdition> findByCodeType(String codeTEdition){
		return dao.findByCodeType(codeTEdition);
	}
	/**
	 * @author yann
	 * @param edition (optionnel)
	 * @param action un objet actionEdition (impression, espace client, email...)
	 * @param typeDoc par exemple FACTURE
	 * Cette méthode va chercher l'edition choisie par défaut si il y en a une, sinon elle renvoi celle par defaut programme, ou celle passée en param si renseigné
	 * @return une edition
	 */
	public TaEdition rechercheEditionActionDefaut(TaEdition edition, TaActionEdition action, String typeDoc) {
		if(edition == null && action != null && typeDoc != null) {
			//liste des editions 
			List<TaEditionDTO> listeEdition = findByCodeTypeDTOAvecActionsEdition(typeDoc.toUpperCase());
			TaEditionMapper editionMapper = new TaEditionMapper();
			
			//rechercher l'edition par defaut pour l'action choisie si il y en a une 
			switch (action.getCodeAction()) {
			case TaActionEdition.code_impression:
				for (TaEditionDTO ed : listeEdition) {
					for (TaActionEditionDTO actionDefaut : ed.getTaActionEditionDTO()) {
						if(actionDefaut.getCodeAction().equals(TaActionEdition.code_impression)) {
							//edition = editionMapper.mapDtoToEntity(ed, edition);
							try {
								edition = findById(ed.getId());
							} catch (FinderException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				break;
			case TaActionEdition.code_mail:
				for (TaEditionDTO ed : listeEdition) {
					for (TaActionEditionDTO actionDefaut : ed.getTaActionEditionDTO()) {
						if(actionDefaut.getCodeAction().equals(TaActionEdition.code_mail)) {
							//edition = editionMapper.mapDtoToEntity(ed, edition);
							try {
								edition = findById(ed.getId());
							} catch (FinderException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
							
				break;
			case TaActionEdition.code_espacecli:
				for (TaEditionDTO ed : listeEdition) {
					for (TaActionEditionDTO actionDefaut : ed.getTaActionEditionDTO()) {
						if(actionDefaut.getCodeAction().equals(TaActionEdition.code_espacecli)) {
							//edition = editionMapper.mapDtoToEntity(ed, edition);
							try {
								edition = findById(ed.getId());
							} catch (FinderException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				break;

			default:
				break;
			}
			
			if(edition == null) {
				switch (typeDoc) {
				case TaDevis.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_DEVIS_DEFAUT).get(0);
					break;
				case TaFacture.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_FACTURE_DEFAUT).get(0);
					break;
				case TaAvoir.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_AVOIR_DEFAUT).get(0);
					break;
				case TaProforma.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_PROFORMA_DEFAUT).get(0);
					break;
				case TaAvisEcheance.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_AVIS_ECHEANCE_DEFAUT).get(0);
					break;
				case TaBonliv.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_BONLIV_DEFAUT).get(0);
					break;
				case TaBoncde.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_BONCDE_DEFAUT).get(0);
					break;
				case TaPrelevement.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_PRELEVEMENT_DEFAUT).get(0);
					break;
				case TaApporteur.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_APPORTEUR_DEFAUT).get(0);
					break;
				case TaAcompte.TYPE_DOC:
					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_ACOMPTE_DEFAUT).get(0);
					break;
//				case TaBoncdeAchat.TYPE_DOC:
//					edition = rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_BONCDE_DEFAUT).get(0);
//					break;

				default:
					break;
				}
				
			}
		}
		
		return edition;
	}
	
	public List<TaEdition> rechercheEditionDisponibleProgrammeDefaut(String codeTypeEdition, String idAction) {
		List<TaEdition> l = new ArrayList<>();
		TaEdition e = null;
		InputStream is = null;
		byte[] b = null;
		
		try {
			switch (idAction) {			
				case ConstActionInterne.EDITION_DEVIS_DEFAUT:
				e = new TaEdition();
				is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_DEVIS_FICHIER);
				b = IOUtils.toByteArray(is);
				e.setFichierBlob(b);
				
				e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_DEVIS_CODE);
				e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_DEVIS_LIBELLE);
				e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_DEVIS_DESCRIPTION);
				e.setLibrairie(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_DEVIS);
				
				l.add(e);
				break;
				
				case ConstActionInterne.EDITION_FACTURE_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_FACTURE_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_FACTURE_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_FACTURE_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_FACTURE_DESCRIPTION);
					e.setLibrairie(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_FACTURE);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_PROFORMA_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_PROFORMA_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_PROFORMA_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_PROFORMA_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_PROFORMA_DESCRIPTION);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_BONLIV_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_BONLIV_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_BONLIV_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_BONLIV_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_BONLIV_DESCRIPTION);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_AVOIR_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_AVOIR_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_AVOIR_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_AVOIR_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_AVOIR_DESCRIPTION);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_AVIS_ECHEANCE_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_AVIS_ECHEANCE_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_AVIS_ECHEANCE_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_AVIS_ECHEANCE_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_AVIS_ECHEANCE_DESCRIPTION);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_BONCDE_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_BONCDE_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_BONCDE_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_BONCDE_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_BONCDE_DESCRIPTION);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_ACOMPTE_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_ACOMPTE_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_ACOMPTE_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_ACOMPTE_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_ACOMPTE_DESCRIPTION);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_APPORTEUR_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_APPORTEUR_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_APPORTEUR_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_APPORTEUR_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_APPORTEUR_DESCRIPTION);
					
					l.add(e);
					break;
					
				case ConstActionInterne.EDITION_PRELEVEMENT_DEFAUT:
					e = new TaEdition();
					is = EditionProgrammeDefaut.class.getClassLoader().getResourceAsStream(EditionProgrammeDefaut.EDITION_DEFAUT_PRELEVEMENT_FICHIER);
					b = IOUtils.toByteArray(is);
					e.setFichierBlob(b);
					
					e.setCodeEdition(EditionProgrammeDefaut.EDITION_DEFAUT_PRELEVEMENT_CODE);
					e.setLibelleEdition(EditionProgrammeDefaut.EDITION_DEFAUT_PRELEVEMENT_LIBELLE);
					e.setDescriptionEdition(EditionProgrammeDefaut.EDITION_DEFAUT_PRELEVEMENT_DESCRIPTION);
					
					l.add(e);
					break;
					
				default:
					break;
					
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return l;
	}
	
	public List<TaEdition> rechercheEditionDisponible(String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees) {
		
		List<TaEdition> l = new ArrayList<>();
		l.addAll(dao.rechercheEditionDisponible(codeTypeEdition, idAction,listeCodeEditionDejaImportees));
		l.addAll(rechercheEditionDisponibleProgrammeDefaut(codeTypeEdition, idAction));
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaEditionDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaEditionDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaEdition transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEdition transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEdition persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdEdition()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaEdition merge(TaEdition detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEdition merge(TaEdition detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEdition findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEdition findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEdition> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<TaEditionDTO> findByCodeTypeDTOAvecActionsEdition(String codeTEdition){
		List<TaEditionDTO> liste = findByCodeTypeDTO(codeTEdition);
		List<TaEditionDTO> listeFinale = new ArrayList<TaEditionDTO>();
		for (TaEditionDTO taEditionDTO : liste) {
			taEditionDTO = rempliTaActionServiceDTO(taEditionDTO);
			listeFinale.add(taEditionDTO);
		}
		return listeFinale;
	}
	public TaEditionDTO rempliTaActionServiceDTO(TaEditionDTO ed){
		List<TaActionEditionDTO> actions = new ArrayList<TaActionEditionDTO>();
		actions = taActionEditionService.findAllByIdEdtionDTO(ed.getId());
		for (TaActionEditionDTO action : actions) {
			ed.getTaActionEditionDTO().add(action);
		}
		return ed;
	}
	
	public List<TaEditionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaEditionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEdition> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaEditionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaEditionDTO entityToDTO(TaEdition entity) {
//		TaEditionDTO dto = new TaEditionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEditionMapper mapper = new TaEditionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaEditionDTO> listEntityToListDTO(List<TaEdition> entity) {
		List<TaEditionDTO> l = new ArrayList<TaEditionDTO>();

		for (TaEdition taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaEditionDTO> selectAllDTO() {
		System.out.println("List of TaEditionDTO EJB :");
		ArrayList<TaEditionDTO> liste = new ArrayList<TaEditionDTO>();

		List<TaEdition> projects = selectAll();
		for(TaEdition project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaEditionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaEditionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaEditionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaEditionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaEditionDTO dto, String validationContext) throws EJBException {
		try {
			TaEditionMapper mapper = new TaEditionMapper();
			TaEdition entity = null;
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
	
	public void persistDTO(TaEditionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaEditionDTO dto, String validationContext) throws CreateException {
		try {
			TaEditionMapper mapper = new TaEditionMapper();
			TaEdition entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaEditionDTO dto) throws RemoveException {
		try {
			TaEditionMapper mapper = new TaEditionMapper();
			TaEdition entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEdition refresh(TaEdition persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEdition value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEdition value, String propertyName, String validationContext) {
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
	public void validateDTO(TaEditionDTO dto, String validationContext) {
		try {
			TaEditionMapper mapper = new TaEditionMapper();
			TaEdition entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEditionDTO> validator = new BeanValidator<TaEditionDTO>(TaEditionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaEditionDTO dto, String propertyName, String validationContext) {
		try {
			TaEditionMapper mapper = new TaEditionMapper();
			TaEdition entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEditionDTO> validator = new BeanValidator<TaEditionDTO>(TaEditionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaEditionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaEditionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEdition value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEdition value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}
