package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.info#DomainReturn
//@XmlType(name="DomainGandi")
public class InfosDomainList  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8856210201729371939L;

	/**
	 * @var mixed
	 */
	private Object[] status;


	/**
	 * @var Int

	 */
	private Date dateUpdated;

	/**
	 * @var Int
	 */
	private Date dateDelete;

	/**
	 * @var Int
	 */
	private Date dateHoldEnd;

	/**
	 * @var String
	 */
	private String fqdn;

	/**
	 * @var Int
	 */
	private Date dateRegistryEnd;

	/**
	 * @var mixed
	 */
	private Object[] nameservers;

	/**
	 * @var String
	 */
	private String authinfo;
	
	/**
	 * @var Date
	 */
	private Date authinfoExpirationDate;

	/**
	 * @var Int
	 */
	private Date dateRegistryCreation;


	/**
	 * @var String
	 */
	private String tld;


	/**
	 * @var Int
	 */
	private  Date dateCreated;


	/**
	 * @var Int
	 */
	private int id;

	/**
	 * @var Int
	 */
	private Date dateHoldBegin;
	/**
	 * @var boolean
	 */
	private boolean is_premium;
	/**
	 * @var boolean
	 */
	private boolean is_special_price;
	
	public InfosDomainList() {
		
	}



	public InfosDomainList(HashMap<String, Object> infos){
		this.status = (Object[]) infos.get("status");
		this.dateUpdated = (Date) infos.get("date_updated");
		this.dateDelete = (Date) infos.get("date_delete");
		this.dateHoldEnd = (Date) infos.get("date_hold_end");
		this.fqdn = (String) infos.get("fqdn");
		this.dateRegistryEnd = (Date) infos.get("date_registry_end");
		this.nameservers = (Object[]) infos.get("nameservers");
		this.authinfo = (String) infos.get("authinfo");
		this.dateRegistryCreation = (Date) infos.get("date_registry_creation");
		this.tld = (String) infos.get("tld");
		this.dateCreated = (Date) infos.get("date_created");
		this.id= (int) infos.get("id");
		this.dateHoldBegin = (Date) infos.get("date_hold_begin");
		this.is_premium = (boolean) infos.get("is_premium");
		this.is_special_price = (boolean) infos.get("is_special_price");
		this.authinfoExpirationDate = (Date) infos.get("authinfo_expiration_date");
	}


	public Date getAuthinfoExpirationDate() {
		return authinfoExpirationDate;
	}


	public void setAuthinfoExpirationDate(Date authinfoExpirationDate) {
		this.authinfoExpirationDate = authinfoExpirationDate;
	}


	public Object[] getStatus() {
		return status;
	}


	public void setStatus(Object[] status) {
		this.status = status;
	}


	public Date getDateUpdated() {
		return dateUpdated;
	}


	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}


	public Date getDateDelete() {
		return dateDelete;
	}


	public void setDateDelete(Date dateDelete) {
		this.dateDelete = dateDelete;
	}


	public Date getDateHoldEnd() {
		return dateHoldEnd;
	}


	public void setDateHoldEnd(Date dateHoldEnd) {
		this.dateHoldEnd = dateHoldEnd;
	}


	public String getFqdn() {
		return fqdn;
	}


	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}


	public Date getDateRegistryEnd() {
		return dateRegistryEnd;
	}


	public void setDateRegistryEnd(Date dateRegistryEnd) {
		this.dateRegistryEnd = dateRegistryEnd;
	}


	public Object[] getNameservers() {
		return nameservers;
	}


	public void setNameservers(Object[] nameservers) {
		this.nameservers = nameservers;
	}


	public String getAuthinfo() {
		return authinfo;
	}


	public void setAuthinfo(String authinfo) {
		this.authinfo = authinfo;
	}


	public Date getDateRegistryCreation() {
		return dateRegistryCreation;
	}


	public void setDateRegistryCreation(Date dateRegistryCreation) {
		this.dateRegistryCreation = dateRegistryCreation;
	}


	public String getTld() {
		return tld;
	}


	public void setTld(String tld) {
		this.tld = tld;
	}


	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getDateHoldBegin() {
		return dateHoldBegin;
	}


	public void setDateHoldBegin(Date dateHoldBegin) {
		this.dateHoldBegin = dateHoldBegin;
	}


	public boolean isIs_premium() {
		return is_premium;
	}


	public void setIs_premium(boolean is_premium) {
		this.is_premium = is_premium;
	}


	public boolean isIs_special_price() {
		return is_special_price;
	}


	public void setIs_special_price(boolean is_special_price) {
		this.is_special_price = is_special_price;
	}




}