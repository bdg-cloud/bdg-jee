package fr.legrain.bdg.welcome.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


@ManagedBean
@ViewScoped 
public class ListeWelcomeController implements Serializable{
	
	private static String F_ACTIVE = "<i class=\"fa fa-check\" style=\"color:green;\"></i>"; 
	private static String F_ENCOURS = "<i class=\"fa fa-hourglass-half\"></i>"; 
	private static String F_BIENTOT = "<i class=\"fa fa-lightbulb-o\"></i>"; 
	
    
	private List<DescFonctionnalites> listetiers;
    private List<DescFonctionnalites> listearticle;
    private List<DescFonctionnalites> listefacture;
    private List<DescFonctionnalites> listedevis;
    private List<DescFonctionnalites> listeavoir;
    private List<DescFonctionnalites> listebonlivraison;
    private List<DescFonctionnalites> listeboncommande;
    private List<DescFonctionnalites> listeproforma;
    private List<DescFonctionnalites> listeacompte;
    private List<DescFonctionnalites> listeapporteur;
    private List<DescFonctionnalites> listeavisprelevement;
    private List<DescFonctionnalites> listesreception;
    private List<DescFonctionnalites> listeslotstracabilite;
    private List<DescFonctionnalites> listestocksinventaire;
    private List<DescFonctionnalites> listefabrication;
    private List<DescFonctionnalites> listeparamtiers;
    private List<DescFonctionnalites> listeparamarticles;
    private List<DescFonctionnalites> listeparamdocuments;
    private List<DescFonctionnalites> listeparamsolstyce;
    






    public List<DescFonctionnalites> getListeDevis() {
		return listedevis;
	}

	public void setListeDevis(List<DescFonctionnalites> listedevis) {
		this.listedevis = listedevis;
	}

	public List<DescFonctionnalites> getListeFabrication() {
		return listefabrication;
	}

	public void setListeFabrication(List<DescFonctionnalites> listefabrication) {
		this.listefabrication = listefabrication;
	}

	public List<DescFonctionnalites> getListeArticle() {
		return listearticle;
	}

	public void setListeArticle(List<DescFonctionnalites> listearticle) {
		this.listearticle = listearticle;
	}

	@PostConstruct
    public void init() {
    	createTiers();
    	createArticle();
    	createFacture();
    	createDevis();
    	createAvoir();
    	createBonCommande();
    	createBonLivraison();
    	createProForma();
    	createAcompte();
    	createApporteur();
    	createAvisPrelevement();
    	createReception();
    	createLotsTracabilite();
    	createStocksInventaire();
    	createParamTiers();
    	createParamArticles();
    	createParamDocuments();
    	createParamSolstyce();
    	createFabrication();
    }    

    public List<DescFonctionnalites> getListeTiers() {
		return listetiers;
	}




	public void setListeTiers(List<DescFonctionnalites> listetiers) {
		this.listetiers = listetiers;
	}    


	public List<DescFonctionnalites> getListeFacture() {
		return listefacture;
	}


