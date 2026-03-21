import {Routes} from '@angular/router';
import {Login} from './components/login/login';
import {Dashboard} from './components/dashboard/dashboard';
import {TaskForm} from './components/task-form/task-form';

export const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: Login},
  {path: "dashboard", component: Dashboard},
  {path: "task-form", component: TaskForm},
  {path: "task-form/edit/:id", component: TaskForm}
];
