export interface IEspecialidade {
  id: number;
  nome?: string | null;
}

export type NewEspecialidade = Omit<IEspecialidade, 'id'> & { id: null };
