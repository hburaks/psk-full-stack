import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {UserRoutingModule} from './user-routing.module';
import { SharedModule } from '../shared/shared.module';
import { UserPanelComponent } from './pages/user-panel/user-panel.component';
import { MyUpcomingSessionComponent } from './pages/my-upcoming-session/my-upcoming-session.component';


@NgModule({
  declarations: [UserPanelComponent, MyUpcomingSessionComponent],
  imports: [
    CommonModule,
    UserRoutingModule,
    SharedModule,
  ]
})
export class UserModule {
}
