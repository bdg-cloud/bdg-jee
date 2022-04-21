package fr.legrain.angdi.service.remote;

import javax.ejb.Remote;

@Remote
public interface IGandiService {

	/**
	 * Ajoute une ligne de DNS pour un sous domaine donné
	 * @param string $username
	 * @param string $password
	 * @param string $domain
	 * @param string $subdomain
	 * @param string $ip
	 * @return bool
	 */
	public boolean registerDNSSubDomain(String securityKey, String domain,
			String subdomain, String ip);

	/**
	 * Supprime les DNS pour un sous domaine donné
	 * @param string $username
	 * @param string $password
	 * @param string $domain
	 * @param string $subdomain
	 * @return bool
	 */
	public boolean deleteDNSSubdomain(String securityKey, String domain,
			String subdomain);

}