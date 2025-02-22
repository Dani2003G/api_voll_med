package med.voll.api.domain.consulta;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgendaDeConsulta {

    private final ConsultaRepository consultaRepository;

    private final MedicoRepository medicoRepository;

    private final PacienteRepository pacienteRepository;

    private final List<ValidadorAgendamentoDeConsulta> validadores;

    private final List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
        if (!pacienteRepository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (Objects.nonNull(dados.idMedico()) && !medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("Id do médico informado não existe!");
        }

        validadores.forEach(validador -> validador.validar(dados));

        Paciente paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        Medico medico = escolherMedido(dados);
        if (Objects.isNull(medico)) {
            throw new ValidacaoException("Não existe médico disponível nessa data!");
        }

        Consulta consulta = new Consulta(null, medico, paciente, dados.data(), null);

        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedido(DadosAgendamentoConsulta dados) {
        if (Objects.nonNull(dados.idMedico())) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (Objects.isNull(dados.especialidade())) {
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }

    public void cancelar(@Valid DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ServiceException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        Consulta consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }
}
