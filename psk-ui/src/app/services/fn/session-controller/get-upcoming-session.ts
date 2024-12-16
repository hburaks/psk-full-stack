/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SessionResponse } from '../../models/session-response';

export interface GetUpcomingSession$Params {
}

export function getUpcomingSession(http: HttpClient, rootUrl: string, params?: GetUpcomingSession$Params, context?: HttpContext): Observable<StrictHttpResponse<SessionResponse>> {
  const rb = new RequestBuilder(rootUrl, getUpcomingSession.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<SessionResponse>;
    })
  );
}

getUpcomingSession.PATH = '/sessions/upcoming-session';
