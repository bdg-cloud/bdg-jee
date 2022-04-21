package fr.legrain.import_agrigest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.legrain.import_agrigest.model.Pieces;
import fr.legrain.import_agrigest.model.Tva;

public class PieceDao extends AbstractDAO {


	public Boolean ajout_Piece(String keepDossier, Integer nPiece, Date datePiece)  throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {

		Pieces piece = new Pieces();
		
		piece.setpDossier(keepDossier);
		piece.setpPiece(nPiece);
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");       		 
			String retour = dateFormat.format(datePiece);
			piece.setpDate(new Date(dateFormat.parse(retour).getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new SQLException(e);
		}
		
		

//		Select(String.Format("PDossier='{0}' And PPiece={1} And PDate=#{2:MM/dd/yy}#", KeepDossier, NPiece, DatePiece), "PDossier")
		String requete ="select * from Pieces where PDossier=? And PPiece=?  And PDate=?"; 
		st = sqlServer.prepareStatement(requete);
		st.setString(1, piece.getpDossier());
		st.setInt(2, piece.getpPiece());
		st.setDate(3,new java.sql.Date(piece.getpDate().getTime()));
		rs=st.executeQuery();

		if(!rs.next()) {
			requete="insert into Pieces(pDossier,pPiece,pDate,pAExtourner,pEstExtourne)"
					+ " values(?,?,?,?,?)";
			ps = sqlServer.prepareStatement(requete);

//			private String pDossier; //8
//			private Integer pPiece; //4
//			private Date pDate; //8
//			private Boolean pAExtourner; //1
//			private Boolean pEstExtourne; //1

			ps.setObject(1,piece.getpDossier(), Types.VARCHAR);
			ps.setObject(2,piece.getpPiece(), Types.INTEGER);
			ps.setObject(3,piece.getpDate(), Types.DATE);
			ps.setObject(4,piece.getpAExtourner(), Types.BOOLEAN);
			ps.setObject(5,piece.getpEstExtourne(), Types.BOOLEAN);
			

			ps.execute();

			return true;
		}
		else 
			return false;
//			Throw New WarningExport(String.Format("La pièce {0} du {1:dd/MM/yy} existe déjà.", NPiece, DatePiece))

		} finally {
			if(rs!=null)rs.close();
			if(ps!=null)ps.close();
			if(st!=null)st.close();
		}
		}

	
	//par défaut, enregistrement est à false
	public Integer maxNumPiece(String keepDossier, Integer numPiece, Date datePiece, int debFourchette , int finFourchette) throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {

		Integer retour=null;
		try {
			

			String requete ="select * from Pieces where PDossier=? And PPiece=?  And PDate=?"; 
			st = sqlServer.prepareStatement(requete);
			st.setString(1, keepDossier);
			st.setInt(2, numPiece);
			st.setDate(3,new java.sql.Date(datePiece.getTime()));
			rs=st.executeQuery();

		if(rs.next()) {
//			rs.close();
			requete ="select max(PPiece) from Pieces where PDossier=? And PPiece between ? and ?  And PDate=?";
			st = sqlServer.prepareStatement(requete);
			st.setString(1, keepDossier);
			st.setInt(2, debFourchette);
			st.setInt(3, finFourchette);
			st.setDate(4,new java.sql.Date(datePiece.getTime()));
			rs=st.executeQuery();
			if(rs.next()) {
				retour= rs.getInt(1)+1;
			}
		}else retour= numPiece;
		return retour;
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

