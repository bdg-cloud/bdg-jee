package fr.legrain.bdg.servicewebexterne.service.remote;

import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;

public interface ITransactionnalMergeLigneServiceExterneServiceRemote {
	public TaLigneServiceWebExterne mergeLigneServiceWebExterne(TaLigneServiceWebExterne ligne) throws Exception;
}
