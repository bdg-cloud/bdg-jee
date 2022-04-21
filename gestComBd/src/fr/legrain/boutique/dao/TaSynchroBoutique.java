package fr.legrain.boutique.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ta_synchro_boutique")
public class TaSynchroBoutique implements java.io.Serializable {
	
	private int id;
	private Date derniereSynchro;
	
	public TaSynchroBoutique() {
		super();
	}
	
	public TaSynchroBoutique(int id, Date derniereSynchro) {
		super();
		this.id = id;
		this.derniereSynchro = derniereSynchro;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "derniere_synchro", unique = true, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDerniereSynchro() {
		return derniereSynchro;
	}
	public void setDerniereSynchro(Date derniereSynchro) {
		this.derniereSynchro = derniereSynchro;
	}

}
