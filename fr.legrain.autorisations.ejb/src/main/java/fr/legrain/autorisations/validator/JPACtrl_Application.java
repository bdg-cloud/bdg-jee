package fr.legrain.autorisations.validator;

/**
 * <p>Title: Gestion Commerciale</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Le Grain A.A
 * @version 1.0
 */

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import fr.legrain.autorisations.controle.model.TaControle;
import fr.legrain.autorisations.data.JPACtrlChamp;
import fr.legrain.bdg.controle.service.remote.ITaControleServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.MessCtrlLgr;
import fr.legrain.lib.data.ModeObjet;
import fr.legrain.validator.ICtrlLgr;

/**
 * <p>Title: </p>
 * <p>Description: Controles généreaux de l'application de test.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Le Grain SA</p>
 * @author Le Grain SA
 * @version 1.0
 */
@Stateless
//@Singleton
public class JPACtrl_Application extends JPACtrlChamp implements ICtrlLgr {

	static Logger logger = Logger.getLogger(JPACtrl_Application.class.getName());
	protected Properties listeCtrl; //Liste créée à partir du fichier ini //TStringList
	protected Map<String,TaControle> listeCtrlMap;
	private String FFichierIni; //chemin du fichier ini contenant la liste des controle pour chacun des champs
	private boolean modeServeur = false;
	
	@EJB
	private ITaControleServiceRemote controleServiceRemote;
	
	public void setFFichierIni(String value) { //procedure EcritFichierIni(value : string);
		try {
			if (!new File(value).exists()) {
				throw new ExceptLgr("Le fichier .ini " + value + " est inexistant") ;
			}
			else {
				FFichierIni = value;
				FileInputStream file =  new FileInputStream(FFichierIni);
				listeCtrl.load(file); 
				file.close();
			}
		}
		catch (Exception e) {
			FFichierIni = "";
			logger.error("setFFichierIni : "+"ERREUR",e);

			//abort;
		}
		finally {
			//Screen.Cursor := TmpCursor;
		}
	}

	public String getFFichierIni() { //function LitFichierIni: string;
		return FFichierIni;
	}

	public JPACtrl_Application() {
//		listeCtrl = new Properties();
		
		System.err.println("A FAIRE : Indiquer l'emplacement des fichiers INI");
//		setFFichierIni(Const.C_FICHIER_INI_CTRL);
		
//		setFFichierIni("/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_10a_E4/GestionCommerciale/Bd/CtrlBD.ini");

		//		setFichierIni_ID(Const.C_FICHIER_INI_IDBD);
//		setFichierListeTitre(Const.C_FICHIER_LISTE_TITRE_BD);
	}
	
	@PostConstruct
	public void init() {
		System.err.println("************************************************************************----------------------***************************");
		listeCtrlMap = new HashMap<String, TaControle>();
		
		List<TaControle> listeControle =  controleServiceRemote.selectAll();
		String key = "";
		for (TaControle taControle : listeControle) {
			if(taControle.getContexte()!=null && !taControle.getContexte().equals("")) {
				key = taControle.getContexte()+"."+taControle.getChamp();
			} else {
				key = taControle.getChamp();
			}
			listeCtrlMap.put(key , taControle);
		}
	}



