package fr.legrain.abonnement.service;

import java.util.ArrayList;
import java.util.List;

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

import fr.legrain.abonnement.dao.jpa.IJourRelanceDAO;
import fr.legrain.abonnement.dto.TaJourRelanceDTO;
import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.abonnement.service.remote.ITaJourRelanceServiceRemote;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaJourRelanceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaJourRelanceService extends AbstractApplicationDAOServer<TaJourRelance, TaJourRelanceDTO> implements ITaJourRelanceServiceRemote {
	static Logger logger = Logger.getLogger(TaJourRelanceService.class);

	@Inject private IJourRelanceDAO dao;
	
	/**
	 * Default constructor. 
	 */
	public TaJourRelanceService() {
		super(TaJourRelance.class,TaJourRelanceDTO.class);
	}

	@Override
	public List<TaJourRelance> selectAll() {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}

	public void persist(TaJourRelance transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaJourRelance transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaJourRelance persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdJourRelance()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaJourRelance merge(TaJourRelance detachedInstance) {
		return merge(detachedInstance, null);
	}
	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaJourRelance merge(TaJourRelance detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}



	@Override
	public TaJourRelance findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

//	@Override
//	public TaJourRelance findByCode(String code) throws FinderException {
//		// TODO Auto-generated method stub
//		return dao.findByCode(code);
//	}

	@Override
	public void validateEntity(TaJourRelance value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaJourRelance value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaJourRelance value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaJourRelance value, String propertyName, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaJourRelanceDTO transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaJourRelanceDTO persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaJourRelanceDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaJourRelanceDTO transientInstance, String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaJourRelanceDTO detachedInstance, String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaJourRelanceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaJourRelanceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<TaJourRelanceDTO> findByIdArticleDTO(int idArticle){
		return dao.findByIdArticleDTO(idArticle);
	}

	@Override
	public List<TaJourRelanceDTO> selectAllDTO() {
		System.out.println("List of TaJourRelanceDTO EJB :");
		ArrayList<TaJourRelanceDTO> liste = new ArrayList<TaJourRelanceDTO>();

		List<TaJourRelance> projects = selectAll();
		for(TaJourRelance project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}
	
	public TaJourRelanceDTO entityToDTO(TaJourRelance entity) {
//		TaFamilleDTO dto = new TaFamilleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaJourRelanceMapper mapper = new TaJourRelanceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	@Override
	public TaJourRelanceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

//	@Override
//	public TaJourRelanceDTO findByCodeDTO(String code) throws FinderException {
//		return entityToDTO(findByCode(code));
//	}

	@Override
	public void validateDTO(TaJourRelanceDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaJourRelanceDTO dto, String propertyName) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaJourRelanceDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaJourRelanceDTO dto, String propertyName, String validationContext)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaJourRelanceDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected TaJourRelance refresh(TaJourRelance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public List<TaJourRelanceDTO> findByCodeLight(String code) {
//		// TODO Auto-generated method stub
//		return dao.findByCodeLight(code);
//	}

	@Override
	public List<TaJourRelanceDTO> findAllLight() {
		// TODO Auto-generated method stub
		return dao.findAllLight();
	}

	@Override
	public TaJourRelanceDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaJourRelance findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}
}
