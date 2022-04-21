package fr.legrain.droits.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;

import fr.legrain.general.model.TaParametre;

//@SessionScoped
@ApplicationScoped
public class ParamBdg implements Serializable{

	private static final long serialVersionUID = 576750729407743620L;
	
	private TaParametre taParametre;

	public TaParametre getTaParametre() {
		return taParametre;
	}

	public void setTaParametre(TaParametre taParametre) {
		this.taParametre = taParametre;
	}
	
	
}
