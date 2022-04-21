package fr.legrain.general.service.remote;

import java.io.FileReader;

import javax.ejb.Remote;

import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.model.RetourMajDossier;





@Remote
public interface IMajDossierDiversesServiceRemote {
	public static final String validationContext = "MAJ_DIVERSES";


	public RetourMajDossier testDivers();
	public RetourMajDossier majLigneALigne();
	public RetourMajDossier majEtatDocument();

	public RetourMajDossier updateEtatEncoursTousDocs();
	public RetourMajDossier updateEtatTousDocs();
	public RetourMajDossier majEtatFactureReglee() ;
	public RetourMajDossier majLigneALigneProcedureStockee() ;
}
