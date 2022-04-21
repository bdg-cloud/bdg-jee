package fr.legrain.autorisations.autorisation.model;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fr.legrain.validator.LgrHibernateValidated;



@Entity
@Table(name = "ta_autorisations")
public class TaAutorisations implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3469441956439711712L;

	private Integer idAutorisation;
	private TaTypeProduit taTypeProduit;
	private String codeDossier;
	private String modules;
	private String modulesHTML;
	private Boolean valide=true;

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	@Transient
	private String cheminFichierXlm;
	
	
	public TaAutorisations() {
	}

	public TaAutorisations(Integer idAutorisation) {
		this.idAutorisation = idAutorisation;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_autorisation", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_autorisation",table = "ta_autorisations", champEntite="idAutorisation", clazz = TaAutorisations.class)
	public Integer getIdAutorisation() {
		return this.idAutorisation;
	}

	public void setIdAutorisation(Integer idAutorisation) {
		this.idAutorisation = idAutorisation;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_type")
	public TaTypeProduit getTaTypeProduit() {
		return this.taTypeProduit;
	}

	public void setTaTypeProduit(TaTypeProduit TaTypeProduit) {
		this.taTypeProduit = TaTypeProduit;
	}



	@Column(name = "code_dossier", length = 100)
	//@LgrHibernateValidated(champBd = "code_dossier",table = "ta_autorisations", champEntite="codeDossier", clazz = TaAutorisations.class)
	public String getCodeDossier() {
		return this.codeDossier;
	}

	public void setCodeDossier(String codeTiers) {
		this.codeDossier = codeTiers;
	}



	@Column(name = "modules", length = 100)
	@LgrHibernateValidated(champBd = "modules",table = "ta_autorisations", champEntite="modules", clazz = TaAutorisations.class)
	public String getModules() {
		return this.modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}
	


	@Column(name = "valide", length = 100)
	@LgrHibernateValidated(champBd = "valide",table = "ta_autorisations", champEntite="valide", clazz = TaAutorisations.class)
	public Boolean getValide() {
		return this.valide;
	}

	public void setValide(Boolean valide) {
		this.valide = valide;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}
	

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifAdresse) {
		this.quiModif = quiModifAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifAdresse) {
		this.quandModif = quandModifAdresse;
	}
	


	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Transient
	public String getCheminFichierXlm() {
		return cheminFichierXlm;
	}

	@Transient
	public void setCheminFichierXlm(String cheminFichierXlm) {
		this.cheminFichierXlm = cheminFichierXlm;
	}
	
	//http://stackoverflow.com/questions/25864316/pretty-print-xml-in-java-8
	public static String toPrettyString(String xml, int indent) {
	    try {
	        // Turn xml string into a document
	        Document document = DocumentBuilderFactory.newInstance()
	                .newDocumentBuilder()
	                .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

	        // Remove whitespaces outside tags
	        document.normalize();
	        XPath xPath = XPathFactory.newInstance().newXPath();
	        NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",document, XPathConstants.NODESET);

	        for (int i = 0; i < nodeList.getLength(); ++i) {
	            Node node = nodeList.item(i);
	            node.getParentNode().removeChild(node);
	        }

	        // Setup pretty print options
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        // Return pretty print xml string
	        StringWriter stringWriter = new StringWriter();
	        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
	        return stringWriter.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	@Transient
	@XmlTransient
	public String getModulesHTML() {
		return toPrettyString(this.modules,2)
				.replaceAll("&", "&amp;")
				.replaceAll(" ", "&nbsp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;")
				.replaceAll("\n", "<br/>");

		
	}

	public void setModulesHTML(String modulesHTML) {
		this.modulesHTML = modulesHTML;
	}

}
