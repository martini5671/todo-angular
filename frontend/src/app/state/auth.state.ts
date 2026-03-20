import {inject, Injectable} from '@angular/core';
import {Action, Selector, State, StateContext} from '@ngxs/store';
import {LoginAction, LogoutAction} from './auth.actions';
import {catchError, tap, throwError} from 'rxjs';
import {UserControllerService} from '../modules/openapi';

export interface AuthStateModel {
  token: string | null
  roles: string[] | null
}

@State<AuthStateModel>({
  name: 'auth', // name of state slice
  defaults: {
    token: null,
    roles: null
  } // default value
})
@Injectable() // dzieki temu sa się wstrzykiwać zależności
export class AuthState {

  private userControllerService = inject(UserControllerService)

  @Selector()
  static getToken(state: AuthStateModel) {
    return state.token;
  }

  @Selector()
  static getRoles(state: AuthStateModel) {
    return state.roles;
  }

  @Action(LoginAction)
  login(ctx: StateContext<AuthStateModel>, {email, password}: LoginAction) {
    return this.userControllerService.login({
      username: email,
      password: password
    }).pipe(
      tap(response => {
        ctx.patchState({token: response.token, roles: response.roles});
      }),
      catchError(error => {
        console.error("Login failed", error);
        return throwError(() => error);
      })
    );
  }

  @Action(LogoutAction)
  logout(ctx: StateContext<AuthStateModel>) {
    ctx.patchState({token: null, roles: null});
  }

}
