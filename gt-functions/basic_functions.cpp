/* 
 * File:   basic_functions.c
 * Author: nkabiliravi
 *
 * Created on April 8, 2016, 9:42 PM
 */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include "basic_functions.h"

JNIEXPORT jdouble JNICALL Java_com_gtanalysis_gtexcel_jni_BasicFunctions_sigma
(JNIEnv *env, jobject obj, jdoubleArray arr) {
    //First get a pointer to the elements within the jdoubleArray
    jsize len = env->GetArrayLength(arr);
    jboolean isCopy;
    jdouble* srcArrayElems = env->GetDoubleArrayElements(arr, &isCopy);
    
    jdouble sum = 0.0;

    for (int i = 0; i < len; i++) {
        sum += srcArrayElems[i];
    }

    //Release the pointer to the jdoubleArray
    if (isCopy == JNI_TRUE) {
        env->ReleaseDoubleArrayElements(arr, srcArrayElems, JNI_ABORT);
    }

    return sum;
}

JNIEXPORT jdouble JNICALL Java_com_gtanalysis_gtexcel_jni_BasicFunctions_variance
(JNIEnv *env, jobject obj, jdoubleArray arr) {
    //First get a pointer to the elements within the jdoubleArray
    jsize len = env->GetArrayLength(arr);
    jboolean isCopy;
    jdouble* srcArrayElems = env->GetDoubleArrayElements(arr, &isCopy);
    
    jdouble sum = 0.0;
    jdouble avg = 0.0;

    for (int i = 0; i < len; i++) {
        sum += srcArrayElems[i];
    }
    
    if(len > 0) {
        avg = sum / len;
    }

    //Release the pointer to the jdoubleArray
    if (isCopy == JNI_TRUE) {
        env->ReleaseDoubleArrayElements(arr, srcArrayElems, JNI_ABORT);
    }

    return sum;
}

JNIEXPORT jdouble JNICALL Java_com_gtanalysis_gtexcel_jni_BasicFunctions_average
(JNIEnv *env, jobject obj, jdoubleArray arr) {
    //First get a pointer to the elements within the jdoubleArray
    jsize len = env->GetArrayLength(arr);
    jboolean isCopy;
    jdouble* srcArrayElems = env->GetDoubleArrayElements(arr, &isCopy);
    
    jdouble sum = 0.0;
    jdouble avg = 0.0;

    for (int i = 0; i < len; i++) {
        sum += srcArrayElems[i];
    }
    
    if(len > 0) {
        avg = sum / len;
    }

    jdouble variance = 0.0;
    for (int i = 0; i < len; i++) {
        jdouble item = srcArrayElems[i] - avg;
        variance += item * item;
    }
    
    if(len > 0) {
        variance = variance / len;
    }
    
    //Release the pointer to the jdoubleArray
    if (isCopy == JNI_TRUE) {
        env->ReleaseDoubleArrayElements(arr, srcArrayElems, JNI_ABORT);
    }

    return variance;
}