package fr.legrain.bdg.test.arquillian.integration;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class InfosEntrepriseScreenGrapheneTest {
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
    
//    @Deployment(testable = false)
//    public static EnterpriseArchive createDeployment() {
//    	 EnterpriseArchive ear = ShrinkWrap.create(ZipImporter.class, "eee.ear")
//       		  .importFrom(new File("../dist/eee.ear")).as(EnterpriseArchive.class) ;
//
//       	WebArchive war = ear.getAsType(WebArchive.class, "/eee-war.war");
//       	war.addPackage("com.eee.testutils");
//       	return ear;
//    }
   
    
    @Drone
    private WebDriver browser;

//    @ArquillianResource
//    private URL deploymentUrl;
//    <systemPropertyVariables>
//    <arq.extension.graphene.xxx>http://www.google.com</arq.extension.graphene.xxx>
//</systemPropertyVariables>
//    private String url = "http://demo.devbdg.work/login/login.xhtml";
    private String url = IntegrationTests.URL;
    
    @FindBy(id = "loginForm:j_username")                                  // 2. injects an element
    private WebElement user;
    
    @FindBy(id = "loginForm:j_password")                                  // 2. injects an element
    private WebElement password;
    
    @FindBy(id = "loginForm:id_btn_login")
    private WebElement loginButton;
    
    @FindBy(id = "idLogout")
    private WebElement logoutLink;
    
    @FindBy(id = "idFormMenu:idMenuDossier")
    private WebElement actionMenuDossier;
    
    @FindBy(id = "idFormMenu:idMenuInfosEntreprise")
    private WebElement actionMenuInfosEntreprise;
    
    @FindBy(id = "form:tabView:tab_0:idPanelIdentite")
    private WebElement panelIdentite;
    
    @FindBy(id = "form:tabView:tab_0:idEnregistrerInfosEntreprise")
    private WebElement idEnregistrerInfosEntreprise;
    
    @FindBy(id = "form:tabView:tab_0:idModifierInfosEntreprise")
    private WebElement idModifierInfosEntreprise;
    
    @FindBy(id = "form:tabView:tab_0:idNomInfosEntreprise")
    private WebElement idNomInfosEntreprise;
    
    @FindBy(tagName = "li")                     // 2. injects a first element with given tag name
    private WebElement facesMessage;
    
    
    @Test
    public void should_not_login() {
//    	URL url = new URL("http://www.google.com/");
    	
//    	browser.get("http://www.google.com/");
    	browser.get(url);
//        browser.get(deploymentUrl.toExternalForm() + "login.jsf");      // 1. open the tested page

    	user.clear();
    	password.clear();
    	
    	//user.sendKeys("demosdfsfs"); 
    	user.sendKeys("demo");// 3. control the page
        password.sendKeys("demo");
        
        //loginButton.click();
        guardHttp(loginButton).click();
        
        Actions action = new Actions(browser);
        
        action.moveToElement(actionMenuDossier).perform();
        guardAjax(actionMenuInfosEntreprise).click();

        waitAjax().until().element(idModifierInfosEntreprise).is().present();
        
        guardAjax(idModifierInfosEntreprise).click();
        
        idNomInfosEntreprise.sendKeys("demo");
        
        guardAjax(idEnregistrerInfosEntreprise).click();
        
        waitAjax().until().element(idModifierInfosEntreprise).is().enabled();

       // assertEquals("Utilisateur ou mot de passe incorrects", facesMessage.getText().trim());
//        whoAmI.click();
        
//        assertTrue(signedAs.getText().contains("demo"));
    }
    
    
}