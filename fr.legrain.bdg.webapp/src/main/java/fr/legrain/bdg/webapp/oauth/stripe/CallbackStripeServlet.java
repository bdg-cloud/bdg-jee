package fr.legrain.bdg.webapp.oauth.stripe;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.json.JSONObject;

import com.stripe.Stripe;
import com.stripe.model.oauth.TokenResponse;
import com.stripe.net.OAuth;

import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.paiement.service.PaiementLgr;

@WebServlet("/callback-stripe-connect/*")
public class CallbackStripeServlet extends HttpServlet {

	private static final long serialVersionUID = -9075718678533434258L;
	
	@Inject private StripeTokenSessionBean msTokenSessionBean;
	
	@EJB private ITaOAuthTokenClientServiceRemote taOAuthTokenClientService;
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB private ITaStripeAccountServiceRemote taStripeAccountService;
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementStripeDossierService;

	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
//	private @Inject StripeBean stripeBean;
	private @Resource UserTransaction tx;
	
	private StripeOAuthUtil stripeOAuthUtil = new StripeOAuthUtil();
//	private String accessToken = null;
//	private String refreshToken = null;
//	private Integer expiresIn = null;
	
	private String authorisationCode = null;
	
	private String usermail = null;

	private TransactionSynchronizationRegistry reg;

	private void initTenant(String tenant) {
		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
	}

	private void initTenantRegistry() {
		try {
			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	
	public void init() {
		stripeOAuthUtil.initOAuth(null,lgrEnvironnementServeur.isModeTestOAuth());
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String code = request.getParameter("code");
		String state = request.getParameter("state");
    	System.out.println("Code : "+code);
    	System.out.println("State : "+state);
		//    	state = tenantInfo.getTenantId()+"*"+sessionInfo.getUtilisateur().getId();
    	
    	// une fois retournée par stripe la chaine crypté (qui se termine en théorie par ==) est encodé dans l'url. Les = sont remplacé par %3D.
    	// l'utilisation de URLDecoder.decode(param, "UTF-8"); ne semble pas fonctionner (s'il y a un +, il le remplace par un espace alors que dans ce cas il faudrait conserver le +)
    	// d'où le remplacement "manuel", surement à améliorer
    	state = state.replaceAll("%3D", "="); 
    	
		state = LibCrypto.decrypt(state);
		String tenant = state.split("##")[0];
		String userId = state.split("##")[1];
		System.out.println("tenant : "+tenant);
		System.out.println("userId : "+userId);	
		
		try {
			tx.begin();
			
			initTenantRegistry();
			initTenant(tenant);
			
			boolean modeTest = false;
			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();

			usermail = taUtilisateurService.findById(LibConversion.stringToInteger(userId)).getUsername();
		
	    	authorisationCode = code;
	    	
	    	 response.setContentType("text/html");
	         PrintWriter out = response.getWriter();
	
	         out.println("<html>");
	         out.println("<head>");
	         out.println("<title>OK BDG Cloud **** Stripe Connect</title>");
	         out.println("</head>");
	         out.println("<body bgcolor=\"white\">");
	         out.println("<script>");
	//         out.println("opener.fin();" 
	         out.println("window.close();");
	         out.println("</script>");
	         out.println("</body>");
	         out.println("</html>");
	
	//        response.sendRedirect("https://google.fr");
	         
	         if(modeTest) {
	        	 //utilisé la clé test du programme ou de legrain82
	         } else {
	        	 //utilisé la clé de prod du programme ou de legrain82
	         }
//	         Stripe.apiKey = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh";
//
//	         Map<String, Object> params = new HashMap<>();
//	         params.put("grant_type", "authorization_code");
//	         params.put("code", authorisationCode);
//
//	         TokenResponse rep = OAuth.token(params, null);
	         
	         JSONObject o = new JSONObject(paiementStripeDossierService.autorizationTokenOAuthAccount(authorisationCode));

	         // Access the connected account ID in the response
//	         String accountId = rep.getStripeUserId();
	         String accountId = o.get("stripe_user_id").toString();
	         
	         if(accountId!=null) {
	        	 TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
	        	 taStripeAccount.setIdExterne(accountId);
	        	 taStripeAccount = taStripeAccountService.merge(taStripeAccount);
	        	 
	        	 paiementStripeDossierService.updateAccountFromStripe(null,accountId);
	         }
	         
//	         JSONObject o = new JSONObject(rep.getLastResponse().body());
//	         System.out.println(o.get("access_token"));
//	         System.out.println(o.get("refresh_token"));
//	         System.out.println(o.get("stripe_user_id"));
//	         System.out.println(o.get("scope"));
//	         System.out.println(o.get("stripe_publishable_key"));
//	         System.out.println(o.get("token_type"));
	            
	        //stripeOAuthUtil.getAccessToken(authorisationCode);
	    	//Stocker le token en base
	    	
	    	TaOAuthTokenClient stripeCredential = null;
	    	TaOAuthServiceClient cs = taOAuthTokenClientService.findByCodeService(TaOAuthServiceClient.SERVICE_STRIPE);
	    	
	    	stripeCredential = taOAuthTokenClientService.findByKey(usermail, cs);
	    	
	    	Date d = new Date();
	    	if(stripeCredential==null) {
	    		stripeCredential = new TaOAuthTokenClient();
	    		stripeCredential.setDateCreation(d);
	    	}
    		stripeCredential.setKey(usermail);
    		stripeCredential.setAccessToken(o.get("access_token").toString());
//    		stripeCredential.setExpiresIn(stripeOAuthUtil.getExpiresIn());
    		stripeCredential.setRefreshToken(o.get("refresh_token").toString());
    		stripeCredential.setDateMiseAJour(d);
    		stripeCredential.setTaOAuthServiceClient(cs);
    		taOAuthTokenClientService.merge(stripeCredential);
	    	
	    	tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}