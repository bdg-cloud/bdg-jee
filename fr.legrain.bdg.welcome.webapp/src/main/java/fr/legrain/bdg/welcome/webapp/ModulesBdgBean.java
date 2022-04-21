package fr.legrain.bdg.welcome.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ModulesBdgBean  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1686539171338669810L;

	/**
	 * 
	 */
	private List<Document> documents;
    private Document selectedDocument;
     
    @PostConstruct
    public void init() {
        documents = new ArrayList<Document>();
         
        documents.add(new Document("1", "Factures"));
        documents.add(new Document("2", "Devis"));
        documents.add(new Document("4", "Avoirs"));
        documents.add(new Document("5", "Bon de Commande"));
        documents.add(new Document("6", "Bon de Livraison"));
        documents.add(new Document("7", "Proforma"));
        documents.add(new Document("8", "Factures Apporteur"));
        documents.add(new Document("9", "Acompte"));
    }
 
    public List<Document> getDocuments() {
        return documents;
    }
 
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
 
    public Document getSelectedDocument() {
        return selectedDocument;
    }
 
    public void setSelectedDocument(Document selectedDocument) {
        this.selectedDocument = selectedDocument;
    }


}
