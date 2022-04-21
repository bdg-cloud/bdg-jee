package fr.legrain.servicewebexterne.service.divers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.legrain.autorisation.xml.ListeModules;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaSynchroServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITransactionnalMergeLigneServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaMensuraServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.servicewebexterne.service.divers.mensuraxml.BimetreMensura;
import fr.legrain.servicewebexterne.service.divers.mensuraxml.DocumentDevisMensura;
import fr.legrain.servicewebexterne.service.divers.mensuraxml.HeaderMensura;
import fr.legrain.servicewebexterne.service.divers.mensuraxml.LigneDevisMensura;
import fr.legrain.servicewebexterne.service.divers.mensuraxml.TiersMensura;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
//@TransactionManagement(TransactionManagementType.BEAN)
public class TaMensuraServiceWebExterne implements ITaMensuraServiceWebExterneServiceRemote{
	
	@EJB private ITaLigneServiceWebExterneServiceRemote ligneServiceWebExterneService;
	@EJB private ITaCompteServiceWebExterneServiceRemote compteServiceWebExterneService;
	@EJB private ITaServiceWebExterneServiceRemote serviceWebExterneService;
	@EJB private ITaLiaisonServiceExterneServiceRemote liaisonService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaTTiersServiceRemote taTTiersService;
	@EJB private ITaEmailServiceRemote mailService;
	@EJB private ITaTelephoneServiceRemote telService;
	@EJB private ITaAdresseServiceRemote adresseService;
	@EJB private ITaTAdrServiceRemote taTAdrService;
	@EJB private ITaSynchroServiceExterneServiceRemote synchroService;
	
	@EJB private ITransactionnalMergeLigneServiceExterneServiceRemote transactionalService;
	
	//@Resource private UserTransaction tx; 
	
	private TaCompteServiceWebExterne compteServiceWeb;
	private TaServiceWebExterne serviceWeb;
	protected BdgProperties bdgProperties = null;
	@Inject protected	TenantInfo tenantInfo;
	

	
//  pour prod
//	String username = "ftp-t2g";
//	String password = "PS8tB398hmijYT9";
	String username = "";
	String password = "";
//	String remoteHost = "sftp.shippingbo.com";
//	String remoteFile = "/orders/";
	
//  pour test
//	String username = "40713";
//	String password = "aech7EeL$$";
//	String remoteHost = "sftp.sd5.gpaas.net";
//	String remoteFile = "lamp0/tmp/shippingbo/orders/";
	String remoteHost = "";
	String remoteFile = "";
	
	
	String dossierTravail = "/mensura/devis/";
	
	String codeService = "MENSURA";
	
	
	String localDir = "";
	
