import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoConsulta } from '../tipo-consulta.model';
import { TipoConsultaService } from '../service/tipo-consulta.service';

export const tipoConsultaResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoConsulta> => {
  const id = route.params['id'];
  if (id) {
    return inject(TipoConsultaService)
      .find(id)
      .pipe(
        mergeMap((tipoConsulta: HttpResponse<ITipoConsulta>) => {
          if (tipoConsulta.body) {
            return of(tipoConsulta.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tipoConsultaResolve;
