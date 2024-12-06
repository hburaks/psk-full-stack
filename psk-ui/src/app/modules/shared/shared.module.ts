import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WeeklySessionCalendarComponent } from 'src/app/components/weekly-session-calendar/weekly-session-calendar.component';



@NgModule({
  declarations: [WeeklySessionCalendarComponent],
  imports: [CommonModule],
  exports: [WeeklySessionCalendarComponent],
})
export class SharedModule {}
