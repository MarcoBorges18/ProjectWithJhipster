import dayjs from 'dayjs/esm';

export interface ITipoConsulta {
  id: number;
  nome?: string | null;
  tempo?: dayjs.Dayjs | null;
  valorConsulta?: number | null;
}

export type NewTipoConsulta = Omit<ITipoConsulta, 'id'> & { id: null };
