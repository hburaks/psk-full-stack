import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {UserRoutingModule} from './user-routing.module';
import { SharedModule } from '../shared/shared.module';
import { UserPanelComponent } from './pages/user-panel/user-panel.component';
import { MyUpcomingSessionComponent } from './pages/my-upcoming-session/my-upcoming-session.component';
import { UserTestCardComponent } from './components/user-test-card/user-test-card.component';
import { UserTestCardDetailComponent } from './components/user-test-card-detail/user-test-card-detail.component';
import { UserTestDetailComponent } from './components/user-test-detail/user-test-detail.component';
import { MySessionsComponent } from './pages/my-sessions/my-sessions.component';
import { CreateMySessionComponent } from './pages/create-my-session/create-my-session.component';


@NgModule({
  declarations: [UserPanelComponent, MyUpcomingSessionComponent, UserTestCardComponent, UserTestCardDetailComponent, UserTestDetailComponent, MySessionsComponent, CreateMySessionComponent],
  imports: [
    CommonModule,
    UserRoutingModule,
    SharedModule,
  ]
})
export class UserModule {
}
