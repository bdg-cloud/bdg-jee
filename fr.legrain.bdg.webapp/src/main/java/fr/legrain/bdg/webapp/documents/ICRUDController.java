package fr.legrain.bdg.webapp.documents;

import javax.faces.event.ActionEvent;

public interface ICRUDController {

	void actAnnuler(ActionEvent actionEvent);

	void actEnregistrer(ActionEvent actionEvent);

	void actInserer(ActionEvent actionEvent);

	void actModifier();

	void actModifier(ActionEvent actionEvent);

	void actAide(ActionEvent actionEvent);

	void actSupprimer();

	void actSupprimer(ActionEvent actionEvent);

	void actFermer(ActionEvent actionEvent);

	void actImprimer(ActionEvent actionEvent);
	
	public void autoCompleteMapUIToDTO();
	public void autoCompleteMapDTOtoUI();

}