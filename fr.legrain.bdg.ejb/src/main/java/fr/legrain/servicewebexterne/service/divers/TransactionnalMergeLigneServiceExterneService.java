package fr.legrain.servicewebexterne.service.divers;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.transaction.UserTransaction;

import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITransactionnalMergeLigneServiceExterneServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class TransactionnalMergeLigneServiceExterneService implements ITransactionnalMergeLigneServiceExterneServiceRemote{
	
	@EJB private ITaLigneServiceWebExterneServiceRemote ligneServiceWebExterneService;

	
	@Resource private UserTransaction tx; 

	


	
	@PostConstruct
	public void postConstruct(){

	}
	
	public TaLigneServiceWebExterne mergeLigneServiceWebExterne(TaLigneServiceWebExterne ligne) throws Exception{
		// tx.begin();
    	 ligne = ligneServiceWebExterneService.merge(ligne); 
    	// tx.commit();   	 
    	 return ligne;
	}
	
	

}
