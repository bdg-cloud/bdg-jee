--set schema 'demo';


------------- debut stripe account -----------
CREATE TABLE ta_stripe_account
(
  id_stripe_account serial NOT NULL,
  id_externe dlib255,
  business_type dlib100,
  country dlib50,
  name dlib255,
  email dlib100,
  account_type dlib100,
  default_currency dlib50,
  details_submitted boolean default false,
  charges_enabled boolean default false,
  qui_cree dlib50,
  quand_cree date_heure_lgr,
  qui_modif dlib50,
  quand_modif date_heure_lgr,
  version num_version,
  ip_acces dlib50,
  version_obj did_version_obj,
  CONSTRAINT ta_stripe_account_pkey PRIMARY KEY (id_stripe_account)
 )
WITH (
  OIDS=FALSE
);
ALTER TABLE ta_stripe_account
  OWNER TO bdg;

CREATE TRIGGER tbi_ta_stripe_account
  BEFORE INSERT
  ON ta_stripe_account
  FOR EACH ROW
  EXECUTE PROCEDURE before_insert();

CREATE TRIGGER tbu_ta_stripe_account
  BEFORE UPDATE
  ON ta_stripe_account
  FOR EACH ROW
  EXECUTE PROCEDURE before_update();
  
INSERT INTO ta_oauth_service_client (code, libelle, description, url, urlendpoint, actif, systeme, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES ('STRIPE', 'Stripe', 'Stripe', null, null, true, true, '2021-01-20 16:05:53.647000', '2021-01-20 16:06:01.457000', null, null, null, 0);
 
INSERT INTO ta_oauth_scope_client (code, libelle, description, id_oauth_service_client, identifiant_service, actif, systeme, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES ('STRIPE_READ_ONLY', 'Lecture', null, (select  id from ta_oauth_service_client where code='STRIPE'), null, true, false, null, null, null, null, null, 0);
INSERT INTO ta_oauth_scope_client (code, libelle, description, id_oauth_service_client, identifiant_service, actif, systeme, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES ('STRIPE_READ_WRITE', 'Lecture / Ecriture', null,  (select  id from ta_oauth_service_client where code='STRIPE'), null, true, false, null, null, null, null, null, 0);

INSERT INTO ta_service_web_externe (code_service_web_externe, libelle_service_web_externe, description_service_web_externe, id_t_authentification_defaut, id_t_service_web_externe, url_editeur, url_service, defaut, verson_api, type_api, actif, api_multicompte, systeme, version, version_obj, logo, url_gestion_service_web_externe, id_module_bdg_autorisation) 
VALUES ('MSCALENDAR', 'Microsoft Outlook Calendar', 'Calendrier Outlook', (select  id_t_authentification from ta_t_authentification where code_t_authentification='OAuth'), (select  id_t_service_web_externe from ta_t_service_web_externe where code_t_service_web_externe='BUREAUTIQUE'), 'https://outlook.com', null, false, null, null, true, false, true, '2.0.13', 0, null, null, null);



INSERT INTO ta_service_web_externe (code_service_web_externe, libelle_service_web_externe, description_service_web_externe, id_t_authentification_defaut, id_t_service_web_externe, url_editeur, url_service, defaut, verson_api, type_api, actif, api_multicompte, systeme, version, version_obj, logo, url_gestion_service_web_externe, id_module_bdg_autorisation) 
VALUES ('STRIPE_CONNECT', 'Stripe Connect', 'Service de paiement en ligne', (select  id_t_authentification from ta_t_authentification where code_t_authentification='OAuth'), (select  id_t_service_web_externe from ta_t_service_web_externe where code_t_service_web_externe='PAIEMENT'), 'https://stripe.com', 'https://api.stripe.com', false, null, null, true, false, true, '2.0.13', 0, null, 'https://dashboard.stripe.com/', 'fr.legrain.connexion.paiement.stripe.connect');

update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.connexion.paiement.stripe' where code_service_web_externe = 'STRIPE';
update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.connexion.email.mailjet' where code_service_web_externe = 'MAILJET';
update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.connexion.sms.mailjet' where code_service_web_externe = 'MAILJET SMS';
update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.connexion.sms.ovh' where code_service_web_externe = 'OVH_SMS';
update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.importation.shippingbo' where code_service_web_externe = 'SHIPPINGBO';
update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.importation.mensura' where code_service_web_externe = 'MENSURA';
update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.importation.woocommerce' where code_service_web_externe = 'WOOCOMMERCE';
update ta_service_web_externe set id_module_bdg_autorisation = 'fr.legrain.importation.prestashop' where code_service_web_externe = 'PRESTASHOP';

--update ta_service_web_externe set id_module_bdg_autorisation = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx' where code_service_web_externe = 'GDRIVE';
--update ta_service_web_externe set id_module_bdg_autorisation = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx' where code_service_web_externe = 'DROPBOX';
--update ta_service_web_externe set id_module_bdg_autorisation = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx' where code_service_web_externe = 'GCALENDAR';
--update ta_service_web_externe set id_module_bdg_autorisation = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx' where code_service_web_externe = 'OWNCLOUD';
--update ta_service_web_externe set id_module_bdg_autorisation = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx' where code_service_web_externe = 'MSONEDRIVE';
--update ta_service_web_externe set id_module_bdg_autorisation = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx' where code_service_web_externe = 'GMAP';
--update ta_service_web_externe set id_module_bdg_autorisation = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx' where code_service_web_externe = 'MAILJET_CAMPAGNE';
------------- fin stripe account -----------
