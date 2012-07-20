/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
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
package org.sleuthkit.autopsy.keywordsearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;
import org.apache.solr.common.util.ContentStream;
import org.sleuthkit.autopsy.keywordsearch.ByteContentStream.Encoding;
import org.sleuthkit.datamodel.AbstractContent;
import org.sleuthkit.datamodel.AbstractFile;

/**
 * Converter from AbstractContent into String with specific encoding
 * Then, an adapter back to Solr' ContentStream (which is a specific InputStream), 
 * using the same encoding
 */
public class AbstractFileStringContentStream implements ContentStream {
    //input

    private AbstractFile content;
    private Encoding encoding;
    //converted
    private AbstractFileStringStream stream;
    private static Logger logger = Logger.getLogger(AbstractFileStringContentStream.class.getName());

    public AbstractFileStringContentStream(AbstractFile content, ByteContentStream.Encoding encoding) {
        this.content = content;
        this.encoding = encoding;
        this.stream = new AbstractFileStringStream(content, encoding);
    }

    public AbstractContent getSourceContent() {
        return content;
    }

    @Override
    public String getContentType() {
        return "text/plain;charset=" + encoding.toString();
    }

    @Override
    public String getName() {
        return content.getName();
    }

    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(stream);

    }

    @Override
    public Long getSize() {
        //return convertedLength;
        throw new UnsupportedOperationException("Cannot tell how many chars in converted string, until entire string is converted");
    }

    @Override
    public String getSourceInfo() {
        return "File:" + content.getId();
    }

    @Override
    public InputStream getStream() throws IOException {
        return stream;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        stream.close();
    }
}
