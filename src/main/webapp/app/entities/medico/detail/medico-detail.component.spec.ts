import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MedicoDetailComponent } from './medico-detail.component';

describe('Medico Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedicoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MedicoDetailComponent,
              resolve: { medico: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MedicoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load medico on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MedicoDetailComponent);

      // THEN
      expect(instance.medico).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
