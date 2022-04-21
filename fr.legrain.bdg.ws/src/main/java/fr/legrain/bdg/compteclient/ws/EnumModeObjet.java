
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour enumModeObjet.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumModeObjet"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="C_MO_CONSULTATION"/&gt;
 *     &lt;enumeration value="C_MO_EDITION"/&gt;
 *     &lt;enumeration value="C_MO_INSERTION"/&gt;
 *     &lt;enumeration value="C_MO_IMPORTATION"/&gt;
 *     &lt;enumeration value="C_MO_ERREUR"/&gt;
 *     &lt;enumeration value="C_MO_SUPPRESSION"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "enumModeObjet")
@XmlEnum
public enum EnumModeObjet {

    C_MO_CONSULTATION,
    C_MO_EDITION,
    C_MO_INSERTION,
    C_MO_IMPORTATION,
    C_MO_ERREUR,
    C_MO_SUPPRESSION;

    public String value() {
        return name();
    }

    public static EnumModeObjet fromValue(String v) {
        return valueOf(v);
    }

}
