/**
 * TaRequete.java 
 */
package fr.legrain.requetes.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nicolas²
 *
 */

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_requete")
//@SequenceGenerator(name = "gen_rqt", sequenceName = "num_id_rqt", allocationSize = 1)
@XmlRootElement(name="requete")

public class TaRequete implements java.io.Serializable {
	
	private static final long serialVersionUID = 5429760389009826955L;
	
	private int idRqt;
	private String libRqt;
	private String descRqt;
	private int typeResultat;
	private Set<TaLigneRequete> taLigneRequete = new LinkedHashSet<TaLigneRequete>(0);

	public TaRequete() {
	}


	public TaRequete(int idRqt, String libRqt, String descRqt, int typeResultat) {
		
		this.idRqt = idRqt;
		this.libRqt = libRqt;
		this.descRqt = descRqt;
		this.typeResultat = typeResultat;
		
	}
	
	public TaRequete(String libRqt, String descRqt, int typeResultat) {
		
		this.libRqt = libRqt;
		this.libRqt = libRqt;
		this.descRqt = descRqt;
		this.typeResultat = typeResultat;
		
	}


	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rqt", unique = true, nullable = false)
	public int getIdRqt() {
		return idRqt;
	}


	public void setIdRqt(int idRqt) {
		this.idRqt = idRqt;
	}


	@Column(name = "lib_rqt", nullable = false)
	public String getLibRqt() {
		return libRqt;
	}
	
	public void setLibRqt(String libRqt) {
		this.libRqt = libRqt;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rqt")
	@OrderBy("rqt")
	public Set<TaLigneRequete> getTaLigneRequete() {
		return taLigneRequete;
	}


	public void setTaLigneRequete(Set<TaLigneRequete> taLigneRequete) {
		this.taLigneRequete = taLigneRequete;
	}

	@Column(name = "desc_rqt")
	public String getDescRqt() {
		return descRqt;
	}


	public void setDescRqt(String descRqt) {
		this.descRqt = descRqt;
	}

	@Column(name = "type_resultat", nullable = false)
	public int getTypeResultat() {
		return typeResultat;
	}


	public void setTypeResultat(int typeResultat) {
		this.typeResultat = typeResultat;
	}



}
