import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEspecialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';

export const especialidadeResolve = (route: ActivatedRouteSnapshot): Observable<null | IEspecialidade> => {
  const id = route.params['id'];
  if (id) {
    return inject(EspecialidadeService)
      .find(id)
      .pipe(
        mergeMap((especialidade: HttpResponse<IEspecialidade>) => {
          if (especialidade.body) {
            return of(especialidade.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default especialidadeResolve;
