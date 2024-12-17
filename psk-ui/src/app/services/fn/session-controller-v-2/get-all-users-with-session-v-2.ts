/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseUserWithIncomingSessionResponse } from '../../models/page-response-user-with-incoming-session-response';

export interface GetAllUsersWithSessionV2$Params {
  page: number;
  size: number;
}

export function getAllUsersWithSessionV2(http: HttpClient, rootUrl: string, params: GetAllUsersWithSessionV2$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseUserWithIncomingSessionResponse>> {
  const rb = new RequestBuilder(rootUrl, getAllUsersWithSessionV2.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseUserWithIncomingSessionResponse>;
    })
  );
}

getAllUsersWithSessionV2.PATH = '/v2/sessions/users-with-sessions';
