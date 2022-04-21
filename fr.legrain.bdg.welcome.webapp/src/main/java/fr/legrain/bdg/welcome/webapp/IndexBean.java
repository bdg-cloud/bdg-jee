package fr.legrain.bdg.welcome.webapp;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;

@ManagedBean
@ViewScoped
public class IndexBean implements Serializable {
	
	private static final long serialVersionUID = 6549510033046344030L;
	
	private String ndd;
	private BdgProperties bdgProps;
	
	@PostConstruct
	public void init() {
		bdgProps = new BdgProperties();
		String prefixe = bdgProps.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE)!=null?bdgProps.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE):"";
		ndd = prefixe+bdgProps.getProperty(BdgProperties.KEY_NOM_DOMAINE);
	}

	public String getNdd() {
		return ndd;
	}

	public void setNdd(String ndd) {
		this.ndd = ndd;
	}
	
	
}
