package fr.legrain.article.service;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.eclipse.core.internal.localstore.IUnifiedTreeVisitor;
import org.hibernate.OptimisticLockException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import fr.legrain.article.dao.IArticleComposeDAO;
import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dao.IUniteDAO;
import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleComposeServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRTitreTransportServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaArticleComposeMapper;
import fr.legrain.bdg.model.mapping.mapper.TaArticleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaTiersDTO;


/**
 * Session Bean implementation class TaArticleBean
 */
@Stateless
//@Stateful
//@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaArticleComposeService extends AbstractApplicationDAOServer<TaArticleCompose, TaArticleComposeDTO> implements ITaArticleComposeServiceRemote {

	static Logger logger = Logger.getLogger(TaArticleComposeService.class);

	@Inject private IArticleComposeDAO dao;
	@Inject private IUniteDAO daoUnite;
	@Inject private IFactureDAO daoFacture;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaRTitreTransportServiceRemote daoRTitreTransportService;
	@EJB private ITaTitreTransportServiceRemote daoTitreTransportService;
	@EJB private ITaGenCodeExServiceRemote gencode;
	
	/**
	 * Default constructor. 
	 */
	public TaArticleComposeService() {
		super(TaArticleCompose.class,TaArticleComposeDTO.class);
	}

	//	private String defaultJPQLQuery = "select a from TaArticle a";

	


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaArticleCompose transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaArticleCompose transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaArticleCompose persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdArticleCompose()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}

	public TaArticleCompose merge(TaArticleCompose detachedInstance) {
		return merge(detachedInstance, null);
	}


	
	public TaArticleCompose findById(int id) throws FinderException {
		return dao.findById(id);
	}

	//@RolesAllowed("admin")
	public List<TaArticleCompose> selectAll() {
		return dao.selectAll();
	}

	public TaArticleCompose findByIdArticleParentAndByIdArticleEnfant(int idArticleParent, int idArticleEnfant) {
		return dao.findByIdArticleParentAndByIdArticleEnfant( idArticleParent,  idArticleEnfant);
	}
	
	public List<TaArticleCompose> findAllByIdArticleEnfant(int idArticleEnfant){
		return dao.findAllByIdArticleEnfant(idArticleEnfant);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public List<TaArticleComposeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	public void deleteListDTO(List<TaArticleComposeDTO> liste) {
		 dao.deleteListDTO(liste);
	}
	
	public void deleteList(List<TaArticleCompose> liste) {
		 dao.deleteList(liste);
	}
	public void deleteSet(Set<TaArticleCompose> set) {
		dao.deleteSet(set);
	}


//	public TaArticleComposeDTO entityToDTO(TaArticleCompose entity) {
//		//		TaArticleComposeDTO dto = new TaArticleComposeDTO();
//		//		dto.setId(entity.getIdTCivilite());
//		//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		//		return dto;
//		TaArticleComposeMapper mapper = new TaArticleComposeMapper();
//		return mapper.mapEntityToDto(entity, null);
//	}

//	public List<TaArticleComposeDTO> listEntityToListDTO(List<TaArticleCompose> entity) {
//		List<TaArticleComposeDTO> l = new ArrayList<TaArticleComposeDTO>();
//
//		for (TaArticleCompose taTCivilite : entity) {
//			l.add(entityToDTO(taTCivilite));
//		}
//
//		return l;
//	}

//	@RolesAllowed("admin")
//	public List<TaArticleComposeDTO> selectAllDTO() {
//		System.out.println("List of TaArticleComposeDTO EJB :");
//		ArrayList<TaArticleComposeDTO> liste = new ArrayList<TaArticleComposeDTO>();
//
//		List<TaArticleCompose> projects = selectAll();
//		for(TaArticleCompose project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}

//	public TaArticleComposeDTO findByIdDTO(int id) throws FinderException {
//		return entityToDTO(findById(id));
//	}
//
//	public TaArticleComposeDTO findByCodeDTO(String code) throws FinderException {
//		return entityToDTO(findByCode(code));
//	}

	@Override
	public void error(TaArticleComposeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}

	public void mergeDTO(TaArticleComposeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}



	public void persistDTO(TaArticleComposeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}





	@Override
	protected TaArticleCompose refresh(TaArticleCompose persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaArticleCompose value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaArticleCompose value, String propertyName, String validationContext) {
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
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaArticleComposeDTO dto, String propertyName, String validationContext) throws BusinessValidationException {
		try {
			//			TaArticleComposeMapper mapper = new TaArticleComposeMapper();
			//			TaArticleCompose entity = mapper.mapDtoToEntity(dto,null);
			//			validateEntityProperty(entity,propertyName,validationContext);

			if(validationContext==null) validationContext="";
			//			validateDTO(dto, propertyName, modeObjetEcranServeur, validationContext);
			validateDTO(dto, propertyName, validationContext);

			//validation automatique via la JSR bean validation
			//			BeanValidator<TaArticleComposeDTO> validator = new BeanValidator<TaArticleComposeDTO>(TaArticleComposeDTO.class);
			//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new EJBException(e.getMessage());
			throw new BusinessValidationException(e.getMessage());
		}

	}

	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaArticleComposeDTO dto) {
		validateDTO(dto,null);

	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaArticleComposeDTO dto, String propertyName) throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);

	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaArticleCompose value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaArticleCompose value, String propertyName) {
		validateEntityProperty(value,propertyName,null);

	}


	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaArticleCompose.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}

	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaArticleCompose.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaArticleCompose.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeDTO(TaArticleComposeDTO dto) throws RemoveException {
		try {
			TaArticleComposeMapper mapper = new TaArticleComposeMapper();
			TaArticleCompose entity = mapper.mapDtoToEntity(dto,null);
			dao.remove(entity);
			//supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
		
	}

	@Override
	public void persistDTO(TaArticleComposeDTO transientInstance, String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaArticleComposeDTO detachedInstance, String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaArticleComposeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticleComposeDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaArticleComposeDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaArticleComposeDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaArticleComposeDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaArticleCompose merge(TaArticleCompose detachedInstance, String validationContext) {
		return dao.merge(detachedInstance);
	}

	@Override
	public TaArticleCompose findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}


	


}
