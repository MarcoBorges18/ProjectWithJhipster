package com.reactvyhealthy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente().id(1L).nome("nome1").email("email1").cpf("cpf1").telefone("telefone1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente().id(2L).nome("nome2").email("email2").cpf("cpf2").telefone("telefone2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .cpf(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString());
    }
}
