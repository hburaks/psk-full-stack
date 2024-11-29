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

import { assignTestToUserV2 } from '../fn/test/assign-test-to-user-v-2';
import { AssignTestToUserV2$Params } from '../fn/test/assign-test-to-user-v-2';
import { checkPublicTestAnswer } from '../fn/test/check-public-test-answer';
import { CheckPublicTestAnswer$Params } from '../fn/test/check-public-test-answer';
import { createPublicTestV2 } from '../fn/test/create-public-test-v-2';
import { CreatePublicTestV2$Params } from '../fn/test/create-public-test-v-2';
import { getAllMyTests } from '../fn/test/get-all-my-tests';
import { GetAllMyTests$Params } from '../fn/test/get-all-my-tests';
import { getAllPublicTests } from '../fn/test/get-all-public-tests';
import { GetAllPublicTests$Params } from '../fn/test/get-all-public-tests';
import { getAllTestsAssignedToUserV2 } from '../fn/test/get-all-tests-assigned-to-user-v-2';
import { GetAllTestsAssignedToUserV2$Params } from '../fn/test/get-all-tests-assigned-to-user-v-2';
import { getPublicTestById } from '../fn/test/get-public-test-by-id';
import { GetPublicTestById$Params } from '../fn/test/get-public-test-by-id';
import { MyTestResponse } from '../models/my-test-response';
import { PublicTestAdminResponse } from '../models/public-test-admin-response';
import { PublicTestAnswerCommentResponse } from '../models/public-test-answer-comment-response';
import { PublicTestResponse } from '../models/public-test-response';
import { saveMyTestAnswer } from '../fn/test/save-my-test-answer';
import { SaveMyTestAnswer$Params } from '../fn/test/save-my-test-answer';
import { updatePublicTestAvailabilityV2 } from '../fn/test/update-public-test-availability-v-2';
import { UpdatePublicTestAvailabilityV2$Params } from '../fn/test/update-public-test-availability-v-2';
import { updatePublicTestV2 } from '../fn/test/update-public-test-v-2';
import { UpdatePublicTestV2$Params } from '../fn/test/update-public-test-v-2';
import { UserTestForAdminResponse } from '../models/user-test-for-admin-response';

