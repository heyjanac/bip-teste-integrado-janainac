import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BeneficioService } from '../services/beneficio.service';
import { AlertBannerComponent } from '../components/alert-banner.component';

@Component({
  standalone: true,
  selector: 'app-beneficio-form-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, AlertBannerComponent],
  template: `
    <section class="container">
      <div class="card">
        <h2>{{ editing ? 'Editar benefício' : 'Novo benefício' }}</h2>
        <app-alert-banner [message]="message" [type]="messageType"></app-alert-banner>

        <form [formGroup]="form" (ngSubmit)="salvar()" class="row-wrap">
          <input type="text" placeholder="Nome" formControlName="nome" style="width: 100%;" />
          <textarea placeholder="Descrição" formControlName="descricao" style="width: 100%; min-height: 88px;"></textarea>
          <input type="number" placeholder="Valor" formControlName="valor" step="0.01" style="width: 100%;" />

          <div class="row-wrap">
            <button type="submit" [disabled]="form.invalid || submitting">{{ submitting ? 'Salvando...' : 'Salvar' }}</button>
            <a routerLink="/beneficios"><button type="button" class="secondary">Voltar</button></a>
          </div>
        </form>
      </div>
    </section>
  `
})
export class BeneficioFormPageComponent implements OnInit {
  editing = false;
  beneficioId: number | null = null;
  submitting = false;
  message = '';
  messageType: 'error' | 'success' = 'error';

  form = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(100)]],
    descricao: ['', [Validators.maxLength(255)]],
    valor: [0, [Validators.required, Validators.min(0.01)]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly beneficioService: BeneficioService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.editing = true;
      this.beneficioId = Number(id);
      this.beneficioService.buscarBeneficio(this.beneficioId).subscribe({
        next: (item) => {
          this.form.patchValue({
            nome: item.nome,
            descricao: item.descricao,
            valor: item.valor
          });
        },
        error: (error: Error) => {
          this.message = error.message;
          this.messageType = 'error';
        }
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) {
      return;
    }

    this.submitting = true;
    this.message = '';
    const payload = this.form.getRawValue();

    const request$ = this.editing && this.beneficioId
      ? this.beneficioService.atualizarBeneficio(this.beneficioId, payload)
      : this.beneficioService.criarBeneficio(payload);

    request$.subscribe({
      next: () => {
        this.message = 'Benefício salvo com sucesso.';
        this.messageType = 'success';
        this.submitting = false;
        this.router.navigate(['/beneficios']);
      },
      error: (error: Error) => {
        this.message = error.message;
        this.messageType = 'error';
        this.submitting = false;
      }
    });
  }
}

