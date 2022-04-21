package fr.legrain.moncompte.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaDossierServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaSetupServiceRemote;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.moncompte.dao.ISetupDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaSetupDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.TaSetup;
import fr.legrain.moncompte.model.mapping.mapper.TaSetupMapper;


/**
 * Session Bean implementation class TaSetupBean
 */
@Stateless
//@WebService
//@DeclareRoles("admin")
public class TaSetupService extends AbstractApplicationDAOServer<TaSetup, TaSetupDTO> implements ITaSetupServiceRemote {

	static Logger logger = Logger.getLogger(TaSetupService.class);

	@Inject private ISetupDAO dao;
	@EJB private ITaClientServiceRemote taClientService;
	@EJB private ITaProduitServiceRemote taProduitService;
	@EJB private ITaDossierServiceRemote taDossierService;
	

	/**
	 * Default constructor. 
	 */
	public TaSetupService() {
		super(TaSetup.class,TaSetupDTO.class);
	}

	
/*
 * WEB SERVICE REST => Remettre les annotation JAX-RS dans 	ITaSetupServiceRemote, bien vérifier le déploiement avec compilation maven
 */
	/*
		curl http://dev.admin.moncompte.promethee.biz:8080/moncompte-admin/rest/setup/5
	 */
	public TaSetup getSetupId(String id) {
		System.out.println("TaSetupService.getSetupId() " +id +" ** "+dao.findById(LibConversion.stringToInteger(id)).getLibelle());
		
		//return Response.status(200).entity("getProductId is called, id : " + id).build();
		return dao.findById(LibConversion.stringToInteger(id));

	}
	
