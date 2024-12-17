import {
  Component,
  HostListener,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { UpdateSessionDateV2$Params } from 'src/app/services/fn/session-controller-v-2/update-session-date-v-2';
import { SessionResponseV2 } from 'src/app/services/models';
import { DailyCalendarResponse } from 'src/app/services/models/daily-calendar-response';
import { HourlySessionResponse } from 'src/app/services/models/hourly-session-response';
import { SessionControllerService } from 'src/app/services/services';
import { SessionControllerV2Service } from 'src/app/services/services/session-controller-v-2.service';
import { SessionControllerV3Service } from 'src/app/services/services/session-controller-v-3.service';

@Component({
  selector: 'app-weekly-session-calendar',
  templateUrl: './weekly-session-calendar.component.html',
  styleUrls: ['./weekly-session-calendar.component.scss'],
})
export class WeeklySessionCalendarComponent {
  isScreenSmall: boolean = false;
  isScreenMedium: boolean = false;
  isScreenLarge: boolean = false;

  currentSelectedWeek: Date = new Date();
  previousWeekAvailable: boolean = false;
  nextWeekAvailable: boolean = true;

  showAddSessionModal: boolean = false;

  @Input() isEdit: boolean = false;
  @Input() isCreateMySession: boolean = false;
  weeklySessionCalendar: DailyCalendarResponse[] = [];

  @Output() dateToUpdateSession = new EventEmitter<string | null>();

  constructor(
    private sessionControllerV3Service: SessionControllerV3Service,
    private sessionControllerService: SessionControllerService
  ) {
    this.updateScreenSize();
    this.getWeeklySessions();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.updateScreenSize();
  }

  addSession(date: string | null) {
    this.dateToUpdateSession.emit(date);
  }

  editSession(date: string | null) {
    if (this.isCreateMySession) {
      this.createMySession(date);
    } else {
      this.dateToUpdateSession.emit(date);
    }
  }

  createMySession(date: string | null) {
    this.sessionControllerService.createMySession({ date: date || '' }).subscribe({
      next: (response: number) => {
        console.log('Session created', response);
        window.location.reload();
      },
      error: (error) => {
        console.error('Error creating session', error);
      },
    });
  }

  getFormattedDate(day: DailyCalendarResponse): string {
    let session = day.sessions?.[0];
    const date = new Date(session?.date || '');
    const options = {
      day: '2-digit',
      month: '2-digit',
      year: '2-digit',
    } as const;
    return new Intl.DateTimeFormat('tr-TR', options).format(date);
  }

  getFormattedShortDate(day: DailyCalendarResponse): string {
    let session = day.sessions?.[0];
    const date = new Date(session?.date || '');
    const options = {
      day: '2-digit',
      month: '2-digit',
    } as const;
    return new Intl.DateTimeFormat('tr-TR', options).format(date);
  }

  getFormattedShortDayName(dayOfWeek: string): string {
    const longDayName = this.getFormattedLongDayName(dayOfWeek);
    if (longDayName == 'Pazartesi') {
      return 'Pzt';
    } else if (longDayName == 'Cumartesi' || longDayName == 'Pazar') {
      return 'Cts';
    }
    return longDayName.substring(0, 3);
  }

  getFormattedLongDayName(dayOfWeek: string): string {
    const dayMap = {
      MONDAY: 'Pazartesi',
      TUESDAY: 'Salı',
      WEDNESDAY: 'Çarşamba',
      THURSDAY: 'Perşembe',
      FRIDAY: 'Cuma',
      SATURDAY: 'Cumartesi',
    };
    return dayMap[dayOfWeek as keyof typeof dayMap] || '';
  }

  updateScreenSize() {
    this.isScreenSmall = false;
    this.isScreenMedium = false;
    this.isScreenLarge = false;

    this.isScreenSmall = window.innerWidth < 576;
    this.isScreenMedium = window.innerWidth < 768 && window.innerWidth >= 576;
    this.isScreenLarge = window.innerWidth >= 768;
  }

  formatSessionDate(date: string): string {
    return new Intl.DateTimeFormat('tr-TR', {
      hour: '2-digit',
      minute: '2-digit',
    }).format(new Date(date));
  }

  previousWeek() {
    const previousWeekDate = new Date(this.currentSelectedWeek);
    previousWeekDate.setDate(previousWeekDate.getDate() - 7);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (previousWeekDate <= today) {
      console.log('Cannot go back to previous week');
    } else {
      this.currentSelectedWeek = previousWeekDate;
      this.getWeeklySessions();
      this.previousWeekAvailable = this.checkPreviousWeekAvailable();
      this.nextWeekAvailable = this.checkNextWeekAvailable();
    }
  }

  checkPreviousWeekAvailable() {
    const previousWeekDate = new Date(this.currentSelectedWeek);
    previousWeekDate.setDate(previousWeekDate.getDate() - 7);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return previousWeekDate >= today;
  }

  nextWeek() {
    const nextWeekDate = new Date(this.currentSelectedWeek);
    nextWeekDate.setDate(nextWeekDate.getDate() + 7);
    const maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 28);
    if (nextWeekDate > maxDate) {
      console.log('Cannot go to next week');
    } else {
      this.currentSelectedWeek = nextWeekDate;
      this.nextWeekAvailable = this.checkNextWeekAvailable();
      this.previousWeekAvailable = this.checkPreviousWeekAvailable();
      this.getWeeklySessions();
    }
  }

  checkNextWeekAvailable() {
    const maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 28);
    const nextWeekDate = new Date(this.currentSelectedWeek);
    nextWeekDate.setDate(nextWeekDate.getDate() + 7);
    return nextWeekDate <= maxDate;
  }

  private getWeeklySessions() {
    const specificDate = this.currentSelectedWeek;
    const dateTime =
      specificDate.getUTCFullYear() +
      '-' +
      String(specificDate.getUTCMonth() + 1).padStart(2, '0') +
      '-' +
      String(specificDate.getUTCDate()).padStart(2, '0') +
      'T' +
      String(specificDate.getUTCHours()).padStart(2, '0') +
      ':' +
      String(specificDate.getUTCMinutes()).padStart(2, '0') +
      ':' +
      String(specificDate.getUTCSeconds()).padStart(2, '0');

    this.sessionControllerV3Service.getWeeklyCalendar({ dateTime }).subscribe({
      next: (sessions: Array<DailyCalendarResponse>) => {
        this.weeklySessionCalendar = sessions;
      },
      error: (error) => {
        console.error('Error fetching weekly sessions', error);
        this.weeklySessionCalendar = this.createDefaultWeeklyCalendar(false);
      },
    });
  }

  private createDefaultWeeklyCalendar(
    isAvailable: boolean
  ): DailyCalendarResponse[] {
    let defaultWeeklyCalendar: DailyCalendarResponse[] = [];
    for (let day of [
      'MONDAY',
      'TUESDAY',
      'WEDNESDAY',
      'THURSDAY',
      'FRIDAY',
      'SATURDAY',
    ]) {
      let sessions: HourlySessionResponse[] = [];
      for (let i = 10; i < 20; i++) {
        const date = new Date(this.currentSelectedWeek);
        const dayIndex = [
          'SUNDAY',
          'MONDAY',
          'TUESDAY',
          'WEDNESDAY',
          'THURSDAY',
          'FRIDAY',
          'SATURDAY',
        ].indexOf(day);
        date.setDate(date.getDate() - date.getDay() + dayIndex);
        date.setHours(i, 0, 0, 0);

        let session: HourlySessionResponse = {
          available: isAvailable,
          date: date.toISOString(),
        };
        sessions.push(session);
      }

      defaultWeeklyCalendar.push({
        dayOfWeek: day as
          | 'MONDAY'
          | 'TUESDAY'
          | 'WEDNESDAY'
          | 'THURSDAY'
          | 'FRIDAY'
          | 'SATURDAY'
          | 'SUNDAY',
        sessions: sessions,
      });
    }

    return defaultWeeklyCalendar;
  }
}
