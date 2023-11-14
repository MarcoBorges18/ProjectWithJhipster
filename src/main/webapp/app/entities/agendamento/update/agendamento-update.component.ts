import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { ITipoConsulta } from 'app/entities/tipo-consulta/tipo-consulta.model';
import { TipoConsultaService } from 'app/entities/tipo-consulta/service/tipo-consulta.service';
import { AgendamentoService } from '../service/agendamento.service';
import { IAgendamento } from '../agendamento.model';
import { AgendamentoFormService, AgendamentoFormGroup } from './agendamento-form.service';

@Component({
  standalone: true,
  selector: 'jhi-agendamento-update',
  templateUrl: './agendamento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgendamentoUpdateComponent implements OnInit {
  isSaving = false;
  agendamento: IAgendamento | null = null;

  medicosSharedCollection: IMedico[] = [];
  pacientesSharedCollection: IPaciente[] = [];
  tipoConsultasSharedCollection: ITipoConsulta[] = [];

  editForm: AgendamentoFormGroup = this.agendamentoFormService.createAgendamentoFormGroup();

  constructor(
    protected agendamentoService: AgendamentoService,
    protected agendamentoFormService: AgendamentoFormService,
    protected medicoService: MedicoService,
    protected pacienteService: PacienteService,
    protected tipoConsultaService: TipoConsultaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMedico = (o1: IMedico | null, o2: IMedico | null): boolean => this.medicoService.compareMedico(o1, o2);

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareTipoConsulta = (o1: ITipoConsulta | null, o2: ITipoConsulta | null): boolean =>
    this.tipoConsultaService.compareTipoConsulta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agendamento }) => {
      this.agendamento = agendamento;
      if (agendamento) {
        this.updateForm(agendamento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agendamento = this.agendamentoFormService.getAgendamento(this.editForm);
    if (agendamento.id !== null) {
      this.subscribeToSaveResponse(this.agendamentoService.update(agendamento));
    } else {
      this.subscribeToSaveResponse(this.agendamentoService.create(agendamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgendamento>>): void {
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

  protected updateForm(agendamento: IAgendamento): void {
    this.agendamento = agendamento;
    this.agendamentoFormService.resetForm(this.editForm, agendamento);

    this.medicosSharedCollection = this.medicoService.addMedicoToCollectionIfMissing<IMedico>(
      this.medicosSharedCollection,
      agendamento.medico,
    );
    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      agendamento.paciente,
    );
    this.tipoConsultasSharedCollection = this.tipoConsultaService.addTipoConsultaToCollectionIfMissing<ITipoConsulta>(
      this.tipoConsultasSharedCollection,
      agendamento.tipo,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicoService
      .query()
      .pipe(map((res: HttpResponse<IMedico[]>) => res.body ?? []))
      .pipe(map((medicos: IMedico[]) => this.medicoService.addMedicoToCollectionIfMissing<IMedico>(medicos, this.agendamento?.medico)))
      .subscribe((medicos: IMedico[]) => (this.medicosSharedCollection = medicos));

    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, this.agendamento?.paciente),
        ),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.tipoConsultaService
      .query()
      .pipe(map((res: HttpResponse<ITipoConsulta[]>) => res.body ?? []))
      .pipe(
        map((tipoConsultas: ITipoConsulta[]) =>
          this.tipoConsultaService.addTipoConsultaToCollectionIfMissing<ITipoConsulta>(tipoConsultas, this.agendamento?.tipo),
        ),
      )
      .subscribe((tipoConsultas: ITipoConsulta[]) => (this.tipoConsultasSharedCollection = tipoConsultas));
  }
}
