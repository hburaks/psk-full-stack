import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TestService {
  constructor() {}

  formatAnswerDistribution(answerDistribution: any) {
    if (!answerDistribution) {
      return '';
    }

    return Object.entries(answerDistribution)
      .map(
        ([key, value]) =>
          `${key.replace('ANSWER_', '').replace('_', ' ')}: ${value}`
      )
      .join(' ');
  }
}
