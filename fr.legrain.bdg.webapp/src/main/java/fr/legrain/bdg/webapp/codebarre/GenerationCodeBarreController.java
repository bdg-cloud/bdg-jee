package fr.legrain.bdg.webapp.codebarre;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.xml.serialize.XMLSerializer;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Constants;
import org.krysalis.barcode4j.impl.code128.Code128LogicImpl;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.code128.EAN128LogicImpl;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.service.TaParamEan128Service;
import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEanServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.EtiquetteCodeBarre128Bean;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.lib.data.EnumModeObjet;


//@Named
@Named
@ViewScoped  
public class GenerationCodeBarreController extends AbstractController implements Serializable {  

	private static final long serialVersionUID = -3512103552691966197L;
	
	@Inject @Named(value="etiquetteCodeBarre128Bean")
	private EtiquetteCodeBarre128Bean etiquetteCodeBarre128Bean;
	
	@EJB private ITaParamEan128ServiceRemote taParamEan128Service;
	@EJB private ITaParamEanServiceRemote taParamEanService;
	
	@EJB private ITaUniteServiceRemote taUniteService;
	private TaUnite taUnitePoidsEnKilo = null;
	
	private TaArticle taArticle;
	private TaLot taLot;
	private CodeBarreParam param;
	
	private boolean parametrage = true;
	
	private boolean modeEAN13 = false;
	private boolean modeEAN14 = false;
	private boolean modeEAN128 = true;
	private boolean modeQRCode = false;
	private boolean modeDataMatrix128 = false;
	private boolean afficherEAN128 = true;
	private boolean afficherEAN13 = false;
	private boolean afficherEAN14 = false;
	private boolean afficherQrCode = false;
	private boolean afficherDatamatrix = false;
	private boolean afficherDetail= false;
	
	private boolean modeFichierImage = false;
	
	private boolean modificationAIGS1Manuel = false;
	private boolean modificationCodeBarreManuel = false;
	
	private String codeBarre;
	private String codeBarreBarcode4J;
	private String codeBarreEAN13;
	
	private boolean ean13ArticleGtin14 = false; //calcul de la somme de controle a partir de l'EAN13
	private boolean ean13SurembalageGtin14 = false; //calcul de la somme de controle a partir de l'EAN13
	private boolean ean13SurembalageGtin14Light = false; //Le GTIN14 est une EAN13 précédé par un 0;
	
	private boolean encodeCodeArticle = false;
	private boolean encodeDDM = false;
	private boolean encodeDLC = false;
	private boolean encodeEanArticle = false;
	private boolean encodeEanSurembalage = true;
	private boolean encodeHauteurEnMetre = false;
	private boolean encodeLargeurEnMetre = false;
	private boolean encodeLongueurEnMetre = false;
	private boolean encodeLotFabrication = false;
	private boolean encodePoidsEnKg = false;
	private boolean encodeQuantite = false;
	private boolean encodeQuantiteUnitaire = false;
	private boolean encodeSurfaceEnMetreCarre = false;
	private boolean encodeVolumeEnLitre = false;
	
	private String codeArticle;
	private Date ddm;
	private Date dlc;
	private String eanArticle;
	private String eanSurembalage;
	private BigDecimal hauteurEnMetre;
	private BigDecimal largeurEnMetre;
	private BigDecimal longueurEnMetre;
	private String uniteHauteurEnMetre;
	private String uniteLargeurEnMetre;
	private String uniteLongueurEnMetre;
	private String lotFabrication;
	private BigDecimal poidsEnKg;
	private String unitePoidsEnKg;
	private BigDecimal quantite;
	private BigDecimal quantiteUnitaire;
	private BigDecimal surfaceEnMetreCarre;
	private BigDecimal volumeEnLitre;
	private String uniteSurfaceEnMetreCarre;
	private String uniteVolumeEnLitre;
	
	private boolean encodeEan128 = false;
	private Map<String, Object> listeAiValeur = new HashMap<>();
	
