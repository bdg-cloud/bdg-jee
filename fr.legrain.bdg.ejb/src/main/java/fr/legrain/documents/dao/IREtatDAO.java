package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.RetourMajDossier;
import fr.legrain.document.model.TaREtat;

//@Remote
public interface IREtatDAO extends IGenericDAO<TaREtat> {

	public RetourMajDossier updateEtatEncoursTousDocs() ;
	public RetourMajDossier updateEtatTousDocs();
	public RetourMajDossier majLigneALigneProcedureStockee();
}
