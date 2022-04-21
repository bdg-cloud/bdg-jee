package fr.legrain.bdg.rest.filters.request;

import java.security.Key;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
//https://github.com/agoncal/agoncal-sample-jaxrs/blob/master/04-JWT/src/main/java/org/agoncal/sample/jaxrs/jwt/util/KeyGenerator.java
public interface KeyGenerator {

    Key generateKey();
}