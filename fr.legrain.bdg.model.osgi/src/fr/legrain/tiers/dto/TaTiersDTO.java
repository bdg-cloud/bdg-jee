package fr.legrain.tiers.dto;


import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.validator.LgrHibernateValidated;

public class TaTiersDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4278037257017688067L;
	private Integer id = 0;
	private String codeTiers;
	private String codeCompta;
	private String compte;
	private String nomTiers;
	private String prenomTiers;
	private String surnomTiers;
	private Boolean actifTiers = true;
	private Boolean utiliseCompteClient = false;
	private Boolean emailCleCompteClientEnvoye = false;
	private Boolean ttcTiers = false;
	private Integer idTCivilite;
	private String codeTCivilite;
	private Integer idEntreprise;
	private String nomEntreprise;
	private String liblEntreprise;
	private Integer idTTiers;
	private String codeTTiers;
	private String libelleTTiers;
	private Integer idCommentaire;
	private String commentaire;
	private String codeTTvaDoc;
	private Integer idTTvaDoc;
	
	private Date dateDerniereConnexionCompteClient;
	
	
	private Integer idTEntite;
	private String codeTEntite;
	private String liblTEntite;
	private String tvaIComCompl;
	private String siretCompl;
	private String numAgrementSanitaire;
	private String accise;
	private String ics;
	
	private Integer idAdresse;
	private String adresse1Adresse;
	private String adresse2Adresse;
	private String adresse3Adresse;
	private String codepostalAdresse;
	private String villeAdresse;
	private String paysAdresse;
	
	private Integer idEmail;
	private String adresseEmail;
	
	private String adresseWeb;
	private String numeroTelephone;
	
	
	private String codeCPaiement;
	private String libCPaiement;
	private Integer reportCPaiement=0;
	private Integer finMoisCPaiement=0;
	
	private String codeTPaiement;
	private String libTPaiement;
	private String compteTPaiement;
	private Integer reportTPaiement=0;
	private Integer finMoisTPaiement=0;
	
	private String codeTTarif;
	private Boolean accepte = true;
	
	private Date dateAnniv=new Date();
	
	private int idFamilleTiers;
	private String codeFamilleTiers;
	private String libelleFamilleTiers;
	
	private String maRefTiers;
	
	private Integer versionObj;
	private int nbTiers; // Utilisé pour compter un nombre de tiers
	
	private String importationDivers;
	
	public TaTiersDTO() {
	}

	public TaTiersDTO(Integer id, String codeTiers, String codeTTiers, String nomTiers,
			String prenomTiers, String nomEntreprise, Boolean actifTiers) {
		super();
		this.id = id;
		this.codeTiers = codeTiers;
		this.codeTTiers = codeTTiers;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		this.nomEntreprise = nomEntreprise;
		this.actifTiers = actifTiers;
	}
	
	// Utilisé dans FIND_ALL_LIGHT et FIND_BY_CODE_LIGHT et ...
	public TaTiersDTO(int id, String codeTiers, String codeTTiers, String nomTiers,
			String prenomTiers, String nomEntreprise, int actifTiers) {
		super();
		this.id = id;
		this.codeTiers = codeTiers;
		this.codeTTiers = codeTTiers;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		this.nomEntreprise = nomEntreprise;
		this.actifTiers = LibConversion.intToBoolean(actifTiers);
	}
	

	public TaTiersDTO(Integer id, String codeTiers, String codeTTiers,String nomEntreprise, String nomTiers,
			String prenomTiers, int actifTiers, 
			Integer idFamilleTiers, String codeFamilleTiers, String libelleFamilleTiers, 
			String codepostalAdresse, String villeAdresse, String paysAdresse, String adresseEmail, String numeroTelephone) {
		super();
		this.id = id;
		this.codeTiers = codeTiers;
		this.codeTTiers = codeTTiers;
		this.nomEntreprise = nomEntreprise;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		this.actifTiers = actifTiers==1?true:false;
		this.idFamilleTiers = idFamilleTiers==null?0:1;
		this.codeFamilleTiers = codeFamilleTiers;
		this.libelleFamilleTiers = libelleFamilleTiers;
		this.codepostalAdresse = codepostalAdresse;
		this.villeAdresse = villeAdresse;
		this.paysAdresse =paysAdresse;
		this.adresseEmail = adresseEmail;
		this.numeroTelephone = numeroTelephone;
	}
	
	public TaTiersDTO(Integer id, String codeTiers, String codeTTiers,String nomEntreprise, String nomTiers,
			String prenomTiers, int actifTiers, 
			Integer idFamilleTiers, String codeFamilleTiers, String libelleFamilleTiers, 
			String adresse1Adresse, String adresse2Adresse, String adresse3Adresse, String codepostalAdresse, String villeAdresse, String paysAdresse, String adresseEmail, String numeroTelephone) {
		super();
		this.id = id;
		this.codeTiers = codeTiers;
		this.codeTTiers = codeTTiers;
		this.nomEntreprise = nomEntreprise;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		this.actifTiers = actifTiers==1?true:false;
		this.idFamilleTiers = idFamilleTiers==null?0:1;
		this.codeFamilleTiers = codeFamilleTiers;
		this.libelleFamilleTiers = libelleFamilleTiers;
		this.adresse1Adresse = adresse1Adresse;
		this.adresse2Adresse = adresse2Adresse;
		this.adresse3Adresse = adresse3Adresse;
		this.codepostalAdresse = codepostalAdresse;
		this.villeAdresse = villeAdresse;
		this.paysAdresse =paysAdresse;
		this.adresseEmail = adresseEmail;
		this.numeroTelephone = numeroTelephone;
	}
	
	public TaTiersDTO(Integer id, String codeTiers, String codeTTiers,String codeTTarif,String nomEntreprise, String nomTiers,
			String prenomTiers, int actifTiers, 
			Integer idFamilleTiers, String codeFamilleTiers, String libelleFamilleTiers, 
			String codepostalAdresse, String villeAdresse, String paysAdresse, String adresseEmail, String numeroTelephone) {
		super();
		this.id = id;
		this.codeTiers = codeTiers;
		this.codeTTiers = codeTTiers;
		this.codeTTarif = codeTTarif;
		this.nomEntreprise = nomEntreprise;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		this.actifTiers = actifTiers==1?true:false;
		this.idFamilleTiers = idFamilleTiers==null?0:1;
		this.codeFamilleTiers = codeFamilleTiers;
		this.libelleFamilleTiers = libelleFamilleTiers;
		this.codepostalAdresse = codepostalAdresse;
		this.villeAdresse = villeAdresse;
		this.paysAdresse =paysAdresse;
		this.adresseEmail = adresseEmail;
		this.numeroTelephone = numeroTelephone;
	}
	
	public TaTiersDTO(Integer id, String codeTiers, String codeTTiers, String nomTiers,
			String prenomTiers, int actifTiers) {
		super();
		this.id = id;
		this.codeTiers = codeTiers;
		this.codeTTiers = codeTTiers;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		this.actifTiers = actifTiers==1?true:false;
	}

	
	public TaTiersDTO(String codeTiers, String nomTiers, String prenomTiers, int actifTiers, String codeTCivilite,
			String nomEntreprise) {
		super();
		this.codeTiers = codeTiers;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		this.actifTiers = actifTiers==1?true:false;
		this.codeTCivilite = codeTCivilite;
		this.nomEntreprise = nomEntreprise;
	}

	public TaTiersDTO(int nbTiers, String codeTTiers, int actifTiers) {
		super();
		this.nbTiers = nbTiers;
		this.codeTTiers = codeTTiers;
		this.actifTiers = actifTiers==1?true:false;
	}
	
	public TaTiersDTO(int nbTiers, int actifTiers) {
		super();
		this.nbTiers = nbTiers;
		this.actifTiers = actifTiers==1?true:false;
	}

	public Boolean getActifTiers() {
		return actifTiers;
	}

	public void setActifTiers(Boolean ACTIF_TIERS) {
		firePropertyChange("actifTiers", this.actifTiers, this.actifTiers = ACTIF_TIERS);
	}


	@LgrHibernateValidated(champBd = "code_compta",table = "ta_tiers",champEntite="codeCompta", clazz = TaTiersDTO.class)
	@Size(min=0, max=8)
	public String getCodeCompta() {
		return codeCompta;
	}

	public void setCodeCompta(String CODE_COMPTA) {
		firePropertyChange("codeCompta", this.codeCompta, this.codeCompta = CODE_COMPTA);
	}

	@LgrHibernateValidated(champBd = "nom_entreprise",table = "ta_entreprise",champEntite="nomEntreprise",clazz = TaTiersDTO.class)
	@Size(min=0, max=100)
	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String NOM_ENTREPRISE) {
		firePropertyChange("nomEntreprise", this.nomEntreprise, this.nomEntreprise = NOM_ENTREPRISE);
	}

	@LgrHibernateValidated(champBd = "code_t_civilite",table = "ta_t_civilite",champEntite="codeTCivilite",clazz = TaTiersDTO.class)
	@Size(min=0, max=20)
	public String getCodeTCivilite() {
		return codeTCivilite;
	}

	public void setCodeTCivilite(String CODE_T_CIVILITE) {
		firePropertyChange("codeTCivilite", this.codeTCivilite, this.codeTCivilite = CODE_T_CIVILITE);
	}

	@LgrHibernateValidated(champBd = "code_t_entite",table = "ta_t_entite",champEntite="codeTEntite",clazz = TaTiersDTO.class)
	@Size(min=0, max=20)
	public String getCodeTEntite() {
		return codeTEntite;
	}

	public void setCodeTEntite(String CODE_T_ENTITE) {
		firePropertyChange("codeTEntite", this.codeTEntite, this.codeTEntite = CODE_T_ENTITE);
	}

	@LgrHibernateValidated(champBd = "code_t_tiers",table = "ta_t_tiers",champEntite="codeTTiers",clazz = TaTiersDTO.class)
	@NotNull
	@Size(min=1, max=20)
	public String getCodeTTiers() {
		return codeTTiers;
	}

	public void setCodeTTiers(String CODE_T_TIERS) {
		firePropertyChange("codeTTiers", this.codeTTiers, this.codeTTiers = CODE_T_TIERS);
	}

	@LgrHibernateValidated(champBd = "code_tiers",table = "ta_tiers",champEntite="codeTiers",clazz = TaTiersDTO.class)
	@NotNull
	@Size(min=1, max=20)
	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String CODE_TIERS) {
		firePropertyChange("codeTiers", this.codeTiers, this.codeTiers = CODE_TIERS);
	}

	@LgrHibernateValidated(champBd = "compte",table = "ta_tiers",champEntite="compte",clazz = TaTiersDTO.class)
	@Size(min=0, max=8)
	public String getCompte() {
		return compte;
	}

	public void setCompte(String COMPTE) {
		firePropertyChange("compte", this.compte, this.compte = COMPTE);
	}

	public Integer getIdCommentaire() {
		return idCommentaire;
	}

	public void setIdCommentaire(Integer ID_COMMENTAIRE) {
		firePropertyChange("idCommentaire", this.idCommentaire, this.idCommentaire = ID_COMMENTAIRE);
	}

	public Integer getIdEntreprise() {
		return idEntreprise;
	}

	public void setIdEntreprise(Integer ID_ENTREPRISE) {
		firePropertyChange("idEntreprise", this.idEntreprise, this.idEntreprise = ID_ENTREPRISE);
	}


	public Integer getIdTCivilite() {
		return idTCivilite;
	}

	public void setIdTCivilite(Integer ID_T_CIVILITE) {
		firePropertyChange("ID_T_CIVILITE", this.idTCivilite, this.idTCivilite = ID_T_CIVILITE);
	}

	public Integer getIdTEntite() {
		return idTEntite;
	}

	public void setIdTEntite(Integer ID_T_ENTITE) {
		firePropertyChange("idTEntite", this.idTEntite, this.idTEntite = ID_T_ENTITE);
	}

	public Integer getIdTTiers() {
		return idTTiers;
	}

	public void setIdTTiers(Integer ID_T_TIERS) {
		firePropertyChange("idTTiers", this.idTTiers, this.idTTiers = ID_T_TIERS);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}


	public String getLibelleTTiers() {
		return libelleTTiers;
	}

	public void setLibelleTTiers(String LIBELLE_T_TIERS) {
		firePropertyChange("libelleTTiers", this.libelleTTiers, this.libelleTTiers = LIBELLE_T_TIERS);
	}


	@LgrHibernateValidated(champBd = "commentaire",table = "ta_commentaire",champEntite="commentaire",clazz = TaTiersDTO.class)
	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String LIBL_COMMENTAIRE) {
		firePropertyChange("commentaire", this.commentaire, this.commentaire = LIBL_COMMENTAIRE);
	}

	public String getLiblEntreprise() {
		return liblEntreprise;
	}

	public void setLiblEntreprise(String LIBL_ENTREPRISE) {
		firePropertyChange("liblEntreprise", this.liblEntreprise, this.liblEntreprise = LIBL_ENTREPRISE);
	}

	@NotNull
	@Size(min=0, max=255)
	//@Pattern(regexp = "[A-Za-z -\\.]*", message="Nom tiers non valide")
	@LgrHibernateValidated(champBd = "nomTiers",table = "ta_tiers",champEntite="nomTiers",clazz = TaTiersDTO.class)
	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String NOM_TIERS) {
		firePropertyChange("nomTiers", this.nomTiers, this.nomTiers = NOM_TIERS);
	}
	
	//@Max(10)
	@LgrHibernateValidated(champBd = "prenomTiers",table = "ta_tiers",champEntite="prenomTiers",clazz = TaTiersDTO.class)
	public String getPrenomTiers() {
		return prenomTiers;
	}

	public void setPrenomTiers(String PRENOM_TIERS) {
		firePropertyChange("prenomTiers", this.prenomTiers, this.prenomTiers = PRENOM_TIERS);
	}

	@LgrHibernateValidated(champBd = "siret_compl",table = "ta_compl",champEntite="siretCompl",clazz = TaTiersDTO.class)
	public String getSiretCompl() {
		return siretCompl;
	}

	public void setSiretCompl(String SIRET_COMPL) {
		firePropertyChange("siretCompl", this.siretCompl, this.siretCompl = SIRET_COMPL);
	}

	@LgrHibernateValidated(champBd = "surnom_tiers",table = "ta_tiers",champEntite="surnomTiers",clazz = TaTiersDTO.class)
	public String getSurnomTiers() {
		return surnomTiers;
	}

	public void setSurnomTiers(String SURNOM_TIERS) {
		firePropertyChange("surnomTiers", this.surnomTiers, this.surnomTiers = SURNOM_TIERS);
	}

	public Boolean getTtcTiers() {
		return ttcTiers;
	}

	public void setTtcTiers(Boolean TTC_TIERS) {
		firePropertyChange("ttcTiers", this.ttcTiers, this.ttcTiers = TTC_TIERS);
	}

	@LgrHibernateValidated(champBd = "tva_i_com_compl",table = "ta_compl",champEntite="tvaIComCompl",clazz = TaTiersDTO.class)
	public String getTvaIComCompl() {
		return tvaIComCompl;
	}

	public void setTvaIComCompl(String TVA_I_COM_COMPL) {
		firePropertyChange("tvaIComCompl", this.tvaIComCompl, this.tvaIComCompl = TVA_I_COM_COMPL);
	}
	
	@LgrHibernateValidated(champBd = "adresse1_adresse",table = "ta_adresse",champEntite="adresse1Adresse",clazz = TaTiersDTO.class)
	public String getAdresse1Adresse() {
		return adresse1Adresse;
	}

	public void setAdresse1Adresse(String adresse1Adresse) {
		firePropertyChange("adresse1Adresse", this.adresse1Adresse, this.adresse1Adresse = adresse1Adresse);	
	}

	@LgrHibernateValidated(champBd = "adresse2_adresse",table = "ta_adresse",champEntite="adresse2Adresse",clazz = TaTiersDTO.class)
	public String getAdresse2Adresse() {
		return adresse2Adresse;
	}

	public void setAdresse2Adresse(String adresse2Adresse) {
		firePropertyChange("adresse2Adresse", this.adresse2Adresse, this.adresse2Adresse = adresse2Adresse);	
	}

	@LgrHibernateValidated(champBd = "adresse3_adresse",table = "ta_adresse",champEntite="adresse3Adresse",clazz = TaTiersDTO.class)
	public String getAdresse3Adresse() {
		return adresse3Adresse;
	}

	public void setAdresse3Adresse(String adresse3Adresse) {
		firePropertyChange("adresse3Adresse", this.adresse3Adresse, this.adresse3Adresse = adresse3Adresse);	
	}

	@LgrHibernateValidated(champBd = "codepostal_adresse",table = "ta_adresse",champEntite="codepostalAdresse",clazz = TaTiersDTO.class)
	@Size(min=0, max=10)
	public String getCodepostalAdresse() {
		return codepostalAdresse;
	}

	public void setCodepostalAdresse(String codepostalAdresse) {
		firePropertyChange("codepostalAdresse", this.codepostalAdresse, this.codepostalAdresse = codepostalAdresse);	
	}

	@LgrHibernateValidated(champBd = "ville_adresse",table = "ta_adresse",champEntite="villeAdresse",clazz = TaTiersDTO.class)
	public String getVilleAdresse() {
		return villeAdresse;
	}

	public void setVilleAdresse(String villeAdresse) {
		firePropertyChange("villeAdresse", this.villeAdresse, this.villeAdresse = villeAdresse);	
	}

	@LgrHibernateValidated(champBd = "pays_adresse",table = "ta_adresse",champEntite="paysAdresse",clazz = TaTiersDTO.class)
	public String getPaysAdresse() {
		return paysAdresse;
	}

	public void setPaysAdresse(String paysAdresse) {
		firePropertyChange("paysAdresse", this.paysAdresse, this.paysAdresse = paysAdresse);	
	}
	
	@LgrHibernateValidated(champBd = "adresse_email",table = "ta_email",champEntite="adresseEmail",clazz = TaTiersDTO.class)
	public String getAdresseEmail() {
		return adresseEmail;
	}

	public void setAdresseEmail(String adresseEmail) {
		firePropertyChange("adresseEmail", this.adresseEmail, this.adresseEmail = adresseEmail);
	}
	
	@LgrHibernateValidated(champBd = "adresse_web",table = "ta_web",champEntite="adresseWeb",clazz = TaTiersDTO.class)
	public String getAdresseWeb() {
		return adresseWeb;
	}

	public void setAdresseWeb(String adresseWeb) {
		firePropertyChange("adresseWeb", this.adresseWeb, this.adresseWeb = adresseWeb);
	}
	
	@LgrHibernateValidated(champBd = "numero_telephone",table = "ta_telephone",champEntite="numeroTelephone",clazz = TaTiersDTO.class)
	public String getNumeroTelephone() {
		return numeroTelephone;
	}

	public void setNumeroTelephone(String numeroTelephone) {
		firePropertyChange("numeroTelephone", this.numeroTelephone, this.numeroTelephone = numeroTelephone);
	}



	public Integer getIdAdresse() {
		return idAdresse;
	}


	public void setIdAdresse(Integer idAdresse) {
		this.idAdresse = idAdresse;
	}


	public Integer getIdEmail() {
		return idEmail;
	}


	public void setIdEmail(Integer idEmail) {
		this.idEmail = idEmail;
	}
	

	@LgrHibernateValidated(champBd = "code_t_paiement",table = "ta_t_paiement",champEntite="codeCPaiement",clazz = TaTiersDTO.class)
	@Size(min=0, max=25)
	public String getCodeCPaiement() {
		return codeCPaiement;
	}


	public void setCodeCPaiement(String codeCPaiement) {
		firePropertyChange("codeCPaiement", this.codeCPaiement, this.codeCPaiement = codeCPaiement);
	}

	@LgrHibernateValidated(champBd = "code_t_tarif",table = "ta_t_tarif",champEntite="codeTTarif",clazz = TaTiersDTO.class)
	@Size(min=0, max=20)
	public String getCodeTTarif() {
		return codeTTarif;
	}


	public void setCodeTTarif(String codeTTarif) {
		firePropertyChange("codeTTarif", this.codeTTarif, this.codeTTarif = codeTTarif);
	}


	@LgrHibernateValidated(champBd = "code_t_tva_doc",table = "ta_t_tva_doc",champEntite="codeTTvaDoc",clazz = TaTiersDTO.class)
	@Size(min=0, max=20)
	public String getCodeTTvaDoc() {
		return codeTTvaDoc;
	}


	public void setCodeTTvaDoc(String codeTTvaDoc) {
		firePropertyChange("codeTTvaDoc", this.codeTTvaDoc, this.codeTTvaDoc = codeTTvaDoc);
	}


	public Integer getIdTTvaDoc() {
		return idTTvaDoc;
	}


	public void setIdTTvaDoc(Integer idTTvaDoc) {
		firePropertyChange("idTTvaDoc", this.idTTvaDoc, this.idTTvaDoc = idTTvaDoc);
	}


	public Boolean getAccepte() {
		return accepte;
	}


	public void setAccepte(Boolean accepte) {
		firePropertyChange("accepte", this.accepte, this.accepte = accepte);
	}


	@LgrHibernateValidated(champBd = "code_t_paiement",table = "ta_t_paiement",champEntite="codeTPaiement",clazz = TaTiersDTO.class)
	@Size(min=0, max=20)
	public String getCodeTPaiement() {
		return codeTPaiement;
	}


	public void setCodeTPaiement(String codeTPaiement) {
		firePropertyChange("codeTPaiement", this.codeTPaiement, this.codeTPaiement = codeTPaiement);
	}


	@LgrHibernateValidated(champBd = "accise",table = "ta_compl",champEntite="accise",clazz = TaTiersDTO.class)
	public String getAccise() {
		return accise;
	}


	public void setAccise(String accise) {
		firePropertyChange("accise", this.accise, this.accise = accise);
	}

	@LgrHibernateValidated(champBd = "ics",table = "ta_compl",champEntite="ics",clazz = TaTiersDTO.class)
	public String getIcs() {
		return ics;
	}


	public void setIcs(String ics) {
		firePropertyChange("ics", this.ics, this.ics = ics);
	}

	@Temporal(TemporalType.DATE)
	@LgrHibernateValidated(champBd = "date_anniv",table = "ta_tiers",champEntite="dateAnniv",clazz = TaTiersDTO.class)
	public Date getDateAnniv() {
		return dateAnniv;
	}


	public void setDateAnniv(Date dateAnniv) {
		firePropertyChange("dateAnniv", this.dateAnniv, this.dateAnniv = dateAnniv);
	}


	public int getIdFamilleTiers() {
		return idFamilleTiers;
	}

	public void setIdFamilleTiers(int idFamilleTiers) {
		firePropertyChange("idFamilleTiers", this.idFamilleTiers, this.idFamilleTiers = idFamilleTiers);
	}

	public String getCodeFamilleTiers() {
		return codeFamilleTiers;
	}

	public void setCodeFamilleTiers(String codeFamilleTiers) {
		firePropertyChange("codeFamilleTiers", this.codeFamilleTiers, this.codeFamilleTiers = codeFamilleTiers);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}



	public void setSwtTiers(TaTiersDTO taTiersDTO){		
			this.id = taTiersDTO.id;       				//1
			this.codeTiers = taTiersDTO.codeTiers;					//2
			this.codeCompta = taTiersDTO.codeCompta;				//3
			this.compte = taTiersDTO.compte;							//4
			this.nomTiers = taTiersDTO.nomTiers;					//5
			this.prenomTiers = taTiersDTO.prenomTiers;				//6
			this.surnomTiers = taTiersDTO.surnomTiers;				//7
			this.actifTiers = taTiersDTO.actifTiers;				//8
			this.ttcTiers = taTiersDTO.ttcTiers;					//9
			this.idTCivilite = taTiersDTO.idTCivilite;			//10
			this.codeTCivilite = taTiersDTO.codeTCivilite;		//11
			this.idEntreprise = taTiersDTO.idEntreprise;			//12
			this.nomEntreprise = taTiersDTO.nomEntreprise;		//13
			this.liblEntreprise = taTiersDTO.liblEntreprise;        //14
			this.idTTiers = taTiersDTO.idTTiers;					//15
			this.codeTTiers = taTiersDTO.codeTTiers;				//16
			this.libelleTTiers = taTiersDTO.libelleTTiers;		//17
			this.idCommentaire = taTiersDTO.idCommentaire;			//18
			this.commentaire = taTiersDTO.commentaire;		//19
//			this.ID_I_BANQUE = taTiersDTO.ID_I_BANQUE;				//20
//			this.CODE_BANQUE = taTiersDTO.CODE_BANQUE;				//21
//			this.LIBC_BANQUE = taTiersDTO.LIBC_BANQUE;				//22
//			this.LIBL_BANQUE = taTiersDTO.LIBL_BANQUE;				//23
			this.idTEntite = taTiersDTO.idTEntite;				//24
			this.codeTEntite = taTiersDTO.codeTEntite;			//25
			this.tvaIComCompl = taTiersDTO.tvaIComCompl;		//26
			this.siretCompl = taTiersDTO.siretCompl;
			this.accise = taTiersDTO.accise;//27
			this.ics = taTiersDTO.ics;//28
//			this.CODE_T_ENTITE_ENTREPRISE = taTiersDTO.CODE_T_ENTITE_ENTREPRISE;				//27
			
			this.adresse1Adresse=taTiersDTO.adresse1Adresse;
			this.adresse2Adresse=taTiersDTO.adresse2Adresse;
			this.adresse3Adresse=taTiersDTO.adresse3Adresse;
			this.codepostalAdresse=taTiersDTO.codepostalAdresse;
			this.villeAdresse=taTiersDTO.villeAdresse;
			this.paysAdresse=taTiersDTO.paysAdresse;
			
			this.adresseWeb=taTiersDTO.adresseWeb;
			this.adresseEmail=taTiersDTO.adresseEmail;
			this.numeroTelephone=taTiersDTO.numeroTelephone;
			this.codeTTvaDoc=taTiersDTO.codeTTvaDoc;
			this.idTTvaDoc=taTiersDTO.idTTvaDoc;
			this.dateAnniv=taTiersDTO.dateAnniv;
			this.idFamilleTiers=taTiersDTO.idFamilleTiers;
			this.codeFamilleTiers=taTiersDTO.codeFamilleTiers;
	}
	
	public static TaTiersDTO copy(TaTiersDTO taTiersDTO){
		TaTiersDTO taTiersDTOLoc = new TaTiersDTO();
		taTiersDTOLoc.setId(taTiersDTO.id);       				//1
		taTiersDTOLoc.setCodeTiers(taTiersDTO.codeTiers);					//2
		taTiersDTOLoc.setCodeCompta(taTiersDTO.codeCompta);				//3
		taTiersDTOLoc.setCompte(taTiersDTO.compte);							//4
		taTiersDTOLoc.setNomTiers(taTiersDTO.nomTiers);					//5
		taTiersDTOLoc.setPrenomTiers(taTiersDTO.prenomTiers);				//6
		taTiersDTOLoc.setSurnomTiers(taTiersDTO.surnomTiers);				//7
		taTiersDTOLoc.setActifTiers(taTiersDTO.actifTiers);				//8
		taTiersDTOLoc.setTtcTiers(taTiersDTO.ttcTiers);					//9
		taTiersDTOLoc.setIdTCivilite(taTiersDTO.idTCivilite);			//10
		taTiersDTOLoc.setCodeTCivilite(taTiersDTO.codeTCivilite);		//11
		taTiersDTOLoc.setIdEntreprise(taTiersDTO.idEntreprise);			//12
		taTiersDTOLoc.setNomEntreprise(taTiersDTO.nomEntreprise);		//13
		taTiersDTOLoc.setLiblEntreprise(taTiersDTO.liblEntreprise);        //14
		taTiersDTOLoc.setIdTTiers(taTiersDTO.idTTiers);					//15
		taTiersDTOLoc.setCodeTTiers(taTiersDTO.codeTTiers);				//16
		taTiersDTOLoc.setLibelleTTiers(taTiersDTO.libelleTTiers);		//17
		taTiersDTOLoc.setIdCommentaire(taTiersDTO.idCommentaire);			//18
		taTiersDTOLoc.setCommentaire(taTiersDTO.commentaire);		//19
//		taTiersDTOLoc.setID_I_BANQUE(taTiersDTO.ID_I_BANQUE);				//20
//		taTiersDTOLoc.setCODE_BANQUE(taTiersDTO.CODE_BANQUE);				//21
//		taTiersDTOLoc.setLIBC_BANQUE(taTiersDTO.LIBC_BANQUE);				//22
//		taTiersDTOLoc.setLIBL_BANQUE(taTiersDTO.LIBL_BANQUE);				//23
		taTiersDTOLoc.setIdTEntite(taTiersDTO.idTEntite);				//24
		taTiersDTOLoc.setCodeTEntite(taTiersDTO.codeTEntite);			//25
		taTiersDTOLoc.setTvaIComCompl(taTiersDTO.tvaIComCompl);		//26
		taTiersDTOLoc.setSiretCompl(taTiersDTO.siretCompl);	
		taTiersDTOLoc.setAccise(taTiersDTO.accise);//27
		taTiersDTOLoc.setIcs(taTiersDTO.ics);//28
		
		taTiersDTOLoc.setAdresse1Adresse(taTiersDTO.adresse1Adresse);
		taTiersDTOLoc.setAdresse2Adresse(taTiersDTO.adresse2Adresse);
		taTiersDTOLoc.setAdresse3Adresse(taTiersDTO.adresse3Adresse);
		taTiersDTOLoc.setCodepostalAdresse(taTiersDTO.codepostalAdresse);
		taTiersDTOLoc.setVilleAdresse(taTiersDTO.villeAdresse);
		taTiersDTOLoc.setPaysAdresse(taTiersDTO.paysAdresse);
		
		taTiersDTOLoc.setAdresseEmail(taTiersDTO.adresseEmail);
		taTiersDTOLoc.setAdresseWeb(taTiersDTO.adresseWeb);
		taTiersDTOLoc.setNumeroTelephone(taTiersDTO.numeroTelephone);
		taTiersDTOLoc.setCodeTTvaDoc(taTiersDTO.codeTTvaDoc);
		taTiersDTOLoc.setIdTTvaDoc(taTiersDTO.idTTvaDoc);
		
//		taTiersDTOLoc.setCODE_T_ENTITE_ENTREPRISE(taTiersDTO.CODE_T_ENTITE_ENTREPRISE);				//27
		taTiersDTOLoc.setDateAnniv(taTiersDTO.dateAnniv);
		taTiersDTOLoc.setIdFamilleTiers(taTiersDTO.idFamilleTiers);
		taTiersDTOLoc.setCodeFamilleTiers(taTiersDTO.codeFamilleTiers);
		return taTiersDTOLoc;
	}

	public Boolean getUtiliseCompteClient() {
		return utiliseCompteClient;
	}

	public void setUtiliseCompteClient(Boolean utiliseCompteClient) {
		this.utiliseCompteClient = utiliseCompteClient;
	}

	public String getNumAgrementSanitaire() {
		return numAgrementSanitaire;
	}

	public void setNumAgrementSanitaire(String numAgrementSanitaire) {
		this.numAgrementSanitaire = numAgrementSanitaire;
	}

	public String getLibelleFamilleTiers() {
		return libelleFamilleTiers;
	}

	public void setLibelleFamilleTiers(String libelleFamilleTiers) {
		this.libelleFamilleTiers = libelleFamilleTiers;
	}

	public Boolean getEmailCleCompteClientEnvoye() {
		return emailCleCompteClientEnvoye;
	}

	public void setEmailCleCompteClientEnvoye(Boolean emailCleCompteClientEnvoye) {
		this.emailCleCompteClientEnvoye = emailCleCompteClientEnvoye;
	}

	public Date getDateDerniereConnexionCompteClient() {
		return dateDerniereConnexionCompteClient;
	}

	public void setDateDerniereConnexionCompteClient(Date dateDerniereConnexionCompteClient) {
		this.dateDerniereConnexionCompteClient = dateDerniereConnexionCompteClient;
	}

	public int getNbTiers() {
		return nbTiers;
	}

	public void setNbTiers(int nbTiers) {
		this.nbTiers = nbTiers;
	}

	public String getLiblTEntite() {
		return liblTEntite;
	}

	public void setLiblTEntite(String liblTEntite) {
		this.liblTEntite = liblTEntite;
	}

	public String getLibCPaiement() {
		return libCPaiement;
	}

	public void setLibCPaiement(String libCPaiement) {
		this.libCPaiement = libCPaiement;
	}

	public Integer getReportCPaiement() {
		return reportCPaiement;
	}

	public void setReportCPaiement(Integer reportCPaiement) {
		this.reportCPaiement = reportCPaiement;
	}

	public Integer getFinMoisCPaiement() {
		return finMoisCPaiement;
	}

	public void setFinMoisCPaiement(Integer finMoisCPaiement) {
		this.finMoisCPaiement = finMoisCPaiement;
	}

	public String getLibTPaiement() {
		return libTPaiement;
	}

	public void setLibTPaiement(String libTPaiement) {
		this.libTPaiement = libTPaiement;
	}

	public String getCompteTPaiement() {
		return compteTPaiement;
	}

	public void setCompteTPaiement(String compteTPaiement) {
		this.compteTPaiement = compteTPaiement;
	}

	public Integer getReportTPaiement() {
		return reportTPaiement;
	}

	public void setReportTPaiement(Integer reportTPaiement) {
		this.reportTPaiement = reportTPaiement;
	}

	public Integer getFinMoisTPaiement() {
		return finMoisTPaiement;
	}

	public void setFinMoisTPaiement(Integer finMoisTPaiement) {
		this.finMoisTPaiement = finMoisTPaiement;
	}

	public String getMaRefTiers() {
		return maRefTiers;
	}

	public void setMaRefTiers(String maRefTiers) {
		this.maRefTiers = maRefTiers;
	}


	
	public String getImportationDivers() {
		return importationDivers;
	}

	public void setImportationDivers(String importationDivers) {
		this.importationDivers = importationDivers;
	}

}
