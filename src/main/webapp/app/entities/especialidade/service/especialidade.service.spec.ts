import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEspecialidade } from '../especialidade.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../especialidade.test-samples';

import { EspecialidadeService } from './especialidade.service';

const requireRestSample: IEspecialidade = {
  ...sampleWithRequiredData,
};

describe('Especialidade Service', () => {
  let service: EspecialidadeService;
  let httpMock: HttpTestingController;
  let expectedResult: IEspecialidade | IEspecialidade[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EspecialidadeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Especialidade', () => {
      const especialidade = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(especialidade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Especialidade', () => {
      const especialidade = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(especialidade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Especialidade', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Especialidade', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Especialidade', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEspecialidadeToCollectionIfMissing', () => {
      it('should add a Especialidade to an empty array', () => {
        const especialidade: IEspecialidade = sampleWithRequiredData;
        expectedResult = service.addEspecialidadeToCollectionIfMissing([], especialidade);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(especialidade);
      });

      it('should not add a Especialidade to an array that contains it', () => {
        const especialidade: IEspecialidade = sampleWithRequiredData;
        const especialidadeCollection: IEspecialidade[] = [
          {
            ...especialidade,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, especialidade);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Especialidade to an array that doesn't contain it", () => {
        const especialidade: IEspecialidade = sampleWithRequiredData;
        const especialidadeCollection: IEspecialidade[] = [sampleWithPartialData];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, especialidade);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(especialidade);
      });

      it('should add only unique Especialidade to an array', () => {
        const especialidadeArray: IEspecialidade[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const especialidadeCollection: IEspecialidade[] = [sampleWithRequiredData];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, ...especialidadeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const especialidade: IEspecialidade = sampleWithRequiredData;
        const especialidade2: IEspecialidade = sampleWithPartialData;
        expectedResult = service.addEspecialidadeToCollectionIfMissing([], especialidade, especialidade2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(especialidade);
        expect(expectedResult).toContain(especialidade2);
      });

      it('should accept null and undefined values', () => {
        const especialidade: IEspecialidade = sampleWithRequiredData;
        expectedResult = service.addEspecialidadeToCollectionIfMissing([], null, especialidade, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(especialidade);
      });

      it('should return initial array if no Especialidade is added', () => {
        const especialidadeCollection: IEspecialidade[] = [sampleWithRequiredData];
        expectedResult = service.addEspecialidadeToCollectionIfMissing(especialidadeCollection, undefined, null);
        expect(expectedResult).toEqual(especialidadeCollection);
      });
    });

    describe('compareEspecialidade', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEspecialidade(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEspecialidade(entity1, entity2);
        const compareResult2 = service.compareEspecialidade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEspecialidade(entity1, entity2);
        const compareResult2 = service.compareEspecialidade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEspecialidade(entity1, entity2);
        const compareResult2 = service.compareEspecialidade(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
