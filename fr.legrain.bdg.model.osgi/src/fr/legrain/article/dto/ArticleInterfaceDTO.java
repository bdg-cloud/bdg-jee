package fr.legrain.article.dto;

import java.math.BigDecimal;
import java.util.List;

public class ArticleInterfaceDTO {
	
			private String codeArticle;
			private String libelleArticle;
			private Integer idArticle;
			
			private Integer idUnite1;
			private String codeUnite1;
			private String liblUnite1;
			private Float qte1;
			private Integer idUnite2;
			private String codeUnite2;
			private String liblUnite2;
			private BigDecimal rapport;
			private Integer nbDecimal;
			private Integer sens;
			
			public ArticleInterfaceDTO() {
				super();
			}
			public ArticleInterfaceDTO(String codeArticle, String libelleArticle) {
				super();
				this.codeArticle = codeArticle;
				this.libelleArticle = libelleArticle;
			}
			public String getCodeArticle() {
				return codeArticle;
			}
			public void setCodeArticle(String codeArticle) {
				this.codeArticle = codeArticle;
			}
			public String getLibelleArticle() {
				return libelleArticle;
			}
			public void setLibelleArticle(String libelleArticle) {
				this.libelleArticle = libelleArticle;
			}
			public Integer getIdArticle() {
				return idArticle;
			}
			public void setIdArticle(Integer idArticle) {
				this.idArticle = idArticle;
			}
			
			public Integer getIdUnite1() {
				return idUnite1;
			}
			public void setIdUnite1(Integer idUnite1) {
				this.idUnite1 = idUnite1;
			}
			public String getCodeUnite1() {
				return codeUnite1;
			}
			public void setCodeUnite1(String codeUnite1) {
				this.codeUnite1 = codeUnite1;
			}
			public String getLiblUnite1() {
				return liblUnite1;
			}
			public void setLiblUnite1(String liblUnite1) {
				this.liblUnite1 = liblUnite1;
			}
			public Float getQte1() {
				return qte1;
			}
			public void setQte1(Float qte1) {
				this.qte1 = qte1;
			}
			public Integer getIdUnite2() {
				return idUnite2;
			}
			public void setIdUnite2(Integer idUnite2) {
				this.idUnite2 = idUnite2;
			}
			public String getCodeUnite2() {
				return codeUnite2;
			}
			public void setCodeUnite2(String codeUnite2) {
				this.codeUnite2 = codeUnite2;
			}
			public String getLiblUnite2() {
				return liblUnite2;
			}
			public void setLiblUnite2(String liblUnite2) {
				this.liblUnite2 = liblUnite2;
			}
			public BigDecimal getRapport() {
				return rapport;
			}
			public void setRapport(BigDecimal rapport) {
				this.rapport = rapport;
			}
			public Integer getNbDecimal() {
				return nbDecimal;
			}
			public void setNbDecimal(Integer nbDecimal) {
				this.nbDecimal = nbDecimal;
			}
			public Integer getSens() {
				return sens;
			}
			public void setSens(Integer sens) {
				this.sens = sens;
			}
		
}
