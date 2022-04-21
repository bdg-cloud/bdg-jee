package fr.legrain.servicewebexterne.service.divers.mensuraxml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class BodyMensura {
	
	@XmlElementWrapper(name="LIGNE")
	 ArrayList < LigneDevisMensura > LIGNE = new ArrayList < LigneDevisMensura > ();

	public ArrayList<LigneDevisMensura> getLIGNE() {
		return LIGNE;
	}

	public void setLIGNE(ArrayList<LigneDevisMensura> lIGNE) {
		LIGNE = lIGNE;
	}

}
