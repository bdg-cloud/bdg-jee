package fr.legrain.gestCom.librairiesEcran.swt;

import javax.swing.JComponent;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryDataSet;

import fr.legrain.lib.data.IModelGeneral;
import fr.legrain.lib.gui.ParamAffiche;

public class ParamAfficheAideRechercheSWT extends ParamAffiche {
	private QueryDataSet query = null;
	private Database db = null;
	private String queryString = null;
	private String champsRecherche = null;
	private String champsIdentifiant = null;
	//private Class refCreation = null;
	private JComponent focusDefaut = null;
	private String debutRecherche = null;
	private int[] hiddenColumns = null;
	private String titreRecherche = null;
	private IModelGeneral model = null;
	private Class<?> typeObjet = null;
	private Class<?> typeEntite = null;
	private EJBBaseControllerSWTStandard controllerAppelant = null;
	private String message = null;
	private String JPQLQuery = null;
//	private Boolean affichageAideRemplie = true;
	private Boolean forceAffichageAideRemplie = true;
	
	private Boolean afficheDetail=true;
	private Boolean afficheNouveau=null;
	
	private Shell shellCreation;
	private EJBBaseControllerSWTStandard refCreationSWT = null;
	private String cleListeTitre = null;
	private ParamAffiche paramEcranCreation = null;
	
	private IEditorPart editorCreation = null;
	private String editorCreationId = null;
	private IEditorInput editorInputCreation = null;

	public ParamAffiche getParamEcranCreation() {
		return paramEcranCreation;
	}

	public void setParamEcranCreation(ParamAffiche paramEcranCreation) {
		this.paramEcranCreation = paramEcranCreation;
	}

	
	public JComponent getFocusDefaut() {
		return focusDefaut;
	}
	public void setFocusDefaut(JComponent focusDefaut) {
		this.focusDefaut = focusDefaut;
	}	

	public QueryDataSet getQuery() {
		return query;
	}

	public void setQuery(QueryDataSet query) {
		this.query = query;
	}

	public String getChampsRecherche() {
		return champsRecherche;
	}

	public void setChampsRecherche(String champsRecherche) {
		this.champsRecherche = champsRecherche;
	}

	public String getDebutRecherche() {
		return debutRecherche;
	}

	public void setDebutRecherche(String debutRecherche) {
		this.debutRecherche = debutRecherche;
	}

	public int[] getHiddenColumns() {
		return hiddenColumns;
	}

	public void setHiddenColumns(int[] hiddenColumns) {
		this.hiddenColumns = hiddenColumns;
	}

	public String getChampsIdentifiant() {
		return champsIdentifiant;
	}

	public void setChampsIdentifiant(String champsIdentifiant) {
		this.champsIdentifiant = champsIdentifiant;
	}

	public String getTitreRecherche() {
		return titreRecherche;
	}

	public void setTitreRecherche(String titreRecherche) {
		this.titreRecherche = titreRecherche;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public Database getDb() {
		return db;
	}

	public void setDb(Database db) {
		this.db = db;
	}

	public IModelGeneral getModel() {
		return model;
	}

	public void setModel(IModelGeneral model) {
		this.model = model;
	}

	public Class<?> getTypeObjet() {
		return typeObjet;
	}

	public EJBBaseControllerSWTStandard getControllerAppelant() {
		return controllerAppelant;
	}

	public void setControllerAppelant(EJBBaseControllerSWTStandard controllerAppelant) {
		this.controllerAppelant = controllerAppelant;
	}

	public Shell getShellCreation() {
		return shellCreation;
	}

	public void setShellCreation(Shell shellCreation) {
		this.shellCreation = shellCreation;
	}

	public EJBBaseControllerSWTStandard getRefCreationSWT() {
		return refCreationSWT;
	}

	public void setRefCreationSWT(EJBBaseControllerSWTStandard refCreationSWT) {
		this.refCreationSWT = refCreationSWT;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCleListeTitre() {
		return cleListeTitre;
	}

	public void setCleListeTitre(String cleListeTitre) {
		this.cleListeTitre = cleListeTitre;
	}

	public IEditorPart getEditorCreation() {
		return editorCreation;
	}

	public void setEditorCreation(IEditorPart editorCreation) {
		this.editorCreation = editorCreation;
	}

	public String getEditorCreationId() {
		return editorCreationId;
	}

	public void setEditorCreationId(String editorCreationId) {
		this.editorCreationId = editorCreationId;
	}

	public IEditorInput getEditorInputCreation() {
		return editorInputCreation;
	}

	public void setEditorInputCreation(IEditorInput editorInputCreation) {
		this.editorInputCreation = editorInputCreation;
	}

	public String getJPQLQuery() {
		return JPQLQuery;
	}

	public void setJPQLQuery(String query) {
		JPQLQuery = query;
	}

	public Class<?> getTypeEntite() {
		return typeEntite;
	}

	public void setTypeEntite(Class<?> typeEntite) {
		this.typeEntite = typeEntite;
	}

	public void setTypeObjet(Class<?> typeObjet) {
		this.typeObjet = typeObjet;
	}

	public Boolean getForceAffichageAideRemplie() {
		return forceAffichageAideRemplie;
	}

	public void setForceAffichageAideRemplie(Boolean forceAffichageAideRemplie) {
		this.forceAffichageAideRemplie = forceAffichageAideRemplie;
	}

	public Boolean getAfficheDetail() {
		return afficheDetail;
	}

	public void setAfficheDetail(Boolean afficheCreation) {
		this.afficheDetail = afficheCreation;
	}

	public Boolean getAfficheNouveau() {
		return afficheNouveau;
	}

	public void setAfficheNouveau(Boolean afficheNouveau) {
		this.afficheNouveau = afficheNouveau;
	}

}
