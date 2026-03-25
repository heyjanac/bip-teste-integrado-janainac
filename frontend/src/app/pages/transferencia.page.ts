import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BeneficioService } from '../services/beneficio.service';
import { AlertBannerComponent } from '../components/alert-banner.component';

@Component({
  standalone: true,
  selector: 'app-transferencia-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, AlertBannerComponent],
  template: `
    <section class="container">
      <div class="card">
        <h2>Transferência entre benefícios</h2>
        <app-alert-banner [message]="message" [type]="messageType"></app-alert-banner>

        <form [formGroup]="form" (ngSubmit)="transferir()" class="row-wrap">
          <input type="number" placeholder="ID origem" formControlName="fromId" style="width: 100%;" />
          <input type="number" placeholder="ID destino" formControlName="toId" style="width: 100%;" />
          <input type="number" step="0.01" placeholder="Valor" formControlName="amount" style="width: 100%;" />

          <div class="row-wrap">
            <button type="submit" [disabled]="form.invalid || submitting">{{ submitting ? 'Transferindo...' : 'Transferir' }}</button>
            <a routerLink="/beneficios"><button type="button" class="secondary">Voltar</button></a>
          </div>
        </form>
      </div>
    </section>
  `
})
export class TransferenciaPageComponent {
  submitting = false;
  message = '';
  messageType: 'error' | 'success' = 'error';

  form = this.fb.nonNullable.group({
    fromId: [0, [Validators.required, Validators.min(1)]],
    toId: [0, [Validators.required, Validators.min(1)]],
    amount: [0, [Validators.required, Validators.min(0.01)]]
  });

  constructor(private readonly fb: FormBuilder, private readonly beneficioService: BeneficioService) {}

  transferir(): void {
    if (this.form.invalid) {
      return;
    }

    this.submitting = true;
    this.message = '';

    this.beneficioService.transferir(this.form.getRawValue()).subscribe({
      next: () => {
        this.message = 'Transferência realizada com sucesso.';
        this.messageType = 'success';
        this.submitting = false;
      },
      error: (error: Error) => {
        this.message = error.message;
        this.messageType = 'error';
        this.submitting = false;
      }
    });
  }
}

