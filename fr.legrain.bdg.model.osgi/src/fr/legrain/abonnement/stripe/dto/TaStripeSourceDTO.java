package fr.legrain.abonnement.stripe.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;

public class TaStripeSourceDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
		
	private Integer idStripeCustomer;
	private String idExterneCustomer;
	
	private String codeStripeTSource;
	private String liblStripeTSource;
	private Boolean automatique;
	
	private TaCarteBancaireDTO taCarteBancaireDTO;
	private TaCompteBanqueDTO taCompteBanqueDTO;
	
	private Integer versionObj;
	
	public TaStripeSourceDTO() {
	}
	
	public TaStripeSourceDTO(Integer id, String idExterne, Integer idStripeCustomer, String idExterneCustomer,
			 String nomProprietaire,
			 String numeroCarte,
			 String empreinte,
			 Integer moisExpiration,
			 Integer anneeExpiration,
			 String type,
			 String paysOrigine,
			 String last4Cb,
			 String cvc,
			
			 String iban,
			 String bankCode,
			 String branchCode,
			 String country,
			 String last4Iban,
			 
			 String codeStripeTSource,
	 		String liblStripeTSource,
	 		Boolean automatique
				) {
			this.id =id;
			this.idExterne = idExterne;
			this.idStripeCustomer = idStripeCustomer;
			this.codeStripeTSource = codeStripeTSource;
			this.liblStripeTSource = liblStripeTSource;
			this.automatique = automatique;
			if(nomProprietaire!=null || numeroCarte!=null || empreinte!=null ||
					moisExpiration!=null || anneeExpiration!=null || type!=null ||
					paysOrigine!=null || last4Cb!=null || cvc!=null) {
				taCarteBancaireDTO = new TaCarteBancaireDTO();
				taCarteBancaireDTO.setNomProprietaire(nomProprietaire);
				taCarteBancaireDTO.setNumeroCarte(numeroCarte);
				taCarteBancaireDTO.setEmpreinte(empreinte);
				taCarteBancaireDTO.setMoisExpiration(moisExpiration!=null?moisExpiration:0);
				taCarteBancaireDTO.setAnneeExpiration(anneeExpiration!=null?anneeExpiration:0);
				taCarteBancaireDTO.setType(type);
				taCarteBancaireDTO.setPaysOrigine(paysOrigine);
				taCarteBancaireDTO.setLast4(last4Cb);
				taCarteBancaireDTO.setCvc(cvc);
			}
			
			if(iban!=null || bankCode!=null || branchCode!=null || country!=null || last4Iban!=null) {
				taCompteBanqueDTO = new TaCompteBanqueDTO();
				taCompteBanqueDTO.setIban(iban);
				taCompteBanqueDTO.setBankCode(bankCode);
				taCompteBanqueDTO.setBranchCode(branchCode);
				taCompteBanqueDTO.setCountry(country);
				taCompteBanqueDTO.setLast4(last4Iban);
			}
		}
	
	public TaStripeSourceDTO(Integer id, String idExterne, Integer idStripeCustomer, String idExterneCustomer,
		 String nomProprietaire,
		 String numeroCarte,
		 String empreinte,
		 Integer moisExpiration,
		 Integer anneeExpiration,
		 String type,
		 String paysOrigine,
		 String last4Cb,
		 String cvc,
		
		 String iban,
		 String bankCode,
		 String branchCode,
		 String country,
		 String last4Iban
			) {
		this(id,  idExterne, idStripeCustomer, idExterneCustomer,
				  nomProprietaire,
				  numeroCarte,
				  empreinte,
				  moisExpiration,
				  anneeExpiration,
				  type,
				  paysOrigine,
				  last4Cb,
				  cvc,
				
				  iban,
				  bankCode,
				  branchCode,
				  country,
				  last4Iban,
				  null, null, null);
	}
	
	public static TaStripeSourceDTO copy(TaStripeSourceDTO taEntrepot){
		TaStripeSourceDTO taEntrepotLoc = new TaStripeSourceDTO();
		taEntrepotLoc.setId(taEntrepot.id);
		
		return taEntrepotLoc;
	}

	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getIdExterne() {
		return idExterne;
	}

	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}

	public Integer getIdStripeCustomer() {
		return idStripeCustomer;
	}

	public void setIdStripeCustomer(Integer idStripeCustomer) {
		this.idStripeCustomer = idStripeCustomer;
	}

	public String getIdExterneCustomer() {
		return idExterneCustomer;
	}

	public void setIdExterneCustomer(String idExterneCustomer) {
		this.idExterneCustomer = idExterneCustomer;
	}

	public TaCarteBancaireDTO getTaCarteBancaireDTO() {
		return taCarteBancaireDTO;
	}

	public void setTaCarteBancaireDTO(TaCarteBancaireDTO taCarteBancaireDTO) {
		this.taCarteBancaireDTO = taCarteBancaireDTO;
	}

	public TaCompteBanqueDTO getTaCompteBanqueDTO() {
		return taCompteBanqueDTO;
	}

	public void setTaCompteBanqueDTO(TaCompteBanqueDTO taCompteBanqueDTO) {
		this.taCompteBanqueDTO = taCompteBanqueDTO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idExterne == null) ? 0 : idExterne.hashCode());
		result = prime * result + ((idExterneCustomer == null) ? 0 : idExterneCustomer.hashCode());
		result = prime * result + ((idStripeCustomer == null) ? 0 : idStripeCustomer.hashCode());
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
		TaStripeSourceDTO other = (TaStripeSourceDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idExterne == null) {
			if (other.idExterne != null)
				return false;
		} else if (!idExterne.equals(other.idExterne))
			return false;
		if (idExterneCustomer == null) {
			if (other.idExterneCustomer != null)
				return false;
		} else if (!idExterneCustomer.equals(other.idExterneCustomer))
			return false;
		if (idStripeCustomer != other.idStripeCustomer)
			return false;
		return true;
	}

	public String getCodeStripeTSource() {
		return codeStripeTSource;
	}

	public void setCodeStripeTSource(String codeStripeTSource) {
		this.codeStripeTSource = codeStripeTSource;
	}

	public String getLiblStripeTSource() {
		return liblStripeTSource;
	}

	public void setLiblStripeTSource(String liblStripeTSource) {
		this.liblStripeTSource = liblStripeTSource;
	}

	public Boolean getAutomatique() {
		return automatique;
	}

	public void setAutomatique(Boolean automatique) {
		this.automatique = automatique;
	}

}
