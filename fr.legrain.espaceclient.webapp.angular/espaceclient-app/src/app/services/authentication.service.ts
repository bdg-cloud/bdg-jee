import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

import { User, UserAdapter } from '../model/user';
import { Tiers, TiersAdapter } from '../model/tiers';
import { AppSettings } from '../env';

/**
 * http://ericsmasal.com/2018/07/02/angular-6-with-jwt-and-refresh-tokens-and-a-little-rxjs-6/
 * https://blog.angular-university.io/angular-jwt-authentication/
 */
@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  public listeTiersCompteEspaceClient: Tiers[];

  private _url = "";

  constructor(private http: HttpClient, private router: Router, private adapter: UserAdapter, private tiersAdapter: TiersAdapter,
    private appSettings: AppSettings) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
    this._url = this.appSettings.BASE_API_ENDPOINT + '/auth';
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
    //        return new User();
  }

  login(username: string, password: string) {
    //        const formData = new FormData();
    //        formData.append('loginAngular', username);
    //        formData.append('pwdAngular', password);

    //        localStorage.removeItem('currentUser');

    const body = new HttpParams()
      .set('login', username)
      .set('password', password);

    return this.http.post<any>(
      this._url + "/authenticate-espace-client",
      //{username, password},
      body.toString(),
      this.appSettings.httpOptionsFormEncoded
    )
      .pipe(
        map(user => {
          if (user && user.codeTiers) {
            this.appSettings.CODE_CLIENT_CHEZ_FRS = user.codeTiers;
            this.appSettings.BDG_LOGIN = username;
            this.appSettings.BDG_PASSWORD = password;
          }

          if (user && user.accessToken) {
            localStorage.setItem('currentUser', JSON.stringify(user));

            this.listeTiers().subscribe(data => {
              this.listeTiersCompteEspaceClient = data;
              if(this.listeTiersCompteEspaceClient.length==1) {
                this.router.navigate(['/dashboard']);
              } else {
                this.router.navigate(['/choix-compte-tiers']);
              }
            });
            //this.router.navigate(['/dashboard']);
          }

          return user;
        })
        , catchError(this.handleError)
      );
  }

  handleError(error) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}\nBody: ${error.error}`;
    }
    //window.alert(errorMessage);
    return throwError(errorMessage);
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

  refreshToken() /*: Observable<User> */ {
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    //if(currentUser) {
    let token = currentUser.refreshToken;

    return this.http.post</*User*/any>(
      this._url + "/refresh-espace-client",
      { 'token': token },
      this.appSettings.httpOptionsCTJson
    )
      .pipe(
        map(user => {
          if (user && user.accessToken) {
            currentUser.token = user.token;
            currentUser.accessToken = user.accessToken;
            currentUser.refreshToken = user.refreshToken;
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
          }
          return /*<User>*/currentUser;
        }
        )
      );
    //} else {
    //    console.log("ERREUR");
    //}
  }

  getAuthToken(): string {
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));

    if (currentUser != null) {
      return currentUser.accessToken;
    }

    return '';
  }

  listeTiers(): Observable<Tiers[]> {
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/liste-tiers'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "/"+currentUser.id;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map(
        (data: any[]) =>
          data.map(item => this.tiersAdapter.adapt(item))
      ),
    );

  }

}