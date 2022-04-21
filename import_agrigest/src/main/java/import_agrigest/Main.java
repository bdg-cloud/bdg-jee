package import_agrigest;



import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

import com.mashape.unirest.http.exceptions.UnirestException;

import fr.legrain.import_agrigest.controller.ImportationController;
import fr.legrain.import_agrigest.dao.ActiviteDao;
import fr.legrain.import_agrigest.model.ParametreImport;
import fr.legrain.import_agrigest.sqlserver.util.SQLServerUtil;
import fr.legrain.lib.data.LibConversion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 * Controller de démarrage qui  lance le start, gère le stop de l'application.
 * @author yann
 *
 */





public class Main extends Application {
	

		ImportationController importationController;
	

	    @FXML
	    private Button btnParcourrirApplication;
		@FXML
		private Button btnParcourrirDossier;
		@FXML
		private Button btnParcourrirFichier;
		@FXML
		private Button btnImporter;
		@FXML
		private Button btnFermer;
		@FXML
		private TextField tfBaseApplication; 
		@FXML
		private TextField tfBaseDossier; 
		@FXML
		private TextField tfFichier;
		
		
		@FXML
		private void handleParcourrirDossier() {
			final FileChooser dialog = new FileChooser(); 
			dialog.setInitialDirectory(new File(SQLServerUtil.params.getCheminAccesBase()).getParentFile());
		    final File file = dialog.showOpenDialog(btnParcourrirDossier.getScene().getWindow()); 
		    if (file != null) { 
		    	tfBaseDossier.setText(file.getPath());
		    	SQLServerUtil.params.setCheminAccesBase(tfBaseDossier.getText());
		    } 
		}

		@FXML
		private void handleParcourrirFichier() {
			final FileChooser dialog = new FileChooser(); 
			dialog.setInitialDirectory(new File(SQLServerUtil.params.getCheminFichierExport()).getParentFile());
		    final File file = dialog.showOpenDialog(btnParcourrirFichier.getScene().getWindow()); 
		    if (file != null) { 
		    	tfFichier.setText(file.getPath());
		    	SQLServerUtil.params.setCheminFichierExport(tfFichier.getText());
		    }
		}
		@FXML
		private void handleParcourrirApplication(){
			final FileChooser dialog = new FileChooser(); 
			dialog.setInitialDirectory(new File(sql.params.getCheminAccesBasePlanType()).getParentFile());
		    final File file = dialog.showOpenDialog(btnParcourrirApplication.getScene().getWindow()); 
		    if (file != null) { 
		    	tfBaseApplication.setText(file.getPath());
		    	SQLServerUtil.params.setCheminAccesBasePlanType(tfBaseApplication.getText());
		    }
		}
		
		@FXML
		private void handleImporter() {
			SQLServerUtil.params.setCheminAccesBase(tfBaseDossier.getText());
			SQLServerUtil.params.setCheminAccesBasePlanType(tfBaseApplication.getText());
			SQLServerUtil.params.setCheminFichierExport(tfFichier.getText());
			sql.connection(true);
			importationController.importer(SQLServerUtil.params);
		}
		@FXML
		private void handleFermer() {
			ecritParametreAvantFermeture(SQLServerUtil.params);
			try {
				stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		Stage primaryStage;
		SQLServerUtil sql=new SQLServerUtil();
	/**
	 * Méthode qui surcharge le stop de l'application, qui ferme le programme et arrète le processus timer.
	 */
	@Override
	public void stop() throws Exception {
		sql.closeConnection();
		super.stop();
		Platform.exit();
		
	}
	
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
		if(SQLServerUtil.params!=null) {
			tfBaseApplication.setText(SQLServerUtil.params.getCheminAccesBasePlanType());
			tfBaseDossier.setText(SQLServerUtil.params.getCheminAccesBase());
			tfFichier.setText(SQLServerUtil.params.getCheminFichierExport());
		}
		importationController=new ImportationController();
    }

    
	/**
	 * Méthode qui srucharge le start de l'application et qui lance un timer 
	 */

	@Override
	public void start(Stage pStage) throws UnirestException {
		try {
			
//			sql.setBdDossier(bdDossier);
			sql.init();
			
			primaryStage=pStage;
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,800,400);
//			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			primaryStage.setTitle("Importation dans Agrigest !!!");
			primaryStage.setScene(scene);
			primaryStage.show();
			//lecture d'un fichier xml pour récupérer les préférences du dossier, chemin du fichier d'import, etc...
			
			


//			ActiviteDao activiteDao = new ActiviteDao();
//			activiteDao.ajout_Activite("A0000018", "Isa", "Activité isabelle ........................", true);

		
		} catch(Exception e) {
			e.printStackTrace();
		}

		
		
	}
	
	private void ecritParametreAvantFermeture(ParametreImport params) {
//		Parametre params = sql.getInstance();
//		params.setCheminAccesBase(tfBaseDossier.getText());
//		params.setCheminAccesBasePlanType(tfBaseApplication.getText());
//		params.setCheminFichierExport(tfFichier.getText());
		
//		params.setCheminAccesBase("base dossier");
//		params.setCheminAccesBasePlanType("base plan");
//		params.setCheminFichierExport("fichier");
//		params.setDossier("dossier");
		if(params==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("paramètre null");
			alert.setHeaderText(null);
			alert.setContentText(SQLServerUtil.fichierParametreXml);
			alert.showAndWait();
		}
		

		sql =new SQLServerUtil();
		sql.ecritParametres(params);
	}
		
	public static void main(String[] args) {
		launch(args);
	}

	public ImportationController getImportationController() {
		return importationController;
	}

	public void setImportationController(ImportationController importationController) {
		this.importationController = importationController;
	}
	
	
	
	
	
}
		



