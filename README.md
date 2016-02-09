# violation-eclipse-plugin
Prerequisites 
1.       Download and install maven 3.0.X from https://maven.apache.org/download.cgi
2.       Download a java jdk 6 or later
3.       Set JAVA_HOME env variable and add to windows PATH maven binary folder
4.       Download latest Apache ant binaries and add to windows PATH ant binary folder

Instructions to build plugin

1.       Unzip adg-plugin-sources under a specific folder
2.       To compile sources
	a.       Launch from root folder of sources : mvn.bat clean package -Dtycho.targetPlatform=c:/eclipse3.6.1
3.       To compile sources and create update_site_archive.zip  which is needed by Eclipse IDE
	a.     Launch ant -f build_adg_plugin.xml genPluginArchive
		i.      update_site_archive.zip is created under .\com.castsoftware.devplugin.updatesite\target folder
