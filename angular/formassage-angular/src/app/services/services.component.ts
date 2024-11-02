import { Component } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { ServiceGroupComponent } from '../service-group/service-group.component';

@Component({
  selector: 'app-services',
  standalone: true,
  imports: [SidebarComponent, ServiceGroupComponent],
  templateUrl: './services.component.html',
  styleUrl: './services.component.css',
})
export class ServicesComponent {}
