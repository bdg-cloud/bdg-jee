package fr.legrain.bdg.webapp.oauth.microsoft;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import fr.legrain.droits.model.TaOAuthScopeClient;

/*
 * https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow#request-an-access-token
 */
public class MSOAuthUtil implements Serializable{

	private static final long serialVersionUID = 6035593608201331555L;
	private static /*final*/ String applicationID = null;
	private String endPoint = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?";
	private String applicationMdp = null;
	
	private String scope = null;
	private List<String> listeScope = null;
	private String authorizeUrl = null;
	private String redirectUri = null;
	
	private String accessToken = null;
	private String refreshToken = null;
	private long expiresIn;
	
	public void initOAuth(List<TaOAuthScopeClient> listeScopeMicrosoftSelected, boolean modeTest, String applicationID, String applicationMdp) {
		MSOAuthUtil.applicationID = applicationID;
		this.applicationMdp = applicationMdp;
		initOAuth(listeScopeMicrosoftSelected,modeTest);
	}
	
	public void initOAuth(List<TaOAuthScopeClient> listeScopeMicrosoftSelected, boolean modeTest) {
		if(listeScopeMicrosoftSelected!=null) {
			listeScope = new ArrayList<>();
			listeScope.add("offline_access");
			for (TaOAuthScopeClient taOAuthScopeClient : listeScopeMicrosoftSelected) {
				if(taOAuthScopeClient.getCode().equals(TaOAuthScopeClient.MS_PROFIL)) {
					listeScope.add("user.read");
				} else if(taOAuthScopeClient.getCode().equals(TaOAuthScopeClient.MS_CALENDAR)) {
					listeScope.add("Calendars.Read");
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
			scope = "offline_access user.read Calendars.Read";
		}
		if(modeTest) {
			redirectUri = "https://dev.oauth.promethee.biz:8443/callback-ms";
		} else {
			redirectUri = "https://oauth.bdg.cloud/callback-ms";
		}
		
	}
	
public void getAccessToken(String authorisationCode, boolean modeTest) {
    	
//    	POST /common/oauth2/v2.0/token HTTP/1.1
//    	Host: https://login.microsoftonline.com
//    	Content-Type: application/x-www-form-urlencoded
//
//    	client_id=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
//    	&scope=user.read%20mail.read
//    	&code=OAAABAAAAiL9Kn2Z27UubvWFPbm0gLWQJVzCTE9UkP3pSx1aXxUjq3n8b2JRLk4OxVXr...
//    	&redirect_uri=http%3A%2F%2Flocalhost%2Fmyapp%2F
//    	&grant_type=authorization_code
//    	&client_secret=xxxxxxxxx  // NOTE: Only required for web apps
    	
	initOAuth(null,modeTest);

        String METHOD = "POST";
        try {
            URL QUERY  = new URL("https://login.microsoftonline.com/common/oauth2/v2.0/token");

            String urlParameters = "client_id="+getApplicationid()
            		+"&scope="+getScope()
    				+"&code="+authorisationCode
    				+"&redirect_uri="+getRedirectUri()
    				+"&grant_type=authorization_code"
    				+"&client_secret="+getApplicationMdp()
    				;
            
            HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
            req.setRequestMethod(METHOD);
            req.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
//            req.setRequestProperty("Content-Type", "application/json");
//            req.setRequestProperty("Authorization", "Bearer "+MJ_BEARER_TOKEN);
            
            req.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(req.getOutputStream());
    		wr.writeBytes(urlParameters);
    		wr.flush();
    		wr.close();

//            if(!BODY.isEmpty()) {
//                req.setDoOutput(true);
//                DataOutputStream wr = new DataOutputStream(req.getOutputStream());
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
////                wr.writeBytes(BODY);
////                wr.flush();
//                writer.write(BODY);
//                writer.close();
//                wr.close();
//            }

            String inputLine;
            BufferedReader in;
            int responseCode = req.getResponseCode();
            if ( responseCode == 200 ) {
                //Récupération du résultat de l'appel
                in = new BufferedReader(new InputStreamReader(req.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(req.getErrorStream()));
            }
            StringBuffer response   = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


			JSONObject jb = new JSONObject(response.toString());
			System.out.println("token_type : "+jb.getString("token_type"));
			System.out.println("scope : "+jb.getString("scope"));
			System.out.println("expires_in : "+jb.getInt("expires_in"));
			System.out.println("ext_expires_in : "+jb.getInt("ext_expires_in"));
			System.out.println("access_token : "+jb.getString("access_token"));
			System.out.println("refresh_token : "+jb.getString("refresh_token")); //présent uniquement si on utilise le scope "offline_access"
			
			String accessToken = jb.getString("access_token");
			String refreshToken = jb.getString("refresh_token");
			long expiresIn = jb.getInt("expires_in");
			
			

			 System.out.println(response.toString());
			 
			 setAccessToken(accessToken);
				setRefreshToken(refreshToken);
				setExpiresIn(expiresIn);
			 
			 //testAPI();
			 getNewRefreshToken(refreshToken,modeTest);
			 
			
			
//			msTokenSessionBean.ser("aaa");

        } catch (Exception e) {
            final String errmsg = "Exception: " + e;
            e.printStackTrace();
        }
    }
    
    public void getNewRefreshToken(String refreshToken,boolean modeTest) {
//    	POST /common/oauth2/v2.0/token HTTP/1.1
//    	Host: https://login.microsoftonline.com
//    	Content-Type: application/x-www-form-urlencoded
//
//    	client_id=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
//    	&scope=user.read%20mail.read
//    	&refresh_token=OAAABAAAAiL9Kn2Z27UubvWFPbm0gLWQJVzCTE9UkP3pSx1aXxUjq...
//    	&redirect_uri=http%3A%2F%2Flocalhost%2Fmyapp%2F
//    	&grant_type=refresh_token
//    	&client_secret=xxxxxxxxxxxxxxxxxxxxx      // NOTE: Only required for web a
    	
    	initOAuth(null,modeTest);

        String METHOD = "POST";
        try {
            URL QUERY  = new URL("https://login.microsoftonline.com/common/oauth2/v2.0/token");

            String urlParameters = "client_id="+getApplicationid()
            		+"&scope="+getScope()
    				//+"&code="+authorisationCode
    				+"&refresh_token="+refreshToken
    				+"&redirect_uri="+getRedirectUri()
    				+"&grant_type=refresh_token"
    				+"&client_secret="+getApplicationMdp()
    				;
            
            HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
            req.setRequestMethod(METHOD);
            req.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
//            req.setRequestProperty("Content-Type", "application/json");
//            req.setRequestProperty("Authorization", "Bearer "+MJ_BEARER_TOKEN);
            
            req.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(req.getOutputStream());
    		wr.writeBytes(urlParameters);
    		wr.flush();
    		wr.close();

//            if(!BODY.isEmpty()) {
//                req.setDoOutput(true);
//                DataOutputStream wr = new DataOutputStream(req.getOutputStream());
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
////                wr.writeBytes(BODY);
////                wr.flush();
//                writer.write(BODY);
//                writer.close();
//                wr.close();
//            }

            String inputLine;
            BufferedReader in;
            int responseCode = req.getResponseCode();
            if ( responseCode == 200 ) {
                //Récupération du résultat de l'appel
                in = new BufferedReader(new InputStreamReader(req.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(req.getErrorStream()));
            }
            StringBuffer response   = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


			JSONObject jb = new JSONObject(response.toString());
			System.out.println("token_type : "+jb.getString("token_type"));
			System.out.println("scope : "+jb.getString("scope"));
			System.out.println("expires_in : "+jb.getInt("expires_in"));
//			System.out.println("ext_expires_in : "+jb.getInt("ext_expires_in"));
			System.out.println("access_token : "+jb.getString("access_token"));
			System.out.println("refresh_token : "+jb.getString("refresh_token")); 
			
			String accessToken = jb.getString("access_token");
			String newRefreshToken = jb.getString("refresh_token");
			long expiresIn = jb.getInt("expires_in");
			
			setAccessToken(accessToken);
			setRefreshToken(newRefreshToken);
			setExpiresIn(expiresIn);

			 System.out.println(response.toString());

        } catch (Exception e) {
            final String errmsg = "Exception: " + e;
            e.printStackTrace();
        }
    }
	
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getApplicationMdp() {
		return applicationMdp;
	}
	public void setApplicationMdp(String applicationMdp) {
		this.applicationMdp = applicationMdp;
	}
	public static String getApplicationid() {
		return applicationID;
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

	
}
