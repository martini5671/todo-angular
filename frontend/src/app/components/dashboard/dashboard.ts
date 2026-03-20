import { Component } from '@angular/core';
import {TaskTableComponent} from '../task-table/task-table.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
  standalone: true,
  imports: [TaskTableComponent],
})
export class Dashboard {
}
