package fr.legrain.import_agrigest.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.legrain.import_agrigest.model.Tva;

public class TvaDao extends AbstractDAO {


	
	//par défaut, enregistrement est à false
	public Tva findByCode(String code) throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {

		String requete ="select * from Tva where TTVA=?"; 
		st = sqlServer.prepareStatementApplication(requete);
		st.setString(1, code);
		rs=st.executeQuery();
		Tva retour=null;
		if(rs.next()) {
				Tva tva = new Tva();
				tva.setLibelle(rs.getString("Libellé"));
				tva.settColonne(rs.getInt("TColonne"));
				tva.settCpt(rs.getString("TCpt"));
				tva.settCtrlCl_DC(rs.getString("TCtrlCl_DC"));
				tva.settCtrlCl_Num(rs.getString("TCtrlCl_Num"));
				tva.settCtrlMt(rs.getString("TCtrlMt"));
				tva.settJournal(rs.getString("TJournal"));
				tva.settLigne(rs.getInt("TLigne"));
				tva.settMtTVA(rs.getString("TMtTVA"));
				tva.settTaux(rs.getBigDecimal("TTaux"));
				tva.settTVA(rs.getString("TTVA"));
				tva.setTypTVA(rs.getString("TypTVA"));
				retour= tva;

		}
		return retour;
		} finally {
			if(rs!=null)rs.close();
			if(ps!=null)ps.close();
			if(st!=null)st.close();
		}
		}
	
	//par défaut, enregistrement est à false
	public Tva findByTaux(BigDecimal taux) throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {

		String requete ="select * from Tva where TTVA=?"; 
		st = sqlServer.prepareStatementApplication(requete);
		st.setBigDecimal(1, taux);
		rs=st.executeQuery();
		Tva retour=null;
		if(rs.next()) {
				Tva tva = new Tva();
				tva.setLibelle(rs.getString("Libellé"));
				tva.settColonne(rs.getInt("TColonne"));
				tva.settCpt(rs.getString("TCpt"));
				tva.settCtrlCl_DC(rs.getString("TCtrlCl_DC"));
				tva.settCtrlCl_Num(rs.getString("TCtrlCl_Num"));
				tva.settCtrlMt(rs.getString("TCtrlMt"));
				tva.settJournal(rs.getString("TJournal"));
				tva.settLigne(rs.getInt("TLigne"));
				tva.settMtTVA(rs.getString("TMtTVA"));
				tva.settTaux(rs.getBigDecimal("TTaux"));
				tva.settTVA(rs.getString("TTVA"));
				tva.setTypTVA(rs.getString("TypTVA"));
				retour= tva;

		}
		return retour;
		} finally {
			if(rs!=null)rs.close();
			if(ps!=null)ps.close();
			if(st!=null)st.close();
		}
		}	
}
