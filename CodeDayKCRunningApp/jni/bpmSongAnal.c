#include <jni.h>
#include <string.h>
#include <android/log.h>

#define DEBUG_TAG "NDK_MainActivity"

jstring Java_com_example_codedaykcrunningapp_MainActivity_helloLog(JNIEnv * env, jobject this, jstring logThis)
{
    jboolean isCopy;
    const char * szLogThis = (*env)->GetStringUTFChars(env, logThis, &isCopy);


    (*env)->ReleaseStringUTFChars(env, logThis, szLogThis);

    return logThis;
}
