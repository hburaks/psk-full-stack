/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { AdminTestResponse } from '../../models/admin-test-response';

export interface GetAllTest$Params {
}

export function getAllTest(http: HttpClient, rootUrl: string, params?: GetAllTest$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<AdminTestResponse>>> {
  const rb = new RequestBuilder(rootUrl, getAllTest.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<AdminTestResponse>>;
    })
  );
}

getAllTest.PATH = '/v2/test/all';
