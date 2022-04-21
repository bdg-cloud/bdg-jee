package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlType;
@XmlType(name="InfosPackMail")
public class InfosPackMail implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4754972450945110843L;
	
	


private Object[] autorenew;
private Date date_created;
private Date date_end;
private String domain;
private int domain_id;
private int forward_quota;
//The limit to the number of forwards
private int id;
private int mailbox_quota;
//The limit to the number of mailboxes
private String status;
private int storage_quota;	
//The storage limit, in Go


	public InfosPackMail() {
		
	}
	
	
	
	public InfosPackMail(HashMap<String, Object> infos){
	//	this.status = (Object[]) infos.get("status");
		this.autorenew = (Object[]) infos.get("autorenew");
		this.date_created = (Date) infos.get("date_created");
		this.date_end = (Date) infos.get("date_end");
		this.domain = (String) infos.get("domain");
		this.domain_id = (int) infos.get("domain_id");
		this.forward_quota = (int) infos.get("forward_quota");
		this.id = (int) infos.get("id");
		this.mailbox_quota = (int) infos.get("mailbox_quota");
		this.status = (String) infos.get("status");
		this.storage_quota = (int) infos.get("storage_quota");	
	
	}


public Object[] getAutorenew() {
	return autorenew;
}
public void setAutorenew(Object[] autorenew) {
	this.autorenew = autorenew;
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
public String getDomain() {
	return domain;
}
public void setDomain(String domain) {
	this.domain = domain;
}
public int getDomain_id() {
	return domain_id;
}
public void setDomain_id(int domain_id) {
	this.domain_id = domain_id;
}
public int getForward_quota() {
	return forward_quota;
}
public void setForward_quota(int forward_quota) {
	this.forward_quota = forward_quota;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getMailbox_quota() {
	return mailbox_quota;
}
public void setMailbox_quota(int mailbox_quota) {
	this.mailbox_quota = mailbox_quota;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public int getStorage_quota() {
	return storage_quota;
}
public void setStorage_quota(int storage_quota) {
	this.storage_quota = storage_quota;
}

}
