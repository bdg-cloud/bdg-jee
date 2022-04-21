package fr.legrain.bdg.webapp.multi_tarifs;

import javax.ejb.EJB;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.tiers.dto.TaTTarifDTO;

public class TaLMultiTarifDTOJSF  implements java.io.Serializable{




	/**
	 * 
	 */
	private static final long serialVersionUID = -2550153276877199104L;


	@EJB ITaArticleServiceRemote taArticleService;
	@EJB ITaTTarifServiceRemote taTTarifService;
	

	private TaPrixDTO dto=new TaPrixDTO();
	private TaArticle article;
	private TaArticleDTO articleDTO;
	private TaTTarifDTO typeTarifDTO;
	private boolean modifie=false;
	private boolean suppression=false;
	private boolean supprimable=true;

	public boolean isModifie() {
		return modifie;
	}

	public void setModifie(boolean modifie) {
		this.modifie = modifie;
	}

	
	public void updateDTO() {
		if(dto!=null) {
			
		}
	}



	public TaPrixDTO getDto() {
		return dto;
	}


	public void setDto(TaPrixDTO dto) {
		this.dto = dto;
	}



	public TaArticle getArticle() {
		return article;
	}



	public void setArticle(TaArticle article) {
		this.article = article;
	}



	public TaTTarifDTO getTypeTarifDTO() {
		return typeTarifDTO;
	}



	public void setTypeTarifDTO(TaTTarifDTO typeTarifDTO) {
		this.typeTarifDTO = typeTarifDTO;
	}


	public TaLMultiTarifDTOJSF copy() {
		TaLMultiTarifDTOJSF copy =new TaLMultiTarifDTOJSF();
		copy.setArticle(this.article);
		copy.getDto().setCodeArticle(this.getDto().getCodeArticle());
		copy.getDto().setAccepte(this.getDto().getAccepte());
		copy.getDto().setCodeTTarif(this.getDto().getCodeTTarif());
		copy.getDto().setCoef(this.getDto().getCoef());
		copy.getDto().setId(this.getDto().getId());
		copy.getDto().setIdArticle(this.getDto().getIdArticle());
		copy.getDto().setIdTTarif(this.getDto().getIdTTarif());
		copy.getDto().setLibellecArticle(this.getDto().getLibellecArticle());
		copy.getDto().setPrixPrix(this.getDto().getPrixPrix());
		copy.getDto().setPrixPrixCalc(this.getDto().getPrixPrixCalc());
		copy.getDto().setPrixttcPrix(this.getDto().getPrixttcPrix());
		copy.getDto().setPrixttcPrixCalc(this.getDto().getPrixttcPrixCalc());
		copy.getDto().setTauxTva(this.getDto().getTauxTva());
		copy.setModifie(this.modifie);
		copy.setTypeTarifDTO(this.typeTarifDTO);
		copy.setArticleDTO(this.articleDTO);
		return copy;
	}

	public TaArticleDTO getArticleDTO() {
		return articleDTO;
	}

	public void setArticleDTO(TaArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}

	public boolean isSuppression() {
		return suppression;
	}

	public void setSuppression(boolean suppression) {
		this.suppression = suppression;
	}

	public boolean isSupprimable() {
		return supprimable;
	}

	public void setSupprimable(boolean supprimable) {
		this.supprimable = supprimable;
	}



	
}
