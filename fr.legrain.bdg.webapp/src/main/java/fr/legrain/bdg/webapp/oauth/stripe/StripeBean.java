package fr.legrain.bdg.webapp.oauth.stripe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

//import com.stripe.Stripe;
//import com.stripe.exception.PermissionException;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Account;
//import com.stripe.model.AccountCollection;
//import com.stripe.model.AccountLink;
//import com.stripe.param.AccountCreateParams;
//import com.stripe.param.AccountLinkCreateParams;
//import com.stripe.param.AccountLinkCreateParams.Type;

import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthScopeClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthServiceClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.webapp.agenda.LgrTabScheduleEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaOAuthScopeClient;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.data.LibDate;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;

@Named
@ViewScoped 
//@SessionScoped
public class StripeBean implements Serializable {  
	/*
	 * https://stripe.com/docs/connect/oauth-reference
	 * https://stripe.com/docs/connect/oauth-standard-accounts
	 */
	private static final long serialVersionUID = 8677377809172429592L;

//	private @EJB DevService devService;
////	private @EJB GoogleOAuthClient googleOAuthClient;
	
	private TaStripeAccount taStripeAccount = null;
	
	private String displayName;
	private String surname;
	private String userPrincipalName;
	private Boolean compteVerifieParStripe;
	private Boolean compteUtilisableParStripe;
//	
	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
	
	private StripeOAuthUtil stripeOAuthUtil = new StripeOAuthUtil();
	
	@Inject private StripeTokenSessionBean stripeTokenSessionBean;
	
	private @EJB ITaOAuthTokenClientServiceRemote taAuthTokenClientService;
	private @EJB ITaOAuthServiceClientServiceRemote taAuthServiceClientService;
	private @EJB ITaOAuthScopeClientServiceRemote taAuthScopeClientService;	
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;	
	
	private @EJB ITaStripeAccountServiceRemote taStripeAccountService;
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementStripeDossierService;
	
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	private TaOAuthTokenClient taOAuthTokenClient;
	
	private List<TaOAuthScopeClient> listeScopeStripe = null;
	private List<TaOAuthScopeClient> listeScopeStripeSelected = null;
	
	private String typeIntegration = null;
	private boolean validToken = false;
	
	
	@PostConstruct
	public void init() {
		
		boolean modeTest = false;
		modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
		if(modeTest) {
       	 //utilisé la clé test du programme ou de legrain82
        } else {
       	 //utilisé la clé de prod du programme ou de legrain82
        }
//		Stripe.apiKey = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh";
		

		
		initOAuth();
		validToken = validToken();
		if(validToken) {
			utiliseOAuthCredential();
		}
	}
	
	
	public void updateAccount(String accountId) {
		paiementStripeDossierService.updateAccountFromStripe(null, accountId);
//		try {
//			if(account!=null) {
//				account = Account.retrieve(accountId);
//				taStripeAccount.setIdExterne(account.getId());
//				taStripeAccount.setBusinessType(account.getBusinessType());
//				taStripeAccount.setChargesEnabled(account.getChargesEnabled());
//				taStripeAccount.setCountry(account.getCountry());
//				taStripeAccount.setDefaultCurrency(account.getDefaultCurrency());
//				taStripeAccount.setDetailsSubmitted(account.getDetailsSubmitted());
//				taStripeAccount.setEmail(account.getEmail());
//				taStripeAccount.setType(account.getType());
//				System.out.println("Deleted ===== "+account.getDeleted());
//				taStripeAccount = taStripeAccountService.merge(taStripeAccount);
//			}
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
	}
	
