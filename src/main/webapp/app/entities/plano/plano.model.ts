export interface IPlano {
  id: number;
  convenio?: string | null;
  desconto?: number | null;
}

export type NewPlano = Omit<IPlano, 'id'> & { id: null };
