/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 jOpenDocument, by ILM Informatique. All rights reserved.
 * 
 * The contents of this file are subject to the terms of the GNU
 * General Public License Version 3 only ("GPL").  
 * You may not use this file except in compliance with the License. 
 * You can obtain a copy of the License at http://www.gnu.org/licenses/gpl-3.0.html
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each file.
 * 
 */

package org.jopendocument.model;

import org.jopendocument.model.office.OfficeBody;

public class OpenDocument {

    private OfficeBody body;

    // split

    /**
     * Creates an empty document You may use a loadFrom method on it
     */
    public OpenDocument() {

    }

    public OfficeBody getBody() {
        return this.body;
    }

    public void init(final OfficeBody aBody) {
        if (aBody == null) {
            throw new IllegalArgumentException("OfficeBody cannot be null");
        }

        this.body = aBody;
    }
}
