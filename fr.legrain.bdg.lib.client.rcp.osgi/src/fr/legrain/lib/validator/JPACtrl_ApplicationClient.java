package fr.legrain.lib.validator;

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

import javax.naming.NamingException;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import fr.legrain.bdg.controle.service.remote.ITaControleServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.controle.model.TaControle;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.MessCtrlLgr;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.validator.ICtrlLgr;
import fr.legrain.validator.JPACtrlChampSimple;

/**
 * <p>Title: </p>
 * <p>Description: Controles généreaux de l'application de test.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Le Grain SA</p>
 * @author Le Grain SA
 * @version 1.0
 */

public class JPACtrl_ApplicationClient implements ICtrlLgr {

	static Logger logger = Logger.getLogger(JPACtrl_ApplicationClient.class.getName());
	protected Properties listeCtrl; //Liste créée à partir du fichier ini //TStringList
	protected static Map<String,TaControle> listeCtrlMap;
	private String fichierIni; //chemin du fichier ini contenant la liste des controle pour chacun des champs
	private boolean modeServeur = false;
	private JPACtrlChampSimple controleSimple = null;
	
	public void setFichierIni(String value) { //procedure EcritFichierIni(value : string);
		try {
			if (!new File(value).exists()) {
				throw new ExceptLgr("Le fichier .ini " + value + " est inexistant") ;
			}
			else {
				fichierIni = value;
				FileInputStream file =  new FileInputStream(fichierIni);
				listeCtrl.load(file); 
				file.close();
				
//				TaControle c = null;
//				for (Object k : listeCtrl.keySet()) {
//					c = new TaControle();
//					c.setControleDefaut((String)listeCtrl.get(k));
//					listeCtrlMap.put((String)k, c);
//				}
			}
		}
		catch (Exception e) {
			fichierIni = "";
			logger.error("setFFichierIni : "+"ERREUR",e);

			//abort;
		}
		finally {
			//Screen.Cursor := TmpCursor;
		}
	}

	public String getFichierIni() { //function LitFichierIni: string;
		return fichierIni;
	}

