package fr.legrain.general.service;

import java.util.HashMap;

import java.util.Map;

public class ThreadLocalContextHolder {

	//http://www.adam-bien.com/roller/abien/entry/how_to_pass_context_with
	
    private static final ThreadLocal<Map<String,Object>> THREAD_WITH_CONTEXT = new ThreadLocal<Map<String,Object>>();

    private ThreadLocalContextHolder() {}

    public static void put(String key, Object payload) {

        if(THREAD_WITH_CONTEXT.get() == null){
            THREAD_WITH_CONTEXT.set(new HashMap<String, Object>());
        }
        THREAD_WITH_CONTEXT.get().put(key, payload);
    }

    public static Object get(String key) {
    	if(THREAD_WITH_CONTEXT.get() != null){
    		return THREAD_WITH_CONTEXT.get().get(key);
    	} else {
    		return null;
    	}
    }

    public static void cleanupThread(){
        THREAD_WITH_CONTEXT.remove();
    }
} 

