import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { withNgxsReduxDevtoolsPlugin } from '@ngxs/devtools-plugin';
import { withNgxsRouterPlugin } from '@ngxs/router-plugin';
import { provideStore } from '@ngxs/store';
import { AuthState } from './state/auth.state';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import { provideHotToastConfig } from '@ngxpert/hot-toast';
import {withNgxsStoragePlugin} from '@ngxs/storage-plugin';
import {authInterceptor} from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHotToastConfig(), // @ngxpert/hot-toast providers
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideStore([AuthState], withNgxsReduxDevtoolsPlugin(), withNgxsRouterPlugin(),
      withNgxsStoragePlugin({
        keys: ['auth'] // this can be * to persits everything in local storage this can also be state class
      })),
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
};
