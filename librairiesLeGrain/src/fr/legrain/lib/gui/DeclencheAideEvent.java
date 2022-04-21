package fr.legrain.lib.gui;

import java.util.EventObject;

import javax.swing.JComponent;

public class DeclencheAideEvent extends EventObject {
	
	private JComponent input = null;
	
	public DeclencheAideEvent(Object source, JComponent input) {
		super(source);
		this.input = input;
	}

	public JComponent getInput() {
		return input;
	}

	public void setInput(JComponent input) {
		this.input = input;
	}
	



}