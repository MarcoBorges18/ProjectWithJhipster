import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoConsultaDetailComponent } from './tipo-consulta-detail.component';

describe('TipoConsulta Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoConsultaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TipoConsultaDetailComponent,
              resolve: { tipoConsulta: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoConsultaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tipoConsulta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoConsultaDetailComponent);

      // THEN
      expect(instance.tipoConsulta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
