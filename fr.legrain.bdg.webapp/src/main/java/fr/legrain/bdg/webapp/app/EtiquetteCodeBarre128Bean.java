package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.webapp.codebarre.CodeBarre128PrintParam;

@Named("etiquetteCodeBarre128Bean")
@SessionScoped 
public class EtiquetteCodeBarre128Bean implements Serializable {
	
	private CodeBarre128PrintParam codeBarre128PrintParam;

	private Map<String, TaArticle> listeCodebarreArticle;
	private Map<String, TaLot> listeCodebarreLot;
	
	
	
	private String urlBarcode4J;
	
	//org.krysalis.barcode4j.BarcodeDimension
	private double width;
    private double height = 15.0; //mm
    
    private double widthPlusQuiet;
    private double heightPlusQuiet;
    private double xOffset;
    private double yOffset;
    
    //org.krysalis.barcode4j.impl.code128.Code128Bean
    protected static final double DEFAULT_MODULE_WIDTH = 0.21f; //mm
    
    public void init(CodeBarre128PrintParam codeBarre128PrintParam) {
    	videListe();
    	this.codeBarre128PrintParam = codeBarre128PrintParam;
    	listeCodebarreArticle = codeBarre128PrintParam.getListeCodebarreArticle();
    	listeCodebarreLot = codeBarre128PrintParam.getListeCodebarreLot();
	}
	
	public void videListe() {
		listeCodebarreArticle = new HashMap<>();
		listeCodebarreLot = new HashMap<>();
	}
	
	public String genereUrlCodeBarre(String contexteUrl, String msg) {
//		/gensvg?type=ean-13&amp;fmt=png&amp;msg=' += p
		//return contexteUrl+"/gensvg?type=ean-13&amp;fmt=png&amp;msg="+msg;
		
		
		
		try {
			String url = contexteUrl+"/gensvg?type=code128&msg="+URLEncoder.encode(msg,"UTF-8");
			System.out.println(url);
			return url;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
	
	
	
	public void calcDimensions(String msg) {
       // Code128LogicImpl impl = createLogicImpl();
        int msgLen = 0;

        //msgLen = impl.createEncodedMessage(msg).length + 1;
        msgLen = msg.length() + 1;

        final double width = ((msgLen * 11) + 13) * DEFAULT_MODULE_WIDTH;
        final double qz = 10 * DEFAULT_MODULE_WIDTH;
        final double vqz = 0;

//        return new BarcodeDimension(width, getHeight(),
//                width + (2 * qz), getHeight() + (2 * vqz),
//                qz, vqz);
        this.width = width;
        //this.height = getHeight();
        this.widthPlusQuiet = width + (2 * qz);
        this.heightPlusQuiet = height + (2 * vqz);
        this.xOffset = qz;
        this.yOffset = vqz;
        
        final double unPixelEnMm = 3.7795275591;
        this.width = this.width*unPixelEnMm;
        this.height = 15.0*unPixelEnMm;
    }

	public Map<String, TaArticle> getListeCodebarreArticle() {
		return listeCodebarreArticle;
	}



	public void setListeCodebarreArticle(Map<String, TaArticle> listeCodebarreArticle) {
		this.listeCodebarreArticle = listeCodebarreArticle;
		if(listeCodebarreArticle!=null) {
			for (String cb : listeCodebarreArticle.keySet()) {
				calcDimensions(cb);
			}
		}
	}



	public Map<String, TaLot> getListeCodebarreLot() {
		return listeCodebarreLot;
	}



	public void setListeCodebarreLot(Map<String, TaLot> listeCodebarreLot) {
		this.listeCodebarreLot = listeCodebarreLot;
		if(listeCodebarreLot!=null) {
			for (String cb : listeCodebarreLot.keySet()) {
				calcDimensions(cb);
			}
		}
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public String getUrlBarcode4J() {
		return urlBarcode4J;
	}

	public void setUrlBarcode4J(String urlBarcode4J) {
		this.urlBarcode4J = urlBarcode4J;
	}

	public CodeBarre128PrintParam getCodeBarre128PrintParam() {
		return codeBarre128PrintParam;
	}

	public void setCodeBarre128PrintParam(CodeBarre128PrintParam codeBarre128PrintParam) {
		this.codeBarre128PrintParam = codeBarre128PrintParam;
	}

	
}
