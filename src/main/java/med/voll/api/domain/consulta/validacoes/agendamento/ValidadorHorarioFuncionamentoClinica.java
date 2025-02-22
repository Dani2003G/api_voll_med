package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta {

    public void validar(DadosAgendamentoConsulta dados) {
        LocalDateTime dataConsulta = dados.data();

        Boolean domindo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        Boolean antesDataAberturaDaClinica = dataConsulta.getHour() < 7;
        Boolean depoisDoEncerramentoDaClinica = dataConsulta.getHour() > 18;
        if (domindo || antesDataAberturaDaClinica || depoisDoEncerramentoDaClinica) {
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clinica");
        }
    }

}
