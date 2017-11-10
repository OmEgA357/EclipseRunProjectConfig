package runner;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.springsource.ide.eclipse.commons.frameworks.core.maintype.MainTypeFinder;

public class RunningClass {
	public static final String TYPE_ID = "org.springframework.ide.eclipse.boot.launch";

	public static final String BOOT_LAUNCH_MARKER = "isBootLaunch";

	public static final String ENABLE_LIVE_BEAN_SUPPORT = "spring.boot.livebean.enable";
	public static final boolean DEFAULT_ENABLE_LIVE_BEAN_SUPPORT = true;

	public static final String ENABLE_JMX = "spring.boot.jmx.enable";
	public static final boolean DEFAULT_ENABLE_JMX = true;

	public static final String JMX_PORT = "spring.boot.livebean.port";

	public static final int DEFAULT_JMX_PORT = 0; // means pick it dynamically

	public static final String ANSI_CONSOLE_OUTPUT = "spring.boot.ansi.console";

	public static final String DEFAULT_PROFILE = "";

	public static final String ENABLE_LIFE_CYCLE = "spring.boot.lifecycle.enable";
	public static final boolean DEFAULT_ENABLE_LIFE_CYCLE = true;

	public static final String HIDE_FROM_BOOT_DASH = "spring.boot.dash.hidden";
	public static final boolean DEFAULT_HIDE_FROM_BOOT_DASH = false;

	private static final String TERMINATION_TIMEOUT = "spring.boot.lifecycle.termination.timeout";
	public static final long DEFAULT_TERMINATION_TIMEOUT = 15000; // 15 seconds

	public void runProject() throws CoreException {

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] iProjects = workspaceRoot.getProjects();

		IProject selectedProject = null;
		IJavaProject selectedJavaProject = null;
		IJavaElement javaElement = null;

		for (IProject iProject : iProjects) {
			if (iProject.getName().equalsIgnoreCase("EmployeeDemo-microservice-module")) {
				selectedProject = iProject;
				break;
			}
		}

		if (selectedProject != null) {
			if (selectedProject instanceof IAdaptable) {
				IAdaptable a = (IAdaptable) selectedProject;

				javaElement = (IJavaElement) a.getAdapter(IJavaElement.class);
				selectedJavaProject = javaElement.getJavaProject();
			}
		}

		IType mainType = MainTypeFinder.guessMainTypes(selectedJavaProject, new NullProgressMonitor())[0];

		ILaunchConfigurationType configType = manager
				.getLaunchConfigurationType("org.springframework.ide.eclipse.boot.launch");
		ILaunchConfigurationWorkingCopy workingCopy = configType.newInstance(null,
				manager.generateLaunchConfigurationName(selectedProject + "-runconfig"));

		workingCopy.setAttribute(ATTR_PROJECT_NAME, selectedProject.getName());
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
				mainType.getFullyQualifiedName());
		workingCopy.setAttribute(ENABLE_JMX, DEFAULT_ENABLE_JMX);
		workingCopy.setAttribute(ENABLE_LIVE_BEAN_SUPPORT, DEFAULT_ENABLE_LIVE_BEAN_SUPPORT);
		workingCopy.setAttribute(ENABLE_LIFE_CYCLE, DEFAULT_ENABLE_LIFE_CYCLE);
		workingCopy.setAttribute(TERMINATION_TIMEOUT, "" + DEFAULT_TERMINATION_TIMEOUT);

		ILaunchConfiguration configuration = workingCopy.doSave();
		DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
	}
}
