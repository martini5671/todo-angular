import {Component, inject} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader} from '@angular/material/card';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {HotToastService} from '@ngxpert/hot-toast';
import {UserControllerService} from '../../modules/openapi';
import {catchError, EMPTY, tap} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-register',
  imports: [
    MatCard,
    MatCardHeader,
    MatCardContent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatError,
    MatButton,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  standalone: true
})
export class RegisterComponent {

  private router = inject(Router);
  private toaster = inject(HotToastService);
  private fb = inject(FormBuilder);
  private userService = inject(UserControllerService)

  protected registerForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required]
  }, {
    validators: (group) => {
      const password = group.get('password')?.value;
      const confirmPassword = group.get('confirmPassword')?.value;
      return password === confirmPassword ? null : {notSame: true};
    }
  });

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    this.userService.register({
      email: this.registerForm.value.email!,
      password: this.registerForm.value.password!
    }).pipe(
      tap(() => {
        this.toaster.success("New account was registered. " +
          "Please check email box to finalize");
      }),
      catchError(error => {
        console.log(error)
        if (error instanceof HttpErrorResponse) {
          if (error.status === 409) {
            this.toaster.error("Provided email already exists");
          }
        }
        return EMPTY;
      })
    )
      .subscribe()
  }
}
