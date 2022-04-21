package fr.legrain.bugzilla.rest.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.legrain.bugzilla.rest.model.Bug;
import fr.legrain.bugzilla.rest.model.BugSearch;
import fr.legrain.bugzilla.rest.model.CommentCreate;
import fr.legrain.bugzilla.rest.model.Login;
import fr.legrain.bugzilla.rest.model.ResultCreateBug;
import fr.legrain.bugzilla.rest.model.ResultCreateUserID;
import fr.legrain.bugzilla.rest.model.User;
import fr.legrain.bugzilla.rest.model.UserCreate;
import fr.legrain.bugzilla.rest.model.UserSearch;

/*
 * http://www.jsonschema2pojo.org/
 */
public class BugzillaUtil {

	private String apiKey;
	private String login;		
	private String password; 	
	private String bugzillaURL; 
	private Login loginToken;

	private Client client;
	private WebTarget service;

	private String restURL;
	private String apiKeyParamString;
	private String loginParamString;

	public BugzillaUtil(String bugzillaURL,String apiKey) {
		this.apiKey = apiKey;
		this.bugzillaURL = bugzillaURL;
		
		restURL = this.bugzillaURL+"/rest";
		apiKeyParamString = "api_key="+this.apiKey;
		loginParamString = "login?login="+login+"&password="+password;

//		Configuration config = new ClientConfiguration(null);
//		Client client = ClientBuilder.newClient().register(new Authenticator(login, password));
		client = ClientBuilder.newClient();

	}
	
	public User findUser(String email) {
		service = client.target(restURL+"/user?"+apiKeyParamString+"&names="+email);
		try {
			UserSearch b = service.request(MediaType.APPLICATION_JSON).get(UserSearch.class);
			return b.getResult().getUsers().get(0);
		} catch (javax.ws.rs.NotFoundException e) {
			return null;
		}
	}
	
	public Bug findBug(int bugID) {
		service = client.target(restURL+"/bug?"+apiKeyParamString+"&id="+bugID);
		BugSearch b = service.request(MediaType.APPLICATION_JSON).get(BugSearch.class);
		return b.getResult().getBugs().get(0);
	}
	
	public List<Bug> findBugs(int[] bugIds) {
		
		String param = "/bug?"+apiKeyParamString+"&id=";
		for (int i : bugIds) {
			param+=i+",";
		}
		param = param.substring(0,param.length()-1); //supprime la dernière ","

		//service = client.target("http://bugs.legrain.fr/rest/bug?id=1,2");
		service = client.target(restURL+param);

		BugSearch b = service.request(MediaType.APPLICATION_JSON).get(BugSearch.class);

		return b.getResult().getBugs();
	}
	
	public List<Bug> findBugs(String product) {

		service = client.target(restURL+"/bug?"+apiKeyParamString+"&product="+product);

		BugSearch b = service.request(MediaType.APPLICATION_JSON).get(BugSearch.class);


		return b.getResult().getBugs();
	}
	
	public List<Bug> findBugs(String[] product) {
		
		String param = "/bug?"+apiKeyParamString+"&id=";
		for (String p : product) {
			param+="product="+p+"&";
		}
		
		//String param = "?product=Foo&product=Bar";
		service = client.target(restURL+param);

		BugSearch b = service.request(MediaType.APPLICATION_JSON).get(BugSearch.class);


		return b.getResult().getBugs();
	}
	
	public List<Bug> findBugsParam(String paramString) {
	
		service = client.target(restURL+"/bug?"+apiKeyParamString+"&"+paramString);
		BugSearch b = service.request(MediaType.APPLICATION_JSON).get(BugSearch.class);

		return b.getResult().getBugs();
	}
	
	public String query(String paramString) {
		
		service = client.target(restURL+"?"+apiKeyParamString+"&"+paramString);
		String s = service.request(MediaType.APPLICATION_JSON).get(String.class);

		return s;
	}
	
