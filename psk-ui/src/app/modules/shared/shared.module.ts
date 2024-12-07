import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WeeklySessionCalendarComponent } from 'src/app/components/weekly-session-calendar/weekly-session-calendar.component';
import { BlogCardDetailComponent } from 'src/app/components/blog-card-detail/blog-card-detail.component';
import { EmptyCardComponent } from 'src/app/components/empty-card/empty-card.component';
import { GenericCardDetailComponent } from 'src/app/components/generic-card-detail/generic-card-detail.component';
import { BlogCardListComponent } from 'src/app/components/blog-card-list/blog-card-list.component';
import { BlogCardComponent } from 'src/app/components/blog-card/blog-card.component';
@NgModule({
  declarations: [
    WeeklySessionCalendarComponent,
    BlogCardListComponent,
    BlogCardDetailComponent,
    EmptyCardComponent,
    GenericCardDetailComponent,
    BlogCardComponent,
  ],
  imports: [CommonModule, FormsModule],
  exports: [
    WeeklySessionCalendarComponent,
    BlogCardListComponent,
    BlogCardDetailComponent,
    EmptyCardComponent,
    GenericCardDetailComponent,
  ],
})
export class SharedModule {}
