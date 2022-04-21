package fr.legrain.import_agrigest.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fr.legrain.import_agrigest.model.Mouvements;

public class MouvementDao extends AbstractDAO {




	public void ajout_Mouvement(String keepDossier ,Integer nPiece , Date datePiece, Integer ligne, Integer nOrdre, String nCompte, String nActi, BigDecimal mtDeb, 
			BigDecimal mtCre, String D_C, BigDecimal qte1 , BigDecimal qte2 , String nCompteCtr , String nActiCtr )  throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {

		nCompte=nCompte.replace("+","");
		nCompte = StringUtils.rightPad(nCompte, 8, "0");
		nActi = StringUtils.rightPad(nActi, 4, "0");
		nCompteCtr=nCompteCtr.replace("+","");
		nCompteCtr = StringUtils.rightPad(nCompteCtr, 8, "0");
		nActiCtr = StringUtils.rightPad(nActiCtr, 4, "0");

		Mouvements mouv = new Mouvements();
		mouv.setmDossier(keepDossier);
		mouv.setmPiece(nPiece);
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");       		 
			String retour = dateFormat.format(datePiece);
			mouv.setmDate(new Date(dateFormat.parse(retour).getTime()));
//			mouv.setmEcheance(new Date(dateFormat.parse(retour).getTime()));
			mouv.setmEcheance(null);
			
			mouv.setmLig(ligne);
			mouv.setmOrdre(nOrdre);
			mouv.setmCpt(nCompte);
			mouv.setmActi(nActi);
			mouv.setmMtDeb(mtDeb);
			mouv.setmMtCre(mtCre);
			mouv.setmD_C(D_C);
			mouv.setmQte1(qte1);
			mouv.setmQte2(qte2);
			mouv.setmLettrage(0);
			mouv.setmCouleur(null);
			mouv.setmCptCtr(nCompteCtr);
			mouv.setmActCtr(nActiCtr);
			mouv.setmFolio(null);

			//    If dsAgrigest.Tables("Mouvements").Select("MDossier='" & keepDossier & "' And MPiece='" & nPiece & "' And MDate=#" & datePiece.ToString("MM/dd/yy") & "# And MLig='" & Ligne & "' And MOrdre=" & NOrdre & "").Length = 0 Then
			String requete ="select * from Mouvements where mDossier=? And MPiece=?  And MDate=?  And MLig=?  And MOrdre=?"; 
			st = sqlServer.prepareStatement(requete);
			st.setString(1, mouv.getmDossier());
			st.setInt(2, mouv.getmPiece());
			st.setDate(3,new java.sql.Date(mouv.getmDate().getTime()));
			st.setInt(4, mouv.getmLig());
			st.setInt(5, mouv.getmOrdre());
			rs=st.executeQuery();

			if(!rs.next()) {
//				requete="insert into Mouvements(mDossier,mPiece,mDate,mLig,mOrdre,mCpt,mActi,mMtDeb,mMtCre,mD_C,mQte1,mQte2,mLettrage,mEcheance,mFolio,"
//						+ "mCptCtr,mActCtr,mCouleur,mAN)"
//						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				requete="insert into Mouvements(mDossier,mPiece,mDate,mLig,mOrdre,mCpt,mActi,mMtDeb,mMtCre,mD_C,mQte1,mQte2,mLettrage,mFolio,"
						+ "mCptCtr,mActCtr)"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps = sqlServer.prepareStatement(requete);

				//		private String mDossier; //8
				//		private Integer mPiece; //4
				//		private Date mDate; //8
				//		private Integer mLig; //4
				//		private Integer mOrdre; //1 Octet
				//		private String mCpt; //8
				//		private String mActi; //4
				//		private BigDecimal mMtDeb; //8
				//		private BigDecimal mMtCre; //8
				//		private String mD_C; //1
				//		private BigDecimal mQte1; //8
				//		private BigDecimal mQte2; //8
				//		private Integer mLettrage; //4
				//		private Date mEcheance; //8
				//		private Integer mFolio; //2
				//		private String mCptCtr; //8
				//		private String mActCtr; //4
				//		private Integer mCouleur; //4
				//		private Boolean mAN; //1

				ps.setObject(1,mouv.getmDossier(), Types.VARCHAR);
				ps.setObject(2,mouv.getmPiece(), Types.INTEGER);
				ps.setObject(3,new java.sql.Date(mouv.getmDate().getTime()), Types.DATE);
				ps.setObject(4,mouv.getmLig(), Types.INTEGER);
				ps.setObject(5,mouv.getmOrdre(), Types.INTEGER);
				ps.setObject(6,mouv.getmCpt(), Types.VARCHAR);
				ps.setObject(7,mouv.getmActi(), Types.VARCHAR);
				ps.setObject(8,mouv.getmMtDeb(), Types.NUMERIC);
				ps.setObject(9,mouv.getmMtCre(), Types.NUMERIC);
				ps.setObject(10,mouv.getmD_C(), Types.VARCHAR);
				ps.setObject(11,mouv.getmQte1(), Types.NUMERIC);
				ps.setObject(12,mouv.getmQte2(), Types.NUMERIC);
				ps.setObject(13,mouv.getmLettrage(), Types.INTEGER);
//				ps.setObject(14,new java.sql.Date(mouv.getmEcheance().getTime()), Types.DATE);
				ps.setObject(14,mouv.getmFolio(), Types.INTEGER);
				ps.setObject(15,mouv.getmCptCtr(), Types.VARCHAR);
				ps.setObject(16,mouv.getmActCtr(), Types.VARCHAR);
//				ps.setObject(18,mouv.getmCouleur(), Types.INTEGER);
//				ps.setObject(19,mouv.getmAN(), Types.BOOLEAN);

				ps.execute();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new SQLException(e);
		}
	} finally {
		if(rs!=null)rs.close();
		if(ps!=null)ps.close();
		if(st!=null)st.close();
	}
	}
}

