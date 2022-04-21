package fr.legrain.bdg.welcome.webapp.converter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@ConversationScoped
@FacesConverter(value = "entityConverter")
public class EntityConverter implements javax.faces.convert.Converter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2037682209522300155L;
	
	
//	final private Map<String, Object> converterMap = new HashMap<String, Object>();
//    final private Map<Object, String> reverseConverterMap = new HashMap<Object, String>();
//
//    @Inject
//    private transient Conversation conversation;
//
//
//    private int incrementor = 1;
//
//    @Override
//    public Object getAsObject(FacesContext context, UIComponent component, String value) {
//        if (this.conversation.isTransient()) {
//           // log.warn("Conversion attempted without a long running conversation");
//        }
//
//        return this.converterMap.get(value);
//    }
//
//    @Override
//    public String getAsString(FacesContext context, UIComponent component, Object value) {
//        if (this.conversation.isTransient()) {
//            //log.warn("Conversion attempted without a long running conversation");
//        }
//
//        if (this.reverseConverterMap.containsKey(value)) {
//            return this.reverseConverterMap.get(value);
//        } else {
//            final String incrementorStringValue = String.valueOf(this.incrementor++);
//            this.converterMap.put(incrementorStringValue, value);
//            this.reverseConverterMap.put(value, incrementorStringValue);
//            return incrementorStringValue;
//        }
//    }
    
    
    //private static Map<Object, String> entities = new WeakHashMap<Object, String>();
	//WeakHashMap pose des problèems dans certains cas (combo/autocomplete), les clé sont libérés "trop vite",
	//mais les HashMap classique pourraient poser des problème de mémoire, à surveiller ...
	private static Map<Object, String> entities = new HashMap<Object, String>(); 

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        synchronized (entities) {
            if (!entities.containsKey(entity)) {
                String uuid = UUID.randomUUID().toString();
                entities.put(entity, uuid);
                return uuid;
            } else {
                return entities.get(entity);
            }
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String uuid) {
        for (Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
