package fr.legrain.article.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import fr.legrain.general.model.TaObjetLgr;


@Entity
@Table(name = "ta_type_mouvement")
public class TaTypeMouvement extends TaObjetLgr   implements Serializable{


	private static final long serialVersionUID = -8342954751061673191L;

	private int idTypeMouvement;

	private String code;
	private String libelle;
    private Boolean systeme;
    private Boolean sens;
    private Boolean dms;
    private Boolean crd;
    
    
	private Integer versionObj;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;

	@Transient
	static Logger logger = Logger.getLogger(TaTypeMouvement.class.getName());

	public TaTypeMouvement() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_type_mouvement", unique = true, nullable = false)
	public int getIdTypeMouvement() {
		return this.idTypeMouvement;
	}
	public void setIdTypeMouvement(int id) {
		this.idTypeMouvement = id;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	@Column(name = "systeme")
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}
	
	@Column(name = "sens")
	public Boolean getSens() {
		return sens;
	}

	public void setSens(Boolean sens) {
		this.sens = sens;
	}
	

	@Column(name = "dms")
	public Boolean getDms() {
		return dms;
	}

	public void setDms(Boolean dms) {
		this.dms = dms;
	}
	
	@Column(name = "crd")
	public Boolean getCrd() {
		return crd;
	}

	public void setCrd(Boolean crd) {
		this.crd = crd;
	}
	
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((dms == null) ? 0 : dms.hashCode());
		result = prime * result + idTypeMouvement;
		result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result + ((sens == null) ? 0 : sens.hashCode());
		result = prime * result + ((systeme == null) ? 0 : systeme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaTypeMouvement other = (TaTypeMouvement) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (dms == null) {
			if (other.dms != null)
				return false;
		} else if (!dms.equals(other.dms))
			return false;
		if (idTypeMouvement != other.idTypeMouvement)
			return false;
		if (libelle == null) {
			if (other.libelle != null)
				return false;
		} else if (!libelle.equals(other.libelle))
			return false;
		if (sens == null) {
			if (other.sens != null)
				return false;
		} else if (!sens.equals(other.sens))
			return false;
		if (systeme == null) {
			if (other.systeme != null)
				return false;
		} else if (!systeme.equals(other.systeme))
			return false;
		return true;
	}










}
