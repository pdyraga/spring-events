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

import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.springframework.events.Event.Handler;
import org.springframework.events.mock.MockAEvent;
import org.springframework.events.mock.MockBEvent;

public class AbstractEventTest {

    @Test
    public void shouldDispatchToApplicableHandler() {
        final MockDelegate mockDelegate = createMock(MockDelegate.class);

        final Handler<MockAEvent> handler = new Handler<MockAEvent>() {
            @Override
            public void handleEvent(final MockAEvent event) {
                mockDelegate.onEventDispatched(event);
            }
        };

        final Event event = new MockAEvent("");
        mockDelegate.onEventDispatched(same(event));
        expectLastCall();

        replay(mockDelegate);
        event.dispatch(handler);
        verify(mockDelegate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldNotDispatchToInapplicableHandler() {
        final Handler<MockBEvent> handler = new Handler<MockBEvent>() {
            @Override
            public void handleEvent(final MockBEvent event) {

            }
        };

        new MockAEvent("").dispatch(handler);
    }

    /**
     * Dummy interface required just to assert that event has been
     * dispatched to handler.
     */
    interface MockDelegate {
        void onEventDispatched(Event event);
    }
}
