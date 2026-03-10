import {inject, Injectable} from '@angular/core';
import {Action, Selector, State, StateContext} from '@ngxs/store';
import {LoginAction} from './auth.actions';
import {catchError, take, tap, throwError} from 'rxjs';
import {UserControllerService} from '../modules/openapi';

export interface AuthStateModel {
  token: string | null
}

@State<AuthStateModel>({
  name: 'auth', // name of state slice
  defaults: {token: null} // default value
})
@Injectable() // dzieki temu sa się wstrzykiwać zależności
export class AuthState {

  private userControllerService = inject(UserControllerService)

  @Selector()
  static getToken(state: AuthStateModel) {
    return state.token;
  }

  @Action(LoginAction)
  login(ctx: StateContext<AuthStateModel>, { email, password }: LoginAction) {
    return this.userControllerService.login({
      username: email,
      password: password
    }).pipe(
      take(1), // <-- Add that
      tap(response => {
        console.log('Login response:', response.token);
        ctx.patchState({ token: response.token });
      }),
      catchError(error => {
        console.error("Login failed", error);
        return throwError(() => error);
      })
    );
  }
}
