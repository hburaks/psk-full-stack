/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

export interface UserAnswerResponse {
  answeredAt?: string;
  choiceAnswerType?: 'ANSWER_A' | 'ANSWER_B' | 'ANSWER_C' | 'ANSWER_D' | 'ANSWER_E';
  choiceId?: number;
  choiceText?: string;
  createdBy?: number;
  createdDate?: string;
  id?: number;
  isTestCompleted?: boolean;
  lastModifiedBy?: number;
  lastModifiedDate?: string;
  questionAnswerType?: 'ANSWER_A' | 'ANSWER_B' | 'ANSWER_C' | 'ANSWER_D' | 'ANSWER_E';
  questionId?: number;
  questionOrderIndex?: number;
  questionText?: string;
  testTemplateTitle?: string;
  textAnswer?: string;
  userTestId?: number;
}
