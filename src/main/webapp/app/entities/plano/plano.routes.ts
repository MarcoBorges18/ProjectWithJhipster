import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PlanoComponent } from './list/plano.component';
import { PlanoDetailComponent } from './detail/plano-detail.component';
import { PlanoUpdateComponent } from './update/plano-update.component';
import PlanoResolve from './route/plano-routing-resolve.service';

const planoRoute: Routes = [
  {
    path: '',
    component: PlanoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlanoDetailComponent,
    resolve: {
      plano: PlanoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlanoUpdateComponent,
    resolve: {
      plano: PlanoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlanoUpdateComponent,
    resolve: {
      plano: PlanoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default planoRoute;
