import { Component, inject, OnInit } from '@angular/core';
import { TaskControllerService, TaskDto } from '../../modules/openapi';
import {Observable, of, Subscribable, tap} from 'rxjs';
import {map, catchError} from 'rxjs/operators';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
  standalone: true,
  imports: [AsyncPipe], // no NgFor needed for *ngFor
})
export class Dashboard implements OnInit {
  private taskService = inject(TaskControllerService);

  tasks$!: Observable<TaskDto[]>;

  ngOnInit() {
    this.tasks$ = this.taskService.getAllTasks(0, 10)
      .pipe(
        tap(page => console.log('Fetched tasks', page)),
        map(page => page.content ?? []),
        catchError(err => {
          console.error('Error fetching tasks', err);
          return of([]);
        })
      );
  }
}
