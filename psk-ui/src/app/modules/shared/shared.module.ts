import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WeeklySessionCalendarComponent } from 'src/app/components/weekly-session-calendar/weekly-session-calendar.component';
import { BlogCardDetailComponent } from 'src/app/components/blog-card-detail/blog-card-detail.component';
import { EmptyCardComponent } from 'src/app/components/empty-card/empty-card.component';
import { GenericCardDetailComponent } from 'src/app/components/generic-card-detail/generic-card-detail.component';
import { BlogCardListComponent } from 'src/app/components/blog-card-list/blog-card-list.component';
import { BlogCardComponent } from 'src/app/components/blog-card/blog-card.component';
import { TestCardListComponent } from 'src/app/components/test-card-list/test-card-list.component';
import { TestCardComponent } from 'src/app/components/test-card/test-card.component';
import { TestCardDetailComponent } from 'src/app/components/test-card-detail/test-card-detail.component';
import { TestResultComponent } from 'src/app/components/test-result/test-result.component';
import { EditCommentComponent } from '../admin/components/edit-comment/edit-comment.component';
import { EditUserComponent } from 'src/app/components/edit-user/edit-user.component';
@NgModule({
  declarations: [
    WeeklySessionCalendarComponent,
    BlogCardListComponent,
    BlogCardDetailComponent,
    EmptyCardComponent,
    GenericCardDetailComponent,
    BlogCardComponent,
    TestCardListComponent,
    TestCardComponent,
    TestCardDetailComponent,
    TestResultComponent,
    EditCommentComponent,
    EditUserComponent,
  ],
  imports: [CommonModule, FormsModule],
  exports: [
    WeeklySessionCalendarComponent,
    BlogCardListComponent,
    BlogCardDetailComponent,
    EmptyCardComponent,
    GenericCardDetailComponent,
    BlogCardComponent,
    TestCardListComponent,
    TestCardComponent,
    TestCardDetailComponent,
    TestResultComponent,
    EditCommentComponent,
    EditUserComponent,
  ],
})
export class SharedModule {}