	/**
	 * CtrlSaisie
	 * @param Message TMessCtrlLGR
	 */
	public void ctrlSaisie(MessCtrlLgr message) throws ExceptLgr {
		// super.ctrlSaisie(message); //controle généreaux à l'application
//		/*
		int codeCtrl=0;
		String codeControleStr;
		String key;
		/** @todo Remettre le try {...} ? */
		// try {
		if(message==null)throw new ExceptLgr("Le contrôleur est null",0,true,0);
		//key = message.getNomTable() + "." +message.getNomChamp();
		
//		if(modeServeur) {
//			key = message.getEntityClass().getSimpleName() + "." + message.getNomChamp();
//	    } else {
			if(message.getContexte()!=null && !message.getContexte().equals("")) {
	    		//validation pour un contexte d'écran particulier
	    		key = message.getContexte() + "." + message.getEntityClass().getSimpleName() + "." + message.getNomChamp();
	    	} else {
	    		//pas de contexte écran, validation "par défaut" pour ce champ
	    		key = message.getEntityClass().getSimpleName() + "." + message.getNomChamp();
	    	}
//	    }
		
			
			/**   !!!!!  Rajouter pour palier à la mauvaise remontée du modeEcran !!!!!!  **/
			/*******************************************************************************/
			if(message.getModeObjet()==null){
				message.setModeObjet(new ModeObjet(EnumModeObjet.C_MO_EDITION));
			}
			
			

		if (listeCtrlMap.containsKey(key)) {
			codeControleStr = (String) listeCtrlMap.get(key).getControleDefaut();
			for(int j=0; j<codeControleStr.split(";").length; j++) {
				codeCtrl = LibConversion.stringToInteger((codeControleStr.split(";")[j]).trim());
				switch(codeCtrl) {
				case 100: // Laisser numéro de controle pour pouvoir passer outre les controles !!
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:  break;
						case C_MO_EDITION:  break;
						case C_MO_INSERTION:  break;
						case C_MO_IMPORTATION:break;
						default: break;
						}
					} else {
						
					}
					break;
				case 101: // controle d'unicité
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION: break;
						case C_MO_EDITION:
							//ctrl_ExistePasDansTableEx(message); //passage ejb ==> fait par base de données / JPA 
							break;
						case C_MO_INSERTION:
	//*********************** ctrl_ExistePasDansTable(message);
							break;
						case C_MO_IMPORTATION: break;
						default: break;
						}
					} else {
						
					}
					break;
						
