package fr.legrain.bdg.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.tiers.service.EtatDocument;

//http://stackoverflow.com/questions/3771103/how-do-i-get-a-list-of-all-httpsession-objects-in-a-web-application

@WebListener
public class SessionListener implements HttpSessionListener, ServletContextListener {
	
	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();
	
	private ScheduledExecutorService scheduler; 
	
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB ITaEtatServiceRemote taEtatService;
	@EJB ITaPreferencesServiceRemote taPreferencesService;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.put(session.getId(), session);
        nettoyerCodeGenerePourSessionInnactive();
        nettoyerEtatPourSessionInnactive();
        chargerPreferencesSurNouvelleConnection();
    }
    
    /**
     * A la connexion/login, l'ID de session est automatiquement modifié par le serveur pour des raisons de sécurité.
     * Cette méthode permet de s'assurer de la bonne correspondance clé/valeur de la map.
     * TODO vérifier qu'il n'y a pas de problème d'accès concurent avec les threads.
     */
    public static void updateSessionKeys() {
    	Map<String, HttpSession> sessionTmp=new HashMap<String, HttpSession>();
    	sessionTmp.putAll(sessions);
    	sessions.clear();
    	for (HttpSession s : sessionTmp.values()) {
    		sessions.put(s.getId(), s);
		}
    }
    
    
    public void nettoyerEtatPourSessionInnactive() {
    	taEtatService.razEtatDocument();
    }
    
    public void nettoyerCodeGenerePourSessionInnactive() {
    	List<String> list = new ArrayList<String>(sessions.keySet());
        taGenCodeExService.libereVerrouTout(list);
    }
 
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    	nettoyerEtatPourSessionInnactive();
    	sessions.remove(event.getSession().getId());        
    }

    public static HttpSession find(String sessionId) {
        return sessions.get(sessionId);
    }

    //////////////////// ServletContextListener
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Starting up!");
        scheduler = Executors.newSingleThreadScheduledExecutor();

        //scheduler.scheduleAtFixedRate(new SomeDailyJob(), 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Shutting down!");
        scheduler.shutdownNow();
    }
    
    public class SomeDailyJob implements Runnable {

        @Override
        public void run() {
            System.out.println("SessionListener.SomeDailyJob.run() "+new Date());
            nettoyerCodeGenerePourSessionInnactive();
        }

    }

	public static Map<String, HttpSession> getSessions() {
		return sessions;
	}
	
	
    public void chargerPreferencesSurNouvelleConnection() {
    	taPreferencesService.razPrefGeneral();
    	taPreferencesService.chargePrefGeneral();
    }
    
}