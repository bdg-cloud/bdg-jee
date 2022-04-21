package fr.legrain.bdg.webapp.oauth.microsoft;

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
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

import fr.legrain.bdg.droits.service.remote.ITaOAuthScopeClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthServiceClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.webapp.agenda.LgrTabScheduleEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.droits.model.TaOAuthScopeClient;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.data.LibDate;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;

@Named
@ViewScoped 
//@SessionScoped
public class MSBean implements Serializable {  
	/*
	 * https://developer.microsoft.com/en-us/graph/docs/concepts/auth_v2_user
	 */
	private static final long serialVersionUID = 8677377809172429592L;

//	private @EJB DevService devService;
////	private @EJB GoogleOAuthClient googleOAuthClient;
	
	private String displayName;
	private String surname;
	private String userPrincipalName;
//	
	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	private MSOAuthUtil mSOAuthUtil = new MSOAuthUtil();
	
	@Inject private MSTokenSessionBean msTokenSessionBean;
	
	private @EJB ITaOAuthTokenClientServiceRemote taAuthTokenClientService;
	private @EJB ITaOAuthServiceClientServiceRemote taAuthServiceClientService;
	private @EJB ITaOAuthScopeClientServiceRemote taAuthScopeClientService;	
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	private TaOAuthTokenClient taOAuthTokenClient;
	
	private List<TaOAuthScopeClient> listeScopeMicrosoft = null;
	private List<TaOAuthScopeClient> listeScopeMicrosoftSelected = null;
	
	
	@PostConstruct
	public void init() {
		initOAuth();
		if(validToken()) {
			utiliseOAuthCredential();
		}
	}
	
