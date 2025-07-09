import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'answerTypeDisplay'
})
export class AnswerTypeDisplayPipe implements PipeTransform {

  transform(answerType: string | undefined | null): string {
    if (!answerType) {
      return '';
    }
    
    switch(answerType) {
      case 'ANSWER_A': return 'A';
      case 'ANSWER_B': return 'B';
      case 'ANSWER_C': return 'C';
      case 'ANSWER_D': return 'D';
      case 'ANSWER_E': return 'E';
      default: return answerType;
    }
  }
}