	@PostConstruct
	public void postConstruct(){
		try {
			serviceWeb = serviceWebExterneService.findByCode(codeService);
			initCompteServiceWeb();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initCompteServiceWeb() throws FinderException {
		compteServiceWeb =  compteServiceWebExterneService.findCompteDefautPourFournisseurService(serviceWeb.getCodeServiceWebExterne());
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		compteServiceWeb = util.decrypter(compteServiceWeb);
		remoteHost = compteServiceWeb.getValeur1();
		remoteFile = compteServiceWeb.getValeur2();
	}
	
	
	//Cette méthode sera surement appellée par un CRON toutes les heures
	public Integer recupereFichier() throws EJBException, IOException, FinderException  {
		//je récupère le dernier fichier JSON en SFTP grâce a une libraire JAVA
		// je stocke le fichier dans le tempDir
		return	downloadFileSFTP();		
		
	}
	
	private String checkString(Object string) {
		string = String.valueOf(string);
		if(string.equals("null")) {
			string = "";
		}
		return (String) string;
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	

	
	
	public BimetreMensura recupLignesXml(File f) throws IOException{
		BimetreMensura map = new BimetreMensura();
		InputStream stream = new FileInputStream(f);
		Unmarshaller unmarshaller;
		try {
			unmarshaller = JAXBContext.newInstance(BimetreMensura.class).createUnmarshaller();
			map = (BimetreMensura) unmarshaller.unmarshal(stream);
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
		stream.close();
		return map;
	}
	
	public List<LigneDevisMensura> recupLigneAvecCodeArticle(List<LigneDevisMensura> liste){
		List<LigneDevisMensura> listeLigneDevisAvecCodeArticle = new ArrayList<LigneDevisMensura>();
		
		for (LigneDevisMensura ligne : liste) {
			if(ligne.getCODEARTICLE() != null && !ligne.getCODEARTICLE().isEmpty()) {
				listeLigneDevisAvecCodeArticle.add(ligne);
			}else {
				if(ligne.getLIGNE() != null && !ligne.getLIGNE().isEmpty()){
					listeLigneDevisAvecCodeArticle.addAll(recupLigneAvecCodeArticle(ligne.getLIGNE()));
				}
			}
		}
		
		return listeLigneDevisAvecCodeArticle;
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public Integer enregistreLigneDepuisFichier() throws Exception{
		//je récupère le fichier localement dans le tempDir
		String localPath;
		bdgProperties = new BdgProperties();
		localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId()+dossierTravail);
		Integer nbLignes = 0;
		File dir = new File(localPath);
		File[] directoryListing = dir.listFiles();
		
		List<String> mapErreurFichiers = new ArrayList<String>();
//		Map<String,String> mapErreurLignes = new HashMap<String, String>();
		
		 if (directoryListing != null) {
			 List<TaLiaisonServiceExterne> listeLiaison = new ArrayList<TaLiaisonServiceExterne>();
			 //je parcours tous les fichiers JSON qu'il y a dans /orders dans le dossier temporaire
			    for (File f : directoryListing) {
					
					//File f = new File(localPath);
					if(f != null && f.exists()) {
						
						List<TaLigneServiceWebExterne> listeLignesAMerge = new ArrayList<TaLigneServiceWebExterne>();
						JSONArray jsonArrayOrders;
						String content;
						try {
							BimetreMensura docDevis = recupLignesXml(f);
							HeaderMensura headerMensura = docDevis.getListeDocumentMensura().getDOCUMENT().getHEADER();
							TiersMensura tiersMensura = headerMensura.getTIERS();
							
							
							List<LigneDevisMensura> listeLigneDevis = docDevis.getListeDocumentMensura().getDOCUMENT().getBODY().getLIGNE();
							List<LigneDevisMensura> listeLigneDevisAvecCodeArticle = recupLigneAvecCodeArticle(listeLigneDevis);
							
							
							if(listeLigneDevisAvecCodeArticle != null && !listeLigneDevisAvecCodeArticle.isEmpty()) {
								
								for (LigneDevisMensura ligneDevis : listeLigneDevisAvecCodeArticle) {
									//JSONObject jsonOrderItem = arrayOrderItems.getJSONObject(i2);
						    		 TaLigneServiceWebExterne ligne = new TaLigneServiceWebExterne();
						    		 //concerne la ligne de commande en général
						    		 ligne.setIdCommandeExterne(checkString(headerMensura.getCODEAFFAIRE()));
						    		 ligne.setRefCommandeExterne(checkString(headerMensura.getCODEAFFAIRE()));
							    	 ligne.setLibelle("Ligne n° "+String.valueOf(ligneDevis.getNUMEROTATION())+" "+checkString(ligneDevis.getLIBELLE()));
							    	 
							    	 SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
							    	 Date dateCreation = formatter.parse(checkString(headerMensura.getDATEDOC()));
							    	 ligne.setDateCreationExterne(dateCreation);
							    	// ligne.setDateUpdateExterne(checkString());
							    	// ligne.setJsonInitial(jsonObject.toString());
							    	 ligne.setTaCompteServiceWebExterne(compteServiceWeb);
							    	 
							    	 ligne.setNomTiers(checkString(tiersMensura.getNOM()));
							    	 //ligne.setPrenomTiers(checkString(tiersMensura.getNOM()));
							    	 ligne.setRefTiers(checkString(tiersMensura.getCODECLIENT()));
							    	// ligne.setMailTiers(checkString(shippingAdressJson.get("email")));
							    	 
							    	 //concerne les produits
							    	 ligne.setRefArticle(checkString(ligneDevis.getCODEARTICLE()));
							    	 ligne.setNomArticle(checkString(ligneDevis.getLIBELLE()));
							    	 ligne.setQteArticle(Integer.parseInt(ligneDevis.getQUANTITETOT()));
							    	 
							    	// Integer mttc = 0;
							    	 Double mthtDouble = Double.parseDouble(ligneDevis.getMONTANTHT());
//							    	 Double mttcDouble = null;
//							    	 if(mttc != null && mttc > 0) {
//							    		 mttcDouble = (double) mttc /100;
//							    	 }
							    	 ligne.setMontantHt(checkString(mthtDouble));
							    	// ligne.setMontantTtc(checkString(mttcDouble));
							    	 
							    	 //affecte les entités (tiers, articles) si les liaisons existes
							    	 ligne =  ligneServiceWebExterneService.affecteLiaison( ligne, serviceWeb, listeLiaison, false);

							    	 //pas de création auto de tiers
							    	
							    	 listeLignesAMerge.add(ligne);
									
								}
								
							}
							
							
									    	
						    
						    //on merge toutes les lignes du fichiers
						    for (TaLigneServiceWebExterne li : listeLignesAMerge) {
//						    	 tx.begin();
//						    	 ligneServiceWebExterneService.merge(li); 
//						    	 tx.commit();
						    	 transactionalService.mergeLigneServiceWebExterne(li);
						    	 nbLignes++;
							}
						   
					    	
						    //on supprime le fichier du serveur BDG car on en a plus besoin
							 f.delete();
							 
						} catch (Exception e1) {
							mapErreurFichiers.add(f.getName());
							e1.printStackTrace();
						    
						}
						
						
						
						
						
						
					}
			    }//fin boucle sur les fichiers
			    
			    if(!mapErreurFichiers.isEmpty()) {
					String message = "Les fichiers suivants ont été ignorés à cause d'erreurs. Veuillez contacter le support de BDG pour plus d'informations : ";
					for (String string : mapErreurFichiers) {
						message += string+" ";
					}
					 throw new Exception(message);
				}
			    
			  } else {
			    // Handle the case where dir is not really a directory.
			    // Checking dir.isDirectory() above would not be sufficient
			    // to avoid race conditions with another process that deletes
			    // directories.
			  }
		
		return nbLignes;

	}
	
    public Integer parcourir() throws EJBException {
    	try {
    		return enregistreLigneDepuisFichier();
		} catch (Exception e) {
			 throw new EJBException(e.getMessage());
		}
		
    }
	 
	private void initVariablesSFTP() throws FinderException {
		initCompteServiceWeb();
		username = compteServiceWeb.getLogin();
		password = compteServiceWeb.getPassword();
		
	}
	private SSHClient setupSshj() throws IOException, FinderException {
		initVariablesSFTP();
		DefaultConfig configuration = new DefaultConfig();
//		configuration.setCipherFactories(cipherFactories);
	    SSHClient client = new SSHClient();
	    client.addHostKeyVerifier(new PromiscuousVerifier());

	    client.connect(remoteHost);
	    client.authPassword(username, password);
	    return client;
	}
	
	public Integer downloadFileSFTP() throws IOException, FinderException {
	    SSHClient sshClient = setupSshj(); 
	    SFTPClient sftpClient = sshClient.newSFTPClient();
	    bdgProperties = new BdgProperties();
	    localDir = bdgProperties.osTempDirDossier(tenantInfo.getTenantId()+dossierTravail);   
	    Integer nbFiles = 0;
	    //je liste tous les fichiers de commandes qu'il y a sur le serveur
	    List<RemoteResourceInfo> listRemoteFiles = sftpClient.ls(remoteFile);
	    for (RemoteResourceInfo remoteFileInfo : listRemoteFiles) {
	    	//si le fichier ne commence pas par old
			if(!remoteFileInfo.getName().startsWith("old")) {
				//je télécharge le fichier
				 sftpClient.get(remoteFileInfo.getPath(), localDir);	
				 nbFiles++;
				 Date dateLecture = new Date();
				 SimpleDateFormat ft =  new SimpleDateFormat ("yyyyMMddHHmmss");
				 String newName = "old_"+ft.format(dateLecture)+"_"+remoteFileInfo.getName();
				 String path = remoteFileInfo.getParent();
				 String target = path+"/"+newName;
				 //et je le renomme old avec la date du jour sur le serveur
				 sftpClient.rename(remoteFileInfo.getPath(), target);
			}
		}
	    sftpClient.close();
	    sshClient.disconnect();
	    
	  //enregistrement de la derniere date d'import
		TaSynchroServiceExterne dateSynchro = new TaSynchroServiceExterne();
		Date now = new Date();
		dateSynchro.setDerniereSynchro(now);
		dateSynchro.setTaCompteServiceWebExterne(compteServiceWeb);
		dateSynchro.setTypeEntite(TaSynchroServiceExterne.TYPE_ENTITE_DEVIS);
		dateSynchro= synchroService.merge(dateSynchro);
	    
	    return nbFiles;
	}

	

	public TaCompteServiceWebExterne getCompteServiceWeb() {
		return compteServiceWeb;
	}

	public void setCompteServiceWeb(TaCompteServiceWebExterne compteServiceWeb) {
		this.compteServiceWeb = compteServiceWeb;
	}

}
