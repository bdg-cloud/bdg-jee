import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class Abonnement {
    constructor(
            public codeDocument : string,
            public codeArticle : string,
            public liblArticle : string,
            public libLDocument : string,
            public dateDebut : Date,
            public dateFin : Date,
            public suspension : boolean,
            public dateSuspension : Date,
            public nickname : string,
            public liblFrequence : string,
            public complement1 : string,
            public complement2 : string,
            public complement3 : string,
            public dateAnnulation : Date,
            public codeTiers : string,
            public isDisabled : boolean,
            public percentDone : any,
            public identifiantEtat : string,
           // public produitTelechargeable : boolean,
    ){}
    
    public produitTelechargeable() {
        var myarr = ["MAI-BDG-AGRI-LGR100",
                     "MAI-BDG-AGRI-LGR100",
                     "MAI-BDG-AGRI-LGR200",
                     "MAI-BDG-AGRI-LGR300",
                     "MAI-BDG-AGRI-LGR400",
                     "MAI-BDG-ACT-LGR300",
                     "MAI-BDG-ACT-LGR400",
                     "MAI-BDG-ACT-LGR100",
                     "MAI-BDG-ACT-LGR200",
                     "MAI-BDG-VITI-LGR300",
                     "MAI-BDG-VITI-LGR400",
                     "MAI-EPI-AGRI-LGR100",
                     "MAI-EPI-AGRI-LGR200",
                     "MAI-EPI-AGRI-LGR300",
                     "MAI-EPI-AGRI-LGR400",
                     "MAI-EPI-ACT-LGR100",
                     "MAI-EPI-ACT-LGR200",
                     "MAI-EPI-ACT-LGR300",
                     "MAI-EPI-ACT-LGR400",
                     "MAI-EPI-ASSO-LGR100",
                     "MAI-EPI-ASSO-LGR300",
                     "MAI-EPI-ASSO-LGR400",
                     "MAI-EPIG-AGRI-LGR400",
                     "MAI-EPIG-ACT-LGR400",
                     "MAI-EPI-COG-LGR100",
                     "MAI-EPI-COG-LGR400",
                     "PACK-COMPTA-SERENITE",
                     ];
        var arraycontainsarticle = (myarr.indexOf(this.codeArticle) > -1);
        return arraycontainsarticle;
      }
}

@Injectable({
    providedIn: 'root'
})
export class AbonnementAdapter implements Adapter<Abonnement> {

  adapt(item: any): Abonnement {
      console.log(item);
    return new Abonnement(
      item.codeDocument,
      item.codeArticle,
      item.liblArticle,
      item.libLDocument,
      item.dateDebut?new Date(item.dateDebut):null,
      item.dateFin?new Date(item.dateFin):null,
      
      //item.suspension,
      //item.identifiantEtat==="bdg.etat.document.suspendu",
      item.identifiantEtat!=="bdg.etat.document.encours",
      item.dateSuspension?new Date(item.dateSuspension):null,
      item.nickname,
      item.liblFrequence,
      item.complement1,
      item.complement2,
      item.complement3,
      item.dateAnnulation?new Date(item.dateAnnulation):null,
      
      item.codeTiers,
      false,
      0,
      //true,
      item.identifiantEtat,
    );
  }
}
