package fr.legrain.article.service;

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

import fr.legrain.article.dao.IArticleComposeDAO;
import fr.legrain.article.dao.IComportementArticleComposeDAO;
import fr.legrain.article.dao.IUniteDAO;
import fr.legrain.article.dto.TaComportementArticleComposeDTO;
import fr.legrain.article.model.TaComportementArticleCompose;
import fr.legrain.bdg.article.service.remote.ITaArticleComposeServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaComportementArticleComposeServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRTitreTransportServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaArticleComposeMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaArticleBean
 */
@Stateless
//@Stateful
//@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaComportementArticleComposeService extends AbstractApplicationDAOServer<TaComportementArticleCompose, TaComportementArticleComposeDTO> implements ITaComportementArticleComposeServiceRemote {

	static Logger logger = Logger.getLogger(TaComportementArticleComposeService.class);

	@Inject private IComportementArticleComposeDAO dao;
	@Inject private IUniteDAO daoUnite;
	@Inject private IFactureDAO daoFacture;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaRTitreTransportServiceRemote daoRTitreTransportService;
	@EJB private ITaTitreTransportServiceRemote daoTitreTransportService;
	@EJB private ITaGenCodeExServiceRemote gencode;
	
	/**
	 * Default constructor. 
	 */
	public TaComportementArticleComposeService() {
		super(TaComportementArticleCompose.class,TaComportementArticleComposeDTO.class);
	}

	//	private String defaultJPQLQuery = "select a from TaArticle a";

	


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaComportementArticleCompose transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaComportementArticleCompose transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaComportementArticleCompose persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdComportementArticleCompose()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}

	public TaComportementArticleCompose merge(TaComportementArticleCompose detachedInstance) {
		return merge(detachedInstance, null);
	}


	
	public TaComportementArticleCompose findById(int id) throws FinderException {
		return dao.findById(id);
	}

	//@RolesAllowed("admin")
	public List<TaComportementArticleCompose> selectAll() {
		return dao.selectAll();
	}


	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public List<TaComportementArticleComposeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	public void deleteListDTO(List<TaComportementArticleComposeDTO> liste) {
		 dao.deleteListDTO(liste);
	}
	
	public void deleteList(List<TaComportementArticleCompose> liste) {
		 dao.deleteList(liste);
	}
	public void deleteSet(Set<TaComportementArticleCompose> set) {
		dao.deleteSet(set);
	}





	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}





	@Override
	protected TaComportementArticleCompose refresh(TaComportementArticleCompose persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaComportementArticleCompose value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaComportementArticleCompose value, String propertyName, String validationContext) {
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
	public void validateDTOProperty(TaComportementArticleComposeDTO dto, String propertyName, String validationContext) throws BusinessValidationException {
		try {
			//			TaArticleComposeMapper mapper = new TaArticleComposeMapper();
			//			TaComportementArticleCompose entity = mapper.mapDtoToEntity(dto,null);
			//			validateEntityProperty(entity,propertyName,validationContext);

			if(validationContext==null) validationContext="";
			//			validateDTO(dto, propertyName, modeObjetEcranServeur, validationContext);
			validateDTO(dto, propertyName, validationContext);

			//validation automatique via la JSR bean validation
			//			BeanValidator<TaComportementArticleComposeDTO> validator = new BeanValidator<TaComportementArticleComposeDTO>(TaComportementArticleComposeDTO.class);
			//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new EJBException(e.getMessage());
			throw new BusinessValidationException(e.getMessage());
		}

	}

	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaComportementArticleComposeDTO dto) {
		validateDTO(dto,null);

	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaComportementArticleComposeDTO dto, String propertyName) throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);

	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaComportementArticleCompose value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaComportementArticleCompose value, String propertyName) {
		validateEntityProperty(value,propertyName,null);

	}


	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaComportementArticleCompose.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}

	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaComportementArticleCompose.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaComportementArticleCompose.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeDTO(TaComportementArticleComposeDTO dto) throws RemoveException {
//		try {
//			TaArticleComposeMapper mapper = new TaArticleComposeMapper();
//			TaComportementArticleCompose entity = mapper.mapDtoToEntity(dto,null);
//			dao.remove(entity);
//			//supprimer(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RemoveException(e.getMessage());
//		}
		
	}

	@Override
	public void persistDTO(TaComportementArticleComposeDTO transientInstance, String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaComportementArticleComposeDTO detachedInstance, String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaComportementArticleComposeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaComportementArticleComposeDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaComportementArticleComposeDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaComportementArticleComposeDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaComportementArticleComposeDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaComportementArticleCompose merge(TaComportementArticleCompose detachedInstance, String validationContext) {
		return dao.merge(detachedInstance);
	}

	@Override
	public TaComportementArticleCompose findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persistDTO(TaComportementArticleComposeDTO transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaComportementArticleComposeDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaComportementArticleComposeDTO dto) {
		// TODO Auto-generated method stub
		
	}


	


}
