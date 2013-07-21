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

import java.util.LinkedList;
import java.util.List;

import org.springframework.events.Event.Handler;

/**
 * Basic implementation of {@link HasBroadcastEventHandlers} that publishes
 * {@link Event} to all registered handlers. This publisher works in broadcast
 * mode, that is, all registered parties are meant to receive event. It's up to
 * them to decide, if they are interested in particular event or not.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see HasBroadcastEventHandlers
 */
public final class BroadcastPublisher implements HasBroadcastEventHandlers {

    private final List<Handler< ? >> handlers =
        new LinkedList<Handler< ? >>();

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
    public <H extends Handler< ? extends Event >>
            HandlerRegistration addHandler(final H handler) {
        doAdd(handler);

        return new HandlerRegistration() {
            @Override
            public void removeHandler() {
                doRemove(handler);
            }
       };
    }

    private <E extends Event, H extends Event.Handler<E>> void doPublish(
            final E event) {
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null");
        }
        final List<H> observers = getObservers();
        for (final H handler : observers) {
            event.dispatch(handler);
        }
    }

    @SuppressWarnings("unchecked")
    private <H extends Handler< ? >> List<H> getObservers() {
        return (List<H>) handlers;
    }

    private <H extends Event.Handler< ? >>
            void doAdd(final H handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler is required");
        }
        handlers.add(handler);
    }

    private <H extends Event.Handler< ? >>
            void doRemove(final H handler) {
        handlers.remove(handler);
    }

}
