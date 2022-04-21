package fr.legrain.article.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.ibm.icu.text.DecimalFormatSymbols;

import fr.legrain.article.dao.IParamEan128DAO;
import fr.legrain.article.dto.TaParamEan128DTO;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaParamEan128;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaParamEan128Mapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;

/**
 * Session Bean implementation class TaParamEan128Bean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaParamEan128Service extends AbstractApplicationDAOServer<TaParamEan128, TaParamEan128DTO> implements ITaParamEan128ServiceRemote {

	static Logger logger = Logger.getLogger(TaParamEan128Service.class);
	
	public static final String EAN128_AI_SSCC = "00"; //18 chiffres
	public static final String EAN128_AI_SUR_EMBALAGE = "01"; //14 chiffres
	public static final String EAN128_AI_ARTICLE = "02"; //14 chiffres
	public static final String EAN128_AI_CODE_ARTICLE = "241"; //1 à 30 alphanumériques
	public static final String EAN128_AI_LOT_FABRICATION = "10"; //1 à 20  alphanumériques
	public static final String EAN128_AI_QUANTITE_UNITAIRE = "30"; //1 à 8 chiffres
	public static final String EAN128_AI_QUANTITE = "37"; //1 à 8 chiffres
	public static final String EAN128_AI_DDM = "15"; //6 chiffres
	public static final String EAN128_AI_DLC = "17"; //6 chiffres
	public static final String EAN128_AI_POIDS_EN_KG = "310"; //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	public static final String EAN128_AI_LONGUEUR_EN_METRE = "311"; //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	public static final String EAN128_AI_LARGEUR_EN_METRE = "312"; //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	public static final String EAN128_AI_HAUTEUR_EN_METRE = "313"; //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	public static final String EAN128_AI_SURFACE_EN_METRE = "314"; //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	public static final String EAN128_AI_VOLUME_EN_LITRE = "315"; //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI


	@Inject private IParamEan128DAO dao;
	protected @EJB ITaCoefficientUniteServiceRemote taCoefficientUniteService;
	
	private static char groupSeperator = '\u001D';//(char)29;
    private static String ean128StartCode = "]C1";
    public static Map<String, String> listeRetour;
	/**
	 * Default constructor. 
	 */
	public TaParamEan128Service() {
		super(TaParamEan128.class,TaParamEan128DTO.class);
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaParamEan128 transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaParamEan128 transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaParamEan128 persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaParamEan128 merge(TaParamEan128 detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaParamEan128 merge(TaParamEan128 detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaParamEan128 findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaParamEan128 findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaParamEan128> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaParamEan128DTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaParamEan128DTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaParamEan128> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaParamEan128DTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaParamEan128DTO entityToDTO(TaParamEan128 entity) {
//		TaParamEan128DTO dto = new TaParamEan128DTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaParamEan128Mapper mapper = new TaParamEan128Mapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaParamEan128DTO> listEntityToListDTO(List<TaParamEan128> entity) {
		List<TaParamEan128DTO> l = new ArrayList<TaParamEan128DTO>();

		for (TaParamEan128 taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaParamEan128DTO> selectAllDTO() {
		System.out.println("List of TaParamEan128DTO EJB :");
		ArrayList<TaParamEan128DTO> liste = new ArrayList<TaParamEan128DTO>();

		List<TaParamEan128> projects = selectAll();
		for(TaParamEan128 project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaParamEan128DTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaParamEan128DTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaParamEan128DTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaParamEan128DTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaParamEan128DTO dto, String validationContext) throws EJBException {
		try {
			TaParamEan128Mapper mapper = new TaParamEan128Mapper();
			TaParamEan128 entity = null;
			if(dto.getId()!=0) {
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
	
	public void persistDTO(TaParamEan128DTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaParamEan128DTO dto, String validationContext) throws CreateException {
		try {
			TaParamEan128Mapper mapper = new TaParamEan128Mapper();
			TaParamEan128 entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaParamEan128DTO dto) throws RemoveException {
		try {
			TaParamEan128Mapper mapper = new TaParamEan128Mapper();
			TaParamEan128 entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaParamEan128 refresh(TaParamEan128 persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaParamEan128 value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaParamEan128 value, String propertyName, String validationContext) {
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
	public void validateDTO(TaParamEan128DTO dto, String validationContext) {
		try {
			TaParamEan128Mapper mapper = new TaParamEan128Mapper();
			TaParamEan128 entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamEan128DTO> validator = new BeanValidator<TaParamEan128DTO>(TaParamEan128DTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaParamEan128DTO dto, String propertyName, String validationContext) {
		try {
			TaParamEan128Mapper mapper = new TaParamEan128Mapper();
			TaParamEan128 entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamEan128DTO> validator = new BeanValidator<TaParamEan128DTO>(TaParamEan128DTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaParamEan128DTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaParamEan128DTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaParamEan128 value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaParamEan128 value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<String> recupListIdentifiant(){
		return dao.recupListIdentifiant();
	}
	
	public TaParamEan128 rechercheParam(String identifiant) {
		TaParamEan128 param=dao.findByCode(identifiant);
		return param;
	}
	
	

    //////////////////// Decodage ////////////
	
	
	public Map<String, String>  decodeEan128(String codeBarre) throws Exception {
		//codeBarre=codeBarre+Character.toString(groutSeperator);
		try {
			String separateurLecturePersonnalise = "|";
			String separateurLecturePersonnaliseProtege = "\\|"; //le lecteur/scanner doit est paramétré pour effectuer un remplacement de FNC1 vers "|"

			if(codeBarre.startsWith(ean128StartCode))codeBarre=codeBarre.replaceFirst(ean128StartCode, "");
			if(codeBarre.trim().length()>48)
				throw new Exception("Le code barre est supérieur à 48 caractères, il n'est pas valide");
			//			 if(codeBarre.contains(Character.toString(groupSeperator))) {
			//				 codeBarre=codeBarre.replaceFirst(Character.toString(groupSeperator), "[FNC1]");		
			//			 }
			//je met un identifiant de fin de code
			codeBarre+=Character.toString(groupSeperator);

			//			if(codeBarre.contains(Character.toString(groutSeperator)))
			//				codeBarre=codeBarre.replaceFirst(Character.toString(groutSeperator), "(");		
			//			if(codeBarre.substring(codeBarre.length()-1).equals("("))
			//				codeBarre=codeBarre.substring(0,codeBarre.length()-1);
			//			codeBarre=codeBarre.replace("((", "(");	
			//			codeBarre=codeBarre.replace("))", ")");

			int arrayIndex=0;
			listeRetour = new LinkedHashMap<String, String>();
			List<TaParamEan128> liste= dao.selectAll();
			boolean trouve=false;
			while (codeBarre.trim().length()>0) {
				if(!trouve && arrayIndex==liste.size()) {
					//problème de code barre
					return null;
				}
				arrayIndex=0;
				trouve=false;
				while (arrayIndex<liste.size()) {
					TaParamEan128 a =liste.get(arrayIndex);

					String strAI = a.getCode128();
					String strAId = a.getCode128();
					int intMin = a.getLongBorneMax();
					int intMax = a.getLongBorneMax();
					int intMaxMoinsUn = a.getLongBorneMax()-1;
					Boolean variable = a.getLongVariable();
					String strType = a.getDataType();
					List<String> listeGroup=new LinkedList<String>();
					String strRegExMatch ;
					if(variable)intMin=1;

					if(strAI.equals(EAN128_AI_CODE_ARTICLE)) {
						System.out.println("TaParamEan128Service.decodeEan128()");
					}
					if(strAI.equals(EAN128_AI_LOT_FABRICATION)) {
						System.out.println("TaParamEan128Service.decodeEan128()");
					}
					if(strAI.equals(EAN128_AI_ARTICLE)) {
						System.out.println("TaParamEan128Service.decodeEan128()");
					}
					if(strAI.contains("d"))strAId=strAI.replace("d", "")+"\\d{1}";

					if (strType.equals("alphanumeric")) {
						//   				 strRegExMatch =  "^("+strAId +".[^//(//)]{" + intMin + "," + intMax + "}|\\("+strAId + ".[^//(//)]{" + intMin + "," + intMax + "}|"+strAId+"\\).[^//(//)]{"+intMin+","+intMax+"}|\\("+strAId+"\\).[^//(//)]{" + intMin + "," + intMax + "}"+Character.toString(groupSeperator)+"?)";
						//   				 strRegExMatch =  "^(\\(?"+strAId+"\\)?.[^\\(\\)]{" + intMin + "," + intMax + "}"+Character.toString(groupSeperator)+"?|\\(?"+strAId+"\\)?.{" + intMax + "}|\\(?"+strAId+"\\)?.{"+ intMin + "," + intMax +"}+Character.toString(groupSeperator))";
						if(intMin!=intMax)
							//strRegExMatch =  "^(\\(?"+strAId+"\\)?.{" + intMin + "," + intMaxMoinsUn + "}[\\x1D]|\\(?"+strAId+"\\)?.{" + intMax + "}|\\(?"+strAId+"\\)?.{"+ intMin + "," + intMax +"}\\[FNC1\\])";
							strRegExMatch =  "^(\\(?"+strAId+"\\)?.[^"+separateurLecturePersonnaliseProtege+"]{" + intMin + "," + intMaxMoinsUn + "}[\\|]|\\(?"+strAId+"\\)?.{" + intMax + "}|\\(?"+strAId+"\\)?.{"+ intMin + "," + intMax +"}\\[FNC1\\])";
						else 
							strRegExMatch =  "^(\\(?"+strAId+"\\)?.{" + intMax + "})";
					} else {
						//   				 strRegExMatch =  "^("+strAId + "\\d{" + intMin + "," + intMax + "}|\\("+strAId + "\\d{" + intMin + "," + intMax + "}|"+strAId +"\\)\\d{"+intMin+","+intMax+"}|\\("+strAId+"\\)\\d{" + intMin + "," + intMax + "}"+Character.toString(groupSeperator)+"?)";
						//strRegExMatch =  "^(\\(?"+strAId+"\\)?\\d{" + intMin + "," + intMax + "}[\\x1D]?)";
						strRegExMatch =  "^(\\(?"+strAId+"\\)?\\d{" + intMin + "," + intMax + "}["+separateurLecturePersonnaliseProtege+"]?)";
					}
					////*************////////

					//^ indique début de ligne
					//( début de ce regex
					//  \\(?"+strAId+"\\)?     "\\(?" si parenthese ouvrante ? 0 ou 1 fois   "\\)?" idem parenthese fermante
					//  \\d{" + intMin + "," + intMax + "}  "\\d" tous les chiffres  "{" + intMin + "," + intMax + "}" de intMin à intMax chiffres exemple {2,4} de 2 à 4 chiffres
					// Character.toString(groupSeperator)+"? caractère ascii 29 inidiquant la fin d'une valeur à longueur variable ? existe 1 fois ou pas

					//pour l'alphanumeric, toutes les valeurs sauf les parentheses puisqu'elles servent à repérer un identifiant AI 
					//.[^//(//)]  "." tous les caractères   "[^//(//)]" sauf les parentheses ouvrantes et fermantes
					//) fin de ce regex

					////*************////////

					Pattern pattern = Pattern.compile(strRegExMatch);
					Matcher m = pattern.matcher(codeBarre);

					while(m.find()) {
						int nb=1;

						while(nb<=m.groupCount()) {
							String valeur=m.group(nb);
							if(valeur!=null) {
								String strAITmp=strAI;
								String virgule="";
								if(strAI.contains("d")) {
									String strAISanSd=strAI.replace("d", "");
									int last = valeur.lastIndexOf(strAISanSd)+strAISanSd.length();
									virgule = valeur.substring(last,last+1);
								}
								if(valeur.startsWith("("))strAITmp="("+strAITmp;
								if(valeur.lastIndexOf(")",strAITmp.length())!=-1)strAITmp=strAITmp+")";
								//on enlève la partie du code trouvée pour que le prochain AI soit en début de ligne
								codeBarre=codeBarre.replace(valeur, "");
								if(codeBarre.startsWith("[FNC3]"))codeBarre=""; // fin du code
								valeur=valeur.substring(strAITmp.length());
								//insertion de la virgule pour les AI contenant un "d" qui représente la position de la virgule dans la valeur trouvée
								if(!virgule.equals("")) {
									StringBuilder str = new StringBuilder(valeur);
									str.insert(LibConversion.stringToInteger(virgule), ".");
									valeur=str.toString();
								}
								valeur=valeur.replace("[FNC1]", "");
								valeur=valeur.replace(separateurLecturePersonnalise, "");
								listeRetour.put(strAI, valeur);
								arrayIndex=liste.size();//permet de sortir de la boucle pour passer à une autre partie du code
								trouve = true;// permettra de ne pas créer une boucle infinie si une partie du code n'est pas déchiffrable et que l'on doit abandonner la lecture
							};
							nb++;
						}

					}
					arrayIndex++;
				}
			}

			return listeRetour;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
    
    public String valeur(Map<String, String> listeRetour,String ai) {
    	String valeurRetour="";
    	 if(listeRetour!=null && !listeRetour.isEmpty()) {
    		valeurRetour= listeRetour.get(ai);
    	 }
    	 return valeurRetour;
     }
	
    public String decodeEanSurembalage(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_SUR_EMBALAGE);
     	 }
     	 return valeurRetour;
      }

	
    public String decodeEanArticle(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_ARTICLE);
     	 }
     	 return valeurRetour;
      }
    
    public String recupEan13SurGtin14(String gtin14)   throws Exception {
    	String valeurRetour="";
    	 if(gtin14!=null && !gtin14.equals("")) {
    		if(gtin14.trim().length()!=14)
    			throw new Exception("longueur invalide");
    		valeurRetour=gtin14.trim().substring(1, 13);
    		valeurRetour+=checkSum(valeurRetour);
    		if(valeurRetour.length()!=13)
    			throw new Exception("décodage invalide");
    	 }
    	 return valeurRetour;
     } 
	
    public String decodeCodeArticle(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_CODE_ARTICLE);
     	 }
     	 return valeurRetour;
      } 
    
	
    public String decodeLotFabrication(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_LOT_FABRICATION);
     	 }
     	 return valeurRetour;
      }
    
	
    public String decodeQuantiteUnitaire(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_QUANTITE_UNITAIRE);
     	 }
     	 return valeurRetour;
      }
    
    
	
    public String decodeQuantite(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_QUANTITE);
     	 }
     	 return valeurRetour;
      }
    
    
	
    public Date decodeDDM(Map<String, String> listeRetour) {
    	String valeurRetour="";
      	 if(listeRetour!=null && !listeRetour.isEmpty()) {
      		valeurRetour= listeRetour.get(EAN128_AI_DDM);
      	 }
			SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
			try {
				return new Date(df.parse(valeurRetour).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      	 return null;
       }
    
	
    public Date decodeDLC(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_DLC);
     	 }
			SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
			try {
				return new Date(df.parse(valeurRetour).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     	 return null;
      }
    
    
	
    public BigDecimal decodePoidsEnKg(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_POIDS_EN_KG+"d");
     	 }
     	 return LibConversion.stringToBigDecimal(valeurRetour);
      }
    
    
	
    public BigDecimal decodeLongueurEnMetre(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_LONGUEUR_EN_METRE+"d");
     	 }
     	 return LibConversion.stringToBigDecimal(valeurRetour);
      }
    
    
	
    public BigDecimal decodeLargeurEnMetre(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_LARGEUR_EN_METRE+"d");
     	 }
     	 return LibConversion.stringToBigDecimal(valeurRetour);
      }
    
	
    public BigDecimal decodeHauteurEnMetre(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_HAUTEUR_EN_METRE+"d");
     	 }
     	 return LibConversion.stringToBigDecimal(valeurRetour);
      }
    
	
    public BigDecimal decodeDurfaceEnMetreCarre(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_SURFACE_EN_METRE+"d");
     	 }
     	 return LibConversion.stringToBigDecimal(valeurRetour);
      }
    
    
	
    public BigDecimal decodeVolumeEnLitre(Map<String, String> listeRetour) {
    	String valeurRetour="";
     	 if(listeRetour!=null && !listeRetour.isEmpty()) {
     		valeurRetour= listeRetour.get(EAN128_AI_VOLUME_EN_LITRE+"d");
     	 }
     	 return LibConversion.stringToBigDecimal(valeurRetour);
      }
    

    
    
    
    //////////////////// Encodage ////////////
    public String encodeEan128(String ai,Date valeur) throws Exception {
    	String valeurString="";
    	DateFormat df = new SimpleDateFormat("yyMMdd");
    	valeurString = df.format(valeur);
    	return encodeEan128(ai,valeurString);
    }
    
    public String encodeEan128(String ai,BigDecimal valeur) throws Exception {
    	String valeurString="";
    	valeurString=LibConversion.bigDecimalToString(valeur);
    	return encodeEan128(ai,valeurString);
    }
    
    
    public String encodeEan128(String ai,String valeur) throws Exception {
    	String retour="";
    	if(valeur!=null && !valeur.equals("")) {
    		//si la valeur est remplie
    		int longueur = valeur.length();
    		String variable="";
    		TaParamEan128 param = null;
    		String aiRecherche = ai;
    		if(ai.startsWith("31") 
    			||ai.startsWith("32")	
    			||ai.startsWith("33")
    			||ai.startsWith("34")
    			||ai.startsWith("35")
    			||ai.startsWith("36")
    			||ai.startsWith("39")
    			) {
    			aiRecherche = aiRecherche.substring(0,aiRecherche.length()-1); //3103 => 310
    			param = dao.findByCodeLike(aiRecherche);
    		} else {
    			param = dao.findByCode(ai);
    		}
    		if(param!=null) {
    			//si on trouve le ai dans la liste
    			//si date
    			if(param.getTypeConversion().equals(DATE)) {
    				if(valeur.length()!=param.getLongBorneMax())throw new Exception(param.getLibelle()+" invalide");    				
    			}
    			
    			//si mois
    			if(param.getTypeConversion().equals(MOIS)) {
    				if(valeur.length()!=param.getLongBorneMax()+2)throw new Exception(param.getLibelle()+" invalide");
    			}			
    			
    			//si decimal
    			if(param.getTypeConversion().equals(DECIMAL)) {
    				if(!param.getLongVariable() && valeur.length()!=param.getLongBorneMax()) 
    					throw new Exception(param.getLibelle()+" invalide");
    				else if(param.getLongVariable() && valeur.length()>param.getLongBorneMax())
    					throw new Exception(param.getLibelle()+" invalide");
    			}  			
    			
    			if(longueur>param.getLongBorneMax())longueur=param.getLongBorneMax();
    			if(param.getLongVariable() && param.getLongBorneMax()>longueur) {
    				//on rajout le caractère Ascii 29 (GS) pour signaler la fin du code variable
    				variable=Character.toString(groupSeperator);
    			}
//    			retour="("+ai+")"+valeur.substring(0, longueur)+variable;
    			retour=ai+valeur.substring(0, longueur)+variable/*+ean128StartCode*/;
    		}
    	}
//    	Map<String, String> test = decodeEan128(retour);
//    	if(!(test.containsKey(ai) && test.get(ai).trim().equals(valeur))) {
//    		//problème d'encodage
//    		throw new Exception("décodage invalide"); 
//    	}
    	return retour;
    }
    
    
    
    public String encodeEan128(Map<String, Object> listeAiValeur) throws Exception {
    	String retour="";
    	for (String ai : listeAiValeur.keySet()) {
    		if (listeAiValeur.get(ai) instanceof String) {
    			retour+=encodeEan128(ai, (String)listeAiValeur.get(ai));
    		}else			
    		if (listeAiValeur.get(ai) instanceof BigDecimal) {
    			retour+=encodeEan128(ai, (BigDecimal)listeAiValeur.get(ai));
    		}else			
    		if (listeAiValeur.get(ai) instanceof Date) {
    			retour+=encodeEan128(ai, (Date)listeAiValeur.get(ai));
    		}else
    			throw new Exception("Type objet non implémenté !!!");
    	}

    	return retour;
    }
    
    public String encodeEanSSCC(String valeur) throws Exception {
    	//18 chiffres
    	return encodeEan128(EAN128_AI_SSCC,valeur);
      }   
        
    public String encodeEanSurembalage(String valeur) throws Exception {
    	//14 chiffres
    	return encodeEan128(EAN128_AI_SUR_EMBALAGE,valeur);
      }

	
   public String encodeEanArticle(String valeur)  throws Exception {
	 //14 chiffres
   	return encodeEan128(EAN128_AI_ARTICLE,valeur);
      }
   
	
   public String encodeCodeArticle(String valeur)  throws Exception {
		 //1 à 30 alphanumériques
   	return encodeEan128(EAN128_AI_CODE_ARTICLE,valeur);
   } 
   
	
   public String encodeLotFabrication(String valeur)  throws Exception {
		 //1 à 20  alphanumériques
   	return encodeEan128(EAN128_AI_LOT_FABRICATION,valeur);
   }
   
	
   public String encodeQuantiteUnitaire(BigDecimal valeur)  throws Exception {
	   //1 à 8 chiffres
   	return encodeEan128(EAN128_AI_QUANTITE_UNITAIRE,LibConversion.bigDecimalToString(valeur));
   }
   
   
	
   public String encodeQuantite(BigDecimal valeur)  throws Exception {
	   //1 à 8 chiffres
   	return encodeEan128(EAN128_AI_QUANTITE,LibConversion.bigDecimalToString(valeur));
   }
   
   public String encodeDDM(Date valeur)  throws Exception {
	   //6 chiffres
   	return encodeEan128(EAN128_AI_DDM,valeur);
   }
   
	
   public String encodeDLC(Date valeur)  throws Exception {
	   //6 chiffres
  	return encodeEan128(EAN128_AI_DLC,valeur);
   }
   
   
	
   public String encodePoidsEnKg(BigDecimal valeur, String unite)  throws Exception {
	   //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	   valeur=conversionValeur(unite,TaCoefficientUniteService.KILOGRAMME,valeur);
	   FormattageValue format =format(valeur,EAN128_AI_POIDS_EN_KG);
	   return encodeEan128(EAN128_AI_POIDS_EN_KG+format.pos,format.valeurString);
   	
   }
   
   	
   public String encodeLongueurEnMetre(BigDecimal valeur,String unite)  throws Exception {
	   //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	   valeur=conversionValeur(unite,taCoefficientUniteService.METRE,valeur);
	   FormattageValue format =format(valeur);
   	return encodeEan128(EAN128_AI_LONGUEUR_EN_METRE+format.pos,format.valeurString);
   }
   
   
	
   public String encodeLargeurEnMetre(BigDecimal valeur,String unite)  throws Exception {
	   //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
   valeur=conversionValeur(unite,taCoefficientUniteService.METRE,valeur);
	   FormattageValue format =format(valeur);
   	return encodeEan128(EAN128_AI_LARGEUR_EN_METRE+format.pos,format.valeurString);
   }
   
   public String encodeHauteurEnMetre(BigDecimal valeur,String unite)  throws Exception {
	   //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	   valeur=conversionValeur(unite,taCoefficientUniteService.METRE,valeur);
	   FormattageValue format =format(valeur);
   	return encodeEan128(EAN128_AI_HAUTEUR_EN_METRE+format.pos,format.valeurString);
   }
   
	
   public String encodeSurfaceEnMetreCarre(BigDecimal valeur,String unite)  throws Exception {
	   //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	   valeur=conversionValeur(unite,taCoefficientUniteService.METRE_CUBE,valeur);
	   FormattageValue format =format(valeur);
   	return encodeEan128(EAN128_AI_SURFACE_EN_METRE+format.pos,format.valeurString);
   }
   
   
	
   public String encodeVolumeEnLitre(BigDecimal valeur,String unite)  throws Exception {
	   //6 chiffres en enlevant la virgule et en rajoutant la position de la virgule à la fin du AI
	   valeur=conversionValeur(unite,taCoefficientUniteService.LITRE,valeur);
	   FormattageValue format =format(valeur);
   	return encodeEan128(EAN128_AI_VOLUME_EN_LITRE+format.pos,format.valeurString);
   }
   

   private BigDecimal conversionValeur(String uniteValeur, String uniteDefaut, BigDecimal valeur) {
	   BigDecimal valeurRetour=BigDecimal.ZERO;
	   TaCoefficientUnite coef =recupCoefficientUnite(uniteValeur,uniteDefaut);
	if(coef!=null) {
		if(coef.getOperateurDivise()) 
			valeurRetour=(valeur).divide(coef.getCoeff()
					,MathContext.DECIMAL128).setScale(coef.getNbDecimale(),BigDecimal.ROUND_HALF_UP);
		else valeurRetour=(valeur).multiply(coef.getCoeff());
	}else  {

	}
	return valeurRetour;
   }
	
	
	public TaCoefficientUnite recupCoefficientUnite(String code1, String code2) {
		TaCoefficientUnite coef=null;;
		coef=taCoefficientUniteService.findByCode(code1,code2);
		if(coef!=null)coef.recupRapportEtSens(code1,code2);
		return coef;
	}
	
	private FormattageValue format(BigDecimal valeur) {
		return format(valeur,null);
	}
    
	private FormattageValue format(BigDecimal valeur, String ai) {
		FormattageValue retour = new FormattageValue();
		if(ai!=null ) {//ajout nicolas
			TaParamEan128 paramAi = dao.findByCodeLike(ai);
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
			String DS=Character.toString(dfs.getDecimalSeparator());
			//System.out.println( dfs.getDecimalSeparator() );


			//retour.valeurString =LibConversion.bigDecimalToString(valeur.stripTrailingZeros().toPlainString());
			retour.valeurString = valeur.stripTrailingZeros().toPlainString();
			if(retour.valeurString.contains(DS))retour.pos=/*paramAi.getLongBorneMax()-*/new StringBuilder(retour.valeurString).reverse().lastIndexOf(DS);
			retour.valeurString=retour.valeurString.replace(DS, "");
			while (retour.valeurString.length()<paramAi.getLongBorneMax()) {
				retour.valeurString="0"+retour.valeurString;
			}
		} else {
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.FRENCH);
			String DS=Character.toString(dfs.getDecimalSeparator());
			//System.out.println( dfs.getDecimalSeparator() );


			retour.valeurString =LibConversion.bigDecimalToString(valeur);		   
			if(retour.valeurString.contains(DS))retour.pos=6-retour.valeurString.lastIndexOf(DS);
			retour.valeurString=retour.valeurString.replace(DS, "");
			while (retour.valeurString.length()<6) {
				retour.valeurString="0"+retour.valeurString;
			}
		}
		return retour;
	}
	
	private class FormattageValue {
		int pos=0;
		String valeurString="";
	}
	
	
	
	public static int checkSum (String Input) {
		int evens = 0; //initialize evens variable
		int odds = 0; //initialize odds variable
		int checkSum = 0; //initialize the checkSum
	    for (int i = 0; i < Input.length(); i++) {
	        int digit = Integer.parseInt("" + Input.charAt(i));	        
	        //check if i is odd or even
	        if ((i+1) % 2 == 0) { // check that the character at position "i" is divisible by 2 which means it's even
	            evens += digit;// then add it to the evens
	        } else {
	            odds += digit; // else add it to the odds
	        }
	    }
		odds = odds * 3; //multiply odds by three
		int total = odds + evens; //sum odds and evens
		if (total % 10 == 0){ //if total is divisible by ten, special case
			checkSum = 0;//checksum is zero
		} else { //total is not divisible by ten
			checkSum = 10 - (total % 10); //subtract the ones digit from 10 to find the checksum
		}

		return checkSum;
	}
	
}
