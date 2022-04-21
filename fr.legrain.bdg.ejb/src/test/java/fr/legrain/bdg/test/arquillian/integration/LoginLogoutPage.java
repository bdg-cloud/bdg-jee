package fr.legrain.bdg.test.arquillian.integration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginLogoutPage {

	//////////////////
	// Login / Logout
	//////////////////
	@FindBy(id = "loginForm:j_username")           
	private WebElement user;

	@FindBy(id = "loginForm:j_password")                            
	private WebElement password;

	@FindBy(id = "loginForm:id_btn_login")
	private WebElement loginButton;

	@FindBy(id = "idLogout")
	private WebElement logoutLink;
	
	@FindBy(id = "profile-image")
	private WebElement profileImage;

	public WebElement getUser() {
		return user;
	}

	public void setUser(WebElement user) {
		this.user = user;
	}

	public WebElement getPassword() {
		return password;
	}

	public void setPassword(WebElement password) {
		this.password = password;
	}

	public WebElement getLoginButton() {
		return loginButton;
	}

	public void setLoginButton(WebElement loginButton) {
		this.loginButton = loginButton;
	}

	public WebElement getLogoutLink() {
		return logoutLink;
	}

	public void setLogoutLink(WebElement logoutLink) {
		this.logoutLink = logoutLink;
	}

	public WebElement getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(WebElement profileImage) {
		this.profileImage = profileImage;
	}
	
	
}
