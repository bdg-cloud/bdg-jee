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

import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.documents.dao.IRReglementDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRReglementService extends AbstractApplicationDAOServer<TaRReglement, TaRReglementDTO> implements ITaRReglementServiceRemote {

	static Logger logger = Logger.getLogger(TaRReglementService.class);

	@Inject private IRReglementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRReglementService() {
		super(TaRReglement.class,TaRReglementDTO.class);
	}

	@Override
	public void persistDTO(TaRReglementDTO transientInstance)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaRReglementDTO persistentInstance)
			throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaRReglementDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaRReglementDTO transientInstance,
			String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaRReglementDTO detachedInstance,
			String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaRReglementDTO> findWithNamedQueryDTO(String queryName)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglementDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglementDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRReglementDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaRReglementDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaRReglementDTO dto) {
		// TODO Auto-generated method stub
		validateDTO(dto,null);
	}

	@Override
	public void validateDTOProperty(TaRReglementDTO dto, String propertyName)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaRReglementDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaRReglementDTO dto, String propertyName,
			String validationContext) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaRReglementDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(TaRReglement transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		persist(transientInstance, null);
	}

	@Override
	public void remove(TaRReglement persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}

	@Override
	public TaRReglement merge(TaRReglement detachedInstance) {
		// TODO Auto-generated method stub
		return merge(detachedInstance, null);
	}

	@Override
	public void persist(TaRReglement transientInstance, String validationContext)
			throws CreateException {
		// TODO Auto-generated method stub
		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	@Override
	public TaRReglement merge(TaRReglement detachedInstance,
			String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	@Override
	public TaRReglement findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaRReglement findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findByCode(code);
	}

	@Override
	public void validateEntity(TaRReglement value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaRReglement value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaRReglement value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaRReglement value, String propertyName,
			String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaRReglement> selectAll() {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}

	@Override
	public List<TaRReglement> rechercheDocument(Date dateDeb, Date dateFin) {
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
	public List<TaRReglement> rechercheDocument(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaRReglement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> selectAll(IDocumentTiers taDocument,
			Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TaRReglement refresh(TaRReglement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> selectAll(TaTiers taTiers, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectAll(taTiers,dateDeb,dateFin);
	}

	@Override
	public TaRReglement findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaRReglementDTO> rechercheDocumentDTO(String codeDoc, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(codeDoc, codeTiers);
	}

	@Override
	public List<TaRReglement> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRReglement> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	
}
