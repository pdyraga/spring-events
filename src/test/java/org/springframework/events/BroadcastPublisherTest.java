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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.events.Event.AbstractEvent;

public class BroadcastPublisherTest {

    private MockAEvent mockAEvent;

    private MockBEvent mockBEvent;

    private MockCEvent mockCEvent;

    @Before
    public void setUp() {
        mockAEvent = new MockAEvent("A");
        mockBEvent = new MockBEvent("B");
        mockCEvent = new MockCEvent("C");
    }

    @After
    public void tearDown() {
        mockAEvent = null;
        mockBEvent = null;
        mockCEvent = null;
    }

    @Test(expected=IllegalArgumentException.class)
    public void canNotPublishNullEvent() {
        new BroadcastPublisher().publish(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void canNotAddNullHandler() {
        new BroadcastPublisher().addHandler(null);
    }

    @Test
    public void shouldPublishToAllRegisteredHandlers() {
        final BroadcastPublisher publisher = new BroadcastPublisher();

        publisher.addHandler(new MockHandler<MockAEvent>());
        publisher.addHandler(new MockHandler<MockBEvent>());

        publisher.publish(mockAEvent);
        assertTrue("Event A not handled", mockAEvent.isHandled());

        publisher.publish(mockBEvent);
        assertTrue("Event B not handled", mockBEvent.isHandled());

        publisher.publish(mockCEvent);
        assertTrue("Event C not handled", mockCEvent.isHandled());
    }

    @Test
    public void testNotPublishToRemovedHandler() {
        final BroadcastPublisher eventBus = new BroadcastPublisher();

        final HandlerRegistration registration =
            eventBus.addHandler(new MockHandler<MockAEvent>());

        assertNotNull("Null registration", registration);
        registration.removeHandler();
        eventBus.publish(mockAEvent);
        assertFalse("Event A handled", mockAEvent.isHandled());
    }

    public static class MockAEvent extends AbstractMockEvent {

        public MockAEvent(String string) {
            super(string);
        }

    }

    public static class MockBEvent extends AbstractMockEvent {

        public MockBEvent(String string) {
            super(string);
        }

    }

    public static class MockCEvent extends AbstractMockEvent {

        public MockCEvent(String string) {
            super(string);
        }

    }

    public static abstract class AbstractMockEvent extends
            AbstractEvent<String> {

        private final String source;

        private boolean handled;

        public AbstractMockEvent(final String source) {
            this.source = source;
        }

        @Override
        public String getSource() {
            return source;
        }

        public boolean isHandled() {
            return handled;
        }

        public void setHandled(final boolean handled) {
            this.handled = handled;
        }

    }

    public static class MockHandler<E extends AbstractMockEvent> implements
            Event.Handler<E> {

        @Override
        public void handleEvent(final E event) {
            event.setHandled(true);
        }

    }

}
