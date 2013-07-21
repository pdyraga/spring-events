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

package org.springframework.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic event bus implementation. Usually, there will be only one
 * instance of this class per application.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see EventBus
 * @see HasEventHandlers
 */
public final class SimpleEventBus implements HasEventHandlers {

    private final Map<Class<? extends Event>, List<?>> dispatchers =
        new HashMap<Class<? extends Event>, List<?>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(final Event event) {
        doPublish(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Event, H extends Event.Handler<E>>
            HandlerRegistration addHandler(final Class<E> type, final H handler) {
        doAdd(type, handler);

        return new HandlerRegistration() {
            @Override
            public void removeHandler() {
                doRemove(type, handler);
            }
        };
    }

    private <E extends Event, H extends Event.Handler<E>>
            void doAdd(final Class<E> type, final H handler) {
        if (type == null) {
            throw new IllegalArgumentException("Event type is required");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Event handler is required");
        }
        List<H> handlers = getHandlers(type);
        if (handlers.isEmpty()) {
            handlers = new ArrayList<H>();
            dispatchers.put(type, handlers);
        }
        handlers.add(handler);
    }

    private <E extends Event, H extends Event.Handler<E>>
            void doRemove(final Class<E> type, final H handler) {
        final List<H> handlers = getHandlers(type);
        final boolean removed = handlers.remove(handler);
        if (removed && handlers.isEmpty()) {
            dispatchers.remove(type);
        }
    }

    private <H extends Event.Handler<Event>> void doPublish(
            final Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null");
        }
        final List<H> handlers = getHandlers(event.getClass());
        for (final H handler : handlers) {
            event.dispatch(handler);
        }
    }

    private <H extends Event.Handler<? extends Event>> List<H>
            getHandlers(final Class<? extends Event> type) {
        @SuppressWarnings("unchecked")
        final List<H> handlers = (List<H>) dispatchers.get(type);
        if (handlers == null) {
            return Collections.emptyList();
        }
        return handlers;
    }

}
