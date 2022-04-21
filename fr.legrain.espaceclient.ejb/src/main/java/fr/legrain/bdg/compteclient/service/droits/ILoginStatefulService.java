package fr.legrain.bdg.compteclient.service.droits;

public interface ILoginStatefulService {
	public String login();
	public TenantInfo getTenantInfo();
}
