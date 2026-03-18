import {Component, signal} from '@angular/core';
import {form, FormField, required} from '@angular/forms/signals';

@Component({
  selector: 'app-task-form',
  imports: [
    FormField
  ],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm {

  private taskModel = signal({
    name: ""
  });
  protected taskForm = form(this.taskModel,
    m => {
      required(m.name)
    });
}
