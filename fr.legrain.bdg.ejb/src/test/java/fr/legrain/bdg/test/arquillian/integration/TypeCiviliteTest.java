package fr.legrain.bdg.test.arquillian.integration;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TypeCiviliteTest {
    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String VALEUR_CODE = "TEST";
    
//    @Deployment(testable = false)
//    public static WebArchive createDeployment() {
//        return ShrinkWrap.create(WebArchive.class, "login.war")
//            .addClasses(Credentials.class, User.class, LoginController.class)
//            .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
//            .addAsWebResource(new File(WEBAPP_SRC, "home.xhtml"))
//            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
//            .addAsWebInfResource(
//                new StringAsset("<faces-config version=\"2.0\"/>"),
//                "faces-config.xml");
//    	return LoginScreenGrapheneTest.aa();
//    }
    
//    @Deployment(testable = false)
//    public static EnterpriseArchive createDeployment() {
//    	 EnterpriseArchive ear = ShrinkWrap.create(ZipImporter.class, "eee.ear")
//       		  .importFrom(new File("../dist/eee.ear")).as(EnterpriseArchive.class) ;
//
//       	WebArchive war = ear.getAsType(WebArchive.class, "/eee-war.war");
//       	war.addPackage("com.eee.testutils");
//       	return ear;
//    }
   
    //https://gist.github.com/aslakknutsen/1358803
//    public static final List<String> GZIP_RESOURCES = Arrays.asList(
//    		"style.css", "index.html", "index.xhtml", "index.js", "index.main.dart", "js/index2.js");
//    @Rule
//    public ParameterRule rule = new ParameterRule(GZIP_RESOURCES);
    
    @Drone
    private WebDriver browser;
    
    private String currentWindow = null;

//    @ArquillianResource
//    private URL deploymentUrl;
//    <systemPropertyVariables>
//    <arq.extension.graphene.xxx>http://www.google.com</arq.extension.graphene.xxx>
//</systemPropertyVariables>
//    private String url = "http://demo.devbdg.work/login/login.xhtml";
    private String url = IntegrationTests.URL;
    
    @FindBy(id = "form:idConfirmDialogCenterOui")
    private WebElement idConfirmDialogCenterOui;
    
    @FindBy(id = "form:idConfirmDialogCenterNon")
    private WebElement idConfirmDialogCenterNon;
    //////////////////
    // Login / Logout
    //////////////////
//    @FindBy(id = "loginForm:j_username")           
//    private WebElement user;
//    
//    @FindBy(id = "loginForm:j_password")                            
//    private WebElement password;
//    
//    @FindBy(id = "loginForm:id_btn_login")
//    private WebElement loginButton;
//    
//    @FindBy(id = "idLogout")
//    private WebElement logoutLink;
    
    @Page
    LoginLogoutPage loginLogoutPage;
    
    //////////////////
    // Menu
    //////////////////
    @FindBy(id = "idFormMenu:idMenuParam")
    private WebElement actionMenuParam;
    
    @FindBy(id = "idFormMenu:idMenuParamTiers")
    private WebElement actionMenuParamTiers;
    
    @FindBy(id = "idFormMenu:idMenuParamTypeCivilite")
    private WebElement actionMenuTypeCivilite;
    
    //////////////////
    // Panel / DIV
    //////////////////
    @FindBy(id = "form:tabView:idTabTypeCivilite:typeCiviliteFormPrincipal")
    private WebElement idFormPrincipal;
    
    @FindBy(id = "idDialogTypeCivilite:typeCiviliteMessages")
    private WebElement idMessageJSFDialogue;
    
    ///////////////////////////
    // Boutons / Liens actions
    ///////////////////////////
    @FindBy(id = "idDialogTypeCivilite:idEnregistrerTypeCivilite")
    private WebElement idEnregistrer;
    
    @FindBy(id = "idDialogTypeCivilite:typeCiviliteAnnulerDialogue")
    private WebElement idAnnuler;
    
    @FindBy(id = "form:tabView:idTabTypeCivilite:idModifierTypeCivilite")
    private WebElement idModifier;
    
    @FindBy(id = "form:tabView:idTabTypeCivilite:typeCiviliteSupprimer")
    private WebElement idSupprimer;
    
    @FindBy(id = "form:tabView:idTabTypeCivilite:idInsererTypeCivilite")
    private WebElement idInserer;
    
    @FindBy(id = "form:tabView:idTabTypeCivilite:typeCiviliteFermer")
    private WebElement idFermer;
    
    ///////////////////////////
    // Champ de formulaire
    ///////////////////////////
    @FindBy(id = "idDialogTypeCivilite")
    private WebElement idDialogTypeCivilite;
    
    @FindBy(id = "form:tabView:idTabTypeCivilite:typeCiviliteDataTable")
    private WebElement idDatatablePrincipale;
    
    @FindBy(id = "idDialogTypeCivilite:idTxtCodeTypeCivilite")
    private WebElement idCode;
    
//    @FindBy(id = "idDialogTypeCivilite:idTxtLibcFamilleArticle")
//    private WebElement idLibelleFamilleArticle;
//    
//    @FindBy(id = "idDialogTypeCivilite:idTxtCommentaireFamilleArticle")
//    private WebElement idCommentaireFamilleArticle;
    
    //////////////////////////////////////
    // Réponses dialogue de confirmation
    //////////////////////////////////////
//    @FindBy(id = "form:tabView:idTabTypeCivilite:idOuiConfirmFermerFamilleArticle")
//    private WebElement idOuiConfirmFermerFamilleArticle;
//    
//    @FindBy(id = "form:tabView:idTabTypeCivilite:idNonConfirmFermerFamilleArticle")
//    private WebElement idNonConfirmFermerFamilleArticle;
//    
//    @FindBy(id = "form:tabView:idTabTypeCivilite:idOuiConfirmSupprFamilleArticle")
//    private WebElement idOuiConfirmSupprFamilleArticle;
//    
//    @FindBy(id = "form:tabView:idTabTypeCivilite:idNonConfirmSupprFamilleArticle")
//    private WebElement idNonConfirmSupprFamilleArticle;
    
    //////////////////////////////////////
    // Messages d'information ou d'erreur
    //////////////////////////////////////
    @FindBy(tagName = "li")             
    private WebElement facesMessage;

    private static final String messageErreurDefaut = "Champs obligatoire!";
    
//    private static final String messageErreurUnique = "unique constraint";
    private static final String messageErreurUnique = "contrainte unique";    
    
    private static String codePourSerie = null;
    
    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code   
    	System.out.println("@BeforeClass - oneTimeSetUp");
    	
    	TypeCiviliteTest test = new TypeCiviliteTest();
    	codePourSerie = test.genereCodeUnique();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    	System.out.println("@AfterClass - oneTimeTearDown");
    }
    
    @Before
    public void before() {
       	browser.get(url);
    	currentWindow = browser.getWindowHandle();
    	
    	//assertTrue(user.isDisplayed());
    	
//    	boolean existe = true;
//        try {
//        	logoutLink.isDisplayed();
//        }
//        catch (NoSuchElementException e){
//        	existe = false;
//        }
//        if(existe) {
//        	logout();
//        }

    	loginLogoutPage.getUser().clear();
    	loginLogoutPage.getPassword().clear();
    	
    	login();
        
    	ouvrirEcran();
    }
    
    @After
    public void after() {
    	browser.switchTo().window(currentWindow);
    	browser.get(IntegrationTests.BASE_URL);
    	
    	logout();
    }
    
    public boolean chercheTexteDansTable(WebElement datatable,String texte) {
    	
    	List<WebElement> tableCells = datatable.findElements(By.xpath("div/table/tbody/tr/td[1][text() = '"+texte+"']"));
    	return (tableCells!=null && !tableCells.isEmpty())?true:false;
//    	return (tableCells!=null && !tableCells.isEmpty())?false:true;
    	
//    	//XPath pour Datatable Primefaces
//	    List<WebElement> tableCells = datatable.findElements(By.xpath("div/table/tbody/tr/td"));
//	    boolean trouve = false;
//	    for(WebElement cell : tableCells) {
//	        System.out.println( "Value = " + cell.getText());
//	        if(cell.getText().equals(texte)) {
//	        	trouve = true;
//	        	//break;
//	        }
//	    }
//	    return trouve;
    }
    
    public WebElement chercheLigneDansTable(WebElement datatable,String texte) {
    	//XPath pour Datatable Primefaces    
    	WebElement trouve = null;
////	    By.xpath("//td[contains(lower-case(text()),'youruser')]")
//	    WebElement table = datatable.findElement(By.xpath("div/table"));
//	    // Get the rows which change always as and when users are added
//	    List<WebElement> lignesTr = table.findElements(By.xpath(".//tbody/tr"));
//	    // Loop through each row of users table
//	    for(WebElement l : lignesTr) {
//	       // Get the username
//	       WebElement code = l.findElement(By.xpath(".//td[1]"));
//	       System.out.println("code: " + code.getText());
//	       if(code.getText().equals(texte)) {
//	        	trouve = l;
//	        	break;
//	        }
//	    }
	    
    	try {
    		trouve = datatable.findElement(By.xpath("div/table/tbody/tr/td[1][text() = '"+texte+"']"));
	    } catch (NoSuchElementException ex){
	    	trouve = null;
	    }
    	return (trouve!=null)?trouve = trouve.findElement(By.xpath("..")):null;
	    
//	    return trouve;
    }
    
    public String messageJSFDialogue() {
    	waitAjax().until().element(idMessageJSFDialogue).is().visible();
//    	waitAjax().until().element(idMessageJSFDialogue.findElement(By.xpath("div/ul/li/span"))).is().visible();
    	return idMessageJSFDialogue.findElement(By.xpath("div/ul/li/span[2]")).getText();
    	//span[1] => titre
    	//span[2] => detail
    }
    
    public void login() {
    	loginLogoutPage.getUser().sendKeys("demo");
    	loginLogoutPage.getPassword().sendKeys("demo");
        
        guardHttp(loginLogoutPage.getLoginButton()).click();
        
        assertEquals("Dossier", facesMessage.getText().trim());
    }
    
    public void logout() {
//    	 guardAjax(logoutLink).click();
    	
    	loginLogoutPage.getLogoutLink().click();
    	waitAjax().until().element(loginLogoutPage.getUser()).is().visible();
    }
    
    public boolean elementExiste(WebElement e) {
	    boolean existe = true;
	    try {
	    	e.isDisplayed();
	    } catch (NoSuchElementException ex){
	    	existe = false;
	    }
	    return existe;
    }
    
    public void ouvrirEcran() {
    	Actions action = new Actions(browser);
        
        action.moveToElement(actionMenuParam).perform();
        action.moveToElement(actionMenuParamTiers).perform();
        waitGui().until().element(actionMenuTypeCivilite).is().visible();
        guardAjax(actionMenuTypeCivilite).click();

        waitAjax().until().element(idModifier).is().present();
    }
    
    public void waitDialogIsOpen() {
    	browser.switchTo().frame(browser.findElement(By.tagName("iframe")));
    	waitAjax().until().element(idAnnuler).is().visible();
    }
    
    public void waitDialogIsClose() {
    	try {
    		waitAjax().until("aaaaaaaaaeeeeeeeeee").element(idCode).is().not().present();
//    		WebDriverWait wait = new WebDriverWait(browser,10);
//    		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName);
	    } catch (NoSuchFrameException ex){
	    	//le dialogue est deja fermé
	    }
    	browser.switchTo().window(currentWindow);
    }
    
    public void verifEtatBoutonConsultation() {
    	assertTrue(idInserer.isEnabled());
    	assertTrue(idModifier.isEnabled());
    	assertTrue(idSupprimer.isEnabled());
    	assertTrue(idFermer.isEnabled());
    	//assertTrue(!idSupprimerFamilleArticle.isEnabled());
    }
    
    public void verifEtatBoutonModification() {
    	assertTrue(idEnregistrer.isEnabled());
    	assertTrue(idAnnuler.isEnabled());
    }
    
    public void verifEtatBoutonInsertion() {
    	assertTrue(idEnregistrer.isEnabled());
    	assertTrue(idAnnuler.isEnabled());
    }
    
    public void selectionDansTable() {
    	
    }
    
    /*------------------------------------------------------------------------*/
    /*                        CONSULTATION                                    */
    /*------------------------------------------------------------------------*/