	public void createTiers() {
	    listetiers = new ArrayList<DescFonctionnalites>();

	    listetiers.add(new DescFonctionnalites(
	    		"<b>Fiches contacts :</b> création, modification, suppression ou désactivation. Le nombre de fiche est illimité.",
	    		F_ACTIVE,
	    		""));

	    listetiers.add(new DescFonctionnalites(
	    		"<b>Liste contacts :</b> pour accéder à vos fiches contacts, vous disposez d’une liste d’accès rapide.",
	    		F_ACTIVE,
	    		""));

	    listetiers.add(new DescFonctionnalites(
	    		"<b>Génération automatique des codes contacts :</b> vous pouvez soit laisser le programme générer les codes contacts et personnaliser cette codification, soit saisir un code quelconque à condition qu’il soit unique.",
	    		F_ACTIVE,
	    		""));

	    listetiers.add(new DescFonctionnalites(
	    		"<b>Classement par famille :</b> associez vos contacts à des familles de contacts pour créer des groupes facilement identifiables. Les types de familles sont entièrement paramétrables.",
	    		F_ACTIVE,
	    		""));

	    listetiers.add(new DescFonctionnalites(
	    		"<b>Type de contact :</b> en associant un type (client, fournisseur, organisme, etc ..) à vos contacts, vous définissez à quel groupe appartient ce contact.",
	    		F_ACTIVE,
	    		""));

	    listetiers.add(new DescFonctionnalites(
	    		"<b>Saisie de coordonnées :</b> pour chaque contact, vous disposez d’une fiche complète pour la saisie de l’ensemble de ses coordonnées.",
	    		F_ACTIVE,
	    		""));

	    listetiers.add(new DescFonctionnalites(
	    		"<b>Codes comptable :</b> liaison entre votre contact et le compte comptable et/ou le compte général. Cette information est utilisée lors de l’exportation en comptabilité.",
	    		F_ACTIVE,
	    		""));
	    
	    listetiers.add(new DescFonctionnalites(
	    		"<b>Paramètres :</b> différents paramètres peuvent être saisie dans la fiche contact, comme le mode de saisie (TTC), la localisation TVA, le type et les conditions de paiement ou encore, si c’est un client, l’utilisation du compte client.",
	    		F_ACTIVE,
	    		""));
	    
	    listetiers.add(new DescFonctionnalites(
	    		"<b>Désactiver un contact :</b> si vous n’avez plus de relation avec un contact, vous pouvez désactiver sa fiche.",
	    		F_ACTIVE,
	    		""));
	    
	    listetiers.add(new DescFonctionnalites(
	    		"<b>Espace client en ligne :</b> avec le Bureau de Gestion, vos clients disposent d’un compte client personnel à partir duquel ils peuvent récupérer leurs factures sous format pdf et voir l’historique des factures.",
	    		F_ACTIVE,
	    		""));
	    listetiers.add(new DescFonctionnalites(
	    		"<b>Envoi des factures par Email :</b> D'un clic, envoyez vos factures par email.",
	    		F_ACTIVE,
	    		""));
	    listetiers.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ACTIVE,
	    		""));
	    

	}
	
	public void createArticle() {
	    listearticle = new ArrayList<DescFonctionnalites>();

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Fiches Articles :</b> création, modification, suppression ou désactivation. Le nombre de fiche est illimité.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Liste Articles :</b> pour accéder à vos fiches articles, vous disposez d’une liste d’accès rapide.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Génération automatique des codes Articles :</b> vous pouvez soit laisser le programme générer les codes articles et personnaliser cette codification, soit saisir un code quelconque à condition qu’il soit unique.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Classement par famille :</b> associez vos articles à des familles d’articles pour créer des groupes facilement identifiables. Les types de familles d’articles sont entièrement paramétrable.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Gestion des lots:</b> Vous pouvez activer ou non la gestion des lots pour chaque article. La gestion des lots va de pair avec le module Lots / Stocks.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Code Barre :</b> Renseignez ce champ pour attribuer un code barre à chacun de vos articles.",
	    		F_ENCOURS,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Traçabilité :</b> Vous pouvez renseigner les sections Fabrication et Date limite de consommation afin que le Bureau de Gestion calcule la DDM/DLC lors de vos fabrications.",
	    		F_ACTIVE,
	    		""));
	    
	    listearticle.add(new DescFonctionnalites(
	    		"<b>Tarifs de référence :</b> indiquez le prix par défaut et le code TVA pour l’unité par défaut. ces informations seront proposées lors de la saisie des factures ou autres documents.",
	    		F_ACTIVE,
	    		""));
	    
	    listearticle.add(new DescFonctionnalites(
	    		"<b>Multi-Tarifs :</b> Vous pouvez définir plusieurs tarifs pour un article en fonction du groupe de client et du groupe de tarifs.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Unités :</b> Vous définissez les unités correspondantes à votre articles. Un calcul automatique peut être défini entre les deux unités, si c’est le cas. La formule de calcul est personnalisable.",
	    		F_ACTIVE,
	    		""));
	    
	    listearticle.add(new DescFonctionnalites(
	    		"<b>Alerte stock :</b> Indiquez une limite basse pour être alerté par le programme lorsque le seuil de stock de marchandise est atteint.",
	    		F_ENCOURS,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Marque :</b> Vous pouvez renseigner le nom d’une marque pour un article de votre catalogue. Cette information est reprise dans certaines éditions.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Fournisseurs de l'article :</b> Associez facilement les fournisseurs auprès de qui vous faites vos achats de marchandises. Cette liste peut être complétée soit manuellement depuis la fiche article, soit automatiquement en collectant les informations issues des bons de réception.",
	    		F_ACTIVE,
	    		""));
	    
	    listearticle.add(new DescFonctionnalites(
	    		"<b>Codes comptable :</b> liaison entre votre article et le compte comptable et/ou compte général. Cette information est utilisée lors de l’exportation en comptabilité.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Désactiver un article :</b> si vous n’utilisez plus un article, vous pouvez désactiver sa fiche.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Contrôles qualités :</b> définissez ici les différents contrôles qui sont à vérifier pour l’article courant lors d’une réception ou d’une fabrication. Vous pouvez utiliser des contrôles prédéfinis dans les modèles de contrôles afin de gagner du temps.",
	    		F_ACTIVE,
	    		""));

	    listearticle.add(new DescFonctionnalites(
	    		"<b>Recettes :</b> Indiquez les ingrédients qui sont utilisés lors de la fabrication de l’article courant.",
	    		F_ACTIVE,
	    		""));
	    
	    listearticle.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ACTIVE,
	    		""));
	}

	public void setListeFacture(List<DescFonctionnalites> listefacture) {
		this.listefacture = listefacture;
	}

	public void createFacture() {
    listefacture = new ArrayList<DescFonctionnalites>();

    listefacture.add(new DescFonctionnalites(
    		"<b>Génération automatique numéro facture paramétrable : </b>les numéro de factures sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Pré-remplissage des champs de l’entête de la facture :</b> dès que avez choisi le client a facturer, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé de la facture, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Saisie en mode TTC ou HT en fonction des paramètres du client</b> et modifiable lors de la saisie. Très pratique si l’on doit facturer des professionnels et des particuliers.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Pré-remplissage automatique des lignes de facture</b> suite au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Gestion des lots :</b> si le module gestion des lots est actif, vous pouvez choisir le lot d’origine pour le produit vendu.",
    		F_ACTIVE,
    		""));
    
    listefacture.add(new DescFonctionnalites(
    		"<b>Gestion des stocks :</b> les quantités facturées sont enlevées des stocks, sauf si un bon de livraison, de commande ou devis est lié à la facture.",
    		F_ACTIVE,
    		""));
    
    listefacture.add(new DescFonctionnalites(
    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
    		F_ACTIVE,
    		""));
    
    listefacture.add(new DescFonctionnalites(
    		"<b>Synthèse de la facture en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Règlement de la facture en un clic</b> avec pré-remplissage du moyen de paiement et formule de politesse",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Mise à disposition de la facture dans le compte client en ligne :</b> les factures sont pour l’instant les seuls documents que vos clients peuvent télécharger depuis l’espace client Bureau de Gestion.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Génération de la facture sous format pdf</b> pour être envoyée par Email ou par courrier.",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Les factures peuvent servir de modèles</b> pour n’importe quel autre document",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Les factures peuvent être liées depuis</b> les Bon de livraison, Devis, Bon de commande, Proforma, etc …",
    		F_ACTIVE,
    		""));

    listefacture.add(new DescFonctionnalites(
    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
    		F_ACTIVE,
    		""));
     
}

	public void createDevis() {
	    listedevis = new ArrayList<DescFonctionnalites>();


	    listedevis.add(new DescFonctionnalites(
	    		"<b>Génération automatique numéro devis paramétrable :</b> les numéro de devis sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage des champs de l’entête du devis :</b> dès que avez choisi le client a deviser, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé de la devis, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Saisie en mode TTC ou HT</b> en fonction des paramètres du client et modifiable lors de la saisie. Très pratique si l’on doit faire des devis des professionnels et des particuliers.",
	    		F_ACTIVE,
	    		""));
	    
	    listedevis.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));
	    
	    listedevis.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes de devis suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));
	    
	    listedevis.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Synthèse des devis en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Mise à disposition des devis dans le compte client :</b> tout comme les factures, les devis pourront être mis à disposition depuis l’espace client Bureau de Gestion pour que vos clients puissent les télécharger.",
	    		F_ENCOURS,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Génération des devis sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Les devis peuvent servir de modèles</b> pour n’importe quel autre document",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Les devis peuvent générer</b> des Factures, Bons de livraison, Bons de commande, Proforma, etc …",
	    		F_ACTIVE,
	    		""));

	    listedevis.add(new DescFonctionnalites(
	    		"<b>Tableaux de bord devis : </b>d'un seul coup d'oeil vous connaissez l'état de transformation de vos devis et de ceux à relancer.",
	    		F_ACTIVE,
	    		""));
	    
	    listedevis.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ACTIVE,
	    		""));
	     
	}

	public void createAvoir() {
	    listeavoir = new ArrayList<DescFonctionnalites>();


	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Génération automatique des numéros d’avoir paramétrable :</b> les numéro des avoirs sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage des champs de l’entête de l’avoir :</b> dès que avez choisi le client pour lequel vous devez faire un avoir, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé de l’avoir, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Saisie en mode TTC ou HT</b> en fonction des paramètres du client et modifiable lors de la saisie. Très pratique si l’on doit faire des avoirs pour des professionnels et des particuliers.",
	    		F_ACTIVE,
	    		""));
	    
	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));
	    
	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes de l’avoir suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));
	    
	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Synthèse de l’avoir en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Mise à disposition des avoirs dans le compte client :</b> tout comme les factures, les avoirs pourront être mis à disposition depuis l’espace client Bureau de Gestion pour que vos clients puissent les télécharger.",
	    		F_ENCOURS,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Génération des avoirs sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Les devis peuvent servir de modèles</b> pour n’importe quel autre document",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Les avoirs peuvent être généré</b> depuis les Factures, les Bons de livraison, les Bons de commande, Proforma, etc …",
	    		F_ACTIVE,
	    		""));

	    listeavoir.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	     
	}
	
	public void createBonCommande() {
	    listeboncommande = new ArrayList<DescFonctionnalites>();


	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Génération automatique numéro bon de commande paramétrable :</b> les numéro de bon de commande sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage des champs de l’entête du bon de commande :</b> dès que avez choisi le client, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé du bon de commande, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Saisie en mode TTC ou HT</b> en fonction des paramètres du client et modifiable lors de la saisie. Très pratique si l’on doit faire des bons de commande pour des professionnels ou des particuliers.",
	    		F_ACTIVE,
	    		""));
	    
	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));
	    
	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes du bon de commande suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));
	    
	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Synthèse du bon de commande en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Mise à disposition des bons de commande dans le compte client :</b> tout comme les factures, les bons de commande pourront être mis à disposition depuis l’espace client Bureau de Gestion pour que vos clients puissent les télécharger.",
	    		F_ENCOURS,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Génération des bons de commande sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Les bons de commande peuvent servir de modèles</b> pour n’importe quel autre document",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Les bons de commande peuvent générer</b> les Factures, Bon de livraison, Proforma, etc …",
	    		F_ACTIVE,
	    		""));

	    listeboncommande.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	     
	}

	public void createBonLivraison() {
	    listebonlivraison = new ArrayList<DescFonctionnalites>();


	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Génération automatique numéro bon de livraison paramétrable :</b> les numéro de bon de livraison sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage des champs de l’entête du bon de livraison :</b> dès que avez choisi le client, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé du bon de livraison, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Saisie en mode TTC ou HT</b> en fonction des paramètres du client et modifiable lors de la saisie. Très pratique si l’on doit faire des bons de livraison pour des professionnels ou des particuliers.",
	    		F_ACTIVE,
	    		""));
	    
	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));
	    
	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes du bon de livraison suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));
	    
	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Synthèse du bon de livraison en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Mise à disposition des bons de livraison dans le compte client :</b> tout comme les factures, les bons de livraison pourront être mis à disposition depuis l’espace client Bureau de Gestion pour que vos clients puissent les télécharger.",
	    		F_ENCOURS,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Génération des bons de livraison sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Les bons de livraison peuvent servir de modèles</b> pour n’importe quel autre document",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Les bons de livraison peuvent générer</b> les Factures, Proforma, etc …",
	    		F_ACTIVE,
	    		""));

	    listebonlivraison.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	     
	}

	public void createProForma() {
	    listeproforma = new ArrayList<DescFonctionnalites>();

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Génération automatique numéro facture pro forma paramétrable :</b> les numéro de factures pro forma sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage des champs de l’entête de la facture pro forma :</b> dès que avez choisi le client à facturer, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé de la facture, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Saisie en mode TTC ou HT</b> en fonction des paramètres du client et modifiable lors de la saisie. Très pratique si l’on doit facturer des professionnels et des particuliers.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes de facture pro forma suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
	    		F_ACTIVE,
	    		""));
	    
	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Synthèse de la facture pro forma en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Règlement de la facture pro forma en un clic</b> avec pré-remplissage du moyen de paiement et formule de politesse",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Mise à disposition de la facture pro forma dans le compte client :</b> les factures pro forma pourront être mis à disposition dans l’espace client Bureau de Gestion afin que vos clients puissent les télécharger.",
	    		F_ENCOURS,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Génération de la facture pro forma sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Les factures pro forma peuvent servir de modèles</b> pour n’importe quel autre document",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Les factures pro forma peuvent être liées</b> depuis les Bon de livraison, Devis, Bon de commande,etc … et générer des factures.",
	    		F_ACTIVE,
	    		""));

	    listeproforma.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	     
	}


	public void createAcompte() {
    listeacompte = new ArrayList<DescFonctionnalites>();

    listeacompte.add(new DescFonctionnalites(
    		"<b>Génération automatique des numéros des factures d’acomptes paramétrable :</b> les numéro de factures d’acomptes sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Pré-remplissage des champs de l’entête de la facture d’acompte :</b> dès que avez choisi le client à facturer, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé de la facture, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Saisie en mode TTC ou HT en fonction des paramètres du client</b> et modifiable lors de la saisie. Très pratique si l’on doit facturer des professionnels et des particuliers.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Pré-remplissage automatique des lignes des factures d’acomptes suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
    		F_ACTIVE,
    		""));
    
    listeacompte.add(new DescFonctionnalites(
    		"<b>Synthèse de la facture d’acompte en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Règlement de la facture d’acompte en un clic</b> avec pré-remplissage du moyen de paiement et formule de politesse",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Mise à disposition de la facture d’acompte dans le compte client :</b> les factures pro forma pourront être mis à disposition dans l’espace client Bureau de Gestion afin que vos clients puissent les télécharger.",
    		F_ENCOURS,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Génération de la facture d’acompte sous format pdf</b> pour être envoyée par Email ou par courrier.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Les factures d’acompte peuvent servir de modèles</b> pour n’importe quel autre document",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Les factures d’acomptes peuvent être générées</b> depuis les Bons de livraison, Devis, Bons de commande,etc … et générer des factures.",
    		F_ACTIVE,
    		""));

    listeacompte.add(new DescFonctionnalites(
    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
    		F_ACTIVE,
    		""));
     
}
	
	public void createApporteur() {
	    listeapporteur = new ArrayList<DescFonctionnalites>();

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Génération automatique des numéros des factures apporteurs paramétrables :</b> les numéro de factures apporteurs sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage des champs de l’entête de la facture apporteur:</b> dès que avez choisi le partenaire destinataire de la facture apporteur, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé de la facture, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Saisie en mode TTC ou HT en fonction des paramètres du client</b> et modifiable lors de la saisie. Très pratique si l’on doit facturer des professionnels et des particuliers.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes des factures apporteurs suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
	    		F_ACTIVE,
	    		""));
	    
	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Synthèse de la facture apporteur en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Règlement de la facture apporteur en un clic</b> avec pré-remplissage du moyen de paiement et formule de politesse",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Mise à disposition de la facture apporteurs dans le compte client :</b> les factures apporteurs pourront être mis à disposition dans l’espace client Bureau de Gestion afin que vos clients puissent les télécharger.",
	    		F_ENCOURS,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Génération de la facture apporteur sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Les factures apporteurs peuvent servir de modèles</b> pour n’importe quel autre document",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Les factures apporteurs peuvent être générées</b> depuis les Bons de livraison, Devis, Bons de commande,etc ....",
	    		F_ACTIVE,
	    		""));

	    listeapporteur.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	     
	}
	
	
	public void createAvisPrelevement() {
	    listeavisprelevement = new ArrayList<DescFonctionnalites>();

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Génération automatique des numéros des avis de prélèvements paramétrables :</b> les numéro des avis de prélèvements  sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche client intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage des champs de l’entête des avis de prélèvements :</b> dès que avez choisi le client qui doit être prélevé, le Bureau de Gestion remplit automatique tous les champs de l’entête (libellé, coordonnées du client, adresse de livraison). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Saisie en mode TTC ou HT en fonction des paramètres du client</b> et modifiable lors de la saisie. Très pratique si l’on doit facturer des professionnels et des particuliers.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes des avis de prélèvements suite</b> au choix de l’article (libellé, prix ht, tva, ttc, unité 1 et unité 2, quantité à 1 par défaut). Vous pouvez modifier ce qui ne convient pas.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la TVA :</b> pour chaque ligne d’article, la tva et le ttc sont calculés automatiquement, ainsi que les totaux de synthèse.",
	    		F_ACTIVE,
	    		""));
	    
	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Synthèse de l’avis de prélèvement en fin de saisie :</b> un écran récapitulatif vous est proposé en fin de saisie avant validation et envoi.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Mise à disposition des avis de prélèvements dans le compte client :</b> les avis de prélèvements pourront être mis à disposition dans l’espace client Bureau de Gestion afin que vos clients puissent les télécharger.",
	    		F_ENCOURS,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Génération des avis de prélèvements sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Les avis de prélèvements  peuvent servir de modèles</b> pour n’importe quel autre document",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Les avis de prélèvements peuvent être générées</b> depuis les Bons de livraison, Devis, Bons de commande,etc ....",
	    		F_ACTIVE,
	    		""));

	    listeavisprelevement.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	     
	}
	
	public void createReception() {
	    listesreception = new ArrayList<DescFonctionnalites>();

	    listesreception.add(new DescFonctionnalites(
	    		"<b>Génération automatique des numéros des bons de réceptions paramétrables :</b> les numéro des bons de réceptions sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listesreception.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche fournisseur intuitive :</b> le programme affiche une liste déroulante en fonction de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listesreception.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage du libellé de la réception :</b> dès que avez choisi le fournisseur qui vous livre, le Bureau de Gestion remplit automatiquement le libellé avec le numéro de réception et le nom du fournisseur.",
	    		F_ACTIVE,
	    		""));

	    listesreception.add(new DescFonctionnalites(
	    		"<b>Notez la date et l’heure d’arrivée de vos marchandises </b> en deux clics, vous pouvez ajuster la date et l’heure d’arrivée.",
	    		F_ACTIVE,
	    		""));

	    listesreception.add(new DescFonctionnalites(
	    		"<b>Prestation de services : </b> la notion de prestation de service permet de bloquer la fabrication pour un client en particulier. De fait, vous ne pourrez facturer cette fabrication qu’à ce client.",
	    		F_ACTIVE,
	    		""));

	    listesreception.add(new DescFonctionnalites(
	    		"<b>Saisie et recherche article intuitive :</b> affichage des articles dans une liste déroulante en fonction de votre saisie.",
	    		F_ACTIVE,
	    		""));

	    listesreception.add(new DescFonctionnalites(
	    		"<b>Pré-remplissage automatique des lignes des bons de réceptions </b>suite au choix de l’article, le programme pré-rempli le libellé, génère un numéro de lot, calcule la date de péremption. Vous pouvez modifier ce qui ne convient pas et affecter un entrepôt, un emplacement, les quantités, et aussi saisir vos contrôles qualités.",
	    		F_ACTIVE,
	    		""));
	    
	    listesreception.add(new DescFonctionnalites(
	    		"<b>Génération des bons de réceptions sous format pdf</b> pour être envoyée par Email ou par courrier.",
	    		F_ACTIVE,
	    		""));
	    
	    listesreception.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ACTIVE,
	    		""));
	    
	    listesreception.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	}

	public void createLotsTracabilite() {
	    listeslotstracabilite = new ArrayList<DescFonctionnalites>();

	    listeslotstracabilite.add(new DescFonctionnalites(
	    		" <b>Gestion des lots et traçabilités :</b> consultez facilement la liste de vos lots de marchandises en cours ou terminé. Consultez également l’utilisation des lots, dans quelle fabrication, ou de quels lots est constitué une fabrication. Présenté sous forme d’arbre généalogique, vous saurez tout sur vos fabrications concernant les lots.",
	    		F_ACTIVE,
	    		""));
	    listeslotstracabilite.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	}
	

	public void createStocksInventaire() {
	    listestocksinventaire = new ArrayList<DescFonctionnalites>();

	    listestocksinventaire.add(new DescFonctionnalites(
	    		" <b>Gestion des lots et traçabilités :</b> consultez facilement la liste de vos lots de marchandises en cours ou terminé. Consultez également l’utilisation des lots, dans quelle fabrication, ou de quels lots est constitué une fabrication. Présenté sous forme d’arbre généalogique, vous saurez tout sur vos fabrications concernant les lots.",
	    		F_ACTIVE,
	    		""));
	    listestocksinventaire.add(new DescFonctionnalites(
	    		" <b>Gestion des lots et traçabilités :</b> consultez facilement la liste de vos lots de marchandises en cours ou terminé. Consultez également l’utilisation des lots, dans quelle fabrication, ou de quels lots est constitué une fabrication. Présenté sous forme d’arbre généalogique, vous saurez tout sur vos fabrications concernant les lots.",
	    		F_ACTIVE,
	    		""));
	    listestocksinventaire.add(new DescFonctionnalites(
	    		"<b>Aide contextuelle :</b> vous disposez d’une aide contextuelle partout où cela est nécessaire. Cette aide vous donne des indications sur l’utilisation de l’écran et la façon de remplir certaines zones de saisie.",
	    		F_ENCOURS,
	    		""));
	    }

	public void createFabrication() {
	    listefabrication = new ArrayList<DescFonctionnalites>();

	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Fiches Fabrications :</b> création, modification, suppression ou désactivation. Le nombre de fiche est illimité.",
	    		F_ACTIVE,
	    		""));

	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Génération automatique numéro fabrications paramétrable :</b> les numéros des fabrications sont générés automatiquement et vous pouvez personnaliser cette numérotation à l’aide d’une formule.",
	    		F_ACTIVE,
	    		""));

	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Création de modèle à partir d’une fabrication :</b> Depuis la liste des fabrications, vous pouvez générer des modèles de fabrications qui pourront être utilisés lors de futures fabrications.",
	    		F_ACTIVE,
	    		""));

	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Gestion des lots Produits Finis :</b> Pour chaque Produit Fini, un numéro de lot est généré automatiquement par le programme et les stocks mis à jour (entrepôt, emplacement, quantité).",
	    		F_ACTIVE,
	    		""));

	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Calcul automatique de la date de DLC/DDM : </b> en fonction des critères présents dans l’article fabriqué, le programme propose une date de DLC/DDM. Cette date peut être modifiée lors de la saisie.",
	    		F_ACTIVE,
	    		""));

	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Matières Premières : </b> vous pouvez soit sélectionner un à un les ingrédients utilisés dans votre fabrication, soit affecter en un clic l’ensemble des ingrédients définis dans la recette du produit fini que vous allez fabriquer. Dans ce cas, les quantités de matières premières sont calculées automatiquement, mais vous devez choisir les lots à utiliser. Vous pouvez aussi panacher les deux méthodes.",
	    		F_ACTIVE,
	    		""));

	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Prestation de services : </b> la notion de prestation de service permet de bloquer la fabrication pour un client en particulier. De fait, vous ne pourrez facturer cette fabrication qu’à ce client.",
	    		F_ACTIVE,
	    		""));
	    
	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Contrôles qualités : </b> saisissez les valeurs pour les contrôles qualités lors de la fabrication. Des indicateurs vous donne l’état de vos contrôles.",
	    		F_ACTIVE,
	    		""));
	    
	    listefabrication.add(new DescFonctionnalites(
	    		"<b>Rédacteur et contrôleur : </b> choisissez ici quels sont les utilisateurs qui rédigent ou contrôlent la fabrication.",
	    		F_ACTIVE,
	    		""));
	    
	}

	public void createParamTiers() {
	    listeparamtiers = new ArrayList<DescFonctionnalites>();

	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Type adresse :</b> Définissez des types d’adresses comme de facturation, de livraison, de correspondance, de domicile, et tout autre type que vous jugerez nécessaire.",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Type civilité :</b> Monsieur, Madame, Mademoiselle, Monsieur le Directeur. Définissez tous les types de civilités, et cela de façon homogène pour vos correspondance.",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Type entité :</b> Là aussi, vous pouvez homogénéiser l’écriture des formes juridiques de vos partenaires. Et cela peut être utile pour des ciblages en fonction des formes juridique.",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Type téléphone :</b> Associer un type de téléphone aux numéros de vos correspondants peut vous faire gagner un temps précieux lorsque vous devez appeler vos clients. (Bureau, Domicile, portable personnel, professionnel, accueil)",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Type tiers :</b> Les types de Tiers permettent d’identifier facilement vos Clients, vos Fournisseurs, vos Contacts, vos Prospects, etc ….",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Type site web :</b> Vos Tiers possèdent une ou plusieurs adresses de sites internet ? Identifiez les plus facilement en indiquant s’il s’agit d’un site vitrine, d’une boutique ou bien encore d’une adresse extranet pour passer une commande, ou tout autre chose ...",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Type Email :</b> Vu le nombre d’emails que nous devons gérer pour certains de nos correspondants, un petit classement par type ne peut pas faire de mal.",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Conditions de paiement :</b> Définissez les différents types de conditions de paiement que vous pouvez proposer à vos clients ou que vous avez auprès des différents fournisseurs.",
	    		F_ACTIVE,
	    		""));
	    listeparamtiers.add(new DescFonctionnalites(
	    		"<b>Famille :</b> Regrouper vos Tiers par famille vous permet de constituer des groupes homogènes en fonction d’un critère ou d’un autre.",
	    		F_ACTIVE,
	    		""));
	    }

	public void createParamArticles() {
		listeparamarticles = new ArrayList<DescFonctionnalites>();

		listeparamarticles.add(new DescFonctionnalites(
	    		" <b>Famille :</b> Définissez les familles auxquelles appartiennent vos articles.",
	    		F_ACTIVE,
	    		""));
		listeparamarticles.add(new DescFonctionnalites(
	    		"<b>Code TVA :</b> Gérez facilement les Taux de TVA avec leur Code, qui seront associés aux articles de votre catalogue.",
	    		F_ACTIVE,
	    		""));
		listeparamarticles.add(new DescFonctionnalites(
	    		"<b>Unités :</b> La notion d’unités et de quantités est importante dans votre activité ? Créez autant d’unités que nécessaires avec le gestionnaire d’unités.",
	    		F_ACTIVE,
	    		""));
		listeparamarticles.add(new DescFonctionnalites(
	    		"<b>Coefficient d’unités :</b> Définissez les différents ratios entre vos unités. Et pour une meilleure gestion des stocks, vous pouvez aussi utiliser des unités de références.",
	    		F_ACTIVE,
	    		""));
		listeparamarticles.add(new DescFonctionnalites(
	    		"<b>Famille d’unités :</b> Regrouper les unités par famille peut être utile si vous utilisez beaucoup d’unités dans votre activité pour une meilleure gestion, ou pour tout autre usage qui vous semblera pertinent.",
	    		F_ACTIVE,
	    		""));
		listeparamarticles.add(new DescFonctionnalites(
	    		"<b>Marque :</b> Vous pouvez définir des marques pour les associer à certains de vos articles.",
	    		F_ACTIVE,
	    		""));
		}

	public void createParamDocuments() {
	    listeparamdocuments = new ArrayList<DescFonctionnalites>();

	    listeparamdocuments.add(new DescFonctionnalites(
	    		"<b>Type paiement :</b> les types de paiements peuvent être associé à vos fiches de clients. Et par exemple, lors de la facturation, le type de paiement associé à la fiche du client sera automatiquement sélectionné, et ajustable si besoin est.",
	    		F_ACTIVE,
	    		""));
	    listeparamdocuments.add(new DescFonctionnalites(
	    		"<b>Paiement - échéance :</b> définissez les dates soit pour les conditions de paiement, soit pour la durée de validité de vos documents( devis, bon de commande, ).",
	    		F_ACTIVE,
	    		""));
	    }
	
	public void createParamSolstyce() {
	    listeparamsolstyce = new ArrayList<DescFonctionnalites>();

	    listeparamsolstyce.add(new DescFonctionnalites(
	    		"<b>Entrepôt :</b> Saisissez la liste des entrepôts où sont stockés vos marchandises.",
	    		F_ACTIVE,
	    		""));
	    listeparamsolstyce.add(new DescFonctionnalites(
	    		"<b>Type fabrication :</b> Classez et regroupez vos fabrications en fonction de vos critères.",
	    		F_ACTIVE,
	    		""));
	    listeparamsolstyce.add(new DescFonctionnalites(
	    		"<b>Type réception :</b> vos réceptions de marchandises peuvent elles aussi être classées et regroupées.",
	    		F_ACTIVE,
	    		""));
	    listeparamsolstyce.add(new DescFonctionnalites(
	    		"<b>Type code barre :</b> vos codes barres peuvent avoir un type, toujours pour les classements et recherches",
	    		F_ACTIVE,
	    		""));
	    listeparamsolstyce.add(new DescFonctionnalites(
	    		"<b>Groupe de contrôles :</b> les contrôles qualités aussi peuvent être associés à un groupe.",
	    		F_ACTIVE,
	    		""));
	    }
	
	
	public List<DescFonctionnalites> getListeBonCommande() {
		return listeboncommande;
	}

	public void setListeBonCommande(List<DescFonctionnalites> listeboncommande) {
		this.listeboncommande = listeboncommande;
	}

	public List<DescFonctionnalites> getListeBonLivraison() {
		return listebonlivraison;
	}

	public void setListeBonLivraison(List<DescFonctionnalites> listebonlivraison) {
		this.listebonlivraison = listebonlivraison;
	}

	public List<DescFonctionnalites> getListeAvoir() {
		return listeavoir;
	}

	public void setListeAvoir(List<DescFonctionnalites> listeavoir) {
		this.listeavoir = listeavoir;
	}

	public List<DescFonctionnalites> getListeProForma() {
		return listeproforma;
	}

	public void setListeProForma(List<DescFonctionnalites> listeproforma) {
		this.listeproforma = listeproforma;
	}

	public List<DescFonctionnalites> getListeAcompte() {
		return listeacompte;
	}

	public void setListeAcompte(List<DescFonctionnalites> listeacompte) {
		this.listeacompte = listeacompte;
	}

	public List<DescFonctionnalites> getListeApporteur() {
		return listeapporteur;
	}

	public void setListeApporteur(List<DescFonctionnalites> listeapporteur) {
		this.listeapporteur = listeapporteur;
	}

	public List<DescFonctionnalites> getListeAvisPrelevement() {
		return listeavisprelevement;
	}

	public void setListeAvisPrelevement(List<DescFonctionnalites> listeavisprelevement) {
		this.listeavisprelevement = listeavisprelevement;
	}

	public List<DescFonctionnalites> getListeReception() {
		return listesreception;
	}

	public void setListeReception(List<DescFonctionnalites> listesreception) {
		this.listesreception = listesreception;
	}

	public List<DescFonctionnalites> getListesLotsTracabilite() {
		return listeslotstracabilite;
	}

	public void setListesLotsTracabilite(List<DescFonctionnalites> listeslotstracabilite) {
		this.listeslotstracabilite = listeslotstracabilite;
	}

	public List<DescFonctionnalites> getListeStocksInventaire() {
		return listestocksinventaire;
	}

	public void setListeStocksInventaire(List<DescFonctionnalites> listestocksinventaire) {
		this.listestocksinventaire = listestocksinventaire;
	}

	public List<DescFonctionnalites> getListeParamTiers() {
		return listeparamtiers;
	}

	public void setListeParamTiers(List<DescFonctionnalites> listeparamtiers) {
		this.listeparamtiers = listeparamtiers;
	}

	public List<DescFonctionnalites> getListeParamArticles() {
		return listeparamarticles;
	}

	public void setListeParamArticles(List<DescFonctionnalites> listeparamarticles) {
		this.listeparamarticles = listeparamarticles;
	}

	public List<DescFonctionnalites> getListeParamDocuments() {
		return listeparamdocuments;
	}

	public void setListeParamDocuments(List<DescFonctionnalites> listeparamdocuments) {
		this.listeparamdocuments = listeparamdocuments;
	}

	public List<DescFonctionnalites> getListeParamSolstyce() {
		return listeparamsolstyce;
	}

	public void setListeParamSolstyce(List<DescFonctionnalites> listeparamsolstyce) {
		this.listeparamsolstyce = listeparamsolstyce;
	}
}
 
 
