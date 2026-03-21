import {Component, inject, signal} from '@angular/core';
import {form, FormField, maxLength, minLength, required} from '@angular/forms/signals';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {TaskControllerService} from '../../modules/openapi';
import {catchError, of, tap} from 'rxjs';
import {HotToastService} from '@ngxpert/hot-toast';

@Component({
  selector: 'app-task-form',
  imports: [
    FormField,
    MatFormField,
    MatLabel,
    MatInput,
    MatError,
    MatButton
  ],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm {

  private taskService = inject(TaskControllerService)
  private toaster = inject(HotToastService);

  private taskModel = signal({
    title: "",
    description: ""
  });

  protected taskForm = form(this.taskModel,
    model => {
      required(model.title, {message: 'Title is required'});
      minLength(model.title, 3, {message: 'At minimum 3 characters required'});
      maxLength(model.description, 50, {message: 'Maximum 50 characters'});
    });

  protected onSubmit(event: SubmitEvent) {
    event.preventDefault();

    this.taskService.createTask({
      title: this.taskForm.title().value(),
      description: this.taskForm.description().value(),
    })
      .pipe(
        tap(() => {
          this.toaster.success('Task Created');
          this.taskModel.set({title: "", description: ""});
        }),
        catchError(err => {
          console.error(err);
          this.toaster.error(err.message);
          return of(null);
        })
      )
      .subscribe();
  }
}
