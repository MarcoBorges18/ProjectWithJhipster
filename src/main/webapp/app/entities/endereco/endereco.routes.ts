import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EnderecoComponent } from './list/endereco.component';
import { EnderecoDetailComponent } from './detail/endereco-detail.component';
import { EnderecoUpdateComponent } from './update/endereco-update.component';
import EnderecoResolve from './route/endereco-routing-resolve.service';

const enderecoRoute: Routes = [
  {
    path: '',
    component: EnderecoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnderecoDetailComponent,
    resolve: {
      endereco: EnderecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnderecoUpdateComponent,
    resolve: {
      endereco: EnderecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnderecoUpdateComponent,
    resolve: {
      endereco: EnderecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default enderecoRoute;
