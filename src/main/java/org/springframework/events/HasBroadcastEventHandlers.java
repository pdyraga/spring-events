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
 * Dispatches {@link Event} objects to interested parties. Eases decoupling by
 * allowing objects to interact without having direct dependencies upon one
 * another, and without requiring event sources to maintain handlers
 * list. Objects of this type differ from those implementing {@link HasEventHandlers}
 * in that the registered handlers are meant to receive all published events.
 * It's up to each event handler to decide if certain event is applicable to it.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see Event.Handler
 * @see HandlerRegistration
 * @see HasEventHandlers
 */
public interface HasBroadcastEventHandlers extends EventBus {

    /**
     * Adds handler to receive events.
     * <p>
     * It is rare to call this method directly. In most cases it will be
     * the responsibility of IoC container.
     * <p>
     *
     * @param <H> The type of handler
     * @param handler the handler
     *
     * @return the {@link HandlerRegistration} that can be stored in order to remove the
     *         handler later
     */
    <H extends Event.Handler<? extends Event>>
            HandlerRegistration addHandler(final H handler);

}
