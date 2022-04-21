package fr.legrain.bdg.moncompte.service.remote;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.moncompte.model.TaCategoriePro;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.moncompte.model.TaCgv;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaLimitation;
import fr.legrain.moncompte.model.TaPanier;
import fr.legrain.moncompte.model.TaPartenaire;
import fr.legrain.moncompte.model.TaPrixParUtilisateur;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.TaTNiveau;
import fr.legrain.moncompte.model.TaTypePaiement;
import fr.legrain.moncompte.model.TaTypePartenaire;


@Remote
public interface IMonCompteServiceFacadeRemote {
	public static final String validationContext = "MON_COMPTE";
	
	//liste des produits/modules
	public List<TaProduit> selectAllProduit(String type);
	public List<TaTNiveau> selectAllTNiveau();
	public List<TaProduit> selectAllProduitCategoriePro(int idCategoriePro);
	
	//création/modification clients
	public TaClient mergeClient(TaClient c);
	
	public boolean utilisateurExiste(String adresseEmail);
	public boolean dossierExiste(String codeDossier);
	
	public TaDossier findDossier(String codeDossier);
	public List<TaDossier> findListeDossierClient(int idClient);
	public List<TaDossier> findListeDossierProduit(String idProduit);
	public TaClient findClientDossier(String codeDossier);
	
	//affectation module/autorisation à un client
	
	public TaDossier mergeDossier(TaDossier d);
	
	public TaPanier mergePanier(TaPanier p);
	public List<TaPanier> findPanierDossier(String codeDossier);
	public List<TaPanier> findPanierClient(String codeDossier);
	public TaPanier findPanier(int idPanier);
	
	public List<TaPrixParUtilisateur> findPrixParUtilisateur();
	
	//MAJ des autorisations ?????
	
	//
	public String chargeDernierSetup(String codeTiers, String codeProduit, String versionClient) throws RemoteException;
	
	public TaLimitation findLimitationFacture(String typeLimitation);
	
	public TaTypePaiement findTypePaiement(String codeTypePaiement);
	
	public TaDossier majAutorisation(TaPanier panier);
	
	public TaCgv findCgv(int idCgv);
    public TaCgv findCgvCourant();
    
    public List<TaCategoriePro> selectAllCategoriePro();
    
    public TaCgPartenaire findCgPartenaire(int idCgv);
    public TaCgPartenaire findCgPartenaireCourant();
    public TaTypePartenaire findTypePartenaire(String codeTypePartenaire);
    public TaPartenaire findPartenaire(String codeDossier);
    
    public TaPartenaire findPartenaireByCode(String codePartenaire);
    
    public List<TaCommission> findCommissionPartenaire(String codePartenaire, Date debut, Date fin);
    public TaCommission genereCommission(TaPanier d);
    public Date findDerniereCreationDossierPayantPartenaire(String codePartenaire);
    public BigDecimal findMontantVentePartenaire(String codePartenaire, Date debut, Date fin);
	public BigDecimal findTauxCommissionPartenaireAvecDecote(String codePartenaire);
	public BigDecimal findTauxCommissionPartenaireSansDecote(String codePartenaire);
	public BigDecimal findDecoteTauxCommissionPartenaire(String codePartenaire);
	
}
