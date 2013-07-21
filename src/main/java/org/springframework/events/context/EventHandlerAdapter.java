/*
 * Copyright (C) the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.events.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.events.annotation.EventHandler;
import org.springframework.util.ReflectionUtils;

import org.springframework.events.Event;
import org.springframework.events.Event.Handler;

/**
 * Adapter class for any object that has at least one method that qualifies to
 * be an event handler. The object is adapted to {@link Handler} supporting
 * {@link Event} type of events. Invocation of {@link #handleEvent(Event)}
 * method looks up for matching handler from a list of methods that
 * have been detected upon adapter instantiation. All found matching handlers
 * are invoked.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see Handler
 */
public final class EventHandlerAdapter implements Handler<Event> {

    private final Object target;

    private final Map<Class<?>, List<Method>> handlers =
            new HashMap<Class<?>, List<Method>>();

    /**
     * Class constructor accepting prospective event handler object.
     * If passed object does not have any methods that meet event handler
     * specification the {@link IllegalArgumentException} is thrown so it is
     * important to make sure earlier if object that is about to be passed
     * is valid.
     *
     * @param target anticipated object containing event handling methods.
     */
    public EventHandlerAdapter(final Object target) {
        scanForHandlers(target);
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleEvent(final Event event) {
        for (final Method method : getMethods(event.getClass())) {
            ReflectionUtils.invokeMethod(method, target, event);
        }
    }

    /**
     * Scans for methods decorated with {@link org.springframework.events.annotation.EventHandler} annotations.
     * Found methods are recorded for the later invocation and associated with
     * event type they wish to receive.
     *
     * @param target subject of scanning for annotation
     */
    private void scanForHandlers(final Object target) {
        ReflectionUtils.doWithMethods(target.getClass(),
                new ReflectionUtils.MethodCallback() {

                    @Override
                    public void doWith(final Method method)
                            throws IllegalArgumentException,
                            IllegalAccessException {
                        if (method.getAnnotation(EventHandler.class) != null) {
                            EventHandlerAdapter.this.addHandler(method);
                        }
                    }
                });
    }

    /**
     * Gets list of handler methods for particular event type.
     *
     * @param type event type.
     * @return either list of target's handler methods or an empty list
     * if there is no handler for particular event.
     */
    private List<Method> getMethods(final Class<?> type) {
        final List<Method> methods = handlers.get(type);
        if (methods != null) {
            return methods;
        }
        return Collections.emptyList();
    }

    /**
     * Attempts to register method as an event handler. A prospective candidate
     * must accept only one parameter that inherits/implements {@link Event}
     * interface. If validation fails, then the {@link IllegalArgumentException}
     * is thrown.
     *
     * @param method anticipated event handler.
     */
    private void addHandler(final Method method) {
        final Class<?> parameters[] = method.getParameterTypes();
        if (parameters.length > 1) {
            throw new IllegalArgumentException("Ambiguous event handler");
        }
        addHandler(method, parameters[0]);
    }

    /**
     * Registers anticipated event handling method by associating it with
     * certain event type. It is important to keep in mind that the event type
     * is actually the first (and only one) parameter to the method.
     * It must directly or indirectly inherit from {@link Event}.
     *
     * @param method event handler method
     * @param type event type supported by this handler.
     */
    private void addHandler(final Method method, final Class<?> type) {
        if (!Event.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Ambiguous event handler");
        }
        List<Method> methods = handlers.get(type);
        if (methods == null) {
            methods = new ArrayList<Method>();
            handlers.put(type, methods);
        }
        methods.add(method);
    }

}
