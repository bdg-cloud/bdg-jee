set schema 'legrain82';
--a appliquer sur LEGRAIN82 uniquement

update ta_article set code_module_bdg  = code_article where code_article = 'BDC_GC_VITI_P';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_GC_VITI_PP';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_FBC_VITI_E';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_FBC_VITI_EP';

update ta_article set code_module_bdg  = code_article where code_article = 'BDC-FD';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_FD_B';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_GC';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_FA';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_FA_B';
update ta_article set code_module_bdg  = code_article where code_article = 'BDC_BCBL';