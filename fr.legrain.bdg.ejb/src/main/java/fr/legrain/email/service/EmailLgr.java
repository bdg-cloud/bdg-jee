package fr.legrain.email.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface EmailLgr {
	
	public static final String TYPE_SMTP_DOSSIER = "TYPE_SMTP_DOSSIER";
	public static final String TYPE_SMTP_PROGRAMME_BDG = "TYPE_SMTP_PROGRAMME_BDG";
	
	//Services externes
	public static final String TYPE_MAILJET_DOSSIER = "TYPE_MAILJET_DOSSIER";
	public static final String TYPE_MAILJET_PAR_DEFAUT_BDG = "TYPE_MAILJET_PAR_DEFAUT_BDG";
	public static final String TYPE_MAILJET_PROGRAMME_BDG = "TYPE_MAILJET_PROGRAMME_BDG";
	
	public static final String TYPE_MAIL_CHIMP = "TYPE_MAIL_CHIMP";
	
	String value();
}

