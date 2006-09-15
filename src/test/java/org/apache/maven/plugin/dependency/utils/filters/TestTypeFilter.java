/*
 *  Copyright 2005-2006 Brian Fox (brianefox@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.apache.maven.plugin.dependency.utils.filters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.dependency.utils.ArtifactStubFactory;
import org.apache.maven.plugin.dependency.utils.SilentLog;

/**
 * @author brianf
 *
 */
public class TestTypeFilter
    extends TestCase
{
    Set artifacts = new HashSet();
    Log log = new SilentLog();
    protected void setUp()
        throws Exception
    {
        super.setUp();
        
        ArtifactStubFactory factory = new ArtifactStubFactory(null,false);
        artifacts = factory.getTypedArtifacts();
    }
    
    public void testTypeParsing()
    {
        TypeFilter filter = new TypeFilter("war,jar","sources,zip,");
        List includes = filter.getIncludeTypes();
        List excludes = filter.getExcludeTypes();
     
        assertEquals(2,includes.size());
        assertEquals(2,excludes.size());
        assertEquals("war",includes.get(0).toString());
        assertEquals("jar",includes.get(1).toString());
        assertEquals("sources",excludes.get(0).toString());
        assertEquals("zip",excludes.get(1).toString());
    }
    
    public void testFiltering()
    {
        TypeFilter filter = new TypeFilter("war,jar","war,zip,");
        Set result = filter.filter(artifacts,log);
        assertEquals(2,result.size());
        
        Iterator iter = result.iterator();
        while (iter.hasNext())
        {
            Artifact artifact = (Artifact) iter.next();
            assertTrue(artifact.getType().equals("war") || artifact.getType().equals("jar"));
        }
    }
    
    public void testFiltering2()
    {
        TypeFilter filter = new TypeFilter(null,"war,jar,");
        Set result = filter.filter(artifacts,log);
        assertEquals(2,result.size());
        
        Iterator iter = result.iterator();
        while (iter.hasNext())
        {
            Artifact artifact = (Artifact) iter.next();
            assertTrue(artifact.getType().equals("sources") || artifact.getType().equals("zip"));
        }
    }
    
    public void testFiltering3()
    {
        TypeFilter filter = new TypeFilter(null,null);
        Set result = filter.filter(artifacts,log);
        assertEquals(4,result.size());
    }
}
