import { Component, HostListener, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { CommonService } from 'src/app/custom-services/common.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  isUserHeader: boolean = false;
  isPublicHeader: boolean = true;
  isAdminHeader: boolean = false;

  isScreenSmall: boolean = false;

  isMainActive: boolean = false;
  isAboutActive: boolean = false;
  isBlogActive: boolean = false;
  isTestActive: boolean = false;
  isSessionActive: boolean = false;
  isRegisterActive: boolean = false;
  isLoginActive: boolean = false;
  isPanelActive: boolean = false;

  panelPath: string = '';

  constructor(private router: Router, private commonService: CommonService) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.updateActiveState(this.router.url);
      }
    });
    this.checkUserStatus();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.isScreenSmall = event.target.innerWidth < 768;
  }

  ngOnInit() {
    this.isScreenSmall = window.innerWidth < 768;
    this.updateActiveState(this.router.url);
    this.checkUserStatus();
  }

  checkUserStatus() {
    this.commonService.userStatus$.subscribe((isLoggedIn) => {
      this.isPublicHeader = !isLoggedIn;
      this.isAdminHeader = this.commonService.isAdminRegistered;
      this.isUserHeader = this.commonService.isUserRegistered;
      this.panelPath = this.isUserHeader
        ? '/user/panel'
        : this.isAdminHeader
        ? '/admin/panel'
        : '';
    });
  }

  private updateActiveState(url: string) {
    this.isMainActive = url === '/';
    this.isAboutActive = url === '/about';
    this.isBlogActive = url === '/blog';
    this.isTestActive = url === '/test';
    this.isSessionActive = url === '/session';
    this.isRegisterActive = url === '/register';
    this.isLoginActive = url === '/login';
    this.isPanelActive = url === '/user/panel' || url === '/admin/panel';
  }

  logout() {
    this.isPublicHeader = true;
    this.isAdminHeader = false;
    this.isUserHeader = false;
    this.commonService.isUserRegistered = false;
    this.commonService.isAdminRegistered = false;
    this.commonService.updateUserStatus(false);
    localStorage.clear();
    this.router.navigate(['']);
  }
}
