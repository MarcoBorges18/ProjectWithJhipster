import { IMedico } from 'app/entities/medico/medico.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITipoConsulta } from 'app/entities/tipo-consulta/tipo-consulta.model';

export interface IAgendamento {
  id: number;
  valorFinal?: number | null;
  medico?: Pick<IMedico, 'id'> | null;
  paciente?: Pick<IPaciente, 'id'> | null;
  tipo?: Pick<ITipoConsulta, 'id'> | null;
}

export type NewAgendamento = Omit<IAgendamento, 'id'> & { id: null };
