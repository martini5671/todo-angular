import {Component, inject, OnInit, signal} from '@angular/core';
import {form, FormField, maxLength, minLength, required} from '@angular/forms/signals';
import {MatCheckbox} from '@angular/material/checkbox';
import {MatInput} from '@angular/material/input';
import {MatFormField, MatLabel, MatError} from '@angular/material/form-field';
import {TaskControllerService} from '../../modules/openapi';
import {catchError, of, tap} from 'rxjs';
import {HotToastService} from '@ngxpert/hot-toast';
import {ActivatedRoute} from '@angular/router';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-task-form',
  imports: [
    FormField,
    MatFormField,
    MatLabel,
    MatInput,
    MatError,
    MatCheckbox,
    MatButton
  ],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm implements OnInit {

  private taskService = inject(TaskControllerService)
  private toaster = inject(HotToastService);
  private activatedRoute = inject(ActivatedRoute);
  protected isEditMode = false;

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.isEditMode = !!id;
    if (id) {
      this.loadTask(Number(id));
    }
  }

  private taskModel = signal({
    title: "",
    description: "",
    done: false,
    userId: undefined as number | undefined
  });

  protected taskForm = form(this.taskModel,
    model => {
      required(model.title, {message: 'Title is required'});
      minLength(model.title, 3, {message: 'At minimum 3 characters required'});
      maxLength(model.description, 50, {message: 'Maximum 50 characters'});
    });

  protected onSubmit(event: SubmitEvent) {
    event.preventDefault();

    const taskData: any = {
      title: this.taskForm.title().value(),
      description: this.taskForm.description().value(),
    };

    if (this.isEditMode) {
      taskData.done = this.taskForm.done().value();
    }

    const request$ = this.isEditMode
      ? this.taskService.updateTask(Number(this.activatedRoute.snapshot.paramMap.get('id')), taskData)
      : this.taskService.createTask(taskData);

    request$
      .pipe(
        tap(() => {
          this.toaster.success(this.isEditMode ? 'Task Updated' : 'Task Created');
          if (!this.isEditMode) {
            this.taskModel.set({
              title: "",
              description: "",
              done: false,
              userId: undefined
            });
          }
        }),
        catchError(err => {
          console.error(err);
          this.toaster.error(err.message);
          return of(null);
        })
      )
      .subscribe();
  }

  private loadTask(id: number) {
    this.taskService.getTaskById(id)
      .pipe(
        tap(task => {
          this.taskModel.set({
            title: task.title,
            description: task.description || "",
            done: task.done || false,
            userId: task.userId
          });
        }),
        catchError(err => {
          console.error(err);
          this.toaster.error('Failed to load task');
          return of(null);
        })
      )
      .subscribe();
  }
}
