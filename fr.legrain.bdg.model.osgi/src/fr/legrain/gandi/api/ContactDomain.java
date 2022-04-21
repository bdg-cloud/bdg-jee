package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

public class ContactDomain implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4495577769196344966L;
	
	
	private String brand_number;
	private Object[] bu;	
	private String city	;
	private boolean community;
	private Object[] country;	
	private int data_obfuscated;
	private String email;
	private Object[] extra_parameters;	
	private String family;	
	private String fax;	
	private String given;	
	private String handle;	
	private int id;
	private boolean is_corporate;	
	private String jo_announce_number;	
	private String jo_announce_page;	
	private String jo_declaration_date;
	private String jo_publication_date;	
	private String lang;
	private int mail_obfuscated;	
	private String mobile;	
	private int newsletter;
	private String orgname;	
	private String phone;	
	private String reachability;	
	private String security_question_answer;	
	private String empty; 
	private int security_question_num;	
	private Object[] shippingaddress;	
	private String siren;	
	private String state;
	private String streetaddr;	
	private int third_part_resell;	
	private int type;	
	private String validation;	
	private String vat_number;
	private String zip;	
	
	
	
	
	public ContactDomain(){	
		
	}
	public ContactDomain(HashMap<String, Object> infos){

		  brand_number = (String) infos.get("ai_active");
		  bu= (Object[]) infos.get("ai_active");
		  city= (String) infos.get("ai_active");
		  community= (boolean) infos.get("ai_active");
		  country= (Object[]) infos.get("ai_active");
		  data_obfuscated= (int) infos.get("ai_active");
		  email= (String) infos.get("ai_active");
		  extra_parameters= (Object[]) infos.get("ai_active");
		  family= (String) infos.get("ai_active");
		  fax= (String) infos.get("ai_active");
		  given= (String) infos.get("ai_active");
		  handle= (String) infos.get("ai_active");
		  id= (int) infos.get("ai_active");
		  is_corporate= (boolean) infos.get("ai_active");
		  jo_announce_number= (String) infos.get("ai_active");
		  jo_announce_page= (String) infos.get("ai_active");
		  jo_declaration_date= (String) infos.get("ai_active");
		  jo_publication_date= (String) infos.get("ai_active");
		  lang= (String) infos.get("ai_active");
		  mail_obfuscated= (int) infos.get("ai_active");
		  mobile= (String) infos.get("ai_active");
		  newsletter= (int) infos.get("ai_active");
		  orgname= (String) infos.get("ai_active");
		  phone= (String) infos.get("ai_active");
		  reachability= (String) infos.get("ai_active");
		  security_question_answer= (String) infos.get("ai_active");
		  empty= (String) infos.get("ai_active");
		  security_question_num= (int) infos.get("ai_active");
		  shippingaddress= (Object[]) infos.get("ai_active");
		  siren= (String) infos.get("ai_active");
		  state= (String) infos.get("ai_active");
		  streetaddr= (String) infos.get("ai_active");
		  third_part_resell= (int) infos.get("ai_active");
		  type= (int) infos.get("ai_active");
		  validation= (String) infos.get("ai_active");
		  vat_number= (String) infos.get("ai_active");
		  zip= (String) infos.get("ai_active");		
		
	}
	public String getBrand_number() {
		return brand_number;
	}
	public void setBrand_number(String brand_number) {
		this.brand_number = brand_number;
	}
	public Object[] getBu() {
		return bu;
	}
	public void setBu(Object[] bu) {
		this.bu = bu;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean isCommunity() {
		return community;
	}
	public void setCommunity(boolean community) {
		this.community = community;
	}
	public Object[] getCountry() {
		return country;
	}
	public void setCountry(Object[] country) {
		this.country = country;
	}
	public int getData_obfuscated() {
		return data_obfuscated;
	}
	public void setData_obfuscated(int data_obfuscated) {
		this.data_obfuscated = data_obfuscated;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Object[] getExtra_parameters() {
		return extra_parameters;
	}
	public void setExtra_parameters(Object[] extra_parameters) {
		this.extra_parameters = extra_parameters;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getGiven() {
		return given;
	}
	public void setGiven(String given) {
		this.given = given;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isIs_corporate() {
		return is_corporate;
	}
	public void setIs_corporate(boolean is_corporate) {
		this.is_corporate = is_corporate;
	}
	public String getJo_announce_number() {
		return jo_announce_number;
	}
	public void setJo_announce_number(String jo_announce_number) {
		this.jo_announce_number = jo_announce_number;
	}
	public String getJo_announce_page() {
		return jo_announce_page;
	}
	public void setJo_announce_page(String jo_announce_page) {
		this.jo_announce_page = jo_announce_page;
	}
	public String getJo_declaration_date() {
		return jo_declaration_date;
	}
	public void setJo_declaration_date(String jo_declaration_date) {
		this.jo_declaration_date = jo_declaration_date;
	}
	public String getJo_publication_date() {
		return jo_publication_date;
	}
	public void setJo_publication_date(String jo_publication_date) {
		this.jo_publication_date = jo_publication_date;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public int getMail_obfuscated() {
		return mail_obfuscated;
	}
	public void setMail_obfuscated(int mail_obfuscated) {
		this.mail_obfuscated = mail_obfuscated;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getNewsletter() {
		return newsletter;
	}
	public void setNewsletter(int newsletter) {
		this.newsletter = newsletter;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getReachability() {
		return reachability;
	}
	public void setReachability(String reachability) {
		this.reachability = reachability;
	}
	public String getSecurity_question_answer() {
		return security_question_answer;
	}
	public void setSecurity_question_answer(String security_question_answer) {
		this.security_question_answer = security_question_answer;
	}
	public String getEmpty() {
		return empty;
	}
	public void setEmpty(String empty) {
		this.empty = empty;
	}
	public int getSecurity_question_num() {
		return security_question_num;
	}
	public void setSecurity_question_num(int security_question_num) {
		this.security_question_num = security_question_num;
	}
	public Object[] getShippingaddress() {
		return shippingaddress;
	}
	public void setShippingaddress(Object[] shippingaddress) {
		this.shippingaddress = shippingaddress;
	}
	public String getSiren() {
		return siren;
	}
	public void setSiren(String siren) {
		this.siren = siren;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStreetaddr() {
		return streetaddr;
	}
	public void setStreetaddr(String streetaddr) {
		this.streetaddr = streetaddr;
	}
	public int getThird_part_resell() {
		return third_part_resell;
	}
	public void setThird_part_resell(int third_part_resell) {
		this.third_part_resell = third_part_resell;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValidation() {
		return validation;
	}
	public void setValidation(String validation) {
		this.validation = validation;
	}
	public String getVat_number() {
		return vat_number;
	}
	public void setVat_number(String vat_number) {
		this.vat_number = vat_number;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

}
