import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoConsulta, NewTipoConsulta } from '../tipo-consulta.model';

export type PartialUpdateTipoConsulta = Partial<ITipoConsulta> & Pick<ITipoConsulta, 'id'>;

type RestOf<T extends ITipoConsulta | NewTipoConsulta> = Omit<T, 'tempo'> & {
  tempo?: string | null;
};

export type RestTipoConsulta = RestOf<ITipoConsulta>;

export type NewRestTipoConsulta = RestOf<NewTipoConsulta>;

export type PartialUpdateRestTipoConsulta = RestOf<PartialUpdateTipoConsulta>;

export type EntityResponseType = HttpResponse<ITipoConsulta>;
export type EntityArrayResponseType = HttpResponse<ITipoConsulta[]>;

@Injectable({ providedIn: 'root' })
export class TipoConsultaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-consultas');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tipoConsulta: NewTipoConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoConsulta);
    return this.http
      .post<RestTipoConsulta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tipoConsulta: ITipoConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoConsulta);
    return this.http
      .put<RestTipoConsulta>(`${this.resourceUrl}/${this.getTipoConsultaIdentifier(tipoConsulta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tipoConsulta: PartialUpdateTipoConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoConsulta);
    return this.http
      .patch<RestTipoConsulta>(`${this.resourceUrl}/${this.getTipoConsultaIdentifier(tipoConsulta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTipoConsulta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTipoConsulta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTipoConsultaIdentifier(tipoConsulta: Pick<ITipoConsulta, 'id'>): number {
    return tipoConsulta.id;
  }

  compareTipoConsulta(o1: Pick<ITipoConsulta, 'id'> | null, o2: Pick<ITipoConsulta, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoConsultaIdentifier(o1) === this.getTipoConsultaIdentifier(o2) : o1 === o2;
  }

  addTipoConsultaToCollectionIfMissing<Type extends Pick<ITipoConsulta, 'id'>>(
    tipoConsultaCollection: Type[],
    ...tipoConsultasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoConsultas: Type[] = tipoConsultasToCheck.filter(isPresent);
    if (tipoConsultas.length > 0) {
      const tipoConsultaCollectionIdentifiers = tipoConsultaCollection.map(
        tipoConsultaItem => this.getTipoConsultaIdentifier(tipoConsultaItem)!,
      );
      const tipoConsultasToAdd = tipoConsultas.filter(tipoConsultaItem => {
        const tipoConsultaIdentifier = this.getTipoConsultaIdentifier(tipoConsultaItem);
        if (tipoConsultaCollectionIdentifiers.includes(tipoConsultaIdentifier)) {
          return false;
        }
        tipoConsultaCollectionIdentifiers.push(tipoConsultaIdentifier);
        return true;
      });
      return [...tipoConsultasToAdd, ...tipoConsultaCollection];
    }
    return tipoConsultaCollection;
  }

  protected convertDateFromClient<T extends ITipoConsulta | NewTipoConsulta | PartialUpdateTipoConsulta>(tipoConsulta: T): RestOf<T> {
    return {
      ...tipoConsulta,
      tempo: tipoConsulta.tempo?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTipoConsulta: RestTipoConsulta): ITipoConsulta {
    return {
      ...restTipoConsulta,
      tempo: restTipoConsulta.tempo ? dayjs(restTipoConsulta.tempo) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTipoConsulta>): HttpResponse<ITipoConsulta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTipoConsulta[]>): HttpResponse<ITipoConsulta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
