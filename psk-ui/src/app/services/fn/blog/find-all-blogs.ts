/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import {HttpClient, HttpContext, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {StrictHttpResponse} from '../../strict-http-response';
import {RequestBuilder} from '../../request-builder';

import {PageResponseBlogResponse} from '../../models/page-response-blog-response';

export interface FindAllBlogs$Params {
  page?: number;
  size?: number;
}

export function findAllBlogs(http: HttpClient, rootUrl: string, params?: FindAllBlogs$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseBlogResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllBlogs.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({responseType: 'json', accept: 'application/json', context})
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseBlogResponse>;
    })
  );
}

findAllBlogs.PATH = '/v2/blogs/get-all-blogs';
