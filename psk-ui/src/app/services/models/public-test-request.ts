/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { AdminTestCommentRequest } from '../models/admin-test-comment-request';
import { PublicTestQuestionRequest } from '../models/public-test-question-request';
export interface PublicTestRequest {
  comments?: Array<AdminTestCommentRequest>;
  image?: Blob;
  isActive?: boolean;
  publicTestQuestionRequestList?: Array<PublicTestQuestionRequest>;
  subTitle?: string;
  testId?: number;
  title?: string;
}
