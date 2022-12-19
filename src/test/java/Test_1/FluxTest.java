package Test_1;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FluxTest {

    @Test
    void firstFlux(){

        Flux.just("A","B","C")
                .log()
                .subscribe();

    }

    @Test
    void fluxIterable(){

//        Flux.from(Arrays.asList("A","B","C"))
//        Flux.fromIterable(List.of("A", "B"));
        Flux.fromIterable(Arrays.asList("A","B","C"))
                .log()
                .subscribe();

    }

    @Test
    void fluxFromRange(){

        Flux.range(10,5)
                .log()
                .subscribe();

    }

    @Test
    void fluxFromInterval() throws Exception {

        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(2)//will complete after publish 2 values
                .subscribe();
        Thread.sleep(5000);

    }

    @Test
    void fluxRequest(){

        Flux.range(2,5)
                .log()
                .subscribe(null,
                        null,
                        null,
                        subscription -> subscription.request(3));
        // alternative
//        Flux.range(1,5)
//                .log()
//                .subscribe(new BaseSubscriber<Integer>() {
//                               @Override
//                               protected void hookOnSubscribe(Subscription s) {
//                                   s.request(3L);
//                               }
//                           });
    }

    @Test
    void fluxLimitRate() {

        Flux.range(1, 5)
                .log()
                .limitRate(3)
                .subscribe();

    }

    }
