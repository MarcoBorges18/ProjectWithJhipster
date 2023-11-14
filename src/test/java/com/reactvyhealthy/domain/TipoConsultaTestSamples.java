package com.reactvyhealthy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoConsultaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoConsulta getTipoConsultaSample1() {
        return new TipoConsulta().id(1L).nome("nome1");
    }

    public static TipoConsulta getTipoConsultaSample2() {
        return new TipoConsulta().id(2L).nome("nome2");
    }

    public static TipoConsulta getTipoConsultaRandomSampleGenerator() {
        return new TipoConsulta().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString());
    }
}
