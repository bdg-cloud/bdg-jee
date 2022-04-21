package fr.legrain.bdg.webapp.solstyce;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.document.dto.TaBonReceptionDTO;

public class DetailLotJSF {
	private String type;
	private String nom;
	private TracabiliteLot traca;
	private TaFabricationDTO fab;
	private TaBonReceptionDTO br;
	private String qte;
	private String unite;
	private String mpf;
	private int niveauArbre = 0;
	
	public DetailLotJSF() {
	}
	
	public DetailLotJSF(TracabiliteLot traca) {
		type = "Lot";
		nom = traca.lot.getNumLot();
		this.traca = traca;
	}
	
	public DetailLotJSF(TaFabricationDTO fab) {
		type = "Fab";
		nom = fab.getCodeDocument();
		this.fab = fab;
	}
	
	public DetailLotJSF(TaBonReceptionDTO br) {
		type = "BR";
		nom = br.getCodeDocument();
		this.br = br;
	}
	
	public DetailLotJSF(String type, String nom) {
		super();
		this.type = type;
		this.nom = nom;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@Override
    public String toString() {
        return type+" - "+nom;
    }

	public TracabiliteLot getTraca() {
		return traca;
	}

	public void setTraca(TracabiliteLot traca) {
		this.traca = traca;
	}

	public TaFabricationDTO getFab() {
		return fab;
	}

	public void setFab(TaFabricationDTO fab) {
		this.fab = fab;
	}

	public TaBonReceptionDTO getBr() {
		return br;
	}

	public void setBr(TaBonReceptionDTO br) {
		this.br = br;
	}

	public String getQte() {
		return qte;
	}

	public void setQte(String qte) {
		this.qte = qte;
	}

	public String getUnite() {
		return unite;
	}

	public void setUnite(String unite) {
		this.unite = unite;
	}

	public String getMpf() {
		return mpf;
	}

	public void setMpf(String mpf) {
		this.mpf = mpf;
	}

	public int getNiveauArbre() {
		return niveauArbre;
	}

	public void setNiveauArbre(int niveauArbre) {
		this.niveauArbre = niveauArbre;
	}
	
}
