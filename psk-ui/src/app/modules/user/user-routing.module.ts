import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserPanelComponent} from "./pages/user-panel/user-panel.component";

const routes: Routes = [
  {path: 'panel', component: UserPanelComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule {
}
