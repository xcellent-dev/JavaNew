/*
 */
package com.gtanalysis.gtexcel.model;

/**
 *
 * @author nkabiliravi
 * @param <K> Generic for the key type
 * @param <V> Generic for the value type
 */
public class KeyValue<K, V> {

    private K key;
    private V value;

    public KeyValue() {
    }

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
