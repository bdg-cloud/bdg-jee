package fr.legrain.conformite.model;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.article.model.TaPrix;
import fr.legrain.general.model.TaFichier;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.script.LibScript;
import fr.legrain.validator.LgrHibernateValidated;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "ta_bareme")
public class TaBareme implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -960695967732847226L;
	@Transient
	static Logger logger = Logger.getLogger(TaBareme.class.getName());
//	public static final String REGEX_FORMULE_VALEUR_CALC = "(^|\\p{Punct})c\\d+\\p{Punct}?";
	/**
	 * (c15+c16+c17)-c14 => (c15      +c16         c17)           -c14, il peu rester des caratère {Punct} dans chacune des parties après la sélection
	 * utiliser encore \\p{Punct} pour garder uniquement c15, c16, c17 et c14
	 * 
	 */
	public static final String REGEX_FORMULE_VALEUR_CALC = "((^|\\p{Punct})(c|C)\\d+(\\p{Punct})?)|((c|C)\\d+(\\p{Punct})?)";
	public static final String REGEX_FORMULE_CARACTERE_PONCTUATION = "\\p{Punct}";

	private int idBareme;
	
	private String code;
	private String expressionVerifiee;
	private String cheminDoc;
	private byte[] blobFichier;
	private String actioncorrective;
	private Boolean forcage;
	private TaConformite taConformite;

	private String version;


	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaBareme() {
	}

	public TaBareme(int idBareme) {
		this.idBareme = idBareme;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_bareme", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_bareme",table = "ta_bareme", champEntite="idBareme", clazz = TaBareme.class)
	public int getIdBareme() {
		return this.idBareme;
	}

	public void setIdBareme(int idConformite) {
		this.idBareme = idConformite;
	}
	
	/**
	 * @return the blobFichierOriginal
	 */
	/*
	 * stockage des Blobs dans PostgreSQL :
	 * 
	 * -*- type SQL du champ : "oid" avec l'annotation JPA @Lob sur le champ de l'entitée 
	 * 		=> le champ stocke un pointeur vers la table système "pg_largeobject"
	 *   plus rapide pour les select et les jointures, pas de suppression automatique de l'objet (lo_unlink)
	 *   penser à utiliser -o ou --oid pour les dumps
	 *   
	 *   Exemple de tigger pour la suppression :
	 *   
	 *   CREATE OR REPLACE FUNCTION tbd_ta_bareme()
     *   RETURNS trigger AS
	 *	 $BODY$
	 *	 begin
	 *	  SELECT lo_unlink(old.blob_fichier);  -- deletes large object with OID xxx
	 *	  return null;   
	 *	 end
	 *	 $BODY$
	 *	  LANGUAGE plpgsql VOLATILE
	 *	  COST 100;
	 *	 ALTER FUNCTION tbd_ta_bareme()
	 *	  OWNER TO postgres;
	 *	
	 *	
	 *	CREATE TRIGGER tbd_ta_bareme
  	 *	  BEFORE DELETE
	 *	  ON ta_bareme
	 *	  FOR EACH ROW
	 *	  EXECUTE PROCEDURE tbd_ta_bareme();
	 *   
	 *   
	 *   
	 * -*- type SQL du champ : "bytea" sans l'annotation JPA ( @Lob sur le champ de l'entitée)
	 *     ==> le champ stocke les données binaires directement dans la table cible (octet_length())
	 *   plus simple mais moins efficace si les fichiers sont gros (select *, ou jointure)
	 */
