/**
 * TaLigneRequete.java 
 */
package fr.legrain.requetes.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Nicolas²
 *
 */

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_ligne_requete")
//@SequenceGenerator(name = "gen_ligne_rqt", sequenceName = "num_id_ligne_rqt", allocationSize = 1)


public class TaLigneRequete implements java.io.Serializable {

	private static final long serialVersionUID = 416054980087327313L;
	
	private int id;
	private TaRequete rqt;
	private int idGroupe;
	private int andOrGroupe;
	private int idLigne;
	private String typeLigne;
	private int andOrLigne;
	private int combo1;
	private int combo2;
	private String valeur1;
	private String valeur2;

	public TaLigneRequete() {
	}


	public TaLigneRequete(int id,TaRequete rqt, int idGroupe, int andOrGroupe, int idLigne
			, String typeLigne, int andOrLigne, int combo1, int combo2
			, String valeur1, String valeur2) {
		
		this.id = id;
		this.rqt = rqt;
		this.idGroupe = idGroupe;
		this.andOrGroupe = andOrGroupe;
		this.idLigne = idLigne;
		this.typeLigne = typeLigne;
		this.andOrLigne = andOrLigne;
		this.combo1 = combo1;
		this.combo2 = combo2;
		this.valeur1 = valeur1;
		this.valeur2 = valeur2;
		
	}
	
	public TaLigneRequete(TaRequete rqt, int idGroupe, int andOrGroupe, int idLigne
			, String typeLigne, int andOrLigne, int combo1, int combo2
			, String valeur1, String valeur2) {
		
		this.rqt = rqt;
		this.idGroupe = idGroupe;
		this.andOrGroupe = andOrGroupe;
		this.idLigne = idLigne;
		this.typeLigne = typeLigne;
		this.andOrLigne = andOrLigne;
		this.combo1 = combo1;
		this.combo2 = combo2;
		this.valeur1 = valeur1;
		this.valeur2 = valeur2;
		
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
	@JoinColumn(name = "id_rqt")
	public TaRequete getRqt() {
		return rqt;
	}


	public void setRqt(TaRequete rqt) {
		this.rqt = rqt;
	}


	@Column(name = "id_groupe", nullable = false)
	public int getIdGroupe() {
		return idGroupe;
	}


	public void setIdGroupe(int idGroupe) {
		this.idGroupe = idGroupe;
	}

	@Column(name = "andor_groupe", nullable = false)
	public int getAndOrGroupe() {
		return andOrGroupe;
	}

	
	public void setAndOrGroupe(int andOrGroupe) {
		this.andOrGroupe = andOrGroupe;
	}

	@Column(name = "id_ligne", nullable = false)
	public int getIdLigne() {
		return idLigne;
	}


	public void setIdLigne(int idLigne) {
		this.idLigne = idLigne;
	}

	@Column(name = "type_ligne", nullable = false)
	public String getTypeLigne() {
		return typeLigne;
	}


	public void setTypeLigne(String typeLigne) {
		this.typeLigne = typeLigne;
	}

	@Column(name = "andor_ligne", nullable = false)
	public int getAndOrLigne() {
		return andOrLigne;
	}


	public void setAndOrLigne(int andOrLigne) {
		this.andOrLigne = andOrLigne;
	}

	@Column(name = "combo_1", nullable = false)
	public int getCombo1() {
		return combo1;
	}


	public void setCombo1(int combo1) {
		this.combo1 = combo1;
	}

	@Column(name = "combo_2", nullable = false)
	public int getCombo2() {
		return combo2;
	}


	public void setCombo2(int combo2) {
		this.combo2 = combo2;
	}


	@Column(name = "valeur_1")
	public String getValeur1() {
		return valeur1;
	}


	public void setValeur1(String valeur1) {
		this.valeur1 = valeur1;
	}

	@Column(name = "valeur_2")
	public String getValeur2() {
		return valeur2;
	}


	public void setValeur2(String valeur2) {
		this.valeur2 = valeur2;
	}

}
