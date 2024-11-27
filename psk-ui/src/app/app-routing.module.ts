import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from "./pages/auth/auth.component";
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {BlogComponent} from "./pages/blog/blog.component";
import { adminGuard, userGuard } from './custom-services/guard/auth.guard';
import { AboutComponent } from './pages/about/about.component';
import { BlogCardDetailComponent } from './components/blog-card-detail/blog-card-detail.component';

const routes: Routes = [
  { path: 'register', component: AuthComponent },
  { path: 'login', component: AuthComponent },
  { path: 'activate-account', component: AuthComponent },
  { path: 'main', component: MainPageComponent },
  { path: 'about', component: AboutComponent },
  { path: '', component: MainPageComponent },
  { path: 'blog', component: BlogComponent },
  { path: 'blog/:id', component: BlogCardDetailComponent},
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
  exports: [RouterModule]
})
export class AppRoutingModule {
}