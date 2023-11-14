import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EspecialidadeDetailComponent } from './especialidade-detail.component';

describe('Especialidade Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EspecialidadeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EspecialidadeDetailComponent,
              resolve: { especialidade: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EspecialidadeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load especialidade on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EspecialidadeDetailComponent);

      // THEN
      expect(instance.especialidade).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
