package fr.legrain.validator;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import fr.legrain.controle.model.TaControle;

@RequestScoped
public class ListeControleRequest implements Serializable, IListeControle{

	private static final long serialVersionUID = 6327330658390562332L;
	
	protected Map<String,TaControle> listeCtrlMap;

	public Map<String, TaControle> getListeCtrlMap() {
		return listeCtrlMap;
	}

	public void setListeCtrlMap(Map<String, TaControle> listeCtrlMap) {
		this.listeCtrlMap = listeCtrlMap;
	}
}
