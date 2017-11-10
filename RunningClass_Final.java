package runner;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
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

	public static final String DEFAULT_PROFILE = "";

	public static final String ENABLE_LIFE_CYCLE = "spring.boot.lifecycle.enable";
	public static final boolean DEFAULT_ENABLE_LIFE_CYCLE = true;

	public static final String HIDE_FROM_BOOT_DASH = "spring.boot.dash.hidden";
	public static final boolean DEFAULT_HIDE_FROM_BOOT_DASH = false;

	private static final String TERMINATION_TIMEOUT = "spring.boot.lifecycle.termination.timeout";
	public static final long DEFAULT_TERMINATION_TIMEOUT = 15000; // 15 seconds
	
	public void runProject() throws CoreException {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(ID_JAVA_APPLICATION);
		ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(type);

		ILaunchConfigurationType configType = manager.getLaunchConfigurationType("org.springframework.ide.eclipse.boot.launch");
		ILaunchConfigurationWorkingCopy workingCopy = configType.newInstance(null, manager.generateLaunchConfigurationName(
				"Sample-microservice-module-runconfig"));
		workingCopy.setAttribute(ATTR_PROJECT_NAME, "Sample-microservice-module");
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "com.lti.studio.StudioApplication");
		workingCopy.setAttribute(ENABLE_JMX, DEFAULT_ENABLE_JMX);
		workingCopy.setAttribute(ENABLE_LIVE_BEAN_SUPPORT, DEFAULT_ENABLE_LIVE_BEAN_SUPPORT);
		workingCopy.setAttribute(ENABLE_LIFE_CYCLE, DEFAULT_ENABLE_LIFE_CYCLE);
		workingCopy.setAttribute(TERMINATION_TIMEOUT, ""+DEFAULT_TERMINATION_TIMEOUT);
		
		ILaunchConfiguration configuration = workingCopy.doSave();
		DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
	}
}
