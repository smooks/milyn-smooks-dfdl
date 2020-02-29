package org.milyn.dfdl.delivery;

import org.apache.daffodil.japi.DataProcessor;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.delivery.ContentHandler;
import org.milyn.delivery.annotation.Resource;

@Resource(type = "dfdl-parser")
public class DFDLParserContentHandlerFactory extends AbstractDFDLContentHandlerFactory {
    @Override
    public ContentHandler doCreate(final SmooksResourceConfiguration resourceConfig, final DataProcessor dataProcessor) throws SmooksConfigurationException {
        resourceConfig.setResource("org.milyn.dfdl.DFDLParser");
        return null;
    }
}