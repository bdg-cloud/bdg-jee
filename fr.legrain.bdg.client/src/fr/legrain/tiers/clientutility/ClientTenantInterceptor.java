package fr.legrain.tiers.clientutility;

import java.util.Map;

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;

import fr.legrain.bdg.client.Activator;
import fr.legrain.bdg.client.preferences.PreferenceConstants;

public class ClientTenantInterceptor implements EJBClientInterceptor {

    public void handleInvocation(EJBClientInvocationContext context) throws Exception {
    	
    	String dossier = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SERVEUR_DOSSIER); //demo
//        Object credential = SecurityActions.securityContextGetCredential();
//
//        if (credential != null && credential instanceof PasswordPlusCredential) {
//            PasswordPlusCredential ppCredential = (PasswordPlusCredential) credential;
            Map<String, Object> contextData = context.getContextData();
            contextData.put("tenant", dossier);
//        }
        context.sendRequest();
    }

    public Object handleInvocationResult(EJBClientInvocationContext context) 
            throws Exception {
        return context.getResult();
    }
}