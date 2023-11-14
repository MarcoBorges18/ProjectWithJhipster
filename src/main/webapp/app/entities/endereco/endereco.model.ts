export interface IEndereco {
  id: number;
  logradouro?: string | null;
  bairro?: string | null;
  cep?: string | null;
  numero?: string | null;
  complemento?: string | null;
  cidade?: string | null;
  uf?: string | null;
}

export type NewEndereco = Omit<IEndereco, 'id'> & { id: null };
