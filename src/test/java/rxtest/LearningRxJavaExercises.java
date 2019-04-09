package rxtest;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.ResourceObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class LearningRxJavaExercises {

    @Test
    public void observerToSubscribeObservable() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observer<Integer> myObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer value) {
                System.out.println("Received in On Next: " + value);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("IS DONE");
            }
        };

        source.map(String::length).filter(i -> i >= 5)
                .subscribe(myObserver);

    }

    @Test
    public void shortHandObserverToSubscribeObservable() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Consumer<Integer> onNext = (value) -> System.out.println("Received in On Next: " + value);
        Consumer<Throwable> onError = Throwable::printStackTrace;
        Action onComplete = () -> System.out.println("IS DONE");

        source.map(String::length).filter(i -> i >= 5)
                .subscribe(onNext, onError, onComplete);

    }

    @Test
    public void coldObservable() {
        Observable<String> source =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        source.subscribe(s -> System.out.println("Observer 1: " + s));
        source.map(String::length).subscribe(s -> System.out.println("Observer 2: " + s));

    }

    @Test
    public void connectableObservable() {
        ConnectableObservable<String> source =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon").publish();

        source.subscribe(s -> System.out.println("Observer 1: " + s));
        source.map(String::length).subscribe(s -> System.out.println("Observer 2: " + s));

        // Fire
        source.connect();

    }

    @Test
    public void connectableObservableWithInterval() {

        // Interval por defecto sería Cold, a menos que lo haga un ConnectableObservable a traves del publish
        ConnectableObservable<Long> source =
                Observable.interval(1, TimeUnit.SECONDS).publish();

        source.subscribe(s -> System.out.println("Observer 1: " + s));
        // Fire
        source.connect();
        sleep(5000);

        // Observer 2 se perdio de las primeras 5 emisiones. (Hot Observable)
        source.subscribe(s -> System.out.println("Observer 2: " + s));
        sleep(5000);

    }

    @Test
    public void disposableSimpleUse(){
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);
        Disposable disposable = source.subscribe(System.out::println);

        sleep(5000);
        disposable.dispose();
        System.out.println("Dormir hilo");
        sleep(3000);
    }

    //Manejando Disposable dentro del Observer!!! (DENTRO).
    @Test
    public void disposableIntoTheObserverUse() {

        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);
        Observer observer =new Observer<Long>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable disposable) {
                this.disposable = disposable;
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
                disposable.dispose();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("DONE");
            }
        };

        source.subscribe(observer);

        sleep(4000);
    }

    //Manejando Disposable dentro del Observer!!! (DENTRO).
    // Este maneja el disposable por nosotros. Por si no estamos seguros de cuando o como ejecutar el mismo.
    // O si simplemente queremos delegar la responsabilidad a RxJava.
    @Test
    public void resourceObserverUse() {

        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);

        ResourceObserver<Long> observer = new ResourceObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("DONE");
            }
        };

        //Para ResourceObserver debe usarse este metodo.
        Disposable disposable = source.subscribeWith(observer);
        sleep(3000);
        disposable.dispose();
        sleep(3000);

    }

    // Se puede tener una coleccion de Diposables para luego ejecutar el dispose() de todos al mismo tiempo.
    @Test
    public void compositeDisposableUse() {
        CompositeDisposable disposables = new CompositeDisposable();

        Disposable disposable1 = Observable.interval(1, TimeUnit.SECONDS).subscribe(l -> System.out.println("Observer 1: " + l));
        Disposable disposable2 = Observable.interval(1, TimeUnit.SECONDS).subscribe(l -> System.out.println("Observer 2: " + l));

        disposables.addAll(disposable1, disposable2);

        sleep(3000);
        disposables.dispose();
        sleep(4000);
    }

    @Test
    public void handlingDisposalWithObservableDotCreate() {
        Observable<Integer> source =
                Observable.create(observableEmitter -> {
                    try {
                        for (int i = 0; !observableEmitter.isDisposed(); i++) {
                            observableEmitter.onNext(i);
                            if(observableEmitter.isDisposed())
                                return;
                        }

                    observableEmitter.onComplete();
                    } catch (Exception e){
                        observableEmitter.onError(e);
                    }
                });

        source.subscribe(System.out::println);
    }



    public	static	void sleep(int	millis)	{
        try	{
            Thread.sleep(millis);
        } catch	(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
