package it.mikeslab.labutil.inventories.event;

import java.util.*;

public class ExecutorUtil {
    public static Map<Object, String> instances = new HashMap<>();

    public static void registerEvent(Object object, String inventoryName) {
        instances.put(object, inventoryName);
    }

    public static void unregisterEvent(Object object) {
        instances.remove(object);
    }




}
