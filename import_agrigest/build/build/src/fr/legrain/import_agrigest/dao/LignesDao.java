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

import fr.legrain.import_agrigest.model.Lignes;

public class LignesDao extends AbstractDAO {

private MouvementDao mouvDao = new MouvementDao();
	
	public void ajout_Ligne(String keepDossier, Integer nPiece,Date  datePiece  , Integer ligne , Boolean tvaSansCompte, String codeTVA, String journalTVA, 
			String montantTVA , String nomClient , String nomProduit )  throws SQLException {
		PreparedStatement ps=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {

		Lignes lignes = new Lignes();
		lignes.setlDossier(keepDossier);
		lignes.setlPiece(nPiece);


		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");       		 
			String retour = dateFormat.format(datePiece);
			lignes.setlDate(new Date(dateFormat.parse(retour).getTime()));

		lignes.setlLig(ligne);
		if (tvaSansCompte) {
			lignes.setlTva(codeTVA);
			lignes.setlJournal(journalTVA);
			lignes.setlMtTVA(montantTVA);
		}else {
			lignes.setlTva("");
			lignes.setlJournal("O");
			lignes.setlMtTVA("N");
		}
		String libelleLigne="";
		if(nomClient.length()>17)nomClient=nomClient.substring(0, 17);
		if(nomProduit.length()>17)nomProduit=nomProduit.substring(0, 17);
		libelleLigne = nomClient + " " + nomProduit;
		if(libelleLigne.length()>35)libelleLigne=libelleLigne.substring(0, 35);

		lignes.setlLib(libelleLigne);
		lignes.setlGene("O");
		lignes.setlAna("O");



		//    Select("LDossier='" & KeepDossier & "' And LPiece='" & NPiece & "' And LDate=#" & DatePiece.ToString("MM/dd/yy") & "# And LLig='" & Ligne & "'").GetUpperBound(0) < 0 Then
		String requete ="select * from Lignes where lDossier=? And LPiece=?  And LDate=?  And LLig=?"; 
		st = sqlServer.prepareStatement(requete);
		st.setString(1, lignes.getlDossier());
		st.setInt(2, lignes.getlPiece());
		st.setDate(3,new java.sql.Date(lignes.getlDate().getTime()));
		st.setInt(4, lignes.getlLig());
		rs=st.executeQuery();

		if(!rs.next()) {
			requete="insert into Lignes(lDossier,lPiece,lDate,lLig,lTva,lLib,lGene,lAna,lJournal,lMtTVA)"
					+ " values(?,?,?,?,?,?,?,?,?,?)";
			ps = sqlServer.prepareStatement(requete);

			//			private Integer lPiece;  //4
			//			private Timestamp lDate;  //8
			//			private Integer lLig;  //4
			//			private String lTva; //5
			//			private String lLib; //35
			//			private String lGene; //1
			//			private String lAna; //1
			//			private String lJournal; //4
			//			private String lMtTVA; //1

			ps.setObject(1,lignes.getlDossier(), Types.VARCHAR);
			ps.setObject(2,lignes.getlPiece(), Types.INTEGER);
			ps.setObject(3,lignes.getlDate(), Types.DATE);
			ps.setObject(4,lignes.getlLig(), Types.INTEGER);
			ps.setObject(5,lignes.getlTva(), Types.VARCHAR);
			ps.setObject(6,lignes.getlLib(), Types.VARCHAR);
			ps.setObject(7,lignes.getlGene(), Types.VARCHAR);
			ps.setObject(8,lignes.getlAna(), Types.VARCHAR);
			ps.setObject(9,lignes.getlJournal(), Types.VARCHAR);
			ps.setObject(10,lignes.getlMtTVA(), Types.VARCHAR);

			ps.execute();

		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e);
		}
		} finally {
			if(rs!=null)rs.close();
			if(ps!=null)ps.close();
			if(st!=null)st.close();
		}
		}
	
	public void ajout_Ligne_SansTVA(String dossier , Integer nPiece , Date dtPiece , int  Ligne , String nomClient, String nomProduit , String cptD , 
			String actD , String cptC , String actC , BigDecimal montant )  throws SQLException {
    ajout_Ligne(dossier, nPiece, dtPiece, Ligne, false, "", "O", "N", nomClient, nomProduit);
    mouvDao.ajout_Mouvement(dossier, nPiece, dtPiece, Ligne, 1, cptD, actD, montant, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, cptC, actC);
    mouvDao.ajout_Mouvement(dossier, nPiece, dtPiece, Ligne, 3, cptC, actC, BigDecimal.ZERO, montant, "C", BigDecimal.ZERO, BigDecimal.ZERO, cptD, actD);
	}
}

