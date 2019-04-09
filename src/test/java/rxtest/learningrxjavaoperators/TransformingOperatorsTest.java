package rxtest.learningrxjavaoperators;

import io.reactivex.Observable;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransformingOperatorsTest {

    @Test
    public void map() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");

        Observable.just("1/3/2016", "5/9/2018", "10/24/2019")
        .map(d -> LocalDate.parse(d, dtf))
        .subscribe(dateFormatted -> System.out.println("RECEIVED: " + dateFormatted));
    }

    @Test
    public void cast() {

        Observable<Object> items = Observable.just("Alpha", "Beta", "Gamma")
                .map(s -> (Object) s);

        // Cast es una especializacion del map

        Observable<Object> items2 = Observable.just("Alpha", "Beta", "Gamma")
                .cast(Object.class);

        items.subscribe(o -> System.out.println("Observer 1 Received: " + o.getClass()));
        System.out.println(" ------------------------ ");
        items2.subscribe(o -> System.out.println("Observer 2 Received: " + o.getClass()));
    }

    @Test
    public void startWith() {
        Observable<Object> superheroNames = Observable.just("Captain America", "Ironman", "Spiderman");

        //Agrega un elemento al inicio...
        superheroNames
                .startWith("-- NOMBRES DE SUPERHEROES --")
                .subscribe(System.out::println);
    }

    @Test
    public void startWithArray() {
        Observable<Object> superheroNames = Observable.just("Captain America", "Ironman", "Spiderman");

        // Para agregar varios elementos al inicio
        superheroNames
                .startWithArray("NOMBRES DE SUPERHEROES", "------------")
                .subscribe(System.out::println);
    }

    @Test
    public void defaultIfEmpty() {
        Observable<String> items = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        items.filter(s -> s.startsWith("Z"))
                .defaultIfEmpty("None") // Se retorna esto solo si el Observable recibido es Empty
                .subscribe(s -> System.out.println("RECEIVED: " + s));
    }

    @Test
    public void switchIfEmpty() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .filter(s -> s.startsWith("Z"))
                .switchIfEmpty(Observable.just("Zeta", "Eta", "Theta")) // Emitira este observable si el recibido es empty
                .subscribe(
                        i -> System.out.println("RECEIVED: " + i),
                        e -> System.out.println("RECEIVED ERROR: " + e)
                );
    }

    // Si se tiene un Observable finito y su items implementan Comparable<T>
    // Internamente, se colectaran lasemisiones y se reemitiran en el orden dado
    @Test
    public void sorted() {
        Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                .sorted()
                .subscribe(System.out::println);

        // Se puede pasar un Comparator para espeficar explicitamente el orden.

    }


    public	static	void sleep(int	millis)	{
        try	{
            Thread.sleep(millis);
        } catch	(InterruptedException e) {
            e.printStackTrace();
        }
    }


}
