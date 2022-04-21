package fr.legrain.login.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


@Entity
@Table(name = "ta_utilisateur")
@NamedQueries(value = { 
		@NamedQuery(name=TaUtilisateurLogin.QN.FIND_BY_USERNAME, query="select f from TaUtilisateurLogin f where f.username= :code")
		})
public class TaUtilisateurLogin implements Serializable {
	
	private static final long serialVersionUID = 8501805659367719874L;

	public static class QN {
		public static final String FIND_BY_USERNAME = "TaUtilisateurLogin.findByUsernameLogin";
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="passwd")
	private String passwd;
	
	@OneToMany(mappedBy="taUtilisateur", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
	private List<TaRRoleUtilisateurLogin> roles;
	
//	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
//	@JoinColumn(name = "id_entreprise")
//	private TaEntrepriseClient taEntreprise;
	
	@Column(name="nom")
	private String nom;
	
	@Column(name="prenom")
	private String prenom;
	
	@Column(name="email")
	private String email;
	
	@Column(name="dernier_acces")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dernierAcces;
	
	@Column(name="actif")
	private Integer actif;
	
	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandCree;
	
	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandModif;
	
	@Column(name="qui_cree")
	private Integer quiCree;
	
	@Column(name="qui_modif")
	private Integer quiModif;
	
	@Column(name = "ip_acces")
	private String ipAcces;
	
	@Version
	@Column(name = "version_obj")
	private Integer versionObj;
	
	public String passwordHashSHA256_Base64(String originalPassword) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			
			Base64.Encoder enc = Base64.getEncoder()/*.withoutPadding()*/;
			
//			BASE64Encoder enc = new sun.misc.BASE64Encoder();

			byte[] digest = md.digest(originalPassword.getBytes("UTF-8")); // Missing charset
			String base64 = enc.encodeToString(digest);

			return base64;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean hasRole(String role) {
		for (TaRRoleUtilisateurLogin r : roles) {
			if(r.getTaRole().getRole().equals(role))
			return true;
		}
        return false;
    }
	
	public boolean isDev() {
    	return isDev(getUsername());
    }
    
    public boolean isDev(String username) {
    	boolean isDev = false;
    	if(username!=null && !username.equals("")) {
    		if(username.equals("nicolas")
    				|| username.equals("ppottier")
    				) {
    			isDev = true;
    		}
    	}
    	return isDev;
    }
    
    public boolean isDevLgr() {
    	return isDevLgr(getUsername());
    }
    
    public boolean isDevLgr(String username) {
    	boolean isDev = false;
    	if(username!=null && !username.equals("")) {
    		if(username.equals("nicolas")
    				) {
    			isDev = true;
    		}
    	}
    	return isDev;
    }
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public List<TaRRoleUtilisateurLogin> getRoles() {
		return roles;
	}

	public void setRoles(List<TaRRoleUtilisateurLogin> roles) {
		this.roles = roles;
	}

//	public TaEntrepriseClient getTaEntreprise() {
//		return taEntreprise;
//	}
//
//	public void setTaEntreprise(TaEntrepriseClient userCompany) {
//		this.taEntreprise = userCompany;
//	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDernierAcces() {
		return dernierAcces;
	}

	public void setDernierAcces(Date dernierAcces) {
		this.dernierAcces = dernierAcces;
	}

	public Date getQuandCree() {
		return quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public Integer getQuiCree() {
		return quiCree;
	}

	public Integer getQuiModif() {
		return quiModif;
	}

	public String getIpAcces() {
		return ipAcces;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setQuandCree(Date quanCree) {
		this.quandCree = quanCree;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public void setQuiCree(Integer quiCree) {
		this.quiCree = quiCree;
	}

	public void setQuiModif(Integer quiModif) {
		this.quiModif = quiModif;
	}

	public void setIpAcces(String ip) {
		this.ipAcces = ip;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public Integer getActif() {
		return actif;
	}

	public void setActif(Integer actif) {
		this.actif = actif;
	}

}
