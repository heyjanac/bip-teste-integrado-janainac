import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-alert-banner',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="message" class="alert" [class.error]="type === 'error'" [class.success]="type === 'success'">
      {{ message }}
    </div>
  `,
  styles: [
    `
      .alert {
        border-radius: 8px;
        padding: 10px 12px;
        margin: 12px 0;
        border: 1px solid transparent;
      }

      .error {
        background: #fef2f2;
        border-color: #fecaca;
        color: #991b1b;
      }

      .success {
        background: #f0fdf4;
        border-color: #bbf7d0;
        color: #166534;
      }
    `
  ]
})
export class AlertBannerComponent {
  @Input() message = '';
  @Input() type: 'error' | 'success' = 'error';
}

