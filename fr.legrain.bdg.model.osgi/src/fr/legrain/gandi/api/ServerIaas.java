package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

public class ServerIaas  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6494871715227778687L;
	/**
	 * @var Int
	 */
	private int ai_active;	
	/**
	 * @var Int
	 */
	private int console;
	/**
	 * @var String
	 */
	private String console_url;	
//	only returned with method hosting.vm.info() and vm.info();
	
	/**
	 * @var Int
	 */
	private int cores;
	
	/**
	 * @var Int
	 */
	private int datacenter_id;
	
	/**
	 * @var Int
	 */
	private Date date_created;
	/**
	 * @var Int
	 */
	private Date date_updated;
	
	/**
	 * @var String
	 */
	private String description;
	
	/**
	 * @var mixed
	 */
	private Object[]	disks;	
//	only returned with method hosting.vm.info() and vm.info();
	
	/**
	 * @var mixed
	 */
	private Object[] disks_id;
	
	/**
	 * @var String
	 */
	private String entity_id;
	/**
	 * @var String
	 */
	private String farm;
	/**
	 * @var Int
	 */
	private int flex_shares;
	
	/**
	 * @var mixed
	 */
	private HashMap graph_urls;
	
	
//	only returned with method hosting.vm.info() and vm.info();
	
	/**
	 * @var String
	 */
	private String hostname;
	/**
	 * @var String
	 */
	private String hvm_state;
//	only returned with method hosting.vm.info() and vm.info();
	
	/**
	 * @var Int
	 */
	private int id;
	
	/**
	 * @var mixed
	 */
	private Object[] ifaces;
//	only returned with method hosting.vm.info() and vm.info();
	
	/**
	 * @var mixed
	 */
	private Object[]  ifaces_id;
	
	/**
	 * @var boolean
	 */
	private boolean is_migrating;
	/**
	 * @var Int
	 */
	int memory;
	/**
	 * @var mixed
	 */
	private HashMap probes;
//	only returned with method hosting.vm.info() and vm.info();
	
	/**
	 * @var String
	 */
	private String state;
	
	/**
	 * @var mixed
	 */
	private Object[] triggers;
//	only returned with method hosting.vm.info() and vm.info();
	/**
	 * @var Int
	 */
	private int vm_max_memory;
	
	public ServerIaas(){
	}
	public ServerIaas(HashMap<String, Object> infos){

		this.ai_active = (int) infos.get("ai_active");
		this.console = (int) infos.get("console");
		this.console_url = (String) infos.get("console_url");
		this.cores = (int) infos.get("cores");
		this.datacenter_id = (int) infos.get("datacenter_id");
		this.date_created = (Date) infos.get("date_created");
		this.date_updated = (Date) infos.get("date_updated");
		this.description = (String) infos.get("description");
		this.disks = (Object[]) infos.get("disks");
		this.disks_id = (Object[]) infos.get("disks_id");
		this.farm = (String) infos.get("farm");
		this.flex_shares = (int) infos.get("flex_shares");
		this.graph_urls = (HashMap) infos.get("graph_url");
		this.hostname = (String) infos.get("hostname");
		this.hvm_state = (String) infos.get("hvm_state");
		this.id = (int) infos.get("id");
		this.ifaces = (Object[]) infos.get("ifaces");
		this.ifaces_id = (Object[]) infos.get("ifaces_id");
		this.is_migrating = (boolean) infos.get("is_migrating");
		this.memory = (int) infos.get("memory");
		this.probes = (HashMap) infos.get("probes");
		this.state = (String) infos.get("state");
		this.triggers = (Object[]) infos.get("triggers");
		this.vm_max_memory = (int) infos.get("vm_max_memory");
		
	}


	public int getAi_active() {
		return ai_active;
	}


	public void setAi_active(int ai_active) {
		this.ai_active = ai_active;
	}


	public int getConsole() {
		return console;
	}


	public void setConsole(int console) {
		this.console = console;
	}


	public String getConsole_url() {
		return console_url;
	}


	public void setConsole_url(String console_url) {
		this.console_url = console_url;
	}


	public int getCores() {
		return cores;
	}


	public void setCores(int cores) {
		this.cores = cores;
	}


	public int getDatacenter_id() {
		return datacenter_id;
	}


	public void setDatacenter_id(int datacenter_id) {
		this.datacenter_id = datacenter_id;
	}


	public Date getDate_created() {
		return date_created;
	}


	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}


	public Date getDate_updated() {
		return date_updated;
	}


	public void setDate_updated(Date date_updated) {
		this.date_updated = date_updated;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Object[] getDisks() {
		return disks;
	}


	public void setDisks(Object[] disks) {
		this.disks = disks;
	}


	public Object[] getDisks_id() {
		return disks_id;
	}


	public void setDisks_id(Object[] disks_id) {
		this.disks_id = disks_id;
	}


	public String getEntity_id() {
		return entity_id;
	}


	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}


	public String getFarm() {
		return farm;
	}


	public void setFarm(String farm) {
		this.farm = farm;
	}


	public int getFlex_shares() {
		return flex_shares;
	}


	public void setFlex_shares(int flex_shares) {
		this.flex_shares = flex_shares;
	}


	public HashMap getGraph_urls() {
		return graph_urls;
	}


	public void setGraph_urls(HashMap graph_urls) {
		this.graph_urls = graph_urls;
	}


	public String getHostname() {
		return hostname;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	public String getHvm_state() {
		return hvm_state;
	}


	public void setHvm_state(String hvm_state) {
		this.hvm_state = hvm_state;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Object[] getIfaces() {
		return ifaces;
	}


	public void setIfaces(Object[] ifaces) {
		this.ifaces = ifaces;
	}


	public Object[] getIfaces_id() {
		return ifaces_id;
	}


	public void setIfaces_id(Object[] ifaces_id) {
		this.ifaces_id = ifaces_id;
	}


	public boolean isIs_migrating() {
		return is_migrating;
	}


	public void setIs_migrating(boolean is_migrating) {
		this.is_migrating = is_migrating;
	}


	public int getMemory() {
		return memory;
	}


	public void setMemory(int memory) {
		this.memory = memory;
	}


	public HashMap getProbes() {
		return probes;
	}


	public void setProbes(HashMap probes) {
		this.probes = probes;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public Object[] getTriggers() {
		return triggers;
	}


	public void setTriggers(Object[] triggers) {
		this.triggers = triggers;
	}


	public int getVm_max_memory() {
		return vm_max_memory;
	}


	public void setVm_max_memory(int vm_max_memory) {
		this.vm_max_memory = vm_max_memory;
	}

}
