package fr.legrain.bdg.webapp.aide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped 
public class AideContextuelleController implements Serializable {
	
	private String anneeCourante;
	private String codeAnneeCourante;
	
	private final static String[] imageMenu;
	private final static String[] imageMenuGris;
    private final static String[] definitionMenu;
     
    List<DescMenus> liste;
    
    static {
    	imageMenu = new String[8];
    	imageMenu[0] = "btn/modifier.svg";
    	imageMenu[1] = "btn/supprimer.svg";
    	imageMenu[2] = "btn/inserer.svg";
    	imageMenu[3] = "btn/imprime.svg";
    	imageMenu[4] = "btn/enregistrer.svg";
    	imageMenu[5] = "btn/liste.svg";
    	imageMenu[6] = "btn/fermer.svg";
    	imageMenu[7] = "btn/annuler.svg";
         
    	imageMenuGris = new String[8];
    	imageMenuGris[0] = "btn/modifier-gris.svg";
    	imageMenuGris[1] = "btn/supprimer-gris.svg";
    	imageMenuGris[2] = "btn/inserer-gris.svg";
    	imageMenuGris[3] = "btn/print-gris.svg";
    	imageMenuGris[4] = "btn/enregistrer-gris.svg";
    	imageMenuGris[5] = "btn/liste-gris.svg";
    	imageMenuGris[6] = "btn/fermer.svg";
    	imageMenuGris[7] = "btn/annuler-gris.svg";
         
    	definitionMenu = new String[8];
    	definitionMenu[0] = "Modification de l'enregistrement courant";
    	definitionMenu[1] = "Suppression de l'enregistrement courant";
    	definitionMenu[2] = "Ajout d'un nouvel enregistrement";
    	definitionMenu[3] = "Imprime l'enregistrement courant";
    	definitionMenu[4] = "Enregistre l'enregistrement courant";
    	definitionMenu[5] = "Liste de tous les enregistrements";
    	definitionMenu[6] = "Ferme l'onglet courant";
    	definitionMenu[7] = "Annule les modifications";
    	
    }

    @PostConstruct
    public void init() {
        liste = createMenu(8);
        setAnneeCourante(LibDate.getAnnee(new Date()));
        setCodeAnneeCourante(LibDate.getCodeAnnee(new Date()));
    }    

    


public List<DescMenus> getListe() {
		return liste;
	}

	public void setListe(List<DescMenus> liste) {
		this.liste = liste;
	}

public List<DescMenus> createMenu(int size) {
    List<DescMenus> list = new ArrayList<DescMenus>();
    for(int i = 0 ; i < size ; i++) {
        list.add(new DescMenus(imageMenu[i], imageMenuGris[i], definitionMenu[i]));
    }
     
    return list;
}

public String getAnneeCourante() {
	return anneeCourante;
}




public void setAnneeCourante(String anneeCourante) {
	this.anneeCourante = anneeCourante;
}

public String getCodeAnneeCourante() {
	return codeAnneeCourante;
}




public void setCodeAnneeCourante(String codeAnneeCourante) {
	this.codeAnneeCourante = codeAnneeCourante;
}

// Objet Descmenu
public class DescMenus implements Serializable{

	private String images; 
	private String imagesgris; 
	private  String definitionmenus;

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getImagesGris() {
		return imagesgris;
	}

	public void setImagesGris(String imagesgris) {
		this.imagesgris = imagesgris;
	}

	
	public String getDefinitionmenus() {
		return definitionmenus;
	}

	public void setDefinitionmenus(String definitionmenus) {
		this.definitionmenus = definitionmenus;
	}



	public DescMenus(String images, String imagesgris, String definitionmenus) {
		super();
		this.images = images;
		this.imagesgris = imagesgris;
		this.definitionmenus = definitionmenus;
	}

}
}
 
 
