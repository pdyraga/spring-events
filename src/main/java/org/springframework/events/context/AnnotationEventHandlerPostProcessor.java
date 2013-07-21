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
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.events.HasBroadcastEventHandlers;
import org.springframework.events.annotation.EventHandler;
import org.springframework.util.ReflectionUtils;

import org.springframework.events.Event;
import org.springframework.events.HandlerRegistration;
import org.springframework.events.Event.Handler;

/**
 * Spring post processor responsible for automatic detection of event handlers
 * based on methods. Any bean object that is managed by Spring that
 * has methods annotated with {@link org.springframework.events.annotation.EventHandler}
 * and accepting event type as parameter becomes {@link Event.Handler}.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 */
public final class AnnotationEventHandlerPostProcessor implements
        DestructionAwareBeanPostProcessor {

    @Autowired
    private HasBroadcastEventHandlers publisher;

    private final List<HandlerRegistration> adapters =
        new ArrayList<HandlerRegistration>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessBeforeInitialization(final Object bean,
            final String beanName) throws BeansException {
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessAfterInitialization(final Object bean,
            final String beanName) throws BeansException {
        if (new HandlerInspectorHelper(bean).isHandler()) {
            return registerHandler(bean);
        }

        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeforeDestruction(final Object bean,
            final String beanName) throws BeansException {
        for (final HandlerRegistration adapter : adapters) {
            adapter.removeHandler();
        }
    }

    /**
     * Creates an event handler adapter for Spring bean object.
     * Adapter object is automatically registered in IoC injected
     * {@link org.springframework.events.HasBroadcastEventHandlers} observable object.
     *
     * @param bean reference to the bean that was identified as event handler
     * @return Spring's AOP proxy object.
     */
    private Object registerHandler(final Object bean) {
        final Handler<Event> adapter = new EventHandlerAdapter(bean);
        adapters.add(publisher.addHandler(adapter));
        return AdapterIntroductionInterceptor.createAdapterProxy(bean,
                adapter, Event.Handler.class);
    }

    /**
     * Helper class that scans for methods annotated with
     * {@link org.springframework.events.annotation.EventHandler}.
     * If at least one such method is found then call to {@link #isHandler()}
     * method returns {@code true} indicating that class can be adapted
     * to event handler.
     *
     * @author Robert Bala
     * @author Piotr Dyraga
     * @since 0.1-RELEASE
     * @version %I%, %G%
     */
    private static final class HandlerInspectorHelper {

        private boolean handler;

        public HandlerInspectorHelper(final Object target) {
            ReflectionUtils.doWithMethods(target.getClass(),
                    new ReflectionUtils.MethodCallback() {

                        @Override
                        public void doWith(final Method method)
                                throws IllegalArgumentException,
                                IllegalAccessException {
                            HandlerInspectorHelper.this.inspect(method);
                        }

                    });
        }

        public boolean isHandler() {
            return handler;
        }

        private void inspect(final Method method) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                this.handler = true;
            }
        }
    }
}
