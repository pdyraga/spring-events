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

package org.springframework.events.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.events.annotation.EventHandler;
import org.springframework.events.mock.MockAEvent;
import org.springframework.events.mock.MockBEvent;
import org.springframework.events.mock.MockCEvent;
import org.springframework.stereotype.Component;

public class EventHandlerAdapterTest {

    @Test
    public void shouldDispatchEventHandlingToAnnotatedMethods() {
        final MockAEvent mockAEvent = new MockAEvent();
        final MockBEvent mockBEvent = new MockBEvent();
        final MockCEvent mockCEvent = new MockCEvent();

        final MockEventHandler adaptee = new MockEventHandler();
        final EventHandlerAdapter adapter = new EventHandlerAdapter(adaptee);

        adapter.handleEvent(mockAEvent);
        assertTrue("Event A was not handled", mockAEvent.isHandled());
        adapter.handleEvent(mockBEvent);
        assertTrue("Event B was not handled", mockBEvent.isHandled());
        adapter.handleEvent(mockCEvent);
        assertFalse("Event C was handled", mockCEvent.isHandled());
    }

    @Component
    public static class MockEventHandler {

        public void dummyMethod() {
            // Do nothing
        }

        @EventHandler
        public void handleMockEvent(final MockAEvent event) {
            event.setHandled(Boolean.TRUE);
        }


        @EventHandler
        public void handleMockEvent(final MockBEvent event) {
            event.setHandled(Boolean.TRUE);
        }

        // not annotated with @EventHandler
        public void handle(final MockCEvent event) {
            event.setHandled(Boolean.TRUE);
        }

    }
}
