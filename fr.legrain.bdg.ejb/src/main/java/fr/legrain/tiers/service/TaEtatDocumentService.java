package fr.legrain.tiers.service;

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
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaEtatDocumentMapper;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaEtatDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.documents.dao.IEtatDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

/**
 * Session Bean implementation class TaEtatDocumentBean
*/
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEtatDocumentService extends AbstractApplicationDAOServer<TaEtat, TaEtatDTO> implements ITaEtatServiceRemote {
		
	@Inject EtatDocument etatDocument;

		static Logger logger = Logger.getLogger(TaEtatDocumentService.class);
//		TaEtat documentAbandonne;
//		TaEtat documentBrouillon;
//		TaEtat documentCommercial;
//		TaEtat documentEncours;
//		TaEtat documentPartiellementAbandonne;
//		TaEtat documentPartiellementTransforme;
//		TaEtat documentPreparation;
//		TaEtat documentTermine;
//		TaEtat documentTerminePartiellementAbandonne;
//		TaEtat documentTotalementTransforme;

		@Inject private IEtatDAO dao;

		/**
		 * Default constructor. 
		 */
		public TaEtatDocumentService() {
			super(TaEtat.class,TaEtatDTO.class);
		}
		
		//	private String defaultJPQLQuery = "select a from TaEtatDocument a";

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 										ENTITY
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public void persist(TaEtat transientInstance) throws CreateException {
			persist(transientInstance, null);
		}

		//@TransactionAttribute(TransactionAttributeType.REQUIRED)
		@WebMethod(operationName = "persistValidationContext")
		public void persist(TaEtat transientInstance, String validationContext) throws CreateException {

			validateEntity(transientInstance, validationContext);
	transientInstance.setCodeEtat(transientInstance.getCodeEtat().toUpperCase());
			dao.persist(transientInstance);
		}

		public void remove(TaEtat persistentInstance) throws RemoveException {
			try {
				dao.remove(findById(persistentInstance.getIdEtat()));
			} catch (FinderException e) {
				logger.error("", e);
			}
		}
		
		public TaEtat merge(TaEtat detachedInstance) {
			return merge(detachedInstance, null);
		}

		@Override
		@WebMethod(operationName = "mergeValidationContext")
		public TaEtat merge(TaEtat detachedInstance, String validationContext) {
			validateEntity(detachedInstance, validationContext);
	detachedInstance.setCodeEtat(detachedInstance.getCodeEtat().toUpperCase());
			return dao.merge(detachedInstance);
		}

		public TaEtat findById(int id) throws FinderException {
			return dao.findById(id);
		}

		public TaEtat findByCode(String code) throws FinderException {
			return dao.findByCode(code);
		}

//		@RolesAllowed("admin")
		public List<TaEtat> selectAll() {
			return dao.selectAll();
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 										DTO
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public List<TaEtatDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
			return null;
		}

		@Override
		public List<TaEtatDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
			List<TaEtat> entityList = dao.findWithJPQLQuery(JPQLquery);
			List<TaEtatDTO> l = null;
			if(entityList!=null) {
				l = listEntityToListDTO(entityList);
			}
			return l;
		}

		public TaEtatDTO entityToDTO(TaEtat entity) {
//			TaEtatDTO dto = new TaEtatDTO();
//			dto.setId(entity.getIdTCivilite());
//			dto.setCodeTCivilite(entity.getCodeTCivilite());
//			return dto;
			TaEtatDocumentMapper mapper = new TaEtatDocumentMapper();
			return mapper.mapEntityToDto(entity, null);
		}

		public List<TaEtatDTO> listEntityToListDTO(List<TaEtat> entity) {
			List<TaEtatDTO> l = new ArrayList<TaEtatDTO>();

			for (TaEtat taTCivilite : entity) {
				l.add(entityToDTO(taTCivilite));
			}

			return l;
		}

