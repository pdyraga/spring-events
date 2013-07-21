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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.events.HandlerRegistration;
import org.springframework.events.HasBroadcastEventHandlers;
import org.springframework.events.mock.MockAEvent;
import org.springframework.events.mock.MockHandler;
import org.springframework.test.util.ReflectionTestUtils;

import static org.easymock.EasyMock.*;

public class AnnotationEventHandlerPostProcessorTest {

    private HasBroadcastEventHandlers mockEventBus;
    private AnnotationEventHandlerPostProcessor postProcessor;

    @Before
    public void setUp() {
        this.mockEventBus = createMock(HasBroadcastEventHandlers.class);
        this.postProcessor = new AnnotationEventHandlerPostProcessor();
        ReflectionTestUtils.setField(this.postProcessor, "publisher", this.mockEventBus);
    }

    @After
    public void tearDown() {
        this.postProcessor = null;
        this.mockEventBus = null;
    }

    @Test
    public void shouldNotPostProcessBeforeInitialization() {
        replay(mockEventBus);
        postProcessor.postProcessBeforeInitialization(
                new MockHandler<MockAEvent>(), "eventHandler");
        verify(mockEventBus);
    }

    @Test
    public void shouldRegisterHandlerAfterInitialization() {
        expect(mockEventBus.addHandler(isA(EventHandlerAdapter.class)))
                .andReturn(createMock(HandlerRegistration.class));

        replay(mockEventBus);
        postProcessor.postProcessAfterInitialization(
                new Object(), "dummyBean"); // should do nothing
        postProcessor.postProcessAfterInitialization(
                new MockHandler<MockAEvent>(), "eventHandler");
        verify(mockEventBus);
    }

    @Test
    public void shouldRemoveHandlerBeforeDestruction() {
        final MockHandler<MockAEvent> bean =
                new MockHandler<MockAEvent>();

        final HandlerRegistration mockHandlerRegistration =
                createMock(HandlerRegistration.class);

        // first registering - no way to unregister without registering before ;)
        expect(mockEventBus.addHandler(isA(EventHandlerAdapter.class)))
                .andReturn(mockHandlerRegistration);
        // and now unregistering assertion
        mockHandlerRegistration.removeHandler();
        expectLastCall();

        replay(mockEventBus);
        postProcessor.postProcessAfterInitialization(bean, "eventHandler");
        postProcessor.postProcessBeforeDestruction(bean, "eventHandler");
        verify(mockEventBus);
    }
}
