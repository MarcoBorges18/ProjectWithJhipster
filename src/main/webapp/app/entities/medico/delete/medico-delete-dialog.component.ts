import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedico } from '../medico.model';
import { MedicoService } from '../service/medico.service';

@Component({
  standalone: true,
  templateUrl: './medico-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicoDeleteDialogComponent {
  medico?: IMedico;

  constructor(
    protected medicoService: MedicoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
