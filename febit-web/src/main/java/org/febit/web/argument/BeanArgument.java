/**
 * Copyright 2013 febit.org (support@febit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.febit.web.argument;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import jodd.bean.BeanUtil;
import org.febit.util.ClassUtil;
import org.febit.web.ActionRequest;

/**
 *
 * @author zqq90
 */
public class BeanArgument implements Argument {

    @Override
    public Object resolve(ActionRequest actionRequest, Class type, String name, int index) {
        final Object bean = ClassUtil.newInstance(type);
        final HttpServletRequest request = actionRequest.request;
        if (name == null) {
            Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String param = (String) enumeration.nextElement();
                BeanUtil.declaredForcedSilent.setProperty(bean, param, request.getParameter(param));
            }
        } else {
            int dotIndex = name.length();
            int prefixLen = dotIndex + 1;
            Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String param = (String) enumeration.nextElement();
                if (param.length() > dotIndex
                        && param.charAt(dotIndex) == '.'
                        && param.startsWith(name)) {
                    BeanUtil.declaredForcedSilent.setProperty(bean, param.substring(prefixLen), request.getParameter(param));
                }
            }
        }
        return bean;
    }

    @Override
    public Class[] matchTypes() {
        return new Class[]{
            Object.class
        };
    }
}
