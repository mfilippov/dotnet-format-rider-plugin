package me.filippov.rider.dotnet.format

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "DotNetFormatPluginConfiguration", storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
class DotNetFormatPluginConfiguration: PersistentStateComponent<DotNetFormatPluginConfiguration> {
    @Volatile
    var DOTNET_FORMAT_BEFORE_PROJECT_COMMIT = false

    override fun getState(): DotNetFormatPluginConfiguration {
        return this
    }

    override fun loadState(state: DotNetFormatPluginConfiguration) {
        XmlSerializerUtil.copyBean(state, this)
    }

}