	public void initOAuth() {
		try {
			
			listeScopeStripe = taAuthScopeClientService.selectAll(taAuthServiceClientService.findByCode(TaOAuthServiceClient.SERVICE_STRIPE));
			if(listeScopeStripeSelected==null) {
				listeScopeStripeSelected = new ArrayList<>();
				listeScopeStripeSelected.addAll(listeScopeStripe);
			} else if(listeScopeStripeSelected.isEmpty()) {
				listeScopeStripeSelected.addAll(listeScopeStripe);
			}
			
			String usermail = sessionInfo.getUtilisateur().getUsername();
			
			displayName = null;
			surname = null;
			userPrincipalName = null;
			
			stripeOAuthUtil.initOAuth(listeScopeStripeSelected,lgrEnvironnementServeur.isModeTestOAuth());
			
			taOAuthTokenClient = taAuthTokenClientService.findByKey(usermail, taAuthServiceClientService.findByCode(TaOAuthServiceClient.SERVICE_STRIPE));
	        
			if(taOAuthTokenClient!=null) {
				stripeTokenSessionBean.setAccessToken(taOAuthTokenClient.getAccessToken());
				stripeTokenSessionBean.setRefreshToken(taOAuthTokenClient.getRefreshToken());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void connectOAuth(ActionEvent event) {
		init();
		typeIntegration = TaStripeAccount.STRIPE_CONNECT_INTEGRATION_OAUTH;
		String url = buildUrlOAuth();
		PrimeFaces current = PrimeFaces.current();
        current.executeScript("oauthWindowStripeConnect('"+url+"');");
	}
	
	public TaStripeAccount informationStripeConnectDansDossier() {
		TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
		if(taStripeAccount!=null && taStripeAccount.getIdExterne()!=null && !taStripeAccount.getIdExterne().trim().equals("")) {
			return taStripeAccount;
		}
		return null;
	}
	
	public boolean liaisonStripeConnectValideEtAJour(TaStripeAccount compteStripeConnect) {
		boolean liaisonBDGCompteOKChezStripe = false; 
		
		liaisonBDGCompteOKChezStripe = paiementStripeDossierService.liaisonBDGCompteOKChezStripe(compteStripeConnect.getIdExterne());
//		try {
//			Account account = Account.retrieve(compteStripeConnect.getIdExterne());
//			liaisonBDGCompteOKChezStripe = true; 
//			if(liaisonBDGCompteOKChezStripe) {
//				updateAccount(compteStripeConnect.getIdExterne());
//			}
//		} catch (PermissionException e) {
//			e.printStackTrace();
//			return false;
//		} catch (StripeException e) {
//			e.printStackTrace();
//			return false;
//		}
		return liaisonBDGCompteOKChezStripe;
	}
	
	public TaStripeAccount forcerIdCompteConnectDossier(String nouvelIdCompteStripeConnect) {
		TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
		taStripeAccount.setIdExterne(nouvelIdCompteStripeConnect);
		taStripeAccount = taStripeAccountService.merge(taStripeAccount);
		updateAccount(taStripeAccount.getIdExterne());
		return taStripeAccount;
	}
	
	public void connectApi(ActionEvent event) {
		init();
//		account = null;
		typeIntegration = TaStripeAccount.STRIPE_CONNECT_INTEGRATION_API;
		
		taStripeAccount = informationStripeConnectDansDossier();
		
		if(taStripeAccount!=null) {
			//si on a un id on essai de mettre a jour les infos de BDG sur ce compte à partir de stripe (peut planter si accès révoqué)
			// com.stripe.exception.InvalidRequestException: You requested an account link for an account that is not connected to your platform or does not exist.; request-id: req_L6mCaop7UNhp1r
			
			//pour eviter un plantage, on peut vérifier que cet id de compte est bien lié a BDG chez stripe
			boolean liaisonBDGCompteOKChezStripe = liaisonStripeConnectValideEtAJour(taStripeAccount);
			
			if(!liaisonBDGCompteOKChezStripe) {
				//le compte a surement été révoqué 
				//il faut effacer les donnees concernant le compte dans BDG pour donner la possibilité de refaire une liaison
				taStripeAccount.effacerInfosCompte();
				taStripeAccount = taStripeAccountService.merge(taStripeAccount);
			}
		} 
		if(taStripeAccount.getIdExterne()==null) {
			//pas d'information de compte dans BDG
			
			//TODO a décommander, en commentaire pour limiter la création de nouveau compte (assez long a supprimer chez stripe)
//			account = createAccount(); //creation du compte chez stripe
//			updateAccount(account.getId()); //mise a jour des infos dans BDG
		} 
		
		String url = buildUrlApi(taStripeAccount.getIdExterne());
		PrimeFaces current = PrimeFaces.current();
        current.executeScript("oauthWindowStripeConnect('"+url+"');");
	}
	
	public String buildUrlApi(String accountId) {
		try {
			String state = tenantInfo.getTenantId()+"##"+sessionInfo.getUtilisateur().getId();
			state = LibCrypto.encrypt(state);
			
//			AccountLink ln = createAccountLink(accountId,stripeOAuthUtil.getRedirectUri(),stripeOAuthUtil.getRedirectUri(),state);
//			
//			return ln.getUrl();
			return paiementStripeDossierService.createAccountLink(accountId,stripeOAuthUtil.getRedirectUri(),stripeOAuthUtil.getRedirectUri(),state);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stripeOAuthUtil.getAuthorizeUrl();
	}
	
	public String buildUrlOAuth() {
		try {
			/*
			 * https://stripe.com/docs/connect/oauth-reference
			 */
			TaInfoEntreprise infoEntreprise = taInfoEntrepriseService.findInstance();
			
			String state = tenantInfo.getTenantId()+"##"+sessionInfo.getUtilisateur().getId();
			state = LibCrypto.encrypt(state);
			
			String parametres = "";
			if(infoEntreprise.getEmailInfoEntreprise()!=null) {
				parametres += "&stripe_user[email]="+infoEntreprise.getEmailInfoEntreprise();
			}
			if(infoEntreprise.getWebInfoEntreprise()!=null) {
				parametres += "&stripe_user[url]="+URLEncoder.encode(infoEntreprise.getWebInfoEntreprise(),"UTF-8");
			}
//			parametres += "stripe_user[product_description]=...";
			if(infoEntreprise.getTaTiers()!=null && infoEntreprise.getTaTiers().getNomTiers()!=null) {
				parametres += "&stripe_user[last_name]="+URLEncoder.encode(infoEntreprise.getTaTiers().getNomTiers(),"UTF-8");
			}
			if(infoEntreprise.getTaTiers()!=null && infoEntreprise.getTaTiers().getPrenomTiers()!=null) {
				parametres += "&stripe_user[first_name]="+URLEncoder.encode(infoEntreprise.getTaTiers().getPrenomTiers(),"UTF-8");
			}
			if(infoEntreprise.getNomInfoEntreprise()!=null) {
				parametres += "&stripe_user[business_name]="+URLEncoder.encode(infoEntreprise.getNomInfoEntreprise(),"UTF-8");
			}
			parametres += "&stripe_user[country]=FR";
			
			stripeOAuthUtil.setAuthorizeUrl(
					stripeOAuthUtil.getEndPoint()
					+"client_id="+stripeOAuthUtil.getApplicationId()
					+"&response_type=code"
					+"&redirect_uri="+URLEncoder.encode(stripeOAuthUtil.getRedirectUri(),"UTF-8")
					+"&scope="+URLEncoder.encode(stripeOAuthUtil.getScope(),"UTF-8")
					//+"&stripe_landing=login"
					+"&stripe_landing=register"
					+"&state="+state
					+parametres
					);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stripeOAuthUtil.getAuthorizeUrl();
	}
	
	public void deconnecterOAuth(String idCompteADeconnecter) {
		paiementStripeDossierService.deauthorizeStripeOAuthAccount(stripeOAuthUtil.getApplicationId(), idCompteADeconnecter);
	}

	
	public void updateView(ActionEvent event) {
		init();
//		stripeTokenSessionBean.de("aaa");
//		testAPI();
	}
	
	public boolean showDialog() {
		validToken = validToken();
		if (validToken) {
			return true;
		} else {
			return false;
		}
	}
	
	public void revokeToken(ActionEvent evt) {
		revokeToken();
	}
	
	public void revokeToken() {
		TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
		deconnecterOAuth(taStripeAccount.getIdExterne());
		taStripeAccount.effacerInfosCompte();
		taStripeAccount = taStripeAccountService.merge(taStripeAccount);
		validToken = validToken();
	}
	
	public boolean validToken() {
		try {
			validToken = false;
			TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
			if(taStripeAccount.getIdExterne()!=null) {
				if(paiementStripeDossierService.liaisonBDGCompteOKChezStripe(taStripeAccount.getIdExterne())) {
					paiementStripeDossierService.updateAccountFromStripe(null, taStripeAccount.getIdExterne());
					displayName = taStripeAccount.getIdExterne();
					if(taStripeAccount.getEmail()!=null) {
						userPrincipalName = taStripeAccount.getEmail();
					} else {
						userPrincipalName = "";
					}
					compteVerifieParStripe = taStripeAccount.getDetailsSubmitted();
					compteUtilisableParStripe = taStripeAccount.getChargesEnabled();
					validToken = true;
					return true;
				}
			}
			return false;
//			if(stripeTokenSessionBean!=null && stripeTokenSessionBean.getAccessToken()!=null) {
//				taOAuthTokenClient = taAuthTokenClientService.findByKey(sessionInfo.getUtilisateur().getUsername(), taAuthServiceClientService.findByCode(TaOAuthServiceClient.SERVICE_STRIPE));
//				if(taOAuthTokenClient!=null) {
//					
//					
//
//					long expireIn = taOAuthTokenClient.getExpiresIn()*1000;//durée Microsoft en seconde vers milli seconde
//					long margeSecuriteAppelToken = 5000; //+5s pour couvrir le temps de l'appel suivant
//					expireIn += margeSecuriteAppelToken;
//					
//					Calendar timeout = Calendar.getInstance();
//					timeout.setTimeInMillis(taOAuthTokenClient.getDateMiseAJour().getTime() + expireIn);
//					
////					LocalDate ldDateExpiration = taOAuthTokenClient.getDateMiseAJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////					Duration oneHours2 = Duration.of(expireIn, ChronoUnit.MILLIS);
////					ldDateExpiration = ldDateExpiration.plus(oneHours2.getSeconds(), TemporalUnit.);
////					
////					LocalDate ldMaintenant = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////					
////					Date dateDateExpiration = Date.from(ldDateExpiration.atStartOfDay(ZoneId.systemDefault()).toInstant());
////					Date dateMaintenant = Date.from(ldMaintenant.atStartOfDay(ZoneId.systemDefault()).toInstant());
//					
//					Date dateDateExpiration = new Date(timeout.getTimeInMillis());
//					Date dateMaintenant = new Date();
//					
//					System.out.println("StripeBean.validToken() : Token créé ou mis à jour le : "+taOAuthTokenClient.getDateMiseAJour());
//					System.out.println("StripeBean.validToken() : Delais expiration : "+taOAuthTokenClient.getExpiresIn());
//					System.out.println("StripeBean.validToken() : Date expiration calculée (+sécurité) : "+dateDateExpiration);
//					System.out.println("StripeBean.validToken() : Maintenant : "+dateMaintenant);
//					
//					if(dateDateExpiration.after(dateMaintenant)) {
//						//l'access token est encore valide
//						return true;
//					} else {
//						//l'access token a expiré, il faut en demandé un nouveau à l'aide du refresh token
//						return false;
//					}
//				}
//				return true;
//			} else {
//	//			System.out.println("API Microsoft désactivée pour l'instant.");
//				return false;
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	
	public void utiliseOAuthCredential() {
		testAPI();
	}
	
	public void testAPI() {
		
		
		
		

//    	GET https://graph.microsoft.com/v1.0/me 
//    	Authorization: Bearer eyJ0eXAiO ... 0X2tnSQLEANnSPHY0gKcgw
//    	Host: graph.microsoft.com

//    	init();

//    	String METHOD = "GET";
   	try {
   		
   		
   		
   		
//    		URL QUERY  = new URL("https://graph.microsoft.com/v1.0/me");
//
//    		HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
//    		req.setRequestMethod(METHOD);
//    		req.setRequestProperty( "Authorization", "Bearer "+stripeTokenSessionBean.getAccessToken());
//    		req.setRequestProperty( "Accept", "application/json;odata.metadata=minimal;odata.streaming=true;IEEE754Compatible=false;charset=utf-8");
//
//    		String inputLine;
//    		BufferedReader in;
//    		int responseCode = req.getResponseCode();
//    		if ( responseCode == 200 ) {
//    			//Récupération du résultat de l'appel
//    			in = new BufferedReader(new InputStreamReader(req.getInputStream()));
//    		} else {
//    			in = new BufferedReader(new InputStreamReader(req.getErrorStream()));
//    		}
//    		StringBuffer response   = new StringBuffer();
//
//    		while ((inputLine = in.readLine()) != null) {
//    			response.append(inputLine);
//    		}
//    		in.close();
//
//    		System.out.println(response.toString());
//    		
//    		JSONObject jb = new JSONObject(response.toString());
//    		
//    		if(!jb.isNull("displayName")) {
//	    		System.out.println("displayName : "+jb.getString("displayName"));
//	    		System.out.println("surname : "+jb.getString("surname"));
//	    		System.out.println("userPrincipalName : "+jb.getString("userPrincipalName"));
//	    		
//	    		displayName = jb.getString("displayName");
//	    		surname = jb.getString("surname");
//	    		userPrincipalName = jb.getString("userPrincipalName");
//    		}

    	} catch (Exception e) {
    		final String errmsg = "Exception: " + e;
    		e.printStackTrace();
    	}
    }
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUserPrincipalName() {
		return userPrincipalName;
	}

	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}

	public StripeTokenSessionBean getStripeTokenSessionBean() {
		return stripeTokenSessionBean;
	}

	public void setStripeTokenSessionBean(StripeTokenSessionBean msTokenSessionBean) {
		this.stripeTokenSessionBean = msTokenSessionBean;
	}

	public List<TaOAuthScopeClient> getListeScopeStripe() {
		return listeScopeStripe;
	}

	public void setListeScopeStripe(List<TaOAuthScopeClient> listeScopeMicrosoft) {
		this.listeScopeStripe = listeScopeMicrosoft;
	}

	public List<TaOAuthScopeClient> getListeScopeStripeSelected() {
		return listeScopeStripeSelected;
	}

	public void setListeScopeStripeSelected(List<TaOAuthScopeClient> listeScopeMicrosoftSelected) {
		this.listeScopeStripeSelected = listeScopeMicrosoftSelected;
	}


	public Boolean getCompteVerifieParStripe() {
		return compteVerifieParStripe;
	}


	public void setCompteVerifieParStripe(Boolean compteVerifieParStripe) {
		this.compteVerifieParStripe = compteVerifieParStripe;
	}


	public Boolean getCompteUtilisableParStripe() {
		return compteUtilisableParStripe;
	}


	public void setCompteUtilisableParStripe(Boolean compteUtilisableParStripe) {
		this.compteUtilisableParStripe = compteUtilisableParStripe;
	}


	public boolean isValidToken() {
		return validToken;
	}


	public void setValidToken(boolean validToken) {
		this.validToken = validToken;
	}
}  
              