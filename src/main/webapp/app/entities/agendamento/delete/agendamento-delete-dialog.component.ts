import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAgendamento } from '../agendamento.model';
import { AgendamentoService } from '../service/agendamento.service';

@Component({
  standalone: true,
  templateUrl: './agendamento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AgendamentoDeleteDialogComponent {
  agendamento?: IAgendamento;

  constructor(
    protected agendamentoService: AgendamentoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agendamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
