export interface DailySchedule {
  date: string;
  hours: { [key: number]: boolean };
}
