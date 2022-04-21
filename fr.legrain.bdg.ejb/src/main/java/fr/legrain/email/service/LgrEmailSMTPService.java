package fr.legrain.email.service;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.mail.LgrEmailUtil;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@EmailLgr(EmailLgr.TYPE_SMTP_PROGRAMME_BDG)
public class LgrEmailSMTPService {
	
	private LgrEmailUtil util;
	
	private BdgProperties bdgProperties;
	
	@PostConstruct
	private void init() {
		bdgProperties = new BdgProperties();
//		util = new LgrEmailUtil("smtp.legrain.fr",587,true,"nicolas@legrain.fr","pwd37las67"); //STARTTLS
//		util = new LgrEmailUtil("smtp.bdg.cloud",587,true,"noreply@bdg.cloud","aeHaegh2");
		util = new LgrEmailUtil(
				bdgProperties.getProperty(BdgProperties.KEY_SMTP_HOSTNAME),
				LibConversion.stringToInteger(bdgProperties.getProperty(BdgProperties.KEY_SMTP_PORT)),
				LibConversion.StringToBoolean(bdgProperties.getProperty(BdgProperties.KEY_SMTP_SSL)),
				bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN),
				bdgProperties.getProperty(BdgProperties.KEY_SMTP_PASSWORD)
				); //STARTTLS
	}
	
	public void sendEmail(String subject, String msgTxt, String fromEmail, String fromName, String[][] toEmailandName) {
		util.sendEmail(subject,msgTxt,fromEmail,fromName,toEmailandName);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
}
