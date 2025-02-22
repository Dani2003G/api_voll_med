package med.voll.api.domain.consulta.validacoes.agendamento;

import lombok.RequiredArgsConstructor;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {

    private final MedicoRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        // escolha do medico opcional
        if (Objects.isNull(dados.idMedico())) {
            return;
        }

        Boolean medicoEstaAtivo = repository.findAtivoByid(dados.idMedico());
        if(!medicoEstaAtivo) {
            throw new ValidacaoException("Consulta não pode ser agendada com médico excluído");
        }
    }

}
