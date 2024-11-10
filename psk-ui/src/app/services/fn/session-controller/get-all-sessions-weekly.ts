/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import {HttpClient, HttpContext, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {StrictHttpResponse} from '../../strict-http-response';
import {RequestBuilder} from '../../request-builder';

import {PublicSessionResponse} from '../../models/public-session-response';

export interface GetAllSessionsWeekly$Params {
  dateTime: string;
}

export function getAllSessionsWeekly(http: HttpClient, rootUrl: string, params: GetAllSessionsWeekly$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<PublicSessionResponse>>> {
  const rb = new RequestBuilder(rootUrl, getAllSessionsWeekly.PATH, 'get');
  if (params) {
    rb.query('dateTime', params.dateTime, {});
  }

  return http.request(
    rb.build({responseType: 'json', accept: 'application/json', context})
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<PublicSessionResponse>>;
    })
  );
}

getAllSessionsWeekly.PATH = '/sessions/weekly';
