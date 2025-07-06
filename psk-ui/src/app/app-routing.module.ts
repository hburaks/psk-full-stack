import { inject, NgModule } from '@angular/core';
import { Router, RouterModule, Routes } from '@angular/router';
import { AuthComponent } from './pages/auth/auth.component';
import { MainPageComponent } from './pages/main-page/main-page.component';
import { BlogComponent } from './pages/blog/blog.component';
import { adminGuard, userGuard } from './custom-services/guard/auth.guard';
import { AboutComponent } from './pages/about/about.component';
import { BlogCardDetailComponent } from './components/blog-card-detail/blog-card-detail.component';
import { TestComponent } from './pages/test/test.component';
import { TestCardDetailComponent } from './components/test-card-detail/test-card-detail.component';
import { TestResultComponent } from './components/test-result/test-result.component';
import { SessionsComponent } from './pages/sessions/sessions.component';
import { TokenService } from './custom-services/token/token.service';
import { TestSolverComponent } from './components/test-solver/test-solver.component';

const routes: Routes = [
  { 
    path: 'register', 
    component: AuthComponent,
    canActivate: [() => {
      const tokenService = inject(TokenService);
      const router = inject(Router);
      if (!tokenService.isTokenNotValid()) {
        router.navigate(['main']);
        return false;
      }
      return true;
    }]
  },
  { 
    path: 'login', 
    component: AuthComponent,
    canActivate: [() => {
      const tokenService = inject(TokenService);
      const router = inject(Router);
      if (!tokenService.isTokenNotValid()) {
        router.navigate(['main']); 
        return false;
      }
      return true;
    }]
  },
  { path: 'activate-account', component: AuthComponent },
  { path: 'main', component: MainPageComponent },
  { path: 'about', component: AboutComponent },
  { path: '', component: MainPageComponent },
  { path: 'blog', component: BlogComponent },
  { path: 'blog/:id', component: BlogCardDetailComponent },
  { path: 'test', component: TestComponent },
  { path: 'public-test/:id', component: TestSolverComponent },
  { path: 'user-test/:id', component: TestSolverComponent },
  { path: 'test/:id', component: TestCardDetailComponent },
  { path: 'test-result', component: TestResultComponent },
  { path: 'session', component: SessionsComponent },
  {
    path: 'user',
    loadChildren: () =>
      import('./modules/user/user.module').then((m) => m.UserModule),
    canActivate: [userGuard],
  },
  {
    path: 'admin',
    loadChildren: () =>
      import('./modules/admin/admin.module').then((m) => m.AdminModule),
    canActivate: [adminGuard],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
