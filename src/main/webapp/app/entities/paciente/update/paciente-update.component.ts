import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlano } from 'app/entities/plano/plano.model';
import { PlanoService } from 'app/entities/plano/service/plano.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { PacienteService } from '../service/paciente.service';
import { IPaciente } from '../paciente.model';
import { PacienteFormService, PacienteFormGroup } from './paciente-form.service';

@Component({
  standalone: true,
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;
  paciente: IPaciente | null = null;

  planosCollection: IPlano[] = [];
  enderecosCollection: IEndereco[] = [];

  editForm: PacienteFormGroup = this.pacienteFormService.createPacienteFormGroup();

  constructor(
    protected pacienteService: PacienteService,
    protected pacienteFormService: PacienteFormService,
    protected planoService: PlanoService,
    protected enderecoService: EnderecoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePlano = (o1: IPlano | null, o2: IPlano | null): boolean => this.planoService.comparePlano(o1, o2);

  compareEndereco = (o1: IEndereco | null, o2: IEndereco | null): boolean => this.enderecoService.compareEndereco(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.paciente = paciente;
      if (paciente) {
        this.updateForm(paciente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.pacienteFormService.getPaciente(this.editForm);
    if (paciente.id !== null) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
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

  protected updateForm(paciente: IPaciente): void {
    this.paciente = paciente;
    this.pacienteFormService.resetForm(this.editForm, paciente);

    this.planosCollection = this.planoService.addPlanoToCollectionIfMissing<IPlano>(this.planosCollection, paciente.plano);
    this.enderecosCollection = this.enderecoService.addEnderecoToCollectionIfMissing<IEndereco>(
      this.enderecosCollection,
      paciente.endereco,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.planoService
      .query({ filter: null })
      .pipe(map((res: HttpResponse<IPlano[]>) => res.body ?? []))
      .pipe(map((planos: IPlano[]) => this.planoService.addPlanoToCollectionIfMissing<IPlano>(planos, this.paciente?.plano)))
      .subscribe((planos: IPlano[]) => (this.planosCollection = planos));

    this.enderecoService
      .query({ filter: null })
      .pipe(map((res: HttpResponse<IEndereco[]>) => res.body ?? []))
      .pipe(
        map((enderecos: IEndereco[]) =>
          this.enderecoService.addEnderecoToCollectionIfMissing<IEndereco>(enderecos, this.paciente?.endereco),
        ),
      )
      .subscribe((enderecos: IEndereco[]) => (this.enderecosCollection = enderecos));
  }
}
