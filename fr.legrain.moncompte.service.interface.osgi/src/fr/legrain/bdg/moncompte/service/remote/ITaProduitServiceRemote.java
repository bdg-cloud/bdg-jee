package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaProduitDTO;
import fr.legrain.moncompte.model.TaProduit;


@Remote
//@Path("/product")
public interface ITaProduitServiceRemote extends IGenericCRUDServiceRemote<TaProduit,TaProduitDTO>,
														IAbstractLgrDAOServer<TaProduit>,IAbstractLgrDAOServerDTO<TaProduitDTO>{
	public static final String validationContext = "PRODUIT";
	
//	//@RolesAllowed("admin")
//	@GET
//	@Path("{id}")
//	@Produces("application/json")
//	public TaProduit getProductId(@PathParam("id") String id);
}
