package fr.legrain.bdg.tiers.service.remote;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaTiersServiceRemote extends IGenericCRUDServiceRemote<TaTiers,TaTiersDTO>,
														IAbstractLgrDAOServer<TaTiers>,IAbstractLgrDAOServerDTO<TaTiersDTO>{
	public static final String validationContext = "TIERS";
	public static final String validationContextCommercial = "R_COMMERCIAL";
	public static final String validationContextFournisseur = "FOURNISSEUR_ARTICLE";
	static public final int C_ID_IDENTITE_ENTREPRISE_INT = -1;
	static public final String C_ID_IDENTITE_ENTREPRISE_STR = "-1";
	static public final String C_CODE_IDENTITE_ENTREPRISE_STR = "Z-6PO";
	static public final String C_COMPTE_IDENTITE_ENTREPRISE_STR = "421";
	
	public String getDefaultJPQLQueryIdentiteEntrepise();
	public List<TaTiers> rechercheParType(String codeType);
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	public String getTiersActif();
	public void setPreferences(String preferences);
	public List<Object[]> selectTiersSurTypeTiersLight();
	
	public List<TaTiersDTO> findByCodeLight(String code);
	public List<TaTiersDTO> findAllLight();
	public List<TaTiersDTO> findAllLightAdresseComplete();
	
	public List<TaTiersDTO> findLightTTarif() ;
	
	public int countAllTiersActifTaTiersDTO();
	
	public String generePDF(int idTiers);
	public File exportToCSV(List<TaTiersDTO> l);
	
	public List<TaTiers> selectTiersTypeDoc(String partieRequeteTiers,IDocumentTiers doc,String typeOrigine,String typeDest,Date debut,Date fin);
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeTiersDTO(Date dateDebut, Date dateFin, String codeTiers);
	
	public List<TaTiersDTO> findLightTTarifFamille(String codeTTarif, String codeFamille);
	
	public List<TaTiers> findByEmail(String adresseEmail);
	public List<TaTiers> findByEmailParDefaut(String adresseEmail);
	public TaTiers findByEmailAndCodeTiers(String adresseEmail, String codeTiers);
	public TaTiers findByEmailParDefautAndCodeTiers(String adresseEmail, String codeTiers);
	
	public List<TaTiers> rechercheTiersPourCreationEspaceClientEnSerie();
	public List<TaTiersDTO> findListeTiersBoutique(String codeTTiers);
	
//	public void initAdresseTiers(TaTiers taTiers,Object value,boolean adresseEstRempli);
//	public void initComplTiers(TaTiers taTiers,Object value);
//	public void initCodeTTiers(TaTiers taTiers,Object value);
//	public void initEntrepriseTiers(TaTiers taTiers,Object value);
//	public void initTelephoneTiers(TaTiers taTiers,Object value);
//	public void initCommentaireTiers(TaTiers taTiers,Object value);
//	public void initEmailTiers(TaTiers taTiers,Object value);
//	public void initWebTiers(TaTiers taTiers,Object value);
	
}
