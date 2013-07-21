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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} implementation intended to parse
 * {@code <events:annotation-config>} tag. Registers bean definitions for
 * {@link org.springframework.events.BroadcastPublisher} and
 * {@link org.springframework.events.context.AnnotationEventHandlerPostProcessor}.
 *
 * @author Piotr Dyraga
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see EventsNamespaceHandler
 */
public final class AnnotationConfigDefinitionParser implements BeanDefinitionParser {

    private static final String BASE_PACKAGE = "org.springframework.events";

    /**
     * {@inheritDoc}
     */
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        final RootBeanDefinition broadcastPublisherDef =
                new RootBeanDefinition(BASE_PACKAGE + ".BroadcastPublisher");
        broadcastPublisherDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        final String broadcastPublisherName = BASE_PACKAGE + ".broadcastPublisher";
        parserContext.getRegistry().registerBeanDefinition(broadcastPublisherName, broadcastPublisherDef);

        final RootBeanDefinition  annotationEventHandlerPostProcessorDef =
                new RootBeanDefinition(BASE_PACKAGE + ".context.AnnotationEventHandlerPostProcessor");
        annotationEventHandlerPostProcessorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        final String annotationEventHandlerPostProcessorName =
                BASE_PACKAGE + ".context.annotationEventHandlerPostProcessor";
        parserContext.getRegistry().registerBeanDefinition(annotationEventHandlerPostProcessorName,
            annotationEventHandlerPostProcessorDef);

        return null;
    }
}

