package fr.legrain.bdg.webapp.primefaces;

import org.primefaces.component.datatable.DataTable;

public class LgrDataTable extends DataTable { 
    @Override 
    public String getPaginatorPosition() { 
        return "bottom"; 
    } 
}