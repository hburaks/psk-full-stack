/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PublicTestAdminResponse } from '../../models/public-test-admin-response';

export interface UpdatePublicTestAvailabilityV2$Params {
  testId: number;
  isAvailable: boolean;
}

export function updatePublicTestAvailabilityV2(http: HttpClient, rootUrl: string, params: UpdatePublicTestAvailabilityV2$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestAdminResponse>> {
  const rb = new RequestBuilder(rootUrl, updatePublicTestAvailabilityV2.PATH, 'put');
  if (params) {
    rb.query('testId', params.testId, {});
    rb.query('isAvailable', params.isAvailable, {});
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

updatePublicTestAvailabilityV2.PATH = '/v2/test/update-availability';