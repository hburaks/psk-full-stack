/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationParams } from './api-configuration';

import { TestService } from './services/test.service';
import { BlogService } from './services/blog.service';
import { UserService } from './services/user.service';
import { SessionControllerV2Service } from './services/session-controller-v-2.service';
import { SessionControllerService } from './services/session-controller.service';
import { AuthenticationService } from './services/authentication.service';
import { SessionControllerV3Service } from './services/session-controller-v-3.service';
import { FileControllerService } from './services/file-controller.service';

/**
 * Module that provides all services and configuration.
 */
@NgModule({
  imports: [],
  exports: [],
  declarations: [],
  providers: [
    TestService,
    BlogService,
    UserService,
    SessionControllerV2Service,
    SessionControllerService,
    AuthenticationService,
    SessionControllerV3Service,
    FileControllerService,
    ApiConfiguration
  ],
})
export class ApiModule {
  static forRoot(params: ApiConfigurationParams): ModuleWithProviders<ApiModule> {
    return {
      ngModule: ApiModule,
      providers: [
        {
          provide: ApiConfiguration,
          useValue: params
        }
      ]
    }
  }

  constructor( 
    @Optional() @SkipSelf() parentModule: ApiModule,
    @Optional() http: HttpClient
  ) {
    if (parentModule) {
      throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
    }
    if (!http) {
      throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
      'See also https://github.com/angular/angular/issues/20575');
    }
  }
}
