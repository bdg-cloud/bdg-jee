package fr.legrain.bdg.webapp.oauth.google;

import java.io.IOException;
import java.io.PrintWriter;

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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.paiement.service.LgrEnvironnementServeur;

@WebServlet("/callback-google/*")
public class CallbackGoogleServlet extends HttpServlet {

	private static final long serialVersionUID = -9075718678533434258L;

	@EJB private ITaOAuthTokenClientServiceRemote taOAuthTokenClientService;
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;

	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
	private @Resource UserTransaction tx;

	private @Inject GoogleOAuthUtil googleOAuthUtil;

	private Credential credents = null;

	private String gUserEmail = null;
	private String gUserName = null;
	private String gUserImageUrl = null;
	private String gToken = null;

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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		//    	String user = sessionInfo.getUtilisateur().getUsername();
		//    	String state = request.getQueryString().split("&")[1].split("=")[1];
		System.out.println("Code : "+code);
		System.out.println("State : "+state);
		//    	state = tenantInfo.getTenantId()+"*"+sessionInfo.getUtilisateur().getId();
		state = LibCrypto.decrypt(state);
		String tenant = state.split("##")[0];
		String userId = state.split("##")[1];
		System.out.println("tenant : "+tenant);
		System.out.println("userId : "+userId);	


		try {

			try {
				tx.begin();
				
				initTenantRegistry();
				initTenant(tenant);

				usermail = taUtilisateurService.findById(LibConversion.stringToInteger(userId)).getUsername();

				initOAuth();

				try {
					TokenResponse responseT = googleOAuthUtil.getFlow().newTokenRequest(code).setRedirectUri(googleOAuthUtil.getREDIRECT_URI()).execute();

					// Storing the credentials for later access
					//credents = flow.createAndStoreCredential(response, id);
					credents = googleOAuthUtil.getFlow().createAndStoreCredential(responseT, usermail);

					response.setContentType("text/html");
					PrintWriter out = response.getWriter();

					out.println("<html>");
					out.println("<head>");
					out.println("<title>OK BDG Cloud *** Google</title>");
					out.println("</head>");
					out.println("<body bgcolor=\"white\">");
					out.println("<script>");
					//        		         out.println("opener.fin();" 
					out.println("window.close();");
					out.println("</script>");
					out.println("</body>");
					out.println("</html>");

				} finally {
					// Releasing resources
				}

				tx.commit();

			} catch (Exception e) {
			}
			
			utiliseOAuthCredential();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void initOAuth() {
		googleOAuthUtil.initOAuth(usermail, taOAuthTokenClientService, null,lgrEnvironnementServeur.isModeTestOAuth());
	}

	public void utiliseOAuthCredential() {
		testOAuth2API();
		//		testCalendarAPI();
		//		testDriveAPI();
	}

	public void testOAuth2API() {
		try {
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			//				 Setting up the Oauth2 service client
			Oauth2 userInfoService = new com.google.api.services.oauth2.Oauth2.Builder(
					googleOAuthUtil.getHTTP_TRANSPORT(), 
					googleOAuthUtil.getJSON_FACTORY(), 
					credents)
					.setApplicationName(googleOAuthUtil.getAPPLICATION_NAME())
					.build();
			Userinfoplus userInfo = null;
			com.google.api.services.oauth2.Oauth2.Tokeninfo tok =  userInfoService.tokeninfo();
			try {
				userInfo = userInfoService.userinfo().get().execute();
				//			    } catch (IOException e) {
				//			        System.out.println("An error occurred: " + e);
			} catch (GoogleJsonResponseException e) {
				System.out.println("An error occurred: " + e);
				if((int)e.getDetails().get("code") == 401) {
					System.out.println("GoogleBean.utiliseOAuthCredential()");
				}
			}
			if (userInfo != null && userInfo.getId() != null) {
				gUserEmail = userInfo.getEmail();
				gUserName = userInfo.getName();
				gUserImageUrl = userInfo.getPicture();
				gToken = credents.getAccessToken();
				System.out.println("Email: " + gUserEmail);
				System.out.println("Name: " + gUserName);
				System.out.println("Image: " + gUserImageUrl);
				System.out.println("Token: " + gToken);
			}

		} catch (Throwable t) {
			t.printStackTrace();
			//				System.exit(1);
		}
	}


}