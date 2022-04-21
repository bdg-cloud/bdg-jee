package fr.legrain.tiers.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;

import fr.legrain.document.model.TaEtat;

@SessionScoped
public class EtatDocument implements Serializable{



	private static final long serialVersionUID = 8714481341882039677L;
	Map<String, TaEtat> mapEtat=new LinkedHashMap<>();
	
	
	public Map<String, TaEtat> getMapEtat() {
		return mapEtat;
	}
	public void setMapEtat(Map<String, TaEtat> mapEtat) {
		this.mapEtat = mapEtat;
	}
	
	


	
//	TaEtat documentAbandonne;
//	TaEtat documentBrouillon;
//	TaEtat documentCommercial;
//	TaEtat documentEncours;
//	TaEtat documentPartiellementAbandonne;
//	TaEtat documentPartiellementTransforme;
//	TaEtat documentPreparation;
//	TaEtat documentTermine;
//	TaEtat documentTerminePartiellementAbandonne;
//	TaEtat documentTotalementTransforme;
//	public TaEtat getDocumentAbandonne() {
//		return documentAbandonne;
//	}
//	public void setDocumentAbandonne(TaEtat documentAbandonne) {
//		this.documentAbandonne = documentAbandonne;
//	}
//	public TaEtat getDocumentBrouillon() {
//		return documentBrouillon;
//	}
//	public void setDocumentBrouillon(TaEtat documentBrouillon) {
//		this.documentBrouillon = documentBrouillon;
//	}
//	public TaEtat getDocumentCommercial() {
//		return documentCommercial;
//	}
//	public void setDocumentCommercial(TaEtat documentCommercial) {
//		this.documentCommercial = documentCommercial;
//	}
//	public TaEtat getDocumentEncours() {
//		return documentEncours;
//	}
//	public void setDocumentEncours(TaEtat documentEncours) {
//		this.documentEncours = documentEncours;
//	}
//	public TaEtat getDocumentPartiellementAbandonne() {
//		return documentPartiellementAbandonne;
//	}
//	public void setDocumentPartiellementAbandonne(TaEtat documentPartiellementAbandonne) {
//		this.documentPartiellementAbandonne = documentPartiellementAbandonne;
//	}
//	public TaEtat getDocumentPartiellementTransforme() {
//		return documentPartiellementTransforme;
//	}
//	public void setDocumentPartiellementTransforme(TaEtat documentPartiellementTransforme) {
//		this.documentPartiellementTransforme = documentPartiellementTransforme;
//	}
//	public TaEtat getDocumentPreparation() {
//		return documentPreparation;
//	}
//	public void setDocumentPreparation(TaEtat documentPreparation) {
//		this.documentPreparation = documentPreparation;
//	}
//	public TaEtat getDocumentTermine() {
//		return documentTermine;
//	}
//	public void setDocumentTermine(TaEtat documentTermine) {
//		this.documentTermine = documentTermine;
//	}
//	public TaEtat getDocumentTerminePartiellementAbandonne() {
//		return documentTerminePartiellementAbandonne;
//	}
//	public void setDocumentTerminePartiellementAbandonne(TaEtat documentTerminePartiellementAbandonne) {
//		this.documentTerminePartiellementAbandonne = documentTerminePartiellementAbandonne;
//	}
//	public TaEtat getDocumentTotalementTransforme() {
//		return documentTotalementTransforme;
//	}
//	public void setDocumentTotalementTransforme(TaEtat documentTotalementTransforme) {
//		this.documentTotalementTransforme = documentTotalementTransforme;
//	}



}
