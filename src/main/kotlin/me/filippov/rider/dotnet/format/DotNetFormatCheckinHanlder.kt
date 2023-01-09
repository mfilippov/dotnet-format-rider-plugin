package me.filippov.rider.dotnet.format

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.ui.BooleanCommitOption
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.ui.RefreshableOnComponent
import com.jetbrains.rider.projectView.solutionFile
import com.jetbrains.rider.runtime.RiderDotNetActiveRuntimeHost
import kotlin.io.path.Path
import kotlin.io.path.relativeTo

class DotNetFormatCheckinHanlder(private val panel: CheckinProjectPanel) : CheckinHandler() {
    override fun getBeforeCheckinConfigurationPanel(): RefreshableOnComponent {
        val configuration = panel.project.service<DotNetFormatPluginConfiguration>()
        return BooleanCommitOption(
                panel,
                "Execute DotNet Format",
                false,
                configuration::DOTNET_FORMAT_BEFORE_PROJECT_COMMIT
        )
    }

    override fun beforeCheckin(): ReturnResult {
        if (!enabled(panel)) {
            return ReturnResult.COMMIT
        }
        val dotNetActiveRuntimeHost = RiderDotNetActiveRuntimeHost.getInstance(panel.project)
        val dotNetRuntime = dotNetActiveRuntimeHost.dotNetCoreRuntime.value ?: return ReturnResult.CANCEL
        FileDocumentManager.getInstance().saveAllDocuments()
        val pb = ProcessBuilder()
        pb.command(
                dotNetRuntime.cliExePath,
                "format",
                panel.project.solutionFile.absolutePath,
                "--include",
                *panel.virtualFiles
                        .map { Path(it.path).relativeTo(Path(panel.project.solutionFile.parent)).toString() }
                        .toTypedArray())
        val process = pb.start()
        process.waitFor()
        if (process.exitValue() != 0) {
            return ReturnResult.CANCEL
        }
        panel.virtualFiles.forEach {  it.refresh(true, false) }
        return ReturnResult.COMMIT
    }

    private fun enabled(panel: CheckinProjectPanel): Boolean {
        return panel.project.service<DotNetFormatPluginConfiguration>().DOTNET_FORMAT_BEFORE_PROJECT_COMMIT
    }
}
