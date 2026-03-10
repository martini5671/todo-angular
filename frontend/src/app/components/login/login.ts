import {Component, inject} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {AuthService} from '../../services/auth';
import {Router} from '@angular/router';
import {Store} from '@ngxs/store';
import {LoginAction} from '../../state/auth.actions';

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

  private authService = inject(AuthService);
  private fb = inject(FormBuilder)
  private store = inject(Store)
  private router = inject(Router)

  protected loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  loading = false;
  error?: string;

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = undefined;

    const email = this.loginForm.value.email!
    const password = this.loginForm.value.password!

    this.store.dispatch(new LoginAction(email, password))
      .subscribe({
        next: () => {
          void this.router.navigate(['/dashboard'])
        },
        error: () => {
          console.log("login failed!")
        }
      })

  }
}
