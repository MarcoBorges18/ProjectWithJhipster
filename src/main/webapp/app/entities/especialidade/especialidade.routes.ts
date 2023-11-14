import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EspecialidadeComponent } from './list/especialidade.component';
import { EspecialidadeDetailComponent } from './detail/especialidade-detail.component';
import { EspecialidadeUpdateComponent } from './update/especialidade-update.component';
import EspecialidadeResolve from './route/especialidade-routing-resolve.service';

const especialidadeRoute: Routes = [
  {
    path: '',
    component: EspecialidadeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EspecialidadeDetailComponent,
    resolve: {
      especialidade: EspecialidadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EspecialidadeUpdateComponent,
    resolve: {
      especialidade: EspecialidadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EspecialidadeUpdateComponent,
    resolve: {
      especialidade: EspecialidadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default especialidadeRoute;