	public List<Bug> findBugsTest() {
		//
		// http://bugs.legrain.fr
		// http://www.jsonschema2pojo.org/
		// https://vaadin.com/blog/-/blogs/consuming-rest-services-from-java-applications
		// http://www.javacodegeeks.com/2012/09/simple-rest-client-in-java.html
		// https://bugzilla.readthedocs.org/en/5.0/api/core/v1/bug.html#rest-search-bugs
		//

//		String param = "?product=Foo&product=Bar";
//		query params: ?q=Turku&cnt=10&mode=json&units=metric
//		service = client.target("http://api.openweathermap.org/data/2.5/forecast/daily")
//			.queryParam("cnt", "10")
//			.queryParam("units", "metric");
//		service.queryParam("q", place) .request(MediaType.APPLICATION_JSON) .get(ForecastResponse.class);

		//service = client.target("http://bugs.legrain.fr/rest/bug?id=1");
		//service = client.target("http://bugs.legrain.fr/rest/bug?id=1,2");
		service = client.target(restURL+"/bug?"+apiKeyParamString+"&product=TestProduct");

		// String s = service.request(MediaType.APPLICATION_JSON).get(String.class);
		BugSearch b = service.request(MediaType.APPLICATION_JSON).get(BugSearch.class);

		System.out.println("BugzillaUtil.findBugs()");

		return b.getResult().getBugs();

//		ObjectMapper mapper = new ObjectMapper();
//		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
//		mapper.setAnnotationIntrospector(introspector);
	}

	private void login() {
		service= client.target(restURL+"/"+loginParamString);
		loginToken = service.request(MediaType.APPLICATION_JSON).get(Login.class);
		//		url+="?token="+l.getToken();
	}

	public Integer createBug(Bug b) {

		Integer newBugID = null;

		service = client.target(restURL+"/bug?"+apiKeyParamString);

		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(b);

			//form
			Form form = new Form();
			form.param("value", json);
			form.param("api_key", apiKey);
			
			//Response r = service.path("rest").path("bug").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
			//Response r = service.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));

			//Bug requestResult = service.path("rest").path("bug").request(MediaType.APPLICATION_JSON_TYPE)
			// .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), Bug.class);

			//POJO
			//Response r = service.path("rest").path("bug").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(b,MediaType.APPLICATION_JSON_TYPE));
			Response r = service.request(MediaType.APPLICATION_JSON_TYPE).header("Content-type", "application/json").post(Entity.entity(b,MediaType.APPLICATION_JSON_TYPE));

			System.out.println(r.getStatus()+" "+r.getEntity());
			String retour = r.readEntity(String.class);
			System.out.println(retour);

			ResultCreateBug res;

			res = mapper.readValue(retour, ResultCreateBug.class);
			newBugID = res.getResult().getId();
			
			System.out.println("Nouveau bug ID : "+res.getResult().getId());
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("BugzillaUtil.createBug()");	

		return newBugID;

	}
	
	public Integer createComment(String comment, int bugId) {
		Integer newCommentID = null;
		
		service = client.target(restURL+"/bug/"+bugId+"/comment?"+apiKeyParamString);
		
		CommentCreate c = new CommentCreate();
		c.setComment(comment);
		c.setIsPrivate(false);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
		
			Response r = service.request(MediaType.APPLICATION_JSON_TYPE).header("Content-type", "application/json").post(Entity.entity(c,MediaType.APPLICATION_JSON_TYPE));

			System.out.println(r.getStatus()+" "+r.getEntity());
			String retour = r.readEntity(String.class);
			System.out.println(retour);

			ResultCreateBug res;

			res = mapper.readValue(retour, ResultCreateBug.class);
			newCommentID = res.getResult().getId();
			
			System.out.println("Nouveau bug ID : "+res.getResult().getId());
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("BugzillaUtil.createBug()");	
		
		
		return newCommentID;
	}
	
	public Integer createUser(String email, String name, String password) {
		Integer newUserID = null;
		
		service = client.target(restURL+"/user?"+apiKeyParamString);
		
		UserCreate u = new UserCreate();
		u.setEmail(email);
		u.setFullName(name);
		u.setPassword(password);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
		
			Response r = service.request(MediaType.APPLICATION_JSON_TYPE).header("Content-type", "application/json").post(Entity.entity(u,MediaType.APPLICATION_JSON_TYPE));

			System.out.println(r.getStatus()+" "+r.getEntity());
			String retour = r.readEntity(String.class);
			System.out.println(retour);

			ResultCreateUserID res;

			res = mapper.readValue(retour, ResultCreateUserID.class);
			newUserID = res.getId();
			
			System.out.println("Nouvel utilisteur ID : "+res.getId());
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("BugzillaUtil.createUser()");	
		
		
		return newUserID;
	}

	public class Authenticator implements ClientRequestFilter {

		private final String user;
		private final String password;

		public Authenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		public void filter(ClientRequestContext requestContext) throws IOException {
			MultivaluedMap<String, Object> headers = requestContext.getHeaders();
			final String basicAuthentication = getBasicAuthentication();
			headers.add("Authorization", basicAuthentication);

		}

		private String getBasicAuthentication() {
			String token = this.user + ":" + this.password;
			try {
				return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				throw new IllegalStateException("Cannot encode with UTF-8", ex);
			}
		}
	}

}
