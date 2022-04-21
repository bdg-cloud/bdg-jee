//<![CDATA[ 
/*
 * Fichier a charger/inclure avant les p:socket, sinon dans certains cas primefaces peut chercher a acceder a la fonction JS avant l'inclusion du fichier
 * ce qui provoque une erreur JS et un mauvais fonctionnement de la page
 * <p:socket onMessage="handleMessage" channel="/notifications" />
 */

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    function handleMessage(msg) {
    	//FacesMessage
		msg.severity = 'info';
		PF('growl').show([msg]);
//    	PF('growl').show(msg);
		
		notificationAgenda();
    	
    	//message texte
    	//PF('growl').renderMessage({
    	//	"summary":"BDG",
        //    "detail":msg,
        //    "severity":"info"});
	}
    
    function handleMessageProgramme(msg) {
//    	msg.severity = 'info';
//		PF('growl').show([msg]);
    	
		PF('growl').renderMessage({"summary": "BDG (programme): "+msg.detail,
            "detail": msg.summary,
            "severity":"info"})
		notificationAgenda();
    }
    
    function handleMessageDossier(msg) {
    	PF('growl').renderMessage({"summary": "BDG (dossier): "+msg.detail,
            "detail": msg.summary,
            "severity":"info"})
		notificationAgenda();
    }

    function handleMessageUtilisateur(msg) {
    	PF('growl').renderMessage({"summary": "BDG (utilisateur): "+msg.detail,
            "detail": msg.summary,
            "severity":"info"})
		notificationAgenda();
    }
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

	
//]]>