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

import fr.legrain.bdg.documents.service.remote.ITaLRemiseServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLRemiseDTO;
import fr.legrain.document.model.TaLRemise;
import fr.legrain.documents.dao.ILRemiseDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLRemiseService extends AbstractApplicationDAOServer<TaLRemise, TaLRemiseDTO> implements ITaLRemiseServiceRemote {

	static Logger logger = Logger.getLogger(TaLRemiseService.class);

	@Inject private ILRemiseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLRemiseService() {
		super(TaLRemise.class,TaLRemiseDTO.class);
	}

	@Override
	public void persistDTO(TaLRemiseDTO transientInstance)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaLRemiseDTO persistentInstance)
			throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLRemiseDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaLRemiseDTO transientInstance,
			String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLRemiseDTO detachedInstance,
			String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLRemiseDTO> findWithNamedQueryDTO(String queryName)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemiseDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemiseDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLRemiseDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLRemiseDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaLRemiseDTO dto) {
		// TODO Auto-generated method stub
		validateDTO(dto,null);
	}

	@Override
	public void validateDTOProperty(TaLRemiseDTO dto, String propertyName)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaLRemiseDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaLRemiseDTO dto, String propertyName,
			String validationContext) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaLRemiseDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(TaLRemise transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		persist(transientInstance, null);
	}

	@Override
	public void remove(TaLRemise persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			dao.remove(findById(persistentInstance.getIdLDocument()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}

	@Override
	public TaLRemise merge(TaLRemise detachedInstance) {
		// TODO Auto-generated method stub
		return merge(detachedInstance, null);
	}

	@Override
	public void persist(TaLRemise transientInstance, String validationContext)
			throws CreateException {
		// TODO Auto-generated method stub
		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	@Override
	public TaLRemise merge(TaLRemise detachedInstance,
			String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	@Override
	public TaLRemise findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaLRemise findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findByCode(code);
	}

	@Override
	public void validateEntity(TaLRemise value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLRemise value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaLRemise value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLRemise value, String propertyName,
			String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLRemise> selectAll() {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}

	@Override
	public List<TaLRemise> rechercheDocument(Date dateDeb, Date dateFin) {
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
	public List<TaLRemise> rechercheDocument(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemise> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaLRemise> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemise> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemise> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemise> selectAll(IDocumentTiers taDocument,
			Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TaLRemise refresh(TaLRemise persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TaLRemise findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaLRemiseDTO> RechercheLigneRemiseDTO(String codeRemise) {
		// TODO Auto-generated method stub
		return dao.RechercheLigneRemiseDTO(codeRemise);
	}

	@Override
	public List<TaLRemise> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemise> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLRemise> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	
}
