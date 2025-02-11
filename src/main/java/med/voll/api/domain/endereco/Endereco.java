package med.voll.api.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private String logradouro;

    private String bairro;

    private String cep;

    private String numero;

    private String complemento;

    private String cidade;

    private String uf;

    public Endereco(DadosEndereco dados) {
        this.logradouro = dados.logradouro();
        this.bairro = dados.bairro();
        this.cep = dados.cep();
        this.numero = dados.numero();
        this.complemento = dados.complemento();
        this.cidade = dados.cidade();
        this.uf = dados.uf();
    }

    public void atualizarInformacoes(DadosEndereco dados) {
        if (Objects.nonNull(dados.logradouro())) {
            this.logradouro = dados.logradouro();
        }
        if (Objects.nonNull(dados.bairro())) {
            this.bairro = dados.bairro();
        }
        if (Objects.nonNull(dados.cep())) {
            this.cep = dados.cep();
        }
        if (Objects.nonNull(dados.numero())) {
            this.numero = dados.numero();
        }
        if (Objects.nonNull(dados.complemento())) {
            this.complemento = dados.complemento();
        }
        if (Objects.nonNull(dados.cidade())) {
            this.cidade = dados.cidade();
        }
        if (Objects.nonNull(dados.uf())) {
            this.uf = dados.uf();
        }
    }
}

