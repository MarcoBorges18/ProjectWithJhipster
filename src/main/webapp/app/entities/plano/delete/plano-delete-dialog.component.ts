import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlano } from '../plano.model';
import { PlanoService } from '../service/plano.service';

@Component({
  standalone: true,
  templateUrl: './plano-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlanoDeleteDialogComponent {
  plano?: IPlano;

  constructor(
    protected planoService: PlanoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
