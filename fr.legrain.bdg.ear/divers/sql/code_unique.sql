set schema 'public';
    

ALTER TABLE ONLY ta_acompte
    ADD CONSTRAINT ta_acompte_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_c_paiement
    ADD CONSTRAINT ta_c_paiement_code_unique UNIQUE (code_c_paiement);
    
  

ALTER TABLE ONLY ta_devis
    ADD CONSTRAINT ta_devis_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_document_tiers
    ADD CONSTRAINT ta_doc_tiers_code_unique UNIQUE (code_document_tiers);
    

ALTER TABLE ONLY ta_document_doc
    ADD CONSTRAINT ta_document_code_unique UNIQUE (code_document_doc);
    

ALTER TABLE ONLY ta_end_to_end
    ADD CONSTRAINT ta_end_to_end_code_unique UNIQUE (code_end_to_end);
    

ALTER TABLE ONLY ta_entrepot
    ADD CONSTRAINT ta_entrepot_code_unique UNIQUE (code_entrepot);
    

ALTER TABLE ONLY ta_etat
    ADD CONSTRAINT ta_etat_code_unique UNIQUE (code_etat);
    

ALTER TABLE ONLY ta_fabrication
    ADD CONSTRAINT ta_fabrication_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_facture
    ADD CONSTRAINT ta_facture_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_famille
    ADD CONSTRAINT ta_famille_code_unique UNIQUE (code_famille);
    

ALTER TABLE ONLY ta_famille_tiers
    ADD CONSTRAINT ta_famille_tiers_code_unique UNIQUE (code_famille);
    

ALTER TABLE ONLY ta_famille_unite
    ADD CONSTRAINT ta_famille_unite_code_unique UNIQUE (code_famille);
    

ALTER TABLE ONLY ta_gen_code_ex
    ADD CONSTRAINT ta_gencode_code_unique UNIQUE (code_gen_code, cle_gen_code);
    

ALTER TABLE ONLY ta_gr_mouv_stock
    ADD CONSTRAINT ta_grmouv_code_unique UNIQUE (code_gr_mouv_stock);
    

ALTER TABLE ONLY ta_groupe
    ADD CONSTRAINT ta_groupe_code_unique UNIQUE (code_groupe);
    

ALTER TABLE ONLY ta_label_article
    ADD CONSTRAINT ta_label_article_code_unique UNIQUE (code_label_article);
    

    

ALTER TABLE ONLY ta_mandat
    ADD CONSTRAINT ta_mandat_code_unique UNIQUE (code_mandat);
    

ALTER TABLE ONLY ta_modele_fabrication
    ADD CONSTRAINT ta_modele_fab_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_modele_groupe
    ADD CONSTRAINT ta_modele_groupe_code_unique UNIQUE (code_groupe);
    

ALTER TABLE ONLY ta_prelevement
    ADD CONSTRAINT ta_prelevement_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_proforma
    ADD CONSTRAINT ta_proforma_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_reglement
    ADD CONSTRAINT ta_reglement_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_relance
    ADD CONSTRAINT ta_relance_code_unique UNIQUE (code_relance);
    

ALTER TABLE ONLY ta_remise
    ADD CONSTRAINT ta_remise_code_unique UNIQUE (code_document);
    

ALTER TABLE ONLY ta_support_abon
    ADD CONSTRAINT ta_support_code_unique UNIQUE (code_support_abon);
    

ALTER TABLE ONLY ta_t_abonnement
    ADD CONSTRAINT ta_t_abon_code_unique UNIQUE (code_t_abonnement);
    

ALTER TABLE ONLY ta_t_adr
    ADD CONSTRAINT ta_t_adr_code_unique UNIQUE (code_t_adr);
    

ALTER TABLE ONLY ta_t_article
    ADD CONSTRAINT ta_t_article_code_unique UNIQUE (code_t_article);
    

ALTER TABLE ONLY ta_t_banque
    ADD CONSTRAINT ta_t_banque_code_unique UNIQUE (code_t_banque);
    

ALTER TABLE ONLY ta_t_c_paiement
    ADD CONSTRAINT ta_t_c_paiement_code_unique UNIQUE (code_t_c_paiement);
    

ALTER TABLE ONLY ta_t_civilite
    ADD CONSTRAINT ta_t_civilite_code_unique UNIQUE (code_t_civilite);
    

