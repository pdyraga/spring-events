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

import java.lang.reflect.InvocationTargetException;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInfo;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Helper class that allows dynamic adapter classes to be invoked as Spring AOP
 * advice. This is a package protected class used by the Spring
 * {@link AnnotationEventHandlerPostProcessor}. It is applicable to any adapter
 * object that is of the specified interface.
 *
 * @author Robert Bala
 * @since 0.1-RELEASE
 * @version %I%, %G%
 *
 * @see IntroductionInfo
 * @see IntroductionInterceptor
 */
final class AdapterIntroductionInterceptor implements IntroductionInfo,
        IntroductionInterceptor {

    private final Object adapter;

    private final Class<?> adapterInterface;

    /**
     * Default class constructor specifying an adapter object and interface.
     *
     * @param adapter reference to adapter
     * @param adapterInterface an adapter interface
     */
    AdapterIntroductionInterceptor(final Object adapter,
            final Class<?> adapterInterface) {
        this.adapter = adapter;
        this.adapterInterface = adapterInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (invocation.getMethod().getDeclaringClass().
                equals(adapterInterface)) {
            try {
                return invocation.getMethod().invoke(adapter,
                        invocation.getArguments());
            } catch (InvocationTargetException e) {
                // We want to know what was the real cause
                throw e.getCause();
            }
        }

        return invocation.proceed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean implementsInterface(final Class<?> intf) {
        return adapterInterface.equals(intf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?>[] getInterfaces() {
        return new Class[] { adapterInterface };
    }

    public static Object createAdapterProxy(final Object annotatedHandler,
            final Object adapter, final Class<?> adapterInterface) {
        ProxyFactory pf = new ProxyFactory(annotatedHandler);
        pf.addAdvice(new AdapterIntroductionInterceptor(adapter,
                adapterInterface));
        pf.addInterface(adapterInterface);
        pf.setProxyTargetClass(true);
        pf.setExposeProxy(true);

        return pf.getProxy(annotatedHandler.getClass().getClassLoader());
    }
}
