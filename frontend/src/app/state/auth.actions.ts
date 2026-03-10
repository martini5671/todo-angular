export class LoginAction {
  static readonly type = '[Auth] LoginAction';
  constructor(public email: string, public password: string) {
  }
}
