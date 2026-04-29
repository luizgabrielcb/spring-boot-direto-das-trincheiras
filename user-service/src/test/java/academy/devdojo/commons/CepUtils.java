package academy.devdojo.commons;

import academy.devdojo.response.CepGetResponse;
import org.springframework.stereotype.Component;

@Component
public class CepUtils {
    public CepGetResponse newCepGetResponse() {
       return CepGetResponse.builder()
               .cep("00000000")
               .state("São Paulo")
               .city("Osasco")
               .neighborhood("xaxa")
               .street("rua 123")
               .service("viacep")
               .build();
    }
}
