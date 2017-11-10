package runner;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_CLASSPATH;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_NAME;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;

public class RunningClass {
	public static final String TYPE_ID = "org.springframework.ide.eclipse.boot.launch";

	public static final String BOOT_LAUNCH_MARKER = "isBootLaunch";

	public static final String ENABLE_LIVE_BEAN_SUPPORT = "spring.boot.livebean.enable";
	public static final boolean DEFAULT_ENABLE_LIVE_BEAN_SUPPORT = true;

	public static final String ENABLE_JMX = "spring.boot.jmx.enable";
	public static final boolean DEFAULT_ENABLE_JMX = true;

	public static final String JMX_PORT = "spring.boot.livebean.port";

	public static final int DEFAULT_JMX_PORT = 0; //means pick it dynamically

	public static final String ANSI_CONSOLE_OUTPUT = "spring.boot.ansi.console";

	private static final String PROFILE = "spring.boot.profile";
	public static final String DEFAULT_PROFILE = "";

	public static final String ENABLE_LIFE_CYCLE = "spring.boot.lifecycle.enable";
	public static final boolean DEFAULT_ENABLE_LIFE_CYCLE = true;

	public static final String HIDE_FROM_BOOT_DASH = "spring.boot.dash.hidden";
	public static final boolean DEFAULT_HIDE_FROM_BOOT_DASH = false;

	private static final String ENABLE_CHEAP_ENTROPY_VM_ARGS = "-Djava.security.egd=file:/dev/./urandom ";
	private static final String TERMINATION_TIMEOUT = "spring.boot.lifecycle.termination.timeout";
	public static final long DEFAULT_TERMINATION_TIMEOUT = 15000; // 15 seconds
	
	@SuppressWarnings("deprecation")
	public void runProject() throws CoreException {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(ID_JAVA_APPLICATION);
		ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(type);
		for (int i = 0; i < configurations.length; i++) {
			ILaunchConfiguration configuration = configurations[i];
			if (configuration.getName().equals("Start Tomcat")) {
				configuration.delete();
				break;
			}
		}
		ILaunchConfigurationType configType = manager.getLaunchConfigurationType("org.springframework.ide.eclipse.boot.launch");
		ILaunchConfigurationWorkingCopy workingCopy = configType.newInstance(null, manager.generateLaunchConfigurationName(
				"Sample-microservice-module-Temp"));
		workingCopy.setAttribute(ATTR_PROJECT_NAME, "Sample-microservice-module");
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "com.lti.studio.StudioApplication");
		workingCopy.setAttribute(ENABLE_JMX, DEFAULT_ENABLE_JMX);
		workingCopy.setAttribute(ENABLE_LIVE_BEAN_SUPPORT, DEFAULT_ENABLE_LIVE_BEAN_SUPPORT);
		workingCopy.setAttribute(ENABLE_LIFE_CYCLE, DEFAULT_ENABLE_LIFE_CYCLE);
		workingCopy.setAttribute(TERMINATION_TIMEOUT, ""+DEFAULT_TERMINATION_TIMEOUT);
		//workingCopy.setAttribute(JMX_PORT, DEFAULT_JMX_PORT);

		IVMInstall jre = JavaRuntime.getDefaultVMInstall();
		IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
		for (int i = 0; i < types.length; i++) {
			IVMInstallType type1 = types[i];
			IVMInstall[] jres = type1.getVMInstalls();
			for (int j = 0; j < jres.length; j++) {
				System.out.println(jres[j]);
			}
		}
/*		workingCopy.setAttribute(ATTR_VM_INSTALL_NAME, jre.getName());
		workingCopy.setAttribute(ATTR_VM_INSTALL_TYPE, jre.getVMInstallType().getId());
		workingCopy.setAttribute(ATTR_PROGRAM_ARGUMENTS, "start");

		File jdkHome = jre.getInstallLocation();
		IPath toolsPath = new Path(jdkHome.getAbsolutePath()).append("lib").append("tools.jar");
		IRuntimeClasspathEntry toolsEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(toolsPath);
		toolsEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
		IPath bootstrapPath = new Path("TOMCAT_HOME").append("bin").append("bootstrap.jar");
		IRuntimeClasspathEntry bootstrapEntry = JavaRuntime.newVariableRuntimeClasspathEntry(bootstrapPath);
		bootstrapEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
		IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
		IRuntimeClasspathEntry systemLibsEntry = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
				IRuntimeClasspathEntry.STANDARD_CLASSES);
		List classpath = new ArrayList();
		classpath.add(toolsEntry.getMemento());
		classpath.add(bootstrapEntry.getMemento());
		classpath.add(systemLibsEntry.getMemento());
		workingCopy.setAttribute(ATTR_CLASSPATH, classpath);
		workingCopy.setAttribute(ATTR_DEFAULT_CLASSPATH, false);
		workingCopy.setAttribute(ATTR_VM_ARGUMENTS, "-Djava.endorsed.dirs=\"..\\common\\endorsed\""
				+ "-Dcatalina.base=\"..\"" + "-Dcatalina.home=\"..\"" + "-Djava.io.tmpdir=\"..\\temp\"");
				*/

/*		File workingDir = JavaCore.getClasspathVariable("TOMCAT_HOME").append("bin").toFile();
		workingCopy.setAttribute(ATTR_WORKING_DIRECTORY, workingDir.getAbsolutePath());*/
		
		ILaunchConfiguration configuration = workingCopy.doSave();
		DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
	}
}
