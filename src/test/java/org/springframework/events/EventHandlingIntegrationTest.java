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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.events.annotation.EventHandler;
import org.springframework.events.mock.MockAEvent;
import org.springframework.events.mock.MockBEvent;
import org.springframework.events.mock.MockCEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:/META-INF/spring/testApplicationContext-events.xml")
public class EventHandlingIntegrationTest {

    @Autowired
    private HasBroadcastEventHandlers publisher;

    @Test
    public void shouldPublishViaBroadcastPublisher() {
        final MockAEvent mockAEvent = new MockAEvent();
        final MockBEvent mockBEvent = new MockBEvent();
        final MockCEvent mockCEvent = new MockCEvent();

        publisher.publish(mockAEvent);
        assertTrue("Event A was not handled", mockAEvent.isHandled());
        publisher.publish(mockBEvent);
        assertTrue("Event B was not handled", mockBEvent.isHandled());
        publisher.publish(mockCEvent);
        assertFalse("Event C was handled", mockCEvent.isHandled());
    }

    public static class MockEventHandler {

        public void dummyMethod() {
            // Do nothing
        }

        @EventHandler
        public void handleMockEvent(final MockAEvent event) {
            event.setHandled(true);
        }


        @EventHandler
        public void handleMockEvent(final MockBEvent event) {
            event.setHandled(true);
        }

    }

}
