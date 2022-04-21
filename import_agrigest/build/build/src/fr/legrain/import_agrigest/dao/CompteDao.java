package fr.legrain.import_agrigest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;

import fr.legrain.import_agrigest.model.Compte;

public class CompteDao extends AbstractDAO {

	

	public String ajout_Compte(String keepDossier , String cpt, String libCompte) throws SQLException {
		return ajout_Compte(keepDossier, cpt, libCompte, "", "", false);
	}
	public String ajout_Compte(String keepDossier , String cpt, String libCompte, String libU1, String libU2 , Boolean Enregistrement) throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {
		Compte compte=new Compte();
		cpt=cpt.replace("+","");
		compte.setcCpt(StringUtils.rightPad(cpt, 8, "0"));
		compte.setcDossier(keepDossier);
		if(libCompte.length()>25) compte.setcLib(libCompte.substring(0, 25));
		else compte.setcLib(libCompte);
		if(libU1.length()>2) compte.setcU1(libU1.substring(0, 2));
		else compte.setcU1(libU1);
		if(libU2.length()>2) compte.setcU2(libU2.substring(0, 2));
		else compte.setcU2(libU2);

		String requete ="select * from Comptes where CDossier=? And CCpt=?"; 
		st = sqlServer.prepareStatement(requete);
		st.setString(1, compte.getcDossier());
		st.setString(2, compte.getcCpt());
		rs=st.executeQuery();

		if(!rs.next()) {
				requete="insert into Comptes(cDossier,cCpt,cLib,cU1,cU2)"
						+ " values(?,?,?,?,?)";
				ps = sqlServer.prepareStatement(requete);

				ps.setString(1,compte.getcDossier());
				ps.setString(2,compte.getcCpt());
				ps.setString(3,compte.getcLib());
				ps.setObject(4,compte.getcU1(), Types.VARCHAR);
				ps.setString(5,compte.getcU2());	 

				ps.execute();

				if(Enregistrement) {
//					getSqlServer().connection().commit();
				}	

				return "Le compte a été correctement inséré.";
			
		}
		else
			return "Ce compte est déjà présent en comptabilité.";
	} finally {
		if(rs!=null)rs.close();
		if(ps!=null)ps.close();
		if(st!=null)st.close();
	}
	}
	
}
