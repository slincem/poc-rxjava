package rxtest;

import io.reactivex.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class RxJavaOperatorsTest {

    @Test
    public void mapOperatorTest() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);

        observable.map(Integer::toBinaryString).subscribe(System.out::println);
    }

    @Test
    public void flatMapOperatorTest1() {
        Single<Integer> single = Single.just(1);
        single.flatMap(num -> Single.just(Integer.toBinaryString(num))).subscribe(System.out::println);
    }

    @Test
    public void flatMapOperatorTest2() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable.flatMap(num -> Observable.just(Integer.toBinaryString(num))).subscribe(System.out::println);
    }

    @Test
    public void scanOperatorTest() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable.scan((x, y) -> {
            System.out.println("X es: " + x);
            System.out.println("Y es: " + y);
            return x + y;
        }).subscribe(System.out::println);
    }

    @Test
    public void reduceOperatorTest() {

        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable.reduce((x, y) -> {
            System.out.println("X es: " + x);
            System.out.println("Y es: " + y);
            return x + y;
        }).subscribe(System.out::println);
    }

    @Test
    public void bufferOperatorTest() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        observable.buffer(2).subscribe(System.out::println);
    }

    @Test
    public void bufferOperatorTest2() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // Sumando los valores de la lista usando buffer sería:
        observable.buffer(9).map(list -> list.stream().mapToInt(x -> x).sum()).subscribe(System.out::println);
    }

    @Test
    public void groupByOperatorTest() {
        Observable<Integer> observable = Observable.range(1, 9);

        observable.groupBy(num -> num % 2 == 0).subscribe(eachObservable ->
        {
            System.out.println(eachObservable.toList().subscribe(System.out::println));
            //eachObservable.subscribe(System.out::println, Exception::new, () -> System.out.println("---- Another observable ----"));
        });
    }

    @Test
    public void cacheOperatorTest() throws InterruptedException {
        Flowable<String> nombres = Flowable.fromCallable(() -> {
            Thread.sleep(4000);
            return "Santiago, Lince";
        }).cache();

        nombres.subscribe(nombre -> System.out.println(nombre));

        Thread.sleep(1500);
        Flowable<String> nombresCached = nombres;
        System.out.println("Empezó del cacheado");
        nombresCached.subscribe(System.out::print);
    }

    @Test
    public void filterOperatorTest() {
        Observable<Integer> observable = Observable.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        observable.filter(num -> num % 2 == 0).subscribe(System.out::println);
    }

    @Test
    public void distinctOperatorTest() {
        Observable<Integer> observable = Observable.just(1, 4, 4, 1, 10, 6, 7, 8, 7, 9);
        observable.distinct().subscribe(System.out::println);
    }

    @Test
    public void simpleOperatorsTest() {
        Observable<Integer> observableVacio = Observable.empty();
        Observable<Integer> observable = Observable.range(1, 30);

        observableVacio.first(10).subscribe(System.out::println);
        observable.firstElement().subscribe(System.out::println);
        observableVacio.last(20).subscribe(System.out::println);
        observable.lastElement().subscribe(System.out::println);

        System.out.println("------------ Take --------------");

        observable.take(5).subscribe(num -> System.out.print(num + " - "));
        System.out.println();
        observable.takeLast(5).subscribe(num -> System.out.print(num + " - "));
        System.out.println();
        observable.takeUntil(num -> num > 20).subscribe(num -> System.out.print(num + " - "));
        System.out.println();
        observable.takeWhile(num -> num < 20).subscribe(num -> System.out.print(num + " - "));
        System.out.println();
        System.out.println("--------- Skip ---------");
        observable.skip(20).subscribe(num -> System.out.print(num + " - "));
        System.out.println();
        observable.skipLast(20).subscribe(num -> System.out.print(num + " - "));
        System.out.println();
        // Saltar hasta que otro observable emita
        // observable.skipUntil();
        observable.skipWhile(num -> num < 20).subscribe(num -> System.out.print(num + " - "));
    }

    @Test
    public void mergeOperatorTest() throws InterruptedException {
        Observable<Integer> observableInt = Observable.range(1, 10).delay(1, TimeUnit.SECONDS);
        Observable<String> observableString = Observable.just("Hola", "Mundo", "Rx", "Java");

        Observable.merge(observableInt, observableString).subscribe(System.out::println);
        Thread.sleep(5000);
    }

    @Test
    public void concatOperatorTest() throws InterruptedException {
        Observable<Integer> observableInt = Observable.range(1, 50).delay(1, TimeUnit.SECONDS);
        Observable<String> observableString = Observable.just("Hola", "Mundo", "Rx", "Java");

        Observable.concat(observableInt, observableString).subscribe(System.out::println);
        Thread.sleep(5000);
    }

    // Verificar después
    @Test
    public void switchOperatorTest() throws InterruptedException {
        Observable<String> observableInt = Observable.just("Otra", "Fuente").delay(3, TimeUnit.SECONDS);
        Observable<String> observableString = Observable.just("Hola", "Mundo", "Rx", "Java").repeat();

        Observable<Observable<String>> o = Observable.just(observableString).map(ob -> {
            return observableInt;
        });

        Observable.switchOnNext(o).subscribe(System.out::println);

        Thread.sleep(8000);

    }

    @Test
    public void zipOperatorTest() {
        Observable<Integer> observableInt = Observable.range(1, 50);
        Observable<String> observableString = Observable.just("Hola", "Mundo", "Rx", "Java");

        Observable.zip(observableInt, observableString, (ob1, ob2) -> {
            System.out.println("obInt: " + ob1);
            System.out.println("obString: " + ob2);
            return ob1 + ob2;
        }).subscribe(System.out::println);

    }

    @Test
    public void combineLatestOperatorTest() {
        Observable<Integer> observableInt = Observable.range(1, 50);
        Observable<String> observableString = Observable.just("Hola", "Mundo", "Rx", "Java");

        Observable.combineLatest(observableInt, observableString, (ob1, ob2) -> {
            System.out.println("obInt: " + ob1);
            System.out.println("obString: " + ob2);
            return ob1 + ob2;
        }).subscribe(System.out::println);
    }


    // Verificar después
    @Test
    public void ambOperatorTest() {
        Observable<Integer> observableInt = Observable.range(1, 50);
        Observable<Integer> observableInt2 = Observable.range(1, 50);

        Observable<String> observableString = Observable.just("Hola", "Mundo", "Rx", "Java");

        List<Observable<Integer>> list = new ArrayList<>();
        list.add(observableInt);
        list.add(observableInt2);

        Observable.amb(new Iterable<Observable<Integer>>() {
            @Override
            public Iterator<Observable<Integer>> iterator() {
                return list.iterator();
            }
        }).subscribe(System.out::println);
    }

    @Test
    public void timerOperatorTest() {

    }
}
