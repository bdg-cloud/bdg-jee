package fr.legrain.general.model;

import java.awt.AlphaComposite;

// Generated Sep 1, 2008 3:06:27 PM by Hibernate Tools 3.2.0.CR1

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;







import javax.imageio.ImageIO;
//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
//import javax.persistence.OneToMany;
//import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

/**
 * TaFichier modifier par Dima
 */
@Entity

@Table(name = "ta_fichier")

public class TaFichier implements java.io.Serializable {

	private static final long serialVersionUID = 496592244972519100L;

	/**		Extrais de la BdDs: 

		id_fichier serial NOT NULL,
		nom_fichier_original character varying(50),
		blob_fichier_original oid,
		chemin_fichier_original character varying(255),
		type_fichier_original serial NOT NULL,
		nom_fichier_miniature character varying(50),
		blob_fichier_miniature oid,
		chemin_fichier_miniature character varying(255),
		type_fichier_miniature serial NOT NULL,

		qui_cree character varying(50),
		quand_cree time with time zone DEFAULT now(),
		qui_modif character varying(50),
		quand_modif time with time zone DEFAULT now(),
		version character varying(20),
		ip_acces character(50),
		version_obj integer,
	 */

	private int idFichier;

	private String nomFichierOriginal;
	private byte[] blobFichierOriginal;
	private String cheminFichierOriginal;
	private int typeFichierOriginal;
	private String nomFichierStandart;
	private byte[] blobFichierStandart;
	private String cheminFichierStandart;
	private int typeFichierStandart;
	private String nomFichierMiniature;
	private byte[] blobFichierMiniature;
	private String cheminFichierMiniature;
	private int typeFichierMiniature;

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String version;
	private String ipAcces;
	private Integer versionObj;

	public TaFichier() {
	}

	public TaFichier(int idFichier) {
		this.idFichier = idFichier;
	}

