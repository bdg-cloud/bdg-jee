package fr.legrain.data;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.FinderException;

import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFlashServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLPrelevement;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTicketDeCaisse;

public abstract class AbstractDocumentService<Entity,DTO> extends AbstractApplicationDAOServer<Entity,DTO>  {

	public abstract TaEtat rechercheEtatInitialDocument();
	
    private @EJB ITaApporteurServiceRemote taApporteurServiceRemote;
    private @EJB ITaAbonnementServiceRemote taAbonnementServiceRemote;
    private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceServiceRemote;
    private @EJB ITaAvoirServiceRemote taAvoirServiceRemote;
    private @EJB ITaBoncdeServiceRemote taBoncdeServiceRemote;
    private @EJB ITaBoncdeAchatServiceRemote taBoncdeAchatServiceRemote;
    private @EJB ITaBonlivServiceRemote taBonlivServiceRemote; 
    private @EJB ITaBonReceptionServiceRemote taBonReceptionServiceRemote;
    private @EJB ITaDevisServiceRemote taDevisServiceRemote;
    private @EJB ITaFactureServiceRemote taFactureServiceRemote;
    private @EJB ITaAcompteServiceRemote taAcompteServiceRemote;
    private @EJB ITaPreparationServiceRemote taPreparationServiceRemote;
    private @EJB ITaPrelevementServiceRemote taPrelevementServiceRemote;
    private @EJB ITaProformaServiceRemote taProformaServiceRemote;
    private @EJB ITaFlashServiceRemote taFlashServiceRemote;
    private @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseServiceRemote;
	
    private @EJB ITaLigneALigneServiceRemote taLigneALigneServiceRemote;
	
	public AbstractDocumentService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AbstractDocumentService(Class<Entity> entityClass, Class<DTO> dtoClass) {
		super(entityClass, dtoClass);
		// TODO Auto-generated constructor stub
	}

