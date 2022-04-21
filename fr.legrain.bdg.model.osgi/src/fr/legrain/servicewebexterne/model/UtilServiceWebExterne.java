package fr.legrain.servicewebexterne.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.legrain.lib.data.LibCrypto;

public class UtilServiceWebExterne {
	/**
	 * Constante correspondant aux code dans la base de données : TaServiceWebExterne.codeServiceWebExterne 
	 */
	public static final String CODE_SWE_MAILJET = "MAILJET";
	public static final String CODE_SWE_MAILJET_CAMPAGNE = "MAILJET_CAMPAGNE";
	public static final String CODE_SWE_MAILJET_SMS = "MAILJET SMS";
	public static final String CODE_SWE_OVH_SMS = "OVH_SMS";
	public static final String CODE_SWE_GOOGLE_DRIVE = "GDRIVE";
	public static final String CODE_SWE_GOOGLE_DOC = "GDOC";
	public static final String CODE_SWE_GOOGLE_CALENDAR = "GCALENDAR";
	public static final String CODE_SWE_GOOGLE = "GOOGLE";
	public static final String CODE_SWE_DROPBOX = "DROPBOX";
	public static final String CODE_SWE_STRIPE = "STRIPE";
	public static final String CODE_SWE_STRIPE_CONNECT = "STRIPE_CONNECT";
	public static final String CODE_SWE_PAYPAL = "PAYPAL"; //non implémenté pour l'instant, constante créer pour tester la création dynamique des services
	public static final String CODE_SWE_SHIPPINGBO = "SHIPPINGBO";
	public static final String CODE_SWE_WOOCOMMERCE = "WOOCOMMERCE";
	public static final String CODE_SWE_MENSURA= "MENSURA";
	public static final String CODE_SWE_PRESTASHOP = "PRESTASHOP";
	
	
	//constante pour le formulaire de saisi des identifiants de connexion
	boolean debug = false;
	public static final String IHM_LOGIN = "login";
	public static final String IHM_PASSWORD = "password";
	public static final String IHM_API_KEY_1 = "apiKey1";
	public static final String IHM_API_KEY_2 = "apiKey2";
	public static final String IHM_VALEUR_1 = "valeur1";
	public static final String IHM_VALEUR_2 = "valeur2";
	public static final String IHM_VALEUR_3 = "valeur3";
	public static final String IHM_VALEUR_4 = "valeur4";
	public static final String IHM_VALEUR_5 = "valeur5";
	public static final String IHM_VALEUR_6 = "valeur6";
	public static final String IHM_VALEUR_7 = "valeur7";
	public static final String IHM_VALEUR_8 = "valeur8";
	public static final String IHM_VALEUR_9 = "valeur9";
	public static final String IHM_VALEUR_10 = "valeur10";
	
	public List<String> listProfilLoginPassword = null;
	public List<String> listProfilApiKey = null;
	public List<String> listProfilApiKey4 = null;
	public List<String> listProfilOAuth = null;
	
	public Map<String, String> mapMailjetEmail = null;
	public Map<String, String> mapMailjetSms = null;
	public Map<String, String> mapStripe = null;
	public Map<String, String> mapOvh = null;
	public Map<String, String> mapGoogleOAuth = null;
	public Map<String, String> mapDropbox = null;
	public Map<String, String> mapShippingBo = null;
	public Map<String, String> mapWoocommerce = null;
	public Map<String, String> mapMensura = null;
	public Map<String, String> mapPrestashop = null;
	
