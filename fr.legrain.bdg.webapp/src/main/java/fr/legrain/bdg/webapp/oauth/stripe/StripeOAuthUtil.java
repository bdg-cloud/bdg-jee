package fr.legrain.bdg.webapp.oauth.stripe;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.model.TaOAuthScopeClient;

/*
 * https://stripe.com/docs/connect/oauth-reference
 * https://stripe.com/docs/connect/oauth-standard-accounts
 */
public class StripeOAuthUtil implements Serializable{
	
	private static final String APPLICATION_ID_TEST = "ca_6ujxygNqohXiddAT4CXdKXQzhYATUlDx";
	private static final String APPLICATION_ID_PROD = "ca_6ujxfILdq0drsYzeBPijWLxkXeM4LwEh";
	private String endPoint = "https://connect.stripe.com/oauth/authorize?";
	//private String applicationMdp = "SCE994~]}hnuoblmCMGV90?";
	
	
	private static final String REDIRECT_URI_TEST = "https://dev.oauth.promethee.biz:8443/callback-stripe-connect";
	private static final String REDIRECT_URI_PROD = "https://oauth.bdg.cloud/callback-stripe-connect";
	private static final String FIN_URL_REDIRECT_URI = "/callback-stripe-connect";
	
	private String scope = null;
	private List<String> listeScope = null;
	private String authorizeUrl = null;
	private String redirectUri = null;
	private String applicationId = null;
	
	private String accessToken = null;
	private String refreshToken = null;
	private long expiresIn;
	private BdgProperties bdgProperties = new BdgProperties();
	
	public void initOAuth(List<TaOAuthScopeClient> listeScopeStripeSelected, boolean modeTest) {
		if(listeScopeStripeSelected!=null) {
			listeScope = new ArrayList<>();
			//listeScope.add("read_only");
			for (TaOAuthScopeClient taOAuthScopeClient : listeScopeStripeSelected) {
				if(taOAuthScopeClient.getCode().equals(TaOAuthScopeClient.STRIPE_READ_ONLY)) {
					listeScope.add("read_only");
				} else if(taOAuthScopeClient.getCode().equals(TaOAuthScopeClient.STRIPE_READ_WRITE)) {
					listeScope.add("read_write");
//					listeScope.add("Calendars.ReadWrite");
				}
			}
			scope = "";
			for (String s : listeScope) {
				scope += s+" ";
			}
			scope = scope.substring(0,scope.length()-1);
		} else {
			//pas de liste de scope spécifiques, on demande "tout"
			//scope = "offline_access user.read mail.read";
			//Calendars.Read, Calendars.ReadWrite
			scope = "read_only";
		}
		if(modeTest) {
			redirectUri = REDIRECT_URI_TEST;
			redirectUri = bdgProperties.urlOAuth(true)+FIN_URL_REDIRECT_URI;
			applicationId = APPLICATION_ID_TEST;
		} else {
			redirectUri = REDIRECT_URI_PROD;
			applicationId = APPLICATION_ID_PROD;
		}
	}
	
public void getAccessToken(String authorisationCode) {
//    	
////    	POST /common/oauth2/v2.0/token HTTP/1.1
////    	Host: https://login.microsoftonline.com
////    	Content-Type: application/x-www-form-urlencoded
////
////    	client_id=6731de76-14a6-49ae-97bc-6eba6914391e
////    	&scope=user.read%20mail.read
////    	&code=OAAABAAAAiL9Kn2Z27UubvWFPbm0gLWQJVzCTE9UkP3pSx1aXxUjq3n8b2JRLk4OxVXr...
////    	&redirect_uri=http%3A%2F%2Flocalhost%2Fmyapp%2F
////    	&grant_type=authorization_code
////    	&client_secret=JqQX2PNo9bpM0uEihUPzyrh    // NOTE: Only required for web apps
//    	
//	initOAuth(null);
//
//        String METHOD = "POST";
//        try {
//            URL QUERY  = new URL("https://login.microsoftonline.com/common/oauth2/v2.0/token");
//
//            String urlParameters = "client_id="+getApplicationid()
//            		+"&scope="+getScope()
//    				+"&code="+authorisationCode
//    				+"&redirect_uri="+getRedirectUri()
//    				+"&grant_type=authorization_code"
//    				//+"&client_secret="+getApplicationMdp()
//    				;
//            
//            HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
//            req.setRequestMethod(METHOD);
//            req.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
////            req.setRequestProperty("Content-Type", "application/json");
////            req.setRequestProperty("Authorization", "Bearer "+MJ_BEARER_TOKEN);
//            
//            req.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(req.getOutputStream());
//    		wr.writeBytes(urlParameters);
//    		wr.flush();
//    		wr.close();
//
////            if(!BODY.isEmpty()) {
////                req.setDoOutput(true);
////                DataOutputStream wr = new DataOutputStream(req.getOutputStream());
////                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
//////                wr.writeBytes(BODY);
//////                wr.flush();
////                writer.write(BODY);
////                writer.close();
////                wr.close();
////            }
//
//            String inputLine;
//            BufferedReader in;
//            int responseCode = req.getResponseCode();
//            if ( responseCode == 200 ) {
//                //Récupération du résultat de l'appel
//                in = new BufferedReader(new InputStreamReader(req.getInputStream()));
//            } else {
//                in = new BufferedReader(new InputStreamReader(req.getErrorStream()));
//            }
//            StringBuffer response   = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//
//			JSONObject jb = new JSONObject(response.toString());
//			System.out.println("token_type : "+jb.getString("token_type"));
//			System.out.println("scope : "+jb.getString("scope"));
//			System.out.println("expires_in : "+jb.getInt("expires_in"));
//			System.out.println("ext_expires_in : "+jb.getInt("ext_expires_in"));
//			System.out.println("access_token : "+jb.getString("access_token"));
//			System.out.println("refresh_token : "+jb.getString("refresh_token")); //présent uniquement si on utilise le scope "offline_access"
//			
//			String accessToken = jb.getString("access_token");
//			String refreshToken = jb.getString("refresh_token");
//			long expiresIn = jb.getInt("expires_in");
//			
//			
//
//			 System.out.println(response.toString());
//			 
//			 setAccessToken(accessToken);
//				setRefreshToken(refreshToken);
//				setExpiresIn(expiresIn);
//			 
//			 //testAPI();
//			 getNewRefreshToken(refreshToken);
//			 
//			
//			
////			msTokenSessionBean.ser("aaa");
//
//        } catch (Exception e) {
//            final String errmsg = "Exception: " + e;
//            e.printStackTrace();
//        }
    }
    
