package fr.legrain.email.service;

import javax.enterprise.util.AnnotationLiteral;

public class MyQualifierEmailLiteral extends AnnotationLiteral<EmailLgr> implements EmailLgr {
	
	private static final long serialVersionUID = -5576029537856395306L;
	
	private final String value;

    public MyQualifierEmailLiteral(final String value) {
        this.value = value;
    }

    //@Override
    public String value() {
        return this.value;
    }

}