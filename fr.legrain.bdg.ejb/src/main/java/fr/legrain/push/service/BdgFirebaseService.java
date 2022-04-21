package fr.legrain.push.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FcmOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.login.service.remote.ITaUtilisateurLoginServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.droits.service.TaUtilisateurService;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaTerminalMobile;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@Interceptors(ServerTenantInterceptor.class)
public class BdgFirebaseService {
	
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	private /*final*/ String NOM_PROJET_CONSOLE_GOOGLE = null; 
	
	public BdgFirebaseService() {
		
		
		try {
			taParametreService.initParamBdg();
			NOM_PROJET_CONSOLE_GOOGLE = paramBdg.getTaParametre().getNomProjetGoogleConsole();
			String nomFichierCleJsonGoogleConsole = paramBdg.getTaParametre().getNomFichierCleJsonGoogleConsole();
			/*
			 * D'après la documentation il est fortement préférable d'utiliser la variable d'environement 
			 * GOOGLE_APPLICATION_CREDENTIALS pour indiquer le chemin de ce fichier
			 */
			String cle = nomFichierCleJsonGoogleConsole;
			
			BdgProperties bdgProperties = new BdgProperties();
			InputStream is = bdgProperties.getConfigFileInputStream(cle);
					
//			InputStream is = this.getClass().getResourceAsStream(cle);
			
			
			File f2 = new File("/tmp/cle.json");
			Files.copy(this.getClass().getResourceAsStream(cle), f2.toPath(),StandardCopyOption.REPLACE_EXISTING);
			
			FileInputStream serviceAccount = new FileInputStream(f2);
	
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					//.setCredentials(GoogleCredentials.fromStream(is))
					.setDatabaseUrl("https://"+NOM_PROJET_CONSOLE_GOOGLE+".firebaseio.com")
					.build();
	
			if(FirebaseApp.getApps()==null || FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			} //else appli deja initialisée
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void envoyerMessagePushUtilisateur(String utilisateur, String titre, String contenu) {
		try {
			//TODO récupérer le token du terminal ciblé par le message/notification
			
			/*
			 * TODO A gérer
			 * meme utilisateur connecter sur plusieurs terminal ?
			 * si utilisateur deconnecte => supprimer le token sur son compte
			 * à la connection, lui réaffecté le token
			 * introduire une vérification de l'utilisateur connecter avant d'afficher la notification sur le terminal
			 * 
			 * Tester l'envoi de message à tous les utilisateur d'un dossier, à tous les utilisateur d'un groupe, à un topic, ...
			 */
			// This registration token comes from the client FCM SDKs.
			String registrationToken = "YOUR_REGISTRATION_TOKEN";
			//registrationToken = "cezWwnfZQrA:APA91bEBytQ3oYCN3s4Ye0R13MHzzW9YMGi0WOCJN_UzMdLiUO_PkQG9Kx_8ce0EtCJghrU3WNm48HYjNqHheow4-f1sgoS3Sgm4KdXF3f0ssi5P4nSGMc5LPiMwixKtZ8_H4gOt5Ye3";
			TaUtilisateur taUtilisateur = null;
			taUtilisateur = taUtilisateurService.findByCode(utilisateur);
			if(taUtilisateur!=null) {
				registrationToken = taUtilisateurService.findByCode(utilisateur).getAndroidRegistrationToken();
	
				if(registrationToken!=null && !registrationToken.equals("")) {
					// See documentation on defining a message payload.
					Message message = Message.builder()
//					    .putData("titre", "Information")
//					    .putData("contenu", "Test de notification BDG")
						.putData("titre", titre)
					    .putData("contenu", contenu)
					    .setToken(registrationToken)
					    .build();
		
					// Send a message to the device corresponding to the provided
					// registration token.
					String response = FirebaseMessaging.getInstance().send(message);
					// Response is a message ID string.
					System.out.println("Successfully sent message: " + response);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void envoyerMessagePushTiersEspaceClient(String tiersAdresseEmailEspaceClient, String titre, String resume, 
			String contenu, String url, String image) {
		try {
			//TODO récupérer le token du terminal ciblé par le message/notification
			
			/*
			 * TODO A gérer
			 * meme utilisateur connecter sur plusieurs terminal ? => fait
			 * si utilisateur deconnecte => supprimer le token sur son compte
			 * à la connection, lui réaffecté le token
			 * introduire une vérification de l'utilisateur connecter avant d'afficher la notification sur le terminal
			 * 
			 * Tester l'envoi de message à tous les utilisateur d'un dossier, à tous les utilisateur d'un groupe, à un topic, ...
			 */
			// This registration token comes from the client FCM SDKs.
			String registrationToken = "YOUR_REGISTRATION_TOKEN";
			//registrationToken = "cezWwnfZQrA:APA91bEBytQ3oYCN3s4Ye0R13MHzzW9YMGi0WOCJN_UzMdLiUO_PkQG9Kx_8ce0EtCJghrU3WNm48HYjNqHheow4-f1sgoS3Sgm4KdXF3f0ssi5P4nSGMc5LPiMwixKtZ8_H4gOt5Ye3";
			
			List<TaTiers> listeTiers = taTiersService.findByEmail(tiersAdresseEmailEspaceClient);
			if(listeTiers!=null && !listeTiers.isEmpty()) {
				TaEspaceClient taEspaceClient = taEspaceClientService.findByCodeTiers(listeTiers.get(0).getCodeTiers());
				
				
				if(taEspaceClient!=null) {
					//registrationToken = taEspaceClient.getAndroidRegistrationToken();
					Set<TaTerminalMobile> listeTerminal = taEspaceClient.getTaTerminalMobiles();
		
					if(listeTerminal!=null && !listeTerminal.isEmpty()) {
						
						FcmOptions fcmOption = FcmOptions.builder()
								.setAnalyticsLabel("BDG_TEST")
								.build();
						
						for (TaTerminalMobile taTerminalMobile : listeTerminal) {
							
							
							
							registrationToken = taTerminalMobile.getAndroidRegistrationToken();
							if(registrationToken!=null && !registrationToken.equals("")) {
								
//								Notification notif = Notification.builder()
//										.setTitle(titre)
//										.setBody(resume)
//										.setImage(image)
//										.build();
//								Message.Builder b = Message.builder()
//										.setFcmOptions(fcmOption)
//										.setNotification(notif);
																	
								// See documentation on defining a message payload.
								Message.Builder b = Message.builder()
								    .putData("titre", titre)
								    .putData("resume", resume)
								    .setFcmOptions(fcmOption)
								    .setToken(registrationToken);					    
								
								if(contenu!=null && contenu.trim().equals("")) {
									contenu=null;
								}
								if(contenu!=null) b.putData("contenu", contenu);
								
								if(url!=null && url.trim().equals("")) {
									url=null;
								}
								if(url!=null) b.putData("url", url);
								
								if(image!=null && image.trim().equals("")) {
									image=null;
								}
								if(image!=null) b.putData("image", image);
								
//								byte[] aByteArray = taInfoEntrepriseService.findInstance().getTaTiers().getBlobLogo();
//								String image64 = Base64.getEncoder().encodeToString(aByteArray);
//								b.putData("image", image64);
								
								Message message = b.build();
					
								// Send a message to the device corresponding to the provided
								// registration token.
								String response = FirebaseMessaging.getInstance().send(message);
								// Response is a message ID string.
								System.out.println("Successfully sent message: " + response);
							}
						}
					}
					
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testNotifAndroidUtilisateur(TaUtilisateurDTO u, String titre, String contenu) {
//		TaUtilisateur taUtilisateur = null;
//		try {
//			taUtilisateur = taUtilisateurService.findByCode(u.getEmail());
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String utilisateur = u.getEmail();
		envoyerMessagePushUtilisateur(utilisateur, titre, contenu);
	}
	
	public void testNotifAndroidUtilisateurTous(String titre, String contenu) {
//		String utilisateur = "contact@ltgascons.com";
//		envoyerMessagePushUtilisateur(utilisateur);
	}
	
	public void testNotifAndroidTiersEspaceClient(TaTiersDTO t, String titre, String resume, String contenu, String url, String image) {
		TaTiers taTiers = null;
		try {
			taTiers = taTiersService.findByCode(t.getCodeTiers());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String client = taTiers.getTaEmail().getAdresseEmail();
		envoyerMessagePushTiersEspaceClient(client, titre, resume, contenu, url, image);
	}
	
	public void testNotifAndroidTiersEspaceClientTous(String titre, String contenu) {
//		String client = "nicolas@legrain.fr";
//		envoyerMessagePushTiersEspaceClient(client);
	}
}
