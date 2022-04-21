import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse, HttpSentEvent, HttpHeaderResponse,HttpProgressEvent,HttpResponse,HttpUserEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { BehaviorSubject,from } from 'rxjs';
import { retry, catchError, filter,take,switchMap,finalize,map,tap } from 'rxjs/operators';

import { User } from '../../model/user';
import { AuthenticationService } from '../../services/authentication.service';
import { HttpHeaders } from '@angular/common/http';
import { AppSettings } from '../../env';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

      constructor(private authService: AuthenticationService, private appSettings: AppSettings) { }

      isRefreshingToken: boolean = false;
      tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>(null);

      intercept(request: HttpRequest<any>, next: HttpHandler) : Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any> | any> {

        return next.handle(this.addTokenToRequest(this.addBbgHeaderToRequest(request), this.authService.getAuthToken()))
          .pipe(
            catchError(err => {
              if (err instanceof HttpErrorResponse) {
                switch ((<HttpErrorResponse>err).status) {
                  case 401:
                    return this.handle401Error(request, next);
                  case 400:
                    return <any>this.authService.logout();
                  case 500:
                      if (err.error instanceof Error) {
                        // A client-side or network error occurred. Handle it accordingly.
                        console.error('An error occurred:', err.error.message);
                      } else {
                        // The backend returned an unsuccessful response code.
                        // The response body may contain clues as to what went wrong,
                        console.error(`Backend returned code ${err.status}, body was: ${err.error}`);
                      }
                      return throwError(err);
                }
              } else {
                return throwError(err);
              }
            }));
      }

      private addTokenToRequest(request: HttpRequest<any>, token: string) : HttpRequest<any> {
          
        return request.clone({ setHeaders: { Authorization: `Bearer ${token}`}});
//          return request.clone({ setHeaders: { 
//              //'Accept': 'application/json',
//              //'Content-Type': 'application/json',
//              'Access-Control-Allow-Origin': '*',
//              'Dossier': this.appSettings.BDG_TENANT_DOSSIER, 
//              'Lgr': this.appSettings.BDG_TOKEN, 
//              'Bdg-login': this.appSettings.BDG_LOGIN, 
//              'Bdg-password': this.appSettings.BDG_PASSWORD,
//              'Authorization': `Bearer ${token}`
//              //'No-Auth': 'true',
//        }});
      }
      
      private addBbgHeaderToRequest(request: HttpRequest<any>) : HttpRequest<any> {
          
//        return request.clone({ setHeaders: { Authorization: `Bearer ${token}`}});
          return request.clone({ setHeaders: { 
              //'Accept': 'application/json',
              //'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
              'X-Dossier': this.appSettings.BDG_TENANT_DOSSIER, 
              'X-Lgr': this.appSettings.BDG_TOKEN, 
              'X-Bdg-login': this.appSettings.BDG_LOGIN, 
              'X-Bdg-password': this.appSettings.BDG_PASSWORD
              //'No-Auth': 'true',
        }});
      }

      private handle401Error(request: HttpRequest<any>, next: HttpHandler) {

        if(!this.isRefreshingToken) {
          this.isRefreshingToken = true;

          // Reset here so that the following requests wait until the token
          // comes back from the refreshToken call.
          this.tokenSubject.next(null);
          
          return this.authService.refreshToken()
            .pipe(
//              switchMap((user: User) => {
                    switchMap((user: any) => {
                if(user) {
                  this.tokenSubject.next(user.accessToken);
                  localStorage.setItem('currentUser', JSON.stringify(user));
                  return next.handle(this.addTokenToRequest(this.addBbgHeaderToRequest(request), user.accessToken));
                }

                return <any>this.authService.logout();
              }),
              catchError(err => {
                return <any>this.authService.logout();
              }),
              finalize(() => {
                this.isRefreshingToken = false;
              })
            );
        } else {
          this.isRefreshingToken = false;

          return this.tokenSubject
            .pipe(filter(token => token != null),
              take(1),
              switchMap(token => {
              return next.handle(this.addTokenToRequest(this.addBbgHeaderToRequest(request), token));
            }));
        }
      }
    }