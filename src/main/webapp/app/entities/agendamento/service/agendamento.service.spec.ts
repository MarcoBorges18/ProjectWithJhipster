import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAgendamento } from '../agendamento.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../agendamento.test-samples';

import { AgendamentoService } from './agendamento.service';

const requireRestSample: IAgendamento = {
  ...sampleWithRequiredData,
};

describe('Agendamento Service', () => {
  let service: AgendamentoService;
  let httpMock: HttpTestingController;
  let expectedResult: IAgendamento | IAgendamento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgendamentoService);
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

    it('should create a Agendamento', () => {
      const agendamento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(agendamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Agendamento', () => {
      const agendamento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(agendamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Agendamento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Agendamento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Agendamento', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAgendamentoToCollectionIfMissing', () => {
      it('should add a Agendamento to an empty array', () => {
        const agendamento: IAgendamento = sampleWithRequiredData;
        expectedResult = service.addAgendamentoToCollectionIfMissing([], agendamento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agendamento);
      });

      it('should not add a Agendamento to an array that contains it', () => {
        const agendamento: IAgendamento = sampleWithRequiredData;
        const agendamentoCollection: IAgendamento[] = [
          {
            ...agendamento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAgendamentoToCollectionIfMissing(agendamentoCollection, agendamento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Agendamento to an array that doesn't contain it", () => {
        const agendamento: IAgendamento = sampleWithRequiredData;
        const agendamentoCollection: IAgendamento[] = [sampleWithPartialData];
        expectedResult = service.addAgendamentoToCollectionIfMissing(agendamentoCollection, agendamento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agendamento);
      });

      it('should add only unique Agendamento to an array', () => {
        const agendamentoArray: IAgendamento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const agendamentoCollection: IAgendamento[] = [sampleWithRequiredData];
        expectedResult = service.addAgendamentoToCollectionIfMissing(agendamentoCollection, ...agendamentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agendamento: IAgendamento = sampleWithRequiredData;
        const agendamento2: IAgendamento = sampleWithPartialData;
        expectedResult = service.addAgendamentoToCollectionIfMissing([], agendamento, agendamento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agendamento);
        expect(expectedResult).toContain(agendamento2);
      });

      it('should accept null and undefined values', () => {
        const agendamento: IAgendamento = sampleWithRequiredData;
        expectedResult = service.addAgendamentoToCollectionIfMissing([], null, agendamento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agendamento);
      });

      it('should return initial array if no Agendamento is added', () => {
        const agendamentoCollection: IAgendamento[] = [sampleWithRequiredData];
        expectedResult = service.addAgendamentoToCollectionIfMissing(agendamentoCollection, undefined, null);
        expect(expectedResult).toEqual(agendamentoCollection);
      });
    });

    describe('compareAgendamento', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAgendamento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAgendamento(entity1, entity2);
        const compareResult2 = service.compareAgendamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAgendamento(entity1, entity2);
        const compareResult2 = service.compareAgendamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAgendamento(entity1, entity2);
        const compareResult2 = service.compareAgendamento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
