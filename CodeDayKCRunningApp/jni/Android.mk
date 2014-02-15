LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS := -llog

LOCAL_MODULE    := ndk
LOCAL_SRC_FILES := bpmSongAnal.c

include $(BUILD_SHARED_LIBRARY)