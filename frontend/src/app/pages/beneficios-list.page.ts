import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BeneficioService } from '../services/beneficio.service';
import { Beneficio } from '../models/beneficio.model';
import { AlertBannerComponent } from '../components/alert-banner.component';

@Component({
  standalone: true,
  selector: 'app-beneficios-list-page',
  imports: [CommonModule, RouterLink, CurrencyPipe, AlertBannerComponent],
  template: `
    <section class="container">
      <div class="row-wrap" style="justify-content: space-between; align-items: center; margin-bottom: 12px;">
        <h2>Benefícios</h2>
        <div class="row-wrap">
          <a routerLink="/beneficios/novo"><button>Novo benefício</button></a>
          <a routerLink="/transferencias"><button class="secondary">Transferir</button></a>
        </div>
      </div>

      <app-alert-banner [message]="errorMessage" type="error"></app-alert-banner>

      <div class="card" *ngIf="loading">Carregando benefícios...</div>

      <div class="card" *ngIf="!loading">
        <table style="width: 100%; border-collapse: collapse;">
          <thead>
            <tr>
              <th style="text-align:left; padding: 8px;">ID</th>
              <th style="text-align:left; padding: 8px;">Nome</th>
              <th style="text-align:left; padding: 8px;">Descrição</th>
              <th style="text-align:left; padding: 8px;">Valor</th>
              <th style="text-align:left; padding: 8px;">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let item of beneficios">
              <td style="padding: 8px;">{{ item.id }}</td>
              <td style="padding: 8px;">{{ item.nome }}</td>
              <td style="padding: 8px;">{{ item.descricao }}</td>
              <td style="padding: 8px;">{{ item.valor | currency:'BRL' }}</td>
              <td style="padding: 8px;">
                <div class="row-wrap">
                  <a [routerLink]="['/beneficios', item.id, 'editar']"><button class="secondary">Editar</button></a>
                  <button class="danger" (click)="desativar(item)">Desativar</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  `
})
export class BeneficiosListPageComponent implements OnInit {
  beneficios: Beneficio[] = [];
  loading = true;
  errorMessage = '';

  constructor(private readonly beneficioService: BeneficioService) {}

  ngOnInit(): void {
    this.carregar();
  }

  desativar(item: Beneficio): void {
    this.errorMessage = '';
    this.beneficioService.desativarBeneficio(item.id).subscribe({
      next: () => this.carregar(),
      error: (error: Error) => {
        this.errorMessage = error.message;
      }
    });
  }

  private carregar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.beneficioService.listarBeneficios().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.loading = false;
      },
      error: (error: Error) => {
        this.errorMessage = error.message;
        this.loading = false;
      }
    });
  }
}

