package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoMedico> cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        Medico medico = new Medico(dados);
        repository.save(medico);
        URI uri = uriBuilder.path("/medidos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(sort = {"nome"}) Pageable paginacao) {
        Page<DadosListagemMedico> page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoMedico> atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        Medico medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        Medico medico = repository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoMedico> detalhar(@PathVariable Long id) {
        Medico medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}
