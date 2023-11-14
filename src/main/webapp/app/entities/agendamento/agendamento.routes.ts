import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AgendamentoComponent } from './list/agendamento.component';
import { AgendamentoDetailComponent } from './detail/agendamento-detail.component';
import { AgendamentoUpdateComponent } from './update/agendamento-update.component';
import AgendamentoResolve from './route/agendamento-routing-resolve.service';

const agendamentoRoute: Routes = [
  {
    path: '',
    component: AgendamentoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgendamentoDetailComponent,
    resolve: {
      agendamento: AgendamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgendamentoUpdateComponent,
    resolve: {
      agendamento: AgendamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgendamentoUpdateComponent,
    resolve: {
      agendamento: AgendamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default agendamentoRoute;
