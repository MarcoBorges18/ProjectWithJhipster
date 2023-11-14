package com.reactvyhealthy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnderecoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Endereco getEnderecoSample1() {
        return new Endereco()
            .id(1L)
            .logradouro("logradouro1")
            .bairro("bairro1")
            .cep("cep1")
            .numero("numero1")
            .complemento("complemento1")
            .cidade("cidade1")
            .uf("uf1");
    }

    public static Endereco getEnderecoSample2() {
        return new Endereco()
            .id(2L)
            .logradouro("logradouro2")
            .bairro("bairro2")
            .cep("cep2")
            .numero("numero2")
            .complemento("complemento2")
            .cidade("cidade2")
            .uf("uf2");
    }

    public static Endereco getEnderecoRandomSampleGenerator() {
        return new Endereco()
            .id(longCount.incrementAndGet())
            .logradouro(UUID.randomUUID().toString())
            .bairro(UUID.randomUUID().toString())
            .cep(UUID.randomUUID().toString())
            .numero(UUID.randomUUID().toString())
            .complemento(UUID.randomUUID().toString())
            .cidade(UUID.randomUUID().toString())
            .uf(UUID.randomUUID().toString());
    }
}
