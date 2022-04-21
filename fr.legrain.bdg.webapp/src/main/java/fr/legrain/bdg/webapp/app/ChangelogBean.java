package fr.legrain.bdg.webapp.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.google.gson.stream.JsonReader;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bugzilla.rest.model.Bug;
import fr.legrain.bugzilla.rest.util.BugzillaUtil;

@Named
@ViewScoped 
public class ChangelogBean implements Serializable {
	
	private String changelog = "";
	private String changelogHTML = "";
	private Map<String, String> timestamps;
	
	private BugzillaUtil bz;
	private List<Bug> listeBugs;
	
	private String dateDernierBuildDevLocal = "dateDernierBuildDevLocal";
	private String dateDernierBuildDevInternet = "dateDernierBuildDevInternet";
	private String dateDernierBuildPProd = "dateDernierBuildPProd";
	private String dateDernierBuildProd = "dateDernierBuildProd";
	private String dateBuild = "dateBuild";
	
	private String dateDebutStr;
	private String dateFinStr;
	
	private BdgProperties bdgProperties;
	
	@PostConstruct
	public void init() {
		bdgProperties = new BdgProperties();
//		bz = new BugzillaUtil(BugsBean.bugzillaURL,BugsBean.apiKey);
		bz = new BugzillaUtil(BugsBean.bugzillaURL,"zpV28PVz133uhN8aLHaDKOtRThw3nxc8UMExC6JW");
		readChangelog();
		readTimestamps();
		requeteBugzilla();
	}

	public void readChangelog() {
		try {
			String cheminChangelog = null;
			if(bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE).equals("bdg.cloud")) {
				cheminChangelog = "META-INF/resources/changelog/changelog_prod.txt";
			} else if(bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE).equals("pprodbdg.work")) {
				cheminChangelog = "META-INF/resources/changelog/changelog_pprod.txt";
			} else if(bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE).equals("devbdg.work")) {
				cheminChangelog = "META-INF/resources/changelog/changelog_dev_internet.txt";
			} else { //dev local
				cheminChangelog = "META-INF/resources/changelog/changelog.txt";
			}
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(cheminChangelog);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				changelog += line;
			}
			
			changelogHTML = changelog;
			
			br.close();
		} catch(Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void readTimestamps() {
		try {
		String cheminChangelog = null;
		
		cheminChangelog = "META-INF/resources/changelog/timestamps.txt";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(cheminChangelog);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//		for (String line = br.readLine(); line != null; line = br.readLine()) {
//			changelog += line;
//		}
		timestamps = new HashMap<>();
		
		JsonReader jsr = new JsonReader(br);
		jsr.setLenient(true);
		int i = 5; //5 element dans le fichier
		String k = null;
		String v = null;
		while(jsr.hasNext() && i>0) {
			jsr.beginObject();
			k = jsr.nextName();
			v = jsr.nextString();
			System.out.println("ChangelogBean.readTimestamps() " +  k);
			System.out.println("ChangelogBean.readTimestamps() " + v );
			jsr.endObject();
			i--;
			timestamps.put(k, v);
		}
		br.close();
		} catch(Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void requeteBugzilla() {
		
		String time = null;
		if(bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE).equals("bdg.cloud")) {
			time = dateDernierBuildProd;
		} else if(bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE).equals("pprodbdg.work")) {
			time = dateDernierBuildPProd;
		} else if(bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE).equals("devbdg.work")) {
			time = dateDernierBuildDevInternet;
		} else { //dev local
			time = dateDernierBuildDevLocal;
		}
		//time = dateDernierBuildProd;
		
		dateDebutStr = timestamps.get(time);
		dateFinStr = timestamps.get(dateBuild);
		
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		LocalDateTime dateTimeDebut = LocalDateTime.from(f.parse(timestamps.get(time)));
		LocalDateTime dateTimeFinBuild = LocalDateTime.from(f.parse(timestamps.get(dateBuild)));
		
		listeBugs = bz.findBugsParam("chfieldfrom="+dateDebutStr.split("_")[0]+"&chfieldto="+dateFinStr.split("_")[0]+"&include_fields=id,summary,last_change_time,status,severity,priority,resolution&resolution=FIXED");
//		listeBugs = bz.findBugsParam("chfieldfrom=2017-05-01&chfieldto=2017-06-16&include_fields=id,summary,last_change_time,status,severity,priority,resolution&resolution=FIXED");
		
		if(listeBugs!=null) {
			for (Bug bug : listeBugs) {
				changelog += bug.getSummary();
			}
		}
	}

	public String getChangelog() {
		return changelog;
	}

	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}

	public String getChangelogHTML() {
		return changelogHTML;
	}

	public void setChangelogHTML(String changelogHTML) {
		this.changelogHTML = changelogHTML;
	}

	public List<Bug> getListeBugs() {
		return listeBugs;
	}

	public void setListeBugs(List<Bug> listeBugs) {
		this.listeBugs = listeBugs;
	}

	public String getDateDebutStr() {
		return dateDebutStr;
	}

	public void setDateDebutStr(String dateDebutStr) {
		this.dateDebutStr = dateDebutStr;
	}

	public String getDateFinStr() {
		return dateFinStr;
	}

	public void setDateFinStr(String datefinStr) {
		this.dateFinStr = datefinStr;
	}
	
}