    protected TaEtat etatLigneInsertion(IDocumentTiers detachedInstance) {
    	TaEtat etat = rechercheEtatInitialDocument();
		if(detachedInstance.getTaREtat()!=null && detachedInstance.getTaREtat().getTaEtat()!=null && 
				detachedInstance.getTaREtat().getTaEtat().getPosition().compareTo(taEtatService.documentBrouillon(null).getPosition())>0) {
			etat=taEtatService.documentEncours(null);
		}

    	return etat;
    }
    

	
    protected TaEtat changeEtatDocument(IDocumentTiers detachedInstance) {
		boolean trouveNonTermine=false;
		boolean trouveAbandonne=false;
		boolean trouveTransformation=false;

		List<TaEtat> liste=new LinkedList<TaEtat>();
//		TaEtat etatDocPossible = null;
		TaEtat etatDocPossible = taEtatService.documentEncours(null);
//		int rang=-1;
		int position=-1;
		
		//changement d'état manuel pour abandonner le document -- à remettre si on decide de faire comme cela 
		//(logiquement il faudrait passer par les lignes pour abandonner totalement un document !!!)
//		if(detachedInstance.getTaREtat()!=null && detachedInstance.getTaREtat().getTaEtat()!=null && 
//				detachedInstance.getTaREtat().getTaEtat().getIdentifiant().equals(TaEtat.DOCUMENT_ABANDONNE)) {
//			etatDocPossible=detachedInstance.getTaREtat().getTaEtat();
//		}else {
		for (ILigneDocumentTiers l : detachedInstance.getLignesGeneral()) {//parcours des lignes du document
			if(l.getTaArticle()!=null && l.getQteLDocument()!=null) {//s'il y a un article et une qté not null
				if(!trouveNonTermine && (l.getTaREtatLigneDocument()==null ||
						// ou que l'état est du type d'Etat (TaTEtat) 'EnCours' (il y a aussi le type d'Etat 'Terminé' et d'autres comme 'Brouillon'
						(l.getTaREtatLigneDocument()!=null && l.getTaREtatLigneDocument().getTaEtat()!=null && l.getTaREtatLigneDocument().getTaEtat().getTaTEtat()!=null && l.getTaREtatLigneDocument().getTaEtat().getTaTEtat().getCodeTEtat().equalsIgnoreCase(TaEtat.ENCOURS))))
					trouveNonTermine=true;//alors il reste une ligne au moins non terminée
				if(!trouveAbandonne && 
						//et que l'on trouve un état qui contient le mot abandon dans son identifiant, alors il y a au moins une ligne abandonnée même partiellement
						(l.getTaREtatLigneDocument()!=null && l.getTaREtatLigneDocument().getTaEtat()!=null && l.getTaREtatLigneDocument().getTaEtat().getIdentifiant()!=null && l.getTaREtatLigneDocument().getTaEtat().getIdentifiant().toLowerCase().contains(TaEtat.ABANDONNE.toLowerCase())))
					trouveAbandonne=true;
				if(l.getTaREtatLigneDocument()!=null && l.getTaREtatLigneDocument().getTaEtat()!=null)
					//s'il y a un état sur la ligne alors on le rajoute à la liste des états trouvés
					liste.add(l.getTaREtatLigneDocument().getTaEtat());

				if(!detachedInstance.getTypeDocument().equals(TaFacture.TYPE_DOC)) {
					//si document n'est pas une facture, alors on prends en compte les lignes à lignes pour déterminer l'état
					for (TaLigneALigne ll : l.getTaLigneALignes()) {
						// on parcours la liste de liens pour trouver si la ligne en cours de traitement est une ligne source d'une liaison
						if(!trouveTransformation &&ll.getIdLigneSrc().equals(l.getIdLDocument()))
							trouveTransformation=true;
					}
				}
			}
		}
		//si liste d'état remplie, on initialise avec le premier etat trouvé
		if(liste!=null && !liste.isEmpty())etatDocPossible=liste.get(0);
		//les états ont des positions pour déterminer les différents stade d'évolution d'état d'une ligne
		for (TaEtat taEtat : liste) {
			if(taEtat.getPosition().compareTo(position)>0) {//si postition de l'état > à la dernière position gardée
				//alors on récupère l'état en tant qu'état possible et on garde la position car représente maintenant la plus haute
				etatDocPossible=taEtat;
				position=taEtat.getPosition();
			}
		}

		
		if(trouveNonTermine) {//si au moins une ligne n'est pas terminée
			//si au moins une ligne est abandonnée même partiellement, alors le document est partiellement abandonné mais non terminé
			if(trouveAbandonne)etatDocPossible=taEtatService.documentPartiellementAbandonne(null);
			//s'il n'y aucune ligne abandonnée mais que l'on trouve au moins une ligne transformée, alors il est partiellement transformé
			else if(trouveTransformation && !detachedInstance.getTypeDocument().equals(TaFacture.TYPE_DOC))etatDocPossible=taEtatService.documentPartiellementTransforme(null);
		}
		//si toutes les lignes sont terminées
		else 
			//si on trouve dedans au moins une ligne même partiellement abandonnée, alors le document est terminé partiellement abandonné
			if(trouveAbandonne)etatDocPossible=taEtatService.documentTerminePartiellementAbandonne(null);

		//si trouveNonTermine n'est pas vérifié (cela veut dire peut-être dire que rien n'est transformé
		//et rien abandonné, alors récupère l'état possible le plus haut de la liste si remplie ou l'état enCours )
//		}
		return etatDocPossible;
	}
    
    
		//modifier l'état des lignes et du document en fonction des liens ligne à ligne
    protected void modifEtatLigne(IDocumentTiers detachedInstance) {
    	boolean abandonne=false;
    	for (ILigneDocumentTiers ligne : detachedInstance.getLignesGeneral()) {//parcours toutes les lignes du document
    		// traiter l'état de la ligne
    		if(ligne.getTaArticle()!=null && ligne.getQteLDocument()!=null) {//s'il y a un article et un qté not null
    			BigDecimal totalTransformee=BigDecimal.ZERO;
    			String ligneBefore="";
    			abandonne=false;
    			TaEtat etatLigneOrg = taEtatService.documentEncours(null); //on mets au minimum un état enCours au document
    			TaEtat etatLigneOrgBefore = null;

    			for (TaLigneALigne l : ligne.getTaLigneALignes()) {
    				//parcours toutes les liaisons de la ligne en cours pour récupérer le total des qtés des liaisons
    				if(l.getIdLigneSrc().equals(ligne.getIdLDocument())) {
    					totalTransformee=totalTransformee.add(l.getQteRecue());
    				}
    			}

    			//s'il y a un état old déjà pour la ligne on le met de côté
    			if(ligne.getTaREtatLigneDocument()!=null)etatLigneOrgBefore=ligne.getTaREtatLigneDocument().getTaEtat();
    			else etatLigneOrgBefore=taEtatService.documentEncours(null);//sinon au minumum état EnCours

    			if(etatLigneOrgBefore!=null) {//si old état existe
    				//si ligne a été même partiellement abandonnée alors on garde état abandonné
    				if(etatLigneOrgBefore.getIdentifiant().toLowerCase().contains(TaEtat.ABANDONNE.toLowerCase())) {
//    				if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_ABANDONNE)||etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    					etatLigneOrg=taEtatService.documentAbandonne(null);
//    					etatLigneOrg=etatLigneOrgBefore;
    					abandonne=true;
    				}
    				if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)) {
    					etatLigneOrg=taEtatService.documentEncours(null);// on met enCours et on voit après si document toujours transforme
    				}
    			}
    			if(totalTransformee.compareTo(BigDecimal.ZERO)!=0) {//s'il y a une transformation
    				if(ligne.getQteLDocument().signum()>=0) {
    					//si qté positive ou 0
    					if(totalTransformee.compareTo(ligne.getQteLDocument())>=0)//si transformation > ou égale à la qté de la ligne
    						//si abandonne déjà alors on termine le document puisqu'il est abandonne et en même temps transformé
    						if(abandonne) etatLigneOrg=taEtatService.documentTerminePartiellementAbandonne(null);
    						//sinon on le termine
    						else etatLigneOrg=taEtatService.documentTermine(null);
    					else
    						//sidéjà abandonne  alors on termine le document puisqu'il est abandonne et en même temps transformé
    						if(abandonne) etatLigneOrg=taEtatService.documentTerminePartiellementAbandonne(null);
    						//sinon on le met partiellement transforme
    						else etatLigneOrg=taEtatService.documentPartiellementTransforme(null);
    				}else {
    					//si qté négative
    					if(ligne.getQteLDocument().compareTo(totalTransformee)>=0)
    						//si abandonne déjà alors on termine le document puisqu'il est abandonne et en même temps transformé
    						if(abandonne) etatLigneOrg=taEtatService.documentTerminePartiellementAbandonne(null);
    						//sinon on le termine
    						else etatLigneOrg=taEtatService.documentTermine(null);
    					else
    						//si abandonne déjà alors on termine le document puisqu'il est abandonne et en même temps transformé
    						if(abandonne) etatLigneOrg=taEtatService.documentTerminePartiellementAbandonne(null);
    						//sinon on le met partiellement transforme
    						else etatLigneOrg=taEtatService.documentPartiellementTransforme(null);
    				}
    			}

    			if(etatLigneOrg==null) {//plus rien de transforme
    				if(etatLigneOrgBefore!=null) {//si old état existe
    					//si old état est même partiellement abandonné, alors nouvel état = abandonné
//    					if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_ABANDONNE)||etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    					if(etatLigneOrgBefore.getIdentifiant().toLowerCase().contains(TaEtat.ABANDONNE.toLowerCase())) {
    						etatLigneOrg=taEtatService.documentAbandonne(null);
    					}
    					// si old état est totalment transformé ou partiellement transformé, alors nouvel état devient EnCours puisque plus rien de transformé
    					if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)) {
    						etatLigneOrg=taEtatService.documentEncours(null);
    					}		
    				}
    			}
    			//								else //sinon ,si tout transforme
    			//									if(etatLigneOrg.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)){
    			//										//si tout est transformé et que l'état old n'est pas abandonné même partiellement alors état devient TotalementTransformé
    			//										if(etatLigneOrgBefore!=null && !((etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)))) {
    			//											etatLigneOrg=taEtatService.documentTotalementTransforme(null);
    			//										}
    			//									}
    			if(etatLigneOrg!=null) {
    				ligne.addREtatLigneDoc(etatLigneOrg);
    			}
    		}
    	}
    }
	
    public List<ILigneDocumentTiers>  recupListeLienLigneALigne(IDocumentTiers persistentInstance,List<ILigneDocumentTiers> liste) {
    	/// celle ci permet de faire la même chose mais en plus compare les lignes documents retournées
    	//pour voir si elles ne sont pas déjà présentes dans le cas où un document existe déjà avec des liens
    	// et que certains de ces liens sont supprimés et d'autres ont été créés
    	List<ILigneDocumentTiers>listeLien = recupListeLienLigneALigne(persistentInstance);
    	if(liste!=null) {
    		for (ILigneDocumentTiers l : liste) {
				if(!listeLien.contains(l))listeLien.add(l);
			}
    	}
    	return listeLien;
    }

    
	public List<ILigneDocumentTiers>  recupListeLienLigneALigne(IDocumentTiers persistentInstance) {
		logger.debug("recupListeLienLigneALigne");
		try {
			IDocumentTiers objInitial = persistentInstance;
			//parcours toutes les lignes du document passé en paramètre
			List<ILigneDocumentTiers>listeLien = new LinkedList<ILigneDocumentTiers>();
			for (ILigneDocumentTiers l : objInitial.getLignesGeneral()) {
				for (TaLigneALigne ll : l.getTaLigneALignes()) {
					//et récupère les lignes de document en lien avec si ces lignes sont la source de celui en cours
                    //si type ligne n'est pas du type du document passé en paramètre et que la ligne est source dans la relation, alors on récupère la ligne en question
					if(ll.getTaLAcompte()!=null && ll.getIdLigneSrc().compareTo(ll.getTaLAcompte().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaAcompte.TYPE_DOC))listeLien.add(ll.getTaLAcompte());  
                    if(ll.getTaLApporteur()!=null && ll.getIdLigneSrc().compareTo(ll.getTaLApporteur().getIdLDocument())==0  && !objInitial.getTypeDocument().equals(TaApporteur.TYPE_DOC))listeLien.add(ll.getTaLApporteur());      
                    if(ll.getTaLAvisEcheance()!=null && ll.getIdLigneSrc().compareTo(ll.getTaLAvisEcheance().getIdLDocument())==0  && !objInitial.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))listeLien.add(ll.getTaLAvisEcheance());      
                    if(ll.getTaLAvoir()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLAvoir().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaAvoir.TYPE_DOC))listeLien.add(ll.getTaLAvoir());  
                    if(ll.getTaLBoncde()!=null && ll.getIdLigneSrc().compareTo(ll.getTaLBoncde().getIdLDocument())==0  && !objInitial.getTypeDocument().equals(TaBoncde.TYPE_DOC))listeLien.add(ll.getTaLBoncde());      
                    if(ll.getTaLBoncdeAchat()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLBoncdeAchat().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))listeLien.add(ll.getTaLBoncdeAchat());  
                    if(ll.getTaLBonliv()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLBonliv().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaBonliv.TYPE_DOC))listeLien.add(ll.getTaLBonliv());      
                    if(ll.getTaLBonReception()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLBonReception().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaBonReception.TYPE_DOC))listeLien.add(ll.getTaLBonReception());      
                    if(ll.getTaLDevis()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLDevis().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaDevis.TYPE_DOC))listeLien.add(ll.getTaLDevis());  
                    if(ll.getTaLFacture()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLFacture().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaFacture.TYPE_DOC))listeLien.add(ll.getTaLFacture());    
                    if(ll.getTaLPrelevement()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLPrelevement().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaPrelevement.TYPE_DOC))listeLien.add(ll.getTaLPrelevement());  
                    if(ll.getTaLProforma()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLProforma().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaProforma.TYPE_DOC))listeLien.add(ll.getTaLProforma());  
                    if(ll.getTaLPreparation()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLPreparation().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaPreparation.TYPE_DOC))listeLien.add(ll.getTaLPreparation());
                    if(ll.getTaLTicketDeCaisse()!=null  && ll.getIdLigneSrc().compareTo(ll.getTaLTicketDeCaisse().getIdLDocument())==0 && !objInitial.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))listeLien.add(ll.getTaLTicketDeCaisse());	
				}
			}
			return listeLien;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("recupListeLienLigneALigne failed", re);
			throw re2;
		} 
	}
	
	public  void  mergeEntityLieParLigneALigne(List<ILigneDocumentTiers>listeLien) {
		 mergeEntityLieParLigneALigne(listeLien, true);
	}
	
	public  void  mergeEntityLieParLigneALigne(List<ILigneDocumentTiers>listeLien,boolean rechargerDoc) {
        logger.debug("mergeEntityLieParLigneALigne");
        try {
            IDocumentTiers doc = null;
            int idDoc;
            List<Integer> listeDoc=new LinkedList<>();
            //parcours de la liste de tous les liens qui représentent des lignes de document à merger 
            //car en lien avec un document qui vient d'être modifié ou supprimé
              for (ILigneDocumentTiers ldoc : listeLien) {
            	  doc=ldoc.getTaDocument();
            	  
            	  //suivant le type de document            	  
            	  if(ldoc instanceof TaLAbonnement) {    		  
            		  //si document pas déjà traité donc mergé
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  // si demandé (besoin de raffraichissement dans certain cas!!!), on remonte le document
                        	  if(rechargerDoc)doc=taAbonnementServiceRemote.findDocByLDoc(ldoc);
                        	  // on déclenche une procédure de merge particulière qui ne fait que changer les états des lignes, 
                        	  //du document et qui déclenche dao.merge 
                        	  taAbonnementServiceRemote.mergeEtat((TaAbonnement) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLAcompte) {      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taAcompteServiceRemote.findDocByLDoc(ldoc);
                        	  taAcompteServiceRemote.mergeEtat((TaAcompte) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLApporteur) {
//                    	idDoc=taApporteurServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taApporteurServiceRemote.findDocByLDoc(ldoc);
                        	  taApporteurServiceRemote.mergeEtat((TaApporteur) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLAvisEcheance) {
//                    	idDoc=taAvisEcheanceServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taAvisEcheanceServiceRemote.findDocByLDoc(ldoc);
                        	  taAvisEcheanceServiceRemote.mergeEtat((TaAvisEcheance) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLAvoir) {
//                    	idDoc=taAvoirServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taAvoirServiceRemote.findDocByLDoc(ldoc);
                        	  taAvoirServiceRemote.mergeEtat((TaAvoir) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLBoncde) {
//                    	idDoc=taBoncdeServiceRemote.findDocByLDocDTO(ldoc);      
                           if( !listeDoc.contains(doc.getIdDocument())) {
                        	   if(rechargerDoc)doc=taBoncdeServiceRemote.findDocByLDoc(ldoc);
                             	taBoncdeServiceRemote.mergeEtat((TaBoncde) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLBoncdeAchat) {
//                    	idDoc=taBoncdeAchatServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taBoncdeAchatServiceRemote.findDocByLDoc(ldoc);
                        	  taBoncdeAchatServiceRemote.mergeEtat((TaBoncdeAchat) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLBonliv) {
//                    	idDoc=taBonlivServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taBonlivServiceRemote.findDocByLDoc(ldoc);
                        	  taBonlivServiceRemote.mergeEtat((TaBonliv) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLBonReception) {
//                    	idDoc=taBonReceptionServiceRemote.findDocByLDocDTO(ldoc);     
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taBonReceptionServiceRemote.findDocByLDoc(ldoc);
                        	  taBonReceptionServiceRemote.mergeEtat((TaBonReception) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLDevis) {
//                    	idDoc=taDevisServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taDevisServiceRemote.findDocByLDoc(ldoc);
                        	  taDevisServiceRemote.mergeEtat((TaDevis) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLFacture) {
//                    	idDoc=taFactureServiceRemote.findDocByLDocDTO(ldoc);     
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taFactureServiceRemote.findDocByLDoc(ldoc);
                        	  taFactureServiceRemote.mergeEtat((TaFacture) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLPreparation) {
//                    	idDoc=taPreparationServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taPreparationServiceRemote.findDocByLDoc(ldoc);
                        	  taPreparationServiceRemote.mergeEtat((TaPreparation) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLPrelevement) {
//                    	idDoc=taPrelevementServiceRemote.findDocByLDocDTO(ldoc);      
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taPrelevementServiceRemote.findDocByLDoc(ldoc);
                        	  taPrelevementServiceRemote.mergeEtat((TaPrelevement) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLProforma) {
//                          idDoc=taProformaServiceRemote.findDocByLDocDTO(ldoc);
                          if( !listeDoc.contains(doc.getIdDocument())) {
                        	  if(rechargerDoc)doc=taProformaServiceRemote.findDocByLDoc(ldoc);
                        	  taProformaServiceRemote.mergeEtat((TaProforma) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                    if(ldoc instanceof TaLTicketDeCaisse) {
//                    	idDoc=taTicketDeCaisseServiceRemote.findDocByLDocDTO(ldoc);                          
                    	if( !listeDoc.contains(doc.getIdDocument())) {
                    		if(rechargerDoc)doc=taTicketDeCaisseServiceRemote.findDocByLDoc(ldoc);
                        	  taTicketDeCaisseServiceRemote.mergeEtat((TaTicketDeCaisse) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
              }
        } catch (RuntimeException re) {
              RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
              logger.error("mergeEntityLieParLigneALigne failed", re);
              throw re2;
//        } catch (FinderException e) {
//            logger.error("mergeEntityLieParLigneALigne failed", e);
		} 
  }
	
	
	public  void  mergeEntityLieParLigneALigneDTO(List<IDocumentTiers>listeLien) {
        logger.debug("mergeEntityLieParLigneALigne");
        try {
//            IDocumentTiers doc = null;
            int idDoc;
            List<Integer> listeDoc=new LinkedList<>();
              for (IDocumentTiers doc : listeLien) {
                    if(doc.getTypeDocument().equals(TaAbonnement.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();      
                          if( !listeDoc.contains(idDoc)) {
//                        	  doc=taAbonnementServiceRemote.findById(idDoc);
                        	  taAbonnementServiceRemote.mergeEtat((TaAbonnement) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaAcompte.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                        	  doc=taAcompteServiceRemote.findById(idDoc);
                        	  taAcompteServiceRemote.mergeEtat((TaAcompte) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaApporteur.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                        	  doc=taApporteurServiceRemote.findById(idDoc);
                        	  taApporteurServiceRemote.mergeEtat((TaApporteur) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                        	  doc=taAvisEcheanceServiceRemote.findById(idDoc);
                        	  taAvisEcheanceServiceRemote.mergeEtat((TaAvisEcheance) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaAvoir.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                        	  doc=taAvoirServiceRemote.findById(idDoc);
                        	  taAvoirServiceRemote.mergeEtat((TaAvoir) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaBoncde.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                           if( !listeDoc.contains(idDoc)) {
//                             	doc=taBoncdeServiceRemote.findById(idDoc);
                             	taBoncdeServiceRemote.mergeEtat((TaBoncde) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                          	doc=taBoncdeAchatServiceRemote.findById(idDoc);
                        	  taBoncdeAchatServiceRemote.mergeEtat((TaBoncdeAchat) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaBonliv.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                          	doc=taBonlivServiceRemote.findById(idDoc);
                        	  taBonlivServiceRemote.mergeEtat((TaBonliv) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaBonReception.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();      
                          if( !listeDoc.contains(idDoc)) {
//                          	doc=taBonReceptionServiceRemote.findById(idDoc);
                        	  taBonReceptionServiceRemote.mergeEtat((TaBonReception) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaDevis.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                          	doc=taDevisServiceRemote.findById(idDoc);
                        	  taDevisServiceRemote.mergeEtat((TaDevis) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaFacture.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();      
                          if( !listeDoc.contains(idDoc)) {
//                          	doc=taFactureServiceRemote.findById(idDoc);
                        	  taFactureServiceRemote.mergeEtat((TaFacture) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaPreparation.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                          	doc=taPreparationServiceRemote.findById(idDoc);
                        	  taPreparationServiceRemote.mergeEtat((TaPreparation) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaPrelevement.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();       
                          if( !listeDoc.contains(idDoc)) {
//                          	doc=taPrelevementServiceRemote.findById(idDoc);
                        	  taPrelevementServiceRemote.mergeEtat((TaPrelevement) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaProforma.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument(); 
                          if( !listeDoc.contains(idDoc)) {
//                        	  doc=taProformaServiceRemote.findById(idDoc);
                        	  taProformaServiceRemote.mergeEtat((TaProforma) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
                     if(doc.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC)) {
                    	idDoc=doc.getIdDocument();                           
                    	if( !listeDoc.contains(idDoc)) {
//                        	  doc=taTicketDeCaisseServiceRemote.findById(idDoc);
                        	  taTicketDeCaisseServiceRemote.mergeEtat((TaTicketDeCaisse) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }
              }
        } catch (RuntimeException re) {
              RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
              logger.error("mergeEntityLieParLigneALigne failed", re);
              throw re2;
        } 
//              catch (FinderException e) {
//            logger.error("mergeEntityLieParLigneALigne failed", e);
//		} 
  }

//	public List<TaLigneALigneDTO> recupListeLienLigneALigneDTO(IDocumentTiers persistentInstance) {
//		return taLigneALigneService.recupListeLienLigneALigneDTO(persistentInstance);
//	}
	
}      