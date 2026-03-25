import { Routes } from '@angular/router';
import { BeneficiosListPageComponent } from './pages/beneficios-list.page';
import { BeneficioFormPageComponent } from './pages/beneficio-form.page';
import { TransferenciaPageComponent } from './pages/transferencia.page';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'beneficios' },
  { path: 'beneficios', component: BeneficiosListPageComponent },
  { path: 'beneficios/novo', component: BeneficioFormPageComponent },
  { path: 'beneficios/:id/editar', component: BeneficioFormPageComponent },
  { path: 'transferencias', component: TransferenciaPageComponent }
];

