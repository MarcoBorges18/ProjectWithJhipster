import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEspecialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';

@Component({
  standalone: true,
  templateUrl: './especialidade-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EspecialidadeDeleteDialogComponent {
  especialidade?: IEspecialidade;

  constructor(
    protected especialidadeService: EspecialidadeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.especialidadeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
