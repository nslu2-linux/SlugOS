LICENSE = GPL
SECTION = "console/utils"
PR = "r1"

DESCRIPTION = "strace is a system call tracing tool."

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.bz2 \
	   file://arm-syscallent.patch;patch=1"

inherit autotools

export INCLUDES = "-I. -I./linux"
