import { AfterViewInit, Component, inject, ViewChild } from '@angular/core';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { TaskControllerService, TaskDto } from '../../modules/openapi';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-task-table',
  templateUrl: './task-table.component.html',
  styleUrl: './task-table.component.css',
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule],
})
export class TaskTableComponent implements AfterViewInit {
  private taskService = inject(TaskControllerService);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<TaskDto>();
  displayedColumns = ['id', 'title', 'description', 'done'];

  resultsLength = 0;
  isLoadingResults = true;

  ngAfterViewInit(): void {
    // Load data initially and whenever the page changes
    // exposes a stream of page change events (EventEmitter<PageEvent>).
    // Every time the user clicks next page, previous page, or changes page size, this emits an event.
    this.paginator.page
      .pipe(
        startWith({}), // trigger initial load This is what triggers the first load of data, before the user even clicks anything.
        switchMap(() => {
          this.isLoadingResults = true; //switchMap maps each event paginator event to a new Observable, in this case an HTTP request
          // Call the API, cancel previous requests if new event arrives
          return this.taskService.getAllTasks(
            this.paginator.pageIndex,
            this.paginator.pageSize
          ).pipe(catchError(() => of(null)));
        }),
        map(data => {
          this.isLoadingResults = false;
          if (!data) return [];
          this.resultsLength = data.totalElements ?? 0;
          return data.content ?? [];
        })
      )
      .subscribe(data => (this.dataSource.data = data));
  }
}
