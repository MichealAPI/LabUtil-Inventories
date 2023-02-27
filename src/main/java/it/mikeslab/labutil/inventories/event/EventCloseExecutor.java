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

import it.mikeslab.labutil.inventories.annotations.CloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

public class EventCloseExecutor {
    private final String name;
    private Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> objectContent;

    public EventCloseExecutor(String name) {
        this.name = name;
        register();
    }

    public Consumer<InventoryCloseEvent> loadCloseEvent() {
        return (e) -> {
            if (objectContent.isEmpty()) return;

            objectContent.keySet().stream()
                    .filter(obj -> objectContent.get(obj).getKey().equals(name))
                    .forEach(obj -> objectContent.get(obj).getValue()
                            .forEach(method -> {
                                try {
                                    method.invoke(obj, e);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }));
        };
    }

    public EventCloseExecutor register() {
        Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> involvedClasses = EventsManager.instances;
        this.objectContent = getCloseEventMethods(involvedClasses);
        return this;
    }


    private Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> getCloseEventMethods(Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> instances) {
        return getObjectEntryMap(instances, name);
    }

    @NotNull
    static Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> getObjectEntryMap(Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> instances, String name2) {
        Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> objectContent = new HashMap<>();
        List<Method> methods = new ArrayList<>();

        instances.keySet()
                .stream()
                .filter(s -> instances.get(s).getKey().equals(name2))
                .forEach(val -> {
                    String name = instances.get(val).getKey();
                    for (Method method : val.getClass().getDeclaredMethods()) {
                        if (method.isAnnotationPresent(CloseEvent.class)) {
                            methods.add(method);
                        }
                    }
                    objectContent.put(val, new AbstractMap.SimpleEntry<>(name, methods));
                });

        return objectContent;
    }
}
