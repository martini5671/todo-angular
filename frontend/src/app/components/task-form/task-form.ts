import {Component, inject, OnInit, signal} from '@angular/core';
import {form, FormField, maxLength, minLength, required} from '@angular/forms/signals';
import {MatCheckbox} from '@angular/material/checkbox';
import {MatInput} from '@angular/material/input';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {CreateTaskDto, TaskControllerService, UpdateTaskDto} from '../../modules/openapi';
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
  private taskId: number | undefined;

  ngOnInit() {
    this.taskId = Number(this.activatedRoute.snapshot.paramMap.get('id'));
    if(this.taskId){
      this.isEditMode = true;
      this.loadTask(this.taskId);
    }
  }

  private taskModel = signal({
    title: "",
    description: "",
    done: false
  });

  protected taskForm = form(this.taskModel,
    model => {
      required(model.title, {message: 'Title is required'});
      minLength(model.title, 3, {message: 'At minimum 3 characters required'});
      maxLength(model.description, 50, {message: 'Maximum 50 characters'});
    });

  private createTask = () => {
    const taskData: CreateTaskDto = {
      description: this.taskForm.description().value(),
      title: this.taskForm.title().value()
    }
    this.taskService.createTask(taskData)
      .pipe(
        tap(() => {
          this.toaster.success('Task created!');
        }),
        catchError(err => {
          console.log(err)
          return of(null);
        })
      )
      .subscribe();
  }
  private updateTask = () => {
    const taskData: UpdateTaskDto = {
      title: this.taskForm.title().value(),
      description: this.taskForm.description().value(),
      done: this.taskForm.done().value(),
    }
    this.taskService.updateTask(this.taskId!, taskData)
      .pipe(
        tap(() => {
          this.toaster.success('Task updated!');
        }),
        catchError(err => {
          console.log(err)
          return of(null);
        })
      )
      .subscribe();
  }

  protected onSubmit(event: SubmitEvent) {
    event.preventDefault();
    this.isEditMode ? this.updateTask() : this.createTask();
  }

  private loadTask(id: number) {
    this.taskService.getTaskById(id)
      .pipe(
        tap(task => {
          this.taskModel.set({
            title: task.title,
            description: task.description ? task.description : "",
            done: task.done
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
