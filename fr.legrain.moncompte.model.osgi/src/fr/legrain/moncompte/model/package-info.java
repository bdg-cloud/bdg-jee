@XmlSchema(
		namespace = "http://service.moncompte.legrain.fr/", 
		attributeFormDefault=javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
		elementFormDefault=javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
		xmlns = {
		        @XmlNs(prefix="tns", namespaceURI="http://service.moncompte.legrain.fr/")
		    }
		)
package fr.legrain.moncompte.model;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;
