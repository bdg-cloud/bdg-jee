package fr.legrain.import_agrigest.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;

import fr.legrain.import_agrigest.model.PlanComptable;

public class PlanComptableDao extends AbstractDAO {

	public String ajout_PlanComptable(String keepDossier, String compte, String activite )  throws SQLException {
		return ajout_PlanComptable(keepDossier, compte, activite, false);
	}
	
	public String ajout_PlanComptable(String keepDossier, String compte, String activite , Boolean enregistrement)  throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {

		try {

		PlanComptable plCompte = new PlanComptable();
			plCompte.setpLDossier(keepDossier);
			compte=compte.replace("+","");
			plCompte.setpLCpt(StringUtils.rightPad(compte, 8, "0"));
			plCompte.setpLActi(StringUtils.rightPad(activite, 4, "0"));
			plCompte.setpLRepA_C(BigDecimal.ZERO);
			plCompte.setpLRepA_D(BigDecimal.ZERO);
			plCompte.setpLRepA_U1(BigDecimal.ZERO);
			plCompte.setpLRepA_U2(BigDecimal.ZERO);
			plCompte.setpLRepG_C(BigDecimal.ZERO);
			plCompte.setpLRepG_D(BigDecimal.ZERO);
			plCompte.setpLRepG_U1(BigDecimal.ZERO);
			plCompte.setpLRepG_U2(BigDecimal.ZERO);
			plCompte.setpLSoldeA_C(BigDecimal.ZERO);
			plCompte.setpLSoldeA_D(BigDecimal.ZERO);
			plCompte.setpLSoldeA_U1(BigDecimal.ZERO);
			plCompte.setpLSoldeA_U2(BigDecimal.ZERO);
			plCompte.setpLSoldeG_C(BigDecimal.ZERO);
			plCompte.setpLSoldeG_D(BigDecimal.ZERO);
			plCompte.setpLSoldeG_U1(BigDecimal.ZERO);
			plCompte.setpLSoldeG_U2(BigDecimal.ZERO);
		
   
	String requete ="select * from PlanComptable where PlDossier=? And PlCpt=?  And PlActi=?"; 
	ps = sqlServer.prepareStatement(requete);
	ps.setString(1, plCompte.getpLDossier());
	ps.setString(2, plCompte.getpLCpt());
	ps.setString(3,plCompte.getpLActi());
	rs=ps.executeQuery();

	if(!rs.next()) {

			requete="insert into PlanComptable(pLDossier,pLCpt,pLActi,pLRepG_C,pLRepG_D,pLRepG_U1,pLRepG_U2,pLRepA_C,pLRepA_D,pLRepA_U1,pLRepA_U2,"
					+ "pLSoldeG_C,pLSoldeG_D,pLSoldeG_U1,pLSoldeG_U2,pLSoldeA_C,pLSoldeA_D,pLSoldeA_U1,pLSoldeA_U2)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
//		requete="insert into PlanComptable(pLDossier,pLCpt,pLActi)"
//				+ " values(?,?,?)";
		
			ps = sqlServer.prepareStatement(requete);

			
			ps.setObject(1,plCompte.getpLDossier(), Types.VARCHAR);
			ps.setObject(2,plCompte.getpLCpt(), Types.VARCHAR);
			ps.setObject(3,plCompte.getpLActi(), Types.VARCHAR);
			ps.setObject(4,plCompte.getpLRepG_C(), Types.NUMERIC);
			ps.setObject(5,plCompte.getpLRepG_D(), Types.NUMERIC);
			ps.setObject(6,plCompte.getpLRepG_U1(), Types.NUMERIC);
			ps.setObject(7,plCompte.getpLRepG_U2(), Types.NUMERIC);
			ps.setObject(8,plCompte.getpLRepA_C(), Types.NUMERIC);
			ps.setObject(9,plCompte.getpLRepA_D(), Types.NUMERIC);
			ps.setObject(10,plCompte.getpLRepA_U1(), Types.NUMERIC);
			ps.setObject(11,plCompte.getpLRepA_U2(), Types.NUMERIC);
			ps.setObject(12,plCompte.getpLSoldeG_C(), Types.NUMERIC);
			ps.setObject(13,plCompte.getpLSoldeG_D(), Types.NUMERIC);
			ps.setObject(14,plCompte.getpLSoldeG_U1(), Types.NUMERIC);
			ps.setObject(15,plCompte.getpLSoldeG_U2(), Types.NUMERIC);
			ps.setObject(16,plCompte.getpLSoldeA_C(), Types.NUMERIC);
			ps.setObject(17,plCompte.getpLSoldeA_D(), Types.NUMERIC);
			ps.setObject(18,plCompte.getpLSoldeA_U1(), Types.NUMERIC);
			ps.setObject(19,plCompte.getpLSoldeA_U2(), Types.NUMERIC);
			
			if(!ps.execute()) {
				System.err.println("problème ");
			};


			if(enregistrement) {
//				getSqlServer().connection().commit();
			}
			return "Le compte a été correctement inséré.";

	}
	else {
		return "Ce compte est déjà présent en comptabilité.";
	}
	
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

