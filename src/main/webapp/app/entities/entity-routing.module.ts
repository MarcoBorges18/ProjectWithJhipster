import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'endereco',
        data: { pageTitle: 'Enderecos' },
        loadChildren: () => import('./endereco/endereco.routes'),
      },
      {
        path: 'plano',
        data: { pageTitle: 'Planos' },
        loadChildren: () => import('./plano/plano.routes'),
      },
      {
        path: 'especialidade',
        data: { pageTitle: 'Especialidades' },
        loadChildren: () => import('./especialidade/especialidade.routes'),
      },
      {
        path: 'paciente',
        data: { pageTitle: 'Pacientes' },
        loadChildren: () => import('./paciente/paciente.routes'),
      },
      {
        path: 'medico',
        data: { pageTitle: 'Medicos' },
        loadChildren: () => import('./medico/medico.routes'),
      },
      {
        path: 'tipo-consulta',
        data: { pageTitle: 'TipoConsultas' },
        loadChildren: () => import('./tipo-consulta/tipo-consulta.routes'),
      },
      {
        path: 'agendamento',
        data: { pageTitle: 'Agendamentos' },
        loadChildren: () => import('./agendamento/agendamento.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
