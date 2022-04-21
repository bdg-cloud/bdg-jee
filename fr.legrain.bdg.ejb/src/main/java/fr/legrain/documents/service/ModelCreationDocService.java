package fr.legrain.documents.service;

import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.IModelCreationDocServiceRemote;
import fr.legrain.document.dto.CreationDocLigneDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaCreationDoc;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class ModelCreationDocService  implements IModelCreationDocServiceRemote{
	

	private LinkedList<CreationDocLigneDTO> listeObjet = new LinkedList<CreationDocLigneDTO>();
	private Collection<TaCreationDoc> listeEntity = new LinkedList<TaCreationDoc>();
	


	TypeDoc typeDocPresent = TypeDoc.getInstance();
//	EntityManager em =new TaFactureDAO().getEntityManager();


	
	
	public LinkedList<CreationDocLigneDTO> remplirListe() {
		int compteur=0;
		CreationDocLigneDTO ihmTiers=null;
		compteur++;
		CreationDocLigneDTO ihmligne=null;
		CreationDocLigneDTO ihmDoc=null;
		String codeTiersCourant="";
		int compteurDoc=0;
		if(listeObjet==null)listeObjet=new LinkedList<CreationDocLigneDTO>();
		String libelle="";
 		listeObjet.clear();
 		int compteurDocTiers=0;
		if(getListeEntity()!=null){
		for (Object t : getListeEntity()) {
			if(!codeTiersCourant.equals(((TaCreationDoc)t).getCodeTiers())){
				if(ihmTiers!=null)ihmTiers.setCodeDocument(ihmTiers.getCodeTiers()+" ("+compteurDocTiers+")");
				ihmTiers= new CreationDocLigneDTO(compteur);
				compteurDocTiers=0;
				compteur++;
				ihmTiers.setCodeTiers(((TaCreationDoc)t).getCodeTiers());
				ihmTiers.setCodeTiersParent(((TaCreationDoc)t).getCodeTiers());
				ihmTiers.setCodeDocument(((TaCreationDoc)t).getCodeTiers());
				ihmTiers.setAccepte(true);
				ihmTiers.setNbDecimalesPrix(((TaCreationDoc)t).getNbDecimalesPrix());
				listeObjet.add(ihmTiers);
				codeTiersCourant=((TaCreationDoc)t).getCodeTiers();
			}
			ihmDoc=new CreationDocLigneDTO(compteur);
			compteur++;
			compteurDocTiers++;
			ihmDoc.setCodeDocument(((TaCreationDoc)t).getCodeDocument());
			//ihmDoc.setCodeTiers(ihmTiers.getCodeTiers());
			ihmDoc.setCodeTiersParent(ihmTiers.getCodeTiers());
			ihmDoc.setAccepte(true);
			ihmTiers.getList().add(ihmDoc);
			
			for (IDocumentTiers doc : ((TaCreationDoc)t).getListeDoc()) {
				//libelle+=doc.getCodeDocument()+"-";				
				ihmligne=new CreationDocLigneDTO(compteur);
				compteur++;
				ihmligne.setLigneFinale(true);
				ihmligne.setCodeDocument(ihmDoc.getCodeDocument()) ;
				ihmligne.setCodeTiersParent(ihmTiers.getCodeTiers()) ;
				ihmligne.setCodeDocumentRecup(doc.getCodeDocument());
				ihmligne.setLibelleLigne(doc.getLibelleDocument());
				ihmligne.setDateDocument(doc.getDateDocument());
				ihmligne.setNetTtc(doc.getNetTtcCalc());
				ihmligne.setAccepte(true);
				ihmligne.setNbDecimalesPrix(doc.getNbDecimalesPrix());
				ihmDoc.setNbDecimalesPrix(doc.getNbDecimalesPrix());
				ihmDoc.getList().add(ihmligne);
				compteurDoc++;
				ihmligne=null;
			}
			libelle=((TaCreationDoc)t).getLibelleDocument()+libelle+
					" ("+compteurDoc+")";
			ihmDoc.setLibelleDocument(libelle);
			compteurDoc=0;
			libelle="";
		}
		if(ihmTiers!=null)ihmTiers.setCodeDocument(ihmTiers.getCodeTiers()+" ("+compteurDocTiers+")");
		}

		return listeObjet;
	}




	public LinkedList<CreationDocLigneDTO> getListeObjet() {
		return listeObjet;
	}





	public Collection<TaCreationDoc> getListeEntity() {
		return listeEntity;
	}


	public void setListeEntity(Collection<TaCreationDoc> listeEntity) {
		this.listeEntity = listeEntity;
	}



	
}
