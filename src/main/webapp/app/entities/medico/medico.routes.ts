import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MedicoComponent } from './list/medico.component';
import { MedicoDetailComponent } from './detail/medico-detail.component';
import { MedicoUpdateComponent } from './update/medico-update.component';
import MedicoResolve from './route/medico-routing-resolve.service';

const medicoRoute: Routes = [
  {
    path: '',
    component: MedicoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicoDetailComponent,
    resolve: {
      medico: MedicoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicoUpdateComponent,
    resolve: {
      medico: MedicoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicoUpdateComponent,
    resolve: {
      medico: MedicoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicoRoute;
