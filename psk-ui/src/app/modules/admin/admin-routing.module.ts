import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AdminPanelComponent} from "./pages/admin-panel/admin-panel.component";
import { AdminSessionsComponent } from './components/admin-sessions/admin-sessions.component';
import { EditUserSessionComponent } from './components/edit-user-session/edit-user-session.component';

const routes: Routes = [
  { path: 'panel', component: AdminPanelComponent },
  { path: 'sessions', component: AdminSessionsComponent },
  { path: 'edit-session', component: EditUserSessionComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {

}
