import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from "./auth/auth.component";

const routes: Routes = [
  {path: 'register', component: AuthComponent},
  {path: 'login', component: AuthComponent},
  {path: 'activate-account', component: AuthComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
