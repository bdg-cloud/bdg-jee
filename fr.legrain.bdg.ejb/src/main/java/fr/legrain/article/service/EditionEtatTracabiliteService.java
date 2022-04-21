package fr.legrain.article.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.IEditionEtatTracabiliteDAO;
import fr.legrain.article.dto.EditionEtatTracabiliteDTO;
import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.bdg.article.service.remote.IEditionEtatTracabiliteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.EditionEtatTracabiliteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;

/** Créé par Dima **/

@Stateless 
public class EditionEtatTracabiliteService extends AbstractApplicationDAOServer<EditionEtatTracabilite, EditionEtatTracabiliteDTO> implements IEditionEtatTracabiliteServiceRemote { 

	static Logger logger = Logger.getLogger(EditionEtatTracabiliteService.class);

	@Inject private IEditionEtatTracabiliteDAO dao;
    @EJB private ITaGenCodeExServiceRemote gencode;
	/**
	 * Default constructor. 
	 */
	public EditionEtatTracabiliteService() {
		super(EditionEtatTracabilite.class,EditionEtatTracabiliteDTO.class);
	}
	
	public String genereCode() {

		return null;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(EditionEtatTracabilite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	@Override
	public void persist(EditionEtatTracabilite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	@Override
	public void remove(EditionEtatTracabilite persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	@Override
	public EditionEtatTracabilite merge(EditionEtatTracabilite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	public EditionEtatTracabilite merge(EditionEtatTracabilite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	@Override
	public EditionEtatTracabilite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public EditionEtatTracabilite findByIdWithList(int id) throws FinderException {
		EditionEtatTracabilite fab =  dao.findById(id);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}

	public EditionEtatTracabilite findByCodeWithList(String code)   {
		EditionEtatTracabilite fab =  dao.findByCode(code);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}
	public EditionEtatTracabilite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

	@Override
	public List<EditionEtatTracabilite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public List<EditionEtatTracabiliteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<EditionEtatTracabiliteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<EditionEtatTracabilite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<EditionEtatTracabiliteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public EditionEtatTracabiliteDTO entityToDTO(EditionEtatTracabilite entity)  {
//		EditionEtatTracabiliteDTO dto = new EditionEtatTracabiliteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;

		EditionEtatTracabiliteMapper mapper = new EditionEtatTracabiliteMapper();
		EditionEtatTracabiliteDTO dto = new EditionEtatTracabiliteDTO();
		dto = mapper.mapEntityToDto(entity, null);
		
		
		
		return dto;
	}

	public List<EditionEtatTracabiliteDTO> listEntityToListDTO(List<EditionEtatTracabilite> entity) {
		List<EditionEtatTracabiliteDTO> l = new ArrayList<EditionEtatTracabiliteDTO>();

		for (EditionEtatTracabilite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

	@Override
	public List<EditionEtatTracabiliteDTO> selectAllDTO() {
		System.out.println("List of EditionEtatTracabiliteDTO EJB :");
		ArrayList<EditionEtatTracabiliteDTO> liste = new ArrayList<EditionEtatTracabiliteDTO>();

		List<EditionEtatTracabilite> projects = selectAll();
		for(EditionEtatTracabilite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	@Override
	public EditionEtatTracabiliteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	@Override
	public EditionEtatTracabiliteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(EditionEtatTracabiliteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	@Override
	public void mergeDTO(EditionEtatTracabiliteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	public void mergeDTO(EditionEtatTracabiliteDTO dto, String validationContext) throws EJBException {
		try {
			EditionEtatTracabiliteMapper mapper = new EditionEtatTracabiliteMapper();
			EditionEtatTracabilite entity = null;
//			if(dto.getId()!=null) {
//				entity = dao.findById(dto.getId());
//				if(dto.getVersionObj()!=entity.getVersionObj()) {
//					throw new OptimisticLockException(entity,
//							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
//				} else {
//					 entity = mapper.mapDtoToEntity(dto,entity);
//				}
//			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	@Override
	public void persistDTO(EditionEtatTracabiliteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	public void persistDTO(EditionEtatTracabiliteDTO dto, String validationContext) throws CreateException {
		try {
			EditionEtatTracabiliteMapper mapper = new EditionEtatTracabiliteMapper();
			EditionEtatTracabilite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(EditionEtatTracabiliteDTO dto) throws RemoveException {
		try {
			EditionEtatTracabiliteMapper mapper = new EditionEtatTracabiliteMapper();
			EditionEtatTracabilite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected EditionEtatTracabilite refresh(EditionEtatTracabilite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(EditionEtatTracabilite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(EditionEtatTracabilite value, String propertyName, String validationContext) {
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
	public void validateDTO(EditionEtatTracabiliteDTO dto, String validationContext) {
		try {
			EditionEtatTracabiliteMapper mapper = new EditionEtatTracabiliteMapper();
			EditionEtatTracabilite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<EditionEtatTracabiliteDTO> validator = new BeanValidator<EditionEtatTracabiliteDTO>(EditionEtatTracabiliteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	public void validateDTOProperty(EditionEtatTracabiliteDTO dto, String propertyName, String validationContext) {
		try {
			EditionEtatTracabiliteMapper mapper = new EditionEtatTracabiliteMapper();
			EditionEtatTracabilite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<EditionEtatTracabiliteDTO> validator = new BeanValidator<EditionEtatTracabiliteDTO>(EditionEtatTracabiliteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	public void validateDTO(EditionEtatTracabiliteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	public void validateDTOProperty(EditionEtatTracabiliteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	public void validateEntity(EditionEtatTracabilite value) {
		validateEntity(value,null);
	}

	@Override
	public void validateEntityProperty(EditionEtatTracabilite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	public List <EditionEtatTracabilite> editionPFNL(String numLotPF){
		return dao.editionPFNL(numLotPF);
	}

	@Override
	public List<EditionEtatTracabilite> editionPFL(String libellePF) {
		// TODO Auto-generated method stub
		return dao.editionPFL(libellePF);
	}
	
	public List<EditionEtatTracabilite> editionDMA(String codeArticle, Date dateDebut, Date dateFin){
		return dao.editionDMA(codeArticle, dateDebut, dateFin);
	}
	
	public List<EditionEtatTracabilite> editionDML(String mouvementLot, Date dateDebut, Date dateFin){
		return dao.editionDML(mouvementLot, dateDebut, dateFin);
	}
	
	public List<EditionEtatTracabilite> editionCA(String codeArticle){
		return dao.editionCA(codeArticle);
	}
	
	public List<EditionEtatTracabilite> editionSG(Date dateD, Date dateF, String codeArticle,String familleArticle,String codeEntrepot){
		return dao.editionSG(dateD, dateF,  codeArticle, familleArticle, codeEntrepot);
	}
	


	@Override
	public List<EditionEtatTracabilite> editionCADate(String codeArticle,
			Date dateDebut, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.editionCADate(codeArticle, dateDebut, dateFin);
	}

	@Override
	public List<EditionEtatTracabilite> editionNonConforme(Date dateD,
			Date dateF) {
		// TODO Auto-generated method stub
		return  dao.editionNonConforme(dateD , dateF );
	}

	@Override
	public List<EditionEtatTracabilite> editionDMAtous(String codeArticle,
			Date dateD, Date dateF) {
		return dao.editionDMAtous(codeArticle, dateD, dateF);
	}
}
