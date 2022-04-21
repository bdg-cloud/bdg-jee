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

import fr.legrain.abonnement.dao.jpa.IFrequenceDAO;
import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.abonnement.service.remote.ITaFrequenceServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaFrequenceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaFrequenceService extends AbstractApplicationDAOServer<TaFrequence, TaFrequenceDTO> implements ITaFrequenceServiceRemote {
	static Logger logger = Logger.getLogger(TaFrequenceService.class);

	@Inject private IFrequenceDAO dao;
	
	/**
	 * Default constructor. 
	 */
	public TaFrequenceService() {
		super(TaFrequence.class,TaFrequenceDTO.class);
	}

	@Override
	public List<TaFrequence> selectAll() {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}

	public void persist(TaFrequence transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFrequence transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFrequence persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdFrequence()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaFrequence merge(TaFrequence detachedInstance) {
		return merge(detachedInstance, null);
	}
	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFrequence merge(TaFrequence detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}



	@Override
	public TaFrequence findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaFrequence findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findByCode(code);
	}

	@Override
	public void validateEntity(TaFrequence value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaFrequence value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaFrequence value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaFrequence value, String propertyName, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaFrequenceDTO transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaFrequenceDTO persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaFrequenceDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaFrequenceDTO transientInstance, String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaFrequenceDTO detachedInstance, String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaFrequenceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaFrequenceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaFrequenceDTO> selectAllDTO() {
		System.out.println("List of TaFrequenceDTO EJB :");
		ArrayList<TaFrequenceDTO> liste = new ArrayList<TaFrequenceDTO>();

		List<TaFrequence> projects = selectAll();
		for(TaFrequence project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}
	
	public TaFrequenceDTO entityToDTO(TaFrequence entity) {
//		TaFamilleDTO dto = new TaFamilleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaFrequenceMapper mapper = new TaFrequenceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	@Override
	public TaFrequenceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	@Override
	public TaFrequenceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void validateDTO(TaFrequenceDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaFrequenceDTO dto, String propertyName) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaFrequenceDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaFrequenceDTO dto, String propertyName, String validationContext)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaFrequenceDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected TaFrequence refresh(TaFrequence persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaFrequenceDTO> findByCodeLight(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodeLight(code);
	}

	@Override
	public List<TaFrequenceDTO> findAllLight() {
		// TODO Auto-generated method stub
		return dao.findAllLight();
	}
}
