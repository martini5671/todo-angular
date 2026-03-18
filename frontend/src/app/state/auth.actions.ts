export class LoginAction {
  static readonly type = '[Auth] LoginAction';
  constructor(public email: string, public password: string) {
  }
}

export class LogoutAction {
  static readonly type = '[Auth] LogoutAction';
}
