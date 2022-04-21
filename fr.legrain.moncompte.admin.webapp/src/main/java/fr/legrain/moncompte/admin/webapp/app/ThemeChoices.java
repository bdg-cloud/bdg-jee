package fr.legrain.moncompte.admin.webapp.app;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class ThemeChoices implements Serializable {
	public static final String[] POSSIBLE_THEMES =
		{ "metroui",
		"legrain", 
		"aristo",
		"afterdark", 
		"afternoon",
		"afterwork", 
		"black-tie", 
		"blitzer",
		"bluesky", 
		"casablanca",
		"cruze", 
		"cupertino", 
		"dark-hive", 
		"dot-luv",
		"eggplant", 
		"excite-bike", 
		"flick", 
		"glass-x",
		"home", 
		"hot-sneaks",
		"humanity", 
		"le-frog",
		"midnight", 
		"mint-choc", 
		"overcast", 
		"pepper-grinder",
		"redmond", 
		"rocket",
		"sam", 
		"smoothness",
		"south-street", 
		"start",
		"sunny", 
		"swanky-purse",
		"trontastic", 
		"bootstrap", 
		"ui-darkness",
		"ui-lightness", 
		"vader" 
		};

	public String[] getThemes() {
		return(POSSIBLE_THEMES);
	}

}