				case 102:
					ctrl_EstNonVide(message);
					break;
				case 103: // controle si valeur existe dans table, utilisé lors de la recherche d'un id pour une valeur de clé étrangère
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION: break;
						case C_MO_EDITION:
							ctrl_ExisteDansTable(message);
							break;
						case C_MO_INSERTION:
							ctrl_ExisteDansTable(message);
							break;
						case C_MO_IMPORTATION: break;
						default: break;
						}
					} else {
						
					}
					break;
				case 104: // controle un code postal
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION: break;
						case C_MO_EDITION:
							ctrl_CodePostal(message);
							break;
						case C_MO_INSERTION:
							ctrl_CodePostal(message);
							break;
						case C_MO_IMPORTATION: break;
						default: break;
						}
					} else {
						
					}
					break;
				case 105: // controle si une date à un format valide
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION: break;
						case C_MO_EDITION:
							ctrl_Date(message);
							break;
						case C_MO_INSERTION:
							ctrl_Date(message);
							break;
						case C_MO_IMPORTATION: break;
						default: break;
						}
					} else {
						
					}
					break;
				case 106: // controle si un montant qui doit être supérieur à 0 est supérieur à 0 !
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION: break;
						case C_MO_EDITION:
							ctrl_MontantPositif(message);
							break;
						case C_MO_INSERTION:
							ctrl_MontantPositif(message);
							break;
						case C_MO_IMPORTATION: break;
						default: break;
						}
					} else {
						
					}
					break;
				case 107: //controle si la nouvelle valeur n'est pas vide
					if(message.getModeObjet()!=null) {
						switch (message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:
							break;
						case C_MO_EDITION:
							ctrl_EstNonVide(message);
							break;
						case C_MO_INSERTION:
							ctrl_EstNonVide(message);
							break;
						case C_MO_IMPORTATION:
							break;
						default:
							break;
						}
					} else {
						
					}
					break;//
				case 108: //controle si la nouvelle valeur est du type boolean
					if(message.getModeObjet()!=null) {
						switch (message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:
							break;
						case C_MO_EDITION:
							ctrl_Boolean(message);
							break;
						case C_MO_INSERTION:
							ctrl_Boolean(message);
							break;
						case C_MO_IMPORTATION:
							break;
						default:
							break;
						}
					} else {
						
					}
					break;

				case 109: //controle si la nouvelle valeur ne contient que des Chiffres
					if(message.getModeObjet()!=null) {
						switch (message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:
							break;
						case C_MO_EDITION:
							if (!ctrl_ValeurQueDesChiffres(message,true))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
							break;
						case C_MO_INSERTION:
							if (!ctrl_ValeurQueDesChiffres(message,true))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
							break;
						case C_MO_IMPORTATION:
							break;
						default:
							break;
						}
					} else {
						
					}
					break;
				case 110: //controle si la nouvelle valeur ne contient pas des caractères non autorisés
					if(message.getModeObjet()!=null) {
						switch (message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:
							break;
						case C_MO_EDITION:
							if (!ctrl_ValeursKeyAutorisees(message))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
							break;
						case C_MO_INSERTION:
							if (!ctrl_ValeursKeyAutorisees(message))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
							break;
						case C_MO_IMPORTATION:
							break;
						default:
							break;
						}
					} else {
						
					}
					break;
				case 111: //controle si la nouvelle valeur n'est pas vide sauf si elle est null
					if(message.getModeObjet()!=null) {
					//dans le cas de valeur facultative suivant condition
						switch (message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:
							break;
						case C_MO_EDITION:
							if (message.getValeur()!=null)
								ctrl_EstNonVide(message);
							break;
						case C_MO_INSERTION:
							if (message.getValeur()!=null)
								ctrl_EstNonVide(message);
							break;
						case C_MO_IMPORTATION:
							break;
						default:
							break;
						}
					} else {
						
					}
					break;
				case 112: // Laisser numéro de controle pour pouvoir passer outre les controles !!
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:  break;
						case C_MO_EDITION: ctrl_Longueur(message); break;
						case C_MO_INSERTION: ctrl_Longueur(message); break;
						case C_MO_IMPORTATION: 
							ctrl_Longueur(message); 
							break;
						default: break;
						}
					} else {
						
					}
					break;
				case 113: // Laisser numéro de controle pour pouvoir passer outre les controles !!
					if(message.getModeObjet()!=null) {
						switch(message.getModeObjet().getMode()) {
						case C_MO_CONSULTATION:  break;
						case C_MO_EDITION: ctrl_Majuscule(message); break;
						case C_MO_INSERTION: ctrl_Majuscule(message); break;
						case C_MO_IMPORTATION: 
							ctrl_Majuscule(message); 
							break;
						default: break;
						}
					} else {
						
					}
					break;
				default:
					throw new ExceptLgr("JPACtrl_Application : Code de controle inconnu :["+String.valueOf(codeCtrl) + "]");
//				break;
				}
			}

		} else {
			logger.error("JPACtrl_Application (mode serveur = "+modeServeur+") : Champ ou Table inconnu dans CtrlBD.ini : "+key);
			throw new ExceptLgr("JPACtrl_Application (mode serveur = "+modeServeur+") : Champ ou Table inconnu dans CtrlBD.ini : "+key);
			
			//throw new ExceptLgr("JPACtrl_Application : Champ ou Table inconnu : ["+message.getNomTable() + "].["+message.getNomChamp() +"]");
		}
		//} catch(Exception e){
		//  e.printStackTrace();
		//}
	}

	/**
	 * Called by the garbage collector on an object when garbage collection
	 * determines that there are no more references to the object.
	 *
	 * @throws Throwable the <code>Exception</code> raised by this method
	 */
	protected void finalize() throws Throwable {
	}

	public boolean isModeServeur() {
		return modeServeur;
	}

	public void setModeServeur(boolean modeServeur) {
		this.modeServeur = modeServeur;
	}

}

