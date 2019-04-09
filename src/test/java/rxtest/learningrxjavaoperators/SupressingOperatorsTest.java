package rxtest.learningrxjavaoperators;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SupressingOperatorsTest {

    @Test
    public void filter(){
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .filter(s	->	s.length()	!=	5)
                .subscribe(s	->	System.out.println("RECEIVED:	" + s));
    }

    @Test
    public void take() {
        //First Way
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .take(3)
                .subscribe(s	->	System.out.println("RECEIVED:	" +	s));

        System.out.println(" ----------------- ");

        //Second way - Toma las emisiones durante 2 segundos... Alcanza  tomar 6 para este caso... 6 * 300 = 1800...
        // No alcanza a tomar la siguiente.
        Observable.interval(300,	TimeUnit.MILLISECONDS)
                .take(2,	TimeUnit.SECONDS)
                .subscribe(i	->	System.out.println("RECEIVED:	" +	i));
        sleep(5000);

    }

    // Los *Last() también causan delay, debido a que deben confirmar hasta que llegue el último para empezar a emitir.

    // Va a crear una Cola de emisiones hasta que el onComplete() es llamado
    // De esta manera al final puede identificar las ultimas emisiones para emitirlas
    @Test
    public void takeLast(){
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .takeLast(2)
                .subscribe(s ->	System.out.println("RECEIVED:	" +	s));
    }

    @Test
    public void takeWhile() {
        Observable.range(1, 100)
                .takeWhile(i -> i < 5)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    @Test
    public void takeUntil() {
        Observable.range(1, 100)
                .takeUntil(i -> i >= 5)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    @Test
    public void skip() {
        Observable.range(1, 100)
                .skip(90)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    // Va a crear una Cola de emisiones hasta que el onComplete() es llamado
    // De esta manera al final puede identificar las ultimas emisiones para emitirlas
    @Test
    public void skipLast() {
        Observable.range(1, 100)
                .skipLast(90)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    @Test
    public void skipWhile() {
        Observable.range(1, 100)
                .skipWhile(i -> i <= 95)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    // Verificar después
    @Test
    public void skipUntil() {
        Observable.range(1, 100)
                .skipUntil(Observable.interval(300, TimeUnit.SECONDS))
                .subscribe(i -> System.out.println("RECEIVED: " + i));

        sleep(5000);
    }

    @Test
    public void distinct() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .map(String::length)
                .distinct()
                .subscribe(s ->	System.out.println("RECEIVED: "	+ s));
    }

    @Test
    public void distinctByArgument() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .distinct(String::length)
                .subscribe(s ->	System.out.println("RECEIVED:	"	+	s));
    }

    // No acepta repetidos inmediatamente siguientes.
    @Test
    public void distinctUntilChanged() {
        Observable.just(1, 1, 1, 2, 2, 3, 3, 2, 1, 1)
                .distinctUntilChanged()
                .subscribe(s ->	System.out.println("RECEIVED:	"	+	s));
    }

    @Test
    public void distinctUntilChangedByArgument() {
        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma", "Delta")
                .distinctUntilChanged(String::length)
                .subscribe(s ->	System.out.println("RECEIVED:	"	+	s));
    }


    // Retorna un Maybe<T> por si en la posicion dada no hay elemento...
    // Es decir la cantidad de emisiones es menor a la posicion
    @Test
    public void elementAt() {
        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma", "Delta")
                .elementAt(6)
                .subscribe(
                        s ->	System.out.println("RECEIVED:	"	+	s),
                        Throwable::printStackTrace,
                        () -> System.out.println("No se encontró elemento") // entra aqui
                );
    }

    //Igual que elementAt, pero retorna un Single<T>,
    //por tanto habra un error si no se encuentra un elemento en ese indice
    @Test
    public void elementAtOrError() {
        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma", "Delta")
                .elementAtOrError(6)
                .subscribe(
                        s ->	System.out.println("RECEIVED:	"	+	s),
                        e -> System.out.println("Error, porque no se encontró elemento") // entra aqui
                );
    }

    @Test
    public void firstElement(){
        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma", "Delta")
                .firstElement() //Maybe
                .subscribe(s ->	System.out.println("RECEIVED:	"	+	s));
    }

    @Test
    public void lastElement(){
        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma", "Delta")
                .lastElement() //Maybe
                .subscribe(s ->	System.out.println("RECEIVED:	"	+	s));
    }


    public	static	void sleep(int	millis)	{
        try	{
            Thread.sleep(millis);
        } catch	(InterruptedException e) {
            e.printStackTrace();
        }
    }






}
