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
 * another and without requiring event sources to  maintain handlers list.
 * There will be typically one {@link EventBus} per application, broadcasting
 * events that may be of general interest.
 *
 * @author Robert Bala
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see Event
 * @see HasBroadcastEventHandlers
 */
public interface EventBus {

    /**
     * Publish the {@link Event}. All registered handlers will receive it.
     *
     * @param event
     *            the {@link Event} to be published; must not be {@code null}
     */
    void publish(Event event);

}
