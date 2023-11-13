import { Routes } from '@angular/router';

import ErrorComponent from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    title: 'Página de erro!',
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      errorMessage: 'Você não tem autorização para acessar esta página.',
    },
    title: 'Página de erro!',
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      errorMessage: 'A página não existe.',
    },
    title: 'Página de erro!',
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
