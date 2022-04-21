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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class LoginScreenGrapheneTest {
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
    
    @FindBy(id = "idFormMenu:idMenuInfosEntreprise")
    private WebElement actionMenuInfosEntreprise;
    
    @FindBy(tagName = "li")                     // 2. injects a first element with given tag name
    private WebElement facesMessage;
    
//    @FindByJQuery("p:visible")                  // 3. injects an element using jQuery selector
//    private WebElement signedAs;
//
//    @FindBy(css = "input[type=submit]")
//    private WebElement whoAmI;
    
    
    @Before
    public void before() {
//    	URL url = new URL("http://www.google.com/");
    	
//    	browser.get("http://www.google.com/");
    	browser.get(url);
//      browser.get(deploymentUrl.toExternalForm() + "login.jsf");      // 1. open the tested page

    	user.clear();
    	password.clear();
    }
    
    @Test
    public void should_not_login() {

    	user.sendKeys("demo9");
        password.sendKeys("demo");
        
        guardHttp(loginButton).click();

        assertEquals("Utilisateur ou mot de passe incorrects", facesMessage.getText().trim());
        
//        whoAmI.click();
//        waitAjax().until().element(signedAs).is().present();
//        assertTrue(signedAs.getText().contains("demo"));
    }
    
    @Test
    public void should_login_successfully() {

    	user.sendKeys("demo");
        password.sendKeys("demo");
        
        guardHttp(loginButton).click();

        assertEquals("Dossier", facesMessage.getText().trim());
    }
    
}