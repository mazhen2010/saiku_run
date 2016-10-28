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
package org.saiku.olap.util;

import org.saiku.olap.dto.ISaikuObject;

import java.util.Comparator;

abstract class SaikuObjectComparator<T> implements Comparator<ISaikuObject> {

  private class SaikuNameComparator extends SaikuObjectComparator<ISaikuObject> {

    public int compare( ISaikuObject o1, ISaikuObject o2 ) {
      return o1.getName().compareTo( o2.getName() );
    }
  }

  private class SaikuUniqueNameComparator extends SaikuObjectComparator<ISaikuObject> {

    public int compare( ISaikuObject o1, ISaikuObject o2 ) {
      return o1.getUniqueName().compareTo( o2.getUniqueName() );
    }

  }

}
