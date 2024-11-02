import { Component } from '@angular/core';
import { BannerComponent } from '../banner/banner.component';
import { FeaturesComponent } from '../features/features.component';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [BannerComponent, FeaturesComponent, FooterComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css',
})
export class MainComponent {}
