package fr.legrain.gandi.service.remote;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.gandi.api.Domain;
import fr.legrain.gandi.api.InfosAvailabilityDomainList;
import fr.legrain.gandi.api.InfosCertifSSL;
import fr.legrain.gandi.api.InfosDomainList;
import fr.legrain.gandi.api.InfosOperationDomainRenouv;
import fr.legrain.gandi.api.InfosPackMail;
import fr.legrain.gandi.api.InfosSimpleHostingList;
import fr.legrain.gandi.api.ServerIaas;






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
	
	/**
	 * Liste les Server Iaas
	 * @param string $username
	 * @param string $password
	 * @return List<ServerIaas>
	 */
	public List<ServerIaas> listServer(String securityKey);
	
	/**
	 * Démarre un server Iaas
	 * @param string $username
	 * @param string $password
	 * @param int $vm_id
	 * @return boolean
	 */
	
	public boolean startServer(String securityKey, int vm_id);
	
	/**
	 * Stoppe un server Iaas
	 * @param string $username
	 * @param string $password
	 * @param int $vm_id
	 * @return boolean
	 */
	public boolean stopServer(String securityKey, int vm_id);
	
	/********************DOMAINES*****************************/
	/**
	 * Renouvelle un domaine donné
	 * @param string $username
	 * @param string $password
	 * @param InfosDomainList $domain
	 * @return InfosOperationDomainRenouv 
	 */	
	public InfosOperationDomainRenouv renouvDomaine(String securityKey, String domain, HashMap<String, Integer> params);
	/**
	 * Liste les domaines
	 * @param string $username
	 * @param string $password
	 * @return List<InfosDomain>
	 */	
	public List<Domain> listDomaine(String securityKey);
	/**
	 * Liste les domaines
	 * @param string $username
	 * @param string $password
	 * @return List<InfosDomainList>
	 */	
	public List<InfosDomainList> listInfosDomaine(String securityKey);
	/**
	 * Liste la disponibilité des domaines donnés
	 * @param string $username
	 * @param string $password
	 * @param string[] $nomDomaine
	 * @return List<InfosDomainList>
	 */	
	public List<InfosAvailabilityDomainList> listInfosDomaineAvailability(String securityKey, List<String> nomDomaine);
	
	/*********************PACK MAIL***************************/
	/**
	 * Donne les infos du pack mail d'un nom de domaine
	 * @param string $username
	 * @param string $password
	 * @param string $domain
	 * @return InfosPackMail
	 */	
	public InfosPackMail infosPackMail(String securityKey, String domain);
	
	/************************SIMPLE HOSTING ***********************************/
	/**
	 * Liste les Instances Simples hosting
	 * @param string $username
	 * @param string $password
	 * @return List<InfosSimpleHostingList>
	 */	
	public List<InfosSimpleHostingList> listInfosSimpleHosting(String securityKey);
	/**
	 * Liste les Instances Simples hosting
	 * @param string $username
	 * @param string $password
	 * @return List<InfosCertifSSL>
	 */	
	public List<InfosCertifSSL> listInfosCertifSSL(String securityKey);

}