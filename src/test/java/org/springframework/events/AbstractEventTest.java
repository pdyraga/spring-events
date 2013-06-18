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
