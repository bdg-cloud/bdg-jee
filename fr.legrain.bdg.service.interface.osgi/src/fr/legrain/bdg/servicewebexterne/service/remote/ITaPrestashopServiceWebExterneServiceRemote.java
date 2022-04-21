package fr.legrain.bdg.servicewebexterne.service.remote;

import java.io.IOException;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

public interface ITaPrestashopServiceWebExterneServiceRemote {
	public Integer getOrdersREST() throws Exception;
	
}
