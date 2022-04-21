package fr.legrain.validator;

import java.util.Map;

import fr.legrain.controle.model.TaControle;

public interface IListeControle {
	public Map<String, TaControle> getListeCtrlMap();
	public void setListeCtrlMap(Map<String, TaControle> listeCtrlMap);
}