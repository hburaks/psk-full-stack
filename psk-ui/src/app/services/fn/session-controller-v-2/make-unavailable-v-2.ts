/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PublicSessionResponse } from '../../models/public-session-response';

export interface MakeUnavailableV2$Params {
      body: Array<string>
}

export function makeUnavailableV2(http: HttpClient, rootUrl: string, params: MakeUnavailableV2$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<PublicSessionResponse>>> {
  const rb = new RequestBuilder(rootUrl, makeUnavailableV2.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<PublicSessionResponse>>;
    })
  );
}

makeUnavailableV2.PATH = '/v2/sessions/make-unavailable';
