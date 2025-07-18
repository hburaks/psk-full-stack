/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SubmitTestRequest } from '../../models/submit-test-request';
import { SubmitTestResponse } from '../../models/submit-test-response';

export interface SubmitAndCompleteTest$Params {
  id: number;
      body: SubmitTestRequest
}

export function submitAndCompleteTest(http: HttpClient, rootUrl: string, params: SubmitAndCompleteTest$Params, context?: HttpContext): Observable<StrictHttpResponse<SubmitTestResponse>> {
  const rb = new RequestBuilder(rootUrl, submitAndCompleteTest.PATH, 'post');
  if (params) {
    rb.path('id', params.id, {});
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<SubmitTestResponse>;
    })
  );
}

submitAndCompleteTest.PATH = '/v1/user-tests/{id}/submit';
