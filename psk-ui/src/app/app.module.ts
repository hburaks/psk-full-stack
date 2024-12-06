import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
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
import { UserPanelComponent } from './modules/user/pages/user-panel/user-panel.component';
import { CommonService } from './custom-services/common-service/common.service';
import { AboutComponent } from './pages/about/about.component';
import { BlogCardComponent } from './components/blog-card/blog-card.component';
import { BlogCardDetailComponent } from './components/blog-card-detail/blog-card-detail.component';
import { GenericCardDetailComponent } from './components/generic-card-detail/generic-card-detail.component';
import { EmptyCardComponent } from './components/empty-card/empty-card.component';
import { TestComponent } from './pages/test/test.component';
import { TestCardComponent } from './components/test-card/test-card.component';
import { TestCardDetailComponent } from './components/test-card-detail/test-card-detail.component';
import { TestResultComponent } from './components/test-result/test-result.component';
import { SessionsComponent } from './pages/sessions/sessions.component';
import { HttpTokenInterceptor } from './custom-services/interceptor/http-token.interceptor';
import { TestService } from './custom-services/test/test.service';
import { SharedModule } from './modules/shared/shared.module';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    MainPageComponent,
    HeaderComponent,
    FooterComponent,
    BlogComponent,
    UserPanelComponent,
    AboutComponent,
    BlogCardComponent,
    BlogCardDetailComponent,
    GenericCardDetailComponent,
    EmptyCardComponent,
    TestComponent,
    TestCardComponent,
    TestCardDetailComponent,
    TestResultComponent,
    SessionsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    CodeInputModule,
    SharedModule
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
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
