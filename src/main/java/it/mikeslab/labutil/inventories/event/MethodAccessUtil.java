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


import com.google.common.collect.HashBasedTable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;

/**
 * A utility class that allows accessing the methods of a given class and invoking a method on an instance of the class
 * with a specified name using MethodHandle for improved performance.
 */
public class MethodAccessUtil {
    private static final HashBasedTable<Class<?>, String, MethodHandle> methodHandles = HashBasedTable.create();


    private MethodAccessUtil() {
        // Utility class, not instantiable
    }

    /**
     * Invokes the method with the given name on the specified object instance using MethodHandle for improved
     * performance.
     *
     * @param object the object instance to invoke the method on
     * @param name   the name of the method to invoke
     * @param args   the arguments to pass to the method being invoked
     * @throws Throwable if an exception occurs during the method invocation
     */
    public static void invokeMethod(Object object, String name, Object... args) throws Throwable {
        if(methodHandles.contains(object.getClass(), name)) {
            Objects.requireNonNull(methodHandles.get(object.getClass(), name)).invokeWithArguments(args);
        } else {
            MethodHandle methodHandle = MethodHandles.lookup().findVirtual(object.getClass(), name, MethodType.methodType(Object.class, Object[].class));
            methodHandles.put(object.getClass(), name, methodHandle);
            methodHandle.invokeWithArguments(args);
        }
    }
}
