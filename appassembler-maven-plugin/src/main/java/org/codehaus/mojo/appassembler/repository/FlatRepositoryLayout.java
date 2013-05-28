package org.codehaus.mojo.appassembler.repository;

/*
 * The MIT License
 *
 * Copyright 2005-2007 The Codehaus.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.codehaus.plexus.util.StringUtils;

/**
 * The code in this class is taken from DefaultRepositorylayout, located at:
 * http
 * ://svn.apache.org/viewvc/maven/components/trunk/maven-artifact/src/main/java
 * /org/apache/maven/artifact/repository/layout/DefaultRepositoryLayout.java
 * 
 * @version $Id: FlatRepositoryLayout.java 9147 2009-03-04 23:18:27Z trygvis $
 * @plexus.component 
 *                   role="org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout"
 *                   role-hint="flat"
 */
public class FlatRepositoryLayout implements ArtifactRepositoryLayout {
	private static final char ARTIFACT_SEPARATOR = '-';

	private static final char GROUP_SEPARATOR = '.';

	private final String finalNameTemplate;

	/**
	 * Variables supported are:
	 * 
	 * ${project.groupId} ${project.artifactId} ${project.version}
	 * 
	 * @param finalNameTemplate
	 */
	public FlatRepositoryLayout() {
		this.finalNameTemplate = "${project.groupId}.${project.artifactId}-${project.version}";
	}

	public String pathOf(Artifact artifact) {
		ArtifactHandler artifactHandler = artifact.getArtifactHandler();

		StringBuffer path = new StringBuffer();

		String finalName = finalNameTemplate;
		finalName = StringUtils.replace(finalName, "${project.groupId}", artifact.getGroupId());
		finalName = StringUtils.replace(finalName, "${project.artifactId}", artifact.getArtifactId());
		finalName = StringUtils.replace(finalName, "${project.version}", artifact.getVersion());
		path.append(finalName);

		if (artifact.hasClassifier()) {
			path.append(ARTIFACT_SEPARATOR).append(artifact.getClassifier());
		}

		if (artifactHandler.getExtension() != null && artifactHandler.getExtension().length() > 0) {
			path.append(GROUP_SEPARATOR).append(artifactHandler.getExtension());
		}

		return path.toString();
	}

	public String pathOfLocalRepositoryMetadata(ArtifactMetadata metadata, ArtifactRepository repository) {
		return pathOfRepositoryMetadata(metadata.getLocalFilename(repository));
	}

	private String pathOfRepositoryMetadata(String filename) {
		StringBuffer path = new StringBuffer();

		path.append(filename);

		return path.toString();
	}

	public String pathOfRemoteRepositoryMetadata(ArtifactMetadata metadata) {
		return pathOfRepositoryMetadata(metadata.getRemoteFilename());
	}
}
