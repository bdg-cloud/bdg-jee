package fr.legrain.moncompte.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import fr.legrain.bdg.moncompte.service.remote.ITaDossierServiceRemote;
import fr.legrain.moncompte.model.TaDossier;

@Stateless
public class TimerService {
	/*
	 * https://docs.oracle.com/javaee/7/tutorial/ejb-basicexamples004.htm
	 * http://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html
	 * http://docs.oracle.com/javaee/7/api/javax/ejb/Schedule.html
	 */
	
	@EJB private ITaDossierServiceRemote taDossierService;
	
	//Toutes les heures
	//@Schedule(second="0", minute="0",hour="*", persistent=false)
	
	//Toutes les jours à 2h du matin
	@Schedule(second="0", minute="0",hour="2", persistent=false)
	
	//Toutes les lundi à 2h00
	//@Schedule(second="0", minute="0",hour="2",dayOfWeek="1", persistent=false)
	
	//Toutes les lundi à 2h00, tous les 1er du mois
	//@Schedule(second="0", minute="0",hour="2",dayOfMonth="1", persistent=false)
	
	//Toutes les lundi à 2h00, tous les 1er du mois
	//@Schedule(second="0", minute="0",hour="2",dayOfMonth="1",month="*", year="*", persistent=false)
    public void doWork(){
		
		List<TaDossier> listeDossier = taDossierService.selectAll();
        System.out.println("Timer MAJ autorisation dossier : ");
        
        for (TaDossier dos : listeDossier) {
        	taDossierService.initDroitModules(dos);
		}
    }

}
