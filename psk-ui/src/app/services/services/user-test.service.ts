/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { getCurrentUserTests } from '../fn/user-test/get-current-user-tests';
import { GetCurrentUserTests$Params } from '../fn/user-test/get-current-user-tests';
import { getUserTest } from '../fn/user-test/get-user-test';
import { GetUserTest$Params } from '../fn/user-test/get-user-test';
import { submitAndCompleteTest } from '../fn/user-test/submit-and-complete-test';
import { SubmitAndCompleteTest$Params } from '../fn/user-test/submit-and-complete-test';
import { SubmitTestResponse } from '../models/submit-test-response';
import { UserTestListResponse } from '../models/user-test-list-response';
import { UserTestResponse } from '../models/user-test-response';


/**
 * User endpoints for managing assigned tests
 */
@Injectable({ providedIn: 'root' })
export class UserTestService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `submitAndCompleteTest()` */
  static readonly SubmitAndCompleteTestPath = '/v1/user-tests/{id}/submit';

  /**
   * Submit all answers and complete test in one operation.
   *
   *
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `submitAndCompleteTest()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  submitAndCompleteTest$Response(params: SubmitAndCompleteTest$Params, context?: HttpContext): Observable<StrictHttpResponse<SubmitTestResponse>> {
    return submitAndCompleteTest(this.http, this.rootUrl, params, context);
  }

  /**
   * Submit all answers and complete test in one operation.
   *
   *
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `submitAndCompleteTest$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  submitAndCompleteTest(params: SubmitAndCompleteTest$Params, context?: HttpContext): Observable<SubmitTestResponse> {
    return this.submitAndCompleteTest$Response(params, context).pipe(
      map((r: StrictHttpResponse<SubmitTestResponse>): SubmitTestResponse => r.body)
    );
  }

  /** Path part for operation `getCurrentUserTests()` */
  static readonly GetCurrentUserTestsPath = '/v1/user-tests';

  /**
   * Get current user's tests.
   *
   *
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getCurrentUserTests()` instead.
   *
   * This method doesn't expect any request body.
   */
  getCurrentUserTests$Response(params?: GetCurrentUserTests$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<UserTestListResponse>>> {
    return getCurrentUserTests(this.http, this.rootUrl, params, context);
  }

  /**
   * Get current user's tests.
   *
   *
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getCurrentUserTests$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getCurrentUserTests(params?: GetCurrentUserTests$Params, context?: HttpContext): Observable<Array<UserTestListResponse>> {
    return this.getCurrentUserTests$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<UserTestListResponse>>): Array<UserTestListResponse> => r.body)
    );
  }

  /** Path part for operation `getUserTest()` */
  static readonly GetUserTestPath = '/v1/user-tests/{id}';

  /**
   * Get specific user test.
   *
   *
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getUserTest()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserTest$Response(params: GetUserTest$Params, context?: HttpContext): Observable<StrictHttpResponse<UserTestResponse>> {
    return getUserTest(this.http, this.rootUrl, params, context);
  }

  /**
   * Get specific user test.
   *
   *
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getUserTest$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getUserTest(params: GetUserTest$Params, context?: HttpContext): Observable<UserTestResponse> {
    return this.getUserTest$Response(params, context).pipe(
      map((r: StrictHttpResponse<UserTestResponse>): UserTestResponse => r.body)
    );
  }

}
