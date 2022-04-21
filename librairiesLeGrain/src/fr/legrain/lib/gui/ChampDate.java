package fr.legrain.lib.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import fr.legrain.lib.data.LibDate;

/**
 * @todo FINIR
 * <p>Title: Champ formaté pour la saisie de date.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Le Grain SA</p>
 * @author nicolas
 * @version 1.0
 */

public class ChampDate extends JFormattedTextField {

  private static final long serialVersionUID = 1229174798351399431L;
	
  private static String formatDate = "dd/MM/yyyy";
  private String masqueDate = "##/##/####";
  private char marquePlaceDate = '_';
  DateFormat format = new SimpleDateFormat(formatDate);
//  private final DateFormatter df = new DateFormatter(new SimpleDateFormat(formatDate));

  public ChampDate() {
    //super(new Date());
    //super(new SimpleDateFormat(formatDate));
  // super(new DateFormatter(new SimpleDateFormat(formatDate)));

    try {
      javax.swing.text.MaskFormatter dateMask = new javax.swing.text.MaskFormatter(masqueDate);
      dateMask.setPlaceholderCharacter(marquePlaceDate);
      dateMask.install(this);

      // DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
//      
      //DateFormatter df = new DateFormatter(format);
     // df.setValueClass(Date.class);
    //  setFormatter(df);
      /////////
      //DateFormatter df = new DateFormatter(new SimpleDateFormat("dd.MM.yyyy"));
      //df.setAllowsInvalid(true);
      //df.setOverwriteMode(true);

      //this.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat(formatDate))));
      //this.setFormatterFactory(new DefaultFormatterFactory(df));
      //this.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(DateFormat.getDateInstance(DateFormat.SHORT)))); // 12/31/2000
      //System.out.println(format.format(new Date()));
      setInputVerifier(new InputVerifier() {
        public boolean verify(JComponent input) {    
          try {
        	//jftf.setValue(LibDate.stringToDate(text));  
        	  ((JFormattedTextField) input).
        	    setValue(format.format(LibDate.stringToDate(((JFormattedTextField) input).getText())));
        	//jftf.setValue(df.valueToString(df.stringToValue(text)));
            return true;
          }
          catch (ParseException e) {
            return false;
          }
        }
      });

    /////////
     // setValue(new Date());
     // dateMask.install(this);
      System.out.println(new Date().toString());

    }
    catch (ParseException ex) {
      ex.printStackTrace();
    }
  }

  protected void invalidEdit() {
	  MessageDialog.openWarning(PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getShell(), "ATTENTION", "Date incorrecte");
    //JOptionPane.showMessageDialog(this,"Date incorrecte");
  }

}
