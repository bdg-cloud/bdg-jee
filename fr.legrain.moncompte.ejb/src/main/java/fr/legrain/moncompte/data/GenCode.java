package fr.legrain.moncompte.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ITaGenCodeServiceRemote;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.MessCtrlLgr;
import fr.legrain.moncompte.controle.model.TaGenCode;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Le Grain SA</p>
 * @author Le Grain SA
 * @version 1.0
 */

public class GenCode extends JPACtrlChamp implements IGenCode, Serializable {

	private static final long serialVersionUID = -8573435561265661023L;
	
	@EJB private ITaGenCodeServiceRemote genCodeService = null;

	static private PropertiesConfiguration listeGestCode = new PropertiesConfiguration();
	static Logger logger = Logger.getLogger(GenCode.class.getName());
	protected void ecritFileName(String value) {}
	protected String codeFixe = null;
	protected Integer compteur = null;

	public GenCode() {}
	
	@Override
	public String genereCodeJPA( String section,int rajoutCompteur) throws Exception{
		return (genereCodeJPA(section,rajoutCompteur,""));
	}

	@Override
	public String genereCodeJPA( String section,int rajoutCompteur,String exo) throws Exception{
		String res ="";
		
		TaGenCode genCode = genCodeService.findByCode(section);
		
//		org.apache.commons.configuration.SubsetConfiguration propertie = null;
//		propertie = new org.apache.commons.configuration.SubsetConfiguration(listeGestCode,section,".");
		int pos=0;
		setCodeFixe("");
		setCompteur(0);
//		if (!propertie.isEmpty()){
		if (genCode!=null){
			String cle=null ,valeur =null;
			//for (Iterator iter = propertie.getKeys(); iter.hasNext();) {
//				cle=  iter.next().toString();
				if (genCode.getFixe()!=null){
					//valeur= listeGestCode.getString(section+"."+cle);
					valeur = genCode.getFixe();
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.trim().length();
				}
				if (genCode.getExo()!=null){
//					valeur= listeGestCode.getString(section+"."+cle);
					valeur = genCode.getExo();
					if (valeur.equals("courant")){
						valeur= exo!= null ? exo : "";
						//a ? b : c;
					}else valeur="";
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}				
				if (genCode.getDate()!=null){
					//valeur= listeGestCode.getString(section+"."+cle);
					valeur = genCode.getDate();
					if (valeur.equals("now"))
						valeur=LibDate.dateToString(null);
					else {
						ctrl_Date(valeur);					
					}
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (genCode.getMois()!=null){
					valeur=LibDate.getMois(new Date());
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (genCode.getJour()!=null){
					valeur=LibDate.getJour(new Date());
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (genCode.getAnnee()!=null){
					valeur=LibDate.getAnnee(new Date());
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (genCode.getCompteur()!=null){
//					List<Object> liste = listeGestCode.getList(section+"."+cle);
					List<Object> liste = new LinkedList<Object>();
					String[] aa = genCode.getCompteur().split(",");
					for (int i = 0; i < aa.length; i++) {
						liste.add(aa[i]);
					}
					
					String result = null;
					int depart = pos+1;
					int total = pos+LibConversion.stringToInteger((String)liste.get(2))+1;
					int totalMoins1 = total-1;
					String requete = null;
					if (!LibChaine.empty(res)){
						requete = "select substring (max("+liste.get(1)+") , "+depart+") from "
								+liste.get(0) +"  where substring("+liste.get(1)+" , 1 , "+pos+")= '"+res+"'" ;
						//					if(section.equalsIgnoreCase("TA_FACTURE"))
						//							requete+=" and substring ("+liste.get(1)+" from "+total+")='' and substring ("+liste.get(1)+" from "+totalMoins1+")<>''";
						requete+= " and substring ("+liste.get(1)+" , "+depart+" , "+depart+") between '0' and '9'";
					}else{ 
						requete = "select max("+liste.get(1)+") from "
								+liste.get(0) +"  where " ;
						//							if(section.equalsIgnoreCase("TA_FACTURE"))
						//								requete+=" substring ("+liste.get(1)+" from "+total+")='' and substring ("+liste.get(1)+" from "+totalMoins1+")<>'' and";
						requete+= " substring ("+liste.get(1)+" , "+depart+" , "+depart+") between '0' and '9'";
					}
					Query query =this.getEntityManager().createQuery(requete);
					result= (String) query.getSingleResult();
					if (result!=null){
						valeur=result;							
						while(valeur.startsWith("0"))
							valeur=valeur.replaceFirst("0", "");
					}else valeur="0";					
					valeur = LibConversion.integerToString(LibConversion.stringToInteger(valeur)+1+ rajoutCompteur);
					if (liste.size()>2)
						while (valeur.length()<LibConversion.stringToInteger((String)liste.get(2)))
							valeur="0"+valeur;
					res+=valeur;					
					setCompteur(LibConversion.stringToInteger(valeur));
					pos+=valeur.length();
				}
//			}
		}else throw new Exception();
		return res;
	}

	@Override
	public String genereCode( String section,int rajoutCompteur,String exo) throws Exception{
		String res ="";
		org.apache.commons.configuration.SubsetConfiguration propertie = null;
		propertie = new org.apache.commons.configuration.SubsetConfiguration(listeGestCode,section,".");
		// Map<String,String> cleValeur =new HashMap();
		int pos=0;
		setCodeFixe("");
		setCompteur(0);
		if (!propertie.isEmpty()){
			String cle=null ,valeur =null;
			for (Iterator iter = propertie.getKeys(); iter.hasNext();) {
				cle=  iter.next().toString();
				if (cle.indexOf("fixe")!=-1){
					valeur= listeGestCode.getString(section+"."+cle);
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.trim().length();
				}
				if (cle.indexOf("exo")!=-1){
					valeur= listeGestCode.getString(section+"."+cle);
					if (valeur.equals("courant")){
						valeur= exo!= null ? exo : "";
						//a ? b : c;
					}else valeur="";
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}				
				if (cle.indexOf("date")!=-1){
					valeur= listeGestCode.getString(section+"."+cle);
					if (valeur.equals("now"))
						valeur=LibDate.dateToString(null);
					else {
						ctrl_Date(valeur);					
					}
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (cle.indexOf("mois")!=-1){
					valeur=LibDate.getMois(new Date());
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (cle.indexOf("jour")!=-1){
					valeur=LibDate.getJour(new Date());
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (cle.indexOf("annee")!=-1){
					valeur=LibDate.getAnnee(new Date());
					res+=valeur;
					setCodeFixe(getCodeFixe()+valeur);
					pos+=valeur.length();
				}
				if (cle.indexOf("compteur")!=-1){
					List<Object> liste = listeGestCode.getList(section+"."+cle);
					ResultSet result = null;
					int depart = pos+1;
					int total = pos+LibConversion.stringToInteger((String)liste.get(2))+1;
					int totalMoins1 = total-1;
					String requete = null;
					if (!LibChaine.empty(res)){
						requete = "SELECT substring (max("+liste.get(1)+") from "+depart+") from "
								+liste.get(0) +" where substring("+liste.get(1)+" from 1 for "+pos+")= '"+res+"'" ;
						//					if(section.equalsIgnoreCase("TA_FACTURE"))
						//							requete+=" and substring ("+liste.get(1)+" from "+total+")='' and substring ("+liste.get(1)+" from "+totalMoins1+")<>''";
						requete+= " and substring ("+liste.get(1)+" from "+depart+" for "+depart+") between '0' and '9'";
					}else{ 
						requete = "SELECT max("+liste.get(1)+") from "
								+liste.get(0) +"  where " ;
						//							if(section.equalsIgnoreCase("TA_FACTURE"))
						//								requete+=" substring ("+liste.get(1)+" from "+total+")='' and substring ("+liste.get(1)+" from "+totalMoins1+")<>'' and";
						requete+= " substring ("+liste.get(1)+" from "+depart+" for "+depart+") between '0' and '9'";
					}
					result=this.getCnx().prepareStatement(requete).executeQuery();
					if (result.next()){
						if (result.getString(1)!=null){
							valeur=result.getString(1);
							//if (valeur.length()>pos)
							//							valeur=String.valueOf(LibConversion.stringToInteger(valeur.substring(1,LibConversion.stringToInteger(liste.get(2)))));

							while(valeur.startsWith("0"))
								valeur=valeur.replaceFirst("0", "");
						}else valeur="0";
					}
					else valeur="0";
					valeur = LibConversion.integerToString(LibConversion.stringToInteger(valeur)+1+ rajoutCompteur);
					if (liste.size()>2)
						while (valeur.length()<LibConversion.stringToInteger((String)liste.get(2)))
							valeur="0"+valeur;
					res+=valeur;					
					setCompteur(LibConversion.stringToInteger(valeur));
					pos+=valeur.length();
				}
			}
		}else throw new Exception();
		return res;
	}

	@Override
	public String genereCode( String section,int rajoutCompteur) throws Exception{
		return (genereCode(section,rajoutCompteur,""));
	}


	@Override
	public void setListeGestCode(String fileName) throws Exception {
		try {			
			if (!new File(fileName).exists()) {
				//				MessageDialog.openWarning(Workbench.getInstance()
				//						.getActiveWorkbenchWindow().getShell(), "ATTENTION",
				//						"Le fichier .properties "+ fileName + " est inexistant");
				throw new Exception("Le fichier .properties "+ fileName + " est inexistant");
			} else {
				FileInputStream file = new FileInputStream(fileName);
				listeGestCode.load(new FileInputStream(fileName));
				file.close();
			}
		}
		catch (Exception e) {
			logger.error("Erreur : setListeGestCode", e);
		}
	}

	@Override
	public void reinitialiseListeGestCode(){
		listeGestCode.clear();
	}

	@Override
	public void ctrlSaisie(MessCtrlLgr message) throws ExceptLgr {
		// TODO Raccord de méthode auto-généré

	}

	@Override
	public String getCodeFixe() {
		return codeFixe;
	}

	@Override
	public void setCodeFixe(String codeFixe) {
		this.codeFixe = codeFixe;
	}

	@Override
	public Integer getCompteur() {
		return compteur;
	}

	@Override
	public void setCompteur(Integer compteur) {
		this.compteur = compteur;
	}

}
