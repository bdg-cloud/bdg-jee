package fr.legrain.import_agrigest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DossierDao extends AbstractDAO {

	public String trouverDossier(Date dateDebutExport, Date dateFinExport , Date dateItem )  throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {
			String numDossier = null;
			//    For Each rwDossier As DataRow In dsAgrigest.Tables("Dossiers").Select("", "DDossier")
			//        If rwDossier.Item("DDtFinEx") >= dateDebutExport _
			//        AndAlso rwDossier.Item("DDtDebEx") <= dateFinExport _
			//        AndAlso rwDossier.Item("DDtFinEx") >= dateItem _
			//        AndAlso rwDossier.Item("DDtDebEx") <= dateItem Then
			//            numDossier = rwDossier.Item("DDossier")
			//            Exit For
			//        End If
			//    Next
			String requete ="select * from Dossiers where DDtFinEx >=? And DDtDebEx <=? And DDtFinEx >=? And DDtDebEx <=?"; 
			st = sqlServer.prepareStatement(requete);
			st.setDate(1, new java.sql.Date(dateDebutExport.getTime()));
			st.setDate(2,new java.sql.Date(dateFinExport.getTime()));
			st.setDate(3, new java.sql.Date(dateItem.getTime()));
			st.setDate(4,new java.sql.Date(dateItem.getTime()));
			rs=st.executeQuery();

			if(!rs.next()) {
				numDossier=rs.getString("DDossier");
			}
			return numDossier;
		} finally {
			if(rs!=null)rs.close();
			if(ps!=null)ps.close();
			if(st!=null)st.close();
		}
	}
	

	public String trouverDossier(Date dateItem )  throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {
			try {		
		String numDossier = null;
		String requete ="select * from Dossiers where DDtFinEx >=? And DDtDebEx <=?";

		st = sqlServer.prepareStatement(requete);
		st.setDate(1, new java.sql.Date(dateItem.getTime()));
		st.setDate(2,new java.sql.Date(dateItem.getTime()));
		rs=st.executeQuery();

		if(rs.next()) {
//			numDossier=rs.getString(1);
			numDossier=rs.getString("DDossier");
		}
		return numDossier;
		

		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e);
		}
	} finally {
		if(rs!=null)rs.close();
		if(ps!=null)ps.close();
		if(st!=null)st.close();
	}
}

	
	
	
}
