/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PublicTestAdminResponse } from '../../models/public-test-admin-response';
import { PublicTestRequest } from '../../models/public-test-request';

export interface CreatePublicTestV2$Params {
      body: PublicTestRequest
}

export function createPublicTestV2(http: HttpClient, rootUrl: string, params: CreatePublicTestV2$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestAdminResponse>> {
  const rb = new RequestBuilder(rootUrl, createPublicTestV2.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PublicTestAdminResponse>;
    })
  );
}

createPublicTestV2.PATH = '/v2/test/create';