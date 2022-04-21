package fr.legrain.bdg.recherche.multicritere.service.remote;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.recherche.multicritere.model.GroupeLigne;



@Remote
public interface IRechercheMultiCritereServiceRemote {

	public ArrayList<Object> getResultat(String resultat,List<GroupeLigne> groupe);
}
