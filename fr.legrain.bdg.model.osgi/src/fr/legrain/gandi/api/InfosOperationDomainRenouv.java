package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

public class InfosOperationDomainRenouv  implements java.io.Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8050340318702616839L;
	
	
	private Date date_created;
	private Date date_start;
	private Date date_updated;
	private Object[] errortype;
//	classification of the error cause if the operation is in error (possible values for errortype defined behind)
	private int eta;
//	estimated time of processing the operation
	private int id;
	private HashMap infos;
	private String last_error;	
//	last error of the operation
	private String last_support_error;	
//	last “support” error of the operation (that should be handled manually by the support team)
	private Object[] params;
	private int session_id;	
//	session number for the operation
	private String source;	
//	contact who create the operation
	private String step;	
//	current step of the operation
	private String type;	
	
	public InfosOperationDomainRenouv(){
	}
	
	public InfosOperationDomainRenouv(HashMap<String, Object> infos){

		  date_created = (Date) infos.get("date_created");
		  date_start = (Date) infos.get("date_start");
		  date_updated = (Date) infos.get("date_updated");
		  errortype = (Object[]) infos.get("errortype");
		  eta = (int) infos.get("eta");
		  id = (int) infos.get("id");
		  infos = (HashMap) infos.get("infos");
		  last_error = (String) infos.get("last_error");	
		  last_support_error = (String) infos.get("last_support_error");	
		  params = (Object[]) infos.get("params");
		  session_id = (int) infos.get("session_id");
		  source = (String) infos.get("source");	
		  step = (String) infos.get("step");
		  type = (String) infos.get("type");

		
	}

	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
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

	public Object[] getErrortype() {
		return errortype;
	}

	public void setErrortype(Object[] errortype) {
		this.errortype = errortype;
	}

	public int getEta() {
		return eta;
	}

	public void setEta(int eta) {
		this.eta = eta;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap getInfos() {
		return infos;
	}

	public void setInfos(HashMap infos) {
		this.infos = infos;
	}

	public String getLast_error() {
		return last_error;
	}

	public void setLast_error(String last_error) {
		this.last_error = last_error;
	}

	public String getLast_support_error() {
		return last_support_error;
	}

	public void setLast_support_error(String last_support_error) {
		this.last_support_error = last_support_error;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public int getSession_id() {
		return session_id;
	}

	public void setSession_id(int session_id) {
		this.session_id = session_id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	

}
