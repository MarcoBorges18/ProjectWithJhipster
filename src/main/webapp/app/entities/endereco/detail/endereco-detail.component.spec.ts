import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EnderecoDetailComponent } from './endereco-detail.component';

describe('Endereco Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnderecoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EnderecoDetailComponent,
              resolve: { endereco: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EnderecoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load endereco on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EnderecoDetailComponent);

      // THEN
      expect(instance.endereco).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
