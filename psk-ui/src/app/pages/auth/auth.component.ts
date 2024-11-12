import {Component} from '@angular/core';
import {AuthenticationService} from "../../services/services/authentication.service";
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {AuthenticationResponse} from "../../services/models/authentication-response";
import {DecodedToken} from "../../services/models/decoded-token";
import {ActivatedRoute, Router} from "@angular/router";
import {TokenService} from "../../services/token/token.service";
import {RegistrationRequest} from "../../services/models/registration-request";

@Component({
  selector: 'app-register',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent {

  path: string | null = '';
  birthYears: number[] = [];
  isLogin: boolean = false;
  isRegister: boolean = false;
  isActivateAccount: boolean = false;

  loginRequest: AuthenticationRequest = {
    email: '',
    password: ''
  }
  registerRequest: RegistrationRequest = {
    birthYear: '',
    email: '',
    firstname: '',
    lastname: '',
    password: '',
    phoneNumber: ''
  }


  activationMessage: string[] = [];
  isActivationOkay = true;
  isActivationSubmitted = false;

  confirmPassword: string = '';
  errorMsg: Array<string> = [];


  loginInfoText: string = 'Randevularına ve sana atanmış testlere erişmek için giriş yap.';
  registerInfoText: string = 'Randevu almak ve sana atanan testleri takip edebilmek için kayıt ol.';

  infoText: string = '';

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private tokenService: TokenService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.path = this.route.snapshot.url.toString();
    this.updateContent(this.path);
  }

  updateContent(path: string | null): void {
    if (path === 'activate-account') {
      this.initAcivateAccount();
    } else if (path === 'login') {
      this.initLogin();
    } else if (path === 'register') {
      this.initRegister();
    }
  }

  initRegister(): void {
    this.isRegister = true;
    this.isLogin = false;
    this.isActivateAccount = false;
    this.infoText = this.registerInfoText;
    this.calculateBirthYearOptions();
  }

  initLogin(): void {
    this.isLogin = true;
    this.isRegister = false;
    this.isActivateAccount = false;
    this.infoText = this.loginInfoText;
  }

  initAcivateAccount(): void {
    this.isActivateAccount = true;
    this.isLogin = false;
    this.isRegister = false;
    this.infoText = "Hesabını aktive etmek için mail adresine gönderilen kodu giriniz.";
  }


  calculateBirthYearOptions(): void {
    const minYear = new Date().getFullYear() - 18;
    for (let year = minYear; year >= 1940; year--) {
      this.birthYears.push(year);
    }
  }

  toggleContent(path: string): void {
    this.router.navigate([path]);
  }


  login(): void {
    this.errorMsg = [];
    this.authService.authenticate(
      {
        body: this.loginRequest
      }
    ).subscribe(
      {
        next: (res: AuthenticationResponse) => {
          this.tokenService.token = res.token as string;
          const decodedToken: DecodedToken = JSON.parse(atob(this.tokenService.token.split('.')[1]));
          if (decodedToken.authorities.includes("ROLE_ADMIN")) {
            this.router.navigate(['navigateAdminMain'])
          } else {
            this.router.navigate(['navigateMain'])

          }
        },
        error: (err) => {
          if (err.error.validationErrors) {
            this.errorMsg = err.error.validationErrors;
          } else {
            this.errorMsg.push(err.error.error)
          }
          console.log(err);
        }
      }
    )
  }

  register(): void {
    this.errorMsg = [];
    if (this.registerRequest.password !== this.confirmPassword && this.registerRequest.password !== '') {
      this.errorMsg.push("Şifreler uyuşmuyor.");
      return;
    }
    this.authService.register(
      {
        body: this.registerRequest
      }
    ).subscribe(
      {
        next: (res: any) => {
          console.log(res.toString())
        },
        error: (err) => {
          if (err.error.validationErrors) {
            this.errorMsg = err.error.validationErrors;
          } else {
            this.errorMsg.push(err.error.error)
          }
          console.log(err);
        }
      }
    )
  }

  activateAccount(token: string): void {
    this.errorMsg = [];
    this.authService.confirm(
      {
        token: token
      }
    ).subscribe(
      {
        next: (res: any) => {
          this.activationMessage.push('Hesabınız başarıyla aktive edildi.\nGiriş yapabilirsiniz.');
          this.isActivationSubmitted = true;
          console.log(res.toString())
        },
        error: (err) => {
          this.activationMessage.push('Sistem hatası oluştu.');
          this.isActivationSubmitted = true;
          this.isActivationOkay = false;
          const parsedErr = JSON.parse(err.error);
          if (parsedErr.validationErrors) {
            this.activationMessage = parsedErr.validationErrors;
          } else {
            this.activationMessage.push(parsedErr.error)
          }
          console.log(parsedErr);
        }
      }
    )
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  onCodeCompleted(token: string) {
    this.activateAccount(token);
  }
}
