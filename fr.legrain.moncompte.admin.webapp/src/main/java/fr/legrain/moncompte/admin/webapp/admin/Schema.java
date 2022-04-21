package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.util.List;

public class Schema implements Serializable {
	
	public String nom;
	public String taille1;
	public String taille2;
	private List<Dump> listeDump;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getTaille1() {
		return taille1;
	}
	public void setTaille1(String taille1) {
		this.taille1 = taille1;
	}
	public String getTaille2() {
		return taille2;
	}
	public void setTaille2(String taille2) {
		this.taille2 = taille2;
	}
	public List<Dump> getListeDump() {
		return listeDump;
	}
	public void setListeDump(List<Dump> listeDump) {
		this.listeDump = listeDump;
	}

}