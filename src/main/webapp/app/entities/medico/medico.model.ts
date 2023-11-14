import { IEspecialidade } from 'app/entities/especialidade/especialidade.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';

export interface IMedico {
  id: number;
  nome?: string | null;
  email?: string | null;
  crm?: string | null;
  ativo?: boolean | null;
  especialidade?: Pick<IEspecialidade, 'id'> | null;
  endereco?: Pick<IEndereco, 'id'> | null;
}

export type NewMedico = Omit<IMedico, 'id'> & { id: null };
