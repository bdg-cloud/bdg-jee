package fr.legrain.controle.dao;

import fr.legrain.controle.model.TaGenCodeEx;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IGenCodeExDAO extends IGenericDAO<TaGenCodeEx> {

	public static final String C_NOMFOURNISSEUR="@nom";
	public static final String C_CODEFOURNISSEUR="@codef";
	public static final String C_CODEDOCUMENT="@coded";
	public static final String C_DATEDOCUMENT="@dated";
	public static final String C_DATEDOCUMENT_MOIS="@datedm";
	public static final String C_DATEDOCUMENT_ANNEE="@dateda";
	public static final String C_DATEDOCUMENT_QUANT="@dated_quant";
	public static final String C_EXO="@exo";
	public static final String C_NUM="@num";
	public static final String C_DATE="@date";
	public static final String C_DATE_MOIS="@datem";
	public static final String C_DATE_ANNEE="@datea";
	public static final String C_QUANT="@quant";
	public static final String C_HEURE="@heure";
	public static final String C_DESC="@desc";
	public static final String C_CODETYPE="@codetype";
	public static final String C_VIRTUEL="@virtuel";
	public static final String C_IDARTICLEVIRTUEL="@codea";

	
	public String genereCodeExJPA(TaGenCodeEx genCodeEx, int rajoutCompteur, String section, String exo) throws Exception;
	public String genereCodeExJPA(int rajoutCompteur, String section, String exo) throws Exception;


}
