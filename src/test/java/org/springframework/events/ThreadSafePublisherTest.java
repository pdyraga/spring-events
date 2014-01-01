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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.events.Event.Handler;
import org.springframework.events.mock.MockAEvent;
import org.springframework.events.mock.MockHandler;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ThreadSafePublisherTest {

    private static final int NUMBER_OF_THREADS = 3000;

    private MockAEvent mockAEvent;

    private HasBroadcastEventHandlers mockDelegate;

    private ThreadSafePublisher publisher;

    @Before
    public void setUp() {
        mockAEvent = new MockAEvent();

        this.mockDelegate = createMock(HasBroadcastEventHandlers.class);
        this.publisher = new ThreadSafePublisher(mockDelegate);
    }

    @After
    public void tearDown() {
        this.mockAEvent = null;

        this.publisher = null;
        this.mockDelegate = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void canNotConstructWithNullDelegate() {
        new ThreadSafePublisher(null);
    }

    @Test
    public void shouldDelegateEventPublishing() {
        final Event mockEvent = createMock(Event.class);

        mockDelegate.publish(same(mockEvent));
        expectLastCall();

        replay(mockEvent, mockDelegate);
        publisher.publish(mockEvent);
        verify(mockEvent, mockDelegate);
    }

    @Test
    public void shouldDelegateAddingHandler() {
        final Handler< ? > mockHandler = createMock(Handler.class);
        final HandlerRegistration mockHandlerRegistration =
            createMock(HandlerRegistration.class);

        expect(mockDelegate.addHandler(same(mockHandler)))
            .andReturn(mockHandlerRegistration);

        replay(mockHandler, mockHandlerRegistration, mockDelegate);
        final HandlerRegistration result = publisher.addHandler(mockHandler);
        verify(mockHandler, mockHandlerRegistration, mockDelegate);

        assertNotNull("Null handler registration returned", result);
        assertSame("Unexpected handler registration returned",
                mockHandlerRegistration, result);
    }

    /**
     * we must use concrete implementations here - it's more integration
     * than unit test
     */
    @Test
    public void testPublishWithConcurrentModifications() throws Throwable {
        final ThreadSafePublisher _publisher = new ThreadSafePublisher(
                new BroadcastPublisher());

        final Queue<Throwable> errors = new ConcurrentLinkedQueue<Throwable>();

        final Thread[] threads = new Thread[NUMBER_OF_THREADS];
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    _publisher.addHandler(new MockHandler<MockAEvent>());
                    try {
                        Thread.sleep(Math.round(2 + Math.random() * 3));
                    } catch (InterruptedException e) {
                        errors.add(e);
                    }
                    _publisher.publish(mockAEvent);
                }
            });
            threads[i].setUncaughtExceptionHandler(
                    new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(final Thread t,
                        final Throwable e) {
                    e.printStackTrace();
                    errors.add(e);
                }
            });
        }

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i].start();
        }

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i].join();
        }

        if (!errors.isEmpty()) {
            throw errors.peek();
        }
    }
}
