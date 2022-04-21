package fr.legrain.paiement.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface PaiementLgr {
	public static final String TYPE_STRIPE = "TYPE_STRIPE";
	public static final String TYPE_STRIPE_CONNECT = "TYPE_STRIPE_CONNECT";
	public static final String TYPE_PAYPAL = "TYPE_PAYPAL";
	
	String value();
}

