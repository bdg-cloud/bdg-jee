package fr.legrain.general.dto;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.LinkedMap;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.document.dto.DocumentDTO;

// Generated Mar 1, 2007 11:26:10 AM by Hibernate Tools 3.2.0.b9

/**
 * SWTFichier modifier par Dima
 */
public abstract class AbstractDTO extends ModelObject {

	public static final String f_extract_day ="extract_day";	
	public static final String f_extract_month ="extract_month";	
	public static final String f_extract_year ="extract_year";
	public static final String f_extract_day_int ="extract_day_int";	
	public static final String f_extract_month_int ="extract_month_int";	
	public static final String f_extract_year_int ="extract_year_int";

	
	protected Map<String, String> mapAliasDTO = new LinkedMap();
	protected abstract void initMapDTO();
//	protected abstract   String retournAlias(String prefixe,String champ);
	
	public AbstractDTO() {
	}

	public String retournExtractDaySQLNatif(String prefixe,String champ) {
		return "cast(extract(day from "+prefixe+"."+champ+") as varchar)";
	}
	public String retournExtractDayAndAliasSQLNatif(String prefixe,String champ) {
		return "cast(extract(day from "+prefixe+"."+champ+")as varchar) as "+mapAliasDTO.get(f_extract_day);
	}
	
	public String retournExtractMonthSQLNatif(String prefixe,String champ) {
		return "cast(extract(month from "+prefixe+"."+champ+")as varchar)";
	}
	public String retournExtractMonthAndAliasSQLNatif(String prefixe,String champ) {
		return "cast(extract(month from "+prefixe+"."+champ+")as varchar) as "+mapAliasDTO.get(f_extract_month);
	}

	public String retournExtractYearSQLNatif(String prefixe,String champ) {
		return "cast(extract(year from "+prefixe+"."+champ+")as varchar)";
	}
	public String retournExtractYearAndAliasSQLNatif(String prefixe,String champ) {
		return "cast(extract(year from "+prefixe+"."+champ+")as varchar) as "+mapAliasDTO.get(f_extract_year);
	}
	
	public String retournExtractDay(String prefixe,String champ) {
//		return "cast(extract(day from "+prefixe+"."+champ+") as string)";
		return "extract(day from "+prefixe+"."+champ+") ";
	}
	public String retournExtractDayAndAlias(String prefixe,String champ) {
//		return "cast(extract(day from "+prefixe+"."+champ+")as string) as "+mapAliasDTO.get(f_extract_day);
		return "extract(day from "+prefixe+"."+champ+") as "+mapAliasDTO.get(f_extract_day_int);
	}
	
	public String retournExtractMonth(String prefixe,String champ) {
//		return "cast(extract(month from "+prefixe+"."+champ+")as string)";
		return "extract(month from "+prefixe+"."+champ+")";
	}
	public String retournExtractMonthAndAlias(String prefixe,String champ) {
//		return "cast(extract(month from "+prefixe+"."+champ+")as string) as "+mapAliasDTO.get(f_extract_month);
		return "extract(month from "+prefixe+"."+champ+") as "+mapAliasDTO.get(f_extract_month_int);
	}

	public String retournExtractYear(String prefixe,String champ) {
//		return "cast(extract(year from "+prefixe+"."+champ+")as string)";
		return "extract(year from "+prefixe+"."+champ+")";
	}
	public String retournExtractYearAndAlias(String prefixe,String champ) {
//		return "cast(extract(year from "+prefixe+"."+champ+")as string) as "+mapAliasDTO.get(f_extract_year);
		return "extract(year from "+prefixe+"."+champ+") as "+mapAliasDTO.get(f_extract_year_int);
	}

	
	public String retournAlias(String prefixe,String champ) {
		return " as "+mapAliasDTO.get(prefixe+"."+champ);
	}
	public String retournChampAndAlias(String prefixe,String champ) {
		return prefixe+"."+champ+" as "+mapAliasDTO.get(prefixe+"."+champ);
	}
	
	
	public String retournChampAndAliasSuivantTypeDocument(String prefixe,String champ,Object entity) {
		try {
			PropertyDescriptor props = PropertyUtils.getPropertyDescriptor(entity, champ);
			if(props!=null && props.getReadMethod()!=null
					&& !props.getReadMethod().isAnnotationPresent(Transient.class))
				return prefixe+"."+champ+" as "+mapAliasDTO.get(prefixe+"."+champ);
			else {
				if(props!=null) {
					if(props.getPropertyType().isAssignableFrom(Date.class)) return "cast(null as date) as "+mapAliasDTO.get(prefixe+"."+champ);
				}
				return "cast(null as string) as "+mapAliasDTO.get(prefixe+"."+champ);
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String construitSumCoallesce(String prefixe,String champ) {
		String retour="";
//		String alias=retournAlias(prefixe,champ);
		retour="coalesce(sum("+prefixe+"."+champ+"),0)";
		return retour; 
	}
	
	public String construitSumCoallesceAsAlias(String prefixe,String champ) {
		String retour="";
		String alias=retournAlias(prefixe,champ);
		retour="coalesce(sum("+prefixe+"."+champ+"),0)"+alias;
		return retour; 
	}
	
	public String construitCoallesceAsAlias(String prefixe,String champ) {
		String retour="";
		String alias=retournAlias(prefixe,champ);
		retour="coalesce("+prefixe+"."+champ+",0)"+alias;
		return retour; 
	}
	
	public String construitSumCoallesceAsAliasMtTvaSQL(String prefixe,String champ) {
		String retour="";
		String alias=retournAlias(prefixe,champ);
		retour="coalesce(sum("+prefixe+"."+DocumentDTO.f_net_Ttc_Calc+"),0)-coalesce(sum("+prefixe+"."+DocumentDTO.f_net_Ht_Calc+"),0)"+alias;
		return retour; 
	}

	public String construitSumCoallesceAsAliasMtTva(String prefixe,String champ,String champHT,String champTTC) {
		String retour="";
		String alias=retournAlias(prefixe,champ);
		retour="coalesce(sum("+prefixe+"."+champTTC+"),0)-coalesce(sum("+prefixe+"."+champHT+"),0)"+alias;
		return retour; 
	}
	
}