import { Injectable } from '@angular/core';
import { SessionResponseV2 } from 'src/app/services/models/session-response-v-2';

@Injectable({
  providedIn: 'root'
})
export class CustomSessionService {

  selectedSession: SessionResponseV2 | null = null;

  constructor() { }

  editSession(sessionId: number) {
    // TODO: seans düzenleme sayfasına yönlendirme yapılacak.
  }
}
