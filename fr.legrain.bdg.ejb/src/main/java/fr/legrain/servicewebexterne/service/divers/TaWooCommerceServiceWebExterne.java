package fr.legrain.servicewebexterne.service.divers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.icoderman.woocommerce.ApiVersionType;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.icoderman.woocommerce.WooCommerceAPI;
import com.icoderman.woocommerce.oauth.OAuthConfig;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaSynchroServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaWooCommerceServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITransactionnalMergeLigneServiceExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaWooCommerceServiceWebExterne implements ITaWooCommerceServiceWebExterneServiceRemote{
	
	@EJB private ITaLigneServiceWebExterneServiceRemote ligneServiceWebExterneService;
	@EJB private ITaCompteServiceWebExterneServiceRemote compteServiceWebExterneService;
	@EJB private ITaServiceWebExterneServiceRemote serviceWebExterneService;
	@EJB private ITaLiaisonServiceExterneServiceRemote liaisonService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaTTiersServiceRemote taTTiersService;
	@EJB private ITaEmailServiceRemote mailService;
	@EJB private ITaTelephoneServiceRemote telService;
	@EJB private ITaAdresseServiceRemote adresseService;
	@EJB private ITaTAdrServiceRemote taTAdrService;
	@EJB private ITaSynchroServiceExterneServiceRemote synchroService;
	
	@EJB private ITransactionnalMergeLigneServiceExterneServiceRemote transactionalService;
	
	//@Resource private UserTransaction tx; 
	
	private TaCompteServiceWebExterne compteServiceWeb;
	private TaServiceWebExterne serviceWeb;
	protected BdgProperties bdgProperties = null;
	@Inject protected	TenantInfo tenantInfo;
	
	
	//pour test
//	String consumerKey = "ck_441450196eeeaaff832a8de954817f02e1fecc5c";
//	String consumerSecret = "cs_ad16077da7620b3ea3fc48596826e565363f8a02";
//	String domaine = "http://www.herbae.pageweb.fr";
	String consumerKey = "";
	String consumerSecret = "";
	String domaine = "";
	
	
	String dossierTravail = "/woocommerce/orders/";
	
	
	String localDir = "";
	
	String codeService = "WOOCOMMERCE";
	
	@PostConstruct
	public void postConstruct(){
		try {
			serviceWeb = serviceWebExterneService.findByCode(codeService);
			initCompteServiceWeb();
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initCompteServiceWeb() throws FinderException {
		compteServiceWeb = compteServiceWebExterneService.findCompteDefautPourFournisseurService(serviceWeb.getCodeServiceWebExterne());
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		compteServiceWeb = util.decrypter(compteServiceWeb);
		domaine = compteServiceWeb.getUrlService();
		consumerKey = compteServiceWeb.getApiKey1();
		consumerSecret = compteServiceWeb.getApiKey2();
	}
	
	
	private String checkString(Object string) {
		string = String.valueOf(string);
		if(string.equals("null")) {
			string = "";
		}
		return (String) string;
	}
	
	private String encodeValue(String value) {
	    try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;// TODO Auto-generated catch block
		}
	}
	@SuppressWarnings("unused")
	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String UTF_8 = "UTF-8";
    
	private  String signBaseString(String secret, String signatureBaseString) {
        Mac macInstance;
        try {
        	String digest = null;
            macInstance = Mac.getInstance(HMAC_SHA256);
            //macInstance = Mac.getInstance(HMAC_SHA1);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(UTF_8), HMAC_SHA256);
           // SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(UTF_8), HMAC_SHA1);
            macInstance.init(secretKey);
            
            byte[] bytes = macInstance.doFinal(signatureBaseString.getBytes(UTF_8));
            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
              String hex = Integer.toHexString(0xFF & bytes[i]);
              if (hex.length() == 1) {
                hash.append('0');
              }
              hash.append(hex);
            }
            digest = hash.toString();
            
            
            return digest;
           // return Base64.encodeBase64String(macInstance.doFinal(signatureBaseString.getBytes(UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
	
	@SuppressWarnings("unused")
	private String encodedURL() throws UnsupportedEncodingException{
		String encodedURL = "";
		String httpMethod = "GET";
		String url = "http://www.herbae.pageweb.fr/wp-json/wc/v3/orders";
		String urlEncoded = encodeValue(url);
		
		String paramKeyOauthCK = "oauth_consumer_key";
		String paramValueOauthCK = "ck_441450196eeeaaff832a8de954817f02e1fecc5c";
		String paramKeyOauthCKEncoded = encodeValue(paramKeyOauthCK);
		String paramValueOauthCKEncoded = encodeValue(paramValueOauthCK);
		
		String paramKeyOauth_nonce = "oauth_nonce";
		String paramValueOauth_nonce = "uobkSMOvgqf";
		String paramKeyOauth_nonceEncoded = encodeValue(paramKeyOauth_nonce);
		String paramValueOauth_nonceEncoded = encodeValue(paramValueOauth_nonce);
		
		String paramKeyOauthSignatureMethod = "oauth_signature_method";
		String paramValueOauthSignatureMethod = "HMAC-SHA256";
		String paramKeyOauthSignatureMethodEncoded = encodeValue(paramKeyOauthSignatureMethod);
		String paramValueOauthSignatureMethodEncoded = encodeValue(paramValueOauthSignatureMethod);
		
		String paramKeyOauthTimeStamp = "oauth_timestamp";
		String paramValueOauthTimeStamp = "1599124011";
		String paramKeyOauthTimeStampEncoded = encodeValue(paramKeyOauthTimeStamp);
		String paramValueOauthTimeStampEncoded = encodeValue(paramValueOauthTimeStamp);
		
//		String paramKeyOauthVersion = "oauth_version";
//		String paramValueOauthVersion = "1.0";
		
		String paramKeyPerPage = "per_page";
		String paramValuePerPage = "100";
		String paramKeyPerPageEncoded = encodeValue(paramKeyPerPage);
		String paramValuePerPageEncoded = encodeValue(paramValuePerPage);
		
//		String paramKeyAfter = "after";
//		String paramValueAfter = "2020-08-28T00:00:00Z";
		
		String paramKeyOffset= "offset";
		String paramValueOffset = "0";
		String paramKeyOffsetEncoded= encodeValue(paramKeyOffset);
		String paramValueOffsetEncoded = encodeValue(paramValueOffset);
		
		String paramKeyOauthSignature = "oauth_signature";
		String paramValueOauthSignature = "";
		
		Map<String, String> requestParamsNonEncoded = new HashMap<>();
		requestParamsNonEncoded.put(paramKeyOauthCK, paramValueOauthCK);
		requestParamsNonEncoded.put(paramKeyOauth_nonce, paramValueOauth_nonce);
		requestParamsNonEncoded.put(paramKeyOauthSignatureMethod, paramValueOauthSignatureMethod);
		requestParamsNonEncoded.put(paramKeyOauthTimeStamp, paramValueOauthTimeStamp);
		requestParamsNonEncoded.put(paramKeyPerPage, paramValuePerPage);
		requestParamsNonEncoded.put(paramKeyOffset, paramValueOffset);
		
		String nonEncodedParameterString = requestParamsNonEncoded.keySet().stream()
			      .map(key -> key + "=" + requestParamsNonEncoded.get(key))
			      .collect(Collectors.joining("&"));
		
		//-----------------Collect parameters------------------------------------
		
		//1.Percent encode every key and value that will be signed.
		Map<String, String> requestParams = new HashMap<>();
		requestParams.put(paramKeyOauthCKEncoded, paramValueOauthCKEncoded);
		requestParams.put(paramKeyOauth_nonceEncoded, paramValueOauth_nonceEncoded);
		requestParams.put(paramKeyOauthSignatureMethodEncoded, paramValueOauthSignatureMethodEncoded);
		requestParams.put(paramKeyOauthTimeStampEncoded, paramValueOauthTimeStampEncoded);
		requestParams.put(paramKeyPerPageEncoded, paramValuePerPageEncoded);
		requestParams.put(paramKeyOffsetEncoded, paramValueOffsetEncoded);
		
		 
		//2.Sort the list of parameters alphabetically by encoded key.
	   Map<String, String> requestParamsSorted = requestParams.entrySet().stream()
	                .sorted(Map.Entry.comparingByKey())
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	   //3.For each key/value pair:
	   //  -Append the encoded key to the output string.
	   //  -Append the = character to the output string.
	   //  -Append the encoded value to the output string.
	   //  -If there are more key/value pairs remaining, append a & character to the output string.
	   String partialEncodedParameterString = requestParamsSorted.keySet().stream()
			      .map(key -> key + "=" + requestParams.get(key))
			      .collect(Collectors.joining("&"));
	   
	   
	   System.out.println("parametres partiellement encodé et trié: "+partialEncodedParameterString);
	   
	  //---------------- Create the signature base string-------------------
	   
		//1.Set the output string equal to the uppercase HTTP Method.
		String signatureBase = httpMethod;
		//2.Append the & character to the output string.
		signatureBase += "&";
		//3.Percent encode the URL and append it to the output string.
		signatureBase += urlEncoded;
		//4.Append the & character to the output string.
		signatureBase += "&";
		//5.Percent encode the parameter string and append it to the output string.
		String encodedParameterString = encodeValue(partialEncodedParameterString);
		System.out.println("Parametres string totalement encodé  : "+encodedParameterString);
		signatureBase += encodedParameterString;
		
		
		//---------------Generate the signature-----------------
		//Generate the signature using the signature base string and your consumer secret key with a & character with the HMAC-SHA1 hashing algorithm
		System.out.println("Signature base string  : "+signatureBase);
		String signature = "";
		
		signature = signBaseString(consumerSecret+"&", signatureBase);
		System.out.println("Signature : "+signature);
		
		//-----------Construction de l'url complete (pas dans la doc)--------------
		URL urlURL;
		String urlcomplete = url+"?"+nonEncodedParameterString+ "&"+paramKeyOauthSignature+"="+signature;
		System.out.println("Url complete : "+urlcomplete);
//		try {
//			urlURL = new URL(urlcomplete);
//			HttpURLConnection conn = (HttpURLConnection) urlURL.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("Content-Type", "application/json");
//
//			if (conn.getResponseCode() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : "
//						+ conn.getResponseCode());
//			}
//
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//				(conn.getInputStream())));
//
//			String output;
//			System.out.println("Output from Server .... \n");
//			while ((output = br.readLine()) != null) {
//				System.out.println(output);
//			}
//
//			conn.disconnect();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
		
		return encodedURL;
	}
	

	
	/**
	 * @author yann
	 * Va chercher les commandes sur Woocommerce
	 * probablement déclenché par un CRON au final
	 * @return le nombre de commandes traités
	 * @throws Exception 
	 */
	public Integer getOrdersREST(String etat) throws Exception{
		
		   // encodedURL();
		
			Integer nbFiles = 0;
			OAuthConfig config = new OAuthConfig(domaine, consumerKey, consumerSecret);
	        WooCommerce wooCommerce = new WooCommerceAPI(config, ApiVersionType.V3);
	        // Get all with request parameters
	        Map<String, String> params = new HashMap<>();
	        params.put("per_page","100");
	        params.put("offset","0");
	        params.put("orderby","id");
//	        params.put("status", "completed");
//	        params.put("status", "pending");
	        
	        if(etat != null ) {

	        	params.put("status", etat);
	        }
	        
	        
	       // params.put("oauth_version", "1.0");
	       // params.put("after","2020-08-28T08:00:00");
//	        params.put("after","2020-08-28");
	        @SuppressWarnings("rawtypes")
			List orders = wooCommerce.getAll(EndpointBaseType.ORDERS.getValue(), params);
	        List<String> listeId = ligneServiceWebExterneService.findAllIDExterneByIdCompteService(compteServiceWeb.getIdCompteServiceWebExterne());
	       // nbFiles = orders.size();
	        
	        List<JSONObject> listeJSON = new ArrayList<JSONObject>();
	        for (Object object : orders) {
	        	 String orderStr = JSONObject.valueToString(object);
	        	 JSONObject ligneJson = new JSONObject(orderStr);
	        	 boolean existeDeja = false;
	        	 for (String id : listeId) {
					if(id.equals(String.valueOf(ligneJson.get("id")))) {
						existeDeja = true;
					}
	        	 }
	        	 if(!existeDeja) {
	        		 listeJSON.add(ligneJson);
	        		 System.out.println(orderStr);
	        	 }
	        	 
	        	 
			}
	        nbFiles = listeJSON.size();
	        
			//enregistrement de la derniere date d'import
			TaSynchroServiceExterne dateSynchro = new TaSynchroServiceExterne();
			Date now = new Date();
			dateSynchro.setDerniereSynchro(now);
			dateSynchro.setTaCompteServiceWebExterne(compteServiceWeb);
			dateSynchro.setTypeEntite(TaSynchroServiceExterne.TYPE_ENTITE_COMMANDE);
			dateSynchro= synchroService.merge(dateSynchro);
	        
	      //  return nbFiles;
	       return enregistreLignes(listeJSON);
		  
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public Integer enregistreLignes(List<JSONObject> lignes) throws Exception{
		Integer nbLignes = 0;
		List<String> mapErreurFichiers = new ArrayList<String>();
		List<TaLiaisonServiceExterne> listeLiaison = new ArrayList<TaLiaisonServiceExterne>();
		Map<String, TaTiers> listeRefTiers = new HashMap<String, TaTiers>();
		List<TaTiers> tiersCree = new ArrayList<TaTiers>();
		//on boucle sur les lignes de commandes
		for (JSONObject order : lignes) {
			List<TaLigneServiceWebExterne> listeLignesAMerge = new ArrayList<TaLigneServiceWebExterne>();
			try {		    
			    //on parcours chaque ligne de commande du fichier
			    JSONObject shippingAdressJson = order.getJSONObject("shipping");
			    JSONObject billingJson = order.getJSONObject("billing");
			    JSONArray arrayOrderItems = order.getJSONArray("line_items");
			    	
			    	//on parcours chaque produit différents pour chaque ligne de commande
			    	 for (int i2 = 0; i2 < arrayOrderItems.length(); i2++) {
			    		 JSONObject jsonOrderItem = arrayOrderItems.getJSONObject(i2);
			    		 TaLigneServiceWebExterne ligne = new TaLigneServiceWebExterne();
			    		 //concerne la ligne de commande en général
			    		 //ligne.setRefCommandeExterne(checkString(order.get("order_key")));
			    		 ligne.setRefCommandeExterne(checkString(order.getInt("id")));
			    		 ligne.setIdCommandeExterne(checkString(order.getInt("id")));
				    	 ligne.setLibelle("Ligne n° "+String.valueOf(jsonOrderItem.getInt("id"))+" "+checkString(jsonOrderItem.get("name")));
				    	 
				    	 SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				    	 Date dateCreation = formatter.parse(checkString(order.get("date_created")));
				    	 
				    	 ligne.setDateCreationExterne(dateCreation);
				    	 
				    	 //ligne.setDateUpdateExterne(checkString(order.get("updated_at")));
				    	 ligne.setJsonInitial(order.toString());
				    	 ligne.setTaCompteServiceWebExterne(compteServiceWeb);
				    	 
				    	 ligne.setNomTiers(checkString(billingJson.get("last_name")));
				    	 ligne.setPrenomTiers(checkString(billingJson.get("first_name")));
				    	 ligne.setRefTiers(checkString(billingJson.get("email")));
				    	 ligne.setMailTiers(checkString(billingJson.get("email")));
				    	 
				    	 
				    	 ligne.setEtatLigneExterne(checkString(order.get("status")));
				    	 
				    	 if(ligne.getEtatLigneExterne().equals("completed")) {
				    		 ligne.setEtatLigneExterne("terminée");
				    	 }else if(ligne.getEtatLigneExterne().equals("pending")) {
				    		 ligne.setEtatLigneExterne("en attente paiement");
				    	 }else if(ligne.getEtatLigneExterne().equals("processing")) {
				    		 ligne.setEtatLigneExterne("en cours");
				    	 }
				    	 
				    	 ligne.setRefTypePaiement(checkString(order.get("payment_method")));
				    	 
				    	 //concerne les produits
				    	 ligne.setRefArticle(checkString(jsonOrderItem.get("sku")));
				    	 ligne.setNomArticle(checkString(jsonOrderItem.get("name")));
				    	 ligne.setQteArticle(jsonOrderItem.getInt("quantity"));
				    	 
				    	 Double montantTotalDiscount = Double.parseDouble((String) order.get("discount_total"));
				    	 Double montantTaxDiscount = Double.parseDouble((String) order.get("discount_tax"));
				    	 Double montantFinalDiscount = montantTotalDiscount + montantTaxDiscount;
				    	 
				    	 Double montantTotalShipping = Double.parseDouble((String) order.get("shipping_total"));
				    	 Double montantTaxShipping = Double.parseDouble((String) order.get("shipping_tax"));
				    	 Double montantFinalShipping = montantTotalShipping + montantTaxShipping;
				    	 
				    	 if(montantFinalDiscount != null && montantFinalDiscount > 0) {
				    		 ligne.setMontantTotalDiscountDoc(montantFinalDiscount.toString());
				    	 }
				    	 if(montantFinalShipping != null && montantFinalShipping > 0) {
				    		 ligne.setMontantTotalLivraisonDoc(montantFinalShipping.toString());
				    	 }
				    	 String montantTotalTTC = checkString(order.get("total"));
				    	 ligne.setMontantTtcTotalDoc(checkString(montantTotalTTC));
				    	 
				    	 //je reçois le HT. Je calcule le ttc en ajoutant les taxes au HT.Le client rentre ses prix en TTC sur le woocommerce, et c'est le ttc le plus important
				    	 double montantTaxes =  Double.parseDouble((String) jsonOrderItem.get("total_tax"));
				    	 double montantHTDouble =  Double.parseDouble((String) jsonOrderItem.get("total"));
				    	 double montantTTCDouble = montantHTDouble + montantTaxes;
				    	 ligne.setMontantHt(checkString(jsonOrderItem.get("total")));				    	 
				    	 ligne.setMontantTtc(checkString(montantTTCDouble));
				    	 
				    	 //affecte les entités (tiers, articles) si les liaisons existes
				    	 ligne =  ligneServiceWebExterneService.affecteLiaison( ligne, serviceWeb, listeLiaison, false);
				    	 
				    	 
				    	 
				    	 
				    	//création du tiers si aucune liaisons et aucun tiers qui n'a cette adresse mail
				    	 if(ligne.getTaTiers() == null) {
				    		 Boolean creationTiers = true;
				    		//vérifie si une liaison ne va pas deja etre créer dans cette boucle
				    		 for (Map.Entry<String, TaTiers> entry : listeRefTiers.entrySet()) {
				    			 //si il y a effectivement une liaison qui va être créer, je n'ai pas besoin de créer le tiers, juste besoin d'affecter celui deja créer a cette ligne,
				    			 //plus bas juste avant le merge de la ligne
				    			 if(ligne.getRefTiers().equals(entry.getKey())) {
				    				 creationTiers = false;
				    				
				    			 }
				    		 }
				    		
				    		 if(creationTiers) {
				    			 Date now = new Date();
					    		 TaTiers tiers = new TaTiers();
					    		 //@Transactional(value=TxType.REQUIRES_NEW)
								 TaTTiers typeTiers = taTTiersService.findByCode("C");
					    		 TaAdresse adresse = new TaAdresse();
					    		 TaTAdr typeAdresse = taTAdrService.findByCode("FACT");
					    		 adresse.setTaTAdr(typeAdresse);
					    		 TaTelephone tel = new TaTelephone();
					    		 TaEmail mail = new TaEmail();
					    		 tiers.setNomTiers(checkString(billingJson.get("last_name")));
					    		 tiers.setPrenomTiers(checkString(billingJson.get("first_name")));
					    		 tiers.setActifTiers(1);
					    		 tiers.setDateAnniv(now);
					    		 tiers.setTtcTiers(0);
					    		 //@Transactional(value=TxType.REQUIRES_NEW)
					    		 tiers.setCodeTiers(taTiersService.genereCode(null));  
					    		 tiers.setCompte("411");
					    		 tiers.setTaTTiers(typeTiers);
					    		 if(tiers.getCodeCompta()==null 
					 					|| (tiers.getCodeCompta()!=null && tiers.getCodeCompta().equals(""))
					 					) {
					 				if(tiers.getCodeTiers().length()>6) {
					 					tiers.setCodeCompta(tiers.getCodeTiers().substring(0, 7));
					 				} else  {
					 					tiers.setCodeCompta(tiers.getCodeTiers().substring(0, tiers.getCodeTiers().length()));
					 				}
					 			}
					    		
					    		 adresse.setAdresse1Adresse(checkString(billingJson.get("address_1")));
					    		 adresse.setAdresse2Adresse(checkString(billingJson.get("address_2")));
					    		 adresse.setCodepostalAdresse(checkString(billingJson.get("postcode")));
					    		 adresse.setPaysAdresse(checkString(billingJson.get("country")));
					    		 adresse.setVilleAdresse(checkString(billingJson.get("city")));
					    		 adresse.setTaTiers(tiers);
					    		// adresse = adresseService.merge(adresse);
					    		 
					    		 tel.setNumeroTelephone(checkString(billingJson.get("phone")));
					    		 tel.setTaTiers(tiers);
					    		 tel.setCommAdministratifTelephone(1);
					    		 tel.setCommCommercialTelephone(1);
					    		// tel = telService.merge(tel);
					    		 
					    		 mail.setAdresseEmail(checkString(billingJson.get("email")));
					    		 mail.setTaTiers(tiers);
					    		// mail = mailService.merge(mail);
					    		// tiers.setTaAdresse(adresse);
					    		 tiers.addAdresse(adresse);
					    		 tiers.setTaTelephone(tel);
					    		 tiers.addTelephone(tel);
					    		 tiers.setTaEmail(mail);
					    		 tiers.addEmail(mail);
					    		//@Transactional(value=TxType.REQUIRES_NEW)
					    		 //je vire le merge ici car il pose probleme
					    		//tiers = taTiersService.merge(tiers);
					    		
					    		ligne.setTaTiers(tiers);
					    		
					    		//création de la liaison entre ce nouveau tiers et sont adresse mail chez shippingBo
//					    		TaLiaisonServiceExterne liaison = new TaLiaisonServiceExterne();
//					    		liaison.setIdEntite(tiers.getIdTiers());
//					    		liaison.setRefExterne(ligne.getRefTiers());
//					    		liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
//					    		liaison.setServiceWebExterne(serviceWeb);
//					    		
//					    		liaison =liaisonService.merge(liaison);
//					    		listeLiaison.add(liaison);
					    		
					    		//j'ajoute la référence tiers et le code de ce nouveau tiers 
					    		//pour permettre d'éviter de créer un autre tiers si une meme ref tiers ce présente dans la boucle,
					    		//et bien sur pour créer les liaisons à la fin
					    		listeRefTiers.put(ligne.getRefTiers(), tiers);
					    		
				    		 }
					    		
				    		
				    	 
			    	 	}
				    	 
				    	 
				    	 listeLignesAMerge.add(ligne);
				    	
			    	 }//fin de la boucle sur les lignes de produits a l'intérieur d'une commande
			    	 
			    	 //regroupement des lignes par commandes
					    Map<String,List<TaLigneServiceWebExterne>> mapLignesParCommandes = new HashMap<>();
					    for (TaLigneServiceWebExterne li : listeLignesAMerge) {
							String cle = li.getRefCommandeExterne();
							if(!mapLignesParCommandes.keySet().contains(cle)) {
								mapLignesParCommandes.put(cle, new ArrayList<>());
							}
							mapLignesParCommandes.get(cle).add(li);
						}
					    
					    //parcours de chaque commande et création si nécéssaire de lignes de frais de port et discount
					    for (String idCommande : mapLignesParCommandes.keySet()) {
					    	if(!mapLignesParCommandes.get(idCommande).isEmpty()) {
					    		TaLigneServiceWebExterne dtoEntete = mapLignesParCommandes.get(idCommande).get(0);
					    		//ajout d'une ligne pour la remise globale sur le document
					    		if(dtoEntete.getMontantTotalDiscountDoc() != null && !dtoEntete.getMontantTotalDiscountDoc().isEmpty()) {
					    			 Double montantDiscount = Double.parseDouble(dtoEntete.getMontantTotalDiscountDoc());
					    			 
					    	    	 if(montantDiscount > 0) {
					    	    		 TaLigneServiceWebExterne ligneDiscount = new TaLigneServiceWebExterne();
					    	    		 ligneDiscount.setIdCommandeExterne(idCommande);
					    	    		 ligneDiscount.setRefCommandeExterne(dtoEntete.getRefCommandeExterne());
					    	    		 ligneDiscount.setLibelle("Remise sur la commande");
					    	    		 ligneDiscount.setRefArticle("REMISE");
					    	    		 ligneDiscount.setNomArticle("Remise sur la commande");
					    	    		 ligneDiscount.setRefTiers(dtoEntete.getRefTiers());
					    	    		 ligneDiscount.setQteArticle(1);
					    	    		 ligneDiscount.setPrenomTiers(dtoEntete.getPrenomTiers());
					    	    		 ligneDiscount.setNomTiers(dtoEntete.getNomTiers());
					    	    		 ligneDiscount.setDateCreationExterne(dtoEntete.getDateCreationExterne());
					    	    		 ligneDiscount.setMontantTtc("-"+montantDiscount.toString());
					    	    		 ligneDiscount.setMontantTotalDiscountDoc(dtoEntete.getMontantTotalDiscountDoc());
					    	    		 ligneDiscount.setMontantTotalLivraisonDoc(dtoEntete.getMontantTotalLivraisonDoc());
					    	    		 ligneDiscount.setMontantTtcTotalDoc(dtoEntete.getMontantTtcTotalDoc());
					    	    		 ligneDiscount.setTaCompteServiceWebExterne(compteServiceWeb);
					    	    		 ligneDiscount.setEtatLigneExterne(dtoEntete.getEtatLigneExterne());
					    	    		 //affecte les entités (tiers, articles) si les liaisons existes
					    	    		 ligneDiscount =  ligneServiceWebExterneService.affecteLiaison( ligneDiscount, serviceWeb, listeLiaison, false);
					    	    		 listeLignesAMerge.add(ligneDiscount);
					    	    	 }
					    		}
					    		//ajout d'une ligne pour les frais de port sur le document
					    		if(dtoEntete.getMontantTotalLivraisonDoc() != null && !dtoEntete.getMontantTotalLivraisonDoc().isEmpty()) {
					    			 Double montantLivraison = Double.parseDouble(dtoEntete.getMontantTotalLivraisonDoc());
					    			 
					    	    	 if(montantLivraison > 0) {
					    	    		 TaLigneServiceWebExterne ligneLivraison = new TaLigneServiceWebExterne();
					    	    		 ligneLivraison.setIdCommandeExterne(idCommande);
					    	    		 ligneLivraison.setRefCommandeExterne(dtoEntete.getRefCommandeExterne());
					    	    		 ligneLivraison.setLibelle("Frais de port");
					    	    		 ligneLivraison.setNomArticle("Frais de port");
					    	    		 ligneLivraison.setRefArticle("FRAISPORT");
					    	    		 ligneLivraison.setQteArticle(1);
					    	    		 ligneLivraison.setRefTiers(dtoEntete.getRefTiers());
					    	    		 ligneLivraison.setPrenomTiers(dtoEntete.getPrenomTiers());
					    	    		 ligneLivraison.setNomTiers(dtoEntete.getNomTiers());
					    	    		 ligneLivraison.setDateCreationExterne(dtoEntete.getDateCreationExterne());
					    	    		 ligneLivraison.setMontantTtc(montantLivraison.toString());
					    	    		 ligneLivraison.setMontantTotalDiscountDoc(dtoEntete.getMontantTotalDiscountDoc());
					    	    		 ligneLivraison.setMontantTotalLivraisonDoc(dtoEntete.getMontantTotalLivraisonDoc());
					    	    		 ligneLivraison.setMontantTtcTotalDoc(dtoEntete.getMontantTtcTotalDoc());
					    	    		 ligneLivraison.setTaCompteServiceWebExterne(compteServiceWeb);
					    	    		 ligneLivraison.setEtatLigneExterne(dtoEntete.getEtatLigneExterne());
					    	    		 //affecte les entités (tiers, articles) si les liaisons existes
					    	    		 ligneLivraison =  ligneServiceWebExterneService.affecteLiaison( ligneLivraison, serviceWeb, listeLiaison, false);
					    	    		 listeLignesAMerge.add(ligneLivraison);
					    	    	 }
					    		}
					    		
					    	}
					    }

				    //on merge toutes les lignes du fichiers 
				    for (TaLigneServiceWebExterne li : listeLignesAMerge) {
				    	if(li.getTaTiers() == null) {
				    		for (TaTiers taTiers : tiersCree) {
								if(taTiers.getTaEmail().getAdresseEmail().equals(li.getRefTiers())) {
									taTiers = taTiersService.findById(taTiers.getIdTiers());
									li.setTaTiers(taTiers);
								}
							}
				    	}
				    	 li =  transactionalService.mergeLigneServiceWebExterne(li);
				    	 //ici je rempli une liste de tiers mergés
				    	 boolean existeDeja = false;
				    	 for (TaTiers taTiers : tiersCree) {
				    		 if (taTiers.getCodeTiers().equals(li.getTaTiers().getCodeTiers())) {
				    			 existeDeja = true;
							}
						 }
				    	 if(!existeDeja) {
				    		 tiersCree.add(li.getTaTiers());
				    	 }
				    	 //c'est ici qu'il faut que je merge mes liaisons externes, pas avant. Car ici j'ai l'id du tiers, pas avant, car c'est le merge de la ligne qui va merge le tiers.
				    	 
				    	 //je compare la ligne de service que j'ai a merge avec la liste de ref tiers pour liaison a merge
				    	 for (Map.Entry<String, TaTiers> entry : listeRefTiers.entrySet()) {
				    		 //si je trouve une "ref tiers pour liaison a faire" correspondante a la ligne de service que je vien de merge 
				    		 if(li.getRefTiers().equals(entry.getKey())) {
				    			 //je vérifie que je n'ai pas déjà merge une liaison avec cette ref tiers
				    			 TaLiaisonServiceExterne liaisonExistante = liaisonService.findByRefTiersAndByIdServiceWebExterne(li.getRefTiers(), serviceWeb.getIdServiceWebExterne());
				    			 //si je n'ai pas deja merge une liaison avec cette ref tiers, je l'a créer ci-dessous
				    			 if(liaisonExistante == null) {
				    				//création de la liaison entre ce nouveau tiers et sont adresse mail chez le service externe
							    		TaLiaisonServiceExterne liaison = new TaLiaisonServiceExterne();
							    		liaison.setIdEntite(li.getTaTiers().getIdTiers());
							    		liaison.setRefExterne(li.getRefTiers());
							    		liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
							    		liaison.setServiceWebExterne(serviceWeb);
							    		
							    		liaison =liaisonService.merge(liaison);
							    		listeLiaison.add(liaison);
				    			 }
									
								}
				    		}
				    	
				    	 
				    	 
				    	 nbLignes++;
					}
			   

				 
			} catch (Exception e1) {
				mapErreurFichiers.add(checkString(order.getInt("id")));
				e1.printStackTrace();
			    
			}
						
		
		}//fin boucle sur les lignes de commandes
		
		
		
		if(!mapErreurFichiers.isEmpty()) {
			String message = "Les commandes suivantes ont été ignorés à cause d'erreurs. Veuillez contacter le support de BDG pour plus d'informations : ";
			for (String string : mapErreurFichiers) {
				message += string+" ";
			}
			 throw new Exception(message);
		}
		

		return nbLignes;

	}
	
	 

	public TaCompteServiceWebExterne getCompteServiceWeb() {
		return compteServiceWeb;
	}

	public void setCompteServiceWeb(TaCompteServiceWebExterne compteServiceWeb) {
		this.compteServiceWeb = compteServiceWeb;
	}


}
