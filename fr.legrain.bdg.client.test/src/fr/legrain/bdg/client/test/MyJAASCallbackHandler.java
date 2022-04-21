package fr.legrain.bdg.client.test;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;

public class MyJAASCallbackHandler implements CallbackHandler {

//	private static String s_username;
//	private static String s_password;
	
	private static String s_username ="demo_demo";
	private static String s_password = "demo";

	public static void setCredential(String username, String password) {

		synchronized (MyJAASCallbackHandler .class) {
			s_username = username;
			s_password = password;
		}
	}

	public MyJAASCallbackHandler () {

	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

		synchronized (MyJAASCallbackHandler.class) {
			for (Callback current : callbacks) {
				if (current instanceof RealmCallback) {
					RealmCallback rcb = (RealmCallback) current;
					String defaultText = rcb.getDefaultText();
					rcb.setText(defaultText);
				} else if (current instanceof NameCallback) {
					NameCallback ncb = (NameCallback) current;
					ncb.setName(s_username);
				} else if (current instanceof PasswordCallback) {
					PasswordCallback pcb = (PasswordCallback) current;
					pcb.setPassword(s_password.toCharArray());
				} else {
					throw new UnsupportedCallbackException(current);
				}
			}
		}
	}

}
