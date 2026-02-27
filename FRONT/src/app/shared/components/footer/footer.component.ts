import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  styleUrl: './footer.component.css',
  templateUrl: './footer.component.html',
})
export class FooterComponent {
  public readonly currentYear = new Date().getFullYear();
}