	public UtilServiceWebExterne() {
		listProfilLoginPassword = new ArrayList<>();
		listProfilLoginPassword.add(IHM_LOGIN);
		listProfilLoginPassword.add(IHM_PASSWORD);
		
		listProfilApiKey = new ArrayList<>();
		listProfilApiKey.add(IHM_API_KEY_1);
		listProfilApiKey.add(IHM_API_KEY_2);
		
		listProfilApiKey4 = new ArrayList<>();
		listProfilApiKey4.add(IHM_VALEUR_1);
		listProfilApiKey4.add(IHM_VALEUR_2);
		listProfilApiKey4.add(IHM_VALEUR_3);
		listProfilApiKey4.add(IHM_VALEUR_4);
		
		listProfilOAuth = new ArrayList<>();
		listProfilOAuth.add(IHM_LOGIN);
		listProfilOAuth.add(IHM_PASSWORD);
		
		mapMailjetEmail = new HashMap<String, String>();
		mapMailjetEmail.put(IHM_API_KEY_1, "Clé API publique");
		mapMailjetEmail.put(IHM_API_KEY_2, "Clé API privée");
		
		mapMailjetSms = new HashMap<String, String>();
		mapMailjetSms.put(IHM_API_KEY_1, "Token");
		
		mapStripe = new HashMap<String, String>();
		mapStripe.put(IHM_API_KEY_1, "Clé API secrète live");
		mapStripe.put(IHM_API_KEY_2, "Clé API publique live");
//		mapStripe.put(IHM_VALEUR_1, "Clé API secrète test");
//		mapStripe.put(IHM_VALEUR_2, "Clé API publique test");
		
		mapDropbox = new HashMap<String, String>();
		mapDropbox.put(IHM_LOGIN, "Login");
		mapDropbox.put(IHM_PASSWORD, "Password");
		
		mapShippingBo = new HashMap<String, String>();
		mapShippingBo.put(IHM_LOGIN, "Login");
		mapShippingBo.put(IHM_PASSWORD, "Password");
		mapShippingBo.put(IHM_VALEUR_1, "Remote host");
		mapShippingBo.put(IHM_VALEUR_2, "Remote file url");
		
		mapMensura = new HashMap<String, String>();
		mapMensura.put(IHM_LOGIN, "Login");
		mapMensura.put(IHM_PASSWORD, "Password");
		mapMensura.put(IHM_VALEUR_1, "Remote host");
		mapMensura.put(IHM_VALEUR_2, "Chemin fichier");
		
		
		mapWoocommerce  = new HashMap<String, String>();
		mapWoocommerce.put(IHM_API_KEY_1, "Clé API publique live");
		mapWoocommerce.put(IHM_API_KEY_2, "Clé API secrète live");
		
		mapPrestashop  = new HashMap<String, String>();
		mapPrestashop.put(IHM_API_KEY_1, "Clé API");
//		mapPrestashop.put(IHM_API_KEY_2, "Clé API publique live");
		
		mapGoogleOAuth = new HashMap<String, String>();
		mapGoogleOAuth.put(IHM_LOGIN, "Login");
		mapGoogleOAuth.put(IHM_PASSWORD, "Password");
		
		mapOvh = new HashMap<String, String>();
		mapOvh.put(IHM_VALEUR_1, "APPLICATION_KEY");
		mapOvh.put(IHM_VALEUR_2, "APPLICATION_SECRET");
		mapOvh.put(IHM_VALEUR_3, "CONSUMER_KEY");
		mapOvh.put(IHM_VALEUR_4, "SERVICE_NAME");
	}
	
	public TaCompteServiceWebExterne crypter(TaCompteServiceWebExterne c) {
		if(c!=null) {
			if(c.getLogin()!=null) { c.setLogin(LibCrypto.encrypt(c.getLogin())); }
			if(c.getPassword()!=null) { c.setPassword(LibCrypto.encrypt(c.getPassword())); }
			if(c.getApiKey1()!=null) { c.setApiKey1(LibCrypto.encrypt(c.getApiKey1())); }
			if(c.getApiKey2()!=null) { c.setApiKey2(LibCrypto.encrypt(c.getApiKey2())); }
			if(c.getValeur1()!=null) { c.setValeur1(LibCrypto.encrypt(c.getValeur1())); }
			if(c.getValeur2()!=null) { c.setValeur2(LibCrypto.encrypt(c.getValeur2())); }
			if(c.getValeur3()!=null) { c.setValeur3(LibCrypto.encrypt(c.getValeur3())); }
			if(c.getValeur4()!=null) { c.setValeur4(LibCrypto.encrypt(c.getValeur4())); }
			if(c.getValeur5()!=null) { c.setValeur5(LibCrypto.encrypt(c.getValeur5())); }
			if(c.getValeur6()!=null) { c.setValeur6(LibCrypto.encrypt(c.getValeur6())); }
			if(c.getValeur7()!=null) { c.setValeur7(LibCrypto.encrypt(c.getValeur7())); }
			if(c.getValeur8()!=null) { c.setValeur8(LibCrypto.encrypt(c.getValeur8())); }
			if(c.getValeur9()!=null) { c.setValeur9(LibCrypto.encrypt(c.getValeur9())); }
			if(c.getValeur10()!=null) { c.setValeur10(LibCrypto.encrypt(c.getValeur10())); }
		}
		return c;
	}
	
