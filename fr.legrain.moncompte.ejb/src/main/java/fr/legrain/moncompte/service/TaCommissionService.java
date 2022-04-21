package fr.legrain.moncompte.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import fr.legrain.bdg.moncompte.service.remote.ITaCommissionServiceRemote;
import fr.legrain.moncompte.dao.IClientDAO;
import fr.legrain.moncompte.dao.ICommissionDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaCommissionDTO;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.moncompte.model.mapping.mapper.TaCommissionMapper;


/**
 * Session Bean implementation class TaCommissionBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaCommissionService extends AbstractApplicationDAOServer<TaCommission, TaCommissionDTO> implements ITaCommissionServiceRemote {

	static Logger logger = Logger.getLogger(TaCommissionService.class);

	@Inject private ICommissionDAO dao;
	@Inject private IClientDAO daoClient;
	

	/**
	 * Default constructor. 
	 */
	public TaCommissionService() {
		super(TaCommission.class,TaCommissionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCommission a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCommission transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCommission transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCommission persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCommission merge(TaCommission detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCommission merge(TaCommission detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCommission findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCommission findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCommission> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCommissionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCommissionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCommission> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCommissionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCommissionDTO entityToDTO(TaCommission entity) {
//		TaCommissionDTO dto = new TaCommissionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCommissionMapper mapper = new TaCommissionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCommissionDTO> listEntityToListDTO(List<TaCommission> entity) {
		List<TaCommissionDTO> l = new ArrayList<TaCommissionDTO>();

		for (TaCommission taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCommissionDTO> selectAllDTO() {
		System.out.println("List of TaCommissionDTO EJB :");
		ArrayList<TaCommissionDTO> liste = new ArrayList<TaCommissionDTO>();

		List<TaCommission> projects = selectAll();
		for(TaCommission project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCommissionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCommissionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCommissionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCommissionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCommissionDTO dto, String validationContext) throws EJBException {
		try {
			TaCommissionMapper mapper = new TaCommissionMapper();
			TaCommission entity = null;
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
	
	public void persistDTO(TaCommissionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCommissionDTO dto, String validationContext) throws CreateException {
		try {
			TaCommissionMapper mapper = new TaCommissionMapper();
			TaCommission entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCommissionDTO dto) throws RemoveException {
		try {
			TaCommissionMapper mapper = new TaCommissionMapper();
			TaCommission entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCommission refresh(TaCommission persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCommission value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCommission value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCommissionDTO dto, String validationContext) {
		try {
			TaCommissionMapper mapper = new TaCommissionMapper();
			TaCommission entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCommissionDTO> validator = new BeanValidator<TaCommissionDTO>(TaCommissionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCommissionDTO dto, String propertyName, String validationContext) {
		try {
			TaCommissionMapper mapper = new TaCommissionMapper();
			TaCommission entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCommissionDTO> validator = new BeanValidator<TaCommissionDTO>(TaCommissionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCommissionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCommissionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCommission value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCommission value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}
	
	public List<TaCommission> findCommissionPartenaire(String codePartenaire) {
		return dao.findCommissionPartenaire(codePartenaire);
	}
	
	public List<TaCommission> findCommissionPartenaire(String codePartenaire, Date debut, Date fin) {
		return dao.findCommissionPartenaire(codePartenaire,debut,fin);
	}

	public BigDecimal findMontantVentePartenaire(String codePartenaire, Date debut, Date fin) {
		return dao.findMontantVentePartenaire(codePartenaire, debut, fin);
	}
	
	public Date findDerniereCreationDossierPayantPartenaire(String codePartenaire) {
		return dao.findDerniereCreationDossierPayantPartenaire(codePartenaire);
	}
	
	public BigDecimal findTauxCommissionPartenaire(String codePartenaire) {
		return findTauxCommissionPartenaire(codePartenaire,true);
	}
	
	public BigDecimal findTauxCommissionPartenaire(String codePartenaire, boolean appliqueDecote) {
		BigDecimal taux = new BigDecimal(0);
		
		TaClient partenaire = daoClient.findByCodePartenaire(codePartenaire);
		
		if(partenaire.getTaPartenaire().getTaTypePartenaire().getCode().equals(TaTypePartenaire.TYPE_PART_REVENDEUR_CODE)) {
			Date maintenant = new Date();
			LocalDate maintenant2 = LocalDate.now();//LocalDate.of(2012, Month.DECEMBER, 12)
			Date debut = Date.from(LocalDate.of(maintenant2.getYear(), maintenant2.getMonthValue(), 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date fin = Date.from(LocalDate.of(maintenant2.getYear(), maintenant2.getMonthValue(), maintenant2.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
			boolean premireCommissionDuMois = true;
			List<TaCommission> lc = findCommissionPartenaire(codePartenaire, debut, fin);
			
			if(lc==null || (lc!=null && lc.isEmpty()) ) { //premier calcul de commission du mois, on calcule un nouveau taux par rapport aux dernière ventes
				/*
				REVENDEUR
				Le calcul des commissions est le suivant :
				Pour tous les dossiers qui possèdent le CODE ALLIANCE du partenaire concerné, 
				on calcule montant total des achats pour les douze derniers mois précédant le mois à calculer pour obtenir le niveau de rémunération, 
				c’est à dire le pourcentage de commission que l’on va appliquer pour le mois calculé.
				Les paliers sont les suivants :
				0 € - 49 999 € : 15 %
				50 000 € - 99 999 € : 20 %
				100 000 € - 149 999 € : 25 %
				>= 150 000 : 30 %
	
				Ensuite, on récupère la date du dernier dossier créé à avoir payé un module. 
				Si la durée de la période entre la date du calcul et la date dernier dossier à avoir payé est 
				supérieure ou égale à 12 mois, on enlève 5 % du niveau rémunération par palier de 12 mois.
				*/
				
				LocalDate deb = LocalDate.of(maintenant2.getYear(), maintenant2.getMonthValue(), maintenant2.lengthOfMonth()).minusMonths(1);
				Date debutc = Date.from(LocalDate.of(maintenant2.getYear()-1, maintenant2.getMonthValue(),1).atStartOfDay(ZoneId.systemDefault()).toInstant());
				Date finc = Date.from(LocalDate.of(maintenant2.getYear(), maintenant2.getMonthValue(), maintenant2.lengthOfMonth()).minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
				BigDecimal montantVente =  findMontantVentePartenaire(codePartenaire, debutc, finc);
				montantVente = montantVente.setScale(0, RoundingMode.UP);
				//TODO faire un arrondi
				if(montantVente.compareTo(new BigDecimal(0))>0 && montantVente.compareTo(new BigDecimal(49999))<=0) {
					taux = new BigDecimal(15);
				} else if(montantVente.compareTo(new BigDecimal(50000))>0 && montantVente.compareTo(new BigDecimal(99999))<=0) {
					taux = new BigDecimal(20);
				} else if(montantVente.compareTo(new BigDecimal(100000))>0 && montantVente.compareTo(new BigDecimal(149999))<=0) {
					taux = new BigDecimal(25);
				} else if(montantVente.compareTo(new BigDecimal(150000))>=0) {
					taux = new BigDecimal(30);
				}
				
				//Calcul décote
				if(appliqueDecote) {
					Date dernierDossier = findDerniereCreationDossierPayantPartenaire(codePartenaire);
					Date dateDernierDossier = dernierDossier;
	//				Date dateDernierDossier = dernierDossier.getQuandCree();
					
					Calendar startCalendar = new GregorianCalendar();
					startCalendar.setTime(maintenant);
					Calendar endCalendar = new GregorianCalendar();
					endCalendar.setTime(dateDernierDossier);
		
					int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
					int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
					
					if(diffMonth>=12) {
						do {
							diffMonth = diffMonth - 12;
							taux = taux.subtract(new BigDecimal(5));
						} while (diffMonth>0 && taux.compareTo(new BigDecimal(0))>0);
					}
				}
			} else {
				// on a déjà calculé le taux de comission pour ce moi ci, donc on le réutilise pour toutes les commissions de ce même mois
				taux = lc.get(0).getPourcentageCommission();
			}

		} else if(partenaire.getTaPartenaire().getTaTypePartenaire().getCode().equals(TaTypePartenaire.TYPE_PART_PARRAIN_CODE)) {
			/*
			PARRAINAGE
			On établit que Legrain lui reverse 10 % du montant total HT sous forme d’avoir qu’ont généré ses filleuls sur les douze derniers mois.
			Il faut prévoir une gestion qui permette de déduire le montant des avoirs en cours de validité sur une commande en ligne du BdG.
			*/
			taux = new BigDecimal(10);
		} else {
			
		}
		return taux;
	}
}
