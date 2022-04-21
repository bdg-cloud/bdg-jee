package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.info#DomainReturn
//@XmlType(name="DomainGandi")
public class InfosSimpleHostingList  implements java.io.Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -5483642746461842663L;


	private String catalog_name;
//	Name of the product in the catalog
	private String console;	
//	The console url if any (that does not mean it’s activated)
	private int  data_disk_additional_size;
//	Amount of additional disk (in GB)
	private int  datacenter_id;
//	id of the datacenter of the instance
	private Date date_end;
//	Expiration date of the instance
	private Date date_end_commitment;
//	Date of the end of the commitment
	private Date date_start;
//	Creation date of the instance
	private String entity_id;
	private int  id;
//	ID of the instance
	private String name;
//	Name of the instance
	private boolean need_upgrade;
//	Whether or not the instance needs to be upgraded
	private Object[] servers;
//	List of servers in the instance
	private String size;
//	Size of the instance
	private HashMap snapshot_profile;
//	Snapshot profile affected to this instance
	private String state;
//	Administrative state of the instance
	private String type;
	
	
	public InfosSimpleHostingList() {
		
	}



	public InfosSimpleHostingList(HashMap<String, Object> infos){
//		this.status = (Object[]) infos.get("status");
		this.console= (String) infos.get("console");	
		this.data_disk_additional_size= (int) infos.get("data_disk_additional_size");
		this. datacenter_id= (int) infos.get("datacenter_id");
		this.date_end= (Date) infos.get("date_end");
		this.date_end_commitment= (Date) infos.get("date_end_commitment");
		this.date_start= (Date) infos.get("date_start");
		this.entity_id= (String) infos.get("entity_id");
		this.id= (int) infos.get("id");
		this.name= (String) infos.get("name");
		this.need_upgrade= (boolean) infos.get("need_upgrade");
		this.servers= (Object[]) infos.get("servers");
		this.size= (String) infos.get("size");
		this.snapshot_profile= (HashMap) infos.get("snapshot_profile");
		this.state= (String) infos.get("state");
		this.type= (String) infos.get("type");
	}



	public String getCatalog_name() {
		return catalog_name;
	}



	public void setCatalog_name(String catalog_name) {
		this.catalog_name = catalog_name;
	}



	public String getConsole() {
		return console;
	}



	public void setConsole(String console) {
		this.console = console;
	}



	public int getData_disk_additional_size() {
		return data_disk_additional_size;
	}



	public void setData_disk_additional_size(int data_disk_additional_size) {
		this.data_disk_additional_size = data_disk_additional_size;
	}



	public int getDatacenter_id() {
		return datacenter_id;
	}



	public void setDatacenter_id(int datacenter_id) {
		this.datacenter_id = datacenter_id;
	}



	public Date getDate_end() {
		return date_end;
	}



	public void setDate_end(Date date_end) {
		this.date_end = date_end;
	}



	public Date getDate_end_commitment() {
		return date_end_commitment;
	}



	public void setDate_end_commitment(Date date_end_commitment) {
		this.date_end_commitment = date_end_commitment;
	}



	public Date getDate_start() {
		return date_start;
	}



	public void setDate_start(Date date_start) {
		this.date_start = date_start;
	}



	public String getEntity_id() {
		return entity_id;
	}



	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public boolean isNeed_upgrade() {
		return need_upgrade;
	}



	public void setNeed_upgrade(boolean need_upgrade) {
		this.need_upgrade = need_upgrade;
	}



	public Object[] getServers() {
		return servers;
	}



	public void setServers(Object[] servers) {
		this.servers = servers;
	}



	public String getSize() {
		return size;
	}



	public void setSize(String size) {
		this.size = size;
	}



	public HashMap getSnapshot_profile() {
		return snapshot_profile;
	}



	public void setSnapshot_profile(HashMap snapshot_profile) {
		this.snapshot_profile = snapshot_profile;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}





}