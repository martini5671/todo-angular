import {Component, signal} from '@angular/core';
import {form, FormField, maxLength, minLength, required} from '@angular/forms/signals';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';

@Component({
  selector: 'app-task-form',
  imports: [
    FormField,
    MatFormField,
    MatLabel,
    MatInput
  ],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm {

  private taskModel = signal({
    title: "",
    description: ""
  });
  protected taskForm = form(this.taskModel,
    model => {
      required(model.title);
      minLength(model.title, 3);

      maxLength(model.description, 50);
    });
}
