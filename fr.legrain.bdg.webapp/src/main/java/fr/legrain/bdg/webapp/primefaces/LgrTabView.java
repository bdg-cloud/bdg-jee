package fr.legrain.bdg.webapp.primefaces;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.primefaces.component.tabview.TabView;

@ResourceDependencies({
	@ResourceDependency(library = "lgr", name = "tabview.js") })
public class LgrTabView extends TabView { 
    
}