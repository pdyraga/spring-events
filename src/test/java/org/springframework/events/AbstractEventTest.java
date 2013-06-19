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

import org.springframework.events.Event.AbstractEvent;
import org.springframework.events.Event.Handler;

public class AbstractEventTest {

    @Test
    public void shouldDispatchToApplicableHandler() {
        final MockDelegate mockDelegate = createMock(MockDelegate.class);

        final Handler<TestAEvent> handler = new Handler<TestAEvent>() {
            @Override
            public void handleEvent(final TestAEvent event) {
                mockDelegate.onEventDispatched(event);
            }
        };

        final Event event = new TestAEvent();
        mockDelegate.onEventDispatched(same(event));
        expectLastCall();

        replay(mockDelegate);
        event.dispatch(handler);
        verify(mockDelegate);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldNotDispatchToInapplicableHandler() {
        final Handler<TestBEvent> handler = new Handler<TestBEvent>() {
            @Override
            public void handleEvent(final TestBEvent event) {

            }
        };

        new TestAEvent().dispatch(handler);
    }

    public static class TestAEvent extends AbstractEvent<String> {
        @Override
        public String getSource() {
            return "";
        }
    }

    public static class TestBEvent extends AbstractEvent<String> {
        @Override
        public String getSource() {
            return "";
        }
    }

    /**
     * Dummy interface required just to assert that event has been
     * dispatched to handler.
     */
    interface MockDelegate {
        void onEventDispatched(Event event);
    }
}
