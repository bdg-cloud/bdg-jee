package fr.legrain.documents.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.documents.dao.ILigneALigneDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLigneALigneService extends AbstractApplicationDAOServer<TaLigneALigne, TaLigneALigneDTO> implements ITaLigneALigneServiceRemote {

	static Logger logger = Logger.getLogger(TaLigneALigneService.class);

	@Inject private ILigneALigneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLigneALigneService() {
		super(TaLigneALigne.class,TaLigneALigneDTO.class);
	}

	@Override
	public void persistDTO(TaLigneALigneDTO transientInstance)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDTO(TaLigneALigneDTO persistentInstance)
			throws RemoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLigneALigneDTO detachedInstance) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistDTO(TaLigneALigneDTO transientInstance,
			String validationContext) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mergeDTO(TaLigneALigneDTO detachedInstance,
			String validationContext) throws EJBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLigneALigneDTO> findWithNamedQueryDTO(String queryName)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneDTO> findWithJPQLQueryDTO(String JPQLquery)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigneDTO> selectAllDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLigneALigneDTO findByIdDTO(int id) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLigneALigneDTO findByCodeDTO(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateDTO(TaLigneALigneDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaLigneALigneDTO dto, String propertyName)
			throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTO(TaLigneALigneDTO dto, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDTOProperty(TaLigneALigneDTO dto, String propertyName,
			String validationContext) throws BusinessValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(TaLigneALigneDTO dto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(TaLigneALigne transientInstance) throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(TaLigneALigne persistentInstance) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			dao.remove(merge(persistentInstance));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TaLigneALigne merge(TaLigneALigne detachedInstance) {
		// TODO Auto-generated method stub
		detachedInstance= 	dao.merge(detachedInstance);
		return dao.findById(detachedInstance.getId());
	}

	@Override
	public void persist(TaLigneALigne transientInstance, String validationContext)
			throws CreateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaLigneALigne merge(TaLigneALigne detachedInstance,
			String validationContext) {
		// TODO Auto-generated method stub
		validateEntity(detachedInstance, validationContext);
		return merge(detachedInstance);
	}

	@Override
	public TaLigneALigne findById(int id) throws FinderException {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public TaLigneALigne findByCode(String code) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaLigneALigne value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLigneALigne value, String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntity(TaLigneALigne value, String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateEntityProperty(TaLigneALigne value, String propertyName,
			String validationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLigneALigne> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> rechercheDocument(Date dateDeb, Date dateFin) {
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
	public List<TaLigneALigne> rechercheDocument(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaLigneALigne> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected TaLigneALigne refresh(TaLigneALigne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaLigneALigne findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaLigneALigne> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers, Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeArticle, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeArticle, Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genereCode(Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void annuleCode(String code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verrouilleCode(String code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaLigneALigne> dejaGenere(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenere(requete);
	}

	@Override
	public List<TaLigneALigne> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLigneALigne> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IDocumentTiers> dejaGenereDocument(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenereDocument(requete);
	}

	@Override
	public List<TaLigneALigne> selectAll(IDocumentTiers taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}
	
	public List<TaLigneALigne> selectAll(ILigneDocumentTiers taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectAll(taDocument, dateDeb, dateFin);
	}

	@Override
	public List<TaLigneALigneDTO> dejaGenereLigneDocument(String requete) {
		// TODO Auto-generated method stub
		return dao.dejaGenereLigneDocument(requete);
	}

	@Override
	public List<TaLigneALigne> findByLDocument(ILigneDocumentTiers ligneDocument) {
		// TODO Auto-generated method stub
		return dao.findByLDocument(ligneDocument);
	}
	
	@Override
	public List<TaLigneALigne> findByLDocument(TaLFlash ligneDocument) {
		// TODO Auto-generated method stub
		return dao.findByLDocument(ligneDocument);
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<IDocumentDTO> recupIdDocumentSrc() {
		// TODO Auto-generated method stub
		return dao.recupIdDocumentSrc();
	}

//	@Override
//	public List<TaLigneALigneDTO> recupListeLienLigneALigneDTO(IDocumentTiers persistentInstance) {
//		// TODO Auto-generated method stub
//		return dao.recupListeLienLigneALigneInt(persistentInstance);
//	}
	
}