	public void initOAuth() {
		try {
			taParametreService.initParamBdg();
			
			listeScopeMicrosoft = taAuthScopeClientService.selectAll(taAuthServiceClientService.findByCode(TaOAuthServiceClient.SERVICE_MICROSOFT));
			if(listeScopeMicrosoftSelected==null) {
				listeScopeMicrosoftSelected = new ArrayList<>();
				listeScopeMicrosoftSelected.addAll(listeScopeMicrosoft);
			} else if(listeScopeMicrosoftSelected.isEmpty()) {
				listeScopeMicrosoftSelected.addAll(listeScopeMicrosoft);
			}
			
//			credents = null;
			String usermail = sessionInfo.getUtilisateur().getUsername();
			
			displayName = null;
			surname = null;
			userPrincipalName = null;
			
			mSOAuthUtil.initOAuth(null/*usermail/*, taAuthTokenClientService*/,lgrEnvironnementServeur.isModeTestOAuth(),
					paramBdg.getTaParametre().getMicrosoftApiApplicationId(),
					paramBdg.getTaParametre().getMicrosoftApiApplicationPassword());
			
			taOAuthTokenClient = taAuthTokenClientService.findByKey(usermail, taAuthServiceClientService.findByCode(TaOAuthServiceClient.SERVICE_MICROSOFT));
	        
//	        credents = googleOAuthUtil.getFlow().loadCredential(usermail);
//	        if(credents!=null) {
//	        	gToken = credents.getAccessToken();
//	        }
			if(taOAuthTokenClient!=null) {
				msTokenSessionBean.setAccessToken(taOAuthTokenClient.getAccessToken());
				msTokenSessionBean.setRefreshToken(taOAuthTokenClient.getRefreshToken());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void connectOAuth(ActionEvent event) {
		init();
		String url = buildUrl();
		PrimeFaces current = PrimeFaces.current();
//        current.executeScript("xx = window.open('"+url+"','mywindow', 'resizable=no,toolbar=no,scrollbars=yes,height=450,width=530,top=145,left=235');");
        current.executeScript("oauthWindowMicrosoft('"+url+"');");
	}
	
	public String buildUrl() {
		// Line breaks for legibility only
		try {
			
			String state = tenantInfo.getTenantId()+"##"+sessionInfo.getUtilisateur().getId();
			state = LibCrypto.encrypt(state);
			
			mSOAuthUtil.setAuthorizeUrl(
				mSOAuthUtil.getEndPoint()
				+"client_id="+mSOAuthUtil.getApplicationid()
				+"&response_type=code"
				+"&redirect_uri="+URLEncoder.encode(mSOAuthUtil.getRedirectUri(),"UTF-8")
				+"&response_mode=query"
				+"&scope="+URLEncoder.encode(mSOAuthUtil.getScope(),"UTF-8")
				+"&state="+state
				);
		
//		https://login.microsoftonline.com/common/oauth2/v2.0/authorize?
//		client_id=6731de76-14a6-49ae-97bc-6eba6914391e
//		&response_type=code
//		&redirect_uri=http%3A%2F%2Flocalhost%2Fmyapp%2F
//		&response_mode=query
//		&scope=offline_access%20user.read%20mail.read
//		&state=12345
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mSOAuthUtil.getAuthorizeUrl();
	}
	
	public void updateView(ActionEvent event) {
		init();
		msTokenSessionBean.de("aaa");
		testAPI();
//		findCalendar();
//		findEvents(null,null,null);
		//TODO récuperer le l'access_token de la servlet et faire les appel de l'api pour récupérer les champs à afficher.
		//cf methode testAPI() de la servlet
		//stocket les token dans un repo sécurisé
		//
	}
	
	public boolean showDialog() {
		if (!validToken()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean validToken() {
		try {
			if(msTokenSessionBean!=null && msTokenSessionBean.getAccessToken()!=null) {
				taOAuthTokenClient = taAuthTokenClientService.findByKey(sessionInfo.getUtilisateur().getUsername(), taAuthServiceClientService.findByCode(TaOAuthServiceClient.SERVICE_MICROSOFT));
				if(taOAuthTokenClient!=null) {
					
					

					long expireIn = taOAuthTokenClient.getExpiresIn()*1000;//durée Microsoft en seconde vers milli seconde
					long margeSecuriteAppelToken = 5000; //+5s pour couvrir le temps de l'appel suivant
					expireIn += margeSecuriteAppelToken;
					
					Calendar timeout = Calendar.getInstance();
					timeout.setTimeInMillis(taOAuthTokenClient.getDateMiseAJour().getTime() + expireIn);
					
//					LocalDate ldDateExpiration = taOAuthTokenClient.getDateMiseAJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//					Duration oneHours2 = Duration.of(expireIn, ChronoUnit.MILLIS);
//					ldDateExpiration = ldDateExpiration.plus(oneHours2.getSeconds(), TemporalUnit.);
//					
//					LocalDate ldMaintenant = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//					
//					Date dateDateExpiration = Date.from(ldDateExpiration.atStartOfDay(ZoneId.systemDefault()).toInstant());
//					Date dateMaintenant = Date.from(ldMaintenant.atStartOfDay(ZoneId.systemDefault()).toInstant());
					
					Date dateDateExpiration = new Date(timeout.getTimeInMillis());
					Date dateMaintenant = new Date();
					
					System.out.println("MSBean.validToken() : Token créé ou mis à jour le : "+taOAuthTokenClient.getDateMiseAJour());
					System.out.println("MSBean.validToken() : Delais expiration : "+taOAuthTokenClient.getExpiresIn());
					System.out.println("MSBean.validToken() : Date expiration calculée (+sécurité) : "+dateDateExpiration);
					System.out.println("MSBean.validToken() : Maintenant : "+dateMaintenant);
					
					if(dateDateExpiration.after(dateMaintenant)) {
						//l'access token est encore valide
						return true;
					} else {
						//l'access token a expiré, il faut en demandé un nouveau à l'aide du refresh token
						return false;
					}
				}
				return true;
			} else {
	//			System.out.println("API Microsoft désactivée pour l'instant.");
				return false;
			}
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

    	String METHOD = "GET";
    	try {
    		URL QUERY  = new URL("https://graph.microsoft.com/v1.0/me");

    		HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
    		req.setRequestMethod(METHOD);
    		req.setRequestProperty( "Authorization", "Bearer "+msTokenSessionBean.getAccessToken());
    		req.setRequestProperty( "Accept", "application/json;odata.metadata=minimal;odata.streaming=true;IEEE754Compatible=false;charset=utf-8");

//    		req.setDoOutput(true);
//    		DataOutputStream wr = new DataOutputStream(req.getOutputStream());
//    		wr.writeBytes("");
//    		wr.flush();
//    		wr.close();

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

    		System.out.println(response.toString());
    		
    		JSONObject jb = new JSONObject(response.toString());
    		
    		if(!jb.isNull("displayName")) {
	    		System.out.println("displayName : "+jb.getString("displayName"));
	    		System.out.println("surname : "+jb.getString("surname"));
	    		System.out.println("userPrincipalName : "+jb.getString("userPrincipalName"));
	    		
	    		displayName = jb.getString("displayName");
	    		surname = jb.getString("surname");
	    		userPrincipalName = jb.getString("userPrincipalName");
    		}

//    		{
//    			"@odata.context":"https://graph.microsoft.com/v1.0/$metadata#users/$entity",
//    			"displayName":"xxxxx xxxx",
//    			"surname":"xxxxx",
//    			"givenName":"xxxxx",
//    			"id":"xxxxxxxxxxxxxxxx",
//    			"userPrincipalName":"xxxxxxxxx.lgr@outlook.fr", 
//    			"businessPhones":[],
//    			"jobTitle":null,
//    			"mail":null,
//    			"mobilePhone":null,
//    			"officeLocation":null,
//    			"preferredLanguage":null
//    		}

    		


    	} catch (Exception e) {
    		final String errmsg = "Exception: " + e;
    		e.printStackTrace();
    	}
    }
	
	public List<MsCalendar> findCalendar() {

//    	GET https://graph.microsoft.com/v1.0/me/calendars
//    	Authorization: Bearer eyJ0oXAiO ... 0X2tnSQLEANnSPHY0gKcgi
//    	Host: graph.microsoft.com
		
		List<MsCalendar> listeCal = new LinkedList<>();

//    	init();

    	String METHOD = "GET";
    	try {
    		URL QUERY  = new URL("https://graph.microsoft.com/v1.0/me/calendars");

    		HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
    		req.setRequestMethod(METHOD);
    		req.setRequestProperty( "Authorization", "Bearer "+msTokenSessionBean.getAccessToken());
    		req.setRequestProperty( "Accept", "application/json;odata.metadata=minimal;odata.streaming=true;IEEE754Compatible=false;charset=utf-8");

//    		req.setDoOutput(true);
//    		DataOutputStream wr = new DataOutputStream(req.getOutputStream());
//    		wr.writeBytes("");
//    		wr.flush();
//    		wr.close();

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

    		System.out.println(response.toString());
    		
    		JSONObject jb = new JSONObject(response.toString());
    		JSONArray arr = jb.getJSONArray("value");
    		MsCalendar msCalendar = null;
    		
    		for (int i=0; i<arr.length(); i++) {
    			msCalendar = new MsCalendar();
    			JSONObject o = (JSONObject)arr.get(i);
    			System.out.println("id : "+o.getString("id"));
    			System.out.println("name : "+o.getString("name"));
        		System.out.println("color : "+o.getString("color"));
        		msCalendar.setId(o.getString("id"));
        		msCalendar.setName(o.getString("name"));
        		msCalendar.setColor(o.getString("color"));
        		listeCal.add(msCalendar);
			}
 
//    		{
//    		    "@odata.context": "https://graph.microsoft.com/beta/$metadata#me/calendars",
//    		    "value": [
//    		        {
//    		            "@odata.id": "https://graph.microsoft.com/beta/users('ddfcd489-628b-40d7-b48b-57002df800e5@1717622f-1d94-4d0c-9d74-709fad664b77')/calendars('AAMkAGI2TGuLAAA=')",
//    		            "id": "AAMkAGI2TGuLAAA=",
//    		            "name": "Calendar",
//    		            "color": "auto",
//    		            "changeKey": "nfZyf7VcrEKLNoU37KWlkQAAA0x0+w==",
//    		            "isDefaultCalendar": true,
//    		            "canShare":true,
//    		            "canViewPrivateItems":true,
//    		            "hexColor": "",
//    		            "isShared":false,
//    		            "isSharedWithMe":false,
//    		            "canEdit":true,
//    		            "owner":{
//    		                "name":"Samantha Booth",
//    		                "address":"samanthab@adatum.onmicrosoft.com"
//    		            }
//    		        }
//    		    ]
//    		}

    		
    		return listeCal;

    	} catch (Exception e) {
    		final String errmsg = "Exception: " + e;
    		e.printStackTrace();
    	}
		return listeCal;
    }
	
	public List<MsEvent> findEvents(Date debut, Date fin, List<TaAgenda> listeAgenda) {
		
		List<MsEvent> listeEvt = new LinkedList<>();

		//    	GET https://graph.microsoft.com/v1.0/me/calendars
//    	Authorization: Bearer euJ0eXAiO ... 0X2tnSQLEANnSPHY0gKogw
//    	Host: graph.microsoft.com

//    	init();

    	
			
		
    	String METHOD = "GET";
    	try {
    		
    		for (TaAgenda ag : listeAgenda) {
	//    		URL QUERY  = new URL("https://graph.microsoft.com/v1.0/me/calendars/{id}/events");
	//    		AQMkADAwATM0MDAAMS0xMzMANS0yYjcyLTAwAi0wMAoARgAAA_lgvCZMtn5KgK7LxYt3iO0HAOHPi5bavDxOhuUemAzMUf4AAAIBBgAAAOHPi5bavDxOhuUemAzMUf4AAAITRAAAAA==
	    		URL QUERY  = new URL("https://graph.microsoft.com/v1.0/me/calendars/"+ag.getIdAgendaExterne()+"/events");
	    				
	    				
	    		HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
	    		req.setRequestMethod(METHOD);
	    		req.setRequestProperty( "Authorization", "Bearer "+msTokenSessionBean.getAccessToken());
	    		req.setRequestProperty( "Accept", "application/json;odata.metadata=minimal;odata.streaming=true;IEEE754Compatible=false;charset=utf-8");
	
	//    		req.setDoOutput(true);
	//    		DataOutputStream wr = new DataOutputStream(req.getOutputStream());
	//    		wr.writeBytes("");
	//    		wr.flush();
	//    		wr.close();
	
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
	
	    		System.out.println(response.toString());
	    		
	    		JSONObject jb = new JSONObject(response.toString());
	    		JSONArray arr = jb.getJSONArray("value");
	    		MsEvent msEvent = null;
	    		
	    		for (int i=0; i<arr.length(); i++) {
	    			msEvent = new MsEvent();
	    			
	    			JSONObject o = (JSONObject)arr.get(i);
	    			System.out.println("id : "+o.getString("id"));
	    			System.out.println("subject : "+o.getString("subject"));
	    			System.out.println("bodyPreview : "+o.getString("bodyPreview"));
	    			System.out.println("isAllDay : "+o.getBoolean("isAllDay"));
	    			System.out.println("webLink : "+o.getString("webLink"));
	    			msEvent.setId(o.getString("id"));
	    			msEvent.setSubject(o.getString("subject"));
	    			msEvent.setBodyPreview(o.getString("bodyPreview"));
	    			msEvent.setIsAllDay(o.getBoolean("isAllDay"));
	    			msEvent.setWebLink(o.getString("webLink"));
	    			
	    			JSONObject body = o.getJSONObject("body");
	    			System.out.println("contentType : "+body.getString("contentType"));
	    			System.out.println("content : "+body.getString("content"));
	    			msEvent.setBodyContentType(body.getString("contentType"));
	    			msEvent.setBodyContent(body.getString("content"));
	    			
	    			JSONObject start = o.getJSONObject("start");
	    			System.out.println("dateTime : "+start.getString("dateTime"));
	    			System.out.println("timeZone : "+start.getString("timeZone"));
	    			msEvent.setStartDateTime(start.getString("dateTime"));
	    			msEvent.setStartTimeZone(start.getString("timeZone"));
	    			
	    			JSONObject end = o.getJSONObject("end");
	    			System.out.println("dateTime : "+end.getString("dateTime"));
	    			System.out.println("timeZone : "+end.getString("timeZone"));
	    			msEvent.setEndDateTime(end.getString("dateTime"));
	    			msEvent.setEndTimeZone(end.getString("timeZone"));
	
	    			listeEvt.add(msEvent);
				}
	    		
	    		
	
	//    		{
	//    			  "value": [
	//    			    {
	//    			      "originalStartTimeZone": "originalStartTimeZone-value",
	//    			      "originalEndTimeZone": "originalEndTimeZone-value",
	//    			      "responseStatus": {
	//    			        "response": "",
	//    			        "time": "2016-10-19T10:37:00Z"
	//    			      },
	//    			      "uid": "iCalUId-value",
	//    			      "reminderMinutesBeforeStart": 99,
	//    			      "isReminderOn": true
	//    			    }
	//    			  ]
	//    			}
    	}
    		
    	return listeEvt;

    	} catch (Exception e) {
    		final String errmsg = "Exception: " + e;
    		e.printStackTrace();
    	}
		return listeEvt;
    }
	
	public List<LgrTabScheduleEvent> findListeEventMSCal(Date debut, Date fin, List<TaAgenda> listeAgenda) {
		try {
			List<MsEvent> items = findEvents(debut, fin, listeAgenda);
			List<LgrTabScheduleEvent> liste = new ArrayList<>();
			LgrTabScheduleEvent evt = null;
			TaEvenement e = null;
			DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//			String string2 = "2001-07-04T12:08:56.235-07:00";
//			Date result2 = df2.parse(string2);
			for (MsEvent evMs : items) {
				//evt = new LgrTabScheduleEvent(evGoogle.getSummary(), new Date(evGoogle.getStart().getDate().getValue()), new Date(evGoogle.getEnd().getDate().getValue()));
				evt = new LgrTabScheduleEvent(evMs.getSubject()
						, df2.parse(evMs.getStartDateTime().substring(0, 20))
						, df2.parse((evMs.getEndDateTime().substring(0, 20)))
						);
				evt.setCssLgrTab(LgrTab.CSS_CLASS_TAB_FABRICATION);
				//evt.setIdObjet(evGoogle.getId());
	//			evt.setCodeObjet(taEvenement.getCodeEvenement());
				evt.setData(evMs);
				evt.setOrigine(TaAgenda.AGENDA_MICROSOFT_OUTLOOK);
	//			evt.setTaEvenement(evGoogle);
	//			if(taEvenement.getTaAgenda()!=null) {
	//				evt.setStyleClass(taEvenement.getTaAgenda().getNom().replaceAll(" ", ""));
	//			}
				e = new TaEvenement();
				e.setDateDebut(LibDate.localDateTimeToDate(evt.getStartDate()));
				e.setDateFin(LibDate.localDateTimeToDate(evt.getEndDate()));
				e.setLibelleEvenement(evt.getTitle());
				e.setDescription(evMs.getBodyPreview());
				e.setOrigine(TaAgenda.AGENDA_MICROSOFT_OUTLOOK);
				evt.setTaEvenement(e);
				liste.add(evt);
			}
			return liste;
		} catch (Exception e) {
    		e.printStackTrace();
    	}
		return null;
	}
	
	public List<TaAgenda> findListeMSCal() {
		List<MsCalendar> items = findCalendar();
		List<TaAgenda> liste = new ArrayList<>();
		TaAgenda cal = null;
		for (MsCalendar evMs : items) {
			cal = new TaAgenda();
//			cal.setDescription(evMs.getName());
			cal.setNom(evMs.getName());
			cal.setIdAgendaExterne(evMs.getId());
			cal.setCouleur(evMs.getColor());
			cal.setOrigine(TaAgenda.AGENDA_MICROSOFT_OUTLOOK);
			liste.add(cal);
		}
		return liste;
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

	public MSTokenSessionBean getMsTokenSessionBean() {
		return msTokenSessionBean;
	}

	public void setMsTokenSessionBean(MSTokenSessionBean msTokenSessionBean) {
		this.msTokenSessionBean = msTokenSessionBean;
	}

	public List<TaOAuthScopeClient> getListeScopeMicrosoft() {
		return listeScopeMicrosoft;
	}

	public void setListeScopeMicrosoft(List<TaOAuthScopeClient> listeScopeMicrosoft) {
		this.listeScopeMicrosoft = listeScopeMicrosoft;
	}

	public List<TaOAuthScopeClient> getListeScopeMicrosoftSelected() {
		return listeScopeMicrosoftSelected;
	}

	public void setListeScopeMicrosoftSelected(List<TaOAuthScopeClient> listeScopeMicrosoftSelected) {
		this.listeScopeMicrosoftSelected = listeScopeMicrosoftSelected;
	}
}  
              