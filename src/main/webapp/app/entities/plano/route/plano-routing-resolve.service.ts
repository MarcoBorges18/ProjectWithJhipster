import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlano } from '../plano.model';
import { PlanoService } from '../service/plano.service';

export const planoResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlano> => {
  const id = route.params['id'];
  if (id) {
    return inject(PlanoService)
      .find(id)
      .pipe(
        mergeMap((plano: HttpResponse<IPlano>) => {
          if (plano.body) {
            return of(plano.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default planoResolve;
