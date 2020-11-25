/*-
 * ========================LICENSE_START=================================
 * smooks-dfdl-cartridge
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
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
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.cartridges.dfdl;

import org.smooks.SmooksException;
import org.smooks.cdr.Parameter;
import org.smooks.cdr.ResourceConfig;
import org.smooks.cdr.extension.ExtensionContext;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.dom.DOMVisitBefore;
import org.smooks.xml.DomUtils;
import org.w3c.dom.Element;

import javax.inject.Inject;
import java.util.AbstractMap;

public class MapToResourceConfigFromKeyValueAttributes implements DOMVisitBefore {

    @Inject
    private String mapTo;

    @Inject
    private String keyAttribute;

    @Inject
    private String valueAttribute;

    public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
        final ResourceConfig resourceConfig = ExtensionContext.getExtensionContext(executionContext).getResourceStack().peek();
        final String key = DomUtils.getAttributeValue(element, keyAttribute);
        final String value = DomUtils.getAttributeValue(element, valueAttribute);

        resourceConfig.setParameter(new Parameter(mapTo, new AbstractMap.SimpleEntry(key, value)));
    }
}
