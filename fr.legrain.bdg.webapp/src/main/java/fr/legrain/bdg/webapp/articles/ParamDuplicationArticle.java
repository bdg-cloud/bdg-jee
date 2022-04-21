package fr.legrain.bdg.webapp.articles;

public class ParamDuplicationArticle  implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -488107691467610929L;
	
	
	private Boolean reprisePrix=true;
    private Boolean repriseControls=true;
    private Boolean repriseRecettes=true;
    private Boolean repriseFournisseurs=true;
    private Boolean repriseFamilles=true;
//  private Boolean repriseNotes=true;
    
    
    
    
	public ParamDuplicationArticle() {

	}
	
	
	public Boolean getReprisePrix() {
		return reprisePrix;
	}
	public void setReprisePrix(Boolean reprisePrix) {
		this.reprisePrix = reprisePrix;
	}
	public Boolean getRepriseControls() {
		return repriseControls;
	}
	public void setRepriseControls(Boolean repriseControls) {
		this.repriseControls = repriseControls;
	}
	public Boolean getRepriseRecettes() {
		return repriseRecettes;
	}
	public void setRepriseRecettes(Boolean repriseRecettes) {
		this.repriseRecettes = repriseRecettes;
	}
	public Boolean getRepriseFournisseurs() {
		return repriseFournisseurs;
	}
	public void setRepriseFournisseurs(Boolean repriseFournisseurs) {
		this.repriseFournisseurs = repriseFournisseurs;
	}
	public Boolean getRepriseFamilles() {
		return repriseFamilles;
	}
	public void setRepriseFamilles(Boolean repriseFamilles) {
		this.repriseFamilles = repriseFamilles;
	}
	
	
	


	
}
