package com.guxuede.gm.net.utils;

import java.util.LinkedList;
import java.util.function.Consumer;

public class PackQueue<T> extends LinkedList<T> {

    public void consumerAll(Consumer<T> consumer){
        T p;
        while((p=poll())!=null){
            consumer.accept(p);
        }
    }
}
