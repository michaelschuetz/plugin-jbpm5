package iam.pschaerf.forge.plugin.jbpm5;

import java.io.FileNotFoundException;
import java.io.InputStream;

import iam.pschaerf.forge.plugin.jbpm5.facet.Jbpm5Facet;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.ResourceFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.resources.java.JavaResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.ShellColor;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.events.InstallFacets;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresProject;

@Alias("jbpm5")
@RequiresProject
public class Jbpm5Plugin implements Plugin {
	private final Project project;
	private final Event<InstallFacets> installFacets;
	private final Shell shell;

	@Inject
	public Jbpm5Plugin(Project project, Event<InstallFacets> event, Shell shell) {
		this.project = project;
		this.installFacets = event;
		this.shell = shell;
	}

	@DefaultCommand
	public void status() {
		if (project.hasFacet(Jbpm5Facet.class)) {
			ShellMessages.success(shell, "jBPM 5 is installed.");
		} else {
			ShellMessages.info(shell, "jBPM 5 is not installed. Use 'jbpm5 setup' to get started");
		}

	}

	@Command("setup")
	public void setup() {
		if (!project.hasFacet(Jbpm5Facet.class)) {
			installFacets.fire(new InstallFacets(Jbpm5Facet.class));
		}
		if (project.hasFacet(Jbpm5Facet.class)) {
			ShellMessages.success(shell, "jBPM 5 is configured");
		}
	}

	@Command("help")
	public void help() {
		shell.println("HELP");
	}

	@Command("scaffold")
	public void scaffold() {
		copyResource("", "process.bpmn");

		copySourcesToRootPackage("", "JbpmBean.java");
		copySourcesToRootPackage("", "TestTask1.java");

		copyWebfiles("", "jbpm.xhtml");
		ShellMessages.success(shell, "jBPM 5 scaffold installed.");
	}

	private void copyWebfiles(String webFilePath, String webFileName) {
		if(project.hasFacet(WebResourceFacet.class)) {
			DirectoryResource basePackage = project.getFacet(WebResourceFacet.class).getWebRootDirectory();

			InputStream inputStream = Jbpm5Plugin.class.getResourceAsStream("/webapp"+ webFilePath +"/"+ webFileName);

			FileResource<?> fileResource = (FileResource<?>) basePackage.getChild(webFileName);
			fileResource.setContents(inputStream);
		} else {
			ShellMessages.error(shell, "WebResourceFacet not found!");
		}
	}

	private void copySourcesToRootPackage(String sourceFilePath, String sourceFileName) {
		if(project.hasFacet(JavaSourceFacet.class)) {
			DirectoryResource basePackage = project.getFacet(JavaSourceFacet.class).getBasePackageResource();

			String projektPackageName = project.getFacet(JavaSourceFacet.class).getBasePackage();

			ShellMessages.info(shell, project.getFacet(JavaSourceFacet.class).getBasePackage()); // der komplette packagename de.tesprojkekt
			InputStream inputStream = Jbpm5Plugin.class.getResourceAsStream("/src"+ sourceFilePath +"/"+ sourceFileName);

			FileResource<?> fileResource = (FileResource<?>) basePackage.getChild(sourceFileName);
			fileResource.setContents(inputStream);

			if (fileResource instanceof JavaResource) {
				try {
					JavaSource<?> source = ((JavaResource) fileResource).getJavaSource();
					if(source.isClass()) {
						final JavaSourceFacet javaFacet = project.getFacet(JavaSourceFacet.class);
						JavaClass clazz = (JavaClass) source;

						ShellMessages.info(shell, source.getName() +" => set packageName to: "+ projektPackageName);
						clazz.setPackage(projektPackageName);
						javaFacet.saveJavaSource(clazz);
					}
				} catch (FileNotFoundException e) {
					e.getMessage();
				}
			}

		} else {
			ShellMessages.error(shell, "JavaSourceFacet not found!");
		}
	}

	private void copySources(String sourceFilePath, String sourceFileName) {
		if(project.hasFacet(JavaSourceFacet.class)) {
			DirectoryResource basePackage = project.getFacet(JavaSourceFacet.class).getSourceFolder();
			InputStream inputStream = Jbpm5Plugin.class.getResourceAsStream("/src"+ sourceFilePath +"/"+ sourceFileName);

			FileResource<?> fileResource = (FileResource<?>) basePackage.getChild(sourceFileName);
			fileResource.setContents(inputStream);

		} else {
			ShellMessages.error(shell, "JavaSourceFacet not found!");
		}
	}

	private void copyResource(String resourceFilePath, String resourcesFileName) {
		if(project.hasFacet(ResourceFacet.class)) {
			DirectoryResource resourceRoot = project.getFacet(ResourceFacet.class).getResourceFolder();

			InputStream inputStream = Jbpm5Plugin.class.getResourceAsStream("/resources"+ resourceFilePath +"/"+ resourcesFileName);

			FileResource<?> fileResource = (FileResource<?>) resourceRoot.getChild(resourcesFileName);
			fileResource.setContents(inputStream);

			//TODO only for presentation - adds the packagename to the process.bpmn
			String fileName = fileResource.getName();
			if(fileName.equals("process.bpmn")) {
				String pathName = resourceRoot.getName();
				ShellMessages.info(shell, "FileName "+ pathName +" - "+ fileName);
				String projektPackageName = project.getFacet(JavaSourceFacet.class).getBasePackage();
				XMLEditor.edit(project.getProjectRoot() +"/src/main/"+ pathName +"/"+ fileName, projektPackageName +".*");
			}
 		} else {
			ShellMessages.error(shell, "ResourceFacet not found!");
		}
	}
}
