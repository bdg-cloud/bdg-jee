package fr.legrain.push.service;

import javax.enterprise.util.AnnotationLiteral;

public class MyQualifierMessagePushLiteral extends AnnotationLiteral<MessagePushLgr> implements MessagePushLgr {

	private static final long serialVersionUID = -5229672410699850697L;
	
	private final String value;

    public MyQualifierMessagePushLiteral(final String value) {
        this.value = value;
    }

    //@Override
    public String value() {
        return this.value;
    }

}