//		@RolesAllowed("admin")
		public List<TaEtatDTO> selectAllDTO() {
			System.out.println("List of TaEtatDTO EJB :");
			ArrayList<TaEtatDTO> liste = new ArrayList<TaEtatDTO>();

			List<TaEtat> projects = selectAll();
			for(TaEtat project : projects) {
				liste.add(entityToDTO(project));
			}

			return liste;
		}

		public TaEtatDTO findByIdDTO(int id) throws FinderException {
			return entityToDTO(findById(id));
		}

		public TaEtatDTO findByCodeDTO(String code) throws FinderException {
			return entityToDTO(findByCode(code));
		}

		@Override
		public void error(TaEtatDTO dto) {
			throw new EJBException("Test erreur EJB");
		}

		@Override
		public int selectCount() {
			return dao.selectAll().size();
			//return 0;
		}
		
		public void mergeDTO(TaEtatDTO dto) throws EJBException {
			mergeDTO(dto, null);
		}

		@Override
		@WebMethod(operationName = "mergeDTOValidationContext")
		public void mergeDTO(TaEtatDTO dto, String validationContext) throws EJBException {
			try {
				TaEtatDocumentMapper mapper = new TaEtatDocumentMapper();
				TaEtat entity = null;
				if(dto.getIdEtat()!=null) {
					entity = dao.findById(dto.getIdEtat());
					if(dto.getVersionObj()!=entity.getVersionObj()) {
						throw new OptimisticLockException(entity,
								"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdEtat()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
		
		public void persistDTO(TaEtatDTO dto) throws CreateException {
			persistDTO(dto, null);
		}

		@Override
		@WebMethod(operationName = "persistDTOValidationContext")
		public void persistDTO(TaEtatDTO dto, String validationContext) throws CreateException {
			try {
				TaEtatDocumentMapper mapper = new TaEtatDocumentMapper();
				TaEtat entity = mapper.mapDtoToEntity(dto,null);
				//dao.persist(entity);
				enregistrerPersist(entity, validationContext);
			} catch (Exception e) {
				e.printStackTrace();
				throw new CreateException(e.getMessage());
			}
		}

		@Override
		public void removeDTO(TaEtatDTO dto) throws RemoveException {
			try {
				TaEtatDocumentMapper mapper = new TaEtatDocumentMapper();
				TaEtat entity = mapper.mapDtoToEntity(dto,null);
				//dao.remove(entity);
				supprimer(entity);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RemoveException(e.getMessage());
			}
		}

		@Override
		protected TaEtat refresh(TaEtat persistentInstance) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		@WebMethod(operationName = "validateEntityValidationContext")
		public void validateEntity(TaEtat value, String validationContext) /*throws ExceptLgr*/ {
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
		public void validateEntityProperty(TaEtat value, String propertyName, String validationContext) {
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
		public void validateDTO(TaEtatDTO dto, String validationContext) {
			try {
				TaEtatDocumentMapper mapper = new TaEtatDocumentMapper();
				TaEtat entity = mapper.mapDtoToEntity(dto,null);
				validateEntity(entity,validationContext);
				
				//validation automatique via la JSR bean validation
//				BeanValidator<TaEtatDTO> validator = new BeanValidator<TaEtatDTO>(TaEtatDTO.class);
//				validator.validate(dto);
			} catch (Exception e) {
				e.printStackTrace();
				throw new EJBException(e.getMessage());
			}
		}

		@Override
		@WebMethod(operationName = "validateDTOPropertyValidationContext")
		public void validateDTOProperty(TaEtatDTO dto, String propertyName, String validationContext) {
			try {
				TaEtatDocumentMapper mapper = new TaEtatDocumentMapper();
				TaEtat entity = mapper.mapDtoToEntity(dto,null);
				validateEntityProperty(entity,propertyName,validationContext);
				
				//validation automatique via la JSR bean validation
//				BeanValidator<TaEtatDTO> validator = new BeanValidator<TaEtatDTO>(TaEtatDTO.class);
//				validator.validateField(dto,propertyName);
			} catch (Exception e) {
				e.printStackTrace();
				throw new EJBException(e.getMessage());
			}

		}
		
		@Override
		@WebMethod(operationName = "validateDTO")
		public void validateDTO(TaEtatDTO dto) {
			validateDTO(dto,null);
			
		}

		@Override
		@WebMethod(operationName = "validateDTOProperty")
		public void validateDTOProperty(TaEtatDTO dto, String propertyName) {
			validateDTOProperty(dto,propertyName,null);
			
		}

		@Override
		@WebMethod(operationName = "validateEntity")
		public void validateEntity(TaEtat value) {
			validateEntity(value,null);
		}

		@Override
		@WebMethod(operationName = "validateEntityProperty")
		public void validateEntityProperty(TaEtat value, String propertyName) {
			validateEntityProperty(value,propertyName,null);
			
		}



		/**
		 * renvoi la liste des états par rapport à un identifiant
		 */		
		@Override
		public List<TaEtat> findByIdentifiantAndTypeEtat(String identifiant, String typeEtat) {
			// TODO Auto-generated method stub
			return dao.findByIdentifiantAndTypeEtat(identifiant, typeEtat);
		}
		
		

		@Override
		public List<TaEtat> listeDocumentTermine(String doc){			
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_TERMINE, null);
		}
		
		@Override
		public List<TaEtat> listeDocumentBrouillon(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_BROUILLON, null);
		}

		@Override
		public List<TaEtat> listeDocumentCommercial(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_COMMERCIAL, null);
		}
		@Override
		public List<TaEtat> listeDocumentPreparation(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_PREPARATION, null);
		}
		@Override
		public List<TaEtat> listeDocumentEncours(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_ENCOURS, null);
		}
		@Override
		public List<TaEtat> listeDocumentAbandonne(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_ABANDONNE, null);
		}
		@Override
		public List<TaEtat> listeDocumentPartiellementTransforme(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME, null);
		}
		@Override
		public List<TaEtat> listeDocumentTotalementTransforme(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME, null);
		}
		@Override
		public List<TaEtat> listeDocumentPartiellementAbandonne(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE, null);
		}
		@Override
		public List<TaEtat> listeDocumentTerminePartiellementAbandonne(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE, null);
		}

		@Override
		public List<TaEtat> listeDocumentSuspendu(String doc){
			return dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_SUSPENDU, null);
		}

		@Override
		public TaEtat documentTermine(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_TERMINE);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_TERMINE, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}

		@Override
		public TaEtat documentBrouillon(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_BROUILLON);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_BROUILLON, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}

		@Override
		public TaEtat documentCommercial(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_COMMERCIAL);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_COMMERCIAL, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public TaEtat documentPreparation(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_PREPARATION);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_PREPARATION, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public TaEtat documentEncours(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_ENCOURS);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_ENCOURS, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public TaEtat documentAbandonne(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_ABANDONNE);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_ABANDONNE, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public TaEtat documentPartiellementTransforme(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public TaEtat documentTotalementTransforme(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public TaEtat documentPartiellementAbandonne(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public TaEtat documentTerminePartiellementAbandonne(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}

		@Override
		public TaEtat documentSuspendu(String doc){
			TaEtat o=etatDocument.getMapEtat().get(TaEtat.DOCUMENT_SUSPENDU);
			if(o!=null)return o;
			List<TaEtat> l= dao.findByIdentifiantAndTypeEtat(TaEtat.DOCUMENT_SUSPENDU, null);
			if(l!=null && l.size()>0) {
				o=l.get(0);
			}
			return o; 
		}
		@Override
		public void razEtatDocument() {
			etatDocument.mapEtat.clear();
		}
}
