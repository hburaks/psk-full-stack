import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  HTTP_INTERCEPTORS,
  HttpClient,
  HttpClientModule,
} from '@angular/common/http';
import { AuthComponent } from './pages/auth/auth.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CodeInputModule } from 'angular-code-input';
import { MainPageComponent } from './pages/main-page/main-page.component';
import { HeaderComponent } from './pages/header/header.component';
import { FooterComponent } from './pages/footer/footer.component';
import { BlogComponent } from './pages/blog/blog.component';
import { CommonService } from './custom-services/common-service/common.service';
import { AboutComponent } from './pages/about/about.component';
import { TestComponent } from './pages/test/test.component';
import { SessionsComponent } from './pages/sessions/sessions.component';
import { HttpTokenInterceptor } from './custom-services/interceptor/http-token.interceptor';
import { TestService } from './custom-services/test/test.service';
import { SharedModule } from './modules/shared/shared.module';
import { CustomUserService } from './custom-services/custom-user/custom-user.service';
import { TestSolverComponent } from './components/test-solver/test-solver.component';
import { ApiModule } from './services/api.module';
import { environment } from '../environments/environment';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    MainPageComponent,
    HeaderComponent,
    FooterComponent,
    BlogComponent,
    AboutComponent,
    TestComponent,
    SessionsComponent,
    TestSolverComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    CodeInputModule,
    SharedModule,
    ApiModule.forRoot({ rootUrl: environment.apiUrl }),
  ],
  providers: [
    HttpClient,
    CommonService,
    TestService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true,
    },
    CustomUserService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
