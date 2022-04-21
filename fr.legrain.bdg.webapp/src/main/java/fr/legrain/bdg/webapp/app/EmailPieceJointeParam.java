package fr.legrain.bdg.webapp.app;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;

import javax.activation.MimetypesFileTypeMap;

public class EmailPieceJointeParam implements Serializable {
	private File fichier;
	private String typeMime;
	private String nomFichier;
	
	private final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();  
	
	public File getFichier() {
		return fichier;
	}
	public void setFichier(File fichier) {
		this.fichier = fichier;
	}
	public String getTypeMime() {
		return typeMime;
	}
	public void setTypeMime(String typeMime) {
		this.typeMime = typeMime;
	}
	public String getNomFichier() {
		return nomFichier;
	}
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	
	public long calculeTailleKo() {
		return fichier.length()/1024;
	}
	
	public long calculeTailleMo() {
		return fichier.length()/2048;
	}
	
	public String iconFileType() {
		
		String mimeIcon = "fa fa-file-o";
		String mime = null;
		try {
			mime = Files.probeContentType(fichier.toPath());
			if(mime==null || mime.equals("")) {
				fileTypeMap.getContentType(fichier);
			}
			if(mime!=null && !mime.equals("")) {
				if(mime.contains("pdf")) {
					mimeIcon = "fa fa-file-pdf-o";
				} else if(mime.contains("image")) {
					mimeIcon = "fa fa-file-image-o";
				} else if(mime.contains("zip")) {
					mimeIcon = "fa fa-file-archive-o";
				} else if(mime.contains("text")) {
					mimeIcon = "fa fa-file-text-o";
				} else if(mime.contains("excel")) {
					mimeIcon = "fa fa-file-excel-o";
				} else if(mime.contains("fa fa-file-word-o")) {
					mimeIcon = "fa fa-file-word-o";
				} else if(mime.contains("audio")) {
					mimeIcon = "fa fa-file-audio-o";
				} else if(mime.contains("video")) {
					mimeIcon = "fa fa-file-video-o";
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return mimeIcon;
	}
}
