package fr.legrain.abonnement.stripe.model;

import java.util.Date;

public class TaDevise {
	
	/*
	CREATE TABLE ta_devise (
	  id_devise serial NOT NULL,
	  codeIso dlib255,
	  symbole dlib100,
	  code dlib255,
	  nom dlib255,
	  
	  qui_cree dlib50,
	  quand_cree date_heure_lgr,
	  qui_modif dlib50,
	  quand_modif date_heure_lgr,
	  version num_version,
	  ip_acces dlib50,
	  version_obj did_version_obj,
	  CONSTRAINT ta_subscription_pkey PRIMARY KEY (id_subscription),
	  CONSTRAINT fk_id_subscription_1 FOREIGN KEY (id_article)
	      REFERENCES ta_article (id_article) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
	)
	*/
	
	private int IdDevise;
	private String codeIso; //eur, usd
	private String symbole;
	private String code;
	private String nom;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
}
