package fr.legrain.abonnement.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.dao.jpa.IFrequenceDAO;
import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.abonnement.service.remote.ITaFrequenceServiceRemote;
import fr.legrain.abonnement.service.remote.ITunnelVenteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaFrequenceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TunnelVenteService  implements ITunnelVenteServiceRemote {

	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLAbonnementServiceRemote taLAbonnementService;
	/**
	 * Default constructor. 
	 */
	public TunnelVenteService() {
	}
	

	
}
