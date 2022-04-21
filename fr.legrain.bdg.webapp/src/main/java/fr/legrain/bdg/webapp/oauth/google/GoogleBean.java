package fr.legrain.bdg.webapp.oauth.google;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONObject;
import org.primefaces.PrimeFaces;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver; //On doit avoir le jar de jetty
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

import fr.legrain.bdg.droits.service.remote.ITaOAuthScopeClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthServiceClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.webapp.agenda.LgrTabScheduleEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.droits.model.TaOAuthScopeClient;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.data.LibDate;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;

@Named
@ViewScoped 
//@SessionScoped
public class GoogleBean implements Serializable {  
	
	private static final long serialVersionUID = 8677377809172429592L;

//	private @EJB DevService devService;
//	private @EJB GoogleOAuthClient googleOAuthClient;
	
	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
	
	private @EJB ITaOAuthTokenClientServiceRemote taAuthTokenClientService;
	private @EJB ITaOAuthServiceClientServiceRemote taAuthServiceClientService;	
	private @EJB ITaOAuthScopeClientServiceRemote taAuthScopeClientService;	
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	private List<TaOAuthScopeClient> listeScopeGoogle = null;
	private List<TaOAuthScopeClient> listeScopeGoogleSelected = null;
//	/*
//	 * https://stackoverflow.com/questions/22763934/google-oauth-fails-fetching-redirect-url
//	 */
//	private String googleAuthUrl = null;
//	private LocalServerReceiver receiver = null;
//	private GoogleAuthorizationCodeFlow flow = null;
//	private String redirectUri = null;
//	/** Global instance of the scopes required by this quickstart.
//	 * If modifying these scopes, delete your previously saved credentials
//	 * at ~/.credentials/calendar-java-quickstart
//	 */
//	private List<String> SCOPES = null;
//	
//	private JsonFactory JSON_FACTORY = null;
//	private String APPLICATION_NAME = null;
//	private	FileDataStoreFactory DATA_STORE_FACTORY = null; /** Global instance of the {@link FileDataStoreFactory}. */
//	private HttpTransport HTTP_TRANSPORT;/** Global instance of the HTTP transport. */
//	private GoogleClientSecrets clientSecrets = null;
//	
	private Credential credents = null;
	
	private @Inject GoogleOAuthUtil googleOAuthUtil;
	
	private String gUserEmail = null;
	private String gUserName = null;
	private String gUserImageUrl = null;
	private String gToken = null;
	
	
	@PostConstruct
	public void init() {
		initOAuth();
		if(validToken()) {
			utiliseOAuthCredential();
		}
	}
	