ALTER TABLE ONLY ta_type_code_barre
    ADD CONSTRAINT ta_t_code_barre_code_unique UNIQUE (code_type_code_barre);
    

ALTER TABLE ONLY ta_type_conformite
    ADD CONSTRAINT ta_t_conformite UNIQUE (code);
    

ALTER TABLE ONLY ta_t_doc
    ADD CONSTRAINT ta_t_doc_code_unique UNIQUE (code_t_doc);
    

ALTER TABLE ONLY ta_t_email
    ADD CONSTRAINT ta_t_email_code_unique UNIQUE (code_t_email);
    

ALTER TABLE ONLY ta_t_entite
    ADD CONSTRAINT ta_t_entite_code_unique UNIQUE (code_t_entite);
    

ALTER TABLE ONLY ta_t_fabrication
    ADD CONSTRAINT ta_t_fab_code_unique UNIQUE (code_t_fabrication);
    

ALTER TABLE ONLY ta_t_liens
    ADD CONSTRAINT ta_t_liens_code_unique UNIQUE (code_t_liens);
    

ALTER TABLE ONLY ta_t_ligne
    ADD CONSTRAINT ta_t_ligne_code_unique UNIQUE (code_t_ligne);
    

ALTER TABLE ONLY ta_type_mouvement
    ADD CONSTRAINT ta_t_mouv_code_unique UNIQUE (code);
    

    

ALTER TABLE ONLY ta_t_note_tiers
    ADD CONSTRAINT ta_t_note_tiers_code_unique UNIQUE (code_t_note_tiers);
    

ALTER TABLE ONLY ta_t_operation
    ADD CONSTRAINT ta_t_operation_code_unique UNIQUE (code_t_operation);
    

ALTER TABLE ONLY ta_t_paiement
    ADD CONSTRAINT ta_t_paiement_code_unique UNIQUE (code_t_paiement);
    

ALTER TABLE ONLY ta_t_reception
    ADD CONSTRAINT ta_t_reception_code_unique UNIQUE (code_t_reception);
    

ALTER TABLE ONLY ta_t_relance
    ADD CONSTRAINT ta_t_relance_code_unique UNIQUE (code_t_relance);
    

ALTER TABLE ONLY ta_t_support
    ADD CONSTRAINT ta_t_support_code_unique UNIQUE (code_t_support);
    

ALTER TABLE ONLY ta_t_tarif
    ADD CONSTRAINT ta_t_tarif_code_unique UNIQUE (code_t_tarif);
    

ALTER TABLE ONLY ta_t_tel
    ADD CONSTRAINT ta_t_tel_code_unique UNIQUE (code_t_tel);
    

ALTER TABLE ONLY ta_t_tiers
    ADD CONSTRAINT ta_t_tiers_code_unique UNIQUE (code_t_tiers);
    

ALTER TABLE ONLY ta_t_tva
    ADD CONSTRAINT ta_t_tva_code_unique UNIQUE (code_t_tva);
    

ALTER TABLE ONLY ta_t_tva_doc
    ADD CONSTRAINT ta_t_tva_doc_code_unique UNIQUE (code_t_tva_doc);
    

ALTER TABLE ONLY ta_t_web
    ADD CONSTRAINT ta_t_web_code_unique UNIQUE (code_t_web);
    

ALTER TABLE ONLY ta_tiers
    ADD CONSTRAINT ta_tiers_code_unique UNIQUE (code_tiers);
    

ALTER TABLE ONLY ta_titre_transport
    ADD CONSTRAINT ta_titre_trans_code_unique UNIQUE (code_titre_transport);
    

ALTER TABLE ONLY ta_tva
    ADD CONSTRAINT ta_tva_code_unique UNIQUE (code_tva);
    

ALTER TABLE ONLY ta_unite
    ADD CONSTRAINT ta_unite_code_unique UNIQUE (code_unite);
    

ALTER TABLE ONLY ta_utilisateur
    ADD CONSTRAINT ta_utilisateur_code_unique UNIQUE (username);
    

ALTER TABLE ONLY ta_verrou_code_genere
    ADD CONSTRAINT ta_verrou_code_gen_code_unique UNIQUE (entite, champ, valeur, session_id);
    

ALTER TABLE ONLY ta_verrou_modification
    ADD CONSTRAINT ta_verrou_modif_code_unique UNIQUE (entite, champ, valeur, session_id);
    
                                                                                                                