import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEspecialidade } from 'app/entities/especialidade/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade/service/especialidade.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { MedicoService } from '../service/medico.service';
import { IMedico } from '../medico.model';
import { MedicoFormService, MedicoFormGroup } from './medico-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medico-update',
  templateUrl: './medico-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicoUpdateComponent implements OnInit {
  isSaving = false;
  medico: IMedico | null = null;

  especialidadesCollection: IEspecialidade[] = [];
  enderecosCollection: IEndereco[] = [];

  editForm: MedicoFormGroup = this.medicoFormService.createMedicoFormGroup();

  constructor(
    protected medicoService: MedicoService,
    protected medicoFormService: MedicoFormService,
    protected especialidadeService: EspecialidadeService,
    protected enderecoService: EnderecoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEspecialidade = (o1: IEspecialidade | null, o2: IEspecialidade | null): boolean =>
    this.especialidadeService.compareEspecialidade(o1, o2);

  compareEndereco = (o1: IEndereco | null, o2: IEndereco | null): boolean => this.enderecoService.compareEndereco(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medico }) => {
      this.medico = medico;
      if (medico) {
        this.updateForm(medico);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medico = this.medicoFormService.getMedico(this.editForm);
    if (medico.id !== null) {
      this.subscribeToSaveResponse(this.medicoService.update(medico));
    } else {
      this.subscribeToSaveResponse(this.medicoService.create(medico));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedico>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medico: IMedico): void {
    this.medico = medico;
    this.medicoFormService.resetForm(this.editForm, medico);

    this.especialidadesCollection = this.especialidadeService.addEspecialidadeToCollectionIfMissing<IEspecialidade>(
      this.especialidadesCollection,
      medico.especialidade,
    );
    this.enderecosCollection = this.enderecoService.addEnderecoToCollectionIfMissing<IEndereco>(this.enderecosCollection, medico.endereco);
  }

  protected loadRelationshipsOptions(): void {
    this.especialidadeService
      .query({ filter: null })
      .pipe(map((res: HttpResponse<IEspecialidade[]>) => res.body ?? []))
      .pipe(
        map((especialidades: IEspecialidade[]) =>
          this.especialidadeService.addEspecialidadeToCollectionIfMissing<IEspecialidade>(especialidades, this.medico?.especialidade),
        ),
      )
      .subscribe((especialidades: IEspecialidade[]) => (this.especialidadesCollection = especialidades));

    this.enderecoService
      .query({ filter: null })
      .pipe(map((res: HttpResponse<IEndereco[]>) => res.body ?? []))
      .pipe(
        map((enderecos: IEndereco[]) => this.enderecoService.addEnderecoToCollectionIfMissing<IEndereco>(enderecos, this.medico?.endereco)),
      )
      .subscribe((enderecos: IEndereco[]) => (this.enderecosCollection = enderecos));
  }
}
