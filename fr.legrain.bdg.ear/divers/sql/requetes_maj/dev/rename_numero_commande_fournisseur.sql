--set schema 'demo';

ALTER TABLE ta_facture RENAME COLUMN numero_commande_fournisseur TO num_cmd_fournisseur;
ALTER TABLE ta_bonliv RENAME COLUMN numero_commande_fournisseur TO num_cmd_fournisseur;
ALTER TABLE ta_boncde RENAME COLUMN numero_commande_fournisseur TO num_cmd_fournisseur;
ALTER TABLE ta_flash RENAME COLUMN numero_commande_fournisseur TO num_cmd_fournisseur;
ALTER TABLE ta_panier RENAME COLUMN numero_commande_fournisseur TO num_cmd_fournisseur;
ALTER TABLE ta_preparation RENAME COLUMN numero_commande_fournisseur TO num_cmd_fournisseur;
ALTER TABLE ta_boncde_achat RENAME COLUMN numero_commande_fournisseur TO num_cmd_fournisseur;

ALTER TABLE ta_facture Alter column num_cmd_fournisseur type dlib50;
ALTER TABLE ta_bonliv Alter column num_cmd_fournisseur type dlib50;
ALTER TABLE ta_boncde Alter column num_cmd_fournisseur type dlib50;
ALTER TABLE ta_flash Alter column num_cmd_fournisseur type dlib50;
ALTER TABLE ta_panier Alter column num_cmd_fournisseur type dlib50;
ALTER TABLE ta_preparation Alter column num_cmd_fournisseur type dlib50;
ALTER TABLE ta_boncde_achat Alter column num_cmd_fournisseur type dlib50;