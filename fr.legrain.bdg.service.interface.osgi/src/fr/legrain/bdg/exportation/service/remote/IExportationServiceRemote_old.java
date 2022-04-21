package fr.legrain.bdg.exportation.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.exportation.model.ExportationEpicea;





@Remote
public interface IExportationServiceRemote_old {
	public static final String validationContext = "EXPORTATION";
	public ExportationEpicea  exportJPA(List<TaFacture> idFactureAExporter,List<TaAvoir> idAvoirAExporter,List<TaApporteur> idApporteurAExporter,List<TaAcompte> 
	idAcompteAExporter,List<TaReglement> idReglementAExporter,List<TaRemise> idRemiseAExporter,boolean gererPointages,boolean centralisation) throws Exception ;
	
	public ExportationEpicea exportJPA(List<TaFacture> idFactureAExporter,List<TaAvoir> idAvoirAExporter,List<TaApporteur> idApporteurAExporter,List<TaAcompte> idAcompteAExporter,
			List<TaReglement> idReglementAExporter,List<TaRemise> idRemiseAExporter, 
			int reExportFacture,int reExportAvoir,int reExportAcpporteur,int reExportAcompte,int reExportReglement,int reExportRemise,boolean gererPointages,boolean centralisation) throws Exception;
	
}
