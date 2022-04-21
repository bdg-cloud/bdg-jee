package fr.legrain.import_agrigest.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;

import fr.legrain.import_agrigest.model.Activite;

public class ActiviteDao extends AbstractDAO {

	public String ajout_Activite(String keepDossier , String act , String libActivite  ) throws SQLException {
		return ajout_Activite(keepDossier, act, libActivite, false);
	}
	
	//par défaut, enregistrement est à false
	public String ajout_Activite(String keepDossier , String act , String libActivite , Boolean enregistrement ) throws SQLException {
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {			

		Activite activite = new Activite();
		activite.setaActi(StringUtils.rightPad(act, 4, "0"));
		if(libActivite.length()>20)
			activite.setaLib(libActivite.substring(0, 20));
		else activite.setaLib(libActivite);
		activite.setaDossier(keepDossier);
		activite.setaQte(BigDecimal.ZERO);
		activite.setaDossier(keepDossier);

		String requete ="select * from Activites where ADossier=? And AActi=?"; 
		ps = sqlServer.prepareStatement(requete);
		ps.setString(1, activite.getaDossier());
		ps.setString(2, activite.getaActi());
		rs=ps.executeQuery();

		if(!rs.next()) {
			
				requete="insert into Activites(aDossier,aActi,aLib,aQte,aUnit)"
						+ " values(?,?,?,?,?)";
				ps = sqlServer.prepareStatement(requete);

				ps.setString(1,activite.getaDossier());
				ps.setString(2,activite.getaActi());
				ps.setString(3,activite.getaLib());
				ps.setObject(4,activite.getaQte(), Types.NUMERIC);
				ps.setString(5,activite.getaUnit());	 

				ps.execute();
				if(enregistrement) {
//					getSqlServer().connection().commit();
				}
				return "L'activité a été correctement insérée.";

		}
		else
			return "Cette activité est déjà présente en comptabilité.";
		} finally {
			if(rs!=null)rs.close();
			if(ps!=null)ps.close();
		}
	}
	
	
}
