import {Injectable} from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token(): string {
    return localStorage.getItem('token') as string;
  }

  isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    const jwtHelper = new JwtHelperService();
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }
    const decodedToken = jwtHelper.decodeToken(token);
    if (!decodedToken || !decodedToken.authorities || !Array.isArray(decodedToken.authorities) || decodedToken.authorities.length === 0) {
      localStorage.clear();
      return false;
    }
    return true;
  }


  isTokenNotValid() {
    return !this.isTokenValid();
  }

  get userRoles(): string[] {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      // TODO: test token console.log(decodedToken.authorities);
      return decodedToken.authorities;
    }
    return [];
  }

  isAdmin() {
    return this.userRoles.includes('ROLE_ADMIN');
  }

  isUser() {
    return this.userRoles.includes('ROLE_USER');
  }

  getUserId() {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      return decodedToken.userId;
    }
    return null;
  }

}
