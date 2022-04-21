package fr.legrain.paiement.service;

import javax.enterprise.util.AnnotationLiteral;

public class MyQualifierLiteral extends AnnotationLiteral<PaiementLgr> implements PaiementLgr {
	
    private final String value;

    public MyQualifierLiteral(final String value) {
        this.value = value;
    }

    //@Override
    public String value() {
        return this.value;
    }

}