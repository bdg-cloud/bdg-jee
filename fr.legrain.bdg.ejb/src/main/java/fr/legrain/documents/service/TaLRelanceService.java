package fr.legrain.documents.service;

import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import fr.legrain.bdg.documents.service.remote.ITaLRelanceServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLRelanceDTO;
import fr.legrain.document.model.TaLRelance;
import fr.legrain.documents.dao.ILRelanceDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLRelanceService extends AbstractApplicationDAOServer<TaLRelance, TaLRelanceDTO> implements ITaLRelanceServiceRemote {

	static Logger logger = Logger.getLogger(TaLRelanceService.class);

	@Inject private ILRelanceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLRelanceService() {
		super(TaLRelance.class,TaLRelanceDTO.class);
	}

	@Override
	public void persistDTO(TaLRelanceDTO transientInstance)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaLRelanceDTO persistentInstance)
			throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLRelanceDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaLRelanceDTO transientInstance,
			String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLRelanceDTO detachedInstance,
			String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLRelanceDTO> findWithNamedQueryDTO(String queryName)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRelanceDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRelanceDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLRelanceDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLRelanceDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaLRelanceDTO dto) {
		// TODO Auto-generated method stub
		validateDTO(dto,null);
	}

	@Override
	public void validateDTOProperty(TaLRelanceDTO dto, String propertyName)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaLRelanceDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaLRelanceDTO dto, String propertyName,
			String validationContext) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaLRelanceDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(TaLRelance transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		persist(transientInstance, null);
	}

	@Override
	public void remove(TaLRelance persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			dao.remove(findById(persistentInstance.getIdLRelance()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}

	@Override
	public TaLRelance merge(TaLRelance detachedInstance) {
		// TODO Auto-generated method stub
		return merge(detachedInstance, null);
	}

	@Override
	public void persist(TaLRelance transientInstance, String validationContext)
			throws CreateException {
		// TODO Auto-generated method stub
		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	@Override
	public TaLRelance merge(TaLRelance detachedInstance,
			String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	@Override
	public TaLRelance findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaLRelance findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findByCode(code);
	}

	@Override
	public void validateEntity(TaLRelance value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLRelance value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaLRelance value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLRelance value, String propertyName,
			String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLRelance> selectAll() {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}

	
	@Override
	protected TaLRelance refresh(TaLRelance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
