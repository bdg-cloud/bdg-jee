package fr.legrain.tiers.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;
//import javax.ejb.Remote;

//@Remote
public interface ITiersDAO extends IGenericDAO<TaTiers> {
	public String getDefaultJPQLQueryIdentiteEntrepise();
	public List<TaTiers> rechercheParType(String codeType);
	public String getTiersActif();
	public void setPreferences(String preferences);
	public List<Object[]> selectTiersSurTypeTiersLight();
	public List<TaTiersDTO> findByCodeLight(String code);
	public List<TaTiersDTO> findAllLight();
	public List<TaTiersDTO> findAllLightAdresseComplete();
	
	public int countAllTiersActif();
	
	public List<TaTiers> selectTiersTypeDoc(String partieRequeteTiers,IDocumentTiers doc,String typeOrigine,String typeDest,Date debut,Date fin);
	public Boolean exist(String code);
	public List<TaTiersDTO> findLightTTarif() ;
	public List<TaTiersDTO> findLightTTarifFamille(String codeTTarif, String codeFamille);
	
	public List<TaTiers> findByEmail(String adresseEmail);
	public List<TaTiers> findByEmailParDefaut(String adresseEmail);
	public TaTiers findByEmailAndCodeTiers(String adresseEmail, String codeTiers);
	public TaTiers findByEmailParDefautAndCodeTiers(String adresseEmail, String codeTiers);
	
	public List<TaTiers> rechercheTiersPourCreationEspaceClientEnSerie();
	public List<TaTiersDTO> findListeTiersBoutique(String codeTTiers);
}