	//POST
	/*
	 curl -H "Content-Type: application/json" -X POST -d '{"username":"xyz","password":"xyz"}' http://localhost:3000/api/login
	 
	 curl -H "Content-Type: application/json" -X POST -d '{"libelle":"lib curl","taProduit":{"idProduit":1}}'  http://dev.admin.moncompte.promethee.biz:8080/moncompte-admin/rest/setup/create
	 
	 {"idSetup":7,"taProduit":{"idProduit":1}}
	 */
	public Response createSetup(TaSetup s) {
		System.out.println("qqqqqqqqqq : "+s.getLibelle());
		try {
			if(s.getTaProduit()!=null && s.getTaProduit().getIdProduit()!=null) {
				s.setTaProduit(taProduitService.findById(s.getTaProduit().getIdProduit()));
			}
			enregistrerMerge(s, ITaSetupServiceRemote.validationContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(200).entity("createSetup is called ").build();
	}
	
	/*
	 * Si @WebService sur le service => problème JAXB
	 * http://dev.admin.moncompte.promethee.biz:8080/moncompte-admin/rest/setup/upload
	 *POST
	 curl -v -F key1=value1 -F upload=@localfilename URL
	 curl -v -F key1=value1 -F upload=@//donnees/icon.xpm http://dev.admin.moncompte.promethee.biz:8080/moncompte-admin/rest/setup/upload
	 curl -v -F upload=@/donnees/icon.xpm http://dev.admin.moncompte.promethee.biz:8080/moncompte-admin/rest/setup/upload
	 */
	public Response uploadFile(MultipartFormDataInput input) {
		System.out.println("TaSetupService.uploadFile()");
		
		String UPLOADED_FILE_PATH="";
		String fileName = "";
		
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");

		for (InputPart inputPart : inputParts) {

		 try {

			MultivaluedMap<String, String> header = inputPart.getHeaders();
			fileName = getFileName(header);

			//convert the uploaded file to inputstream
			InputStream inputStream = inputPart.getBody(InputStream.class,null);

			byte [] bytes = IOUtils.toByteArray(inputStream);
				
			//constructs upload file path
			fileName = UPLOADED_FILE_PATH + fileName;
				
			writeFile(bytes,fileName);
				
			System.out.println("Done");

		  } catch (IOException e) {
			e.printStackTrace();
		  }

		}

		return Response.status(200)
		    .entity("uploadFile is called, Uploaded file name : " + fileName).build();
	}
	
	/**
	 * header sample
	 * {
	 * 	Content-Type=[image/png], 
	 * 	Content-Disposition=[form-data; name="file"; filename="filename.extension"]
	 * }
	 **/
	//get uploaded filename, is there a easy way in RESTEasy?
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");
				
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	//save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
	
	//	private String defaultJPQLQuery = "select a from TaSetup a";
	
	public String chargeDernierSetup(String codeClient, String codeProgramme, String versionClient) {
		try {
			TaClient c = taClientService.findByCode(codeClient);
			TaSetup dernier = null;

			if(c!=null) {
				List<TaDossier> listeDossier = taDossierService.findListeDossierClient(c.getId());
			for (TaAutorisation aut : listeDossier.get(0).getListeAutorisation()) { //A FAIRE, gérer le multidossier
				if(aut.getTaProduit().getCode().equals(codeProgramme)) { //le client possède ce programme
					if(aut.getDateFin().after(new Date())) { //la licence pour ce programme est valide
						if(aut.getTaProduit().getListeSetup()!=null && !aut.getTaProduit().getListeSetup().isEmpty()) {
							//trier par date
//							Collections.sort(aut.getTaProduit().getListeSetup(), new Comparator<TaSetup>() {
//								  public int compare(TaSetup o1, TaSetup o2) {
//								      return o1.getDateSetup().compareTo(o2.getDateSetup());
//								  }
//								});
							//Comparaison avec expression Lambda Java 8
							aut.getTaProduit().getListeSetup().sort( 
									(TaSetup p1, TaSetup p2) -> p1.getDateSetup().compareTo(p2.getDateSetup())
									);
							for (TaSetup s : aut.getTaProduit().getListeSetup()) {
								if(s.getDateSetup().before(aut.getDateFin()) && s.getDateSetup().after(aut.getDateAchat())) {
									//Le client à des autorisations valides pour ce serveur
									//if(s.getVersionProduit().equals(versionClient)) {
									if(LibConversion.stringToInteger(versionClient) < LibConversion.stringToInteger(s.getVersionProduit())) {
										//Le client a une version plus ancienne
										dernier = s;
									}
									
								}
							}
						}
					}
				}
			}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaSetup transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaSetup transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaSetup persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdSetup()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaSetup merge(TaSetup detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaSetup merge(TaSetup detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaSetup findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaSetup findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaSetup> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaSetupDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaSetupDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaSetup> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaSetupDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaSetupDTO entityToDTO(TaSetup entity) {
//		TaSetupDTO dto = new TaSetupDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaSetupMapper mapper = new TaSetupMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaSetupDTO> listEntityToListDTO(List<TaSetup> entity) {
		List<TaSetupDTO> l = new ArrayList<TaSetupDTO>();

		for (TaSetup taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaSetupDTO> selectAllDTO() {
		System.out.println("List of TaSetupDTO EJB :");
		ArrayList<TaSetupDTO> liste = new ArrayList<TaSetupDTO>();

		List<TaSetup> projects = selectAll();
		for(TaSetup project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaSetupDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaSetupDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaSetupDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaSetupDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaSetupDTO dto, String validationContext) throws EJBException {
		try {
			TaSetupMapper mapper = new TaSetupMapper();
			TaSetup entity = null;
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
	
	public void persistDTO(TaSetupDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaSetupDTO dto, String validationContext) throws CreateException {
		try {
			TaSetupMapper mapper = new TaSetupMapper();
			TaSetup entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaSetupDTO dto) throws RemoveException {
		try {
			TaSetupMapper mapper = new TaSetupMapper();
			TaSetup entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaSetup refresh(TaSetup persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaSetup value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaSetup value, String propertyName, String validationContext) {
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
	public void validateDTO(TaSetupDTO dto, String validationContext) {
		try {
			TaSetupMapper mapper = new TaSetupMapper();
			TaSetup entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaSetupDTO> validator = new BeanValidator<TaSetupDTO>(TaSetupDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaSetupDTO dto, String propertyName, String validationContext) {
		try {
			TaSetupMapper mapper = new TaSetupMapper();
			TaSetup entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaSetupDTO> validator = new BeanValidator<TaSetupDTO>(TaSetupDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaSetupDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaSetupDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaSetup value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaSetup value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
