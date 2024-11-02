import { Routes } from '@angular/router';
import { ServicesComponent } from './services/services.component';
import { MainComponent } from './main/main.component';
export const routes: Routes = [
  { path: '', component: MainComponent, title: 'Kezdőoldal' },
  {
    path: 'szolgaltatasok',
    component: ServicesComponent,
    title: 'Szolgáltatások',
  },
];
export default routes;
