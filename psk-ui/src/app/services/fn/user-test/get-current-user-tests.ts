/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UserTestListResponse } from '../../models/user-test-list-response';

export interface GetCurrentUserTests$Params {
}

export function getCurrentUserTests(http: HttpClient, rootUrl: string, params?: GetCurrentUserTests$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<UserTestListResponse>>> {
  const rb = new RequestBuilder(rootUrl, getCurrentUserTests.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<UserTestListResponse>>;
    })
  );
}

getCurrentUserTests.PATH = '/v1/user-tests';
