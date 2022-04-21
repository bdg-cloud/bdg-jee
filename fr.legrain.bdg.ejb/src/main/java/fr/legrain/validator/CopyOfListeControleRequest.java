package fr.legrain.validator;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.New;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;

@RequestScoped
public class CopyOfListeControleRequest {
	
//	@Produces @MarqueX1
	public IListeControle getCtrl(@New ListeControleRequest lc, ListeControle lcSession) {
		TransactionSynchronizationRegistry reg;
		try {
			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
		
			if(reg!=null && reg.getResource("tenant")!=null ) {
				return lc;
			} else {
				//return lcSession;
				return null;
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null; 
	}

}
