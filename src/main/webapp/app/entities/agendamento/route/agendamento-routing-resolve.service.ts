import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgendamento } from '../agendamento.model';
import { AgendamentoService } from '../service/agendamento.service';

export const agendamentoResolve = (route: ActivatedRouteSnapshot): Observable<null | IAgendamento> => {
  const id = route.params['id'];
  if (id) {
    return inject(AgendamentoService)
      .find(id)
      .pipe(
        mergeMap((agendamento: HttpResponse<IAgendamento>) => {
          if (agendamento.body) {
            return of(agendamento.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default agendamentoResolve;
