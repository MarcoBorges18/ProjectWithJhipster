import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEspecialidade, NewEspecialidade } from '../especialidade.model';

export type PartialUpdateEspecialidade = Partial<IEspecialidade> & Pick<IEspecialidade, 'id'>;

export type EntityResponseType = HttpResponse<IEspecialidade>;
export type EntityArrayResponseType = HttpResponse<IEspecialidade[]>;

@Injectable({ providedIn: 'root' })
export class EspecialidadeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/especialidades');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(especialidade: NewEspecialidade): Observable<EntityResponseType> {
    return this.http.post<IEspecialidade>(this.resourceUrl, especialidade, { observe: 'response' });
  }

  update(especialidade: IEspecialidade): Observable<EntityResponseType> {
    return this.http.put<IEspecialidade>(`${this.resourceUrl}/${this.getEspecialidadeIdentifier(especialidade)}`, especialidade, {
      observe: 'response',
    });
  }

  partialUpdate(especialidade: PartialUpdateEspecialidade): Observable<EntityResponseType> {
    return this.http.patch<IEspecialidade>(`${this.resourceUrl}/${this.getEspecialidadeIdentifier(especialidade)}`, especialidade, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEspecialidade>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEspecialidade[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEspecialidadeIdentifier(especialidade: Pick<IEspecialidade, 'id'>): number {
    return especialidade.id;
  }

  compareEspecialidade(o1: Pick<IEspecialidade, 'id'> | null, o2: Pick<IEspecialidade, 'id'> | null): boolean {
    return o1 && o2 ? this.getEspecialidadeIdentifier(o1) === this.getEspecialidadeIdentifier(o2) : o1 === o2;
  }

  addEspecialidadeToCollectionIfMissing<Type extends Pick<IEspecialidade, 'id'>>(
    especialidadeCollection: Type[],
    ...especialidadesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const especialidades: Type[] = especialidadesToCheck.filter(isPresent);
    if (especialidades.length > 0) {
      const especialidadeCollectionIdentifiers = especialidadeCollection.map(
        especialidadeItem => this.getEspecialidadeIdentifier(especialidadeItem)!,
      );
      const especialidadesToAdd = especialidades.filter(especialidadeItem => {
        const especialidadeIdentifier = this.getEspecialidadeIdentifier(especialidadeItem);
        if (especialidadeCollectionIdentifiers.includes(especialidadeIdentifier)) {
          return false;
        }
        especialidadeCollectionIdentifiers.push(especialidadeIdentifier);
        return true;
      });
      return [...especialidadesToAdd, ...especialidadeCollection];
    }
    return especialidadeCollection;
  }
}
