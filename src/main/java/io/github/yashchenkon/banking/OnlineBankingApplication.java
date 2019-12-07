package io.github.yashchenkon.banking;

import com.google.inject.Guice;

import io.github.yashchenkon.banking.infra.OnlineBankingApplicationModule;
import io.github.yashchenkon.banking.infra.OnlineBankingApplicationServer;

public class OnlineBankingApplication {

    public static void main(String[] args) {
        Guice.createInjector(new OnlineBankingApplicationModule())
            .getInstance(OnlineBankingApplicationServer.class)
            .run(8080);
    }
}
