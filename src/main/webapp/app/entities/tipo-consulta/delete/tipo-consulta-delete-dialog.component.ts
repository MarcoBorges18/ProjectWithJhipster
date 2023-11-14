import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITipoConsulta } from '../tipo-consulta.model';
import { TipoConsultaService } from '../service/tipo-consulta.service';

@Component({
  standalone: true,
  templateUrl: './tipo-consulta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TipoConsultaDeleteDialogComponent {
  tipoConsulta?: ITipoConsulta;

  constructor(
    protected tipoConsultaService: TipoConsultaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoConsultaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