	private @Inject TenantInfo tenantInfo;
	
	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		param = (CodeBarreParam) sessionMap.get(CodeBarreParam.NOM_OBJET_EN_SESSION);
		sessionMap.remove(CodeBarreParam.NOM_OBJET_EN_SESSION);
		if(param!=null) {
			taArticle = param.getTaArticle();
			taLot = param.getTaLot();
			
			if(taLot!=null) {
				taArticle = taLot.getTaArticle();
//				encodeLotFabrication = true;
				encodeEanSurembalage = true;
				
				ddm = taLot.getDluo();
				dlc = taLot.getDluo();
//				eanSurembalage;
//				hauteurEnMetre;
//				largeurEnMetre;
//				longueurEnMetre;
//				uniteHauteurEnMetre;
//				uniteLargeurEnMetre;
//				uniteLongueurEnMetre;
				lotFabrication = taLot.getNumLot();
				encodeLotFabrication = true;
				poidsEnKg = param.getQuantite(); //TODO A verifier en fonction de l'unité
				//unitePoidsEnKg = ;
				taUnitePoidsEnKilo = param.getTaUnite(); //TODO A verifier en fonction de l'unité
//				quantite ;
//				quantiteUnitaire;
//				surfaceEnMetreCarre;
//				volumeEnLitre;
//				uniteSurfaceEnMetreCarre;
//				uniteVolumeEnLitre;
			}
			codeBarreEAN13 = taArticle.getCodeBarre();
			eanArticle = codeBarreEAN13;
			eanSurembalage = codeBarreEAN13;
			codeArticle = taArticle.getCodeArticle();
			actValiderCodeBarre(null);
		}
	}
	
	public void actImprimerCode128(ActionEvent actionEvent) {
		try {
			System.out.println("GenerationCodeBarreController.actImprimerCode128()");
			
			
			CodeBarre128PrintParam codeBarre128PrintParam = new CodeBarre128PrintParam();
			
			//etiquetteCodeBarre128Bean.videListe();
			try {
				Map<String, TaArticle> map = new HashMap<>();
				Map<String, TaLot> mapLot = new HashMap<>();
//				if(taLot!=null) {
//					mapLot.put(codeBarre, taLot);
//					codeBarre128PrintParam.setListeCodebarreLot(mapLot);
//				} else {
					map.put(codeBarre, taArticle);
					codeBarre128PrintParam.setListeCodebarreArticle(map);
//				}
				codeBarre128PrintParam.setAfficherEAN128(afficherEAN128);
				codeBarre128PrintParam.setAfficherEAN13(afficherEAN13);
				codeBarre128PrintParam.setAfficherEAN14(afficherEAN14);
				codeBarre128PrintParam.setAfficherDatamatrix(afficherDatamatrix);
				codeBarre128PrintParam.setAfficherQrCode(afficherQrCode);
				
				codeBarre128PrintParam.setAfficherDetail(afficherDetail);
				
				if(encodeCodeArticle)
					codeBarre128PrintParam.setCodeArticle(codeArticle);
				if(encodeDDM)
					codeBarre128PrintParam.setDdm(ddm);
				if(encodeDLC)
					codeBarre128PrintParam.setDlc(dlc);
				if(encodeEanArticle)
					codeBarre128PrintParam.setEanArticle(eanArticle);
				if(encodeEanSurembalage)
					codeBarre128PrintParam.setEanSurembalage(eanSurembalage);
				if(encodeHauteurEnMetre) {
					codeBarre128PrintParam.setHauteurEnMetre(hauteurEnMetre);
					codeBarre128PrintParam.setUniteHauteurEnMetre(uniteHauteurEnMetre);
				}
				if(encodeLargeurEnMetre) {
					codeBarre128PrintParam.setLargeurEnMetre(largeurEnMetre);
					codeBarre128PrintParam.setUniteLargeurEnMetre(uniteLargeurEnMetre);
				}
				if(encodeLargeurEnMetre) {
					codeBarre128PrintParam.setLongueurEnMetre(longueurEnMetre);
					codeBarre128PrintParam.setUniteLongueurEnMetre(uniteLongueurEnMetre);
				}
				if(encodeLotFabrication)
					codeBarre128PrintParam.setLotFabrication(lotFabrication);
				if(encodePoidsEnKg) {
					codeBarre128PrintParam.setPoidsEnKg(poidsEnKg);
					codeBarre128PrintParam.setUnitePoidsEnKg(unitePoidsEnKg);
				}
				if(encodeQuantite)
					codeBarre128PrintParam.setQuantite(quantite);
				if(encodeQuantiteUnitaire)
					codeBarre128PrintParam.setQuantiteUnitaire(quantiteUnitaire);
				if(encodeSurfaceEnMetreCarre) {
					codeBarre128PrintParam.setSurfaceEnMetreCarre(surfaceEnMetreCarre);
					codeBarre128PrintParam.setUniteSurfaceEnMetreCarre(uniteSurfaceEnMetreCarre);
				}
				if(encodeVolumeEnLitre) {
					codeBarre128PrintParam.setVolumeEnLitre(volumeEnLitre);
					codeBarre128PrintParam.setUniteVolumeEnLitre(uniteVolumeEnLitre);
				}
				
				etiquetteCodeBarre128Bean.init(codeBarre128PrintParam);
				//etiquetteCodeBarre128Bean.calcDimensions(codeBarre);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Articles", "actImprimerEtiquetteCB"));
			}
			
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Code barre", e.getMessage()/*+" "+ThrowableExceptionLgr.renvoieCauseRuntimeException(e)*/)); 
		}
		//PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actAnnuler(ActionEvent actionEvent) {
		System.out.println("GenerationCodeBarreController.actAnnuler()");
		
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void checkboxChanged(AjaxBehaviorEvent event) {
		actValiderCodeBarre(null);
	}
	
	public void actValiderCodeBarre(ActionEvent actionEvent) {
		System.out.println("GenerationCodeBarreController.actValiderMontant() ");
		try {
			codeBarre = "";
			codeBarreBarcode4J = "";
			Map<String, Object> listeAiValeur = new LinkedHashMap<String, Object>();
			
			if(encodeCodeArticle) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_CODE_ARTICLE,codeArticle);
				codeBarreBarcode4J += TaParamEan128Service.EAN128_AI_CODE_ARTICLE+codeArticle+EAN128Bean.DEFAULT_GROUP_SEPARATOR;
				codeBarre += taParamEan128Service.encodeCodeArticle(codeArticle)/*+"]C1"*/;
			}

			if(encodeEanArticle) {
				String ean13 = null;
				String ean14 = null;
				if(eanArticle.length()==12) {
					System.out.println(eanArticle+" 12 * "+eanArticle);
					ean13 = eanArticle+TaParamEan128Service.checkSum(eanArticle);
				} else {
					ean13 = eanArticle;
				}
				eanArticle = ean13;
				if(ean13.length()==13 && ean13ArticleGtin14) {
					System.out.println(eanArticle+" 13 * "+ean13);
					ean14 = ean13+TaParamEan128Service.checkSum(ean13);
					System.out.println(eanArticle+" 14 * "+ean14);
					eanArticle = ean14;
				} 
				
				//listeAiValeur.put(TaParamEan128Service.EAN128_AI_ARTICLE,"0"+eanArticle);//l'EAN13 dans un EAN128 est sur 14 caractères (précédé par un 0 selon wikipedia)
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_ARTICLE,eanArticle);
				codeBarreBarcode4J += TaParamEan128Service.EAN128_AI_ARTICLE+eanArticle+EAN128Bean.DEFAULT_GROUP_SEPARATOR;;
				codeBarre += taParamEan128Service.encodeEanArticle(eanArticle);
				
			
			}
			if(encodeEanSurembalage) {
				String ean13 = null;
				String ean14 = null;
				if(eanArticle.length()==12) {
					System.out.println(eanArticle+" 12 * "+eanArticle);
					ean13 = eanArticle+TaParamEan128Service.checkSum(eanArticle);
				} else {
					ean13 = eanArticle;
				}
				eanSurembalage = ean13;
				if(ean13.length()==13 && ean13SurembalageGtin14) {
					System.out.println(eanArticle+" 13 * "+ean13);
					ean14 = ean13+TaParamEan128Service.checkSum(ean13);
					System.out.println(eanArticle+" 14 * "+ean14);
					eanSurembalage = ean14;
				} else if(ean13.length()==13 && ean13SurembalageGtin14Light) {
					System.out.println(eanArticle+" 13 * "+ean13);
					ean14 = "0"+ean13;
					System.out.println(eanArticle+" 14 * "+ean14);
					eanSurembalage = ean14;
				} 
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_SUR_EMBALAGE,eanArticle+TaParamEan128Service.checkSum(eanArticle));//l'EAN13 dans un EAN128 est sur 14 caractères (précédé par un 0 selon wikipedia)
				//listeAiValeur.put(TaParamEan128Service.EAN128_AI_SUR_EMBALAGE,eanSurembalage);
				codeBarreBarcode4J += TaParamEan128Service.EAN128_AI_SUR_EMBALAGE+eanSurembalage+EAN128Bean.DEFAULT_GROUP_SEPARATOR;;
				codeBarre += taParamEan128Service.encodeEanSurembalage(eanSurembalage);
			}
			if(encodeDDM) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_DDM,ddm);
				codeBarreBarcode4J += TaParamEan128Service.EAN128_AI_DDM+ddm;
				codeBarre += taParamEan128Service.encodeDDM(ddm);
			}
			if(encodeDLC) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_DLC,dlc);
				codeBarreBarcode4J += TaParamEan128Service.EAN128_AI_DLC+dlc;
				codeBarre += taParamEan128Service.encodeDLC(dlc);
			}
			if(encodeHauteurEnMetre) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_HAUTEUR_EN_METRE,hauteurEnMetre);
				codeBarre += taParamEan128Service.encodeHauteurEnMetre(hauteurEnMetre, uniteHauteurEnMetre);
			}
			if(encodeLargeurEnMetre) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_LARGEUR_EN_METRE,largeurEnMetre);
				codeBarre += taParamEan128Service.encodeLargeurEnMetre(largeurEnMetre, uniteLargeurEnMetre);
			}
			if(encodeLongueurEnMetre) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_LONGUEUR_EN_METRE,longueurEnMetre);
				codeBarre += taParamEan128Service.encodeLongueurEnMetre(longueurEnMetre, uniteLongueurEnMetre) ;
			}
			if(encodePoidsEnKg) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_POIDS_EN_KG,poidsEnKg);
				codeBarre += taParamEan128Service.encodePoidsEnKg(poidsEnKg, taUnitePoidsEnKilo.getCodeUnite());
			}
			if(encodeQuantite) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_QUANTITE,quantite);
				codeBarre += taParamEan128Service.encodeQuantite(quantite);
			}
			if(encodeQuantiteUnitaire) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_QUANTITE_UNITAIRE,quantiteUnitaire);
				codeBarre += taParamEan128Service.encodeQuantiteUnitaire(quantiteUnitaire);
			}
			if(encodeSurfaceEnMetreCarre) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_SURFACE_EN_METRE,surfaceEnMetreCarre);
				codeBarre += taParamEan128Service.encodeSurfaceEnMetreCarre(surfaceEnMetreCarre, uniteSurfaceEnMetreCarre);
			}
			if(encodeVolumeEnLitre) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_VOLUME_EN_LITRE,volumeEnLitre);
				codeBarre += taParamEan128Service.encodeVolumeEnLitre(volumeEnLitre, uniteVolumeEnLitre);
			}
			if(encodeLotFabrication) {
				listeAiValeur.put(TaParamEan128Service.EAN128_AI_LOT_FABRICATION,lotFabrication);
				codeBarreBarcode4J += TaParamEan128Service.EAN128_AI_LOT_FABRICATION+lotFabrication+EAN128Bean.DEFAULT_GROUP_SEPARATOR;
				codeBarre += taParamEan128Service.encodeLotFabrication(lotFabrication);
			}
			if(encodeEan128) {
//				codeBarre += taParamEan128Service.encodeEan128(listeAiValeur);
			}
			
			if(modeFichierImage) {
				barcode4J();
			}
		
