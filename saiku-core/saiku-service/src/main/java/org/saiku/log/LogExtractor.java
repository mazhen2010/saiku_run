/*
 *   Copyright 2012 OSBI Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.saiku.log;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Read and return log files.
 */
public class LogExtractor {

  private String logdirectory;
  private static final Logger log = LoggerFactory.getLogger(LogExtractor.class);

  public String readLog(String path) throws IOException {
      if(path.contains("..")){
        throw new IOException("Cannot display file outside of log folder");
      }
      return FileUtils.readFileToString(new File(logdirectory+File.separator+path));
  }

  public String getLogdirectory() {
    return logdirectory;
  }

  public void setLogdirectory(String logdirectory) {
    this.logdirectory = logdirectory;
  }
}