//    @Test
//    public void consultation() {
//        login();
//        ouvrirEcran();
//        verifEtatBoutonConsultation();
//    }
    
    /*------------------------------------------------------------------------*/
    /*                        LISTE                                           */
    /*------------------------------------------------------------------------*/
    @Test
    public void ouvertureListe() {
        verifEtatBoutonConsultation();
    }
    
    /*------------------------------------------------------------------------*/
    /*                        FERMETURE                                       */
    /*------------------------------------------------------------------------*/
    @Test
    public void fermerConsultation() {

        idFermer.click();
        
        assertTrue(idConfirmDialogCenterOui.isDisplayed());
        
        idConfirmDialogCenterOui.click();
        
        boolean existe = elementExiste(idFormPrincipal);
        
        assertFalse(existe); // l'élément n'existe plus
    }
    
    /*------------------------------------------------------------------------*/
    /*                        INSERTION                                       */
    /*------------------------------------------------------------------------*/
    @Test
    @Ignore("Test à finir : Manque la confirmation d'annulation")
    public void insertionAnnulationNonValidee() {
        
        guardAjax(idInserer).click();
     
        waitDialogIsOpen();
        
//        idCodeFamilleArticle.sendKeys(VALEUR_CODE);
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        idAnnuler.click();
        
//        idOuiConfirmFermerFamilleArticle.click();
        
//        browser.switchTo().window(currentWindow);
        
        fail("Test à finir : Manque la confirmation d'annulation");
        

    }
    
    @Test
    @Ignore("Test à finir : Manque la confirmation d'annulation")
    public void insertionAnnulationValidee() {
        
        guardAjax(idInserer).click();
        
        waitDialogIsOpen();
        
//        idCodeFamilleArticle.sendKeys(VALEUR_CODE);
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        idAnnuler.click();
        
//        idOuiConfirmFermerFamilleArticle.click();
        
//        browser.switchTo().window(currentWindow);
        
        fail("Test à finir : Manque la confirmation d'annulation");
    }
    
    public String genereCodeUnique() {
    	String code = VALEUR_CODE+new Date().getTime();
    	return code;
    }
    
    @Test
    public void insertionChampsObligatoire() {
    	
        guardAjax(idInserer).click();
        
        waitDialogIsOpen();
        
        String code = genereCodeUnique();
        idCode.sendKeys(code);
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
        //idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        idEnregistrer.click();
        
        waitDialogIsClose();
        
        boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
        
        assertTrue(trouve);
        
        //fail("Test à finir : Vérifier que la nouvelle valeur est bien dans la liste");
    }
    
    @Test
    public void insertionVide() {
        
        guardAjax(idInserer).click();
        
        
        waitDialogIsOpen();
        
        idCode.clear();
//        idLibelleFamilleArticle.clear();
//        idCommentaireFamilleArticle.clear();
        
        guardAjax(idEnregistrer).click();
        
        //waitAjax().until().element(idCodeFamilleArticle).is().not().present();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
        //fail("Test à finir : Vérifier les messages d'erreur");
        
    }
    
    @Test
    public void insertionCodeObligatoire() {
            
        guardAjax(idInserer).click();
        
        waitDialogIsOpen();
        
        idCode.clear();
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
     
        guardAjax(idEnregistrer).click();
        
//        assertEquals("Champs obligatoire!", facesMessage.getText().trim());
        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        fail("Test à finir : Vérifier les messages d'erreurs");
    }
    
