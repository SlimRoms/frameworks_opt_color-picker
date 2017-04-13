LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_USE_AAPT2 := true
LOCAL_MODULE := color-picker-res
LOCAL_MANIFEST_FILE := cpl/src/main/AndroidManifest.xml
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/cpl/src/main/res
LOCAL_JAVA_LANGUAGE_VERSION := 1.7
include $(BUILD_STATIC_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_USE_AAPT2 := true
LOCAL_MODULE := color-picker
LOCAL_SRC_FILES := $(call all-java-files-under,cpl/src/main/java)
LOCAL_MANIFEST_FILE := cpl/src/main/AndroidManifest.xml
LOCAL_STATIC_ANDROID_LIBRARIES := \
    color-picker-res
LOCAL_SHARED_ANDROID_LIBRARIES := \
    android-support-v7-appcompat \
    android-support-v7-preference \
    android-support-v7-recyclerview \
    android-support-v4 \
    android-support-annotations
LOCAL_JAR_EXCLUDE_FILES := none
LOCAL_JAVA_LANGUAGE_VERSION := 1.7
include $(BUILD_STATIC_JAVA_LIBRARY)
