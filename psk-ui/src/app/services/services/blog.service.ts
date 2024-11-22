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

import { deleteBlog } from '../fn/blog/delete-blog';
import { DeleteBlog$Params } from '../fn/blog/delete-blog';
import { findAllBlogs } from '../fn/blog/find-all-blogs';
import { FindAllBlogs$Params } from '../fn/blog/find-all-blogs';
import { findAllBlogsShareable } from '../fn/blog/find-all-blogs-shareable';
import { FindAllBlogsShareable$Params } from '../fn/blog/find-all-blogs-shareable';
import { PageResponseBlogResponse } from '../models/page-response-blog-response';
import { saveBlog } from '../fn/blog/save-blog';
import { SaveBlog$Params } from '../fn/blog/save-blog';
import { updateBlog } from '../fn/blog/update-blog';
import { UpdateBlog$Params } from '../fn/blog/update-blog';
import { updateShareableStatus } from '../fn/blog/update-shareable-status';
import { UpdateShareableStatus$Params } from '../fn/blog/update-shareable-status';

@Injectable({ providedIn: 'root' })
export class BlogService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `updateBlog()` */
  static readonly UpdateBlogPath = '/v2/blogs/update/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateBlog()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateBlog$Response(params: UpdateBlog$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return updateBlog(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateBlog$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateBlog(params: UpdateBlog$Params, context?: HttpContext): Observable<number> {
    return this.updateBlog$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `updateShareableStatus()` */
  static readonly UpdateShareableStatusPath = '/v2/blogs/update-shareable-status';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateShareableStatus()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateShareableStatus$Response(params: UpdateShareableStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return updateShareableStatus(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateShareableStatus$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateShareableStatus(params: UpdateShareableStatus$Params, context?: HttpContext): Observable<number> {
    return this.updateShareableStatus$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `saveBlog()` */
  static readonly SaveBlogPath = '/v2/blogs/save';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveBlog()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveBlog$Response(params?: SaveBlog$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return saveBlog(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveBlog$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveBlog(params?: SaveBlog$Params, context?: HttpContext): Observable<number> {
    return this.saveBlog$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `findAllBlogsShareable()` */
  static readonly FindAllBlogsShareablePath = '/v3/blogs/get-all-blogs';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllBlogsShareable()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBlogsShareable$Response(params?: FindAllBlogsShareable$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseBlogResponse>> {
    return findAllBlogsShareable(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllBlogsShareable$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBlogsShareable(params?: FindAllBlogsShareable$Params, context?: HttpContext): Observable<PageResponseBlogResponse> {
    return this.findAllBlogsShareable$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseBlogResponse>): PageResponseBlogResponse => r.body)
    );
  }

  /** Path part for operation `findAllBlogs()` */
  static readonly FindAllBlogsPath = '/v2/blogs/get-all-blogs';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllBlogs()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBlogs$Response(params?: FindAllBlogs$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseBlogResponse>> {
    return findAllBlogs(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllBlogs$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBlogs(params?: FindAllBlogs$Params, context?: HttpContext): Observable<PageResponseBlogResponse> {
    return this.findAllBlogs$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseBlogResponse>): PageResponseBlogResponse => r.body)
    );
  }

  /** Path part for operation `deleteBlog()` */
  static readonly DeleteBlogPath = '/v2/blogs/delete/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteBlog()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteBlog$Response(params: DeleteBlog$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return deleteBlog(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteBlog$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteBlog(params: DeleteBlog$Params, context?: HttpContext): Observable<boolean> {
    return this.deleteBlog$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
    );
  }

}
