import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TipoConsultaComponent } from './list/tipo-consulta.component';
import { TipoConsultaDetailComponent } from './detail/tipo-consulta-detail.component';
import { TipoConsultaUpdateComponent } from './update/tipo-consulta-update.component';
import TipoConsultaResolve from './route/tipo-consulta-routing-resolve.service';

const tipoConsultaRoute: Routes = [
  {
    path: '',
    component: TipoConsultaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoConsultaDetailComponent,
    resolve: {
      tipoConsulta: TipoConsultaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoConsultaUpdateComponent,
    resolve: {
      tipoConsulta: TipoConsultaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoConsultaUpdateComponent,
    resolve: {
      tipoConsulta: TipoConsultaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoConsultaRoute;
