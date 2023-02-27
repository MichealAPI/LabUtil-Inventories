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

import it.mikeslab.labutil.inventories.annotations.ItemFinalizing;
import it.mikeslab.labutil.inventories.component.CustomInventory;
import it.mikeslab.labutil.inventories.component.CustomItem;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.util.Consumer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemFinalizingExecutor {
    private final String name;
    private Map<Object, Method> methods;

    public ItemFinalizingExecutor(String name) {
        this.name = name;
        register();
    }

    public CustomItem loadItemFinalizingExecutor(CustomItem customItem, int slot) {
        if (methods.isEmpty()) return customItem;

        for (Map.Entry<Object, Method> entry : methods.entrySet()) {
            try {
                Method method = entry.getValue();
                Object instance = entry.getKey();
                return (CustomItem) method.invoke(instance, customItem, slot);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return customItem;
    }


    public ItemFinalizingExecutor register() {
        Map<Object, String> involvedClasses = EventsManager.instances;
        this.methods = getItemFinalizingMethods(involvedClasses);
        return this;
    }


    private Map<Object, Method> getItemFinalizingMethods(Map<Object, String> objects) {
        Map<Object, Method> methods = new HashMap<>();


        for(Map.Entry<Object, String> entry : objects.entrySet()) {
            if(!Objects.equals(entry.getValue(), name)) continue;

            Object instance = entry.getKey();

            Class<?> clazz = instance.getClass();
            Method[] classMethods = clazz.getMethods();

            for(Method method : classMethods) {
                if(method.isAnnotationPresent(ItemFinalizing.class)) {
                    methods.put(instance, method);
                }
            }
        }
        return methods;
    }
}
