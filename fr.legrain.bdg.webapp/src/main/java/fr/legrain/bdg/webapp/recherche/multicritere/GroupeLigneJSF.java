package fr.legrain.bdg.webapp.recherche.multicritere;

import java.io.Serializable;

import fr.legrain.recherche.multicritere.model.GroupeLigne;
import fr.legrain.recherche.multicritere.model.LigneCritere;

public class GroupeLigneJSF implements Serializable{

	private GroupeLigne groupeLigne;
	
	public GroupeLigneJSF(int numero){
		groupeLigne = new GroupeLigne(numero);
	}

	public void actLigneDown(LigneCritere ligneCritere/*ActionEvent actionEvent*/){
		ligneCritere.actLigneDown();
	}

	public void actLigneUp(LigneCritere ligneCritere/*ActionEvent actionEvent*/){
		ligneCritere.actLigneUp();
	}

	public void actLigneDelete(LigneCritere ligneCritere/*ActionEvent actionEvent*/){
		ligneCritere.actLigneDelete();
	}

	public void actChoixCritere(LigneCritere ligneCritere/*AjaxBehaviorEvent event*/){
		ligneCritere.actChoixCritere();
	}

	public void actChoixComparaison(LigneCritere ligneCritere/*AjaxBehaviorEvent event*/){
		ligneCritere.actChoixComparaison();
	}

	public GroupeLigne getGroupeLigne() {
		return groupeLigne;
	}

	public void setGroupeLigne(GroupeLigne groupeLigne) {
		this.groupeLigne = groupeLigne;
	}
}
