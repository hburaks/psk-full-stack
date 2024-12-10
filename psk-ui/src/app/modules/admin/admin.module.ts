import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminPanelComponent } from './pages/admin-panel/admin-panel.component';
import { UserNavUnderlineComponent } from '../../components/user-nav-underline/user-nav-underline.component';
import { UpcomingSessionComponent } from './components/upcoming-session/upcoming-session.component';
import { AdminTestCardComponent } from './components/admin-test-card/admin-test-card.component';
import { FormsModule } from '@angular/forms';
import { ToastComponent } from 'src/app/components/toast/toast.component';
import { AdminTestResultComponent } from './components/admin-test-result/admin-test-result.component';
import { AdminSessionsComponent } from './components/admin-sessions/admin-sessions.component';
import { EditUserSessionComponent } from './components/edit-user-session/edit-user-session.component';
import { SharedModule } from '../shared/shared.module';
import { AdminAllSessionsComponent } from './pages/admin-all-sessions/admin-all-sessions.component';
import { AdminAllUsersComponent } from './pages/admin-all-users/admin-all-users.component';
import { EditBlogComponent } from './pages/edit-blog/edit-blog.component';
import { EditTestComponent } from './pages/edit-test/edit-test.component';

@NgModule({
  declarations: [
    AdminPanelComponent,
    UserNavUnderlineComponent,
    UpcomingSessionComponent,
    AdminTestCardComponent,
    ToastComponent,
    AdminTestResultComponent,
    AdminSessionsComponent,
    EditUserSessionComponent,
    AdminAllSessionsComponent,
    AdminAllUsersComponent,
    EditBlogComponent,
    EditTestComponent,
  ],
  imports: [CommonModule, AdminRoutingModule, FormsModule, SharedModule],
})
export class AdminModule {}