//    @Test
//    public void insertionLibelleObligatoire() {
//        
//        guardAjax(idInserer).click();
//        
//        waitDialogIsOpen();
//        
//        idCode.sendKeys(VALEUR_CODE);
////        idLibelleFamilleArticle.clear();
////        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
//        
//        guardAjax(idEnregistrer).click();
//        
//        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        
////        fail("Test à finir : Vérifier les messages d'erreurs");
//    }
    
    @Test
    public void insertionCodeUnique() {
        
        guardAjax(idInserer).click();
        
        waitDialogIsOpen();
        
        idCode.sendKeys(VALEUR_CODE);
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        guardAjax(idEnregistrer).click();
        
        boolean b = messageJSFDialogue().contains(messageErreurUnique);
        //assertEquals(messageErreurDefaut, messageJSFDialogue());
        assertTrue(b);
        
//        fail("Test à finir : Vérifier les messages d'erreurs");
    }
    
    @Test
    public void insertionComplet() {
        
        guardAjax(idInserer).click();
        
        waitDialogIsOpen();
        
        String code = genereCodeUnique();
        idCode.sendKeys(code);
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        guardAjax(idEnregistrer).click();
        
        waitDialogIsClose();
        
       // waitAjax().until().element(idCodeFamilleArticle).is().not().present();
        
//        fail("Test à finir : Vérifier que la nouvelle valeur est bien dans la liste");
        boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
        
        assertTrue(trouve);

    }
    
    /*------------------------------------------------------------------------*/
    /*                        MODIFICATION                                    */
    /*------------------------------------------------------------------------*/
    
    @Test
    @Ignore("Test à finir : Manque la confirmation d'annulation")
    public void modificationAnnulationNonValidee() {
        
        guardAjax(idModifier).click();
        
        waitDialogIsOpen();
        
//      idCodeFamilleArticle.sendKeys(VALEUR_CODE);
//      idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//      idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
      
        idAnnuler.click();
      
//      idOuiConfirmFermerFamilleArticle.click();
      
//      browser.switchTo().window(currentWindow);
      
        fail("Test à finir : Manque la confirmation d'annulation");
        

    }
    
    @Test
    @Ignore("Test à finir : Manque la confirmation d'annulation")
    public void modificationAnnulationValidee() {
        
        guardAjax(idModifier).click();
               
        waitDialogIsOpen();
        
        
        idAnnuler.click();
//        browser.close();
//        
////        browser.switchTo().frame(0);
//        //browser.switchTo().defaultContent();
        waitDialogIsClose();
//        
        //guardAjax(idModifierFamilleArticle).click();
        
        verifEtatBoutonConsultation();
//        
//        browser.switchTo().frame(browser.findElement(By.tagName("iframe")));
//        waitAjax().until().element(idAnnulerFamilleArticle).is().present();
     
//        waitAjax().until().element(idModifierInfosEntreprise).is().enabled();
       // assertEquals("Utilisateur ou mot de passe incorrects", facesMessage.getText().trim());
//        assertTrue(signedAs.getText().contains(VALEUR_CODE));
        
        fail("Test à finir : Manque la confirmation d'annulation");
    }
    
    @Test
    public void modificationChampsObligatoire() {
        
        guardAjax(idModifier).click();
        
        waitDialogIsOpen();
        
        String code = VALEUR_CODE;
        idCode.sendKeys(code);
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        idEnregistrer.click();
        
        waitDialogIsClose();
        
       // waitAjax().until().element(idCodeFamilleArticle).is().not().present();
        
        boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
        
        assertTrue(trouve);
        
        fail("Test à finir : Vérifier que la nouvelle valeur est bien dans la liste");
    }
    
    @Test
    public void modificationVide() {
        
        guardAjax(idModifier).click();
        
        waitDialogIsOpen();
        
        idCode.clear();
//        idLibelleFamilleArticle.clear();
//        idCommentaireFamilleArticle.clear();
        
        guardAjax(idEnregistrer).click();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        fail("Test à finir : Vérifier les messages d'erreur");
    }
    
    @Test
    public void modificationCodeObligatoire() {
        
        guardAjax(idModifier).click();
        
        waitDialogIsOpen();
        
        idCode.clear();
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        guardAjax(idEnregistrer).click();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        fail("Test à finir : Vérifier les messages d'erreur");
    }
    
