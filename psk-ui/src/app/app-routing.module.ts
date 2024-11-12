import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from "./pages/auth/auth.component";
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {BlogComponent} from "./pages/blog/blog.component";
import {adminGuard, userGuard} from "./services/guard/auth.guard";

const routes: Routes = [
  {path: 'register', component: AuthComponent},
  {path: 'login', component: AuthComponent},
  {path: 'activate-account', component: AuthComponent},
  {path: 'main', component: MainPageComponent},
  {path: '', component: MainPageComponent},
  {path: 'blog', component: BlogComponent},
  {
    path: 'user',
    loadChildren: () => import('./modules/user/user.module').then(m => m.UserModule),
    canActivate: [userGuard]
  },
  {
    path: 'admin',
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule),
    canActivate: [adminGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
