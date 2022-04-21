package fr.legrain.bdg.webapp.solstyce;

import org.primefaces.model.TreeNode;

import fr.legrain.article.model.TaLot;

public class DetailLotOngletJSF {
	private TaLot taLot;
	private TreeNode root;
    private TreeNode selectedNode;
    private String sens = "origine";
    
    public DetailLotOngletJSF() {
    	
    }
    
    public DetailLotOngletJSF(TaLot taLot, TreeNode root, TreeNode selectedNode) {
    	this.taLot = taLot;
    	this.root = root;
    	this.selectedNode = selectedNode;
    }
    
	public TaLot getTaLot() {
		return taLot;
	}
	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}
	public TreeNode getRoot() {
		return root;
	}
	public void setRoot(TreeNode root) {
		this.root = root;
	}
	public TreeNode getSelectedNode() {
		return selectedNode;
	}
	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}
    
    
	
}
