package fr.legrain.validator;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.SessionScoped;

import fr.legrain.controle.model.TaControle;

@SessionScoped
//@ApplicationScoped
public class ListeControle implements Serializable, IListeControle{

	private static final long serialVersionUID = -6046078813831557057L;
	
	protected Map<String,TaControle> listeCtrlMap;

	public Map<String, TaControle> getListeCtrlMap() {
		return listeCtrlMap;
	}

	public void setListeCtrlMap(Map<String, TaControle> listeCtrlMap) {
		this.listeCtrlMap = listeCtrlMap;
	}
}
