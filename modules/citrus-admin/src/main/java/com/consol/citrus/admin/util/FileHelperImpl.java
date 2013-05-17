/*
 * Copyright 2006-2013 the original author or authors.
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

package com.consol.citrus.admin.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * @author Martin Maher, Christoph Deppisch
 * @since 2013.04.22
 */
@Component
public class FileHelperImpl implements FileHelper {
    
    /**
     * {@inheritDoc}
     */
    public String[] getFolders(String directory) {
        if (new File(directory).exists()) {
            String[] files = new File(directory).list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.charAt(0) != '.' && new File(dir, name).isDirectory();
                }
            });
            Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);

            return files;
        } else {
            throw new IllegalArgumentException("Could not open directory because it does not exist: " + directory);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public File findFileInPath(File path, String filename, boolean recursive) {
        if (!path.isDirectory()) {
            String msg = String.format("Expected a directory but instead got '%s'", path.getAbsolutePath());
            throw new UnsupportedOperationException(msg);
        }

        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                File returnedFile = findFileInPath(file, filename, recursive);
                if (returnedFile != null) {
                    return returnedFile;
                }
            } else {
                if (file.getName().equals(filename)) {
                    return file;
                }
            }
        }
        return null;
    }
}