	public void initOAuth() {
		try {
			listeScopeGoogle = taAuthScopeClientService.selectAll(taAuthServiceClientService.findByCode(TaOAuthServiceClient.SERVICE_GOOGLE));
			if(listeScopeGoogleSelected==null) {
				listeScopeGoogleSelected = new ArrayList<>();
				listeScopeGoogleSelected.addAll(listeScopeGoogle);
			} else if(listeScopeGoogleSelected.isEmpty()) {
				listeScopeGoogleSelected.addAll(listeScopeGoogle);
			}
			
			credents = null;
			String usermail = sessionInfo.getUtilisateur().getUsername();
			
			gUserEmail = null;
			gUserName = null;
			gUserImageUrl = null;
			gToken = null;
			
			googleOAuthUtil.initOAuth(usermail, taAuthTokenClientService, listeScopeGoogleSelected,lgrEnvironnementServeur.isModeTestOAuth());
	        
	        
	        credents = googleOAuthUtil.getFlow().loadCredential(usermail);
	        if(credents!=null) {
	        	gToken = credents.getAccessToken();
	        }
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public boolean showDialog() {
		if ((credents == null) || !validToken()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void revokeToken(ActionEvent evt) {
		revokeToken();
		listeScopeGoogleSelected.addAll(listeScopeGoogle);
	}
	
	public void revokeToken() {
    	String METHOD = "GET";
    	try {
    		URL QUERY  = new URL("https://accounts.google.com/o/oauth2/revoke?token="+gToken);

    		HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
    		req.setRequestMethod(METHOD);
    		req.setRequestProperty( "Authorization", "Bearer "+gToken);
    		req.setRequestProperty( "Accept", "application/json");

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

    		String usermail = sessionInfo.getUtilisateur().getUsername();
    		googleOAuthUtil.getDATA_STORE_FACTORY().getDataStore(usermail).clear();
    		googleOAuthUtil.getFlow().getCredentialDataStore().delete(usermail);

    		JSONObject jb = new JSONObject(response.toString());
//    		System.out.println("displayName : "+jb.getString("displayName"));

    		System.out.println(response.toString());
    		initOAuth();

    	} catch (Exception e) {
    		final String errmsg = "Exception: " + e;
    		e.printStackTrace();
    	}
	}
	
	public void cleanDataStore() {
		
	}
	
	public boolean validToken() {
//		//https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=ya29.GlsqBnlkMUJy4FBY9RMC7fq0sFwM7KeAwz3PkAju9NM4PLrb3eoN9KVBJF-uRih1xV06UbuaUaz7DCQwbrk3qYbzzT5xgAwskjRd3WKG8BHv8nCm3h45E9n533KJ
//		
//		boolean tokenValide = true;
//		Oauth2 userInfoService = new com.google.api.services.oauth2.Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credents).setApplicationName(APPLICATION_NAME)
//         .build();
//		Userinfoplus userInfo = null;
//		 try {
//		com.google.api.services.oauth2.Oauth2.Tokeninfo tok =  userInfoService.tokeninfo();
//	   
//	        userInfo = userInfoService.userinfo().get().execute();
//
//	    } catch (GoogleJsonResponseException e) {
//	    	System.out.println("An error occurred: " + e);
//	    	if((int)e.getDetails().get("code") == 401) {
//	    		System.out.println("GoogleBean.utiliseOAuthCredential()");
//	    		tokenValide = false;
//	    	}
//	    } catch (IOException e) {
//	    	System.out.println("An error occurred: " + e);
//	    }
//		return tokenValide;
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		boolean retour = false;
		String METHOD = "GET";
    	try {
    		URL QUERY  = new URL("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token="+gToken);

    		HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
    		req.setRequestMethod(METHOD);
    		req.setRequestProperty( "Authorization", "Bearer "+gToken);
    		req.setRequestProperty( "Accept", "application/json");

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
    			retour = true;
    			in = new BufferedReader(new InputStreamReader(req.getInputStream()));
    		} else {
    			retour = false;
    			in = new BufferedReader(new InputStreamReader(req.getErrorStream()));
    		}
    		StringBuffer response   = new StringBuffer();

    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();


    		JSONObject jb = new JSONObject(response.toString());
    		if(!jb.isNull("error")) {
    			if(jb.getString("error").equals("invalid_token"));
    			System.out.println("Token google invaldie/expiré ...");
    		}


    		System.out.println(response.toString());

    		return retour;
    	} catch (Exception e) {
    		final String errmsg = "Exception: " + e;
    		e.printStackTrace();
    	}
		return retour;
	}
	
	public void connectOAuth(ActionEvent event) {
		try {
			initOAuth();
			if (showDialog() ) {
				try {
					// Creating a new authorization URL
					AuthorizationCodeRequestUrl authorizationUrl = googleOAuthUtil.getFlow().newAuthorizationUrl();

					// Setting the redirect URI
					authorizationUrl.setRedirectUri(googleOAuthUtil.getREDIRECT_URI());	
					
					String state = tenantInfo.getTenantId()+"##"+sessionInfo.getUtilisateur().getId();
					state = LibCrypto.encrypt(state);
					authorizationUrl.setState(state);

					// Building the authorization URL
					//This url will be fetched right after, as a button callback (target:_blank)
					//by using : FacesContext.getCurrentInstance().getExternalContext().redirect(googleAuthUrl);
					googleOAuthUtil.setGoogleAuthUrl(authorizationUrl.build());

					// Logging a short message
					System.out.println("Creating the authorization URL : " + googleOAuthUtil.getGoogleAuthUrl());

					PrimeFaces current = PrimeFaces.current();
					//        		         current.executeScript("oauthWindowHandle = window.open('"+url+"','mywindow', 'resizable=no,toolbar=no,scrollbars=yes,height=450,width=530,top=145,left=235');");
					current.executeScript("oauthWindowGoogle('"+googleOAuthUtil.getGoogleAuthUrl()+"');");

				} finally {
					// Releasing resources
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	
	public void updateView(ActionEvent event) {
		initOAuth();
		if(validToken()) {
			utiliseOAuthCredential();
		}
	}
	
	public void utiliseOAuthCredential() {
		testOAuth2API();
//		testCalendarAPI(null,null,null);
		testDriveAPI();
		
//		java.io.File f = new java.io.File("/home/nicolas/Téléchargements/GS1_General_Specifications.pdf");
//		if(f.exists()) {
//			uploadFile(f,f.getName(),"application/pdf");
//		}
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
	
	public void testGmailAPI() {
		
	}
	
	public void testSheetsAPI() {
		
	}
	
	public void testDocsAPI() {
		//API en beta test pour les dev, il faut demander les accès à Google
	}
	
	public void testDriveAPI() {
		try {
		    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//			 Setting up the Drive service client
			Drive service = new Drive.Builder(googleOAuthUtil.getHTTP_TRANSPORT(), 
					googleOAuthUtil.getJSON_FACTORY(), 
					credents)
					.setApplicationName(googleOAuthUtil.getAPPLICATION_NAME())
					.build();

	        // Print the names and IDs for up to 10 files.
	        FileList result = service.files().list()
	                .setPageSize(10)
	                .setFields("nextPageToken, files(id, name)")
	                .execute();
	        List<com.google.api.services.drive.model.File> files = result.getFiles();
	        if (files == null || files.isEmpty()) {
	            System.out.println("No files found.");
	        } else {
	            System.out.println("Files:");
	            for (com.google.api.services.drive.model.File file : files) {
	                System.out.printf("%s (%s)\n", file.getName(), file.getId());
	            }
	        }


		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void uploadFile(java.io.File fichier, String nomFichier, String fileTypeMime) {
		try {
			Drive service = new Drive.Builder(googleOAuthUtil.getHTTP_TRANSPORT(), 
					googleOAuthUtil.getJSON_FACTORY(), 
					credents)
					.setApplicationName(googleOAuthUtil.getAPPLICATION_NAME())
					.build();
			
			File fileMetadata = new File();
			fileMetadata.setName(nomFichier);
			java.io.File filePath = fichier;
			FileContent mediaContent = new FileContent(fileTypeMime, filePath);
			File file = service.files().create(fileMetadata, mediaContent)
			    .setFields("id")
			    .execute();
			System.out.println("File ID: " + file.getId());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Event> testCalendarAPI(Date debut, Date fin, List<TaAgenda> listeAgenda) {
		try {
			 // Setting up the calendar service client
			 Calendar client = new com.google.api.services.calendar.Calendar.Builder(
					googleOAuthUtil.getHTTP_TRANSPORT(), 
					googleOAuthUtil.getJSON_FACTORY(), 
					credents)
					.setApplicationName(googleOAuthUtil.getAPPLICATION_NAME())
					.build();
			 
			  com.google.api.services.calendar.Calendar.Events.List l = client.events().list("primary");
			  
		        DateTime now = new DateTime(System.currentTimeMillis());
		        if(debut!=null) {
		        	DateTime deb = new DateTime(debut);
		        	l.setTimeMin(deb);
		        } else {
		        	l.setTimeMin(now);
		        }
	        	if(fin!=null) {
		        	DateTime end = new DateTime(fin);
		        	l.setTimeMax(end);
		        }
		        l.setOrderBy("startTime")
	             .setSingleEvents(true);
//	            .setMaxResults(10)
		        
		        List<Event> items = null;
		        for (TaAgenda taAgenda : listeAgenda) {
		        	l.setCalendarId(taAgenda.getIdAgendaExterne());
		        	Events eventsTemp = l.execute();
		        	if(items == null) {
		        		items = eventsTemp.getItems();
		        	} else {
		        		//ajouter
		        		items.addAll(eventsTemp.getItems());
		        	}
				}
		        
//		        Events events = client.events().list("primary")
////		        		.setCalendarId("philippe82.lgr@gmail.com")
//		            .setMaxResults(10)
//		            //.setTimeMin(now)
//		            .setTimeMin(deb)
//		            .setTimeMax(end)
//		            .setOrderBy("startTime")
//		            .setSingleEvents(true)
//		            .execute();
//		        List<Event> items = events.getItems();
		        
		        if (items.size() == 0) {
		            System.out.println("No upcoming events found.");
		        } else {
		            System.out.println("Upcoming events");
		            for (Event ev : items) {
		                DateTime start = ev.getStart().getDateTime();
		                if (start == null) {
		                    start = ev.getStart().getDate();
		                }
		                System.out.printf("%s (%s)\n", ev.getSummary(), start);
		            }
		        }   

		        return items;
		        
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	public List<CalendarListEntry> testCalendarAPICalendar() {
		try {
			 // Setting up the calendar service client
			 Calendar client = new com.google.api.services.calendar.Calendar.Builder(
					googleOAuthUtil.getHTTP_TRANSPORT(), 
					googleOAuthUtil.getJSON_FACTORY(), 
					credents)
					.setApplicationName(googleOAuthUtil.getAPPLICATION_NAME())
					.build();
			 
		        DateTime now = new DateTime(System.currentTimeMillis());
		        
		        CalendarList feed = client.calendarList().list().execute();
		        List<CalendarListEntry> items = feed.getItems();
		        for (CalendarListEntry ev : items) {
	               
	                System.out.println(ev.getSummary() + " " +ev.getId());
	            }
		        
		       
		        return items;
		        
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public void createEvent(TaEvenement evt, String googleCalendarId) {
		try {
			Calendar client = new com.google.api.services.calendar.Calendar.Builder(
					googleOAuthUtil.getHTTP_TRANSPORT(), 
					googleOAuthUtil.getJSON_FACTORY(), 
					credents)
					.setApplicationName(googleOAuthUtil.getAPPLICATION_NAME())
					.build();

			Event event = new Event()
					.setSummary(evt.getLibelleEvenement())
//					.setLocation("800 Howard St., San Francisco, CA 94103")
					.setDescription(evt.getDescription());

			DateTime startDateTime = new DateTime("2018-10-28T09:00:00-07:00");
			EventDateTime start = new EventDateTime()
					.setDateTime(startDateTime)
					.setTimeZone("America/Los_Angeles");
			event.setStart(start);

			DateTime endDateTime = new DateTime("2018-10-28T17:00:00-07:00");
			EventDateTime end = new EventDateTime()
					.setDateTime(endDateTime)
					.setTimeZone("America/Los_Angeles");
			event.setEnd(end);

//			String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//			event.setRecurrence(Arrays.asList(recurrence));
//
//			EventAttendee[] attendees = new EventAttendee[] {
//					new EventAttendee().setEmail("lpage@example.com"),
//					new EventAttendee().setEmail("sbrin@example.com"),
//			};
//			event.setAttendees(Arrays.asList(attendees));
//
//			EventReminder[] reminderOverrides = new EventReminder[] {
//					new EventReminder().setMethod("email").setMinutes(24 * 60),
//					new EventReminder().setMethod("popup").setMinutes(10),
//			};
//			Event.Reminders reminders = new Event.Reminders()
//					.setUseDefault(false)
//					.setOverrides(Arrays.asList(reminderOverrides));
//			event.setReminders(reminders);

			String calendarId = "primary";
			if(googleCalendarId!=null) {
				calendarId = googleCalendarId;
			}
			event = client.events().insert(calendarId, event).execute();
			System.out.printf("Event created: %s\n", event.getHtmlLink());

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void updateEvent(LgrTabScheduleEvent evt, String googleCalendarId) {
		try {
			Calendar client = new com.google.api.services.calendar.Calendar.Builder(
					googleOAuthUtil.getHTTP_TRANSPORT(), 
					googleOAuthUtil.getJSON_FACTORY(), 
					credents)
					.setApplicationName(googleOAuthUtil.getAPPLICATION_NAME())
					.build();

			Event event = (Event)evt.getData();
			
					event.setSummary(evt.getTaEvenement().getLibelleEvenement())
//					.setLocation("800 Howard St., San Francisco, CA 94103")
					.setDescription(evt.getTaEvenement().getDescription());

			DateTime startDateTime = new DateTime(evt.getTaEvenement().getDateDebut());
			EventDateTime start = new EventDateTime()
					.setDateTime(startDateTime)
//					.setTimeZone("America/Los_Angeles")
					;
			event.setStart(start);

			DateTime endDateTime = new DateTime(evt.getTaEvenement().getDateFin());
			EventDateTime end = new EventDateTime()
					.setDateTime(endDateTime)
//					.setTimeZone("America/Los_Angeles")
					;
			event.setEnd(end);

//			String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//			event.setRecurrence(Arrays.asList(recurrence));
//
//			EventAttendee[] attendees = new EventAttendee[] {
//					new EventAttendee().setEmail("lpage@example.com"),
//					new EventAttendee().setEmail("sbrin@example.com"),
//			};
//			event.setAttendees(Arrays.asList(attendees));
//
//			EventReminder[] reminderOverrides = new EventReminder[] {
//					new EventReminder().setMethod("email").setMinutes(24 * 60),
//					new EventReminder().setMethod("popup").setMinutes(10),
//			};
//			Event.Reminders reminders = new Event.Reminders()
//					.setUseDefault(false)
//					.setOverrides(Arrays.asList(reminderOverrides));
//			event.setReminders(reminders);

			String calendarId = "primary";
			if(googleCalendarId!=null) {
				calendarId = googleCalendarId;
			}
			event = client.events().update(calendarId, event.getId(), event).execute();
			System.out.printf("Event updates: %s\n", event.getHtmlLink());

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public List<LgrTabScheduleEvent> findListeEventGoogleCal(Date debut, Date fin, List<TaAgenda> listeAgenda) {
		List<Event> items = testCalendarAPI(debut, fin, listeAgenda);
		List<LgrTabScheduleEvent> liste = new ArrayList<>();
		LgrTabScheduleEvent evt = null;
		TaEvenement e = null;
		for (Event evGoogle : items) {
			//evt = new LgrTabScheduleEvent(evGoogle.getSummary(), new Date(evGoogle.getStart().getDate().getValue()), new Date(evGoogle.getEnd().getDate().getValue()));
			evt = new LgrTabScheduleEvent(evGoogle.getSummary()
					, new Date(evGoogle.getStart().getDateTime()!=null?evGoogle.getStart().getDateTime().getValue():evGoogle.getStart().getDate().getValue())
					, new Date(evGoogle.getEnd().getDateTime()!=null?evGoogle.getEnd().getDateTime().getValue():evGoogle.getEnd().getDate().getValue()));
			evt.setCssLgrTab(LgrTab.CSS_CLASS_TAB_FABRICATION);
			//evt.setIdObjet(evGoogle.getId());
//			evt.setCodeObjet(taEvenement.getCodeEvenement());
			evt.setData(evGoogle);
			evt.setOrigine(TaAgenda.AGENDA_GOOGLE_CALENDAR);
//			evt.setTaEvenement(evGoogle);
//			if(taEvenement.getTaAgenda()!=null) {
//				evt.setStyleClass(taEvenement.getTaAgenda().getNom().replaceAll(" ", ""));
//			}
			e = new TaEvenement();
			e.setDateDebut(LibDate.localDateTimeToDate(evt.getStartDate()));
			e.setDateFin(LibDate.localDateTimeToDate(evt.getEndDate()));
			e.setLibelleEvenement(evt.getTitle());
			e.setDescription(evGoogle.getDescription());
			e.setOrigine(TaAgenda.AGENDA_GOOGLE_CALENDAR);
			evt.setTaEvenement(e);
			liste.add(evt);
		}
		return liste;
	}
	
	public List<TaAgenda> findListeGoogleCal() {
		List<CalendarListEntry> items = testCalendarAPICalendar();
		List<TaAgenda> liste = new ArrayList<>();
		TaAgenda cal = null;
		for (CalendarListEntry evGoogle : items) {
			cal = new TaAgenda();
			cal.setDescription(evGoogle.getSummary());
			cal.setNom(evGoogle.getSummary());
			cal.setIdAgendaExterne(evGoogle.getId());
			cal.setCouleur(evGoogle.getColorId());
			cal.setOrigine(TaAgenda.AGENDA_GOOGLE_CALENDAR);
			liste.add(cal);
		}
		return liste;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void connectOAuthLocalServerReceiver(ActionEvent event) {
//		googleOAuthClient.testCalendar();
		try {
			initOAuth();
        
//	        String usermail = "aaa";
//	        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//        		 .setDataStoreFactory(DATA_STORE_FACTORY)
//        		 //.setApprovalPrompt("force")
////        		 .setAccessType("offline")
//        		 .build();
	        
	        //DATA_STORE_FACTORY.getDataStore(usermail).clear();
	        //flow.getCredentialDataStore().delete(usermail);
	        
//        		 credents = flow.loadCredential(usermail);
        		 String redirect_url = null;
        		 
        		 // Checking if the given user is not authorized
        		 //if (credents == null || DATA_STORE_FACTORY.getDataStore(usermail).isEmpty()) {
        		 if (showDialog() ) {
        		     // Creating a local receiver
        		     //LocalServerReceiver receiver = new LocalServerReceiver();
        			 /*
        			 You should do this by using a LocalServerReceiver.Builder.
					According to documentation non-parameter constructor is: Constructor that starts the server on "localhost" selects an unused port.
					So you can use builder, set proper host name (remote server) and build LocalServerReceiver instance. (or you can use LocalServerReceiver(host, port) constructor)
					This should set redirect_uri to proper address.
        			  */
        			 googleOAuthUtil.setReceiver(new LocalServerReceiver.Builder().build()); //on peut changer l'url de redirection ici, elle doit juste etre déclaré chez google
        		     try {
        		         // Getting the redirect URI
        		    	 googleOAuthUtil.setRedirectUri(googleOAuthUtil.getReceiver().getRedirectUri());

        		         // Creating a new authorization URL
        		         AuthorizationCodeRequestUrl authorizationUrl = googleOAuthUtil.getFlow().newAuthorizationUrl();

        		         // Setting the redirect URI
//        		         authorizationUrl.setRedirectUri(redirectUri);
        		         authorizationUrl.setRedirectUri("https://dev.oauth.promethee.biz:8443/callback-google");	

        		         // Building the authorization URL
        		         String url = authorizationUrl.build();

        		         // Logging a short message
        		         System.out.println("Creating the authorization URL : " + url);

        		         //This url will be fetched right after, as a button callback (target:_blank)
        		         //by using : FacesContext.getCurrentInstance().getExternalContext().redirect(googleAuthUrl);
        		         googleOAuthUtil.setGoogleAuthUrl(url);
        		         
        		         //on doit renvoyer le navigateur vers googleAuthUrl pour que l'utilisateur valide l'accès
        		         
        		         PrimeFaces current = PrimeFaces.current();
//        		         current.executeScript("oauthWindowHandle = window.open('"+url+"','mywindow', 'resizable=no,toolbar=no,scrollbars=yes,height=450,width=530,top=145,left=235');");
        		         current.executeScript("oauthWindow('"+url+"');");

        		     } finally {
        		         // Releasing resources
//        		         receiver.stop();
        		     }
        		 }
 		 
		} catch (Throwable t) {
			t.printStackTrace();
//			System.exit(1);
		}
	}
	
	/**
	 * Si utilisation du serveur jetty intégré pour la l'url de redirection, sinon utilise la servlet
	 * @param event
	 */
	public void waitConnectOAuthLocalServerReceiver(ActionEvent event) {
		try {
			initOAuth();
	
	        String usermail = sessionInfo.getUtilisateur().getUsername();
//	        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//        		 .setDataStoreFactory(DATA_STORE_FACTORY)
////        		 .setAccessType("offline")
//        		 .build();

//        		 credents = flow.loadCredential(usermail);
        		 String redirect_url = null;

        		 // Checking if the given user is not authorized
        		 if (showDialog()) {
        		     // Creating a local receiver
        		     //LocalServerReceiver receiver = new LocalServerReceiver();
        			 /*
        			 You should do this by using a LocalServerReceiver.Builder.
					According to documentation non-parameter constructor is: Constructor that starts the server on "localhost" selects an unused port.
					So you can use builder, set proper host name (remote server) and build LocalServerReceiver instance. (or you can use LocalServerReceiver(host, port) constructor)
					This should set redirect_uri to proper address.
        			  */
//        			 receiver = new LocalServerReceiver.Builder().build(); //on peut changer l'url de redirection ici, elle doit juste etre déclaré chez google
        		     try {

        		         //une fois l'accès validé, google renvoi vers l'url de redirection avec le paramtre ...?code=xxxxxxxxxxxx
        		         //on peut alors récupérer le code, à ce moment la on peut utiliser l'URLRewriting pour gérer les sous domaines. cf => tuckey.org
        		         
        		         //A ce moment là, LocalServerReceiver semble lancer un serveur Jetty sur un port libre pour l'url de redirection spécifier
        		         //et attends que google revienne vers cette url avec le paramètre.

        		         // Receiving authorization code
        		         String code = googleOAuthUtil.getReceiver().waitForCode();

        		         // Exchanging it for an access token
        		         TokenResponse response = googleOAuthUtil.getFlow().newTokenRequest(code).setRedirectUri(googleOAuthUtil.getRedirectUri()).execute();

        		         // Storing the credentials for later access
        		         //credents = flow.createAndStoreCredential(response, id);
        		         credents = googleOAuthUtil.getFlow().createAndStoreCredential(response, usermail);
        		         
        		         PrimeFaces current = PrimeFaces.current();
        		         current.executeScript("oauthWindowHandle.close();");
//        		         current.executeScript("closeOauthGoogle();");


        		     } finally {
        		         // Releasing resources
        		    	 googleOAuthUtil.getReceiver().stop();
        		     }
        		 }
        		 
        			 utiliseOAuthCredential();
        			 
		} catch (Throwable t) {
			t.printStackTrace();
//			System.exit(1);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public String getgUserEmail() {
		return gUserEmail;
	}

	public void setgUserEmail(String gUserEmail) {
		this.gUserEmail = gUserEmail;
	}

	public String getgUserName() {
		return gUserName;
	}

	public void setgUserName(String gUserName) {
		this.gUserName = gUserName;
	}

	public String getgUserImageUrl() {
		return gUserImageUrl;
	}

	public void setgUserImageUrl(String gUserImageUrl) {
		this.gUserImageUrl = gUserImageUrl;
	}

	public String getgToken() {
		return gToken;
	}

	public void setgToken(String gToken) {
		this.gToken = gToken;
	}

	public List<TaOAuthScopeClient> getListeScopeGoogle() {
		return listeScopeGoogle;
	}

	public void setListeScopeGoogle(List<TaOAuthScopeClient> listeScopeGoogle) {
		this.listeScopeGoogle = listeScopeGoogle;
	}

	public List<TaOAuthScopeClient> getListeScopeGoogleSelected() {
		return listeScopeGoogleSelected;
	}

	public void setListeScopeGoogleSelected(List<TaOAuthScopeClient> listeScopeGoogleSelected) {
		this.listeScopeGoogleSelected = listeScopeGoogleSelected;
	}
	
}  
              