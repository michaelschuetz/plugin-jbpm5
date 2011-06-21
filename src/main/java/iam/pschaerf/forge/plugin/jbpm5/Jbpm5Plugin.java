package iam.pschaerf.forge.plugin.jbpm5;

import iam.pschaerf.forge.plugin.jbpm5.facet.Jbpm5Facet;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Project;
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

	@Inject
	public Jbpm5Plugin(Project project, Event<InstallFacets> event) {
		this.project = project;
		this.installFacets = event;
	}

	@DefaultCommand
	public void status(PipeOut out) {
		if (project.hasFacet(Jbpm5Facet.class)) {
			out.println("jBPM 5 is installed.");
		} else {
			out.println("jBPM 5 is not installed. Use 'jbpm5 setup' to get started");
		}
		
	}

	@Command("setup")
	public void setup(final PipeOut out) {	
		if (!project.hasFacet(Jbpm5Facet.class)) {
			installFacets.fire(new InstallFacets(Jbpm5Facet.class));
		}
		if (project.hasFacet(Jbpm5Facet.class)) {
			ShellMessages.success(out, "jBPM 5 is configured");
		}	
	}
	
	@Command("help")
	public void help(final PipeOut out) {
		out.print(ShellColor.GREEN, "HELP");
	}
}
