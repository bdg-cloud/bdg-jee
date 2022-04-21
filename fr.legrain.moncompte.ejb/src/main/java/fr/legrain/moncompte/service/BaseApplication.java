package fr.legrain.moncompte.service;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class BaseApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
    	
    	Set<Class<?>> resources = new java.util.HashSet<>();
    	
//    	resources.add(SecurityInterceptor.class);
    	
        return resources;
        
    }
}