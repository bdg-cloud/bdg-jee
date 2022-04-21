package fr.legrain.controle.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.service.TaLotService;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.controle.dao.IGenCodeExDAO;
import fr.legrain.controle.model.TaGenCodeEx;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaGenCodeEx.
 * @see fr.legrain.tiers.model.old.TaGenCode
 * @author Hibernate Tools
 */
public class GenCodeExDAO implements IGenCodeExDAO {

	static Logger logger = Logger.getLogger(GenCodeExDAO.class);
	
	


	
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaGenCodeEx a";
	
	public GenCodeExDAO(){
	}



	@Override
	public void persist(TaGenCodeEx transientInstance) {
		logger.debug("persisting TaGenCodeEx instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaGenCodeEx persistentInstance) {
		logger.debug("removing TaGenCodeEx instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaGenCodeEx merge(TaGenCodeEx detachedInstance) {
		logger.debug("merging TaGenCodeEx instance");
		try {
			TaGenCodeEx result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaGenCodeEx findById(int id) {
		logger.debug("getting TaGenCodeEx instance with id: " + id);
		try {
			TaGenCodeEx instance = entityManager.find(TaGenCodeEx.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaGenCodeEx> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaGenCodeEx");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaGenCodeEx> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaGenCodeEx> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaGenCodeEx> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaGenCodeEx> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaGenCodeEx> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaGenCodeEx value) throws Exception {
		BeanValidator<TaGenCodeEx> validator = new BeanValidator<TaGenCodeEx>(TaGenCodeEx.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaGenCodeEx value, String propertyName) throws Exception {
		BeanValidator<TaGenCodeEx> validator = new BeanValidator<TaGenCodeEx>(TaGenCodeEx.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaGenCodeEx transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaGenCodeEx findByCode(String code) {
		logger.debug("getting TaGenCodeEx instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaGenCodeEx a where a.cleGencode='"+code+"'");
				TaGenCodeEx instance = (TaGenCodeEx)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public String genereCodeExJPA(TaGenCodeEx genCodeEx, int rajoutCompteur, String section, String exo) throws Exception {
		String res =null;
		String regex="";
		List<String> listeGroup=new LinkedList<String>();

		if(genCodeEx!=null && genCodeEx.getValeurGenCode()!=null){
			try {
				
				res="";
				regex+="([^{}]*)(\\{"+C_EXO+"\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_NUM+":?\\d?\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DATE		+":?[a-zA-Z]{2,4}\\/[a-zA-Z]{2,4}\\/[a-zA-Z]{2,4}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DATE_MOIS	+":?[a-zA-Z]{2}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DATE_ANNEE	+":?[a-zA-Z]{2,4}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_QUANT+"\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DATEDOCUMENT		+":?[a-zA-Z]{2,4}\\/[a-zA-Z]{2,4}\\/[a-zA-Z]{2,4}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DATEDOCUMENT_MOIS	+":?[a-zA-Z]{2}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DATEDOCUMENT_ANNEE	+":?[a-zA-Z]{2,4}\\})([^{}]*)";
//				regex+="|([^{}]*)(\\{"+C_DATEDOCUMENT_INV+":?[a-zA-Z]{2,4}\\/[a-zA-Z]{2,4}\\/[a-zA-Z]{2,4}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DATEDOCUMENT_QUANT+"\\})([^{}]*)";				
				//regex+="|([^{}]*)(\\{"+C_HEURE+":?([hH]{0,2}-[mM]{0,2}-[sS]{0,2})?\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_HEURE+":[hH]{0,2}-[mM]{0,2}-[sS]{0,2}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_HEURE+":[hH]{0,2}-[mM]{0,2}\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_NOMFOURNISSEUR	+":?\\d?\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_CODEFOURNISSEUR+":?\\d?\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_CODEDOCUMENT	+":?\\d?\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_DESC			+":?\\d?\\})([^{}]*)";
				regex+="|([^{}]*)(\\{"+C_CODETYPE		+":?\\d?\\})([^{}]*)";
				//regex+="|([^{}]*)(\\{"+C_DESC+":?\\d{0,2}\\}([^{}]*))";
	
				Pattern pattern = Pattern.compile(regex);
				Matcher m = pattern.matcher(genCodeEx.getValeurGenCode());
				String type;
				String format;
//				SimpleDateFormat df = new SimpleDateFormat(format);				
				int pos=1;
				while(m.find()) {
					int nb=1;
					
					while(nb<=m.groupCount()) {	
						listeGroup.add(m.group(nb));
						nb++;
					};
				}		
				
				if(genCodeEx.isVirtuel()){
					listeGroup.add(0,TaLotService.DEBUT_VIRTUEL_LOT);
					if(genCodeEx.getIdArticle()!=null && !genCodeEx.getIdArticle().isEmpty())
						listeGroup.add(1,genCodeEx.getIdArticle()+"_");
				}
				for (String val : listeGroup) {
					if(val!=null){
						if(val.startsWith("{")){
							type = null;
							format="5";
							String[] morceaux=val.split("}");
							if(morceaux.length>0){
								morceaux=morceaux[0].split(":");
								type=morceaux[0].substring(1);
								if(morceaux.length>1)format=morceaux[1];
							}
							if(type!=null){
								Calendar c = Calendar.getInstance();
								switch (type) {
									case C_EXO:
										pos=res.length();
										res+=exo;
										break;					
									case C_NUM:
										//on va récupérer le dernier numéro						
										pos=res.length()+1;
			
										String result = null;
										String valeur = null;
										int depart = pos;
										int longeur = LibConversion.stringToInteger(format);
										int total = pos+longeur;
										int totalMoins1 = total-1;
										String requete = null;
										if (!LibChaine.empty(res)){
											requete = "select substring (max(a."+genCodeEx.getCodeGencode()+") , "+depart+","+longeur+") from "
													+genCodeEx.getCleGencode() +" a  where substring(a."+genCodeEx.getCodeGencode()+" , 1 , "+(pos-1)+")= '"+res+"'" ;
											//					if(section.equalsIgnoreCase("TA_FACTURE"))
											//							requete+=" and substring ("+liste.get(1)+" from "+total+")='' and substring ("+liste.get(1)+" from "+totalMoins1+")<>''";
											requete+= " and substring (a."+genCodeEx.getCodeGencode()+" , "+depart+" , "+1+") between '0' and '9'";
										}
										else{ 
											requete = "select substring (max(a."+genCodeEx.getCodeGencode()+") , "+depart+","+longeur+") from "
													+genCodeEx.getCleGencode() +" a  where substring (a."+genCodeEx.getCodeGencode()+" , "+depart+" , "+1+") between '0' and '9'";							
				//							requete = "select max(a."+genCodeEx.getCodeGencode()+") from "
				//									+genCodeEx.getCleGencode() +" a  where " ;
				//							//							if(section.equalsIgnoreCase("TA_FACTURE"))
				//							//								requete+=" substring ("+liste.get(1)+" from "+total+")='' and substring ("+liste.get(1)+" from "+totalMoins1+")<>'' and";
				//							requete+= " substring (a."+genCodeEx.getCodeGencode()+" , "+depart+" , "+depart+") between '0' and '9'";
										}
										Query query =entityManager.createQuery(requete);
										result= (String) query.getSingleResult();
										if (result!=null){
											valeur=result;							
											while(valeur.startsWith("0"))
												valeur=valeur.replaceFirst("0", "");
										}else valeur="0";					
										valeur = LibConversion.integerToString(LibConversion.stringToInteger(valeur)+1+ rajoutCompteur);
//										valeur = LibConversion.integerToString(LibConversion.stringToInteger(valeur)+1);					
										if (format!=null &&!format.equals("0"))
											while (valeur.length()<LibConversion.stringToInteger(format))
												valeur="0"+valeur;
										res+=valeur;					
				//						setCompteur(LibConversion.stringToInteger(valeur));
										pos+=valeur.length();
														
										break;
									case C_DATE:
										format=format.toLowerCase();
										format=format.replaceAll("j", "d");
										format=format.replaceAll("m", "M");
										format=format.replaceAll("a", "y");
										SimpleDateFormat df = new SimpleDateFormat(format);
										res+= df.format(new Date());
										
										break;
									case C_DATE_MOIS:
//										format=format.toLowerCase();
//										format=format.replaceAll("j", "d");
//										format=format.replaceAll("m", "M");
//										format=format.replaceAll("a", "y");
//										SimpleDateFormat df = new SimpleDateFormat(format);
//										res+= df.format(new Date());
										//res+=new Date().getYear()
										c.setTime(new Date());
										int mois = c.get(Calendar.MONTH)+1;
										res+=String.format("%02d", Integer.parseInt(""+mois));
										//res+=mois;
										break;
									case C_DATE_ANNEE:
//										format=format.toLowerCase();
//										format=format.replaceAll("j", "d");
//										format=format.replaceAll("m", "M");
//										format=format.replaceAll("a", "y");
//										SimpleDateFormat df = new SimpleDateFormat(format);
//										res+= df.format(new Date());
										c.setTime(new Date());
										int annee = c.get(Calendar.YEAR);
										if(format.length()==2) {
											res+=LibConversion.integerToString(annee).substring(2,4);
										} else {
											res+=annee;
										}
										break;
									case C_QUANT:						
										res+= LibConversion.integerToString(LibDate.quantieme(new Date()));
										break;						
									case C_HEURE:
										format=format.replaceAll("h", "H");
										format=format.replaceAll("M", "m");
										format=format.replaceAll("S", "s");
										SimpleDateFormat dh = new SimpleDateFormat(format);
										res+= dh.format(new Date());
										break;
									case C_CODEFOURNISSEUR:
										//aller chercher le nom du fournisseur
										if(genCodeEx.getCodeFournisseur()!=null){
											res+=genCodeEx.getCodeFournisseur();
										}
										break;										
									case C_NOMFOURNISSEUR:
										//aller chercher le nom du fournisseur
										if(genCodeEx.getNomFournisseur()!=null){
											if(genCodeEx.getNomFournisseur().length()>0 && genCodeEx.getNomFournisseur().length()>4)
											res+=genCodeEx.getNomFournisseur().substring(0, 4);
											else res+=genCodeEx.getNomFournisseur().substring(0,genCodeEx.getNomFournisseur().length() );
										}
										break;	
									case C_CODEDOCUMENT:
										//aller chercher le nom du fournisseur
										if(genCodeEx.getCodeDocument()!=null){
											res+=genCodeEx.getCodeDocument();
										}
										break;
									case C_DATEDOCUMENT:
										format=format.toLowerCase();
										format=format.replaceAll("j", "d");
										format=format.replaceAll("m", "M");
										format=format.replaceAll("a", "y");
										SimpleDateFormat dfdoc = new SimpleDateFormat(format);
										//aller chercher la date du document
										if(genCodeEx.getDateDocument()!=null){
											res+=dfdoc.format(genCodeEx.getDateDocument());
										}
										break;	
									case C_DATEDOCUMENT_MOIS:
//										format=format.toLowerCase();
//										format=format.replaceAll("j", "d");
//										format=format.replaceAll("m", "M");
//										format=format.replaceAll("a", "y");
//										SimpleDateFormat df = new SimpleDateFormat(format);
//										res+= df.format(new Date());
										//res+=new Date().getYear()
										if(genCodeEx.getDateDocument()!=null){
											c.setTime(genCodeEx.getDateDocument());
											int moisDoc = c.get(Calendar.MONTH)+1;
											res+=String.format("%02d", Integer.parseInt(""+moisDoc));
											//res+=mois;
										}
										break;
									case C_DATEDOCUMENT_ANNEE:
//										format=format.toLowerCase();
//										format=format.replaceAll("j", "d");
//										format=format.replaceAll("m", "M");
//										format=format.replaceAll("a", "y");
//										SimpleDateFormat df = new SimpleDateFormat(format);
//										res+= df.format(new Date());
										if(genCodeEx.getDateDocument()!=null){
											c.setTime(genCodeEx.getDateDocument());
											int anneeDoc = c.get(Calendar.YEAR);
											if(format.length()==2) {
												res+=LibConversion.integerToString(anneeDoc).substring(2,4);
											} else {
												res+=anneeDoc;
											}
										}
										break;
									case C_DATEDOCUMENT_QUANT:
										if(genCodeEx.getDateDocument()!=null){
										res+= LibConversion.integerToString(LibDate.quantieme(genCodeEx.getDateDocument()));
										}
										break;										
									case C_DESC:
										//aller chercher le nom du fournisseur
										if(genCodeEx.getDescArticle()!=null){
											if(genCodeEx.getDescArticle().length()>0 && genCodeEx.getDescArticle().length()>8)
											res+=genCodeEx.getDescArticle().substring(0, 8);
											else res+=genCodeEx.getDescArticle().substring(0,genCodeEx.getDescArticle().length() );
										}
										break;						
									case C_CODETYPE:
										//aller chercher le nom du fournisseur
										if(genCodeEx.getCodeTypeDocument()!=null){
											res+=genCodeEx.getCodeTypeDocument();
										}
										break;						
									default:
										break;
								}
							}
						} else {
							res += val.toUpperCase();
						}
					}
				}
			} catch (Exception e) {
				logger.error("Erreur : lors de la génération du code ", e);
			}
		}
		return res;		
	}
	
	public String genereCodeExJPA(int rajoutCompteur, String section, String exo) throws Exception{		
		TaGenCodeEx genCodeEx = findByCode(section);		
		return genereCodeExJPA(genCodeEx,rajoutCompteur,section,exo);
	}

}
