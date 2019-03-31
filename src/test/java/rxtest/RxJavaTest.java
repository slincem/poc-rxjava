package rxtest;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class RxJavaTest {

    @Test
    public void prueba1(){
        Flowable<String> nombres = Flowable.just("Capitan America", "Iron man", "Spider man");

        nombres.map(String::toUpperCase).subscribe(System.out::println);

        System.out.println("Finished");
    }

    @Test
    public void prueba2(){
        Flowable<String> nombres = Flowable.just("Capitan America", "Iron man", "Spider man");

        nombres.observeOn(Schedulers.computation()).map(String::toUpperCase).subscribe(System.out::println);

        System.out.println("Finished");
    }

    @Test
    public void prueba3() throws InterruptedException{
        Single<String> nombre = Single.just("Santiago");
        nombre.subscribeOn(Schedulers.computation()).map(String::toUpperCase).subscribe((nombreActual) -> {
            System.out.println(Thread.currentThread().getName() + " " + nombreActual);
        });
        System.out.println("Finished");
        Thread.sleep(2000);
    }


    @Test
    public void prueba4() {
        Flowable<String> nombres = Flowable.just("Capitan America", "Iron man", "Spider man");
        Disposable suscriptor = nombres.subscribe(nombre -> System.out.println(nombre));

        suscriptor.dispose();
    }

    @Test
    public void prueba5() throws  InterruptedException{
        Flowable<String> nombres = Flowable.just("Capitan America", "Iron man", "Spider man");
        Disposable suscriptor = nombres.observeOn(Schedulers.computation()).subscribe(nombre -> System.out.println(nombre));

        suscriptor.dispose();
        Thread.sleep(2000);
    }

    @Test
    public void prueba6() throws  InterruptedException{
        Flowable.fromCallable(() -> {
            Thread.sleep(2000);
            return "Santiago, Lince";
        }).subscribe(System.out::print);
    }

    @Test
    public void prueba7() throws  InterruptedException {
        Flowable<String> nombres = Flowable.fromCallable(() -> {
            Thread.sleep(2000);
            return "Santiago, Lince";
        });

        nombres.subscribe(nombre -> System.out.println(nombre));
    }
    @Test
    public void prueba8() throws  InterruptedException {
        Flowable<String> nombres = Flowable.fromCallable(() -> {
            Thread.sleep(4000);
            return "Santiago, Lince";
        }).cache();

        nombres.subscribe(nombre -> System.out.println(nombre));

        Flowable<String> nombresCached = nombres;
        System.out.println("Empezó del cacheado");
        nombresCached.subscribe(System.out::print);
    }

    @Test
    public void pruebaAvoidBackpressureSampleMethod9() throws InterruptedException {
        Flowable<String> nombres = Flowable.just("Capitan America", "Iron man", "Spider man",
                "Black Panter", "Dr Strange", "Thanos", "Thor");

        nombres.sample(1, TimeUnit.NANOSECONDS).subscribe(System.out::println);

        Thread.sleep(10000);
    }

    @Test
    public void pruebaHandlingError(){
        Flowable<Integer> numeros = Flowable.just(1,2,3,4,5,6,7);
        numeros.subscribe(number -> System.out.println(number/0), throwable -> System.out.println("Se lanza una excepción"));
    }

    @Test
    public void pruebaHandlingErrorAtTheMiddleOfTheFlow(){
        Flowable<Integer> numeros = Flowable.just(1,2,3,4,5,6,7);
        numeros.subscribe(number -> {
                    number = number == 3 ? number/0:number;
                    System.out.println(number);
                },
                throwable -> System.out.println("Se lanza una excepción"));
    }

    @Test
    public void pruebaHandlingErrorWithoutErrors(){
        Flowable<Integer> numeros = Flowable.just(1,2,3,4,5,6,7);
        numeros.subscribe(number -> System.out.println(number), throwable -> System.out.println("No paso nada"));
    }
}
