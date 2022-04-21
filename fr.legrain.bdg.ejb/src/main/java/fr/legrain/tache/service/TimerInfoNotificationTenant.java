package fr.legrain.tache.service;

import java.io.Serializable;

import fr.legrain.tache.model.TaNotification;

public class TimerInfoNotificationTenant implements Serializable{

	private static final long serialVersionUID = -4438427890606863275L;
	
	private TaNotification taNotification;
	private String tenant;
	
	public TimerInfoNotificationTenant(TaNotification taNotification, String tenant) {
		this.taNotification = taNotification;
		this.tenant = tenant;
	}
	
	public TaNotification getTaNotification() {
		return taNotification;
	}
	public void setTaNotification(TaNotification taNotification) {
		this.taNotification = taNotification;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	

}
