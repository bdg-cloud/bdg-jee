package fr.legrain.bdg.client.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import fr.legrain.bdg.client.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class ServeurPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public ServeurPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		/////////////////////////////////////////
		addField(
		new StringFieldEditor(PreferenceConstants.SERVEUR, "Serveur (domaine/IP)", getFieldEditorParent()));
		
		addField(
		new StringFieldEditor(PreferenceConstants.SERVEUR_PORT, "Port du serveur", getFieldEditorParent()));
		
		addField(
		new StringFieldEditor(PreferenceConstants.SERVEUR_LOGIN, "Identifiant", getFieldEditorParent()));
		
		addField(
		new StringFieldEditor(PreferenceConstants.SERVEUR_PASSWORD, "Mot de passe", getFieldEditorParent()));
		
		addField(
		new StringFieldEditor(PreferenceConstants.SERVEUR_DOSSIER, "Dossier", getFieldEditorParent()));

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}