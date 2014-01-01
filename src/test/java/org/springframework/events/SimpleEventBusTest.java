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

import org.springframework.events.mock.MockAEvent;
import org.springframework.events.mock.MockBEvent;
import org.springframework.events.mock.MockCEvent;
import org.springframework.events.mock.MockHandler;

public class SimpleEventBusTest {

    private MockAEvent mockAEvent;

    private MockBEvent mockBEvent;

    private MockCEvent mockCEvent;

    @Before
    public void setUp() {
        mockAEvent = new MockAEvent();
        mockBEvent = new MockBEvent();
        mockCEvent = new MockCEvent();
    }

    @After
    public void tearDown() {
        mockAEvent = null;
        mockBEvent = null;
        mockCEvent = null;
    }

    @Test(expected=IllegalArgumentException.class)
    public void canNotPublishNullEvent() {
        new SimpleEventBus().publish(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void canNotAddHandlerWithoutType() {
        new SimpleEventBus().addHandler(null, new MockHandler<MockAEvent>());
    }

    @Test(expected=IllegalArgumentException.class)
    public void canNotAddNullHandler() {
        new SimpleEventBus().addHandler(MockAEvent.class, null);
    }

    @Test
    public void shouldPublishToAppropriateHandlers() {
        final SimpleEventBus eventBus = new SimpleEventBus();

        eventBus.addHandler(MockAEvent.class, new MockHandler<MockAEvent>());
        eventBus.addHandler(MockBEvent.class, new MockHandler<MockBEvent>());

        eventBus.publish(mockAEvent);
        assertTrue("Event A not handled", mockAEvent.isHandled());

        eventBus.publish(mockBEvent);
        assertTrue("Event B not handled", mockBEvent.isHandled());

        eventBus.publish(mockCEvent);
        assertFalse("Event C handled", mockCEvent.isHandled());
    }

    @Test
    public void shouldNotPublishToRemovedHandler() {
        final SimpleEventBus eventBus = new SimpleEventBus();

        final HandlerRegistration registration =
            eventBus.addHandler(MockAEvent.class,
                    new MockHandler<MockAEvent>());

        assertNotNull("Null registration", registration);
        registration.removeHandler();
        eventBus.publish(mockAEvent);
        assertFalse("Event A handled", mockAEvent.isHandled());
    }
}
