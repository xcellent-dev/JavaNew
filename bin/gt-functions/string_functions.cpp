/*
 */

/* 
 * File:   string_functions.cpp
 * Author: nkabiliravi
 * 
 * Created on April 9, 2016, 1:50 PM
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include "string_functions.h"

JNIEXPORT jint JNICALL Java_com_gtanalysis_gtexcel_jni_StringFunctions_arrlength
(JNIEnv *env, jobject obj, jobjectArray arr) {

    int size = env->GetArrayLength(arr);
    
    return size;
}
