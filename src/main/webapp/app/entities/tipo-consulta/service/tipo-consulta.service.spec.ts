import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITipoConsulta } from '../tipo-consulta.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tipo-consulta.test-samples';

import { TipoConsultaService, RestTipoConsulta } from './tipo-consulta.service';

const requireRestSample: RestTipoConsulta = {
  ...sampleWithRequiredData,
  tempo: sampleWithRequiredData.tempo?.format(DATE_FORMAT),
};

describe('TipoConsulta Service', () => {
  let service: TipoConsultaService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoConsulta | ITipoConsulta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoConsultaService);
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

    it('should create a TipoConsulta', () => {
      const tipoConsulta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoConsulta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoConsulta', () => {
      const tipoConsulta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoConsulta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoConsulta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoConsulta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoConsulta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTipoConsultaToCollectionIfMissing', () => {
      it('should add a TipoConsulta to an empty array', () => {
        const tipoConsulta: ITipoConsulta = sampleWithRequiredData;
        expectedResult = service.addTipoConsultaToCollectionIfMissing([], tipoConsulta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoConsulta);
      });

      it('should not add a TipoConsulta to an array that contains it', () => {
        const tipoConsulta: ITipoConsulta = sampleWithRequiredData;
        const tipoConsultaCollection: ITipoConsulta[] = [
          {
            ...tipoConsulta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoConsultaToCollectionIfMissing(tipoConsultaCollection, tipoConsulta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoConsulta to an array that doesn't contain it", () => {
        const tipoConsulta: ITipoConsulta = sampleWithRequiredData;
        const tipoConsultaCollection: ITipoConsulta[] = [sampleWithPartialData];
        expectedResult = service.addTipoConsultaToCollectionIfMissing(tipoConsultaCollection, tipoConsulta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoConsulta);
      });

      it('should add only unique TipoConsulta to an array', () => {
        const tipoConsultaArray: ITipoConsulta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoConsultaCollection: ITipoConsulta[] = [sampleWithRequiredData];
        expectedResult = service.addTipoConsultaToCollectionIfMissing(tipoConsultaCollection, ...tipoConsultaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoConsulta: ITipoConsulta = sampleWithRequiredData;
        const tipoConsulta2: ITipoConsulta = sampleWithPartialData;
        expectedResult = service.addTipoConsultaToCollectionIfMissing([], tipoConsulta, tipoConsulta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoConsulta);
        expect(expectedResult).toContain(tipoConsulta2);
      });

      it('should accept null and undefined values', () => {
        const tipoConsulta: ITipoConsulta = sampleWithRequiredData;
        expectedResult = service.addTipoConsultaToCollectionIfMissing([], null, tipoConsulta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoConsulta);
      });

      it('should return initial array if no TipoConsulta is added', () => {
        const tipoConsultaCollection: ITipoConsulta[] = [sampleWithRequiredData];
        expectedResult = service.addTipoConsultaToCollectionIfMissing(tipoConsultaCollection, undefined, null);
        expect(expectedResult).toEqual(tipoConsultaCollection);
      });
    });

    describe('compareTipoConsulta', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoConsulta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTipoConsulta(entity1, entity2);
        const compareResult2 = service.compareTipoConsulta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTipoConsulta(entity1, entity2);
        const compareResult2 = service.compareTipoConsulta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTipoConsulta(entity1, entity2);
        const compareResult2 = service.compareTipoConsulta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
