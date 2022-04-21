package fr.legrain.bdg.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import fr.legrain.bdg.compteclientfinal.service.LgrEmail;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.filters.request.JWTTokenNeeded;
import fr.legrain.bdg.rest.model.ParamCompteEspaceClient;
import fr.legrain.bdg.rest.model.ParamPub;
import fr.legrain.bdg.rest.proxy.multitenant.IEspaceClientRestMultitenantProxy;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.moncompte.ws.TaDossier;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;
import fr.legrain.tiers.dto.TaAdresseDTO;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.dto.TiersDossierDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCompl;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TaWeb;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/espace-client")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class EspaceClientRestService extends AbstractRestService {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaLFactureServiceRemote taLFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private IEspaceClientRestMultitenantProxy multitenantProxy;
	
	@EJB private ITaParamEspaceClientServiceRemote taParamEspaceClientService;
	
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
	private @EJB ITaTTiersServiceRemote taTTiersService;
	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;

	
	@EJB private ITaTAdrServiceRemote taTAdrService;
	@EJB private ITaAdresseServiceRemote taAdresseService;

	@EJB private LgrEmail lgrEmail;

	private BdgProperties bdgProperties;

	private MonCompteWebServiceClientCXF wsMonCompte;
	
	private static final int BUFFER_SIZE = 4096;

	@PostConstruct
	public void init() {
		try {
			wsMonCompte = new MonCompteWebServiceClientCXF();

			bdgProperties = new BdgProperties();
			
			taParametreService.initParamBdg();
			////			util = new LgrEmailUtil("smtp.legrain.fr",587,true,"nicolas@legrain.fr","pwd37las67"); //STARTTLS
			////			util = new LgrEmailUtil("smtp.bdg.cloud",587,true,"noreply@bdg.cloud","aeHaegh2");
			//			util = new LgrEmailUtil(
			//					bdgProperties.getProperty(BdgProperties.KEY_SMTP_HOSTNAME),
			//					LibConversion.stringToInteger(bdgProperties.getProperty(BdgProperties.KEY_SMTP_PORT)),
			//					LibConversion.StringToBoolean(bdgProperties.getProperty(BdgProperties.KEY_SMTP_SSL)),
			//					bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN),
			//					bdgProperties.getProperty(BdgProperties.KEY_SMTP_PASSWORD)
			//					); //STARTTLS
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@POST()
	@Path("/creer-compte-espace-client")
	@Operation(summary = "Création d'un compte espace client", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response creerCompteEspaceClient(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) throws EJBException{
		try {
			TaEspaceClientDTO dto = null;
			TaUtilisateur taUtilisateur = new TaUtilisateur();
			
			
			ParamCompteEspaceClient paramCompteEspaceClient = new ParamCompteEspaceClient();
			p.decrypte();
			p.setPassword(taUtilisateur.passwordHashSHA256_Base64(p.getPassword()));
			paramCompteEspaceClient = p;
			
			if(p.getDateLimiteLien()!=null) {
				Calendar cal = Calendar.getInstance(); // creates calendar
				cal.setTime(p.getDateLimiteLien());               // sets calendar time/date
				cal.add(Calendar.DAY_OF_WEEK, 1);      // adds one hour
				Date dateValidite = cal.getTime();  
				
				if(dateValidite.before(new Date())) {
					throw new Exception("Ce liens n'est plus valide.");
				}
			}
			
			TaEspaceClientDTO retour = null;
			TaParamEspaceClient param = taParamEspaceClientService.findInstance();
			
				TaEspaceClient taEspaceClient = taEspaceClientService.findByCode(paramCompteEspaceClient.getEmail());
				if(taEspaceClient!=null) {
					throw new Exception("Un compte avec cette adresse email existe déjà.");
				}
			
				TaTiers taTiers = null;
				if(param.getLiaisonNouveauCompteEmailAuto()!=null && param.getLiaisonNouveauCompteEmailAuto()) {
					List<TaTiers>  listeTiers = taTiersService.findByEmailParDefaut(paramCompteEspaceClient.getEmail());
					if(listeTiers!=null && !listeTiers.isEmpty()) {
						taTiers = listeTiers.get(0);
					}
					if(taTiers==null) {
						//pas de tiers correspondant, lever une exception ?
					}
				}
				
//				if(paramCompteEspaceClient.getCodeTiers()!=null) {
//					//taTiers = taTiersService.findByCode(p.getcodeTiers());
//					//taTiers = taTiersService.findByCode(paramCompteEspaceClient.getCodeTiers());
//					if(taTiers==null) {
//						//pas de tiers correspondant, lever une exception ?
//					}
//				}
				taEspaceClient = new TaEspaceClient();
				taEspaceClient.setTaTiers(taTiers);
				taEspaceClient.setEmail(paramCompteEspaceClient.getEmail());
				taEspaceClient.setPassword(paramCompteEspaceClient.getPassword());
				taEspaceClient.setNom(paramCompteEspaceClient.getNom());
				taEspaceClient.setPrenom(paramCompteEspaceClient.getPrenom());
				taEspaceClient.setActif(true);
				taEspaceClient = taEspaceClientService.merge(taEspaceClient);
				retour = taEspaceClientService.findByIdDTO(taEspaceClient.getIdEspaceClient());

			dto = retour;
			
			return Response.status(200)
					.entity(dto)
					.build(); 
		} catch(Exception e) {
			//throw new EJBException(e);
			String messageErreur = "Une erreur inconnue est survenue pendant l'enregistrement de l'espace client";
			if(e.getMessage() != null ) {
				 messageErreur = e.getMessage();
				if(e.getMessage().contains("ta_espace_client_email_idx_a")) {
					//même email avec ce meme id tiers existe deja
					messageErreur = "même email avec ce meme id tiers existe deja";
				} else if(e.getMessage().contains("ta_espace_client_email_idx_b")) {
					//même email sans code tiers existe deja
					messageErreur = "même email sans code tiers existe deja";
				}
			}
			
			return Response.serverError()
					.entity(messageErreur)
					.build();
		}
	}

	@PUT()
	@Path("/modifier-compte-espace-client")
	@Operation(summary = "Mise a jour d'un compte espace client", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response modifierCompteEspaceClient(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) throws EJBException{
		try {
			TaEspaceClientDTO dto = null;
			TaUtilisateur taUtilisateur = new TaUtilisateur();
			p.setPassword(taUtilisateur.passwordHashSHA256_Base64(p.getPassword()));

			TaEspaceClientDTO retour = null;
				TaTiers taTiers = null;
				if(p.getCodeTiers()!=null) {
					taTiers = taTiersService.findByCode(p.getCodeTiers());
				}
				TaEspaceClient taEspaceClient = taEspaceClientService.findById(p.getId());
				taEspaceClient.setTaTiers(taTiers);
//				taEspaceClient.setEmail(p.getEmail());
//				taEspaceClient.setPassword(p.getPassword());
//				taEspaceClient.setActif(true);
				taEspaceClientService.merge(taEspaceClient);
				retour = taEspaceClientService.findByIdDTO(taEspaceClient.getIdEspaceClient());

			dto = retour;
			
			return Response.status(200)
					.entity(dto)
					.build(); 
		} catch(Exception e) {
			String messageErreur = e.getMessage();
			if(e.getMessage().contains("ta_espace_client_email_idx_a")) {
				//même email avec ce meme id tiers existe deja
				messageErreur = "même email avec ce meme id tiers existe deja";
			} else if(e.getMessage().contains("ta_espace_client_email_idx_b")) {
				//même email sans code tiers existe deja
				messageErreur = "même email sans code tiers existe deja";
			}
			return Response.serverError()
					.entity(messageErreur)
					.build();
		}
	}
	
	@PUT()
	@Path("/liaison-nouveau-compte-espace-client")
	@Operation(summary = "Mise a jour d'un compte espace client", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response liaisonNouveauCompteEspaceClient(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) throws EJBException{
		try {
			TaUtilisateur taUtilisateur = new TaUtilisateur();
			p.setPassword(taUtilisateur.passwordHashSHA256_Base64(p.getPassword()));
			TaEspaceClientDTO dto = null;

			TaEspaceClientDTO retour = null;
				TaTiers taTiers = null;
				TaEspaceClient taEspaceClient = null;
				if(p.getCodeTiers()!=null) {
					taEspaceClient = taEspaceClientService.findByCodeTiers(p.getCodeTiers());
					if(taEspaceClient!=null && !taEspaceClient.getEmail().equals(p.getEmail())) {
						throw new Exception("Ce code tiers est déjà lié à une autre adresse email");
					}
					
					taTiers = taTiersService.findByCode(p.getCodeTiers());
					if(taTiers==null) {
						throw new Exception("Ce code tiers introuvable");
					}
				} else {
					taEspaceClient = taEspaceClientService.findById(p.getId());
					String codeTypeTiersDefaut = "VISIT";
					TaTTiers taTTiers;
					taTTiers = taTTiersService.findByCode(codeTypeTiersDefaut);
					
					String codeTypeTvaDefaut = "F";
					TaTTvaDoc taTTvaDoc;
					taTTvaDoc = taTTvaDocService.findByCode(codeTypeTvaDefaut);
					
					taTiers = new TaTiers();
//					taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
					taTiers.setCodeTiers(taTiersService.genereCode(null));
					taTiers.setCodeCompta(taTiers.getCodeTiers());
					taTiers.setTaTTiers(taTTiers);
					taTiers.setCompte("411");
					taTiers.setNomTiers(taEspaceClient.getNom());
					taTiers.setPrenomTiers(taEspaceClient.getPrenom());
					
					TaEmail taEmail = new TaEmail();
					taEmail.setTaTiers(taTiers);
					taEmail.setAdresseEmail(taEspaceClient.getEmail());
					taTiers.setTaEmail(taEmail);
					taTiers.addEmail(taEmail);

					if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
						taTiers.setCompte(taTTiers.getCompteTTiers());
					} 
					taTiers.setActifTiers(1);
					taTiers.setTtcTiers(0);
					taTiers.setTaTTvaDoc(taTTvaDoc);
					taTiers = taTiersService.merge(taTiers);
				}
				taEspaceClient = taEspaceClientService.findById(p.getId());
				taEspaceClient.setTaTiers(taTiers);
				taEspaceClient.setPrenom(taTiers.getNomTiers());
				taEspaceClient.setNom(taTiers.getPrenomTiers());
//				taEspaceClient.setEmail(p.getEmail());
//				taEspaceClient.setPassword(p.getPassword());
//				taEspaceClient.setActif(true);
				taEspaceClientService.merge(taEspaceClient);
				retour = taEspaceClientService.findByIdDTO(taEspaceClient.getIdEspaceClient());
				
			dto =  retour;
			return Response.status(200)
					.entity(dto)
					.build(); 
		} catch(Exception e) {
			//throw new EJBException(e);
			String messageErreur = "Une erreur inconnue est survenue pendant la liaison entre votre nouveau code tier et votre nouveau compte espace client";
			if( e.getMessage() != null) {
				if(e.getMessage().contains("ta_espace_client_email_idx_a")) {
					//même email avec ce meme id tiers existe deja
					messageErreur = "même email avec ce meme id tiers existe deja";
				} else if(e.getMessage().contains("ta_espace_client_email_idx_b")) {
					//même email sans code tiers existe deja
					messageErreur = "même email sans code tiers existe deja";
				}
			}
			
			return Response.serverError()
					.entity(messageErreur)
					.build();
		}
	}

	@POST()
	@Path("/email-creer-compte-espace-client")
	@Operation(summary = "Email pour valider la création d'un espace client", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response emailCreerCompteEspaceClient(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) throws EJBException{
		
		try {
			TaUtilisateur taUtilisateur = new TaUtilisateur();
			p.setPassword(taUtilisateur.passwordHashSHA256_Base64(p.getPassword()));
			lgrEmail.emailPourLaValidationDeCreationDunCompteClient(p);
			return Response.status(200)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}
	
//	@POST()
//	@Path("/email-creer-compte-espace-client-deja-client-verif")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@JWTTokenNeeded
//	@Operation(summary = "Email pour valider la création d'un espace client avec vérification préalable.", tags = {MyApplication.TAG_ESPACE_CLIENT})
//	public Response emailCreerCompteEspaceClientDejaClientVerification(
//			@HeaderParam(X_HEADER_DOSSIER) String dossier,
//			ParamCompteEspaceClient p) throws EJBException{
//		
//		dossier = initTenant(dossier);
//		p.setCodeDossierFournisseur(dossier);
//		try {
//			boolean dejaClient = true;
//			TaUtilisateur taUtilisateur = new TaUtilisateur();
//			p.setPassword(taUtilisateur.passwordHashSHA256_Base64(p.getPassword()));
//			
//			if(dejaClient) {
//				List<TaTiers> listeTiers = taTiersService.findByEmail(p.getEmail());
//				boolean emailDansDossier = listeTiers!=null && !listeTiers.isEmpty();
//				if(emailDansDossier) {
//					boolean unSeulTiersAvecCetEmail = listeTiers.size()==1;
//					if(unSeulTiersAvecCetEmail) {
//						
//						boolean tiersPossedeEspaceClient = taEspaceClientService.findByCodeTiers(listeTiers.get(0).getCodeTiers())!=null;
//						if(tiersPossedeEspaceClient) {
//							//ERREUR
//							//message pour contacter le support ou changer le mot de passe
//						} else {
//							//"NORMAL", la liaison sera automatique (on a trouvé un seul tiers et il n'y a pas de compte espace client)
//							//si le code tiers est rempli on pourra faire une vérification supplémentaire
//							lgrEmail.emailPourLaValidationDeCreationDunCompteClient(p);
//						}
//					} else { //plusieurs tiers avec cet email
//						boolean codeTiersRempli = p.codeTiers!=null && !p.codeTiers.equals("");
//						if(codeTiersRempli) {
//							boolean codeTiersOK = taTiersService.findByCode(p.codeTiers) != null;
//							boolean codeTiersInvalide = !codeTiersOK;
//							if(codeTiersInvalide) {
//								//ERREUR, contacter
//							} else if(codeTiersOK) {
//								/*Dans ces cas vérifier pourquoi l'adresse email n'est pas la meme et traiter le cas : maj de l'adresse OU ajout de l'adresse OU Erreur*/
//								boolean pasDespaceClientSurLeTiers = taEspaceClientService.findByCodeTiers(p.getCodeTiers())==null;
//								if(pasDespaceClientSurLeTiers) {
//									//"NORMAL", la liaison sera automatique (on a trouvé un seul tiers au final (email + code tiers) et il n'y a pas de compte espace client)
//								} else {
//									//ERREUR, compte deja existant
//									//message pour contacter le support ou changer le mot de passe
//								}
//							}
//						} else {
//						//renvoyer cette liste avec l'information pour savoir si ces tiers ont deja des comptes ou pas
//						//cette liste permettra de faire un choix
//							for (TaTiers taTiers : listeTiers) {
//								
//							}
//							//Le choix sera renvoyé a une autre methode pour finaliser la procédure de l'envoi du mail de confirmation ou pas
//						}
//					}
//				} else {
//					//"ERREUR"
//					//pas deja client ou probleme
//					//utiliser l'autre cas (deja client)
//				}
//			} else { //pas deja client
//				List<TaTiers> listeTiers = taTiersService.findByEmail(p.getEmail());
//				boolean adresseEmailPasDejaDansLeDossier = listeTiers==null || listeTiers.isEmpty();
//				if(adresseEmailPasDejaDansLeDossier) {
//					//"NORMAL" (pas de liaison automatique)
//				} else {
//					if(leTiersQuiACetteAdresseADejaUnEspaceClient) {
//						//ERREUR
//						//message pour contacter le support ou changer le mot de passe
//					} else {
//						//"ERREUR", l'utilisateur est finalement deja client (et on pourra faire une liaison automatique)
//					}
//				}
//			}
//
//			
//			lgrEmail.emailPourLaValidationDeCreationDunCompteClient(p);
//			return Response.status(200)
//					//.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//					//.entity(dto)
//					.build(); 
//		} catch(EJBException e) {
//			throw new EJBException(e);
//		}
//	}

	@POST()
	@Path("/demande-mdp")
	@Operation(summary = "Demander la réinitialisation du mot de passe", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response demandeMotDePasse(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) throws EJBException {

		try {
			TaEspaceClientDTO dto = null;
			
			TaEspaceClientDTO retour = null;
			try {
				TaEspaceClient taEspaceClient = null;
				if(p.getEmail()!=null) {
					taEspaceClient = taEspaceClientService.findByCode(p.getEmail());
				}
				if(taEspaceClient != null) {
						System.out.println("MdpOublieBean.demandeMotDePasse() => adresseEmail : "+p.getEmail());

//						String a = p.getCodeDossierFournisseur()+separateurChaineCryptee+p.getEmail();
//						String b = LibCrypto.encrypt(a);
						
						p.setDateLimiteLien(new Date());
						p.crypte();

						//		String subject = "mon sujet ..";
						//		String msgTxt = "mon email ...";
						//		String fromEmail = "nicolas@legrain.fr";
						//		String fromName = "Nicolas";
						//		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};

						//String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Demande de changement de mot de passe";
						String subject = p.getHostname()+" - Demande de changement de mot de passe";

						String utilisateur = p.getEmail();
						String lienCnx = "";
						//		String lienCnx = "http://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+tenantDossier+"."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+":"+bdgProperties.getProperty(BdgProperties.KEY_PORT_HTTP,"")+"/login/mdp_validation.xhtml?p="+URLEncoder.encode(b, "UTF-8")+"";
//						lienCnx = bdgProperties.url("espaceclient")+"/login/mdp_validation.xhtml?p="+URLEncoder.encode(p.infoCompteCryptees, "UTF-8")+"";
						String port = p.getPort()!=null?(!p.getPort().equals("80")?":"+p.getPort():""):"";
						lienCnx = "https://"+p.getHostname()+port+"/#/validation-modif-mdp?p="+URLEncoder.encode(p.infoCompteCryptees, "UTF-8")+"";
						
						String lienCnxTxt = "";
						String msgTxt = "Bonjour "+utilisateur+",\n"
								+"\n"
								+"nous avons reçu une demande de modification de mot passe pour votre dossier Bureau de Gestion.\n"
								+"\n"
								+"Afin de finaliser ce changement de mot de passe, merci de cliquer sur le lien suivant :\n"
								+"\n"
								+""+lienCnx+"\n"
								+"\n"
								+"Ce lien sera valide pendant 24 heures seulement.\n"
								+"\n"
								//+"Si vous ne pouvez pas cliquer sur le lien, copiez le entièrement et collez le dans la barre d'adresse de votre navigateur puis validez.\n"
								//+"\n"
								//+""+lienCnxTxt+"\n"
								//+"\n"
								+"Si vous n'êtes pas à l'origine de cette demande, ignorez ce message. Vous conserverez vos identifiants habituels pour vous connecter sur le Bureau de Gestion.\n"
								+"\n"
								+"Cordialement,\n"
								+"\n"
								+"Service Gestion.\n"
								+"\n"
								+"Le Bureau de Gestion est un service en ligne dédié à la gestion des entreprises proposé par Legrain SAS. \n"
								;
						String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
						String fromName = "Bureau de Gestion";
						String[][] toEmailandName = new String[][]{ {p.getEmail(),""}};

						lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);

						//envoie d'une copie à legrain
						toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};//Zee6Zasa
						lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
						//TODO faire avec BCC

//						FacesContext context = FacesContext.getCurrentInstance();  
//						context.addMessage(null, new FacesMessage("", 
//								"Vous allez recevoir un email pour changer votre mot de passe à l'adresse suivante : "+p.getEmail()
//								)); 
	//
//						actionOk = true;
				} else {
					//ERREUR
					//Aucun utilisateur avec cet identifiant n'existe dans le dossier : "+tenantDossier, 
				}
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			dto = retour;

			return Response.status(200)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}

	}

	@POST()
	@Path("/decrypter")
	@Operation(summary = "Décrypter les paramètres d'un compte espace client (récupéré crypter dans une url par exemple)", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response decrypterCompteEspaceClient(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) {
		try {
			p.decrypte();
			return Response.status(200)
					.entity(p)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@POST()
	@Path("/crypter")
	@Operation(summary = "Crypter les paramètres d'un compte espace client (pour les passer dans une url par exemple)", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response crypterCompteEspaceClient(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) {
		try {
			p.crypte();
			return Response.status(200)
					.entity(p)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@POST()
	@Path("/confirme-mdp")
	@Operation(summary = "Confirmation du changement de mot de passe", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response changeMotDePasse(@Parameter(required = true, allowEmptyValue = false) ParamCompteEspaceClient p) throws Exception {

		try {
			p.decrypteDateLimite();
			
			if(p.getDateLimiteLien()!=null) {
				Calendar cal = Calendar.getInstance(); // creates calendar
				cal.setTime(p.getDateLimiteLien());               // sets calendar time/date
				cal.add(Calendar.DAY_OF_WEEK, 1);      // adds one hour
				Date dateValidite = cal.getTime();  
				
				if(dateValidite.before(new Date())) {
					throw new Exception("Ce liens n'est plus valide.");
				}
			}
			
			try {
				TaEspaceClient taEspaceClient = null;
				if(p.getEmail()!=null) {
					taEspaceClient = taEspaceClientService.findByCode(p.getEmail());
				}
				if(taEspaceClient != null) {
		
					if(p.getPassword()!=null && !p.getPassword().equals("") 
							&& p.getPasswordConf()!=null && !p.getPasswordConf().equals("") 
							&& p.getPassword().equals(p.getPasswordConf())) {
						//le nouveau mot de passe saisie est correct
						TaUtilisateur taUtilisateur = new TaUtilisateur();
						p.setPassword(taUtilisateur.passwordHashSHA256_Base64(p.getPassword()));
		
						if(true /*verifComplexiteMotDePasse(p.getPassword())*/) {
							taEspaceClient.setPassword(p.getPassword());
		//					taEspaceClient.setPasswd(taEspaceClient.passwordHashSHA256_Base64(p.getPassword()));
							taEspaceClientService.enregistrerMerge(taEspaceClient);
							
		
							//		String subject = "mon sujet ..";
							//		String msgTxt = "mon email ...";
							//		String fromEmail = "nicolas@legrain.fr";
							//		String fromName = "Nicolas";
							//		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};
		
							String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de modification de votre mot de passe";
		
							String utilisateur = p.getEmail();
							String msgTxt = "Bonjour "+utilisateur+",\n"
									+"\n"
									+"nous vous confirmons que la modification de votre mot de passe a bien été prise en compte.\n"
									+"\n"
									+"Notez bien que seul votre mot de passe a été changé et que le nom d'utilisateur est toujours le même.\n"
									+"\n"
									+"Important !\n"
									+"\n"
									+"Si vous n'êtes pas l'origine de cette modification, veuillez contacter le plus vite possible notre support soit :\n"
									+"\n"
									+"Par téléphone : 05.63.30.31.44\n"
									+"\n"
									+"Par Email : support@legrain.fr\n"
									+"\n"
									+"En cas de communication par email, n'oubliez pas de rappeler votre nom d'utilisateur et votre code client.\n"
									+"\n"
									+"Cordialement,\n"
									+"\n"
									+"Service Gestion.\n"
									+"\n"
									+"Le Bureau de Gestion est un service en ligne dédié à la gestion des entreprises proposé par Legrain SAS. \n"
									;
							String fromEmail = "legrain@legrain.fr";
							String fromName = "Legrain";
							String[][] toEmailandName = new String[][]{ {p.getEmail(),""}};
		
							lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		
							//envoie d'une copie à legrain
							//toEmailandName = new String[][]{ {"legrain@legrain.fr",null}};
							//lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
							//TODO faire avec BCC
		
//							FacesContext context = FacesContext.getCurrentInstance();  
//							context.addMessage(null, new FacesMessage("", 
//									//"Votre nouveau mot de passe viens de vous être envoyé à l'adresse suivante : "+adresseEmail
//									"Votre mot de passe a bien été changé. Un mail de confirmation a été envoyé à l'adresse suivante : "+p.getEmail()+" **** "+p.getPassword()
//									)); 
		
		//					actionOk = true;
		
		
						} else {
//							FacesContext context = FacesContext.getCurrentInstance();  
//							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Votre nouveau mot de passe est trop simple", 
//									""
//									)); 
						}
					} else {
		
//						FacesContext context = FacesContext.getCurrentInstance();  
//						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Les valeurs saisie doivent être identique", 
//								""
//								)); 
					}
				} else {
//					FacesContext context = FacesContext.getCurrentInstance();  
//					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aucun utilisateur avec cet identifiant n'existe dans le dossier : "+p.getCodeDossierFournisseur(), 
//							""
//							)); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.status(200)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@POST()
	@Path("/email-publicite")
	@Operation(summary = "Envoi d'un email publicitaire au un client qui a cliqué dessus", tags = {MyApplication.TAG_ESPACE_CLIENT, MyApplication.TAG_DIVERS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response emailPublcite(@Parameter(required = true, allowEmptyValue = false) ParamPub p) throws EJBException{
		try {
			TaTiers t = taTiersService.findByCode(p.getCodeTiers());
			lgrEmail.emailPublicite(p,t);
			return Response.status(200)
					.build(); 
		} catch(EJBException | FinderException e) {
			throw new EJBException(e);
		}
	}

	@GET()
	@Path("/parametres")
	@Operation(summary = "Ensemble des paramètres de l'espace client", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response parametreEspaceClient() {
		try {

			TaParamEspaceClientDTO t = null;
			try {
				t = taParamEspaceClientService.findInstanceDTO();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return Response.status(200)
					.entity(t)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}
	
	@GET()
	@Path("/liste-tiers/{id}")
	@Operation(summary = "Liste des comptes tiers lié à cet espace client. La Liaison se fait par l'adresse email.", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response listeTiersEspaceClient(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int idEspaceClient
			) {
		try {
			List<TaTiersDTO> listeDesTiersLieACetEspaceClient = new ArrayList<>();
			List<TaTiers> listeTiers = taTiersService.findByEmail(taEspaceClientService.findById(idEspaceClient).getEmail());
			for (TaTiers taTiers : listeTiers) {
				listeDesTiersLieACetEspaceClient.add(taTiersService.findByIdDTO(taTiers.getIdTiers()));
			}
			return Response.status(200)
					.entity(listeDesTiersLieACetEspaceClient)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GET()
	@Path("/logo-login")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne le logo par défaut sous forme de BLOB",
	tags = {MyApplication.TAG_ESPACE_CLIENT},
	description = "Retourne le logo s'il existe, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pas de logo disponible")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response logoEspaceClient(
			@HeaderParam(X_HEADER_DOSSIER) String dossier
			) {
		try {
			TaParamEspaceClientDTO b =  taParamEspaceClientService.findInstanceDTO();

			BdgProperties bdgProperties = new BdgProperties();
			String localPath = bdgProperties.osTempDirDossier(dossier)+"/"+bdgProperties.tmpFileName(""/*taEdition.getFichierNom()*/);

			if(b.getLogoLogin()!=null) {
				OutputStream  os = new FileOutputStream(localPath); 
				os.write(b.getLogoLogin()); 
				os.close(); 

				File file = new File(localPath);

				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();
			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}


		} catch(EJBException e) {
			throw new EJBException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GET()
	@Path("/background-login")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne l'image de background a afficher sur la page de connexion de l'espace client.", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response backgroundLoginEspaceClient(
			@HeaderParam(X_HEADER_DOSSIER) String dossier
			) {
		try {
			TaParamEspaceClientDTO b = taParamEspaceClientService.findInstanceDTO();

			BdgProperties bdgProperties = new BdgProperties();
			String localPath = bdgProperties.osTempDirDossier(dossier)+"/"+bdgProperties.tmpFileName(""/*taEdition.getFichierNom()*/);

			if(b.getImageBackgroundLogin()!=null) {
				OutputStream  os = new FileOutputStream(localPath); 
				os.write(b.getImageBackgroundLogin()); 
				os.close(); 

				//File file = new File(multitenantProxy.logo(dossier));
				File file = new File(localPath);

				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();
			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}


		} catch(EJBException e) {
			throw new EJBException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GET()
	@Path("/logos")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne les logos par défaut sous forme de BLOB",
	tags = {MyApplication.TAG_ESPACE_CLIENT},
	description = "Retourne le logo s'il existe, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pas de logo disponible")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response logos(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("nomLogo") String nomLogo) {
		try {
			TaParamEspaceClientDTO b = taParamEspaceClientService.findInstanceDTO();

			BdgProperties bdgProperties = new BdgProperties();
			String localPath = bdgProperties.osTempDirDossier(dossier)+"/"+bdgProperties.tmpFileName(""/*taEdition.getFichierNom()*/);

			byte[] logo = null;
			switch (nomLogo) {
			case "header":
				logo = b.getLogoHeader();
				break;
			case "page_simple":
				logo = b.getLogoPagesSimples();
				break;
			case "footer":
				logo = b.getLogoFooter();
				break;
			default:
				break;
			}

			if(logo!=null) {
				OutputStream  os = new FileOutputStream(localPath); 
				os.write(logo); 
				os.close(); 

				//File file = new File(multitenantProxy.logo(dossier));
				File file = new File(localPath);

				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();
			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}

		} catch(EJBException e) {
			throw new EJBException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GET()
	@Path("/fournisseurs")
	@Operation(summary = "Listes des dossiers BDG qui ont le module Espace Client", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response listeFournisseurDTO() {

		String idProduitCompteClient = "fr.legrain.compteclient";
		List<TiersDossierDTO> m = new ArrayList<>();
		
		try {
			//MonCompteWebServiceClientCXF wsMonCompte = new MonCompteWebServiceClientCXF();
			List<TaDossier> ld = wsMonCompte.findListeDossierProduit(idProduitCompteClient);
			//List<TaDossier> ld = wsMonCompte.findListeDossierClient(37);
			for (TaDossier taDossier : ld) {
				initTenant(taDossier.getCode());
				System.out.println("dossier : "+taDossier.getCode());
				//TaInfoEntreprise infosEts = taInfoEntrepriseService.findInstance();
				TaInfoEntreprise infosEts = multitenantProxy.infosMultiTenant(taDossier.getCode());
				TaTiersDTO t =multitenantProxy.infosTiersMultiTenantDTO(taDossier.getCode());
				//m.add(new TiersDossier(taDossier.getCode(), infosEts.getTaTiers()));
				m.add(new TiersDossierDTO(taDossier.getCode(), t));
			}

			return Response.status(200)
					//.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					.entity(m)
					.build();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; 
	}

	public boolean clientExisteChezFournisseur(String codeDossierFournisseur, String codeTiers, String cleLiaisonCompteClient) {
		codeDossierFournisseur = initTenant(codeDossierFournisseur);
		return multitenantProxy.clientExisteChezFournisseur(codeDossierFournisseur, codeTiers, cleLiaisonCompteClient);
	}

	@GET()
	@Path("/infos-tiers")
	@Operation(summary = "Retourne les informations du tiers connecté à l'espace client", tags = {MyApplication.TAG_ESPACE_CLIENT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response infosClientChezFournisseurDTO(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers) {
		TaTiersDTO dto = null;
		try {
			dto = taTiersService.findByCodeDTO(codeTiers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.status(200)
				.entity(dto)
				.build(); 
	}
	
    @POST()
    @Path("/tiers/{idTiers}/adresses")
    @Operation(summary = "Création d'une nouvelle adresse pour ce tiers",
    tags = {MyApplication.TAG_TIERS},
    description = "Création d'une nouvelle adresse pour ce tiers",
    responses = {
	//            @ApiResponse(description = "The pet", content = @Content(
	//                    schema = @Schema(implementation = TaTiersDTO.class)
	//            )),
	//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response postCreateAdresseTiers(
    		@Parameter(required = true, allowEmptyValue = false) @PathParam("idTiers") int idTiers,
    		@Parameter(required = true, allowEmptyValue = false) TaAdresseDTO dto) {
    	
    	TaTiers t = null;
    	TaAdresse a = new TaAdresse();
    	
    	LgrDozerMapper<TaAdresseDTO,TaAdresse> mapperUIToModel  = new LgrDozerMapper<TaAdresseDTO,TaAdresse>();
    	LgrDozerMapper<TaAdresse,TaAdresseDTO> mapperModelToUI  = new LgrDozerMapper<TaAdresse,TaAdresseDTO>();
    	    		
    	try {
    		t = taTiersService.findById(idTiers);

    		mapperUIToModel.map(dto, a);

			TaTAdr taTAdresse;
			if(dto.getCodeTAdr()!=null) {
				taTAdresse = taTAdrService.findByCode(dto.getCodeTAdr());
				a.setTaTAdr(taTAdresse);
			}
			a.setTaTiers(t);
			if(t.getTaAdresse()==null) {
				t.setTaAdresse(a);
			}
			t.addAdresse(a);

    		t = taTiersService.merge(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(200).entity(t).build();
    	
	}
    
    @PUT()
    @Path("/tiers/{idTiers}/adresses/{idAdresse}")
    @Operation(summary = "Mise à jour d'un adresse pour ce tiers",
    tags = {MyApplication.TAG_TIERS},
    description = "Mise à jour d'un adresse pour ce tiers",
    responses = {
	//            @ApiResponse(description = "The pet", content = @Content(
	//                    schema = @Schema(implementation = TaTiersDTO.class)
	//            )),
	//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response putUpdateAdresseTiers(
    		@Parameter(required = true, allowEmptyValue = false) @PathParam("idAdresse") int idAdresse,
    		@Parameter(required = true, allowEmptyValue = false) TaAdresseDTO dto) {
    	
    	TaTiers t = null;
    	TaAdresse a = null;
    	
    	LgrDozerMapper<TaAdresseDTO,TaAdresse> mapperUIToModel  = new LgrDozerMapper<TaAdresseDTO,TaAdresse>();
    	LgrDozerMapper<TaAdresse,TaAdresseDTO> mapperModelToUI  = new LgrDozerMapper<TaAdresse,TaAdresseDTO>();
    	    		
    	try {
    		a = taAdresseService.findById(idAdresse);

    		mapperUIToModel.map(dto, a);
//
//			TaTAdr taTAdresse;
//			if(dto.getCodeTAdr()!=null) {
//				taTAdresse = taTAdrService.findByCode(dto.getCodeTAdr());
//				a.setTaTAdr(taTAdresse);
//			}
//			t.addAdresse(a);

    		a = taAdresseService.merge(a);
//    		t = taTiersService.merge(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(200).entity(a).build();
    	
	}
    
    @PUT()
    @Path("/tiers/finalise-nouveau-tiers/{idTiers}")
    @Operation(summary = "Mise à jour d'un nouveau tiers, tiers de type VISITeur pour lequel seule les informations du compte client sont remplie (nom, prénom, email)",
    tags = {MyApplication.TAG_TIERS},
    description = "Mise à jour d'un nouveau tiers, tiers de type VISITeur pour lequel seule les informations du compte client sont remplie (nom, prénom, email)",
    responses = {
	//            @ApiResponse(description = "The pet", content = @Content(
	//                    schema = @Schema(implementation = TaTiersDTO.class)
	//            )),
	//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response finaliseNouveauTiers(
    		@Parameter(required = true, allowEmptyValue = false) @PathParam("idTiers") int idTiers,
    		@Parameter(required = true, allowEmptyValue = false) TaTiersDTO dto) {
    	
    	/*
    	 * TODO
    	 * TODO pour les modifications d'un tiers "par lui même" via l'espace client par exmple 
    	 * TODO vérifier que l'identité de l'utilisateur qui appelle l'api correspond au bon tiers
    	 * TODO
    	 */
    	
    	TaTiers t = null;
    	TaAdresse a = null;
    	
    	LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel  = new LgrDozerMapper<TaTiersDTO,TaTiers>();
    	LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
    	    		
    	try {
    		t = taTiersService.findById(idTiers);

    		if(dto.getNumeroTelephone()!=null) {
    			TaTelephone tel = new TaTelephone();
    			boolean idTelExists = false;
    			if(t.getTaTelephones()!=null && !t.getTaTelephones().isEmpty()) {
    				//nouveau tiers, donc on peut vider la liste
    				t.setTaTelephone(null);
    				for (TaTelephone te : t.getTaTelephones()) {
						te.setTaTiers(null);
					}
    				t.getTaTelephones().clear();
    			  idTelExists = t.getTaTelephones().stream() .anyMatch(phone -> phone.getNumeroTelephone().equals(tel.getNumeroTelephone()));
    			}
    			
    			if(!idTelExists) {
        			tel.setTaTiers(t);
        			tel.setNumeroTelephone(dto.getNumeroTelephone());
        			if(t.getTaTelephone()==null) {
        				t.setTaTelephone(tel);
        			}
        			
    				t.addTelephone(tel);
    			}
    		}
    		
    		if(dto.getAdresseWeb()!=null) {
    			TaWeb web = new TaWeb();
    			boolean idWebExists = false;
    			if(t.getTaWebs()!=null && !t.getTaWebs().isEmpty()) {
    				//nouveau tiers, donc on peut vider la liste
    				t.setTaWeb(null);
    				for (TaWeb te : t.getTaWebs()) {
						te.setTaTiers(null);
					}
    				t.getTaWebs().clear();
    			  idWebExists = t.getTaWebs().stream() .anyMatch(w -> w.getAdresseWeb().equals(web.getAdresseWeb()));
    			}
    			
    			if(!idWebExists) {
    				web.setTaTiers(t);
        			web.setAdresseWeb(dto.getAdresseWeb());
        			if(t.getTaWeb()==null) {
        				t.setTaWeb(web);
        			}
        			
    				t.addWeb(web);
    			}
    		}
    		
    		if(dto.getNomEntreprise()!=null) {
    			if(t.getTaEntreprise()==null) {
    				t.setTaEntreprise(new TaEntreprise());
    			}
    			t.getTaEntreprise().setNomEntreprise(dto.getNomEntreprise());
    			
    		}
    		
    		if(dto.getSiretCompl()!=null || dto.getTvaIComCompl()!=null) {
    			if(t.getTaCompl()==null) {
    				t.setTaCompl(new TaCompl());
    			}
    			t.getTaCompl().setSiretCompl(dto.getSiretCompl());
    			t.getTaCompl().setTvaIComCompl(dto.getTvaIComCompl());
    		}
    		
    		mapperUIToModel.map(dto, t);



    		t = taTiersService.merge(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(200).entity(a).build();
    	
	}

	public byte[] pdfClient(String codeDossierFournisseur, String codeTiers) {
		codeDossierFournisseur = initTenant(codeDossierFournisseur);
		return multitenantProxy.pdfClient(codeDossierFournisseur, codeTiers);
	}


	@GET()
	@Path("/setup")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Déclanche le téléchargement d'un fichier setup", tags = {MyApplication.TAG_ESPACE_CLIENT, MyApplication.TAG_DIVERS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response setup(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("fichier") String fichier) {

		//File file = new File(multitenantProxy.pdfFactureFile(codeDossierFournisseur, codeFactureChezCeFournisseur));
		System.out.println("CompteClientFinalRestService.setup() "+fichier);
		//   $(curl -u "login:password" -s 'http://217.70.189.183:8080/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,":",//crumb)')
		SetupLegrain s = null;
		String fichierTemp = null;

		try {
			String protocol = "https";
			String host = paramBdg.getTaParametre().getJenkinsApiHost();
			int port = 443;
			String usernName = paramBdg.getTaParametre().getJenkinsLogin();
			String password = paramBdg.getTaParametre().getJenkinsPassword();

			//	        DefaultHttpClient httpclient = new DefaultHttpClient();
			//	        httpclient.getCredentialsProvider().setCredentials(
			//	                new AuthScope(host, port), 
			//	                new UsernamePasswordCredentials(usernName, password));

			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			} };

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, null);
			// Create an ssl socket factory with our all-trusting manager
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			});
			// be authentic
			//			Authenticator.setDefault(new Authenticator() {
			//			    @Override
			//			    protected PasswordAuthentication getPasswordAuthentication() {
			//			        return new PasswordAuthentication(usernName, password.toCharArray());
			//			    }
			//			});

			String jenkinsUrl = protocol + "://" + host + ":" + port + "/";

			String METHOD = "GET";
			URL QUERY  = new URL(jenkinsUrl + "crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)");
			String BODY   = "";

			HttpURLConnection req = (HttpURLConnection)QUERY.openConnection();
			req.setRequestMethod(METHOD);
			//req.setRequestProperty("Content-Type",      "application/json");
			req.setRequestProperty("Authorization", "Basic "+paramBdg.getTaParametre().getJenkinsApiKey());
			//req.setRequestProperty("Authorization", "Basic login:password");

			if(!BODY.isEmpty()) {
				req.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(req.getOutputStream());
				wr.writeBytes(BODY);
				wr.flush();
				wr.close();
			}

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

			String crumb = response.toString().split(":")[1];
			System.out.println(response.toString());
			crumb = response.toString();
			System.out.println(crumb);

			s = new SetupLegrain(null, null, null);
			s.init();
			s = s.m.get(fichier);

			SchemaResolver sr = new SchemaResolver();
			//String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("setup.exe");

			//downloadFile(jenkinsUrl+"job/setup_bdg_general_N1_product/lastSuccessfulBuild/artifact/setup_bdg_general_n1.exe","/tmp/",crumb);
			//downloadFile(jenkinsUrl+s.finUrl(),"/tmp/",crumb);
			fichierTemp = downloadFile(jenkinsUrl+s.finUrl(),bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier()),crumb);

		} catch(Exception e) {
			e.printStackTrace();
		}

		File file = new File(fichierTemp);
		ResponseBuilder response = Response.ok((Object) file,MediaType.APPLICATION_OCTET_STREAM);
		//response.header("Content-Disposition", "attachment; filename=new-android-book.exe");
		response.header("Content-Disposition","inline; filename="+s.nomArtifact);
		//response.header("Content-Disposition",null);
		//response.header("Content-Type","application/x-msdownload");
		response.header("Content-Length",new File(fichierTemp).length());
		//response.header("fileName","setup_bdg_general_n1.exe");
		return response.build();
	}

	/**
	 * Downloads a file from a URL
	 * @param fileURL HTTP URL of the file to be downloaded
	 * @param saveDir path of the directory to save the file
	 * @throws IOException
	 */
	public  String downloadFile(String fileURL, String saveDir, String crumb)
			throws IOException {

		String fileName = "";
		URL url = new URL(fileURL);
		String saveFilePath = null;

		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestProperty(crumb.split(":")[0],crumb.split(":")[1]);
		httpConn.setRequestProperty("Authorization", "Basic "+paramBdg.getTaParametre().getJenkinsApiKey()); //"nicolas:la_cle_api" encodé en base64

		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {

			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10,
							disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
						fileURL.length());
			}

			fileName = bdgProperties.tmpFileName(fileName);

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			saveFilePath = saveDir + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();

			System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
		return saveFilePath;
	}

	@GET()
	@Path("/nom-fichier")
	@Operation(summary = "Retourne le nom d'un fichier téléchargeable dans l'espace client (setup)", tags = {MyApplication.TAG_ESPACE_CLIENT, MyApplication.TAG_DIVERS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response nomFichier(@Parameter(required = true, allowEmptyValue = false) @QueryParam("fichier") String fichier) {

		try {

			SetupLegrain s = new SetupLegrain(null, null, null);
			s.init();
			s = s.m.get(fichier);

			return Response.status(200)
					.entity("{\"b\":\""+s.nomArtifact+"\"}")
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	class SetupLegrain {
		public String codeArticle;
		public String nomJob;
		public String nomArtifact;
		//public String numeroJob = "lastSuccessfulBuild";
		public String numeroJob = "PRODUCTION"; //nom de la promotion

		public Map<String,SetupLegrain> m = null;

		public String finUrl() {
			return "job/"+nomJob+"/"+numeroJob+"/artifact/"+nomArtifact;
		}

		public SetupLegrain(String codeArticle, String nomJob, String nomArtifact) {
			this.codeArticle = codeArticle;
			this.nomJob = nomJob;
			this.nomArtifact = nomArtifact;
		}

		public Map<String,SetupLegrain> init() {
			if(m==null || m.isEmpty()) {
				m = new HashMap<>();

				/*
				 *MAI-BDG-AGRI-LGR100	MAI-BDG-AGRI-LGR101
				 *MAI-BDG-AGRI-LGR200	Maintenance Bureau de Gestion Classique Agriculture LGR200
				 *MAI-BDG-AGRI-LGR300	Maintenance Bureau de Gestion Classique Agriculture LGR300
				 *MAI-BDG-AGRI-LGR400	Maintenance Bureau de Gestion Classique Agriculture LGR400
				 *MAI-BDG-ACT-LGR300	Maintenance Bureau de Gestion Classique Artisans Commerçants TPE LGR300
				 *MAI-BDG-ACT-LGR400	Maintenance Bureau de Gestion Classique Artisans Commerçants TPE LGR400
				 *MAI-BDG-ACT-LGR100	Maintenance Bureau de Gestion Classique Artisans Commerçant TPE LGR100
				 *MAI-BDG-ACT-LGR200	Maintenance Bureau de Gestion Classique Artisans Commerçant TPE LGR200
				 *MAI-BDG-VITI-LGR300	Maintenance Bureau de Gestion Classique Viticulture LGR300
				 *MAI-BDG-VITI-LGR400	Maintenance Bureau de Gestion Classique Viticulture LGR400
				 *MAI-EPI-AGRI-LGR100	Maintenance Epicéa Agriculture LGR100
				 *MAI-EPI-AGRI-LGR200	Maintenance Epicéa Agriculture LGR200
				 *MAI-EPI-AGRI-LGR300	Maintenance Epicéa Agriculture LGR300
				 *MAI-EPI-AGRI-LGR400	Maintenance Epicéa Agriculture LGR400
				 *MAI-EPI-ACT-LGR100	Maintenance Epicéa Artisans Commerçant TPE LGR100
				 *MAI-EPI-ACT-LGR200	Maintenance Epicéa Artisans Commerçant TPE LGR200
				 *MAI-EPI-ACT-LGR300	Maintenance Epicéa Artisans Commerçant TPE LGR300
				 *MAI-EPI-ACT-LGR400	Maintenance Epicéa Artisans Commerçant TPE LGR400
				 *MAI-EPI-ASSO-LGR100	Maintenance Epicéa Association LGR100
				 *MAI-EPI-ASSO-LGR300	Maintenance Epicéa Association LGR300
				 *MAI-EPI-ASSO-LGR400	Maintenance Epicéa Association LGR400
				 *MAI-EPIG-AGRI-LGR400	Maintenance Epicéa Groupe Agriculture LGR400
				 *MAI-EPIG-ACT-LGR400	Maintenance Epicéa Groupe Artisans Commerçant TPE LGR400
				 *MAI-EPI-COG-LGR100	Maintenance Epicéa version COGERE LGR100
				 *MAI-EPI-COG-LGR400	Maintenance Epicéa version COGERE LGR400
				PACK-COMPTA-SERENITE	Pack Compta Sérénité
				 */

				//http://217.70.189.183:8080/job/setup_bdg_contact_N1_product/lastSuccessfulBuild/artifact/setup_bdg_contact_n1.exe
				//    			SetupLegrain setup_bdg_contact_N1_product = new SetupLegrain("XXXXXXXXX", "setup_bdg_contact_N1_product", "setup_bdg_contact_N1_product");
				//    			m.put("XXXXXXXXX", setup_bdg_contact_N1_product);

				//http://217.70.189.183:8080/job/setup_bdg_edu_N3_product/lastSuccessfulBuild/artifact/setup_bdg_edu_n3.exe
				//    			SetupLegrain setup_bdg_edu_N3_product = new SetupLegrain("XXXXXXXXX", "setup_bdg_edu_N3_product", "etup_bdg_edu_n3.exe");
				//    			m.put("XXXXXXXXX", setup_bdg_edu_N3_product);

				//http://217.70.189.183:8080/job/setup_bdg_general_demo_product/lastSuccessfulBuild/artifact/setup_bdg_general_demo.exe
				//    			SetupLegrain setup_bdg_general_demo_product = new SetupLegrain("XXXXXXXXX", "setup_bdg_general_demo_product", "setup_bdg_general_demo.exe");
				//    			m.put("XXXXXXXXX", setup_bdg_general_demo_product);

				//http://217.70.189.183:8080/job/setup_bdg_general_N0_product/lastSuccessfulBuild/artifact/setup_bdg_general_n0.exe
				//    			SetupLegrain setup_bdg_general_N0_product = new SetupLegrain("XXXXXXXXX", "setup_bdg_general_N0_product", "setup_bdg_general_n0.exe");
				//    			m.put("XXXXXXXXX", setup_bdg_general_N0_product);

				// http://217.70.189.183:8080/job/setup_bdg_general_N1_product/lastSuccessfulBuild/artifact/setup_bdg_general_n1.exe
				SetupLegrain setup_bdg_general_N1_product = new SetupLegrain("MAI-BDG-ACT-LGR100", "setup_bdg_general_N1_product", "setup_bdg_general_n1.exe");
				m.put("MAI-BDG-ACT-LGR100", setup_bdg_general_N1_product);
				SetupLegrain setup_bdg_agri_N1_product = new SetupLegrain("MAI-BDG-AGRI-LGR100", "setup_bdg_general_N1_product", "setup_bdg_general_n1.exe");
				m.put("MAI-BDG-AGRI-LGR100", setup_bdg_agri_N1_product);

				//http://217.70.189.183:8080/job/setup_bdg_general_N2_product/lastSuccessfulBuild/artifact/setup_bdg_general_n2.exe
				SetupLegrain setup_bdg_general_N2_product = new SetupLegrain("MAI-BDG-ACT-LGR200", "setup_bdg_general_N2_product", "setup_bdg_general_n2.exe");
				m.put("MAI-BDG-ACT-LGR200", setup_bdg_general_N2_product);
				SetupLegrain setup_bdg_agri_N2_product = new SetupLegrain("MAI-BDG-AGRI-LGR200", "setup_bdg_general_N2_product", "setup_bdg_general_n2.exe");
				m.put("MAI-BDG-AGRI-LGR200", setup_bdg_agri_N2_product);

				//http://217.70.189.183:8080/job/setup_bdg_general_N3_product/lastSuccessfulBuild/artifact/setup_bdg_general_n3.exe
				SetupLegrain setup_bdg_general_N3_product = new SetupLegrain("MAI-BDG-ACT-LGR300", "setup_bdg_general_N3_product", "setup_bdg_general_n3.exe");
				m.put("MAI-BDG-ACT-LGR300", setup_bdg_general_N3_product);
				SetupLegrain setup_bdg_agri_N3_product = new SetupLegrain("MAI-BDG-AGRI-LGR300", "setup_bdg_general_N3_product", "setup_bdg_general_n3.exe");
				m.put("MAI-BDG-AGRI-LGR300", setup_bdg_agri_N3_product);

				//http://217.70.189.183:8080/job/setup_bdg_general_N4_product/lastSuccessfulBuild/artifact/setup_bdg_general_n4.exe
				SetupLegrain setup_bdg_general_N4_product = new SetupLegrain("MAI-BDG-ACT-LGR400", "setup_bdg_general_N4_product", "setup_bdg_general_n4.exe");
				m.put("MAI-BDG-ACT-LGR400", setup_bdg_general_N4_product);
				SetupLegrain setup_bdg_agri_N4_product = new SetupLegrain("MAI-BDG-AGRI-LGR400", "setup_bdg_general_N4_product", "setup_bdg_general_n4.exe");
				m.put("MAI-BDG-AGRI-LGR400", setup_bdg_agri_N4_product);

				//http://217.70.189.183:8080/job/setup_bdg_viti_N3_product/lastSuccessfulBuild/artifact/setup_bdg_viti_n3.exe
				SetupLegrain setup_bdg_viti_N3_product = new SetupLegrain("MAI-BDG-VITI-LGR300", "setup_bdg_viti_N3_product", "setup_bdg_viti_n3.exe");
				m.put("MAI-BDG-VITI-LGR300", setup_bdg_viti_N3_product);

				//http://217.70.189.183:8080/job/setup_bdg_viti_N4_product/lastSuccessfulBuild/artifact/setup_bdg_viti_n4.exe
				SetupLegrain setup_bdg_viti_N4_product = new SetupLegrain("MAI-BDG-VITI-LGR400", "setup_bdg_viti_N4_product", "setup_bdg_viti_n4.exe");
				m.put("MAI-BDG-VITI-LGR400", setup_bdg_viti_N4_product);

				//http://217.70.189.183:8080/job/setup_epicea_Asso_N1/lastSuccessfulBuild/artifact/setup_epi_Asso_N1.exe
				SetupLegrain setup_epicea_Asso_N1 = new SetupLegrain("MAI-EPI-ASSO-LGR100", "setup_epicea_Asso_N1", "setup_epi_Asso_N1.exe");
				m.put("MAI-EPI-ASSO-LGR100", setup_epicea_Asso_N1);

				//http://217.70.189.183:8080/job/setup_epicea_Asso_N3/lastSuccessfulBuild/artifact/setup_epi_Asso_N3.exe
				SetupLegrain setup_epicea_Asso_N3 = new SetupLegrain("MAI-EPI-ASSO-LGR300", "setup_epicea_Asso_N3", "setup_epi_Asso_N3.exe");
				m.put("MAI-EPI-ASSO-LGR300", setup_epicea_Asso_N3);

				//http://217.70.189.183:8080/job/setup_epicea_Asso_N4/lastSuccessfulBuild/artifact/setup_epi_Asso_N4.exe
				SetupLegrain setup_epicea_Asso_N4 = new SetupLegrain("MAI-EPI-ASSO-LGR400", "setup_epicea_Asso_N4", "setup_epi_Asso_N4.exe");
				m.put("MAI-EPI-ASSO-LGR400", setup_epicea_Asso_N4);

				//http://217.70.189.183:8080/job/setup_epicea_Cogere_N1/lastSuccessfulBuild/artifact/setup_epi_Cogere_N1.exe
				SetupLegrain setup_epicea_Cogere_N1 = new SetupLegrain("MAI-EPI-COG-LGR100", "setup_epicea_Cogere_N1", "setup_epi_Cogere_N1.exe");
				m.put("MAI-EPI-COG-LGR100", setup_epicea_Cogere_N1);

				//http://217.70.189.183:8080/job/setup_epicea_Cogere_N4/lastSuccessfulBuild/artifact/setup_epi_Cogere_N4.exe
				SetupLegrain setup_epicea_Cogere_N4 = new SetupLegrain("MAI-EPI-COG-LGR400", "setup_epicea_Cogere_N4", "setup_epi_Cogere_N4.exe");
				m.put("MAI-EPI-COG-LGR400", setup_epicea_Cogere_N4);

				//http://217.70.189.183:8080/job/setup_epicea_Demo_N4/lastSuccessfulBuild/artifact/setup_epi_Demo_N4.exe
				//    			SetupLegrain setup_epicea_Demo_N4 = new SetupLegrain("XXXXXXXXX", "setup_epicea_Demo_N4", "setup_epi_Demo_N4.exe");
				//    			m.put("XXXXXXXXX", setup_epicea_Demo_N4);

				//http://217.70.189.183:8080/job/setup_epicea_Educ_N4/lastSuccessfulBuild/artifact/setup_epi_Educ_N4.exe
				//    			SetupLegrain setup_epicea_Educ_N4 = new SetupLegrain("XXXXXXXXX", "setup_epicea_Educ_N4", "setup_epi_Educ_N4.exe");
				//    			m.put("XXXXXXXXX", setup_epicea_Educ_N4);

				//http://217.70.189.183:8080/job/setup_epicea_Normal_N1/lastSuccessfulBuild/artifact/setup_epi_Normal_N1.exe
				SetupLegrain setup_epicea_Normal_N1 = new SetupLegrain("MAI-EPI-ACT-LGR100", "setup_epicea_Normal_N1", "setup_epi_Normal_N1.exe");
				m.put("MAI-EPI-ACT-LGR100", setup_epicea_Normal_N1);
				SetupLegrain setup_epicea_agri_N1 = new SetupLegrain("MAI-EPI-AGRI-LGR100", "setup_epicea_Normal_N1", "setup_epi_Normal_N1.exe");
				m.put("MAI-EPI-AGRI-LGR100", setup_epicea_agri_N1);

				//http://217.70.189.183:8080/job/setup_epicea_Normal_N2/lastSuccessfulBuild/artifact/setup_epi_Normal_N2.exe
				SetupLegrain setup_epicea_Normal_N2 = new SetupLegrain("MAI-EPI-ACT-LGR200", "setup_epicea_Normal_N2", "setup_epi_Normal_N2.exe");
				m.put("MAI-EPI-ACT-LGR200", setup_epicea_Normal_N2);
				SetupLegrain setup_epicea_agri_N2 = new SetupLegrain("MAI-EPI-AGRI-LGR200", "setup_epicea_Normal_N2", "setup_epi_Normal_N2.exe");
				m.put("MAI-EPI-AGRI-LGR200", setup_epicea_agri_N2);

				//http://217.70.189.183:8080/job/setup_epicea_Normal_N3/lastSuccessfulBuild/artifact/setup_epi_Normal_N3.exe
				SetupLegrain setup_epicea_Normal_N3 = new SetupLegrain("MAI-EPI-ACT-LGR300", "setup_epicea_Normal_N3", "setup_epi_Normal_N3.exe");
				m.put("MAI-EPI-ACT-LGR300", setup_epicea_Normal_N3);
				SetupLegrain setup_epicea_agri_N3 = new SetupLegrain("MAI-EPI-AGRI-LGR300", "setup_epicea_Normal_N3", "setup_epi_Normal_N3.exe");
				m.put("MAI-EPI-AGRI-LGR300", setup_epicea_agri_N3);

				//http://217.70.189.183:8080/job/setup_epicea_Normal_N4/lastSuccessfulBuild/artifact/setup_epi_Normal_N4.exe
				SetupLegrain setup_epicea_Normal_N4 = new SetupLegrain("MAI-EPI-ACT-LGR400", "setup_epicea_Normal_N4", "setup_epi_Normal_N4.exe");
				m.put("MAI-EPI-ACT-LGR400", setup_epicea_Normal_N4);
				SetupLegrain setup_epicea_agri_N4 = new SetupLegrain("MAI-EPI-AGRI-LGR400", "setup_epicea_Normal_N4", "setup_epi_Normal_N4.exe");
				m.put("MAI-EPI-AGRI-LGR400", setup_epicea_agri_N4);
				SetupLegrain setup_epicea_agri_grp_N4 = new SetupLegrain("MAI-EPIG-AGRI-LGR400", "setup_epicea_Normal_N4", "setup_epi_Normal_N4.exe");
				m.put("MAI-EPIG-AGRI-LGR400", setup_epicea_agri_grp_N4);
				SetupLegrain setup_epicea_normal_grp_N4 = new SetupLegrain("MAI-EPIG-ACT-LGR400", "setup_epicea_Normal_N4", "setup_epi_Normal_N4.exe");
				m.put("MAI-EPIG-ACT-LGR400", setup_epicea_normal_grp_N4);

				SetupLegrain setup_epicea_compte_serenite_N4 = new SetupLegrain("PACK-COMPTA-SERENITE", "setup_epicea_Normal_N4", "setup_epi_Normal_N4.exe");
				m.put("PACK-COMPTA-SERENITE", setup_epicea_Normal_N4);

			}
			return m;

		}
	}

}
