import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgendamento, NewAgendamento } from '../agendamento.model';

export type PartialUpdateAgendamento = Partial<IAgendamento> & Pick<IAgendamento, 'id'>;

export type EntityResponseType = HttpResponse<IAgendamento>;
export type EntityArrayResponseType = HttpResponse<IAgendamento[]>;

@Injectable({ providedIn: 'root' })
export class AgendamentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agendamentos');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(agendamento: NewAgendamento): Observable<EntityResponseType> {
    return this.http.post<IAgendamento>(this.resourceUrl, agendamento, { observe: 'response' });
  }

  update(agendamento: IAgendamento): Observable<EntityResponseType> {
    return this.http.put<IAgendamento>(`${this.resourceUrl}/${this.getAgendamentoIdentifier(agendamento)}`, agendamento, {
      observe: 'response',
    });
  }

  partialUpdate(agendamento: PartialUpdateAgendamento): Observable<EntityResponseType> {
    return this.http.patch<IAgendamento>(`${this.resourceUrl}/${this.getAgendamentoIdentifier(agendamento)}`, agendamento, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgendamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgendamento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgendamentoIdentifier(agendamento: Pick<IAgendamento, 'id'>): number {
    return agendamento.id;
  }

  compareAgendamento(o1: Pick<IAgendamento, 'id'> | null, o2: Pick<IAgendamento, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgendamentoIdentifier(o1) === this.getAgendamentoIdentifier(o2) : o1 === o2;
  }

  addAgendamentoToCollectionIfMissing<Type extends Pick<IAgendamento, 'id'>>(
    agendamentoCollection: Type[],
    ...agendamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agendamentos: Type[] = agendamentosToCheck.filter(isPresent);
    if (agendamentos.length > 0) {
      const agendamentoCollectionIdentifiers = agendamentoCollection.map(
        agendamentoItem => this.getAgendamentoIdentifier(agendamentoItem)!,
      );
      const agendamentosToAdd = agendamentos.filter(agendamentoItem => {
        const agendamentoIdentifier = this.getAgendamentoIdentifier(agendamentoItem);
        if (agendamentoCollectionIdentifiers.includes(agendamentoIdentifier)) {
          return false;
        }
        agendamentoCollectionIdentifiers.push(agendamentoIdentifier);
        return true;
      });
      return [...agendamentosToAdd, ...agendamentoCollection];
    }
    return agendamentoCollection;
  }
}
