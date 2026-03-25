import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

function messageForStatus(status: number): string {
  if (status === 0) {
    return 'Nao foi possivel conectar ao backend.';
  }
  if (status === 400) {
    return 'Requisicao invalida. Verifique os dados enviados.';
  }
  if (status === 404) {
    return 'Recurso nao encontrado.';
  }
  if (status === 409) {
    return 'Operacao em conflito. Verifique saldo e dados da transferencia.';
  }
  return 'Erro inesperado ao processar a solicitacao.';
}

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const friendly = messageForStatus(error.status);
      return throwError(() => new Error(friendly));
    })
  );
};
