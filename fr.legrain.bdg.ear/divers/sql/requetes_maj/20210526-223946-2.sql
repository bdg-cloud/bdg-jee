
--set schema 'demo';

--  !!!!!!!!!!  j'ai mis ces maj dans un fichier à part car le dossier chokauto sur le cloud a déjà ces 2 champs rempli lors de l'importation de ces articles et tiers
-- car il avait des champs que l'on exploitera plus tard, donc pour ne pas que toutes les maj de ce dossier plante, je sépare, donc il n'y aura que celle là qui plantera pour ce dossier

  
  -- pour importation de champs que l'on ne peut pas encore exploiter ----
  ALTER TABLE ta_article ADD COLUMN importation_divers text;
COMMENT ON COLUMN ta_article.importation_divers IS 'importation divers champs en json pour utilisation ultérieure';


  ALTER TABLE ta_tiers ADD COLUMN importation_divers text;
COMMENT ON COLUMN ta_tiers.importation_divers IS 'importation divers champs en json pour utilisation ultérieure';

