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

package org.springframework.events.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be placed on methods that can handle events.
 * <p/>
 * For each event, only a single method will be invoked per object instance with
 * annotated methods. This method is resolved in the following order:
 * <ol>
 * <li>First, the event handler methods of the actual class (at runtime) are
 * searched
 * <li>If a method is found with a parameter that the event can be assigned to,
 * it is marked as eligible
 * <li>After a class has been evaluated (but before any super class), the most
 * specific event handler method is called. That means that if an event handler
 * for a class A and one for a class B are eligible, and B is a subclass of A,
 * then the method with a parameter of type B will be chosen
 * <li>If no method is found in the actual class, its super class is evaluated.
 * <li>If still no method is found, the event listener ignores the event
 * </ol>
 * Note: if there are two event handler methods accepting the same argument, the
 * behavior is undefined.
 *
 * @author Robert Bala
 * @since 0.1-RELEASE
 * @version %I%, %G%
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {

}