	public JPACtrl_ApplicationClient() {
//		listeCtrl = new Properties();
		
		if(listeCtrlMap==null) {
			listeCtrlMap = new HashMap<String, TaControle>();
			
			try {
				ITaControleServiceRemote controleServiceRemote = new EJBLookup<ITaControleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_CONTROLE, ITaControleServiceRemote.class.getName());
				
				List<TaControle> listeControle =  controleServiceRemote.selectAll();
				String key = "";
				for (TaControle taControle : listeControle) {
					if(taControle.getContexte()!=null && !taControle.getContexte().equals("")) {
						key = taControle.getContexte().trim()+"."+taControle.getChamp().trim();
					} else {
						key = taControle.getChamp();
					}
					listeCtrlMap.put(key , taControle);
				}
				
	//			TaControle newControle = null;
	//			for (TaControle taControle : listeControle) {
	//				String c = taControle.getChamp();
	////				if(c.indexOf("DTO")==-1 && c.indexOf(".")!=-1) {
	////					
	////					newControle = new TaControle();
	////					c = c.substring(0,c.indexOf("."))+"DTO"+c.substring(c.indexOf("."));
	////					System.out.println(taControle.getChamp()+" ===> "+c);
	////					newControle.setChamp(c);
	////					newControle.setContexte(taControle.getContexte());
	////					newControle.setClient(taControle.getClient());
	////					newControle.setServeur(taControle.getServeur());
	////					newControle.setControleDefaut(taControle.getControleDefaut());
	////					newControle.setControleUtilisateur(taControle.getControleUtilisateur());
	////					controleServiceRemote.enregistrerMerge(newControle);
	////				}
	//				
	//				if(c.indexOf("DTO")!=-1) {
	//					
	//					taControle.setControleDefaut(taControle.getControleDefaut().replaceAll("101;", ""));
	//					taControle.setControleDefaut(taControle.getControleDefaut().replaceAll("101", ""));
	//
	//					controleServiceRemote.enregistrerMerge(taControle,ITaControleServiceRemote.validationContext);
	//				}
	//			}
			
				System.err.println("A FAIRE : Indiquer l'emplacement des fichiers INI");
		//		setFFichierIni(Const.C_FICHIER_INI_CTRL);
				
	//			setFichierIni("/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_10a_E4/GestionCommerciale/Bd/CtrlBD.ini");
				
		//		setFichierIni_ID(Const.C_FICHIER_INI_IDBD);
		//		setFichierListeTitre(Const.C_FICHIER_LISTE_TITRE_BD);
				controleSimple = new JPACtrlChampSimple();
				
			} catch (NamingException e) {
				logger.error("",e);
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		if(controleSimple==null)controleSimple = new JPACtrlChampSimple();
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
		if(message==null)throw new ExceptLgr("Le contrôleur (MessCtrlLgr) est null",0,true,0);
		//key = message.getNomTable() + "." +message.getNomChamp();
		if(modeServeur) {
			key = message.getEntityClass().getSimpleName() + "." + message.getNomChamp();
	    } else {
	    	if(message.getContexte()!=null && !message.getContexte().equals("")) {
	    		//validation pour un contexte d'écran particulier
	    		key = message.getContexte() + "." + message.getEntityClass().getSimpleName() + "." + message.getNomChamp();
	    	} else {
	    		//pas de contexte écran, validation "par défaut" pour ce champ
	    		key = message.getEntityClass().getSimpleName() + "." + message.getNomChamp();
	    	}
	    }
//		if (listeCtrl.containsKey(key)) {
//			codeControleStr = (String) listeCtrl.get(key);
		if (listeCtrlMap.containsKey(key)) {
			codeControleStr = (String) listeCtrlMap.get(key).getControleDefaut();
			for(int j=0; j<codeControleStr.split(";").length; j++) {
				codeCtrl = LibConversion.stringToInteger(codeControleStr.split(";")[j]);
				switch(codeCtrl) {
				case 100: // Laisser numéro de controle pour pouvoir passer outre les controles !!
//					switch(message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:  break;
//					case C_MO_EDITION:  break;
//					case C_MO_INSERTION:  break;
//					case C_MO_IMPORTATION:break;
//					default: break;
//					}
					break;
				case 101: // controle d'unicité
////					switch(message.getModeObjet().getMode()) {
////					case C_MO_CONSULTATION: break;
////					case C_MO_EDITION:
////						ctrl_ExistePasDansTableEx(message);
////						break;
////					case C_MO_INSERTION:
//						ctrl_ExistePasDansTable(message);
//						break;
////					case C_MO_IMPORTATION: break;
////					default: break;
////					}
////					//MessageDlg('TAssoCtrl : code ['+IntToStr(CodeCtrl)+']', mtWarning, [mbOK], 0);
					break;
				case 102:
					//message.setMsgID("C_90001");
					controleSimple.ctrl_EstNonVide(message);
					//MessageDlg('TAssoCtrl : code ['+IntToStr(CodeCtrl)+']', mtWarning, [mbOK], 0);
					break;
				case 103: // controle si valeur existe dans table, utilisé lors de la recherche d'un id pour une valeur de clé étrangère
//					switch(message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION: break;
//					case C_MO_EDITION:
//						ctrl_ExisteDansTable(message);
//						break;
//					case C_MO_INSERTION:
					
					//ctrl_ExisteDansTable(message); //ejb commentaire
					System.out.println("EJB => suspresion du Controle 103, controle si valeur existe dans table, utilisé lors de la recherche d'un id pour une valeur de clé étrangère");
						break;
//					case C_MO_IMPORTATION: break;
//					default: break;
//					}
//					//JOptionPane.showMessageDialog(null,"TTestSpecifiqueCtrl : code NON FINI["+String.valueOf(codeCtrl) + "]","ERREUR",JOptionPane.WARNING_MESSAGE); //mtWarning
//					break;
				case 104: // controle un code postal
//					switch(message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION: break;
//					case C_MO_EDITION:
//						ctrl_CodePostal(message);
//						break;
//					case C_MO_INSERTION:
					controleSimple.ctrl_CodePostal(message);
						break;
//					case C_MO_IMPORTATION: break;
//					default: break;
//					}
//					//MessageDlg('TAssoCtrl : code NON FINI ['+IntToStr(CodeCtrl)+']', mtWarning, [mbOK], 0);
//					break;
				case 105: // controle si une date à un format valide
//					switch(message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION: break;
//					case C_MO_EDITION:
//						ctrl_Date(message);
//						break;
//					case C_MO_INSERTION:
					controleSimple.ctrl_Date(message);
						break;
//					case C_MO_IMPORTATION: break;
//					default: break;
//					}
//					//MessageDlg('TAssoCtrl : code NON FINI ['+IntToStr(CodeCtrl)+']', mtWarning, [mbOK], 0);
//					break;
				case 106: // controle si un montant qui doit être supérieur à 0 est supérieur à 0 !
//					switch(message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION: break;
//					case C_MO_EDITION:
//						ctrl_MontantPositif(message);
//						break;
//					case C_MO_INSERTION:
					controleSimple.ctrl_MontantPositif(message);
						break;
//					case C_MO_IMPORTATION: break;
//					default: break;
//					}
//					//MessageDlg('TAssoCtrl : code NON FINI ['+IntToStr(CodeCtrl)+']', mtWarning, [mbOK], 0);
//					break;
				case 107: //controle si la nouvelle valeur n'est pas vide
//					switch (message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:
//						break;
//					case C_MO_EDITION:
//						ctrl_EstNonVide(message);
//						break;
//					case C_MO_INSERTION:
					controleSimple.ctrl_EstNonVide(message);
						break;
//					case C_MO_IMPORTATION:
//						break;
//					default:
//						break;
//					}
//					break;//
				case 108: //controle si la nouvelle valeur est du type boolean
//					switch (message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:
//						break;
//					case C_MO_EDITION:
//						ctrl_Boolean(message);
//						break;
//					case C_MO_INSERTION:
					controleSimple.ctrl_Boolean(message);
						break;
//					case C_MO_IMPORTATION:
//						break;
//					default:
//						break;
//					}
//					break;//

				case 109: //controle si la nouvelle valeur ne contient que des Chiffres
//					switch (message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:
//						break;
//					case C_MO_EDITION:
//						if (!ctrl_ValeurQueDesChiffres(message,true))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
//						break;
//					case C_MO_INSERTION:
						if (!controleSimple.ctrl_ValeurQueDesChiffres(message,true))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
						break;
//					case C_MO_IMPORTATION:
//						break;
//					default:
//						break;
//					}
//					break;//
				case 110: //controle si la nouvelle valeur ne contient pas des caractères non autorisés
//					switch (message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:
//						break;
//					case C_MO_EDITION:
//						if (!ctrl_ValeursKeyAutorisees(message))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
//						break;
//					case C_MO_INSERTION:
						if (!controleSimple.ctrl_ValeursKeyAutorisees(message))throw new ExceptLgr(message,message.getMessageAffiche(),message.getCodeErreur(),(message.isAfficheMessage() && !message.isDejaAffiche()),JOptionPane.ERROR_MESSAGE);
						break;
//					case C_MO_IMPORTATION:
//						break;
//					default:
//						break;
//					}
//					break;// 
				case 111: //controle si la nouvelle valeur n'est pas vide sauf si elle est null
					//dans le cas de valeur facultative suivant condition
//					switch (message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:
//						break;
//					case C_MO_EDITION:
//						if (message.getValeur()!=null)
//							ctrl_EstNonVide(message);
//						break;
//					case C_MO_INSERTION:
						if (message.getValeur()!=null)
							controleSimple.ctrl_EstNonVide(message);
						break;
//					case C_MO_IMPORTATION:
//						break;
//					default:
//						break;
//					}
//					break;//
				case 112: // Laisser numéro de controle pour pouvoir passer outre les controles !!
//					switch(message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:  break;
//					case C_MO_EDITION: ctrl_Longueur(message); break;
//					case C_MO_INSERTION: ctrl_Longueur(message); break;
//					case C_MO_IMPORTATION: 
					
					//ctrl_Longueur(message); //ejb commentaire 
					//controleSimple.ctrl_Longueur(message);
					System.out.println("EJB => suspresion du Controle 112, validation de la longueur d'un champ par rapport au DDL de la base de données");
					
//						break;
//					default: break;
//					}
					break;
				case 113: // Laisser numéro de controle pour pouvoir passer outre les controles !!
//					switch(message.getModeObjet().getMode()) {
//					case C_MO_CONSULTATION:  break;
//					case C_MO_EDITION: ctrl_Majuscule(message); break;
//					case C_MO_INSERTION: ctrl_Majuscule(message); break;
//					case C_MO_IMPORTATION: 
					controleSimple.ctrl_Majuscule(message); 
//						break;
//					default: break;
//					}
					break;
				default:
//					JOptionPane.showMessageDialog(null,"TTestSpecifiqueCtrl : Code de controle inconnu :["+String.valueOf(codeCtrl) + "]","ERREUR",JOptionPane.WARNING_MESSAGE); //mtWarning
					throw new ExceptLgr("Code de controle inconnu :["+String.valueOf(codeCtrl) + "] (JPACtrl_ApplicationClient)");
//				break;
				}
			}

		} else {
			//JOptionPane.showMessageDialog(null,"TTestSpecifiqueCtrl : Champ ou Table inconnu : ["+message.getNomTable() + "].["+message.getNomChamp() +"]","ERREUR",JOptionPane.WARNING_MESSAGE); //mtWarning
			logger.error("Champ ou Table inconnu : ["+key+"] aucun controle défini pour ce champ (JPACtrl_ApplicationClient)");
			//throw new ExceptLgr("Champ ou Table inconnu : ["+key+"] aucun controle défini pour ce champ (JPACtrl_ApplicationClient)");
		}
		//} catch(Exception e){
		//  e.printStackTrace();
		//abort;
		//}
//		*/
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