//	@Lob
	@Column(name = "blob_fichier")
	@LgrHibernateValidated(champBd = "blob_fichier",table = "ta_bareme", champEntite="blobFichier", clazz = TaBareme.class)
	public byte[] getBlobFichier() {
		return blobFichier;
	}

	/**
	 * @param blobFichierOriginal the blobFichierOriginal to set
	 */

	public void setBlobFichier(byte[] blobFichier) {
		this.blobFichier = blobFichier;
	}

	
	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}



	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTCivilite) {
		this.quiCree = quiCreeTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTCivilite) {
		this.quandCree = quandCreeTCivilite;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTCivilite) {
		this.quiModif = quiModifTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTCivilite) {
		this.quandModif = quandModifTCivilite;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "expression_verifiee", length = 50)
	@LgrHibernateValidated(champBd = "expression_verifiee",table = "ta_bareme", champEntite="expressionVerifiee", clazz = TaBareme.class)
	public String getExpressionVerifiee() {
		return expressionVerifiee;
	}

	public void setExpressionVerifiee(String valeurDefaut) {
		this.expressionVerifiee = valeurDefaut;
	}

	@Column(name = "chemin_doc", length = 50)
	@LgrHibernateValidated(champBd = "chemin_doc",table = "ta_bareme", champEntite="cheminDoc", clazz = TaBareme.class)
	public String getCheminDoc() {
		return cheminDoc;
	}

	public void setCheminDoc(String deuxiemeValeur) {
		this.cheminDoc = deuxiemeValeur;
	}

	@Column(name = "action_corrective", length = 50)
	@LgrHibernateValidated(champBd = "action_corrective",table = "ta_bareme", champEntite="actioncorrective", clazz = TaBareme.class)
	public String getActioncorrective() {
		return actioncorrective;
	}

	public void setActioncorrective(String libelleConformite) {
		this.actioncorrective = libelleConformite;
	}

	@Column(name = "forcage")
	public Boolean getForcage() {
		return forcage;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_conformite")
	@LgrHibernateValidated(champBd = "id_conformite",table = "ta_bareme", champEntite="taConformite.idConformite", clazz = TaConformite.class)
	@XmlElement
	@XmlInverseReference(mappedBy="taConformites")
	public TaConformite getTaConformite() {
		return taConformite;
	}

	public void setTaConformite(TaConformite taConformite) {
		this.taConformite = taConformite;
	}

	public void setForcage(Boolean forcage) {
		this.forcage = forcage;
	}
	
	//TODO à fusioner avec ControleConformiteJSF.validation()
	public boolean verfierExpression(TaResultatConformite r) {
		//expressionVerifiee
		String VAL_CONSTATEE_DANS_EXPRESSION = "val";
		String expr = expressionVerifiee;
		boolean scriptInterpretable = true;
		
		
		expr = expr.replace("=", "==");
		expr = expr.replace("!==", "!=");
		expr = expr.replace("<==", "<=");
		expr = expr.replace(">==", ">=");
		
		expr = expr.replace(" et ", " && ");
		expr = expr.replace(" and ", " && ");
		expr = expr.replace(" ou ", " || ");
		expr = expr.replace(" or ", " || ");
		
		if(taConformite.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_TEXTE)
				&& !LibCalcul.isNumeric(r.getValeurConstatee())) {
			expr = expr.replace(VAL_CONSTATEE_DANS_EXPRESSION, "'"+r.getValeurConstatee()+"'");
			if(expr.contains("==")) {
				//en texte libre, s'il y a un =, val doit être égal à la chaine après le =
				expr = expr.replace(expr.substring(expr.indexOf("==")), "=='"+expr.substring(expr.indexOf("==")+2)+"'");
			}
		} else {
			//pas de ''
			if(expr.contains(VAL_CONSTATEE_DANS_EXPRESSION) && r.getValeurConstatee()!=null) {
				expr = expr.replace(VAL_CONSTATEE_DANS_EXPRESSION, r.getValeurConstatee());
			} else {
				scriptInterpretable = false;
			}
		}
		
		if(!LibCalcul.isNumeric(r.getValeurConstatee())) {
			if(taConformite.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_LISTE)
					|| taConformite.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_SIMPLE)
					){
				String[] valeurPossible = taConformite.getDeuxiemeValeur().split(";");
				for (String s : valeurPossible) {
					expr = expr.replace(s, "'"+s+"'");
				}
			}
		}
		
		if(!LibCalcul.isNumeric(r.getValeurConstatee())) {
			if(taConformite.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CHOIX_MULTIPLE)){
				//gestion de la liste de valeur sélectionné
				String[] v = r.getValeurConstatee().split(";");
				for (String s : v) {
					expr = expr.replace(s, "'"+s+"'");
				}
			}
		}
		
		if(taConformite.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_DATE)
			|| taConformite.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_HEURE)
			) {
			try {
				Date d = LibDate.stringToDate(r.getValeurConstatee());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if(taConformite.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)) {
			//http://imss-www.upmf-grenoble.fr/prevert/Prog/Java/CoursJava/expressionsRegulieres.html
			//http://www.regexplanet.com/advanced/java/index.html
			
			Pattern pattern = Pattern.compile(TaBareme.REGEX_FORMULE_VALEUR_CALC); 
			// cherche les occurrences c1,c535,c22 en début de formule ou dans la formule si elles sont précéder ou suivies de +-/*()=
		    Matcher matcher = pattern.matcher(expr);
		    // Check all occurrences
		    while (matcher.find()) {
		        String match = matcher.group().substring(1);
		        match = match.substring(0,match.length()-1);
//		        expr = expr.replace(matcher.group(), " [résultat du controle "+match+", ID "+match.substring(1)+"] ");
		        int idControle = LibConversion.stringToInteger(match.substring(1));
		        for(TaResultatConformite lr : r.getTaLot().getTaResultatConformites()) {
		        	if(lr.getTaConformite().getIdConformite()==idControle) {
		        		 expr = expr.replace(match, lr.getValeurConstatee());
		        	}
		        }

		    }
		}

		
		LibScript ls = new LibScript();
		Object res = null;
		
		try {
			if(scriptInterpretable) {
				
				//Remplacement pour les fonctions mathématiques
				//http://www.w3schools.com/jsref/jsref_obj_math.asp
				expr = expr.replace("abs(", "Math.abs(");
				
				expr = calculDeDate(expr);
				expr = calculDeHeure(expr);
				
				res = ls.evalScript(expr, LibScript.SCRIPT_JAVASCRIPT);
		//		res = ls.evalScript(expr, LibScript.SCRIPT_RUBY);
		//		res = ls.evalScript(expr, LibScript.SCRIPT_PYTHON);
		//		res = ls.evalScript(expr, LibScript.SCRIPT_GROOVY);
			}
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(res!=null) {
			return LibConversion.StringToBoolean(res.toString());
		} else {
			return false;
		}
	}
	
	static public String calculDeDate(String expr) {
		//String FONCTION_PATTERN ="\\w\\s*\\(\\s*(\\s*\\w+\\s*,?\\s*)*\\s*\\)";
		try {
			String nomFonction = "diffjour";
			String FONCTION_PATTERN ="diffjour\\s*\\(\\s*(\\s*(\\w|/)+\\s*,?\\s*)*\\s*\\)";

			Pattern pattern;
			Matcher matcher;
			pattern = Pattern.compile(FONCTION_PATTERN);
			matcher = pattern.matcher(expr);
			while (matcher.find()) {
				String match = matcher.group();
				String group = matcher.group();
				System.out.println("ControleConformiteJSF.calculDeDate()**** "+match);
				if(match.startsWith(nomFonction)){
					match = match.trim();
					match = match.replace(" ", "");
					match = match.replace(nomFonction+"(", "");
					match = match.replace(")", "");
					String param[] = match.split(",");
					if(param.length==2) {
						//OK
						Date d1 = LibDate.stringToDate(param[0]);
						Date d2 = LibDate.stringToDate(param[1]);

						LocalDate ld1 = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate ld2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

						long res = java.time.temporal.ChronoUnit.DAYS.between(ld1, ld2);
						res = Math.abs(res);
						String r = ""+res;
						expr = expr.replace(group, r);
					} else {
						//erreur
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expr;
	}
	
	static public String calculDeHeure(String expr) {
		//String FONCTION_PATTERN ="\\w\\s*\\(\\s*(\\s*\\w+\\s*,?\\s*)*\\s*\\)";
		try {
			String nomFonction = "diffheure";
			String FONCTION_PATTERN ="diffheure\\s*\\(\\s*(\\s*(\\w|/)+\\s*,?\\s*)*\\s*\\)";

			Pattern pattern;
			Matcher matcher;
			pattern = Pattern.compile(FONCTION_PATTERN);
			matcher = pattern.matcher(expr);
			while (matcher.find()) {
				String match = matcher.group();
				String group = matcher.group();
				System.out.println("ControleConformiteJSF.calculDeHeure()**** "+match);
				if(match.startsWith(nomFonction)){
					match = match.trim();
					match = match.replace(" ", "");
					match = match.replace(nomFonction+"(", "");
					match = match.replace(")", "");
					String param[] = match.split(",");
					if(param.length==2) {
						//OK
						Date d1 = LibDate.stringToDate(param[0]);
						Date d2 = LibDate.stringToDate(param[1]);

						LocalDate ld1 = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate ld2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

						long res = java.time.temporal.ChronoUnit.HOURS.between(ld1, ld2);
						res = Math.abs(res);
						String r = ""+res;
						expr = expr.replace(group, r);
					} else {
						//erreur
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expr;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		TaBareme objet = new TaBareme();
		try {
			objet.setActioncorrective(this.actioncorrective);
			objet.setBlobFichier(this.blobFichier);
			objet.setCheminDoc(this.cheminDoc);
			objet.setExpressionVerifiee(this.expressionVerifiee);
			objet.setForcage(forcage);		
			
		} catch (Exception e) {
			logger.error("", e);
		}
		// on renvoie le clone
		return objet;
	}
}