//		codeBarre = "";
//		codeBarre += taParamEan128Service.encodeEan128(listeAiValeur);
			
		System.out.println("GenerationCodeBarreController.actValiderCodeBarre() codeBarre : "+codeBarre);
//		ou exemple de récupération d'un ean13Article à partir d'un 128
		
//		try {
//			Map<String, String> liste =taParamEan128Service.decodeEan128(selectedTaArticleDTO.getCodeBarre());
//			if(liste!=null && !liste.isEmpty())
//			String retour=taParamEan128Service.recupEan13SurGtin14(liste.get("02"));
		
//		RequestContext context = RequestContext.getCurrentInstance();
//		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("code barre", retour));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Code barre", e.getMessage()/*+" "+ThrowableExceptionLgr.renvoieCauseRuntimeException(e)*/)); 
		}
	}
	
	public List<TaUnite> uniteAutoComplete(String query) {
		//List<TaUnite> allValues = taUniteService.findByCodeUniteStock(selectedLigneJSF.getTaArticle().getTaUniteReference().getCodeUnite(), query.toUpperCase());
		List<TaUnite> allValues = taUniteService.selectAll();
		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
		TaUnite civ=new TaUnite();
		civ.setCodeUnite(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public String barcode4J() throws Exception {
		final char FNC1 = Code128LogicImpl.FNC_1;
		final char GS = EAN128Bean.DEFAULT_GROUP_SEPARATOR;
		final char CD = EAN128Bean.DEFAULT_CHECK_DIGIT_MARKER;

		SchemaResolver sr = new SchemaResolver();
		BdgProperties bdgProperties = new BdgProperties();
		String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName("code.png");
		String localPathSvg = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName("code.svg");

		EAN128Bean bean = new EAN128Bean();
		EAN128LogicImpl impl;
		//241 FAPC500 10 78979779

//		impl = new EAN128LogicImpl(ChecksumMode.CP_AUTO, null);
//		impl.addAI("241FAPC500",0,EAN128AI.getAI("241", EAN128AI.TYPEAlphaNum));
//		impl.addAI("1078979779",0,EAN128AI.getAI("10", EAN128AI.TYPEAlphaNum));

		//        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getHumanReadableMsg());
		//        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getMessage());
		//        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getCode128Msg());
		//        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getEncodedMessage(impl.getCode128Msg()));

		final int dpi = 150;

		bean.setChecksumMode(ChecksumMode.CP_AUTO);
		bean.setOmitBrackets(false);
		bean.setCodeset(Code128Constants.CODESET_C);
		bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
		bean.doQuietZone(true);
		bean.setQuietZone(5);
		bean.setFontSize(2d);

		//Configure the barcode generator
		bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
		//width exactly one pixel
		//bean.setWideFactor(3);
		bean.doQuietZone(false);

		//Open output file
		File outputFile = new File(localPath);
		OutputStream out = new FileOutputStream(outputFile);
		try {
			//Set up the canvas provider for monochrome PNG output 
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(
					out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
			
			DOMImplementation implSvg = SVGDOMImplementation.getDOMImplementation();
			SVGCanvasProvider canvasSvg = new SVGCanvasProvider(implSvg, true, 0);
			
//			bean.generateBarcode(canvas, "021111111111116"+GS+"10LBR182_18/07/17_1");
//			bean.generateBarcode(canvasSvg, "021111111111116"+GS+"10LBR182_18/07/17_1");
			
			bean.generateBarcode(canvas, codeBarreBarcode4J);
			bean.generateBarcode(canvasSvg, codeBarreBarcode4J);
			
			Document svg = canvasSvg.getDOM();
			DOMSource source = new DOMSource(svg);
		    FileWriter writer = new FileWriter(new File(localPathSvg));
		    StreamResult result = new StreamResult(writer);

		    //1 erreur sur le serveur de prod, surement plusieurs jar xerces différent => java.lang.AbstractMethodError: Receiver class org.apache.batik.dom.svg.SVGOMDocument does not define or inherit an implementation of the resolved method abstract getXmlStandalone()Z of interface org.w3c.dom.Document.

//		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
//		    Transformer transformer = transformerFactory.newTransformer();
//		    transformer.transform(source, result);
		    
		    //2
		    XMLSerializer xml = new XMLSerializer(writer, null);
		    xml.serialize(svg);
		    
		    //3
//		    new XMLWriter(new FileOutputStream(localPathSvg),
//		              new OutputFormat(){{
//		                        setEncoding("UTF-8");
//		                        setIndent("    ");
//		                        setTrimText(false);
//		                        setNewlines(true);
//		                        setPadText(true);
//		              }}).write(svg);

			
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			SVGUtility.writeSVGDocument(doc, out, StandardCharsets.UTF_8.toString());
//			setValue(prettyPrint(new String(out.toByteArray())));

			// bean.setTemplate("(241)n7+cd+(10)n10+cd");
			//Generate the barcode
			
			//bean.generateBarcode(canvas, "10AB-123");

			//Signal end of generation
			canvas.finish();
		} finally {
			out.close();
		}
		return localPathSvg;

	}
	
	static public void actDialoguePaiementEcheanceParCarte(CodeBarreParam param) {
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 800);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//        params.put("modeEcranDefaut", list);
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		sessionMap.put(CodeBarreParam.NOM_OBJET_EN_SESSION, param);
        
        PrimeFaces.current().dialog().openDynamic("codebarre/dialog_codebarre_128", options, params);
	}
	
	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		System.out.println("GenerationCodeBarreController.handleReturnDialoguePaiementEcheanceParCarte()");
		try {
//			if(event!=null && event.getObject()!=null) {
//				RetourPaiementCarteBancaire e = (RetourPaiementCarteBancaire) event.getObject();
//				
//				//taDossierRcd = taDossierRcdService.findById(taDossierRcd.getIdDossierRcd());
//				
//				for (TaEcheance taEcheance : taDossierRcd.getTaEcheances()) {
//					if(taEcheance.getIdEcheance()==selectedTaEcheanceDTO.getId()) {
//						taEcheance.setTaReglementAssurance(e.getTaReglementAssurance());
//						taEcheance.getTaReglementAssurance().setTaEcheance(taEcheance);
//					}
//				}
//				
//				actEnregistrer(null);
//				actActionApresPaiementAccepte();
//				listeTaEcheanceDTO = taEcheanceService.findAllEcheanceRCDIdDTO(taDossierRcd.getIdDossierRcd());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ITaParamEan128ServiceRemote getTaParamEan128Service() {
		return taParamEan128Service;
	}

	public void setTaParamEan128Service(ITaParamEan128ServiceRemote taParamEan128Service) {
		this.taParamEan128Service = taParamEan128Service;
	}

	public ITaParamEanServiceRemote getTaParamEanService() {
		return taParamEanService;
	}

	public void setTaParamEanService(ITaParamEanServiceRemote taParamEanService) {
		this.taParamEanService = taParamEanService;
	}

	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}

	public CodeBarreParam getParam() {
		return param;
	}

	public void setParam(CodeBarreParam param) {
		this.param = param;
	}

	public boolean isParametrage() {
		return parametrage;
	}

	public void setParametrage(boolean parametrage) {
		this.parametrage = parametrage;
	}

	public boolean isModeEAN13() {
		return modeEAN13;
	}

	public void setModeEAN13(boolean modeEAN13) {
		this.modeEAN13 = modeEAN13;
	}

	public boolean isModeEAN128() {
		return modeEAN128;
	}

	public void setModeEAN128(boolean modeEAN128) {
		this.modeEAN128 = modeEAN128;
	}

	public String getCodeBarre() {
		return codeBarre;
	}

	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}

	public boolean isEncodeCodeArticle() {
		return encodeCodeArticle;
	}

	public void setEncodeCodeArticle(boolean encodeCodeArticle) {
		this.encodeCodeArticle = encodeCodeArticle;
	}

	public boolean isEncodeDDM() {
		return encodeDDM;
	}

	public void setEncodeDDM(boolean encodeDDM) {
		this.encodeDDM = encodeDDM;
	}

	public boolean isEncodeDLC() {
		return encodeDLC;
	}

	public void setEncodeDLC(boolean encodeDLC) {
		this.encodeDLC = encodeDLC;
	}

	public boolean isEncodeEanArticle() {
		return encodeEanArticle;
	}

	public void setEncodeEanArticle(boolean encodeEanArticle) {
		this.encodeEanArticle = encodeEanArticle;
	}

	public boolean isEncodeEanSurembalage() {
		return encodeEanSurembalage;
	}

	public void setEncodeEanSurembalage(boolean encodeEanSurembalage) {
		this.encodeEanSurembalage = encodeEanSurembalage;
	}

	public boolean isEncodeHauteurEnMetre() {
		return encodeHauteurEnMetre;
	}

	public void setEncodeHauteurEnMetre(boolean encodeHauteurEnMetre) {
		this.encodeHauteurEnMetre = encodeHauteurEnMetre;
	}

	public boolean isEncodeLargeurEnMetre() {
		return encodeLargeurEnMetre;
	}

	public void setEncodeLargeurEnMetre(boolean encodeLargeurEnMetre) {
		this.encodeLargeurEnMetre = encodeLargeurEnMetre;
	}

	public boolean isEncodeLongueurEnMetre() {
		return encodeLongueurEnMetre;
	}

	public void setEncodeLongueurEnMetre(boolean encodeLongueurEnMetre) {
		this.encodeLongueurEnMetre = encodeLongueurEnMetre;
	}

	public boolean isEncodeLotFabrication() {
		return encodeLotFabrication;
	}

	public void setEncodeLotFabrication(boolean encodeLotFabrication) {
		this.encodeLotFabrication = encodeLotFabrication;
	}

	public boolean isEncodePoidsEnKg() {
		return encodePoidsEnKg;
	}

	public void setEncodePoidsEnKg(boolean encodePoidsEnKg) {
		this.encodePoidsEnKg = encodePoidsEnKg;
	}

	public boolean isEncodeQuantite() {
		return encodeQuantite;
	}

	public void setEncodeQuantite(boolean encodeQuantite) {
		this.encodeQuantite = encodeQuantite;
	}

	public boolean isEncodeQuantiteUnitaire() {
		return encodeQuantiteUnitaire;
	}

	public void setEncodeQuantiteUnitaire(boolean encodeQuantiteUnitaire) {
		this.encodeQuantiteUnitaire = encodeQuantiteUnitaire;
	}

	public boolean isEncodeSurfaceEnMetreCarre() {
		return encodeSurfaceEnMetreCarre;
	}

	public void setEncodeSurfaceEnMetreCarre(boolean encodeSurfaceEnMetreCarre) {
		this.encodeSurfaceEnMetreCarre = encodeSurfaceEnMetreCarre;
	}

	public boolean isEncodeVolumeEnLitre() {
		return encodeVolumeEnLitre;
	}

	public void setEncodeVolumeEnLitre(boolean encodeVolumeEnLitre) {
		this.encodeVolumeEnLitre = encodeVolumeEnLitre;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public boolean isEncodeEan128() {
		return encodeEan128;
	}

	public void setEncodeEan128(boolean encodeEan128) {
		this.encodeEan128 = encodeEan128;
	}

	public Map<String, Object> getListeAiValeur() {
		return listeAiValeur;
	}

	public void setListeAiValeur(Map<String, Object> listeAiValeur) {
		this.listeAiValeur = listeAiValeur;
	}

	public Date getDdm() {
		return ddm;
	}

	public void setDdm(Date ddm) {
		this.ddm = ddm;
	}

	public Date getDlc() {
		return dlc;
	}

	public void setDlc(Date dlc) {
		this.dlc = dlc;
	}

	public String getEanArticle() {
		return eanArticle;
	}

	public void setEanArticle(String eanArticle) {
		this.eanArticle = eanArticle;
	}

	public String getEanSurembalage() {
		return eanSurembalage;
	}

	public void setEanSurembalage(String eanSurembalage) {
		this.eanSurembalage = eanSurembalage;
	}

	public BigDecimal getHauteurEnMetre() {
		return hauteurEnMetre;
	}

	public void setHauteurEnMetre(BigDecimal hauteurEnMetre) {
		this.hauteurEnMetre = hauteurEnMetre;
	}

	public BigDecimal getLargeurEnMetre() {
		return largeurEnMetre;
	}

	public void setLargeurEnMetre(BigDecimal largeurEnMetre) {
		this.largeurEnMetre = largeurEnMetre;
	}

	public BigDecimal getLongueurEnMetre() {
		return longueurEnMetre;
	}

	public void setLongueurEnMetre(BigDecimal longueurEnMetre) {
		this.longueurEnMetre = longueurEnMetre;
	}

	public String getUniteHauteurEnMetre() {
		return uniteHauteurEnMetre;
	}

	public void setUniteHauteurEnMetre(String uniteHauteurEnMetre) {
		this.uniteHauteurEnMetre = uniteHauteurEnMetre;
	}

	public String getUniteLargeurEnMetre() {
		return uniteLargeurEnMetre;
	}

	public void setUniteLargeurEnMetre(String uniteLargeurEnMetre) {
		this.uniteLargeurEnMetre = uniteLargeurEnMetre;
	}

	public String getUniteLongueurEnMetre() {
		return uniteLongueurEnMetre;
	}

	public void setUniteLongueurEnMetre(String uniteLongueurEnMetre) {
		this.uniteLongueurEnMetre = uniteLongueurEnMetre;
	}

	public String getLotFabrication() {
		return lotFabrication;
	}

	public void setLotFabrication(String lotFabrication) {
		this.lotFabrication = lotFabrication;
	}

	public BigDecimal getPoidsEnKg() {
		return poidsEnKg;
	}

	public void setPoidsEnKg(BigDecimal poidsEnKg) {
		this.poidsEnKg = poidsEnKg;
	}

	public String getUnitePoidsEnKg() {
		return unitePoidsEnKg;
	}

	public void setUnitePoidsEnKg(String unitePoidsEnKg) {
		this.unitePoidsEnKg = unitePoidsEnKg;
	}

	public BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}

	public BigDecimal getQuantiteUnitaire() {
		return quantiteUnitaire;
	}

	public void setQuantiteUnitaire(BigDecimal quantiteUnitaire) {
		this.quantiteUnitaire = quantiteUnitaire;
	}

	public BigDecimal getSurfaceEnMetreCarre() {
		return surfaceEnMetreCarre;
	}

	public void setSurfaceEnMetreCarre(BigDecimal surfaceEnMetreCarre) {
		this.surfaceEnMetreCarre = surfaceEnMetreCarre;
	}

	public BigDecimal getVolumeEnLitre() {
		return volumeEnLitre;
	}

	public void setVolumeEnLitre(BigDecimal volumeEnLitre) {
		this.volumeEnLitre = volumeEnLitre;
	}

	public String getUniteSurfaceEnMetreCarre() {
		return uniteSurfaceEnMetreCarre;
	}

	public void setUniteSurfaceEnMetreCarre(String uniteSurfaceEnMetreCarre) {
		this.uniteSurfaceEnMetreCarre = uniteSurfaceEnMetreCarre;
	}

	public String getUniteVolumeEnLitre() {
		return uniteVolumeEnLitre;
	}

	public void setUniteVolumeEnLitre(String uniteVolumeEnLitre) {
		this.uniteVolumeEnLitre = uniteVolumeEnLitre;
	}

	public EtiquetteCodeBarre128Bean getEtiquetteCodeBarre128Bean() {
		return etiquetteCodeBarre128Bean;
	}

	public void setEtiquetteCodeBarre128Bean(EtiquetteCodeBarre128Bean etiquetteCodeBarre128Bean) {
		this.etiquetteCodeBarre128Bean = etiquetteCodeBarre128Bean;
	}

	public boolean isModeDataMatrix128() {
		return modeDataMatrix128;
	}

	public void setModeDataMatrix128(boolean modeDataMatrix128) {
		this.modeDataMatrix128 = modeDataMatrix128;
	}

	public boolean isAfficherDatamatrix() {
		return afficherDatamatrix;
	}

	public void setAfficherDatamatrix(boolean afficherDatamatrix) {
		this.afficherDatamatrix = afficherDatamatrix;
	}

	public boolean isModeQRCode() {
		return modeQRCode;
	}

	public void setModeQRCode(boolean modeQRCode) {
		this.modeQRCode = modeQRCode;
	}

	public boolean isAfficherEAN128() {
		return afficherEAN128;
	}

	public void setAfficherEAN128(boolean afficherEAN128) {
		this.afficherEAN128 = afficherEAN128;
	}

	public boolean isAfficherEAN13() {
		return afficherEAN13;
	}

	public void setAfficherEAN13(boolean afficherEAN13) {
		this.afficherEAN13 = afficherEAN13;
	}

	public boolean isAfficherQrCode() {
		return afficherQrCode;
	}

	public void setAfficherQrCode(boolean afficherQrCode) {
		this.afficherQrCode = afficherQrCode;
	}

	public boolean isModeEAN14() {
		return modeEAN14;
	}

	public void setModeEAN14(boolean modeEAN14) {
		this.modeEAN14 = modeEAN14;
	}

	public boolean isAfficherEAN14() {
		return afficherEAN14;
	}

	public void setAfficherEAN14(boolean afficherEAN14) {
		this.afficherEAN14 = afficherEAN14;
	}

	public String getCodeBarreEAN13() {
		return codeBarreEAN13;
	}

	public void setCodeBarreEAN13(String codeBarreEAN13) {
		this.codeBarreEAN13 = codeBarreEAN13;
	}

	public boolean isModificationAIGS1Manuel() {
		return modificationAIGS1Manuel;
	}

	public void setModificationAIGS1Manuel(boolean modificationAIGS1Manuel) {
		this.modificationAIGS1Manuel = modificationAIGS1Manuel;
	}

	public boolean isModificationCodeBarreManuel() {
		return modificationCodeBarreManuel;
	}

	public void setModificationCodeBarreManuel(boolean modificationCodeBarreManuel) {
		this.modificationCodeBarreManuel = modificationCodeBarreManuel;
	}

	public String getCodeBarreBarcode4J() {
		return codeBarreBarcode4J;
	}

	public void setCodeBarreBarcode4J(String codeBarreBarcode4J) {
		this.codeBarreBarcode4J = codeBarreBarcode4J;
	}

	public TaUnite getTaUnitePoidsEnKilo() {
		return taUnitePoidsEnKilo;
	}

	public void setTaUnitePoidsEnKilo(TaUnite taUnitePoidsEnKilo) {
		this.taUnitePoidsEnKilo = taUnitePoidsEnKilo;
	}

	public boolean isEan13ArticleGtin14() {
		return ean13ArticleGtin14;
	}

	public void setEan13ArticleGtin14(boolean ean13ArticleGtin14) {
		this.ean13ArticleGtin14 = ean13ArticleGtin14;
	}

	public boolean isEan13SurembalageGtin14() {
		return ean13SurembalageGtin14;
	}

	public void setEan13SurembalageGtin14(boolean ean13SurembalageGtin14) {
		this.ean13SurembalageGtin14 = ean13SurembalageGtin14;
	}

	public boolean isEan13SurembalageGtin14Light() {
		return ean13SurembalageGtin14Light;
	}

	public void setEan13SurembalageGtin14Light(boolean ean13SurembalageGtin14Light) {
		this.ean13SurembalageGtin14Light = ean13SurembalageGtin14Light;
	}

	public boolean isAfficherDetail() {
		return afficherDetail;
	}

	public void setAfficherDetail(boolean afficherDetail) {
		this.afficherDetail = afficherDetail;
	}

	public boolean isModeFichierImage() {
		return modeFichierImage;
	}

	public void setModeFichierImage(boolean modeFichierImage) {
		this.modeFichierImage = modeFichierImage;
	}
	

}  
