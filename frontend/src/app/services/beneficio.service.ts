import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Beneficio, BeneficioCreateRequest } from '../models/beneficio.model';
import { TransferenciaRequest } from '../models/transferencia.model';

@Injectable({ providedIn: 'root' })
export class BeneficioService {
  private readonly base = environment.apiBaseUrl;

  constructor(private readonly http: HttpClient) {}

  listarBeneficios(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(`${this.base}/beneficios`);
  }

  buscarBeneficio(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.base}/beneficios/${id}`);
  }

  criarBeneficio(payload: BeneficioCreateRequest): Observable<Beneficio> {
    return this.http.post<Beneficio>(`${this.base}/beneficios`, payload);
  }

  atualizarBeneficio(id: number, payload: BeneficioCreateRequest): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.base}/beneficios/${id}`, payload);
  }

  desativarBeneficio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/beneficios/${id}`);
  }

  transferir(payload: TransferenciaRequest): Observable<void> {
    return this.http.post<void>(`${this.base}/transferencias`, payload);
  }
}

