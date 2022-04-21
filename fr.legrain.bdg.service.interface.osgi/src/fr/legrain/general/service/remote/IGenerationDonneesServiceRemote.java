package fr.legrain.general.service.remote;

import javax.ejb.Remote;

@Remote
public interface IGenerationDonneesServiceRemote {

	public void genTousLesParametres();

	public void genTousLesParametresArticles();

	public void genTousLesParametresTiers();

	public void genTousLesParametresSolstyce();

	public void genFamilleArticle();

	public void genUnites();

	public void genTypeAdresse();

	public void genTypeCivilite();

	public void genTypeEntite();

	public void genTypeTelephone();

	public void genTypeTiers();

	public void genTypeWeb();

	public void genTypeEmail();

	public void genFamilleTiers();

	public void genEntrepot();

	public void genTypeFabrication();

	public void genTypeReception();

	public void genTypeCodeBarre();

	public void genGroupeControles();

	public void genData();

	public void familleArticle();

	public void unites();

	public void typeAdresse();

	public void typeCivilite();

	public void typeEntite();

	public void typeTelephone();

	public void typeTiers();

	public void typeWeb();

	public void typeEmail();

	public void familleTiers();

	public void entrepot();

	public void typeFabrication();

	public void typeReception();

	public void typeCodeBarre();

	public void groupeControles();

	public void articleReelle();

	public void tiersReel();

	public void article();

	public void tiers();

	public void bonDeReceptionReel();

	public void bonDeReception();

	public void fabrication();

	public void controleConf();

}