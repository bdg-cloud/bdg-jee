package fr.legrain.bdg.client.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import fr.legrain.bdg.client.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.SERVEUR,"localhost");
		store.setDefault(PreferenceConstants.SERVEUR_PORT,"8080");
		store.setDefault(PreferenceConstants.SERVEUR_LOGIN,"demo");
		store.setDefault(PreferenceConstants.SERVEUR_PASSWORD,"demo");
		store.setDefault(PreferenceConstants.SERVEUR_DOSSIER,"demo");
	}

}
