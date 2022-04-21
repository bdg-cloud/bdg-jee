package fr.legrain.bugzilla.rest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"can_login",
	"email",
	"email_enabled",
	"groups",
	"id",
	"login_denied_text",
	"name",
	"real_name",
	"saved_reports",
	"saved_searches"
})
public class User {

	@JsonProperty("can_login")
	private Boolean canLogin;
	@JsonProperty("email")
	private String email;
	@JsonProperty("email_enabled")
	private Boolean emailEnabled;
	@JsonProperty("groups")
	private List<Group> groups = new ArrayList<Group>();
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("login_denied_text")
	private String loginDeniedText;
	@JsonProperty("name")
	private String name;
	@JsonProperty("real_name")
	private String realName;
	@JsonProperty("saved_reports")
	private List<Object> savedReports = new ArrayList<Object>();
	@JsonProperty("saved_searches")
	private List<Object> savedSearches = new ArrayList<Object>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The canLogin
	 */
	@JsonProperty("can_login")
	public Boolean getCanLogin() {
		return canLogin;
	}

	/**
	 *
	 * @param canLogin
	 * The can_login
	 */
	@JsonProperty("can_login")
	public void setCanLogin(Boolean canLogin) {
		this.canLogin = canLogin;
	}

	/**
	 *
	 * @return
	 * The email
	 */
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	/**
	 *
	 * @param email
	 * The email
	 */
	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 *
	 * @return
	 * The emailEnabled
	 */
	@JsonProperty("email_enabled")
	public Boolean getEmailEnabled() {
		return emailEnabled;
	}

	/**
	 *
	 * @param emailEnabled
	 * The email_enabled
	 */
	@JsonProperty("email_enabled")
	public void setEmailEnabled(Boolean emailEnabled) {
		this.emailEnabled = emailEnabled;
	}

	/**
	 *
	 * @return
	 * The groups
	 */
	@JsonProperty("groups")
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 *
	 * @param groups
	 * The groups
	 */
	@JsonProperty("groups")
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 *
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 *
	 * @return
	 * The loginDeniedText
	 */
	@JsonProperty("login_denied_text")
	public String getLoginDeniedText() {
		return loginDeniedText;
	}

	/**
	 *
	 * @param loginDeniedText
	 * The login_denied_text
	 */
	@JsonProperty("login_denied_text")
	public void setLoginDeniedText(String loginDeniedText) {
		this.loginDeniedText = loginDeniedText;
	}

	/**
	 *
	 * @return
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 * The realName
	 */
	@JsonProperty("real_name")
	public String getRealName() {
		return realName;
	}

	/**
	 *
	 * @param realName
	 * The real_name
	 */
	@JsonProperty("real_name")
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 *
	 * @return
	 * The savedReports
	 */
	@JsonProperty("saved_reports")
	public List<Object> getSavedReports() {
		return savedReports;
	}

	/**
	 *
	 * @param savedReports
	 * The saved_reports
	 */
	@JsonProperty("saved_reports")
	public void setSavedReports(List<Object> savedReports) {
		this.savedReports = savedReports;
	}

	/**
	 *
	 * @return
	 * The savedSearches
	 */
	@JsonProperty("saved_searches")
	public List<Object> getSavedSearches() {
		return savedSearches;
	}

	/**
	 *
	 * @param savedSearches
	 * The saved_searches
	 */
	@JsonProperty("saved_searches")
	public void setSavedSearches(List<Object> savedSearches) {
		this.savedSearches = savedSearches;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}



