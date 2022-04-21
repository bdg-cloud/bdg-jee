package fr.legrain.servicewebexterne.service.divers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
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
import org.jruby.ir.operands.Array;
import org.json.JSONArray;
import org.json.JSONObject;


import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaSynchroServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaPrestashopServiceWebExterneServiceRemote;
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
public class TaPrestashopServiceWebExterne implements ITaPrestashopServiceWebExterneServiceRemote{
	
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
	
	//prestashop api key = "61XVX9HFVXZRMRXKW3F8D5C45NUB9KQY";
	//https://www.pro-boutique.fr/api/orders/?ws_key=61XVX9HFVXZRMRXKW3F8D5C45NUB9KQY&display=full&output_format=JSON
	
	
	String consumerKey = "";
//	String consumerSecret = "";
	String domaine = "";
	//String domaine = "https://www.pro-boutique.fr/api/";
	
	
	String dossierTravail = "/prestashop/orders/";
	
	
	String localDir = "";
	
	String codeService = "PRESTASHOP";
	
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
		//consumerSecret = compteServiceWeb.getApiKey2();
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
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	
	private JSONObject httpRequestGET(String chemin, Map<String, String> param) {
		JSONObject object = null;
		String url = domaine;
		try {
			initCompteServiceWeb();
		
			String httpMethod = "GET";
			
			String paramKeyWSAPIKey = "ws_key";
			String paramValueWSAPIKey = consumerKey;
			
			String paramKeyOutputFormat = "output_format";
			String paramValueOutputFormat = "JSON";

			param.put(paramKeyOutputFormat, paramValueOutputFormat);
			
			String nonEncodedParameterString = param.keySet().stream()
				      .map(key -> key + "=" + param.get(key))
				      .collect(Collectors.joining("&"));
			
			//-----------Construction de l'url complete --------------
			URL urlURL;
			String urlcomplete = url+chemin+"?"+paramKeyWSAPIKey+"="+paramValueWSAPIKey+"&"+nonEncodedParameterString;
			System.out.println("Url complete : "+urlcomplete);
			try {
				urlURL = new URL(urlcomplete);
				HttpURLConnection conn = (HttpURLConnection) urlURL.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				
				 String jsonText = readAll(br);
				 
				 conn.disconnect();
				 
				 if(jsonText.startsWith("{")) {
					 object = new JSONObject(jsonText);
				 }
			
			 
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FinderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return object;
	}
	
	
	
	/**
	 * @author yann
	 * Va chercher les commandes sur le service externe
	 * probablement déclenché par un CRON au final
	 * @return le nombre de commandes traités
	 * @throws Exception 
	 */
	public Integer getOrdersREST() throws Exception{
		 String chemin = "orders";
		 Integer nbFiles = 0;
		Map<String, String> param = new HashMap<>();
		param.put("display", "full");
		param.put("date", "1");
		Date dateDernierImport = synchroService.findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO(TaSynchroServiceExterne.TYPE_ENTITE_COMMANDE, compteServiceWeb.getIdCompteServiceWebExterne());
		if(dateDernierImport != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
			String strDate = dateFormat.format(dateDernierImport);
			String prefixDate = ">[";
			String suffixDate = "]";
			strDate = prefixDate+strDate+suffixDate;
			//param.put("filter[date_add]","<[2000-00-00%2000:00:00]");
			param.put("filter[date_add]",strDate);
		}
		
		JSONObject ordersJSON = httpRequestGET( chemin, param);
		
		//enregistrement de la derniere date d'import
		TaSynchroServiceExterne dateSynchro = new TaSynchroServiceExterne();
		Date now = new Date();
		dateSynchro.setDerniereSynchro(now);
		dateSynchro.setTaCompteServiceWebExterne(compteServiceWeb);
		dateSynchro.setTypeEntite(TaSynchroServiceExterne.TYPE_ENTITE_COMMANDE);
		dateSynchro= synchroService.merge(dateSynchro);
//		System.setProperty("https.protocols", "TLSv1.1");
		System.setProperty("https.protocols", "TLSv1.3");
		if(ordersJSON == null) {
			
			return nbFiles;
		}else {
			JSONArray ordersArray = ordersJSON.getJSONArray("orders");
		    List<JSONObject> orders = new ArrayList<JSONObject>();

			for (int i = 0; i < ordersArray.length(); i++) {
				JSONObject order = ordersArray.getJSONObject(i);
				orders.add(order);
				
			}
			
			return enregistreLignes(orders);
			
		}

	        
	     
	     
		  
	}
	
	private JSONObject getCustomerJSON(Integer id) {
		String chemin ="customers/"+id;
		Map<String, String> param = new HashMap<>();
		JSONObject customer = httpRequestGET(chemin, param ).getJSONObject("customer");
		
		return customer;
	}
	
	private JSONObject getAdresseJSON(Integer id) {
		String chemin ="addresses/"+id;
		Map<String, String> param = new HashMap<>();
		JSONObject adresse = httpRequestGET(chemin, param ).getJSONObject("address");
		
		return adresse;
	}
	
	private JSONObject getCountryJSON(Integer id) {
		String chemin ="countries/"+id;
		Map<String, String> param = new HashMap<>();
		JSONObject country = httpRequestGET(chemin, param ).getJSONObject("country");
		
		return country;
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public Integer enregistreLignes(List<JSONObject> lignes) throws Exception{
		Integer nbLignes = 0;
		List<String> mapErreurFichiers = new ArrayList<String>();
		List<TaLiaisonServiceExterne> listeLiaison = new ArrayList<TaLiaisonServiceExterne>();
		Map<String, TaTiers> listeRefTiers = new HashMap<String, TaTiers>();
		List<TaTiers> tiersCree = new ArrayList<TaTiers>();
		for (JSONObject order : lignes) {
			List<TaLigneServiceWebExterne> listeLignesAMerge = new ArrayList<TaLigneServiceWebExterne>();
			try {		    
			    //on parcours chaque ligne de commande du fichier
				System.setProperty("https.protocols", "TLSv1.3");
			    JSONObject shippingAdressJson = getAdresseJSON(order.getInt("id_address_invoice"));
			    System.setProperty("https.protocols", "TLSv1.3");
			    JSONObject country = getCountryJSON(shippingAdressJson.getInt("id_country"));
			    System.setProperty("https.protocols", "TLSv1.3");
				JSONObject customer = getCustomerJSON(order.getInt("id_customer"));
				System.setProperty("https.protocols", "TLSv1.3");
			    JSONArray arrayOrderItems =  order.getJSONObject("associations").getJSONArray("order_rows");
			    	
			    	//on parcours chaque produit différents pour chaque ligne de commande
			    	 for (int i2 = 0; i2 < arrayOrderItems.length(); i2++) {
			    		 JSONObject jsonOrderItem = arrayOrderItems.getJSONObject(i2);
			    		 TaLigneServiceWebExterne ligne = new TaLigneServiceWebExterne();
			    		 //concerne la ligne de commande en général
			    		 System.setProperty("https.protocols", "TLSv1.3");
			    		 ligne.setRefCommandeExterne(checkString(order.get("reference")));
			    		 System.setProperty("https.protocols", "TLSv1.3");
			    		 ligne.setIdCommandeExterne(checkString(order.get("reference")));
				    	 ligne.setLibelle("Ligne n° "+checkString(jsonOrderItem.get("id"))+" "+checkString(jsonOrderItem.get("product_name")));
				    	 
				    	 SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	 Date dateCreation = formatter.parse(checkString(order.get("date_add")));
				    	 
				    	 ligne.setDateCreationExterne(dateCreation);
				    	 //ligne.setDateUpdateExterne(checkString(order.get("updated_at")));
				    	 ligne.setJsonInitial(order.toString());
				    	 ligne.setTaCompteServiceWebExterne(compteServiceWeb);
				    	 
				    	 ligne.setNomTiers(checkString(customer.get("lastname")));
				    	 ligne.setPrenomTiers(checkString(customer.get("firstname")));
				    	 ligne.setRefTiers(checkString(customer.get("email")));
				    	 ligne.setMailTiers(checkString(customer.get("email")));
				    	 
				    	 ligne.setRefTypePaiement(checkString(order.get("payment")));
				    	 
				    	 //concerne les produits
				    	 ligne.setRefArticle(checkString(jsonOrderItem.get("product_reference")));
				    	 ligne.setNomArticle(checkString(jsonOrderItem.get("product_name")));
				    	 ligne.setQteArticle(jsonOrderItem.getInt("product_quantity"));
				    	 
				    	 Double montantTotalDiscount =  Double.parseDouble((String) order.get("total_discounts_tax_incl"));
				    	 Double montantTotalShipping =  Double.parseDouble((String) order.get("total_shipping_tax_incl"));
				    	 String montantTotalTTC = checkString(order.get("total_paid_tax_incl"));
				    	 
				    	 if(montantTotalDiscount != null && montantTotalDiscount > 0) {
				    		 ligne.setMontantTotalDiscountDoc(montantTotalDiscount.toString());
				    	 }
				    	 if(montantTotalShipping != null && montantTotalShipping > 0) {
				    		 ligne.setMontantTotalLivraisonDoc(montantTotalShipping.toString());
				    	 }
				    	 
				    	 ligne.setMontantTtcTotalDoc(montantTotalTTC);
				    	 
				    	 Integer quantity =  ligne.getQteArticle();
//				    	 double montantTaxes =  Double.parseDouble((String) jsonOrderItem.get("total_tax"));
				    	 double montantHTDouble =  Double.parseDouble((String) jsonOrderItem.get("unit_price_tax_excl"));
				    	 double montantTTCDouble =  Double.parseDouble((String) jsonOrderItem.get("unit_price_tax_incl"));
				    	 montantHTDouble = montantHTDouble * quantity;
				    	 montantTTCDouble = montantTTCDouble * quantity;
				    	 ligne.setMontantHt(checkString(montantHTDouble));				    	 
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
						    		 tiers.setNomTiers(checkString(customer.get("lastname")));
						    		 tiers.setPrenomTiers(checkString(customer.get("firstname")));
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
						    		
						    		 adresse.setAdresse1Adresse(checkString(shippingAdressJson.get("address1")));
						    		 adresse.setAdresse2Adresse(checkString(shippingAdressJson.get("address2")));
						    		 adresse.setCodepostalAdresse(checkString(shippingAdressJson.get("postcode")));
						    		 adresse.setPaysAdresse(checkString(country.get("name")));
						    		 adresse.setVilleAdresse(checkString(shippingAdressJson.get("city")));
						    		 adresse.setTaTiers(tiers);
						    		// adresse = adresseService.merge(adresse);
						    		 
						    		 tel.setNumeroTelephone(checkString(shippingAdressJson.get("phone")));
						    		 tel.setTaTiers(tiers);
						    		 tel.setCommAdministratifTelephone(1);
						    		 tel.setCommCommercialTelephone(1);
						    		// tel = telService.merge(tel);
						    		 
						    		 mail.setAdresseEmail(checkString(customer.get("email")));
						    		 mail.setTaTiers(tiers);
						    		// mail = mailService.merge(mail);
						    		// tiers.setTaAdresse(adresse);
						    		 tiers.addAdresse(adresse);
						    		 tiers.setTaTelephone(tel);
						    		 tiers.addTelephone(tel);
						    		 tiers.setTaEmail(mail);
						    		 tiers.addEmail(mail);
						    		//@Transactional(value=TxType.REQUIRES_NEW)
						    		//tiers = taTiersService.merge(tiers);
						    		
						    		ligne.setTaTiers(tiers);
						    		
						    		//création de la liaison entre ce nouveau tiers et sont adresse mail chez shippingBo
//						    		TaLiaisonServiceExterne liaison = new TaLiaisonServiceExterne();
//						    		liaison.setIdEntite(tiers.getIdTiers());
//						    		liaison.setRefExterne(ligne.getRefTiers());
//						    		liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
//						    		liaison.setServiceWebExterne(serviceWeb);
//						    		
//						    		liaison =liaisonService.merge(liaison);
//						    		listeLiaison.add(liaison);
						    		
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
				    	    		 ligneDiscount.setRefArticle("REMISESHIPPINGBO");
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
				    	    		 ligneLivraison.setRefArticle("FRAISPORTSHIPPINGBO");
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
						
		
		}//fin boucle sur les fichiers
		
		if(!mapErreurFichiers.isEmpty()) {
			String message = "Les commandes suivantes ont été ignorés à cause d'erreurs. Veuillez contacter le support de BDG pour plus d'informations : ";
			for (String string : mapErreurFichiers) {
				message += string+" ";
			}
			 throw new Exception(message);
		}
		
		//enregistrement de la derniere date d'import
		TaSynchroServiceExterne dateSynchro = new TaSynchroServiceExterne();
		Date now = new Date();
		dateSynchro.setDerniereSynchro(now);
		dateSynchro.setTaCompteServiceWebExterne(compteServiceWeb);
		dateSynchro.setTypeEntite(TaSynchroServiceExterne.TYPE_ENTITE_COMMANDE);
		dateSynchro= synchroService.merge(dateSynchro);
		return nbLignes;

	}
	
	 

	public TaCompteServiceWebExterne getCompteServiceWeb() {
		return compteServiceWeb;
	}

	public void setCompteServiceWeb(TaCompteServiceWebExterne compteServiceWeb) {
		this.compteServiceWeb = compteServiceWeb;
	}


}
