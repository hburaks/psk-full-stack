/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { PublicTestCommentRequest } from '../models/public-test-comment-request';
import { PublicTestQuestionRequest } from '../models/public-test-question-request';
export interface PublicTestRequest {
  comments?: Array<PublicTestCommentRequest>;
  cover?: Array<string>;
  isActive?: boolean;
  publicTestQuestionRequestList?: Array<PublicTestQuestionRequest>;
  subTitle?: string;
  testId?: number;
  title?: string;
}
