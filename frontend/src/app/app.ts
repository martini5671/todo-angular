import {Component, signal} from '@angular/core';
import {MenuComponent} from './components/menu/menu.component';

@Component({
  selector: 'app-root',
  imports: [MenuComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('login');
}
