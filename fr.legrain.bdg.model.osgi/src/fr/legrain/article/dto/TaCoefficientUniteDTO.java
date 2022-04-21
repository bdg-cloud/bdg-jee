package fr.legrain.article.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;

public class TaCoefficientUniteDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 6010463839415201746L;

	private Integer id;

	private String codeUniteA;
	private String codeUniteB;

	private BigDecimal coeffUniteAVersB;
	private BigDecimal coeffUniteBVersA;
	private BigDecimal coeff;
	
	private Integer nbDecimaleAVersB;
	private Integer nbDecimaleBVersA;
	private Integer nbDecimale;
	
	private Boolean operateurAVersBDivise = false;
	private Boolean operateurBVersADivise = false;
	private Boolean operateurDivise = false;
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaCoefficientUniteDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodeUniteA() {
		return codeUniteA;
	}

	public void setCodeUniteA(String codeUniteA) {
		this.codeUniteA = codeUniteA;
	}

	public String getCodeUniteB() {
		return codeUniteB;
	}

	public void setCodeUniteB(String codeUniteB) {
		this.codeUniteB = codeUniteB;
	}

	public BigDecimal getCoeffUniteAVersB() {
		return coeffUniteAVersB;
	}

	public void setCoeffUniteAVersB(BigDecimal coeffUniteAVersB) {
		this.coeffUniteAVersB = coeffUniteAVersB;
	}

	
	public BigDecimal getCoeffUniteBVersA() {
		return coeffUniteBVersA;
	}

	public void setCoeffUniteBVersA(BigDecimal coeffUniteBVersA) {
		this.coeffUniteBVersA = coeffUniteBVersA;
	}


	public Integer getNbDecimaleAVersB() {
		return nbDecimaleAVersB;
	}

	public void setNbDecimaleAVersB(Integer nbDecimaleAVersB) {
		this.nbDecimaleAVersB = nbDecimaleAVersB;
	}


	public Integer getNbDecimaleBVersA() {
		return nbDecimaleBVersA;
	}

	public void setNbDecimaleBVersA(Integer nbDecimaleBVersA) {
		this.nbDecimaleBVersA = nbDecimaleBVersA;
	}

	public Boolean getOperateurAVersBDivise() {
		return operateurAVersBDivise;
	}

	public void setOperateurAVersBDivise(Boolean operateurAVersBDivise) {
		this.operateurAVersBDivise = operateurAVersBDivise;
	}

	public Boolean getOperateurBVersADivise() {
		return operateurBVersADivise;
	}

	public void setOperateurBVersADivise(Boolean operateurBVersADivise) {
		this.operateurBVersADivise = operateurBVersADivise;
	}

	public void recupRapportEtSens(String codeUnite1,String codeUnite2) {
		if(this.codeUniteA!=null && !this.codeUniteA.equals(codeUnite1)) {
			coeff=this.coeffUniteAVersB;
			nbDecimale=this.nbDecimaleAVersB;
			operateurDivise=this.operateurAVersBDivise;
		}else
			if(this.codeUniteB!=null && !this.codeUniteA.equals(codeUnite1)) {
				coeff=this.coeffUniteBVersA;
				nbDecimale=this.nbDecimaleBVersA;
				operateurDivise=this.operateurBVersADivise;	
			}
	}

	public BigDecimal getCoeff() {
		return coeff;
	}

	public void setCoeff(BigDecimal coeff) {
		this.coeff = coeff;
	}

	public Integer getNbDecimale() {
		return nbDecimale;
	}

	public void setNbDecimale(Integer nbDecimale) {
		this.nbDecimale = nbDecimale;
	}

	public Boolean getOperateurDivise() {
		return operateurDivise;
	}

	public void setOperateurDivise(Boolean operateurDivise) {
		this.operateurDivise = operateurDivise;
	}

}
