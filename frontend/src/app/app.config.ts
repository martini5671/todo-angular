import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { withNgxsReduxDevtoolsPlugin } from '@ngxs/devtools-plugin';
import { withNgxsRouterPlugin } from '@ngxs/router-plugin';
import { provideStore } from '@ngxs/store';
import { AuthState } from './state/auth.state';
import { provideHttpClient } from '@angular/common/http';
import { provideHotToastConfig } from '@ngxpert/hot-toast';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHotToastConfig(), // @ngxpert/hot-toast providers
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideStore([AuthState], withNgxsReduxDevtoolsPlugin(), withNgxsRouterPlugin()),
    provideHttpClient(),
    provideHotToastConfig(),
  ],
};
