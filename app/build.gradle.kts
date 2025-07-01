@file:Suppress("UnstableApiUsage")

import com.itsaky.androidide.plugins.AndroidIDEAssetsPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties
plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("androidx.navigation.safeargs.kotlin")
}

apply {
  plugin(AndroidIDEAssetsPlugin::class.java)
}

android {
  namespace = BuildConfig.packageName

  defaultConfig {
    applicationId = BuildConfig.packageName
    vectorDrawables.useSupportLibrary = true
  }

  androidResources {
    generateLocaleConfig = true
  }


    signingConfigs {
        create("release") {
            val localProperties = Properties()
            val localPropertiesFile = rootProject.file("signing.properties")
            enableV1Signing = true // 启用 V1 签名
            enableV2Signing = true // 启用 V2 签名 (推荐，Android 7.0+)
            enableV3Signing = true // 启用 V3 签名 (推荐，Android 9+)
            if (localPropertiesFile.exists()) {
                localProperties.load(FileInputStream(localPropertiesFile))
                
                val storeFilePath = localProperties.getProperty("storeFile")
                val storePasswordValue = localProperties.getProperty("storePassword")
                val keyAliasValue = localProperties.getProperty("keyAlias")
                val keyPasswordValue = localProperties.getProperty("keyPassword")
                
                if (storeFilePath != null && storePasswordValue != null && 
                    keyAliasValue != null && keyPasswordValue != null) {
                    storeFile = file(storeFilePath)
                    storePassword = storePasswordValue
                    keyAlias = keyAliasValue
                    keyPassword = keyPasswordValue
                }
            }
        }
    }
    

  buildTypes {
      all{
    signingConfig = signingConfigs.getByName("release")
    }
    release {
      isShrinkResources = true
    }
  }

  lint {
    abortOnError = false
    disable.addAll(arrayOf("VectorPath", "NestedWeights", "ContentDescription", "SmallSp"))
  }
}

kapt { arguments { arg("eventBusIndex", "${BuildConfig.packageName}.events.AppEventsIndex") } }

dependencies {
  debugImplementation(libs.common.leakcanary)

  // Annotation processors
  kapt(libs.common.glide.ap)
  kapt(libs.google.auto.service)
  kapt(projects.annotationProcessors)

  implementation(libs.common.editor)
  implementation(libs.common.utilcode)
  implementation(libs.common.glide)
  implementation(libs.common.jsoup)
  implementation(libs.common.kotlin.coroutines.android)
  implementation(libs.common.retrofit)
  implementation(libs.common.retrofit.gson)

  implementation(libs.google.auto.service.annotations)
  implementation(libs.google.gson)
  implementation(libs.google.guava)

  // Git
  implementation(libs.git.jgit)

  // AndroidX
  implementation(libs.androidx.splashscreen)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.coordinatorlayout)
  implementation(libs.androidx.drawer)
  implementation(libs.androidx.grid)
  implementation(libs.androidx.nav.fragment)
  implementation(libs.androidx.nav.ui)
  implementation(libs.androidx.preference)
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.transition)
  implementation(libs.androidx.vectors)
  implementation(libs.androidx.animated.vectors)
  implementation(libs.androidx.work)
  implementation(libs.androidx.work.ktx)
  implementation(libs.google.material)
  implementation(libs.google.flexbox)

  // Kotlin
  implementation(libs.androidx.core.ktx)
  implementation(libs.common.kotlin)

  // Local projects here
  implementation(projects.actions)
  implementation(projects.buildInfo)
  implementation(projects.common)
  implementation(projects.editor)
  implementation(projects.termux.termuxApp)
  implementation(projects.termux.termuxView)
  implementation(projects.termux.termuxEmulator)
  implementation(projects.termux.termuxShared)
  implementation(projects.eventbus)
  implementation(projects.eventbusAndroid)
  implementation(projects.eventbusEvents)
  implementation(projects.gradlePluginConfig)
  implementation(projects.idestats)
  implementation(projects.subprojects.aaptcompiler)
  implementation(projects.subprojects.appintro)
  implementation(projects.subprojects.javacServices)
  implementation(projects.subprojects.javapoet)
  implementation(projects.subprojects.xmlUtils)
  implementation(projects.subprojects.projects)
  implementation(projects.subprojects.toolingApi)
  implementation(projects.logsender)
  implementation(projects.lsp.api)
  implementation(projects.lsp.java)
  implementation(projects.lsp.xml)
  implementation(projects.lexers)
  implementation(projects.lookup)
  implementation(projects.preferences)
  implementation(projects.resources)
  implementation(projects.treeview)
  implementation(projects.templatesApi)
  implementation(projects.templatesImpl)
  implementation(projects.uidesigner)
  implementation(projects.xmlInflater)
  implementation(project(":MTDataFilesProvider"))

  // This is to build the tooling-api-impl project before the app is built
  // So we always copy the latest JAR file to assets
  compileOnly(projects.subprojects.toolingApiImpl)

  testImplementation(projects.testing.unit)
  androidTestImplementation(projects.testing.android)
}