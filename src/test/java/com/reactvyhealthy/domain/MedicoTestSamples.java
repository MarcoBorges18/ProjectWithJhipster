package com.reactvyhealthy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medico getMedicoSample1() {
        return new Medico().id(1L).nome("nome1").email("email1").crm("crm1");
    }

    public static Medico getMedicoSample2() {
        return new Medico().id(2L).nome("nome2").email("email2").crm("crm2");
    }

    public static Medico getMedicoRandomSampleGenerator() {
        return new Medico()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .crm(UUID.randomUUID().toString());
    }
}
