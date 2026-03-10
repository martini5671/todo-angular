import {Component, inject} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader} from '@angular/material/card';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {Store} from '@ngxs/store';
import {LoginAction} from '../../state/auth.actions';
import {HotToastService} from '@ngxpert/hot-toast';
import {AuthState} from '../../state/auth.state';

@Component({
  selector: 'app-login',
  imports: [
    MatCard,
    MatCardHeader,
    MatCardContent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatError,
    MatButton
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private router = inject(Router);
  private toaster = inject(HotToastService);
  private fb = inject(FormBuilder)
  private store = inject(Store)

  protected loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    const email = this.loginForm.value.email!;
    const password = this.loginForm.value.password!;

    this.store.dispatch(new LoginAction(email, password))
      .subscribe({
        next: () => {
          this.toaster.success('Logged in successfully');
          const token = this.store.selectSnapshot(AuthState.getToken);
          console.log("Logged in token:", token);
          void this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error(err);
          this.toaster.error('Login failed');
        }
      });
  }
}
