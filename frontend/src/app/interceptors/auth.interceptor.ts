import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Store } from '@ngxs/store';
import { AuthState } from '../state/auth.state';
import { catchError, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import {LogoutAction} from '../state/auth.actions';
import {Router} from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);
  const router = inject(Router);

  const token = store.selectSnapshot(AuthState.getToken);

  let request = req;

  if (token) {
    request = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 403) {
        console.log('Forbidden → logging out');

        // dispatch logout action (NGXS)
        store.dispatch(new LogoutAction());
        void router.navigate(['/login']);
      }

      return throwError(() => error);
    })
  );
};
