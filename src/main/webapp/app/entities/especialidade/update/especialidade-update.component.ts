import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEspecialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';
import { EspecialidadeFormService, EspecialidadeFormGroup } from './especialidade-form.service';

@Component({
  standalone: true,
  selector: 'jhi-especialidade-update',
  templateUrl: './especialidade-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EspecialidadeUpdateComponent implements OnInit {
  isSaving = false;
  especialidade: IEspecialidade | null = null;

  editForm: EspecialidadeFormGroup = this.especialidadeFormService.createEspecialidadeFormGroup();

  constructor(
    protected especialidadeService: EspecialidadeService,
    protected especialidadeFormService: EspecialidadeFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialidade }) => {
      this.especialidade = especialidade;
      if (especialidade) {
        this.updateForm(especialidade);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const especialidade = this.especialidadeFormService.getEspecialidade(this.editForm);
    if (especialidade.id !== null) {
      this.subscribeToSaveResponse(this.especialidadeService.update(especialidade));
    } else {
      this.subscribeToSaveResponse(this.especialidadeService.create(especialidade));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEspecialidade>>): void {
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

  protected updateForm(especialidade: IEspecialidade): void {
    this.especialidade = especialidade;
    this.especialidadeFormService.resetForm(this.editForm, especialidade);
  }
}
