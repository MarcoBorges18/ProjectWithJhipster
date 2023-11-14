import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlano } from '../plano.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../plano.test-samples';

import { PlanoService } from './plano.service';

const requireRestSample: IPlano = {
  ...sampleWithRequiredData,
};

describe('Plano Service', () => {
  let service: PlanoService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlano | IPlano[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlanoService);
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

    it('should create a Plano', () => {
      const plano = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(plano).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Plano', () => {
      const plano = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(plano).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Plano', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Plano', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Plano', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlanoToCollectionIfMissing', () => {
      it('should add a Plano to an empty array', () => {
        const plano: IPlano = sampleWithRequiredData;
        expectedResult = service.addPlanoToCollectionIfMissing([], plano);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plano);
      });

      it('should not add a Plano to an array that contains it', () => {
        const plano: IPlano = sampleWithRequiredData;
        const planoCollection: IPlano[] = [
          {
            ...plano,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlanoToCollectionIfMissing(planoCollection, plano);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Plano to an array that doesn't contain it", () => {
        const plano: IPlano = sampleWithRequiredData;
        const planoCollection: IPlano[] = [sampleWithPartialData];
        expectedResult = service.addPlanoToCollectionIfMissing(planoCollection, plano);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plano);
      });

      it('should add only unique Plano to an array', () => {
        const planoArray: IPlano[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const planoCollection: IPlano[] = [sampleWithRequiredData];
        expectedResult = service.addPlanoToCollectionIfMissing(planoCollection, ...planoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const plano: IPlano = sampleWithRequiredData;
        const plano2: IPlano = sampleWithPartialData;
        expectedResult = service.addPlanoToCollectionIfMissing([], plano, plano2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plano);
        expect(expectedResult).toContain(plano2);
      });

      it('should accept null and undefined values', () => {
        const plano: IPlano = sampleWithRequiredData;
        expectedResult = service.addPlanoToCollectionIfMissing([], null, plano, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plano);
      });

      it('should return initial array if no Plano is added', () => {
        const planoCollection: IPlano[] = [sampleWithRequiredData];
        expectedResult = service.addPlanoToCollectionIfMissing(planoCollection, undefined, null);
        expect(expectedResult).toEqual(planoCollection);
      });
    });

    describe('comparePlano', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlano(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlano(entity1, entity2);
        const compareResult2 = service.comparePlano(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlano(entity1, entity2);
        const compareResult2 = service.comparePlano(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlano(entity1, entity2);
        const compareResult2 = service.comparePlano(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
