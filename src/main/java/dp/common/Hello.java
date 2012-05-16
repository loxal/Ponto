/*
 * Copyright 2012 Alexander Orlov <alexander.orlov@loxal.net>
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

package dp.common;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// POJO, no interface no extends

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

@Path("/hello")
public class Hello {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello Jersey in PLAIN";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayJsonHello() {
//        return "{ Hello Jersey in JSON }";
        return "{\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Foo\",\n" +
                "        \"price\": 123,\n" +
                "        \"tags\": [\"Bar\",\"Eek\"],\n" +
                "        \"stock\": { \"warehouse\":300, \"retail\":20 }\n" +
                "}";
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    public String sayXMLHello() {
        return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello() {
        return "<html> " + "<title>" + "Hello Jersey in HTML!!!!!" + "</title>"
                + "<body><h1>" + "Hello Jersey in HTML yeah!!!" + "</body></h1>" + "</html> ";
    }

}