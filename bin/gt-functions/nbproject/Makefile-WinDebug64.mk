#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=MinGW64-Windows
CND_DLIB_EXT=dll
CND_CONF=WinDebug64
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/basic_functions.o \
	${OBJECTDIR}/string_functions.o


# C Compiler Flags
CFLAGS=-m64

# CC Compiler Flags
CCFLAGS=-m64
CXXFLAGS=-m64

# Fortran Compiler Flags
FFLAGS=-m64

# Assembler Flags
ASFLAGS=--64

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ../lib/win64/gt-functions.${CND_DLIB_EXT}

../lib/win64/gt-functions.${CND_DLIB_EXT}: ${OBJECTFILES}
	${MKDIR} -p ../lib/win64
	${LINK.cc} -o ../lib/win64/gt-functions.${CND_DLIB_EXT} ${OBJECTFILES} ${LDLIBSOPTIONS} -shared

${OBJECTDIR}/basic_functions.o: basic_functions.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/include/darwin -I/C/Program\ Files/Java/jdk1.8.0_20/include -I/C/Program\ Files/Java/jdk1.8.0_20/include/win32  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/basic_functions.o basic_functions.cpp

${OBJECTDIR}/string_functions.o: string_functions.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home/include/darwin -I/C/Program\ Files/Java/jdk1.8.0_20/include -I/C/Program\ Files/Java/jdk1.8.0_20/include/win32  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/string_functions.o string_functions.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} ../lib/win64/gt-functions.${CND_DLIB_EXT}

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
