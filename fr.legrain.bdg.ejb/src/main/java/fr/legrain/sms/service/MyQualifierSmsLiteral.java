package fr.legrain.sms.service;

import javax.enterprise.util.AnnotationLiteral;

public class MyQualifierSmsLiteral extends AnnotationLiteral<SmsLgr> implements SmsLgr {

	private static final long serialVersionUID = -5229672410699850697L;
	
	private final String value;

    public MyQualifierSmsLiteral(final String value) {
        this.value = value;
    }

    //@Override
    public String value() {
        return this.value;
    }

}