@Injectable({ providedIn: 'root' })
export class TestService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `updatePublicTestV2()` */
  static readonly UpdatePublicTestV2Path = '/v2/test/update';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updatePublicTestV2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updatePublicTestV2$Response(params: UpdatePublicTestV2$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestAdminResponse>> {
    return updatePublicTestV2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updatePublicTestV2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updatePublicTestV2(params: UpdatePublicTestV2$Params, context?: HttpContext): Observable<PublicTestAdminResponse> {
    return this.updatePublicTestV2$Response(params, context).pipe(
      map((r: StrictHttpResponse<PublicTestAdminResponse>): PublicTestAdminResponse => r.body)
    );
  }

  /** Path part for operation `updatePublicTestAvailabilityV2()` */
  static readonly UpdatePublicTestAvailabilityV2Path = '/v2/test/update-availability';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updatePublicTestAvailabilityV2()` instead.
   *
   * This method doesn't expect any request body.
   */
  updatePublicTestAvailabilityV2$Response(params: UpdatePublicTestAvailabilityV2$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestAdminResponse>> {
    return updatePublicTestAvailabilityV2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updatePublicTestAvailabilityV2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  updatePublicTestAvailabilityV2(params: UpdatePublicTestAvailabilityV2$Params, context?: HttpContext): Observable<PublicTestAdminResponse> {
    return this.updatePublicTestAvailabilityV2$Response(params, context).pipe(
      map((r: StrictHttpResponse<PublicTestAdminResponse>): PublicTestAdminResponse => r.body)
    );
  }

  /** Path part for operation `checkPublicTestAnswer()` */
  static readonly CheckPublicTestAnswerPath = '/v3/test/check-answer';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `checkPublicTestAnswer()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  checkPublicTestAnswer$Response(params: CheckPublicTestAnswer$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestAnswerCommentResponse>> {
    return checkPublicTestAnswer(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `checkPublicTestAnswer$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  checkPublicTestAnswer(params: CheckPublicTestAnswer$Params, context?: HttpContext): Observable<PublicTestAnswerCommentResponse> {
    return this.checkPublicTestAnswer$Response(params, context).pipe(
      map((r: StrictHttpResponse<PublicTestAnswerCommentResponse>): PublicTestAnswerCommentResponse => r.body)
    );
  }

  /** Path part for operation `createPublicTestV2()` */
  static readonly CreatePublicTestV2Path = '/v2/test/create';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createPublicTestV2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPublicTestV2$Response(params: CreatePublicTestV2$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestAdminResponse>> {
    return createPublicTestV2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createPublicTestV2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createPublicTestV2(params: CreatePublicTestV2$Params, context?: HttpContext): Observable<PublicTestAdminResponse> {
    return this.createPublicTestV2$Response(params, context).pipe(
      map((r: StrictHttpResponse<PublicTestAdminResponse>): PublicTestAdminResponse => r.body)
    );
  }

  /** Path part for operation `assignTestToUserV2()` */
  static readonly AssignTestToUserV2Path = '/v2/test/assign-test';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `assignTestToUserV2()` instead.
   *
   * This method doesn't expect any request body.
   */
  assignTestToUserV2$Response(params: AssignTestToUserV2$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return assignTestToUserV2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `assignTestToUserV2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  assignTestToUserV2(params: AssignTestToUserV2$Params, context?: HttpContext): Observable<boolean> {
    return this.assignTestToUserV2$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
    );
  }

  /** Path part for operation `saveMyTestAnswer()` */
  static readonly SaveMyTestAnswerPath = '/tests/my-tests/save-answer';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveMyTestAnswer()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveMyTestAnswer$Response(params: SaveMyTestAnswer$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return saveMyTestAnswer(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveMyTestAnswer$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveMyTestAnswer(params: SaveMyTestAnswer$Params, context?: HttpContext): Observable<boolean> {
    return this.saveMyTestAnswer$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
    );
  }

  /** Path part for operation `getAllPublicTests()` */
  static readonly GetAllPublicTestsPath = '/v3/test';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllPublicTests()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllPublicTests$Response(params?: GetAllPublicTests$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<PublicTestResponse>>> {
    return getAllPublicTests(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllPublicTests$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllPublicTests(params?: GetAllPublicTests$Params, context?: HttpContext): Observable<Array<PublicTestResponse>> {
    return this.getAllPublicTests$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<PublicTestResponse>>): Array<PublicTestResponse> => r.body)
    );
  }

  /** Path part for operation `getPublicTestById()` */
  static readonly GetPublicTestByIdPath = '/v3/test/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getPublicTestById()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPublicTestById$Response(params: GetPublicTestById$Params, context?: HttpContext): Observable<StrictHttpResponse<PublicTestResponse>> {
    return getPublicTestById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getPublicTestById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getPublicTestById(params: GetPublicTestById$Params, context?: HttpContext): Observable<PublicTestResponse> {
    return this.getPublicTestById$Response(params, context).pipe(
      map((r: StrictHttpResponse<PublicTestResponse>): PublicTestResponse => r.body)
    );
  }

  /** Path part for operation `getAllTestsAssignedToUserV2()` */
  static readonly GetAllTestsAssignedToUserV2Path = '/v2/test/user-tests/{userId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllTestsAssignedToUserV2()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllTestsAssignedToUserV2$Response(params: GetAllTestsAssignedToUserV2$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<UserTestForAdminResponse>>> {
    return getAllTestsAssignedToUserV2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllTestsAssignedToUserV2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllTestsAssignedToUserV2(params: GetAllTestsAssignedToUserV2$Params, context?: HttpContext): Observable<Array<UserTestForAdminResponse>> {
    return this.getAllTestsAssignedToUserV2$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<UserTestForAdminResponse>>): Array<UserTestForAdminResponse> => r.body)
    );
  }

  /** Path part for operation `getAllMyTests()` */
  static readonly GetAllMyTestsPath = '/tests/my-tests';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllMyTests()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllMyTests$Response(params?: GetAllMyTests$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<MyTestResponse>>> {
    return getAllMyTests(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllMyTests$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllMyTests(params?: GetAllMyTests$Params, context?: HttpContext): Observable<Array<MyTestResponse>> {
    return this.getAllMyTests$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<MyTestResponse>>): Array<MyTestResponse> => r.body)
    );
  }

}
