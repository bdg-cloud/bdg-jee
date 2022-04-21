package fr.legrain.servicewebexterne.service.divers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaShippingBoServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaSynchroServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITransactionnalMergeLigneServiceExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
//@TransactionManagement(TransactionManagementType.BEAN)
public class TaShippingBoServiceWebExterne implements ITaShippingBoServiceWebExterneServiceRemote{
	
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
	
	
	String dossierTravail = "/shippingbo/orders/";
	
	
	String localDir = "";
	
	String codeService = "SHIPPINGBO";
	
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
		compteServiceWeb = compteServiceWebExterneService.findCompteDefautPourFournisseurService(serviceWeb.getCodeServiceWebExterne());
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		compteServiceWeb = util.decrypter(compteServiceWeb);
		remoteHost = compteServiceWeb.getValeur1();
		remoteFile = compteServiceWeb.getValeur2();
	}
	
	//Cette méthode sera surement appellée par un CRON toutes les heures
	@SuppressWarnings("unused")
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
		
		

		
		 if (directoryListing != null) {
			 List<TaLiaisonServiceExterne> listeLiaison = new ArrayList<TaLiaisonServiceExterne>();
			 Map<String, TaTiers> listeRefTiers = new HashMap<String, TaTiers>();
			List<TaTiers> tiersCree = new ArrayList<TaTiers>();
			 //je parcours tous les fichiers JSON qu'il y a dans /orders dans le dossier temporaire
			    for (File f : directoryListing) {
					
					//File f = new File(localPath);
					if(f != null && f.exists()) {
						
						List<TaLigneServiceWebExterne> listeLignesAMerge = new ArrayList<TaLigneServiceWebExterne>();
						JSONArray jsonArrayOrders;
						String content;
						try {
							 InputStream is = new FileInputStream(f);
							//content = FileUtils.readFileToString(f, "utf-8");
							 BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
						     content = readAll(rd);
							 // Convert JSON string to JSONObject
						    JSONObject ligneJson = new JSONObject(content); 
						    is.close();
						    
						    jsonArrayOrders =  ligneJson.getJSONArray("orders");
						    
						    //on parcours chaque ligne de commande du fichier
						    for (int i = 0; i < jsonArrayOrders.length(); i++) {
						    	JSONObject jsonObject = jsonArrayOrders.getJSONObject(i);
						    	JSONObject shippingAdressJson = jsonObject.getJSONObject("billing_address");
						    	
						    	JSONArray arrayOrderItems = jsonObject.getJSONArray("order_items");
						    	
						    	
						    	//on parcours chaque produit différents pour chaque ligne de commande
						    	 for (int i2 = 0; i2 < arrayOrderItems.length(); i2++) {
						    		 JSONObject jsonOrderItem = arrayOrderItems.getJSONObject(i2);
						    		 TaLigneServiceWebExterne ligne = new TaLigneServiceWebExterne();
						    		 //concerne la ligne de commande en général
						    		 ligne.setIdCommandeExterne(checkString(jsonObject.get("id")));
						    		 ligne.setRefCommandeExterne(checkString(jsonObject.get("id")));
							    	 ligne.setLibelle("Ligne n° "+String.valueOf(jsonOrderItem.getInt("id"))+" "+checkString(jsonOrderItem.get("title")));
							    	
							    	 SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							    	 Date dateCreation = formatter.parse(checkString(jsonObject.get("created_at")));
							    	 Date dateUpdate = formatter.parse(checkString(jsonObject.get("updated_at")));
							    	 
							    	 ligne.setDateCreationExterne(dateCreation);
							    	 ligne.setDateUpdateExterne(dateUpdate);
							    	 
							    	 
							    	 ligne.setJsonInitial(jsonObject.toString());
							    	 
							    	 //a enregistrer
							    	 ligne.setTaCompteServiceWebExterne(compteServiceWeb);
							    	 
							    	 ligne.setNomTiers(checkString(shippingAdressJson.get("lastname")));
							    	 ligne.setPrenomTiers(checkString(shippingAdressJson.get("firstname")));
							    	 ligne.setRefTiers(checkString(shippingAdressJson.get("email")));
							    	 ligne.setMailTiers(checkString(shippingAdressJson.get("email")));
							    	 
							    	 //concerne les produits
							    	 ligne.setRefArticle(checkString(jsonOrderItem.get("product_ref")));
							    	 ligne.setNomArticle(checkString(jsonOrderItem.get("title")));
							    	 ligne.setQteArticle(jsonOrderItem.getInt("quantity"));
							    	 
							    	 Integer montantTotalTTC = jsonObject.getInt("total_price_cents");
							    	 Double montantTotalTTCDouble = null;
							    	 if(montantTotalTTC != null && montantTotalTTC > 0) {
							    		 montantTotalTTCDouble = (double) montantTotalTTC /100;
							    	 }
							    	 
							    	 Integer montantTotalDiscount = jsonObject.getInt("total_discount_tax_included_cents");
							    	 Integer montantTotalShipping = jsonObject.getInt("total_shipping_tax_included_cents");
							    	 Double montantTotalDiscountDouble = null;
							    	 Double montantTotalShippingDouble = null;
							    	 if(montantTotalDiscount != null && montantTotalDiscount > 0) {
							    		 montantTotalDiscountDouble = (double) montantTotalDiscount /100;
							    		 ligne.setMontantTotalDiscountDoc(montantTotalDiscountDouble.toString());
							    	 }
							    	 if(montantTotalShipping != null && montantTotalShipping > 0) {
							    		 montantTotalShippingDouble = (double) montantTotalShipping /100;
							    		 ligne.setMontantTotalLivraisonDoc(montantTotalShippingDouble.toString());
							    	 }
							    	 
							    	 
							    	 ligne.setMontantTtcTotalDoc(checkString(montantTotalTTCDouble));
							    	 
							    	 Integer mttc = jsonOrderItem.getInt("price_tax_included_cents");
							    	 Double mttcDouble = null;
							    	 if(mttc != null && mttc > 0) {
							    		 mttcDouble = (double) mttc /100;
							    	 }
							    	 
							    	 ligne.setMontantTtc(checkString(mttcDouble));
							    	 
							    	 //affecte les entités (tiers, articles) si les liaisons existes
							    	 ligne =  ligneServiceWebExterneService.affecteLiaison( ligne, serviceWeb, listeLiaison, false);
							    	//création du tiers si aucune liaisons et aucun tiers qui n'a cette adresse mail
									//TaTiers tiers= taTiersService.findBy(ligne.getRefTiers());
							    	 if(ligne.getTaTiers() == null) {
							    		 Boolean creationTiers = true;
								    		//vérifie si une liaison ne va pas deja etre créer dans cette boucle
								    		 for (Map.Entry<String, TaTiers> entry : listeRefTiers.entrySet()) {
								    			 //si il y a effectivement une liaison qui va être créer, je n'ai pas besoin de créer le tiers, juste besoin d'affecter celui deja créer a cette ligne,
								    			 //plus bas juste avant le merge de la ligne
								    			 if(ligne.getRefTiers().equals(entry.getKey())) {
								    				 creationTiers = false;
								    				
								    			 }
								    		 }
							    		
								    		 if(creationTiers) {
								    			 Date now = new Date();
									    		 TaTiers tiers = new TaTiers();
									    		 //@Transactional(value=TxType.REQUIRES_NEW)
												 TaTTiers typeTiers = taTTiersService.findByCode("C");
									    		 TaAdresse adresse = new TaAdresse();
									    		 TaTAdr typeAdresse = taTAdrService.findByCode("FACT");
									    		 adresse.setTaTAdr(typeAdresse);
									    		 TaTelephone tel = new TaTelephone();
									    		 TaEmail mail = new TaEmail();
									    		 tiers.setNomTiers(checkString(shippingAdressJson.get("lastname")));
									    		 if(tiers.getNomTiers().equals("")) {
									    			 tiers.setNomTiers(checkString(shippingAdressJson.get("fullname")));
									    		 }
									    		 tiers.setPrenomTiers(checkString(shippingAdressJson.get("firstname")));
									    		 tiers.setActifTiers(1);
									    		 tiers.setDateAnniv(now);
									    		 tiers.setTtcTiers(0);
									    		 //@Transactional(value=TxType.REQUIRES_NEW)
									    		 tiers.setCodeTiers(taTiersService.genereCode(null));  
									    		 tiers.setCompte("411");
									    		 tiers.setTaTTiers(typeTiers);
									    		 if(tiers.getCodeCompta()==null 
									 					|| (tiers.getCodeCompta()!=null && tiers.getCodeCompta().equals(""))
									 					) {
									 				if(tiers.getCodeTiers().length()>6) {
									 					tiers.setCodeCompta(tiers.getCodeTiers().substring(0, 7));
									 				} else  {
									 					tiers.setCodeCompta(tiers.getCodeTiers().substring(0, tiers.getCodeTiers().length()));
									 				}
									 			}
									    		
									    		 adresse.setAdresse1Adresse(checkString(shippingAdressJson.get("street1")));
									    		 adresse.setAdresse2Adresse(checkString(shippingAdressJson.get("street2")));
									    		 adresse.setAdresse3Adresse(checkString(shippingAdressJson.get("street3")));
									    		 adresse.setCodepostalAdresse(checkString(shippingAdressJson.get("zip")));
									    		 adresse.setPaysAdresse(checkString(shippingAdressJson.get("country")));
									    		 adresse.setVilleAdresse(checkString(shippingAdressJson.get("city")));
									    		 adresse.setTaTiers(tiers);
									    		// adresse = adresseService.merge(adresse);
									    		 
									    		 tel.setNumeroTelephone(checkString(shippingAdressJson.get("phone1")));
									    		 tel.setTaTiers(tiers);
									    		 tel.setCommAdministratifTelephone(1);
									    		 tel.setCommCommercialTelephone(1);
									    		// tel = telService.merge(tel);
									    		 
									    		 mail.setAdresseEmail(checkString(shippingAdressJson.get("email")));
									    		 mail.setTaTiers(tiers);
									    		// mail = mailService.merge(mail);
									    		// tiers.setTaAdresse(adresse);
									    		 tiers.addAdresse(adresse);
									    		 tiers.setTaTelephone(tel);
									    		 tiers.addTelephone(tel);
									    		 tiers.setTaEmail(mail);
									    		 tiers.addEmail(mail);
									    		//@Transactional(value=TxType.REQUIRES_NEW)
									    		//tiers = taTiersService.merge(tiers);
									    		
									    		ligne.setTaTiers(tiers);
									    		//création de la liaison entre ce nouveau tiers et sont adresse mail chez shippingBo
//									    		TaLiaisonServiceExterne liaison = new TaLiaisonServiceExterne();
//									    		liaison.setIdEntite(tiers.getIdTiers());
//									    		liaison.setRefExterne(ligne.getRefTiers());
//									    		liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
//									    		liaison.setServiceWebExterne(serviceWeb);
//									    		
//									    		liaison =liaisonService.merge(liaison);
//									    		listeLiaison.add(liaison);
									    		
									    		//j'ajoute la référence tiers et le code de ce nouveau tiers 
									    		//pour permettre d'éviter de créer un autre tiers si une meme ref tiers ce présente dans la boucle,
									    		//et bien sur pour créer les liaisons à la fin
									    		listeRefTiers.put(ligne.getRefTiers(), tiers);
								    		 }
								    		 
								    		
								    		
							    		
							    	 
						    	 	}
							    	 
							    	
							    	 listeLignesAMerge.add(ligne);
							    	
						    	 }//fin de la boucle sur les lignes de produits a l'intérieur d'une commande
						    	
						    	
						    }//fin boucle sur ligne de commande
						    
						    //regroupement des lignes par commandes
						    Map<String,List<TaLigneServiceWebExterne>> mapLignesParCommandes = new HashMap<>();
						    for (TaLigneServiceWebExterne li : listeLignesAMerge) {
								String cle = li.getRefCommandeExterne();
								if(!mapLignesParCommandes.keySet().contains(cle)) {
									mapLignesParCommandes.put(cle, new ArrayList<>());
								}
								mapLignesParCommandes.get(cle).add(li);
							}
						    
						    //parcours de chaque commande et création si nécéssaire de lignes de frais de port et discount
						    for (String idCommande : mapLignesParCommandes.keySet()) {
						    	if(!mapLignesParCommandes.get(idCommande).isEmpty()) {
						    		TaLigneServiceWebExterne dtoEntete = mapLignesParCommandes.get(idCommande).get(0);
						    		//ajout d'une ligne pour la remise globale sur le document
						    		if(dtoEntete.getMontantTotalDiscountDoc() != null && !dtoEntete.getMontantTotalDiscountDoc().isEmpty()) {
						    			 Double montantDiscount = Double.parseDouble(dtoEntete.getMontantTotalDiscountDoc());
						    			 
						    	    	 if(montantDiscount > 0) {
						    	    		 TaLigneServiceWebExterne ligneDiscount = new TaLigneServiceWebExterne();
						    	    		 ligneDiscount.setIdCommandeExterne(idCommande);
						    	    		 ligneDiscount.setRefCommandeExterne(dtoEntete.getRefCommandeExterne());
						    	    		 ligneDiscount.setLibelle("Remise sur la commande");
						    	    		 ligneDiscount.setRefArticle("REMISESHIPPINGBO");
						    	    		 ligneDiscount.setNomArticle("Remise sur la commande");
						    	    		 ligneDiscount.setRefTiers(dtoEntete.getRefTiers());
						    	    		 ligneDiscount.setQteArticle(1);
						    	    		 ligneDiscount.setPrenomTiers(dtoEntete.getPrenomTiers());
						    	    		 ligneDiscount.setNomTiers(dtoEntete.getNomTiers());
						    	    		 ligneDiscount.setDateCreationExterne(dtoEntete.getDateCreationExterne());
						    	    		 ligneDiscount.setMontantTtc("-"+montantDiscount.toString());
						    	    		 ligneDiscount.setMontantTotalDiscountDoc(dtoEntete.getMontantTotalDiscountDoc());
						    	    		 ligneDiscount.setMontantTotalLivraisonDoc(dtoEntete.getMontantTotalLivraisonDoc());
						    	    		 ligneDiscount.setMontantTtcTotalDoc(dtoEntete.getMontantTtcTotalDoc());
						    	    		 ligneDiscount.setTaCompteServiceWebExterne(compteServiceWeb);
						    	    		 //affecte les entités (tiers, articles) si les liaisons existes
						    	    		 ligneDiscount =  ligneServiceWebExterneService.affecteLiaison( ligneDiscount, serviceWeb, listeLiaison, false);
						    	    		 listeLignesAMerge.add(ligneDiscount);
						    	    	 }
						    		}
						    		//ajout d'une ligne pour les frais de port sur le document
						    		if(dtoEntete.getMontantTotalLivraisonDoc() != null && !dtoEntete.getMontantTotalLivraisonDoc().isEmpty()) {
						    			 Double montantLivraison = Double.parseDouble(dtoEntete.getMontantTotalLivraisonDoc());
						    			 
						    	    	 if(montantLivraison > 0) {
						    	    		 TaLigneServiceWebExterne ligneLivraison = new TaLigneServiceWebExterne();
						    	    		 ligneLivraison.setIdCommandeExterne(idCommande);
						    	    		 ligneLivraison.setRefCommandeExterne(dtoEntete.getRefCommandeExterne());
						    	    		 ligneLivraison.setLibelle("Frais de port");
						    	    		 ligneLivraison.setNomArticle("Frais de port");
						    	    		 ligneLivraison.setRefArticle("FRAISPORTSHIPPINGBO");
						    	    		 ligneLivraison.setQteArticle(1);
						    	    		 ligneLivraison.setRefTiers(dtoEntete.getRefTiers());
						    	    		 ligneLivraison.setPrenomTiers(dtoEntete.getPrenomTiers());
						    	    		 ligneLivraison.setNomTiers(dtoEntete.getNomTiers());
						    	    		 ligneLivraison.setDateCreationExterne(dtoEntete.getDateCreationExterne());
						    	    		 ligneLivraison.setMontantTtc(montantLivraison.toString());
						    	    		 ligneLivraison.setMontantTotalDiscountDoc(dtoEntete.getMontantTotalDiscountDoc());
						    	    		 ligneLivraison.setMontantTotalLivraisonDoc(dtoEntete.getMontantTotalLivraisonDoc());
						    	    		 ligneLivraison.setMontantTtcTotalDoc(dtoEntete.getMontantTtcTotalDoc());
						    	    		 ligneLivraison.setTaCompteServiceWebExterne(compteServiceWeb);
						    	    		 //affecte les entités (tiers, articles) si les liaisons existes
						    	    		 ligneLivraison =  ligneServiceWebExterneService.affecteLiaison( ligneLivraison, serviceWeb, listeLiaison, false);
						    	    		 listeLignesAMerge.add(ligneLivraison);
						    	    	 }
						    		}
						    		
						    	}
						    }
						    
						    
						    //on merge toutes les lignes du fichiers 
						    for (TaLigneServiceWebExterne li : listeLignesAMerge) {
						    	if(li.getTaTiers() == null) {
						    		for (TaTiers taTiers : tiersCree) {
										if(taTiers.getTaEmail().getAdresseEmail().equals(li.getRefTiers())) {
											taTiers = taTiersService.findById(taTiers.getIdTiers());
											li.setTaTiers(taTiers);
										}
									}
						    	}
						    	 li =  transactionalService.mergeLigneServiceWebExterne(li);
						    	 //ici je rempli une liste de tiers mergés
						    	 boolean existeDeja = false;
						    	 for (TaTiers taTiers : tiersCree) {
						    		 if (taTiers.getCodeTiers().equals(li.getTaTiers().getCodeTiers())) {
						    			 existeDeja = true;
									}
								 }
						    	 if(!existeDeja) {
						    		 tiersCree.add(li.getTaTiers());
						    	 }
						    	 //c'est ici qu'il faut que je merge mes liaisons externes, pas avant. Car ici j'ai l'id du tiers, pas avant, car c'est le merge de la ligne qui va merge le tiers.
						    	 
						    	 //je compare la ligne de service que j'ai a merge avec la liste de ref tiers pour liaison a merge
						    	 for (Map.Entry<String, TaTiers> entry : listeRefTiers.entrySet()) {
						    		 //si je trouve une "ref tiers pour liaison a faire" correspondante a la ligne de service que je vien de merge 
						    		 if(li.getRefTiers().equals(entry.getKey())) {
						    			 //je vérifie que je n'ai pas déjà merge une liaison avec cette ref tiers
						    			 TaLiaisonServiceExterne liaisonExistante = liaisonService.findByRefTiersAndByIdServiceWebExterne(li.getRefTiers(), serviceWeb.getIdServiceWebExterne());
						    			 //si je n'ai pas deja merge une liaison avec cette ref tiers, je l'a créer ci-dessous
						    			 if(liaisonExistante == null) {
						    				//création de la liaison entre ce nouveau tiers et sont adresse mail chez le service externe
									    		TaLiaisonServiceExterne liaison = new TaLiaisonServiceExterne();
									    		liaison.setIdEntite(li.getTaTiers().getIdTiers());
									    		liaison.setRefExterne(li.getRefTiers());
									    		liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
									    		liaison.setServiceWebExterne(serviceWeb);
									    		
									    		liaison =liaisonService.merge(liaison);
									    		listeLiaison.add(liaison);
						    			 }
											
										}
						    		}
						    	
						    	 
						    	 
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
  		dateSynchro.setTypeEntite(TaSynchroServiceExterne.TYPE_ENTITE_COMMANDE);
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
