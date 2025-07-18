/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PublicTestResultResponse } from '../../models/public-test-result-response';
import { PublicTestSubmissionRequest } from '../../models/public-test-submission-request';

export interface SubmitTestAnswers$Params {
  id: number;
      body: PublicTestSubmissionRequest
}

export function submitTestAnswers(http: HttpClient, rootUrl: string, params: SubmitTestAnswers$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestResultResponse>> {
  const rb = new RequestBuilder(rootUrl, submitTestAnswers.PATH, 'post');
  if (params) {
    rb.path('id', params.id, {});
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PublicTestResultResponse>;
    })
  );
}

submitTestAnswers.PATH = '/v3/public/tests/{id}/submit';
