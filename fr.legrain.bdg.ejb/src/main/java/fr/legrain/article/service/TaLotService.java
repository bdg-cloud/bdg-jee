package fr.legrain.article.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.ILotDAO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.article.model.TracabiliteMP;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.mapper.TaLotMapper;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;


/**
 * Session Bean implementation class TaLotBean
 */
@Stateless
//@Stateful
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLotService extends AbstractApplicationDAOServer<TaLot, TaLotDTO> implements ITaLotServiceRemote {

	static Logger logger = Logger.getLogger(TaLotService.class);
	public static final String DEBUT_VIRTUEL_LOT ="VIRT_";
	
	@Inject private ILotDAO dao;
	@Inject private	SessionInfo sessionInfo;
	
	@EJB private ITaGenCodeExServiceRemote gencode;
	@EJB private ITaArticleServiceRemote taArticleServiceRemote;
	@EJB private ITaResultatConformiteServiceRemote taResultatConformiteService;
	
	/**
	 * Default constructor. 
	 */
	public TaLotService() {
		super(TaLot.class,TaLotDTO.class);
	}
	
	public TracabiliteLot findTracaLot(String numLot) {
		return dao.findTracaLot(numLot);
	}
	
	public TaLot createLotVirtuelArticleFabOrBR(int idArticle,Map<String, String> params) {
		TaLot l = null ;
		String codeGenere="";
			TaArticle art = null;
			try {
				art = taArticleServiceRemote.findById(idArticle);
				l = new TaLot();
				l.setTaArticle(art);
				l.setVirtuel(true);
				l.setVirtuelUnique(true);
				params.put(Const.C_VIRTUEL, "true");
				codeGenere=genereCode(params);
				l.setNumLot(DEBUT_VIRTUEL_LOT+art.getCodeArticle()+"_"+codeGenere);
				l.setLibelle(art.getLibellecArticle());
				l = merge(l);
			} catch (FinderException e) {
				e.printStackTrace();
			}		
		return l;
	}
	
	
	public TaLot findOrCreateLotVirtuelArticle(int idArticle) {
		TaLot l = dao.findLotVirtuelArticle(idArticle);
		if(l==null) {
			TaArticle art = null;
			try {
				art = taArticleServiceRemote.findById(idArticle);
				l = new TaLot();
				l.setTaArticle(art);
				l.setVirtuel(true);
				l.setNumLot(DEBUT_VIRTUEL_LOT+art.getCodeArticle());
				l.setLibelle(art.getLibellecArticle());
				l = merge(l);
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		return l;
	}
	
	public String validationLot(TaLot lot) {
		
		String action = TaStatusConformite.C_TYPE_ACTION_VIDE;
		if(lot!=null){
			
			TaStatusConformite s = taResultatConformiteService.etatLot(lot.getIdLot());
			if(s!=null) {
				action = s.getCodeStatusConformite();
			
				if(action.equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER)
						|| action.equals(TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE)
						|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
						) {
					lot.setPresenceDeNonConformite(true);
				} else {
					lot.setPresenceDeNonConformite(false);
				} 
				
				if(action.equals(TaStatusConformite.C_TYPE_ACTION_OK)
						|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
						) {
					lot.setLotConforme(true);
				} else {
					lot.setLotConforme(false);
				} 
			}
		}
		return action;
	}
	
	public void changeLotTermine(List<Integer> listeDesIdLotATraite, boolean termine) {
		dao.changeLotTermine(listeDesIdLotATraite, termine);
	}
	
	public List<TaLotDTO> findLot(Date debut, Date fin, Boolean presenceDeNonConformite) {
		return dao.findLot(debut, fin, presenceDeNonConformite);
	}
	
	public List<TaLotDTO> findLotAvecQte(Date debut, Date fin, Boolean presenceDeNonConformite) {
		return dao.findLotAvecQte(debut, fin, presenceDeNonConformite);
	}
	
	public List<TaLotDTO> findLotNonConforme(Date debut, Date fin) {
		return dao.findLotNonConforme(debut, fin);
	}
	
	public TaLot fetchResultatControleLazy(int idLot) {
		return dao.fetchResultatControleLazy(idLot);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLot transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLot transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLot persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLot()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLot merge(TaLot detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLot merge(TaLot detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLot findById(int id) throws FinderException {
		return dao.findById(id);
	}
	
	public TaLot findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLot> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	@Override
	public List<TaLotDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLot> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLotDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLotDTO entityToDTO(TaLot entity) {
//		TaLotDTO dto = new TaLotDTO();
//		dto.setIdArticle(entity.getTaArticle().getIdArticle());
//		dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
//		dto.setLibelleArticle(entity.getTaArticle().getLibellecArticle());
//		dto.setId(entity.getIdLot()) ;
//		dto.setNumLot(entity.getNumLot()) ;
//		dto.setId(entity.getIdLot());
//		dto.setCodeUnite1(entity.getTaArticle().getTaRapportUnite().getTaUnite1().getCodeUnite());
//		dto.setCodeUnite2(entity.getTaArticle().getTaRapportUnite().getTaUnite2().getCodeUnite());
//		dto.setIdUnite1(entity.getTaArticle().getTaRapportUnite().getTaUnite1().getIdUnite());
//		dto.setIdUnite2(entity.getTaArticle().getTaRapportUnite().getTaUnite2().getIdUnite());
//		dto.setRapport(entity.getTaArticle().getTaRapportUnite().getRapport());
//		dto.setLiblUnite1(entity.getTaArticle().getTaRapportUnite().getTaUnite1().getLiblUnite());
//		dto.setLiblUnite2(entity.getTaArticle().getTaRapportUnite().getTaUnite2().getLiblUnite());
//		return dto;
		
		TaLotMapper mapper = new TaLotMapper();
		return mapper.mapEntityToDto(entity, null);
		
		
		
	}

	public List<TaLotDTO> listEntityToListDTO(List<TaLot> entity) {
		List<TaLotDTO> l = new ArrayList<TaLotDTO>();

		for (TaLot taLotArticle : entity) {
			l.add(entityToDTO(taLotArticle));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLotDTO> selectAllDTO() {
		System.out.println("List of TaLotDTO EJB :");
		ArrayList<TaLotDTO> liste = new ArrayList<TaLotDTO>();

		List<TaLot> projects = selectAll();
		for(TaLot project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}
	public List<TaLotDTO> selectAllDTOLight() {
		System.out.println("List of TaLotDTO EJB :");
		return findAllLight();
//		ArrayList<TaLotDTO> liste = new ArrayList<TaLotDTO>();
//		List<Object[]> l = dao.findAllObjectLight();
//		for(Object[] o :l){
//			TaLotDTO dto = new TaLotDTO();
//			if(o[0]!=null)dto.setId((Integer) o[0]);
//			if(o[1]!=null)dto.setIdArticle((Integer) o[1]);
//			//dto.setIdLot((Integer) o[2]);
//			if(o[3]!=null)dto.setCodeArticle( (String) o[3]);
//			if(o[4]!=null)dto.setLibelleArticle((String) o[4]);
//			if(o[5]!=null)dto.setNumLot((String) o[5]);
//			if(o[6]!=null)dto.setCodeUnite1((String) o[6]);
//			if(o[7]!=null)dto.setCodeUnite2((String) o[7]);
//			if(o[8]!=null)dto.setRapport((BigDecimal) o[8]);
//			if(o[9]!=null)dto.setNbDecimal((Integer) o[9]);
//			if(o[10]!=null)dto.setSens(LibConversion.intToBoolean((Integer) o[10]));
//			if(o[11]!=null)dto.setTermine((Boolean) o[11]);
//			liste.add(dto);
//		}
//		
//
//		return liste;
	}
	
	public TaLotDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLotDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLotDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLotDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLotDTO dto, String validationContext) throws EJBException {
		try {
			TaLotMapper mapper = new TaLotMapper();
			TaLot entity = null;
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
	
	public void persistDTO(TaLotDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLotDTO dto, String validationContext) throws CreateException {
		try {
			
			
			TaLot entity =  dao.findByIdAAndIdLot(dto.getIdArticle(), dto.getId());
			dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}
	public TaLot entityFromDTO(TaLotDTO dto)  {
		
			
			
			TaLot entity =  dao.findByIdAAndIdLot(dto.getIdArticle(), dto.getId());
		return entity;
		
	}

	@Override
	public void removeDTO(TaLotDTO dto) throws RemoveException {
		try {
			TaLotMapper mapper = new TaLotMapper();
			TaLot entity = mapper.mapDtoToEntity(dto,null);
			dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLot refresh(TaLot persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLot value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLot value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLotDTO dto, String validationContext) {
		try {
			TaLotMapper mapper = new TaLotMapper();
			TaLot entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLotDTO> validator = new BeanValidator<TaLotDTO>(TaLotDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLotDTO dto, String propertyName, String validationContext) {
		try {
			TaLotMapper mapper = new TaLotMapper();
			TaLot entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLotDTO> validator = new BeanValidator<TaLotDTO>(TaLotDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLotDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLotDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLot value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLot value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaLot.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaLot.class.getSimpleName()),code,false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaLot.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<TaLotDTO> findWithNamedQueryDTO(String arg0)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLot findByIdAAndIdLot(int idArticle, int idLot) {
		TaLot la= dao.findByIdAAndIdLot(idArticle, idLot);
		la.getTaArticle();
//		la.getTaLot();
		return la;
	}
	
	@Override
	public TaLot findByCodeAAndNumLot(String codeA, String numLot) {
		TaLot la = dao.findByCodeAAndNumLot(codeA, numLot);
		la.getTaArticle();
//		la.getTaLot();
		return la;
	}

	@Override
	public List<TaArticle> articleEnStock() {
		// TODO Auto-generated method stub
		return dao.articleEnStock();
	}

	@Override
	public List<TaLot> lotsArticle(String codeArticle, String codeEntrepot,
			String emplacement, Boolean termine) {
		// TODO Auto-generated method stub
		return dao.lotsArticle( codeArticle,  codeEntrepot,	 emplacement,  termine);
	}

	@Override
	public List<TaLot> lotsNonTermine() {
		// TODO Auto-generated method stub
		return dao.lotsNonTermine();
	}

	/* - Dima - Début - */
//	@Override
//	public List<TaLot> findByNonConforme() {
//		// TODO Auto-generated method stub
//		return dao.findByNonConforme();
//	}
	@Override
	public List<TaLot> findByNonConformeNoDate() {
		// TODO Auto-generated method stub
		return dao.findByNonConformeNoDate();
	}
	@Override
	public List<TaResultatCorrection> findByNonConformeCorrection(){
		// TODO Auto-generated method stub
		return dao.findByNonConformeCorrection();
	}
	/* - Dima -  Fin  - */

	@Override
	public List<TaLotDTO> findAllLight() {
		// TODO Auto-generated method stub
		return dao.findAllLight();
	}

	@Override
	public List<TaLotDTO> findByCodeLight(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodeLight(code);
	}

	@Override
	public List<TaLotDTO> selectAllInFabrication() {
		// TODO Auto-generated method stub
		return dao.selectAllInFabrication();
	}

	@Override
	public List<TaLotDTO> selectAllInReception() {
		// TODO Auto-generated method stub
		return dao.selectAllInReception();
	}

	@Override
	public TracabiliteMP findTracaMP(String numLot) {
		// TODO Auto-generated method stub
		return dao.findTracaMP(numLot);
	}

	@Override
	public List<TaLotDTO> selectAllPFInFabrication() {
		// TODO Auto-generated method stub
		return dao.selectAllPFInFabrication();
	}

	@Override
	public TaLot findAvecResultatConformite(String numLot) {
		// TODO Auto-generated method stub
		return dao.findAvecResultatConformite(numLot);
	}

	@Override
	public boolean controle(TaLot lot) {
		return dao.controle(lot);
	}
	
	@Override
	public String retourControl(TaLot lot) {
		return dao.retourControl(lot);
	}
	
	@Override
	public TaLot initListeResultatControle(List<TaConformite> l, TaLot lot) {
		return dao.initListeResultatControle(l, lot);
	}

	@Override
	public void suppression_lot_non_utilise() {
		// TODO Auto-generated method stub
		 dao.suppression_lot_non_utilise();
	}

	@Override
	public TaLot findLotVirtuelArticle(int idArticle) {
		// TODO Auto-generated method stub
		return dao.findLotVirtuelArticle(idArticle);
	}
	
	
}
