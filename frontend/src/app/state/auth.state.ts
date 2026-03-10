import {inject, Injectable} from '@angular/core';
import {Action, Selector, State, StateContext} from '@ngxs/store';
import {LoginAction} from './auth.actions';
import {AuthService} from '../services/auth';
import {tap} from 'rxjs';

export interface AuthStateModel {
  token: string | null
}

@State<AuthStateModel>({
  name: 'auth', // name of state slice
  defaults: {token: null} // default value
})
@Injectable() // dzieki temu sa się wstrzykiwać zależności
export class AuthState {

  private authService = inject(AuthService);

  @Selector()
  static getToken(state: AuthStateModel) {
    return state.token;
  }

  @Action(LoginAction)
  login(ctx: StateContext<AuthStateModel>, {email, password}: LoginAction) {
    console.log("login action in AuthState received email:" + email + " password:" + password);
    return this.authService.login({email, password}).pipe(
      tap(response => {
        ctx.patchState({token: response.token});
      })
    )
  }

}
