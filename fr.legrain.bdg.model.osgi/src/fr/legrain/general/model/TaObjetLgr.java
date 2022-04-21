package fr.legrain.general.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;


//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TaObjetLgr implements Serializable {

	private static final long serialVersionUID = 496592244972519100L;


	@Transient
	protected String quiCree;
	@Transient
	protected Date quandCree;
	@Transient
	protected String quiModif;
	@Transient
	protected Date quandModif;
	@Transient
	protected String version;
	@Transient
	protected String ipAcces;

	
//	protected Integer versionObj;

	public TaObjetLgr() {
	}



	@Transient
	public String getQuiCree() {
		return quiCree;
	}

	/**
	 * @param quiCree the quiCree to set
	 */
	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	/**
	 * @return the quandCree
	 */
	@Transient
	public Date getQuandCree() {
		return quandCree;
	}

	/**
	 * @param quandCree the quandCree to set
	 */
	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}

	/**
	 * @return the quiModif
	 */
	@Transient
	public String getQuiModif() {
		return quiModif;
	}

	/**
	 * @param quiModif the quiModif to set
	 */
	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}

	/**
	 * @return the quandModif
	 */
	@Transient
	public Date getQuandModif() {
		return quandModif;
	}

	/**
	 * @param quandModif the quandModif to set
	 */
	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	/**
	 * @return the version
	 */
	@Transient
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the ipAcces
	 */
	@Transient
	public String getIpAcces() {
		return ipAcces;
	}

	/**
	 * @param ipAcces the ipAcces to set
	 */
	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

//	/**
//	 * @return the versionObj
//	 */
//	@Version
//	@Column(name = "version_obj", precision = 15)
//	public Integer getVersionObj() {
//		return versionObj;
//	}
//
//	/**
//	 * @param versionObj the versionObj to set
//	 */
//	public void setVersionObj(Integer versionObj) {
//		this.versionObj = versionObj;
//	}

	

}
