package fr.legrain.bdg.webapp.dev;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dump {
	
	public String nom;
	public String chemin;
	public Date date;

	public Date calculeDate() {
		//bdg_schema_demo1_2016-02-03_15-53-46.backup
		Date date = null;
		if(nom!=null) {
			String datetime_s = nom.substring(nom.length()-26);
			datetime_s = datetime_s.substring(0,datetime_s.length()-7);
			
			String date_s = datetime_s.split("_")[0];
			String time_s = datetime_s.split("_")[1];
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
			try {
				date = formatter.parse(datetime_s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.date = date;
			
		}
		return date;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
		calculeDate();
	}
	public String getChemin() {
		return chemin;
	}
	public void setChemin(String chemin) {
		this.chemin = chemin;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	

}