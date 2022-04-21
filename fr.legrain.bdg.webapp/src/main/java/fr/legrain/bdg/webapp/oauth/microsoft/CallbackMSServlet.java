package fr.legrain.bdg.webapp.oauth.microsoft;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

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

import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.paiement.service.LgrEnvironnementServeur;

@WebServlet("/callback-ms/*")
public class CallbackMSServlet extends HttpServlet {

	private static final long serialVersionUID = -9075718678533434258L;
	
	@Inject private MSTokenSessionBean msTokenSessionBean;
	
	@EJB private ITaOAuthTokenClientServiceRemote taOAuthTokenClientService;
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;

	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
	private @Inject MSBean msBean;
	private @Resource UserTransaction tx;
	
	private MSOAuthUtil mSOAuthUtil = new MSOAuthUtil();
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
		mSOAuthUtil.initOAuth(null,lgrEnvironnementServeur.isModeTestOAuth());
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String code = request.getParameter("code");
		String state = request.getParameter("state");
    	System.out.println("Code : "+code);
    	System.out.println("State : "+state);
		//    	state = tenantInfo.getTenantId()+"*"+sessionInfo.getUtilisateur().getId();
		state = LibCrypto.decrypt(state);
		String tenant = state.split("##")[0];
		String userId = state.split("##")[1];
		System.out.println("tenant : "+tenant);
		System.out.println("userId : "+userId);	
		
		try {
			tx.begin();
			
			initTenantRegistry();
			initTenant(tenant);

			usermail = taUtilisateurService.findById(LibConversion.stringToInteger(userId)).getUsername();
		
	    	authorisationCode = code;
	    	
	    	 response.setContentType("text/html");
	         PrintWriter out = response.getWriter();
	
	         out.println("<html>");
	         out.println("<head>");
	         out.println("<title>OK BDG Cloud **** Microsoft</title>");
	         out.println("</head>");
	         out.println("<body bgcolor=\"white\">");
	         out.println("<script>");
	//         out.println("opener.fin();" 
	         out.println("window.close();");
	         out.println("</script>");
	         out.println("</body>");
	         out.println("</html>");
	
	//        response.sendRedirect("https://google.fr");
	            
	         mSOAuthUtil.getAccessToken(authorisationCode,lgrEnvironnementServeur.isModeTestOAuth());
	    	//Stocker le token en base
	    	
	    	TaOAuthTokenClient msCredential = null;
	    	TaOAuthServiceClient cs = taOAuthTokenClientService.findByCodeService(TaOAuthServiceClient.SERVICE_MICROSOFT);
	    	
	    	msCredential = taOAuthTokenClientService.findByKey(usermail, cs);
	    	
	    	Date d = new Date();
	    	if(msCredential==null) {
	    		msCredential = new TaOAuthTokenClient();
	    		msCredential.setDateCreation(d);
	    	}
    		msCredential.setKey(usermail);
    		msCredential.setAccessToken(mSOAuthUtil.getAccessToken());
    		msCredential.setExpiresIn(mSOAuthUtil.getExpiresIn());
    		msCredential.setRefreshToken(mSOAuthUtil.getRefreshToken());
    		msCredential.setDateMiseAJour(d);
    		msCredential.setTaOAuthServiceClient(cs);
    		taOAuthTokenClientService.merge(msCredential);
	    	
	    	tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    
//    public void testAPI() {
//
////    	GET https://graph.microsoft.com/v1.0/me 
////    	Authorization: Bearer eyJ0eXAiO ... 0X2tnSQLEANnSPHY0gKcgw
////    	Host: graph.microsoft.com
//
//    	init();
//
//    	String METHOD = "GET";
//    	try {
//    		URL QUERY  = new URL("https://graph.microsoft.com/v1.0/me");
//
//    		HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
//    		req.setRequestMethod(METHOD);
//    		req.setRequestProperty( "Authorization", "Bearer "+msBean.getMsTokenSessionBean().getAccessToken());
//    		req.setRequestProperty( "Accept", "application/json;odata.metadata=minimal;odata.streaming=true;IEEE754Compatible=false;charset=utf-8");
//
////    		req.setDoOutput(true);
////    		DataOutputStream wr = new DataOutputStream(req.getOutputStream());
////    		wr.writeBytes("");
////    		wr.flush();
////    		wr.close();
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
//
//    		JSONObject jb = new JSONObject(response.toString());
//    		System.out.println("displayName : "+jb.getString("displayName"));
//    		System.out.println("surname : "+jb.getString("surname"));
//    		System.out.println("userPrincipalName : "+jb.getString("userPrincipalName"));
//
////    		{
////    			"@odata.context":"https://graph.microsoft.com/v1.0/$metadata#users/$entity",
////    			"displayName":"xxxxx xxxxxxx",
////    			"surname":"xxxxx",
////    			"givenName":"xxxx",
////    			"id":"0c94bb9687386d3f",
////    			"userPrincipalName":"xxxxxxx.lgr@outlook.fr",
////    			"businessPhones":[],
////    			"jobTitle":null,
////    			"mail":null,
////    			"mobilePhone":null,
////    			"officeLocation":null,
////    			"preferredLanguage":null
////    		}
//
//    		System.out.println(response.toString());
//
//
//    	} catch (Exception e) {
//    		final String errmsg = "Exception: " + e;
//    		e.printStackTrace();
//    	}
//    }

}