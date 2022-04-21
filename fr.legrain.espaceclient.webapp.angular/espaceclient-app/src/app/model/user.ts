import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class User {
    constructor(
            public id: number,
    public username: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public token: string,
    
    public accessToken: string,
    public refreshToken: string,
//    public tenantCode: string,
//    public tokenExpiration: string,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class UserAdapter implements Adapter<User> {

  adapt(item: any): User {
      console.log(item);
    return new User(
            item.id,
      item.username,
      item.password,
      item.firstName,
      item.lastName,
      item.token,
      item.accessToken,
      item.accessToken,
//      item.tenantCode,
//      item.tokenExpiration
    );
  }
}