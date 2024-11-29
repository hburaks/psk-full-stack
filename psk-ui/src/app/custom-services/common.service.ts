import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { TokenService } from './token/token.service';
import { BlogResponse } from '../services/models';

@Injectable({
  providedIn: 'root',
})
export class CommonService {
  isUserRegistered: boolean = false;
  isAdminRegistered: boolean = false;

  fetchedBlogList: BlogResponse[] = [];

  private userStatusSubject = new BehaviorSubject<boolean>(
    this.checkInitialUserStatus()
  );
  userStatus$ = this.userStatusSubject.asObservable();

  constructor(
    private tokenService: TokenService
  ) {}

  updateUserStatus(isLoggedIn: boolean) {
    this.userStatusSubject.next(isLoggedIn);
    localStorage.setItem('isLoggedIn', JSON.stringify(isLoggedIn));
  }

  private checkInitialUserStatus(): boolean {
    const isLoggedIn = this.tokenService.isTokenValid();
    if (isLoggedIn) {
      const roles = this.tokenService.userRoles;
      this.isUserRegistered = roles.includes('ROLE_USER');
      this.isAdminRegistered = roles.includes('ROLE_ADMIN');
    }
    return isLoggedIn;
  }

  getBlogCardDetail(id: number): BlogResponse | undefined {
    return this.fetchedBlogList.find((blog) => blog.id == id);
  }

}
