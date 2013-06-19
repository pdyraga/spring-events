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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.events.Event.Handler;

/**
 * Thread-safe adapter for {@link HasBroadcastEventHandlers} interface. It uses
 * read-write locking mechanism making concurrent access more efficient than in case
 * of standard method-level synchronization.
 *
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 */
public final class ThreadSafePublisher implements HasBroadcastEventHandlers {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final HasBroadcastEventHandlers delegate;

    /**
     * Constructs {@link ThreadSafePublisher} with delegate
     * {@link HasBroadcastEventHandlers} instance taken as parameter.
     * Delegate must not be {@code null}.
     *
     * @param delegate
     *            not-{@code null} reference to the delegate
     *            {@link HasBroadcastEventHandlers}
     */
    @Autowired
    public ThreadSafePublisher(
            @Qualifier("broadcastPublisher") final HasBroadcastEventHandlers delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate must not be null");
        }
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(final Event event) {
        try {
            lock.readLock().lock();
            delegate.publish(event);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H extends Handler<? extends Event>> HandlerRegistration addHandler(
            final H handler) {
        try {
            lock.writeLock().lock();
            return delegate.addHandler(handler);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
