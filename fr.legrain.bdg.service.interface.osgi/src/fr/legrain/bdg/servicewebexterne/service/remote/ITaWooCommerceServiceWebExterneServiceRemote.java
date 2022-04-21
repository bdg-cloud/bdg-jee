package fr.legrain.bdg.servicewebexterne.service.remote;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

public interface ITaWooCommerceServiceWebExterneServiceRemote {
	public Integer getOrdersREST(String etat) throws Exception;
	
}
