import { Component, Input } from '@angular/core';
import { FeatureCard } from '../feature-card';

@Component({
  selector: 'app-feature-card',
  standalone: true,
  imports: [],
  templateUrl: './feature-card.component.html',
  styleUrl: './feature-card.component.css',
})
export class FeatureCardComponent {
  @Input() featureCard!: FeatureCard;
}
