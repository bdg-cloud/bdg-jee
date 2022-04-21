--REQUETES pour faire fonctionner les infos_abonnement comme sur les autres documents (Yann)
--set schema 'demo';
ALTER TABLE ta_infos_abonnement ADD CONSTRAINT ta_infos_abonnement_id_document_key UNIQUE (id_document);
ALTER TABLE ta_abonnement ADD COLUMN id_infos_document did_facultatif;
UPDATE ta_abonnement SET id_infos_document = ta_infos_abonnement.id_infos_document FROM ta_infos_abonnement WHERE ta_infos_abonnement.id_document = ta_abonnement.id_document; 
ALTER  TABLE ta_abonnement ALTER COLUMN id_infos_document SET NOT NULL;
ALTER  TABLE ta_abonnement ADD CONSTRAINT fk_id_infos_abonnement FOREIGN KEY (id_infos_document)
      REFERENCES ta_infos_abonnement (id_infos_document) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE ta_infos_abonnement ALTER COLUMN id_document DROP NOT NULL;

