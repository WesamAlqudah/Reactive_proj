package Test_1;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    @Test
    void firstMono(){

        Mono.just("A")
                .log()
                .subscribe();
    }

    @Test
    void monoWithDoOn(){

        Mono.just("A")
                .log()
                .doOnSubscribe(subs -> System.out.println("Subscribed" + subs))
                .doOnRequest(request -> System.out.println("request" + request))
                .doOnSuccess(complete -> System.out.println("request" + complete))
                .subscribe();

    }

    @Test
    void EmptyMono() {

        Mono.empty()
                .log()
                .subscribe(System.out::println);
    }

    @Test
    void EmptyCompleteConsumerMono() {

        Mono.empty()
                .log()
                .subscribe(System.out::println,
                        null,
                        ()->System.out.println("Done")
                );
    }

    @Test
    void errorRuntimeExcMono() {

        Mono.error(new RuntimeException())
                .log()
                .subscribe();
    }

    @Test
    void errorExcMono() {

        Mono.error(new Exception())
                .log()
                .subscribe();
    }

    @Test
    void errorConsumerMono() {

        Mono.error(new Exception())
                .log()
                .subscribe(System.out::println,
                        e-> System.out.println("Error: "+ e));
    }

    @Test
    void errorDoOnMono() {

        Mono.error(new Exception())
                .doOnError(e-> System.out.println("Error: "+ e))
                .log()
                .subscribe();
    }

    @Test
    void errorOnErrorResumeMono() {

        Mono.error(new Exception())
                .onErrorResume(e-> {System.out.println("caught: "+ e);
                    return Mono.just("B");
                })
                .log()
                .subscribe();
    }

    }
