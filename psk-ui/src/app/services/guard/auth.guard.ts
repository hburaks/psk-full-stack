import {CanActivateFn, Router} from '@angular/router';
import {TokenService} from '../token/token.service';
import {inject} from '@angular/core';


export const userGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  if (tokenService.isTokenNotValid() || !tokenService.userRoles.includes('ROLE_USER')) {
    router.navigate(['login']);
    return false;
  }
  return true;
};

export const adminGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  if (tokenService.isTokenNotValid() || !tokenService.userRoles.includes('ROLE_ADMIN')) {
    router.navigate(['login']);
    return false;
  }
  return true;
};
