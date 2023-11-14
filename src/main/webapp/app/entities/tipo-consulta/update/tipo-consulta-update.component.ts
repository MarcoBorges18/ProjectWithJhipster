import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoConsulta } from '../tipo-consulta.model';
import { TipoConsultaService } from '../service/tipo-consulta.service';
import { TipoConsultaFormService, TipoConsultaFormGroup } from './tipo-consulta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tipo-consulta-update',
  templateUrl: './tipo-consulta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoConsultaUpdateComponent implements OnInit {
  isSaving = false;
  tipoConsulta: ITipoConsulta | null = null;

  editForm: TipoConsultaFormGroup = this.tipoConsultaFormService.createTipoConsultaFormGroup();

  constructor(
    protected tipoConsultaService: TipoConsultaService,
    protected tipoConsultaFormService: TipoConsultaFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoConsulta }) => {
      this.tipoConsulta = tipoConsulta;
      if (tipoConsulta) {
        this.updateForm(tipoConsulta);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoConsulta = this.tipoConsultaFormService.getTipoConsulta(this.editForm);
    if (tipoConsulta.id !== null) {
      this.subscribeToSaveResponse(this.tipoConsultaService.update(tipoConsulta));
    } else {
      this.subscribeToSaveResponse(this.tipoConsultaService.create(tipoConsulta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoConsulta>>): void {
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

  protected updateForm(tipoConsulta: ITipoConsulta): void {
    this.tipoConsulta = tipoConsulta;
    this.tipoConsultaFormService.resetForm(this.editForm, tipoConsulta);
  }
}
