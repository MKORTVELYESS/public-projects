import { Component } from '@angular/core';
import { ServiceItemComponent } from '../service-item/service-item.component';

@Component({
  selector: 'app-service-group',
  standalone: true,
  imports: [ServiceItemComponent],
  templateUrl: './service-group.component.html',
  styleUrl: './service-group.component.css',
})
export class ServiceGroupComponent {}
