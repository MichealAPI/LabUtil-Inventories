/*
 * Copyright (c) 2023 MikesLab
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.mikeslab.labutil.inventories.event;

import it.mikeslab.labutil.inventories.annotations.OpenEvent;
import it.mikeslab.labutil.inventories.component.CustomItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ItemFinalizingExecutor {
    private final String name;
    private Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> objectContent;

    public ItemFinalizingExecutor(String name) {
        this.name = name;
        register();
    }

    public CustomItem loadItemFinalizingExecutor(CustomItem customItem, int slot) {
        if (objectContent.isEmpty()) return customItem;
        AtomicReference<AbstractMap.SimpleEntry<Object, Method>> methodEntry = new AtomicReference<>();
        
        objectContent.keySet().stream()
                .filter(obj -> objectContent.get(obj).getKey().equals(name))
                .forEach(obj -> methodEntry.set(new AbstractMap.SimpleEntry<>(obj, objectContent.get(obj).getValue().get(0))));
        try {
            return (CustomItem) methodEntry.get().getValue().invoke(methodEntry.get().getKey(), customItem, slot);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }



    public ItemFinalizingExecutor register() {
        Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> involvedClasses = EventsManager.instances;
        this.objectContent = getItemFinalizingMethods(involvedClasses);
        return this;
    }


    private Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> getItemFinalizingMethods(Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> instances) {
        Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> objectContent = new HashMap<>();
        List<Method> methods = new ArrayList<>();

        instances.keySet()
                .stream()
                .filter(s -> instances.get(s).getKey().equals(name))
                .forEach(val -> {
                    String name = instances.get(val).getKey();
                    for (Method method : val.getClass().getDeclaredMethods()) {
                        if (method.isAnnotationPresent(OpenEvent.class)) {
                            methods.add(method);
                        }
                    }
                    objectContent.put(val, new AbstractMap.SimpleEntry<>(name, methods));
                });

        return objectContent;
    }
}
