package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.script.ScriptException;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.conformite.model.TaBareme;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.conformite.model.TaTypeConformite;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.script.LibScript;

public class ControleConformiteJSF implements Serializable {

	private static final long serialVersionUID = 7327857850862911647L;
	
	private TaConformite c;
	private TaResultatConformite r;
	
	private TaUtilisateurDTO utilisateurControleurDTO;
//	private TaUtilisateurDTO utilisateurServiceQualiteDTO;
//	private TaUtilisateurDTO utilisateurDirectionDTO;
	 
	private String valeurTexte;
	private Boolean valeurBool;
	private Boolean disabled = false;
	private Date date = new Date();
	private List<String> valeurPossible;
	private List<String> valeurResultat;
	
	private Boolean nouveauControlePourCorrigerValeurFausse;
	
	private List<TaResultatConformite> historique;
	
	private Date dateControle;
	
	private String action;
	
	private LgrDozerMapper<TaUtilisateur,TaUtilisateurDTO> mapperModelToUIUtilisateur  = new LgrDozerMapper<TaUtilisateur,TaUtilisateurDTO>();
	
	public ControleConformiteJSF(TaConformite c) {
		this(c,new ArrayList<TaResultatConformite>(),null);
	}
	
	public ControleConformiteJSF(TaConformite c, Boolean nouveauControlePourCorrigerValeurFausse) {
		this(c,new ArrayList<TaResultatConformite>(),nouveauControlePourCorrigerValeurFausse);
	}
	
	public ControleConformiteJSF(TaConformite c, List<TaResultatConformite> historiqueResultatConformiteR, Boolean nouveauControlePourCorrigerValeurFausse) {
		this(c,(historiqueResultatConformiteR!=null && !historiqueResultatConformiteR.isEmpty()) ? historiqueResultatConformiteR.get(0): null,nouveauControlePourCorrigerValeurFausse);
	}
	
	public ControleConformiteJSF(TaConformite c, TaResultatConformite r,Boolean nouveauControlePourCorrigerValeurFausse) {
		this(c,r,null,nouveauControlePourCorrigerValeurFausse);
	}
	
	public ControleConformiteJSF(TaConformite c, TaResultatConformite r) {
		this(c,r,null,null);
	}
	
	public ControleConformiteJSF(TaConformite c, TaResultatConformite r, List<TaResultatConformite> historiqueResultatConformiteR) {
		this(c,r,historiqueResultatConformiteR,null);
	}
	
	public ControleConformiteJSF(TaConformite c, TaResultatConformite r, List<TaResultatConformite> historiqueResultatConformiteR, Boolean nouveauControlePourCorrigerValeurFausse) {
		this.c = c;
		this.r = r;
		this.historique = historiqueResultatConformiteR;
		this.nouveauControlePourCorrigerValeurFausse = nouveauControlePourCorrigerValeurFausse;
		
		if(r==null && historique!=null && !historique.isEmpty()) {
			r=historique.get(0);
			this.r = r;
		}
		
		if(r !=null && r.getTaUtilisateurControleur()!=null) {
			utilisateurControleurDTO = new TaUtilisateurDTO();
			mapperModelToUIUtilisateur.map(r.getTaUtilisateurControleur(), utilisateurControleurDTO);
		}
		if(r!=null) {
			dateControle = r.getDateControle();
		}
		
		if(c!=null && (c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_LISTE)
				|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_SIMPLE)
				|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_MULTIPLE))
				) {
			valeurPossible = new ArrayList<String>();
			String[] valeurs = c.getDeuxiemeValeur().split(";");
			for (int i = 0; i < valeurs.length; i++) {
				valeurPossible.add(valeurs[i]);
			}
		}
		
//		if(c.getCtrlFacultatif()!=null && c.getCtrlFacultatif()) {
//			action = TaStatusConformite.C_TYPE_ACTION_VIDE_FACULTATIF;
//		} else {
			action = TaStatusConformite.C_TYPE_ACTION_VIDE;
