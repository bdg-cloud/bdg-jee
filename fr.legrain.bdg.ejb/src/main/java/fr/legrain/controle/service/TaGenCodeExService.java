package fr.legrain.controle.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.model.mapping.mapper.TaGenCodeExMapper;
import fr.legrain.controle.dao.IGenCodeExDAO;
import fr.legrain.controle.dao.IVerrouCodeGenereDAO;
import fr.legrain.controle.dto.TaGenCodeExDTO;
import fr.legrain.controle.model.TaGenCodeEx;
import fr.legrain.controle.model.TaVerrouCodeGenere;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;

/**
 * Session Bean implementation class TaGenCodeExBean
 */

//@Stateless
@Singleton
//@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaGenCodeExService extends AbstractApplicationDAOServer<TaGenCodeEx, TaGenCodeExDTO> implements ITaGenCodeExServiceRemote {

	static Logger logger = Logger.getLogger(TaGenCodeExService.class);

	@Inject private IGenCodeExDAO dao;
	@Inject private ITaInfoEntrepriseServiceLocal entrepriseService;
	@Inject private IVerrouCodeGenereDAO daoVerrouCodeGenere;
	//@EJB private ITaVerrouCodeGenereServiceRemote taVerrouCodeGenereService;
	@Inject private	SessionInfo sessionInfo;

	/**
	 * Default constructor. 
	 */
	public TaGenCodeExService() {
		super(TaGenCodeEx.class,TaGenCodeExDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaGenCodeExEx a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaGenCodeEx transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaGenCodeEx transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaGenCodeEx persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaGenCodeEx merge(TaGenCodeEx detachedInstance) {
		return merge(detachedInstance,null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaGenCodeEx merge(TaGenCodeEx detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaGenCodeEx findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaGenCodeEx findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaGenCodeEx> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaGenCodeExDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaGenCodeExDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaGenCodeEx> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaGenCodeExDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaGenCodeExDTO entityToDTO(TaGenCodeEx entity) {
		TaGenCodeExMapper mapper = new TaGenCodeExMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaGenCodeExDTO> listEntityToListDTO(List<TaGenCodeEx> entity) {
		List<TaGenCodeExDTO> l = new ArrayList<TaGenCodeExDTO>();

		for (TaGenCodeEx taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaGenCodeExDTO> selectAllDTO() {
		System.out.println("List of TaGenCodeExDTO EJB :");
		ArrayList<TaGenCodeExDTO> liste = new ArrayList<TaGenCodeExDTO>();

		List<TaGenCodeEx> projects = selectAll();
		for(TaGenCodeEx project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaGenCodeExDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaGenCodeExDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaGenCodeExDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaGenCodeExDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaGenCodeExDTO dto, String validationContext) throws EJBException {
		try {
			TaGenCodeExMapper mapper = new TaGenCodeExMapper();
			TaGenCodeEx entity = null;
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
	
	public void persistDTO(TaGenCodeExDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaGenCodeExDTO dto, String validationContext) throws CreateException {
		try {
			TaGenCodeExMapper mapper = new TaGenCodeExMapper();
			TaGenCodeEx entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaGenCodeExDTO dto) throws RemoveException {
		try {
			TaGenCodeExMapper mapper = new TaGenCodeExMapper();
			TaGenCodeEx entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaGenCodeEx refresh(TaGenCodeEx persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaGenCodeEx value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaGenCodeEx value, String propertyName, String validationContext) {
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
	public void validateDTO(TaGenCodeExDTO dto, String validationContext) {
		try {
			TaGenCodeExMapper mapper = new TaGenCodeExMapper();
			TaGenCodeEx entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGenCodeExDTO> validator = new BeanValidator<TaGenCodeExDTO>(TaGenCodeExDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	public void validateDTOProperty(TaGenCodeExDTO dto, String propertyName, String validationContext) {
		try {
			TaGenCodeExMapper mapper = new TaGenCodeExMapper();
			TaGenCodeEx entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGenCodeExDTO> validator = new BeanValidator<TaGenCodeExDTO>(TaGenCodeExDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}

	@Override
	public void validateDTO(TaGenCodeExDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	public void validateDTOProperty(TaGenCodeExDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	public void validateEntity(TaGenCodeEx value) {
		validateEntity(value,null);
	}

	@Override
	public void validateEntityProperty(TaGenCodeEx value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public String genereCodeExJPA(TaGenCodeEx genCodeEx, int rajoutCompteur, String section,
			String exo) throws Exception {
		// TODO Auto-generated method stub
		return dao.genereCodeExJPA(genCodeEx,rajoutCompteur,section, exo);
	}

	@Override
	public String genereCodeExJPA(int rajoutCompteur, String section, String exo) throws Exception {
		// TODO Auto-generated method stub
		return dao.genereCodeExJPA(rajoutCompteur,section, exo);
	}

//	public String genereCodeJPA() throws Exception{
//		return genereCodeJPA(new LinkedList<String>());		
//	}


	
	public boolean autoriseUtilisationCodeGenere(String code,String nomTable) throws Exception{
		return autoriseUtilisationCodeGenere(code,nomTable,true);
	}	

	public boolean autoriseUtilisationCodeGenere(String code) throws Exception{
		return autoriseUtilisationCodeGenere(code,nomTable,true);
	}

	/**
	 * Retourne vrai ssi le code généré passé en paramètre n'est pas déjà vérrouillé
	 */
	//public boolean autoriseUtilisationCodeGenere(String code,String nomTable,Boolean verif_Connection) throws Exception{
	public boolean autoriseUtilisationCodeGenere(TaGenCodeEx genCode, String code,Boolean verif_Connection) throws Exception{
		boolean res = true;
		TaVerrouCodeGenere verrou = new TaVerrouCodeGenere();
		verrou.setEntite(genCode.getCleGencode());
		verrou.setChamp(genCode.getCodeGencode());
		verrou.setValeur(code);
		
		res = !daoVerrouCodeGenere.estVerrouille(genCode.getCleGencode(), genCode.getCodeGencode(), code);
//		String champVerif = gestionModif.recupChampGenere(nomTable);
//		if (champVerif!=null)
//			res= gestionModif.autoriseModifCodeGenere(nomTable,champVerif,code,verif_Connection);
		return res;
	}	

	/**
	 * Verrouillage d'un code prédement généré ou modifier manuellement
	 */
	//public boolean rentreCodeGenere(String code,String ChampCourant) throws Exception{
	public boolean rentreCodeGenere(TaGenCodeEx genCode, String code, String sessionID) throws Exception{
		try{
			boolean res = false;
			
			if(!code.equals("")) {
				if(autoriseUtilisationCodeGenere(genCode,code,true)) { //ce code n'est pas déjà vérouiller
					TaVerrouCodeGenere verrou = new TaVerrouCodeGenere();
					verrou.setEntite(genCode.getCleGencode());
					verrou.setChamp(genCode.getCodeGencode());
					verrou.setValeur(code);
					verrou.setSessionID(sessionID);
					verrou=daoVerrouCodeGenere.merge(verrou);
				}
			}
//			String champVerif=gestionModif.recupChampGenere(this.nomTable);
//			if (champVerif.equals(ChampCourant)|| ChampCourant==null)
//				if (champVerif!=null && code !=null && !LibChaine.empty(code))
//				{
//					gestionModif.rentreEnModif(nomTable,champVerif,code);
//					champGenere=champVerif;
//					valeurGenere=code;
//					res=true;
//				}
			return res;
		}catch(Exception e){
			logger.error("",e);
			return false;
		}
	}

	/**
	 * Libère/dévérouille un code prédement généré 
	 * Par ce qu'il n'a pas été utilisé (enregistrement annulé) ou il a été validé (enregistrement validé)
	 */
	//public void annulerCodeGenere(String code,String ChampCourant) throws Exception{
	public void annulerCodeGenere(TaGenCodeEx genCode, String code, boolean tous) throws Exception {
//		String champVerif=gestionModif.recupChampGenere(this.nomTable);
//		if (champVerif.equals(ChampCourant)|| ChampCourant==null)
//			if (champVerif!=null && code !=null && !LibChaine.empty(code))
//			{
//				gestionModif.annuleModif(nomTable,champVerif,code,false);
//			}
		
		//TaGenCodeEx c = dao.findByCode(nomTable);
		if(!appelClientRiche()) {
			TaVerrouCodeGenere verrou = daoVerrouCodeGenere.findVerrou(genCode.getCleGencode(), genCode.getCodeGencode(), code);
			if(tous && verrou!=null) {
				daoVerrouCodeGenere.libereVerrouTout(genCode.getCleGencode(), genCode.getCodeGencode(), sessionInfo.getSessionID());
			} else {
				if(verrou!=null) {
					daoVerrouCodeGenere.remove(verrou);
				}
			}
		}
	}
	
	public void annulerCodeGenere(TaGenCodeEx genCode, String code) throws Exception {
		annulerCodeGenere(genCode,code,true);
	}
	
	public void libereVerrouTout() {
		daoVerrouCodeGenere.libereVerrouTout();
	}

//	@Schedule(second="*/30", minute="*", hour="*")
//	public void traiterTrenteSecondes() {
//		System.out.println("Execution du traitement toutes les 30 secondes "+new Date());
//	}
	
	public void libereVerrouTout(List<String> listeSessionID) {
		daoVerrouCodeGenere.libereVerrouTout(listeSessionID);
	}

	public String genereCodeJPA(String nomClass) throws Exception{
		try{
			boolean nonAutorise = false;
			String nouveauCode =null;
			int compteur = -1;

			//code.setListeGestCode(Const.C_FICHIER_GESTCODE);
			//			TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO();
			TaInfoEntreprise taInfoEntreprise = entrepriseService.findInstance();
			TaGenCodeEx taGenCodeEx = dao.findByCode(nomClass);

			while (!nonAutorise) {
				compteur ++;
				nouveauCode = genereCodeExJPA(compteur,nomClass,taInfoEntreprise.getCodexoInfoEntreprise());
				//nonAutorise=autoriseUtilisationCodeGenere(res,nomTableMere,true);
				nonAutorise = autoriseUtilisationCodeGenere(taGenCodeEx,nouveauCode,true);
//				if(listeCodes.size()>0)
//					nonAutorise=!listeCodes.contains(res);
			}
			if(!appelClientRiche()) {
				rentreCodeGenere(taGenCodeEx, nouveauCode, sessionInfo.getSessionID());
			}
			code.reinitialiseListeGestCode();
			return nouveauCode;
		}catch(Exception e){
			logger.error("",e);
			return "";
		}
	}
	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
	public String genereCodeJPA(String nomClass, Map<String, String> params)
			throws Exception {
		try{
			boolean nonAutorise = false;
			String nouveauCode =null;
			int compteur = -1;

			//code.setListeGestCode(Const.C_FICHIER_GESTCODE);
			//			TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO();
			TaInfoEntreprise taInfoEntreprise = entrepriseService.findInstance();
			TaGenCodeEx taGenCodeEx = dao.findByCode(nomClass);
			String valeur=null;
			if(params!=null && params.size()>0){
				valeur=params.get(IGenCodeExDAO.C_NOMFOURNISSEUR);
				if(valeur!=null)taGenCodeEx.setNomFournisseur(valeur);
				valeur=params.get(IGenCodeExDAO.C_CODEFOURNISSEUR);
				if(valeur!=null)taGenCodeEx.setCodeFournisseur(valeur);
				valeur=params.get(IGenCodeExDAO.C_CODEDOCUMENT);
				if(valeur!=null)taGenCodeEx.setCodeDocument(valeur);
				valeur=params.get(IGenCodeExDAO.C_DESC);
				if(valeur!=null)taGenCodeEx.setDescArticle(valeur);
				valeur=params.get(IGenCodeExDAO.C_CODETYPE);
				if(valeur!=null)taGenCodeEx.setCodeTypeDocument(valeur);
				valeur=params.get(IGenCodeExDAO.C_VIRTUEL);
				if(valeur!=null)taGenCodeEx.setVirtuel(LibConversion.StringToBoolean(valeur));
				valeur=params.get(IGenCodeExDAO.C_IDARTICLEVIRTUEL);
				if(valeur!=null)taGenCodeEx.setIdArticle(valeur);
				
				valeur=params.get(IGenCodeExDAO.C_DATEDOCUMENT);
				if(valeur!=null)taGenCodeEx.setDateDocument(LibDate.stringToDate(valeur));
			}

			while (!nonAutorise) {
//				if(taGenCodeEx.isMemeCompteur())compteur =0;
//				else 
				compteur ++;
//				nouveauCode = genereCodeExJPA(compteur,nomClass,taInfoEntreprise.getCodexoInfoEntreprise());
				nouveauCode = genereCodeExJPA(taGenCodeEx,compteur,nomClass,taInfoEntreprise.getCodexoInfoEntreprise());
				//nonAutorise=autoriseUtilisationCodeGenere(res,nomTableMere,true);
				nonAutorise = autoriseUtilisationCodeGenere(taGenCodeEx,nouveauCode,true);
//				if(listeCodes.size()>0)
//					nonAutorise=!listeCodes.contains(res);
			}
			if(!appelClientRiche()) {
				rentreCodeGenere(taGenCodeEx, nouveauCode, sessionInfo.getSessionID());
			}
			code.reinitialiseListeGestCode();
			return nouveauCode;
		}catch(Exception e){
			logger.error("",e);
			return "";
		}
	}

}
