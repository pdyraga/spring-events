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

/**
 * Represents an object that is a kind of notification about some event occurrence.
 * Each event should provide information about source of state change so later it
 * gives opportunity to perform handler specific action on that object.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @param <T>
 *            subject of notification.
 */
public interface Event {

    /**
     * Accepts an event handler to dispatch this message to. It serves the
     * purpose of accept method in GoF visitor pattern.
     *
     * @param <E> Type of event to dispatch.
     * @param <H> Type of handler to dispatch to.
     * @param handler event handler implementation.
     */
    <E extends Event, H extends Event.Handler<E>> void dispatch(H handler);

    /**
     * Base class for implementation of events. Provides implementation of event
     * dispatching and possibility to get information
     * about an object that reported its state change {@link #getSource()}.
     *
     * @author Robert Bala
     * @since 0.1-RELEASE
     * @version %I%, %G%
     * @see Event
     *
     * @param <T>
     *            subject of notification.
     */
    abstract class AbstractEvent<T> implements Event {

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        @Override
        public final <E extends Event, H extends Event.Handler<E>>
                void dispatch(final H handler) {
            if (handler == null) {
                throw new IllegalArgumentException("Handler is required");
            }
            try {
                handler.handleEvent((E) this);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Inapplicable handler");
            }
        }

        /**
         * Gets event's associated source of state change.
         *
         * @return subject of notification.
         */
        public abstract T getSource();

    }

    /**
     * Marker interface for event handlers. All event handlers should extend
     * {@link Handler}. It represents an object that receives desired event
     * in order to handle it.
     *
     * @author Robert Bala
     * @author Piotr Dyraga
     * @since 0.1-RELEASE
     * @version %I%, %G%
     *
     * @param <E> type of supported {@link Event} by this object.
     */
    interface Handler<E extends Event> {

        /**
         * Handles received event.
         *
         * @param event an {@link Event} that is expected to be supported.
         */
        void handleEvent(E event);

    }

}