	public TaCompteServiceWebExterne decrypter(TaCompteServiceWebExterne c) {
		if(c!=null) {
			if(c.getLogin()!=null) { c.setLogin(LibCrypto.decrypt(c.getLogin())); }
			if(c.getPassword()!=null) { c.setPassword(LibCrypto.decrypt(c.getPassword())); }
			if(c.getApiKey1()!=null) { c.setApiKey1(LibCrypto.decrypt(c.getApiKey1())); }
			if(c.getApiKey2()!=null) { c.setApiKey2(LibCrypto.decrypt(c.getApiKey2())); }
			if(c.getValeur1()!=null) { c.setValeur1(LibCrypto.decrypt(c.getValeur1())); }
			if(c.getValeur2()!=null) { c.setValeur2(LibCrypto.decrypt(c.getValeur2())); }
			if(c.getValeur3()!=null) { c.setValeur3(LibCrypto.decrypt(c.getValeur3())); }
			if(c.getValeur4()!=null) { c.setValeur4(LibCrypto.decrypt(c.getValeur4())); }
			if(c.getValeur5()!=null) { c.setValeur5(LibCrypto.decrypt(c.getValeur5())); }
			if(c.getValeur6()!=null) { c.setValeur6(LibCrypto.decrypt(c.getValeur6())); }
			if(c.getValeur7()!=null) { c.setValeur7(LibCrypto.decrypt(c.getValeur7())); }
			if(c.getValeur8()!=null) { c.setValeur8(LibCrypto.decrypt(c.getValeur8())); }
			if(c.getValeur9()!=null) { c.setValeur9(LibCrypto.decrypt(c.getValeur9())); }
			if(c.getValeur10()!=null) { c.setValeur10(LibCrypto.decrypt(c.getValeur10())); }
		}
		return c;
	}
	
	public boolean afficher(TaServiceWebExterne service, String nomChamp) {
		if(service!=null) {
			return afficher(service.getCodeServiceWebExterne(), nomChamp);
		} else {
			return false;
		}
		
	}
	
	public boolean afficher(String codeService, String nomChamp) {
		
		boolean affiche = false;
		if(debug) {
			affiche = true;
		} else {
			switch (codeService) {
			case CODE_SWE_MAILJET:
				affiche = mapMailjetEmail.keySet().contains(nomChamp);
				break;
			case CODE_SWE_MAILJET_CAMPAGNE:
				affiche = mapMailjetEmail.keySet().contains(nomChamp);
				break;
			case CODE_SWE_MAILJET_SMS:
				affiche = mapMailjetSms.keySet().contains(nomChamp);
				break;
			case CODE_SWE_STRIPE:
				affiche = mapStripe.keySet().contains(nomChamp);
				break;
			case CODE_SWE_SHIPPINGBO:
				affiche = mapShippingBo.keySet().contains(nomChamp);
				break;
			case CODE_SWE_WOOCOMMERCE:
				affiche = mapWoocommerce.keySet().contains(nomChamp);
				break;
			case CODE_SWE_PRESTASHOP:
				affiche = mapPrestashop.keySet().contains(nomChamp);
				break;
			case CODE_SWE_MENSURA:
				affiche = mapMensura.keySet().contains(nomChamp);
				break;
			case CODE_SWE_OVH_SMS:
				affiche = mapOvh.keySet().contains(nomChamp);
				break;
			case CODE_SWE_DROPBOX:
				affiche = mapDropbox.keySet().contains(nomChamp);
				break;
			case CODE_SWE_GOOGLE_DRIVE:
			case CODE_SWE_GOOGLE_CALENDAR:
				affiche = mapGoogleOAuth.keySet().contains(nomChamp);
				break;
			default:
				break;
			}
		}
		return affiche;
	}
	
	public String label(String codeService, String nomChamp) {
		
		String l = "";
		if(debug) {
			l = "";
		} else {
			switch (codeService) {
			case CODE_SWE_MAILJET:
				l = mapMailjetEmail.get(nomChamp);
				break;
			case CODE_SWE_MAILJET_CAMPAGNE:
				l = mapMailjetEmail.get(nomChamp);
				break;
			case CODE_SWE_MAILJET_SMS:
				l = mapMailjetSms.get(nomChamp);
				break;
			case CODE_SWE_STRIPE:
				l = mapStripe.get(nomChamp);
				break;
			case CODE_SWE_OVH_SMS:
				l = mapOvh.get(nomChamp);
				break;
			case CODE_SWE_SHIPPINGBO:
				l = mapShippingBo.get(nomChamp);
				break;
			case CODE_SWE_WOOCOMMERCE:
				l = mapWoocommerce.get(nomChamp);
				break;
			case CODE_SWE_PRESTASHOP:
				l = mapPrestashop.get(nomChamp);
				break;
			case CODE_SWE_MENSURA:
				l = mapMensura.get(nomChamp);
				break;
			case CODE_SWE_DROPBOX:
				l = mapDropbox.get(nomChamp);
				break;
			case CODE_SWE_GOOGLE_DRIVE:
			case CODE_SWE_GOOGLE_CALENDAR:
				l = mapGoogleOAuth.get(nomChamp);
				break;
			default:
				break;
			}
		}
		return l;
	}
}
