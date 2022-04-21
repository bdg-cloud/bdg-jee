package fr.legrain.bdg.test.arquillian.integration;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

import fr.legrain.general.service.remote.IGenerationDonneesServiceRemote;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class FamilleArticleTest {
    private static final String WEBAPP_SRC = "src/main/webapp";
    
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
    
    //@Deployment(testable = false, name = "dep3", order = 3) //test client
    @Deployment(testable = true, name = "dep3", order = 3) //test serveur
    public static EnterpriseArchive createDeployment() {
    	
    	 EnterpriseArchive ear = ShrinkWrap.create(ZipImporter.class, "fr.legrain.bdg.ear.ear")
       		  .importFrom(new File("../fr.legrain.bdg.ear/target/fr.legrain.bdg.ear.ear")).as(EnterpriseArchive.class) ;
    	 
    	 JavaArchive jar = ear.getAsType(JavaArchive.class, "/fr.legrain.bdg.ejb.jar");

    	 jar.addPackage("fr.legrain.bdg.test.arquillian.integration");
    	 jar.addAsResource(new File("../arquillian.xml"));

       	return ear;
    }
    
    @Deployment(testable = false, name = "dep1", order = 1)
    public static EnterpriseArchive createAutorisationDeployment() {
    	 EnterpriseArchive ear = ShrinkWrap.create(ZipImporter.class, "fr.legrain.autorisations.ear.ear")
       		  .importFrom(new File("../fr.legrain.autorisations.ear/target/fr.legrain.autorisations.ear.ear")).as(EnterpriseArchive.class) ;
       	return ear;
    }
    
    @Deployment(testable = false, name = "dep2", order = 2)
    public static EnterpriseArchive createMonCompteDeployment() {
    	 EnterpriseArchive ear = ShrinkWrap.create(ZipImporter.class, "fr.legrain.moncompte.ear.ear")
       		  .importFrom(new File("../fr.legrain.moncompte.ear/target/fr.legrain.moncompte.ear.ear")).as(EnterpriseArchive.class) ;
       	return ear;
    }
   
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
    
    @FindBy(id = "idFormMenu:idMenuParamArticle")
    private WebElement actionMenuParamArticle;
    
    @FindBy(id = "idFormMenu:idMenuParamFamilleArticle")
    private WebElement actionMenuFamilleArticle;
    
    //////////////////
    // Panel / DIV
    //////////////////
    @FindBy(id = "form:tabView:idTabFamilleArticle:familleFormPrincipal")
    private WebElement idFormPrincipal;
    
    @FindBy(id = "idDialogFamilleArticle:familleMessages")
    private WebElement idMessageJSFDialogue;
    
    ///////////////////////////
    // Boutons / Liens actions
    ///////////////////////////
    @FindBy(id = "idDialogFamilleArticle:idEnregistrerFamilleArticle")
    private WebElement idEnregistrerFamilleArticle;
    
    @FindBy(id = "idDialogFamilleArticle:familleArticlesAnnulerDialogue")
    private WebElement idAnnulerFamilleArticle;
    
    @FindBy(id = "form:tabView:idTabFamilleArticle:idModifierFamilleArticle")
    private WebElement idModifierFamilleArticle;
    
    @FindBy(id = "form:tabView:idTabFamilleArticle:familleSupprimer")
    private WebElement idSupprimerFamilleArticle;
    
    @FindBy(id = "form:tabView:idTabFamilleArticle:idInsererFamilleArticle")
    private WebElement idInsererFamilleArticle;
    
    @FindBy(id = "form:tabView:idTabFamilleArticle:familleFermer")
    private WebElement idFermerFamilleArticle;
    
    ///////////////////////////
    // Champ de formulaire
    ///////////////////////////
    @FindBy(id = "idDialogFamilleArticle")
    private WebElement idDialogFamilleArticle;
    
    @FindBy(id = "form:tabView:idTabFamilleArticle:familleDataTable")
    private WebElement idDatatablePrincipale;
    
    @FindBy(id = "idDialogFamilleArticle:idTxtCodeFamilleArticle")
    private WebElement idCodeFamilleArticle;
    
    @FindBy(id = "idDialogFamilleArticle:idTxtLibcFamilleArticle")
    private WebElement idLibelleFamilleArticle;
    
    @FindBy(id = "idDialogFamilleArticle:idTxtCommentaireFamilleArticle")
    private WebElement idCommentaireFamilleArticle;
    
    //////////////////////////////////////
    // Réponses dialogue de confirmation
    //////////////////////////////////////
//    @FindBy(id = "form:tabView:idTabFamilleArticle:idOuiConfirmFermerFamilleArticle")
//    private WebElement idOuiConfirmFermerFamilleArticle;
//    
//    @FindBy(id = "form:tabView:idTabFamilleArticle:idNonConfirmFermerFamilleArticle")
//    private WebElement idNonConfirmFermerFamilleArticle;
//    
//    @FindBy(id = "form:tabView:idTabFamilleArticle:idOuiConfirmSupprFamilleArticle")
//    private WebElement idOuiConfirmSupprFamilleArticle;
//    
//    @FindBy(id = "form:tabView:idTabFamilleArticle:idNonConfirmSupprFamilleArticle")
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
    
    static private boolean init = false;
    private @EJB IGenerationDonneesServiceRemote generationDonneesService;
    
    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code   
    	System.out.println("@BeforeClass - oneTimeSetUp");
    	
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    	System.out.println("@AfterClass - oneTimeTearDown");
    }
    
    @Before
    public void before() {
    	if(!init) {
    		generationDonneesService.genTousLesParametres();
    		init = true;
    	}
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
        action.moveToElement(actionMenuParamArticle).perform();
        waitGui().until().element(actionMenuFamilleArticle).is().visible();
//        guardAjax(actionMenuFamilleArticle).click();
        actionMenuFamilleArticle.click();

        waitAjax().until().element(idModifierFamilleArticle).is().present();
    }
    
    public void waitDialogIsOpen() {
    	browser.switchTo().frame(browser.findElement(By.tagName("iframe")));
    	waitAjax().until().element(idAnnulerFamilleArticle).is().visible();
    }
    
    public void waitDialogIsClose() {
    	try {
    		waitAjax().until("La boite de dialogue ne se ferme pas.").element(idCodeFamilleArticle).is().not().present();
//    		WebDriverWait wait = new WebDriverWait(browser,10);
//    		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName);
	    } catch (NoSuchFrameException ex){
	    	//le dialogue est deja fermé
	    }
    	browser.switchTo().window(currentWindow);
    }
    
    public void verifEtatBoutonConsultation() {
    	assertTrue(idInsererFamilleArticle.isEnabled());
    	assertTrue(idModifierFamilleArticle.isEnabled());
    	assertTrue(idSupprimerFamilleArticle.isEnabled());
    	assertTrue(idFermerFamilleArticle.isEnabled());
    	//assertTrue(!idSupprimerFamilleArticle.isEnabled());
    }
    
    public void verifEtatBoutonModification() {
    	assertTrue(idEnregistrerFamilleArticle.isEnabled());
    	assertTrue(idAnnulerFamilleArticle.isEnabled());
    }
    
    public void verifEtatBoutonInsertion() {
    	assertTrue(idEnregistrerFamilleArticle.isEnabled());
    	assertTrue(idAnnulerFamilleArticle.isEnabled());
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
//    @OperateOnDeployment("dep1")
    public void ouvertureListe() {
        verifEtatBoutonConsultation();
    }
    
    /*------------------------------------------------------------------------*/
    /*                        FERMETURE                                       */
    /*------------------------------------------------------------------------*/
    @Test
    public void fermerConsultation() {

        idFermerFamilleArticle.click();
        
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
        
        guardAjax(idInsererFamilleArticle).click();
     
        waitDialogIsOpen();
        
//        idCodeFamilleArticle.sendKeys("demo");
//        idLibelleFamilleArticle.sendKeys("demo");
//        idCommentaireFamilleArticle.sendKeys("demo");
        
        idAnnulerFamilleArticle.click();
        
//        idOuiConfirmFermerFamilleArticle.click();
        
//        browser.switchTo().window(currentWindow);
        
        fail("Test à finir : Manque la confirmation d'annulation");
        

    }
    
    @Test
    @Ignore("Test à finir : Manque la confirmation d'annulation")
    public void insertionAnnulationValidee() {
        
        guardAjax(idInsererFamilleArticle).click();
        
        waitDialogIsOpen();
        
//        idCodeFamilleArticle.sendKeys("demo");
//        idLibelleFamilleArticle.sendKeys("demo");
//        idCommentaireFamilleArticle.sendKeys("demo");
        
        idAnnulerFamilleArticle.click();
        
//        idOuiConfirmFermerFamilleArticle.click();
        
//        browser.switchTo().window(currentWindow);
        
        fail("Test à finir : Manque la confirmation d'annulation");
    }
    
    public String genereCodeUnique() {
    	String code = "demo"+new Date().getTime();
    	return code;
    }
    
    @Test
    public void insertionChampsObligatoire() {
    	
        guardAjax(idInsererFamilleArticle).click();
        
        waitDialogIsOpen();
        
        String code = genereCodeUnique();
        idCodeFamilleArticle.sendKeys(code);
        idLibelleFamilleArticle.sendKeys("demo");
        //idCommentaireFamilleArticle.sendKeys("demo");
        
        idEnregistrerFamilleArticle.click();
        
        waitDialogIsClose();
        
        boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
        
        assertTrue(trouve);
        
        //fail("Test à finir : Vérifier que la nouvelle valeur est bien dans la liste");
    }
    
    @Test
    public void insertionVide() {
        
        guardAjax(idInsererFamilleArticle).click();
        
        
        waitDialogIsOpen();
        
        idCodeFamilleArticle.clear();
        idLibelleFamilleArticle.clear();
        idCommentaireFamilleArticle.clear();
        
        guardAjax(idEnregistrerFamilleArticle).click();
        
        //waitAjax().until().element(idCodeFamilleArticle).is().not().present();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
        //fail("Test à finir : Vérifier les messages d'erreur");
        
    }
    
    @Test
    public void insertionCodeObligatoire() {
            
        guardAjax(idInsererFamilleArticle).click();
        
        waitDialogIsOpen();
        
        idCodeFamilleArticle.clear();
        idLibelleFamilleArticle.sendKeys("demo");
        idCommentaireFamilleArticle.sendKeys("demo");
     
        guardAjax(idEnregistrerFamilleArticle).click();
        
//        assertEquals("Champs obligatoire!", facesMessage.getText().trim());
        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        fail("Test à finir : Vérifier les messages d'erreurs");
    }
    
    @Test
    public void insertionLibelleObligatoire() {
        
        guardAjax(idInsererFamilleArticle).click();
        
        waitDialogIsOpen();
        
        idCodeFamilleArticle.sendKeys("demo");
        idLibelleFamilleArticle.clear();
        idCommentaireFamilleArticle.sendKeys("demo");
        
        guardAjax(idEnregistrerFamilleArticle).click();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
        
//        fail("Test à finir : Vérifier les messages d'erreurs");
    }
    
    @Test
    public void insertionCodeUnique() {
        
        guardAjax(idInsererFamilleArticle).click();
        
        waitDialogIsOpen();
        
        idCodeFamilleArticle.sendKeys("demo");
        idLibelleFamilleArticle.sendKeys("demo");
        idCommentaireFamilleArticle.sendKeys("demo");
        
        guardAjax(idEnregistrerFamilleArticle).click();
        
        boolean b = messageJSFDialogue().contains(messageErreurUnique);
        //assertEquals(messageErreurDefaut, messageJSFDialogue());
        assertTrue(b);
        
//        fail("Test à finir : Vérifier les messages d'erreurs");
    }
    
    @Test
    public void insertionComplet() {
        
        guardAjax(idInsererFamilleArticle).click();
        
        waitDialogIsOpen();
        
        String code = genereCodeUnique();
        idCodeFamilleArticle.sendKeys(code);
        idLibelleFamilleArticle.sendKeys("demo");
        idCommentaireFamilleArticle.sendKeys("demo");
        
        guardAjax(idEnregistrerFamilleArticle).click();
        
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
        
        guardAjax(idModifierFamilleArticle).click();
        
        waitDialogIsOpen();
        
//      idCodeFamilleArticle.sendKeys("demo");
//      idLibelleFamilleArticle.sendKeys("demo");
//      idCommentaireFamilleArticle.sendKeys("demo");
      
        idAnnulerFamilleArticle.click();
      
//      idOuiConfirmFermerFamilleArticle.click();
      
//      browser.switchTo().window(currentWindow);
      
        fail("Test à finir : Manque la confirmation d'annulation");
        

    }
    
    @Test
    @Ignore("Test à finir : Manque la confirmation d'annulation")
    public void modificationAnnulationValidee() {
        
        guardAjax(idModifierFamilleArticle).click();
               
        waitDialogIsOpen();
        
        
        idAnnulerFamilleArticle.click();
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
//        assertTrue(signedAs.getText().contains("demo"));
        
        fail("Test à finir : Manque la confirmation d'annulation");
    }
    
    @Test
    public void modificationChampsObligatoire() {
        
        guardAjax(idModifierFamilleArticle).click();
        
        waitDialogIsOpen();
        
        String code = "demo";
        idCodeFamilleArticle.sendKeys(code);
        idLibelleFamilleArticle.sendKeys("demo");
        idCommentaireFamilleArticle.sendKeys("demo");
        
        idEnregistrerFamilleArticle.click();
        
        waitDialogIsClose();
        
       // waitAjax().until().element(idCodeFamilleArticle).is().not().present();
        
        boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
        
        assertTrue(trouve);
        
        fail("Test à finir : Vérifier que la nouvelle valeur est bien dans la liste");
    }
    
    @Test
    public void modificationVide() {
        
        guardAjax(idModifierFamilleArticle).click();
        
        waitDialogIsOpen();
        
        idCodeFamilleArticle.clear();
        idLibelleFamilleArticle.clear();
        idCommentaireFamilleArticle.clear();
        
        guardAjax(idEnregistrerFamilleArticle).click();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        fail("Test à finir : Vérifier les messages d'erreur");
    }
    
    @Test
    public void modificationCodeObligatoire() {
        
        guardAjax(idModifierFamilleArticle).click();
        
        waitDialogIsOpen();
        
        idCodeFamilleArticle.clear();
        idLibelleFamilleArticle.sendKeys("demo");
        idCommentaireFamilleArticle.sendKeys("demo");
        
        guardAjax(idEnregistrerFamilleArticle).click();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        fail("Test à finir : Vérifier les messages d'erreur");
    }
    
    @Test
    public void modificationLibelleObligatoire() {
        
        guardAjax(idModifierFamilleArticle).click();
        
        waitDialogIsOpen();
        
        idCodeFamilleArticle.sendKeys("demo");
        idLibelleFamilleArticle.clear();
        idCommentaireFamilleArticle.sendKeys("demo");
        
        guardAjax(idEnregistrerFamilleArticle).click();
        
        assertEquals(messageErreurDefaut, messageJSFDialogue());
//        fail("Test à finir : Vérifier les messages d'erreur");
    }
    
    @Test
    public void modificationCodeUnique() {
        
        guardAjax(idModifierFamilleArticle).click();
        
        waitDialogIsOpen();
         
         idCodeFamilleArticle.sendKeys("demo");
         idLibelleFamilleArticle.sendKeys("demo");
         idCommentaireFamilleArticle.sendKeys("demo");
         
         guardAjax(idEnregistrerFamilleArticle).click();
         
         boolean b = messageJSFDialogue().contains(messageErreurUnique);
         //assertEquals(messageErreurDefaut, messageJSFDialogue());
         assertTrue(b);
//         fail("Test à finir : Vérifier les messages d'erreur");
    }
    
    @Test
    public void modificationComplet() {
        
        guardAjax(idModifierFamilleArticle).click();
        
        waitDialogIsOpen();
        
        String code = "demo";
        idCodeFamilleArticle.sendKeys(code);
        idLibelleFamilleArticle.sendKeys("demo");
        idCommentaireFamilleArticle.sendKeys("demo");
        
        idEnregistrerFamilleArticle.click();
        
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
    	String code = "demo1464087167125";
    	
//    	boolean trouve1 = chercheTexteDansTable(idDatatablePrincipale,code);
//        assertTrue(trouve1);
        
    	WebElement ligne = chercheLigneDansTable(idDatatablePrincipale,code);
    	if(ligne!=null) {
    		guardAjax(ligne).click();
    		idSupprimerFamilleArticle.click();
    		
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
    	String code = "demo1464087167125";
    	
//    	boolean trouve1 = chercheTexteDansTable(idDatatablePrincipale,code);
//        assertTrue(trouve1);
        
    	WebElement ligne = chercheLigneDansTable(idDatatablePrincipale,code);
    	if(ligne!=null) {
    		guardAjax(ligne).click();
    		idSupprimerFamilleArticle.click();
    		
    		assertTrue(idConfirmDialogCenterOui.isDisplayed());
            
    		guardAjax(idConfirmDialogCenterOui).click();
            
            boolean trouve = chercheTexteDansTable(idDatatablePrincipale,code);
            
            assertFalse(trouve);
    	} else {
    		fail("Ligne a supprimer non trouvée");    
    	}    	
    }
}