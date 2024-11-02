import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { BannerComponent } from './banner/banner.component';
import { FeaturesComponent } from './features/features.component';
import { MainComponent } from './main/main.component';
import { FooterComponent } from './footer/footer.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    BannerComponent,
    FeaturesComponent,
    MainComponent,
    FooterComponent,
    RouterModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'formassage-angular';
}
