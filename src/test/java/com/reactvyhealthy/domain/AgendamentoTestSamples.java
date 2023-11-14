package com.reactvyhealthy.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AgendamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Agendamento getAgendamentoSample1() {
        return new Agendamento().id(1L);
    }

    public static Agendamento getAgendamentoSample2() {
        return new Agendamento().id(2L);
    }

    public static Agendamento getAgendamentoRandomSampleGenerator() {
        return new Agendamento().id(longCount.incrementAndGet());
    }
}
