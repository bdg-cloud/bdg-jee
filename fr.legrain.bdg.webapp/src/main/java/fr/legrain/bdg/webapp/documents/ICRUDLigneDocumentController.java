package fr.legrain.bdg.webapp.documents;

import javax.faces.event.ActionEvent;

public interface ICRUDLigneDocumentController {

	void actAutoInsererLigne(ActionEvent actionEvent);

	void actInsererLigne(ActionEvent actionEvent);

	void actEnregistrerLigne(ActionEvent actionEvent);

	void actAnnulerLigne(ActionEvent actionEvent);

	void actModifierLigne(ActionEvent actionEvent);

	void actSupprimerLigne();

	void actSupprimerLigne(ActionEvent actionEvent);

}