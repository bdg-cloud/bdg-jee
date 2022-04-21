package fr.legrain.article.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CodeBarreExchange {

	
	 final private Map<String, String> mapData =new LinkedHashMap<String, String>();

	 private static final Map<String, AII> aiinfo = new LinkedHashMap<String, CodeBarreExchange.AII>();
	 
	
	static class AII {
	    final int minLength;
	    final int maxLength;

	    public AII(String id, int minLength, int maxLength) {
	      this.minLength = minLength;
	      this.maxLength = maxLength;
	    }
	  }
	
	
	private static void ai(String id, int minLength, int maxLength) {
	    aiinfo.put(id, new AII(id, minLength, maxLength));
	  }

	  private static void ai(String id, int length) {
	    aiinfo.put(id, new AII(id, length, length));
	  }
	  
	  
	  static {
		    ai("00", 18, 18);
		    ai("01", 14);
		    ai("02", 14);
		    ai("10", 1, 20);
		    ai("11", 6);
		    ai("12", 6);

//		    13	Packaging Date	6
//		    15	Best Before Date (YYMMDD)	6
//		    17	Expiration Date	6
//		    20	Product Variant	2
//		    21	Serial Number	variable, up to 20
//		    22	Secondary Data Fields	variable, up to 29
//		    23n	Lot number n	variable, up to 19
//		    240	Additional Product Identification	variable, up to 30
//		    241	Customer Part Number	variable, up to 30
//		    242	Made-to-Order Variation Number	variable, up to 6
//		    250	Secondary Serial Number	variable, up to 30
//		    251	Reference to Source Entity	variable, up to 30
//		    253	Global Document Type Identifier	variable, 13F17
//		    254	GLN Extension Component	variable, up to 20
//		    255	Global Coupon Number (GCN)	variable, 13–25
//		    30	Count of items	variable, up to 8
//		    310y	Product Net Weight in kg	6
//		    311y	Product Length/1st Dimension, in meters	6
//		    312y	Product Width/Diameter/2nd Dimension, in meters	6
//		    313y	Product Depth/Thickness/Height/3rd Dimension, in meters	6
//		    314y	Product Area, in square meters	6
//		    315y	Product Net Volume, in liters	6
//		    316y	Product Net Volume, in cubic meters	6
//		    320y	Product Net Weight, in pounds	6
//		    321y	Product Length/1st Dimension, in inches	6
//		    322y	Product Length/1st Dimension, in feet	6
//		    323y	Product Length/1st Dimension, in yards	6
//		    324y	Product Width/Diameter/2nd Dimension, in inches	6
//		    325y	Product Width/Diameter/2nd Dimension, in feet	6
//		    326y	Product Width/Diameter/2nd Dimension, in yards	6
//		    327y	Product Depth/Thickness/Height/3rd Dimension, in inches	6
//		    328y	Product Depth/Thickness/Height/3rd Dimension, in feet	6
//		    329y	Product Depth/Thickness/3rd Dimension, in yards	6
//		    330y	Container Gross Weight (kg)	6
//		    331y	Container Length/1st Dimension (Meters)	6
//		    332y	Container Width/Diameter/2nd Dimension (Meters)	6
//		    333y	Container Depth/Thickness/3rd Dimension (Meters)	6
//		    334y	Container Area (Square Meters)	6
//		    335y	Container Gross Volume (Liters)	6
//		    336y	Container Gross Volume (Cubic Meters)	6
//		    340y	Container Gross Weight (Pounds)	6
//		    341y	Container Length/1st Dimension, in inches	6
//		    342y	Container Length/1st Dimension, in feet	6
//		    343y	Container Length/1st Dimension in, in yards	6
//		    344y	Container Width/Diameter/2nd Dimension, in inches	6
//		    345y	Container Width/Diameter/2nd Dimension, in feet	6
//		    346y	Container Width/Diameter/2nd Dimension, in yards	6
//		    347y	Container Depth/Thickness/Height/3rd Dimension, in inches	6
//		    348y	Container Depth/Thickness/Height/3rd Dimension, in feet	6
//		    349y	Container Depth/Thickness/Height/3rd Dimension, in yards	6
//		    350y	Product Area (Square Inches)	6
//		    351y	Product Area (Square Feet)	6
//		    352y	Product Area (Square Yards)	6
//		    353y	Container Area (Square Inches)	6
//		    354y	Container Area (Square Feet)	6
//		    355y	Container Area (Square Yards)	6
//		    356y	Net Weight (Troy Ounces)	6
//		    357y	Net Weight/Volume (Ounces)	6
//		    360y	Product Volume (Quarts)	6
//		    361y	Product Volume (Gallons)	6
//		    362y	Container Gross Volume (Quarts)	6
//		    363y	Container Gross Volume (U.S. Gallons)	6
//		    364y	Product Volume (Cubic Inches)	6
//		    365y	Product Volume (Cubic Feet)	6
//		    366y	Product Volume (Cubic Yards)	6
//		    367y	Container Gross Volume (Cubic Inches)	6
//		    368y	Container Gross Volume (Cubic Feet)	6
//		    369y	Container Gross Volume (Cubic Yards)	6
//		    37	Number of Units Contained	variable, up to 8
//		    390y	Amount payable (local currency)	variable, up to 15
//		    391y	Amount payable (with ISO currency code)	variable, 3–18
//		    392y	Amount payable per single item (local currency)	variable, up to 15
//		    393y	Amount payable per single item (with ISO currency code)	variable, 3–18
//		    400	Customer Purchase Order Number	variable, up to 30
//		    401	Consignment Number	variable, up to 30
//		    402	Bill of Lading number	17
//		    403	Routing code	variable, up to 30
//		    410	Ship To/Deliver To Location Code (Global Location Number)	13
//		    411	Bill To/Invoice Location Code (Global Location Number)	13
//		    412	Purchase From Location Code (Global Location Number)	13
//		    413	Ship for, Deliver for, or Forward to Location Code (Global Location Number)	13
//		    414	Identification of a physical location (Global Location Number)	13
//		    420	Ship To/Deliver To Postal Code (Single Postal Authority)	variable, up to 20
//		    421	Ship To/Deliver To Postal Code (with ISO country code)	variable, 3–15
//		    422	Country of Origin (ISO country code)	3
//		    423	Country or countries of initial processing	variable, 3–15
//		    424	Country of processing	3
//		    425	Country of disassembly	3
//		    426	Country of full process chain	3
//		    7001	NATO Stock Number (NSN)	13
//		    7002	UN/ECE Meat Carcasses and cuts classification	variable, up to 30
//		    7003	expiration date and time	10
//		    7004	Active Potency	variable, up to 4
//		    703n	Processor approval (with ISO country code); n indicates sequence number of several processors	variable, 3–30
//		    8001	Roll Products: Width/Length/Core Diameter/Direction/Splices	14
//		    8002	Mobile phone identifier	variable, up to 20
//		    8003	Global Returnable Asset Identifier	variable, 14–30
//		    8004	Global Individual Asset Identifier	variable, up to 30
//		    8005	Price per Unit of Measure	6
//		    8006	identification of the components of an item	18
//		    8007	International Bank Account Number	variable, up to 30
//		    8008	Date/time of production	variable, 8–12
//		    8018	Global Service Relation Number	18
//		    8020	Payment slip reference number	variable, up to 25
//		    8100	Coupon Extended Code: Number System and Offer	6
//		    8101	Coupon Extended Code: Number System, Offer, End of Offer	10
//		    8102	Coupon Extended Code: Number System preceded by 0	2
//		    8110	Coupon code ID (North America)	variable, up to 30
//		    8200	Extended Packaging URL	variable, up to 70
//		    90	Mutually Agreed Between Trading Partners	variable, up to 30
//		    91–99	Internal Company Codes	variable, up to 30		    
		    // TODO: continue according to http://en.wikipedia.org/wiki/GS1-128
		  }
	public CodeBarreExchange() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	   * Decodes a Unicode string from a Code128-like encoding.
	   *
	   * @param fnc1 The character that represents FNC1.
	   */
	public CodeBarreExchange(String s, char fnc1) {
		StringBuilder ai = new StringBuilder();
		int index = 0;
		while (index < s.length()) {
			ai.append(s.charAt(index++));
			AII info = aiinfo.get(ai.toString());
			if (info != null) {
				StringBuilder value = new StringBuilder();
				for (int i = 0; i < info.maxLength && index < s.length(); i++) {
					char c = s.charAt(index++);
					if (c == fnc1) {
						break;
					}
					value.append(c);
				}
				if (value.length() < info.minLength) {
					throw new IllegalArgumentException("Short field for AI \"" + ai + "\": \"" + value + "\".");
				}
				mapData.put(ai.toString(), value.toString());
				ai.setLength(0);
			}
		}
		if (ai.length() > 0) {
			throw new IllegalArgumentException("Unknown AI \"" + ai + "\".");
		}
	}
	  

//	  private static DateMidnight asDate(String s) {
//	    if (s == null) {
//	      return null;
//	    }
//	    String century = s.compareTo("500000") < 0 ? "20" : "19";
//	    return new DateMidnight(century + s);
//	  }

//	  public DateMidnight getDueDate() {
//	    return asDate(mapData.get("12"));
//	  }
	
	  
	public Map<String, String> litCodeBarre(String codeBarre){
		Map<String, String> retour=new LinkedHashMap<String, String>();
		
		
		return retour;
	}
	
	public String  EcritCodeBarre( Map<String, String> liste){
		String retour="";
		
		
		return retour;
	}	
}
