import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlano, NewPlano } from '../plano.model';

export type PartialUpdatePlano = Partial<IPlano> & Pick<IPlano, 'id'>;

export type EntityResponseType = HttpResponse<IPlano>;
export type EntityArrayResponseType = HttpResponse<IPlano[]>;

@Injectable({ providedIn: 'root' })
export class PlanoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/planos');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(plano: NewPlano): Observable<EntityResponseType> {
    return this.http.post<IPlano>(this.resourceUrl, plano, { observe: 'response' });
  }

  update(plano: IPlano): Observable<EntityResponseType> {
    return this.http.put<IPlano>(`${this.resourceUrl}/${this.getPlanoIdentifier(plano)}`, plano, { observe: 'response' });
  }

  partialUpdate(plano: PartialUpdatePlano): Observable<EntityResponseType> {
    return this.http.patch<IPlano>(`${this.resourceUrl}/${this.getPlanoIdentifier(plano)}`, plano, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlano>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlano[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlanoIdentifier(plano: Pick<IPlano, 'id'>): number {
    return plano.id;
  }

  comparePlano(o1: Pick<IPlano, 'id'> | null, o2: Pick<IPlano, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlanoIdentifier(o1) === this.getPlanoIdentifier(o2) : o1 === o2;
  }

  addPlanoToCollectionIfMissing<Type extends Pick<IPlano, 'id'>>(
    planoCollection: Type[],
    ...planosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const planos: Type[] = planosToCheck.filter(isPresent);
    if (planos.length > 0) {
      const planoCollectionIdentifiers = planoCollection.map(planoItem => this.getPlanoIdentifier(planoItem)!);
      const planosToAdd = planos.filter(planoItem => {
        const planoIdentifier = this.getPlanoIdentifier(planoItem);
        if (planoCollectionIdentifiers.includes(planoIdentifier)) {
          return false;
        }
        planoCollectionIdentifiers.push(planoIdentifier);
        return true;
      });
      return [...planosToAdd, ...planoCollection];
    }
    return planoCollection;
  }
}
