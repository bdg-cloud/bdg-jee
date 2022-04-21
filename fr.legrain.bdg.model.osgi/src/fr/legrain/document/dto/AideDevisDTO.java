package fr.legrain.document.dto;


import java.math.BigDecimal;
import java.util.Date;

public class AideDevisDTO extends AideDocumentCommunDTO {


	private static final long serialVersionUID = 4051181108447363358L;

	public AideDevisDTO() {
	}

	public AideDevisDTO(Integer id, String codeDocument,
			Date dateDocument, Date dateEchDocument, Date dateLivDocument,
			String libelleDocument, String codeTiers, String nomTiers,
			String codeCompta, String compte, BigDecimal regleDocument,
			BigDecimal netTtcCalc, BigDecimal netHtCalc, String commentaire,
			 BigDecimal regleCompletDocument,
			BigDecimal resteAReglerComplet, Boolean export) {
		super();
		this.setId(id);
		this.setCodeDocument(codeDocument);
		this.setDateDocument(dateDocument);
		this.setDateEchDocument(dateEchDocument);
		this.setDateLivDocument(dateLivDocument);
		this.setLibelleDocument(libelleDocument);
		this.setCodeTiers(codeTiers);
		this.setNomTiers(nomTiers);
		this.setCodeCompta(codeCompta);
		this.setCompte( compte);
		this.setNetTtcCalc( netTtcCalc);
		this.setNetHtCalc( netHtCalc);
		this.setCommentaire( commentaire);
	}
	
	
	public void setIHMEnteteDEVIS(AideDevisDTO ihmEnteteDocument){
		setId(ihmEnteteDocument.getId());
		setCodeDocument(ihmEnteteDocument.getCodeDocument());
		setDateDocument(ihmEnteteDocument.getDateDocument());
		setDateEchDocument(ihmEnteteDocument.getDateEchDocument());
		setDateLivDocument(ihmEnteteDocument.getDateLivDocument());
		setLibelleDocument(ihmEnteteDocument.getLibelleDocument());
		setCodeTiers(ihmEnteteDocument.getCodeTiers());
		setNomTiers(ihmEnteteDocument.getNomTiers());
		setCodeCompta(ihmEnteteDocument.getCodeCompta());
		setCompte(ihmEnteteDocument.getCompte());
		setCommentaire(ihmEnteteDocument.getCommentaire());
		setNetHtCalc(ihmEnteteDocument.getNetHtCalc());
		setNetTtcCalc(ihmEnteteDocument.getNetTtcCalc());
	}
	
	public static AideDevisDTO copy(AideDevisDTO ihmEnteteDocument){
		AideDevisDTO ihmEnteteDocumentLoc = new AideDevisDTO();
		ihmEnteteDocumentLoc.setId(ihmEnteteDocument.getId());
		ihmEnteteDocumentLoc.setCodeDocument(ihmEnteteDocument.getCodeDocument());
		ihmEnteteDocumentLoc.setDateDocument(ihmEnteteDocument.getDateDocument());
		ihmEnteteDocumentLoc.setDateEchDocument(ihmEnteteDocument.getDateEchDocument());
		ihmEnteteDocumentLoc.setDateLivDocument(ihmEnteteDocument.getDateLivDocument());
		ihmEnteteDocumentLoc.setLibelleDocument(ihmEnteteDocument.getLibelleDocument());
		ihmEnteteDocumentLoc.setCodeTiers(ihmEnteteDocument.getCodeTiers());
		ihmEnteteDocumentLoc.setNomTiers(ihmEnteteDocument.getNomTiers());
		ihmEnteteDocumentLoc.setCodeCompta(ihmEnteteDocument.getCodeCompta());
		ihmEnteteDocumentLoc.setCompte(ihmEnteteDocument.getCompte());
		ihmEnteteDocumentLoc.setCommentaire(ihmEnteteDocument.getCommentaire());
		ihmEnteteDocumentLoc.setNetHtCalc(ihmEnteteDocument.getNetHtCalc());
		ihmEnteteDocumentLoc.setNetTtcCalc(ihmEnteteDocument.getNetTtcCalc());
		return ihmEnteteDocumentLoc;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		return true;
	}

}
