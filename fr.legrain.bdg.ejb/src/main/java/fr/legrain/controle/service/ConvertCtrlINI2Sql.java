package fr.legrain.controle.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ConvertCtrlINI2Sql {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader br;
		BufferedWriter writer;
		
		//INSERT INTO TA_CONTROLE (CHAMP, CONTEXTE, CLIENT, SERVEUR, CONTROLE_DEFAUT) VALUES ('TaTCiviliteDTO.idTCivilite', NULL, NULL, NULL, '100');
		
		String debut = "INSERT INTO TA_CONTROLE (CHAMP, CONTEXTE, CLIENT, SERVEUR, CONTROLE_DEFAUT) VALUES ('";
	   
		try {
			
//			br = new BufferedReader(new FileReader("/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_10a_E4/GestionCommerciale/Bd/CtrlBD.ini"));
//			writer = new BufferedWriter(new FileWriter("/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_10a_E4/fr.legrain.bdg.ejb/src/main/java/fr/legrain/controle/service/a.txt"));
			br = new BufferedReader(new FileReader("/home/nicolas/Bureau/aa.txt"));
			writer = new BufferedWriter(new FileWriter("/home/nicolas/Bureau/bb.txt"));

			String line;
			String contexte;
			String champ;
			String ctrl;
			String sql;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if(line.indexOf("DTO")!=-1) {
					//DTO on ignore
					if(line.indexOf('.')!=-1 && line.indexOf('=')!=-1) {
						//contexte = line.substring(0,line.indexOf('.'));
						champ = line.substring(0,line.indexOf('='));
						ctrl = line.substring(line.indexOf('=')+1);
						sql = debut+champ+"', NULL, NULL, NULL, '"+ctrl+"');";
						if(champ.indexOf(".")!=-1) {
							writer.write (sql+"\n");
						}
					} else {
						writer.write ("==================================> "+line+"\n");
					}
					
				}
				else if(line.trim().length()==0 || line.indexOf("#")!=-1) {
					//ligne vide ou commentaire
					//writer.write (line);
					
				} else {
					
					//if(Character.isUpperCase(line.charAt(0)) ) {
						if(line.indexOf('.')!=-1 && line.indexOf('=')!=-1) {
							contexte = line.substring(0,line.indexOf('.'));
							champ = line.substring(contexte.length()+1,line.indexOf('='));
							ctrl = line.substring(line.indexOf('=')+1);
							sql = debut+champ+"','"+contexte+"', NULL, NULL, '"+ctrl+"');";
							if(champ.indexOf(".")!=-1) {
								writer.write (sql+"\n");
							}
						} else {
							writer.write ("==================================> "+line+"\n");
						}
				}
				//writer.write (line);
			}
			br.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