//    @Test
//    public void modificationLibelleObligatoire() {
//        
//        guardAjax(idModifier).click();
//        
//        waitDialogIsOpen();
//        
//        idCode.sendKeys(VALEUR_CODE);
////        idLibelleFamilleArticle.clear();
////        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
//        
//        guardAjax(idEnregistrer).click();
//        
//        assertEquals(messageErreurDefaut, messageJSFDialogue());
////        fail("Test à finir : Vérifier les messages d'erreur");
//    }
    
    @Test
    public void modificationCodeUnique() {
        
        guardAjax(idModifier).click();
        
        waitDialogIsOpen();
         
         idCode.sendKeys(VALEUR_CODE);
//         idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//         idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
         
         guardAjax(idEnregistrer).click();
         
         boolean b = messageJSFDialogue().contains(messageErreurUnique);
         //assertEquals(messageErreurDefaut, messageJSFDialogue());
         assertTrue(b);
//         fail("Test à finir : Vérifier les messages d'erreur");
    }
    
    @Test
    public void modificationComplet() {
        
        guardAjax(idModifier).click();
        
        waitDialogIsOpen();
        
        String code = VALEUR_CODE;
        idCode.sendKeys(code);
//        idLibelleFamilleArticle.sendKeys(VALEUR_CODE);
//        idCommentaireFamilleArticle.sendKeys(VALEUR_CODE);
        
        idEnregistrer.click();
        
        waitDialogIsClose();
        
       // waitAjax().until().element(idCodeFamilleArticle).is().not().present();
        
        boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
        
        assertTrue(trouve);
        
//        fail("Test à finir : Vérifier que la nouvelle valeur est bien dans la liste");

    }
    
    /*------------------------------------------------------------------------*/
    /*                        SUPPRESSION                                     */
    /*------------------------------------------------------------------------*/
    @Test
    public void suppressionNonValidee() {
    	String code = VALEUR_CODE;
    	
//    	boolean trouve1 = chercheTexteDansTable(idDatatablePrincipale,code);
//        assertTrue(trouve1);
        
    	WebElement ligne = chercheLigneDansTable(idDatatablePrincipale,code);
    	if(ligne!=null) {
    		guardAjax(ligne).click();
    		idSupprimer.click();
    		
    		assertTrue(idConfirmDialogCenterNon.isDisplayed());
            
    		guardAjax(idConfirmDialogCenterNon).click();
            
            boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
            
            assertTrue(trouve);
    	} else {
    		fail("Ligne a supprimer non trouvée");    
    	}    
    }
    
    @Test
    public void suppressionValidee() {
    	String code = VALEUR_CODE;
    	
//    	boolean trouve1 = chercheTexteDansTable(idDatatablePrincipale,code);
//        assertTrue(trouve1);
        
    	WebElement ligne = chercheLigneDansTable(idDatatablePrincipale,code);
    	if(ligne!=null) {
    		guardAjax(ligne).click();
    		idSupprimer.click();
    		
    		assertTrue(idConfirmDialogCenterOui.isDisplayed());
            
    		guardAjax(idConfirmDialogCenterOui).click();
            
            boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
            
            assertFalse(trouve);
    	} else {
    		fail("Ligne a supprimer non trouvée");    
    	}    	
    }
}