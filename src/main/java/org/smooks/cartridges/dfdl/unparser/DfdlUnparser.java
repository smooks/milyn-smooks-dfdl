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
package org.smooks.cartridges.dfdl.unparser;

import org.apache.daffodil.japi.DataProcessor;
import org.apache.daffodil.japi.Diagnostic;
import org.apache.daffodil.japi.UnparseResult;
import org.apache.daffodil.japi.infoset.W3CDOMInfosetInputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smooks.SmooksException;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.sax.annotation.StreamResultWriter;
import org.smooks.delivery.sax.ng.ParameterizedVisitor;
import org.smooks.io.NullWriter;
import org.w3c.dom.Element;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;


@StreamResultWriter
public class DfdlUnparser implements ParameterizedVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DfdlUnparser.class);
    private final DataProcessor dataProcessor;

    @Inject
    private String schemaURI;

    public DfdlUnparser(final DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    @Override
    public void visitAfter(final Element element, final ExecutionContext executionContext) throws SmooksException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final UnparseResult unparseResult = dataProcessor.unparse(new W3CDOMInfosetInputter(element.getOwnerDocument()), Channels.newChannel(byteArrayOutputStream));
        for (Diagnostic diagnostic : unparseResult.getDiagnostics()) {
            if (diagnostic.isError()) {
                throw new SmooksException(diagnostic.getSomeMessage(), diagnostic.getSomeCause());
            } else {
                LOGGER.warn(diagnostic.getMessage());
            }
        }
        try { 
            ((NullWriter) executionContext.getWriter()).getParentWriter().write(byteArrayOutputStream.toString());
        } catch (IOException e) {
            throw new SmooksException(e.getMessage(), e);
        }
    }

    @Override
    public int getMaxNodeDepth() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void visitBefore(Element element, ExecutionContext executionContext) {
        executionContext.setWriter(new NullWriter(executionContext.getWriter()));
    }
}
