import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlano } from '../plano.model';
import { PlanoService } from '../service/plano.service';
import { PlanoFormService, PlanoFormGroup } from './plano-form.service';

@Component({
  standalone: true,
  selector: 'jhi-plano-update',
  templateUrl: './plano-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlanoUpdateComponent implements OnInit {
  isSaving = false;
  plano: IPlano | null = null;

  editForm: PlanoFormGroup = this.planoFormService.createPlanoFormGroup();

  constructor(
    protected planoService: PlanoService,
    protected planoFormService: PlanoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plano }) => {
      this.plano = plano;
      if (plano) {
        this.updateForm(plano);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plano = this.planoFormService.getPlano(this.editForm);
    if (plano.id !== null) {
      this.subscribeToSaveResponse(this.planoService.update(plano));
    } else {
      this.subscribeToSaveResponse(this.planoService.create(plano));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlano>>): void {
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

  protected updateForm(plano: IPlano): void {
    this.plano = plano;
    this.planoFormService.resetForm(this.editForm, plano);
  }
}
