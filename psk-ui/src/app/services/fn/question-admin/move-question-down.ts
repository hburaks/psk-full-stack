/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { QuestionResponse } from '../../models/question-response';

export interface MoveQuestionDown$Params {
  id: number;
}

export function moveQuestionDown(http: HttpClient, rootUrl: string, params: MoveQuestionDown$Params, context?: HttpContext): Observable<StrictHttpResponse<QuestionResponse>> {
  const rb = new RequestBuilder(rootUrl, moveQuestionDown.PATH, 'post');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<QuestionResponse>;
    })
  );
}

moveQuestionDown.PATH = '/v2/admin/questions/{id}/move-down';
