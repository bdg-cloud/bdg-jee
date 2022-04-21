package fr.legrain.bdg.webapp.oauth.microsoft;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
/**
 * Pour google, les token sont stockés avec les credential dans le datastore (avec une clé/nom d'utilisateur).
 * Le datastore (fichier) permet donc de faire le liens entre la servlet et le bean.
 * 
 * Dans le cas MS, pas de store.
 * Le sessionscope bean ne fonctionne pas cas l'url de redirection est oauth.bdg.cloud
 * et le bean s'execute sur ledossier.bdg.cloud, donc la session est différente, donc le sessionscope ne fonctionne pas.
 * La sérialisation/désérialisation semble fonctionné pour tester mais pas sécurisé DU TOUT
 * 
 * Si on arrive à faire une redirection de l'url oauth.bdg.cloud vers ledossier.bdg.cloud le sessionscope devrait fonctionné.
 * Il faut pour cela passé te tenant en parametre, peut être avec le parametre 'state' de oauth.
 * 
 * Il faudrait peut etre faire stocker les tokens/credential dans des tables dans la base de données et peut être de façon crypté.
 * 
 * @author nicolas
 *
 */
public class MSTokenSessionBean implements Serializable{

	private static final long serialVersionUID = 4611417841478258511L;
	
	private String accessToken;
	private String refreshToken;
	private long expiresIn;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	
	public void ser(String user) {
		try {
	        FileOutputStream fileOut =
	        new FileOutputStream("/tmp/"+user+".ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(this);
	        out.close();
	        fileOut.close();
	        System.out.printf("Serialized data is saved in /tmp/"+user+".ser");
	     } catch (IOException i) {
	        i.printStackTrace();
	     }
	}

	public void de(String user) {
		MSTokenSessionBean e = null;
	    try {
	       FileInputStream fileIn = new FileInputStream("/tmp/"+user+".ser");
	       ObjectInputStream in = new ObjectInputStream(fileIn);
	       e = (MSTokenSessionBean) in.readObject();
	       this.setAccessToken(e.getAccessToken());
	       this.setRefreshToken(e.getRefreshToken());
	       in.close();
	       fileIn.close();
	    } catch (IOException i) {
	       i.printStackTrace();
	       return;
	    } catch (ClassNotFoundException c) {
	       System.out.println("Employee class not found");
	       c.printStackTrace();
	       return;
	    }

	 }
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
}
