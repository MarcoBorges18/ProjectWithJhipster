import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEndereco } from '../endereco.model';
import { EnderecoService } from '../service/endereco.service';

export const enderecoResolve = (route: ActivatedRouteSnapshot): Observable<null | IEndereco> => {
  const id = route.params['id'];
  if (id) {
    return inject(EnderecoService)
      .find(id)
      .pipe(
        mergeMap((endereco: HttpResponse<IEndereco>) => {
          if (endereco.body) {
            return of(endereco.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default enderecoResolve;
