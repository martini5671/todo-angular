import {Component, inject} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader} from '@angular/material/card';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {Router, RouterLink} from '@angular/router';
import {HotToastService} from '@ngxpert/hot-toast';

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
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  standalone: true
})
export class RegisterComponent {

  private router = inject(Router);
  private toaster = inject(HotToastService);
  private fb = inject(FormBuilder);

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

    // This is a stub for registration logic.
    // The user will connect it to the backend.
    const {email, password} = this.registerForm.value;
    console.log('Registering user:', {email, password});

    this.toaster.info('Registration logic not yet implemented. Connecting to backend...');

    // Simulate successful registration
    setTimeout(() => {
      this.toaster.success('Registration successful! Please log in.');
      void this.router.navigate(['/login']);
    }, 1000);
  }
}
