package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

public class InfosCertifSSL  implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7105555712564065014L;
	
	private Object[] altnames;	
//	Alt Name when the cert package permit it
	private String assumed_name	;
//	Does business as name
	private String business_category;	
//	Business Category (see Clause 5 of the EV Guidelines)
	private boolean card_pay_trustlogo; 
	private String cert;
	private String cn;
	private String comodo_id;	
	private String contact;
//	The certificate’s owner
	private String csr;
//	Certificate Signing Request
	private Date date_created;	 
	private Date date_end;	 
	private Date date_incorporation;	 
//	Date of Incorporation
	private Date date_start;	
	private Date date_updated;	 
	private int id;	
	private String ida_email;	
//	IdAuthority email address
	private String ida_fax;	
//	IdAuthority fax number
	private String ida_tel;	
//	IdAuthority telephone number
	private String joi_country;	
//	Jurisdiction of Incorporation: Country
	private String joi_locality;
//	Jurisdiction of Incorporation: Locality
	private String joi_state;	
//	Jurisdiction of Incorporation: State
	private String middleman;	
//	The contact who bought the certificate
	private String order_number;	
	private String package_gandi;	
//	Name of the Cert package
	private String provider_cert_id;
	private int sha_version;	
//	SHA version 1 or 2
	private int software;	
//	Software in which you will use the cert
	private String status;
	private boolean trustlogo;	
//	Add COMODO SSL TrustLogo to this package
	private HashMap trustlogo_token;	
	
	
	public InfosCertifSSL(){
	}
	public InfosCertifSSL(HashMap<String, Object> infos){

//		this.ai_active = (int) infos.get("ai_active");
		this.altnames = (Object[]) infos.get("altnames");;	
		this.assumed_name = (String) infos.get("assumed_name");
		this.business_category= (String) infos.get("business_category");	
		this.card_pay_trustlogo= (boolean) infos.get("card_pay_trustlogo"); 
		this.cert= (String) infos.get("cert");
		this.cn= (String) infos.get("cn");
		this.comodo_id= (String) infos.get("comodo_id");	
		this.contact= (String) infos.get("contact");
		this.csr= (String) infos.get("csr");
		this.date_created= (Date) infos.get("date_created");	 
		this.date_end= (Date) infos.get("date_end");	 
		this.date_incorporation= (Date) infos.get("date_incorporation");	 
		this.date_start= (Date) infos.get("date_start");	
		this.date_updated= (Date) infos.get("date_updated");	 
		this.id= (int) infos.get("id");	
		this.ida_email= (String) infos.get("ida_email");	
		this.ida_fax= (String) infos.get("ida_fax");	
		this.ida_tel= (String) infos.get("ida_tel");	
		this.joi_country= (String) infos.get("joi_country");	
		this.joi_locality= (String) infos.get("joi_locality");
		this.joi_state= (String) infos.get("joi_state");	
		this.middleman= (String) infos.get("middleman");	
		this.order_number= (String) infos.get("order_number");	
		this.package_gandi= (String) infos.get("package");	
		this.provider_cert_id= (String) infos.get("provider_cert_id");
		this.sha_version= (int) infos.get("sha_version");	
		this.software= (int) infos.get("software");	
		this.status= (String) infos.get("status");
		this.trustlogo= (boolean) infos.get("trustlogo");	
		this.trustlogo_token= (HashMap) infos.get("trustlogo_token");	

	}
	public Object[] getAltnames() {
		return altnames;
	}
	public void setAltnames(Object[] altnames) {
		this.altnames = altnames;
	}
	public String getAssumed_name() {
		return assumed_name;
	}
	public void setAssumed_name(String assumed_name) {
		this.assumed_name = assumed_name;
	}
	public String getBusiness_category() {
		return business_category;
	}
	public void setBusiness_category(String business_category) {
		this.business_category = business_category;
	}
	public boolean isCard_pay_trustlogo() {
		return card_pay_trustlogo;
	}
	public void setCard_pay_trustlogo(boolean card_pay_trustlogo) {
		this.card_pay_trustlogo = card_pay_trustlogo;
	}
	public String getCert() {
		return cert;
	}
	public void setCert(String cert) {
		this.cert = cert;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getComodo_id() {
		return comodo_id;
	}
	public void setComodo_id(String comodo_id) {
		this.comodo_id = comodo_id;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCsr() {
		return csr;
	}
	public void setCsr(String csr) {
		this.csr = csr;
	}
	public Date getDate_created() {
		return date_created;
	}
	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}
	public Date getDate_end() {
		return date_end;
	}
	public void setDate_end(Date date_end) {
		this.date_end = date_end;
	}
	public Date getDate_incorporation() {
		return date_incorporation;
	}
	public void setDate_incorporation(Date date_incorporation) {
		this.date_incorporation = date_incorporation;
	}
	public Date getDate_start() {
		return date_start;
	}
	public void setDate_start(Date date_start) {
		this.date_start = date_start;
	}
	public Date getDate_updated() {
		return date_updated;
	}
	public void setDate_updated(Date date_updated) {
		this.date_updated = date_updated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIda_email() {
		return ida_email;
	}
	public void setIda_email(String ida_email) {
		this.ida_email = ida_email;
	}
	public String getIda_fax() {
		return ida_fax;
	}
	public void setIda_fax(String ida_fax) {
		this.ida_fax = ida_fax;
	}
	public String getIda_tel() {
		return ida_tel;
	}
	public void setIda_tel(String ida_tel) {
		this.ida_tel = ida_tel;
	}
	public String getJoi_country() {
		return joi_country;
	}
	public void setJoi_country(String joi_country) {
		this.joi_country = joi_country;
	}
	public String getJoi_locality() {
		return joi_locality;
	}
	public void setJoi_locality(String joi_locality) {
		this.joi_locality = joi_locality;
	}
	public String getJoi_state() {
		return joi_state;
	}
	public void setJoi_state(String joi_state) {
		this.joi_state = joi_state;
	}
	public String getMiddleman() {
		return middleman;
	}
	public void setMiddleman(String middleman) {
		this.middleman = middleman;
	}
	public String getOrder_number() {
		return order_number;
	}
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	public String getPackage_gandi() {
		return package_gandi;
	}
	public void setPackage_gandi(String package_gandi) {
		this.package_gandi = package_gandi;
	}
	public String getProvider_cert_id() {
		return provider_cert_id;
	}
	public void setProvider_cert_id(String provider_cert_id) {
		this.provider_cert_id = provider_cert_id;
	}
	public int getSha_version() {
		return sha_version;
	}
	public void setSha_version(int sha_version) {
		this.sha_version = sha_version;
	}
	public int getSoftware() {
		return software;
	}
	public void setSoftware(int software) {
		this.software = software;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isTrustlogo() {
		return trustlogo;
	}
	public void setTrustlogo(boolean trustlogo) {
		this.trustlogo = trustlogo;
	}
	public HashMap getTrustlogo_token() {
		return trustlogo_token;
	}
	public void setTrustlogo_token(HashMap trustlogo_token) {
		this.trustlogo_token = trustlogo_token;
	}


}