import {Component, inject, OnInit} from '@angular/core';
import {AuthState} from '../../state/auth.state';
import {Store} from '@ngxs/store';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit{
  private store = inject(Store)

  ngOnInit() {
    this.store.select(AuthState.getToken)
      .subscribe(token => {
        console.log('Token from state:', token);
      });
  }
}
