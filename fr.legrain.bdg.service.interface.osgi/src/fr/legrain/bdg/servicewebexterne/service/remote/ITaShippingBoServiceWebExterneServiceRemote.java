package fr.legrain.bdg.servicewebexterne.service.remote;

import java.io.IOException;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

public interface ITaShippingBoServiceWebExterneServiceRemote {
	
	public Integer enregistreLigneDepuisFichier() throws Exception;
	public Integer parcourir() throws EJBException;
	public Integer recupereFichier() throws EJBException,IOException,FinderException;
	
}
