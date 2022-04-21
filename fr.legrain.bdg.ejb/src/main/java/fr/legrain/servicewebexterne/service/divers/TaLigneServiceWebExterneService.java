package fr.legrain.servicewebexterne.service.divers;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
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
import org.hibernate.OptimisticLockException;

import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.dao.ITaLigneServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.mapper.TaLigneServiceWebExterneMapper;
import fr.legrain.servicewebexterne.model.EnteteDocExterne;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLigneServiceWebExterneService extends AbstractApplicationDAOServer<TaLigneServiceWebExterne, TaLigneServiceWebExterneDTO> implements ITaLigneServiceWebExterneServiceRemote {

	static Logger logger = Logger.getLogger(TaLigneServiceWebExterneService.class);

	@Inject private ITaLigneServiceWebExterneDAO dao;
	@EJB private ITaLiaisonServiceExterneServiceRemote liaisonService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaTiersServiceRemote taTiersService;

	/**
	 * Default constructor. 
	 */
	public TaLigneServiceWebExterneService() {
		super(TaLigneServiceWebExterne.class,TaLigneServiceWebExterneDTO.class);
	}
	
	public void persist(TaLigneServiceWebExterne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLigneServiceWebExterne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLigneServiceWebExterne persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLigneServiceWebExterne()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLigneServiceWebExterne merge(TaLigneServiceWebExterne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLigneServiceWebExterne merge(TaLigneServiceWebExterne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLigneServiceWebExterne findById(int id) throws FinderException {
		return dao.findById(id);
	}
	
	public TaLigneServiceWebExterne findByIdAvecTiers(int id) throws FinderException {
		TaLigneServiceWebExterne ligne =  dao.findById(id);
		ligne.getTaTiers().getIdTiers();
		return ligne;
	}

	public TaLigneServiceWebExterne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLigneServiceWebExterne> selectAll() {
		return dao.selectAll();
	}
	

	public TaLigneServiceWebExterne affecteLiaison(TaLigneServiceWebExterne ligne, TaServiceWebExterne service) {
		return affecteLiaison(ligne, service, null);
	}
	
	public TaLigneServiceWebExterne affecteLiaison(TaLigneServiceWebExterne ligne, TaServiceWebExterne service, List<TaLiaisonServiceExterne> listeLiaison) {
		return affecteLiaison(ligne, service, listeLiaison, true);
	}
	public TaLigneServiceWebExterne affecteLiaison(TaLigneServiceWebExterne ligne, TaServiceWebExterne service, List<TaLiaisonServiceExterne> listeLiaison, boolean merge) {
		 TaLiaisonServiceExterne liaisonArt = liaisonService.findByRefArticleAndByIdServiceWebExterne(ligne.getRefArticle(), service.getIdServiceWebExterne());

		 boolean ligneModifier = false;
		 if(liaisonArt == null) {
	    		//on parcours la liste des liaisons qui on était créer ici mais pas encore merge (pas encore en base)
				 if(listeLiaison != null) {
					 for (TaLiaisonServiceExterne li : listeLiaison) {
							if(li.getRefExterne().equals(ligne.getRefArticle())) {
								liaisonArt = li;
							}
						}
				 }
	    		 
	     }
    	 if(liaisonArt != null) {
    		 try {
				TaArticle article = taArticleService.findById(liaisonArt.getIdEntite());
				
				if(article != null) {
					ligne.setTaArticle(article);
					ligneModifier = true;
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 
    	 //ici on regarde si il existe deja une liaison pour cette référence de tiers (l'email chez shippingbo)
    	 TaLiaisonServiceExterne liaisonTiers = null;
    	 //@Transactional(value=TxType.REQUIRES_NEW)
    	 liaisonTiers = liaisonService.findByRefTiersAndByIdServiceWebExterne(ligne.getRefTiers(), service.getIdServiceWebExterne());  
    	 if(liaisonTiers == null) {
	    		//on parcours la liste des liaisons qui on était créer ici mais pas encore merge (pas encore en base)
    		 if(listeLiaison != null) {
    			 for (TaLiaisonServiceExterne li : listeLiaison) {
						if(li.getRefExterne().equals(ligne.getRefTiers())) {
							  liaisonTiers = li;
						}
				}
    		 }
	    		 
    	 }
    	 if(liaisonTiers != null) {
    		 try {
    			 //@Transactional(value=TxType.REQUIRES_NEW)
				TaTiers tiers = taTiersService.findById(liaisonTiers.getIdEntite());
				
				if(tiers != null) {
					ligne.setTaTiers(tiers);
					ligneModifier = true;
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	 if(ligneModifier && merge) {
    		 ligne = merge(ligne);
    	 }
    	 
    	 return ligne;
	}
	
	public List<String> findAllIDExterneByIdCompteService(Integer id){
		return dao.findAllIDExterneByIdCompteService(id);
	}
	
	
	
	public File exportToCSV(List<TaLigneServiceWebExterneDTO> liste) {
		SchemaResolver sr = new SchemaResolver();
		String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("ligne_service_externe.csv");
		try {
			File f = new File(localPath);
			FileWriter writer = new FileWriter(f);

			CSVWriter csvWriter = new CSVWriter(writer, ';', '\'');

//			List<String[]> data = toStringArray(l);
//			csvWriter.writeAll(data);
//			csvWriter.close();

			BeanToCsv bc = new BeanToCsv();
			CustomCSVMappingStrategy<TaLigneServiceWebExterneDTO> mappingStrategy = new CustomCSVMappingStrategy<TaLigneServiceWebExterneDTO>();
		    mappingStrategy.setType(TaLigneServiceWebExterneDTO.class);
		    String[] columns = new String[]{
		    	"idCommandeExterne",
			    "libelle",
			    "dateCreationExterne",
			    "prenomTiers",
			    "nomTiers",
			    "codeTiers",
			    "refArticle",
			    "nomArticle",
			    "libellecArticle",
				"codeArticle",
				"qteArticle",
				"numLot",
				"montantTtcTotalDoc",
				"refTypePaiement",
				"etatLigneExterne"
			};
		    String[] head = new String[]{
			    	"Num document",
				    "Libelle",
				    "Date ",
				    "Prenom ",
				    "Nom",
				    "Code tiers",
				    "Ref Article",
				    "Nom Article",
				    "Libelle Article",
					"Code Article",
					"Quantite ",
					"Num de Lot",
					"Montant TTC",
					"référence du type paiement",
					"Etat de la ligne"
				};
		    mappingStrategy.setHead(head);
		    mappingStrategy.setColumnMapping(columns);
		   // mappingStrategy.setType(TaLigneServiceWebExterneDTO.class);
		    StatefulBeanToCsvBuilder<TaLigneServiceWebExterneDTO> builder = new StatefulBeanToCsvBuilder<TaLigneServiceWebExterneDTO>(writer);
		    @SuppressWarnings("unchecked")
			StatefulBeanToCsv<TaLigneServiceWebExterneDTO> beanWriter = builder.withMappingStrategy(mappingStrategy).build();
		    beanWriter.write(liste);
		    writer.close();
		    
		    
			return f;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	////////////////////////////DTO/////////////////////////////////////////////////////////////
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermine(){
		return dao.findAllDTOTermine();
	}
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermine(){
		return dao.findAllDTONonTermine();
	}
	
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdCompteServiceWebExterne(Integer id){
		return dao.findAllDTOTermineByIdCompteServiceWebExterne(id);
	}
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdCompteServiceWebExterne(Integer id){
		return dao.findAllDTONonTermineByIdCompteServiceWebExterne(id);
	}
	
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdServiceWebExterne(Integer id){
		return dao.findAllDTOTermineByIdServiceWebExterne(id);
	}
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdServiceWebExterne(Integer id){
		return dao.findAllDTONonTermineByIdServiceWebExterne(id);
	}
	
	
	
	
	public List<TaLigneServiceWebExterneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLigneServiceWebExterneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLigneServiceWebExterne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLigneServiceWebExterneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLigneServiceWebExterneDTO entityToDTO(TaLigneServiceWebExterne entity) {
//		TaLigneServiceWebExterneDTO dto = new TaLigneServiceWebExterneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLigneServiceWebExterneMapper mapper = new TaLigneServiceWebExterneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLigneServiceWebExterneDTO> listEntityToListDTO(List<TaLigneServiceWebExterne> entity) {
		List<TaLigneServiceWebExterneDTO> l = new ArrayList<TaLigneServiceWebExterneDTO>();

		for (TaLigneServiceWebExterne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLigneServiceWebExterneDTO> selectAllDTO() {
		System.out.println("List of TaLigneServiceWebExterneDTO EJB :");
		ArrayList<TaLigneServiceWebExterneDTO> liste = new ArrayList<TaLigneServiceWebExterneDTO>();

		List<TaLigneServiceWebExterne> projects = selectAll();
		for(TaLigneServiceWebExterne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLigneServiceWebExterneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLigneServiceWebExterneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLigneServiceWebExterneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLigneServiceWebExterneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLigneServiceWebExterneDTO dto, String validationContext) throws EJBException {
		try {
			TaLigneServiceWebExterneMapper mapper = new TaLigneServiceWebExterneMapper();
			TaLigneServiceWebExterne entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaLigneServiceWebExterneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLigneServiceWebExterneDTO dto, String validationContext) throws CreateException {
		try {
			TaLigneServiceWebExterneMapper mapper = new TaLigneServiceWebExterneMapper();
			TaLigneServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLigneServiceWebExterneDTO dto) throws RemoveException {
		try {
			TaLigneServiceWebExterneMapper mapper = new TaLigneServiceWebExterneMapper();
			TaLigneServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLigneServiceWebExterne refresh(TaLigneServiceWebExterne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLigneServiceWebExterne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLigneServiceWebExterne value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLigneServiceWebExterneDTO dto, String validationContext) {
		try {
			TaLigneServiceWebExterneMapper mapper = new TaLigneServiceWebExterneMapper();
			TaLigneServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLigneServiceWebExterneDTO> validator = new BeanValidator<TaLigneServiceWebExterneDTO>(TaLigneServiceWebExterneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLigneServiceWebExterneDTO dto, String propertyName, String validationContext) {
		try {
			TaLigneServiceWebExterneMapper mapper = new TaLigneServiceWebExterneMapper();
			TaLigneServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLigneServiceWebExterneDTO> validator = new BeanValidator<TaLigneServiceWebExterneDTO>(TaLigneServiceWebExterneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLigneServiceWebExterneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLigneServiceWebExterneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLigneServiceWebExterne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLigneServiceWebExterne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	
	
	
	
}
