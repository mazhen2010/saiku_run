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
package org.saiku.web.rest.resources;

import org.saiku.service.PlatformUtilsService;
import org.saiku.service.util.dto.Plugin;

import com.qmino.miredot.annotations.ReturnType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Info Resource to get platform information.
 */
@Component
@Path("/saiku/info")
@XmlAccessorType( XmlAccessType.NONE)
public class InfoResource {

  private static final Logger log = LoggerFactory.getLogger( InfoResource.class );

  private PlatformUtilsService platformService;

  //@Autowired
  public void setPlatformUtilsService(PlatformUtilsService ps) {
    this.platformService = ps;
  }

  /**
   * Get a list of available plugins.
   * @summary Get plugins
   * @return A response containing a list of plugins.
   */
  @GET
  @Produces({"application/json" })
  @ReturnType("java.util.List<Plugin>")
  public Response getAvailablePlugins() {

    GenericEntity<List<Plugin>> entity =
         new GenericEntity<List<Plugin>>(platformService.getAvailablePlugins()){};
     return Response.ok(entity).build();
  }

}
