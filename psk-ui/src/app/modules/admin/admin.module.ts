import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AdminRoutingModule} from "./admin-routing.module";
import {AdminPanelComponent} from './pages/admin-panel/admin-panel.component';
import { UserNavUnderlineComponent } from '../../components/user-nav-underline/user-nav-underline.component';
import { UpcomingSessionComponent } from './components/upcoming-session/upcoming-session.component';


@NgModule({
  declarations: [AdminPanelComponent, UserNavUnderlineComponent, UpcomingSessionComponent],
  imports: [CommonModule, AdminRoutingModule],
})
export class AdminModule {}
