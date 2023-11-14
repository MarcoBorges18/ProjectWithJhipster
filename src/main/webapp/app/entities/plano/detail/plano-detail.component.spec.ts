import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlanoDetailComponent } from './plano-detail.component';

describe('Plano Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PlanoDetailComponent,
              resolve: { plano: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlanoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load plano on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlanoDetailComponent);

      // THEN
      expect(instance.plano).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
