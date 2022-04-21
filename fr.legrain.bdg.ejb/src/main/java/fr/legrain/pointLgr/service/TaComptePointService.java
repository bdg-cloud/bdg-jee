package fr.legrain.pointLgr.service;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.abonnement.model.TaAbonnementOld;
import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.model.mapping.mapper.TaComptePointMapper;
import fr.legrain.bdg.pointLgr.service.remote.ITaComptePointServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.pointLgr.dao.jpa.TaArticlePointDAO;
import fr.legrain.pointLgr.dao.jpa.TaIndicePointDAO;
import fr.legrain.pointLgr.dao.jpa.TaTypeMouvPointDAO;
import fr.legrain.pointLgr.dto.ComptePointDTO;
import fr.legrain.pointLgr.model.TaArticlePoint;
import fr.legrain.pointLgr.model.TaComptePoint;
import fr.legrain.pointLgr.model.TaIndicePoint;
import fr.legrain.pointLgr.model.TaTypeMouvPoint;
import fr.legrain.point_Lgr.dao.IArticlePointDAO;
import fr.legrain.point_Lgr.dao.IComptePointDAO;
import fr.legrain.point_Lgr.dao.ITypeMouvPointDAO;
import fr.legrain.supportAbonnement.dao.jpa.TaSupportAbonDAO;
import fr.legrain.supportAbonnement.model.TaSupportAbon;
import fr.legrain.tiers.dao.IFamilleTiersDAO;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.model.TaTiers;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaComptePointService extends AbstractApplicationDAOServer<TaComptePoint, ComptePointDTO> implements ITaComptePointServiceRemote {




	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaComptePointService.class);

	@Inject private IComptePointDAO dao;
	@Inject ITiersDAO daoTiers;
	@Inject ITypeMouvPointDAO daoTypePoint;
	@Inject IArticlePointDAO daoArticlePoint;
	@Inject IFamilleTiersDAO daoFamilleTiers;
	@Inject IArticleDAO daoArticle;
	
	public TaComptePointService(Class<TaComptePoint> entityClass,
			Class<ComptePointDTO> dtoClass) {
		super(entityClass, dtoClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void error(ComptePointDTO arg0) {
		throw new EJBException("Test erreur EJB");
	}



	@Override
	public ComptePointDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}



	@Override
	public ComptePointDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}



	@Override
	public List<ComptePointDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		List<TaComptePoint> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<ComptePointDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}



	@Override
	public List<ComptePointDTO> findWithNamedQueryDTO(String arg0)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void mergeDTO(ComptePointDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}



	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(ComptePointDTO dto, String validationContext) throws EJBException {
		try {
			TaComptePointMapper mapper = new TaComptePointMapper();
			TaComptePoint entity = null;
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



	@Override
	public void persistDTO(ComptePointDTO dto) throws CreateException {
		persistDTO(dto, null);
	}



	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(ComptePointDTO dto, String validationContext)
			throws CreateException {
		try {
			TaComptePointMapper mapper = new TaComptePointMapper();
			TaComptePoint entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}



	@Override
	public void removeDTO(ComptePointDTO dto) throws RemoveException {
		try {
			TaComptePointMapper mapper = new TaComptePointMapper();
			TaComptePoint entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}



//	@RolesAllowed("admin")
	public List<ComptePointDTO> selectAllDTO() {
		System.out.println("List of ComptePointDTO EJB :");
		ArrayList<ComptePointDTO> liste = new ArrayList<ComptePointDTO>();

		List<TaComptePoint> projects = selectAll();
		for(TaComptePoint project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}



	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(ComptePointDTO dto) {
		validateDTO(dto,null);
	}



	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(ComptePointDTO dto, String validationContext) {
		try {
			TaComptePointMapper mapper = new TaComptePointMapper();
			TaComptePoint entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAcompteDTO> validator = new BeanValidator<TaAcompteDTO>(TaAcompteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}


	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(ComptePointDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
	}



	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(ComptePointDTO dto, String propertyName,
			String validationContext) {
		try {
			TaComptePointMapper mapper = new TaComptePointMapper();
			TaComptePoint entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAcompteDTO> validator = new BeanValidator<TaAcompteDTO>(TaAcompteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}



	@Override
	public TaComptePoint findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}



	@Override
	public TaComptePoint findById(int id) throws FinderException {
		return dao.findById(id);
	}



	@Override
	public TaComptePoint merge(TaComptePoint detachedInstance) {
		return merge(detachedInstance, null);
	}



	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaComptePoint merge(TaComptePoint detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}



	@Override
	public void persist(TaComptePoint transientInstance) throws CreateException {
		persist(transientInstance, null);
	}



	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaComptePoint transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}



	@Override
	public void remove(TaComptePoint persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdPoint()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}



//	@RolesAllowed("admin")
	public List<TaComptePoint> selectAll() {
		return dao.selectAll();
	}



	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaComptePoint value) {
		validateEntity(value,null);
	}



	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaComptePoint value, String validationContext) {
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
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaComptePoint value, String propertyName) {
		// TODO Auto-generated method stub
		validateEntityProperty(value,propertyName,null);
	}



	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaComptePoint value, String propertyName,
			String validationContext) {
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
	protected TaComptePoint refresh(TaComptePoint persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}


	public ComptePointDTO entityToDTO(TaComptePoint entity) {
		TaComptePointMapper mapper = new TaComptePointMapper();
		return mapper.mapEntityToDto(entity, null);
	}
	
	
	public List<ComptePointDTO> listEntityToListDTO(List<TaComptePoint> entity) {
		List<ComptePointDTO> l = new ArrayList<ComptePointDTO>();

		for (TaComptePoint taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}
	

	
	public BigDecimal calculPointAchatSupport(TaSupportAbon taSupportAbon) {
		try {
			TaArticlePointDAO daoArticlePoint = new TaArticlePointDAO();
			TaArticlePoint articlePoint=null;
			BigDecimal remisePoint=null;
			BigDecimal pointAnnee=null;
			articlePoint=daoArticlePoint.findByArticle(taSupportAbon.getTaArticle().getIdArticle());
			if(articlePoint!=null && articlePoint.getTaTypeMouvPoint()!=null && articlePoint.getTaTypeMouvPoint().getTypeMouv().toUpperCase().equals("EP")){
				if(taSupportAbon.getTaLDocument()!=null){
					remisePoint=calculRemisePointFacture(taSupportAbon.getTaLDocument().getTaDocument())	;
					pointAnnee=taSupportAbon.getTaLDocument().getMtHtLApresRemiseGlobaleDocument().multiply(articlePoint.getPoints(),MathContext.DECIMAL128).
							divide(articlePoint.getPrixReference(),MathContext.DECIMAL128).setScale(2, BigDecimal.ROUND_HALF_UP);
					if(remisePoint.compareTo(BigDecimal.ZERO)!=0){
						pointAnnee=pointAnnee.subtract(pointAnnee.multiply(remisePoint,MathContext.DECIMAL128).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
			logger.debug("calculPointAnnee successful");
			return pointAnnee;
		} catch (RuntimeException re) {
			logger.error("calculPointAnnee failed", re);
			throw re;
		}
	}
	
	public BigDecimal calculPointAnnee(TaAbonnementOld taAbonnement) {
		try {

			TaArticlePoint articlePoint=null;
			BigDecimal remisePoint=null;
			BigDecimal pointAnnee=null;
			articlePoint=daoArticlePoint.findByArticle(taAbonnement.getTaArticle().getIdArticle());
			if(articlePoint!=null && articlePoint.getTaTypeMouvPoint()!=null && articlePoint.getTaTypeMouvPoint().getTypeMouv().toUpperCase().equals("EP")){
				if(taAbonnement.getTaLDocument()!=null){
					remisePoint=calculRemisePointFacture(taAbonnement.getTaLDocument().getTaDocument())	;
					if(articlePoint.getPrixReference()!=null && articlePoint.getPrixReference().signum()!=0)
					pointAnnee=taAbonnement.getTaLDocument().getMtHtLApresRemiseGlobaleDocument().multiply(articlePoint.getPoints(),MathContext.DECIMAL128).
							divide(articlePoint.getPrixReference(),MathContext.DECIMAL128).setScale(2, BigDecimal.ROUND_HALF_UP);
					if(remisePoint.compareTo(BigDecimal.ZERO)!=0){
						pointAnnee=pointAnnee.subtract(pointAnnee.multiply(remisePoint,MathContext.DECIMAL128).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
//			if(pointAnnee!=null && !pointAnnee.equals(BigDecimal.ZERO))pointAnnee=(pointAnnee.divide(BigDecimal.valueOf(1).add((taAbonnement.getTaLDocument().getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
//					)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));
			logger.debug("calculPointAnnee successful");
			return pointAnnee;
		} catch (RuntimeException re) {
			logger.error("calculPointAnnee failed", re);
			throw re;
		}
	}

	

	public Boolean recupIndice(TaAbonnementOld taAbonnement) {
		try {
			TaArticlePointDAO daoArticlePoint = new TaArticlePointDAO();
			TaArticlePoint articlePoint=null;
			articlePoint=daoArticlePoint.findByArticle(taAbonnement.getTaArticle().getIdArticle());
			if(articlePoint!=null ){
				return LibConversion.intToBoolean(articlePoint.getIndice());				
			}
			logger.debug("recupIndice successful");
			return null;
		} catch (RuntimeException re) {
			logger.error("recupIndice failed", re);
			throw re;
		}
	}
	
	

	public BigDecimal calculPointIndice(TaAvisEcheance taAvisEcheance) {
		try {
			//si Qté <12, on ne traite pas
			// si codeProduit identique, on ne traite pas
			
			TaIndicePointDAO daoIndicePoint = new TaIndicePointDAO();
			TaIndicePoint indicePoint=null;
			BigDecimal point=null;
			BigDecimal tranche=BigDecimal.ZERO;
			LinkedList<TaLAvisEcheance> tabLigneCumule=new LinkedList<TaLAvisEcheance>();
			Boolean indice=true;
			Integer indiceTotal=0;
			List<TaArticle> listeProduit=new LinkedList<TaArticle>();
			listeProduit.clear();
			for (TaLAvisEcheance ligne : taAvisEcheance.getLignes()) {
				indice=null;
				if(ligne.getTaLEcheance()!=null && ligne.getTaLEcheance().getTaArticle()!=null)
					//mise en comm car obsolète (abonnementOld) et risque de faire planté 
					//&& !partenaire100Pour100(ligne.getTaLEcheance().getTaAbonnement())
					
					if(!listeProduit.contains(ligne.getTaLEcheance().getTaArticle())&& 
							ligne.getTaLEcheance().getQteLDocument().compareTo(BigDecimal.valueOf(12))>=0)
						//mise en comm car obsolète (abonnementOld) et risque de faire planté 
						//indice=recupIndice(ligne.getTaLEcheance().getTaAbonnement());
					if(indice!=null && indice==true){
						listeProduit.add(ligne.getTaLEcheance().getTaArticle());
						tabLigneCumule.add(ligne);
						tranche=tranche.add(ligne.getTaLEcheance().getMtHtLApresRemiseGlobaleDocument());
					}
			}
			//avec le totalindice, récupérer le pourcentage correspondant
			if(tranche!=null && tranche.signum()>0 && listeProduit.size()>1)indicePoint=daoIndicePoint.RechercheMaxParTranche(tranche,listeProduit.size());
			if(indicePoint!=null && indicePoint.getPoints()!=null){
				return indicePoint.getPoints();
//				for (TaLAvisEcheance taLAvisEcheance : tabLigneCumule) {
//					if(point==null)point=new BigDecimal(0);
//					point=point.add(taLAvisEcheance.getMtHtLApresRemiseGlobaleDocument().
//							multiply(indicePoint.getPoints(), MathContext.DECIMAL128).
//							divide(BigDecimal.valueOf(100),MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP));
//				}
//				if(point!=null)point=point.multiply(BigDecimal.valueOf(10), MathContext.DECIMAL128);
//				if(point!=null && !point.equals(BigDecimal.ZERO))point=(point.divide(BigDecimal.valueOf(1).add((ligne.getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
//						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));				
			}			
			logger.debug("calculPointIndice successful");
			return null;
		} catch (RuntimeException re) {
			logger.error("calculPointIndice failed", re);
			throw re;
		}
	}
	
	
	public BigDecimal calculPointTotalAnneeSupport(TaSupportAbon taSupportAbon ) {
		try {
			BigDecimal pointAnnee=null;
			Date datePeremption=null;
			
			if(taSupportAbon!=null && taSupportAbon.getTaLDocument()!=null){
				datePeremption=taSupportAbon.getTaLDocument().getTaDocument().getDateDocument();
				datePeremption=LibDate.incrementDate(datePeremption, 0, 18, 0);
				pointAnnee=calculPointAchatSupport(taSupportAbon);

				TaComptePoint comptePoint=null;
				
				if(pointAnnee!=null && pointAnnee.signum()!=0){
					comptePoint = new TaComptePoint(0);
				comptePoint.setCodeDocument(taSupportAbon.getTaLDocument().getTaDocument().getCodeDocument());
				comptePoint.setDateAcquisition(taSupportAbon.getTaLDocument().getTaDocument().getDateDocument());
				comptePoint.setDatePeremption(datePeremption);
				comptePoint.setPoint(pointAnnee);
				comptePoint.setTaTiers(taSupportAbon.getTaTiers());
				comptePoint.setTaTypeMouvPoint(new TaTypeMouvPointDAO().findByCode("EP"));
				merge(comptePoint);
				}		
			}
			logger.debug("calculPointTotalAnneeSupport successful");
			return pointAnnee;
		} catch (RuntimeException re) {
			logger.error("calculPointTotalAnneeSupport failed", re);
			throw re;
		}
	}


	public boolean partenaire100Pour100(TaAbonnementOld taAbonnement){
//passage EJB
//		try {		
//
//		//je récupère le % associé au groupe s'il y en a un
////		TaFamilleTiersDAO daoFamille=new TaFamilleTiersDAO();
//		TaPourcGroupe Pourcgroupe=null;
//		TaPourcGroupeDAO daoPourcGroupe = new TaPourcGroupeDAO();
//
//		Query ejbQuery = getEntityManager().createQuery("select s from TaSupportAbon s  where s.idSupportAbon="+taAbonnement.getTaSupportAbon().getIdSupportAbon());
//		TaSupportAbon taSupportAbon= (TaSupportAbon)ejbQuery.getSingleResult();
//			Pourcgroupe=null;
//			BigDecimal valeur=null;			
//
///*passage ejb isabelle pour compilation*/
//			//			if(taSupportAbon instanceof TaLicenceBdg){
////				if(((TaLicenceBdg)taSupportAbon).getGroupe()!=null){
////					if(taAbonnement.getTaTAbonnement()!=null)
////						Pourcgroupe=daoPourcGroupe.findByTAbonnementFamille(((TaLicenceBdg)taSupportAbon).getGroupe().getIdFamille(),taAbonnement.getTaTAbonnement().getIdTAbonnement());
////				}
////			}
////			if(taSupportAbon instanceof TaLicenceEpicea){
////				if(((TaLicenceEpicea)taSupportAbon).getGroupe()!=null){
////					if(taAbonnement.getTaTAbonnement()!=null && ((TaLicenceEpicea)taSupportAbon).getGroupe()!=null)
////						Pourcgroupe=daoPourcGroupe.findByTAbonnementFamille(((TaLicenceEpicea)taSupportAbon).getGroupe().getIdFamille(),taAbonnement.getTaTAbonnement().getIdTAbonnement());
////				}
////			}
////			if(taSupportAbon instanceof TaLicenceSara){
////				if(((TaLicenceSara)taSupportAbon).getGroupe()!=null){
////					if(taAbonnement.getTaTAbonnement()!=null && ((TaLicenceSara)taSupportAbon).getGroupe()!=null)
////						Pourcgroupe=daoPourcGroupe.findByTAbonnementFamille(((TaLicenceSara)taSupportAbon).getGroupe().getIdFamille(),taAbonnement.getTaTAbonnement().getIdTAbonnement());
////				}
////			}
//
//			return (Pourcgroupe!=null && Pourcgroupe.getPourcentage().compareTo(BigDecimal.valueOf(100))==0);
//		} catch (Exception e) {
//			logger.error("partenaire100Pour100 failed", e);
//		}
		return false;
	}
	
	
	public BigDecimal calculPointCogere(TaAvisEcheance taAvisEcheance) {
		/*passage ejb isabelle pour compilation*/
//		try {
//			TaArticle ArticlePointBonus=daoArticle.remonteArticlePointBonus();
//			BigDecimal tauxTvaPoint=BigDecimal.ZERO;
//			if(ArticlePointBonus.getTaTva()!=null)
//				tauxTvaPoint=ArticlePointBonus.getTaTva().getTauxTva();
//			//point calculer en fonction du nombre d'année de maintenance payée sur Epicéa
//			//ces points ne sont applicable que sur la maintenance Epicéa
//			final String  libelleGroupeCogere="COGERE";
//			BigDecimal pointCogere=new BigDecimal(0);
////			JPABdLgr bdlgr=new JPABdLgr();
//			TaAbonnement abonnement = null;
//			LinkedHashMap<Integer, TaLAvisEcheance> tabIndice=new LinkedHashMap<Integer, TaLAvisEcheance>();
//			Integer nbMois=0;
//			Date dateMin=null;
//			Date dateMax=null;
//			BigDecimal moisConsommes=BigDecimal.ZERO;
//			BigDecimal qteRestant=BigDecimal.ZERO;
//			BigDecimal qtePlage=BigDecimal.ZERO;
//			pointCogere=BigDecimal.ZERO;
//
//			TaSupportAbonDAO daoSupport =new TaSupportAbonDAO();
//			TaFamilleTiers groupeCogere=null;
//			if(daoFamilleTiers.exist(libelleGroupeCogere)) groupeCogere=daoFamilleTiers.findByCode(libelleGroupeCogere);
//			Boolean continuer=true;
//			
//			for (TaLAvisEcheance ligne : taAvisEcheance.getLignes()) {
//				if(ligne.getTaLEcheance()!=null && ligne.getTaLEcheance().getTaAbonnement()!=null && 
//						ligne.getTaLEcheance().getTaAbonnement().getTaSupportAbon()!=null && 
//						ligne.getTaLEcheance().getTaAbonnement().getTaSupportAbon().getTaTSupport()!=null && 
//						ligne.getTaLEcheance().getTaAbonnement().getTaSupportAbon().getTaTSupport().getIdTSupport()==1){ //si produit Epicéa
//					TaSupportAbon support=daoSupport.findById(ligne.getTaLEcheance().getTaAbonnement().getTaSupportAbon().getIdSupportAbon());
//					if(support!=null && support instanceof TaLicenceEpicea){
//						continuer=((TaLicenceEpicea)support).getGroupe()!=null;
//						if(groupeCogere == null) {
//							MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "groupe "+libelleGroupeCogere+" " +
//									" inéxistant", "Le groupe "+libelleGroupeCogere+" n'a pas été trouvé.");
//							continuer=false;
//						}
//						if(continuer )continuer=(((TaLicenceEpicea)support).getGroupe().getIdFamille()==groupeCogere.getIdFamille());
//					}
//					if(continuer){	
//						String sqlDocumentsE2="select dateDiff (month, min(DATE_DEBUT),max(DATE_FIN))as moisConsommes " +
//								" from TA_ABONNEMENT a  where a.ID_SUPPORT_ABON="+ligne.getTaLEcheance().getTaAbonnement().getTaSupportAbon().getIdSupportAbon();
//						PreparedStatement prep= bdlgr.getCnx().prepareStatement(sqlDocumentsE2);
//						//					Query ejbQuery = entityManager.createQuery("select dateDiff (month, min(dateDebut),max(dateFin))as diff " +
//						//							" from TaAbonnement a  where s.idSupportAbon="+ligne.getTaLEcheance().getTaAbonnement().getTaSupportAbon().getIdSupportAbon());
//						//					List<Object> l = ejbQuery.getResultList();
//						ResultSet l=prep.executeQuery(sqlDocumentsE2);
//						while (l.next()) {
//							moisConsommes = BigDecimal.valueOf(l.getInt("moisConsommes"));							
//						}
//						if(moisConsommes!=null && moisConsommes.compareTo(BigDecimal.valueOf(60))<0){
//							BigDecimal qteAPendre=ligne.getTaLEcheance().getQteLDocument();
//							BigDecimal qteP1=BigDecimal.ZERO;
//							BigDecimal qteP2=BigDecimal.ZERO;
//							BigDecimal qteP3=BigDecimal.ZERO;
//							if(moisConsommes.compareTo(BigDecimal.valueOf(12))<0) {
//								qteAPendre=qteAPendre.subtract(BigDecimal.valueOf(12).subtract(moisConsommes));
//								moisConsommes=BigDecimal.valueOf(12);
//							}else if(moisConsommes.add(ligne.getTaLEcheance().getQteLDocument()).compareTo(BigDecimal.valueOf(60))>0){
//								qteAPendre=BigDecimal.valueOf(60).subtract(moisConsommes);
//							}
//							if(moisConsommes.compareTo(BigDecimal.valueOf(12))>=0 && moisConsommes.compareTo(BigDecimal.valueOf(36))<=0 && qteAPendre.signum()>0){
//								//premiere tranche
//								qteP1=BigDecimal.valueOf(36).subtract(moisConsommes);
//								if(qteP1.compareTo(qteAPendre)>0)qteP1=qteAPendre;
//								moisConsommes=moisConsommes.add(qteP1);
//								pointCogere=pointCogere.add(qteP1.multiply(BigDecimal.valueOf(5)).multiply(BigDecimal.valueOf(10)));
//								qteAPendre=qteAPendre.subtract(qteP1);
//							}
//							if(moisConsommes.compareTo(BigDecimal.valueOf(36))>=0 && moisConsommes.compareTo(BigDecimal.valueOf(48))<=0 && qteAPendre.signum()>0){
//								//deuxième tranche
//								qteP2=BigDecimal.valueOf(48).subtract(moisConsommes);
//								if(qteP2.compareTo(qteAPendre)>0)qteP2=qteAPendre;
//								moisConsommes=moisConsommes.add(qteP2);
//								pointCogere=pointCogere.add(qteP2.multiply(BigDecimal.valueOf(3.33)).multiply(BigDecimal.valueOf(10)));
//								qteAPendre=qteAPendre.subtract(qteP2);
//							}
//							if(moisConsommes.compareTo(BigDecimal.valueOf(48))>=0 && moisConsommes.compareTo(BigDecimal.valueOf(60))<=0 && qteAPendre.signum()>0){
//								//deuxième tranche
//								qteP3=BigDecimal.valueOf(60).subtract(moisConsommes);
//								if(qteP3.compareTo(qteAPendre)>0)qteP3=qteAPendre;
//								moisConsommes=moisConsommes.add(qteP3);
//								pointCogere=pointCogere.add(qteP3.multiply(BigDecimal.valueOf(1.66)).multiply(BigDecimal.valueOf(10)));
//							}
//						}
//					}
//				}
//				if(tauxTvaPoint.compareTo(BigDecimal.ZERO)!=0){
//					BigDecimal tva=pointCogere.multiply(tauxTvaPoint).divide(new BigDecimal(100),MathContext.DECIMAL128).
//							setScale(2,BigDecimal.ROUND_HALF_UP);
//					pointCogere=pointCogere.add(tva);
//				}
//				logger.debug("calculPointCogere successful");
//				return pointCogere;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("calculPointCogere failed", re);
//			throw re;
//		} catch (SQLException e) {
//			logger.error("calculPointCogere failed", e);			
//		}
		return null;
	}

	
	
	public BigDecimal calculPointHistorique(TaAbonnementOld taAbonnement) {
		try {
			TaArticlePointDAO daoArticlePoint = new TaArticlePointDAO();
			TaArticlePoint articlePoint=null;
			BigDecimal remisePoint=null;
			BigDecimal pointHistorique=new BigDecimal(0);
			articlePoint=daoArticlePoint.findByArticle(taAbonnement.getTaArticle().getIdArticle());
			if(articlePoint!=null && articlePoint.getTaTypeMouvPoint()!=null && articlePoint.getTaTypeMouvPoint().getTypeMouv().toUpperCase().equals("EF")){
				if(taAbonnement.getTaLDocument()!=null){
//					TaLFactureDAO daoLFacture = new TaLFactureDAO();
					remisePoint=calculRemisePointFacture(taAbonnement.getTaLDocument().getTaDocument())	;
					pointHistorique=taAbonnement.getTaLDocument().getMtHtLApresRemiseGlobaleDocument().multiply(articlePoint.getPoints(), MathContext.DECIMAL128).
							divide(articlePoint.getPrixReference(), MathContext.DECIMAL128).setScale(2, BigDecimal.ROUND_HALF_UP);
					if(remisePoint.compareTo(BigDecimal.ZERO)!=0){
						pointHistorique=pointHistorique.subtract(pointHistorique.multiply(remisePoint, MathContext.DECIMAL128).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128).
								setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
//			if(pointHistorique!=null && !pointHistorique.equals(BigDecimal.ZERO))pointHistorique=(pointHistorique.divide(BigDecimal.valueOf(1).add((taAbonnement.getTaLDocument().getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
//					)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));	
			logger.debug("calculPointHistorique successful");
			return pointHistorique;
		} catch (RuntimeException re) {
			logger.error("calculPointHistorique failed", re);
			throw re;
		}
	}
	
	public BigDecimal calculPointTotalAnnee(TaAbonnementOld taAbonnement , Boolean avecHistorique) {
		try {
			BigDecimal pointAnnee=null;
			BigDecimal pointPartenaire=null;
			BigDecimal pointTotalAnnee=null;			
			TaSupportAbonDAO daoSupportAbonDAO = new TaSupportAbonDAO();
			TaSupportAbon support = null;
			Date datePeremption=null;
			
			if(taAbonnement!=null && taAbonnement.getTaLDocument()!=null){
				datePeremption=taAbonnement.getTaLDocument().getTaDocument().getDateDocument();
				datePeremption=LibDate.incrementDate(datePeremption, 0, 18, 0);
				support=taAbonnement.getTaSupportAbon();
				pointAnnee=calculPointAnnee(taAbonnement);
//				pointPartenaire=calculPointPartenaire(taAbonnement);
				if(pointAnnee!=null && pointAnnee.signum()!=0 && avecHistorique){
					pointTotalAnnee=new BigDecimal(0);
					for (TaAbonnementOld abonnement : support.getTaAbonnements()) {
						pointTotalAnnee=pointTotalAnnee.add(calculPointHistorique(abonnement));
					}
				}
				TaComptePoint comptePoint=null;
				
				if(pointAnnee!=null && pointAnnee.signum()!=0){
					comptePoint = new TaComptePoint(0);
				comptePoint.setCodeDocument(taAbonnement.getTaLDocument().getTaDocument().getCodeDocument());
				comptePoint.setDateAcquisition(taAbonnement.getTaLDocument().getTaDocument().getDateDocument());
				comptePoint.setDatePeremption(datePeremption);
				comptePoint.setPoint(pointAnnee);
				comptePoint.setTaTiers(taAbonnement.getTaTiers());
				comptePoint.setTaTypeMouvPoint(new TaTypeMouvPointDAO().findByCode("EP"));
				merge(comptePoint);
				}
				
				if(pointTotalAnnee!=null && pointTotalAnnee.signum()!=0){
				comptePoint = new TaComptePoint(0);
				comptePoint.setCodeDocument(taAbonnement.getTaLDocument().getTaDocument().getCodeDocument());
				comptePoint.setDateAcquisition(taAbonnement.getTaLDocument().getTaDocument().getDateDocument());
				comptePoint.setDatePeremption(datePeremption);
				comptePoint.setPoint(pointTotalAnnee);
				comptePoint.setTaTiers(taAbonnement.getTaTiers());
				comptePoint.setTaTypeMouvPoint(new TaTypeMouvPointDAO().findByCode("EF"));				
				merge(comptePoint);
				}
				
//				if(pointPartenaire!=null && pointPartenaire.signum()!=0){
//				comptePoint = new TaComptePoint(0);
//				comptePoint.setCodeDocument(taAbonnement.getTaLDocument().getTaDocument().getCodeDocument());
//				comptePoint.setDateAcquisition(taAbonnement.getTaLDocument().getTaDocument().getDateDocument());
//				comptePoint.setDatePeremption(datePeremption);
//				comptePoint.setPoint(pointPartenaire);
//				comptePoint.setTaTiers(taAbonnement.getTaTiers());
//				comptePoint.setTaTypeMouvPoint(new TaTypeMouvPointDAO().findByCode("EG"));				
//				merge(comptePoint);
//				}
				
			}
			logger.debug("calculPointTotalAnnee successful");
			return pointTotalAnnee;
		} catch (RuntimeException re) {
			logger.error("calculPointTotalAnnee failed", re);
			throw re;
		}
	}

	public BigDecimal calculRemisePointAvisEcheance(IDocumentTiers doc, Map<String,BigDecimal> listePointBonus) {
		BigDecimal remisePoint=new BigDecimal(0);
		BigDecimal totalTTCLigne=new BigDecimal(0);
		
		TaTypeMouvPointDAO daoTypePoint = new TaTypeMouvPointDAO();
		try {
			List<TaTypeMouvPoint>listeType= daoTypePoint.selectAll();
			List<String> listeArticleType=new LinkedList<String>();
			for (TaTypeMouvPoint type : listeType) {
				if(!type.getTypeMouv().equals("EF")&&!type.getTypeMouv().equals("EP")&&!type.getTypeMouv().equals("EG"))
					if(!listeArticleType.contains(type.getTaArticle().getCodeArticle()))listeArticleType.add(type.getTaArticle().getCodeArticle());
			}

			for (String codeArticlePoint : listePointBonus.keySet()) {
				if(codeArticlePoint!=null){
					if(listePointBonus.get(codeArticlePoint)!=null && listeArticleType.contains(codeArticlePoint))
						remisePoint=remisePoint.add(listePointBonus.get(codeArticlePoint));
				}
			}		
			if(remisePoint!=null){
				remisePoint=remisePoint.divide(BigDecimal.valueOf(10)).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			logger.debug("calculRemisePointFacture successful");
			return remisePoint;
		} catch (RuntimeException re) {
			logger.error("calculRemisePointFacture failed", re);
			throw re;
		}
	}
	
	public BigDecimal calculRemisePointFacture(IDocumentTiers doc) {
		BigDecimal remisePoint=new BigDecimal(0);

		
		try {
			List<TaTypeMouvPoint>listeType= daoTypePoint.selectAll();
			List<TaArticle> listeArticleType=new LinkedList<TaArticle>();
			for (TaTypeMouvPoint type : listeType) {
				if(!listeArticleType.contains(type.getTaArticle()))listeArticleType.add(type.getTaArticle());
			}

			if(doc instanceof TaFacture){
				for (TaLFacture ligne : ((TaFacture)doc).getLignes()) {
					if(ligne.getTaArticle()!=null && listeArticleType.contains(ligne.getTaArticle())){
						remisePoint=ligne.getMtHtLApresRemiseGlobaleDocument().multiply(BigDecimal.valueOf(-1),MathContext.DECIMAL128);
						remisePoint=remisePoint.multiply(BigDecimal.valueOf(100)).divide(ligne.getTaDocument().getMtHtCalc().add(remisePoint),MathContext.DECIMAL128).
								setScale(2, BigDecimal.ROUND_HALF_UP);
					}
				}			
			}
			if(doc instanceof TaAvisEcheance){
				for (TaLAvisEcheance ligne : ((TaAvisEcheance)doc).getLignes()) {
					if(ligne.getTaArticle()!=null && listeArticleType.contains(ligne.getTaArticle())){
						remisePoint=ligne.getMtHtLApresRemiseGlobaleDocument().multiply(BigDecimal.valueOf(-1),MathContext.DECIMAL128);
						remisePoint=remisePoint.multiply(BigDecimal.valueOf(100)).divide(ligne.getTaDocument().getMtHtCalc().add(remisePoint),MathContext.DECIMAL128).
								setScale(2, BigDecimal.ROUND_HALF_UP);
					}
				}			
			}			
			logger.debug("calculRemisePointFacture successful");
			return remisePoint;
		} catch (RuntimeException re) {
			logger.error("calculRemisePointFacture failed", re);
			throw re;
		}
	}

	

	public List<TaComptePoint> findByTiersAndDate(Integer idTiers,Date dateDeb,Date dateFin) {
//passage EJB
		List<TaComptePoint> listeTemp=new ArrayList<TaComptePoint>();
//		List<TaTypeMouvPoint>listeType=null;
//		List<TaComptePoint> l=null;
//		TaComptePoint taComptePointTemp=null;
//		TaTiers taTiers = daoTiers.findById(idTiers);
//		logger.debug("getting TaComptePoint  with id: " + idTiers);
//		if(dateDeb.before(new Date()))dateDeb=new Date();
//		
//		//*******************************
//		calculPointUtilise(idTiers,dateDeb);
//		//*******************************
//
//		try {
//			Query query = getEntityManager().createQuery("select a from TaTypeMouvPoint a ");	
//			listeType=query.getResultList();
//			//for (TaTypeMouvPoint taTypeMouvPoint : listeType) {
//			query=null; // and a.taTypeMouvPoint=?
//			query = getEntityManager().createQuery("select a from TaComptePoint a join a.taTiers t where t.idTiers = ? " +
//					" and(a.datePeremption is not null )" +
//					" and a.datePeremption >=?  and (a.dateAcquisition <= ? or a.dateAcquisition is null)" +
//					"  order by a.dateAcquisition,a.datePeremption"
//					);
//			query.setParameter(1, idTiers);
//			//query.setParameter(2, taTypeMouvPoint);
//			query.setParameter(2, dateDeb,TemporalType.DATE);
//			query.setParameter(3, dateFin,TemporalType.DATE);
//			l = query.getResultList();
//			if(l.size()>0){
//				taComptePointTemp=new TaComptePoint();
//				taComptePointTemp.setPoint(new BigDecimal(0));				
//				for (TaComptePoint taComptePoint : l) {
//					taComptePointTemp.setTaTiers(taComptePoint.getTaTiers());
//					taComptePointTemp.setPoint(taComptePointTemp.getPoint().add(taComptePoint.getPoint()));
//					taComptePointTemp.setTaTypeMouvPoint(taComptePoint.getTaTypeMouvPoint());
//				}
//			}
//			if(taComptePointTemp!=null){
//				TaComptePoint pointSolde= soldeReelTiers(taTiers, dateDeb, dateFin);
//				if(taComptePointTemp.getPoint().signum()>0){
//					if(pointSolde.getPoint()==null || pointSolde.getPoint().signum()<=0)taComptePointTemp=null;
//					else
//					if( pointSolde.getPoint().compareTo(taComptePointTemp.getPoint())<=0 )
//						taComptePointTemp.setPoint(pointSolde.getPoint());
//				}
//					
//				if(taComptePointTemp!=null)listeTemp.add(taComptePointTemp);
//			}
//			//}
			return listeTemp;
//		} catch (RuntimeException re) {
//			throw re;
//		} catch (Exception e) {
//			return null;
//		}
	}
	
	
	public TaComptePoint soldeReelTiers(TaTiers taTiers,Date dateDeb,Date dateFin) {
//passage EJB
////		List<TaComptePoint> listeTemp=new ArrayList<TaComptePoint>();
//		List<TaTypeMouvPoint>listeType=null;
		TaComptePoint taComptePoint=null;
//		BigDecimal point=null;
//		TaComptePoint taComptePointTemp=null;
//		logger.debug("getting soldeReelTiers  with id: " + taTiers.getIdTiers());
//
//		//*******************************
//		calculPointUtilise(taTiers.getIdTiers(),dateDeb);
//		//*******************************
//
//		try {
//			Query query = getEntityManager().createQuery("select a from TaTypeMouvPoint a ");	
//			listeType=query.getResultList();
//			//for (TaTypeMouvPoint taTypeMouvPoint : listeType) {
//			query=null; // and a.taTypeMouvPoint=?
//			query = getEntityManager().createQuery("select sum(a.point) from TaComptePoint a join a.taTiers t where t.idTiers = ? "
//					//" and ((a.datePeremption >= ? )or (a.datePeremption is null and a.dateUtilisation>=?))" +
////					"  order by a.dateUtilisation,a.datePeremption"
//					);
//			query.setParameter(1, taTiers.getIdTiers());
//			//query.setParameter(2, taTypeMouvPoint);
////			query.setParameter(2, dateDeb,TemporalType.DATE);
////			query.setParameter(3, dateFin,TemporalType.DATE);
//			taComptePoint=new TaComptePoint();
//			point = (BigDecimal)query.getSingleResult();
//			if(point.signum()!=0){
//				taComptePoint.setTaTiers(taTiers);
//				taComptePoint.setPoint(point);	
//			}
//			//}
			return taComptePoint;
//		} catch (RuntimeException re) {
//			throw re;
//		} catch (Exception e) {
//			return null;
//		}
	}

	
	
	public void calculPointUtilise(Integer idTiers, Date dateDeb) {
//passage EJB
//		List<TaTypeMouvPoint>listeType=null;
//		List<TaLAvisEcheance> l = null;
//		List<TaLFacture> f = null;
//		TaComptePoint comptePoint=null;
//		List<String> listeCodeArticlePoint=new LinkedList<String>();
//		TaTypeMouvPoint typeEu= new TaTypeMouvPointDAO().findByCode("EU");
//		if(dateDeb==null)dateDeb=new Date(0);
//		logger.debug("calculPointUtilise  with id: " + idTiers);
//		try {
//			Query query = null;
////			getEntityManager().getTransaction().begin();
//			query = getEntityManager().createQuery("delete from TaComptePoint  where taTiers.idTiers =? "+
//					" and dateUtilisation is not null and dateUtilisation>=? and taTypeMouvPoint=? ");
//			query.setParameter(1, idTiers);
//			query.setParameter(2, dateDeb,TemporalType.DATE);	
//			query.setParameter(3, typeEu);
//			query.executeUpdate();
////			getEntityManager().getTransaction().commit();
//			query=null;
//			query = getEntityManager().createQuery("select a from TaTypeMouvPoint a ");
//			listeType=query.getResultList();
//
////
//			for (TaTypeMouvPoint taTypeMouvPoint : listeType) {
//				if(!listeCodeArticlePoint.contains(taTypeMouvPoint.getTaArticle().getCodeArticle()))
//					listeCodeArticlePoint.add(taTypeMouvPoint.getTaArticle().getCodeArticle());
//			}
//			
//			for (String codeArticle : listeCodeArticlePoint) {				
//			
//				query = getEntityManager().createQuery("select a from TaLAvisEcheance a join a.taDocument d join d.taTiers t join a.taArticle ar where t.idTiers = ? " +
//						" and ar.codeArticle=? and d.dateDocument>=? and not exists(select rd from TaRDocument rd join rd.taAvisEcheance av " +
//						" where av.idDocument=d.idDocument and rd.taFacture is not null) ");// +
////						" union " +
////						"select lf from TaLFacture lf join lf.taDocument d join d.taTiers t join lf.taArticle ar where t.idTiers = ? " +
////						" and ar.codeArticle=? and d.dateDocument>=? ");
//				
//				query.setParameter(1, idTiers);
//				query.setParameter(2, codeArticle);
//				query.setParameter(3, dateDeb,TemporalType.DATE);
//				l=query.getResultList();
//				TaTypeMouvPoint typePointUtilise=new TaTypeMouvPointDAO().findByCode("EU");
//				for (TaLAvisEcheance taLAvisEcheance : l) {
//					getEntityManager().getTransaction().begin();
//					comptePoint=new TaComptePoint(0);
//					comptePoint.setTaTiers(taLAvisEcheance.getTaDocument().getTaTiers());
//					comptePoint.setCodeDocument(taLAvisEcheance.getTaDocument().getCodeDocument());
//					comptePoint.setDateUtilisation(taLAvisEcheance.getTaDocument().getDateDocument());
//					comptePoint.setPoint(taLAvisEcheance.getMtTtcLDocument().multiply(BigDecimal.valueOf(10)));
//					comptePoint.setTaTypeMouvPoint(typePointUtilise);
//					getEntityManager().merge(comptePoint);
////					getEntityManager().getTransaction().commit();
//				}
//				
//				query = getEntityManager().createQuery("select lf from TaLFacture lf join lf.taDocument d join d.taTiers t join lf.taArticle ar where t.idTiers = ? " +
//						" and ar.codeArticle=? and d.dateDocument>=? ");
//				
//				query.setParameter(1, idTiers);
//				query.setParameter(2, codeArticle);
//				query.setParameter(3, dateDeb,TemporalType.DATE);
//				f=query.getResultList();
//				for (TaLFacture taLAvisEcheance : f) {
//					getEntityManager().getTransaction().begin();
//					comptePoint=new TaComptePoint(0);
//					comptePoint.setTaTiers(taLAvisEcheance.getTaDocument().getTaTiers());
//					comptePoint.setCodeDocument(taLAvisEcheance.getTaDocument().getCodeDocument());
//					comptePoint.setDateUtilisation(taLAvisEcheance.getTaDocument().getDateDocument());
//					comptePoint.setPoint(taLAvisEcheance.getMtTtcLApresRemiseGlobaleDocument().multiply(BigDecimal.valueOf(10)));
//					comptePoint.setTaTypeMouvPoint(typePointUtilise);
//					getEntityManager().merge(comptePoint);
////					getEntityManager().getTransaction().commit();
//				}				
//			}
//			
//
//			
//
//		} catch (RuntimeException re) {
////			if(entityManager.getTransaction().isActive())entityManager.getTransaction().rollback();
//			throw re;
//		} catch (Exception e) {
//		}
	}
	

	public BigDecimal calculPointPartenaire(TaAbonnementOld taAbonnement,TaLAvisEcheance ligneAvisEcheance, Map<String,BigDecimal> listePointBonus) {
		/*passage ejb isabelle pour compilation*/
//		try {
//			BigDecimal pointPartenaire=new BigDecimal(0);
//			BigDecimal remisePoint=null;
//			//je récupère le % associé au groupe s'il y en a un
//			TaPourcGroupe Pourcgroupe=null;
//			TaPourcGroupeDAO daoPourcGroupe = new TaPourcGroupeDAO();
////			TaLFacture ligneFacture=null;
//			Query ejbQuery = getEntityManager().createQuery("select s from TaSupportAbon s  where s.idSupportAbon="+taAbonnement.getTaSupportAbon().getIdSupportAbon());
//			List<TaSupportAbon> l = ejbQuery.getResultList();
//			for (TaSupportAbon taSupportAbon : l) {
//				Pourcgroupe=null;
//				BigDecimal valeur=null;
//				
//				if(taSupportAbon instanceof TaLicenceBdg){
//					if(((TaLicenceBdg)taSupportAbon).getGroupe()!=null){
//					if(taAbonnement.getTaTAbonnement()!=null)
//						Pourcgroupe=daoPourcGroupe.findByTAbonnementFamille(((TaLicenceBdg)taSupportAbon).getGroupe().getIdFamille(),taAbonnement.getTaTAbonnement().getIdTAbonnement());
//					}
//				}
//				if(taSupportAbon instanceof TaLicenceEpicea){
//					if(((TaLicenceEpicea)taSupportAbon).getGroupe()!=null){
//					if(taAbonnement.getTaTAbonnement()!=null && ((TaLicenceEpicea)taSupportAbon).getGroupe()!=null)
//						Pourcgroupe=daoPourcGroupe.findByTAbonnementFamille(((TaLicenceEpicea)taSupportAbon).getGroupe().getIdFamille(),taAbonnement.getTaTAbonnement().getIdTAbonnement());
//					}
//				}
//				if(taSupportAbon instanceof TaLicenceSara){
//					if(((TaLicenceSara)taSupportAbon).getGroupe()!=null){
//					if(taAbonnement.getTaTAbonnement()!=null && ((TaLicenceSara)taSupportAbon).getGroupe()!=null)
//						Pourcgroupe=daoPourcGroupe.findByTAbonnementFamille(((TaLicenceSara)taSupportAbon).getGroupe().getIdFamille(),taAbonnement.getTaTAbonnement().getIdTAbonnement());
//					}
//				}
//				
//				if(Pourcgroupe!=null){
////					ligneFacture=taAbonnement.getTaLDocument();
//					if(ligneAvisEcheance!=null){
//						remisePoint=calculRemisePointAvisEcheance(ligneAvisEcheance.getTaDocument(),listePointBonus)	;
//						valeur=ligneAvisEcheance.getMtTtcLApresRemiseGlobaleDocument().multiply(Pourcgroupe.getPourcentage(), MathContext.DECIMAL128).
//						divide(BigDecimal.valueOf(100),MathContext.DECIMAL128).setScale(2,RoundingMode.HALF_UP);
//						if(valeur!=null)valeur=valeur.multiply(BigDecimal.valueOf(10),MathContext.DECIMAL128).setScale(2,RoundingMode.HALF_UP);
//						pointPartenaire=pointPartenaire.add(valeur);
//						if(remisePoint.compareTo(BigDecimal.ZERO)!=0){
//							pointPartenaire=pointPartenaire.subtract(pointPartenaire.multiply(remisePoint,MathContext.DECIMAL128).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
//						}
//					}
//				}				
//			}		
////			if(pointPartenaire!=null && !pointPartenaire.equals(BigDecimal.ZERO))pointPartenaire=(pointPartenaire.divide(BigDecimal.valueOf(1).add((ligneFacture.getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
////					)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));
//			logger.debug("calculPointPartenaire successful");
//			return pointPartenaire;
//		} catch (RuntimeException re) {
//			logger.error("calculPointPartenaire failed", re);
//			throw re;
//		}
		return null;
	}
	
	
	public void removeTousLesPointsBonus(IDocumentTiers persistentInstance,boolean message,boolean bundleExist) throws Exception {
		/*passage ejb isabelle pour compilation*/
//		// ***TODO ISA **  a remettre après avoir trouver comment contourner le problème des liaison avec les points bonus pour autres utilisateur de BDG   ******
//		try {	
//			if(bundleExist){
//				boolean continuer=!message;
//				List<TaComptePoint> listeComptePoint=null;
//				listeComptePoint=selectComptePointBonusFacture(persistentInstance);
//				if(listeComptePoint!=null && listeComptePoint.size()>0){
//					if(!continuer)continuer=MessageDialog.openQuestion(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "document liée à points bonus", 
//							"le document "+persistentInstance.getCodeDocument()+" est liée à "+listeComptePoint.size()+" ligne(s) de points bonus. Ils vont être supprimés. Voulez-vous continuer ?");
//					if(continuer){
//						for (TaComptePoint obj : listeComptePoint) {
//							obj.setTaAvisEcheance(null);
//							remove(obj);
//						}
//					}else throw new Exception();
//				}
//			}
//		} catch (RuntimeException re) {
//			logger.error("removeTousLesAbonnements failed", re);
//			throw re;
//		}		
	}
	

	
	public List<TaComptePoint> selectComptePointBonusFacture(IDocumentTiers doc) {
//passage EJB
		List<TaComptePoint> l=null;
//		logger.debug("selectComptePointBonusFacture");
//		try {
//			if(doc!=null && doc.getIdDocument()!=0){
//			Query query = getEntityManager().createQuery("select f from TaComptePoint f " +
//					" where f.codeDocument like '"+doc.getCodeDocument()+"'" );
//			l = query.getResultList();
//			}
//			logger.debug("selectComptePointBonusFacture successful");
			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectComptePointBonusFacture failed", re);
//			throw re;
//		}
	}
	
	


	public TaComptePoint soldeReelPointTiers(Integer idTiers,Date dateDeb,Date dateFin) {
//passage EJB
//		List<TaComptePoint> listeTemp=new ArrayList<TaComptePoint>();
//		List<TaTypeMouvPoint>listeType=null;
//		List<TaComptePoint> l=null;
		TaComptePoint taComptePointTemp=null;
//		logger.debug("getting TaComptePoint  with id: " + idTiers);
//		
//		//*******************************
//		calculPointUtilise(idTiers,dateDeb);
//		//*******************************
//		
//		try {
//			Query query = getEntityManager().createQuery("select a from TaTypeMouvPoint a ");	
//			listeType=query.getResultList();
//			//for (TaTypeMouvPoint taTypeMouvPoint : listeType) {
//				query=null; // and a.taTypeMouvPoint=?
//				query = getEntityManager().createQuery("select a from TaComptePoint a join a.taTiers t where t.idTiers = ? " +
//						" and ((a.datePeremption >= ? )or (a.datePeremption is null and a.dateUtilisation>=?))" +
//						"  order by a.dateUtilisation,a.datePeremption"
//						);
//				query.setParameter(1, idTiers);
//				//query.setParameter(2, taTypeMouvPoint);
//				query.setParameter(2, dateDeb,TemporalType.DATE);
//				query.setParameter(3, dateFin,TemporalType.DATE);
//				l = query.getResultList();
//				taComptePointTemp=new TaComptePoint();
//				taComptePointTemp.setPoint(new BigDecimal(0));				
//				for (TaComptePoint taComptePoint : l) {
//					taComptePointTemp.setTaTiers(taComptePoint.getTaTiers());
//					taComptePointTemp.setPoint(taComptePointTemp.getPoint().add(taComptePoint.getPoint()));
//					taComptePointTemp.setTaTypeMouvPoint(taComptePoint.getTaTypeMouvPoint());
//				}
//				if(taComptePointTemp!=null)listeTemp.add(taComptePointTemp);
//			//}
			return null;
//		} catch (RuntimeException re) {
//			throw re;
//		} catch (Exception e) {
//			return null;
//		}
	}
	

	public Boolean ExistPointTiersTypeMouvDocument(TaAbonnementOld abonnement,Integer idTypeMouv) {
//passage EJB
//		logger.debug("getting ExistPointTiersTypeMouvDocument  with id: " + abonnement.getTaTiers().getIdTiers());
//		try {
//			Query query = getEntityManager().createQuery("select count(a) from TaComptePoint a join a.taTypeMouvPoint tm" +
//					" join a.taTiers t where t.idTiers = ? " +
//					" and tm.idTypeMouv = ? and a.codeDocument = ?");
//			query.setParameter(1, abonnement.getTaTiers().getIdTiers());
//			query.setParameter(2, idTypeMouv);
//			query.setParameter(3, abonnement.getTaLDocument().getTaDocument().getCodeDocument());
//			Long instance = (Long)query.getSingleResult();
//			logger.debug("get successful");
//			return instance!=0;
//		} catch (RuntimeException re) {
//			throw re;
//		} catch (Exception e) {
			return false;
//		}
	}
	
	

	
	
}
