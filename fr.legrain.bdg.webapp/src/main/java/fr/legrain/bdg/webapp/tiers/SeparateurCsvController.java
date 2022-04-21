package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.export.CSVOptions;
import org.primefaces.component.export.ExporterOptions;
import org.primefaces.util.ComponentUtils;

@Named
@ViewScoped  
public class SeparateurCsvController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7717000594547209993L;

	public SeparateurCsvController() {
		
	}
	
	public ExporterOptions getCsvOptions() {
//        return new CSVOptions('"', ';', "\n");
       // is equivalent to 
       return CSVOptions.EXCEL_NORTHERN_EUROPE;
//        
   }
	
	public void formatExcel(Object doc) {
	    HSSFWorkbook book = (HSSFWorkbook)doc;
	    HSSFSheet sheet = book.getSheetAt(0); 

	    HSSFRow header = sheet.getRow(0);
	    int colCount = header.getPhysicalNumberOfCells();
	    int rowCount = sheet.getPhysicalNumberOfRows();

	    HSSFCellStyle intStyle = book.createCellStyle();
	    intStyle.setDataFormat((short)1);

	    HSSFCellStyle decStyle = book.createCellStyle();
	    decStyle.setDataFormat((short)2);

	    HSSFCellStyle euroStyle = book.createCellStyle();
	    euroStyle.setDataFormat((short)5);


	    for(int rowInd = 1; rowInd < rowCount; rowInd++) {
	        HSSFRow row = sheet.getRow(rowInd);
	        for(int cellInd = 1; cellInd < colCount; cellInd++) {
	            HSSFCell cell = row.getCell(cellInd);

	            //This is sortof a hack to counter PF exporting all data as text
	            //We capture the existing value as string, convert to int, 
	            //then format the cell to be numeric and reset the value to be int
	            String strVal = cell.getStringCellValue();

	            //this has to be done to temporarily blank out the cell value
	            //because setting the type to numeric directly will cause 
	            //an IllegalStateException because POI stupidly thinks
	            //the cell is text because it was exported as such by PF...
	            cell.setCellType(CellType.BLANK);
	            cell.setCellType(CellType.NUMERIC);

	            strVal = strVal.replace(",", StringUtils.EMPTY);

	            if(strVal.indexOf('.') == -1) {
	                //integer
	                //numStyle.setDataFormat((short)1);
	                int intVal = Integer.valueOf(strVal);

	                cell.setCellStyle(intStyle);
	                cell.setCellValue(intVal);
	            } else {
	                //double
	                if(strVal.startsWith("€")) {
	                    strVal = strVal.replace("€", StringUtils.EMPTY);
	                    //numStyle.setDataFormat((short)5);
	                    cell.setCellStyle(euroStyle);
	                } else {
	                    //numStyle.setDataFormat((short)2);
	                    cell.setCellStyle(decStyle);
	                }

	                double dblVal = Double.valueOf(strVal);
	                cell.setCellValue(dblVal);
	            }
	        }
	        
	    }
	}
	
	public void ppMethod(Object document) {   
	    Workbook workbook = (Workbook) document;
	    
	    CellStyle totalCellStyle = workbook.createCellStyle(); 

	    totalCellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

	    Cell currentCell = workbook.getSheetAt(0).getRow(0).getCell(0);


	    currentCell.setCellValue(Double.parseDouble(currentCell.getStringCellValue().replace("'","" )));
	    
	}
	
	public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
 
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellValue(Double.parseDouble(cell.getStringCellValue().replace("'","" )));
                cell.setCellStyle(style);
            }
        }
    }
	
	public void postProcessXLS1(Object document) {

		
    }
	
	public String exportNumber(UIColumn column) {
        String value = "";
        for(UIComponent child: column.getChildren()) {
            if(child instanceof ValueHolder) {          	
                value = ComponentUtils.getValueToRender(FacesContext.getCurrentInstance(), child).replaceAll(" ", "");                             
            }
        }
        
        return value.replaceAll("€","");
	}
	
	public String exportNumber1(UIColumn column) {
		String value = "";
        for(UIComponent child: column.getChildren()) {
            if(child instanceof ValueHolder) {
                value = ComponentUtils.getValueToRender(FacesContext.getCurrentInstance(), child);
                
            }
        }
        		
        return value.replaceAll(",",".");        
	}
}	
