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
 * Represents an object that has a collection of event handlers associated with
 * it and propagating events to them.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see Event.Handler
 * @see HandlerRegistration
 * @see HasBroadcastEventHandlers
 */
public interface HasEventHandlers extends EventBus {

    /**
     * Adds handler to receive events of certain type from all sources.
     * <p>
     * It is rare to call this method directly. In most cases it will be
     * the responsibility of IoC container.
     * <p>
     *
     * @param <H> The type of handler
     * @param type the event type associated with this handler
     * @param handler the handler
     *
     * @return the {@link HandlerRegistration} that can be stored in order to remove the
     *         handler later
     */
    <E extends Event, H extends Event.Handler<E>>
        HandlerRegistration addHandler(final Class<E> type, final H handler);

}
