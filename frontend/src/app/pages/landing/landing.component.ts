import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../shared/header/header.component';
import { LandingAnimationComponent } from './animation.component';

@Component({
  selector: 'app-landing',
  imports: [RouterLink, HeaderComponent, LandingAnimationComponent],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss'
})
export class LandingComponent {}
