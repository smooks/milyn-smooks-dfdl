/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
*/
package org.milyn.cartridges.dfdl.cdr.extension;

import org.milyn.SmooksException;
import org.milyn.cdr.Parameter;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.cdr.annotation.ConfigParam;
import org.milyn.cdr.extension.ExtensionContext;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.dom.DOMVisitBefore;
import org.milyn.xml.DomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.AbstractMap;

public class MapToResourceConfigFromKeyValueAttributes implements DOMVisitBefore {

    @ConfigParam
    private String mapTo;

    @ConfigParam
    private String keyAttribute;

    @ConfigParam
    private String valueAttribute;

    public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
        final SmooksResourceConfiguration config = ExtensionContext.getExtensionContext(executionContext).getResourceStack().peek();
        final String key = DomUtils.getAttributeValue(element, keyAttribute);
        final String value = DomUtils.getAttributeValue(element, valueAttribute);

        config.setParameter(new Parameter(mapTo, new AbstractMap.SimpleEntry(key, value)));
    }
}