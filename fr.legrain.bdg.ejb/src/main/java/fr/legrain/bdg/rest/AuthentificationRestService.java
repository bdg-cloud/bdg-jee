package fr.legrain.bdg.rest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurWebServiceServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.filters.request.SecretService;
import fr.legrain.bdg.rest.model.ParamFirebaseCloudMessaging;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.dto.TaUtilisateurWebServiceDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.TaUtilisateurWebServiceService;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaTerminalMobile;
import fr.legrain.tiers.model.TaTiers;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class AuthentificationRestService extends AbstractRestService {

	//http://www.mkyong.com/tutorials/jax-rs-tutorials/

	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaTTiersServiceRemote taTTiersService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	@EJB private ITaUtilisateurWebServiceServiceRemote taUtilisateurWebServiceService;

	//	@Inject
	//    private KeyGenerator keyGenerator;
	@Inject private SecretService f;

	@Context
	private UriInfo uriInfo;

	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI = new LgrDozerMapper<>();

	/////////////////////////////////////////////////////////////////////////////////////////

	private String issueToken(String dossier, String login, Integer idTiers, String utilisateur) {
		//      Key key = keyGenerator.generateKey();

		String jwtToken = null;
		JwtBuilder jwtBuilder = Jwts.builder()
				.setSubject(login)
				.setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date())
				.setExpiration(toDate(LocalDateTime.now().plusDays(7L)))
				.claim(AbstractRestService.JWT_CLAIM_DOSSIER, dossier)
				//.setExpiration(toDate(LocalDateTime.now().plusSeconds(30L)))
				//.signWith(SignatureAlgorithm.HS512, key)
				.signWith(f.getKeyHS256());

		if(idTiers!=null) {
			jwtBuilder.claim(AbstractRestService.JWT_CLAIM_TIERS, idTiers);
		}
		if(utilisateur!=null) {
			jwtBuilder.claim(AbstractRestService.JWT_CLAIM_UTILISATEUR, utilisateur);
		}
		jwtToken = jwtBuilder.compact();
		return jwtToken;
	}

	private String issueRefreshToken(String dossier, String login, Integer idTiers, String utilisateur) {
		//    Key key = keyGenerator.generateKey();
		String jwtToken = null;
		JwtBuilder jwtBuilder = Jwts.builder()
				.setSubject(login)
				.setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date())
				.setExpiration(toDate(LocalDateTime.now().plusDays(15L)))
				.claim(AbstractRestService.JWT_CLAIM_DOSSIER, dossier)
				//.setExpiration(toDate(LocalDateTime.now().plusSeconds(60L)))
				//.signWith(SignatureAlgorithm.HS512, key)
				.signWith(f.getKeyHS256());
		if(idTiers!=null) {
			jwtBuilder.claim(AbstractRestService.JWT_CLAIM_TIERS, idTiers);
		}
		if(utilisateur!=null) {
			jwtBuilder.claim(AbstractRestService.JWT_CLAIM_UTILISATEUR, utilisateur);
		}
		jwtToken = jwtBuilder.compact();
		return jwtToken;
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	@POST()
	@Path("/authenticate-espace-client")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Operation(summary = "Authentification et connexion d'un utilisateur du service client (un des tiers/clients du dossier)",
	tags = {MyApplication.TAG_AUTH_ESPACE_CLIENT},
	description = "Création d'une paire de token JWT",
	responses = {
			@ApiResponse(responseCode = CODE_HTTP_200, description = "L'utilisateur espace client authentifié, contenant entre autre les 2 tokens access et refresh", 
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = TaUtilisateurWebServiceDTO.class)
							)),
			@ApiResponse(responseCode = CODE_HTTP_500, description = "Login ou mot de passe invalide."),
	})
	public Response authenticateEspaceClient(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @FormParam("login") String loginAngular,
			@Parameter(required = true, allowEmptyValue = false) @FormParam("password") String pwdAngular) {

		TaTiersDTO t = null;

		try {


			TaUtilisateur taUtilisateur = new TaUtilisateur();
			pwdAngular = taUtilisateur.passwordHashSHA256_Base64(pwdAngular);
			TaEspaceClientDTO taEspaceClient = taEspaceClientService.loginDTO(loginAngular, pwdAngular);


			// Issue a token for the user
			String token = issueToken(dossier,loginAngular,taEspaceClient.getIdTiers(),null);
			String refreshToken = issueRefreshToken(dossier,loginAngular,taEspaceClient.getIdTiers(),null);
			//TaEspaceClient taEspaceClient = taEspaceClientService.login(login, password);
			//TaEspaceClient taEspaceClient = taEspaceClientService.login(loginAngular, pwdAngular);


			TaEspaceClient ec = taEspaceClientService.findById(taEspaceClient.getId());
			ec.setDateDerniereConnexion(new Date());
			taEspaceClientService.merge(ec);

			taEspaceClient.setToken(token);
			taEspaceClient.setAccessToken(token);
			taEspaceClient.setRefreshToken(refreshToken);

			// Return the token on the response
			//            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();

			//	    	return Response.status(200).entity(t).build();
			return Response.status(200)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					//	    			.entity(
					//	    					"{\"id\":1,\"username\":\"a\",\"password\":\"rr\",\"firstName\":\"zzz\",\"lastName\":\"eee\","
					//	    					+ "\"token\":\""+token+"\",\"accessToken\":\""+token+"\",\"refreshToken\":\""+refreshToken+"\"}"
					//	    					)
					.entity(taEspaceClient)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError()
					.entity(
							"Email ou mot de passe invalide."
							)
					.build();
		}
		//    	return Response.ok(t,MediaType.APPLICATION_XML).build();
		//		return Response.status(200).entity("getUserByName is called, name : " + id).build();
		//		} else {
		//			return Response.status(Status.UNAUTHORIZED).entity(t).build();
		//		}
	}

	@POST()
	@Path("/refresh-espace-client")
	@Operation(summary = "Création d'un nouveau token de connexion à partir d'un refresh token quand le token de connexion a expirer",
	tags = {MyApplication.TAG_AUTH_ESPACE_CLIENT},
	description = "Renouvellement de la paire de token d'authentification",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	public Response refreshEspaceClient(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) String refreshToken) {

		TaTiersDTO t = null;

		Integer idTiers = (Integer) Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(refreshToken).getBody().get(AbstractRestService.JWT_CLAIM_TIERS);
		String login = (String) Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(refreshToken).getBody().getSubject();
		try {

			//t = taTiersService.findById(id);
			//t = taTiersService.findByIdDTO(id);
			//System.out.println("TiersRestService.getTiersById() : "+t.getCodeTiers());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Issue a token for the user
		String token = issueToken(dossier,login,idTiers,null);
		refreshToken = issueRefreshToken(dossier,login,idTiers,null);

		// Return the token on the response
		//            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();

		//	    	return Response.status(200).entity(t).build();
		return Response.status(200)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.entity(
						"{\"id\":1,\"username\":\"a\",\"password\":\"rr\",\"firstName\":\"zzz\",\"lastName\":\"eee\","
								+ "\"token\":\""+token+"\",\"accessToken\":\""+token+"\",\"refreshToken\":\""+refreshToken+"\"}"
						)
				.build();

	}

	@POST()
	@Path("/{id}/android-registration-token")
	@Operation(summary = "Association de l'identifiant unique Android au compte client du tiers correspondant",
	tags = {MyApplication.TAG_AUTH_ESPACE_CLIENT},
	description = "L'android registration token est notamment utilisé pour les push Firebase",
	responses = {
			@ApiResponse(responseCode = "200", description = "Le token Android a été associé au compte client correspondant")
	})
	public Response updateAndroidRegistrationToken(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@HeaderParam(X_HEADER_LOGIN) String login,
			@HeaderParam(X_HEADER_PASSWORD) String password,

			@Parameter(name = "id", description = "ID du compte client qui utilise ce terminal") 
			@PathParam("id") int id,

			@Parameter(name = "androidRegistrationToken", description = "Token unique du terminal généré par le SDK Firebase dans l'application Android") 
			String androidRegistrationToken
			) {


		if(initTenantAndAuthenticate(dossier,login, password)) {
			//    	TaTiers t = null;

			try {
				TaEspaceClient taEspaceClient = taEspaceClientService.findById(id);
				//TODO a la sortie du webservice la chaine est entourée par des guillemet, chercher pourquoi ?
				androidRegistrationToken = androidRegistrationToken.replaceAll("\"", ""); 
				//taEspaceClient.setAndroidRegistrationToken(androidRegistrationToken);
				if(taEspaceClient.getTaTerminalMobiles()==null) {
					taEspaceClient.setTaTerminalMobiles(new HashSet<>());
				}
				boolean existeDeja = false;
				//TODO remplacer le for par une methode plus optimisé avec des requetes
				for (TaTerminalMobile tm : taEspaceClient.getTaTerminalMobiles()) {
					if(tm.getAndroidRegistrationToken().equals(androidRegistrationToken)) {
						existeDeja = true;
					}
				}
				if(!existeDeja) {
					TaTerminalMobile nouveauTerminal = new TaTerminalMobile();
					nouveauTerminal.setAndroidRegistrationToken(androidRegistrationToken);
					nouveauTerminal.setTaEspaceClient(taEspaceClient);
					taEspaceClient.getTaTerminalMobiles().add(nouveauTerminal);
				}
				taEspaceClientService.merge(taEspaceClient);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return Response.status(200).build();

		} else {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@POST()
	@Path("/{id}/android-registration-token-update")
	@Operation(summary = "Association de l'identifiant unique Android au compte client du tiers correspondant",
	tags = {MyApplication.TAG_AUTH_ESPACE_CLIENT},
	description = "L'android registration token est notamment utilisé pour les push Firebase",
	responses = {
			@ApiResponse(responseCode = "200", description = "Le token Android a été associé au compte client correspondant")
	})
	public Response updateAndroidRegistrationToken(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@HeaderParam(X_HEADER_LOGIN) String login,
			@HeaderParam(X_HEADER_PASSWORD) String password,

			@Parameter(name = "id", description = "ID du compte client qui utilise ce terminal") 
			@PathParam("id") int id,

			@Parameter(name = "paramFirebaseCloudMessaging", 
			description = "Objet contenant le nouveau token unique du terminal généré par le SDK Firebase dans l'application Android, ainsi que l'ancien token") 
			ParamFirebaseCloudMessaging paramFirebaseCloudMessaging
			) {


		if(initTenantAndAuthenticate(dossier,login, password)) {

			try {
				TaEspaceClient taEspaceClient = taEspaceClientService.findById(id);
				String androidRegistrationToken = paramFirebaseCloudMessaging.getNouveauToken();
				String ancienRegistrationToken = paramFirebaseCloudMessaging.getAncienToken();
				//TODO a la sortie du webservice la chaine est entourée par des guillemet, chercher pourquoi ?
				androidRegistrationToken = androidRegistrationToken.replaceAll("\"", ""); 
				//taEspaceClient.setAndroidRegistrationToken(androidRegistrationToken);
				if(taEspaceClient.getTaTerminalMobiles()==null) {
					taEspaceClient.setTaTerminalMobiles(new HashSet<>());
				}
				if(ancienRegistrationToken!=null && !ancienRegistrationToken.equals("") && !ancienRegistrationToken.equals(androidRegistrationToken)) {
					//on essaye de mettre a jour un ancien token
					boolean ancienTokenTrouve = false;
					for (TaTerminalMobile tm : taEspaceClient.getTaTerminalMobiles()) {
						if(tm.getAndroidRegistrationToken().equals(ancienRegistrationToken)) {
							ancienTokenTrouve = true; //mise a jour
							tm.setAndroidRegistrationToken(androidRegistrationToken);
						}
					}
				} else {
					//pas d'ancien token, on verifie que le token n'existe pas deja et s'il n'existe pas on l'ajoute
					boolean existeDeja = false;
					//TODO remplacer le for par une methode plus optimisé avec des requetes
					for (TaTerminalMobile tm : taEspaceClient.getTaTerminalMobiles()) {
						if(tm.getAndroidRegistrationToken().equals(androidRegistrationToken)) {
							existeDeja = true; //rien a faire
						}
					}
					if(!existeDeja) {
						TaTerminalMobile nouveauTerminal = new TaTerminalMobile();
						nouveauTerminal.setAndroidRegistrationToken(androidRegistrationToken);
						nouveauTerminal.setTaEspaceClient(taEspaceClient);
						taEspaceClient.getTaTerminalMobiles().add(nouveauTerminal);
					}
				}
				taEspaceClientService.merge(taEspaceClient);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			return Response.status(200).build();

		} else {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@POST()
	@Path("/authenticate")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Operation(summary = "Authentification et connexion d'un utilisateur du dossier Bureau de Gestion",
	tags = {MyApplication.TAG_AUTH_BDG},
	description = "Création d'une paire de token JWT",
	responses = {
			@ApiResponse(responseCode = CODE_HTTP_200, description = "L'utilisateur authentifié, contenant entre autre les 2 tokens access et refresh", 
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = TaUtilisateurWebServiceDTO.class)
							)),
			@ApiResponse(responseCode = CODE_HTTP_500, description = "Login ou mot de passe invalide."),
			@ApiResponse(responseCode = CODE_HTTP_403, description = "Non autorisé"),
			
	})
	@SecurityRequirements
	public Response authenticate(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @FormParam("login") String loginForm, 
			@Parameter(required = true, allowEmptyValue = false) @FormParam("password") String pwdForm) {

		TaUtilisateurWebServiceDTO taUtilisateurDTO = null;
		try {

			TaUtilisateur taUtilisateur = new TaUtilisateur();
			pwdForm = taUtilisateur.passwordHashSHA256_Base64(pwdForm);
			taUtilisateurDTO = taUtilisateurWebServiceService.loginDTO(loginForm, pwdForm);

			// Issue a token for the user
			String token = issueToken(dossier, loginForm, null,taUtilisateurDTO.getLogin());
			String refreshToken = issueRefreshToken(dossier, loginForm, null,taUtilisateurDTO.getLogin());


			if(taUtilisateurDTO!=null) {
				//					taUtilisateurDTO.setToken(token);
				taUtilisateurDTO.setAccessToken(token);
				taUtilisateurDTO.setRefreshToken(refreshToken);
			}

			// Return the token on the response
			//          return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();

			//	    	return Response.status(200).entity(t).build();
			return Response.status(200)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					//	    			.entity(
					//	    					"{\"id\":1,\"username\":\"a\",\"password\":\"rr\",\"firstName\":\"zzz\",\"lastName\":\"eee\","
					//	    					+ "\"token\":\""+token+"\",\"accessToken\":\""+token+"\",\"refreshToken\":\""+refreshToken+"\"}"
					//	    					)
					.entity(taUtilisateurDTO)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError()
					//		    			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					.entity(
							"Email ou mot de passe invalide."
							)
					.build();
		}
	}

	@POST()
	@Path("/refresh")
	@Operation(summary = "Création d'un nouveau token de connexion à partir d'un refresh token quand le token de connexion a expirer",
	tags = {MyApplication.TAG_AUTH_BDG},
	description = "Renouvellement de la paire de token d'authentification",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	public Response refresh(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) String refreshToken) {

		TaTiersDTO t = null;

		String utilisateur = (String) Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(refreshToken).getBody().get(AbstractRestService.JWT_CLAIM_UTILISATEUR);
		String login = (String) Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(refreshToken).getBody().getSubject();
		// Issue a token for the user
		String token = issueToken(dossier, login, null,utilisateur);
		refreshToken = issueRefreshToken(dossier, login, null,utilisateur);

		return Response.status(200)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.entity(
						"{\"id\":1,\"username\":\"a\",\"password\":\"rr\",\"firstName\":\"zzz\",\"lastName\":\"eee\","
								+ "\"token\":\""+token+"\",\"accessToken\":\""+token+"\",\"refreshToken\":\""+refreshToken+"\"}"
						)
				.build();
	}

}