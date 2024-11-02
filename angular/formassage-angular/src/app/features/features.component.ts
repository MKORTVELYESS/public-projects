import { Component } from '@angular/core';
import { FeatureCardComponent } from '../feature-card/feature-card.component';
import { FeatureCard } from '../feature-card';
import { NgFor } from '@angular/common';
@Component({
  selector: 'app-features',
  standalone: true,
  imports: [FeatureCardComponent, NgFor],
  templateUrl: './features.component.html',
  styleUrl: './features.component.css',
})
export class FeaturesComponent {
  featureCardList: FeatureCard[] = [
    {
      id: 1,
      photo: 'images/pexels-olly-3757952.jpg',
      name: 'Masszázs, alakformálás, bőrszépítés',
      subtitle: 'Masszázs típusok, fogasztás és cellulit kezelés',
      details: 'Részletekért és árakért kattints',
    },
    {
      id: 2,
      photo: 'images/pexels-magicbowls-1830208-3543716.jpg',
      name: 'Kezelések, Ezoterápia, Stresszoldás',
      subtitle: 'Kranioszakrális, Reiki és Tibeti hangtál kezelés',
      details: 'Részletekért és árakért kattints',
    },

    {
      id: 3,
      photo: 'images/pexels-valeriya-939835.jpg',
      name: 'Géllakk, műköröm valamint manikűr',
      subtitle: 'Manikűr és műköröm építő technikák, képek',
      details: 'Részletekért és árakért kattints',
    },

    // {
    //   id: 4,
    //   photo: 'images/pic04.jpg',
    //   name: 'Egyéb',
    //   subtitle: 'Ásvány kristály ékszerek rendelése személyre szólóan',
    //   details: 'Részletekért és árakért kattints ↗️',
    // },
  ];
}
