package fr.legrain.document.controller;

import java.util.ArrayList;
import java.util.HashSet;

import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTDocServiceRemote;
import fr.legrain.document.model.TaDocumentEditable;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.documents.dao.TaTDocDAO;
import fr.legrain.gestCom.Appli.IlgrMapper;
import fr.legrain.gestCom.Module_Document.SWTDocumentEditable;
import fr.legrain.gestCom.Module_Document.SWTTDoc;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;

public class MapperDocumentEditable implements IlgrMapper<SWTDocumentEditable, TaDocumentEditable> {
	
	private LgrDozerMapper<SWTDocumentEditable,TaDocumentEditable> mapperUIToModel  = new LgrDozerMapper<SWTDocumentEditable,TaDocumentEditable>();
	private LgrDozerMapper<TaDocumentEditable,SWTDocumentEditable> mapperModelToUI  = new LgrDozerMapper<TaDocumentEditable,SWTDocumentEditable>();
	
	private LgrDozerMapper<SWTTDoc,TaTDoc> mapperUIToModelTdoc  = new LgrDozerMapper<SWTTDoc,TaTDoc>();
	private LgrDozerMapper<TaTDoc,SWTTDoc> mapperModelToUITdoc  = new LgrDozerMapper<TaTDoc,SWTTDoc>();
	
	private EntityManager em = null;

	@Override
	public TaDocumentEditable dtoToEntity(SWTDocumentEditable e) {
		TaDocumentEditable doc = new TaDocumentEditable();
		mapperUIToModel.map(e, doc);
		
		doc.setTaTDoc(new HashSet<TaTDoc>());
		ITaTDocServiceRemote taTDocDAO = null;
		TaTDoc taTDoc = null;
		if(em!=null) {
			try {
				taTDocDAO = new EJBLookup<ITaTDocServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_DOC_SERVICE, ITaTDocServiceRemote.class.getName());
	//			taTDocDAO = new TaTDocDAO(em);
				for (SWTTDoc tdoc : e.getTaTDoc()) {
					taTDoc = taTDocDAO.findById(tdoc.getIdTDoc());
					doc.getTaTDoc().add(taTDoc);
				}
			} catch (NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FinderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return doc;
	}
	
	public TaDocumentEditable dtoToEntity(SWTDocumentEditable e, TaDocumentEditable doc) {
		//TaDocumentEditable doc = new TaDocumentEditable();
		mapperUIToModel.map(e, doc);
		
		doc.setTaTDoc(new HashSet<TaTDoc>());
		ITaTDocServiceRemote taTDocDAO = null;
		TaTDoc taTDoc = null;
		if(em!=null && e.getTaTDoc()!=null) {
			try {
				taTDocDAO = new EJBLookup<ITaTDocServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_DOC_SERVICE, ITaTDocServiceRemote.class.getName());
				//taTDocDAO = new TaTDocDAO(em);
				for (SWTTDoc tdoc : e.getTaTDoc()) {
					taTDoc = taTDocDAO.findById(tdoc.getIdTDoc());
					doc.getTaTDoc().add(taTDoc);
				}
			} catch (NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FinderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
//		else { //TODO A SUPPRIMER, AFFECTATION D'UN TYPE PAR DEFAUT
//			taTDocDAO = new TaTDocDAO(em);
//			taTDoc = taTDocDAO.findById(1);
//			doc.getTaTDoc().add(taTDoc);
//		}
		return doc;
	}

	@Override
	public SWTDocumentEditable entityToDto(TaDocumentEditable e) {
		SWTDocumentEditable doc = new SWTDocumentEditable();
		mapperModelToUI.map(e, doc);
		
		doc.setTaTDoc(new ArrayList<SWTTDoc>());
		SWTTDoc swtTDoc = null;
		for (TaTDoc tdoc : e.getTaTDoc()) {
			swtTDoc = new SWTTDoc();
			mapperModelToUITdoc.map(tdoc, swtTDoc);
			doc.getTaTDoc().add(swtTDoc);
		}
		return doc;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
