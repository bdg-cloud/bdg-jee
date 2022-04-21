package fr.legrain.bdg.dms.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.dms.model.TaEtatMouvementDms;





@Remote
public interface IEtatDmsServiceRemote {
	public static final String validationContext = "DMS";
	public List<TaEtatMouvementDms> calculEtatDms(TaEtatMouvementDms criteres,boolean synthese);
	public List<TaEtatMouvementDms> calculEtatFinDms(TaEtatMouvementDms criteres);
	public List<TaEtatMouvementDms> calculEtatReportDms(TaEtatMouvementDms criteres);
	
}