	/**
	 * @param idFichier
	 * @param nomFichierOriginal
	 * @param blobFichierOriginal
	 * @param cheminFichierOriginal
	 * @param typeFichierOriginal
	 * @param nomFichierMiniature
	 * @param blobFichierMiniature
	 * @param cheminFichierMiniature
	 * @param typeFichierMiniature
	 * @param quiCree
	 * @param quandCree
	 * @param quiModif
	 * @param quandModif
	 * @param version
	 * @param ipAcces
	 * @param versionObj
	 */
	public TaFichier(int idFichier, String nomFichierOriginal,
			byte[] blobFichierOriginal, String cheminFichierOriginal,
			int typeFichierOriginal, String nomFichierMiniature,
			byte[] blobFichierMiniature, String cheminFichierMiniature,
			int typeFichierMiniature, String quiCree, Date quandCree,
			String quiModif, Date quandModif, String version, String ipAcces,
			Integer versionObj) {
		super();
		this.idFichier = idFichier;
		this.nomFichierOriginal = nomFichierOriginal;
		this.blobFichierOriginal = blobFichierOriginal;
		this.cheminFichierOriginal = cheminFichierOriginal;
		this.typeFichierOriginal = typeFichierOriginal;
		this.nomFichierMiniature = nomFichierMiniature;
		this.blobFichierMiniature = blobFichierMiniature;
		this.cheminFichierMiniature = cheminFichierMiniature;
		this.typeFichierMiniature = typeFichierMiniature;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.version = version;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	/**
	 * @return the idFichier
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fichier", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_fichier",table = "ta_fichier", champEntite="idFichier", clazz = TaFichier.class)
	public int getIdFichier() {
		return idFichier;
	}

	/**
	 * @param idFichier the idFichier to set
	 */
	public void setIdFichier(int idFichier) {
		this.idFichier = idFichier;
	}

	/**
	 * @return the nomFichierOriginal
	 */
	@Column(name = "nom_fichier_original", length = 50)
	@LgrHibernateValidated(champBd = "nom_fichier_original",table = "ta_fichier", champEntite="nomFichierOriginal", clazz = TaFichier.class)
	public String getNomFichierOriginal() {
		return nomFichierOriginal;
	}

	/**
	 * @param nomFichierOriginal the nomFichierOriginal to set
	 */
	public void setNomFichierOriginal(String nomFichierOriginal) {
		this.nomFichierOriginal = nomFichierOriginal;
	}

	/**
	 * @return the blobFichierOriginal
	 */
	@Lob
	@Column(name = "blob_fichier_original")
	@LgrHibernateValidated(champBd = "blob_fichier_original",table = "ta_fichier", champEntite="blobFichierOriginal", clazz = TaFichier.class)
	public byte[] getBlobFichierOriginal() {
		return blobFichierOriginal;
	}

	/**
	 * @param blobFichierOriginal the blobFichierOriginal to set
	 */

	public void setBlobFichierOriginal(byte[] blobFichierOriginal) {
		this.blobFichierOriginal = blobFichierOriginal;
	}

	/**
	 * @return the cheminFichierOriginal
	 */
	@Column(name = "chemin_fichier_original")
	@LgrHibernateValidated(champBd = "chemin_fichier_original",table = "ta_fichier", champEntite="cheminFichierOriginal", clazz = TaFichier.class)
	public String getCheminFichierOriginal() {
		return cheminFichierOriginal;
	}

	/**
	 * @param cheminFichierOriginal the cheminFichierOriginal to set
	 */
	public void setCheminFichierOriginal(String cheminFichierOriginal) {
		this.cheminFichierOriginal = cheminFichierOriginal;
	}

	/**
	 * @return the typeFichierOriginal
	 */
	@Column(name = "type_fichier_original", nullable = false)
	@LgrHibernateValidated(champBd = "type_fichier_original",table = "ta_fichier", champEntite="typeFichierOriginal", clazz = TaFichier.class)
	public int getTypeFichierOriginal() {
		return typeFichierOriginal;
	}

	/**
	 * @param typeFichierOriginal the typeFichierOriginal to set
	 */
	public void setTypeFichierOriginal(int typeFichierOriginal) {
		this.typeFichierOriginal = typeFichierOriginal;
	}

	/**
	 * @return the nomFichierStandart
	 */
	@Column(name = "nom_fichier_standart", length = 50)
	@LgrHibernateValidated(champBd = "nom_fichier_standart",table = "ta_fichier", champEntite="nomFichierStandart", clazz = TaFichier.class)
	public String getNomFichierStandart() {
		return nomFichierStandart;
	}

	/**
	 * @param nomFichierStandart the nomFichierStandart to set
	 */
	public void setNomFichierStandart(String nomFichierStandart) {
		this.nomFichierStandart = nomFichierStandart;
	}

	/**
	 * @return the blobFichierStandart
	 */
	@Lob
	@Column(name = "blob_fichier_standart")
	@LgrHibernateValidated(champBd = "blob_fichier_standart",table = "ta_fichier", champEntite="blobFichierStandart", clazz = TaFichier.class)
	public byte[] getBlobFichierStandart() {
		return blobFichierStandart;
	}

	/**
	 * @param blobFichierStandart the blobFichierStandart to set
	 */
	public void setBlobFichierStandart(byte[] blobFichierStandart) {
		this.blobFichierStandart = blobFichierStandart;
	}

	/**
	 * @return the cheminFichierStandart
	 */
	@Column(name = "chemin_fichier_standart")
	@LgrHibernateValidated(champBd = "chemin_fichier_standart",table = "ta_fichier", champEntite="cheminFichierStandart", clazz = TaFichier.class)
	public String getCheminFichierStandart() {
		return cheminFichierStandart;
	}

	/**
	 * @param cheminFichierStandart the cheminFichierStandart to set
	 */
	public void setCheminFichierStandart(String cheminFichierStandart) {
		this.cheminFichierStandart = cheminFichierStandart;
	}

	/**
	 * @return the typeFichierStandart
	 */
	@Column(name = "type_fichier_standart", nullable = false)
	@LgrHibernateValidated(champBd = "type_fichier_standart",table = "ta_fichier", champEntite="typeFichierStandart", clazz = TaFichier.class)
	public int getTypeFichierStandart() {
		return typeFichierStandart;
	}

	/**
	 * @param typeFichierStandart the typeFichierStandart to set
	 */
	public void setTypeFichierStandart(int typeFichierStandart) {
		this.typeFichierStandart = typeFichierStandart;
	}

	/**
	 * @return the nomFichierMiniature
	 */
	@Column(name = "nom_fichier_miniature", length = 50)
	@LgrHibernateValidated(champBd = "nom_fichier_miniature",table = "ta_fichier", champEntite="nomFichierMiniature", clazz = TaFichier.class)
	public String getNomFichierMiniature() {
		return nomFichierMiniature;
	}

	/**
	 * @param nomFichierMiniature the nomFichierMiniature to set
	 */
	public void setNomFichierMiniature(String nomFichierMiniature) {
		this.nomFichierMiniature = nomFichierMiniature;
	}

	/**
	 * @return the blobFichierMiniature
	 */
	@Lob
	@Column(name = "blob_fichier_miniature")
	@LgrHibernateValidated(champBd = "blob_fichier_miniature",table = "ta_fichier", champEntite="blobFichierMiniature", clazz = TaFichier.class)
	public byte[] getBlobFichierMiniature() {
		return blobFichierMiniature;
	}

	/**
	 * @param blobFichierMiniature the blobFichierMiniature to set
	 */

	public void setBlobFichierMiniature(byte[] blobFichierMiniature) {
		this.blobFichierMiniature = blobFichierMiniature;
	}

	/**
	 * @return the cheminFichierMiniature
	 */
	@Column(name = "chemin_fichier_miniature")
	@LgrHibernateValidated(champBd = "chemin_fichier_miniature",table = "ta_fichier", champEntite="cheminFichierMiniature", clazz = TaFichier.class)
	public String getCheminFichierMiniature() {
		return cheminFichierMiniature;
	}

	/**
	 * @param cheminFichierMiniature the cheminFichierMiniature to set
	 */
	public void setCheminFichierMiniature(String cheminFichierMiniature) {
		this.cheminFichierMiniature = cheminFichierMiniature;
	}

	/**
	 * @return the typeFichierMiniature
	 */
	@Column(name = "type_fichier_miniature", nullable = false)
	@LgrHibernateValidated(champBd = "type_fichier_miniature",table = "ta_fichier", champEntite="typeFichierMiniature", clazz = TaFichier.class)
	public int getTypeFichierMiniature() {
		return typeFichierMiniature;
	}

	/**
	 * @param typeFichierMiniature the typeFichierMiniature to set
	 */
	public void setTypeFichierMiniature(int typeFichierMiniature) {
		this.typeFichierMiniature = typeFichierMiniature;
	}

	/**
	 * @return the quiCree
	 */
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return quiCree;
	}