    public void getNewRefreshToken(String refreshToken) {
////    	POST /common/oauth2/v2.0/token HTTP/1.1
////    	Host: https://login.microsoftonline.com
////    	Content-Type: application/x-www-form-urlencoded
////
////    	client_id=6731de76-14a6-49ae-97bc-6eba6914391e
////    	&scope=user.read%20mail.read
////    	&refresh_token=OAAABAAAAiL9Kn2Z27UubvWFPbm0gLWQJVzCTE9UkP3pSx1aXxUjq...
////    	&redirect_uri=http%3A%2F%2Flocalhost%2Fmyapp%2F
////    	&grant_type=refresh_token
////    	&client_secret=JqQX2PNo9bpM0uEihUPzyrh      // NOTE: Only required for web a
//    	
//    	initOAuth(null);
//
//        String METHOD = "POST";
//        try {
//            URL QUERY  = new URL("https://login.microsoftonline.com/common/oauth2/v2.0/token");
//
//            String urlParameters = "client_id="+getApplicationid()
//            		+"&scope="+getScope()
//    				//+"&code="+authorisationCode
//    				+"&refresh_token="+refreshToken
//    				+"&redirect_uri="+getRedirectUri()
//    				+"&grant_type=refresh_token"
//    				//+"&client_secret="+getApplicationMdp()
//    				;
//            
//            HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
//            req.setRequestMethod(METHOD);
//            req.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
////            req.setRequestProperty("Content-Type", "application/json");
////            req.setRequestProperty("Authorization", "Bearer "+MJ_BEARER_TOKEN);
//            
//            req.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(req.getOutputStream());
//    		wr.writeBytes(urlParameters);
//    		wr.flush();
//    		wr.close();
//
////            if(!BODY.isEmpty()) {
////                req.setDoOutput(true);
////                DataOutputStream wr = new DataOutputStream(req.getOutputStream());
////                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
//////                wr.writeBytes(BODY);
//////                wr.flush();
////                writer.write(BODY);
////                writer.close();
////                wr.close();
////            }
//
//            String inputLine;
//            BufferedReader in;
//            int responseCode = req.getResponseCode();
//            if ( responseCode == 200 ) {
//                //Récupération du résultat de l'appel
//                in = new BufferedReader(new InputStreamReader(req.getInputStream()));
//            } else {
//                in = new BufferedReader(new InputStreamReader(req.getErrorStream()));
//            }
//            StringBuffer response   = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//
//			JSONObject jb = new JSONObject(response.toString());
//			System.out.println("token_type : "+jb.getString("token_type"));
//			System.out.println("scope : "+jb.getString("scope"));
//			System.out.println("expires_in : "+jb.getInt("expires_in"));
////			System.out.println("ext_expires_in : "+jb.getInt("ext_expires_in"));
//			System.out.println("access_token : "+jb.getString("access_token"));
//			System.out.println("refresh_token : "+jb.getString("refresh_token")); 
//			
//			String accessToken = jb.getString("access_token");
//			String newRefreshToken = jb.getString("refresh_token");
//			long expiresIn = jb.getInt("expires_in");
//			
//			setAccessToken(accessToken);
//			setRefreshToken(newRefreshToken);
//			setExpiresIn(expiresIn);
//
//			 System.out.println(response.toString());
//
//        } catch (Exception e) {
//            final String errmsg = "Exception: " + e;
//            e.printStackTrace();
//        }
    }
	
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public static String getApplicationIdTest() {
		return APPLICATION_ID_TEST;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getAuthorizeUrl() {
		return authorizeUrl;
	}
	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

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

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
}
