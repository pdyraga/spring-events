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

package org.springframework.events.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Handles custom 'events' namespace in Spring XML configuration files.
 * Returns bean definition parsers for our custom tags.
 *
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see org.springframework.beans.factory.xml.NamespaceHandler
 * @see AnnotationConfigDefinitionParser
 */
public final class EventsNamespaceHandler extends NamespaceHandlerSupport {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        registerBeanDefinitionParser("annotation-config", new AnnotationConfigDefinitionParser());
    }
}
