package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.info#DomainReturn
//@XmlType(name="DomainGandi")
public class Domain  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8856210201729371939L;

	/**
	 * @var mixed
	 */
	public Object[] status;

	/**
	 * @var Int
	 */
	public Date datePendingDeleteEnd;

	/**
	 * @var Int
	 */
	public int zoneId;

	/**
	 * @var mixed
	 */
	public Object[] tags;

	/**
	 * @var Int

	 */
	public Date dateUpdated;

	/**
	 * @var Int
	 */
	public Date dateDelete;

	/**
	 * @var Int
	 */
	public Date dateHoldEnd;

	/**
	 * @var String
	 */
	public String fqdn;

	/**
	 * @var Int
	 */
	public Date dateRegistryEnd;

	/**
	 * @var mixed
	 */
	public Object[] nameservers;

	/**
	 * @var String
	 */
	public String authinfo;

	/**
	 * @var Int
	 */
	public Date dateRegistryCreation;

	/**
	 * @var Int
	 */
	public Date dateRenewBegin;

	/**
	 * @var String
	 */
	public String tld;

	/**
	 * @var mixed
	 */
	public Object[] services;

	/**
	 * @var Int
	 */
	public  Date dateCreated;

	/**
	 * @var Int
	 */
	public Date dateRestoreEnd;

	/**
	 * @var String
	 */
	public String autorenew;

	/**
	 * @var mixed
	 */
	public HashMap contacts;

	/**
	 * @var Int
	 */
	public int id;

	/**
	 * @var Int
	 */
	public Date dateHoldBegin;
	
	public Domain() {
		
	}


	/**
	 * @param mixed $status
	 * @param Int $datePendingDeleteEnd
	 * @param Int $zoneId
	 * @param mixed $tags
	 * @param Int $dateUpdated
	 * @param Int $dateDelete
	 * @param Int $dateHoldEnd
	 * @param String $fqdn
	 * @param Int $dateRegistryEnd
	 * @param mixed $nameservers
	 * @param String $authinfo
	 * @param Int $dateRegistryCreation
	 * @param Int $dateRenewBegin
	 * @param String $tld
	 * @param mixed $services
	 * @param Int $dateCreated
	 * @param Int $dateRestoreEnd
	 * @param String $autorenew
	 * @param mixed $contacts
	 * @param Int $id
	 * @param Int $dateHoldBegin
	 */
	public Domain(HashMap<String, Object> infos){
		this.status = (Object[]) infos.get("status");
		this.datePendingDeleteEnd = (Date) infos.get("date_pending_delete_end");
		this.zoneId = (int) infos.get("zone_id");
		this.tags = (Object[]) infos.get("tags");
		this.dateUpdated = (Date) infos.get("date_updated");
		this.dateDelete = (Date) infos.get("date_delete");
		this.dateHoldEnd = (Date) infos.get("date_hold_end");
		this.fqdn = (String) infos.get("fqdn");
		this.dateRegistryEnd = (Date) infos.get("date_registry_end");
		this.nameservers = (Object[]) infos.get("nameservers");
		this.authinfo = (String) infos.get("authinfo");
		this.dateRegistryCreation = (Date) infos.get("date_registry_creation");
		this.dateRenewBegin = (Date) infos.get("date_renew_begin");
		this.tld = (String) infos.get("tld");
		this.services = (Object[]) infos.get("services");
		this.dateCreated = (Date) infos.get("date_created");
		this.dateRestoreEnd = (Date) infos.get("date_restore_end");
		this.autorenew = (String) infos.get("autorenew");
		this.contacts = (HashMap) infos.get("contacts");
		this.id= (int) infos.get("id");
		this.dateHoldBegin = (Date) infos.get("date_hold_begin");
	}




}