/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SessionResponseV2 } from '../../models/session-response-v-2';

export interface GetUpcomingSessionsV2$Params {
}

export function getUpcomingSessionsV2(http: HttpClient, rootUrl: string, params?: GetUpcomingSessionsV2$Params, context?: HttpContext): Observable<StrictHttpResponse<SessionResponseV2>> {
  const rb = new RequestBuilder(rootUrl, getUpcomingSessionsV2.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<SessionResponseV2>;
    })
  );
}

getUpcomingSessionsV2.PATH = '/v2/sessions/upcoming-session';