//		}
		
		//TODO Réaffecté les valeurs d'un résultat précédent s'il existe
		if(r!=null) {
			//si valeur texte
			if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_LISTE)
					|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_SIMPLE)
					|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_TEXTE)
					|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_NUMERIQUE)
					|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)
					) {
				setValeurTexte(r.getValeurConstatee());
			} else if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_DATE)) {
				//setDate(r.getValeurConstatee());
				try {
					date = LibDate.stringToDate(r.getValeurConstatee());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_HEURE)) {
				if(r.getValeurConstatee()!=null) {
					date = new Date(Long.valueOf(r.getValeurConstatee()));
				}
			} else if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_MULTIPLE)) {
				if(r.getValeurConstatee()!=null) {
					String[] v = r.getValeurConstatee().split(";");
					if(valeurResultat==null) {
						valeurResultat = new ArrayList<String>();
					} else {
						valeurResultat.clear();
					}
					for (String s : v) {
						valeurResultat.add(s);
					}
				}
			}			
			
			if(r.resultatSaisi()) { //une valeur a bien été saisie pour ce controle
				disabled = true;
//				if(r.getTaResultatCorrections()==null || r.getTaResultatCorrections().isEmpty()){
//					if(c.getTaBaremes()!=null && !c.getTaBaremes().isEmpty()) {
//						action = TaStatusConformite.C_TYPE_ACTION_OK;
						if(historique!=null && !historique.isEmpty() && historique.size()>=2) {
//							action = TaStatusConformite.C_TYPE_ACTION_CORRIGE;
							//action = historique.get(historique.size()-1).getTaStatusConformite().getCodeStatusConformite();
							
//							if(historique.get(0).getTaStatusConformite().getCodeStatusConformite().equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER)) {
//								action = TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE;
								action = historique.get(0).getTaStatusConformite().getCodeStatusConformite();
//							} else if(historique.get(0).getTaStatusConformite().getCodeStatusConformite().equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER_BLOQUANT)) {
//								action = TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE_BLOQUANT;
//							}
//						}
//					} else {
//						if(c.getCtrlFacultatif()!=null && c.getCtrlFacultatif()) {
//							action = TaStatusConformite.C_TYPE_ACTION_VIDE_FACULTATIF;
//						} else {
//							action = TaStatusConformite.C_TYPE_ACTION_VIDE;
//						}
//					}
				} else {
//					boolean dejaCorrige = true;
//					for(TaResultatCorrection correction : r.getTaResultatCorrections()) {
//						if( (correction.getEffectuee()==null || !correction.getEffectuee()) 
//								&& (correction.getObservation()==null || correction.getObservation().equals("")) 
//								) {
//							dejaCorrige = false;
//						}
//					}
//					if(dejaCorrige) {
//						action = TaStatusConformite.C_TYPE_ACTION_CORRIGE;
//						if(historique!=null && !historique.isEmpty() && historique.size()<2) {
//							action = TaStatusConformite.C_TYPE_ACTION_OK;
//						}
//					} else {
//						action = TaStatusConformite.C_TYPE_ACTION_A_CORRIGER;
//					}
					if(r.getTaStatusConformite()!=null && r.getTaStatusConformite().getCodeStatusConformite()!=null) {
						action = r.getTaStatusConformite().getCodeStatusConformite();
					}
				}
			} else {
				//aucune valeur n'a encore été saisie pour ce controle
//				if(c.getCtrlFacultatif()!=null && c.getCtrlFacultatif()) {
//					action = TaStatusConformite.C_TYPE_ACTION_VIDE_FACULTATIF;
//				} else {
					action = TaStatusConformite.C_TYPE_ACTION_VIDE;
//				}
				
				if(c.getValeurDefaut()!=null){ //pas de résultat saisi ==> initialisation des composant JSF avec les valeurs par défaut
					if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_LISTE)
							|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_SIMPLE)
							|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_TEXTE)
							|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_NUMERIQUE)
							|| c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)
							) {
						setValeurTexte(c.getValeurDefaut());
					} else if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_DATE)) {
						try {
							date = LibDate.stringToDate(c.getValeurDefaut());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_HEURE)) {
						if(!c.getValeurDefaut().equals("")) {
							date = new Date(Long.valueOf(c.getValeurDefaut()));
						}
					} else if(c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_MULTIPLE)) {
						String[] v = c.getValeurDefaut().split(";");
						if(valeurResultat==null) {
							valeurResultat = new ArrayList<String>();
						} else {
							valeurResultat.clear();
						}
						for (String s : v) {
							valeurResultat.add(s);
						}
					}	
				}
			
			}
		}
		
		
	}
	
	public String validation(TaLot taLot) {
		return validation(taLot,null);
	}
	
	public String validation(TaLot taLot, List<ControleConformiteJSF> listeControle) {
		return validation(taLot,listeControle,null);
	}
	
	//TODO //TODO à fusioner avec TaBareme.verfierExpression()
	public String validation(TaLot taLot, List<ControleConformiteJSF> listeControle, List<ControleConformiteJSF> listeControleTemporaire) {
//		//cropsal
//		val='Bon'
//		val>=2 and val<=5
//		val<-0.15
//		val>=2 and val<=4.4
//		//Micouleau	
//		Val absolue >0.5

		try {
			System.out.println("Validation du controle : "+getC().getLibelleConformite()+" avec la valeur : ("+getValeurTexte()+")("+getValeurBool()+")("+getDate()+")("+getValeurResultat()+")");
			
			boolean controleValide = false;
			boolean scriptInterpretable = true;
			
			if(r==null) {
				r = new TaResultatConformite();
			}
			//TODO faire des tests suivant les type de composant pour prendre le texte, la date, le booleen ou la liste
			if(getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_TEXTE)
					|| getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_LISTE)
					|| getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_NUMERIQUE)
					|| getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_SIMPLE)
					
					) {
				r.setValeurConstatee(getValeurTexte());
			} else if(getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)) {
				//http://imss-www.upmf-grenoble.fr/prevert/Prog/Java/CoursJava/expressionsRegulieres.html
				//http://www.regexplanet.com/advanced/java/index.html
				
				//////////
				boolean debugExpression = true;
				if(debugExpression) {System.out.println("****--------------------********debugExpression*************** ID lot : "+taLot.getIdLot());}
	
				Pattern pattern = Pattern.compile(TaBareme.REGEX_FORMULE_VALEUR_CALC); 
				// cherche les occurrences c1,c535,c22 en début de formule ou dans la formule si elles sont précéder ou suivies de +-/*()=
				String expr = getC().getDeuxiemeValeur();
				
				if(debugExpression) {System.out.println("************debugExpression*************** : "+expr);}
				
				expr = expr.replace("=", "==");
				expr = expr.replace("!==", "!=");
				expr = expr.replace("<==", "<=");
				expr = expr.replace(">==", ">=");
				
				expr = expr.replace(" et ", " && ");
				expr = expr.replace(" and ", " && ");
				expr = expr.replace(" ou ", " || ");
				expr = expr.replace(" or ", " || ");
				
				if(debugExpression) {System.out.println("************debugExpression*************** : "+expr);}
				
				
				if(debugExpression) {System.out.println("************debugExpression*************** : "+expr);}
				
				if(expr != null && !expr.equals("")) {
					Matcher matcher = pattern.matcher(expr);
					// Check all occurrences
					while (matcher.find()) {
						String match = matcher.group();
						match = match.replaceAll("(\\p{Punct})", ""); //supprime le c dans cXXX (ex:c41=>41) et supprime les + - * / ...
						match = match.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("c", "").replaceAll("C", "");
						//match = match.substring(0,match.length()-1);
						//			        expr = expr.replace(matcher.group(), " [résultat du controle "+match+", ID "+match.substring(1)+"] ");
						//int idControle = LibConversion.stringToInteger(match.substring(1));
						int idControle = LibConversion.stringToInteger(match);
	//					if(r.getTaLot()!=null && r.getTaLot().getTaResultatConformites()!=null) {
	//						for(TaResultatConformite lr : r.getTaLot().getTaResultatConformites()) {
	//							if(lr.getTaConformite().getIdConformite()==idControle) {
	//								expr = expr.replace(match, lr.getValeurConstatee());
	//							}
	//						}
	//					}
						if(debugExpression) {System.out.println("************debugExpression***********match **** : "+match);}
						if(listeControle!=null) {
							boolean trouve = false;
							if(listeControleTemporaire!=null) {
								//Calcul à partir des valeur controles qui ne sont pas encore enregistré : saisi suite à une action corrective
							
								for(ControleConformiteJSF lr : listeControleTemporaire) {
									if(debugExpression) {System.out.println("************debugExpression********* ID ctrl recherché **** : "+idControle+" dispo => "+lr.getC().getIdConformite());}
									if(lr.getC().getIdConformite()==idControle) {
										if(debugExpression) {System.out.println("************debugExpression********* ID ctrl trouvé ");}
										trouve = true;
										if(lr.getR()!=null && lr.getR().getValeurConstatee()!=null && !lr.getR().getValeurConstatee().equals("")) {
											match = matcher.group().replaceAll("(\\p{Punct})", "");
											expr = expr.replace(match, lr.getR().getValeurConstatee());
//											if(lr.getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_DATE)) {
//												//https://stackoverflow.com/questions/3224834/get-difference-between-2-dates-in-javascript
//												//c'est une valeur date qu'il faut utiliser dans l'exression
//												//var _MS_PER_DAY = 1000 * 60 * 60 * 24;
//												//var timeDiff = Math.abs(date2.getTime() - date1.getTime());
//												//var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
//												String d = "var dateObject = new Date("+lr.getR().getValeurConstatee()+");";
//											}
											if(debugExpression) {System.out.println("************debugExpression***********match/replace du résultat **** : "+match+" => "+expr);}
										} else {
											scriptInterpretable = false;
											System.out.println("****** Pas de valeur pour le controle avec l'id "+idControle+" dans la liste de controle de l'article");
										}
									}
								}
							}
							for(ControleConformiteJSF lr : listeControle) {
								if(debugExpression) {System.out.println("************debugExpression********* ID ctrl recherché **** : "+idControle+" dispo => "+lr.getC().getIdConformite());}
								if(lr.getC().getIdConformite()==idControle) {
									if(debugExpression) {System.out.println("************debugExpression********* ID ctrl trouvé ");}
									trouve = true;
									if(lr.getR()!=null && lr.getR().getValeurConstatee()!=null && !lr.getR().getValeurConstatee().equals("")) {
										match = matcher.group().replaceAll("(\\p{Punct})", "");
										expr = expr.replace(match, lr.getR().getValeurConstatee());
										if(debugExpression) {System.out.println("************debugExpression***********match/replace du résultat **** : "+match+" => "+expr);}
									} else {
										scriptInterpretable = false;
										System.out.println("****** Pas de valeur pour le controle avec l'id "+idControle+" dans la liste de controle de l'article");
									}
								}
							}
							
							if(!trouve){
								scriptInterpretable = false;
								System.out.println("********* ------ Pas de controle avec l'id "+idControle+" dans la liste de controle de l'article");
							}
						}
	
					}
					
					LibScript ls = new LibScript();
					Object res = null;
					
					if(debugExpression) {System.out.println("************debugExpression*************** scriptInterpretable : "+scriptInterpretable);}
					if(scriptInterpretable) {
						//Remplacement pour les fonctions mathématiques
						//http://www.w3schools.com/jsref/jsref_obj_math.asp
						expr = expr.replace("abs(", "Math.abs(");
						
						expr = TaBareme.calculDeDate(expr);
						expr = TaBareme.calculDeHeure(expr); //a ajouter dans le calcul de TaBareme aussi
						
//						res = ls.evalScript(expr, LibScript.SCRIPT_JAVASCRIPT);
//						res = ls.evalScript(expr, LibScript.SCRIPT_RUBY);
//						res = ls.evalScript(expr, LibScript.SCRIPT_PYTHON);
						res = ls.evalScript(expr, LibScript.SCRIPT_GROOVY);
						
						//utiliser Apache JEXL ?
					}
					
					if(res!=null) {
						r.setValeurConstatee(res.toString());
						if(debugExpression) {System.out.println("************debugExpression*************** setValeurConstatee : "+res.toString());}
					} else {
						r.setValeurConstatee(null);
						if(debugExpression) {System.out.println("************debugExpression*************** setValeurConstatee : null");}
					}
					setValeurTexte(r.getValeurConstatee());
				}
				if(debugExpression) {System.out.println("****--------------------********debugExpression*************** : ");}
			} else if(getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_MULTIPLE)) {
				String resultat = "";
				if(!getValeurResultat().isEmpty()) {
					for (String s : getValeurResultat()) {
						resultat += s+";";
					}
					resultat = resultat.substring(0,resultat.length()-1);//pour supprimer le dernier ;
				}
				r.setValeurConstatee(resultat);
			} else if(getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_HEURE)) {
				r.setValeurConstatee(String.valueOf(getDate().getTime()));
			} else if(getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_DATE)
					) {
				r.setValeurConstatee(LibDate.dateToString(getDate()));
			}
			
			r.setTaConformite(c);
			r.setTaLot(taLot);
			
			if(r.resultatSaisi()) {
				
				boolean tousBaremeValide = true;
				if(scriptInterpretable && c.getTaBaremes()!=null && !c.getTaBaremes().isEmpty()) {
					for(TaBareme b : c.getTaBaremes()) {
						controleValide = b.verfierExpression(r);
						
						
							TaResultatCorrection corr = r.chercheCorrectionPourBareme(b);
							if(corr==null) {
								corr = new TaResultatCorrection();
								corr.setTaResultatConformite(r);
								corr.setTaBareme(b);
								r.getTaResultatCorrections().add(corr);
							}
							if(!controleValide) {
								//ajoute une correction
								tousBaremeValide = false;
							} else {
								//corr.setEffectuee(true); 
							}
	//					} else {
	//						//supprime des corrections
	//						r.getTaResultatCorrections().clear();
	//					}
					}
					
		//			if(!controleValide) {
					if(!tousBaremeValide) {
//						if(c.getCtrlBloquant()!=null && c.getCtrlBloquant()) {
//							action = TaStatusConformite.C_TYPE_ACTION_A_CORRIGER_BLOQUANT;
//						} else {
							if(nouveauControlePourCorrigerValeurFausse!=null && nouveauControlePourCorrigerValeurFausse) {
								//la correction ne fonctionne pas => non corrige
								action = TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE; 
							} else {
								action = TaStatusConformite.C_TYPE_ACTION_A_CORRIGER;
							}
//						}
					} else {
						if(nouveauControlePourCorrigerValeurFausse!=null && nouveauControlePourCorrigerValeurFausse) {
							action = TaStatusConformite.C_TYPE_ACTION_CORRIGE;
						} else {
							action = TaStatusConformite.C_TYPE_ACTION_OK;
						}
					}
				} else {
					//pas de barème
					TaResultatCorrection corr = null;
					if(corr==null) {
						corr = new TaResultatCorrection();
						corr.setTaResultatConformite(r);
						//corr.setTaBareme(b);
						r.getTaResultatCorrections().add(corr);
					}
//					if(c.getCtrlFacultatif()!=null && c.getCtrlFacultatif()) {
//						action = TaStatusConformite.C_TYPE_ACTION_VIDE_FACULTATIF;
//					} else {
//						action = TaStatusConformite.C_TYPE_ACTION_VIDE;
//					}
					action = TaStatusConformite.C_TYPE_ACTION_OK;
				}
			} else {
//				if(c.getCtrlFacultatif()!=null && c.getCtrlFacultatif()) {
//					action = TaStatusConformite.C_TYPE_ACTION_VIDE_FACULTATIF;
//				} else {
					action = TaStatusConformite.C_TYPE_ACTION_VIDE;
//				}
			}
			
			return action;
		} catch(ScriptException e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Fabrication", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getC().getLibelleConformite(), e.getMessage()));
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Fabrication", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getC().getLibelleConformite(), e.getMessage()));
		}
		
		return action;
	}
	
	
	
	public void validationChampsCalcules(TaLot taLot, List<ControleConformiteJSF> listeControle) {
		validationChampsCalcules(taLot,listeControle,null);
	}
	
	public void validationChampsCalcules(TaLot taLot, List<ControleConformiteJSF> listeControle,List<ControleConformiteJSF> listeControleTemporaire) {
		for (ControleConformiteJSF controleConformiteJSF : listeControle) {
			if(controleConformiteJSF.getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)) {
				controleConformiteJSF.validation(taLot,listeControle,listeControleTemporaire);
			}
		}
	}

	public TaConformite getC() {
		return c;
	}

	public void setC(TaConformite c) {
		this.c = c;
	}

	public String getValeurTexte() {
		return valeurTexte;
	}

	public void setValeurTexte(String valeurTexte) {
		this.valeurTexte = valeurTexte;
	}

	public Boolean getValeurBool() {
		return valeurBool;
	}

	public void setValeurBool(Boolean valeurBool) {
		this.valeurBool = valeurBool;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getValeurPossible() {
		return valeurPossible;
	}

	public void setValeurPossible(List<String> valeurPossible) {
		this.valeurPossible = valeurPossible;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<String> getValeurResultat() {
		return valeurResultat;
	}

	public void setValeurResultat(List<String> valeurResultat) {
		this.valeurResultat = valeurResultat;
	}

	public TaResultatConformite getR() {
		return r;
	}

	public void setR(TaResultatConformite r) {
		this.r = r;
	}

	public TaUtilisateurDTO getUtilisateurControleurDTO() {
		return utilisateurControleurDTO;
	}

	public void setUtilisateurControleurDTO(TaUtilisateurDTO utilisateurDTO) {
		this.utilisateurControleurDTO = utilisateurDTO;
	}

	public Date getDateControle() {
		return dateControle;
	}

	public void setDateControle(Date dateControle) {
		this.dateControle = dateControle;
	}

	public List<TaResultatConformite> getHistorique() {
		return historique;
	}

	public void setHistorique(List<TaResultatConformite> historique) {
		this.historique = historique;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

//	public TaUtilisateurDTO getUtilisateurServiceQualiteDTO() {
//		return utilisateurServiceQualiteDTO;
//	}
//
//	public void setUtilisateurServiceQualiteDTO(TaUtilisateurDTO utilisateurServiceQualiteDTO) {
//		this.utilisateurServiceQualiteDTO = utilisateurServiceQualiteDTO;
//	}
//
//	public TaUtilisateurDTO getUtilisateurDirectionDTO() {
//		return utilisateurDirectionDTO;
//	}
//
//	public void setUtilisateurDirectionDTO(TaUtilisateurDTO utilisateurDirectionDTO) {
//		this.utilisateurDirectionDTO = utilisateurDirectionDTO;
//	}

}
