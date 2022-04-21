package fr.legrain.documents.service;

import java.util.Date;
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

import fr.legrain.bdg.documents.service.remote.ITaRAvoirServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaRAvoirDTO;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.documents.dao.IRAvoirDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRAvoirService extends AbstractApplicationDAOServer<TaRAvoir, TaRAvoirDTO> implements ITaRAvoirServiceRemote {

	static Logger logger = Logger.getLogger(TaRAvoirService.class);

	@Inject private IRAvoirDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRAvoirService() {
		super(TaRAvoir.class,TaRAvoirDTO.class);
	}

	@Override
	public void persistDTO(TaRAvoirDTO transientInstance)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaRAvoirDTO persistentInstance)
			throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaRAvoirDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaRAvoirDTO transientInstance,
			String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaRAvoirDTO detachedInstance,
			String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaRAvoirDTO> findWithNamedQueryDTO(String queryName)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoirDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoirDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRAvoirDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRAvoirDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaRAvoirDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaRAvoirDTO dto, String propertyName)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaRAvoirDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaRAvoirDTO dto, String propertyName,
			String validationContext) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaRAvoirDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(TaRAvoir transientInstance) throws CreateException {
		// TODO Auto-generated method stub

		dao.persist(transientInstance);
	}

	@Override
	public void remove(TaRAvoir persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}

	@Override
	public TaRAvoir merge(TaRAvoir detachedInstance) {
		// TODO Auto-generated method stub
		validateEntity(detachedInstance);

		return dao.merge(detachedInstance);
	}

	@Override
	public void persist(TaRAvoir transientInstance, String validationContext)
			throws CreateException {
		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
		
	}

	@Override
	public TaRAvoir merge(TaRAvoir detachedInstance,
			String validationContext) {
		// TODO Auto-generated method stub
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	@Override
	public TaRAvoir findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaRAvoir findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findByCode(code);
	}

	@Override
	public void validateEntity(TaRAvoir value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaRAvoir value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaRAvoir value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaRAvoir value, String propertyName,
			String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaRAvoir> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocument(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocument(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> selectAll(IDocumentTiers taDocument,
			Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TaRAvoir refresh(TaRAvoir persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> selectAll(TaTiers taTiers, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRAvoir findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaRAvoir> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRAvoir> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
