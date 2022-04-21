package fr.legrain.gandi.api;

import java.util.Date;
import java.util.HashMap;

//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.info#DomainReturn
//@XmlType(name="DomainGandi")
public class InfosAvailabilityDomainList  implements java.io.Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5282864302106878379L;

	/**
	 * @var mixed
	 */
	private String status;
	
	private String name;

	
	public InfosAvailabilityDomainList() {
		
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}





}