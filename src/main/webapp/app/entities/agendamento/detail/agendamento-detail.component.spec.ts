import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgendamentoDetailComponent } from './agendamento-detail.component';

describe('Agendamento Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgendamentoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AgendamentoDetailComponent,
              resolve: { agendamento: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AgendamentoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load agendamento on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AgendamentoDetailComponent);

      // THEN
      expect(instance.agendamento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