	/**
	 * @param quiCree the quiCree to set
	 */
	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	/**
	 * @return the quandCree
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return quandCree;
	}

	/**
	 * @param quandCree the quandCree to set
	 */
	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}

	/**
	 * @return the quiModif
	 */
	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return quiModif;
	}

	/**
	 * @param quiModif the quiModif to set
	 */
	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}

	/**
	 * @return the quandModif
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return quandModif;
	}

	/**
	 * @param quandModif the quandModif to set
	 */
	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	/**
	 * @return the version
	 */
	@Column(name = "version", length = 20)
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the ipAcces
	 */
	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return ipAcces;
	}

	/**
	 * @param ipAcces the ipAcces to set
	 */
	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	/**
	 * @return the versionObj
	 */
	@Version
	@Column(name = "version_obj", precision = 15)
	public Integer getVersionObj() {
		return versionObj;
	}

	/**
	 * @param versionObj the versionObj to set
	 */
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(blobFichierMiniature);
		result = prime * result + Arrays.hashCode(blobFichierOriginal);
		result = prime * result + Arrays.hashCode(blobFichierStandart);
		result = prime
				* result
				+ ((cheminFichierMiniature == null) ? 0
						: cheminFichierMiniature.hashCode());
		result = prime
				* result
				+ ((cheminFichierOriginal == null) ? 0 : cheminFichierOriginal
						.hashCode());
		result = prime
				* result
				+ ((cheminFichierStandart == null) ? 0 : cheminFichierStandart
						.hashCode());
		result = prime * result + idFichier;
		result = prime * result + ((ipAcces == null) ? 0 : ipAcces.hashCode());
		result = prime
				* result
				+ ((nomFichierMiniature == null) ? 0 : nomFichierMiniature
						.hashCode());
		result = prime
				* result
				+ ((nomFichierOriginal == null) ? 0 : nomFichierOriginal
						.hashCode());
		result = prime
				* result
				+ ((nomFichierStandart == null) ? 0 : nomFichierStandart
						.hashCode());
		result = prime * result
				+ ((quandCree == null) ? 0 : quandCree.hashCode());
		result = prime * result
				+ ((quandModif == null) ? 0 : quandModif.hashCode());
		result = prime * result + ((quiCree == null) ? 0 : quiCree.hashCode());
		result = prime * result
				+ ((quiModif == null) ? 0 : quiModif.hashCode());
		result = prime * result + typeFichierMiniature;
		result = prime * result + typeFichierOriginal;
		result = prime * result + typeFichierStandart;
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result
				+ ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaFichier other = (TaFichier) obj;
		if (!Arrays.equals(blobFichierMiniature, other.blobFichierMiniature))
			return false;
		if (!Arrays.equals(blobFichierOriginal, other.blobFichierOriginal))
			return false;
		if (!Arrays.equals(blobFichierStandart, other.blobFichierStandart))
			return false;
		if (cheminFichierMiniature == null) {
			if (other.cheminFichierMiniature != null)
				return false;
		} else if (!cheminFichierMiniature.equals(other.cheminFichierMiniature))
			return false;
		if (cheminFichierOriginal == null) {
			if (other.cheminFichierOriginal != null)
				return false;
		} else if (!cheminFichierOriginal.equals(other.cheminFichierOriginal))
			return false;
		if (cheminFichierStandart == null) {
			if (other.cheminFichierStandart != null)
				return false;
		} else if (!cheminFichierStandart.equals(other.cheminFichierStandart))
			return false;
		if (idFichier != other.idFichier)
			return false;
		if (ipAcces == null) {
			if (other.ipAcces != null)
				return false;
		} else if (!ipAcces.equals(other.ipAcces))
			return false;
		if (nomFichierMiniature == null) {
			if (other.nomFichierMiniature != null)
				return false;
		} else if (!nomFichierMiniature.equals(other.nomFichierMiniature))
			return false;
		if (nomFichierOriginal == null) {
			if (other.nomFichierOriginal != null)
				return false;
		} else if (!nomFichierOriginal.equals(other.nomFichierOriginal))
			return false;
		if (nomFichierStandart == null) {
			if (other.nomFichierStandart != null)
				return false;
		} else if (!nomFichierStandart.equals(other.nomFichierStandart))
			return false;
		if (quandCree == null) {
			if (other.quandCree != null)
				return false;
		} else if (!quandCree.equals(other.quandCree))
			return false;
		if (quandModif == null) {
			if (other.quandModif != null)
				return false;
		} else if (!quandModif.equals(other.quandModif))
			return false;
		if (quiCree == null) {
			if (other.quiCree != null)
				return false;
		} else if (!quiCree.equals(other.quiCree))
			return false;
		if (quiModif == null) {
			if (other.quiModif != null)
				return false;
		} else if (!quiModif.equals(other.quiModif))
			return false;
		if (typeFichierMiniature != other.typeFichierMiniature)
			return false;
		if (typeFichierOriginal != other.typeFichierOriginal)
			return false;
		if (typeFichierStandart != other.typeFichierStandart)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

	public void fichierVersBinaire (String nomFichier) { //par Dima
		File f = new File(nomFichier);
		Path path = Paths.get(nomFichier);
		try {
			byte[] data = Files.readAllBytes(path);
			setNomFichierOriginal(f.getName());
			setBlobFichierOriginal(data);
			System.out.println("!!!!!!!!!!!!!! encodage de \"<"+nomFichier+">\" est reussi !!!!!!!!!!!!!!!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("!!!!!!!!!!!!!! encodage de \"<"+nomFichier+">\" n'est reussi!");
		}

	}

	public void binaireVersFichier (String lien) { //par Dima
		File f = new File(lien+getNomFichierOriginal());
		try {
			FileOutputStream fs = new FileOutputStream(f);
			System.out.println("****************  OID de BLOB  *******************");
			System.out.println("Nom fichier : "+getNomFichierOriginal());
			System.out.println(getBlobFichierOriginal());
			System.out.println("**************************************************");
			fs.write(getBlobFichierOriginal());
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param return (int)ID_Fichier
	 * */
	public int uploadFichier(String nom, byte[] img){
		setNomFichierOriginal(nom);
		setBlobFichierOriginal(img);
		return getIdFichier();
	}//uploadFichier

	/**
	 * scale image
	 * 
	 * @param sbi image to scale
	 * @param imageType type of image
	 * @param dWidth width of destination image
	 * @param dHeight height of destination image
	 * @param fWidth x-factor for transformation / scaling
	 * @param fHeight y-factor for transformation / scaling
	 * @return scaled image
	 */
	public static BufferedImage scale(BufferedImage sbi, int iWidth, int iHeight) {//par Dima

		int imageWidth = 1;
		int imageHeight = 1;
		
		int dWidth = iWidth;
		int dHeight = iHeight;
		double scaleX = (double)1;
		double scaleY = (double)1;
		double scale = (double)1;
		
		try {
			//init dimension de l'image d'origine
			imageWidth  = sbi.getWidth();
			imageHeight = sbi.getHeight();
		
			scaleX = (double)imageWidth/dWidth;
			scaleY = (double)imageHeight/dHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
//		if (imageHeight > imageWidth){
//			scaleX = (double)imageHeight/dWidth;
//			scaleY = (double)imageWidth/dHeight;
//		}
		

		System.out.println("******************* Ratio X/Y **************************** "+scaleX+" / "+scaleY+" *******************************************************");
		
		if (scaleX >= scaleY){
			scale = scaleX;
		} else {
			scale = scaleY;
		}
		
		System.out.println("******************* Orig X/Y  **************************** "+imageWidth+" / "+imageHeight+" *******************************************************");
		
		dWidth = (int) (imageWidth / scaleX);
		dHeight = (int) (imageHeight / scaleY); 
		
		System.out.println("******************* Demande X/Y **************************** "+dWidth+" / "+dHeight+" *******************************************************");

		if (imageWidth >= dWidth && imageHeight >= dHeight){
			scaleY = scaleX = scale;
			dWidth = (int) (imageWidth * scaleX);
			dHeight = (int) (imageHeight * scaleY);
		}

		if (imageHeight >= dWidth && imageWidth >= dHeight){
			scaleY = scaleX = scale;
		}
		
//		if (imageWidth >= dWidth && imageHeight < dHeight){
//			scaleY = scaleX = (double)dWidth/imageWidth;
//			dHeight = imageHeight;
//			if (dWidth == imageWidth) 
//				dWidth = imageWidth;
//			else
//				dWidth = (int) (imageWidth * scaleX);
//		}
//		
//		if (imageWidth < dWidth && imageHeight >= dHeight){
//			scaleX = scaleY = (double)dHeight/imageHeight;
//			dWidth = imageWidth;
//			if (dHeight == imageHeight)
//				dHeight = imageHeight;
//			else
//				dHeight = (int) (imageHeight * scaleY);
//		}
		
		if (imageWidth >= dWidth && imageHeight < dHeight){
			scaleY = scaleX = (double)dWidth/imageWidth;
			dHeight = (int) (imageHeight * scaleY);
		}
		
		if (imageWidth < dWidth && imageHeight >= dHeight){
			scaleX = scaleY = (double)dHeight/imageHeight;
			dWidth = (int) (imageWidth * scaleX);
		}
		
		if (dWidth == imageWidth && dHeight == imageHeight){
			scaleX = (double)1;
			scaleY = (double)1;
		}
			
		if (dWidth != imageWidth){
			if (dWidth > imageWidth)
				dWidth = imageWidth;
			else
				dWidth = (int) (imageWidth * scaleX);
		} else {
			dWidth = imageWidth;
		}

		if (dHeight != imageHeight){
			if (dHeight > imageHeight)
				dHeight = imageHeight;
			else
				dHeight = (int) (imageHeight * scaleY);
		} else {
			dHeight = imageHeight;
		}
		
//		if (dWidth > iWidth) {
//			scaleX = scaleY = (double)iWidth/dWidth; 
//			scale = (scale/(double)iWidth/dWidth);
//			dWidth = (int) (imageWidth * scale);
//			dHeight = (int) (imageHeight * scale);
//		}
//		if (dHeight > iHeight) {
//			scaleX = scaleY = (double)iHeight/dHeight; 
//			scale = (scale/(double)iHeight/dHeight);
//			dHeight = (int) (imageHeight * scale);
//			dWidth = (int) (imageWidth * scale);
//		}
		
		System.out.println("******************* Corrigé X/Y **************************** "+dWidth+" / "+dHeight+" *******************************************************");
		
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage Image = bilinearScaleOp.filter(sbi, new BufferedImage(dWidth, dHeight, sbi.getType()));

		return Image;

	}

	public byte[] resizeImage(byte [] img, String name, int xDemande, int yDemande){//par Dima

		byte[] imageInByte = null;
		try {

			InputStream in1 = new ByteArrayInputStream(img);
			BufferedImage originalImage = ImageIO.read(in1);

			BufferedImage smallImage = scale(originalImage, xDemande, yDemande);
			//			BufferedImage smallImage = scale(originalImage, originalImage.getType(), 600, 480, (double)(xDemande/100), (double)(yDemande/100));

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(smallImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();

			// convert byte array back to BufferedImage - partie TEST
			InputStream in = new ByteArrayInputStream(imageInByte);
			BufferedImage bImageFromConvert = ImageIO.read(in);

			ImageIO.write(bImageFromConvert, "jpg", new File("c:\\tmp\\"+name+"-new-output.jpg"));
			//			ImageIO.write(bImageFromConvert, "jpg", imageInByte);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

				return imageInByte;
		//return null; // etat TEST
	}
	
	/**
     * This function resize the image file and returns the BufferedImage object that can be saved to file system.
     */
    public static BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }
    
    public static BufferedImage resizeImageMax(final Image image, int maxWidth, int maxHeight) {
    	BufferedImage res = null;
    	int newWidth = 0;
    	int newHeight = 0;
    	
    	int imgWidth = image.getWidth(null);
    	int imgHeight = image.getHeight(null);
    	
    	double scaleW = 0;
    	double scaleH = 0;
    	double scale = 0;
    	
    	if(imgWidth>maxWidth) {
    		//trop large, il faut redimensionner
    		scaleW = (double)maxWidth/(double)imgWidth;
    	} 
    	if(imgHeight>maxHeight) {
    		//trop haut, il faut redimensionner
    		scaleH = (double)maxHeight/(double)imgHeight;
    	}
    	
    	//on prend le plus petit ratio différent de 0
    	if(scaleW!=0 && scaleH!=0 && scaleW<=scaleH) {
    		scale = scaleW;
    	} else if(scaleW!=0 && scaleH!=0 && scaleW>scaleH) {
    		scale = scaleH;
    	} else if(scaleW==0 && scaleH!=0) {
    		scale = scaleH;
    	} else if(scaleH==0 && scaleW!=0) {
    		scale = scaleW;
    	}
    	
    	if(scale!=0) {
	    	newWidth = (int) (imgWidth*scale);
	    	newHeight = (int) (imgHeight*scale);
    	}
    	
    	System.out.println("=== Image "+imgWidth+"x"+imgHeight);
    	System.out.println("=== Ratio "+scale);
    	System.out.println("=== Image nouvelle "+newWidth+"x"+newHeight);
    	
    	res = resizeImage(image, newWidth, newHeight);
    	return res;
    }
    
    /**
     * scale image
     * 
     * @param sbi image to scale
     * @param imageType type of image
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     * @param fWidth x-factor for transformation / scaling
     * @param fHeight y-factor for transformation / scaling
     * @return scaled image
     */
    public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if(sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

}
